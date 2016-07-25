/*     */ package com.sun.xml.internal.fastinfoset.stax;
/*     */ 
/*     */ import javax.xml.stream.Location;
/*     */ 
/*     */ public class EventLocation
/*     */   implements Location
/*     */ {
/*  34 */   String _systemId = null;
/*  35 */   String _publicId = null;
/*  36 */   int _column = -1;
/*  37 */   int _line = -1;
/*  38 */   int _charOffset = -1;
/*     */ 
/*     */   public static Location getNilLocation()
/*     */   {
/*  45 */     return new EventLocation();
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/*  53 */     return this._line;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/*  61 */     return this._column;
/*     */   }
/*     */ 
/*     */   public int getCharacterOffset()
/*     */   {
/*  73 */     return this._charOffset;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/*  81 */     return this._publicId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/*  89 */     return this._systemId;
/*     */   }
/*     */ 
/*     */   public void setLineNumber(int line) {
/*  93 */     this._line = line;
/*     */   }
/*     */   public void setColumnNumber(int col) {
/*  96 */     this._column = col;
/*     */   }
/*     */   public void setCharacterOffset(int offset) {
/*  99 */     this._charOffset = offset;
/*     */   }
/*     */   public void setPublicId(String id) {
/* 102 */     this._publicId = id;
/*     */   }
/*     */   public void setSystemId(String id) {
/* 105 */     this._systemId = id;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 109 */     StringBuffer sbuffer = new StringBuffer();
/* 110 */     sbuffer.append("Line number = " + this._line);
/* 111 */     sbuffer.append("\n");
/* 112 */     sbuffer.append("Column number = " + this._column);
/* 113 */     sbuffer.append("\n");
/* 114 */     sbuffer.append("System Id = " + this._systemId);
/* 115 */     sbuffer.append("\n");
/* 116 */     sbuffer.append("Public Id = " + this._publicId);
/* 117 */     sbuffer.append("\n");
/* 118 */     sbuffer.append("CharacterOffset = " + this._charOffset);
/* 119 */     sbuffer.append("\n");
/* 120 */     return sbuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.EventLocation
 * JD-Core Version:    0.6.2
 */