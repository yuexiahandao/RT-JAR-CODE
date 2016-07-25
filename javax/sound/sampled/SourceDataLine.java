package javax.sound.sampled;

public abstract interface SourceDataLine extends DataLine
{
  public abstract void open(AudioFormat paramAudioFormat, int paramInt)
    throws LineUnavailableException;

  public abstract void open(AudioFormat paramAudioFormat)
    throws LineUnavailableException;

  public abstract int write(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.SourceDataLine
 * JD-Core Version:    0.6.2
 */