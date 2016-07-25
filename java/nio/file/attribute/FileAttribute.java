package java.nio.file.attribute;

public abstract interface FileAttribute<T>
{
  public abstract String name();

  public abstract T value();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.FileAttribute
 * JD-Core Version:    0.6.2
 */