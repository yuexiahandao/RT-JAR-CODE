package javax.sound.midi;

import java.util.EventListener;

public abstract interface MetaEventListener extends EventListener
{
  public abstract void meta(MetaMessage paramMetaMessage);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.MetaEventListener
 * JD-Core Version:    0.6.2
 */