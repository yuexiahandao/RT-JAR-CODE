package sun.print;

import javax.print.PrintService;

public abstract interface BackgroundLookupListener
{
  public abstract void notifyServices(PrintService[] paramArrayOfPrintService);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.BackgroundLookupListener
 * JD-Core Version:    0.6.2
 */