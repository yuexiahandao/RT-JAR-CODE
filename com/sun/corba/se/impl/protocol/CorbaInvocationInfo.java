/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
/*     */ import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class CorbaInvocationInfo
/*     */   implements ClientInvocationInfo
/*     */ {
/*     */   private boolean isRetryInvocation;
/*     */   private int entryCount;
/*     */   private ORB orb;
/*     */   private Iterator contactInfoListIterator;
/*     */   private ClientRequestDispatcher clientRequestDispatcher;
/*     */   private MessageMediator messageMediator;
/*     */ 
/*     */   private CorbaInvocationInfo()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CorbaInvocationInfo(ORB paramORB)
/*     */   {
/*  57 */     this.orb = paramORB;
/*  58 */     this.isRetryInvocation = false;
/*  59 */     this.entryCount = 0;
/*     */   }
/*     */ 
/*     */   public Iterator getContactInfoListIterator()
/*     */   {
/*  64 */     return this.contactInfoListIterator;
/*     */   }
/*     */ 
/*     */   public void setContactInfoListIterator(Iterator paramIterator)
/*     */   {
/*  69 */     this.contactInfoListIterator = paramIterator;
/*     */   }
/*     */ 
/*     */   public boolean isRetryInvocation()
/*     */   {
/*  74 */     return this.isRetryInvocation;
/*     */   }
/*     */ 
/*     */   public void setIsRetryInvocation(boolean paramBoolean)
/*     */   {
/*  79 */     this.isRetryInvocation = paramBoolean;
/*     */   }
/*     */ 
/*     */   public int getEntryCount()
/*     */   {
/*  84 */     return this.entryCount;
/*     */   }
/*     */ 
/*     */   public void incrementEntryCount()
/*     */   {
/*  89 */     this.entryCount += 1;
/*     */   }
/*     */ 
/*     */   public void decrementEntryCount()
/*     */   {
/*  94 */     this.entryCount -= 1;
/*     */   }
/*     */ 
/*     */   public void setClientRequestDispatcher(ClientRequestDispatcher paramClientRequestDispatcher)
/*     */   {
/*  99 */     this.clientRequestDispatcher = paramClientRequestDispatcher;
/*     */   }
/*     */ 
/*     */   public ClientRequestDispatcher getClientRequestDispatcher()
/*     */   {
/* 104 */     return this.clientRequestDispatcher;
/*     */   }
/*     */ 
/*     */   public void setMessageMediator(MessageMediator paramMessageMediator)
/*     */   {
/* 109 */     this.messageMediator = paramMessageMediator;
/*     */   }
/*     */ 
/*     */   public MessageMediator getMessageMediator()
/*     */   {
/* 114 */     return this.messageMediator;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.CorbaInvocationInfo
 * JD-Core Version:    0.6.2
 */