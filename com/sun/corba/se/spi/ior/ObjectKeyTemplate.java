package com.sun.corba.se.spi.ior;

import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import org.omg.CORBA_2_3.portable.OutputStream;

public abstract interface ObjectKeyTemplate extends Writeable
{
  public abstract ORBVersion getORBVersion();

  public abstract int getSubcontractId();

  public abstract int getServerId();

  public abstract String getORBId();

  public abstract ObjectAdapterId getObjectAdapterId();

  public abstract byte[] getAdapterId();

  public abstract void write(ObjectId paramObjectId, OutputStream paramOutputStream);

  public abstract CorbaServerRequestDispatcher getServerRequestDispatcher(ORB paramORB, ObjectId paramObjectId);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.ObjectKeyTemplate
 * JD-Core Version:    0.6.2
 */