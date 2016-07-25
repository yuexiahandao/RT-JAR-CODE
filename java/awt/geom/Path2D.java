/*      */ package java.awt.geom;
/*      */ 
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.StreamCorruptedException;
/*      */ import java.util.Arrays;
/*      */ import sun.awt.geom.Curve;
/*      */ 
/*      */ public abstract class Path2D
/*      */   implements Shape, Cloneable
/*      */ {
/*      */   public static final int WIND_EVEN_ODD = 0;
/*      */   public static final int WIND_NON_ZERO = 1;
/*      */   private static final byte SEG_MOVETO = 0;
/*      */   private static final byte SEG_LINETO = 1;
/*      */   private static final byte SEG_QUADTO = 2;
/*      */   private static final byte SEG_CUBICTO = 3;
/*      */   private static final byte SEG_CLOSE = 4;
/*      */   transient byte[] pointTypes;
/*      */   transient int numTypes;
/*      */   transient int numCoords;
/*      */   transient int windingRule;
/*      */   static final int INIT_SIZE = 20;
/*      */   static final int EXPAND_MAX = 500;
/*      */   private static final byte SERIAL_STORAGE_FLT_ARRAY = 48;
/*      */   private static final byte SERIAL_STORAGE_DBL_ARRAY = 49;
/*      */   private static final byte SERIAL_SEG_FLT_MOVETO = 64;
/*      */   private static final byte SERIAL_SEG_FLT_LINETO = 65;
/*      */   private static final byte SERIAL_SEG_FLT_QUADTO = 66;
/*      */   private static final byte SERIAL_SEG_FLT_CUBICTO = 67;
/*      */   private static final byte SERIAL_SEG_DBL_MOVETO = 80;
/*      */   private static final byte SERIAL_SEG_DBL_LINETO = 81;
/*      */   private static final byte SERIAL_SEG_DBL_QUADTO = 82;
/*      */   private static final byte SERIAL_SEG_DBL_CUBICTO = 83;
/*      */   private static final byte SERIAL_SEG_CLOSE = 96;
/*      */   private static final byte SERIAL_PATH_END = 97;
/*      */ 
/*      */   Path2D()
/*      */   {
/*      */   }
/*      */ 
/*      */   Path2D(int paramInt1, int paramInt2)
/*      */   {
/*  130 */     setWindingRule(paramInt1);
/*  131 */     this.pointTypes = new byte[paramInt2];
/*      */   }
/*      */ 
/*      */   abstract float[] cloneCoordsFloat(AffineTransform paramAffineTransform);
/*      */ 
/*      */   abstract double[] cloneCoordsDouble(AffineTransform paramAffineTransform);
/*      */ 
/*      */   abstract void append(float paramFloat1, float paramFloat2);
/*      */ 
/*      */   abstract void append(double paramDouble1, double paramDouble2);
/*      */ 
/*      */   abstract Point2D getPoint(int paramInt);
/*      */ 
/*      */   abstract void needRoom(boolean paramBoolean, int paramInt);
/*      */ 
/*      */   abstract int pointCrossings(double paramDouble1, double paramDouble2);
/*      */ 
/*      */   abstract int rectCrossings(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*      */ 
/*      */   public abstract void moveTo(double paramDouble1, double paramDouble2);
/*      */ 
/*      */   public abstract void lineTo(double paramDouble1, double paramDouble2);
/*      */ 
/*      */   public abstract void quadTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*      */ 
/*      */   public abstract void curveTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*      */ 
/*      */   public final synchronized void closePath()
/*      */   {
/* 1768 */     if ((this.numTypes == 0) || (this.pointTypes[(this.numTypes - 1)] != 4)) {
/* 1769 */       needRoom(true, 0);
/* 1770 */       this.pointTypes[(this.numTypes++)] = 4;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void append(Shape paramShape, boolean paramBoolean)
/*      */   {
/* 1797 */     append(paramShape.getPathIterator(null), paramBoolean);
/*      */   }
/*      */ 
/*      */   public abstract void append(PathIterator paramPathIterator, boolean paramBoolean);
/*      */ 
/*      */   public final synchronized int getWindingRule()
/*      */   {
/* 1835 */     return this.windingRule;
/*      */   }
/*      */ 
/*      */   public final void setWindingRule(int paramInt)
/*      */   {
/* 1851 */     if ((paramInt != 0) && (paramInt != 1)) {
/* 1852 */       throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
/*      */     }
/*      */ 
/* 1856 */     this.windingRule = paramInt;
/*      */   }
/*      */ 
/*      */   public final synchronized Point2D getCurrentPoint()
/*      */   {
/* 1868 */     int i = this.numCoords;
/* 1869 */     if ((this.numTypes < 1) || (i < 1)) {
/* 1870 */       return null;
/*      */     }
/* 1872 */     if (this.pointTypes[(this.numTypes - 1)] == 4)
/*      */     {
/* 1874 */       for (int j = this.numTypes - 2; j > 0; j--) {
/* 1875 */         switch (this.pointTypes[j]) {
/*      */         case 0:
/* 1877 */           break;
/*      */         case 1:
/* 1879 */           i -= 2;
/* 1880 */           break;
/*      */         case 2:
/* 1882 */           i -= 4;
/* 1883 */           break;
/*      */         case 3:
/* 1885 */           i -= 6;
/*      */         case 4:
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1892 */     return getPoint(i - 2);
/*      */   }
/*      */ 
/*      */   public final synchronized void reset()
/*      */   {
/* 1903 */     this.numTypes = (this.numCoords = 0);
/*      */   }
/*      */ 
/*      */   public abstract void transform(AffineTransform paramAffineTransform);
/*      */ 
/*      */   public final synchronized Shape createTransformedShape(AffineTransform paramAffineTransform)
/*      */   {
/* 1939 */     Path2D localPath2D = (Path2D)clone();
/* 1940 */     if (paramAffineTransform != null) {
/* 1941 */       localPath2D.transform(paramAffineTransform);
/*      */     }
/* 1943 */     return localPath2D;
/*      */   }
/*      */ 
/*      */   public final Rectangle getBounds()
/*      */   {
/* 1951 */     return getBounds2D().getBounds();
/*      */   }
/*      */ 
/*      */   public static boolean contains(PathIterator paramPathIterator, double paramDouble1, double paramDouble2)
/*      */   {
/* 1970 */     if (paramDouble1 * 0.0D + paramDouble2 * 0.0D == 0.0D)
/*      */     {
/* 1974 */       int i = paramPathIterator.getWindingRule() == 1 ? -1 : 1;
/* 1975 */       int j = Curve.pointCrossingsForPath(paramPathIterator, paramDouble1, paramDouble2);
/* 1976 */       return (j & i) != 0;
/*      */     }
/*      */ 
/* 1983 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean contains(PathIterator paramPathIterator, Point2D paramPoint2D)
/*      */   {
/* 2002 */     return contains(paramPathIterator, paramPoint2D.getX(), paramPoint2D.getY());
/*      */   }
/*      */ 
/*      */   public final boolean contains(double paramDouble1, double paramDouble2)
/*      */   {
/* 2010 */     if (paramDouble1 * 0.0D + paramDouble2 * 0.0D == 0.0D)
/*      */     {
/* 2014 */       if (this.numTypes < 2) {
/* 2015 */         return false;
/*      */       }
/* 2017 */       int i = this.windingRule == 1 ? -1 : 1;
/* 2018 */       return (pointCrossings(paramDouble1, paramDouble2) & i) != 0;
/*      */     }
/*      */ 
/* 2025 */     return false;
/*      */   }
/*      */ 
/*      */   public final boolean contains(Point2D paramPoint2D)
/*      */   {
/* 2034 */     return contains(paramPoint2D.getX(), paramPoint2D.getY());
/*      */   }
/*      */ 
/*      */   public static boolean contains(PathIterator paramPathIterator, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 2071 */     if ((Double.isNaN(paramDouble1 + paramDouble3)) || (Double.isNaN(paramDouble2 + paramDouble4)))
/*      */     {
/* 2080 */       return false;
/*      */     }
/* 2082 */     if ((paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 2083 */       return false;
/*      */     }
/* 2085 */     int i = paramPathIterator.getWindingRule() == 1 ? -1 : 2;
/* 2086 */     int j = Curve.rectCrossingsForPath(paramPathIterator, paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4);
/* 2087 */     return (j != -2147483648) && ((j & i) != 0);
/*      */   }
/*      */ 
/*      */   public static boolean contains(PathIterator paramPathIterator, Rectangle2D paramRectangle2D)
/*      */   {
/* 2120 */     return contains(paramPathIterator, paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*      */   }
/*      */ 
/*      */   public final boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 2143 */     if ((Double.isNaN(paramDouble1 + paramDouble3)) || (Double.isNaN(paramDouble2 + paramDouble4)))
/*      */     {
/* 2152 */       return false;
/*      */     }
/* 2154 */     if ((paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 2155 */       return false;
/*      */     }
/* 2157 */     int i = this.windingRule == 1 ? -1 : 2;
/* 2158 */     int j = rectCrossings(paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4);
/* 2159 */     return (j != -2147483648) && ((j & i) != 0);
/*      */   }
/*      */ 
/*      */   public final boolean contains(Rectangle2D paramRectangle2D)
/*      */   {
/* 2183 */     return contains(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*      */   }
/*      */ 
/*      */   public static boolean intersects(PathIterator paramPathIterator, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 2221 */     if ((Double.isNaN(paramDouble1 + paramDouble3)) || (Double.isNaN(paramDouble2 + paramDouble4)))
/*      */     {
/* 2230 */       return false;
/*      */     }
/* 2232 */     if ((paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 2233 */       return false;
/*      */     }
/* 2235 */     int i = paramPathIterator.getWindingRule() == 1 ? -1 : 2;
/* 2236 */     int j = Curve.rectCrossingsForPath(paramPathIterator, paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4);
/* 2237 */     return (j == -2147483648) || ((j & i) != 0);
/*      */   }
/*      */ 
/*      */   public static boolean intersects(PathIterator paramPathIterator, Rectangle2D paramRectangle2D)
/*      */   {
/* 2270 */     return intersects(paramPathIterator, paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*      */   }
/*      */ 
/*      */   public final boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 2292 */     if ((Double.isNaN(paramDouble1 + paramDouble3)) || (Double.isNaN(paramDouble2 + paramDouble4)))
/*      */     {
/* 2301 */       return false;
/*      */     }
/* 2303 */     if ((paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 2304 */       return false;
/*      */     }
/* 2306 */     int i = this.windingRule == 1 ? -1 : 2;
/* 2307 */     int j = rectCrossings(paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4);
/* 2308 */     return (j == -2147483648) || ((j & i) != 0);
/*      */   }
/*      */ 
/*      */   public final boolean intersects(Rectangle2D paramRectangle2D)
/*      */   {
/* 2331 */     return intersects(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*      */   }
/*      */ 
/*      */   public final PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble)
/*      */   {
/* 2348 */     return new FlatteningPathIterator(getPathIterator(paramAffineTransform), paramDouble);
/*      */   }
/*      */ 
/*      */   public abstract Object clone();
/*      */ 
/*      */   final void writeObject(ObjectOutputStream paramObjectOutputStream, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 2388 */     paramObjectOutputStream.defaultWriteObject();
/*      */     double[] arrayOfDouble;
/*      */     float[] arrayOfFloat;
/* 2393 */     if (paramBoolean) {
/* 2394 */       arrayOfDouble = ((Double)this).doubleCoords;
/* 2395 */       arrayOfFloat = null;
/*      */     } else {
/* 2397 */       arrayOfFloat = ((Float)this).floatCoords;
/* 2398 */       arrayOfDouble = null;
/*      */     }
/*      */ 
/* 2401 */     int i = this.numTypes;
/*      */ 
/* 2403 */     paramObjectOutputStream.writeByte(paramBoolean ? 49 : 48);
/*      */ 
/* 2406 */     paramObjectOutputStream.writeInt(i);
/* 2407 */     paramObjectOutputStream.writeInt(this.numCoords);
/* 2408 */     paramObjectOutputStream.writeByte((byte)this.windingRule);
/*      */ 
/* 2410 */     int j = 0;
/* 2411 */     for (int k = 0; k < i; k++)
/*      */     {
/*      */       int m;
/*      */       int n;
/* 2414 */       switch (this.pointTypes[k]) {
/*      */       case 0:
/* 2416 */         m = 1;
/* 2417 */         n = paramBoolean ? 80 : 64;
/*      */ 
/* 2420 */         break;
/*      */       case 1:
/* 2422 */         m = 1;
/* 2423 */         n = paramBoolean ? 81 : 65;
/*      */ 
/* 2426 */         break;
/*      */       case 2:
/* 2428 */         m = 2;
/* 2429 */         n = paramBoolean ? 82 : 66;
/*      */ 
/* 2432 */         break;
/*      */       case 3:
/* 2434 */         m = 3;
/* 2435 */         n = paramBoolean ? 83 : 67;
/*      */ 
/* 2438 */         break;
/*      */       case 4:
/* 2440 */         m = 0;
/* 2441 */         n = 96;
/* 2442 */         break;
/*      */       default:
/* 2446 */         throw new InternalError("unrecognized path type");
/*      */       }
/* 2448 */       paramObjectOutputStream.writeByte(n);
/*      */       while (true) { m--; if (m < 0) break;
/* 2450 */         if (paramBoolean) {
/* 2451 */           paramObjectOutputStream.writeDouble(arrayOfDouble[(j++)]);
/* 2452 */           paramObjectOutputStream.writeDouble(arrayOfDouble[(j++)]);
/*      */         } else {
/* 2454 */           paramObjectOutputStream.writeFloat(arrayOfFloat[(j++)]);
/* 2455 */           paramObjectOutputStream.writeFloat(arrayOfFloat[(j++)]);
/*      */         }
/*      */       }
/*      */     }
/* 2459 */     paramObjectOutputStream.writeByte(97);
/*      */   }
/*      */ 
/*      */   final void readObject(ObjectInputStream paramObjectInputStream, boolean paramBoolean)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 2465 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 2470 */     paramObjectInputStream.readByte();
/* 2471 */     int i = paramObjectInputStream.readInt();
/* 2472 */     int j = paramObjectInputStream.readInt();
/*      */     try {
/* 2474 */       setWindingRule(paramObjectInputStream.readByte());
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 2476 */       throw new InvalidObjectException(localIllegalArgumentException.getMessage());
/*      */     }
/*      */ 
/* 2479 */     this.pointTypes = new byte[i < 0 ? 20 : i];
/* 2480 */     if (j < 0) {
/* 2481 */       j = 40;
/*      */     }
/* 2483 */     if (paramBoolean)
/* 2484 */       ((Double)this).doubleCoords = new double[j];
/*      */     else {
/* 2486 */       ((Float)this).floatCoords = new float[j];
/*      */     }
/*      */ 
/* 2490 */     for (int k = 0; (i < 0) || (k < i); k++)
/*      */     {
/* 2495 */       int i2 = paramObjectInputStream.readByte();
/*      */       int m;
/*      */       int n;
/*      */       int i1;
/* 2496 */       switch (i2) {
/*      */       case 64:
/* 2498 */         m = 0;
/* 2499 */         n = 1;
/* 2500 */         i1 = 0;
/* 2501 */         break;
/*      */       case 65:
/* 2503 */         m = 0;
/* 2504 */         n = 1;
/* 2505 */         i1 = 1;
/* 2506 */         break;
/*      */       case 66:
/* 2508 */         m = 0;
/* 2509 */         n = 2;
/* 2510 */         i1 = 2;
/* 2511 */         break;
/*      */       case 67:
/* 2513 */         m = 0;
/* 2514 */         n = 3;
/* 2515 */         i1 = 3;
/* 2516 */         break;
/*      */       case 80:
/* 2519 */         m = 1;
/* 2520 */         n = 1;
/* 2521 */         i1 = 0;
/* 2522 */         break;
/*      */       case 81:
/* 2524 */         m = 1;
/* 2525 */         n = 1;
/* 2526 */         i1 = 1;
/* 2527 */         break;
/*      */       case 82:
/* 2529 */         m = 1;
/* 2530 */         n = 2;
/* 2531 */         i1 = 2;
/* 2532 */         break;
/*      */       case 83:
/* 2534 */         m = 1;
/* 2535 */         n = 3;
/* 2536 */         i1 = 3;
/* 2537 */         break;
/*      */       case 96:
/* 2540 */         m = 0;
/* 2541 */         n = 0;
/* 2542 */         i1 = 4;
/* 2543 */         break;
/*      */       case 97:
/* 2546 */         if (i < 0) {
/*      */           break label500;
/*      */         }
/* 2549 */         throw new StreamCorruptedException("unexpected PATH_END");
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*      */       case 87:
/*      */       case 88:
/*      */       case 89:
/*      */       case 90:
/*      */       case 91:
/*      */       case 92:
/*      */       case 93:
/*      */       case 94:
/*      */       case 95:
/*      */       default:
/* 2552 */         throw new StreamCorruptedException("unrecognized path type");
/*      */       }
/* 2554 */       needRoom(i1 != 0, n * 2);
/* 2555 */       if (m != 0) while (true) {
/* 2556 */           n--; if (n < 0) break;
/* 2557 */           append(paramObjectInputStream.readDouble(), paramObjectInputStream.readDouble());
/*      */         } while (true)
/*      */       {
/* 2560 */         n--; if (n < 0) break;
/* 2561 */         append(paramObjectInputStream.readFloat(), paramObjectInputStream.readFloat());
/*      */       }
/*      */ 
/* 2564 */       this.pointTypes[(this.numTypes++)] = i1;
/*      */     }
/* 2566 */     label500: if ((i >= 0) && (paramObjectInputStream.readByte() != 97))
/* 2567 */       throw new StreamCorruptedException("missing PATH_END");
/*      */   }
/*      */ 
/*      */   public static class Double extends Path2D
/*      */     implements Serializable
/*      */   {
/*      */     transient double[] doubleCoords;
/*      */     private static final long serialVersionUID = 1826762518450014216L;
/*      */ 
/*      */     public Double()
/*      */     {
/*  994 */       this(1, 20);
/*      */     }
/*      */ 
/*      */     public Double(int paramInt)
/*      */     {
/* 1008 */       this(paramInt, 20);
/*      */     }
/*      */ 
/*      */     public Double(int paramInt1, int paramInt2)
/*      */     {
/* 1027 */       super(paramInt2);
/* 1028 */       this.doubleCoords = new double[paramInt2 * 2];
/*      */     }
/*      */ 
/*      */     public Double(Shape paramShape)
/*      */     {
/* 1041 */       this(paramShape, null);
/*      */     }
/*      */ 
/*      */     public Double(Shape paramShape, AffineTransform paramAffineTransform)
/*      */     {
/*      */       Object localObject;
/* 1057 */       if ((paramShape instanceof Path2D)) {
/* 1058 */         localObject = (Path2D)paramShape;
/* 1059 */         setWindingRule(((Path2D)localObject).windingRule);
/* 1060 */         this.numTypes = ((Path2D)localObject).numTypes;
/* 1061 */         this.pointTypes = Arrays.copyOf(((Path2D)localObject).pointTypes, ((Path2D)localObject).pointTypes.length);
/*      */ 
/* 1063 */         this.numCoords = ((Path2D)localObject).numCoords;
/* 1064 */         this.doubleCoords = ((Path2D)localObject).cloneCoordsDouble(paramAffineTransform);
/*      */       } else {
/* 1066 */         localObject = paramShape.getPathIterator(paramAffineTransform);
/* 1067 */         setWindingRule(((PathIterator)localObject).getWindingRule());
/* 1068 */         this.pointTypes = new byte[20];
/* 1069 */         this.doubleCoords = new double[40];
/* 1070 */         append((PathIterator)localObject, false);
/*      */       }
/*      */     }
/*      */ 
/*      */     float[] cloneCoordsFloat(AffineTransform paramAffineTransform) {
/* 1075 */       float[] arrayOfFloat = new float[this.doubleCoords.length];
/* 1076 */       if (paramAffineTransform == null) {
/* 1077 */         for (int i = 0; i < this.numCoords; i++)
/* 1078 */           arrayOfFloat[i] = ((float)this.doubleCoords[i]);
/*      */       }
/*      */       else {
/* 1081 */         paramAffineTransform.transform(this.doubleCoords, 0, arrayOfFloat, 0, this.numCoords / 2);
/*      */       }
/* 1083 */       return arrayOfFloat;
/*      */     }
/*      */ 
/*      */     double[] cloneCoordsDouble(AffineTransform paramAffineTransform)
/*      */     {
/*      */       double[] arrayOfDouble;
/* 1088 */       if (paramAffineTransform == null) {
/* 1089 */         arrayOfDouble = Arrays.copyOf(this.doubleCoords, this.doubleCoords.length);
/*      */       }
/*      */       else {
/* 1092 */         arrayOfDouble = new double[this.doubleCoords.length];
/* 1093 */         paramAffineTransform.transform(this.doubleCoords, 0, arrayOfDouble, 0, this.numCoords / 2);
/*      */       }
/* 1095 */       return arrayOfDouble;
/*      */     }
/*      */ 
/*      */     void append(float paramFloat1, float paramFloat2) {
/* 1099 */       this.doubleCoords[(this.numCoords++)] = paramFloat1;
/* 1100 */       this.doubleCoords[(this.numCoords++)] = paramFloat2;
/*      */     }
/*      */ 
/*      */     void append(double paramDouble1, double paramDouble2) {
/* 1104 */       this.doubleCoords[(this.numCoords++)] = paramDouble1;
/* 1105 */       this.doubleCoords[(this.numCoords++)] = paramDouble2;
/*      */     }
/*      */ 
/*      */     Point2D getPoint(int paramInt) {
/* 1109 */       return new Point2D.Double(this.doubleCoords[paramInt], this.doubleCoords[(paramInt + 1)]);
/*      */     }
/*      */ 
/*      */     void needRoom(boolean paramBoolean, int paramInt)
/*      */     {
/* 1114 */       if ((paramBoolean) && (this.numTypes == 0)) {
/* 1115 */         throw new IllegalPathStateException("missing initial moveto in path definition");
/*      */       }
/*      */ 
/* 1118 */       int i = this.pointTypes.length;
/*      */       int j;
/* 1119 */       if (this.numTypes >= i) {
/* 1120 */         j = i;
/* 1121 */         if (j > 500) {
/* 1122 */           j = 500;
/*      */         }
/* 1124 */         this.pointTypes = Arrays.copyOf(this.pointTypes, i + j);
/*      */       }
/* 1126 */       i = this.doubleCoords.length;
/* 1127 */       if (this.numCoords + paramInt > i) {
/* 1128 */         j = i;
/* 1129 */         if (j > 1000) {
/* 1130 */           j = 1000;
/*      */         }
/* 1132 */         if (j < paramInt) {
/* 1133 */           j = paramInt;
/*      */         }
/* 1135 */         this.doubleCoords = Arrays.copyOf(this.doubleCoords, i + j);
/*      */       }
/*      */     }
/*      */ 
/*      */     public final synchronized void moveTo(double paramDouble1, double paramDouble2)
/*      */     {
/* 1144 */       if ((this.numTypes > 0) && (this.pointTypes[(this.numTypes - 1)] == 0)) {
/* 1145 */         this.doubleCoords[(this.numCoords - 2)] = paramDouble1;
/* 1146 */         this.doubleCoords[(this.numCoords - 1)] = paramDouble2;
/*      */       } else {
/* 1148 */         needRoom(false, 2);
/* 1149 */         this.pointTypes[(this.numTypes++)] = 0;
/* 1150 */         this.doubleCoords[(this.numCoords++)] = paramDouble1;
/* 1151 */         this.doubleCoords[(this.numCoords++)] = paramDouble2;
/*      */       }
/*      */     }
/*      */ 
/*      */     public final synchronized void lineTo(double paramDouble1, double paramDouble2)
/*      */     {
/* 1160 */       needRoom(true, 2);
/* 1161 */       this.pointTypes[(this.numTypes++)] = 1;
/* 1162 */       this.doubleCoords[(this.numCoords++)] = paramDouble1;
/* 1163 */       this.doubleCoords[(this.numCoords++)] = paramDouble2;
/*      */     }
/*      */ 
/*      */     public final synchronized void quadTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */     {
/* 1173 */       needRoom(true, 4);
/* 1174 */       this.pointTypes[(this.numTypes++)] = 2;
/* 1175 */       this.doubleCoords[(this.numCoords++)] = paramDouble1;
/* 1176 */       this.doubleCoords[(this.numCoords++)] = paramDouble2;
/* 1177 */       this.doubleCoords[(this.numCoords++)] = paramDouble3;
/* 1178 */       this.doubleCoords[(this.numCoords++)] = paramDouble4;
/*      */     }
/*      */ 
/*      */     public final synchronized void curveTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */     {
/* 1189 */       needRoom(true, 6);
/* 1190 */       this.pointTypes[(this.numTypes++)] = 3;
/* 1191 */       this.doubleCoords[(this.numCoords++)] = paramDouble1;
/* 1192 */       this.doubleCoords[(this.numCoords++)] = paramDouble2;
/* 1193 */       this.doubleCoords[(this.numCoords++)] = paramDouble3;
/* 1194 */       this.doubleCoords[(this.numCoords++)] = paramDouble4;
/* 1195 */       this.doubleCoords[(this.numCoords++)] = paramDouble5;
/* 1196 */       this.doubleCoords[(this.numCoords++)] = paramDouble6;
/*      */     }
/*      */ 
/*      */     int pointCrossings(double paramDouble1, double paramDouble2)
/*      */     {
/* 1201 */       double[] arrayOfDouble = this.doubleCoords;
/*      */       double d1;
/* 1202 */       double d3 = d1 = arrayOfDouble[0];
/*      */       double d2;
/* 1203 */       double d4 = d2 = arrayOfDouble[1];
/* 1204 */       int i = 0;
/* 1205 */       int j = 2;
/* 1206 */       for (int k = 1; k < this.numTypes; k++)
/*      */       {
/*      */         double d5;
/*      */         double d6;
/* 1207 */         switch (this.pointTypes[k]) {
/*      */         case 0:
/* 1209 */           if (d4 != d2) {
/* 1210 */             i += Curve.pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d1, d2);
/*      */           }
/*      */ 
/* 1215 */           d1 = d3 = arrayOfDouble[(j++)];
/* 1216 */           d2 = d4 = arrayOfDouble[(j++)];
/* 1217 */           break;
/*      */         case 1:
/* 1219 */           i += Curve.pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d5 = arrayOfDouble[(j++)], d6 = arrayOfDouble[(j++)]);
/*      */ 
/* 1224 */           d3 = d5;
/* 1225 */           d4 = d6;
/* 1226 */           break;
/*      */         case 2:
/* 1228 */           i += Curve.pointCrossingsForQuad(paramDouble1, paramDouble2, d3, d4, arrayOfDouble[(j++)], arrayOfDouble[(j++)], d5 = arrayOfDouble[(j++)], d6 = arrayOfDouble[(j++)], 0);
/*      */ 
/* 1236 */           d3 = d5;
/* 1237 */           d4 = d6;
/* 1238 */           break;
/*      */         case 3:
/* 1240 */           i += Curve.pointCrossingsForCubic(paramDouble1, paramDouble2, d3, d4, arrayOfDouble[(j++)], arrayOfDouble[(j++)], arrayOfDouble[(j++)], arrayOfDouble[(j++)], d5 = arrayOfDouble[(j++)], d6 = arrayOfDouble[(j++)], 0);
/*      */ 
/* 1250 */           d3 = d5;
/* 1251 */           d4 = d6;
/* 1252 */           break;
/*      */         case 4:
/* 1254 */           if (d4 != d2) {
/* 1255 */             i += Curve.pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d1, d2);
/*      */           }
/*      */ 
/* 1260 */           d3 = d1;
/* 1261 */           d4 = d2;
/*      */         }
/*      */       }
/*      */ 
/* 1265 */       if (d4 != d2) {
/* 1266 */         i += Curve.pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d1, d2);
/*      */       }
/*      */ 
/* 1271 */       return i;
/*      */     }
/*      */ 
/*      */     int rectCrossings(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */     {
/* 1277 */       double[] arrayOfDouble = this.doubleCoords;
/*      */       double d3;
/* 1279 */       double d1 = d3 = arrayOfDouble[0];
/*      */       double d4;
/* 1280 */       double d2 = d4 = arrayOfDouble[1];
/* 1281 */       int i = 0;
/* 1282 */       int j = 2;
/* 1283 */       for (int k = 1; 
/* 1284 */         (i != -2147483648) && (k < this.numTypes); 
/* 1285 */         k++)
/*      */       {
/*      */         double d5;
/*      */         double d6;
/* 1287 */         switch (this.pointTypes[k]) {
/*      */         case 0:
/* 1289 */           if ((d1 != d3) || (d2 != d4)) {
/* 1290 */             i = Curve.rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4);
/*      */           }
/*      */ 
/* 1299 */           d3 = d1 = arrayOfDouble[(j++)];
/* 1300 */           d4 = d2 = arrayOfDouble[(j++)];
/* 1301 */           break;
/*      */         case 1:
/* 1303 */           d5 = arrayOfDouble[(j++)];
/* 1304 */           d6 = arrayOfDouble[(j++)];
/* 1305 */           i = Curve.rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d5, d6);
/*      */ 
/* 1311 */           d1 = d5;
/* 1312 */           d2 = d6;
/* 1313 */           break;
/*      */         case 2:
/* 1315 */           i = Curve.rectCrossingsForQuad(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, arrayOfDouble[(j++)], arrayOfDouble[(j++)], d5 = arrayOfDouble[(j++)], d6 = arrayOfDouble[(j++)], 0);
/*      */ 
/* 1325 */           d1 = d5;
/* 1326 */           d2 = d6;
/* 1327 */           break;
/*      */         case 3:
/* 1329 */           i = Curve.rectCrossingsForCubic(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, arrayOfDouble[(j++)], arrayOfDouble[(j++)], arrayOfDouble[(j++)], arrayOfDouble[(j++)], d5 = arrayOfDouble[(j++)], d6 = arrayOfDouble[(j++)], 0);
/*      */ 
/* 1341 */           d1 = d5;
/* 1342 */           d2 = d6;
/* 1343 */           break;
/*      */         case 4:
/* 1345 */           if ((d1 != d3) || (d2 != d4)) {
/* 1346 */             i = Curve.rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4);
/*      */           }
/*      */ 
/* 1353 */           d1 = d3;
/* 1354 */           d2 = d4;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1360 */       if ((i != -2147483648) && ((d1 != d3) || (d2 != d4)))
/*      */       {
/* 1363 */         i = Curve.rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4);
/*      */       }
/*      */ 
/* 1372 */       return i;
/*      */     }
/*      */ 
/*      */     public final void append(PathIterator paramPathIterator, boolean paramBoolean)
/*      */     {
/* 1380 */       double[] arrayOfDouble = new double[6];
/* 1381 */       while (!paramPathIterator.isDone()) {
/* 1382 */         switch (paramPathIterator.currentSegment(arrayOfDouble)) {
/*      */         case 0:
/* 1384 */           if ((!paramBoolean) || (this.numTypes < 1) || (this.numCoords < 1)) {
/* 1385 */             moveTo(arrayOfDouble[0], arrayOfDouble[1]);
/*      */           }
/*      */           else {
/* 1388 */             if ((this.pointTypes[(this.numTypes - 1)] != 4) && (this.doubleCoords[(this.numCoords - 2)] == arrayOfDouble[0]) && (this.doubleCoords[(this.numCoords - 1)] == arrayOfDouble[1]))
/*      */             {
/*      */               break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 1:
/* 1397 */           lineTo(arrayOfDouble[0], arrayOfDouble[1]);
/* 1398 */           break;
/*      */         case 2:
/* 1400 */           quadTo(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2], arrayOfDouble[3]);
/*      */ 
/* 1402 */           break;
/*      */         case 3:
/* 1404 */           curveTo(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2], arrayOfDouble[3], arrayOfDouble[4], arrayOfDouble[5]);
/*      */ 
/* 1407 */           break;
/*      */         case 4:
/* 1409 */           closePath();
/*      */         }
/*      */ 
/* 1412 */         paramPathIterator.next();
/* 1413 */         paramBoolean = false;
/*      */       }
/*      */     }
/*      */ 
/*      */     public final void transform(AffineTransform paramAffineTransform)
/*      */     {
/* 1422 */       paramAffineTransform.transform(this.doubleCoords, 0, this.doubleCoords, 0, this.numCoords / 2);
/*      */     }
/*      */ 
/*      */     public final synchronized Rectangle2D getBounds2D()
/*      */     {
/* 1431 */       int i = this.numCoords;
/*      */       double d4;
/*      */       double d2;
/*      */       double d3;
/* 1432 */       if (i > 0) {
/* 1433 */         d2 = d4 = this.doubleCoords[(--i)];
/* 1434 */         d1 = d3 = this.doubleCoords[(--i)];
/* 1435 */         while (i > 0) {
/* 1436 */           double d5 = this.doubleCoords[(--i)];
/* 1437 */           double d6 = this.doubleCoords[(--i)];
/* 1438 */           if (d6 < d1) d1 = d6;
/* 1439 */           if (d5 < d2) d2 = d5;
/* 1440 */           if (d6 > d3) d3 = d6;
/* 1441 */           if (d5 > d4) d4 = d5;
/*      */         }
/*      */       }
/* 1444 */       double d1 = d2 = d3 = d4 = 0.0D;
/*      */ 
/* 1446 */       return new Rectangle2D.Double(d1, d2, d3 - d1, d4 - d2);
/*      */     }
/*      */ 
/*      */     public final PathIterator getPathIterator(AffineTransform paramAffineTransform)
/*      */     {
/* 1465 */       if (paramAffineTransform == null) {
/* 1466 */         return new CopyIterator(this);
/*      */       }
/* 1468 */       return new TxIterator(this, paramAffineTransform);
/*      */     }
/*      */ 
/*      */     public final Object clone()
/*      */     {
/* 1486 */       return new Double(this);
/*      */     }
/*      */ 
/*      */     private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */       throws IOException
/*      */     {
/* 1618 */       super.writeObject(paramObjectOutputStream, true);
/*      */     }
/*      */ 
/*      */     private void readObject(ObjectInputStream paramObjectInputStream)
/*      */       throws ClassNotFoundException, IOException
/*      */     {
/* 1637 */       super.readObject(paramObjectInputStream, true);
/*      */     }
/*      */ 
/*      */     static class CopyIterator extends Path2D.Iterator {
/*      */       double[] doubleCoords;
/*      */ 
/*      */       CopyIterator(Path2D.Double paramDouble) {
/* 1644 */         super();
/* 1645 */         this.doubleCoords = paramDouble.doubleCoords;
/*      */       }
/*      */ 
/*      */       public int currentSegment(float[] paramArrayOfFloat) {
/* 1649 */         int i = this.path.pointTypes[this.typeIdx];
/* 1650 */         int j = curvecoords[i];
/* 1651 */         if (j > 0) {
/* 1652 */           for (int k = 0; k < j; k++) {
/* 1653 */             paramArrayOfFloat[k] = ((float)this.doubleCoords[(this.pointIdx + k)]);
/*      */           }
/*      */         }
/* 1656 */         return i;
/*      */       }
/*      */ 
/*      */       public int currentSegment(double[] paramArrayOfDouble) {
/* 1660 */         int i = this.path.pointTypes[this.typeIdx];
/* 1661 */         int j = curvecoords[i];
/* 1662 */         if (j > 0) {
/* 1663 */           System.arraycopy(this.doubleCoords, this.pointIdx, paramArrayOfDouble, 0, j);
/*      */         }
/*      */ 
/* 1666 */         return i;
/*      */       }
/*      */     }
/*      */ 
/*      */     static class TxIterator extends Path2D.Iterator {
/*      */       double[] doubleCoords;
/*      */       AffineTransform affine;
/*      */ 
/* 1675 */       TxIterator(Path2D.Double paramDouble, AffineTransform paramAffineTransform) { super();
/* 1676 */         this.doubleCoords = paramDouble.doubleCoords;
/* 1677 */         this.affine = paramAffineTransform; }
/*      */ 
/*      */       public int currentSegment(float[] paramArrayOfFloat)
/*      */       {
/* 1681 */         int i = this.path.pointTypes[this.typeIdx];
/* 1682 */         int j = curvecoords[i];
/* 1683 */         if (j > 0) {
/* 1684 */           this.affine.transform(this.doubleCoords, this.pointIdx, paramArrayOfFloat, 0, j / 2);
/*      */         }
/*      */ 
/* 1687 */         return i;
/*      */       }
/*      */ 
/*      */       public int currentSegment(double[] paramArrayOfDouble) {
/* 1691 */         int i = this.path.pointTypes[this.typeIdx];
/* 1692 */         int j = curvecoords[i];
/* 1693 */         if (j > 0) {
/* 1694 */           this.affine.transform(this.doubleCoords, this.pointIdx, paramArrayOfDouble, 0, j / 2);
/*      */         }
/*      */ 
/* 1697 */         return i;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Float extends Path2D
/*      */     implements Serializable
/*      */   {
/*      */     transient float[] floatCoords;
/*      */     private static final long serialVersionUID = 6990832515060788886L;
/*      */ 
/*      */     public Float()
/*      */     {
/*  160 */       this(1, 20);
/*      */     }
/*      */ 
/*      */     public Float(int paramInt)
/*      */     {
/*  174 */       this(paramInt, 20);
/*      */     }
/*      */ 
/*      */     public Float(int paramInt1, int paramInt2)
/*      */     {
/*  193 */       super(paramInt2);
/*  194 */       this.floatCoords = new float[paramInt2 * 2];
/*      */     }
/*      */ 
/*      */     public Float(Shape paramShape)
/*      */     {
/*  207 */       this(paramShape, null);
/*      */     }
/*      */ 
/*      */     public Float(Shape paramShape, AffineTransform paramAffineTransform)
/*      */     {
/*      */       Object localObject;
/*  223 */       if ((paramShape instanceof Path2D)) {
/*  224 */         localObject = (Path2D)paramShape;
/*  225 */         setWindingRule(((Path2D)localObject).windingRule);
/*  226 */         this.numTypes = ((Path2D)localObject).numTypes;
/*  227 */         this.pointTypes = Arrays.copyOf(((Path2D)localObject).pointTypes, ((Path2D)localObject).pointTypes.length);
/*      */ 
/*  229 */         this.numCoords = ((Path2D)localObject).numCoords;
/*  230 */         this.floatCoords = ((Path2D)localObject).cloneCoordsFloat(paramAffineTransform);
/*      */       } else {
/*  232 */         localObject = paramShape.getPathIterator(paramAffineTransform);
/*  233 */         setWindingRule(((PathIterator)localObject).getWindingRule());
/*  234 */         this.pointTypes = new byte[20];
/*  235 */         this.floatCoords = new float[40];
/*  236 */         append((PathIterator)localObject, false);
/*      */       }
/*      */     }
/*      */ 
/*      */     float[] cloneCoordsFloat(AffineTransform paramAffineTransform)
/*      */     {
/*      */       float[] arrayOfFloat;
/*  242 */       if (paramAffineTransform == null) {
/*  243 */         arrayOfFloat = Arrays.copyOf(this.floatCoords, this.floatCoords.length);
/*      */       } else {
/*  245 */         arrayOfFloat = new float[this.floatCoords.length];
/*  246 */         paramAffineTransform.transform(this.floatCoords, 0, arrayOfFloat, 0, this.numCoords / 2);
/*      */       }
/*  248 */       return arrayOfFloat;
/*      */     }
/*      */ 
/*      */     double[] cloneCoordsDouble(AffineTransform paramAffineTransform) {
/*  252 */       double[] arrayOfDouble = new double[this.floatCoords.length];
/*  253 */       if (paramAffineTransform == null) {
/*  254 */         for (int i = 0; i < this.numCoords; i++)
/*  255 */           arrayOfDouble[i] = this.floatCoords[i];
/*      */       }
/*      */       else {
/*  258 */         paramAffineTransform.transform(this.floatCoords, 0, arrayOfDouble, 0, this.numCoords / 2);
/*      */       }
/*  260 */       return arrayOfDouble;
/*      */     }
/*      */ 
/*      */     void append(float paramFloat1, float paramFloat2) {
/*  264 */       this.floatCoords[(this.numCoords++)] = paramFloat1;
/*  265 */       this.floatCoords[(this.numCoords++)] = paramFloat2;
/*      */     }
/*      */ 
/*      */     void append(double paramDouble1, double paramDouble2) {
/*  269 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble1);
/*  270 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble2);
/*      */     }
/*      */ 
/*      */     Point2D getPoint(int paramInt) {
/*  274 */       return new Point2D.Float(this.floatCoords[paramInt], this.floatCoords[(paramInt + 1)]);
/*      */     }
/*      */ 
/*      */     void needRoom(boolean paramBoolean, int paramInt)
/*      */     {
/*  279 */       if ((paramBoolean) && (this.numTypes == 0)) {
/*  280 */         throw new IllegalPathStateException("missing initial moveto in path definition");
/*      */       }
/*      */ 
/*  283 */       int i = this.pointTypes.length;
/*      */       int j;
/*  284 */       if (this.numTypes >= i) {
/*  285 */         j = i;
/*  286 */         if (j > 500) {
/*  287 */           j = 500;
/*      */         }
/*  289 */         this.pointTypes = Arrays.copyOf(this.pointTypes, i + j);
/*      */       }
/*  291 */       i = this.floatCoords.length;
/*  292 */       if (this.numCoords + paramInt > i) {
/*  293 */         j = i;
/*  294 */         if (j > 1000) {
/*  295 */           j = 1000;
/*      */         }
/*  297 */         if (j < paramInt) {
/*  298 */           j = paramInt;
/*      */         }
/*  300 */         this.floatCoords = Arrays.copyOf(this.floatCoords, i + j);
/*      */       }
/*      */     }
/*      */ 
/*      */     public final synchronized void moveTo(double paramDouble1, double paramDouble2)
/*      */     {
/*  309 */       if ((this.numTypes > 0) && (this.pointTypes[(this.numTypes - 1)] == 0)) {
/*  310 */         this.floatCoords[(this.numCoords - 2)] = ((float)paramDouble1);
/*  311 */         this.floatCoords[(this.numCoords - 1)] = ((float)paramDouble2);
/*      */       } else {
/*  313 */         needRoom(false, 2);
/*  314 */         this.pointTypes[(this.numTypes++)] = 0;
/*  315 */         this.floatCoords[(this.numCoords++)] = ((float)paramDouble1);
/*  316 */         this.floatCoords[(this.numCoords++)] = ((float)paramDouble2);
/*      */       }
/*      */     }
/*      */ 
/*      */     public final synchronized void moveTo(float paramFloat1, float paramFloat2)
/*      */     {
/*  334 */       if ((this.numTypes > 0) && (this.pointTypes[(this.numTypes - 1)] == 0)) {
/*  335 */         this.floatCoords[(this.numCoords - 2)] = paramFloat1;
/*  336 */         this.floatCoords[(this.numCoords - 1)] = paramFloat2;
/*      */       } else {
/*  338 */         needRoom(false, 2);
/*  339 */         this.pointTypes[(this.numTypes++)] = 0;
/*  340 */         this.floatCoords[(this.numCoords++)] = paramFloat1;
/*  341 */         this.floatCoords[(this.numCoords++)] = paramFloat2;
/*      */       }
/*      */     }
/*      */ 
/*      */     public final synchronized void lineTo(double paramDouble1, double paramDouble2)
/*      */     {
/*  350 */       needRoom(true, 2);
/*  351 */       this.pointTypes[(this.numTypes++)] = 1;
/*  352 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble1);
/*  353 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble2);
/*      */     }
/*      */ 
/*      */     public final synchronized void lineTo(float paramFloat1, float paramFloat2)
/*      */     {
/*  371 */       needRoom(true, 2);
/*  372 */       this.pointTypes[(this.numTypes++)] = 1;
/*  373 */       this.floatCoords[(this.numCoords++)] = paramFloat1;
/*  374 */       this.floatCoords[(this.numCoords++)] = paramFloat2;
/*      */     }
/*      */ 
/*      */     public final synchronized void quadTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */     {
/*  384 */       needRoom(true, 4);
/*  385 */       this.pointTypes[(this.numTypes++)] = 2;
/*  386 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble1);
/*  387 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble2);
/*  388 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble3);
/*  389 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble4);
/*      */     }
/*      */ 
/*      */     public final synchronized void quadTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*      */     {
/*  414 */       needRoom(true, 4);
/*  415 */       this.pointTypes[(this.numTypes++)] = 2;
/*  416 */       this.floatCoords[(this.numCoords++)] = paramFloat1;
/*  417 */       this.floatCoords[(this.numCoords++)] = paramFloat2;
/*  418 */       this.floatCoords[(this.numCoords++)] = paramFloat3;
/*  419 */       this.floatCoords[(this.numCoords++)] = paramFloat4;
/*      */     }
/*      */ 
/*      */     public final synchronized void curveTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */     {
/*  430 */       needRoom(true, 6);
/*  431 */       this.pointTypes[(this.numTypes++)] = 3;
/*  432 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble1);
/*  433 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble2);
/*  434 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble3);
/*  435 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble4);
/*  436 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble5);
/*  437 */       this.floatCoords[(this.numCoords++)] = ((float)paramDouble6);
/*      */     }
/*      */ 
/*      */     public final synchronized void curveTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
/*      */     {
/*  465 */       needRoom(true, 6);
/*  466 */       this.pointTypes[(this.numTypes++)] = 3;
/*  467 */       this.floatCoords[(this.numCoords++)] = paramFloat1;
/*  468 */       this.floatCoords[(this.numCoords++)] = paramFloat2;
/*  469 */       this.floatCoords[(this.numCoords++)] = paramFloat3;
/*  470 */       this.floatCoords[(this.numCoords++)] = paramFloat4;
/*  471 */       this.floatCoords[(this.numCoords++)] = paramFloat5;
/*  472 */       this.floatCoords[(this.numCoords++)] = paramFloat6;
/*      */     }
/*      */ 
/*      */     int pointCrossings(double paramDouble1, double paramDouble2)
/*      */     {
/*  477 */       float[] arrayOfFloat = this.floatCoords;
/*      */       double d1;
/*  478 */       double d3 = d1 = arrayOfFloat[0];
/*      */       double d2;
/*  479 */       double d4 = d2 = arrayOfFloat[1];
/*  480 */       int i = 0;
/*  481 */       int j = 2;
/*  482 */       for (int k = 1; k < this.numTypes; k++)
/*      */       {
/*      */         double d5;
/*      */         double d6;
/*  483 */         switch (this.pointTypes[k]) {
/*      */         case 0:
/*  485 */           if (d4 != d2) {
/*  486 */             i += Curve.pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d1, d2);
/*      */           }
/*      */ 
/*  491 */           d1 = d3 = arrayOfFloat[(j++)];
/*  492 */           d2 = d4 = arrayOfFloat[(j++)];
/*  493 */           break;
/*      */         case 1:
/*  495 */           i += Curve.pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d5 = arrayOfFloat[(j++)], d6 = arrayOfFloat[(j++)]);
/*      */ 
/*  500 */           d3 = d5;
/*  501 */           d4 = d6;
/*  502 */           break;
/*      */         case 2:
/*  504 */           i += Curve.pointCrossingsForQuad(paramDouble1, paramDouble2, d3, d4, arrayOfFloat[(j++)], arrayOfFloat[(j++)], d5 = arrayOfFloat[(j++)], d6 = arrayOfFloat[(j++)], 0);
/*      */ 
/*  512 */           d3 = d5;
/*  513 */           d4 = d6;
/*  514 */           break;
/*      */         case 3:
/*  516 */           i += Curve.pointCrossingsForCubic(paramDouble1, paramDouble2, d3, d4, arrayOfFloat[(j++)], arrayOfFloat[(j++)], arrayOfFloat[(j++)], arrayOfFloat[(j++)], d5 = arrayOfFloat[(j++)], d6 = arrayOfFloat[(j++)], 0);
/*      */ 
/*  526 */           d3 = d5;
/*  527 */           d4 = d6;
/*  528 */           break;
/*      */         case 4:
/*  530 */           if (d4 != d2) {
/*  531 */             i += Curve.pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d1, d2);
/*      */           }
/*      */ 
/*  536 */           d3 = d1;
/*  537 */           d4 = d2;
/*      */         }
/*      */       }
/*      */ 
/*  541 */       if (d4 != d2) {
/*  542 */         i += Curve.pointCrossingsForLine(paramDouble1, paramDouble2, d3, d4, d1, d2);
/*      */       }
/*      */ 
/*  547 */       return i;
/*      */     }
/*      */ 
/*      */     int rectCrossings(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */     {
/*  553 */       float[] arrayOfFloat = this.floatCoords;
/*      */       double d3;
/*  555 */       double d1 = d3 = arrayOfFloat[0];
/*      */       double d4;
/*  556 */       double d2 = d4 = arrayOfFloat[1];
/*  557 */       int i = 0;
/*  558 */       int j = 2;
/*  559 */       for (int k = 1; 
/*  560 */         (i != -2147483648) && (k < this.numTypes); 
/*  561 */         k++)
/*      */       {
/*      */         double d5;
/*      */         double d6;
/*  563 */         switch (this.pointTypes[k]) {
/*      */         case 0:
/*  565 */           if ((d1 != d3) || (d2 != d4)) {
/*  566 */             i = Curve.rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4);
/*      */           }
/*      */ 
/*  575 */           d3 = d1 = arrayOfFloat[(j++)];
/*  576 */           d4 = d2 = arrayOfFloat[(j++)];
/*  577 */           break;
/*      */         case 1:
/*  579 */           i = Curve.rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d5 = arrayOfFloat[(j++)], d6 = arrayOfFloat[(j++)]);
/*      */ 
/*  586 */           d1 = d5;
/*  587 */           d2 = d6;
/*  588 */           break;
/*      */         case 2:
/*  590 */           i = Curve.rectCrossingsForQuad(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, arrayOfFloat[(j++)], arrayOfFloat[(j++)], d5 = arrayOfFloat[(j++)], d6 = arrayOfFloat[(j++)], 0);
/*      */ 
/*  600 */           d1 = d5;
/*  601 */           d2 = d6;
/*  602 */           break;
/*      */         case 3:
/*  604 */           i = Curve.rectCrossingsForCubic(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, arrayOfFloat[(j++)], arrayOfFloat[(j++)], arrayOfFloat[(j++)], arrayOfFloat[(j++)], d5 = arrayOfFloat[(j++)], d6 = arrayOfFloat[(j++)], 0);
/*      */ 
/*  616 */           d1 = d5;
/*  617 */           d2 = d6;
/*  618 */           break;
/*      */         case 4:
/*  620 */           if ((d1 != d3) || (d2 != d4)) {
/*  621 */             i = Curve.rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4);
/*      */           }
/*      */ 
/*  628 */           d1 = d3;
/*  629 */           d2 = d4;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  635 */       if ((i != -2147483648) && ((d1 != d3) || (d2 != d4)))
/*      */       {
/*  638 */         i = Curve.rectCrossingsForLine(i, paramDouble1, paramDouble2, paramDouble3, paramDouble4, d1, d2, d3, d4);
/*      */       }
/*      */ 
/*  647 */       return i;
/*      */     }
/*      */ 
/*      */     public final void append(PathIterator paramPathIterator, boolean paramBoolean)
/*      */     {
/*  655 */       float[] arrayOfFloat = new float[6];
/*  656 */       while (!paramPathIterator.isDone()) {
/*  657 */         switch (paramPathIterator.currentSegment(arrayOfFloat)) {
/*      */         case 0:
/*  659 */           if ((!paramBoolean) || (this.numTypes < 1) || (this.numCoords < 1)) {
/*  660 */             moveTo(arrayOfFloat[0], arrayOfFloat[1]);
/*      */           }
/*      */           else {
/*  663 */             if ((this.pointTypes[(this.numTypes - 1)] != 4) && (this.floatCoords[(this.numCoords - 2)] == arrayOfFloat[0]) && (this.floatCoords[(this.numCoords - 1)] == arrayOfFloat[1]))
/*      */             {
/*      */               break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 1:
/*  672 */           lineTo(arrayOfFloat[0], arrayOfFloat[1]);
/*  673 */           break;
/*      */         case 2:
/*  675 */           quadTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
/*      */ 
/*  677 */           break;
/*      */         case 3:
/*  679 */           curveTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
/*      */ 
/*  682 */           break;
/*      */         case 4:
/*  684 */           closePath();
/*      */         }
/*      */ 
/*  687 */         paramPathIterator.next();
/*  688 */         paramBoolean = false;
/*      */       }
/*      */     }
/*      */ 
/*      */     public final void transform(AffineTransform paramAffineTransform)
/*      */     {
/*  697 */       paramAffineTransform.transform(this.floatCoords, 0, this.floatCoords, 0, this.numCoords / 2);
/*      */     }
/*      */ 
/*      */     public final synchronized Rectangle2D getBounds2D()
/*      */     {
/*  706 */       int i = this.numCoords;
/*      */       float f4;
/*      */       float f2;
/*      */       float f3;
/*  707 */       if (i > 0) {
/*  708 */         f2 = f4 = this.floatCoords[(--i)];
/*  709 */         f1 = f3 = this.floatCoords[(--i)];
/*  710 */         while (i > 0) {
/*  711 */           float f5 = this.floatCoords[(--i)];
/*  712 */           float f6 = this.floatCoords[(--i)];
/*  713 */           if (f6 < f1) f1 = f6;
/*  714 */           if (f5 < f2) f2 = f5;
/*  715 */           if (f6 > f3) f3 = f6;
/*  716 */           if (f5 > f4) f4 = f5;
/*      */         }
/*      */       }
/*  719 */       float f1 = f2 = f3 = f4 = 0.0F;
/*      */ 
/*  721 */       return new Rectangle2D.Float(f1, f2, f3 - f1, f4 - f2);
/*      */     }
/*      */ 
/*      */     public final PathIterator getPathIterator(AffineTransform paramAffineTransform)
/*      */     {
/*  736 */       if (paramAffineTransform == null) {
/*  737 */         return new CopyIterator(this);
/*      */       }
/*  739 */       return new TxIterator(this, paramAffineTransform);
/*      */     }
/*      */ 
/*      */     public final Object clone()
/*      */     {
/*  757 */       if ((this instanceof GeneralPath)) {
/*  758 */         return new GeneralPath(this);
/*      */       }
/*  760 */       return new Float(this);
/*      */     }
/*      */ 
/*      */     private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */       throws IOException
/*      */     {
/*  893 */       super.writeObject(paramObjectOutputStream, false);
/*      */     }
/*      */ 
/*      */     private void readObject(ObjectInputStream paramObjectInputStream)
/*      */       throws ClassNotFoundException, IOException
/*      */     {
/*  912 */       super.readObject(paramObjectInputStream, false);
/*      */     }
/*      */ 
/*      */     static class CopyIterator extends Path2D.Iterator {
/*      */       float[] floatCoords;
/*      */ 
/*      */       CopyIterator(Path2D.Float paramFloat) {
/*  919 */         super();
/*  920 */         this.floatCoords = paramFloat.floatCoords;
/*      */       }
/*      */ 
/*      */       public int currentSegment(float[] paramArrayOfFloat) {
/*  924 */         int i = this.path.pointTypes[this.typeIdx];
/*  925 */         int j = curvecoords[i];
/*  926 */         if (j > 0) {
/*  927 */           System.arraycopy(this.floatCoords, this.pointIdx, paramArrayOfFloat, 0, j);
/*      */         }
/*      */ 
/*  930 */         return i;
/*      */       }
/*      */ 
/*      */       public int currentSegment(double[] paramArrayOfDouble) {
/*  934 */         int i = this.path.pointTypes[this.typeIdx];
/*  935 */         int j = curvecoords[i];
/*  936 */         if (j > 0) {
/*  937 */           for (int k = 0; k < j; k++) {
/*  938 */             paramArrayOfDouble[k] = this.floatCoords[(this.pointIdx + k)];
/*      */           }
/*      */         }
/*  941 */         return i;
/*      */       }
/*      */     }
/*      */ 
/*      */     static class TxIterator extends Path2D.Iterator {
/*      */       float[] floatCoords;
/*      */       AffineTransform affine;
/*      */ 
/*  950 */       TxIterator(Path2D.Float paramFloat, AffineTransform paramAffineTransform) { super();
/*  951 */         this.floatCoords = paramFloat.floatCoords;
/*  952 */         this.affine = paramAffineTransform; }
/*      */ 
/*      */       public int currentSegment(float[] paramArrayOfFloat)
/*      */       {
/*  956 */         int i = this.path.pointTypes[this.typeIdx];
/*  957 */         int j = curvecoords[i];
/*  958 */         if (j > 0) {
/*  959 */           this.affine.transform(this.floatCoords, this.pointIdx, paramArrayOfFloat, 0, j / 2);
/*      */         }
/*      */ 
/*  962 */         return i;
/*      */       }
/*      */ 
/*      */       public int currentSegment(double[] paramArrayOfDouble) {
/*  966 */         int i = this.path.pointTypes[this.typeIdx];
/*  967 */         int j = curvecoords[i];
/*  968 */         if (j > 0) {
/*  969 */           this.affine.transform(this.floatCoords, this.pointIdx, paramArrayOfDouble, 0, j / 2);
/*      */         }
/*      */ 
/*  972 */         return i;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class Iterator
/*      */     implements PathIterator
/*      */   {
/*      */     int typeIdx;
/*      */     int pointIdx;
/*      */     Path2D path;
/* 2576 */     static final int[] curvecoords = { 2, 2, 4, 6, 0 };
/*      */ 
/*      */     Iterator(Path2D paramPath2D) {
/* 2579 */       this.path = paramPath2D;
/*      */     }
/*      */ 
/*      */     public int getWindingRule() {
/* 2583 */       return this.path.getWindingRule();
/*      */     }
/*      */ 
/*      */     public boolean isDone() {
/* 2587 */       return this.typeIdx >= this.path.numTypes;
/*      */     }
/*      */ 
/*      */     public void next() {
/* 2591 */       int i = this.path.pointTypes[(this.typeIdx++)];
/* 2592 */       this.pointIdx += curvecoords[i];
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.Path2D
 * JD-Core Version:    0.6.2
 */