/*     */ package com.sun.xml.internal.txw2;
/*     */ 
/*     */ import com.sun.xml.internal.txw2.annotation.XmlElement;
/*     */ import com.sun.xml.internal.txw2.annotation.XmlNamespace;
/*     */ import com.sun.xml.internal.txw2.output.TXWSerializer;
/*     */ import com.sun.xml.internal.txw2.output.XmlSerializer;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public abstract class TXW
/*     */ {
/*     */   static QName getTagName(Class<?> c)
/*     */   {
/*  45 */     String localName = "";
/*  46 */     String nsUri = "##default";
/*     */ 
/*  48 */     XmlElement xe = (XmlElement)c.getAnnotation(XmlElement.class);
/*  49 */     if (xe != null) {
/*  50 */       localName = xe.value();
/*  51 */       nsUri = xe.ns();
/*     */     }
/*     */ 
/*  54 */     if (localName.length() == 0) {
/*  55 */       localName = c.getName();
/*  56 */       int idx = localName.lastIndexOf('.');
/*  57 */       if (idx >= 0) {
/*  58 */         localName = localName.substring(idx + 1);
/*     */       }
/*  60 */       localName = Character.toLowerCase(localName.charAt(0)) + localName.substring(1);
/*     */     }
/*     */ 
/*  63 */     if (nsUri.equals("##default")) {
/*  64 */       Package pkg = c.getPackage();
/*  65 */       if (pkg != null) {
/*  66 */         XmlNamespace xn = (XmlNamespace)pkg.getAnnotation(XmlNamespace.class);
/*  67 */         if (xn != null)
/*  68 */           nsUri = xn.value();
/*     */       }
/*     */     }
/*  71 */     if (nsUri.equals("##default")) {
/*  72 */       nsUri = "";
/*     */     }
/*  74 */     return new QName(nsUri, localName);
/*     */   }
/*     */ 
/*     */   public static <T extends TypedXmlWriter> T create(Class<T> rootElement, XmlSerializer out)
/*     */   {
/*  88 */     if ((out instanceof TXWSerializer)) {
/*  89 */       TXWSerializer txws = (TXWSerializer)out;
/*  90 */       return txws.txw._element(rootElement);
/*     */     }
/*     */ 
/*  93 */     Document doc = new Document(out);
/*  94 */     QName n = getTagName(rootElement);
/*  95 */     return new ContainerElement(doc, null, n.getNamespaceURI(), n.getLocalPart())._cast(rootElement);
/*     */   }
/*     */ 
/*     */   public static <T extends TypedXmlWriter> T create(QName tagName, Class<T> rootElement, XmlSerializer out)
/*     */   {
/* 111 */     if ((out instanceof TXWSerializer)) {
/* 112 */       TXWSerializer txws = (TXWSerializer)out;
/* 113 */       return txws.txw._element(tagName, rootElement);
/*     */     }
/* 115 */     return new ContainerElement(new Document(out), null, tagName.getNamespaceURI(), tagName.getLocalPart())._cast(rootElement);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.TXW
 * JD-Core Version:    0.6.2
 */