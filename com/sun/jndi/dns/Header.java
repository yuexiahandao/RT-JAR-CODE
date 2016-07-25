/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ class Header
/*     */ {
/*     */   static final int HEADER_SIZE = 12;
/*     */   static final short QR_BIT = -32768;
/*     */   static final short OPCODE_MASK = 30720;
/*     */   static final int OPCODE_SHIFT = 11;
/*     */   static final short AA_BIT = 1024;
/*     */   static final short TC_BIT = 512;
/*     */   static final short RD_BIT = 256;
/*     */   static final short RA_BIT = 128;
/*     */   static final short RCODE_MASK = 15;
/*     */   int xid;
/*     */   boolean query;
/*     */   int opcode;
/*     */   boolean authoritative;
/*     */   boolean truncated;
/*     */   boolean recursionDesired;
/*     */   boolean recursionAvail;
/*     */   int rcode;
/*     */   int numQuestions;
/*     */   int numAnswers;
/*     */   int numAuthorities;
/*     */   int numAdditionals;
/*     */ 
/*     */   Header(byte[] paramArrayOfByte, int paramInt)
/*     */     throws NamingException
/*     */   {
/*  71 */     decode(paramArrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   private void decode(byte[] paramArrayOfByte, int paramInt)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/*  81 */       int i = 0;
/*     */ 
/*  83 */       if (paramInt < 12) {
/*  84 */         throw new CommunicationException("DNS error: corrupted message header");
/*     */       }
/*     */ 
/*  88 */       this.xid = getShort(paramArrayOfByte, i);
/*  89 */       i += 2;
/*     */ 
/*  92 */       int j = (short)getShort(paramArrayOfByte, i);
/*  93 */       i += 2;
/*  94 */       this.query = ((j & 0xFFFF8000) == 0);
/*  95 */       this.opcode = ((j & 0x7800) >>> 11);
/*  96 */       this.authoritative = ((j & 0x400) != 0);
/*  97 */       this.truncated = ((j & 0x200) != 0);
/*  98 */       this.recursionDesired = ((j & 0x100) != 0);
/*  99 */       this.recursionAvail = ((j & 0x80) != 0);
/* 100 */       this.rcode = (j & 0xF);
/*     */ 
/* 103 */       this.numQuestions = getShort(paramArrayOfByte, i);
/* 104 */       i += 2;
/* 105 */       this.numAnswers = getShort(paramArrayOfByte, i);
/* 106 */       i += 2;
/* 107 */       this.numAuthorities = getShort(paramArrayOfByte, i);
/* 108 */       i += 2;
/* 109 */       this.numAdditionals = getShort(paramArrayOfByte, i);
/* 110 */       i += 2;
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 113 */       throw new CommunicationException("DNS error: corrupted message header");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int getShort(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 123 */     return (paramArrayOfByte[paramInt] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 1)] & 0xFF;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.Header
 * JD-Core Version:    0.6.2
 */