package javax.swing.plaf;

import javax.swing.JComboBox;

public abstract class ComboBoxUI extends ComponentUI
{
  public abstract void setPopupVisible(JComboBox paramJComboBox, boolean paramBoolean);

  public abstract boolean isPopupVisible(JComboBox paramJComboBox);

  public abstract boolean isFocusTraversable(JComboBox paramJComboBox);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.ComboBoxUI
 * JD-Core Version:    0.6.2
 */