package sun.nio.fs;

import java.nio.file.attribute.BasicFileAttributes;

public abstract interface BasicFileAttributesHolder
{
  public abstract BasicFileAttributes get();

  public abstract void invalidate();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.BasicFileAttributesHolder
 * JD-Core Version:    0.6.2
 */