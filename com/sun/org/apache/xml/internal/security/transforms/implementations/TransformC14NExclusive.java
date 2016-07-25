/*    */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*    */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315ExclOmitComments;
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
/*    */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*    */ import java.io.OutputStream;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class TransformC14NExclusive extends TransformSpi
/*    */ {
/*    */   public static final String implementedTransformURI = "http://www.w3.org/2001/10/xml-exc-c14n#";
/*    */ 
/*    */   protected String engineGetURI()
/*    */   {
/* 54 */     return "http://www.w3.org/2001/10/xml-exc-c14n#";
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*    */     throws CanonicalizationException
/*    */   {
/* 67 */     return enginePerformTransform(paramXMLSignatureInput, null, paramTransform);
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream, Transform paramTransform) throws CanonicalizationException
/*    */   {
/*    */     try {
/* 73 */       String str = null;
/*    */ 
/* 75 */       if (paramTransform.length("http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces") == 1)
/*    */       {
/* 79 */         localObject = XMLUtils.selectNode(paramTransform.getElement().getFirstChild(), "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces", 0);
/*    */ 
/* 85 */         str = new InclusiveNamespaces((Element)localObject, paramTransform.getBaseURI()).getInclusiveNamespaces();
/*    */       }
/*    */ 
/* 89 */       Object localObject = new Canonicalizer20010315ExclOmitComments();
/*    */ 
/* 91 */       if (paramOutputStream != null) {
/* 92 */         ((Canonicalizer20010315ExclOmitComments)localObject).setWriter(paramOutputStream);
/*    */       }
/*    */ 
/* 95 */       byte[] arrayOfByte = ((Canonicalizer20010315ExclOmitComments)localObject).engineCanonicalize(paramXMLSignatureInput, str);
/*    */ 
/* 97 */       XMLSignatureInput localXMLSignatureInput = new XMLSignatureInput(arrayOfByte);
/* 98 */       if (paramOutputStream != null) {
/* 99 */         localXMLSignatureInput.setOutputStream(paramOutputStream);
/*    */       }
/* 101 */       return localXMLSignatureInput;
/*    */     } catch (XMLSecurityException localXMLSecurityException) {
/* 103 */       throw new CanonicalizationException("empty", localXMLSecurityException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14NExclusive
 * JD-Core Version:    0.6.2
 */