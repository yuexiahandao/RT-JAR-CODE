package javax.script;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

public abstract interface ScriptContext
{
  public static final int ENGINE_SCOPE = 100;
  public static final int GLOBAL_SCOPE = 200;

  public abstract void setBindings(Bindings paramBindings, int paramInt);

  public abstract Bindings getBindings(int paramInt);

  public abstract void setAttribute(String paramString, Object paramObject, int paramInt);

  public abstract Object getAttribute(String paramString, int paramInt);

  public abstract Object removeAttribute(String paramString, int paramInt);

  public abstract Object getAttribute(String paramString);

  public abstract int getAttributesScope(String paramString);

  public abstract Writer getWriter();

  public abstract Writer getErrorWriter();

  public abstract void setWriter(Writer paramWriter);

  public abstract void setErrorWriter(Writer paramWriter);

  public abstract Reader getReader();

  public abstract void setReader(Reader paramReader);

  public abstract List<Integer> getScopes();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.script.ScriptContext
 * JD-Core Version:    0.6.2
 */