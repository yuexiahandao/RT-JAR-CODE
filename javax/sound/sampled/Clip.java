package javax.sound.sampled;

import java.io.IOException;

public abstract interface Clip extends DataLine
{
  public static final int LOOP_CONTINUOUSLY = -1;

  public abstract void open(AudioFormat paramAudioFormat, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws LineUnavailableException;

  public abstract void open(AudioInputStream paramAudioInputStream)
    throws LineUnavailableException, IOException;

  public abstract int getFrameLength();

  public abstract long getMicrosecondLength();

  public abstract void setFramePosition(int paramInt);

  public abstract void setMicrosecondPosition(long paramLong);

  public abstract void setLoopPoints(int paramInt1, int paramInt2);

  public abstract void loop(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.Clip
 * JD-Core Version:    0.6.2
 */