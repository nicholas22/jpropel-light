import static propel.core.functional.predicates.Strings.*;
import static propel.core.functional.projections.Projections.*;
import lombok.ExtensionMethod;
import lombok.val;
import propel.core.utils.Linq;

@ExtensionMethod({Linq.class})
public class Main
{

  public static void main(String[] args)
  {
    // LINQ, extension methods, functions
    val names = new String[] { "john", "james", "john", "eddie" }.where(startsWith("j")).distinct().select(toUpperCase());  
    names.all(println());   
  }
}
