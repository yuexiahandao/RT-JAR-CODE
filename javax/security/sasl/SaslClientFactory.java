package javax.security.sasl;

import java.util.Map;
import javax.security.auth.callback.CallbackHandler;

public abstract interface SaslClientFactory
{
  public abstract SaslClient createSaslClient(String[] paramArrayOfString, String paramString1, String paramString2, String paramString3, Map<String, ?> paramMap, CallbackHandler paramCallbackHandler)
    throws SaslException;

  public abstract String[] getMechanismNames(Map<String, ?> paramMap);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.sasl.SaslClientFactory
 * JD-Core Version:    0.6.2
 */