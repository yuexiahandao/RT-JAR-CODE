/*      */ package sun.awt.geom;
/*      */ 
/*      */ import java.awt.geom.IllegalPathStateException;
/*      */ import java.awt.geom.PathIterator;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public abstract class Curve
/*      */ {
/*      */   public static final int INCREASING = 1;
/*      */   public static final int DECREASING = -1;
/*      */   protected int direction;
/*      */   public static final int RECT_INTERSECTS = -2147483648;
/*      */   public static final double TMIN = 0.001D;
/*      */ 
/*      */   public static void insertMove(Vector paramVector, double paramDouble1, double paramDouble2)
/*      */   {
/*   42 */     paramVector.add(new Order0(paramDouble1, paramDouble2));
/*      */   }
/*      */ 
/*      */   public static void insertLine(Vector paramVector, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/*   49 */     if (paramDouble2 < paramDouble4) {
/*   50 */       paramVector.add(new Order1(paramDouble1, paramDouble2, paramDouble3, paramDouble4, 1));
/*      */     }
/*   53 */     else if (paramDouble2 > paramDouble4)
/*   54 */       paramVector.add(new Order1(paramDouble3, paramDouble4, paramDouble1, paramDouble2, -1));
/*      */   }
/*      */ 
/*      */   public static void insertQuad(Vector paramVector, double paramDouble1, double paramDouble2, double[] paramArrayOfDouble)
/*      */   {
/*   66 */     double d = paramArrayOfDouble[3];
/*   67 */     if (paramDouble2 > d) {
/*   68 */       Order2.insert(paramVector, paramArrayOfDouble, paramArrayOfDouble[2], d, paramArrayOfDouble[0], paramArrayOfDouble[1], paramDouble1, paramDouble2, -1);
/*      */     }
/*      */     else
/*      */     {
/*   73 */       if ((paramDouble2 == d) && (paramDouble2 == paramArrayOfDouble[1]))
/*      */       {
/*   75 */         return;
/*      */       }
/*   77 */       Order2.insert(paramVector, paramArrayOfDouble, paramDouble1, paramDouble2, paramArrayOfDouble[0], paramArrayOfDouble[1], paramArrayOfDouble[2], d, 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void insertCubic(Vector paramVector, double paramDouble1, double paramDouble2, double[] paramArrayOfDouble)
/*      */   {
/*   89 */     double d = paramArrayOfDouble[5];
/*   90 */     if (paramDouble2 > d) {
/*   91 */       Order3.insert(paramVector, paramArrayOfDouble, paramArrayOfDouble[4], d, paramArrayOfDouble[2], paramArrayOfDouble[3], paramArrayOfDouble[0], paramArrayOfDouble[1], paramDouble1, paramDouble2, -1);
/*      */     }
/*      */     else
/*      */     {
/*   97 */       if ((paramDouble2 == d) && (paramDouble2 == paramArrayOfDouble[1]) && (paramDouble2 == paramArrayOfDouble[3]))
/*      */       {
/*   99 */         return;
/*      */       }
/*  101 */       Order3.insert(paramVector, paramArrayOfDouble, paramDouble1, paramDouble2, paramArrayOfDouble[0], paramArrayOfDouble[1], paramArrayOfDouble[2], paramArrayOfDouble[3], paramArrayOfDouble[4], d, 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static int pointCrossingsForPath(PathIterator paramPathIterator, double paramDouble1, double paramDouble2)
/*      */   {
/*  127 */     if (paramPathIterator.isDone()) {
/*  128 */       return 0;
/*      */     }
/*  130 */     double[] arrayOfDouble = new double[6];
/*  131 */     if (paramPathIterator.currentSegment(arrayOfDouble) != 0) {
/*  132 */       throw new IllegalPathStateException("missing initial moveto in path definition");
/*      */     }
/*      */ 
/*  135 */     paramPathIterator.next();
/*  136 */     double d1 = arrayOfDouble[0];
/*  137 */     double d2 = arrayOfDouble[1];
/*  138 */     double d3 = d1;
/*  139 */     double d4 = d2;
/*      */ 
/*  141 */     int i = 0;
/*  142 */     while (!paramPathIterator.isDone())
/*      */     {
/*      */       double d5;
/*      */       double d6;
/*  143 */       switch (paramPathIterator.currentSegment(arrayOfDouble)) {
/*      */       case 0:
/*  145 */         if (d4 != d2) {
/*  146 */           i += pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d1, d2);
/*      */         }
/*      */ 
/*  150 */         d1 = d3 = arrayOfDouble[0];
/*  151 */         d2 = d4 = arrayOfDouble[1];
/*  152 */         break;
/*      */       case 1:
/*  154 */         d5 = arrayOfDouble[0];
/*  155 */         d6 = arrayOfDouble[1];
/*  156 */         i += pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d5, d6);
/*      */ 
/*  159 */         d3 = d5;
/*  160 */         d4 = d6;
/*  161 */         break;
/*      */       case 2:
/*  163 */         d5 = arrayOfDouble[2];
/*  164 */         d6 = arrayOfDouble[3];
/*  165 */         i += pointCrossingsForQuad(paramDouble1, paramDouble2, d3, d4, arrayOfDouble[0], arrayOfDouble[1], d5, d6, 0);
/*      */ 
/*  169 */         d3 = d5;
/*  170 */         d4 = d6;
/*  171 */         break;
/*      */       case 3:
/*  173 */         d5 = arrayOfDouble[4];
/*  174 */         d6 = arrayOfDouble[5];
/*  175 */         i += pointCrossingsForCubic(paramDouble1, paramDouble2, d3, d4, arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2], arrayOfDouble[3], d5, d6, 0);
/*      */ 
/*  180 */         d3 = d5;
/*  181 */         d4 = d6;
/*  182 */         break;
/*      */       case 4:
/*  184 */         if (d4 != d2) {
/*  185 */           i += pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d1, d2);
/*      */         }
/*      */ 
/*  189 */         d3 = d1;
/*  190 */         d4 = d2;
/*      */       }
/*      */ 
/*  193 */       paramPathIterator.next();
/*      */     }
/*  195 */     if (d4 != d2) {
/*  196 */       i += pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d1, d2);
/*      */     }
/*      */ 
/*  200 */     return i;
/*      */   }
/*      */ 
/*      */   public static int pointCrossingsForLine(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */   {
/*  214 */     if ((paramDouble2 < paramDouble4) && (paramDouble2 < paramDouble6)) return 0;
/*  215 */     if ((paramDouble2 >= paramDouble4) && (paramDouble2 >= paramDouble6)) return 0;
/*      */ 
/*  217 */     if ((paramDouble1 >= paramDouble3) && (paramDouble1 >= paramDouble5)) return 0;
/*  218 */     if ((paramDouble1 < paramDouble3) && (paramDouble1 < paramDouble5)) return paramDouble4 < paramDouble6 ? 1 : -1;
/*  219 */     double d = paramDouble3 + (paramDouble2 - paramDouble4) * (paramDouble5 - paramDouble3) / (paramDouble6 - paramDouble4);
/*  220 */     if (paramDouble1 >= d) return 0;
/*  221 */     return paramDouble4 < paramDouble6 ? 1 : -1;
/*      */   }
/*      */ 
/*      */   public static int pointCrossingsForQuad(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, int paramInt)
/*      */   {
/*  239 */     if ((paramDouble2 < paramDouble4) && (paramDouble2 < paramDouble6) && (paramDouble2 < paramDouble8)) return 0;
/*  240 */     if ((paramDouble2 >= paramDouble4) && (paramDouble2 >= paramDouble6) && (paramDouble2 >= paramDouble8)) return 0;
/*      */ 
/*  242 */     if ((paramDouble1 >= paramDouble3) && (paramDouble1 >= paramDouble5) && (paramDouble1 >= paramDouble7)) return 0;
/*  243 */     if ((paramDouble1 < paramDouble3) && (paramDouble1 < paramDouble5) && (paramDouble1 < paramDouble7)) {
/*  244 */       if (paramDouble2 >= paramDouble4) {
/*  245 */         if (paramDouble2 < paramDouble8) return 1;
/*      */ 
/*      */       }
/*  248 */       else if (paramDouble2 >= paramDouble8) return -1;
/*      */ 
/*  251 */       return 0;
/*      */     }
/*      */ 
/*  254 */     if (paramInt > 52) return pointCrossingsForLine(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble7, paramDouble8);
/*  255 */     double d1 = (paramDouble3 + paramDouble5) / 2.0D;
/*  256 */     double d2 = (paramDouble4 + paramDouble6) / 2.0D;
/*  257 */     double d3 = (paramDouble5 + paramDouble7) / 2.0D;
/*  258 */     double d4 = (paramDouble6 + paramDouble8) / 2.0D;
/*  259 */     paramDouble5 = (d1 + d3) / 2.0D;
/*  260 */     paramDouble6 = (d2 + d4) / 2.0D;
/*  261 */     if ((Double.isNaN(paramDouble5)) || (Double.isNaN(paramDouble6)))
/*      */     {
/*  265 */       return 0;
/*      */     }
/*  267 */     return pointCrossingsForQuad(paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, paramDouble5, paramDouble6, paramInt + 1) + pointCrossingsForQuad(paramDouble1, paramDouble2, paramDouble5, paramDouble6, d3, d4, paramDouble7, paramDouble8, paramInt + 1);
/*      */   }
/*      */ 
/*      */   public static int pointCrossingsForCubic(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, int paramInt)
/*      */   {
/*  291 */     if ((paramDouble2 < paramDouble4) && (paramDouble2 < paramDouble6) && (paramDouble2 < paramDouble8) && (paramDouble2 < paramDouble10)) return 0;
/*  292 */     if ((paramDouble2 >= paramDouble4) && (paramDouble2 >= paramDouble6) && (paramDouble2 >= paramDouble8) && (paramDouble2 >= paramDouble10)) return 0;
/*      */ 
/*  294 */     if ((paramDouble1 >= paramDouble3) && (paramDouble1 >= paramDouble5) && (paramDouble1 >= paramDouble7) && (paramDouble1 >= paramDouble9)) return 0;
/*  295 */     if ((paramDouble1 < paramDouble3) && (paramDouble1 < paramDouble5) && (paramDouble1 < paramDouble7) && (paramDouble1 < paramDouble9)) {
/*  296 */       if (paramDouble2 >= paramDouble4) {
/*  297 */         if (paramDouble2 < paramDouble10) return 1;
/*      */ 
/*      */       }
/*  300 */       else if (paramDouble2 >= paramDouble10) return -1;
/*      */ 
/*  303 */       return 0;
/*      */     }
/*      */ 
/*  306 */     if (paramInt > 52) return pointCrossingsForLine(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble9, paramDouble10);
/*  307 */     double d1 = (paramDouble5 + paramDouble7) / 2.0D;
/*  308 */     double d2 = (paramDouble6 + paramDouble8) / 2.0D;
/*  309 */     paramDouble5 = (paramDouble3 + paramDouble5) / 2.0D;
/*  310 */     paramDouble6 = (paramDouble4 + paramDouble6) / 2.0D;
/*  311 */     paramDouble7 = (paramDouble7 + paramDouble9) / 2.0D;
/*  312 */     paramDouble8 = (paramDouble8 + paramDouble10) / 2.0D;
/*  313 */     double d3 = (paramDouble5 + d1) / 2.0D;
/*  314 */     double d4 = (paramDouble6 + d2) / 2.0D;
/*  315 */     double d5 = (d1 + paramDouble7) / 2.0D;
/*  316 */     double d6 = (d2 + paramDouble8) / 2.0D;
/*  317 */     d1 = (d3 + d5) / 2.0D;
/*  318 */     d2 = (d4 + d6) / 2.0D;
/*  319 */     if ((Double.isNaN(d1)) || (Double.isNaN(d2)))
/*      */     {
/*  323 */       return 0;
/*      */     }
/*  325 */     return pointCrossingsForCubic(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, d3, d4, d1, d2, paramInt + 1) + pointCrossingsForCubic(paramDouble1, paramDouble2, d1, d2, d5, d6, paramDouble7, paramDouble8, paramDouble9, paramDouble10, paramInt + 1);
/*      */   }
/*      */ 
/*      */   public static int rectCrossingsForPath(PathIterator paramPathIterator, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/*  381 */     if ((paramDouble3 <= paramDouble1) || (paramDouble4 <= paramDouble2)) {
/*  382 */       return 0;
/*      */     }
/*  384 */     if (paramPathIterator.isDone()) {
/*  385 */       return 0;
/*      */     }
/*  387 */     double[] arrayOfDouble = new double[6];
/*  388 */     if (paramPathIterator.currentSegment(arrayOfDouble) != 0) {
/*  389 */       throw new IllegalPathStateException("missing initial moveto in path definition");
/*      */     }
/*      */ 
/*  392 */     paramPathIterator.next();
/*      */     double d3;
/*  394 */     double d1 = d3 = arrayOfDouble[0];
/*      */     double d4;
/*  395 */     double d2 = d4 = arrayOfDouble[1];
/*  396 */     int i = 0;
/*  397 */     while ((i != -2147483648) && (!paramPathIterator.isDone()))
/*      */     {
/*      */       double d5;
/*      */       double d6;
/*  398 */       switch (paramPathIterator.currentSegment(arrayOfDouble)) {
/*      */       case 0:
/*  400 */         if ((d1 != d3) || (d2 != d4)) {
/*  401 */           i = rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4);
/*      */         }
/*      */ 
/*  409 */         d3 = d1 = arrayOfDouble[0];
/*  410 */         d4 = d2 = arrayOfDouble[1];
/*  411 */         break;
/*      */       case 1:
/*  413 */         d5 = arrayOfDouble[0];
/*  414 */         d6 = arrayOfDouble[1];
/*  415 */         i = rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d5, d6);
/*      */ 
/*  420 */         d1 = d5;
/*  421 */         d2 = d6;
/*  422 */         break;
/*      */       case 2:
/*  424 */         d5 = arrayOfDouble[2];
/*  425 */         d6 = arrayOfDouble[3];
/*  426 */         i = rectCrossingsForQuad(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, arrayOfDouble[0], arrayOfDouble[1], d5, d6, 0);
/*      */ 
/*  432 */         d1 = d5;
/*  433 */         d2 = d6;
/*  434 */         break;
/*      */       case 3:
/*  436 */         d5 = arrayOfDouble[4];
/*  437 */         d6 = arrayOfDouble[5];
/*  438 */         i = rectCrossingsForCubic(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2], arrayOfDouble[3], d5, d6, 0);
/*      */ 
/*  445 */         d1 = d5;
/*  446 */         d2 = d6;
/*  447 */         break;
/*      */       case 4:
/*  449 */         if ((d1 != d3) || (d2 != d4)) {
/*  450 */           i = rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4);
/*      */         }
/*      */ 
/*  456 */         d1 = d3;
/*  457 */         d2 = d4;
/*      */       }
/*      */ 
/*  462 */       paramPathIterator.next();
/*      */     }
/*  464 */     if ((i != -2147483648) && ((d1 != d3) || (d2 != d4))) {
/*  465 */       i = rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4);
/*      */     }
/*      */ 
/*  473 */     return i;
/*      */   }
/*      */ 
/*      */   public static int rectCrossingsForLine(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8)
/*      */   {
/*  487 */     if ((paramDouble6 >= paramDouble4) && (paramDouble8 >= paramDouble4)) return paramInt;
/*  488 */     if ((paramDouble6 <= paramDouble2) && (paramDouble8 <= paramDouble2)) return paramInt;
/*  489 */     if ((paramDouble5 <= paramDouble1) && (paramDouble7 <= paramDouble1)) return paramInt;
/*  490 */     if ((paramDouble5 >= paramDouble3) && (paramDouble7 >= paramDouble3))
/*      */     {
/*  496 */       if (paramDouble6 < paramDouble8)
/*      */       {
/*  499 */         if (paramDouble6 <= paramDouble2) paramInt++;
/*  500 */         if (paramDouble8 >= paramDouble4) paramInt++; 
/*      */       }
/*  501 */       else if (paramDouble8 < paramDouble6)
/*      */       {
/*  504 */         if (paramDouble8 <= paramDouble2) paramInt--;
/*  505 */         if (paramDouble6 >= paramDouble4) paramInt--;
/*      */       }
/*  507 */       return paramInt;
/*      */     }
/*      */ 
/*  513 */     if (((paramDouble5 > paramDouble1) && (paramDouble5 < paramDouble3) && (paramDouble6 > paramDouble2) && (paramDouble6 < paramDouble4)) || ((paramDouble7 > paramDouble1) && (paramDouble7 < paramDouble3) && (paramDouble8 > paramDouble2) && (paramDouble8 < paramDouble4)))
/*      */     {
/*  516 */       return -2147483648;
/*      */     }
/*      */ 
/*  520 */     double d1 = paramDouble5;
/*  521 */     if (paramDouble6 < paramDouble2)
/*  522 */       d1 += (paramDouble2 - paramDouble6) * (paramDouble7 - paramDouble5) / (paramDouble8 - paramDouble6);
/*  523 */     else if (paramDouble6 > paramDouble4) {
/*  524 */       d1 += (paramDouble4 - paramDouble6) * (paramDouble7 - paramDouble5) / (paramDouble8 - paramDouble6);
/*      */     }
/*  526 */     double d2 = paramDouble7;
/*  527 */     if (paramDouble8 < paramDouble2)
/*  528 */       d2 += (paramDouble2 - paramDouble8) * (paramDouble5 - paramDouble7) / (paramDouble6 - paramDouble8);
/*  529 */     else if (paramDouble8 > paramDouble4) {
/*  530 */       d2 += (paramDouble4 - paramDouble8) * (paramDouble5 - paramDouble7) / (paramDouble6 - paramDouble8);
/*      */     }
/*  532 */     if ((d1 <= paramDouble1) && (d2 <= paramDouble1)) return paramInt;
/*  533 */     if ((d1 >= paramDouble3) && (d2 >= paramDouble3)) {
/*  534 */       if (paramDouble6 < paramDouble8)
/*      */       {
/*  537 */         if (paramDouble6 <= paramDouble2) paramInt++;
/*  538 */         if (paramDouble8 >= paramDouble4) paramInt++; 
/*      */       }
/*  539 */       else if (paramDouble8 < paramDouble6)
/*      */       {
/*  542 */         if (paramDouble8 <= paramDouble2) paramInt--;
/*  543 */         if (paramDouble6 >= paramDouble4) paramInt--;
/*      */       }
/*  545 */       return paramInt;
/*      */     }
/*  547 */     return -2147483648;
/*      */   }
/*      */ 
/*      */   public static int rectCrossingsForQuad(int paramInt1, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, int paramInt2)
/*      */   {
/*  563 */     if ((paramDouble6 >= paramDouble4) && (paramDouble8 >= paramDouble4) && (paramDouble10 >= paramDouble4)) return paramInt1;
/*  564 */     if ((paramDouble6 <= paramDouble2) && (paramDouble8 <= paramDouble2) && (paramDouble10 <= paramDouble2)) return paramInt1;
/*  565 */     if ((paramDouble5 <= paramDouble1) && (paramDouble7 <= paramDouble1) && (paramDouble9 <= paramDouble1)) return paramInt1;
/*  566 */     if ((paramDouble5 >= paramDouble3) && (paramDouble7 >= paramDouble3) && (paramDouble9 >= paramDouble3))
/*      */     {
/*  575 */       if (paramDouble6 < paramDouble10)
/*      */       {
/*  577 */         if ((paramDouble6 <= paramDouble2) && (paramDouble10 > paramDouble2)) paramInt1++;
/*  578 */         if ((paramDouble6 < paramDouble4) && (paramDouble10 >= paramDouble4)) paramInt1++; 
/*      */       }
/*  579 */       else if (paramDouble10 < paramDouble6)
/*      */       {
/*  581 */         if ((paramDouble10 <= paramDouble2) && (paramDouble6 > paramDouble2)) paramInt1--;
/*  582 */         if ((paramDouble10 < paramDouble4) && (paramDouble6 >= paramDouble4)) paramInt1--;
/*      */       }
/*  584 */       return paramInt1;
/*      */     }
/*      */ 
/*  589 */     if (((paramDouble5 < paramDouble3) && (paramDouble5 > paramDouble1) && (paramDouble6 < paramDouble4) && (paramDouble6 > paramDouble2)) || ((paramDouble9 < paramDouble3) && (paramDouble9 > paramDouble1) && (paramDouble10 < paramDouble4) && (paramDouble10 > paramDouble2)))
/*      */     {
/*  592 */       return -2147483648;
/*      */     }
/*      */ 
/*  596 */     if (paramInt2 > 52) {
/*  597 */       return rectCrossingsForLine(paramInt1, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble9, paramDouble10);
/*      */     }
/*      */ 
/*  601 */     double d1 = (paramDouble5 + paramDouble7) / 2.0D;
/*  602 */     double d2 = (paramDouble6 + paramDouble8) / 2.0D;
/*  603 */     double d3 = (paramDouble7 + paramDouble9) / 2.0D;
/*  604 */     double d4 = (paramDouble8 + paramDouble10) / 2.0D;
/*  605 */     paramDouble7 = (d1 + d3) / 2.0D;
/*  606 */     paramDouble8 = (d2 + d4) / 2.0D;
/*  607 */     if ((Double.isNaN(paramDouble7)) || (Double.isNaN(paramDouble8)))
/*      */     {
/*  611 */       return 0;
/*      */     }
/*  613 */     paramInt1 = rectCrossingsForQuad(paramInt1, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, d1, d2, paramDouble7, paramDouble8, paramInt2 + 1);
/*      */ 
/*  617 */     if (paramInt1 != -2147483648) {
/*  618 */       paramInt1 = rectCrossingsForQuad(paramInt1, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble7, paramDouble8, d3, d4, paramDouble9, paramDouble10, paramInt2 + 1);
/*      */     }
/*      */ 
/*  623 */     return paramInt1;
/*      */   }
/*      */ 
/*      */   public static int rectCrossingsForCubic(int paramInt1, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12, int paramInt2)
/*      */   {
/*  640 */     if ((paramDouble6 >= paramDouble4) && (paramDouble8 >= paramDouble4) && (paramDouble10 >= paramDouble4) && (paramDouble12 >= paramDouble4)) {
/*  641 */       return paramInt1;
/*      */     }
/*  643 */     if ((paramDouble6 <= paramDouble2) && (paramDouble8 <= paramDouble2) && (paramDouble10 <= paramDouble2) && (paramDouble12 <= paramDouble2)) {
/*  644 */       return paramInt1;
/*      */     }
/*  646 */     if ((paramDouble5 <= paramDouble1) && (paramDouble7 <= paramDouble1) && (paramDouble9 <= paramDouble1) && (paramDouble11 <= paramDouble1)) {
/*  647 */       return paramInt1;
/*      */     }
/*  649 */     if ((paramDouble5 >= paramDouble3) && (paramDouble7 >= paramDouble3) && (paramDouble9 >= paramDouble3) && (paramDouble11 >= paramDouble3))
/*      */     {
/*  658 */       if (paramDouble6 < paramDouble12)
/*      */       {
/*  660 */         if ((paramDouble6 <= paramDouble2) && (paramDouble12 > paramDouble2)) paramInt1++;
/*  661 */         if ((paramDouble6 < paramDouble4) && (paramDouble12 >= paramDouble4)) paramInt1++; 
/*      */       }
/*  662 */       else if (paramDouble12 < paramDouble6)
/*      */       {
/*  664 */         if ((paramDouble12 <= paramDouble2) && (paramDouble6 > paramDouble2)) paramInt1--;
/*  665 */         if ((paramDouble12 < paramDouble4) && (paramDouble6 >= paramDouble4)) paramInt1--;
/*      */       }
/*  667 */       return paramInt1;
/*      */     }
/*      */ 
/*  672 */     if (((paramDouble5 > paramDouble1) && (paramDouble5 < paramDouble3) && (paramDouble6 > paramDouble2) && (paramDouble6 < paramDouble4)) || ((paramDouble11 > paramDouble1) && (paramDouble11 < paramDouble3) && (paramDouble12 > paramDouble2) && (paramDouble12 < paramDouble4)))
/*      */     {
/*  675 */       return -2147483648;
/*      */     }
/*      */ 
/*  679 */     if (paramInt2 > 52) {
/*  680 */       return rectCrossingsForLine(paramInt1, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble11, paramDouble12);
/*      */     }
/*      */ 
/*  684 */     double d1 = (paramDouble7 + paramDouble9) / 2.0D;
/*  685 */     double d2 = (paramDouble8 + paramDouble10) / 2.0D;
/*  686 */     paramDouble7 = (paramDouble5 + paramDouble7) / 2.0D;
/*  687 */     paramDouble8 = (paramDouble6 + paramDouble8) / 2.0D;
/*  688 */     paramDouble9 = (paramDouble9 + paramDouble11) / 2.0D;
/*  689 */     paramDouble10 = (paramDouble10 + paramDouble12) / 2.0D;
/*  690 */     double d3 = (paramDouble7 + d1) / 2.0D;
/*  691 */     double d4 = (paramDouble8 + d2) / 2.0D;
/*  692 */     double d5 = (d1 + paramDouble9) / 2.0D;
/*  693 */     double d6 = (d2 + paramDouble10) / 2.0D;
/*  694 */     d1 = (d3 + d5) / 2.0D;
/*  695 */     d2 = (d4 + d6) / 2.0D;
/*  696 */     if ((Double.isNaN(d1)) || (Double.isNaN(d2)))
/*      */     {
/*  700 */       return 0;
/*      */     }
/*  702 */     paramInt1 = rectCrossingsForCubic(paramInt1, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8, d3, d4, d1, d2, paramInt2 + 1);
/*      */ 
/*  706 */     if (paramInt1 != -2147483648) {
/*  707 */       paramInt1 = rectCrossingsForCubic(paramInt1, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d5, d6, paramDouble9, paramDouble10, paramDouble11, paramDouble12, paramInt2 + 1);
/*      */     }
/*      */ 
/*  712 */     return paramInt1;
/*      */   }
/*      */ 
/*      */   public Curve(int paramInt) {
/*  716 */     this.direction = paramInt;
/*      */   }
/*      */ 
/*      */   public final int getDirection() {
/*  720 */     return this.direction;
/*      */   }
/*      */ 
/*      */   public final Curve getWithDirection(int paramInt) {
/*  724 */     return this.direction == paramInt ? this : getReversedCurve();
/*      */   }
/*      */ 
/*      */   public static double round(double paramDouble)
/*      */   {
/*  729 */     return paramDouble;
/*      */   }
/*      */ 
/*      */   public static int orderof(double paramDouble1, double paramDouble2) {
/*  733 */     if (paramDouble1 < paramDouble2) {
/*  734 */       return -1;
/*      */     }
/*  736 */     if (paramDouble1 > paramDouble2) {
/*  737 */       return 1;
/*      */     }
/*  739 */     return 0;
/*      */   }
/*      */ 
/*      */   public static long signeddiffbits(double paramDouble1, double paramDouble2) {
/*  743 */     return Double.doubleToLongBits(paramDouble1) - Double.doubleToLongBits(paramDouble2);
/*      */   }
/*      */   public static long diffbits(double paramDouble1, double paramDouble2) {
/*  746 */     return Math.abs(Double.doubleToLongBits(paramDouble1) - Double.doubleToLongBits(paramDouble2));
/*      */   }
/*      */ 
/*      */   public static double prev(double paramDouble) {
/*  750 */     return Double.longBitsToDouble(Double.doubleToLongBits(paramDouble) - 1L);
/*      */   }
/*      */   public static double next(double paramDouble) {
/*  753 */     return Double.longBitsToDouble(Double.doubleToLongBits(paramDouble) + 1L);
/*      */   }
/*      */ 
/*      */   public String toString() {
/*  757 */     return "Curve[" + getOrder() + ", " + "(" + round(getX0()) + ", " + round(getY0()) + "), " + controlPointString() + "(" + round(getX1()) + ", " + round(getY1()) + "), " + (this.direction == 1 ? "D" : "U") + "]";
/*      */   }
/*      */ 
/*      */   public String controlPointString()
/*      */   {
/*  767 */     return ""; } 
/*      */   public abstract int getOrder();
/*      */ 
/*      */   public abstract double getXTop();
/*      */ 
/*      */   public abstract double getYTop();
/*      */ 
/*      */   public abstract double getXBot();
/*      */ 
/*      */   public abstract double getYBot();
/*      */ 
/*      */   public abstract double getXMin();
/*      */ 
/*      */   public abstract double getXMax();
/*      */ 
/*      */   public abstract double getX0();
/*      */ 
/*      */   public abstract double getY0();
/*      */ 
/*      */   public abstract double getX1();
/*      */ 
/*      */   public abstract double getY1();
/*      */ 
/*      */   public abstract double XforY(double paramDouble);
/*      */ 
/*      */   public abstract double TforY(double paramDouble);
/*      */ 
/*      */   public abstract double XforT(double paramDouble);
/*      */ 
/*      */   public abstract double YforT(double paramDouble);
/*      */ 
/*      */   public abstract double dXforT(double paramDouble, int paramInt);
/*      */ 
/*      */   public abstract double dYforT(double paramDouble, int paramInt);
/*      */ 
/*      */   public abstract double nextVertical(double paramDouble1, double paramDouble2);
/*      */ 
/*  795 */   public int crossingsFor(double paramDouble1, double paramDouble2) { if ((paramDouble2 >= getYTop()) && (paramDouble2 < getYBot()) && 
/*  796 */       (paramDouble1 < getXMax()) && (
/*  796 */       (paramDouble1 < getXMin()) || (paramDouble1 < XforY(paramDouble2)))) {
/*  797 */       return 1;
/*      */     }
/*      */ 
/*  800 */     return 0; }
/*      */ 
/*      */   public boolean accumulateCrossings(Crossings paramCrossings)
/*      */   {
/*  804 */     double d1 = paramCrossings.getXHi();
/*  805 */     if (getXMin() >= d1) {
/*  806 */       return false;
/*      */     }
/*  808 */     double d2 = paramCrossings.getXLo();
/*  809 */     double d3 = paramCrossings.getYLo();
/*  810 */     double d4 = paramCrossings.getYHi();
/*  811 */     double d5 = getYTop();
/*  812 */     double d6 = getYBot();
/*      */     double d8;
/*      */     double d7;
/*  814 */     if (d5 < d3) {
/*  815 */       if (d6 <= d3) {
/*  816 */         return false;
/*      */       }
/*  818 */       d8 = d3;
/*  819 */       d7 = TforY(d3);
/*      */     } else {
/*  821 */       if (d5 >= d4) {
/*  822 */         return false;
/*      */       }
/*  824 */       d8 = d5;
/*  825 */       d7 = 0.0D;
/*      */     }
/*      */     double d10;
/*      */     double d9;
/*  827 */     if (d6 > d4) {
/*  828 */       d10 = d4;
/*  829 */       d9 = TforY(d4);
/*      */     } else {
/*  831 */       d10 = d6;
/*  832 */       d9 = 1.0D;
/*      */     }
/*  834 */     int i = 0;
/*  835 */     int j = 0;
/*      */     while (true) {
/*  837 */       double d11 = XforT(d7);
/*  838 */       if (d11 < d1) {
/*  839 */         if ((j != 0) || (d11 > d2)) {
/*  840 */           return true;
/*      */         }
/*  842 */         i = 1;
/*      */       } else {
/*  844 */         if (i != 0) {
/*  845 */           return true;
/*      */         }
/*  847 */         j = 1;
/*      */       }
/*  849 */       if (d7 >= d9) {
/*      */         break;
/*      */       }
/*  852 */       d7 = nextVertical(d7, d9);
/*      */     }
/*  854 */     if (i != 0) {
/*  855 */       paramCrossings.record(d8, d10, this.direction);
/*      */     }
/*  857 */     return false;
/*      */   }
/*      */ 
/*      */   public abstract void enlarge(Rectangle2D paramRectangle2D);
/*      */ 
/*      */   public Curve getSubCurve(double paramDouble1, double paramDouble2) {
/*  863 */     return getSubCurve(paramDouble1, paramDouble2, this.direction);
/*      */   }
/*      */ 
/*      */   public abstract Curve getReversedCurve();
/*      */ 
/*      */   public abstract Curve getSubCurve(double paramDouble1, double paramDouble2, int paramInt);
/*      */ 
/*      */   public int compareTo(Curve paramCurve, double[] paramArrayOfDouble)
/*      */   {
/*  874 */     double d1 = paramArrayOfDouble[0];
/*  875 */     double d2 = paramArrayOfDouble[1];
/*  876 */     d2 = Math.min(Math.min(d2, getYBot()), paramCurve.getYBot());
/*  877 */     if (d2 <= paramArrayOfDouble[0]) {
/*  878 */       System.err.println("this == " + this);
/*  879 */       System.err.println("that == " + paramCurve);
/*  880 */       System.out.println("target range = " + paramArrayOfDouble[0] + "=>" + paramArrayOfDouble[1]);
/*  881 */       throw new InternalError("backstepping from " + paramArrayOfDouble[0] + " to " + d2);
/*      */     }
/*  883 */     paramArrayOfDouble[1] = d2;
/*  884 */     if (getXMax() <= paramCurve.getXMin()) {
/*  885 */       if (getXMin() == paramCurve.getXMax()) {
/*  886 */         return 0;
/*      */       }
/*  888 */       return -1;
/*      */     }
/*  890 */     if (getXMin() >= paramCurve.getXMax()) {
/*  891 */       return 1;
/*      */     }
/*      */ 
/*  899 */     double d3 = TforY(d1);
/*  900 */     double d4 = YforT(d3);
/*  901 */     if (d4 < d1) {
/*  902 */       d3 = refineTforY(d3, d4, d1);
/*  903 */       d4 = YforT(d3);
/*      */     }
/*  905 */     double d5 = TforY(d2);
/*  906 */     if (YforT(d5) < d1) {
/*  907 */       d5 = refineTforY(d5, YforT(d5), d1);
/*      */     }
/*      */ 
/*  910 */     double d6 = paramCurve.TforY(d1);
/*  911 */     double d7 = paramCurve.YforT(d6);
/*  912 */     if (d7 < d1) {
/*  913 */       d6 = paramCurve.refineTforY(d6, d7, d1);
/*  914 */       d7 = paramCurve.YforT(d6);
/*      */     }
/*  916 */     double d8 = paramCurve.TforY(d2);
/*  917 */     if (paramCurve.YforT(d8) < d1) {
/*  918 */       d8 = paramCurve.refineTforY(d8, paramCurve.YforT(d8), d1);
/*      */     }
/*      */ 
/*  921 */     double d9 = XforT(d3);
/*  922 */     double d10 = paramCurve.XforT(d6);
/*  923 */     double d11 = Math.max(Math.abs(d1), Math.abs(d2));
/*  924 */     double d12 = Math.max(d11 * 1.0E-014D, 1.0E-300D);
/*      */     double d14;
/*      */     double d15;
/*      */     double d16;
/*  925 */     if (fairlyClose(d9, d10)) {
/*  926 */       d13 = d12;
/*  927 */       d14 = Math.min(d12 * 10000000000000.0D, (d2 - d1) * 0.1D);
/*  928 */       d15 = d1 + d13;
/*  929 */       while (d15 <= d2) {
/*  930 */         if (fairlyClose(XforY(d15), paramCurve.XforY(d15))) {
/*  931 */           if (d13 *= 2.0D > d14)
/*  932 */             d13 = d14;
/*      */         }
/*      */         else {
/*  935 */           d15 -= d13;
/*      */           while (true) {
/*  937 */             d13 /= 2.0D;
/*  938 */             d16 = d15 + d13;
/*  939 */             if (d16 <= d15) {
/*      */               break;
/*      */             }
/*  942 */             if (fairlyClose(XforY(d16), paramCurve.XforY(d16))) {
/*  943 */               d15 = d16;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  948 */         d15 += d13;
/*      */       }
/*  950 */       if (d15 > d1) {
/*  951 */         if (d15 < d2) {
/*  952 */           paramArrayOfDouble[1] = d15;
/*      */         }
/*  954 */         return 0;
/*      */       }
/*      */     }
/*      */ 
/*  958 */     if (d12 <= 0.0D) {
/*  959 */       System.out.println("ymin = " + d12);
/*      */     }
/*      */ 
/*  965 */     while ((d3 < d5) && (d6 < d8)) {
/*  966 */       d13 = nextVertical(d3, d5);
/*  967 */       d14 = XforT(d13);
/*  968 */       d15 = YforT(d13);
/*  969 */       d16 = paramCurve.nextVertical(d6, d8);
/*  970 */       double d17 = paramCurve.XforT(d16);
/*  971 */       double d18 = paramCurve.YforT(d16);
/*      */       try
/*      */       {
/*  977 */         if (findIntersect(paramCurve, paramArrayOfDouble, d12, 0, 0, d3, d9, d4, d13, d14, d15, d6, d10, d7, d16, d17, d18))
/*      */         {
/*  980 */           break;
/*      */         }
/*      */       } catch (Throwable localThrowable) {
/*  983 */         System.err.println("Error: " + localThrowable);
/*  984 */         System.err.println("y range was " + paramArrayOfDouble[0] + "=>" + paramArrayOfDouble[1]);
/*  985 */         System.err.println("s y range is " + d4 + "=>" + d15);
/*  986 */         System.err.println("t y range is " + d7 + "=>" + d18);
/*  987 */         System.err.println("ymin is " + d12);
/*  988 */         return 0;
/*      */       }
/*  990 */       if (d15 < d18) {
/*  991 */         if (d15 > paramArrayOfDouble[0]) {
/*  992 */           if (d15 >= paramArrayOfDouble[1]) break;
/*  993 */           paramArrayOfDouble[1] = d15; break;
/*      */         }
/*      */ 
/*  997 */         d3 = d13;
/*  998 */         d9 = d14;
/*  999 */         d4 = d15;
/*      */       } else {
/* 1001 */         if (d18 > paramArrayOfDouble[0]) {
/* 1002 */           if (d18 >= paramArrayOfDouble[1]) break;
/* 1003 */           paramArrayOfDouble[1] = d18; break;
/*      */         }
/*      */ 
/* 1007 */         d6 = d16;
/* 1008 */         d10 = d17;
/* 1009 */         d7 = d18;
/*      */       }
/*      */     }
/* 1012 */     double d13 = (paramArrayOfDouble[0] + paramArrayOfDouble[1]) / 2.0D;
/*      */ 
/* 1028 */     return orderof(XforY(d13), paramCurve.XforY(d13));
/*      */   }
/*      */ 
/*      */   public boolean findIntersect(Curve paramCurve, double[] paramArrayOfDouble, double paramDouble1, int paramInt1, int paramInt2, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12, double paramDouble13)
/*      */   {
/* 1053 */     if ((paramDouble4 > paramDouble13) || (paramDouble10 > paramDouble7)) {
/* 1054 */       return false;
/*      */     }
/* 1056 */     if ((Math.min(paramDouble3, paramDouble6) > Math.max(paramDouble9, paramDouble12)) || (Math.max(paramDouble3, paramDouble6) < Math.min(paramDouble9, paramDouble12)))
/*      */     {
/* 1059 */       return false;
/*      */     }
/*      */     double d1;
/*      */     double d2;
/*      */     double d3;
/*      */     double d4;
/*      */     double d5;
/*      */     double d6;
/* 1065 */     if (paramDouble5 - paramDouble2 > 0.001D) {
/* 1066 */       d1 = (paramDouble2 + paramDouble5) / 2.0D;
/* 1067 */       d2 = XforT(d1);
/* 1068 */       d3 = YforT(d1);
/* 1069 */       if ((d1 == paramDouble2) || (d1 == paramDouble5)) {
/* 1070 */         System.out.println("s0 = " + paramDouble2);
/* 1071 */         System.out.println("s1 = " + paramDouble5);
/* 1072 */         throw new InternalError("no s progress!");
/*      */       }
/* 1074 */       if (paramDouble11 - paramDouble8 > 0.001D) {
/* 1075 */         d4 = (paramDouble8 + paramDouble11) / 2.0D;
/* 1076 */         d5 = paramCurve.XforT(d4);
/* 1077 */         d6 = paramCurve.YforT(d4);
/* 1078 */         if ((d4 == paramDouble8) || (d4 == paramDouble11)) {
/* 1079 */           System.out.println("t0 = " + paramDouble8);
/* 1080 */           System.out.println("t1 = " + paramDouble11);
/* 1081 */           throw new InternalError("no t progress!");
/*      */         }
/* 1083 */         if ((d3 >= paramDouble10) && (d6 >= paramDouble4) && 
/* 1084 */           (findIntersect(paramCurve, paramArrayOfDouble, paramDouble1, paramInt1 + 1, paramInt2 + 1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, paramDouble8, paramDouble9, paramDouble10, d4, d5, d6)))
/*      */         {
/* 1087 */           return true;
/*      */         }
/*      */ 
/* 1090 */         if ((d3 >= d6) && 
/* 1091 */           (findIntersect(paramCurve, paramArrayOfDouble, paramDouble1, paramInt1 + 1, paramInt2 + 1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4, d5, d6, paramDouble11, paramDouble12, paramDouble13)))
/*      */         {
/* 1094 */           return true;
/*      */         }
/*      */ 
/* 1097 */         if ((d6 >= d3) && 
/* 1098 */           (findIntersect(paramCurve, paramArrayOfDouble, paramDouble1, paramInt1 + 1, paramInt2 + 1, d1, d2, d3, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10, d4, d5, d6)))
/*      */         {
/* 1101 */           return true;
/*      */         }
/*      */ 
/* 1104 */         if ((paramDouble7 >= d6) && (paramDouble13 >= d3) && 
/* 1105 */           (findIntersect(paramCurve, paramArrayOfDouble, paramDouble1, paramInt1 + 1, paramInt2 + 1, d1, d2, d3, paramDouble5, paramDouble6, paramDouble7, d4, d5, d6, paramDouble11, paramDouble12, paramDouble13)))
/*      */         {
/* 1108 */           return true;
/*      */         }
/*      */       }
/*      */       else {
/* 1112 */         if ((d3 >= paramDouble10) && 
/* 1113 */           (findIntersect(paramCurve, paramArrayOfDouble, paramDouble1, paramInt1 + 1, paramInt2, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, paramDouble8, paramDouble9, paramDouble10, paramDouble11, paramDouble12, paramDouble13)))
/*      */         {
/* 1116 */           return true;
/*      */         }
/*      */ 
/* 1119 */         if ((paramDouble13 >= d3) && 
/* 1120 */           (findIntersect(paramCurve, paramArrayOfDouble, paramDouble1, paramInt1 + 1, paramInt2, d1, d2, d3, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10, paramDouble11, paramDouble12, paramDouble13)))
/*      */         {
/* 1123 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 1127 */     else if (paramDouble11 - paramDouble8 > 0.001D) {
/* 1128 */       d1 = (paramDouble8 + paramDouble11) / 2.0D;
/* 1129 */       d2 = paramCurve.XforT(d1);
/* 1130 */       d3 = paramCurve.YforT(d1);
/* 1131 */       if ((d1 == paramDouble8) || (d1 == paramDouble11)) {
/* 1132 */         System.out.println("t0 = " + paramDouble8);
/* 1133 */         System.out.println("t1 = " + paramDouble11);
/* 1134 */         throw new InternalError("no t progress!");
/*      */       }
/* 1136 */       if ((d3 >= paramDouble4) && 
/* 1137 */         (findIntersect(paramCurve, paramArrayOfDouble, paramDouble1, paramInt1, paramInt2 + 1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10, d1, d2, d3)))
/*      */       {
/* 1140 */         return true;
/*      */       }
/*      */ 
/* 1143 */       if ((paramDouble7 >= d3) && 
/* 1144 */         (findIntersect(paramCurve, paramArrayOfDouble, paramDouble1, paramInt1, paramInt2 + 1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, d1, d2, d3, paramDouble11, paramDouble12, paramDouble13)))
/*      */       {
/* 1147 */         return true;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1152 */       d1 = paramDouble6 - paramDouble3;
/* 1153 */       d2 = paramDouble7 - paramDouble4;
/* 1154 */       d3 = paramDouble12 - paramDouble9;
/* 1155 */       d4 = paramDouble13 - paramDouble10;
/* 1156 */       d5 = paramDouble9 - paramDouble3;
/* 1157 */       d6 = paramDouble10 - paramDouble4;
/* 1158 */       double d7 = d3 * d2 - d4 * d1;
/* 1159 */       if (d7 != 0.0D) {
/* 1160 */         double d8 = 1.0D / d7;
/* 1161 */         double d9 = (d3 * d6 - d4 * d5) * d8;
/* 1162 */         double d10 = (d1 * d6 - d2 * d5) * d8;
/* 1163 */         if ((d9 >= 0.0D) && (d9 <= 1.0D) && (d10 >= 0.0D) && (d10 <= 1.0D)) {
/* 1164 */           d9 = paramDouble2 + d9 * (paramDouble5 - paramDouble2);
/* 1165 */           d10 = paramDouble8 + d10 * (paramDouble11 - paramDouble8);
/* 1166 */           if ((d9 < 0.0D) || (d9 > 1.0D) || (d10 < 0.0D) || (d10 > 1.0D)) {
/* 1167 */             System.out.println("Uh oh!");
/*      */           }
/* 1169 */           double d11 = (YforT(d9) + paramCurve.YforT(d10)) / 2.0D;
/* 1170 */           if ((d11 <= paramArrayOfDouble[1]) && (d11 > paramArrayOfDouble[0])) {
/* 1171 */             paramArrayOfDouble[1] = d11;
/* 1172 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1178 */     return false;
/*      */   }
/*      */ 
/*      */   public double refineTforY(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 1182 */     double d1 = 1.0D;
/*      */     while (true) {
/* 1184 */       double d2 = (paramDouble1 + d1) / 2.0D;
/* 1185 */       if ((d2 == paramDouble1) || (d2 == d1)) {
/* 1186 */         return d1;
/*      */       }
/* 1188 */       double d3 = YforT(d2);
/* 1189 */       if (d3 < paramDouble3) {
/* 1190 */         paramDouble1 = d2;
/* 1191 */         paramDouble2 = d3;
/* 1192 */       } else if (d3 > paramDouble3) {
/* 1193 */         d1 = d2;
/*      */       } else {
/* 1195 */         return d1;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean fairlyClose(double paramDouble1, double paramDouble2) {
/* 1201 */     return Math.abs(paramDouble1 - paramDouble2) < Math.max(Math.abs(paramDouble1), Math.abs(paramDouble2)) * 1.0E-010D;
/*      */   }
/*      */ 
/*      */   public abstract int getSegment(double[] paramArrayOfDouble);
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.geom.Curve
 * JD-Core Version:    0.6.2
 */