package java.security;

import java.io.Serializable;

public abstract interface Key extends Serializable
{
  public static final long serialVersionUID = 6603384152749567654L;

  public abstract String getAlgorithm();

  public abstract String getFormat();

  public abstract byte[] getEncoded();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Key
 * JD-Core Version:    0.6.2
 */