/*     */ package com.sun.management.jmx;
/*     */ 
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationListener;
/*     */ 
/*     */ @Deprecated
/*     */ public class TraceListener
/*     */   implements NotificationListener
/*     */ {
/*     */   protected PrintStream out;
/*  47 */   protected boolean needTobeClosed = false;
/*  48 */   protected boolean formated = false;
/*     */ 
/*     */   public TraceListener()
/*     */   {
/*  59 */     this.out = System.out;
/*     */   }
/*     */ 
/*     */   public TraceListener(PrintStream paramPrintStream)
/*     */     throws IllegalArgumentException
/*     */   {
/*  72 */     if (paramPrintStream == null)
/*  73 */       throw new IllegalArgumentException("An PrintStream object should be specified.");
/*  74 */     this.out = paramPrintStream;
/*     */   }
/*     */ 
/*     */   public TraceListener(String paramString)
/*     */     throws IOException
/*     */   {
/*  86 */     this.out = new PrintStream(new FileOutputStream(paramString, true));
/*     */ 
/*  88 */     this.needTobeClosed = true;
/*     */   }
/*     */ 
/*     */   public void setFormated(boolean paramBoolean)
/*     */   {
/* 100 */     this.formated = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void handleNotification(Notification paramNotification, Object paramObject)
/*     */   {
/* 107 */     if ((paramNotification instanceof TraceNotification)) {
/* 108 */       TraceNotification localTraceNotification = (TraceNotification)paramNotification;
/*     */ 
/* 110 */       if (this.formated) {
/* 111 */         this.out.print("\nGlobal sequence number: " + localTraceNotification.globalSequenceNumber + "     Sequence number: " + localTraceNotification.sequenceNumber + "\n" + "Level: " + Trace.getLevel(localTraceNotification.level) + "     Type: " + Trace.getType(localTraceNotification.type) + "\n" + "Class  Name: " + new String(localTraceNotification.className) + "\n" + "Method Name: " + new String(localTraceNotification.methodName) + "\n");
/*     */ 
/* 116 */         if (localTraceNotification.exception != null) {
/* 117 */           localTraceNotification.exception.printStackTrace(this.out);
/* 118 */           this.out.println();
/*     */         }
/*     */ 
/* 121 */         if (localTraceNotification.info != null)
/* 122 */           this.out.println("Information: " + localTraceNotification.info);
/*     */       } else {
/* 124 */         this.out.print("(" + localTraceNotification.className + " " + localTraceNotification.methodName + ") ");
/*     */ 
/* 126 */         if (localTraceNotification.exception != null) {
/* 127 */           localTraceNotification.exception.printStackTrace(this.out);
/* 128 */           this.out.println();
/*     */         }
/*     */ 
/* 131 */         if (localTraceNotification.info != null)
/* 132 */           this.out.println(localTraceNotification.info);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFile(String paramString)
/*     */     throws IOException
/*     */   {
/* 145 */     PrintStream localPrintStream = new PrintStream(new FileOutputStream(paramString, true));
/*     */ 
/* 147 */     if (this.needTobeClosed) {
/* 148 */       this.out.close();
/*     */     }
/* 150 */     this.out = localPrintStream;
/* 151 */     this.needTobeClosed = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.jmx.TraceListener
 * JD-Core Version:    0.6.2
 */