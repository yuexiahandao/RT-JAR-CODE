package com.sun.media.sound;

import javax.sound.midi.MidiChannel;

public abstract interface ModelChannelMixer extends MidiChannel
{
  public abstract boolean process(float[][] paramArrayOfFloat, int paramInt1, int paramInt2);

  public abstract void stop();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelChannelMixer
 * JD-Core Version:    0.6.2
 */