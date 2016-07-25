/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.XPathFilter2ParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.XPathType;
/*     */ import javax.xml.crypto.dsig.spec.XPathType.Filter;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMXPathFilter2Transform extends ApacheTransform
/*     */ {
/*     */   public void init(TransformParameterSpec paramTransformParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  61 */     if (paramTransformParameterSpec == null)
/*  62 */       throw new InvalidAlgorithmParameterException("params are required");
/*  63 */     if (!(paramTransformParameterSpec instanceof XPathFilter2ParameterSpec)) {
/*  64 */       throw new InvalidAlgorithmParameterException("params must be of type XPathFilter2ParameterSpec");
/*     */     }
/*     */ 
/*  67 */     this.params = paramTransformParameterSpec;
/*     */   }
/*     */ 
/*     */   public void init(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  73 */     super.init(paramXMLStructure, paramXMLCryptoContext);
/*     */     try {
/*  75 */       unmarshalParams(DOMUtils.getFirstChildElement(this.transformElem));
/*     */     } catch (MarshalException localMarshalException) {
/*  77 */       throw ((InvalidAlgorithmParameterException)new InvalidAlgorithmParameterException().initCause(localMarshalException));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void unmarshalParams(Element paramElement) throws MarshalException
/*     */   {
/*  83 */     ArrayList localArrayList = new ArrayList();
/*  84 */     while (paramElement != null) {
/*  85 */       String str1 = paramElement.getFirstChild().getNodeValue();
/*  86 */       String str2 = DOMUtils.getAttributeValue(paramElement, "Filter");
/*     */ 
/*  88 */       if (str2 == null) {
/*  89 */         throw new MarshalException("filter cannot be null");
/*     */       }
/*  91 */       XPathType.Filter localFilter = null;
/*  92 */       if (str2.equals("intersect"))
/*  93 */         localFilter = XPathType.Filter.INTERSECT;
/*  94 */       else if (str2.equals("subtract"))
/*  95 */         localFilter = XPathType.Filter.SUBTRACT;
/*  96 */       else if (str2.equals("union"))
/*  97 */         localFilter = XPathType.Filter.UNION;
/*     */       else {
/*  99 */         throw new MarshalException("Unknown XPathType filter type" + str2);
/*     */       }
/*     */ 
/* 102 */       NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/* 103 */       if (localNamedNodeMap != null) {
/* 104 */         int i = localNamedNodeMap.getLength();
/* 105 */         HashMap localHashMap = new HashMap(i);
/* 106 */         for (int j = 0; j < i; j++) {
/* 107 */           Attr localAttr = (Attr)localNamedNodeMap.item(j);
/* 108 */           String str3 = localAttr.getPrefix();
/* 109 */           if ((str3 != null) && (str3.equals("xmlns"))) {
/* 110 */             localHashMap.put(localAttr.getLocalName(), localAttr.getValue());
/*     */           }
/*     */         }
/* 113 */         localArrayList.add(new XPathType(str1, localFilter, localHashMap));
/*     */       } else {
/* 115 */         localArrayList.add(new XPathType(str1, localFilter));
/*     */       }
/*     */ 
/* 118 */       paramElement = DOMUtils.getNextSiblingElement(paramElement);
/*     */     }
/* 120 */     this.params = new XPathFilter2ParameterSpec(localArrayList);
/*     */   }
/*     */ 
/*     */   public void marshalParams(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws MarshalException
/*     */   {
/* 126 */     super.marshalParams(paramXMLStructure, paramXMLCryptoContext);
/* 127 */     XPathFilter2ParameterSpec localXPathFilter2ParameterSpec = (XPathFilter2ParameterSpec)getParameterSpec();
/*     */ 
/* 129 */     String str1 = DOMUtils.getNSPrefix(paramXMLCryptoContext, "http://www.w3.org/2002/06/xmldsig-filter2");
/* 130 */     String str2 = "xmlns:" + str1;
/*     */ 
/* 132 */     List localList = localXPathFilter2ParameterSpec.getXPathList();
/* 133 */     int i = 0; for (int j = localList.size(); i < j; i++) {
/* 134 */       XPathType localXPathType = (XPathType)localList.get(i);
/* 135 */       Element localElement = DOMUtils.createElement(this.ownerDoc, "XPath", "http://www.w3.org/2002/06/xmldsig-filter2", str1);
/*     */ 
/* 137 */       localElement.appendChild(this.ownerDoc.createTextNode(localXPathType.getExpression()));
/*     */ 
/* 139 */       DOMUtils.setAttribute(localElement, "Filter", localXPathType.getFilter().toString());
/*     */ 
/* 141 */       localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", str2, "http://www.w3.org/2002/06/xmldsig-filter2");
/*     */ 
/* 145 */       Iterator localIterator = localXPathType.getNamespaceMap().entrySet().iterator();
/* 146 */       while (localIterator.hasNext()) {
/* 147 */         Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 148 */         localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + (String)localEntry.getKey(), (String)localEntry.getValue());
/*     */       }
/*     */ 
/* 152 */       this.transformElem.appendChild(localElement);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMXPathFilter2Transform
 * JD-Core Version:    0.6.2
 */