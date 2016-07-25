package java.util.concurrent;

public abstract interface TransferQueue<E> extends BlockingQueue<E>
{
  public abstract boolean tryTransfer(E paramE);

  public abstract void transfer(E paramE)
    throws InterruptedException;

  public abstract boolean tryTransfer(E paramE, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract boolean hasWaitingConsumer();

  public abstract int getWaitingConsumerCount();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.TransferQueue
 * JD-Core Version:    0.6.2
 */