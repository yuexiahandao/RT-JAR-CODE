/*    */ package org.jcp.xml.dsig.internal.dom;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import java.io.IOException;
/*    */ import javax.xml.crypto.OctetStreamData;
/*    */ 
/*    */ public class ApacheOctetStreamData extends OctetStreamData
/*    */   implements ApacheData
/*    */ {
/*    */   private XMLSignatureInput xi;
/*    */ 
/*    */   public ApacheOctetStreamData(XMLSignatureInput paramXMLSignatureInput)
/*    */     throws CanonicalizationException, IOException
/*    */   {
/* 41 */     super(paramXMLSignatureInput.getOctetStream(), paramXMLSignatureInput.getSourceURI(), paramXMLSignatureInput.getMIMEType());
/* 42 */     this.xi = paramXMLSignatureInput;
/*    */   }
/*    */ 
/*    */   public XMLSignatureInput getXMLSignatureInput() {
/* 46 */     return this.xi;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.ApacheOctetStreamData
 * JD-Core Version:    0.6.2
 */