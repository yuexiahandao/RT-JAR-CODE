/*     */ package com.sun.corba.se.impl.oa.toa;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ 
/*     */ public final class TransientObjectManager
/*     */ {
/*     */   private ORB orb;
/*  39 */   private int maxSize = 128;
/*     */   private Element[] elementArray;
/*     */   private Element freeList;
/*     */ 
/*     */   void dprint(String paramString)
/*     */   {
/*  44 */     ORBUtility.dprint(this, paramString);
/*     */   }
/*     */ 
/*     */   public TransientObjectManager(ORB paramORB)
/*     */   {
/*  49 */     this.orb = paramORB;
/*     */ 
/*  51 */     this.elementArray = new Element[this.maxSize];
/*  52 */     this.elementArray[(this.maxSize - 1)] = new Element(this.maxSize - 1, null);
/*  53 */     for (int i = this.maxSize - 2; i >= 0; i--)
/*  54 */       this.elementArray[i] = new Element(i, this.elementArray[(i + 1)]);
/*  55 */     this.freeList = this.elementArray[0];
/*     */   }
/*     */ 
/*     */   public synchronized byte[] storeServant(Object paramObject1, Object paramObject2)
/*     */   {
/*  60 */     if (this.freeList == null) {
/*  61 */       doubleSize();
/*     */     }
/*  63 */     Element localElement = this.freeList;
/*  64 */     this.freeList = ((Element)this.freeList.servant);
/*     */ 
/*  66 */     byte[] arrayOfByte = localElement.getKey(paramObject1, paramObject2);
/*  67 */     if (this.orb.transientObjectManagerDebugFlag)
/*  68 */       dprint("storeServant returns key for element " + localElement);
/*  69 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public synchronized Object lookupServant(byte[] paramArrayOfByte)
/*     */   {
/*  74 */     int i = ORBUtility.bytesToInt(paramArrayOfByte, 0);
/*  75 */     int j = ORBUtility.bytesToInt(paramArrayOfByte, 4);
/*     */ 
/*  77 */     if (this.orb.transientObjectManagerDebugFlag) {
/*  78 */       dprint("lookupServant called with index=" + i + ", counter=" + j);
/*     */     }
/*  80 */     if ((this.elementArray[i].counter == j) && (this.elementArray[i].valid))
/*     */     {
/*  82 */       if (this.orb.transientObjectManagerDebugFlag)
/*  83 */         dprint("\tcounter is valid");
/*  84 */       return this.elementArray[i].servant;
/*     */     }
/*     */ 
/*  88 */     if (this.orb.transientObjectManagerDebugFlag)
/*  89 */       dprint("\tcounter is invalid");
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized Object lookupServantData(byte[] paramArrayOfByte)
/*     */   {
/*  95 */     int i = ORBUtility.bytesToInt(paramArrayOfByte, 0);
/*  96 */     int j = ORBUtility.bytesToInt(paramArrayOfByte, 4);
/*     */ 
/*  98 */     if (this.orb.transientObjectManagerDebugFlag) {
/*  99 */       dprint("lookupServantData called with index=" + i + ", counter=" + j);
/*     */     }
/* 101 */     if ((this.elementArray[i].counter == j) && (this.elementArray[i].valid))
/*     */     {
/* 103 */       if (this.orb.transientObjectManagerDebugFlag)
/* 104 */         dprint("\tcounter is valid");
/* 105 */       return this.elementArray[i].servantData;
/*     */     }
/*     */ 
/* 109 */     if (this.orb.transientObjectManagerDebugFlag)
/* 110 */       dprint("\tcounter is invalid");
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized void deleteServant(byte[] paramArrayOfByte)
/*     */   {
/* 116 */     int i = ORBUtility.bytesToInt(paramArrayOfByte, 0);
/* 117 */     if (this.orb.transientObjectManagerDebugFlag) {
/* 118 */       dprint("deleting servant at index=" + i);
/*     */     }
/* 120 */     this.elementArray[i].delete(this.freeList);
/* 121 */     this.freeList = this.elementArray[i];
/*     */   }
/*     */ 
/*     */   public synchronized byte[] getKey(Object paramObject)
/*     */   {
/* 126 */     for (int i = 0; i < this.maxSize; i++) {
/* 127 */       if ((this.elementArray[i].valid) && (this.elementArray[i].servant == paramObject))
/*     */       {
/* 129 */         return this.elementArray[i].toBytes();
/*     */       }
/*     */     }
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   private void doubleSize()
/*     */   {
/* 139 */     Element[] arrayOfElement = this.elementArray;
/* 140 */     int i = this.maxSize;
/* 141 */     this.maxSize *= 2;
/* 142 */     this.elementArray = new Element[this.maxSize];
/*     */ 
/* 144 */     for (int j = 0; j < i; j++) {
/* 145 */       this.elementArray[j] = arrayOfElement[j];
/*     */     }
/* 147 */     this.elementArray[(this.maxSize - 1)] = new Element(this.maxSize - 1, null);
/* 148 */     for (j = this.maxSize - 2; j >= i; j--)
/* 149 */       this.elementArray[j] = new Element(j, this.elementArray[(j + 1)]);
/* 150 */     this.freeList = this.elementArray[i];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.toa.TransientObjectManager
 * JD-Core Version:    0.6.2
 */