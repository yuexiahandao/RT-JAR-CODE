/*     */ package com.sun.org.apache.xml.internal.security.transforms;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class TransformSpi
/*     */ {
/*     */ 
/*     */   /** @deprecated */
/*  44 */   protected Transform _transformObject = null;
/*     */ 
/*     */   /** @deprecated */
/*     */   protected void setTransform(Transform paramTransform)
/*     */   {
/*  52 */     this._transformObject = paramTransform;
/*     */   }
/*     */ 
/*     */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream, Transform paramTransform)
/*     */     throws IOException, CanonicalizationException, InvalidCanonicalizerException, TransformationException, ParserConfigurationException, SAXException
/*     */   {
/*  74 */     return enginePerformTransform(paramXMLSignatureInput, paramTransform);
/*     */   }
/*     */ 
/*     */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*     */     throws IOException, CanonicalizationException, InvalidCanonicalizerException, TransformationException, ParserConfigurationException, SAXException
/*     */   {
/*     */     try
/*     */     {
/* 101 */       TransformSpi localTransformSpi = (TransformSpi)getClass().newInstance();
/* 102 */       localTransformSpi.setTransform(paramTransform);
/* 103 */       return localTransformSpi.enginePerformTransform(paramXMLSignatureInput);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 105 */       throw new TransformationException("", localInstantiationException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 107 */       throw new TransformationException("", localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
/*     */     throws IOException, CanonicalizationException, InvalidCanonicalizerException, TransformationException, ParserConfigurationException, SAXException
/*     */   {
/* 129 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   protected abstract String engineGetURI();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.TransformSpi
 * JD-Core Version:    0.6.2
 */