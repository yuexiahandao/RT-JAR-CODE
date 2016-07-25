package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.Channel;

abstract interface SelChImpl extends Channel
{
  public abstract FileDescriptor getFD();

  public abstract int getFDVal();

  public abstract boolean translateAndUpdateReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl);

  public abstract boolean translateAndSetReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl);

  public abstract void translateAndSetInterestOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl);

  public abstract int validOps();

  public abstract void kill()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SelChImpl
 * JD-Core Version:    0.6.2
 */