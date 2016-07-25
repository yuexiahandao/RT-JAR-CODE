package com.sun.xml.internal.messaging.saaj.packaging.mime;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
import javax.activation.DataSource;

public abstract interface MultipartDataSource extends DataSource
{
  public abstract int getCount();

  public abstract MimeBodyPart getBodyPart(int paramInt)
    throws MessagingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.MultipartDataSource
 * JD-Core Version:    0.6.2
 */