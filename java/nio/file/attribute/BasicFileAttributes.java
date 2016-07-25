package java.nio.file.attribute;

public abstract interface BasicFileAttributes
{
  public abstract FileTime lastModifiedTime();

  public abstract FileTime lastAccessTime();

  public abstract FileTime creationTime();

  public abstract boolean isRegularFile();

  public abstract boolean isDirectory();

  public abstract boolean isSymbolicLink();

  public abstract boolean isOther();

  public abstract long size();

  public abstract Object fileKey();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.BasicFileAttributes
 * JD-Core Version:    0.6.2
 */