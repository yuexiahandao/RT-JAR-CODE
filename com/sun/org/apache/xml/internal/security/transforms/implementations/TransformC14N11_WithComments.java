/*    */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*    */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_WithComments;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class TransformC14N11_WithComments extends TransformSpi
/*    */ {
/*    */   protected String engineGetURI()
/*    */   {
/* 41 */     return "http://www.w3.org/2006/12/xml-c14n11#WithComments";
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*    */     throws CanonicalizationException
/*    */   {
/* 47 */     return enginePerformTransform(paramXMLSignatureInput, null, paramTransform);
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream, Transform paramTransform)
/*    */     throws CanonicalizationException
/*    */   {
/* 54 */     Canonicalizer11_WithComments localCanonicalizer11_WithComments = new Canonicalizer11_WithComments();
/* 55 */     if (paramOutputStream != null) {
/* 56 */       localCanonicalizer11_WithComments.setWriter(paramOutputStream);
/*    */     }
/*    */ 
/* 59 */     byte[] arrayOfByte = null;
/* 60 */     arrayOfByte = localCanonicalizer11_WithComments.engineCanonicalize(paramXMLSignatureInput);
/* 61 */     XMLSignatureInput localXMLSignatureInput = new XMLSignatureInput(arrayOfByte);
/* 62 */     if (paramOutputStream != null) {
/* 63 */       localXMLSignatureInput.setOutputStream(paramOutputStream);
/*    */     }
/* 65 */     return localXMLSignatureInput;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14N11_WithComments
 * JD-Core Version:    0.6.2
 */