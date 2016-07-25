/*    */ package org.jcp.xml.dsig.internal.dom;
/*    */ 
/*    */ import java.security.InvalidAlgorithmParameterException;
/*    */ import javax.xml.crypto.MarshalException;
/*    */ import javax.xml.crypto.XMLCryptoContext;
/*    */ import javax.xml.crypto.XMLStructure;
/*    */ import javax.xml.crypto.dom.DOMStructure;
/*    */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*    */ import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public final class DOMXSLTTransform extends ApacheTransform
/*    */ {
/*    */   public void init(TransformParameterSpec paramTransformParameterSpec)
/*    */     throws InvalidAlgorithmParameterException
/*    */   {
/* 48 */     if (paramTransformParameterSpec == null) {
/* 49 */       throw new InvalidAlgorithmParameterException("params are required");
/*    */     }
/* 51 */     if (!(paramTransformParameterSpec instanceof XSLTTransformParameterSpec)) {
/* 52 */       throw new InvalidAlgorithmParameterException("unrecognized params");
/*    */     }
/* 54 */     this.params = paramTransformParameterSpec;
/*    */   }
/*    */ 
/*    */   public void init(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext)
/*    */     throws InvalidAlgorithmParameterException
/*    */   {
/* 60 */     super.init(paramXMLStructure, paramXMLCryptoContext);
/* 61 */     unmarshalParams(DOMUtils.getFirstChildElement(this.transformElem));
/*    */   }
/*    */ 
/*    */   private void unmarshalParams(Element paramElement) {
/* 65 */     this.params = new XSLTTransformParameterSpec(new DOMStructure(paramElement));
/*    */   }
/*    */ 
/*    */   public void marshalParams(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext)
/*    */     throws MarshalException
/*    */   {
/* 71 */     super.marshalParams(paramXMLStructure, paramXMLCryptoContext);
/* 72 */     XSLTTransformParameterSpec localXSLTTransformParameterSpec = (XSLTTransformParameterSpec)getParameterSpec();
/*    */ 
/* 74 */     Node localNode = ((DOMStructure)localXSLTTransformParameterSpec.getStylesheet()).getNode();
/*    */ 
/* 76 */     DOMUtils.appendChild(this.transformElem, localNode);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMXSLTTransform
 * JD-Core Version:    0.6.2
 */