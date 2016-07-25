package sun.org.mozilla.javascript.internal;

public abstract interface ConstProperties
{
  public abstract void putConst(String paramString, Scriptable paramScriptable, Object paramObject);

  public abstract void defineConst(String paramString, Scriptable paramScriptable);

  public abstract boolean isConst(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ConstProperties
 * JD-Core Version:    0.6.2
 */