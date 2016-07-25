package javax.imageio.event;

import java.util.EventListener;
import javax.imageio.ImageReader;

public abstract interface IIOReadWarningListener extends EventListener
{
  public abstract void warningOccurred(ImageReader paramImageReader, String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.event.IIOReadWarningListener
 * JD-Core Version:    0.6.2
 */