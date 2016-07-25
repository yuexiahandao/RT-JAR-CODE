package sun.awt.windows;

import java.awt.Color;

class WColor
{
  static final int WINDOW_BKGND = 1;
  static final int WINDOW_TEXT = 2;
  static final int FRAME = 3;
  static final int SCROLLBAR = 4;
  static final int MENU_BKGND = 5;
  static final int MENU_TEXT = 6;
  static final int BUTTON_BKGND = 7;
  static final int BUTTON_TEXT = 8;
  static final int HIGHLIGHT = 9;

  static native Color getDefaultColor(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WColor
 * JD-Core Version:    0.6.2
 */