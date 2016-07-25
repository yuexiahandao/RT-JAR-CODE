package javax.xml.bind;

import org.xml.sax.ContentHandler;

public abstract interface UnmarshallerHandler extends ContentHandler
{
  public abstract Object getResult()
    throws JAXBException, IllegalStateException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.UnmarshallerHandler
 * JD-Core Version:    0.6.2
 */