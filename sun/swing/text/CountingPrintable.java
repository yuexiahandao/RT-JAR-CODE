package sun.swing.text;

import java.awt.print.Printable;

public abstract interface CountingPrintable extends Printable
{
  public abstract int getNumberOfPages();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.text.CountingPrintable
 * JD-Core Version:    0.6.2
 */