package org.omg.CORBA;

public abstract class Context
{
  public abstract String context_name();

  public abstract Context parent();

  public abstract Context create_child(String paramString);

  public abstract void set_one_value(String paramString, Any paramAny);

  public abstract void set_values(NVList paramNVList);

  public abstract void delete_values(String paramString);

  public abstract NVList get_values(String paramString1, int paramInt, String paramString2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.Context
 * JD-Core Version:    0.6.2
 */