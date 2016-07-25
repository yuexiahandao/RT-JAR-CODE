package java.nio.file.attribute;

import java.io.IOException;

public abstract interface BasicFileAttributeView extends FileAttributeView
{
  public abstract String name();

  public abstract BasicFileAttributes readAttributes()
    throws IOException;

  public abstract void setTimes(FileTime paramFileTime1, FileTime paramFileTime2, FileTime paramFileTime3)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.BasicFileAttributeView
 * JD-Core Version:    0.6.2
 */