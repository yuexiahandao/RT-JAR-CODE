package sun.org.mozilla.javascript.internal.debug;

public abstract interface DebuggableScript
{
  public abstract boolean isTopLevel();

  public abstract boolean isFunction();

  public abstract String getFunctionName();

  public abstract int getParamCount();

  public abstract int getParamAndVarCount();

  public abstract String getParamOrVarName(int paramInt);

  public abstract String getSourceName();

  public abstract boolean isGeneratedScript();

  public abstract int[] getLineNumbers();

  public abstract int getFunctionCount();

  public abstract DebuggableScript getFunction(int paramInt);

  public abstract DebuggableScript getParent();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.debug.DebuggableScript
 * JD-Core Version:    0.6.2
 */