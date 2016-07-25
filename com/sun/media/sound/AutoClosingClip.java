package com.sun.media.sound;

import javax.sound.sampled.Clip;

abstract interface AutoClosingClip extends Clip
{
  public abstract boolean isAutoClosing();

  public abstract void setAutoClosing(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AutoClosingClip
 * JD-Core Version:    0.6.2
 */