package javax.xml.ws;

import java.util.Map;

public abstract interface BindingProvider
{
  public static final String USERNAME_PROPERTY = "javax.xml.ws.security.auth.username";
  public static final String PASSWORD_PROPERTY = "javax.xml.ws.security.auth.password";
  public static final String ENDPOINT_ADDRESS_PROPERTY = "javax.xml.ws.service.endpoint.address";
  public static final String SESSION_MAINTAIN_PROPERTY = "javax.xml.ws.session.maintain";
  public static final String SOAPACTION_USE_PROPERTY = "javax.xml.ws.soap.http.soapaction.use";
  public static final String SOAPACTION_URI_PROPERTY = "javax.xml.ws.soap.http.soapaction.uri";

  public abstract Map<String, Object> getRequestContext();

  public abstract Map<String, Object> getResponseContext();

  public abstract Binding getBinding();

  public abstract EndpointReference getEndpointReference();

  public abstract <T extends EndpointReference> T getEndpointReference(Class<T> paramClass);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.BindingProvider
 * JD-Core Version:    0.6.2
 */