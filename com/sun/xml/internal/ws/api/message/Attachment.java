package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;

public abstract interface Attachment
{
  @NotNull
  public abstract String getContentId();

  public abstract String getContentType();

  public abstract byte[] asByteArray();

  public abstract DataHandler asDataHandler();

  public abstract Source asSource();

  public abstract InputStream asInputStream();

  public abstract void writeTo(OutputStream paramOutputStream)
    throws IOException;

  public abstract void writeTo(SOAPMessage paramSOAPMessage)
    throws SOAPException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.Attachment
 * JD-Core Version:    0.6.2
 */