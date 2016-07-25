package javax.tools;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public abstract interface JavaFileManager extends Closeable, Flushable, OptionChecker
{
  public abstract ClassLoader getClassLoader(Location paramLocation);

  public abstract Iterable<JavaFileObject> list(Location paramLocation, String paramString, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean)
    throws IOException;

  public abstract String inferBinaryName(Location paramLocation, JavaFileObject paramJavaFileObject);

  public abstract boolean isSameFile(FileObject paramFileObject1, FileObject paramFileObject2);

  public abstract boolean handleOption(String paramString, Iterator<String> paramIterator);

  public abstract boolean hasLocation(Location paramLocation);

  public abstract JavaFileObject getJavaFileForInput(Location paramLocation, String paramString, JavaFileObject.Kind paramKind)
    throws IOException;

  public abstract JavaFileObject getJavaFileForOutput(Location paramLocation, String paramString, JavaFileObject.Kind paramKind, FileObject paramFileObject)
    throws IOException;

  public abstract FileObject getFileForInput(Location paramLocation, String paramString1, String paramString2)
    throws IOException;

  public abstract FileObject getFileForOutput(Location paramLocation, String paramString1, String paramString2, FileObject paramFileObject)
    throws IOException;

  public abstract void flush()
    throws IOException;

  public abstract void close()
    throws IOException;

  public static abstract interface Location
  {
    public abstract String getName();

    public abstract boolean isOutputLocation();
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.JavaFileManager
 * JD-Core Version:    0.6.2
 */