/*     */ package com.sun.xml.internal.stream.events;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ 
/*     */ public class StartDocumentEvent extends DummyEvent
/*     */   implements StartDocument
/*     */ {
/*     */   protected String fSystemId;
/*     */   protected String fEncodingScheam;
/*     */   protected boolean fStandalone;
/*     */   protected String fVersion;
/*  46 */   private boolean fEncodingSchemeSet = false;
/*  47 */   private boolean fStandaloneSet = false;
/*  48 */   private boolean nestedCall = false;
/*     */ 
/*     */   public StartDocumentEvent() {
/*  51 */     init("UTF-8", "1.0", true, null);
/*     */   }
/*     */ 
/*     */   public StartDocumentEvent(String encoding) {
/*  55 */     init(encoding, "1.0", true, null);
/*     */   }
/*     */ 
/*     */   public StartDocumentEvent(String encoding, String version) {
/*  59 */     init(encoding, version, true, null);
/*     */   }
/*     */ 
/*     */   public StartDocumentEvent(String encoding, String version, boolean standalone) {
/*  63 */     this.fStandaloneSet = true;
/*  64 */     init(encoding, version, standalone, null);
/*     */   }
/*     */ 
/*     */   public StartDocumentEvent(String encoding, String version, boolean standalone, Location loc) {
/*  68 */     this.fStandaloneSet = true;
/*  69 */     init(encoding, version, standalone, loc);
/*     */   }
/*     */   protected void init(String encoding, String version, boolean standalone, Location loc) {
/*  72 */     setEventType(7);
/*  73 */     this.fEncodingScheam = encoding;
/*  74 */     this.fVersion = version;
/*  75 */     this.fStandalone = standalone;
/*  76 */     if ((encoding != null) && (!encoding.equals(""))) {
/*  77 */       this.fEncodingSchemeSet = true;
/*     */     } else {
/*  79 */       this.fEncodingSchemeSet = false;
/*  80 */       this.fEncodingScheam = "UTF-8";
/*     */     }
/*  82 */     this.fLocation = loc;
/*     */   }
/*     */ 
/*     */   public String getSystemId() {
/*  86 */     if (this.fLocation == null) {
/*  87 */       return "";
/*     */     }
/*  89 */     return this.fLocation.getSystemId();
/*     */   }
/*     */ 
/*     */   public String getCharacterEncodingScheme()
/*     */   {
/*  94 */     return this.fEncodingScheam;
/*     */   }
/*     */ 
/*     */   public boolean isStandalone() {
/*  98 */     return this.fStandalone;
/*     */   }
/*     */ 
/*     */   public String getVersion() {
/* 102 */     return this.fVersion;
/*     */   }
/*     */ 
/*     */   public void setStandalone(boolean flag) {
/* 106 */     this.fStandaloneSet = true;
/* 107 */     this.fStandalone = flag;
/*     */   }
/*     */ 
/*     */   public void setStandalone(String s) {
/* 111 */     this.fStandaloneSet = true;
/* 112 */     if (s == null) {
/* 113 */       this.fStandalone = true;
/* 114 */       return;
/*     */     }
/* 116 */     if (s.equals("yes"))
/* 117 */       this.fStandalone = true;
/*     */     else
/* 119 */       this.fStandalone = false;
/*     */   }
/*     */ 
/*     */   public boolean encodingSet() {
/* 123 */     return this.fEncodingSchemeSet;
/*     */   }
/*     */ 
/*     */   public boolean standaloneSet() {
/* 127 */     return this.fStandaloneSet;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding) {
/* 131 */     this.fEncodingScheam = encoding;
/*     */   }
/*     */ 
/*     */   void setDeclaredEncoding(boolean value) {
/* 135 */     this.fEncodingSchemeSet = value;
/*     */   }
/*     */ 
/*     */   public void setVersion(String s) {
/* 139 */     this.fVersion = s;
/*     */   }
/*     */ 
/*     */   void clear() {
/* 143 */     this.fEncodingScheam = "UTF-8";
/* 144 */     this.fStandalone = true;
/* 145 */     this.fVersion = "1.0";
/* 146 */     this.fEncodingSchemeSet = false;
/* 147 */     this.fStandaloneSet = false;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 151 */     String s = "<?xml version=\"" + this.fVersion + "\"";
/* 152 */     s = s + " encoding='" + this.fEncodingScheam + "'";
/* 153 */     if (this.fStandaloneSet) {
/* 154 */       if (this.fStandalone)
/* 155 */         s = s + " standalone='yes'?>";
/*     */       else
/* 157 */         s = s + " standalone='no'?>";
/*     */     }
/* 159 */     else s = s + "?>";
/*     */ 
/* 161 */     return s;
/*     */   }
/*     */ 
/*     */   public boolean isStartDocument() {
/* 165 */     return true;
/*     */   }
/*     */ 
/*     */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*     */     throws IOException
/*     */   {
/* 171 */     writer.write(toString());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.StartDocumentEvent
 * JD-Core Version:    0.6.2
 */