# Hi there!

jpropel-light is a Java library which can seriously hinder your salary if you are paid by the number of lines of code you create!

    // create alphabet char[]
    char[] alphabet = new Character('A').to(new Character('Z')).unbox();
 
    // join two arrays and put in list 
    List<Character> allowed = alphabet.join(numbers).toList(); 

    // select names starting with j, using LINQ-style statements
    new String[] { "james", "john", "john", "eddie" }.where(startsWith("j")).distinct().all(println());
    

It is a free and open-source Java library aiming to cut down code bloat, boilerplate and generally the number of lines of code Java developers have to write in order to complete a task.

It comes with full LINQ support, reified generic collections and concise one-liners for performing common tasks such as reading an entire text file into memory, array manipulations, XML processing/querying, etc. 
Let's examine some of them.



## LINQ

A very popular language feature of C#, Language INtegrated Query adds native data querying capabilities. These can, for example, be used to project and filter data in arrays and enumerable classes (source: [LINQ](http://en.wikipedia.org/wiki/LINQ))

    var results = someCollection.where(c => c.SomeProperty < someValue * 2);

Here is an equivalent Java snippet:

    List<SomeElement> results = new ArrayList<SomeElement>();
    for(SomeElement c : SomeCollection)
      if(c.SomeProperty < someValue * 2)
        results.add(c);

This is a trivial example, but it becomes obvious that for more complex queries, such as group by, select many, etc. the code becomes much more verbose. Even the low level of verbosity shown above hides the real purpose of the snippet, so one has to read it multiple lines of code in their entirety to understand it. This gets in the way of the developer and is not good for productivity. 

So is the following *valid* Java code perhaps better?

    List results = someCollection.where(propertyLessThan(someValue*2));

