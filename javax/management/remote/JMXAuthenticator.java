package javax.management.remote;

import javax.security.auth.Subject;

public abstract interface JMXAuthenticator
{
  public abstract Subject authenticate(Object paramObject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.JMXAuthenticator
 * JD-Core Version:    0.6.2
 */