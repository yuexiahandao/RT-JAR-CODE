/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import java.io.Serializable;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public abstract class SnmpMibGroup extends SnmpMibOid
/*     */   implements Serializable
/*     */ {
/*  67 */   protected Hashtable<Long, Long> subgroups = null;
/*     */ 
/*     */   public abstract boolean isTable(long paramLong);
/*     */ 
/*     */   public abstract boolean isVariable(long paramLong);
/*     */ 
/*     */   public abstract boolean isReadable(long paramLong);
/*     */ 
/*     */   public abstract SnmpMibTable getTable(long paramLong);
/*     */ 
/*     */   public void validateVarId(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 118 */     if (!isVariable(paramLong))
/* 119 */       throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public boolean isNestedArc(long paramLong)
/*     */   {
/* 144 */     if (this.subgroups == null) return false;
/* 145 */     Object localObject = this.subgroups.get(new Long(paramLong));
/*     */ 
/* 148 */     return localObject != null;
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
/*     */   public void getRootOid(Vector paramVector)
/*     */   {
/*     */   }
/*     */ 
/*     */   void registerNestedArc(long paramLong)
/*     */   {
/* 266 */     Long localLong = new Long(paramLong);
/* 267 */     if (this.subgroups == null) this.subgroups = new Hashtable();
/*     */ 
/* 269 */     this.subgroups.put(localLong, localLong);
/*     */   }
/*     */ 
/*     */   protected void registerObject(long paramLong)
/*     */     throws IllegalAccessException
/*     */   {
/* 296 */     long[] arrayOfLong = new long[1];
/* 297 */     arrayOfLong[0] = paramLong;
/* 298 */     super.registerNode(arrayOfLong, 0, null);
/*     */   }
/*     */ 
/*     */   void registerNode(long[] paramArrayOfLong, int paramInt, SnmpMibNode paramSnmpMibNode)
/*     */     throws IllegalAccessException
/*     */   {
/* 317 */     super.registerNode(paramArrayOfLong, paramInt, paramSnmpMibNode);
/* 318 */     if (paramInt < 0) return;
/* 319 */     if (paramInt >= paramArrayOfLong.length) return;
/*     */ 
/* 322 */     registerNestedArc(paramArrayOfLong[paramInt]);
/*     */   }
/*     */ 
/*     */   void findHandlingNode(SnmpVarBind paramSnmpVarBind, long[] paramArrayOfLong, int paramInt, SnmpRequestTree paramSnmpRequestTree)
/*     */     throws SnmpStatusException
/*     */   {
/* 333 */     int i = paramArrayOfLong.length;
/* 334 */     Object localObject1 = null;
/*     */ 
/* 336 */     if (paramSnmpRequestTree == null) {
/* 337 */       throw new SnmpStatusException(5);
/*     */     }
/* 339 */     Object localObject2 = paramSnmpRequestTree.getUserData();
/*     */ 
/* 341 */     if (paramInt >= i)
/*     */     {
/* 343 */       throw new SnmpStatusException(6);
/*     */     }
/*     */ 
/* 346 */     long l = paramArrayOfLong[paramInt];
/*     */ 
/* 348 */     if (isNestedArc(l))
/*     */     {
/* 351 */       super.findHandlingNode(paramSnmpVarBind, paramArrayOfLong, paramInt, paramSnmpRequestTree);
/* 352 */       return;
/* 353 */     }if (isTable(l))
/*     */     {
/* 357 */       SnmpMibTable localSnmpMibTable = getTable(l);
/*     */ 
/* 360 */       localSnmpMibTable.findHandlingNode(paramSnmpVarBind, paramArrayOfLong, paramInt + 1, paramSnmpRequestTree);
/*     */     }
/*     */     else
/*     */     {
/* 364 */       validateVarId(l, localObject2);
/*     */ 
/* 367 */       if (paramInt + 2 > i) {
/* 368 */         throw new SnmpStatusException(224);
/*     */       }
/*     */ 
/* 372 */       if (paramInt + 2 < i) {
/* 373 */         throw new SnmpStatusException(224);
/*     */       }
/*     */ 
/* 376 */       if (paramArrayOfLong[(paramInt + 1)] != 0L) {
/* 377 */         throw new SnmpStatusException(224);
/*     */       }
/*     */ 
/* 380 */       paramSnmpRequestTree.add(this, paramInt, paramSnmpVarBind);
/*     */     }
/*     */   }
/*     */ 
/*     */   long[] findNextHandlingNode(SnmpVarBind paramSnmpVarBind, long[] paramArrayOfLong, int paramInt1, int paramInt2, SnmpRequestTree paramSnmpRequestTree, AcmChecker paramAcmChecker)
/*     */     throws SnmpStatusException
/*     */   {
/* 392 */     int i = paramArrayOfLong.length;
/* 393 */     Object localObject1 = null;
/*     */ 
/* 395 */     if (paramSnmpRequestTree == null)
/*     */     {
/* 400 */       throw new SnmpStatusException(225);
/*     */     }
/* 402 */     Object localObject2 = paramSnmpRequestTree.getUserData();
/* 403 */     int j = paramSnmpRequestTree.getRequestPduVersion();
/*     */ 
/* 410 */     if (paramInt1 >= i) {
/* 411 */       return super.findNextHandlingNode(paramSnmpVarBind, paramArrayOfLong, paramInt1, paramInt2, paramSnmpRequestTree, paramAcmChecker);
/*     */     }
/*     */ 
/* 415 */     long l = paramArrayOfLong[paramInt1];
/*     */ 
/* 417 */     long[] arrayOfLong1 = null;
/*     */     long[] arrayOfLong2;
/*     */     try
/*     */     {
/* 422 */       if (isTable(l))
/*     */       {
/* 427 */         SnmpMibTable localSnmpMibTable = getTable(l);
/*     */ 
/* 430 */         paramAcmChecker.add(paramInt2, l);
/*     */         try {
/* 432 */           arrayOfLong1 = localSnmpMibTable.findNextHandlingNode(paramSnmpVarBind, paramArrayOfLong, paramInt1 + 1, paramInt2 + 1, paramSnmpRequestTree, paramAcmChecker);
/*     */         }
/*     */         catch (SnmpStatusException localSnmpStatusException3)
/*     */         {
/* 436 */           throw new SnmpStatusException(225);
/*     */         } finally {
/* 438 */           paramAcmChecker.remove(paramInt2);
/*     */         }
/*     */ 
/* 441 */         arrayOfLong1[paramInt2] = l;
/* 442 */         return arrayOfLong1;
/* 443 */       }if (isReadable(l))
/*     */       {
/* 446 */         if (paramInt1 == i - 1)
/*     */         {
/* 453 */           arrayOfLong1 = new long[paramInt2 + 2];
/* 454 */           arrayOfLong1[(paramInt2 + 1)] = 0L;
/* 455 */           arrayOfLong1[paramInt2] = l;
/*     */ 
/* 457 */           paramAcmChecker.add(paramInt2, arrayOfLong1, paramInt2, 2);
/*     */           try {
/* 459 */             paramAcmChecker.checkCurrentOid();
/*     */           } catch (SnmpStatusException localSnmpStatusException1) {
/* 461 */             throw new SnmpStatusException(225);
/*     */           } finally {
/* 463 */             paramAcmChecker.remove(paramInt2, 2);
/*     */           }
/*     */ 
/* 467 */           paramSnmpRequestTree.add(this, paramInt2, paramSnmpVarBind);
/* 468 */           return arrayOfLong1;
/*     */         }
/*     */ 
/*     */       }
/* 480 */       else if (isNestedArc(l))
/*     */       {
/* 487 */         SnmpMibNode localSnmpMibNode = getChild(l);
/*     */ 
/* 489 */         if (localSnmpMibNode != null) {
/* 490 */           paramAcmChecker.add(paramInt2, l);
/*     */           try {
/* 492 */             arrayOfLong1 = localSnmpMibNode.findNextHandlingNode(paramSnmpVarBind, paramArrayOfLong, paramInt1 + 1, paramInt2 + 1, paramSnmpRequestTree, paramAcmChecker);
/*     */ 
/* 495 */             arrayOfLong1[paramInt2] = l;
/* 496 */             return arrayOfLong1;
/*     */           } finally {
/* 498 */             paramAcmChecker.remove(paramInt2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 506 */       throw new SnmpStatusException(225);
/*     */     }
/*     */     catch (SnmpStatusException localSnmpStatusException2)
/*     */     {
/* 512 */       arrayOfLong2 = new long[1];
/* 513 */       arrayOfLong2[0] = getNextVarId(l, localObject2, j);
/* 514 */     }return findNextHandlingNode(paramSnmpVarBind, arrayOfLong2, 0, paramInt2, paramSnmpRequestTree, paramAcmChecker);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibGroup
 * JD-Core Version:    0.6.2
 */