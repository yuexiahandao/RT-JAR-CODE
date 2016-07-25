/*     */ package org.xml.sax.ext;
/*     */ 
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public class Locator2Impl extends LocatorImpl
/*     */   implements Locator2
/*     */ {
/*     */   private String encoding;
/*     */   private String version;
/*     */ 
/*     */   public Locator2Impl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Locator2Impl(Locator locator)
/*     */   {
/*  74 */     super(locator);
/*  75 */     if ((locator instanceof Locator2)) {
/*  76 */       Locator2 l2 = (Locator2)locator;
/*     */ 
/*  78 */       this.version = l2.getXMLVersion();
/*  79 */       this.encoding = l2.getEncoding();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getXMLVersion()
/*     */   {
/*  93 */     return this.version;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 101 */     return this.encoding;
/*     */   }
/*     */ 
/*     */   public void setXMLVersion(String version)
/*     */   {
/* 115 */     this.version = version;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 124 */     this.encoding = encoding;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.ext.Locator2Impl
 * JD-Core Version:    0.6.2
 */