How is this possible? Well, firstly we have to import the right class, which would be the Linq class of the framework. It has implementations for all methods found in the .NET implementation such as: [all, any, select, where, distinct, first, last, zip, etc.](https://github.com/nicholas22/jpropel-light/blob/master/src/propel/core/utils/Linq.java)

The second step is to annotate the class that contains your code with the @ExtensionMethod annotation, imported from a library called [lombok-pg](https://github.com/peichhorn/lombok-pg). This library makes a lot of the shown syntactic sugar possible by instructing your compiler to pre-process the class code before compiling. This is done transparently to the developer. As far as bytecode is concerned, the code is converted to standard Java calls. So for the snippet above, the code effectively becomes:

    List results = Linq.where(someCollection, propertyLessThan(someValue*2));

This you will recognise is standard Java static method call. This is how [extension methods](http://en.wikipedia.org/wiki/Extension_method) work under-the-hood in other languages too. 
But crucially, you as a developer *do not* have to work as such and can be much more expressive, using a fluent API: 

    new String[] {"james","john","john","eddie"}.where(startsWith("j").select(toUppercase()).distinct().toList();

Isn't that nice? :)

The above statement returns ["JAMES", "JOHN"] and is more readable and concise than the following equivalent (but horrible) statement:

    Linq.toList(Linq.distinct(Linq.select(Linq.where(new String[] { "james","john","john","eddie"}, startsWith("j")), toUppercase())));

It's verbose and bug prone because it's hard to understand. Let's get back on the prettier version:

    new String[] {"james","john","john","eddie"}.where(startsWith("j").select(toUppercase()).distinct().toList();


The other interesting with the above statement is how can we seemingly pass functions as arguments (e.g. see startsWith, toUppercase). Everyone knows that Java does not have first class functions, in other words, cannot pass methods/functions around as objects. This is another area where lombok-pg helps us, allowing for the annotation of methods/functions with @Function, enabling easier functional programming in Java. 

    @Function 
    private static String toUppercase(String element) { 
       return element.toUpperCase(); 
    }

The above annotation will pre-process the code by wrapping the annotated function in an anonymous class, allowing us to pass it around as an object. This is what the resulting code would look like if you decompiled the class:

    private static Function1<String, String> toUppercase(String element) {
      return new Function1<String, String>() {
        public String apply(String arg) { 
          return element.toUpperCase(); 
        } 
      } 
    }

This is very similar to how Scala functions are implemented. You can then pass these 'functions' (which are really just Java objects / anonymous classes) around, just like you would pass objects around in other languages. And these are the types of objects that the Linq class accepts as selectors, predicates, filters, etc. Here is for example the source code of the select() method:

    public static TResult[] select(final TSource[] values, final Function1<TSource, TResult> selector) 
    { 
      List result = new ArrayList(values.length); 
    
      for(TSource item : values) 
        result.add(selector.apply(item)); 
    
      return toArray(result, selector.getReturnType()); 
    }

As you can see, all the above method does is call the apply() method of the function to extract the required data, oblivious to the actual implementation of your selector. The JPropel library comes with a number of predicates and projections built-in, such as the ones shown above e.g. startsWith, endsWith, contains, equal, etc. The Linq class static methods accept these as well as custom (written by you!) functions to perform select, where and other such operations, in a way that is suitable to your program.

The JVM deals with anonymous classes very frequently when you code against it using languages such as Scala. A benchmark between [C++, Go, Java and Scala](http://www.readwriteweb.com/hack/2011/06/cpp-go-java-scala-performance-benchmark.php) which made the news recently, showed that Scala's performance is very much on par with Java's. So clearly the JVM can cope with wrapping methods into anonymous classes pretty well. Therefore there is a minimal performance consideration when using such code, but you can also cache such 'functions' around when you know you will use them very frequently.

Most Linq methods come in two flavors. One accepts generic arrays and one accepts generic Iterables. There are two reasons for this. Firstly, arrays and Iterables do not share a common super class which would allow for traversal of items. Secondly, arrays and Iterables are handled completely differently from the JPropel library perspective. 

So when you pass an array to be processed, the resulting array is allocated and all results are inserted before it is returned. This is not the case with iterables. Iterables are created using a block iterator, similarly to how yielding works in C# and is down to lombok's [yield construct](http://peichhorn.github.com/lombok-pg/Yield.html). This means that if you do not iterate over the entire collection, then only some of the elements are processed, which is more efficient, because it allows you, for instance, to break the iteration process without consuming as much memory and processing resources as the array would.



## Reified generic collections

Generics in Java are implemented using [type erasure](http://en.wikipedia.org/wiki/Type_erasure). This means that generic type information is removed at run-time. A list of integers and a list of strings have the same class type in Java, which creates an interesting set of problems in areas such as overloading, instantiation and introspection. Type erasure is considered by many an inferior way of implementing generics. The subject of [erasure vs reification](http://beust.com/weblog/2011/07/29/erasure-vs-reification/) has been covered neatly already so let's focus on what's important here. Since the JVM erases the run-time type of generics, we have to live with this. Or do we?

Neil Gafter came up with a workaround to this problem, called [super-type token](http://gafter.blogspot.com/2006/11/reified-generics-for-java.html). The whole idea is based on the fact that the JVM does not erase type information of anonymous classes. Therefore if you instantiate a class using anonymous class semantics, you can query its run-time type parameter(s): 

    List<String> list = new ArrayList<String>() {}; // note: braces after parentheses

The JPropel library embraces this pattern and provides a wide array of collections that follow it, such as Lists, Maps, Hashtables and others.  This allows you to do common sense things such as:

    LinkedList<String> list = new LinkedList<String>(){};
    String[] items = list.toArray();

With type-erased collections you would have to do nonsensical things such as: 

    Object[] items = list.toArray();

or

    String[] items = list.toArray(new String[list.size()]);

Being able to query the run-time type parameter can bring several benefits, especially if you are creating a generic container, e.g. your own collection classes. But in such scenarios, if you have multiple layers of generic containers, you may not be able to reliably obtain the run-time type information. One example:

    public class MyClass<T> {
      public MyClass() {
        List<T> list = new ReifiedArrayList<T>() {}; // no can do
      }
    }

The reason this is not possible, is because the type T of MyClass is not known. In such scenarios you have to pass the type explicitly, as such:

    public class MyClass<T> {
      public MyClass() {
        Class<?> clazz = SuperTypeToken.getClazz(this);
        List<T> list = new ReifiedArrayList<T>(clazz) {}; // OK
      }
    }

You then go ahead and instantiate MyClass using anonymous class semantics, as shown above (or pass the type explicitly if you wish). Although this is not ideal, at least it is possible to accommodate for multi-level generic containers.

Finally, it is also possible to instantiate collections with multiple type parameters:

    AvlHashtable<UUID, String> lookup = AvlHashtable<UUID, String>() {};
    Iterable<UUID> keys = lookup.getKeys();
    Iterable<String> values = lookup.getValues();

This is possible because the SuperTypeToken class has an overloaded method that allows you to obtain the generic run-time parameter type based on its zero-based index.



## Fun with utilities

There are a lot of very concise one-liners supported by the library which allow you to focus on the task at hand instead of writing boilerplate. Here are some examples:

    // create alphabet
    char[] alphabet = new Character('A').to(new Character('Z'));
    char[] numbers = new Charracter('1').to(new Character('9'));
    
    // join two arrays and put in a list
    List<Character> allowed = alphabet.join(numbers).toList();
    
    // load entire text file in memory
    String data = file.readFileToEnd();
    
    // append to file
    file.appendText("Some more text");
    
    // XML compacting
    String compacted = someXml.compact();
    
    // copy stream over
    inputStream.copy(outputStream);
    
    // Culture-aware case-insensitive string comparison
    boolean trueInGermany = StringUtils.equal("straße", "STRASSE", StringComparison.CurrentCultureIgnoreCase);
    
    // check if all elements are present
    boolean itsTrue = "abcdef".containsAll(new String[] {"bc", "cd"}, StringComparison.Ordinal);`

    // benchmark
    Stopwatch sw = Stopwatch.startNew();
    long ns = sw.getElapsedNanos();

There are too many utilities to cover here, around the areas of reflection, XML, conversions (e.g. binary, octal, hex, decimal, base64, etc.), character escaping, hashing, strings, etc. Have a look at the [propel.core.utils.*](https://github.com/nicholas22/jpropel-light/tree/master/src/propel/core/utils) package for more. For more examples also see [the JPropel changelog] (https://github.com/nicholas22/jpropel) 


## JPropel

JPropel-light (this project!) is a lightweight version of the [JPropel](https://github.com/nicholas22/jpropel) library. If you do not mind a couple of extra dependencies (BouncyCastle and SLF4j), have a look at it as it contains some extra utility functionality.

For instance, the CryptographicString class allows you to store a secret in memory in encrypted form:

    CryptographicString cs = new CryptographicString("super secret"); 
    char[] unencrypted1 = cs.asCharArray();
    byte[] unencrypted2 = cs.asByteArray();
    String unencrypted3 = cs.asString(); // beware of String interning

Another construct you may find interesting is the ability to trace method calls, logging all input and output data (or exceptions thrown) in a highly configurable way. This is done without any AOP library dependencies or custom AOP language.

    @Trace(level=LogLevel.INFO)
    public int add(int a, int b) { 
       return a+b;
    }

This allows you to focus on the task at hand without worrying if you have enough logging within a method. To enable the tracing you have to instantiate your class (perhaps via a factory newInstance() method) in a way that allows the tracing to take place:

    MyInterface myClass = new Tracer<MyInterface>(new MyClass());

All calls to methods of the myClass instance that have been annotated with @Trace will emit logging statements of all method arguments, results (if not void) and exceptions (if thrown). The logger uses  SLF4j so will plug into any popular logging library you are using. The format of messages is highly configurable as well.

Finally, another nice feature is a validation framework, where you define your POJOs' metadata and can validate in a single line of code, removing the need for convoluted if-then-else statements sprinkled around many places in your code:

    boolean notNull = true;
    boolean notEmpty = true;
    boolean notNullChars = true;
    StringPropertyMetadata name = new StringPropertyMetadata("Name", 3, 20, notNull, notEmpty, notNullChars);

    // perform validation
    name.validate(null); // "Name cannot be null"
    name.validate(""); // "Name cannot be empty"
    name.validate("John"); // OK
    name.validate("Jo"); // "Name cannot be 2 or fewer characters in length"
    name.validate("123456789012345678901"); // "Name cannot be 21 or more characters in length"



##Lombok-pg

JPropel and JPropel-light depend on the [lombok-pg](https://github.com/peichhorn/lombok-pg) library. 

If you are using an IDE such as Eclipse or Netbeans, you should patch it so that it uses lombok as a Java agent. The process is very simple: just double-click on the lombok.jar found in the "lib" folder. This will enable much of the syntactic sugar shown above.

Javac and Ant do not require any patching as such.

You may visit the [lombok](http://projectlombok.org/slideshow.html) project website for more info, or ask [Philipp](https://github.com/peichhorn) more about it.


##Changelog

####1.0.7: InvokeOneArg, InvokeNoArg
Functional invocation is now cleaner, by using static imports on methods of these two classes.



####1.0.6: SharedMapMultimap 
Thread-safe version of MapMultimap (SharedMapMultimap).
Linq.single() ensuring a single element exist in an iterable/array.
StringUtils.crop() is a trim-like function where you specify which characters to leave in, rather than trim.



####1.0.5: Predicates split up Objects, Strings, Arrays and Iterables classes
A breaking change for the modularization of predicates



####1.0.4: Function1<?, Boolean> became Predicate1<?>
A breaking change that uses the @Predicate annotation instead of @Function. Annotated methods return Predicate1<?> instead of Function1<?, Boolean>



####1.0.3: Minor improvements

####MapMultimap data structure
Another type-aware collection, this is a "map of maps".

     val map = new MapMultimap<String, String, Integer>() {};
     map.put("Male", "Nick", 18);
     map.put("Male", "John", 25);
     map.put("Female", "Jo", 20);

     // get map of all males
     Map<String, Integer> males = map.get("Male");

     // check if anyone is 23 years old
     boolean notTrue = map.getValue(23);

####Improved extension method support for java.io.File
Most methods in FileUtils will now accept a File object as first argument, facilitating extension method use:

     // line-oriented file input
     String[] lines = new File("myFile.txt").readFileToEnd().split("\r\n");



####1.0.2: New features, upgrades, etc.
Lots of new statically importable predicates and projections:

    import static propel.core.functional.predicates.Predicates.*;
    import static propel.core.functional.projections.Projections.*;

examples: instanceOf(), appendToFile(), copyFile, moveFile(), etc.

####Matcher allowing for fewer if-then-else statements (beta!)

    val matcher = new Matcher<Object, Person>();

    // wire-up appropriate matching conditions to handlers
    matcher.addAction(instanceOf(SalesPerson.class), salesHandler);
    matcher.addAction(instanceOf(MarketingPerson.class), marketingHandler);
    matcher.addAction(instanceOf(AdminPerson.class), adminHandler);

    // this is the default action, will match if nothing else matches
    matcher.setDefaultAction(throwDetailed(new Exception("An unrecognised person type was given: ")));

####Functional transaction manager
A transaction manager which accepts functions as actions and rollback actions.

    // we will transactionally replace a file, i.e. revert changes if something fails
    ITransactionManager tm = new TransactionManager();

    String originalPath = "/source/file"
    String destinationPath = "/destination/file";
    String tempPath = destinationPath + CONSTANT.DOT + RandomUtils.getPseudoAlphanumericText(16);

    // action is to move the file to a temporary location, upon failure move it back
    if (new File(originalPath).exists())
      tm.add(moveFile(originalPath, tempPath), moveFile(tempPath, originalPath));

    // action is to append file contents, upon failure delete this new file
    tm.add(appendToFile(originalPath, xmlData), deleteFile(originalPath));

    // action is to delete temporary file, this is last action hence no need for recovery counter-action
    tm.add(deleteFile(tempPath));

    // perform operations, upon failure this will execute all rollback actions, before re-throwing
    tm.commitWithRollback();



####1.0.1: Added println() predicate

    new String[] { "hello", "world" }.all(println());



####1.0.0: Creation
Lightweight version forked from [JPropel](https://github.com/nicholas22/jpropel)
