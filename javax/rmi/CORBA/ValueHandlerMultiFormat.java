package javax.rmi.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.OutputStream;

public abstract interface ValueHandlerMultiFormat extends ValueHandler
{
  public abstract byte getMaximumStreamFormatVersion();

  public abstract void writeValue(OutputStream paramOutputStream, Serializable paramSerializable, byte paramByte);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.CORBA.ValueHandlerMultiFormat
 * JD-Core Version:    0.6.2
 */