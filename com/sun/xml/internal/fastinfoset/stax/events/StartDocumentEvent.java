/*     */ package com.sun.xml.internal.fastinfoset.stax.events;
/*     */ 
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ 
/*     */ public class StartDocumentEvent extends EventBase
/*     */   implements StartDocument
/*     */ {
/*     */   protected String _systemId;
/*  37 */   protected String _encoding = "UTF-8";
/*  38 */   protected boolean _standalone = true;
/*  39 */   protected String _version = "1.0";
/*  40 */   private boolean _encodingSet = false;
/*  41 */   private boolean _standaloneSet = false;
/*     */ 
/*     */   public void reset() {
/*  44 */     this._encoding = "UTF-8";
/*  45 */     this._standalone = true;
/*  46 */     this._version = "1.0";
/*  47 */     this._encodingSet = false;
/*  48 */     this._standaloneSet = false;
/*     */   }
/*     */   public StartDocumentEvent() {
/*  51 */     this(null, null);
/*     */   }
/*     */ 
/*     */   public StartDocumentEvent(String encoding) {
/*  55 */     this(encoding, null);
/*     */   }
/*     */ 
/*     */   public StartDocumentEvent(String encoding, String version) {
/*  59 */     if (encoding != null) {
/*  60 */       this._encoding = encoding;
/*  61 */       this._encodingSet = true;
/*     */     }
/*  63 */     if (version != null)
/*  64 */       this._version = version;
/*  65 */     setEventType(7);
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/*  75 */     return super.getSystemId();
/*     */   }
/*     */ 
/*     */   public String getCharacterEncodingScheme()
/*     */   {
/*  83 */     return this._encoding;
/*     */   }
/*     */ 
/*     */   public boolean encodingSet()
/*     */   {
/*  90 */     return this._encodingSet;
/*     */   }
/*     */ 
/*     */   public boolean isStandalone()
/*     */   {
/*  99 */     return this._standalone;
/*     */   }
/*     */ 
/*     */   public boolean standaloneSet()
/*     */   {
/* 106 */     return this._standaloneSet;
/*     */   }
/*     */ 
/*     */   public String getVersion()
/*     */   {
/* 114 */     return this._version;
/*     */   }
/*     */ 
/*     */   public void setStandalone(boolean standalone)
/*     */   {
/* 119 */     this._standaloneSet = true;
/* 120 */     this._standalone = standalone;
/*     */   }
/*     */ 
/*     */   public void setStandalone(String s) {
/* 124 */     this._standaloneSet = true;
/* 125 */     if (s == null) {
/* 126 */       this._standalone = true;
/* 127 */       return;
/*     */     }
/* 129 */     if (s.equals("yes"))
/* 130 */       this._standalone = true;
/*     */     else
/* 132 */       this._standalone = false;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 137 */     this._encoding = encoding;
/* 138 */     this._encodingSet = true;
/*     */   }
/*     */ 
/*     */   void setDeclaredEncoding(boolean value) {
/* 142 */     this._encodingSet = value;
/*     */   }
/*     */ 
/*     */   public void setVersion(String s) {
/* 146 */     this._version = s;
/*     */   }
/*     */ 
/*     */   void clear() {
/* 150 */     this._encoding = "UTF-8";
/* 151 */     this._standalone = true;
/* 152 */     this._version = "1.0";
/* 153 */     this._encodingSet = false;
/* 154 */     this._standaloneSet = false;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 158 */     String s = "<?xml version=\"" + this._version + "\"";
/* 159 */     s = s + " encoding='" + this._encoding + "'";
/* 160 */     if (this._standaloneSet) {
/* 161 */       if (this._standalone)
/* 162 */         s = s + " standalone='yes'?>";
/*     */       else
/* 164 */         s = s + " standalone='no'?>";
/*     */     }
/* 166 */     else s = s + "?>";
/*     */ 
/* 168 */     return s;
/*     */   }
/*     */ 
/*     */   public boolean isStartDocument() {
/* 172 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.StartDocumentEvent
 * JD-Core Version:    0.6.2
 */