/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ abstract class MIMEEvent
/*     */ {
/*  56 */   static final StartMessage START_MESSAGE = new StartMessage();
/*  57 */   static final StartPart START_PART = new StartPart();
/*  58 */   static final EndPart END_PART = new EndPart();
/*  59 */   static final EndMessage END_MESSAGE = new EndMessage();
/*     */ 
/*     */   abstract EVENT_TYPE getEventType();
/*     */ 
/*     */   static final class Content extends MIMEEvent
/*     */   {
/*     */     private final ByteBuffer buf;
/*     */ 
/*     */     Content(ByteBuffer buf)
/*     */     {
/*  99 */       this.buf = buf;
/*     */     }
/*     */ 
/*     */     MIMEEvent.EVENT_TYPE getEventType() {
/* 103 */       return MIMEEvent.EVENT_TYPE.CONTENT;
/*     */     }
/*     */ 
/*     */     ByteBuffer getData() {
/* 107 */       return this.buf;
/*     */     }
/*     */   }
/*     */ 
/*     */   static enum EVENT_TYPE
/*     */   {
/*  35 */     START_MESSAGE, START_PART, HEADERS, CONTENT, END_PART, END_MESSAGE;
/*     */   }
/*     */ 
/*     */   static final class EndMessage extends MIMEEvent
/*     */   {
/*     */     MIMEEvent.EVENT_TYPE getEventType()
/*     */     {
/* 113 */       return MIMEEvent.EVENT_TYPE.END_MESSAGE;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class EndPart extends MIMEEvent
/*     */   {
/*     */     MIMEEvent.EVENT_TYPE getEventType()
/*     */     {
/*  75 */       return MIMEEvent.EVENT_TYPE.END_PART;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Headers extends MIMEEvent {
/*     */     InternetHeaders ih;
/*     */ 
/*     */     Headers(InternetHeaders ih) {
/*  83 */       this.ih = ih;
/*     */     }
/*     */ 
/*     */     MIMEEvent.EVENT_TYPE getEventType() {
/*  87 */       return MIMEEvent.EVENT_TYPE.HEADERS;
/*     */     }
/*     */ 
/*     */     InternetHeaders getHeaders() {
/*  91 */       return this.ih;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class StartMessage extends MIMEEvent
/*     */   {
/*     */     MIMEEvent.EVENT_TYPE getEventType()
/*     */     {
/*  63 */       return MIMEEvent.EVENT_TYPE.START_MESSAGE;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class StartPart extends MIMEEvent {
/*     */     MIMEEvent.EVENT_TYPE getEventType() {
/*  69 */       return MIMEEvent.EVENT_TYPE.START_PART;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.MIMEEvent
 * JD-Core Version:    0.6.2
 */