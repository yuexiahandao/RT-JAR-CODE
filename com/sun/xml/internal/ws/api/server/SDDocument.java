package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.Nullable;
import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

@ManagedData
public abstract interface SDDocument
{
  @ManagedAttribute
  public abstract QName getRootName();

  @ManagedAttribute
  public abstract boolean isWSDL();

  @ManagedAttribute
  public abstract boolean isSchema();

  @ManagedAttribute
  public abstract Set<String> getImports();

  @ManagedAttribute
  public abstract URL getURL();

  public abstract void writeTo(@Nullable PortAddressResolver paramPortAddressResolver, DocumentAddressResolver paramDocumentAddressResolver, OutputStream paramOutputStream)
    throws IOException;

  public abstract void writeTo(PortAddressResolver paramPortAddressResolver, DocumentAddressResolver paramDocumentAddressResolver, XMLStreamWriter paramXMLStreamWriter)
    throws XMLStreamException, IOException;

  public static abstract interface Schema extends SDDocument
  {
    @ManagedAttribute
    public abstract String getTargetNamespace();
  }

  public static abstract interface WSDL extends SDDocument
  {
    @ManagedAttribute
    public abstract String getTargetNamespace();

    @ManagedAttribute
    public abstract boolean hasPortType();

    @ManagedAttribute
    public abstract boolean hasService();

    @ManagedAttribute
    public abstract Set<QName> getAllServices();
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.SDDocument
 * JD-Core Version:    0.6.2
 */