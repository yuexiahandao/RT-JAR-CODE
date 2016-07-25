package com.sun.corba.se.spi.ior;

import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;
import java.util.List;
import org.omg.CORBA_2_3.portable.OutputStream;
import org.omg.IOP.TaggedComponent;

public abstract interface TaggedProfileTemplate extends List, Identifiable, WriteContents, MakeImmutable
{
  public abstract Iterator iteratorById(int paramInt);

  public abstract TaggedProfile create(ObjectKeyTemplate paramObjectKeyTemplate, ObjectId paramObjectId);

  public abstract void write(ObjectKeyTemplate paramObjectKeyTemplate, ObjectId paramObjectId, OutputStream paramOutputStream);

  public abstract boolean isEquivalent(TaggedProfileTemplate paramTaggedProfileTemplate);

  public abstract TaggedComponent[] getIOPComponents(ORB paramORB, int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.TaggedProfileTemplate
 * JD-Core Version:    0.6.2
 */