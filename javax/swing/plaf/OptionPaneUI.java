package javax.swing.plaf;

import javax.swing.JOptionPane;

public abstract class OptionPaneUI extends ComponentUI
{
  public abstract void selectInitialValue(JOptionPane paramJOptionPane);

  public abstract boolean containsCustomComponents(JOptionPane paramJOptionPane);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.OptionPaneUI
 * JD-Core Version:    0.6.2
 */