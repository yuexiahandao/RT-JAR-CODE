/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMXPathTransform extends ApacheTransform
/*     */ {
/*     */   public void init(TransformParameterSpec paramTransformParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  51 */     if (paramTransformParameterSpec == null)
/*  52 */       throw new InvalidAlgorithmParameterException("params are required");
/*  53 */     if (!(paramTransformParameterSpec instanceof XPathFilterParameterSpec)) {
/*  54 */       throw new InvalidAlgorithmParameterException("params must be of type XPathFilterParameterSpec");
/*     */     }
/*     */ 
/*  57 */     this.params = paramTransformParameterSpec;
/*     */   }
/*     */ 
/*     */   public void init(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  63 */     super.init(paramXMLStructure, paramXMLCryptoContext);
/*  64 */     unmarshalParams(DOMUtils.getFirstChildElement(this.transformElem));
/*     */   }
/*     */ 
/*     */   private void unmarshalParams(Element paramElement) {
/*  68 */     String str1 = paramElement.getFirstChild().getNodeValue();
/*     */ 
/*  70 */     NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/*  71 */     if (localNamedNodeMap != null) {
/*  72 */       int i = localNamedNodeMap.getLength();
/*  73 */       HashMap localHashMap = new HashMap(i);
/*  74 */       for (int j = 0; j < i; j++) {
/*  75 */         Attr localAttr = (Attr)localNamedNodeMap.item(j);
/*  76 */         String str2 = localAttr.getPrefix();
/*  77 */         if ((str2 != null) && (str2.equals("xmlns"))) {
/*  78 */           localHashMap.put(localAttr.getLocalName(), localAttr.getValue());
/*     */         }
/*     */       }
/*  81 */       this.params = new XPathFilterParameterSpec(str1, localHashMap);
/*     */     } else {
/*  83 */       this.params = new XPathFilterParameterSpec(str1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void marshalParams(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws MarshalException
/*     */   {
/*  90 */     super.marshalParams(paramXMLStructure, paramXMLCryptoContext);
/*  91 */     XPathFilterParameterSpec localXPathFilterParameterSpec = (XPathFilterParameterSpec)getParameterSpec();
/*     */ 
/*  93 */     Element localElement = DOMUtils.createElement(this.ownerDoc, "XPath", "http://www.w3.org/2000/09/xmldsig#", DOMUtils.getSignaturePrefix(paramXMLCryptoContext));
/*     */ 
/*  96 */     localElement.appendChild(this.ownerDoc.createTextNode(localXPathFilterParameterSpec.getXPath()));
/*     */ 
/*  99 */     Iterator localIterator = localXPathFilterParameterSpec.getNamespaceMap().entrySet().iterator();
/* 100 */     while (localIterator.hasNext()) {
/* 101 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 102 */       localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + (String)localEntry.getKey(), (String)localEntry.getValue());
/*     */     }
/*     */ 
/* 106 */     this.transformElem.appendChild(localElement);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMXPathTransform
 * JD-Core Version:    0.6.2
 */