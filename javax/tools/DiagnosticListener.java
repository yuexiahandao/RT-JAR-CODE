package javax.tools;

public abstract interface DiagnosticListener<S>
{
  public abstract void report(Diagnostic<? extends S> paramDiagnostic);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.DiagnosticListener
 * JD-Core Version:    0.6.2
 */