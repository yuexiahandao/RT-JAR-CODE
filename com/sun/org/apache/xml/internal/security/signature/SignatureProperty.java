/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class SignatureProperty extends SignatureElementProxy
/*     */ {
/*     */   public SignatureProperty(Document paramDocument, String paramString)
/*     */   {
/*  47 */     this(paramDocument, paramString, null);
/*     */   }
/*     */ 
/*     */   public SignatureProperty(Document paramDocument, String paramString1, String paramString2)
/*     */   {
/*  59 */     super(paramDocument);
/*     */ 
/*  61 */     setTarget(paramString1);
/*  62 */     setId(paramString2);
/*     */   }
/*     */ 
/*     */   public SignatureProperty(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  73 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public void setId(String paramString)
/*     */   {
/*  83 */     if (paramString != null)
/*  84 */       setLocalIdAttribute("Id", paramString);
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/*  94 */     return this._constructionElement.getAttributeNS(null, "Id");
/*     */   }
/*     */ 
/*     */   public void setTarget(String paramString)
/*     */   {
/* 104 */     if (paramString != null)
/* 105 */       this._constructionElement.setAttributeNS(null, "Target", paramString);
/*     */   }
/*     */ 
/*     */   public String getTarget()
/*     */   {
/* 115 */     return this._constructionElement.getAttributeNS(null, "Target");
/*     */   }
/*     */ 
/*     */   public Node appendChild(Node paramNode)
/*     */   {
/* 125 */     return this._constructionElement.appendChild(paramNode);
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 130 */     return "SignatureProperty";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.SignatureProperty
 * JD-Core Version:    0.6.2
 */