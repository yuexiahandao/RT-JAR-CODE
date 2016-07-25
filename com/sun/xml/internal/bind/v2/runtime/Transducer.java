package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.AccessorException;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

public abstract interface Transducer<ValueT>
{
  public abstract boolean isDefault();

  public abstract boolean useNamespace();

  public abstract void declareNamespace(ValueT paramValueT, XMLSerializer paramXMLSerializer)
    throws AccessorException;

  @NotNull
  public abstract CharSequence print(@NotNull ValueT paramValueT)
    throws AccessorException;

  public abstract ValueT parse(CharSequence paramCharSequence)
    throws AccessorException, SAXException;

  public abstract void writeText(XMLSerializer paramXMLSerializer, ValueT paramValueT, String paramString)
    throws IOException, SAXException, XMLStreamException, AccessorException;

  public abstract void writeLeafElement(XMLSerializer paramXMLSerializer, Name paramName, @NotNull ValueT paramValueT, String paramString)
    throws IOException, SAXException, XMLStreamException, AccessorException;

  public abstract QName getTypeName(@NotNull ValueT paramValueT);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.Transducer
 * JD-Core Version:    0.6.2
 */