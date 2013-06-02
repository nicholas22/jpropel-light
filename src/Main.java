import static propel.core.functional.predicates.Strings.startsWith;
import static propel.core.utils.Linq.where;
import static propel.core.utils.Linq.distinct;
import propel.core.utils.Linq;
import lombok.val;

public class Main
{

  public static void main(String[] args)
      throws Exception
  {
    val names = new String[] {"john", "james", "john", "eddie"};
    val jnames = where(names, startsWith("j"));
    val djnames = distinct(jnames);
    
    System.out.println(Linq.toString(djnames));
    
    // Note: if you get compilation errors, make sure you've patched your IDE with "lombok-pg"
    // Simply copy the lombok-pg-11.0.jar (from lib) to your $ECLIPSE_HOME and edit eclipse.ini, add the following at the end of the file:
    //
    // -javaagent:lombok-pg-11.0.jar
    // -Xbootclasspath/a:lombok-pg-11.0.jar
    //
    // Note: If you already have a -Xbootclasspath configured in your eclipse.ini, append lombok-pg to it, rather than adding another entry
    // If you don't use Eclipse, look for the "lombok project" installation instructions online.

    // Note: In short, to compile the library, you should have lombok-pg configured as described above.
    // But when merely consuming this library, you can opt for "lombok" (not lombok-pg branch), which is more well supported across IDEs.        
    // If there are a lot of people preferring to continue using lombok-pg, a new branch will be considered e.g. jpropel-lombok-pg. 
  }

}
