/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SnmpMibOid extends SnmpMibNode
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5012254771107446812L;
/* 531 */   private NonSyncVector<SnmpMibNode> children = new NonSyncVector(1);
/*     */ 
/* 536 */   private int nbChildren = 0;
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*  84 */     for (Enumeration localEnumeration = paramSnmpMibSubRequest.getElements(); localEnumeration.hasMoreElements(); ) {
/*  85 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*  86 */       SnmpStatusException localSnmpStatusException = new SnmpStatusException(225);
/*     */ 
/*  88 */       paramSnmpMibSubRequest.registerGetException(localSnmpVarBind, localSnmpStatusException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 107 */     for (Enumeration localEnumeration = paramSnmpMibSubRequest.getElements(); localEnumeration.hasMoreElements(); ) {
/* 108 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/* 109 */       SnmpStatusException localSnmpStatusException = new SnmpStatusException(6);
/*     */ 
/* 111 */       paramSnmpMibSubRequest.registerSetException(localSnmpVarBind, localSnmpStatusException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 130 */     for (Enumeration localEnumeration = paramSnmpMibSubRequest.getElements(); localEnumeration.hasMoreElements(); ) {
/* 131 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/* 132 */       SnmpStatusException localSnmpStatusException = new SnmpStatusException(6);
/*     */ 
/* 134 */       paramSnmpMibSubRequest.registerCheckException(localSnmpVarBind, localSnmpStatusException);
/*     */     }
/*     */   }
/*     */ 
/*     */   void findHandlingNode(SnmpVarBind paramSnmpVarBind, long[] paramArrayOfLong, int paramInt, SnmpRequestTree paramSnmpRequestTree)
/*     */     throws SnmpStatusException
/*     */   {
/* 152 */     int i = paramArrayOfLong.length;
/* 153 */     Object localObject = null;
/*     */ 
/* 155 */     if (paramSnmpRequestTree == null) {
/* 156 */       throw new SnmpStatusException(5);
/*     */     }
/* 158 */     if (paramInt > i)
/*     */     {
/* 160 */       throw new SnmpStatusException(225);
/*     */     }
/* 162 */     if (paramInt == i)
/*     */     {
/* 164 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 170 */     SnmpMibNode localSnmpMibNode = getChild(paramArrayOfLong[paramInt]);
/*     */ 
/* 181 */     if (localSnmpMibNode == null)
/* 182 */       paramSnmpRequestTree.add(this, paramInt, paramSnmpVarBind);
/*     */     else
/* 184 */       localSnmpMibNode.findHandlingNode(paramSnmpVarBind, paramArrayOfLong, paramInt + 1, paramSnmpRequestTree);
/*     */   }
/*     */ 
/*     */   long[] findNextHandlingNode(SnmpVarBind paramSnmpVarBind, long[] paramArrayOfLong, int paramInt1, int paramInt2, SnmpRequestTree paramSnmpRequestTree, AcmChecker paramAcmChecker)
/*     */     throws SnmpStatusException
/*     */   {
/* 201 */     int i = paramArrayOfLong.length;
/* 202 */     Object localObject1 = null;
/* 203 */     long[] arrayOfLong1 = null;
/* 204 */     if (paramSnmpRequestTree == null)
/*     */     {
/* 209 */       throw new SnmpStatusException(225);
/*     */     }
/* 211 */     Object localObject2 = paramSnmpRequestTree.getUserData();
/* 212 */     int j = paramSnmpRequestTree.getRequestPduVersion();
/*     */ 
/* 214 */     if (paramInt1 >= i) {
/* 215 */       arrayOfLong2 = new long[1];
/* 216 */       arrayOfLong2[0] = getNextVarId(-1L, localObject2, j);
/* 217 */       arrayOfLong1 = findNextHandlingNode(paramSnmpVarBind, arrayOfLong2, 0, paramInt2, paramSnmpRequestTree, paramAcmChecker);
/*     */ 
/* 219 */       return arrayOfLong1;
/*     */     }
/*     */ 
/* 224 */     long[] arrayOfLong2 = new long[1];
/* 225 */     long l = paramArrayOfLong[paramInt1];
/*     */     while (true)
/*     */     {
/*     */       try
/*     */       {
/* 230 */         SnmpMibNode localSnmpMibNode = getChild(l);
/*     */ 
/* 232 */         if (localSnmpMibNode == null)
/*     */         {
/* 234 */           throw new SnmpStatusException(225);
/*     */         }
/*     */ 
/* 239 */         paramAcmChecker.add(paramInt2, l);
/*     */         try {
/* 241 */           arrayOfLong1 = localSnmpMibNode.findNextHandlingNode(paramSnmpVarBind, paramArrayOfLong, paramInt1 + 1, paramInt2 + 1, paramSnmpRequestTree, paramAcmChecker);
/*     */         }
/*     */         finally
/*     */         {
/* 245 */           paramAcmChecker.remove(paramInt2);
/*     */         }
/*     */ 
/* 250 */         arrayOfLong1[paramInt2] = l;
/* 251 */         return arrayOfLong1;
/*     */       }
/*     */       catch (SnmpStatusException localSnmpStatusException)
/*     */       {
/* 256 */         l = getNextVarId(l, localObject2, j);
/*     */ 
/* 259 */         arrayOfLong2[0] = l;
/* 260 */         paramInt1 = 1;
/* 261 */         paramArrayOfLong = arrayOfLong2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getRootOid(Vector<Integer> paramVector)
/*     */   {
/* 275 */     if (this.nbChildren != 1) {
/* 276 */       return;
/*     */     }
/* 278 */     paramVector.addElement(Integer.valueOf(this.varList[0]));
/*     */ 
/* 282 */     ((SnmpMibNode)this.children.firstElement()).getRootOid(paramVector);
/*     */   }
/*     */ 
/*     */   public void registerNode(String paramString, SnmpMibNode paramSnmpMibNode)
/*     */     throws IllegalAccessException
/*     */   {
/* 291 */     SnmpOid localSnmpOid = new SnmpOid(paramString);
/* 292 */     registerNode(localSnmpOid.longValue(), 0, paramSnmpMibNode);
/*     */   }
/*     */ 
/*     */   void registerNode(long[] paramArrayOfLong, int paramInt, SnmpMibNode paramSnmpMibNode)
/*     */     throws IllegalAccessException
/*     */   {
/* 304 */     if (paramInt >= paramArrayOfLong.length) {
/* 305 */       throw new IllegalAccessException();
/*     */     }
/*     */ 
/* 309 */     long l = paramArrayOfLong[paramInt];
/*     */ 
/* 314 */     int i = retrieveIndex(l);
/* 315 */     if (i == this.nbChildren) {
/* 316 */       this.nbChildren += 1;
/* 317 */       this.varList = new int[this.nbChildren];
/* 318 */       this.varList[0] = ((int)l);
/* 319 */       i = 0;
/* 320 */       if (paramInt + 1 == paramArrayOfLong.length)
/*     */       {
/* 326 */         this.children.insertElementAt(paramSnmpMibNode, i);
/* 327 */         return;
/*     */       }
/*     */ 
/* 332 */       localObject = new SnmpMibOid();
/* 333 */       this.children.insertElementAt(localObject, i);
/* 334 */       ((SnmpMibOid)localObject).registerNode(paramArrayOfLong, paramInt + 1, paramSnmpMibNode);
/* 335 */       return;
/*     */     }
/* 337 */     if (i == -1)
/*     */     {
/* 340 */       localObject = new int[this.nbChildren + 1];
/* 341 */       localObject[this.nbChildren] = ((int)l);
/* 342 */       System.arraycopy(this.varList, 0, localObject, 0, this.nbChildren);
/* 343 */       this.varList = ((int[])localObject);
/* 344 */       this.nbChildren += 1;
/* 345 */       SnmpMibNode.sort(this.varList);
/* 346 */       int j = retrieveIndex(l);
/* 347 */       this.varList[j] = ((int)l);
/* 348 */       if (paramInt + 1 == paramArrayOfLong.length)
/*     */       {
/* 354 */         this.children.insertElementAt(paramSnmpMibNode, j);
/* 355 */         return;
/*     */       }
/* 357 */       SnmpMibOid localSnmpMibOid = new SnmpMibOid();
/*     */ 
/* 360 */       this.children.insertElementAt(localSnmpMibOid, j);
/* 361 */       localSnmpMibOid.registerNode(paramArrayOfLong, paramInt + 1, paramSnmpMibNode);
/* 362 */       return;
/*     */     }
/*     */ 
/* 367 */     Object localObject = (SnmpMibNode)this.children.elementAt(i);
/* 368 */     if (paramInt + 1 == paramArrayOfLong.length)
/*     */     {
/* 371 */       if (localObject == paramSnmpMibNode) return;
/* 372 */       if ((localObject != null) && (paramSnmpMibNode != null))
/*     */       {
/* 380 */         if ((paramSnmpMibNode instanceof SnmpMibGroup))
/*     */         {
/* 384 */           ((SnmpMibOid)localObject).exportChildren((SnmpMibOid)paramSnmpMibNode);
/* 385 */           this.children.setElementAt(paramSnmpMibNode, i);
/* 386 */           return;
/*     */         }
/* 388 */         if (((paramSnmpMibNode instanceof SnmpMibOid)) && ((localObject instanceof SnmpMibGroup)))
/*     */         {
/* 394 */           ((SnmpMibOid)paramSnmpMibNode).exportChildren((SnmpMibOid)localObject);
/* 395 */           return;
/* 396 */         }if ((paramSnmpMibNode instanceof SnmpMibOid))
/*     */         {
/* 401 */           ((SnmpMibOid)localObject).exportChildren((SnmpMibOid)paramSnmpMibNode);
/* 402 */           this.children.setElementAt(paramSnmpMibNode, i);
/* 403 */           return;
/*     */         }
/*     */       }
/* 406 */       this.children.setElementAt(paramSnmpMibNode, i);
/* 407 */       return;
/*     */     }
/* 409 */     if (localObject == null)
/* 410 */       throw new IllegalAccessException();
/* 411 */     ((SnmpMibOid)localObject).registerNode(paramArrayOfLong, paramInt + 1, paramSnmpMibNode);
/*     */   }
/*     */ 
/*     */   void exportChildren(SnmpMibOid paramSnmpMibOid)
/*     */     throws IllegalAccessException
/*     */   {
/* 426 */     if (paramSnmpMibOid == null) return;
/* 427 */     long[] arrayOfLong = new long[1];
/* 428 */     for (int i = 0; i < this.nbChildren; i++) {
/* 429 */       SnmpMibNode localSnmpMibNode = (SnmpMibNode)this.children.elementAt(i);
/* 430 */       if (localSnmpMibNode != null) {
/* 431 */         arrayOfLong[0] = this.varList[i];
/* 432 */         paramSnmpMibOid.registerNode(arrayOfLong, 0, localSnmpMibNode);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   SnmpMibNode getChild(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 443 */     int i = getInsertAt(paramLong);
/* 444 */     if (i >= this.nbChildren) {
/* 445 */       throw new SnmpStatusException(225);
/*     */     }
/* 447 */     if (this.varList[i] != (int)paramLong) {
/* 448 */       throw new SnmpStatusException(225);
/*     */     }
/*     */ 
/* 452 */     SnmpMibNode localSnmpMibNode = null;
/*     */     try {
/* 454 */       localSnmpMibNode = (SnmpMibNode)this.children.elementAtNonSync(i);
/*     */     } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 456 */       throw new SnmpStatusException(225);
/*     */     }
/* 458 */     if (localSnmpMibNode == null)
/* 459 */       throw new SnmpStatusException(224);
/* 460 */     return localSnmpMibNode;
/*     */   }
/*     */ 
/*     */   private int retrieveIndex(long paramLong)
/*     */   {
/* 465 */     int i = 0;
/* 466 */     int j = (int)paramLong;
/* 467 */     if ((this.varList == null) || (this.varList.length < 1)) {
/* 468 */       return this.nbChildren;
/*     */     }
/* 470 */     int k = this.varList.length - 1;
/* 471 */     int m = i + (k - i) / 2;
/* 472 */     int n = 0;
/* 473 */     while (i <= k) {
/* 474 */       n = this.varList[m];
/* 475 */       if (j == n)
/*     */       {
/* 478 */         return m;
/*     */       }
/* 480 */       if (n < j)
/* 481 */         i = m + 1;
/*     */       else {
/* 483 */         k = m - 1;
/*     */       }
/* 485 */       m = i + (k - i) / 2;
/*     */     }
/* 487 */     return -1;
/*     */   }
/*     */ 
/*     */   private int getInsertAt(long paramLong)
/*     */   {
/* 492 */     int i = 0;
/* 493 */     int j = (int)paramLong;
/* 494 */     if (this.varList == null)
/* 495 */       return -1;
/* 496 */     int k = this.varList.length - 1;
/* 497 */     int m = 0;
/*     */ 
/* 504 */     int n = i + (k - i) / 2;
/* 505 */     while (i <= k)
/*     */     {
/* 507 */       m = this.varList[n];
/*     */ 
/* 511 */       if (j == m) {
/* 512 */         return n;
/*     */       }
/* 514 */       if (m < j)
/* 515 */         i = n + 1;
/*     */       else {
/* 517 */         k = n - 1;
/*     */       }
/* 519 */       n = i + (k - i) / 2;
/*     */     }
/*     */ 
/* 522 */     return n;
/*     */   }
/*     */ 
/*     */   class NonSyncVector<E> extends Vector<E>
/*     */   {
/*     */     public NonSyncVector(int arg2)
/*     */     {
/* 547 */       super();
/*     */     }
/*     */ 
/*     */     final void addNonSyncElement(E paramE) {
/* 551 */       ensureCapacity(this.elementCount + 1);
/* 552 */       this.elementData[(this.elementCount++)] = paramE;
/*     */     }
/*     */ 
/*     */     final E elementAtNonSync(int paramInt)
/*     */     {
/* 557 */       return this.elementData[paramInt];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibOid
 * JD-Core Version:    0.6.2
 */