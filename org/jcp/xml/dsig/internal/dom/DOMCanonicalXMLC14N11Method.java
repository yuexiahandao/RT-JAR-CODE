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
/*    */ public final class DOMCanonicalXMLC14N11Method extends ApacheCanonicalizer
/*    */ {
/*    */   public static final String C14N_11 = "http://www.w3.org/2006/12/xml-c14n11";
/*    */   public static final String C14N_11_WITH_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11#WithComments";
/*    */ 
/*    */   public void init(TransformParameterSpec paramTransformParameterSpec)
/*    */     throws InvalidAlgorithmParameterException
/*    */   {
/* 52 */     if (paramTransformParameterSpec != null)
/* 53 */       throw new InvalidAlgorithmParameterException("no parameters should be specified for Canonical XML 1.1 algorithm");
/*    */   }
/*    */ 
/*    */   public Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext)
/*    */     throws TransformException
/*    */   {
/* 64 */     if ((paramData instanceof DOMSubTreeData)) {
/* 65 */       DOMSubTreeData localDOMSubTreeData = (DOMSubTreeData)paramData;
/* 66 */       if (localDOMSubTreeData.excludeComments()) {
/*    */         try {
/* 68 */           this.apacheCanonicalizer = Canonicalizer.getInstance("http://www.w3.org/2006/12/xml-c14n11");
/*    */         } catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 70 */           throw new TransformException("Couldn't find Canonicalizer for: http://www.w3.org/2006/12/xml-c14n11: " + localInvalidCanonicalizerException.getMessage(), localInvalidCanonicalizerException);
/*    */         }
/*    */ 
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 77 */     return canonicalize(paramData, paramXMLCryptoContext);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMCanonicalXMLC14N11Method
 * JD-Core Version:    0.6.2
 */