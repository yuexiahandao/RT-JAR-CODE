/*    */ package com.sun.org.apache.xml.internal.security.keys.content.x509;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class XMLX509CRL extends SignatureElementProxy
/*    */   implements XMLX509DataContent
/*    */ {
/*    */   public XMLX509CRL(Element paramElement, String paramString)
/*    */     throws XMLSecurityException
/*    */   {
/* 46 */     super(paramElement, paramString);
/*    */   }
/*    */ 
/*    */   public XMLX509CRL(Document paramDocument, byte[] paramArrayOfByte)
/*    */   {
/* 57 */     super(paramDocument);
/*    */ 
/* 59 */     addBase64Text(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public byte[] getCRLBytes()
/*    */     throws XMLSecurityException
/*    */   {
/* 69 */     return getBytesFromTextChild();
/*    */   }
/*    */ 
/*    */   public String getBaseLocalName()
/*    */   {
/* 74 */     return "X509CRL";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509CRL
 * JD-Core Version:    0.6.2
 */