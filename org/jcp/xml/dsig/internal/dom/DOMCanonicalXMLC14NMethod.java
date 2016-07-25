/*    */ package org.jcp.xml.dsig.internal.dom;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
/*    */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*    */ import java.security.InvalidAlgorithmParameterException;
/*    */ import javax.xml.crypto.Data;
/*    */ import javax.xml.crypto.XMLCryptoContext;
/*    */ import javax.xml.crypto.dsig.TransformException;
/*    */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*    */ 
/*    */ public final class DOMCanonicalXMLC14NMethod extends ApacheCanonicalizer
/*    */ {
/*    */   public void init(TransformParameterSpec paramTransformParameterSpec)
/*    */     throws InvalidAlgorithmParameterException
/*    */   {
/* 48 */     if (paramTransformParameterSpec != null)
/* 49 */       throw new InvalidAlgorithmParameterException("no parameters should be specified for Canonical XML C14N algorithm");
/*    */   }
/*    */ 
/*    */   public Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext)
/*    */     throws TransformException
/*    */   {
/* 60 */     if ((paramData instanceof DOMSubTreeData)) {
/* 61 */       DOMSubTreeData localDOMSubTreeData = (DOMSubTreeData)paramData;
/* 62 */       if (localDOMSubTreeData.excludeComments()) {
/*    */         try {
/* 64 */           this.apacheCanonicalizer = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
/*    */         }
/*    */         catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 67 */           throw new TransformException("Couldn't find Canonicalizer for: http://www.w3.org/TR/2001/REC-xml-c14n-20010315: " + localInvalidCanonicalizerException.getMessage(), localInvalidCanonicalizerException);
/*    */         }
/*    */ 
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 75 */     return canonicalize(paramData, paramXMLCryptoContext);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMCanonicalXMLC14NMethod
 * JD-Core Version:    0.6.2
 */