/*     */ package org.xml.sax.helpers;
/*     */ 
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class LocatorImpl
/*     */   implements Locator
/*     */ {
/*     */   private String publicId;
/*     */   private String systemId;
/*     */   private int lineNumber;
/*     */   private int columnNumber;
/*     */ 
/*     */   public LocatorImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public LocatorImpl(Locator locator)
/*     */   {
/* 103 */     setPublicId(locator.getPublicId());
/* 104 */     setSystemId(locator.getSystemId());
/* 105 */     setLineNumber(locator.getLineNumber());
/* 106 */     setColumnNumber(locator.getColumnNumber());
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 126 */     return this.publicId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 140 */     return this.systemId;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 153 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 166 */     return this.columnNumber;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String publicId)
/*     */   {
/* 185 */     this.publicId = publicId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 198 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public void setLineNumber(int lineNumber)
/*     */   {
/* 210 */     this.lineNumber = lineNumber;
/*     */   }
/*     */ 
/*     */   public void setColumnNumber(int columnNumber)
/*     */   {
/* 222 */     this.columnNumber = columnNumber;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.helpers.LocatorImpl
 * JD-Core Version:    0.6.2
 */