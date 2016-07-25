package javax.xml.bind;

import java.io.IOException;
import javax.xml.transform.Result;

public abstract class SchemaOutputResolver
{
  public abstract Result createOutput(String paramString1, String paramString2)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.SchemaOutputResolver
 * JD-Core Version:    0.6.2
 */