import static propel.core.functional.predicates.Strings.println;
import static propel.core.functional.predicates.Strings.startsWith;
import static propel.core.functional.projections.Projections.toUpperCase;
import java.util.concurrent.ConcurrentHashMap;
import lombok.ExtensionMethod;
import lombok.val;
import propel.core.collections.maps.primitive.LongHashMap;
import propel.core.tracing.Stopwatch;
import propel.core.utils.Linq;

@ExtensionMethod({Linq.class})
public class Main
{

  public static void main(String[] args)
  {
    // LINQ, extension methods, functions
    val names = new String[] {"john", "james", "john", "eddie"}.where(startsWith("j")).distinct().select(toUpperCase());
    names.all(println());

    // Note: if you get compilation errors, make sure you've installed "lombok-pg"
    // Simply copy the lombok-pg-10.8-SNAPSHOT.jar (from lib) to your $ECLIPSE_HOME and edit eclipse.ini, add the following at the end of the file:
    //
    // -javaagent:lombok-pg-10.8-SNAPSHOT.jar
    // -Xbootclasspath/a:lombok-pg-10.8-SNAPSHOT.jar
    //
    // Note: If you already have a -Xbootclasspath configured in your eclipse.ini, append lombok-pg to it, rather than adding another entry
    // If you don't use Eclipse, look for the "lombok project" installation instructions online.

  }
}
