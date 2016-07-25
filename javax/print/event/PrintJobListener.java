package javax.print.event;

public abstract interface PrintJobListener
{
  public abstract void printDataTransferCompleted(PrintJobEvent paramPrintJobEvent);

  public abstract void printJobCompleted(PrintJobEvent paramPrintJobEvent);

  public abstract void printJobFailed(PrintJobEvent paramPrintJobEvent);

  public abstract void printJobCanceled(PrintJobEvent paramPrintJobEvent);

  public abstract void printJobNoMoreEvents(PrintJobEvent paramPrintJobEvent);

  public abstract void printJobRequiresAttention(PrintJobEvent paramPrintJobEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.event.PrintJobListener
 * JD-Core Version:    0.6.2
 */