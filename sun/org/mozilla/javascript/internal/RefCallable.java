package sun.org.mozilla.javascript.internal;

public abstract interface RefCallable extends Callable
{
  public abstract Ref refCall(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.RefCallable
 * JD-Core Version:    0.6.2
 */