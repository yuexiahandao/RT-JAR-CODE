package javax.transaction.xa;

public abstract interface XAResource
{
  public static final int TMENDRSCAN = 8388608;
  public static final int TMFAIL = 536870912;
  public static final int TMJOIN = 2097152;
  public static final int TMNOFLAGS = 0;
  public static final int TMONEPHASE = 1073741824;
  public static final int TMRESUME = 134217728;
  public static final int TMSTARTRSCAN = 16777216;
  public static final int TMSUCCESS = 67108864;
  public static final int TMSUSPEND = 33554432;
  public static final int XA_RDONLY = 3;
  public static final int XA_OK = 0;

  public abstract void commit(Xid paramXid, boolean paramBoolean)
    throws XAException;

  public abstract void end(Xid paramXid, int paramInt)
    throws XAException;

  public abstract void forget(Xid paramXid)
    throws XAException;

  public abstract int getTransactionTimeout()
    throws XAException;

  public abstract boolean isSameRM(XAResource paramXAResource)
    throws XAException;

  public abstract int prepare(Xid paramXid)
    throws XAException;

  public abstract Xid[] recover(int paramInt)
    throws XAException;

  public abstract void rollback(Xid paramXid)
    throws XAException;

  public abstract boolean setTransactionTimeout(int paramInt)
    throws XAException;

  public abstract void start(Xid paramXid, int paramInt)
    throws XAException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.transaction.xa.XAResource
 * JD-Core Version:    0.6.2
 */