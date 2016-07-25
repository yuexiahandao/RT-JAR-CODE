package javax.print;

import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobAttributeListener;
import javax.print.event.PrintJobListener;

public abstract interface DocPrintJob
{
  public abstract PrintService getPrintService();

  public abstract PrintJobAttributeSet getAttributes();

  public abstract void addPrintJobListener(PrintJobListener paramPrintJobListener);

  public abstract void removePrintJobListener(PrintJobListener paramPrintJobListener);

  public abstract void addPrintJobAttributeListener(PrintJobAttributeListener paramPrintJobAttributeListener, PrintJobAttributeSet paramPrintJobAttributeSet);

  public abstract void removePrintJobAttributeListener(PrintJobAttributeListener paramPrintJobAttributeListener);

  public abstract void print(Doc paramDoc, PrintRequestAttributeSet paramPrintRequestAttributeSet)
    throws PrintException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.DocPrintJob
 * JD-Core Version:    0.6.2
 */