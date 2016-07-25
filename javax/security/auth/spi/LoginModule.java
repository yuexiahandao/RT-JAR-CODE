package javax.security.auth.spi;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public abstract interface LoginModule
{
  public abstract void initialize(Subject paramSubject, CallbackHandler paramCallbackHandler, Map<String, ?> paramMap1, Map<String, ?> paramMap2);

  public abstract boolean login()
    throws LoginException;

  public abstract boolean commit()
    throws LoginException;

  public abstract boolean abort()
    throws LoginException;

  public abstract boolean logout()
    throws LoginException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.spi.LoginModule
 * JD-Core Version:    0.6.2
 */