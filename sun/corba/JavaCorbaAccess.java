package sun.corba;

import com.sun.corba.se.impl.io.ValueHandlerImpl;

public abstract interface JavaCorbaAccess
{
  public abstract ValueHandlerImpl newValueHandlerImpl();

  public abstract Class<?> loadClass(String paramString)
    throws ClassNotFoundException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.corba.JavaCorbaAccess
 * JD-Core Version:    0.6.2
 */