package javax.xml.bind;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public abstract interface Unmarshaller
{
  public abstract Object unmarshal(File paramFile)
    throws JAXBException;

  public abstract Object unmarshal(InputStream paramInputStream)
    throws JAXBException;

  public abstract Object unmarshal(Reader paramReader)
    throws JAXBException;

  public abstract Object unmarshal(URL paramURL)
    throws JAXBException;

  public abstract Object unmarshal(InputSource paramInputSource)
    throws JAXBException;

  public abstract Object unmarshal(Node paramNode)
    throws JAXBException;

  public abstract <T> JAXBElement<T> unmarshal(Node paramNode, Class<T> paramClass)
    throws JAXBException;

  public abstract Object unmarshal(Source paramSource)
    throws JAXBException;

  public abstract <T> JAXBElement<T> unmarshal(Source paramSource, Class<T> paramClass)
    throws JAXBException;

  public abstract Object unmarshal(XMLStreamReader paramXMLStreamReader)
    throws JAXBException;

  public abstract <T> JAXBElement<T> unmarshal(XMLStreamReader paramXMLStreamReader, Class<T> paramClass)
    throws JAXBException;

  public abstract Object unmarshal(XMLEventReader paramXMLEventReader)
    throws JAXBException;

  public abstract <T> JAXBElement<T> unmarshal(XMLEventReader paramXMLEventReader, Class<T> paramClass)
    throws JAXBException;

  public abstract UnmarshallerHandler getUnmarshallerHandler();

  /** @deprecated */
  public abstract void setValidating(boolean paramBoolean)
    throws JAXBException;

  /** @deprecated */
  public abstract boolean isValidating()
    throws JAXBException;

  public abstract void setEventHandler(ValidationEventHandler paramValidationEventHandler)
    throws JAXBException;

  public abstract ValidationEventHandler getEventHandler()
    throws JAXBException;

  public abstract void setProperty(String paramString, Object paramObject)
    throws PropertyException;

  public abstract Object getProperty(String paramString)
    throws PropertyException;

  public abstract void setSchema(Schema paramSchema);

  public abstract Schema getSchema();

  public abstract void setAdapter(XmlAdapter paramXmlAdapter);

  public abstract <A extends XmlAdapter> void setAdapter(Class<A> paramClass, A paramA);

  public abstract <A extends XmlAdapter> A getAdapter(Class<A> paramClass);

  public abstract void setAttachmentUnmarshaller(AttachmentUnmarshaller paramAttachmentUnmarshaller);

  public abstract AttachmentUnmarshaller getAttachmentUnmarshaller();

  public abstract void setListener(Listener paramListener);

  public abstract Listener getListener();

  public static abstract class Listener
  {
    public void beforeUnmarshal(Object target, Object parent)
    {
    }

    public void afterUnmarshal(Object target, Object parent)
    {
    }
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.Unmarshaller
 * JD-Core Version:    0.6.2
 */