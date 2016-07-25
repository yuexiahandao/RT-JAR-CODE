/*      */ package sun.java2d.loops;
/*      */ 
/*      */ import java.awt.geom.Path2D.Float;
/*      */ import java.awt.geom.PathIterator;
/*      */ import java.awt.geom.QuadCurve2D;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class ProcessPath
/*      */ {
/*  140 */   public static final int PH_MODE_DRAW_CLIP = 0;
/*      */   public static final int PH_MODE_FILL_CLIP = 1;
/*  140 */   public static EndSubPathHandler noopEndSubPathHandler = new EndSubPathHandler() { public void processEndSubPath() {  }  } ;
/*      */   private static final float UPPER_BND = 8.507059E+037F;
/*      */   private static final float LOWER_BND = -8.507059E+037F;
/*      */   private static final int FWD_PREC = 7;
/*      */   private static final int MDP_PREC = 10;
/*      */   private static final int MDP_MULT = 1024;
/*      */   private static final int MDP_HALF_MULT = 512;
/*      */   private static final int UPPER_OUT_BND = 1048576;
/*      */   private static final int LOWER_OUT_BND = -1048576;
/*      */   private static final float CALC_UBND = 1048576.0F;
/*      */   private static final float CALC_LBND = -1048576.0F;
/*      */   public static final int EPSFX = 1;
/*      */   public static final float EPSF = 0.000976563F;
/*      */   private static final int MDP_W_MASK = -1024;
/*      */   private static final int MDP_F_MASK = 1023;
/*      */   private static final int MAX_CUB_SIZE = 256;
/*      */   private static final int MAX_QUAD_SIZE = 1024;
/*      */   private static final int DF_CUB_STEPS = 3;
/*      */   private static final int DF_QUAD_STEPS = 2;
/*      */   private static final int DF_CUB_SHIFT = 6;
/*      */   private static final int DF_QUAD_SHIFT = 1;
/*      */   private static final int DF_CUB_COUNT = 8;
/*      */   private static final int DF_QUAD_COUNT = 4;
/*      */   private static final int DF_CUB_DEC_BND = 262144;
/*      */   private static final int DF_CUB_INC_BND = 32768;
/*      */   private static final int DF_QUAD_DEC_BND = 8192;
/*      */   private static final int DF_QUAD_INC_BND = 1024;
/*      */   private static final int CUB_A_SHIFT = 7;
/*      */   private static final int CUB_B_SHIFT = 11;
/*      */   private static final int CUB_C_SHIFT = 13;
/*      */   private static final int CUB_A_MDP_MULT = 128;
/*      */   private static final int CUB_B_MDP_MULT = 2048;
/*      */   private static final int CUB_C_MDP_MULT = 8192;
/*      */   private static final int QUAD_A_SHIFT = 7;
/*      */   private static final int QUAD_B_SHIFT = 9;
/*      */   private static final int QUAD_A_MDP_MULT = 128;
/*      */   private static final int QUAD_B_MDP_MULT = 512;
/*      */   private static final int CRES_MIN_CLIPPED = 0;
/*      */   private static final int CRES_MAX_CLIPPED = 1;
/*      */   private static final int CRES_NOT_CLIPPED = 3;
/*      */   private static final int CRES_INVISIBLE = 4;
/*      */   private static final int DF_MAX_POINT = 256;
/*      */ 
/*  148 */   public static boolean fillPath(DrawHandler paramDrawHandler, Path2D.Float paramFloat, int paramInt1, int paramInt2) { FillProcessHandler localFillProcessHandler = new FillProcessHandler(paramDrawHandler);
/*  149 */     if (!doProcessPath(localFillProcessHandler, paramFloat, paramInt1, paramInt2)) {
/*  150 */       return false;
/*      */     }
/*  152 */     FillPolygon(localFillProcessHandler, paramFloat.getWindingRule());
/*  153 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean drawPath(DrawHandler paramDrawHandler, EndSubPathHandler paramEndSubPathHandler, Path2D.Float paramFloat, int paramInt1, int paramInt2)
/*      */   {
/*  161 */     return doProcessPath(new DrawProcessHandler(paramDrawHandler, paramEndSubPathHandler), paramFloat, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public static boolean drawPath(DrawHandler paramDrawHandler, Path2D.Float paramFloat, int paramInt1, int paramInt2)
/*      */   {
/*  169 */     return doProcessPath(new DrawProcessHandler(paramDrawHandler, noopEndSubPathHandler), paramFloat, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private static float CLIP(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, double paramDouble)
/*      */   {
/*  283 */     return (float)(paramFloat2 + (paramDouble - paramFloat1) * (paramFloat4 - paramFloat2) / (paramFloat3 - paramFloat1));
/*      */   }
/*      */ 
/*      */   private static int CLIP(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble) {
/*  287 */     return (int)(paramInt2 + (paramDouble - paramInt1) * (paramInt4 - paramInt2) / (paramInt3 - paramInt1));
/*      */   }
/*      */ 
/*      */   private static boolean IS_CLIPPED(int paramInt)
/*      */   {
/*  297 */     return (paramInt == 0) || (paramInt == 1);
/*      */   }
/*      */ 
/*      */   private static int TESTANDCLIP(float paramFloat1, float paramFloat2, float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  307 */     int i = 3;
/*  308 */     if ((paramArrayOfFloat[paramInt1] < paramFloat1) || (paramArrayOfFloat[paramInt1] > paramFloat2))
/*      */     {
/*      */       double d;
/*  309 */       if (paramArrayOfFloat[paramInt1] < paramFloat1) {
/*  310 */         if (paramArrayOfFloat[paramInt3] < paramFloat1) {
/*  311 */           return 4;
/*      */         }
/*  313 */         i = 0;
/*  314 */         d = paramFloat1;
/*      */       } else {
/*  316 */         if (paramArrayOfFloat[paramInt3] > paramFloat2) {
/*  317 */           return 4;
/*      */         }
/*  319 */         i = 1;
/*  320 */         d = paramFloat2;
/*      */       }
/*  322 */       paramArrayOfFloat[paramInt2] = CLIP(paramArrayOfFloat[paramInt1], paramArrayOfFloat[paramInt2], paramArrayOfFloat[paramInt3], paramArrayOfFloat[paramInt4], d);
/*  323 */       paramArrayOfFloat[paramInt1] = ((float)d);
/*      */     }
/*  325 */     return i;
/*      */   }
/*      */ 
/*      */   private static int TESTANDCLIP(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  332 */     int i = 3;
/*  333 */     if ((paramArrayOfInt[paramInt3] < paramInt1) || (paramArrayOfInt[paramInt3] > paramInt2))
/*      */     {
/*      */       double d;
/*  334 */       if (paramArrayOfInt[paramInt3] < paramInt1) {
/*  335 */         if (paramArrayOfInt[paramInt5] < paramInt1) {
/*  336 */           return 4;
/*      */         }
/*  338 */         i = 0;
/*  339 */         d = paramInt1;
/*      */       } else {
/*  341 */         if (paramArrayOfInt[paramInt5] > paramInt2) {
/*  342 */           return 4;
/*      */         }
/*  344 */         i = 1;
/*  345 */         d = paramInt2;
/*      */       }
/*  347 */       paramArrayOfInt[paramInt4] = CLIP(paramArrayOfInt[paramInt3], paramArrayOfInt[paramInt4], paramArrayOfInt[paramInt5], paramArrayOfInt[paramInt6], d);
/*  348 */       paramArrayOfInt[paramInt3] = ((int)d);
/*      */     }
/*  350 */     return i;
/*      */   }
/*      */ 
/*      */   private static int CLIPCLAMP(float paramFloat1, float paramFloat2, float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  383 */     paramArrayOfFloat[paramInt5] = paramArrayOfFloat[paramInt1];
/*  384 */     paramArrayOfFloat[paramInt6] = paramArrayOfFloat[paramInt2];
/*  385 */     int i = TESTANDCLIP(paramFloat1, paramFloat2, paramArrayOfFloat, paramInt1, paramInt2, paramInt3, paramInt4);
/*  386 */     if (i == 0) {
/*  387 */       paramArrayOfFloat[paramInt5] = paramArrayOfFloat[paramInt1];
/*  388 */     } else if (i == 1) {
/*  389 */       paramArrayOfFloat[paramInt5] = paramArrayOfFloat[paramInt1];
/*  390 */       i = 1;
/*  391 */     } else if (i == 4) {
/*  392 */       if (paramArrayOfFloat[paramInt1] > paramFloat2) {
/*  393 */         i = 4;
/*      */       } else {
/*  395 */         paramArrayOfFloat[paramInt1] = paramFloat1;
/*  396 */         paramArrayOfFloat[paramInt3] = paramFloat1;
/*  397 */         i = 3;
/*      */       }
/*      */     }
/*  400 */     return i;
/*      */   }
/*      */ 
/*      */   private static int CLIPCLAMP(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
/*      */   {
/*  407 */     paramArrayOfInt[paramInt7] = paramArrayOfInt[paramInt3];
/*  408 */     paramArrayOfInt[paramInt8] = paramArrayOfInt[paramInt4];
/*  409 */     int i = TESTANDCLIP(paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5, paramInt6);
/*  410 */     if (i == 0) {
/*  411 */       paramArrayOfInt[paramInt7] = paramArrayOfInt[paramInt3];
/*  412 */     } else if (i == 1) {
/*  413 */       paramArrayOfInt[paramInt7] = paramArrayOfInt[paramInt3];
/*  414 */       i = 1;
/*  415 */     } else if (i == 4) {
/*  416 */       if (paramArrayOfInt[paramInt3] > paramInt2) {
/*  417 */         i = 4;
/*      */       } else {
/*  419 */         paramArrayOfInt[paramInt3] = paramInt1;
/*  420 */         paramArrayOfInt[paramInt5] = paramInt1;
/*  421 */         i = 3;
/*      */       }
/*      */     }
/*  424 */     return i;
/*      */   }
/*      */ 
/*      */   private static void DrawMonotonicQuad(ProcessHandler paramProcessHandler, float[] paramArrayOfFloat, boolean paramBoolean, int[] paramArrayOfInt)
/*      */   {
/*  673 */     int i = (int)(paramArrayOfFloat[0] * 1024.0F);
/*  674 */     int j = (int)(paramArrayOfFloat[1] * 1024.0F);
/*      */ 
/*  676 */     int k = (int)(paramArrayOfFloat[4] * 1024.0F);
/*  677 */     int m = (int)(paramArrayOfFloat[5] * 1024.0F);
/*      */ 
/*  680 */     int n = (i & 0x3FF) << 1;
/*  681 */     int i1 = (j & 0x3FF) << 1;
/*      */ 
/*  684 */     int i2 = 4;
/*      */ 
/*  687 */     int i3 = 1;
/*      */ 
/*  689 */     int i4 = (int)((paramArrayOfFloat[0] - 2.0F * paramArrayOfFloat[2] + paramArrayOfFloat[4]) * 128.0F);
/*      */ 
/*  691 */     int i5 = (int)((paramArrayOfFloat[1] - 2.0F * paramArrayOfFloat[3] + paramArrayOfFloat[5]) * 128.0F);
/*      */ 
/*  694 */     int i6 = (int)((-2.0F * paramArrayOfFloat[0] + 2.0F * paramArrayOfFloat[2]) * 512.0F);
/*  695 */     int i7 = (int)((-2.0F * paramArrayOfFloat[1] + 2.0F * paramArrayOfFloat[3]) * 512.0F);
/*      */ 
/*  697 */     int i8 = 2 * i4;
/*  698 */     int i9 = 2 * i5;
/*      */ 
/*  700 */     int i10 = i4 + i6;
/*  701 */     int i11 = i5 + i7;
/*      */ 
/*  705 */     int i14 = i;
/*  706 */     int i15 = j;
/*      */ 
/*  708 */     int i16 = Math.max(Math.abs(i8), Math.abs(i9));
/*      */ 
/*  710 */     int i17 = k - i;
/*  711 */     int i18 = m - j;
/*      */ 
/*  713 */     int i19 = i & 0xFFFFFC00;
/*  714 */     int i20 = j & 0xFFFFFC00;
/*      */ 
/*  722 */     while (i16 > 8192) {
/*  723 */       i10 = (i10 << 1) - i4;
/*  724 */       i11 = (i11 << 1) - i5;
/*  725 */       i2 <<= 1;
/*  726 */       i16 >>= 2;
/*  727 */       n <<= 2;
/*  728 */       i1 <<= 2;
/*  729 */       i3 += 2;
/*      */     }
/*      */ 
/*  732 */     while (i2-- > 1) {
/*  733 */       n += i10;
/*  734 */       i1 += i11;
/*      */ 
/*  736 */       i10 += i8;
/*  737 */       i11 += i9;
/*      */ 
/*  739 */       int i12 = i14;
/*  740 */       int i13 = i15;
/*      */ 
/*  742 */       i14 = i19 + (n >> i3);
/*  743 */       i15 = i20 + (i1 >> i3);
/*      */ 
/*  752 */       if ((k - i14 ^ i17) < 0) {
/*  753 */         i14 = k;
/*      */       }
/*      */ 
/*  757 */       if ((m - i15 ^ i18) < 0) {
/*  758 */         i15 = m;
/*      */       }
/*      */ 
/*  761 */       paramProcessHandler.processFixedLine(i12, i13, i14, i15, paramArrayOfInt, paramBoolean, false);
/*      */     }
/*      */ 
/*  770 */     paramProcessHandler.processFixedLine(i14, i15, k, m, paramArrayOfInt, paramBoolean, false);
/*      */   }
/*      */ 
/*      */   private static void ProcessMonotonicQuad(ProcessHandler paramProcessHandler, float[] paramArrayOfFloat, int[] paramArrayOfInt)
/*      */   {
/*  782 */     float[] arrayOfFloat = new float[6];
/*      */     float f3;
/*  786 */     float f1 = f3 = paramArrayOfFloat[0];
/*      */     float f4;
/*  787 */     float f2 = f4 = paramArrayOfFloat[1];
/*  788 */     for (int i = 2; i < 6; i += 2) {
/*  789 */       f1 = f1 > paramArrayOfFloat[i] ? paramArrayOfFloat[i] : f1;
/*  790 */       f3 = f3 < paramArrayOfFloat[i] ? paramArrayOfFloat[i] : f3;
/*  791 */       f2 = f2 > paramArrayOfFloat[(i + 1)] ? paramArrayOfFloat[(i + 1)] : f2;
/*  792 */       f4 = f4 < paramArrayOfFloat[(i + 1)] ? paramArrayOfFloat[(i + 1)] : f4;
/*      */     }
/*      */ 
/*  795 */     if (paramProcessHandler.clipMode == 0)
/*      */     {
/*  800 */       if ((paramProcessHandler.dhnd.xMaxf >= f1) && (paramProcessHandler.dhnd.xMinf <= f3) && (paramProcessHandler.dhnd.yMaxf >= f2) && (paramProcessHandler.dhnd.yMinf <= f4));
/*      */     }
/*      */     else
/*      */     {
/*  810 */       if ((paramProcessHandler.dhnd.yMaxf < f2) || (paramProcessHandler.dhnd.yMinf > f4) || (paramProcessHandler.dhnd.xMaxf < f1))
/*      */       {
/*  813 */         return;
/*      */       }
/*      */ 
/*  820 */       if (paramProcessHandler.dhnd.xMinf > f3)
/*      */       {
/*      */         float tmp257_256 = (paramArrayOfFloat[4] = paramProcessHandler.dhnd.xMinf); paramArrayOfFloat[2] = tmp257_256; paramArrayOfFloat[0] = tmp257_256;
/*      */       }
/*      */     }
/*      */ 
/*  825 */     if ((f3 - f1 > 1024.0F) || (tmp257_256 - f2 > 1024.0F)) {
/*  826 */       arrayOfFloat[4] = paramArrayOfFloat[4];
/*  827 */       arrayOfFloat[5] = paramArrayOfFloat[5];
/*  828 */       arrayOfFloat[2] = ((paramArrayOfFloat[2] + paramArrayOfFloat[4]) / 2.0F);
/*  829 */       arrayOfFloat[3] = ((paramArrayOfFloat[3] + paramArrayOfFloat[5]) / 2.0F);
/*  830 */       paramArrayOfFloat[2] = ((paramArrayOfFloat[0] + paramArrayOfFloat[2]) / 2.0F);
/*  831 */       paramArrayOfFloat[3] = ((paramArrayOfFloat[1] + paramArrayOfFloat[3]) / 2.0F);
/*      */       float tmp355_354 = ((paramArrayOfFloat[2] + arrayOfFloat[2]) / 2.0F); arrayOfFloat[0] = tmp355_354; paramArrayOfFloat[4] = tmp355_354;
/*      */       float tmp371_370 = ((paramArrayOfFloat[3] + arrayOfFloat[3]) / 2.0F); arrayOfFloat[1] = tmp371_370; paramArrayOfFloat[5] = tmp371_370;
/*      */ 
/*  835 */       ProcessMonotonicQuad(paramProcessHandler, paramArrayOfFloat, paramArrayOfInt);
/*      */ 
/*  837 */       ProcessMonotonicQuad(paramProcessHandler, arrayOfFloat, paramArrayOfInt);
/*      */     } else {
/*  839 */       DrawMonotonicQuad(paramProcessHandler, paramArrayOfFloat, (paramProcessHandler.dhnd.xMinf >= f1) || (paramProcessHandler.dhnd.xMaxf <= f3) || (paramProcessHandler.dhnd.yMinf >= f2) || (paramProcessHandler.dhnd.yMaxf <= tmp257_256), paramArrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void ProcessQuad(ProcessHandler paramProcessHandler, float[] paramArrayOfFloat, int[] paramArrayOfInt)
/*      */   {
/*  863 */     double[] arrayOfDouble = new double[2];
/*  864 */     int i = 0;
/*      */     double d2;
/*      */     double d3;
/*      */     double d1;
/*  872 */     if (((paramArrayOfFloat[0] > paramArrayOfFloat[2]) || (paramArrayOfFloat[2] > paramArrayOfFloat[4])) && ((paramArrayOfFloat[0] < paramArrayOfFloat[2]) || (paramArrayOfFloat[2] < paramArrayOfFloat[4])))
/*      */     {
/*  880 */       d2 = paramArrayOfFloat[0] - 2.0F * paramArrayOfFloat[2] + paramArrayOfFloat[4];
/*  881 */       if (d2 != 0.0D)
/*      */       {
/*  885 */         d3 = paramArrayOfFloat[0] - paramArrayOfFloat[2];
/*      */ 
/*  887 */         d1 = d3 / d2;
/*  888 */         if ((d1 < 1.0D) && (d1 > 0.0D)) {
/*  889 */           arrayOfDouble[(i++)] = d1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  899 */     if (((paramArrayOfFloat[1] > paramArrayOfFloat[3]) || (paramArrayOfFloat[3] > paramArrayOfFloat[5])) && ((paramArrayOfFloat[1] < paramArrayOfFloat[3]) || (paramArrayOfFloat[3] < paramArrayOfFloat[5])))
/*      */     {
/*  907 */       d2 = paramArrayOfFloat[1] - 2.0F * paramArrayOfFloat[3] + paramArrayOfFloat[5];
/*      */ 
/*  909 */       if (d2 != 0.0D)
/*      */       {
/*  913 */         d3 = paramArrayOfFloat[1] - paramArrayOfFloat[3];
/*      */ 
/*  915 */         d1 = d3 / d2;
/*  916 */         if ((d1 < 1.0D) && (d1 > 0.0D)) {
/*  917 */           if (i > 0)
/*      */           {
/*  921 */             if (arrayOfDouble[0] > d1) {
/*  922 */               arrayOfDouble[(i++)] = arrayOfDouble[0];
/*  923 */               arrayOfDouble[0] = d1;
/*  924 */             } else if (arrayOfDouble[0] < d1) {
/*  925 */               arrayOfDouble[(i++)] = d1;
/*      */             }
/*      */           }
/*  928 */           else arrayOfDouble[(i++)] = d1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  935 */     switch (i) {
/*      */     case 0:
/*  937 */       break;
/*      */     case 1:
/*  939 */       ProcessFirstMonotonicPartOfQuad(paramProcessHandler, paramArrayOfFloat, paramArrayOfInt, (float)arrayOfDouble[0]);
/*      */ 
/*  941 */       break;
/*      */     case 2:
/*  943 */       ProcessFirstMonotonicPartOfQuad(paramProcessHandler, paramArrayOfFloat, paramArrayOfInt, (float)arrayOfDouble[0]);
/*      */ 
/*  945 */       d1 = arrayOfDouble[1] - arrayOfDouble[0];
/*  946 */       if (d1 > 0.0D) {
/*  947 */         ProcessFirstMonotonicPartOfQuad(paramProcessHandler, paramArrayOfFloat, paramArrayOfInt, (float)(d1 / (1.0D - arrayOfDouble[0])));
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/*  956 */     ProcessMonotonicQuad(paramProcessHandler, paramArrayOfFloat, paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   private static void ProcessFirstMonotonicPartOfQuad(ProcessHandler paramProcessHandler, float[] paramArrayOfFloat, int[] paramArrayOfInt, float paramFloat)
/*      */   {
/*  969 */     float[] arrayOfFloat = new float[6];
/*      */ 
/*  971 */     arrayOfFloat[0] = paramArrayOfFloat[0];
/*  972 */     arrayOfFloat[1] = paramArrayOfFloat[1];
/*  973 */     arrayOfFloat[2] = (paramArrayOfFloat[0] + paramFloat * (paramArrayOfFloat[2] - paramArrayOfFloat[0]));
/*  974 */     arrayOfFloat[3] = (paramArrayOfFloat[1] + paramFloat * (paramArrayOfFloat[3] - paramArrayOfFloat[1]));
/*  975 */     paramArrayOfFloat[2] += paramFloat * (paramArrayOfFloat[4] - paramArrayOfFloat[2]);
/*  976 */     paramArrayOfFloat[3] += paramFloat * (paramArrayOfFloat[5] - paramArrayOfFloat[3]);
/*      */     float tmp106_105 = (arrayOfFloat[2] + paramFloat * (paramArrayOfFloat[2] - arrayOfFloat[2])); arrayOfFloat[4] = tmp106_105; paramArrayOfFloat[0] = tmp106_105;
/*      */     float tmp129_128 = (arrayOfFloat[3] + paramFloat * (paramArrayOfFloat[3] - arrayOfFloat[3])); arrayOfFloat[5] = tmp129_128; paramArrayOfFloat[1] = tmp129_128;
/*      */ 
/*  980 */     ProcessMonotonicQuad(paramProcessHandler, arrayOfFloat, paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   private static void DrawMonotonicCubic(ProcessHandler paramProcessHandler, float[] paramArrayOfFloat, boolean paramBoolean, int[] paramArrayOfInt)
/*      */   {
/*  992 */     int i = (int)(paramArrayOfFloat[0] * 1024.0F);
/*  993 */     int j = (int)(paramArrayOfFloat[1] * 1024.0F);
/*      */ 
/*  995 */     int k = (int)(paramArrayOfFloat[6] * 1024.0F);
/*  996 */     int m = (int)(paramArrayOfFloat[7] * 1024.0F);
/*      */ 
/*  999 */     int n = (i & 0x3FF) << 6;
/* 1000 */     int i1 = (j & 0x3FF) << 6;
/*      */ 
/* 1006 */     int i2 = 32768;
/* 1007 */     int i3 = 262144;
/*      */ 
/* 1010 */     int i4 = 8;
/*      */ 
/* 1013 */     int i5 = 6;
/*      */ 
/* 1015 */     int i6 = (int)((-paramArrayOfFloat[0] + 3.0F * paramArrayOfFloat[2] - 3.0F * paramArrayOfFloat[4] + paramArrayOfFloat[6]) * 128.0F);
/*      */ 
/* 1017 */     int i7 = (int)((-paramArrayOfFloat[1] + 3.0F * paramArrayOfFloat[3] - 3.0F * paramArrayOfFloat[5] + paramArrayOfFloat[7]) * 128.0F);
/*      */ 
/* 1020 */     int i8 = (int)((3.0F * paramArrayOfFloat[0] - 6.0F * paramArrayOfFloat[2] + 3.0F * paramArrayOfFloat[4]) * 2048.0F);
/*      */ 
/* 1022 */     int i9 = (int)((3.0F * paramArrayOfFloat[1] - 6.0F * paramArrayOfFloat[3] + 3.0F * paramArrayOfFloat[5]) * 2048.0F);
/*      */ 
/* 1025 */     int i10 = (int)((-3.0F * paramArrayOfFloat[0] + 3.0F * paramArrayOfFloat[2]) * 8192.0F);
/* 1026 */     int i11 = (int)((-3.0F * paramArrayOfFloat[1] + 3.0F * paramArrayOfFloat[3]) * 8192.0F);
/*      */ 
/* 1028 */     int i12 = 6 * i6;
/* 1029 */     int i13 = 6 * i7;
/*      */ 
/* 1031 */     int i14 = i12 + i8;
/* 1032 */     int i15 = i13 + i9;
/*      */ 
/* 1034 */     int i16 = i6 + (i8 >> 1) + i10;
/* 1035 */     int i17 = i7 + (i9 >> 1) + i11;
/*      */ 
/* 1039 */     int i20 = i;
/* 1040 */     int i21 = j;
/*      */ 
/* 1043 */     int i22 = i & 0xFFFFFC00;
/* 1044 */     int i23 = j & 0xFFFFFC00;
/*      */ 
/* 1046 */     int i24 = k - i;
/* 1047 */     int i25 = m - j;
/*      */ 
/* 1049 */     while (i4 > 0)
/*      */     {
/* 1051 */       while ((Math.abs(i14) > i3) || (Math.abs(i15) > i3))
/*      */       {
/* 1053 */         i14 = (i14 << 1) - i12;
/* 1054 */         i15 = (i15 << 1) - i13;
/* 1055 */         i16 = (i16 << 2) - (i14 >> 1);
/* 1056 */         i17 = (i17 << 2) - (i15 >> 1);
/* 1057 */         i4 <<= 1;
/* 1058 */         i3 <<= 3;
/* 1059 */         i2 <<= 3;
/* 1060 */         n <<= 3;
/* 1061 */         i1 <<= 3;
/* 1062 */         i5 += 3;
/*      */       }
/*      */ 
/* 1070 */       while (((i4 & 0x1) == 0) && (i5 > 6) && (Math.abs(i16) <= i2) && (Math.abs(i17) <= i2))
/*      */       {
/* 1072 */         i16 = (i16 >> 2) + (i14 >> 3);
/* 1073 */         i17 = (i17 >> 2) + (i15 >> 3);
/* 1074 */         i14 = i14 + i12 >> 1;
/* 1075 */         i15 = i15 + i13 >> 1;
/* 1076 */         i4 >>= 1;
/* 1077 */         i3 >>= 3;
/* 1078 */         i2 >>= 3;
/* 1079 */         n >>= 3;
/* 1080 */         i1 >>= 3;
/* 1081 */         i5 -= 3;
/*      */       }
/*      */ 
/* 1084 */       i4--;
/*      */ 
/* 1091 */       if (i4 > 0) {
/* 1092 */         n += i16;
/* 1093 */         i1 += i17;
/*      */ 
/* 1095 */         i16 += i14;
/* 1096 */         i17 += i15;
/* 1097 */         i14 += i12;
/* 1098 */         i15 += i13;
/*      */ 
/* 1100 */         int i18 = i20;
/* 1101 */         int i19 = i21;
/*      */ 
/* 1103 */         i20 = i22 + (n >> i5);
/* 1104 */         i21 = i23 + (i1 >> i5);
/*      */ 
/* 1113 */         if ((k - i20 ^ i24) < 0) {
/* 1114 */           i20 = k;
/*      */         }
/*      */ 
/* 1118 */         if ((m - i21 ^ i25) < 0) {
/* 1119 */           i21 = m;
/*      */         }
/*      */ 
/* 1122 */         paramProcessHandler.processFixedLine(i18, i19, i20, i21, paramArrayOfInt, paramBoolean, false);
/*      */       }
/*      */       else {
/* 1125 */         paramProcessHandler.processFixedLine(i20, i21, k, m, paramArrayOfInt, paramBoolean, false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void ProcessMonotonicCubic(ProcessHandler paramProcessHandler, float[] paramArrayOfFloat, int[] paramArrayOfInt)
/*      */   {
/* 1140 */     float[] arrayOfFloat = new float[8];
/*      */     float f4;
/* 1145 */     float f3 = f4 = paramArrayOfFloat[0];
/*      */     float f6;
/* 1146 */     float f5 = f6 = paramArrayOfFloat[1];
/*      */ 
/* 1148 */     for (int i = 2; i < 8; i += 2) {
/* 1149 */       f3 = f3 > paramArrayOfFloat[i] ? paramArrayOfFloat[i] : f3;
/* 1150 */       f4 = f4 < paramArrayOfFloat[i] ? paramArrayOfFloat[i] : f4;
/* 1151 */       f5 = f5 > paramArrayOfFloat[(i + 1)] ? paramArrayOfFloat[(i + 1)] : f5;
/* 1152 */       f6 = f6 < paramArrayOfFloat[(i + 1)] ? paramArrayOfFloat[(i + 1)] : f6;
/*      */     }
/*      */ 
/* 1155 */     if (paramProcessHandler.clipMode == 0)
/*      */     {
/* 1159 */       if ((paramProcessHandler.dhnd.xMaxf >= f3) && (paramProcessHandler.dhnd.xMinf <= f4) && (paramProcessHandler.dhnd.yMaxf >= f5) && (paramProcessHandler.dhnd.yMinf <= f6));
/*      */     }
/*      */     else
/*      */     {
/* 1169 */       if ((paramProcessHandler.dhnd.yMaxf < f5) || (paramProcessHandler.dhnd.yMinf > f6) || (paramProcessHandler.dhnd.xMaxf < f3))
/*      */       {
/* 1172 */         return;
/*      */       }
/*      */ 
/* 1179 */       if (paramProcessHandler.dhnd.xMinf > f4)
/*      */       {
/*      */         float tmp262_261 = (paramArrayOfFloat[4] = paramArrayOfFloat[6] = paramProcessHandler.dhnd.xMinf); paramArrayOfFloat[2] = tmp262_261; paramArrayOfFloat[0] = tmp262_261;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1185 */     if ((f4 - f3 > 256.0F) || (f6 - f5 > 256.0F)) {
/* 1186 */       arrayOfFloat[6] = paramArrayOfFloat[6];
/* 1187 */       arrayOfFloat[7] = paramArrayOfFloat[7];
/* 1188 */       arrayOfFloat[4] = ((paramArrayOfFloat[4] + paramArrayOfFloat[6]) / 2.0F);
/* 1189 */       arrayOfFloat[5] = ((paramArrayOfFloat[5] + paramArrayOfFloat[7]) / 2.0F);
/* 1190 */       float f1 = (paramArrayOfFloat[2] + paramArrayOfFloat[4]) / 2.0F;
/* 1191 */       float f2 = (paramArrayOfFloat[3] + paramArrayOfFloat[5]) / 2.0F;
/* 1192 */       arrayOfFloat[2] = ((f1 + arrayOfFloat[4]) / 2.0F);
/* 1193 */       arrayOfFloat[3] = ((f2 + arrayOfFloat[5]) / 2.0F);
/* 1194 */       paramArrayOfFloat[2] = ((paramArrayOfFloat[0] + paramArrayOfFloat[2]) / 2.0F);
/* 1195 */       paramArrayOfFloat[3] = ((paramArrayOfFloat[1] + paramArrayOfFloat[3]) / 2.0F);
/* 1196 */       paramArrayOfFloat[4] = ((paramArrayOfFloat[2] + f1) / 2.0F);
/* 1197 */       paramArrayOfFloat[5] = ((paramArrayOfFloat[3] + f2) / 2.0F);
/*      */       float tmp433_432 = ((paramArrayOfFloat[4] + arrayOfFloat[2]) / 2.0F); arrayOfFloat[0] = tmp433_432; paramArrayOfFloat[6] = tmp433_432;
/*      */       float tmp450_449 = ((paramArrayOfFloat[5] + arrayOfFloat[3]) / 2.0F); arrayOfFloat[1] = tmp450_449; paramArrayOfFloat[7] = tmp450_449;
/*      */ 
/* 1201 */       ProcessMonotonicCubic(paramProcessHandler, paramArrayOfFloat, paramArrayOfInt);
/*      */ 
/* 1203 */       ProcessMonotonicCubic(paramProcessHandler, arrayOfFloat, paramArrayOfInt);
/*      */     } else {
/* 1205 */       DrawMonotonicCubic(paramProcessHandler, paramArrayOfFloat, (paramProcessHandler.dhnd.xMinf > f3) || (paramProcessHandler.dhnd.xMaxf < f4) || (paramProcessHandler.dhnd.yMinf > f5) || (paramProcessHandler.dhnd.yMaxf < f6), paramArrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void ProcessCubic(ProcessHandler paramProcessHandler, float[] paramArrayOfFloat, int[] paramArrayOfInt)
/*      */   {
/* 1231 */     double[] arrayOfDouble1 = new double[4];
/* 1232 */     double[] arrayOfDouble2 = new double[3];
/* 1233 */     double[] arrayOfDouble3 = new double[2];
/* 1234 */     int i = 0;
/*      */     int j;
/*      */     int k;
/* 1241 */     if (((paramArrayOfFloat[0] > paramArrayOfFloat[2]) || (paramArrayOfFloat[2] > paramArrayOfFloat[4]) || (paramArrayOfFloat[4] > paramArrayOfFloat[6])) && ((paramArrayOfFloat[0] < paramArrayOfFloat[2]) || (paramArrayOfFloat[2] < paramArrayOfFloat[4]) || (paramArrayOfFloat[4] < paramArrayOfFloat[6])))
/*      */     {
/* 1251 */       arrayOfDouble2[2] = (-paramArrayOfFloat[0] + 3.0F * paramArrayOfFloat[2] - 3.0F * paramArrayOfFloat[4] + paramArrayOfFloat[6]);
/* 1252 */       arrayOfDouble2[1] = (2.0F * (paramArrayOfFloat[0] - 2.0F * paramArrayOfFloat[2] + paramArrayOfFloat[4]));
/* 1253 */       arrayOfDouble2[0] = (-paramArrayOfFloat[0] + paramArrayOfFloat[2]);
/*      */ 
/* 1255 */       j = QuadCurve2D.solveQuadratic(arrayOfDouble2, arrayOfDouble3);
/*      */ 
/* 1261 */       for (k = 0; k < j; k++) {
/* 1262 */         if ((arrayOfDouble3[k] > 0.0D) && (arrayOfDouble3[k] < 1.0D)) {
/* 1263 */           arrayOfDouble1[(i++)] = arrayOfDouble3[k];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1273 */     if (((paramArrayOfFloat[1] > paramArrayOfFloat[3]) || (paramArrayOfFloat[3] > paramArrayOfFloat[5]) || (paramArrayOfFloat[5] > paramArrayOfFloat[7])) && ((paramArrayOfFloat[1] < paramArrayOfFloat[3]) || (paramArrayOfFloat[3] < paramArrayOfFloat[5]) || (paramArrayOfFloat[5] < paramArrayOfFloat[7])))
/*      */     {
/* 1283 */       arrayOfDouble2[2] = (-paramArrayOfFloat[1] + 3.0F * paramArrayOfFloat[3] - 3.0F * paramArrayOfFloat[5] + paramArrayOfFloat[7]);
/* 1284 */       arrayOfDouble2[1] = (2.0F * (paramArrayOfFloat[1] - 2.0F * paramArrayOfFloat[3] + paramArrayOfFloat[5]));
/* 1285 */       arrayOfDouble2[0] = (-paramArrayOfFloat[1] + paramArrayOfFloat[3]);
/*      */ 
/* 1287 */       j = QuadCurve2D.solveQuadratic(arrayOfDouble2, arrayOfDouble3);
/*      */ 
/* 1293 */       for (k = 0; k < j; k++) {
/* 1294 */         if ((arrayOfDouble3[k] > 0.0D) && (arrayOfDouble3[k] < 1.0D)) {
/* 1295 */           arrayOfDouble1[(i++)] = arrayOfDouble3[k];
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1300 */     if (i > 0)
/*      */     {
/* 1304 */       Arrays.sort(arrayOfDouble1, 0, i);
/*      */ 
/* 1307 */       ProcessFirstMonotonicPartOfCubic(paramProcessHandler, paramArrayOfFloat, paramArrayOfInt, (float)arrayOfDouble1[0]);
/*      */ 
/* 1309 */       for (j = 1; j < i; j++) {
/* 1310 */         double d = arrayOfDouble1[j] - arrayOfDouble1[(j - 1)];
/* 1311 */         if (d > 0.0D) {
/* 1312 */           ProcessFirstMonotonicPartOfCubic(paramProcessHandler, paramArrayOfFloat, paramArrayOfInt, (float)(d / (1.0D - arrayOfDouble1[(j - 1)])));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1319 */     ProcessMonotonicCubic(paramProcessHandler, paramArrayOfFloat, paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   private static void ProcessFirstMonotonicPartOfCubic(ProcessHandler paramProcessHandler, float[] paramArrayOfFloat, int[] paramArrayOfInt, float paramFloat)
/*      */   {
/* 1333 */     float[] arrayOfFloat = new float[8];
/*      */ 
/* 1336 */     arrayOfFloat[0] = paramArrayOfFloat[0];
/* 1337 */     arrayOfFloat[1] = paramArrayOfFloat[1];
/* 1338 */     float f1 = paramArrayOfFloat[2] + paramFloat * (paramArrayOfFloat[4] - paramArrayOfFloat[2]);
/* 1339 */     float f2 = paramArrayOfFloat[3] + paramFloat * (paramArrayOfFloat[5] - paramArrayOfFloat[3]);
/* 1340 */     arrayOfFloat[2] = (paramArrayOfFloat[0] + paramFloat * (paramArrayOfFloat[2] - paramArrayOfFloat[0]));
/* 1341 */     arrayOfFloat[3] = (paramArrayOfFloat[1] + paramFloat * (paramArrayOfFloat[3] - paramArrayOfFloat[1]));
/* 1342 */     arrayOfFloat[4] = (arrayOfFloat[2] + paramFloat * (f1 - arrayOfFloat[2]));
/* 1343 */     arrayOfFloat[5] = (arrayOfFloat[3] + paramFloat * (f2 - arrayOfFloat[3]));
/* 1344 */     paramArrayOfFloat[4] += paramFloat * (paramArrayOfFloat[6] - paramArrayOfFloat[4]);
/* 1345 */     paramArrayOfFloat[5] += paramFloat * (paramArrayOfFloat[7] - paramArrayOfFloat[5]);
/* 1346 */     paramArrayOfFloat[2] = (f1 + paramFloat * (paramArrayOfFloat[4] - f1));
/* 1347 */     paramArrayOfFloat[3] = (f2 + paramFloat * (paramArrayOfFloat[5] - f2));
/*      */     float tmp203_202 = (arrayOfFloat[4] + paramFloat * (paramArrayOfFloat[2] - arrayOfFloat[4])); arrayOfFloat[6] = tmp203_202; paramArrayOfFloat[0] = tmp203_202;
/*      */     float tmp227_226 = (arrayOfFloat[5] + paramFloat * (paramArrayOfFloat[3] - arrayOfFloat[5])); arrayOfFloat[7] = tmp227_226; paramArrayOfFloat[1] = tmp227_226;
/*      */ 
/* 1351 */     ProcessMonotonicCubic(paramProcessHandler, arrayOfFloat, paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   private static void ProcessLine(ProcessHandler paramProcessHandler, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int[] paramArrayOfInt)
/*      */   {
/* 1363 */     boolean bool1 = false;
/*      */ 
/* 1365 */     float[] arrayOfFloat = { paramFloat1, paramFloat2, paramFloat3, paramFloat4, 0.0F, 0.0F };
/*      */ 
/* 1369 */     float f1 = paramProcessHandler.dhnd.xMinf;
/* 1370 */     float f2 = paramProcessHandler.dhnd.yMinf;
/* 1371 */     float f3 = paramProcessHandler.dhnd.xMaxf;
/* 1372 */     float f4 = paramProcessHandler.dhnd.yMaxf;
/*      */ 
/* 1377 */     int i2 = TESTANDCLIP(f2, f4, arrayOfFloat, 1, 0, 3, 2);
/* 1378 */     if (i2 == 4) return;
/* 1379 */     bool1 = IS_CLIPPED(i2);
/*      */ 
/* 1383 */     i2 = TESTANDCLIP(f2, f4, arrayOfFloat, 3, 2, 1, 0);
/* 1384 */     if (i2 == 4) return;
/* 1385 */     boolean bool2 = IS_CLIPPED(i2);
/* 1386 */     bool1 = (bool1) || (bool2);
/*      */     int i;
/*      */     int j;
/*      */     int k;
/*      */     int m;
/* 1388 */     if (paramProcessHandler.clipMode == 0)
/*      */     {
/* 1392 */       i2 = TESTANDCLIP(f1, f3, arrayOfFloat, 0, 1, 2, 3);
/* 1393 */       if (i2 == 4) return;
/* 1394 */       bool1 = (bool1) || (IS_CLIPPED(i2));
/*      */ 
/* 1398 */       i2 = TESTANDCLIP(f1, f3, arrayOfFloat, 2, 3, 0, 1);
/* 1399 */       if (i2 == 4) return;
/* 1400 */       bool2 = (bool2) || (IS_CLIPPED(i2));
/* 1401 */       bool1 = (bool1) || (bool2);
/* 1402 */       i = (int)(arrayOfFloat[0] * 1024.0F);
/* 1403 */       j = (int)(arrayOfFloat[1] * 1024.0F);
/* 1404 */       k = (int)(arrayOfFloat[2] * 1024.0F);
/* 1405 */       m = (int)(arrayOfFloat[3] * 1024.0F);
/*      */ 
/* 1407 */       paramProcessHandler.processFixedLine(i, j, k, m, paramArrayOfInt, bool1, bool2);
/*      */     }
/*      */     else
/*      */     {
/* 1426 */       i2 = CLIPCLAMP(f1, f3, arrayOfFloat, 0, 1, 2, 3, 4, 5);
/* 1427 */       i = (int)(arrayOfFloat[0] * 1024.0F);
/* 1428 */       j = (int)(arrayOfFloat[1] * 1024.0F);
/*      */       int n;
/*      */       int i1;
/* 1431 */       if (i2 == 0) {
/* 1432 */         n = (int)(arrayOfFloat[4] * 1024.0F);
/* 1433 */         i1 = (int)(arrayOfFloat[5] * 1024.0F);
/* 1434 */         paramProcessHandler.processFixedLine(n, i1, i, j, paramArrayOfInt, false, bool2);
/*      */       }
/* 1437 */       else if (i2 == 4) {
/* 1438 */         return;
/*      */       }
/*      */ 
/* 1446 */       i2 = CLIPCLAMP(f1, f3, arrayOfFloat, 2, 3, 0, 1, 4, 5);
/*      */ 
/* 1449 */       bool2 = (bool2) || (i2 == 1);
/*      */ 
/* 1451 */       k = (int)(arrayOfFloat[2] * 1024.0F);
/* 1452 */       m = (int)(arrayOfFloat[3] * 1024.0F);
/* 1453 */       paramProcessHandler.processFixedLine(i, j, k, m, paramArrayOfInt, false, bool2);
/*      */ 
/* 1457 */       if (i2 == 0) {
/* 1458 */         n = (int)(arrayOfFloat[4] * 1024.0F);
/* 1459 */         i1 = (int)(arrayOfFloat[5] * 1024.0F);
/* 1460 */         paramProcessHandler.processFixedLine(k, m, n, i1, paramArrayOfInt, false, bool2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean doProcessPath(ProcessHandler paramProcessHandler, Path2D.Float paramFloat, float paramFloat1, float paramFloat2)
/*      */   {
/* 1469 */     float[] arrayOfFloat1 = new float[8];
/* 1470 */     float[] arrayOfFloat2 = new float[8];
/* 1471 */     float[] arrayOfFloat3 = { 0.0F, 0.0F };
/* 1472 */     float[] arrayOfFloat4 = new float[2];
/* 1473 */     int[] arrayOfInt = new int[5];
/* 1474 */     int i = 0;
/* 1475 */     int j = 0;
/*      */ 
/* 1477 */     arrayOfInt[0] = 0;
/*      */ 
/* 1482 */     paramProcessHandler.dhnd.adjustBounds(-1048576, -1048576, 1048576, 1048576);
/*      */ 
/* 1495 */     if (paramProcessHandler.dhnd.strokeControl == 2) {
/* 1496 */       arrayOfFloat3[0] = -0.5F;
/* 1497 */       arrayOfFloat3[1] = -0.5F;
/* 1498 */       paramFloat1 = (float)(paramFloat1 - 0.5D);
/* 1499 */       paramFloat2 = (float)(paramFloat2 - 0.5D);
/*      */     }
/*      */ 
/* 1502 */     PathIterator localPathIterator = paramFloat.getPathIterator(null);
/*      */ 
/* 1504 */     while (!localPathIterator.isDone())
/*      */     {
/*      */       float f1;
/*      */       float f2;
/* 1505 */       switch (localPathIterator.currentSegment(arrayOfFloat1))
/*      */       {
/*      */       case 0:
/* 1508 */         if ((i != 0) && (j == 0)) {
/* 1509 */           if ((paramProcessHandler.clipMode == 1) && (
/* 1510 */             (arrayOfFloat2[0] != arrayOfFloat3[0]) || (arrayOfFloat2[1] != arrayOfFloat3[1])))
/*      */           {
/* 1513 */             ProcessLine(paramProcessHandler, arrayOfFloat2[0], arrayOfFloat2[1], arrayOfFloat3[0], arrayOfFloat3[1], arrayOfInt);
/*      */           }
/*      */ 
/* 1518 */           paramProcessHandler.processEndSubPath();
/*      */         }
/*      */ 
/* 1521 */         arrayOfFloat1[0] += paramFloat1;
/* 1522 */         arrayOfFloat1[1] += paramFloat2;
/*      */ 
/* 1530 */         if ((arrayOfFloat2[0] < 8.507059E+037F) && (arrayOfFloat2[0] > -8.507059E+037F) && (arrayOfFloat2[1] < 8.507059E+037F) && (arrayOfFloat2[1] > -8.507059E+037F))
/*      */         {
/* 1535 */           i = 1;
/* 1536 */           j = 0;
/* 1537 */           arrayOfFloat3[0] = arrayOfFloat2[0];
/* 1538 */           arrayOfFloat3[1] = arrayOfFloat2[1];
/*      */         } else {
/* 1540 */           j = 1;
/*      */         }
/* 1542 */         arrayOfInt[0] = 0;
/* 1543 */         break;
/*      */       case 1:
/* 1545 */         f1 = arrayOfFloat2[2] = arrayOfFloat1[0] + paramFloat1;
/* 1546 */         f2 = arrayOfFloat2[3] = arrayOfFloat1[1] + paramFloat2;
/*      */ 
/* 1555 */         if ((f1 < 8.507059E+037F) && (f1 > -8.507059E+037F) && (f2 < 8.507059E+037F) && (f2 > -8.507059E+037F))
/*      */         {
/* 1560 */           if (j != 0)
/*      */           {
/*      */             float tmp395_393 = f1; arrayOfFloat3[0] = tmp395_393; arrayOfFloat2[0] = tmp395_393;
/*      */             float tmp406_404 = f2; arrayOfFloat3[1] = tmp406_404; arrayOfFloat2[1] = tmp406_404;
/* 1563 */             i = 1;
/* 1564 */             j = 0;
/*      */           } else {
/* 1566 */             ProcessLine(paramProcessHandler, arrayOfFloat2[0], arrayOfFloat2[1], arrayOfFloat2[2], arrayOfFloat2[3], arrayOfInt);
/*      */ 
/* 1568 */             arrayOfFloat2[0] = f1;
/* 1569 */             arrayOfFloat2[1] = f2; }  } break;
/*      */       case 2:
/* 1574 */         arrayOfFloat2[2] = (arrayOfFloat1[0] + paramFloat1);
/* 1575 */         arrayOfFloat2[3] = (arrayOfFloat1[1] + paramFloat2);
/* 1576 */         f1 = arrayOfFloat2[4] = arrayOfFloat1[2] + paramFloat1;
/* 1577 */         f2 = arrayOfFloat2[5] = arrayOfFloat1[3] + paramFloat2;
/*      */ 
/* 1587 */         if ((f1 < 8.507059E+037F) && (f1 > -8.507059E+037F) && (f2 < 8.507059E+037F) && (f2 > -8.507059E+037F))
/*      */         {
/* 1592 */           if (j != 0)
/*      */           {
/*      */             float tmp546_544 = f1; arrayOfFloat3[0] = tmp546_544; arrayOfFloat2[0] = tmp546_544;
/*      */             float tmp557_555 = f2; arrayOfFloat3[1] = tmp557_555; arrayOfFloat2[1] = tmp557_555;
/* 1595 */             i = 1;
/* 1596 */             j = 0;
/*      */           } else {
/* 1598 */             if ((arrayOfFloat2[2] < 8.507059E+037F) && (arrayOfFloat2[2] > -8.507059E+037F) && (arrayOfFloat2[3] < 8.507059E+037F) && (arrayOfFloat2[3] > -8.507059E+037F))
/*      */             {
/* 1603 */               ProcessQuad(paramProcessHandler, arrayOfFloat2, arrayOfInt);
/*      */             }
/* 1605 */             else ProcessLine(paramProcessHandler, arrayOfFloat2[0], arrayOfFloat2[1], arrayOfFloat2[4], arrayOfFloat2[5], arrayOfInt);
/*      */ 
/* 1609 */             arrayOfFloat2[0] = f1;
/* 1610 */             arrayOfFloat2[1] = f2; }  } break;
/*      */       case 3:
/* 1615 */         arrayOfFloat2[2] = (arrayOfFloat1[0] + paramFloat1);
/* 1616 */         arrayOfFloat2[3] = (arrayOfFloat1[1] + paramFloat2);
/* 1617 */         arrayOfFloat2[4] = (arrayOfFloat1[2] + paramFloat1);
/* 1618 */         arrayOfFloat2[5] = (arrayOfFloat1[3] + paramFloat2);
/* 1619 */         f1 = arrayOfFloat2[6] = arrayOfFloat1[4] + paramFloat1;
/* 1620 */         f2 = arrayOfFloat2[7] = arrayOfFloat1[5] + paramFloat2;
/*      */ 
/* 1630 */         if ((f1 < 8.507059E+037F) && (f1 > -8.507059E+037F) && (f2 < 8.507059E+037F) && (f2 > -8.507059E+037F))
/*      */         {
/* 1635 */           if (j != 0)
/*      */           {
/*      */             float tmp773_772 = arrayOfFloat2[6]; arrayOfFloat3[0] = tmp773_772; arrayOfFloat2[0] = tmp773_772;
/*      */             float tmp787_786 = arrayOfFloat2[7]; arrayOfFloat3[1] = tmp787_786; arrayOfFloat2[1] = tmp787_786;
/* 1638 */             i = 1;
/* 1639 */             j = 0;
/*      */           } else {
/* 1641 */             if ((arrayOfFloat2[2] < 8.507059E+037F) && (arrayOfFloat2[2] > -8.507059E+037F) && (arrayOfFloat2[3] < 8.507059E+037F) && (arrayOfFloat2[3] > -8.507059E+037F) && (arrayOfFloat2[4] < 8.507059E+037F) && (arrayOfFloat2[4] > -8.507059E+037F) && (arrayOfFloat2[5] < 8.507059E+037F) && (arrayOfFloat2[5] > -8.507059E+037F))
/*      */             {
/* 1650 */               ProcessCubic(paramProcessHandler, arrayOfFloat2, arrayOfInt);
/*      */             }
/* 1652 */             else ProcessLine(paramProcessHandler, arrayOfFloat2[0], arrayOfFloat2[1], arrayOfFloat2[6], arrayOfFloat2[7], arrayOfInt);
/*      */ 
/* 1656 */             arrayOfFloat2[0] = f1;
/* 1657 */             arrayOfFloat2[1] = f2; }  } break;
/*      */       case 4:
/* 1662 */         if ((i != 0) && (j == 0)) {
/* 1663 */           j = 0;
/* 1664 */           if ((arrayOfFloat2[0] != arrayOfFloat3[0]) || (arrayOfFloat2[1] != arrayOfFloat3[1]))
/*      */           {
/* 1667 */             ProcessLine(paramProcessHandler, arrayOfFloat2[0], arrayOfFloat2[1], arrayOfFloat3[0], arrayOfFloat3[1], arrayOfInt);
/*      */ 
/* 1674 */             arrayOfFloat2[0] = arrayOfFloat3[0];
/* 1675 */             arrayOfFloat2[1] = arrayOfFloat3[1];
/*      */           }
/* 1677 */           paramProcessHandler.processEndSubPath();
/*      */         }
/*      */         break;
/*      */       }
/* 1681 */       localPathIterator.next();
/*      */     }
/*      */ 
/* 1685 */     if ((i & (j == 0 ? 1 : 0)) != 0) {
/* 1686 */       if ((paramProcessHandler.clipMode == 1) && (
/* 1687 */         (arrayOfFloat2[0] != arrayOfFloat3[0]) || (arrayOfFloat2[1] != arrayOfFloat3[1])))
/*      */       {
/* 1690 */         ProcessLine(paramProcessHandler, arrayOfFloat2[0], arrayOfFloat2[1], arrayOfFloat3[0], arrayOfFloat3[1], arrayOfInt);
/*      */       }
/*      */ 
/* 1695 */       paramProcessHandler.processEndSubPath();
/*      */     }
/* 1697 */     return true;
/*      */   }
/*      */ 
/*      */   private static void FillPolygon(FillProcessHandler paramFillProcessHandler, int paramInt)
/*      */   {
/* 1902 */     int n = paramFillProcessHandler.dhnd.xMax - 1;
/* 1903 */     FillData localFillData = paramFillProcessHandler.fd;
/* 1904 */     int i1 = localFillData.plgYMin;
/* 1905 */     int i2 = localFillData.plgYMax;
/* 1906 */     int i3 = (i2 - i1 >> 10) + 4;
/*      */ 
/* 1911 */     int i4 = i1 - 1 & 0xFFFFFC00;
/*      */ 
/* 1917 */     int i6 = paramInt == 1 ? -1 : 1;
/*      */ 
/* 1921 */     List localList = localFillData.plgPnts;
/* 1922 */     int k = localList.size();
/*      */ 
/* 1924 */     if (k <= 1) return;
/*      */ 
/* 1926 */     Point[] arrayOfPoint = new Point[i3];
/*      */ 
/* 1933 */     Point localPoint1 = (Point)localList.get(0);
/* 1934 */     localPoint1.prev = null;
/* 1935 */     for (int i7 = 0; i7 < k - 1; i7++) {
/* 1936 */       localPoint1 = (Point)localList.get(i7);
/* 1937 */       Point localPoint3 = (Point)localList.get(i7 + 1);
/* 1938 */       int i9 = localPoint1.y - i4 - 1 >> 10;
/* 1939 */       localPoint1.nextByY = arrayOfPoint[i9];
/* 1940 */       arrayOfPoint[i9] = localPoint1;
/* 1941 */       localPoint1.next = localPoint3;
/* 1942 */       localPoint3.prev = localPoint1;
/*      */     }
/*      */ 
/* 1945 */     Point localPoint2 = (Point)localList.get(k - 1);
/* 1946 */     int i8 = localPoint2.y - i4 - 1 >> 10;
/* 1947 */     localPoint2.nextByY = arrayOfPoint[i8];
/* 1948 */     arrayOfPoint[i8] = localPoint2;
/*      */ 
/* 1950 */     ActiveEdgeList localActiveEdgeList = new ActiveEdgeList(null);
/*      */ 
/* 1952 */     int j = i4 + 1024; for (int i = 0; 
/* 1953 */       (j <= i2) && (i < i3); i++)
/*      */     {
/* 1955 */       for (Point localPoint4 = arrayOfPoint[i]; localPoint4 != null; localPoint4 = localPoint4.nextByY)
/*      */       {
/* 1959 */         if ((localPoint4.prev != null) && (!localPoint4.prev.lastPoint)) {
/* 1960 */           if ((localPoint4.prev.edge != null) && (localPoint4.prev.y <= j)) {
/* 1961 */             localActiveEdgeList.delete(localPoint4.prev.edge);
/* 1962 */             localPoint4.prev.edge = null;
/* 1963 */           } else if (localPoint4.prev.y > j) {
/* 1964 */             localActiveEdgeList.insert(localPoint4.prev, j);
/*      */           }
/*      */         }
/*      */ 
/* 1968 */         if ((!localPoint4.lastPoint) && (localPoint4.next != null)) {
/* 1969 */           if ((localPoint4.edge != null) && (localPoint4.next.y <= j)) {
/* 1970 */             localActiveEdgeList.delete(localPoint4.edge);
/* 1971 */             localPoint4.edge = null;
/* 1972 */           } else if (localPoint4.next.y > j) {
/* 1973 */             localActiveEdgeList.insert(localPoint4, j);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1978 */       if (!localActiveEdgeList.isEmpty())
/*      */       {
/* 1980 */         localActiveEdgeList.sort();
/*      */ 
/* 1982 */         int i5 = 0;
/* 1983 */         int m = 0;
/*      */         int i11;
/* 1985 */         int i10 = i11 = paramFillProcessHandler.dhnd.xMin;
/* 1986 */         Edge localEdge = localActiveEdgeList.head;
/* 1987 */         while (localEdge != null) {
/* 1988 */           i5 += localEdge.dir;
/* 1989 */           if (((i5 & i6) != 0) && (m == 0)) {
/* 1990 */             i10 = localEdge.x + 1024 - 1 >> 10;
/* 1991 */             m = 1;
/*      */           }
/*      */ 
/* 1994 */           if (((i5 & i6) == 0) && (m != 0)) {
/* 1995 */             i11 = localEdge.x - 1 >> 10;
/* 1996 */             if (i10 <= i11) {
/* 1997 */               paramFillProcessHandler.dhnd.drawScanline(i10, i11, j >> 10);
/*      */             }
/* 1999 */             m = 0;
/*      */           }
/*      */ 
/* 2002 */           localEdge.x += localEdge.dx;
/* 2003 */           localEdge = localEdge.next;
/*      */         }
/*      */ 
/* 2009 */         if ((m != 0) && (i10 <= n))
/*      */         {
/* 2014 */           paramFillProcessHandler.dhnd.drawScanline(i10, n, j >> 10);
/*      */         }
/*      */       }
/* 1953 */       j += 1024;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ActiveEdgeList
/*      */   {
/*      */     ProcessPath.Edge head;
/*      */ 
/*      */     public boolean isEmpty()
/*      */     {
/* 1778 */       return this.head == null;
/*      */     }
/*      */ 
/*      */     public void insert(ProcessPath.Point paramPoint, int paramInt) {
/* 1782 */       ProcessPath.Point localPoint = paramPoint.next;
/* 1783 */       int i = paramPoint.x; int j = paramPoint.y;
/* 1784 */       int k = localPoint.x; int m = localPoint.y;
/*      */ 
/* 1786 */       if (j == m)
/*      */       {
/* 1788 */         return;
/*      */       }
/* 1790 */       int n = k - i;
/* 1791 */       int i1 = m - j;
/*      */       int i3;
/*      */       int i4;
/*      */       int i5;
/* 1794 */       if (j < m) {
/* 1795 */         i3 = i;
/* 1796 */         i4 = paramInt - j;
/* 1797 */         i5 = -1;
/*      */       } else {
/* 1799 */         i3 = k;
/* 1800 */         i4 = paramInt - m;
/* 1801 */         i5 = 1;
/*      */       }
/*      */       int i2;
/* 1809 */       if ((n > 1048576.0F) || (n < -1048576.0F)) {
/* 1810 */         i2 = (int)(n * 1024.0D / i1);
/* 1811 */         i3 += (int)(n * i4 / i1);
/*      */       } else {
/* 1813 */         i2 = (n << 10) / i1;
/* 1814 */         i3 += n * i4 / i1;
/*      */       }
/*      */ 
/* 1817 */       ProcessPath.Edge localEdge = new ProcessPath.Edge(paramPoint, i3, i2, i5);
/*      */ 
/* 1820 */       localEdge.next = this.head;
/* 1821 */       localEdge.prev = null;
/* 1822 */       if (this.head != null) {
/* 1823 */         this.head.prev = localEdge;
/*      */       }
/* 1825 */       this.head = (paramPoint.edge = localEdge);
/*      */     }
/*      */ 
/*      */     public void delete(ProcessPath.Edge paramEdge) {
/* 1829 */       ProcessPath.Edge localEdge1 = paramEdge.prev;
/* 1830 */       ProcessPath.Edge localEdge2 = paramEdge.next;
/* 1831 */       if (localEdge1 != null)
/* 1832 */         localEdge1.next = localEdge2;
/*      */       else {
/* 1834 */         this.head = localEdge2;
/*      */       }
/* 1836 */       if (localEdge2 != null)
/* 1837 */         localEdge2.prev = localEdge1;
/*      */     }
/*      */ 
/*      */     public void sort()
/*      */     {
/* 1852 */       Object localObject2 = null;
/* 1853 */       int i = 1;
/*      */ 
/* 1857 */       while ((localObject2 != this.head.next) && (i != 0)) {
/* 1858 */         Object localObject1 = localEdge1 = this.head;
/* 1859 */         localEdge2 = localEdge1.next;
/* 1860 */         i = 0;
/* 1861 */         while (localEdge1 != localObject2) {
/* 1862 */           if (localEdge1.x >= localEdge2.x) {
/* 1863 */             i = 1;
/*      */             ProcessPath.Edge localEdge3;
/* 1864 */             if (localEdge1 == this.head) {
/* 1865 */               localEdge3 = localEdge2.next;
/* 1866 */               localEdge2.next = localEdge1;
/* 1867 */               localEdge1.next = localEdge3;
/* 1868 */               this.head = localEdge2;
/* 1869 */               localObject1 = localEdge2;
/*      */             } else {
/* 1871 */               localEdge3 = localEdge2.next;
/* 1872 */               localEdge2.next = localEdge1;
/* 1873 */               localEdge1.next = localEdge3;
/* 1874 */               ((ProcessPath.Edge)localObject1).next = localEdge2;
/* 1875 */               localObject1 = localEdge2;
/*      */             }
/*      */           } else {
/* 1878 */             localObject1 = localEdge1;
/* 1879 */             localEdge1 = localEdge1.next;
/*      */           }
/* 1881 */           localEdge2 = localEdge1.next;
/* 1882 */           if (localEdge2 == localObject2) localObject2 = localEdge1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1887 */       ProcessPath.Edge localEdge1 = this.head;
/* 1888 */       ProcessPath.Edge localEdge2 = null;
/* 1889 */       while (localEdge1 != null) {
/* 1890 */         localEdge1.prev = localEdge2;
/* 1891 */         localEdge2 = localEdge1;
/* 1892 */         localEdge1 = localEdge1.next;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class DrawHandler
/*      */   {
/*      */     public int xMin;
/*      */     public int yMin;
/*      */     public int xMax;
/*      */     public int yMax;
/*      */     public float xMinf;
/*      */     public float yMinf;
/*      */     public float xMaxf;
/*      */     public float yMaxf;
/*      */     public int strokeControl;
/*      */ 
/*      */     public DrawHandler(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*   63 */       setBounds(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */ 
/*      */     public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*   68 */       this.xMin = paramInt1;
/*   69 */       this.yMin = paramInt2;
/*   70 */       this.xMax = paramInt3;
/*   71 */       this.yMax = paramInt4;
/*      */ 
/*   84 */       this.xMinf = (paramInt1 - 0.5F);
/*   85 */       this.yMinf = (paramInt2 - 0.5F);
/*   86 */       this.xMaxf = (paramInt3 - 0.5F - 0.000976563F);
/*   87 */       this.yMaxf = (paramInt4 - 0.5F - 0.000976563F);
/*      */     }
/*      */ 
/*      */     public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*   93 */       this.strokeControl = paramInt5;
/*   94 */       setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void adjustBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*   99 */       if (this.xMin > paramInt1) paramInt1 = this.xMin;
/*  100 */       if (this.xMax < paramInt3) paramInt3 = this.xMax;
/*  101 */       if (this.yMin > paramInt2) paramInt2 = this.yMin;
/*  102 */       if (this.yMax < paramInt4) paramInt4 = this.yMax;
/*  103 */       setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public DrawHandler(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  107 */       this(paramInt1, paramInt2, paramInt3, paramInt4, 0);
/*      */     }
/*      */ 
/*      */     public abstract void drawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */     public abstract void drawPixel(int paramInt1, int paramInt2);
/*      */ 
/*      */     public abstract void drawScanline(int paramInt1, int paramInt2, int paramInt3);
/*      */   }
/*      */ 
/*      */   private static class DrawProcessHandler extends ProcessPath.ProcessHandler
/*      */   {
/*      */     ProcessPath.EndSubPathHandler processESP;
/*      */ 
/*      */     public DrawProcessHandler(ProcessPath.DrawHandler paramDrawHandler, ProcessPath.EndSubPathHandler paramEndSubPathHandler)
/*      */     {
/*  433 */       super(0);
/*  434 */       this.dhnd = paramDrawHandler;
/*  435 */       this.processESP = paramEndSubPathHandler;
/*      */     }
/*      */ 
/*      */     public void processEndSubPath() {
/*  439 */       this.processESP.processEndSubPath();
/*      */     }
/*      */ 
/*      */     void PROCESS_LINE(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int[] paramArrayOfInt)
/*      */     {
/*  444 */       int i = paramInt1 >> 10;
/*  445 */       int j = paramInt2 >> 10;
/*  446 */       int k = paramInt3 >> 10;
/*  447 */       int m = paramInt4 >> 10;
/*      */ 
/*  450 */       if ((i ^ k | j ^ m) == 0) {
/*  451 */         if ((paramBoolean) && ((this.dhnd.yMin > j) || (this.dhnd.yMax <= j) || (this.dhnd.xMin > i) || (this.dhnd.xMax <= i)))
/*      */         {
/*  455 */           return;
/*      */         }
/*  457 */         if (paramArrayOfInt[0] == 0) {
/*  458 */           paramArrayOfInt[0] = 1;
/*  459 */           paramArrayOfInt[1] = i;
/*  460 */           paramArrayOfInt[2] = j;
/*  461 */           paramArrayOfInt[3] = i;
/*  462 */           paramArrayOfInt[4] = j;
/*  463 */           this.dhnd.drawPixel(i, j);
/*  464 */         } else if (((i != paramArrayOfInt[3]) || (j != paramArrayOfInt[4])) && ((i != paramArrayOfInt[1]) || (j != paramArrayOfInt[2])))
/*      */         {
/*  466 */           this.dhnd.drawPixel(i, j);
/*  467 */           paramArrayOfInt[3] = i;
/*  468 */           paramArrayOfInt[4] = j;
/*      */         }
/*  470 */         return;
/*      */       }
/*      */ 
/*  473 */       if ((!paramBoolean) || ((this.dhnd.yMin <= j) && (this.dhnd.yMax > j) && (this.dhnd.xMin <= i) && (this.dhnd.xMax > i)))
/*      */       {
/*  480 */         if ((paramArrayOfInt[0] == 1) && (((paramArrayOfInt[1] == i) && (paramArrayOfInt[2] == j)) || ((paramArrayOfInt[3] == i) && (paramArrayOfInt[4] == j))))
/*      */         {
/*  484 */           this.dhnd.drawPixel(i, j);
/*      */         }
/*      */       }
/*      */ 
/*  488 */       this.dhnd.drawLine(i, j, k, m);
/*      */ 
/*  490 */       if (paramArrayOfInt[0] == 0) {
/*  491 */         paramArrayOfInt[0] = 1;
/*  492 */         paramArrayOfInt[1] = i;
/*  493 */         paramArrayOfInt[2] = j;
/*  494 */         paramArrayOfInt[3] = i;
/*  495 */         paramArrayOfInt[4] = j;
/*      */       }
/*      */ 
/*  501 */       if (((paramArrayOfInt[1] == k) && (paramArrayOfInt[2] == m)) || ((paramArrayOfInt[3] == k) && (paramArrayOfInt[4] == m)))
/*      */       {
/*  504 */         if ((paramBoolean) && ((this.dhnd.yMin > m) || (this.dhnd.yMax <= m) || (this.dhnd.xMin > k) || (this.dhnd.xMax <= k)))
/*      */         {
/*  509 */           return;
/*      */         }
/*      */ 
/*  512 */         this.dhnd.drawPixel(k, m);
/*      */       }
/*  514 */       paramArrayOfInt[3] = k;
/*  515 */       paramArrayOfInt[4] = m;
/*      */     }
/*      */ 
/*      */     void PROCESS_POINT(int paramInt1, int paramInt2, boolean paramBoolean, int[] paramArrayOfInt)
/*      */     {
/*  520 */       int i = paramInt1 >> 10;
/*  521 */       int j = paramInt2 >> 10;
/*  522 */       if ((paramBoolean) && ((this.dhnd.yMin > j) || (this.dhnd.yMax <= j) || (this.dhnd.xMin > i) || (this.dhnd.xMax <= i)))
/*      */       {
/*  526 */         return;
/*      */       }
/*      */ 
/*  536 */       if (paramArrayOfInt[0] == 0) {
/*  537 */         paramArrayOfInt[0] = 1;
/*  538 */         paramArrayOfInt[1] = i;
/*  539 */         paramArrayOfInt[2] = j;
/*  540 */         paramArrayOfInt[3] = i;
/*  541 */         paramArrayOfInt[4] = j;
/*  542 */         this.dhnd.drawPixel(i, j);
/*  543 */       } else if (((i != paramArrayOfInt[3]) || (j != paramArrayOfInt[4])) && ((i != paramArrayOfInt[1]) || (j != paramArrayOfInt[2])))
/*      */       {
/*  545 */         this.dhnd.drawPixel(i, j);
/*  546 */         paramArrayOfInt[3] = i;
/*  547 */         paramArrayOfInt[4] = j;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void processFixedLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/*  580 */       int i = paramInt1 ^ paramInt3 | paramInt2 ^ paramInt4;
/*      */ 
/*  582 */       if ((i & 0xFFFFFC00) == 0)
/*      */       {
/*  586 */         if (i == 0)
/*  587 */           PROCESS_POINT(paramInt1 + 512, paramInt2 + 512, paramBoolean1, paramArrayOfInt);
/*      */         return;
/*      */       }
/*      */       int j;
/*      */       int m;
/*      */       int k;
/*      */       int n;
/*  593 */       if ((paramInt1 == paramInt3) || (paramInt2 == paramInt4)) {
/*  594 */         j = paramInt1 + 512;
/*  595 */         m = paramInt3 + 512;
/*  596 */         k = paramInt2 + 512;
/*  597 */         n = paramInt4 + 512;
/*      */       }
/*      */       else {
/*  600 */         int i1 = paramInt3 - paramInt1;
/*  601 */         int i2 = paramInt4 - paramInt2;
/*      */ 
/*  604 */         int i3 = paramInt1 & 0xFFFFFC00;
/*  605 */         int i4 = paramInt2 & 0xFFFFFC00;
/*  606 */         int i5 = paramInt3 & 0xFFFFFC00;
/*  607 */         int i6 = paramInt4 & 0xFFFFFC00;
/*      */         int i7;
/*      */         int i8;
/*      */         int i9;
/*  610 */         if ((i3 == paramInt1) || (i4 == paramInt2))
/*      */         {
/*  614 */           j = paramInt1 + 512;
/*  615 */           k = paramInt2 + 512;
/*      */         }
/*      */         else {
/*  618 */           i7 = paramInt1 < paramInt3 ? i3 + 1024 : i3;
/*  619 */           i8 = paramInt2 < paramInt4 ? i4 + 1024 : i4;
/*      */ 
/*  622 */           i9 = paramInt2 + (i7 - paramInt1) * i2 / i1;
/*  623 */           if ((i9 >= i4) && (i9 <= i4 + 1024)) {
/*  624 */             j = i7;
/*  625 */             k = i9 + 512;
/*      */           }
/*      */           else {
/*  628 */             i9 = paramInt1 + (i8 - paramInt2) * i1 / i2;
/*  629 */             j = i9 + 512;
/*  630 */             k = i8;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  635 */         if ((i5 == paramInt3) || (i6 == paramInt4))
/*      */         {
/*  639 */           m = paramInt3 + 512;
/*  640 */           n = paramInt4 + 512;
/*      */         }
/*      */         else {
/*  643 */           i7 = paramInt1 > paramInt3 ? i5 + 1024 : i5;
/*  644 */           i8 = paramInt2 > paramInt4 ? i6 + 1024 : i6;
/*      */ 
/*  647 */           i9 = paramInt4 + (i7 - paramInt3) * i2 / i1;
/*  648 */           if ((i9 >= i6) && (i9 <= i6 + 1024)) {
/*  649 */             m = i7;
/*  650 */             n = i9 + 512;
/*      */           }
/*      */           else {
/*  653 */             i9 = paramInt3 + (i8 - paramInt4) * i1 / i2;
/*  654 */             m = i9 + 512;
/*  655 */             n = i8;
/*      */           }
/*      */         }
/*      */       }
/*  659 */       PROCESS_LINE(j, k, m, n, paramBoolean1, paramArrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Edge
/*      */   {
/*      */     int x;
/*      */     int dx;
/*      */     ProcessPath.Point p;
/*      */     int dir;
/*      */     Edge prev;
/*      */     Edge next;
/*      */ 
/*      */     public Edge(ProcessPath.Point paramPoint, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1724 */       this.p = paramPoint;
/* 1725 */       this.x = paramInt1;
/* 1726 */       this.dx = paramInt2;
/* 1727 */       this.dir = paramInt3;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface EndSubPathHandler
/*      */   {
/*      */     public abstract void processEndSubPath();
/*      */   }
/*      */ 
/*      */   private static class FillData
/*      */   {
/*      */     List<ProcessPath.Point> plgPnts;
/*      */     public int plgYMin;
/*      */     public int plgYMax;
/*      */ 
/*      */     public FillData()
/*      */     {
/* 1747 */       this.plgPnts = new Vector(256);
/*      */     }
/*      */ 
/*      */     public void addPoint(int paramInt1, int paramInt2, boolean paramBoolean) {
/* 1751 */       if (this.plgPnts.size() == 0) {
/* 1752 */         this.plgYMin = (this.plgYMax = paramInt2);
/*      */       } else {
/* 1754 */         this.plgYMin = (this.plgYMin > paramInt2 ? paramInt2 : this.plgYMin);
/* 1755 */         this.plgYMax = (this.plgYMax < paramInt2 ? paramInt2 : this.plgYMax);
/*      */       }
/*      */ 
/* 1758 */       this.plgPnts.add(new ProcessPath.Point(paramInt1, paramInt2, paramBoolean));
/*      */     }
/*      */ 
/*      */     public boolean isEmpty() {
/* 1762 */       return this.plgPnts.size() == 0;
/*      */     }
/*      */ 
/*      */     public boolean isEnded() {
/* 1766 */       return ((ProcessPath.Point)this.plgPnts.get(this.plgPnts.size() - 1)).lastPoint;
/*      */     }
/*      */ 
/*      */     public boolean setEnded() {
/* 1770 */       return ((ProcessPath.Point)this.plgPnts.get(this.plgPnts.size() - 1)).lastPoint = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FillProcessHandler extends ProcessPath.ProcessHandler
/*      */   {
/*      */     ProcessPath.FillData fd;
/*      */ 
/*      */     public void processFixedLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 2040 */       if (paramBoolean1)
/*      */       {
/* 2046 */         int[] arrayOfInt = { paramInt1, paramInt2, paramInt3, paramInt4, 0, 0 };
/* 2047 */         int i = (int)(this.dhnd.xMinf * 1024.0F);
/* 2048 */         int j = (int)(this.dhnd.xMaxf * 1024.0F);
/* 2049 */         int k = (int)(this.dhnd.yMinf * 1024.0F);
/* 2050 */         int m = (int)(this.dhnd.yMaxf * 1024.0F);
/*      */ 
/* 2055 */         int n = ProcessPath.TESTANDCLIP(k, m, arrayOfInt, 1, 0, 3, 2);
/* 2056 */         if (n == 4) return;
/*      */ 
/* 2061 */         n = ProcessPath.TESTANDCLIP(k, m, arrayOfInt, 3, 2, 1, 0);
/* 2062 */         if (n == 4) return;
/* 2063 */         boolean bool = ProcessPath.IS_CLIPPED(n);
/*      */ 
/* 2070 */         n = ProcessPath.CLIPCLAMP(i, j, arrayOfInt, 0, 1, 2, 3, 4, 5);
/*      */ 
/* 2073 */         if (n == 0) {
/* 2074 */           processFixedLine(arrayOfInt[4], arrayOfInt[5], arrayOfInt[0], arrayOfInt[1], paramArrayOfInt, false, bool);
/*      */         }
/* 2077 */         else if (n == 4) {
/* 2078 */           return;
/*      */         }
/*      */ 
/* 2086 */         n = ProcessPath.CLIPCLAMP(i, j, arrayOfInt, 2, 3, 0, 1, 4, 5);
/*      */ 
/* 2089 */         bool = (bool) || (n == 1);
/*      */ 
/* 2091 */         processFixedLine(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3], paramArrayOfInt, false, bool);
/*      */ 
/* 2095 */         if (n == 0) {
/* 2096 */           processFixedLine(arrayOfInt[2], arrayOfInt[3], arrayOfInt[4], arrayOfInt[5], paramArrayOfInt, false, bool);
/*      */         }
/*      */ 
/* 2100 */         return;
/*      */       }
/*      */ 
/* 2106 */       if ((this.fd.isEmpty()) || (this.fd.isEnded())) {
/* 2107 */         this.fd.addPoint(paramInt1, paramInt2, false);
/*      */       }
/*      */ 
/* 2110 */       this.fd.addPoint(paramInt3, paramInt4, false);
/*      */ 
/* 2112 */       if (paramBoolean2)
/* 2113 */         this.fd.setEnded();
/*      */     }
/*      */ 
/*      */     FillProcessHandler(ProcessPath.DrawHandler paramDrawHandler)
/*      */     {
/* 2118 */       super(1);
/* 2119 */       this.fd = new ProcessPath.FillData();
/*      */     }
/*      */ 
/*      */     public void processEndSubPath() {
/* 2123 */       if (!this.fd.isEmpty())
/* 2124 */         this.fd.setEnded();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Point
/*      */   {
/*      */     public int x;
/*      */     public int y;
/*      */     public boolean lastPoint;
/*      */     public Point prev;
/*      */     public Point next;
/*      */     public Point nextByY;
/*      */     public ProcessPath.Edge edge;
/*      */ 
/*      */     public Point(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */     {
/* 1709 */       this.x = paramInt1;
/* 1710 */       this.y = paramInt2;
/* 1711 */       this.lastPoint = paramBoolean;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class ProcessHandler
/*      */     implements ProcessPath.EndSubPathHandler
/*      */   {
/*      */     ProcessPath.DrawHandler dhnd;
/*      */     int clipMode;
/*      */ 
/*      */     public ProcessHandler(ProcessPath.DrawHandler paramDrawHandler, int paramInt)
/*      */     {
/*  130 */       this.dhnd = paramDrawHandler;
/*  131 */       this.clipMode = paramInt;
/*      */     }
/*      */ 
/*      */     public abstract void processFixedLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, boolean paramBoolean1, boolean paramBoolean2);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.ProcessPath
 * JD-Core Version:    0.6.2
 */