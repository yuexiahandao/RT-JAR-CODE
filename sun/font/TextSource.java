package sun.font;

import java.awt.Font;
import java.awt.font.FontRenderContext;

public abstract class TextSource
{
  public static final boolean WITHOUT_CONTEXT = false;
  public static final boolean WITH_CONTEXT = true;

  public abstract char[] getChars();

  public abstract int getStart();

  public abstract int getLength();

  public abstract int getContextStart();

  public abstract int getContextLength();

  public abstract int getLayoutFlags();

  public abstract int getBidiLevel();

  public abstract Font getFont();

  public abstract FontRenderContext getFRC();

  public abstract CoreMetrics getCoreMetrics();

  public abstract TextSource getSubSource(int paramInt1, int paramInt2, int paramInt3);

  public abstract String toString(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.TextSource
 * JD-Core Version:    0.6.2
 */