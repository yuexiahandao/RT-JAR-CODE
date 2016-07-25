/*    */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*    */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class TransformC14N11 extends TransformSpi
/*    */ {
/*    */   protected String engineGetURI()
/*    */   {
/* 41 */     return "http://www.w3.org/2006/12/xml-c14n11";
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
/* 53 */     Canonicalizer11_OmitComments localCanonicalizer11_OmitComments = new Canonicalizer11_OmitComments();
/* 54 */     if (paramOutputStream != null) {
/* 55 */       localCanonicalizer11_OmitComments.setWriter(paramOutputStream);
/*    */     }
/* 57 */     byte[] arrayOfByte = null;
/* 58 */     arrayOfByte = localCanonicalizer11_OmitComments.engineCanonicalize(paramXMLSignatureInput);
/* 59 */     XMLSignatureInput localXMLSignatureInput = new XMLSignatureInput(arrayOfByte);
/* 60 */     if (paramOutputStream != null) {
/* 61 */       localXMLSignatureInput.setOutputStream(paramOutputStream);
/*    */     }
/* 63 */     return localXMLSignatureInput;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14N11
 * JD-Core Version:    0.6.2
 */