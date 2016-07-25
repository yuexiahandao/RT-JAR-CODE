package com.sun.org.omg.SendingContext;

import com.sun.org.omg.CORBA.Repository;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
import org.omg.SendingContext.RunTimeOperations;

public abstract interface CodeBaseOperations extends RunTimeOperations
{
  public abstract Repository get_ir();

  public abstract String implementation(String paramString);

  public abstract String[] implementations(String[] paramArrayOfString);

  public abstract FullValueDescription meta(String paramString);

  public abstract FullValueDescription[] metas(String[] paramArrayOfString);

  public abstract String[] bases(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.SendingContext.CodeBaseOperations
 * JD-Core Version:    0.6.2
 */