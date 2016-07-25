package sun.net.spi.nameservice;

public abstract interface NameServiceDescriptor
{
  public abstract NameService createNameService()
    throws Exception;

  public abstract String getProviderName();

  public abstract String getType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.spi.nameservice.NameServiceDescriptor
 * JD-Core Version:    0.6.2
 */