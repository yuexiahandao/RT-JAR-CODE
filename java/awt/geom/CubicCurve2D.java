/*      */ package java.awt.geom;
/*      */ 
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import sun.awt.geom.Curve;
/*      */ 
/*      */ public abstract class CubicCurve2D
/*      */   implements Shape, Cloneable
/*      */ {
/*      */   public abstract double getX1();
/*      */ 
/*      */   public abstract double getY1();
/*      */ 
/*      */   public abstract Point2D getP1();
/*      */ 
/*      */   public abstract double getCtrlX1();
/*      */ 
/*      */   public abstract double getCtrlY1();
/*      */ 
/*      */   public abstract Point2D getCtrlP1();
/*      */ 
/*      */   public abstract double getCtrlX2();
/*      */ 
/*      */   public abstract double getCtrlY2();
/*      */ 
/*      */   public abstract Point2D getCtrlP2();
/*      */ 
/*      */   public abstract double getX2();
/*      */ 
/*      */   public abstract double getY2();
/*      */ 
/*      */   public abstract Point2D getP2();
/*      */ 
/*      */   public abstract void setCurve(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8);
/*      */ 
/*      */   public void setCurve(double[] paramArrayOfDouble, int paramInt)
/*      */   {
/*  731 */     setCurve(paramArrayOfDouble[(paramInt + 0)], paramArrayOfDouble[(paramInt + 1)], paramArrayOfDouble[(paramInt + 2)], paramArrayOfDouble[(paramInt + 3)], paramArrayOfDouble[(paramInt + 4)], paramArrayOfDouble[(paramInt + 5)], paramArrayOfDouble[(paramInt + 6)], paramArrayOfDouble[(paramInt + 7)]);
/*      */   }
/*      */ 
/*      */   public void setCurve(Point2D paramPoint2D1, Point2D paramPoint2D2, Point2D paramPoint2D3, Point2D paramPoint2D4)
/*      */   {
/*  751 */     setCurve(paramPoint2D1.getX(), paramPoint2D1.getY(), paramPoint2D2.getX(), paramPoint2D2.getY(), paramPoint2D3.getX(), paramPoint2D3.getY(), paramPoint2D4.getX(), paramPoint2D4.getY());
/*      */   }
/*      */ 
/*      */   public void setCurve(Point2D[] paramArrayOfPoint2D, int paramInt)
/*      */   {
/*  766 */     setCurve(paramArrayOfPoint2D[(paramInt + 0)].getX(), paramArrayOfPoint2D[(paramInt + 0)].getY(), paramArrayOfPoint2D[(paramInt + 1)].getX(), paramArrayOfPoint2D[(paramInt + 1)].getY(), paramArrayOfPoint2D[(paramInt + 2)].getX(), paramArrayOfPoint2D[(paramInt + 2)].getY(), paramArrayOfPoint2D[(paramInt + 3)].getX(), paramArrayOfPoint2D[(paramInt + 3)].getY());
/*      */   }
/*      */ 
/*      */   public void setCurve(CubicCurve2D paramCubicCurve2D)
/*      */   {
/*  779 */     setCurve(paramCubicCurve2D.getX1(), paramCubicCurve2D.getY1(), paramCubicCurve2D.getCtrlX1(), paramCubicCurve2D.getCtrlY1(), paramCubicCurve2D.getCtrlX2(), paramCubicCurve2D.getCtrlY2(), paramCubicCurve2D.getX2(), paramCubicCurve2D.getY2());
/*      */   }
/*      */ 
/*      */   public static double getFlatnessSq(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8)
/*      */   {
/*  812 */     return Math.max(Line2D.ptSegDistSq(paramDouble1, paramDouble2, paramDouble7, paramDouble8, paramDouble3, paramDouble4), Line2D.ptSegDistSq(paramDouble1, paramDouble2, paramDouble7, paramDouble8, paramDouble5, paramDouble6));
/*      */   }
/*      */ 
/*      */   public static double getFlatness(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8)
/*      */   {
/*  846 */     return Math.sqrt(getFlatnessSq(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8));
/*      */   }
/*      */ 
/*      */   public static double getFlatnessSq(double[] paramArrayOfDouble, int paramInt)
/*      */   {
/*  864 */     return getFlatnessSq(paramArrayOfDouble[(paramInt + 0)], paramArrayOfDouble[(paramInt + 1)], paramArrayOfDouble[(paramInt + 2)], paramArrayOfDouble[(paramInt + 3)], paramArrayOfDouble[(paramInt + 4)], paramArrayOfDouble[(paramInt + 5)], paramArrayOfDouble[(paramInt + 6)], paramArrayOfDouble[(paramInt + 7)]);
/*      */   }
/*      */ 
/*      */   public static double getFlatness(double[] paramArrayOfDouble, int paramInt)
/*      */   {
/*  884 */     return getFlatness(paramArrayOfDouble[(paramInt + 0)], paramArrayOfDouble[(paramInt + 1)], paramArrayOfDouble[(paramInt + 2)], paramArrayOfDouble[(paramInt + 3)], paramArrayOfDouble[(paramInt + 4)], paramArrayOfDouble[(paramInt + 5)], paramArrayOfDouble[(paramInt + 6)], paramArrayOfDouble[(paramInt + 7)]);
/*      */   }
/*      */ 
/*      */   public double getFlatnessSq()
/*      */   {
/*  898 */     return getFlatnessSq(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), getX2(), getY2());
/*      */   }
/*      */ 
/*      */   public double getFlatness()
/*      */   {
/*  910 */     return getFlatness(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), getX2(), getY2());
/*      */   }
/*      */ 
/*      */   public void subdivide(CubicCurve2D paramCubicCurve2D1, CubicCurve2D paramCubicCurve2D2)
/*      */   {
/*  926 */     subdivide(this, paramCubicCurve2D1, paramCubicCurve2D2);
/*      */   }
/*      */ 
/*      */   public static void subdivide(CubicCurve2D paramCubicCurve2D1, CubicCurve2D paramCubicCurve2D2, CubicCurve2D paramCubicCurve2D3)
/*      */   {
/*  945 */     double d1 = paramCubicCurve2D1.getX1();
/*  946 */     double d2 = paramCubicCurve2D1.getY1();
/*  947 */     double d3 = paramCubicCurve2D1.getCtrlX1();
/*  948 */     double d4 = paramCubicCurve2D1.getCtrlY1();
/*  949 */     double d5 = paramCubicCurve2D1.getCtrlX2();
/*  950 */     double d6 = paramCubicCurve2D1.getCtrlY2();
/*  951 */     double d7 = paramCubicCurve2D1.getX2();
/*  952 */     double d8 = paramCubicCurve2D1.getY2();
/*  953 */     double d9 = (d3 + d5) / 2.0D;
/*  954 */     double d10 = (d4 + d6) / 2.0D;
/*  955 */     d3 = (d1 + d3) / 2.0D;
/*  956 */     d4 = (d2 + d4) / 2.0D;
/*  957 */     d5 = (d7 + d5) / 2.0D;
/*  958 */     d6 = (d8 + d6) / 2.0D;
/*  959 */     double d11 = (d3 + d9) / 2.0D;
/*  960 */     double d12 = (d4 + d10) / 2.0D;
/*  961 */     double d13 = (d5 + d9) / 2.0D;
/*  962 */     double d14 = (d6 + d10) / 2.0D;
/*  963 */     d9 = (d11 + d13) / 2.0D;
/*  964 */     d10 = (d12 + d14) / 2.0D;
/*  965 */     if (paramCubicCurve2D2 != null) {
/*  966 */       paramCubicCurve2D2.setCurve(d1, d2, d3, d4, d11, d12, d9, d10);
/*      */     }
/*      */ 
/*  969 */     if (paramCubicCurve2D3 != null)
/*  970 */       paramCubicCurve2D3.setCurve(d9, d10, d13, d14, d5, d6, d7, d8);
/*      */   }
/*      */ 
/*      */   public static void subdivide(double[] paramArrayOfDouble1, int paramInt1, double[] paramArrayOfDouble2, int paramInt2, double[] paramArrayOfDouble3, int paramInt3)
/*      */   {
/* 1006 */     double d1 = paramArrayOfDouble1[(paramInt1 + 0)];
/* 1007 */     double d2 = paramArrayOfDouble1[(paramInt1 + 1)];
/* 1008 */     double d3 = paramArrayOfDouble1[(paramInt1 + 2)];
/* 1009 */     double d4 = paramArrayOfDouble1[(paramInt1 + 3)];
/* 1010 */     double d5 = paramArrayOfDouble1[(paramInt1 + 4)];
/* 1011 */     double d6 = paramArrayOfDouble1[(paramInt1 + 5)];
/* 1012 */     double d7 = paramArrayOfDouble1[(paramInt1 + 6)];
/* 1013 */     double d8 = paramArrayOfDouble1[(paramInt1 + 7)];
/* 1014 */     if (paramArrayOfDouble2 != null) {
/* 1015 */       paramArrayOfDouble2[(paramInt2 + 0)] = d1;
/* 1016 */       paramArrayOfDouble2[(paramInt2 + 1)] = d2;
/*      */     }
/* 1018 */     if (paramArrayOfDouble3 != null) {
/* 1019 */       paramArrayOfDouble3[(paramInt3 + 6)] = d7;
/* 1020 */       paramArrayOfDouble3[(paramInt3 + 7)] = d8;
/*      */     }
/* 1022 */     d1 = (d1 + d3) / 2.0D;
/* 1023 */     d2 = (d2 + d4) / 2.0D;
/* 1024 */     d7 = (d7 + d5) / 2.0D;
/* 1025 */     d8 = (d8 + d6) / 2.0D;
/* 1026 */     double d9 = (d3 + d5) / 2.0D;
/* 1027 */     double d10 = (d4 + d6) / 2.0D;
/* 1028 */     d3 = (d1 + d9) / 2.0D;
/* 1029 */     d4 = (d2 + d10) / 2.0D;
/* 1030 */     d5 = (d7 + d9) / 2.0D;
/* 1031 */     d6 = (d8 + d10) / 2.0D;
/* 1032 */     d9 = (d3 + d5) / 2.0D;
/* 1033 */     d10 = (d4 + d6) / 2.0D;
/* 1034 */     if (paramArrayOfDouble2 != null) {
/* 1035 */       paramArrayOfDouble2[(paramInt2 + 2)] = d1;
/* 1036 */       paramArrayOfDouble2[(paramInt2 + 3)] = d2;
/* 1037 */       paramArrayOfDouble2[(paramInt2 + 4)] = d3;
/* 1038 */       paramArrayOfDouble2[(paramInt2 + 5)] = d4;
/* 1039 */       paramArrayOfDouble2[(paramInt2 + 6)] = d9;
/* 1040 */       paramArrayOfDouble2[(paramInt2 + 7)] = d10;
/*      */     }
/* 1042 */     if (paramArrayOfDouble3 != null) {
/* 1043 */       paramArrayOfDouble3[(paramInt3 + 0)] = d9;
/* 1044 */       paramArrayOfDouble3[(paramInt3 + 1)] = d10;
/* 1045 */       paramArrayOfDouble3[(paramInt3 + 2)] = d5;
/* 1046 */       paramArrayOfDouble3[(paramInt3 + 3)] = d6;
/* 1047 */       paramArrayOfDouble3[(paramInt3 + 4)] = d7;
/* 1048 */       paramArrayOfDouble3[(paramInt3 + 5)] = d8;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static int solveCubic(double[] paramArrayOfDouble)
/*      */   {
/* 1069 */     return solveCubic(paramArrayOfDouble, paramArrayOfDouble);
/*      */   }
/*      */ 
/*      */   public static int solveCubic(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
/*      */   {
/* 1092 */     double d1 = paramArrayOfDouble1[3];
/* 1093 */     if (d1 == 0.0D) {
/* 1094 */       return QuadCurve2D.solveQuadratic(paramArrayOfDouble1, paramArrayOfDouble2);
/*      */     }
/*      */ 
/* 1098 */     double d2 = paramArrayOfDouble1[2] / d1;
/* 1099 */     double d3 = paramArrayOfDouble1[1] / d1;
/* 1100 */     double d4 = paramArrayOfDouble1[0] / d1;
/*      */ 
/* 1111 */     double d5 = d2 * d2;
/* 1112 */     double d6 = 0.3333333333333333D * (-0.3333333333333333D * d5 + d3);
/* 1113 */     double d7 = 0.5D * (0.0740740740740741D * d2 * d5 - 0.3333333333333333D * d2 * d3 + d4);
/*      */ 
/* 1117 */     double d8 = d6 * d6 * d6;
/* 1118 */     double d9 = d7 * d7 + d8;
/*      */ 
/* 1120 */     double d10 = 0.3333333333333333D * d2;
/*      */     double d11;
/*      */     double d12;
/*      */     int i;
/* 1123 */     if (d9 < 0.0D)
/*      */     {
/* 1125 */       d11 = 0.3333333333333333D * Math.acos(-d7 / Math.sqrt(-d8));
/* 1126 */       d12 = 2.0D * Math.sqrt(-d6);
/*      */ 
/* 1128 */       if (paramArrayOfDouble2 == paramArrayOfDouble1) {
/* 1129 */         paramArrayOfDouble1 = Arrays.copyOf(paramArrayOfDouble1, 4);
/*      */       }
/*      */ 
/* 1132 */       paramArrayOfDouble2[0] = (d12 * Math.cos(d11));
/* 1133 */       paramArrayOfDouble2[1] = (-d12 * Math.cos(d11 + 1.047197551196598D));
/* 1134 */       paramArrayOfDouble2[2] = (-d12 * Math.cos(d11 - 1.047197551196598D));
/* 1135 */       i = 3;
/*      */ 
/* 1137 */       for (int j = 0; j < i; j++) {
/* 1138 */         paramArrayOfDouble2[j] -= d10;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1144 */       d11 = Math.sqrt(d9);
/* 1145 */       d12 = Math.cbrt(d11 - d7);
/* 1146 */       double d13 = -Math.cbrt(d11 + d7);
/* 1147 */       double d14 = d12 + d13;
/*      */ 
/* 1149 */       i = 1;
/*      */ 
/* 1151 */       double d15 = 1200000000.0D * Math.ulp(Math.abs(d14) + Math.abs(d10));
/* 1152 */       if ((iszero(d9, d15)) || (within(d12, d13, d15))) {
/* 1153 */         if (paramArrayOfDouble2 == paramArrayOfDouble1) {
/* 1154 */           paramArrayOfDouble1 = Arrays.copyOf(paramArrayOfDouble1, 4);
/*      */         }
/* 1156 */         paramArrayOfDouble2[1] = (-(d14 / 2.0D) - d10);
/* 1157 */         i = 2;
/*      */       }
/*      */ 
/* 1160 */       paramArrayOfDouble2[0] = (d14 - d10);
/*      */     }
/*      */ 
/* 1163 */     if (i > 1) {
/* 1164 */       i = fixRoots(paramArrayOfDouble1, paramArrayOfDouble2, i);
/*      */     }
/* 1166 */     if ((i > 2) && ((paramArrayOfDouble2[2] == paramArrayOfDouble2[1]) || (paramArrayOfDouble2[2] == paramArrayOfDouble2[0]))) {
/* 1167 */       i--;
/*      */     }
/* 1169 */     if ((i > 1) && (paramArrayOfDouble2[1] == paramArrayOfDouble2[0])) {
/* 1170 */       paramArrayOfDouble2[1] = paramArrayOfDouble2[(--i)];
/*      */     }
/* 1172 */     return i;
/*      */   }
/*      */ 
/*      */   private static int fixRoots(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, int paramInt)
/*      */   {
/* 1183 */     double[] arrayOfDouble = { paramArrayOfDouble1[1], 2.0D * paramArrayOfDouble1[2], 3.0D * paramArrayOfDouble1[3] };
/* 1184 */     int i = QuadCurve2D.solveQuadratic(arrayOfDouble, arrayOfDouble);
/* 1185 */     if ((i == 2) && (arrayOfDouble[0] == arrayOfDouble[1]))
/* 1186 */       i--;
/*      */     double d1;
/* 1188 */     if ((i == 2) && (arrayOfDouble[0] > arrayOfDouble[1])) {
/* 1189 */       d1 = arrayOfDouble[0];
/* 1190 */       arrayOfDouble[0] = arrayOfDouble[1];
/* 1191 */       arrayOfDouble[1] = d1;
/*      */     }
/*      */     double d2;
/*      */     double d3;
/*      */     double d4;
/*      */     double d5;
/*      */     double d6;
/* 1204 */     if (paramInt == 3) {
/* 1205 */       d1 = getRootUpperBound(paramArrayOfDouble1);
/* 1206 */       d2 = -d1;
/*      */ 
/* 1208 */       Arrays.sort(paramArrayOfDouble2, 0, paramInt);
/* 1209 */       if (i == 2)
/*      */       {
/* 1212 */         paramArrayOfDouble2[0] = refineRootWithHint(paramArrayOfDouble1, d2, arrayOfDouble[0], paramArrayOfDouble2[0]);
/* 1213 */         paramArrayOfDouble2[1] = refineRootWithHint(paramArrayOfDouble1, arrayOfDouble[0], arrayOfDouble[1], paramArrayOfDouble2[1]);
/* 1214 */         paramArrayOfDouble2[2] = refineRootWithHint(paramArrayOfDouble1, arrayOfDouble[1], d1, paramArrayOfDouble2[2]);
/* 1215 */         return 3;
/* 1216 */       }if (i == 1)
/*      */       {
/* 1220 */         d3 = paramArrayOfDouble1[3];
/* 1221 */         d4 = -d3;
/*      */ 
/* 1223 */         d5 = arrayOfDouble[0];
/* 1224 */         d6 = solveEqn(paramArrayOfDouble1, 3, d5);
/*      */ 
/* 1236 */         if (oppositeSigns(d4, d6))
/* 1237 */           paramArrayOfDouble2[0] = bisectRootWithHint(paramArrayOfDouble1, d2, d5, paramArrayOfDouble2[0]);
/* 1238 */         else if (oppositeSigns(d6, d3))
/* 1239 */           paramArrayOfDouble2[0] = bisectRootWithHint(paramArrayOfDouble1, d5, d1, paramArrayOfDouble2[2]);
/*      */         else {
/* 1241 */           paramArrayOfDouble2[0] = d5;
/*      */         }
/*      */       }
/* 1244 */       else if (i == 0) {
/* 1245 */         paramArrayOfDouble2[0] = bisectRootWithHint(paramArrayOfDouble1, d2, d1, paramArrayOfDouble2[1]);
/*      */       }
/*      */     }
/* 1248 */     else if ((paramInt == 2) && (i == 2))
/*      */     {
/* 1257 */       d1 = paramArrayOfDouble2[0];
/* 1258 */       d2 = paramArrayOfDouble2[1];
/* 1259 */       d3 = arrayOfDouble[0];
/* 1260 */       d4 = arrayOfDouble[1];
/*      */ 
/* 1268 */       d5 = Math.abs(d3 - d1) > Math.abs(d4 - d1) ? d3 : d4;
/* 1269 */       d6 = solveEqn(paramArrayOfDouble1, 3, d5);
/*      */ 
/* 1271 */       if (iszero(d6, 10000000.0D * Math.ulp(d5))) {
/* 1272 */         double d7 = solveEqn(paramArrayOfDouble1, 3, d2);
/* 1273 */         paramArrayOfDouble2[1] = (Math.abs(d7) < Math.abs(d6) ? d2 : d5);
/* 1274 */         return 2;
/*      */       }
/*      */     }
/*      */ 
/* 1278 */     return 1;
/*      */   }
/*      */ 
/*      */   private static double refineRootWithHint(double[] paramArrayOfDouble, double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/* 1283 */     if (!inInterval(paramDouble3, paramDouble1, paramDouble2)) {
/* 1284 */       return paramDouble3;
/*      */     }
/* 1286 */     double[] arrayOfDouble = { paramArrayOfDouble[1], 2.0D * paramArrayOfDouble[2], 3.0D * paramArrayOfDouble[3] };
/* 1287 */     double d1 = paramDouble3;
/* 1288 */     for (int i = 0; i < 3; i++) {
/* 1289 */       double d2 = solveEqn(arrayOfDouble, 2, paramDouble3);
/* 1290 */       double d3 = solveEqn(paramArrayOfDouble, 3, paramDouble3);
/* 1291 */       double d4 = -(d3 / d2);
/* 1292 */       double d5 = paramDouble3 + d4;
/*      */ 
/* 1294 */       if ((d2 == 0.0D) || (d3 == 0.0D) || (paramDouble3 == d5))
/*      */       {
/*      */         break;
/*      */       }
/* 1298 */       paramDouble3 = d5;
/*      */     }
/* 1300 */     if ((within(paramDouble3, d1, 1000.0D * Math.ulp(d1))) && (inInterval(paramDouble3, paramDouble1, paramDouble2))) {
/* 1301 */       return paramDouble3;
/*      */     }
/* 1303 */     return d1;
/*      */   }
/*      */ 
/*      */   private static double bisectRootWithHint(double[] paramArrayOfDouble, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 1307 */     double d1 = Math.min(Math.abs(paramDouble3 - paramDouble1) / 64.0D, 0.0625D);
/* 1308 */     double d2 = Math.min(Math.abs(paramDouble3 - paramDouble2) / 64.0D, 0.0625D);
/* 1309 */     double d3 = paramDouble3 - d1;
/* 1310 */     double d4 = paramDouble3 + d2;
/* 1311 */     double d5 = solveEqn(paramArrayOfDouble, 3, d3);
/* 1312 */     double d6 = solveEqn(paramArrayOfDouble, 3, d4);
/* 1313 */     while (oppositeSigns(d5, d6)) {
/* 1314 */       if (d3 >= d4) {
/* 1315 */         return d3;
/*      */       }
/* 1317 */       paramDouble1 = d3;
/* 1318 */       paramDouble2 = d4;
/* 1319 */       d1 /= 64.0D;
/* 1320 */       d2 /= 64.0D;
/* 1321 */       d3 = paramDouble3 - d1;
/* 1322 */       d4 = paramDouble3 + d2;
/* 1323 */       d5 = solveEqn(paramArrayOfDouble, 3, d3);
/* 1324 */       d6 = solveEqn(paramArrayOfDouble, 3, d4);
/*      */     }
/* 1326 */     if (d5 == 0.0D) {
/* 1327 */       return d3;
/*      */     }
/* 1329 */     if (d6 == 0.0D) {
/* 1330 */       return d4;
/*      */     }
/*      */ 
/* 1333 */     return bisectRoot(paramArrayOfDouble, paramDouble1, paramDouble2);
/*      */   }
/*      */ 
/*      */   private static double bisectRoot(double[] paramArrayOfDouble, double paramDouble1, double paramDouble2) {
/* 1337 */     double d1 = solveEqn(paramArrayOfDouble, 3, paramDouble1);
/* 1338 */     double d2 = paramDouble1 + (paramDouble2 - paramDouble1) / 2.0D;
/* 1339 */     while ((d2 != paramDouble1) && (d2 != paramDouble2)) {
/* 1340 */       double d3 = solveEqn(paramArrayOfDouble, 3, d2);
/* 1341 */       if (d3 == 0.0D) {
/* 1342 */         return d2;
/*      */       }
/* 1344 */       if (oppositeSigns(d1, d3)) {
/* 1345 */         paramDouble2 = d2;
/*      */       } else {
/* 1347 */         d1 = d3;
/* 1348 */         paramDouble1 = d2;
/*      */       }
/* 1350 */       d2 = paramDouble1 + (paramDouble2 - paramDouble1) / 2.0D;
/*      */     }
/* 1352 */     return d2;
/*      */   }
/*      */ 
/*      */   private static boolean inInterval(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 1356 */     return (paramDouble2 <= paramDouble1) && (paramDouble1 <= paramDouble3);
/*      */   }
/*      */ 
/*      */   private static boolean within(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 1360 */     double d = paramDouble2 - paramDouble1;
/* 1361 */     return (d <= paramDouble3) && (d >= -paramDouble3);
/*      */   }
/*      */ 
/*      */   private static boolean iszero(double paramDouble1, double paramDouble2) {
/* 1365 */     return within(paramDouble1, 0.0D, paramDouble2);
/*      */   }
/*      */ 
/*      */   private static boolean oppositeSigns(double paramDouble1, double paramDouble2) {
/* 1369 */     return ((paramDouble1 < 0.0D) && (paramDouble2 > 0.0D)) || ((paramDouble1 > 0.0D) && (paramDouble2 < 0.0D));
/*      */   }
/*      */ 
/*      */   private static double solveEqn(double[] paramArrayOfDouble, int paramInt, double paramDouble) {
/* 1373 */     double d = paramArrayOfDouble[paramInt];
/*      */     while (true) { paramInt--; if (paramInt < 0) break;
/* 1375 */       d = d * paramDouble + paramArrayOfDouble[paramInt];
/*      */     }
/* 1377 */     return d;
/*      */   }
/*      */ 
/*      */   private static double getRootUpperBound(double[] paramArrayOfDouble)
/*      */   {
/* 1389 */     double d1 = paramArrayOfDouble[3];
/* 1390 */     double d2 = paramArrayOfDouble[2];
/* 1391 */     double d3 = paramArrayOfDouble[1];
/* 1392 */     double d4 = paramArrayOfDouble[0];
/*      */ 
/* 1394 */     double d5 = 1.0D + Math.max(Math.max(Math.abs(d2), Math.abs(d3)), Math.abs(d4)) / Math.abs(d1);
/* 1395 */     d5 += Math.ulp(d5) + 1.0D;
/* 1396 */     return d5;
/*      */   }
/*      */ 
/*      */   public boolean contains(double paramDouble1, double paramDouble2)
/*      */   {
/* 1405 */     if (paramDouble1 * 0.0D + paramDouble2 * 0.0D != 0.0D)
/*      */     {
/* 1411 */       return false;
/*      */     }
/*      */ 
/* 1415 */     double d1 = getX1();
/* 1416 */     double d2 = getY1();
/* 1417 */     double d3 = getX2();
/* 1418 */     double d4 = getY2();
/* 1419 */     int i = Curve.pointCrossingsForLine(paramDouble1, paramDouble2, d1, d2, d3, d4) + Curve.pointCrossingsForCubic(paramDouble1, paramDouble2, d1, d2, getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), d3, d4, 0);
/*      */ 
/* 1426 */     return (i & 0x1) == 1;
/*      */   }
/*      */ 
/*      */   public boolean contains(Point2D paramPoint2D)
/*      */   {
/* 1434 */     return contains(paramPoint2D.getX(), paramPoint2D.getY());
/*      */   }
/*      */ 
/*      */   public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 1443 */     if ((paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 1444 */       return false;
/*      */     }
/*      */ 
/* 1447 */     int i = rectCrossings(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*      */ 
/* 1453 */     return i != 0;
/*      */   }
/*      */ 
/*      */   public boolean intersects(Rectangle2D paramRectangle2D)
/*      */   {
/* 1461 */     return intersects(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*      */   }
/*      */ 
/*      */   public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 1469 */     if ((paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 1470 */       return false;
/*      */     }
/*      */ 
/* 1473 */     int i = rectCrossings(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/* 1474 */     return (i != 0) && (i != -2147483648);
/*      */   }
/*      */ 
/*      */   private int rectCrossings(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 1478 */     int i = 0;
/* 1479 */     if ((getX1() != getX2()) || (getY1() != getY2())) {
/* 1480 */       i = Curve.rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4, getX1(), getY1(), getX2(), getY2());
/*      */ 
/* 1485 */       if (i == -2147483648) {
/* 1486 */         return i;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1491 */     return Curve.rectCrossingsForCubic(i, paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4, getX2(), getY2(), getCtrlX2(), getCtrlY2(), getCtrlX1(), getCtrlY1(), getX1(), getY1(), 0);
/*      */   }
/*      */ 
/*      */   public boolean contains(Rectangle2D paramRectangle2D)
/*      */   {
/* 1505 */     return contains(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*      */   }
/*      */ 
/*      */   public Rectangle getBounds()
/*      */   {
/* 1513 */     return getBounds2D().getBounds();
/*      */   }
/*      */ 
/*      */   public PathIterator getPathIterator(AffineTransform paramAffineTransform)
/*      */   {
/* 1533 */     return new CubicIterator(this, paramAffineTransform);
/*      */   }
/*      */ 
/*      */   public PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble)
/*      */   {
/* 1556 */     return new FlatteningPathIterator(getPathIterator(paramAffineTransform), paramDouble);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 1569 */       return super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/* 1572 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public static class Double extends CubicCurve2D
/*      */     implements Serializable
/*      */   {
/*      */     public double x1;
/*      */     public double y1;
/*      */     public double ctrlx1;
/*      */     public double ctrly1;
/*      */     public double ctrlx2;
/*      */     public double ctrly2;
/*      */     public double x2;
/*      */     public double y2;
/*      */     private static final long serialVersionUID = -4202960122839707295L;
/*      */ 
/*      */     public Double()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Double(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8)
/*      */     {
/*  440 */       setCurve(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8);
/*      */     }
/*      */ 
/*      */     public double getX1()
/*      */     {
/*  448 */       return this.x1;
/*      */     }
/*      */ 
/*      */     public double getY1()
/*      */     {
/*  456 */       return this.y1;
/*      */     }
/*      */ 
/*      */     public Point2D getP1()
/*      */     {
/*  464 */       return new Point2D.Double(this.x1, this.y1);
/*      */     }
/*      */ 
/*      */     public double getCtrlX1()
/*      */     {
/*  472 */       return this.ctrlx1;
/*      */     }
/*      */ 
/*      */     public double getCtrlY1()
/*      */     {
/*  480 */       return this.ctrly1;
/*      */     }
/*      */ 
/*      */     public Point2D getCtrlP1()
/*      */     {
/*  488 */       return new Point2D.Double(this.ctrlx1, this.ctrly1);
/*      */     }
/*      */ 
/*      */     public double getCtrlX2()
/*      */     {
/*  496 */       return this.ctrlx2;
/*      */     }
/*      */ 
/*      */     public double getCtrlY2()
/*      */     {
/*  504 */       return this.ctrly2;
/*      */     }
/*      */ 
/*      */     public Point2D getCtrlP2()
/*      */     {
/*  512 */       return new Point2D.Double(this.ctrlx2, this.ctrly2);
/*      */     }
/*      */ 
/*      */     public double getX2()
/*      */     {
/*  520 */       return this.x2;
/*      */     }
/*      */ 
/*      */     public double getY2()
/*      */     {
/*  528 */       return this.y2;
/*      */     }
/*      */ 
/*      */     public Point2D getP2()
/*      */     {
/*  536 */       return new Point2D.Double(this.x2, this.y2);
/*      */     }
/*      */ 
/*      */     public void setCurve(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8)
/*      */     {
/*  548 */       this.x1 = paramDouble1;
/*  549 */       this.y1 = paramDouble2;
/*  550 */       this.ctrlx1 = paramDouble3;
/*  551 */       this.ctrly1 = paramDouble4;
/*  552 */       this.ctrlx2 = paramDouble5;
/*  553 */       this.ctrly2 = paramDouble6;
/*  554 */       this.x2 = paramDouble7;
/*  555 */       this.y2 = paramDouble8;
/*      */     }
/*      */ 
/*      */     public Rectangle2D getBounds2D()
/*      */     {
/*  563 */       double d1 = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
/*      */ 
/*  565 */       double d2 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
/*      */ 
/*  567 */       double d3 = Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2));
/*      */ 
/*  569 */       double d4 = Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2));
/*      */ 
/*  571 */       return new Rectangle2D.Double(d1, d2, d3 - d1, d4 - d2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Float extends CubicCurve2D
/*      */     implements Serializable
/*      */   {
/*      */     public float x1;
/*      */     public float y1;
/*      */     public float ctrlx1;
/*      */     public float ctrly1;
/*      */     public float ctrlx2;
/*      */     public float ctrly2;
/*      */     public float x2;
/*      */     public float y2;
/*      */     private static final long serialVersionUID = -1272015596714244385L;
/*      */ 
/*      */     public Float()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Float(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
/*      */     {
/*  157 */       setCurve(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8);
/*      */     }
/*      */ 
/*      */     public double getX1()
/*      */     {
/*  165 */       return this.x1;
/*      */     }
/*      */ 
/*      */     public double getY1()
/*      */     {
/*  173 */       return this.y1;
/*      */     }
/*      */ 
/*      */     public Point2D getP1()
/*      */     {
/*  181 */       return new Point2D.Float(this.x1, this.y1);
/*      */     }
/*      */ 
/*      */     public double getCtrlX1()
/*      */     {
/*  189 */       return this.ctrlx1;
/*      */     }
/*      */ 
/*      */     public double getCtrlY1()
/*      */     {
/*  197 */       return this.ctrly1;
/*      */     }
/*      */ 
/*      */     public Point2D getCtrlP1()
/*      */     {
/*  205 */       return new Point2D.Float(this.ctrlx1, this.ctrly1);
/*      */     }
/*      */ 
/*      */     public double getCtrlX2()
/*      */     {
/*  213 */       return this.ctrlx2;
/*      */     }
/*      */ 
/*      */     public double getCtrlY2()
/*      */     {
/*  221 */       return this.ctrly2;
/*      */     }
/*      */ 
/*      */     public Point2D getCtrlP2()
/*      */     {
/*  229 */       return new Point2D.Float(this.ctrlx2, this.ctrly2);
/*      */     }
/*      */ 
/*      */     public double getX2()
/*      */     {
/*  237 */       return this.x2;
/*      */     }
/*      */ 
/*      */     public double getY2()
/*      */     {
/*  245 */       return this.y2;
/*      */     }
/*      */ 
/*      */     public Point2D getP2()
/*      */     {
/*  253 */       return new Point2D.Float(this.x2, this.y2);
/*      */     }
/*      */ 
/*      */     public void setCurve(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8)
/*      */     {
/*  265 */       this.x1 = ((float)paramDouble1);
/*  266 */       this.y1 = ((float)paramDouble2);
/*  267 */       this.ctrlx1 = ((float)paramDouble3);
/*  268 */       this.ctrly1 = ((float)paramDouble4);
/*  269 */       this.ctrlx2 = ((float)paramDouble5);
/*  270 */       this.ctrly2 = ((float)paramDouble6);
/*  271 */       this.x2 = ((float)paramDouble7);
/*  272 */       this.y2 = ((float)paramDouble8);
/*      */     }
/*      */ 
/*      */     public void setCurve(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
/*      */     {
/*  302 */       this.x1 = paramFloat1;
/*  303 */       this.y1 = paramFloat2;
/*  304 */       this.ctrlx1 = paramFloat3;
/*  305 */       this.ctrly1 = paramFloat4;
/*  306 */       this.ctrlx2 = paramFloat5;
/*  307 */       this.ctrly2 = paramFloat6;
/*  308 */       this.x2 = paramFloat7;
/*  309 */       this.y2 = paramFloat8;
/*      */     }
/*      */ 
/*      */     public Rectangle2D getBounds2D()
/*      */     {
/*  317 */       float f1 = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
/*      */ 
/*  319 */       float f2 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
/*      */ 
/*  321 */       float f3 = Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2));
/*      */ 
/*  323 */       float f4 = Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2));
/*      */ 
/*  325 */       return new Rectangle2D.Float(f1, f2, f3 - f1, f4 - f2);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.CubicCurve2D
 * JD-Core Version:    0.6.2
 */