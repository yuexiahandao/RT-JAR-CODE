package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

abstract interface DOMDocumentHandler extends XMLDocumentHandler
{
  public abstract void setDOMResult(DOMResult paramDOMResult);

  public abstract void doctypeDecl(DocumentType paramDocumentType)
    throws XNIException;

  public abstract void characters(Text paramText)
    throws XNIException;

  public abstract void cdata(CDATASection paramCDATASection)
    throws XNIException;

  public abstract void comment(Comment paramComment)
    throws XNIException;

  public abstract void processingInstruction(ProcessingInstruction paramProcessingInstruction)
    throws XNIException;

  public abstract void setIgnoringCharacters(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
 * JD-Core Version:    0.6.2
 */