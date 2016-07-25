/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ class ResourceRecords
/*     */ {
/*  48 */   Vector question = new Vector();
/*  49 */   Vector answer = new Vector();
/*  50 */   Vector authority = new Vector();
/*  51 */   Vector additional = new Vector();
/*     */   boolean zoneXfer;
/*     */ 
/*     */   ResourceRecords(byte[] paramArrayOfByte, int paramInt, Header paramHeader, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/*  68 */     if (paramBoolean) {
/*  69 */       this.answer.ensureCapacity(8192);
/*     */     }
/*  71 */     this.zoneXfer = paramBoolean;
/*  72 */     add(paramArrayOfByte, paramInt, paramHeader);
/*     */   }
/*     */ 
/*     */   int getFirstAnsType()
/*     */   {
/*  80 */     if (this.answer.size() == 0) {
/*  81 */       return -1;
/*     */     }
/*  83 */     return ((ResourceRecord)this.answer.firstElement()).getType();
/*     */   }
/*     */ 
/*     */   int getLastAnsType()
/*     */   {
/*  91 */     if (this.answer.size() == 0) {
/*  92 */       return -1;
/*     */     }
/*  94 */     return ((ResourceRecord)this.answer.lastElement()).getType();
/*     */   }
/*     */ 
/*     */   void add(byte[] paramArrayOfByte, int paramInt, Header paramHeader)
/*     */     throws NamingException
/*     */   {
/* 105 */     int i = 12;
/*     */     try
/*     */     {
/*     */       ResourceRecord localResourceRecord;
/* 108 */       for (int j = 0; j < paramHeader.numQuestions; j++) {
/* 109 */         localResourceRecord = new ResourceRecord(paramArrayOfByte, paramInt, i, true, false);
/* 110 */         if (!this.zoneXfer) {
/* 111 */           this.question.addElement(localResourceRecord);
/*     */         }
/* 113 */         i += localResourceRecord.size();
/*     */       }
/*     */ 
/* 116 */       for (j = 0; j < paramHeader.numAnswers; j++) {
/* 117 */         localResourceRecord = new ResourceRecord(paramArrayOfByte, paramInt, i, false, !this.zoneXfer);
/*     */ 
/* 119 */         this.answer.addElement(localResourceRecord);
/* 120 */         i += localResourceRecord.size();
/*     */       }
/*     */ 
/* 123 */       if (this.zoneXfer) {
/* 124 */         return;
/*     */       }
/*     */ 
/* 127 */       for (j = 0; j < paramHeader.numAuthorities; j++) {
/* 128 */         localResourceRecord = new ResourceRecord(paramArrayOfByte, paramInt, i, false, true);
/* 129 */         this.authority.addElement(localResourceRecord);
/* 130 */         i += localResourceRecord.size();
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
/*     */     {
/* 136 */       throw new CommunicationException("DNS error: corrupted message");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.ResourceRecords
 * JD-Core Version:    0.6.2
 */