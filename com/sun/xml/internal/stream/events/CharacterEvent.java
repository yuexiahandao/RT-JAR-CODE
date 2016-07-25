/*     */ package com.sun.xml.internal.stream.events;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.stream.events.Characters;
/*     */ 
/*     */ public class CharacterEvent extends DummyEvent
/*     */   implements Characters
/*     */ {
/*     */   private String fData;
/*     */   private boolean fIsCData;
/*     */   private boolean fIsIgnorableWhitespace;
/*  50 */   private boolean fIsSpace = false;
/*     */ 
/*  52 */   private boolean fCheckIfSpaceNeeded = true;
/*     */ 
/*     */   public CharacterEvent() {
/*  55 */     this.fIsCData = false;
/*  56 */     init();
/*     */   }
/*     */ 
/*     */   public CharacterEvent(String data)
/*     */   {
/*  64 */     this.fIsCData = false;
/*  65 */     init();
/*  66 */     this.fData = data;
/*     */   }
/*     */ 
/*     */   public CharacterEvent(String data, boolean flag)
/*     */   {
/*  75 */     init();
/*  76 */     this.fData = data;
/*  77 */     this.fIsCData = flag;
/*     */   }
/*     */ 
/*     */   public CharacterEvent(String data, boolean flag, boolean isIgnorableWhiteSpace)
/*     */   {
/*  87 */     init();
/*  88 */     this.fData = data;
/*  89 */     this.fIsCData = flag;
/*  90 */     this.fIsIgnorableWhitespace = isIgnorableWhiteSpace;
/*     */   }
/*     */ 
/*     */   protected void init() {
/*  94 */     setEventType(4);
/*     */   }
/*     */ 
/*     */   public String getData()
/*     */   {
/* 102 */     return this.fData;
/*     */   }
/*     */ 
/*     */   public void setData(String data)
/*     */   {
/* 110 */     this.fData = data;
/* 111 */     this.fCheckIfSpaceNeeded = true;
/*     */   }
/*     */ 
/*     */   public boolean isCData()
/*     */   {
/* 119 */     return this.fIsCData;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 127 */     if (this.fIsCData) {
/* 128 */       return "<![CDATA[" + getData() + "]]>";
/*     */     }
/* 130 */     return this.fData;
/*     */   }
/*     */ 
/*     */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*     */     throws IOException
/*     */   {
/* 150 */     if (this.fIsCData)
/* 151 */       writer.write("<![CDATA[" + getData() + "]]>");
/*     */     else
/* 153 */       charEncode(writer, this.fData);
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableWhiteSpace()
/*     */   {
/* 164 */     return this.fIsIgnorableWhitespace;
/*     */   }
/*     */ 
/*     */   public boolean isWhiteSpace()
/*     */   {
/* 177 */     if (this.fCheckIfSpaceNeeded) {
/* 178 */       checkWhiteSpace();
/* 179 */       this.fCheckIfSpaceNeeded = false;
/*     */     }
/* 181 */     return this.fIsSpace;
/*     */   }
/*     */ 
/*     */   private void checkWhiteSpace()
/*     */   {
/* 186 */     if ((this.fData != null) && (this.fData.length() > 0)) {
/* 187 */       this.fIsSpace = true;
/* 188 */       for (int i = 0; i < this.fData.length(); i++)
/* 189 */         if (!XMLChar.isSpace(this.fData.charAt(i))) {
/* 190 */           this.fIsSpace = false;
/* 191 */           break;
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.CharacterEvent
 * JD-Core Version:    0.6.2
 */