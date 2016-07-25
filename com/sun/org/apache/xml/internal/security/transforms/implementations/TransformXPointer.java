/*    */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
/*    */ 
/*    */ public class TransformXPointer extends TransformSpi
/*    */ {
/*    */   public static final String implementedTransformURI = "http://www.w3.org/TR/2001/WD-xptr-20010108";
/*    */ 
/*    */   protected String engineGetURI()
/*    */   {
/* 48 */     return "http://www.w3.org/TR/2001/WD-xptr-20010108";
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*    */     throws TransformationException
/*    */   {
/* 62 */     Object[] arrayOfObject = { "http://www.w3.org/TR/2001/WD-xptr-20010108" };
/*    */ 
/* 64 */     throw new TransformationException("signature.Transform.NotYetImplemented", arrayOfObject);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXPointer
 * JD-Core Version:    0.6.2
 */