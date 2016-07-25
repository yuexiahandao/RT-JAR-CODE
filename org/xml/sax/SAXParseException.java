/*     */ package org.xml.sax;
/*     */ 
/*     */ public class SAXParseException extends SAXException
/*     */ {
/*     */   private String publicId;
/*     */   private String systemId;
/*     */   private int lineNumber;
/*     */   private int columnNumber;
/*     */   static final long serialVersionUID = -5651165872476709336L;
/*     */ 
/*     */   public SAXParseException(String message, Locator locator)
/*     */   {
/*  83 */     super(message);
/*  84 */     if (locator != null) {
/*  85 */       init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
/*     */     }
/*     */     else
/*  88 */       init(null, null, -1, -1);
/*     */   }
/*     */ 
/*     */   public SAXParseException(String message, Locator locator, Exception e)
/*     */   {
/* 110 */     super(message, e);
/* 111 */     if (locator != null) {
/* 112 */       init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
/*     */     }
/*     */     else
/* 115 */       init(null, null, -1, -1);
/*     */   }
/*     */ 
/*     */   public SAXParseException(String message, String publicId, String systemId, int lineNumber, int columnNumber)
/*     */   {
/* 144 */     super(message);
/* 145 */     init(publicId, systemId, lineNumber, columnNumber);
/*     */   }
/*     */ 
/*     */   public SAXParseException(String message, String publicId, String systemId, int lineNumber, int columnNumber, Exception e)
/*     */   {
/* 176 */     super(message, e);
/* 177 */     init(publicId, systemId, lineNumber, columnNumber);
/*     */   }
/*     */ 
/*     */   private void init(String publicId, String systemId, int lineNumber, int columnNumber)
/*     */   {
/* 194 */     this.publicId = publicId;
/* 195 */     this.systemId = systemId;
/* 196 */     this.lineNumber = lineNumber;
/* 197 */     this.columnNumber = columnNumber;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 210 */     return this.publicId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 226 */     return this.systemId;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 241 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 256 */     return this.columnNumber;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 265 */     StringBuilder buf = new StringBuilder(getClass().getName());
/* 266 */     String message = getLocalizedMessage();
/* 267 */     if (this.publicId != null) buf.append("publicId: ").append(this.publicId);
/* 268 */     if (this.systemId != null) buf.append("; systemId: ").append(this.systemId);
/* 269 */     if (this.lineNumber != -1) buf.append("; lineNumber: ").append(this.lineNumber);
/* 270 */     if (this.columnNumber != -1) buf.append("; columnNumber: ").append(this.columnNumber);
/*     */ 
/* 273 */     if (message != null) buf.append("; ").append(message);
/* 274 */     return buf.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.SAXParseException
 * JD-Core Version:    0.6.2
 */