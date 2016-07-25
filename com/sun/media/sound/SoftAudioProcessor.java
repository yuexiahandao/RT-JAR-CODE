package com.sun.media.sound;

public abstract interface SoftAudioProcessor
{
  public abstract void globalParameterControlChange(int[] paramArrayOfInt, long paramLong1, long paramLong2);

  public abstract void init(float paramFloat1, float paramFloat2);

  public abstract void setInput(int paramInt, SoftAudioBuffer paramSoftAudioBuffer);

  public abstract void setOutput(int paramInt, SoftAudioBuffer paramSoftAudioBuffer);

  public abstract void setMixMode(boolean paramBoolean);

  public abstract void processAudio();

  public abstract void processControlLogic();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftAudioProcessor
 * JD-Core Version:    0.6.2
 */