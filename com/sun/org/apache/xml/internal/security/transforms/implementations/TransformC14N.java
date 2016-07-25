/*    */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*    */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315OmitComments;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class TransformC14N extends TransformSpi
/*    */ {
/*    */   public static final String implementedTransformURI = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
/*    */ 
/*    */   protected String engineGetURI()
/*    */   {
/* 49 */     return "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*    */     throws CanonicalizationException
/*    */   {
/* 58 */     return enginePerformTransform(paramXMLSignatureInput, null, paramTransform);
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream, Transform paramTransform) throws CanonicalizationException
/*    */   {
/* 63 */     Canonicalizer20010315OmitComments localCanonicalizer20010315OmitComments = new Canonicalizer20010315OmitComments();
/* 64 */     if (paramOutputStream != null) {
/* 65 */       localCanonicalizer20010315OmitComments.setWriter(paramOutputStream);
/*    */     }
/* 67 */     byte[] arrayOfByte = null;
/* 68 */     arrayOfByte = localCanonicalizer20010315OmitComments.engineCanonicalize(paramXMLSignatureInput);
/* 69 */     XMLSignatureInput localXMLSignatureInput = new XMLSignatureInput(arrayOfByte);
/* 70 */     if (paramOutputStream != null) {
/* 71 */       localXMLSignatureInput.setOutputStream(paramOutputStream);
/*    */     }
/* 73 */     return localXMLSignatureInput;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14N
 * JD-Core Version:    0.6.2
 */