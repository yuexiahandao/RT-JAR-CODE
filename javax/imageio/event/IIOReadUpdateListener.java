package javax.imageio.event;

import java.awt.image.BufferedImage;
import java.util.EventListener;
import javax.imageio.ImageReader;

public abstract interface IIOReadUpdateListener extends EventListener
{
  public abstract void passStarted(ImageReader paramImageReader, BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt);

  public abstract void imageUpdate(ImageReader paramImageReader, BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt);

  public abstract void passComplete(ImageReader paramImageReader, BufferedImage paramBufferedImage);

  public abstract void thumbnailPassStarted(ImageReader paramImageReader, BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt);

  public abstract void thumbnailUpdate(ImageReader paramImageReader, BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt);

  public abstract void thumbnailPassComplete(ImageReader paramImageReader, BufferedImage paramBufferedImage);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.event.IIOReadUpdateListener
 * JD-Core Version:    0.6.2
 */