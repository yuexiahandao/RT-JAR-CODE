package javax.smartcardio;

import java.nio.ByteBuffer;

public abstract class CardChannel
{
  public abstract Card getCard();

  public abstract int getChannelNumber();

  public abstract ResponseAPDU transmit(CommandAPDU paramCommandAPDU)
    throws CardException;

  public abstract int transmit(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
    throws CardException;

  public abstract void close()
    throws CardException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.smartcardio.CardChannel
 * JD-Core Version:    0.6.2
 */