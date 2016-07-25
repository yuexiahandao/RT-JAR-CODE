/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dsig.TransformException;
/*     */ import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public final class DOMExcC14NMethod extends ApacheCanonicalizer
/*     */ {
/*     */   public void init(TransformParameterSpec paramTransformParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  54 */     if (paramTransformParameterSpec != null) {
/*  55 */       if (!(paramTransformParameterSpec instanceof ExcC14NParameterSpec)) {
/*  56 */         throw new InvalidAlgorithmParameterException("params must be of type ExcC14NParameterSpec");
/*     */       }
/*     */ 
/*  59 */       this.params = ((C14NMethodParameterSpec)paramTransformParameterSpec);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void init(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext) throws InvalidAlgorithmParameterException
/*     */   {
/*  65 */     super.init(paramXMLStructure, paramXMLCryptoContext);
/*  66 */     Element localElement = DOMUtils.getFirstChildElement(this.transformElem);
/*  67 */     if (localElement == null) {
/*  68 */       this.params = null;
/*  69 */       this.inclusiveNamespaces = null;
/*  70 */       return;
/*     */     }
/*  72 */     unmarshalParams(localElement);
/*     */   }
/*     */ 
/*     */   private void unmarshalParams(Element paramElement) {
/*  76 */     String str = paramElement.getAttributeNS(null, "PrefixList");
/*  77 */     this.inclusiveNamespaces = str;
/*  78 */     int i = 0;
/*  79 */     int j = str.indexOf(' ');
/*  80 */     ArrayList localArrayList = new ArrayList();
/*  81 */     while (j != -1) {
/*  82 */       localArrayList.add(str.substring(i, j));
/*  83 */       i = j + 1;
/*  84 */       j = str.indexOf(' ', i);
/*     */     }
/*  86 */     if (i <= str.length()) {
/*  87 */       localArrayList.add(str.substring(i));
/*     */     }
/*  89 */     this.params = new ExcC14NParameterSpec(localArrayList);
/*     */   }
/*     */ 
/*     */   public void marshalParams(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws MarshalException
/*     */   {
/*  95 */     super.marshalParams(paramXMLStructure, paramXMLCryptoContext);
/*  96 */     AlgorithmParameterSpec localAlgorithmParameterSpec = getParameterSpec();
/*  97 */     if (localAlgorithmParameterSpec == null) {
/*  98 */       return;
/*     */     }
/*     */ 
/* 101 */     String str = DOMUtils.getNSPrefix(paramXMLCryptoContext, "http://www.w3.org/2001/10/xml-exc-c14n#");
/*     */ 
/* 103 */     Element localElement = DOMUtils.createElement(this.ownerDoc, "InclusiveNamespaces", "http://www.w3.org/2001/10/xml-exc-c14n#", str);
/*     */ 
/* 106 */     if ((str == null) || (str.length() == 0)) {
/* 107 */       localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2001/10/xml-exc-c14n#");
/*     */     }
/*     */     else {
/* 110 */       localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str, "http://www.w3.org/2001/10/xml-exc-c14n#");
/*     */     }
/*     */ 
/* 114 */     ExcC14NParameterSpec localExcC14NParameterSpec = (ExcC14NParameterSpec)localAlgorithmParameterSpec;
/* 115 */     StringBuffer localStringBuffer = new StringBuffer("");
/* 116 */     List localList = localExcC14NParameterSpec.getPrefixList();
/* 117 */     int i = 0; for (int j = localList.size(); i < j; i++) {
/* 118 */       localStringBuffer.append((String)localList.get(i));
/* 119 */       if (i < j - 1) {
/* 120 */         localStringBuffer.append(" ");
/*     */       }
/*     */     }
/* 123 */     DOMUtils.setAttribute(localElement, "PrefixList", localStringBuffer.toString());
/* 124 */     this.inclusiveNamespaces = localStringBuffer.toString();
/* 125 */     this.transformElem.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   public String getParamsNSURI() {
/* 129 */     return "http://www.w3.org/2001/10/xml-exc-c14n#";
/*     */   }
/*     */ 
/*     */   public Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws TransformException
/*     */   {
/* 138 */     if ((paramData instanceof DOMSubTreeData)) {
/* 139 */       DOMSubTreeData localDOMSubTreeData = (DOMSubTreeData)paramData;
/* 140 */       if (localDOMSubTreeData.excludeComments()) {
/*     */         try {
/* 142 */           this.apacheCanonicalizer = Canonicalizer.getInstance("http://www.w3.org/2001/10/xml-exc-c14n#");
/*     */         }
/*     */         catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 145 */           throw new TransformException("Couldn't find Canonicalizer for: http://www.w3.org/2001/10/xml-exc-c14n#: " + localInvalidCanonicalizerException.getMessage(), localInvalidCanonicalizerException);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 153 */     return canonicalize(paramData, paramXMLCryptoContext);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMExcC14NMethod
 * JD-Core Version:    0.6.2
 */