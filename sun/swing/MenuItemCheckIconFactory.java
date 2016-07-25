package sun.swing;

import javax.swing.Icon;
import javax.swing.JMenuItem;

public abstract interface MenuItemCheckIconFactory
{
  public abstract Icon getIcon(JMenuItem paramJMenuItem);

  public abstract boolean isCompatible(Object paramObject, String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.MenuItemCheckIconFactory
 * JD-Core Version:    0.6.2
 */