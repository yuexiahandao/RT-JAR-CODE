package javax.xml.stream;

import javax.xml.namespace.NamespaceContext;

public abstract interface XMLStreamWriter
{
  public abstract void writeStartElement(String paramString)
    throws XMLStreamException;

  public abstract void writeStartElement(String paramString1, String paramString2)
    throws XMLStreamException;

  public abstract void writeStartElement(String paramString1, String paramString2, String paramString3)
    throws XMLStreamException;

  public abstract void writeEmptyElement(String paramString1, String paramString2)
    throws XMLStreamException;

  public abstract void writeEmptyElement(String paramString1, String paramString2, String paramString3)
    throws XMLStreamException;

  public abstract void writeEmptyElement(String paramString)
    throws XMLStreamException;

  public abstract void writeEndElement()
    throws XMLStreamException;

  public abstract void writeEndDocument()
    throws XMLStreamException;

  public abstract void close()
    throws XMLStreamException;

  public abstract void flush()
    throws XMLStreamException;

  public abstract void writeAttribute(String paramString1, String paramString2)
    throws XMLStreamException;

  public abstract void writeAttribute(String paramString1, String paramString2, String paramString3, String paramString4)
    throws XMLStreamException;

  public abstract void writeAttribute(String paramString1, String paramString2, String paramString3)
    throws XMLStreamException;

  public abstract void writeNamespace(String paramString1, String paramString2)
    throws XMLStreamException;

  public abstract void writeDefaultNamespace(String paramString)
    throws XMLStreamException;

  public abstract void writeComment(String paramString)
    throws XMLStreamException;

  public abstract void writeProcessingInstruction(String paramString)
    throws XMLStreamException;

  public abstract void writeProcessingInstruction(String paramString1, String paramString2)
    throws XMLStreamException;

  public abstract void writeCData(String paramString)
    throws XMLStreamException;

  public abstract void writeDTD(String paramString)
    throws XMLStreamException;

  public abstract void writeEntityRef(String paramString)
    throws XMLStreamException;

  public abstract void writeStartDocument()
    throws XMLStreamException;

  public abstract void writeStartDocument(String paramString)
    throws XMLStreamException;

  public abstract void writeStartDocument(String paramString1, String paramString2)
    throws XMLStreamException;

  public abstract void writeCharacters(String paramString)
    throws XMLStreamException;

  public abstract void writeCharacters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws XMLStreamException;

  public abstract String getPrefix(String paramString)
    throws XMLStreamException;

  public abstract void setPrefix(String paramString1, String paramString2)
    throws XMLStreamException;

  public abstract void setDefaultNamespace(String paramString)
    throws XMLStreamException;

  public abstract void setNamespaceContext(NamespaceContext paramNamespaceContext)
    throws XMLStreamException;

  public abstract NamespaceContext getNamespaceContext();

  public abstract Object getProperty(String paramString)
    throws IllegalArgumentException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.XMLStreamWriter
 * JD-Core Version:    0.6.2
 */