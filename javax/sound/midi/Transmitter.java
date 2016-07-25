package javax.sound.midi;

public abstract interface Transmitter extends AutoCloseable
{
  public abstract void setReceiver(Receiver paramReceiver);

  public abstract Receiver getReceiver();

  public abstract void close();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.Transmitter
 * JD-Core Version:    0.6.2
 */