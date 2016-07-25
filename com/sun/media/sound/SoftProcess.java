package com.sun.media.sound;

public abstract interface SoftProcess extends SoftControl
{
  public abstract void init(SoftSynthesizer paramSoftSynthesizer);

  public abstract double[] get(int paramInt, String paramString);

  public abstract void processControlLogic();

  public abstract void reset();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftProcess
 * JD-Core Version:    0.6.2
 */