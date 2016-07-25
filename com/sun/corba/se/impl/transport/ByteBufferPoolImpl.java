/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.pept.transport.ByteBufferPool;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class ByteBufferPoolImpl
/*     */   implements ByteBufferPool
/*     */ {
/*     */   private ORB itsOrb;
/*     */   private int itsByteBufferSize;
/*     */   private ArrayList itsPool;
/*  44 */   private int itsObjectCounter = 0;
/*     */   private boolean debug;
/*     */ 
/*     */   public ByteBufferPoolImpl(ORB paramORB)
/*     */   {
/*  51 */     this.itsByteBufferSize = paramORB.getORBData().getGIOPFragmentSize();
/*  52 */     this.itsPool = new ArrayList();
/*  53 */     this.itsOrb = paramORB;
/*  54 */     this.debug = paramORB.transportDebugFlag;
/*     */   }
/*     */ 
/*     */   public ByteBuffer getByteBuffer(int paramInt)
/*     */   {
/*  75 */     ByteBuffer localByteBuffer = null;
/*     */ 
/*  77 */     if ((paramInt <= this.itsByteBufferSize) && (!this.itsOrb.getORBData().disableDirectByteBufferUse()))
/*     */     {
/*     */       int i;
/*  82 */       synchronized (this.itsPool)
/*     */       {
/*  84 */         i = this.itsPool.size();
/*  85 */         if (i > 0)
/*     */         {
/*  87 */           localByteBuffer = (ByteBuffer)this.itsPool.remove(i - 1);
/*     */ 
/*  90 */           localByteBuffer.clear();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  98 */       if (i <= 0)
/*     */       {
/* 100 */         localByteBuffer = ByteBuffer.allocateDirect(this.itsByteBufferSize);
/*     */       }
/*     */ 
/* 106 */       this.itsObjectCounter += 1;
/*     */     }
/*     */     else
/*     */     {
/* 112 */       localByteBuffer = ByteBuffer.allocate(paramInt);
/*     */     }
/*     */ 
/* 115 */     return localByteBuffer;
/*     */   }
/*     */ 
/*     */   public void releaseByteBuffer(ByteBuffer paramByteBuffer)
/*     */   {
/* 140 */     if (paramByteBuffer.isDirect())
/*     */     {
/* 142 */       synchronized (this.itsPool)
/*     */       {
/* 146 */         int i = 0;
/* 147 */         int j = 0;
/*     */         Object localObject1;
/* 149 */         if (this.debug)
/*     */         {
/* 154 */           for (int k = 0; (k < this.itsPool.size()) && (i == 0); k++)
/*     */           {
/* 156 */             localObject1 = (ByteBuffer)this.itsPool.get(k);
/* 157 */             if (paramByteBuffer == localObject1)
/*     */             {
/* 159 */               i = 1;
/* 160 */               j = System.identityHashCode(paramByteBuffer);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 168 */         if ((i == 0) || (!this.debug))
/*     */         {
/* 171 */           this.itsPool.add(paramByteBuffer);
/*     */         }
/*     */         else
/*     */         {
/* 175 */           String str = Thread.currentThread().getName();
/* 176 */           localObject1 = new Throwable(str + ": Duplicate ByteBuffer reference (" + j + ")");
/*     */ 
/* 180 */           ((Throwable)localObject1).printStackTrace(System.out);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 187 */       this.itsObjectCounter -= 1;
/*     */     }
/*     */     else
/*     */     {
/* 192 */       paramByteBuffer = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int activeCount()
/*     */   {
/* 203 */     return this.itsObjectCounter;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.ByteBufferPoolImpl
 * JD-Core Version:    0.6.2
 */