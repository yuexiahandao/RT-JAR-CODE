package java.awt.image.renderable;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.util.Vector;

public abstract interface RenderableImage
{
  public static final String HINTS_OBSERVED = "HINTS_OBSERVED";

  public abstract Vector<RenderableImage> getSources();

  public abstract Object getProperty(String paramString);

  public abstract String[] getPropertyNames();

  public abstract boolean isDynamic();

  public abstract float getWidth();

  public abstract float getHeight();

  public abstract float getMinX();

  public abstract float getMinY();

  public abstract RenderedImage createScaledRendering(int paramInt1, int paramInt2, RenderingHints paramRenderingHints);

  public abstract RenderedImage createDefaultRendering();

  public abstract RenderedImage createRendering(RenderContext paramRenderContext);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.renderable.RenderableImage
 * JD-Core Version:    0.6.2
 */