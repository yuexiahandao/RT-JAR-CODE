package javax.imageio.spi;

public abstract interface RegisterableService
{
  public abstract void onRegistration(ServiceRegistry paramServiceRegistry, Class<?> paramClass);

  public abstract void onDeregistration(ServiceRegistry paramServiceRegistry, Class<?> paramClass);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.RegisterableService
 * JD-Core Version:    0.6.2
 */