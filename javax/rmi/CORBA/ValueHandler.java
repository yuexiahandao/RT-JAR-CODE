package javax.rmi.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.SendingContext.RunTime;

public abstract interface ValueHandler
{
  public abstract void writeValue(OutputStream paramOutputStream, Serializable paramSerializable);

  public abstract Serializable readValue(InputStream paramInputStream, int paramInt, Class paramClass, String paramString, RunTime paramRunTime);

  public abstract String getRMIRepositoryID(Class paramClass);

  public abstract boolean isCustomMarshaled(Class paramClass);

  public abstract RunTime getRunTimeCodeBase();

  public abstract Serializable writeReplace(Serializable paramSerializable);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.CORBA.ValueHandler
 * JD-Core Version:    0.6.2
 */