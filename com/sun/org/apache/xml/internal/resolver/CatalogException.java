/*     */ package com.sun.org.apache.xml.internal.resolver;
/*     */ 
/*     */ public class CatalogException extends Exception
/*     */ {
/*     */   public static final int WRAPPER = 1;
/*     */   public static final int INVALID_ENTRY = 2;
/*     */   public static final int INVALID_ENTRY_TYPE = 3;
/*     */   public static final int NO_XML_PARSER = 4;
/*     */   public static final int UNKNOWN_FORMAT = 5;
/*     */   public static final int UNPARSEABLE = 6;
/*     */   public static final int PARSE_FAILED = 7;
/*     */   public static final int UNENDED_COMMENT = 8;
/*  59 */   private Exception exception = null;
/*  60 */   private int exceptionType = 0;
/*     */ 
/*     */   public CatalogException(int type, String message)
/*     */   {
/*  69 */     super(message);
/*  70 */     this.exceptionType = type;
/*  71 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public CatalogException(int type)
/*     */   {
/*  80 */     super("Catalog Exception " + type);
/*  81 */     this.exceptionType = type;
/*  82 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public CatalogException(Exception e)
/*     */   {
/*  96 */     this.exceptionType = 1;
/*  97 */     this.exception = e;
/*     */   }
/*     */ 
/*     */   public CatalogException(String message, Exception e)
/*     */   {
/* 110 */     super(message);
/* 111 */     this.exceptionType = 1;
/* 112 */     this.exception = e;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 126 */     String message = super.getMessage();
/*     */ 
/* 128 */     if ((message == null) && (this.exception != null)) {
/* 129 */       return this.exception.getMessage();
/*     */     }
/* 131 */     return message;
/*     */   }
/*     */ 
/*     */   public Exception getException()
/*     */   {
/* 142 */     return this.exception;
/*     */   }
/*     */ 
/*     */   public int getExceptionType()
/*     */   {
/* 152 */     return this.exceptionType;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 162 */     if (this.exception != null) {
/* 163 */       return this.exception.toString();
/*     */     }
/* 165 */     return super.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.CatalogException
 * JD-Core Version:    0.6.2
 */