package com.sun.media.sound;

public abstract interface ModelOscillator
{
  public abstract int getChannels();

  public abstract float getAttenuation();

  public abstract ModelOscillatorStream open(float paramFloat);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelOscillator
 * JD-Core Version:    0.6.2
 */