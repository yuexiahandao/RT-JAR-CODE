package javax.print;

import javax.print.attribute.PrintRequestAttributeSet;

public abstract interface MultiDocPrintJob extends DocPrintJob
{
  public abstract void print(MultiDoc paramMultiDoc, PrintRequestAttributeSet paramPrintRequestAttributeSet)
    throws PrintException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.MultiDocPrintJob
 * JD-Core Version:    0.6.2
 */