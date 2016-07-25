/*      */ package java.awt.geom;
/*      */ 
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public abstract class QuadCurve2D
/*      */   implements Shape, Cloneable
/*      */ {
/*      */   private static final int BELOW = -2;
/*      */   private static final int LOWEDGE = -1;
/*      */   private static final int INSIDE = 0;
/*      */   private static final int HIGHEDGE = 1;
/*      */   private static final int ABOVE = 2;
/*      */ 
/*      */   public abstract double getX1();
/*      */ 
/*      */   public abstract double getY1();
/*      */ 
/*      */   public abstract Point2D getP1();
/*      */ 
/*      */   public abstract double getCtrlX();
/*      */ 
/*      */   public abstract double getCtrlY();
/*      */ 
/*      */   public abstract Point2D getCtrlPt();
/*      */ 
/*      */   public abstract double getX2();
/*      */ 
/*      */   public abstract double getY2();
/*      */ 
/*      */   public abstract Point2D getP2();
/*      */ 
/*      */   public abstract void setCurve(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*      */ 
/*      */   public void setCurve(double[] paramArrayOfDouble, int paramInt)
/*      */   {
/*  562 */     setCurve(paramArrayOfDouble[(paramInt + 0)], paramArrayOfDouble[(paramInt + 1)], paramArrayOfDouble[(paramInt + 2)], paramArrayOfDouble[(paramInt + 3)], paramArrayOfDouble[(paramInt + 4)], paramArrayOfDouble[(paramInt + 5)]);
/*      */   }
/*      */ 
/*      */   public void setCurve(Point2D paramPoint2D1, Point2D paramPoint2D2, Point2D paramPoint2D3)
/*      */   {
/*  577 */     setCurve(paramPoint2D1.getX(), paramPoint2D1.getY(), paramPoint2D2.getX(), paramPoint2D2.getY(), paramPoint2D3.getX(), paramPoint2D3.getY());
/*      */   }
/*      */ 
/*      */   public void setCurve(Point2D[] paramArrayOfPoint2D, int paramInt)
/*      */   {
/*  595 */     setCurve(paramArrayOfPoint2D[(paramInt + 0)].getX(), paramArrayOfPoint2D[(paramInt + 0)].getY(), paramArrayOfPoint2D[(paramInt + 1)].getX(), paramArrayOfPoint2D[(paramInt + 1)].getY(), paramArrayOfPoint2D[(paramInt + 2)].getX(), paramArrayOfPoint2D[(paramInt + 2)].getY());
/*      */   }
/*      */ 
/*      */   public void setCurve(QuadCurve2D paramQuadCurve2D)
/*      */   {
/*  608 */     setCurve(paramQuadCurve2D.getX1(), paramQuadCurve2D.getY1(), paramQuadCurve2D.getCtrlX(), paramQuadCurve2D.getCtrlY(), paramQuadCurve2D.getX2(), paramQuadCurve2D.getY2());
/*      */   }
/*      */ 
/*      */   public static double getFlatnessSq(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */   {
/*  631 */     return Line2D.ptSegDistSq(paramDouble1, paramDouble2, paramDouble5, paramDouble6, paramDouble3, paramDouble4);
/*      */   }
/*      */ 
/*      */   public static double getFlatness(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */   {
/*  652 */     return Line2D.ptSegDist(paramDouble1, paramDouble2, paramDouble5, paramDouble6, paramDouble3, paramDouble4);
/*      */   }
/*      */ 
/*      */   public static double getFlatnessSq(double[] paramArrayOfDouble, int paramInt)
/*      */   {
/*  668 */     return Line2D.ptSegDistSq(paramArrayOfDouble[(paramInt + 0)], paramArrayOfDouble[(paramInt + 1)], paramArrayOfDouble[(paramInt + 4)], paramArrayOfDouble[(paramInt + 5)], paramArrayOfDouble[(paramInt + 2)], paramArrayOfDouble[(paramInt + 3)]);
/*      */   }
/*      */ 
/*      */   public static double getFlatness(double[] paramArrayOfDouble, int paramInt)
/*      */   {
/*  686 */     return Line2D.ptSegDist(paramArrayOfDouble[(paramInt + 0)], paramArrayOfDouble[(paramInt + 1)], paramArrayOfDouble[(paramInt + 4)], paramArrayOfDouble[(paramInt + 5)], paramArrayOfDouble[(paramInt + 2)], paramArrayOfDouble[(paramInt + 3)]);
/*      */   }
/*      */ 
/*      */   public double getFlatnessSq()
/*      */   {
/*  700 */     return Line2D.ptSegDistSq(getX1(), getY1(), getX2(), getY2(), getCtrlX(), getCtrlY());
/*      */   }
/*      */ 
/*      */   public double getFlatness()
/*      */   {
/*  713 */     return Line2D.ptSegDist(getX1(), getY1(), getX2(), getY2(), getCtrlX(), getCtrlY());
/*      */   }
/*      */ 
/*      */   public void subdivide(QuadCurve2D paramQuadCurve2D1, QuadCurve2D paramQuadCurve2D2)
/*      */   {
/*  732 */     subdivide(this, paramQuadCurve2D1, paramQuadCurve2D2);
/*      */   }
/*      */ 
/*      */   public static void subdivide(QuadCurve2D paramQuadCurve2D1, QuadCurve2D paramQuadCurve2D2, QuadCurve2D paramQuadCurve2D3)
/*      */   {
/*  752 */     double d1 = paramQuadCurve2D1.getX1();
/*  753 */     double d2 = paramQuadCurve2D1.getY1();
/*  754 */     double d3 = paramQuadCurve2D1.getCtrlX();
/*  755 */     double d4 = paramQuadCurve2D1.getCtrlY();
/*  756 */     double d5 = paramQuadCurve2D1.getX2();
/*  757 */     double d6 = paramQuadCurve2D1.getY2();
/*  758 */     double d7 = (d1 + d3) / 2.0D;
/*  759 */     double d8 = (d2 + d4) / 2.0D;
/*  760 */     double d9 = (d5 + d3) / 2.0D;
/*  761 */     double d10 = (d6 + d4) / 2.0D;
/*  762 */     d3 = (d7 + d9) / 2.0D;
/*  763 */     d4 = (d8 + d10) / 2.0D;
/*  764 */     if (paramQuadCurve2D2 != null) {
/*  765 */       paramQuadCurve2D2.setCurve(d1, d2, d7, d8, d3, d4);
/*      */     }
/*  767 */     if (paramQuadCurve2D3 != null)
/*  768 */       paramQuadCurve2D3.setCurve(d3, d4, d9, d10, d5, d6);
/*      */   }
/*      */ 
/*      */   public static void subdivide(double[] paramArrayOfDouble1, int paramInt1, double[] paramArrayOfDouble2, int paramInt2, double[] paramArrayOfDouble3, int paramInt3)
/*      */   {
/*  803 */     double d1 = paramArrayOfDouble1[(paramInt1 + 0)];
/*  804 */     double d2 = paramArrayOfDouble1[(paramInt1 + 1)];
/*  805 */     double d3 = paramArrayOfDouble1[(paramInt1 + 2)];
/*  806 */     double d4 = paramArrayOfDouble1[(paramInt1 + 3)];
/*  807 */     double d5 = paramArrayOfDouble1[(paramInt1 + 4)];
/*  808 */     double d6 = paramArrayOfDouble1[(paramInt1 + 5)];
/*  809 */     if (paramArrayOfDouble2 != null) {
/*  810 */       paramArrayOfDouble2[(paramInt2 + 0)] = d1;
/*  811 */       paramArrayOfDouble2[(paramInt2 + 1)] = d2;
/*      */     }
/*  813 */     if (paramArrayOfDouble3 != null) {
/*  814 */       paramArrayOfDouble3[(paramInt3 + 4)] = d5;
/*  815 */       paramArrayOfDouble3[(paramInt3 + 5)] = d6;
/*      */     }
/*  817 */     d1 = (d1 + d3) / 2.0D;
/*  818 */     d2 = (d2 + d4) / 2.0D;
/*  819 */     d5 = (d5 + d3) / 2.0D;
/*  820 */     d6 = (d6 + d4) / 2.0D;
/*  821 */     d3 = (d1 + d5) / 2.0D;
/*  822 */     d4 = (d2 + d6) / 2.0D;
/*  823 */     if (paramArrayOfDouble2 != null) {
/*  824 */       paramArrayOfDouble2[(paramInt2 + 2)] = d1;
/*  825 */       paramArrayOfDouble2[(paramInt2 + 3)] = d2;
/*  826 */       paramArrayOfDouble2[(paramInt2 + 4)] = d3;
/*  827 */       paramArrayOfDouble2[(paramInt2 + 5)] = d4;
/*      */     }
/*  829 */     if (paramArrayOfDouble3 != null) {
/*  830 */       paramArrayOfDouble3[(paramInt3 + 0)] = d3;
/*  831 */       paramArrayOfDouble3[(paramInt3 + 1)] = d4;
/*  832 */       paramArrayOfDouble3[(paramInt3 + 2)] = d5;
/*  833 */       paramArrayOfDouble3[(paramInt3 + 3)] = d6;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static int solveQuadratic(double[] paramArrayOfDouble)
/*      */   {
/*  855 */     return solveQuadratic(paramArrayOfDouble, paramArrayOfDouble);
/*      */   }
/*      */ 
/*      */   public static int solveQuadratic(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
/*      */   {
/*  879 */     double d1 = paramArrayOfDouble1[2];
/*  880 */     double d2 = paramArrayOfDouble1[1];
/*  881 */     double d3 = paramArrayOfDouble1[0];
/*  882 */     int i = 0;
/*  883 */     if (d1 == 0.0D)
/*      */     {
/*  885 */       if (d2 == 0.0D)
/*      */       {
/*  887 */         return -1;
/*      */       }
/*  889 */       paramArrayOfDouble2[(i++)] = (-d3 / d2);
/*      */     }
/*      */     else {
/*  892 */       double d4 = d2 * d2 - 4.0D * d1 * d3;
/*  893 */       if (d4 < 0.0D)
/*      */       {
/*  895 */         return 0;
/*      */       }
/*  897 */       d4 = Math.sqrt(d4);
/*      */ 
/*  903 */       if (d2 < 0.0D) {
/*  904 */         d4 = -d4;
/*      */       }
/*  906 */       double d5 = (d2 + d4) / -2.0D;
/*      */ 
/*  908 */       paramArrayOfDouble2[(i++)] = (d5 / d1);
/*  909 */       if (d5 != 0.0D) {
/*  910 */         paramArrayOfDouble2[(i++)] = (d3 / d5);
/*      */       }
/*      */     }
/*  913 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean contains(double paramDouble1, double paramDouble2)
/*      */   {
/*  922 */     double d1 = getX1();
/*  923 */     double d2 = getY1();
/*  924 */     double d3 = getCtrlX();
/*  925 */     double d4 = getCtrlY();
/*  926 */     double d5 = getX2();
/*  927 */     double d6 = getY2();
/*      */ 
/*  995 */     double d7 = d1 - 2.0D * d3 + d5;
/*  996 */     double d8 = d2 - 2.0D * d4 + d6;
/*  997 */     double d9 = paramDouble1 - d1;
/*  998 */     double d10 = paramDouble2 - d2;
/*  999 */     double d11 = d5 - d1;
/* 1000 */     double d12 = d6 - d2;
/*      */ 
/* 1002 */     double d13 = (d9 * d8 - d10 * d7) / (d11 * d8 - d12 * d7);
/* 1003 */     if ((d13 < 0.0D) || (d13 > 1.0D) || (d13 != d13)) {
/* 1004 */       return false;
/*      */     }
/*      */ 
/* 1007 */     double d14 = d7 * d13 * d13 + 2.0D * (d3 - d1) * d13 + d1;
/* 1008 */     double d15 = d8 * d13 * d13 + 2.0D * (d4 - d2) * d13 + d2;
/* 1009 */     double d16 = d11 * d13 + d1;
/* 1010 */     double d17 = d12 * d13 + d2;
/*      */ 
/* 1012 */     return ((paramDouble1 >= d14) && (paramDouble1 < d16)) || ((paramDouble1 >= d16) && (paramDouble1 < d14)) || ((paramDouble2 >= d15) && (paramDouble2 < d17)) || ((paramDouble2 >= d17) && (paramDouble2 < d15));
/*      */   }
/*      */ 
/*      */   public boolean contains(Point2D paramPoint2D)
/*      */   {
/* 1023 */     return contains(paramPoint2D.getX(), paramPoint2D.getY());
/*      */   }
/*      */ 
/*      */   private static void fillEqn(double[] paramArrayOfDouble, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 1041 */     paramArrayOfDouble[0] = (paramDouble2 - paramDouble1);
/* 1042 */     paramArrayOfDouble[1] = (paramDouble3 + paramDouble3 - paramDouble2 - paramDouble2);
/* 1043 */     paramArrayOfDouble[2] = (paramDouble2 - paramDouble3 - paramDouble3 + paramDouble4);
/*      */   }
/*      */ 
/*      */   private static int evalQuadratic(double[] paramArrayOfDouble1, int paramInt, boolean paramBoolean1, boolean paramBoolean2, double[] paramArrayOfDouble2, double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/* 1061 */     int i = 0;
/* 1062 */     for (int j = 0; j < paramInt; j++) {
/* 1063 */       double d1 = paramArrayOfDouble1[j];
/* 1064 */       if ((paramBoolean1 ? d1 >= 0.0D : d1 > 0.0D) && (paramBoolean2 ? d1 <= 1.0D : d1 < 1.0D) && ((paramArrayOfDouble2 == null) || (paramArrayOfDouble2[1] + 2.0D * paramArrayOfDouble2[2] * d1 != 0.0D)))
/*      */       {
/* 1069 */         double d2 = 1.0D - d1;
/* 1070 */         paramArrayOfDouble1[(i++)] = (paramDouble1 * d2 * d2 + 2.0D * paramDouble2 * d1 * d2 + paramDouble3 * d1 * d1);
/*      */       }
/*      */     }
/* 1073 */     return i;
/*      */   }
/*      */ 
/*      */   private static int getTag(double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/* 1089 */     if (paramDouble1 <= paramDouble2) {
/* 1090 */       return paramDouble1 < paramDouble2 ? -2 : -1;
/*      */     }
/* 1092 */     if (paramDouble1 >= paramDouble3) {
/* 1093 */       return paramDouble1 > paramDouble3 ? 2 : 1;
/*      */     }
/* 1095 */     return 0;
/*      */   }
/*      */ 
/*      */   private static boolean inwards(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1106 */     switch (paramInt1) {
/*      */     case -2:
/*      */     case 2:
/*      */     default:
/* 1110 */       return false;
/*      */     case -1:
/* 1112 */       return (paramInt2 >= 0) || (paramInt3 >= 0);
/*      */     case 0:
/* 1114 */       return true;
/*      */     case 1:
/* 1116 */     }return (paramInt2 <= 0) || (paramInt3 <= 0);
/*      */   }
/*      */ 
/*      */   public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 1126 */     if ((paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 1127 */       return false;
/*      */     }
/*      */ 
/* 1134 */     double d1 = getX1();
/* 1135 */     double d2 = getY1();
/* 1136 */     int i = getTag(d1, paramDouble1, paramDouble1 + paramDouble3);
/* 1137 */     int j = getTag(d2, paramDouble2, paramDouble2 + paramDouble4);
/* 1138 */     if ((i == 0) && (j == 0)) {
/* 1139 */       return true;
/*      */     }
/* 1141 */     double d3 = getX2();
/* 1142 */     double d4 = getY2();
/* 1143 */     int k = getTag(d3, paramDouble1, paramDouble1 + paramDouble3);
/* 1144 */     int m = getTag(d4, paramDouble2, paramDouble2 + paramDouble4);
/* 1145 */     if ((k == 0) && (m == 0)) {
/* 1146 */       return true;
/*      */     }
/* 1148 */     double d5 = getCtrlX();
/* 1149 */     double d6 = getCtrlY();
/* 1150 */     int n = getTag(d5, paramDouble1, paramDouble1 + paramDouble3);
/* 1151 */     int i1 = getTag(d6, paramDouble2, paramDouble2 + paramDouble4);
/*      */ 
/* 1155 */     if ((i < 0) && (k < 0) && (n < 0)) {
/* 1156 */       return false;
/*      */     }
/* 1158 */     if ((j < 0) && (m < 0) && (i1 < 0)) {
/* 1159 */       return false;
/*      */     }
/* 1161 */     if ((i > 0) && (k > 0) && (n > 0)) {
/* 1162 */       return false;
/*      */     }
/* 1164 */     if ((j > 0) && (m > 0) && (i1 > 0)) {
/* 1165 */       return false;
/*      */     }
/*      */ 
/* 1173 */     if ((inwards(i, k, n)) && (inwards(j, m, i1)))
/*      */     {
/* 1177 */       return true;
/*      */     }
/* 1179 */     if ((inwards(k, i, n)) && (inwards(m, j, i1)))
/*      */     {
/* 1183 */       return true;
/*      */     }
/*      */ 
/* 1187 */     int i2 = i * k <= 0 ? 1 : 0;
/* 1188 */     int i3 = j * m <= 0 ? 1 : 0;
/* 1189 */     if ((i == 0) && (k == 0) && (i3 != 0)) {
/* 1190 */       return true;
/*      */     }
/* 1192 */     if ((j == 0) && (m == 0) && (i2 != 0)) {
/* 1193 */       return true;
/*      */     }
/*      */ 
/* 1202 */     double[] arrayOfDouble1 = new double[3];
/* 1203 */     double[] arrayOfDouble2 = new double[3];
/* 1204 */     if (i3 == 0)
/*      */     {
/* 1210 */       fillEqn(arrayOfDouble1, j < 0 ? paramDouble2 : paramDouble2 + paramDouble4, d2, d6, d4);
/* 1211 */       return (solveQuadratic(arrayOfDouble1, arrayOfDouble2) == 2) && (evalQuadratic(arrayOfDouble2, 2, true, true, null, d1, d5, d3) == 2) && (getTag(arrayOfDouble2[0], paramDouble1, paramDouble1 + paramDouble3) * getTag(arrayOfDouble2[1], paramDouble1, paramDouble1 + paramDouble3) <= 0);
/*      */     }
/*      */ 
/* 1218 */     if (i2 == 0)
/*      */     {
/* 1224 */       fillEqn(arrayOfDouble1, i < 0 ? paramDouble1 : paramDouble1 + paramDouble3, d1, d5, d3);
/* 1225 */       return (solveQuadratic(arrayOfDouble1, arrayOfDouble2) == 2) && (evalQuadratic(arrayOfDouble2, 2, true, true, null, d2, d6, d4) == 2) && (getTag(arrayOfDouble2[0], paramDouble2, paramDouble2 + paramDouble4) * getTag(arrayOfDouble2[1], paramDouble2, paramDouble2 + paramDouble4) <= 0);
/*      */     }
/*      */ 
/* 1234 */     double d7 = d3 - d1;
/* 1235 */     double d8 = d4 - d2;
/* 1236 */     double d9 = d4 * d1 - d3 * d2;
/*      */ 
/* 1238 */     if (j == 0)
/* 1239 */       i4 = i;
/*      */     else {
/* 1241 */       i4 = getTag((d9 + d7 * (j < 0 ? paramDouble2 : paramDouble2 + paramDouble4)) / d8, paramDouble1, paramDouble1 + paramDouble3);
/*      */     }
/* 1243 */     if (m == 0)
/* 1244 */       i5 = k;
/*      */     else {
/* 1246 */       i5 = getTag((d9 + d7 * (m < 0 ? paramDouble2 : paramDouble2 + paramDouble4)) / d8, paramDouble1, paramDouble1 + paramDouble3);
/*      */     }
/*      */ 
/* 1250 */     if (i4 * i5 <= 0) {
/* 1251 */       return true;
/*      */     }
/*      */ 
/* 1280 */     int i4 = i4 * i <= 0 ? j : m;
/*      */ 
/* 1287 */     fillEqn(arrayOfDouble1, i5 < 0 ? paramDouble1 : paramDouble1 + paramDouble3, d1, d5, d3);
/* 1288 */     int i6 = solveQuadratic(arrayOfDouble1, arrayOfDouble2);
/*      */ 
/* 1293 */     evalQuadratic(arrayOfDouble2, i6, true, true, null, d2, d6, d4);
/*      */ 
/* 1297 */     int i5 = getTag(arrayOfDouble2[0], paramDouble2, paramDouble2 + paramDouble4);
/*      */ 
/* 1301 */     return i4 * i5 <= 0;
/*      */   }
/*      */ 
/*      */   public boolean intersects(Rectangle2D paramRectangle2D)
/*      */   {
/* 1309 */     return intersects(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*      */   }
/*      */ 
/*      */   public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 1317 */     if ((paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 1318 */       return false;
/*      */     }
/*      */ 
/* 1322 */     return (contains(paramDouble1, paramDouble2)) && (contains(paramDouble1 + paramDouble3, paramDouble2)) && (contains(paramDouble1 + paramDouble3, paramDouble2 + paramDouble4)) && (contains(paramDouble1, paramDouble2 + paramDouble4));
/*      */   }
/*      */ 
/*      */   public boolean contains(Rectangle2D paramRectangle2D)
/*      */   {
/* 1333 */     return contains(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*      */   }
/*      */ 
/*      */   public Rectangle getBounds()
/*      */   {
/* 1341 */     return getBounds2D().getBounds();
/*      */   }
/*      */ 
/*      */   public PathIterator getPathIterator(AffineTransform paramAffineTransform)
/*      */   {
/* 1359 */     return new QuadIterator(this, paramAffineTransform);
/*      */   }
/*      */ 
/*      */   public PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble)
/*      */   {
/* 1381 */     return new FlatteningPathIterator(getPathIterator(paramAffineTransform), paramDouble);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 1395 */       return super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/* 1398 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public static class Double extends QuadCurve2D
/*      */     implements Serializable
/*      */   {
/*      */     public double x1;
/*      */     public double y1;
/*      */     public double ctrlx;
/*      */     public double ctrly;
/*      */     public double x2;
/*      */     public double y2;
/*      */     private static final long serialVersionUID = 4217149928428559721L;
/*      */ 
/*      */     public Double()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Double(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */     {
/*  339 */       setCurve(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6);
/*      */     }
/*      */ 
/*      */     public double getX1()
/*      */     {
/*  347 */       return this.x1;
/*      */     }
/*      */ 
/*      */     public double getY1()
/*      */     {
/*  355 */       return this.y1;
/*      */     }
/*      */ 
/*      */     public Point2D getP1()
/*      */     {
/*  363 */       return new Point2D.Double(this.x1, this.y1);
/*      */     }
/*      */ 
/*      */     public double getCtrlX()
/*      */     {
/*  371 */       return this.ctrlx;
/*      */     }
/*      */ 
/*      */     public double getCtrlY()
/*      */     {
/*  379 */       return this.ctrly;
/*      */     }
/*      */ 
/*      */     public Point2D getCtrlPt()
/*      */     {
/*  387 */       return new Point2D.Double(this.ctrlx, this.ctrly);
/*      */     }
/*      */ 
/*      */     public double getX2()
/*      */     {
/*  395 */       return this.x2;
/*      */     }
/*      */ 
/*      */     public double getY2()
/*      */     {
/*  403 */       return this.y2;
/*      */     }
/*      */ 
/*      */     public Point2D getP2()
/*      */     {
/*  411 */       return new Point2D.Double(this.x2, this.y2);
/*      */     }
/*      */ 
/*      */     public void setCurve(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */     {
/*  422 */       this.x1 = paramDouble1;
/*  423 */       this.y1 = paramDouble2;
/*  424 */       this.ctrlx = paramDouble3;
/*  425 */       this.ctrly = paramDouble4;
/*  426 */       this.x2 = paramDouble5;
/*  427 */       this.y2 = paramDouble6;
/*      */     }
/*      */ 
/*      */     public Rectangle2D getBounds2D()
/*      */     {
/*  435 */       double d1 = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
/*  436 */       double d2 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
/*  437 */       double d3 = Math.max(Math.max(this.x1, this.x2), this.ctrlx);
/*  438 */       double d4 = Math.max(Math.max(this.y1, this.y2), this.ctrly);
/*  439 */       return new Rectangle2D.Double(d1, d2, d3 - d1, d4 - d2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Float extends QuadCurve2D
/*      */     implements Serializable
/*      */   {
/*      */     public float x1;
/*      */     public float y1;
/*      */     public float ctrlx;
/*      */     public float ctrly;
/*      */     public float x2;
/*      */     public float y2;
/*      */     private static final long serialVersionUID = -8511188402130719609L;
/*      */ 
/*      */     public Float()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Float(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
/*      */     {
/*  126 */       setCurve(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
/*      */     }
/*      */ 
/*      */     public double getX1()
/*      */     {
/*  134 */       return this.x1;
/*      */     }
/*      */ 
/*      */     public double getY1()
/*      */     {
/*  142 */       return this.y1;
/*      */     }
/*      */ 
/*      */     public Point2D getP1()
/*      */     {
/*  150 */       return new Point2D.Float(this.x1, this.y1);
/*      */     }
/*      */ 
/*      */     public double getCtrlX()
/*      */     {
/*  158 */       return this.ctrlx;
/*      */     }
/*      */ 
/*      */     public double getCtrlY()
/*      */     {
/*  166 */       return this.ctrly;
/*      */     }
/*      */ 
/*      */     public Point2D getCtrlPt()
/*      */     {
/*  174 */       return new Point2D.Float(this.ctrlx, this.ctrly);
/*      */     }
/*      */ 
/*      */     public double getX2()
/*      */     {
/*  182 */       return this.x2;
/*      */     }
/*      */ 
/*      */     public double getY2()
/*      */     {
/*  190 */       return this.y2;
/*      */     }
/*      */ 
/*      */     public Point2D getP2()
/*      */     {
/*  198 */       return new Point2D.Float(this.x2, this.y2);
/*      */     }
/*      */ 
/*      */     public void setCurve(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */     {
/*  209 */       this.x1 = ((float)paramDouble1);
/*  210 */       this.y1 = ((float)paramDouble2);
/*  211 */       this.ctrlx = ((float)paramDouble3);
/*  212 */       this.ctrly = ((float)paramDouble4);
/*  213 */       this.x2 = ((float)paramDouble5);
/*  214 */       this.y2 = ((float)paramDouble6);
/*      */     }
/*      */ 
/*      */     public void setCurve(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
/*      */     {
/*  233 */       this.x1 = paramFloat1;
/*  234 */       this.y1 = paramFloat2;
/*  235 */       this.ctrlx = paramFloat3;
/*  236 */       this.ctrly = paramFloat4;
/*  237 */       this.x2 = paramFloat5;
/*  238 */       this.y2 = paramFloat6;
/*      */     }
/*      */ 
/*      */     public Rectangle2D getBounds2D()
/*      */     {
/*  246 */       float f1 = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
/*  247 */       float f2 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
/*  248 */       float f3 = Math.max(Math.max(this.x1, this.x2), this.ctrlx);
/*  249 */       float f4 = Math.max(Math.max(this.y1, this.y2), this.ctrly);
/*  250 */       return new Rectangle2D.Float(f1, f2, f3 - f1, f4 - f2);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.QuadCurve2D
 * JD-Core Version:    0.6.2
 */