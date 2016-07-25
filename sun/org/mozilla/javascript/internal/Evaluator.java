package sun.org.mozilla.javascript.internal;

import java.util.List;
import sun.org.mozilla.javascript.internal.ast.ScriptNode;

public abstract interface Evaluator
{
  public abstract Object compile(CompilerEnvirons paramCompilerEnvirons, ScriptNode paramScriptNode, String paramString, boolean paramBoolean);

  public abstract Function createFunctionObject(Context paramContext, Scriptable paramScriptable, Object paramObject1, Object paramObject2);

  public abstract Script createScriptObject(Object paramObject1, Object paramObject2);

  public abstract void captureStackInfo(RhinoException paramRhinoException);

  public abstract String getSourcePositionFromStack(Context paramContext, int[] paramArrayOfInt);

  public abstract String getPatchedStack(RhinoException paramRhinoException, String paramString);

  public abstract List<String> getScriptStack(RhinoException paramRhinoException);

  public abstract void setEvalScriptFlag(Script paramScript);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Evaluator
 * JD-Core Version:    0.6.2
 */