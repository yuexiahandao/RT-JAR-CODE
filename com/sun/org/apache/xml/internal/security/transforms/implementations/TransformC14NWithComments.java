/*    */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*    */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315WithComments;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class TransformC14NWithComments extends TransformSpi
/*    */ {
/*    */   public static final String implementedTransformURI = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
/*    */ 
/*    */   protected String engineGetURI()
/*    */   {
/* 46 */     return "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*    */     throws CanonicalizationException
/*    */   {
/* 53 */     return enginePerformTransform(paramXMLSignatureInput, null, paramTransform);
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream, Transform paramTransform)
/*    */     throws CanonicalizationException
/*    */   {
/* 60 */     Canonicalizer20010315WithComments localCanonicalizer20010315WithComments = new Canonicalizer20010315WithComments();
/* 61 */     if (paramOutputStream != null) {
/* 62 */       localCanonicalizer20010315WithComments.setWriter(paramOutputStream);
/*    */     }
/*    */ 
/* 65 */     byte[] arrayOfByte = null;
/* 66 */     arrayOfByte = localCanonicalizer20010315WithComments.engineCanonicalize(paramXMLSignatureInput);
/* 67 */     XMLSignatureInput localXMLSignatureInput = new XMLSignatureInput(arrayOfByte);
/* 68 */     if (paramOutputStream != null) {
/* 69 */       localXMLSignatureInput.setOutputStream(paramOutputStream);
/*    */     }
/* 71 */     return localXMLSignatureInput;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14NWithComments
 * JD-Core Version:    0.6.2
 */