/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.Init;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.URIDereferencer;
/*     */ import javax.xml.crypto.URIReference;
/*     */ import javax.xml.crypto.URIReferenceException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dom.DOMURIReference;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DOMURIDereferencer
/*     */   implements URIDereferencer
/*     */ {
/*  49 */   static final URIDereferencer INSTANCE = new DOMURIDereferencer();
/*     */ 
/*     */   private DOMURIDereferencer()
/*     */   {
/*  54 */     Init.init();
/*     */   }
/*     */ 
/*     */   public Data dereference(URIReference paramURIReference, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws URIReferenceException
/*     */   {
/*  60 */     if (paramURIReference == null) {
/*  61 */       throw new NullPointerException("uriRef cannot be null");
/*     */     }
/*  63 */     if (paramXMLCryptoContext == null) {
/*  64 */       throw new NullPointerException("context cannot be null");
/*     */     }
/*     */ 
/*  67 */     DOMURIReference localDOMURIReference = (DOMURIReference)paramURIReference;
/*  68 */     Attr localAttr = (Attr)localDOMURIReference.getHere();
/*  69 */     String str1 = paramURIReference.getURI();
/*  70 */     DOMCryptoContext localDOMCryptoContext = (DOMCryptoContext)paramXMLCryptoContext;
/*  71 */     String str2 = paramXMLCryptoContext.getBaseURI();
/*     */ 
/*  73 */     boolean bool = Utils.secureValidation(paramXMLCryptoContext);
/*     */     Object localObject1;
/*     */     Object localObject2;
/*  76 */     if ((str1 != null) && (str1.length() != 0) && (str1.charAt(0) == '#')) {
/*  77 */       localObject1 = str1.substring(1);
/*     */ 
/*  79 */       if (((String)localObject1).startsWith("xpointer(id(")) {
/*  80 */         int i = ((String)localObject1).indexOf('\'');
/*  81 */         int j = ((String)localObject1).indexOf('\'', i + 1);
/*  82 */         localObject1 = ((String)localObject1).substring(i + 1, j);
/*     */       }
/*     */ 
/*  85 */       localObject2 = localDOMCryptoContext.getElementById((String)localObject1);
/*  86 */       if (localObject2 != null) {
/*  87 */         if (bool) {
/*  88 */           localObject3 = ((Node)localObject2).getOwnerDocument().getDocumentElement();
/*     */ 
/*  90 */           if (!XMLUtils.protectAgainstWrappingAttack((Node)localObject3, (Element)localObject2, (String)localObject1))
/*     */           {
/*  93 */             String str3 = "Multiple Elements with the same ID " + (String)localObject1 + " were detected";
/*     */ 
/*  95 */             throw new URIReferenceException(str3);
/*     */           }
/*     */         }
/*     */ 
/*  99 */         Object localObject3 = new XMLSignatureInput((Node)localObject2);
/* 100 */         if (!str1.substring(1).startsWith("xpointer(id(")) {
/* 101 */           ((XMLSignatureInput)localObject3).setExcludeComments(true);
/*     */         }
/*     */ 
/* 104 */         ((XMLSignatureInput)localObject3).setMIMEType("text/xml");
/* 105 */         if ((str2 != null) && (str2.length() > 0))
/* 106 */           ((XMLSignatureInput)localObject3).setSourceURI(str2.concat(localAttr.getNodeValue()));
/*     */         else {
/* 108 */           ((XMLSignatureInput)localObject3).setSourceURI(localAttr.getNodeValue());
/*     */         }
/* 110 */         return new ApacheNodeSetData((XMLSignatureInput)localObject3);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 115 */       localObject1 = ResourceResolver.getInstance(localAttr, str2, bool);
/*     */ 
/* 117 */       localObject2 = ((ResourceResolver)localObject1).resolve(localAttr, str2);
/* 118 */       if (((XMLSignatureInput)localObject2).isOctetStream()) {
/* 119 */         return new ApacheOctetStreamData((XMLSignatureInput)localObject2);
/*     */       }
/* 121 */       return new ApacheNodeSetData((XMLSignatureInput)localObject2);
/*     */     }
/*     */     catch (Exception localException) {
/* 124 */       throw new URIReferenceException(localException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMURIDereferencer
 * JD-Core Version:    0.6.2
 */