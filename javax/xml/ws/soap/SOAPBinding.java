package javax.xml.ws.soap;

import java.util.Set;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.Binding;

public abstract interface SOAPBinding extends Binding
{
  public static final String SOAP11HTTP_BINDING = "http://schemas.xmlsoap.org/wsdl/soap/http";
  public static final String SOAP12HTTP_BINDING = "http://www.w3.org/2003/05/soap/bindings/HTTP/";
  public static final String SOAP11HTTP_MTOM_BINDING = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true";
  public static final String SOAP12HTTP_MTOM_BINDING = "http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true";

  public abstract Set<String> getRoles();

  public abstract void setRoles(Set<String> paramSet);

  public abstract boolean isMTOMEnabled();

  public abstract void setMTOMEnabled(boolean paramBoolean);

  public abstract SOAPFactory getSOAPFactory();

  public abstract MessageFactory getMessageFactory();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.soap.SOAPBinding
 * JD-Core Version:    0.6.2
 */