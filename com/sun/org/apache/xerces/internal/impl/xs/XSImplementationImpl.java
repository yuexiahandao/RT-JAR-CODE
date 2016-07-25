/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.CoreDOMImplementationImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSException;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSImplementation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSLoader;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ 
/*     */ public class XSImplementationImpl extends CoreDOMImplementationImpl
/*     */   implements XSImplementation
/*     */ {
/*  52 */   static XSImplementationImpl singleton = new XSImplementationImpl();
/*     */ 
/*     */   public static DOMImplementation getDOMImplementation()
/*     */   {
/*  60 */     return singleton;
/*     */   }
/*     */ 
/*     */   public boolean hasFeature(String feature, String version)
/*     */   {
/*  84 */     return ((feature.equalsIgnoreCase("XS-Loader")) && ((version == null) || (version.equals("1.0")))) || (super.hasFeature(feature, version));
/*     */   }
/*     */ 
/*     */   public XSLoader createXSLoader(StringList versions)
/*     */     throws XSException
/*     */   {
/*  94 */     XSLoader loader = new XSLoaderImpl();
/*  95 */     if (versions == null) {
/*  96 */       return loader;
/*     */     }
/*  98 */     for (int i = 0; i < versions.getLength(); i++) {
/*  99 */       if (!versions.item(i).equals("1.0")) {
/* 100 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { versions.item(i) });
/*     */ 
/* 105 */         throw new XSException((short)1, msg);
/*     */       }
/*     */     }
/* 108 */     return loader;
/*     */   }
/*     */ 
/*     */   public StringList getRecognizedVersions()
/*     */   {
/* 115 */     StringListImpl list = new StringListImpl(new String[] { "1.0" }, 1);
/* 116 */     return list;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSImplementationImpl
 * JD-Core Version:    0.6.2
 */