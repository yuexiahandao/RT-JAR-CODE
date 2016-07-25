package javax.print;

public abstract interface CancelablePrintJob extends DocPrintJob
{
  public abstract void cancel()
    throws PrintException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.CancelablePrintJob
 * JD-Core Version:    0.6.2
 */