/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.Transform;
/*     */ import javax.xml.crypto.dsig.TransformException;
/*     */ import javax.xml.crypto.dsig.TransformService;
/*     */ import javax.xml.crypto.dsig.dom.DOMSignContext;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DOMTransform extends DOMStructure
/*     */   implements Transform
/*     */ {
/*     */   protected TransformService spi;
/*     */ 
/*     */   public DOMTransform(TransformService paramTransformService)
/*     */   {
/*  61 */     this.spi = paramTransformService;
/*     */   }
/*     */ 
/*     */   public DOMTransform(Element paramElement, XMLCryptoContext paramXMLCryptoContext, Provider paramProvider)
/*     */     throws MarshalException
/*     */   {
/*  73 */     String str = DOMUtils.getAttributeValue(paramElement, "Algorithm");
/*     */     try {
/*  75 */       this.spi = TransformService.getInstance(str, "DOM");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException1) {
/*     */       try {
/*  78 */         this.spi = TransformService.getInstance(str, "DOM", paramProvider);
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException2) {
/*  80 */         throw new MarshalException(localNoSuchAlgorithmException2);
/*     */       }
/*     */     }
/*     */     try {
/*  84 */       this.spi.init(new javax.xml.crypto.dom.DOMStructure(paramElement), paramXMLCryptoContext);
/*     */     } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/*  86 */       throw new MarshalException(localInvalidAlgorithmParameterException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final AlgorithmParameterSpec getParameterSpec() {
/*  91 */     return this.spi.getParameterSpec();
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm() {
/*  95 */     return this.spi.getAlgorithm();
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext)
/*     */     throws MarshalException
/*     */   {
/* 104 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 106 */     Element localElement = null;
/* 107 */     if (paramNode.getLocalName().equals("Transforms")) {
/* 108 */       localElement = DOMUtils.createElement(localDocument, "Transform", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */     }
/*     */     else {
/* 111 */       localElement = DOMUtils.createElement(localDocument, "CanonicalizationMethod", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */     }
/*     */ 
/* 114 */     DOMUtils.setAttribute(localElement, "Algorithm", getAlgorithm());
/*     */ 
/* 116 */     this.spi.marshalParams(new javax.xml.crypto.dom.DOMStructure(localElement), paramDOMCryptoContext);
/*     */ 
/* 119 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   public Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws TransformException
/*     */   {
/* 135 */     return this.spi.transform(paramData, paramXMLCryptoContext);
/*     */   }
/*     */ 
/*     */   public Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext, OutputStream paramOutputStream)
/*     */     throws TransformException
/*     */   {
/* 153 */     return this.spi.transform(paramData, paramXMLCryptoContext, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 157 */     if (this == paramObject) {
/* 158 */       return true;
/*     */     }
/*     */ 
/* 161 */     if (!(paramObject instanceof Transform)) {
/* 162 */       return false;
/*     */     }
/* 164 */     Transform localTransform = (Transform)paramObject;
/*     */ 
/* 166 */     return (getAlgorithm().equals(localTransform.getAlgorithm())) && (DOMUtils.paramsEqual(getParameterSpec(), localTransform.getParameterSpec()));
/*     */   }
/*     */ 
/*     */   Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext, DOMSignContext paramDOMSignContext)
/*     */     throws MarshalException, TransformException
/*     */   {
/* 189 */     marshal(paramDOMSignContext.getParent(), DOMUtils.getSignaturePrefix(paramDOMSignContext), paramDOMSignContext);
/*     */ 
/* 191 */     return transform(paramData, paramXMLCryptoContext);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMTransform
 * JD-Core Version:    0.6.2
 */