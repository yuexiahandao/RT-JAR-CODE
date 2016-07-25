package javax.sound.sampled;

import java.util.EventListener;

public abstract interface LineListener extends EventListener
{
  public abstract void update(LineEvent paramLineEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.LineListener
 * JD-Core Version:    0.6.2
 */