/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public abstract class SnmpMibNode
/*     */   implements Serializable
/*     */ {
/*     */   protected int[] varList;
/*     */ 
/*     */   public long getNextVarId(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/*  79 */     return getNextIdentifier(this.varList, paramLong);
/*     */   }
/*     */ 
/*     */   public long getNextVarId(long paramLong, Object paramObject, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 104 */     long l = paramLong;
/*     */     do
/* 106 */       l = getNextVarId(l, paramObject);
/* 107 */     while (skipVariable(l, paramObject, paramInt));
/*     */ 
/* 109 */     return l;
/*     */   }
/*     */ 
/*     */   protected boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   void findHandlingNode(SnmpVarBind paramSnmpVarBind, long[] paramArrayOfLong, int paramInt, SnmpRequestTree paramSnmpRequestTree)
/*     */     throws SnmpStatusException
/*     */   {
/* 158 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   long[] findNextHandlingNode(SnmpVarBind paramSnmpVarBind, long[] paramArrayOfLong, int paramInt1, int paramInt2, SnmpRequestTree paramSnmpRequestTree, AcmChecker paramAcmChecker)
/*     */     throws SnmpStatusException
/*     */   {
/* 186 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public abstract void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public static void sort(int[] paramArrayOfInt)
/*     */   {
/* 248 */     QuickSort(paramArrayOfInt, 0, paramArrayOfInt.length - 1);
/*     */   }
/*     */ 
/*     */   public void getRootOid(Vector<Integer> paramVector)
/*     */   {
/*     */   }
/*     */ 
/*     */   static void QuickSort(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 278 */     int i = paramInt1;
/* 279 */     int j = paramInt2;
/*     */ 
/* 282 */     if (paramInt2 > paramInt1)
/*     */     {
/* 287 */       int k = paramArrayOfInt[((paramInt1 + paramInt2) / 2)];
/*     */ 
/* 290 */       while (i <= j)
/*     */       {
/* 294 */         while ((i < paramInt2) && (paramArrayOfInt[i] < k)) {
/* 295 */           i++;
/*     */         }
/*     */ 
/* 300 */         while ((j > paramInt1) && (paramArrayOfInt[j] > k)) {
/* 301 */           j--;
/*     */         }
/*     */ 
/* 304 */         if (i <= j) {
/* 305 */           swap(paramArrayOfInt, i, j);
/* 306 */           i++;
/* 307 */           j--;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 314 */       if (paramInt1 < j) {
/* 315 */         QuickSort(paramArrayOfInt, paramInt1, j);
/*     */       }
/*     */ 
/* 320 */       if (i < paramInt2)
/* 321 */         QuickSort(paramArrayOfInt, i, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static final int getNextIdentifier(int[] paramArrayOfInt, long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 346 */     int[] arrayOfInt = paramArrayOfInt;
/* 347 */     int i = (int)paramLong;
/*     */ 
/* 349 */     if (arrayOfInt == null) {
/* 350 */       throw new SnmpStatusException(225);
/*     */     }
/* 352 */     int j = 0;
/* 353 */     int k = arrayOfInt.length;
/* 354 */     int m = j + (k - j) / 2;
/* 355 */     int n = 0;
/*     */ 
/* 359 */     if (k < 1) {
/* 360 */       throw new SnmpStatusException(225);
/*     */     }
/* 362 */     if (arrayOfInt[(k - 1)] <= i) {
/* 363 */       throw new SnmpStatusException(225);
/*     */     }
/* 365 */     while (j <= k) {
/* 366 */       n = arrayOfInt[m];
/* 367 */       if (i == n)
/*     */       {
/* 370 */         m++;
/* 371 */         return arrayOfInt[m];
/*     */       }
/* 373 */       if (n < i)
/* 374 */         j = m + 1;
/*     */       else {
/* 376 */         k = m - 1;
/*     */       }
/* 378 */       m = j + (k - j) / 2;
/*     */     }
/* 380 */     return arrayOfInt[m];
/*     */   }
/*     */ 
/*     */   private static final void swap(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 390 */     int i = paramArrayOfInt[paramInt1];
/* 391 */     paramArrayOfInt[paramInt1] = paramArrayOfInt[paramInt2];
/* 392 */     paramArrayOfInt[paramInt2] = i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibNode
 * JD-Core Version:    0.6.2
 */