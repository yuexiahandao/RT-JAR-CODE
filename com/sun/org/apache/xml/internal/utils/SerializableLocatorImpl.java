/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class SerializableLocatorImpl
/*     */   implements Locator, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -2660312888446371460L;
/*     */   private String publicId;
/*     */   private String systemId;
/*     */   private int lineNumber;
/*     */   private int columnNumber;
/*     */ 
/*     */   public SerializableLocatorImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SerializableLocatorImpl(Locator locator)
/*     */   {
/*  76 */     setPublicId(locator.getPublicId());
/*  77 */     setSystemId(locator.getSystemId());
/*  78 */     setLineNumber(locator.getLineNumber());
/*  79 */     setColumnNumber(locator.getColumnNumber());
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/*  98 */     return this.publicId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 112 */     return this.systemId;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 125 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 138 */     return this.columnNumber;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String publicId)
/*     */   {
/* 156 */     this.publicId = publicId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 169 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public void setLineNumber(int lineNumber)
/*     */   {
/* 181 */     this.lineNumber = lineNumber;
/*     */   }
/*     */ 
/*     */   public void setColumnNumber(int columnNumber)
/*     */   {
/* 193 */     this.columnNumber = columnNumber;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.SerializableLocatorImpl
 * JD-Core Version:    0.6.2
 */