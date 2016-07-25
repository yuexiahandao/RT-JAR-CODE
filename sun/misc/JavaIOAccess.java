package sun.misc;

import java.io.Console;
import java.nio.charset.Charset;

public abstract interface JavaIOAccess
{
  public abstract Console console();

  public abstract Charset charset();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JavaIOAccess
 * JD-Core Version:    0.6.2
 */