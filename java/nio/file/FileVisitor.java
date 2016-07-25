package java.nio.file;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;

public abstract interface FileVisitor<T>
{
  public abstract FileVisitResult preVisitDirectory(T paramT, BasicFileAttributes paramBasicFileAttributes)
    throws IOException;

  public abstract FileVisitResult visitFile(T paramT, BasicFileAttributes paramBasicFileAttributes)
    throws IOException;

  public abstract FileVisitResult visitFileFailed(T paramT, IOException paramIOException)
    throws IOException;

  public abstract FileVisitResult postVisitDirectory(T paramT, IOException paramIOException)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.FileVisitor
 * JD-Core Version:    0.6.2
 */