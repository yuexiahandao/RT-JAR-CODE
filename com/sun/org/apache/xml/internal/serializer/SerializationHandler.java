package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import javax.xml.transform.Transformer;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;

public abstract interface SerializationHandler extends ExtendedContentHandler, ExtendedLexicalHandler, XSLOutputAttributes, DeclHandler, DTDHandler, ErrorHandler, DOMSerializer, Serializer
{
  public abstract void setContentHandler(ContentHandler paramContentHandler);

  public abstract void close();

  public abstract void serialize(Node paramNode)
    throws IOException;

  public abstract boolean setEscaping(boolean paramBoolean)
    throws SAXException;

  public abstract void setIndentAmount(int paramInt);

  public abstract void setTransformer(Transformer paramTransformer);

  public abstract Transformer getTransformer();

  public abstract void setNamespaceMappings(NamespaceMappings paramNamespaceMappings);

  public abstract void flushPending()
    throws SAXException;

  public abstract void setDTDEntityExpansion(boolean paramBoolean);

  public abstract void setIsStandalone(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.SerializationHandler
 * JD-Core Version:    0.6.2
 */