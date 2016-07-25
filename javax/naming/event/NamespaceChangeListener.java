package javax.naming.event;

public abstract interface NamespaceChangeListener extends NamingListener
{
  public abstract void objectAdded(NamingEvent paramNamingEvent);

  public abstract void objectRemoved(NamingEvent paramNamingEvent);

  public abstract void objectRenamed(NamingEvent paramNamingEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.event.NamespaceChangeListener
 * JD-Core Version:    0.6.2
 */