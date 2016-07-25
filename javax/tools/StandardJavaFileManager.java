package javax.tools;

import java.io.File;
import java.io.IOException;

public abstract interface StandardJavaFileManager extends JavaFileManager
{
  public abstract boolean isSameFile(FileObject paramFileObject1, FileObject paramFileObject2);

  public abstract Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> paramIterable);

  public abstract Iterable<? extends JavaFileObject> getJavaFileObjects(File[] paramArrayOfFile);

  public abstract Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> paramIterable);

  public abstract Iterable<? extends JavaFileObject> getJavaFileObjects(String[] paramArrayOfString);

  public abstract void setLocation(JavaFileManager.Location paramLocation, Iterable<? extends File> paramIterable)
    throws IOException;

  public abstract Iterable<? extends File> getLocation(JavaFileManager.Location paramLocation);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.StandardJavaFileManager
 * JD-Core Version:    0.6.2
 */