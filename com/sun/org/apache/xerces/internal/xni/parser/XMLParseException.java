/*     */ package com.sun.org.apache.xerces.internal.xni.parser;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ 
/*     */ public class XMLParseException extends XNIException
/*     */ {
/*     */   static final long serialVersionUID = 1732959359448549967L;
/*     */   protected String fPublicId;
/*     */   protected String fLiteralSystemId;
/*     */   protected String fExpandedSystemId;
/*     */   protected String fBaseSystemId;
/*  57 */   protected int fLineNumber = -1;
/*     */ 
/*  60 */   protected int fColumnNumber = -1;
/*     */ 
/*  63 */   protected int fCharacterOffset = -1;
/*     */ 
/*     */   public XMLParseException(XMLLocator locator, String message)
/*     */   {
/*  71 */     super(message);
/*  72 */     if (locator != null) {
/*  73 */       this.fPublicId = locator.getPublicId();
/*  74 */       this.fLiteralSystemId = locator.getLiteralSystemId();
/*  75 */       this.fExpandedSystemId = locator.getExpandedSystemId();
/*  76 */       this.fBaseSystemId = locator.getBaseSystemId();
/*  77 */       this.fLineNumber = locator.getLineNumber();
/*  78 */       this.fColumnNumber = locator.getColumnNumber();
/*  79 */       this.fCharacterOffset = locator.getCharacterOffset();
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLParseException(XMLLocator locator, String message, Exception exception)
/*     */   {
/*  86 */     super(message, exception);
/*  87 */     if (locator != null) {
/*  88 */       this.fPublicId = locator.getPublicId();
/*  89 */       this.fLiteralSystemId = locator.getLiteralSystemId();
/*  90 */       this.fExpandedSystemId = locator.getExpandedSystemId();
/*  91 */       this.fBaseSystemId = locator.getBaseSystemId();
/*  92 */       this.fLineNumber = locator.getLineNumber();
/*  93 */       this.fColumnNumber = locator.getColumnNumber();
/*  94 */       this.fCharacterOffset = locator.getCharacterOffset();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 104 */     return this.fPublicId;
/*     */   }
/*     */ 
/*     */   public String getExpandedSystemId()
/*     */   {
/* 109 */     return this.fExpandedSystemId;
/*     */   }
/*     */ 
/*     */   public String getLiteralSystemId()
/*     */   {
/* 114 */     return this.fLiteralSystemId;
/*     */   }
/*     */ 
/*     */   public String getBaseSystemId()
/*     */   {
/* 119 */     return this.fBaseSystemId;
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 124 */     return this.fLineNumber;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 129 */     return this.fColumnNumber;
/*     */   }
/*     */ 
/*     */   public int getCharacterOffset()
/*     */   {
/* 134 */     return this.fCharacterOffset;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 144 */     StringBuffer str = new StringBuffer();
/* 145 */     if (this.fPublicId != null) {
/* 146 */       str.append(this.fPublicId);
/*     */     }
/* 148 */     str.append(':');
/* 149 */     if (this.fLiteralSystemId != null) {
/* 150 */       str.append(this.fLiteralSystemId);
/*     */     }
/* 152 */     str.append(':');
/* 153 */     if (this.fExpandedSystemId != null) {
/* 154 */       str.append(this.fExpandedSystemId);
/*     */     }
/* 156 */     str.append(':');
/* 157 */     if (this.fBaseSystemId != null) {
/* 158 */       str.append(this.fBaseSystemId);
/*     */     }
/* 160 */     str.append(':');
/* 161 */     str.append(this.fLineNumber);
/* 162 */     str.append(':');
/* 163 */     str.append(this.fColumnNumber);
/* 164 */     str.append(':');
/* 165 */     str.append(this.fCharacterOffset);
/* 166 */     str.append(':');
/* 167 */     String message = getMessage();
/* 168 */     if (message == null) {
/* 169 */       Exception exception = getException();
/* 170 */       if (exception != null) {
/* 171 */         message = exception.getMessage();
/*     */       }
/*     */     }
/* 174 */     if (message != null) {
/* 175 */       str.append(message);
/*     */     }
/* 177 */     return str.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.parser.XMLParseException
 * JD-Core Version:    0.6.2
 */