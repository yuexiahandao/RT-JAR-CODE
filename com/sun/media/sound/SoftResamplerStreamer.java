package com.sun.media.sound;

import java.io.IOException;

public abstract interface SoftResamplerStreamer extends ModelOscillatorStream
{
  public abstract void open(ModelWavetable paramModelWavetable, float paramFloat)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftResamplerStreamer
 * JD-Core Version:    0.6.2
 */