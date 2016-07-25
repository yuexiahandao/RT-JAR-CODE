/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public class SAXSourceLocator extends LocatorImpl
/*     */   implements SourceLocator, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 3181680946321164112L;
/*     */   Locator m_locator;
/*     */ 
/*     */   public SAXSourceLocator()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SAXSourceLocator(Locator locator)
/*     */   {
/*  61 */     this.m_locator = locator;
/*  62 */     setColumnNumber(locator.getColumnNumber());
/*  63 */     setLineNumber(locator.getLineNumber());
/*  64 */     setPublicId(locator.getPublicId());
/*  65 */     setSystemId(locator.getSystemId());
/*     */   }
/*     */ 
/*     */   public SAXSourceLocator(SourceLocator locator)
/*     */   {
/*  76 */     this.m_locator = null;
/*  77 */     setColumnNumber(locator.getColumnNumber());
/*  78 */     setLineNumber(locator.getLineNumber());
/*  79 */     setPublicId(locator.getPublicId());
/*  80 */     setSystemId(locator.getSystemId());
/*     */   }
/*     */ 
/*     */   public SAXSourceLocator(SAXParseException spe)
/*     */   {
/*  92 */     setLineNumber(spe.getLineNumber());
/*  93 */     setColumnNumber(spe.getColumnNumber());
/*  94 */     setPublicId(spe.getPublicId());
/*  95 */     setSystemId(spe.getSystemId());
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 111 */     return null == this.m_locator ? super.getPublicId() : this.m_locator.getPublicId();
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 130 */     return null == this.m_locator ? super.getSystemId() : this.m_locator.getSystemId();
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 150 */     return null == this.m_locator ? super.getLineNumber() : this.m_locator.getLineNumber();
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 170 */     return null == this.m_locator ? super.getColumnNumber() : this.m_locator.getColumnNumber();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.SAXSourceLocator
 * JD-Core Version:    0.6.2
 */