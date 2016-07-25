/*     */ package sun.awt.geom;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public abstract class AreaOp
/*     */ {
/*     */   public static final int CTAG_LEFT = 0;
/*     */   public static final int CTAG_RIGHT = 1;
/*     */   public static final int ETAG_IGNORE = 0;
/*     */   public static final int ETAG_ENTER = 1;
/*     */   public static final int ETAG_EXIT = -1;
/*     */   public static final int RSTAG_INSIDE = 1;
/*     */   public static final int RSTAG_OUTSIDE = -1;
/* 181 */   private static Comparator YXTopComparator = new Comparator() {
/*     */     public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/* 183 */       Curve localCurve1 = ((Edge)paramAnonymousObject1).getCurve();
/* 184 */       Curve localCurve2 = ((Edge)paramAnonymousObject2).getCurve();
/*     */       double d1;
/*     */       double d2;
/* 186 */       if (((d1 = localCurve1.getYTop()) == (d2 = localCurve2.getYTop())) && 
/* 187 */         ((d1 = localCurve1.getXTop()) == (d2 = localCurve2.getXTop()))) {
/* 188 */         return 0;
/*     */       }
/*     */ 
/* 191 */       if (d1 < d2) {
/* 192 */         return -1;
/*     */       }
/* 194 */       return 1;
/*     */     }
/* 181 */   };
/*     */ 
/* 437 */   private static CurveLink[] EmptyLinkList = new CurveLink[2];
/* 438 */   private static ChainEnd[] EmptyChainList = new ChainEnd[2];
/*     */ 
/*     */   public abstract void newRow();
/*     */ 
/*     */   public abstract int classify(Edge paramEdge);
/*     */ 
/*     */   public abstract int getState();
/*     */ 
/*     */   public Vector calculate(Vector paramVector1, Vector paramVector2)
/*     */   {
/* 156 */     Vector localVector = new Vector();
/* 157 */     addEdges(localVector, paramVector1, 0);
/* 158 */     addEdges(localVector, paramVector2, 1);
/* 159 */     localVector = pruneEdges(localVector);
/*     */ 
/* 168 */     return localVector;
/*     */   }
/*     */ 
/*     */   private static void addEdges(Vector paramVector1, Vector paramVector2, int paramInt) {
/* 172 */     Enumeration localEnumeration = paramVector2.elements();
/* 173 */     while (localEnumeration.hasMoreElements()) {
/* 174 */       Curve localCurve = (Curve)localEnumeration.nextElement();
/* 175 */       if (localCurve.getOrder() > 0)
/* 176 */         paramVector1.add(new Edge(localCurve, paramInt));
/*     */     }
/*     */   }
/*     */ 
/*     */   private Vector pruneEdges(Vector paramVector)
/*     */   {
/* 199 */     int i = paramVector.size();
/* 200 */     if (i < 2) {
/* 201 */       return paramVector;
/*     */     }
/* 203 */     Edge[] arrayOfEdge = (Edge[])paramVector.toArray(new Edge[i]);
/* 204 */     Arrays.sort(arrayOfEdge, YXTopComparator);
/*     */ 
/* 212 */     int j = 0;
/* 213 */     int k = 0;
/* 214 */     int m = 0;
/* 215 */     int n = 0;
/* 216 */     double[] arrayOfDouble = new double[2];
/* 217 */     Vector localVector1 = new Vector();
/* 218 */     Vector localVector2 = new Vector();
/* 219 */     Vector localVector3 = new Vector();
/*     */ 
/* 221 */     while (j < i) {
/* 222 */       double d1 = arrayOfDouble[0];
/*     */       Object localObject1;
/* 224 */       for (m = n = k - 1; m >= j; m--) {
/* 225 */         localObject1 = arrayOfEdge[m];
/* 226 */         if (((Edge)localObject1).getCurve().getYBot() > d1) {
/* 227 */           if (n > m) {
/* 228 */             arrayOfEdge[n] = localObject1;
/*     */           }
/* 230 */           n--;
/*     */         }
/*     */       }
/* 233 */       j = n + 1;
/*     */ 
/* 235 */       if (j >= k) {
/* 236 */         if (k >= i) {
/*     */           break;
/*     */         }
/* 239 */         d1 = arrayOfEdge[k].getCurve().getYTop();
/* 240 */         if (d1 > arrayOfDouble[0]) {
/* 241 */           finalizeSubCurves(localVector1, localVector2);
/*     */         }
/* 243 */         arrayOfDouble[0] = d1;
/*     */       }
/*     */ 
/* 246 */       while (k < i) {
/* 247 */         localObject1 = arrayOfEdge[k];
/* 248 */         if (((Edge)localObject1).getCurve().getYTop() > d1) {
/*     */           break;
/*     */         }
/* 251 */         k++;
/*     */       }
/*     */ 
/* 256 */       arrayOfDouble[1] = arrayOfEdge[j].getCurve().getYBot();
/* 257 */       if (k < i) {
/* 258 */         d1 = arrayOfEdge[k].getCurve().getYTop();
/* 259 */         if (arrayOfDouble[1] > d1) {
/* 260 */           arrayOfDouble[1] = d1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 272 */       int i1 = 1;
/* 273 */       for (m = j; m < k; m++) {
/* 274 */         localObject1 = arrayOfEdge[m];
/* 275 */         ((Edge)localObject1).setEquivalence(0);
/* 276 */         for (n = m; n > j; n--) {
/* 277 */           Edge localEdge = arrayOfEdge[(n - 1)];
/* 278 */           int i2 = ((Edge)localObject1).compareTo(localEdge, arrayOfDouble);
/* 279 */           if (arrayOfDouble[1] <= arrayOfDouble[0]) {
/* 280 */             throw new InternalError("backstepping to " + arrayOfDouble[1] + " from " + arrayOfDouble[0]);
/*     */           }
/*     */ 
/* 283 */           if (i2 >= 0) {
/* 284 */             if (i2 != 0)
/*     */             {
/*     */               break;
/*     */             }
/*     */ 
/* 289 */             int i3 = localEdge.getEquivalence();
/* 290 */             if (i3 == 0) {
/* 291 */               i3 = i1++;
/* 292 */               localEdge.setEquivalence(i3);
/*     */             }
/* 294 */             ((Edge)localObject1).setEquivalence(i3);
/* 295 */             break;
/*     */           }
/*     */ 
/* 298 */           arrayOfEdge[n] = localEdge;
/*     */         }
/* 300 */         arrayOfEdge[n] = localObject1;
/*     */       }
/*     */ 
/* 314 */       newRow();
/* 315 */       double d2 = arrayOfDouble[0];
/* 316 */       double d3 = arrayOfDouble[1];
/*     */       int i4;
/* 317 */       for (m = j; m < k; m++) {
/* 318 */         localObject1 = arrayOfEdge[m];
/*     */ 
/* 320 */         int i5 = ((Edge)localObject1).getEquivalence();
/* 321 */         if (i5 != 0)
/*     */         {
/* 327 */           int i6 = getState();
/* 328 */           i4 = i6 == 1 ? -1 : 1;
/*     */ 
/* 331 */           Object localObject4 = null;
/* 332 */           Object localObject5 = localObject1;
/* 333 */           double d4 = d3;
/*     */           do
/*     */           {
/* 337 */             classify((Edge)localObject1);
/* 338 */             if ((localObject4 == null) && (((Edge)localObject1).isActiveFor(d2, i4)))
/*     */             {
/* 341 */               localObject4 = localObject1;
/*     */             }
/* 343 */             d1 = ((Edge)localObject1).getCurve().getYBot();
/* 344 */             if (d1 > d4) {
/* 345 */               localObject5 = localObject1;
/* 346 */               d4 = d1;
/*     */             }
/* 348 */             m++; } while ((m < k) && ((localObject1 = arrayOfEdge[m]).getEquivalence() == i5));
/*     */ 
/* 350 */           m--;
/* 351 */           if (getState() == i6)
/* 352 */             i4 = 0;
/*     */           else
/* 354 */             localObject1 = localObject4 != null ? localObject4 : localObject5;
/*     */         }
/*     */         else {
/* 357 */           i4 = classify((Edge)localObject1);
/*     */         }
/* 359 */         if (i4 != 0) {
/* 360 */           ((Edge)localObject1).record(d3, i4);
/* 361 */           localVector3.add(new CurveLink(((Edge)localObject1).getCurve(), d2, d3, i4));
/*     */         }
/*     */       }
/*     */ 
/* 365 */       if (getState() != -1) {
/* 366 */         System.out.println("Still inside at end of active edge list!");
/* 367 */         System.out.println("num curves = " + (k - j));
/* 368 */         System.out.println("num links = " + localVector3.size());
/* 369 */         System.out.println("y top = " + arrayOfDouble[0]);
/* 370 */         if (k < i) {
/* 371 */           System.out.println("y top of next curve = " + arrayOfEdge[k].getCurve().getYTop());
/*     */         }
/*     */         else {
/* 374 */           System.out.println("no more curves");
/*     */         }
/* 376 */         for (m = j; m < k; m++) {
/* 377 */           localObject1 = arrayOfEdge[m];
/* 378 */           System.out.println(localObject1);
/* 379 */           i4 = ((Edge)localObject1).getEquivalence();
/* 380 */           if (i4 != 0) {
/* 381 */             System.out.println("  was equal to " + i4 + "...");
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 392 */       resolveLinks(localVector1, localVector2, localVector3);
/* 393 */       localVector3.clear();
/*     */ 
/* 396 */       arrayOfDouble[0] = d3;
/*     */     }
/* 398 */     finalizeSubCurves(localVector1, localVector2);
/* 399 */     Vector localVector4 = new Vector();
/* 400 */     Enumeration localEnumeration = localVector1.elements();
/* 401 */     while (localEnumeration.hasMoreElements()) {
/* 402 */       Object localObject2 = (CurveLink)localEnumeration.nextElement();
/* 403 */       localVector4.add(((CurveLink)localObject2).getMoveto());
/* 404 */       Object localObject3 = localObject2;
/* 405 */       while ((localObject3 = ((CurveLink)localObject3).getNext()) != null) {
/* 406 */         if (!((CurveLink)localObject2).absorb((CurveLink)localObject3)) {
/* 407 */           localVector4.add(((CurveLink)localObject2).getSubCurve());
/* 408 */           localObject2 = localObject3;
/*     */         }
/*     */       }
/* 411 */       localVector4.add(((CurveLink)localObject2).getSubCurve());
/*     */     }
/* 413 */     return localVector4;
/*     */   }
/*     */ 
/*     */   public static void finalizeSubCurves(Vector paramVector1, Vector paramVector2) {
/* 417 */     int i = paramVector2.size();
/* 418 */     if (i == 0) {
/* 419 */       return;
/*     */     }
/* 421 */     if ((i & 0x1) != 0) {
/* 422 */       throw new InternalError("Odd number of chains!");
/*     */     }
/* 424 */     ChainEnd[] arrayOfChainEnd = new ChainEnd[i];
/* 425 */     paramVector2.toArray(arrayOfChainEnd);
/* 426 */     for (int j = 1; j < i; j += 2) {
/* 427 */       ChainEnd localChainEnd1 = arrayOfChainEnd[(j - 1)];
/* 428 */       ChainEnd localChainEnd2 = arrayOfChainEnd[j];
/* 429 */       CurveLink localCurveLink = localChainEnd1.linkTo(localChainEnd2);
/* 430 */       if (localCurveLink != null) {
/* 431 */         paramVector1.add(localCurveLink);
/*     */       }
/*     */     }
/* 434 */     paramVector2.clear();
/*     */   }
/*     */ 
/*     */   public static void resolveLinks(Vector paramVector1, Vector paramVector2, Vector paramVector3)
/*     */   {
/* 444 */     int i = paramVector3.size();
/*     */     CurveLink[] arrayOfCurveLink;
/* 446 */     if (i == 0) {
/* 447 */       arrayOfCurveLink = EmptyLinkList;
/*     */     } else {
/* 449 */       if ((i & 0x1) != 0) {
/* 450 */         throw new InternalError("Odd number of new curves!");
/*     */       }
/* 452 */       arrayOfCurveLink = new CurveLink[i + 2];
/* 453 */       paramVector3.toArray(arrayOfCurveLink);
/*     */     }
/* 455 */     int j = paramVector2.size();
/*     */     ChainEnd[] arrayOfChainEnd;
/* 457 */     if (j == 0) {
/* 458 */       arrayOfChainEnd = EmptyChainList;
/*     */     } else {
/* 460 */       if ((j & 0x1) != 0) {
/* 461 */         throw new InternalError("Odd number of chains!");
/*     */       }
/* 463 */       arrayOfChainEnd = new ChainEnd[j + 2];
/* 464 */       paramVector2.toArray(arrayOfChainEnd);
/*     */     }
/* 466 */     int k = 0;
/* 467 */     int m = 0;
/* 468 */     paramVector2.clear();
/* 469 */     Object localObject1 = arrayOfChainEnd[0];
/* 470 */     ChainEnd localChainEnd1 = arrayOfChainEnd[1];
/* 471 */     Object localObject2 = arrayOfCurveLink[0];
/* 472 */     CurveLink localCurveLink = arrayOfCurveLink[1];
/* 473 */     while ((localObject1 != null) || (localObject2 != null))
/*     */     {
/* 478 */       int n = localObject2 == null ? 1 : 0;
/* 479 */       int i1 = localObject1 == null ? 1 : 0;
/*     */ 
/* 481 */       if ((n == 0) && (i1 == 0))
/*     */       {
/* 487 */         n = ((k & 0x1) == 0) && (((ChainEnd)localObject1).getX() == localChainEnd1.getX()) ? 1 : 0;
/*     */ 
/* 489 */         i1 = ((m & 0x1) == 0) && (((CurveLink)localObject2).getX() == localCurveLink.getX()) ? 1 : 0;
/*     */ 
/* 492 */         if ((n == 0) && (i1 == 0))
/*     */         {
/* 498 */           double d1 = ((ChainEnd)localObject1).getX();
/* 499 */           double d2 = ((CurveLink)localObject2).getX();
/* 500 */           n = (localChainEnd1 != null) && (d1 < d2) && (obstructs(localChainEnd1.getX(), d2, k)) ? 1 : 0;
/*     */ 
/* 503 */           i1 = (localCurveLink != null) && (d2 < d1) && (obstructs(localCurveLink.getX(), d1, m)) ? 1 : 0;
/*     */         }
/*     */       }
/*     */       Object localObject3;
/* 508 */       if (n != 0) {
/* 509 */         localObject3 = ((ChainEnd)localObject1).linkTo(localChainEnd1);
/* 510 */         if (localObject3 != null) {
/* 511 */           paramVector1.add(localObject3);
/*     */         }
/* 513 */         k += 2;
/* 514 */         localObject1 = arrayOfChainEnd[k];
/* 515 */         localChainEnd1 = arrayOfChainEnd[(k + 1)];
/*     */       }
/* 517 */       if (i1 != 0) {
/* 518 */         localObject3 = new ChainEnd((CurveLink)localObject2, null);
/* 519 */         ChainEnd localChainEnd2 = new ChainEnd(localCurveLink, (ChainEnd)localObject3);
/* 520 */         ((ChainEnd)localObject3).setOtherEnd(localChainEnd2);
/* 521 */         paramVector2.add(localObject3);
/* 522 */         paramVector2.add(localChainEnd2);
/* 523 */         m += 2;
/* 524 */         localObject2 = arrayOfCurveLink[m];
/* 525 */         localCurveLink = arrayOfCurveLink[(m + 1)];
/*     */       }
/* 527 */       if ((n == 0) && (i1 == 0))
/*     */       {
/* 531 */         ((ChainEnd)localObject1).addLink((CurveLink)localObject2);
/* 532 */         paramVector2.add(localObject1);
/* 533 */         k++;
/* 534 */         localObject1 = localChainEnd1;
/* 535 */         localChainEnd1 = arrayOfChainEnd[(k + 1)];
/* 536 */         m++;
/* 537 */         localObject2 = localCurveLink;
/* 538 */         localCurveLink = arrayOfCurveLink[(m + 1)];
/*     */       }
/*     */     }
/* 541 */     if ((paramVector2.size() & 0x1) != 0)
/* 542 */       System.out.println("Odd number of chains!");
/*     */   }
/*     */ 
/*     */   public static boolean obstructs(double paramDouble1, double paramDouble2, int paramInt)
/*     */   {
/* 559 */     return paramDouble1 <= paramDouble2;
/*     */   }
/*     */ 
/*     */   public static class AddOp extends AreaOp.CAGOp
/*     */   {
/*     */     public boolean newClassification(boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/*  69 */       return (paramBoolean1) || (paramBoolean2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class CAGOp extends AreaOp
/*     */   {
/*     */     boolean inLeft;
/*     */     boolean inRight;
/*     */     boolean inResult;
/*     */ 
/*     */     public CAGOp()
/*     */     {
/*  34 */       super();
/*     */     }
/*     */ 
/*     */     public void newRow()
/*     */     {
/*  40 */       this.inLeft = false;
/*  41 */       this.inRight = false;
/*  42 */       this.inResult = false;
/*     */     }
/*     */ 
/*     */     public int classify(Edge paramEdge) {
/*  46 */       if (paramEdge.getCurveTag() == 0)
/*  47 */         this.inLeft = (!this.inLeft);
/*     */       else {
/*  49 */         this.inRight = (!this.inRight);
/*     */       }
/*  51 */       boolean bool = newClassification(this.inLeft, this.inRight);
/*  52 */       if (this.inResult == bool) {
/*  53 */         return 0;
/*     */       }
/*  55 */       this.inResult = bool;
/*  56 */       return bool ? 1 : -1;
/*     */     }
/*     */ 
/*     */     public int getState() {
/*  60 */       return this.inResult ? 1 : -1;
/*     */     }
/*     */ 
/*     */     public abstract boolean newClassification(boolean paramBoolean1, boolean paramBoolean2);
/*     */   }
/*     */ 
/*     */   public static class EOWindOp extends AreaOp
/*     */   {
/*     */     private boolean inside;
/*     */ 
/*     */     public EOWindOp()
/*     */     {
/* 113 */       super();
/*     */     }
/*     */ 
/*     */     public void newRow() {
/* 117 */       this.inside = false;
/*     */     }
/*     */ 
/*     */     public int classify(Edge paramEdge)
/*     */     {
/* 123 */       boolean bool = !this.inside;
/* 124 */       this.inside = bool;
/* 125 */       return bool ? 1 : -1;
/*     */     }
/*     */ 
/*     */     public int getState() {
/* 129 */       return this.inside ? 1 : -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class IntOp extends AreaOp.CAGOp
/*     */   {
/*     */     public boolean newClassification(boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/*  81 */       return (paramBoolean1) && (paramBoolean2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class NZWindOp extends AreaOp
/*     */   {
/*     */     private int count;
/*     */ 
/*     */     public NZWindOp()
/*     */     {
/*  91 */       super();
/*     */     }
/*     */ 
/*     */     public void newRow() {
/*  95 */       this.count = 0;
/*     */     }
/*     */ 
/*     */     public int classify(Edge paramEdge)
/*     */     {
/* 101 */       int i = this.count;
/* 102 */       int j = i == 0 ? 1 : 0;
/* 103 */       i += paramEdge.getCurve().getDirection();
/* 104 */       this.count = i;
/* 105 */       return i == 0 ? -1 : j;
/*     */     }
/*     */ 
/*     */     public int getState() {
/* 109 */       return this.count == 0 ? -1 : 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SubOp extends AreaOp.CAGOp
/*     */   {
/*     */     public boolean newClassification(boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/*  75 */       return (paramBoolean1) && (!paramBoolean2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class XorOp extends AreaOp.CAGOp
/*     */   {
/*     */     public boolean newClassification(boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/*  87 */       return paramBoolean1 != paramBoolean2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.geom.AreaOp
 * JD-Core Version:    0.6.2
 */