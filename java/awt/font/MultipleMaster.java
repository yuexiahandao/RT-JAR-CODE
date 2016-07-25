package java.awt.font;

import java.awt.Font;

public abstract interface MultipleMaster
{
  public abstract int getNumDesignAxes();

  public abstract float[] getDesignAxisRanges();

  public abstract float[] getDesignAxisDefaults();

  public abstract String[] getDesignAxisNames();

  public abstract Font deriveMMFont(float[] paramArrayOfFloat);

  public abstract Font deriveMMFont(float[] paramArrayOfFloat, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.MultipleMaster
 * JD-Core Version:    0.6.2
 */