package javax.imageio.event;

import java.util.EventListener;
import javax.imageio.ImageWriter;

public abstract interface IIOWriteProgressListener extends EventListener
{
  public abstract void imageStarted(ImageWriter paramImageWriter, int paramInt);

  public abstract void imageProgress(ImageWriter paramImageWriter, float paramFloat);

  public abstract void imageComplete(ImageWriter paramImageWriter);

  public abstract void thumbnailStarted(ImageWriter paramImageWriter, int paramInt1, int paramInt2);

  public abstract void thumbnailProgress(ImageWriter paramImageWriter, float paramFloat);

  public abstract void thumbnailComplete(ImageWriter paramImageWriter);

  public abstract void writeAborted(ImageWriter paramImageWriter);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.event.IIOWriteProgressListener
 * JD-Core Version:    0.6.2
 */