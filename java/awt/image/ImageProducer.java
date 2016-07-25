package java.awt.image;

public abstract interface ImageProducer
{
  public abstract void addConsumer(ImageConsumer paramImageConsumer);

  public abstract boolean isConsumer(ImageConsumer paramImageConsumer);

  public abstract void removeConsumer(ImageConsumer paramImageConsumer);

  public abstract void startProduction(ImageConsumer paramImageConsumer);

  public abstract void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ImageProducer
 * JD-Core Version:    0.6.2
 */