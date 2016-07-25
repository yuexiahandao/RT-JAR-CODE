package java.awt;

import java.awt.image.ColorModel;

public abstract interface Composite
{
  public abstract CompositeContext createContext(ColorModel paramColorModel1, ColorModel paramColorModel2, RenderingHints paramRenderingHints);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Composite
 * JD-Core Version:    0.6.2
 */