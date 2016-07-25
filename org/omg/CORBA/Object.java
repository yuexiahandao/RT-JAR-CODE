package org.omg.CORBA;

public abstract interface Object
{
  public abstract boolean _is_a(String paramString);

  public abstract boolean _is_equivalent(Object paramObject);

  public abstract boolean _non_existent();

  public abstract int _hash(int paramInt);

  public abstract Object _duplicate();

  public abstract void _release();

  public abstract Object _get_interface_def();

  public abstract Request _request(String paramString);

  public abstract Request _create_request(Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue);

  public abstract Request _create_request(Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue, ExceptionList paramExceptionList, ContextList paramContextList);

  public abstract Policy _get_policy(int paramInt);

  public abstract DomainManager[] _get_domain_managers();

  public abstract Object _set_policy_override(Policy[] paramArrayOfPolicy, SetOverrideType paramSetOverrideType);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.Object
 * JD-Core Version:    0.6.2
 */