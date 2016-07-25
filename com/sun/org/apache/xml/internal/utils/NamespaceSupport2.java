/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.util.EmptyStackException;
/*     */ import java.util.Enumeration;
/*     */ import org.xml.sax.helpers.NamespaceSupport;
/*     */ 
/*     */ public class NamespaceSupport2 extends NamespaceSupport
/*     */ {
/*     */   private Context2 currentContext;
/*     */   public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
/*     */ 
/*     */   public NamespaceSupport2()
/*     */   {
/*  81 */     reset();
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 101 */     this.currentContext = new Context2(null);
/* 102 */     this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
/*     */   }
/*     */ 
/*     */   public void pushContext()
/*     */   {
/* 127 */     Context2 parentContext = this.currentContext;
/* 128 */     this.currentContext = parentContext.getChild();
/* 129 */     if (this.currentContext == null) {
/* 130 */       this.currentContext = new Context2(parentContext);
/*     */     }
/*     */     else
/*     */     {
/* 135 */       this.currentContext.setParent(parentContext);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void popContext()
/*     */   {
/* 155 */     Context2 parentContext = this.currentContext.getParent();
/* 156 */     if (parentContext == null) {
/* 157 */       throw new EmptyStackException();
/*     */     }
/* 159 */     this.currentContext = parentContext;
/*     */   }
/*     */ 
/*     */   public boolean declarePrefix(String prefix, String uri)
/*     */   {
/* 199 */     if ((prefix.equals("xml")) || (prefix.equals("xmlns"))) {
/* 200 */       return false;
/*     */     }
/* 202 */     this.currentContext.declarePrefix(prefix, uri);
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   public String[] processName(String qName, String[] parts, boolean isAttribute)
/*     */   {
/* 249 */     String[] name = this.currentContext.processName(qName, isAttribute);
/* 250 */     if (name == null) {
/* 251 */       return null;
/*     */     }
/*     */ 
/* 255 */     System.arraycopy(name, 0, parts, 0, 3);
/* 256 */     return parts;
/*     */   }
/*     */ 
/*     */   public String getURI(String prefix)
/*     */   {
/* 274 */     return this.currentContext.getURI(prefix);
/*     */   }
/*     */ 
/*     */   public Enumeration getPrefixes()
/*     */   {
/* 293 */     return this.currentContext.getPrefixes();
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri)
/*     */   {
/* 317 */     return this.currentContext.getPrefix(uri);
/*     */   }
/*     */ 
/*     */   public Enumeration getPrefixes(String uri)
/*     */   {
/* 353 */     return new PrefixForUriEnumerator(this, uri, getPrefixes());
/*     */   }
/*     */ 
/*     */   public Enumeration getDeclaredPrefixes()
/*     */   {
/* 371 */     return this.currentContext.getDeclaredPrefixes();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.NamespaceSupport2
 * JD-Core Version:    0.6.2
 */