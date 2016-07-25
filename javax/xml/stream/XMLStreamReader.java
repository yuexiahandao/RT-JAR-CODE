package javax.xml.stream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

public abstract interface XMLStreamReader extends XMLStreamConstants
{
  public abstract Object getProperty(String paramString)
    throws IllegalArgumentException;

  public abstract int next()
    throws XMLStreamException;

  public abstract void require(int paramInt, String paramString1, String paramString2)
    throws XMLStreamException;

  public abstract String getElementText()
    throws XMLStreamException;

  public abstract int nextTag()
    throws XMLStreamException;

  public abstract boolean hasNext()
    throws XMLStreamException;

  public abstract void close()
    throws XMLStreamException;

  public abstract String getNamespaceURI(String paramString);

  public abstract boolean isStartElement();

  public abstract boolean isEndElement();

  public abstract boolean isCharacters();

  public abstract boolean isWhiteSpace();

  public abstract String getAttributeValue(String paramString1, String paramString2);

  public abstract int getAttributeCount();

  public abstract QName getAttributeName(int paramInt);

  public abstract String getAttributeNamespace(int paramInt);

  public abstract String getAttributeLocalName(int paramInt);

  public abstract String getAttributePrefix(int paramInt);

  public abstract String getAttributeType(int paramInt);

  public abstract String getAttributeValue(int paramInt);

  public abstract boolean isAttributeSpecified(int paramInt);

  public abstract int getNamespaceCount();

  public abstract String getNamespacePrefix(int paramInt);

  public abstract String getNamespaceURI(int paramInt);

  public abstract NamespaceContext getNamespaceContext();

  public abstract int getEventType();

  public abstract String getText();

  public abstract char[] getTextCharacters();

  public abstract int getTextCharacters(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
    throws XMLStreamException;

  public abstract int getTextStart();

  public abstract int getTextLength();

  public abstract String getEncoding();

  public abstract boolean hasText();

  public abstract Location getLocation();

  public abstract QName getName();

  public abstract String getLocalName();

  public abstract boolean hasName();

  public abstract String getNamespaceURI();

  public abstract String getPrefix();

  public abstract String getVersion();

  public abstract boolean isStandalone();

  public abstract boolean standaloneSet();

  public abstract String getCharacterEncodingScheme();

  public abstract String getPITarget();

  public abstract String getPIData();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.XMLStreamReader
 * JD-Core Version:    0.6.2
 */