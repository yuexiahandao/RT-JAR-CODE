package javax.swing.text;

import java.awt.Graphics;
import java.awt.Shape;

public abstract interface Highlighter
{
  public abstract void install(JTextComponent paramJTextComponent);

  public abstract void deinstall(JTextComponent paramJTextComponent);

  public abstract void paint(Graphics paramGraphics);

  public abstract Object addHighlight(int paramInt1, int paramInt2, HighlightPainter paramHighlightPainter)
    throws BadLocationException;

  public abstract void removeHighlight(Object paramObject);

  public abstract void removeAllHighlights();

  public abstract void changeHighlight(Object paramObject, int paramInt1, int paramInt2)
    throws BadLocationException;

  public abstract Highlight[] getHighlights();

  public static abstract interface Highlight
  {
    public abstract int getStartOffset();

    public abstract int getEndOffset();

    public abstract Highlighter.HighlightPainter getPainter();
  }

  public static abstract interface HighlightPainter
  {
    public abstract void paint(Graphics paramGraphics, int paramInt1, int paramInt2, Shape paramShape, JTextComponent paramJTextComponent);
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.Highlighter
 * JD-Core Version:    0.6.2
 */