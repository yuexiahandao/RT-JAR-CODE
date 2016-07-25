package java.nio.channels;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public abstract interface AsynchronousByteChannel extends AsynchronousChannel
{
  public abstract <A> void read(ByteBuffer paramByteBuffer, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler);

  public abstract Future<Integer> read(ByteBuffer paramByteBuffer);

  public abstract <A> void write(ByteBuffer paramByteBuffer, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler);

  public abstract Future<Integer> write(ByteBuffer paramByteBuffer);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.AsynchronousByteChannel
 * JD-Core Version:    0.6.2
 */