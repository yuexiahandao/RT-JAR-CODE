package sun.rmi.transport.proxy;

abstract interface CGICommandHandler
{
  public abstract String getName();

  public abstract void execute(String paramString)
    throws CGIClientException, CGIServerException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.CGICommandHandler
 * JD-Core Version:    0.6.2
 */