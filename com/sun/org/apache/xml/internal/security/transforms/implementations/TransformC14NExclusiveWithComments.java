/*    */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*    */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315ExclWithComments;
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
/*    */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*    */ import java.io.OutputStream;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class TransformC14NExclusiveWithComments extends TransformSpi
/*    */ {
/*    */   public static final String implementedTransformURI = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
/*    */ 
/*    */   protected String engineGetURI()
/*    */   {
/* 54 */     return "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*    */     throws CanonicalizationException
/*    */   {
/* 63 */     return enginePerformTransform(paramXMLSignatureInput, null, paramTransform);
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream, Transform paramTransform) throws CanonicalizationException
/*    */   {
/*    */     try {
/* 69 */       String str = null;
/*    */ 
/* 71 */       if (paramTransform.length("http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces") == 1)
/*    */       {
/* 75 */         localObject = XMLUtils.selectNode(paramTransform.getElement().getFirstChild(), "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces", 0);
/*    */ 
/* 81 */         str = new InclusiveNamespaces((Element)localObject, paramTransform.getBaseURI()).getInclusiveNamespaces();
/*    */       }
/*    */ 
/* 85 */       Object localObject = new Canonicalizer20010315ExclWithComments();
/*    */ 
/* 87 */       if (paramOutputStream != null) {
/* 88 */         ((Canonicalizer20010315ExclWithComments)localObject).setWriter(paramOutputStream);
/*    */       }
/*    */ 
/* 91 */       byte[] arrayOfByte = ((Canonicalizer20010315ExclWithComments)localObject).engineCanonicalize(paramXMLSignatureInput, str);
/* 92 */       return new XMLSignatureInput(arrayOfByte);
/*    */     }
/*    */     catch (XMLSecurityException localXMLSecurityException)
/*    */     {
/* 96 */       throw new CanonicalizationException("empty", localXMLSecurityException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14NExclusiveWithComments
 * JD-Core Version:    0.6.2
 */