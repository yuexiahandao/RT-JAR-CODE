package javax.xml.ws.handler.soap;

import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;

public abstract interface SOAPMessageContext extends MessageContext
{
  public abstract SOAPMessage getMessage();

  public abstract void setMessage(SOAPMessage paramSOAPMessage);

  public abstract Object[] getHeaders(QName paramQName, JAXBContext paramJAXBContext, boolean paramBoolean);

  public abstract Set<String> getRoles();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.handler.soap.SOAPMessageContext
 * JD-Core Version:    0.6.2
 */