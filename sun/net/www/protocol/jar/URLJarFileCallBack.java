package sun.net.www.protocol.jar;

import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

public abstract interface URLJarFileCallBack
{
  public abstract JarFile retrieve(URL paramURL)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.jar.URLJarFileCallBack
 * JD-Core Version:    0.6.2
 */