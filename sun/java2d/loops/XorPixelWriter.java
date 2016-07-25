/*      */ package sun.java2d.loops;
/*      */ 
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ 
/*      */ abstract class XorPixelWriter extends PixelWriter
/*      */ {
/*      */   protected ColorModel dstCM;
/*      */ 
/*      */   public void writePixel(int paramInt1, int paramInt2)
/*      */   {
/* 1024 */     Object localObject = this.dstRast.getDataElements(paramInt1, paramInt2, null);
/* 1025 */     xorPixel(localObject);
/* 1026 */     this.dstRast.setDataElements(paramInt1, paramInt2, localObject);
/*      */   }
/*      */ 
/*      */   protected abstract void xorPixel(Object paramObject);
/*      */ 
/*      */   public static class ByteData extends XorPixelWriter {
/*      */     byte[] xorData;
/*      */ 
/*      */     ByteData(Object paramObject1, Object paramObject2) {
/* 1035 */       this.xorData = ((byte[])paramObject1);
/* 1036 */       xorPixel(paramObject2);
/* 1037 */       this.xorData = ((byte[])paramObject2);
/*      */     }
/*      */ 
/*      */     protected void xorPixel(Object paramObject) {
/* 1041 */       byte[] arrayOfByte = (byte[])paramObject;
/* 1042 */       for (int i = 0; i < arrayOfByte.length; i++)
/*      */       {
/*      */         int tmp18_17 = i;
/*      */         byte[] tmp18_16 = arrayOfByte; tmp18_16[tmp18_17] = ((byte)(tmp18_16[tmp18_17] ^ this.xorData[i]));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DoubleData extends XorPixelWriter
/*      */   {
/*      */     long[] xorData;
/*      */ 
/*      */     DoubleData(Object paramObject1, Object paramObject2)
/*      */     {
/* 1108 */       double[] arrayOfDouble1 = (double[])paramObject1;
/* 1109 */       double[] arrayOfDouble2 = (double[])paramObject2;
/* 1110 */       this.xorData = new long[arrayOfDouble1.length];
/* 1111 */       for (int i = 0; i < arrayOfDouble1.length; i++)
/* 1112 */         this.xorData[i] = (Double.doubleToLongBits(arrayOfDouble1[i]) ^ Double.doubleToLongBits(arrayOfDouble2[i]));
/*      */     }
/*      */ 
/*      */     protected void xorPixel(Object paramObject)
/*      */     {
/* 1118 */       double[] arrayOfDouble = (double[])paramObject;
/* 1119 */       for (int i = 0; i < arrayOfDouble.length; i++) {
/* 1120 */         long l = Double.doubleToLongBits(arrayOfDouble[i]) ^ this.xorData[i];
/* 1121 */         arrayOfDouble[i] = Double.longBitsToDouble(l);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class FloatData extends XorPixelWriter
/*      */   {
/*      */     int[] xorData;
/*      */ 
/*      */     FloatData(Object paramObject1, Object paramObject2)
/*      */     {
/* 1086 */       float[] arrayOfFloat1 = (float[])paramObject1;
/* 1087 */       float[] arrayOfFloat2 = (float[])paramObject2;
/* 1088 */       this.xorData = new int[arrayOfFloat1.length];
/* 1089 */       for (int i = 0; i < arrayOfFloat1.length; i++)
/* 1090 */         this.xorData[i] = (Float.floatToIntBits(arrayOfFloat1[i]) ^ Float.floatToIntBits(arrayOfFloat2[i]));
/*      */     }
/*      */ 
/*      */     protected void xorPixel(Object paramObject)
/*      */     {
/* 1096 */       float[] arrayOfFloat = (float[])paramObject;
/* 1097 */       for (int i = 0; i < arrayOfFloat.length; i++) {
/* 1098 */         int j = Float.floatToIntBits(arrayOfFloat[i]) ^ this.xorData[i];
/* 1099 */         arrayOfFloat[i] = Float.intBitsToFloat(j);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class IntData extends XorPixelWriter
/*      */   {
/*      */     int[] xorData;
/*      */ 
/*      */     IntData(Object paramObject1, Object paramObject2)
/*      */     {
/* 1069 */       this.xorData = ((int[])paramObject1);
/* 1070 */       xorPixel(paramObject2);
/* 1071 */       this.xorData = ((int[])paramObject2);
/*      */     }
/*      */ 
/*      */     protected void xorPixel(Object paramObject) {
/* 1075 */       int[] arrayOfInt = (int[])paramObject;
/* 1076 */       for (int i = 0; i < arrayOfInt.length; i++)
/* 1077 */         arrayOfInt[i] ^= this.xorData[i];
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ShortData extends XorPixelWriter
/*      */   {
/*      */     short[] xorData;
/*      */ 
/*      */     ShortData(Object paramObject1, Object paramObject2)
/*      */     {
/* 1052 */       this.xorData = ((short[])paramObject1);
/* 1053 */       xorPixel(paramObject2);
/* 1054 */       this.xorData = ((short[])paramObject2);
/*      */     }
/*      */ 
/*      */     protected void xorPixel(Object paramObject) {
/* 1058 */       short[] arrayOfShort = (short[])paramObject;
/* 1059 */       for (int i = 0; i < arrayOfShort.length; i++)
/*      */       {
/*      */         int tmp18_17 = i;
/*      */         short[] tmp18_16 = arrayOfShort; tmp18_16[tmp18_17] = ((short)(tmp18_16[tmp18_17] ^ this.xorData[i]));
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.XorPixelWriter
 * JD-Core Version:    0.6.2
 */