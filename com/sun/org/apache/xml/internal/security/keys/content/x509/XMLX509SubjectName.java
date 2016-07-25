/*     */ package com.sun.org.apache.xml.internal.security.keys.content.x509;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.RFC2253Parser;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.X509Certificate;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class XMLX509SubjectName extends SignatureElementProxy
/*     */   implements XMLX509DataContent
/*     */ {
/*     */   public XMLX509SubjectName(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  48 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public XMLX509SubjectName(Document paramDocument, String paramString)
/*     */   {
/*  59 */     super(paramDocument);
/*     */ 
/*  61 */     addText(paramString);
/*     */   }
/*     */ 
/*     */   public XMLX509SubjectName(Document paramDocument, X509Certificate paramX509Certificate)
/*     */   {
/*  71 */     this(paramDocument, RFC2253Parser.normalize(paramX509Certificate.getSubjectDN().getName()));
/*     */   }
/*     */ 
/*     */   public String getSubjectName()
/*     */   {
/*  82 */     return RFC2253Parser.normalize(getTextFromTextChild());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  87 */     if (paramObject == null) {
/*  88 */       return false;
/*     */     }
/*     */ 
/*  91 */     if (!getClass().getName().equals(paramObject.getClass().getName())) {
/*  92 */       return false;
/*     */     }
/*     */ 
/*  95 */     XMLX509SubjectName localXMLX509SubjectName = (XMLX509SubjectName)paramObject;
/*  96 */     String str1 = localXMLX509SubjectName.getSubjectName();
/*  97 */     String str2 = getSubjectName();
/*     */ 
/*  99 */     return str2.equals(str1);
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 104 */     return "X509SubjectName";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SubjectName
 * JD-Core Version:    0.6.2
 */