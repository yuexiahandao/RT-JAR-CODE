package sun.awt.geom;

public abstract interface PathConsumer2D
{
  public abstract void moveTo(float paramFloat1, float paramFloat2);

  public abstract void lineTo(float paramFloat1, float paramFloat2);

  public abstract void quadTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public abstract void curveTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

  public abstract void closePath();

  public abstract void pathDone();

  public abstract long getNativeConsumer();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.geom.PathConsumer2D
 * JD-Core Version:    0.6.2
 */