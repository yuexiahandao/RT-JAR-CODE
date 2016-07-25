package sun.font;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public abstract interface TextLineComponent
{
  public static final int LEFT_TO_RIGHT = 0;
  public static final int RIGHT_TO_LEFT = 1;
  public static final int UNCHANGED = 2;

  public abstract CoreMetrics getCoreMetrics();

  public abstract void draw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2);

  public abstract Rectangle2D getCharVisualBounds(int paramInt);

  public abstract Rectangle2D getVisualBounds();

  public abstract float getAdvance();

  public abstract Shape getOutline(float paramFloat1, float paramFloat2);

  public abstract int getNumCharacters();

  public abstract float getCharX(int paramInt);

  public abstract float getCharY(int paramInt);

  public abstract float getCharAdvance(int paramInt);

  public abstract boolean caretAtOffsetIsValid(int paramInt);

  public abstract int getLineBreakIndex(int paramInt, float paramFloat);

  public abstract float getAdvanceBetween(int paramInt1, int paramInt2);

  public abstract Rectangle2D getLogicalBounds();

  public abstract Rectangle2D getItalicBounds();

  public abstract AffineTransform getBaselineTransform();

  public abstract boolean isSimple();

  public abstract Rectangle getPixelBounds(FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2);

  public abstract TextLineComponent getSubset(int paramInt1, int paramInt2, int paramInt3);

  public abstract int getNumJustificationInfos();

  public abstract void getJustificationInfos(GlyphJustificationInfo[] paramArrayOfGlyphJustificationInfo, int paramInt1, int paramInt2, int paramInt3);

  public abstract TextLineComponent applyJustificationDeltas(float[] paramArrayOfFloat, int paramInt, boolean[] paramArrayOfBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.TextLineComponent
 * JD-Core Version:    0.6.2
 */