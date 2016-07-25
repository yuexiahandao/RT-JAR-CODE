package org.omg.CORBA;

public abstract interface CustomMarshal
{
  public abstract void marshal(DataOutputStream paramDataOutputStream);

  public abstract void unmarshal(DataInputStream paramDataInputStream);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.CustomMarshal
 * JD-Core Version:    0.6.2
 */