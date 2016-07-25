package com.sun.org.apache.xerces.internal.jaxp.validation;

import java.io.IOException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.xml.sax.SAXException;

abstract interface ValidatorHelper
{
  public abstract void validate(Source paramSource, Result paramResult)
    throws SAXException, IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.ValidatorHelper
 * JD-Core Version:    0.6.2
 */