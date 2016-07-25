package java.io;

public abstract interface Externalizable extends Serializable
{
  public abstract void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException;

  public abstract void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.Externalizable
 * JD-Core Version:    0.6.2
 */