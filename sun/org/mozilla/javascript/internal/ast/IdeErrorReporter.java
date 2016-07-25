package sun.org.mozilla.javascript.internal.ast;

import sun.org.mozilla.javascript.internal.ErrorReporter;

public abstract interface IdeErrorReporter extends ErrorReporter
{
  public abstract void warning(String paramString1, String paramString2, int paramInt1, int paramInt2);

  public abstract void error(String paramString1, String paramString2, int paramInt1, int paramInt2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.IdeErrorReporter
 * JD-Core Version:    0.6.2
 */