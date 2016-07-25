package com.sun.media.sound;

public abstract interface ModelWavetable extends ModelOscillator
{
  public static final int LOOP_TYPE_OFF = 0;
  public static final int LOOP_TYPE_FORWARD = 1;
  public static final int LOOP_TYPE_RELEASE = 2;
  public static final int LOOP_TYPE_PINGPONG = 4;
  public static final int LOOP_TYPE_REVERSE = 8;

  public abstract AudioFloatInputStream openStream();

  public abstract float getLoopLength();

  public abstract float getLoopStart();

  public abstract int getLoopType();

  public abstract float getPitchcorrection();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelWavetable
 * JD-Core Version:    0.6.2
 */