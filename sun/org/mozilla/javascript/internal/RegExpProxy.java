package sun.org.mozilla.javascript.internal;

public abstract interface RegExpProxy
{
  public static final int RA_MATCH = 1;
  public static final int RA_REPLACE = 2;
  public static final int RA_SEARCH = 3;

  public abstract boolean isRegExp(Scriptable paramScriptable);

  public abstract Object compileRegExp(Context paramContext, String paramString1, String paramString2);

  public abstract Scriptable wrapRegExp(Context paramContext, Scriptable paramScriptable, Object paramObject);

  public abstract Object action(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject, int paramInt);

  public abstract int find_split(Context paramContext, Scriptable paramScriptable1, String paramString1, String paramString2, Scriptable paramScriptable2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean[] paramArrayOfBoolean, String[][] paramArrayOfString);

  public abstract Object js_split(Context paramContext, Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.RegExpProxy
 * JD-Core Version:    0.6.2
 */