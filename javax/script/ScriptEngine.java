package javax.script;

import java.io.Reader;

public abstract interface ScriptEngine
{
  public static final String ARGV = "javax.script.argv";
  public static final String FILENAME = "javax.script.filename";
  public static final String ENGINE = "javax.script.engine";
  public static final String ENGINE_VERSION = "javax.script.engine_version";
  public static final String NAME = "javax.script.name";
  public static final String LANGUAGE = "javax.script.language";
  public static final String LANGUAGE_VERSION = "javax.script.language_version";

  public abstract Object eval(String paramString, ScriptContext paramScriptContext)
    throws ScriptException;

  public abstract Object eval(Reader paramReader, ScriptContext paramScriptContext)
    throws ScriptException;

  public abstract Object eval(String paramString)
    throws ScriptException;

  public abstract Object eval(Reader paramReader)
    throws ScriptException;

  public abstract Object eval(String paramString, Bindings paramBindings)
    throws ScriptException;

  public abstract Object eval(Reader paramReader, Bindings paramBindings)
    throws ScriptException;

  public abstract void put(String paramString, Object paramObject);

  public abstract Object get(String paramString);

  public abstract Bindings getBindings(int paramInt);

  public abstract void setBindings(Bindings paramBindings, int paramInt);

  public abstract Bindings createBindings();

  public abstract ScriptContext getContext();

  public abstract void setContext(ScriptContext paramScriptContext);

  public abstract ScriptEngineFactory getFactory();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.script.ScriptEngine
 * JD-Core Version:    0.6.2
 */