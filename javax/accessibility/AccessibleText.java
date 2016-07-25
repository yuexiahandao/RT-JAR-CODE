package javax.accessibility;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.text.AttributeSet;

public abstract interface AccessibleText
{
  public static final int CHARACTER = 1;
  public static final int WORD = 2;
  public static final int SENTENCE = 3;

  public abstract int getIndexAtPoint(Point paramPoint);

  public abstract Rectangle getCharacterBounds(int paramInt);

  public abstract int getCharCount();

  public abstract int getCaretPosition();

  public abstract String getAtIndex(int paramInt1, int paramInt2);

  public abstract String getAfterIndex(int paramInt1, int paramInt2);

  public abstract String getBeforeIndex(int paramInt1, int paramInt2);

  public abstract AttributeSet getCharacterAttribute(int paramInt);

  public abstract int getSelectionStart();

  public abstract int getSelectionEnd();

  public abstract String getSelectedText();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleText
 * JD-Core Version:    0.6.2
 */