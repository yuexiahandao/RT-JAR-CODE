package javax.script;

import java.io.Reader;

public abstract interface Compilable
{
  public abstract CompiledScript compile(String paramString)
    throws ScriptException;

  public abstract CompiledScript compile(Reader paramReader)
    throws ScriptException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.script.Compilable
 * JD-Core Version:    0.6.2
 */