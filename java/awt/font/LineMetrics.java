package java.awt.font;

public abstract class LineMetrics
{
  public abstract int getNumChars();

  public abstract float getAscent();

  public abstract float getDescent();

  public abstract float getLeading();

  public abstract float getHeight();

  public abstract int getBaselineIndex();

  public abstract float[] getBaselineOffsets();

  public abstract float getStrikethroughOffset();

  public abstract float getStrikethroughThickness();

  public abstract float getUnderlineOffset();

  public abstract float getUnderlineThickness();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.LineMetrics
 * JD-Core Version:    0.6.2
 */