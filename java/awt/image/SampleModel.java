/*      */ package java.awt.image;
/*      */ 
/*      */ public abstract class SampleModel
/*      */ {
/*      */   protected int width;
/*      */   protected int height;
/*      */   protected int numBands;
/*      */   protected int dataType;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public SampleModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  124 */     long l = paramInt2 * paramInt3;
/*  125 */     if ((paramInt2 <= 0) || (paramInt3 <= 0)) {
/*  126 */       throw new IllegalArgumentException("Width (" + paramInt2 + ") and height (" + paramInt3 + ") must be > 0");
/*      */     }
/*      */ 
/*  129 */     if (l >= 2147483647L) {
/*  130 */       throw new IllegalArgumentException("Dimensions (width=" + paramInt2 + " height=" + paramInt3 + ") are too large");
/*      */     }
/*      */ 
/*  134 */     if ((paramInt1 < 0) || ((paramInt1 > 5) && (paramInt1 != 32)))
/*      */     {
/*  138 */       throw new IllegalArgumentException("Unsupported dataType: " + paramInt1);
/*      */     }
/*      */ 
/*  142 */     if (paramInt4 <= 0) {
/*  143 */       throw new IllegalArgumentException("Number of bands must be > 0");
/*      */     }
/*      */ 
/*  146 */     this.dataType = paramInt1;
/*  147 */     this.width = paramInt2;
/*  148 */     this.height = paramInt3;
/*  149 */     this.numBands = paramInt4;
/*      */   }
/*      */ 
/*      */   public final int getWidth()
/*      */   {
/*  157 */     return this.width;
/*      */   }
/*      */ 
/*      */   public final int getHeight()
/*      */   {
/*  165 */     return this.height;
/*      */   }
/*      */ 
/*      */   public final int getNumBands()
/*      */   {
/*  173 */     return this.numBands;
/*      */   }
/*      */ 
/*      */   public abstract int getNumDataElements();
/*      */ 
/*      */   public final int getDataType()
/*      */   {
/*  197 */     return this.dataType;
/*      */   }
/*      */ 
/*      */   public int getTransferType()
/*      */   {
/*  218 */     return this.dataType;
/*      */   }
/*      */ 
/*      */   public int[] getPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/*      */     int[] arrayOfInt;
/*  241 */     if (paramArrayOfInt != null)
/*  242 */       arrayOfInt = paramArrayOfInt;
/*      */     else {
/*  244 */       arrayOfInt = new int[this.numBands];
/*      */     }
/*  246 */     for (int i = 0; i < this.numBands; i++) {
/*  247 */       arrayOfInt[i] = getSample(paramInt1, paramInt2, i, paramDataBuffer);
/*      */     }
/*      */ 
/*  250 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public abstract Object getDataElements(int paramInt1, int paramInt2, Object paramObject, DataBuffer paramDataBuffer);
/*      */ 
/*      */   public Object getDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject, DataBuffer paramDataBuffer)
/*      */   {
/*  356 */     int i = getTransferType();
/*  357 */     int j = getNumDataElements();
/*  358 */     int k = 0;
/*  359 */     Object localObject = null;
/*      */ 
/*  361 */     int m = paramInt1 + paramInt3;
/*  362 */     int n = paramInt2 + paramInt4;
/*      */ 
/*  364 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (m < 0) || (m > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (n < 0) || (n > this.height))
/*      */     {
/*  367 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */     int i3;
/*      */     int i5;
/*      */     int i7;
/*      */     int i9;
/*  370 */     switch (i)
/*      */     {
/*      */     case 0:
/*      */       byte[] arrayOfByte2;
/*  377 */       if (paramObject == null)
/*  378 */         arrayOfByte2 = new byte[j * paramInt3 * paramInt4];
/*      */       else {
/*  380 */         arrayOfByte2 = (byte[])paramObject;
/*      */       }
/*  382 */       for (int i1 = paramInt2; i1 < n; i1++) {
/*  383 */         for (int i2 = paramInt1; i2 < m; i2++) {
/*  384 */           localObject = getDataElements(i2, i1, localObject, paramDataBuffer);
/*  385 */           byte[] arrayOfByte1 = (byte[])localObject;
/*  386 */           for (i3 = 0; i3 < j; i3++) {
/*  387 */             arrayOfByte2[(k++)] = arrayOfByte1[i3];
/*      */           }
/*      */         }
/*      */       }
/*  391 */       paramObject = arrayOfByte2;
/*  392 */       break;
/*      */     case 1:
/*      */     case 2:
/*      */       short[] arrayOfShort1;
/*  400 */       if (paramObject == null)
/*  401 */         arrayOfShort1 = new short[j * paramInt3 * paramInt4];
/*      */       else {
/*  403 */         arrayOfShort1 = (short[])paramObject;
/*      */       }
/*  405 */       for (i3 = paramInt2; i3 < n; i3++) {
/*  406 */         for (int i4 = paramInt1; i4 < m; i4++) {
/*  407 */           localObject = getDataElements(i4, i3, localObject, paramDataBuffer);
/*  408 */           short[] arrayOfShort2 = (short[])localObject;
/*  409 */           for (i5 = 0; i5 < j; i5++) {
/*  410 */             arrayOfShort1[(k++)] = arrayOfShort2[i5];
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  415 */       paramObject = arrayOfShort1;
/*  416 */       break;
/*      */     case 3:
/*      */       int[] arrayOfInt1;
/*  423 */       if (paramObject == null)
/*  424 */         arrayOfInt1 = new int[j * paramInt3 * paramInt4];
/*      */       else {
/*  426 */         arrayOfInt1 = (int[])paramObject;
/*      */       }
/*  428 */       for (i5 = paramInt2; i5 < n; i5++) {
/*  429 */         for (int i6 = paramInt1; i6 < m; i6++) {
/*  430 */           localObject = getDataElements(i6, i5, localObject, paramDataBuffer);
/*  431 */           int[] arrayOfInt2 = (int[])localObject;
/*  432 */           for (i7 = 0; i7 < j; i7++) {
/*  433 */             arrayOfInt1[(k++)] = arrayOfInt2[i7];
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  438 */       paramObject = arrayOfInt1;
/*  439 */       break;
/*      */     case 4:
/*      */       float[] arrayOfFloat1;
/*  446 */       if (paramObject == null)
/*  447 */         arrayOfFloat1 = new float[j * paramInt3 * paramInt4];
/*      */       else {
/*  449 */         arrayOfFloat1 = (float[])paramObject;
/*      */       }
/*  451 */       for (i7 = paramInt2; i7 < n; i7++) {
/*  452 */         for (int i8 = paramInt1; i8 < m; i8++) {
/*  453 */           localObject = getDataElements(i8, i7, localObject, paramDataBuffer);
/*  454 */           float[] arrayOfFloat2 = (float[])localObject;
/*  455 */           for (i9 = 0; i9 < j; i9++) {
/*  456 */             arrayOfFloat1[(k++)] = arrayOfFloat2[i9];
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  461 */       paramObject = arrayOfFloat1;
/*  462 */       break;
/*      */     case 5:
/*      */       double[] arrayOfDouble1;
/*  469 */       if (paramObject == null)
/*  470 */         arrayOfDouble1 = new double[j * paramInt3 * paramInt4];
/*      */       else {
/*  472 */         arrayOfDouble1 = (double[])paramObject;
/*      */       }
/*  474 */       for (i9 = paramInt2; i9 < n; i9++) {
/*  475 */         for (int i10 = paramInt1; i10 < m; i10++) {
/*  476 */           localObject = getDataElements(i10, i9, localObject, paramDataBuffer);
/*  477 */           double[] arrayOfDouble2 = (double[])localObject;
/*  478 */           for (int i11 = 0; i11 < j; i11++) {
/*  479 */             arrayOfDouble1[(k++)] = arrayOfDouble2[i11];
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  484 */       paramObject = arrayOfDouble1;
/*      */     }
/*      */ 
/*  488 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public abstract void setDataElements(int paramInt1, int paramInt2, Object paramObject, DataBuffer paramDataBuffer);
/*      */ 
/*      */   public void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject, DataBuffer paramDataBuffer)
/*      */   {
/*  586 */     int i = 0;
/*  587 */     Object localObject = null;
/*  588 */     int j = getTransferType();
/*  589 */     int k = getNumDataElements();
/*      */ 
/*  591 */     int m = paramInt1 + paramInt3;
/*  592 */     int n = paramInt2 + paramInt4;
/*      */ 
/*  594 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (m < 0) || (m > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (n < 0) || (n > this.height))
/*      */     {
/*  597 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */     int i3;
/*      */     int i5;
/*      */     int i7;
/*      */     int i9;
/*  600 */     switch (j)
/*      */     {
/*      */     case 0:
/*  604 */       byte[] arrayOfByte1 = (byte[])paramObject;
/*  605 */       byte[] arrayOfByte2 = new byte[k];
/*      */ 
/*  607 */       for (int i1 = paramInt2; i1 < n; i1++) {
/*  608 */         for (int i2 = paramInt1; i2 < m; i2++) {
/*  609 */           for (i3 = 0; i3 < k; i3++) {
/*  610 */             arrayOfByte2[i3] = arrayOfByte1[(i++)];
/*      */           }
/*      */ 
/*  613 */           setDataElements(i2, i1, arrayOfByte2, paramDataBuffer);
/*      */         }
/*      */       }
/*  616 */       break;
/*      */     case 1:
/*      */     case 2:
/*  621 */       short[] arrayOfShort1 = (short[])paramObject;
/*  622 */       short[] arrayOfShort2 = new short[k];
/*      */ 
/*  624 */       for (i3 = paramInt2; i3 < n; i3++) {
/*  625 */         for (int i4 = paramInt1; i4 < m; i4++) {
/*  626 */           for (i5 = 0; i5 < k; i5++) {
/*  627 */             arrayOfShort2[i5] = arrayOfShort1[(i++)];
/*      */           }
/*      */ 
/*  630 */           setDataElements(i4, i3, arrayOfShort2, paramDataBuffer);
/*      */         }
/*      */       }
/*  633 */       break;
/*      */     case 3:
/*  637 */       int[] arrayOfInt1 = (int[])paramObject;
/*  638 */       int[] arrayOfInt2 = new int[k];
/*      */ 
/*  640 */       for (i5 = paramInt2; i5 < n; i5++) {
/*  641 */         for (int i6 = paramInt1; i6 < m; i6++) {
/*  642 */           for (i7 = 0; i7 < k; i7++) {
/*  643 */             arrayOfInt2[i7] = arrayOfInt1[(i++)];
/*      */           }
/*      */ 
/*  646 */           setDataElements(i6, i5, arrayOfInt2, paramDataBuffer);
/*      */         }
/*      */       }
/*  649 */       break;
/*      */     case 4:
/*  653 */       float[] arrayOfFloat1 = (float[])paramObject;
/*  654 */       float[] arrayOfFloat2 = new float[k];
/*      */ 
/*  656 */       for (i7 = paramInt2; i7 < n; i7++) {
/*  657 */         for (int i8 = paramInt1; i8 < m; i8++) {
/*  658 */           for (i9 = 0; i9 < k; i9++) {
/*  659 */             arrayOfFloat2[i9] = arrayOfFloat1[(i++)];
/*      */           }
/*      */ 
/*  662 */           setDataElements(i8, i7, arrayOfFloat2, paramDataBuffer);
/*      */         }
/*      */       }
/*  665 */       break;
/*      */     case 5:
/*  669 */       double[] arrayOfDouble1 = (double[])paramObject;
/*  670 */       double[] arrayOfDouble2 = new double[k];
/*      */ 
/*  672 */       for (i9 = paramInt2; i9 < n; i9++)
/*  673 */         for (int i10 = paramInt1; i10 < m; i10++) {
/*  674 */           for (int i11 = 0; i11 < k; i11++) {
/*  675 */             arrayOfDouble2[i11] = arrayOfDouble1[(i++)];
/*      */           }
/*      */ 
/*  678 */           setDataElements(i10, i9, arrayOfDouble2, paramDataBuffer);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public float[] getPixel(int paramInt1, int paramInt2, float[] paramArrayOfFloat, DataBuffer paramDataBuffer)
/*      */   {
/*      */     float[] arrayOfFloat;
/*  706 */     if (paramArrayOfFloat != null)
/*  707 */       arrayOfFloat = paramArrayOfFloat;
/*      */     else {
/*  709 */       arrayOfFloat = new float[this.numBands];
/*      */     }
/*  711 */     for (int i = 0; i < this.numBands; i++) {
/*  712 */       arrayOfFloat[i] = getSampleFloat(paramInt1, paramInt2, i, paramDataBuffer);
/*      */     }
/*  714 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   public double[] getPixel(int paramInt1, int paramInt2, double[] paramArrayOfDouble, DataBuffer paramDataBuffer)
/*      */   {
/*      */     double[] arrayOfDouble;
/*  737 */     if (paramArrayOfDouble != null)
/*  738 */       arrayOfDouble = paramArrayOfDouble;
/*      */     else {
/*  740 */       arrayOfDouble = new double[this.numBands];
/*      */     }
/*  742 */     for (int i = 0; i < this.numBands; i++) {
/*  743 */       arrayOfDouble[i] = getSampleDouble(paramInt1, paramInt2, i, paramDataBuffer);
/*      */     }
/*  745 */     return arrayOfDouble;
/*      */   }
/*      */ 
/*      */   public int[] getPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/*  770 */     int i = 0;
/*  771 */     int j = paramInt1 + paramInt3;
/*  772 */     int k = paramInt2 + paramInt4;
/*      */ 
/*  774 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (j < 0) || (j > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (k < 0) || (k > this.height))
/*      */     {
/*  777 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */     int[] arrayOfInt;
/*  780 */     if (paramArrayOfInt != null)
/*  781 */       arrayOfInt = paramArrayOfInt;
/*      */     else {
/*  783 */       arrayOfInt = new int[this.numBands * paramInt3 * paramInt4];
/*      */     }
/*  785 */     for (int m = paramInt2; m < k; m++) {
/*  786 */       for (int n = paramInt1; n < j; n++) {
/*  787 */         for (int i1 = 0; i1 < this.numBands; i1++) {
/*  788 */           arrayOfInt[(i++)] = getSample(n, m, i1, paramDataBuffer);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  793 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public float[] getPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float[] paramArrayOfFloat, DataBuffer paramDataBuffer)
/*      */   {
/*  818 */     int i = 0;
/*  819 */     int j = paramInt1 + paramInt3;
/*  820 */     int k = paramInt2 + paramInt4;
/*      */ 
/*  822 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (j < 0) || (j > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (k < 0) || (k > this.height))
/*      */     {
/*  825 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */     float[] arrayOfFloat;
/*  828 */     if (paramArrayOfFloat != null)
/*  829 */       arrayOfFloat = paramArrayOfFloat;
/*      */     else {
/*  831 */       arrayOfFloat = new float[this.numBands * paramInt3 * paramInt4];
/*      */     }
/*  833 */     for (int m = paramInt2; m < k; m++) {
/*  834 */       for (int n = paramInt1; n < j; n++) {
/*  835 */         for (int i1 = 0; i1 < this.numBands; i1++) {
/*  836 */           arrayOfFloat[(i++)] = getSampleFloat(n, m, i1, paramDataBuffer);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  841 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   public double[] getPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double[] paramArrayOfDouble, DataBuffer paramDataBuffer)
/*      */   {
/*  865 */     int i = 0;
/*  866 */     int j = paramInt1 + paramInt3;
/*  867 */     int k = paramInt2 + paramInt4;
/*      */ 
/*  869 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (j < 0) || (j > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (k < 0) || (k > this.height))
/*      */     {
/*  872 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */     double[] arrayOfDouble;
/*  875 */     if (paramArrayOfDouble != null)
/*  876 */       arrayOfDouble = paramArrayOfDouble;
/*      */     else {
/*  878 */       arrayOfDouble = new double[this.numBands * paramInt3 * paramInt4];
/*      */     }
/*      */ 
/*  881 */     for (int m = paramInt2; m < k; m++) {
/*  882 */       for (int n = paramInt1; n < j; n++) {
/*  883 */         for (int i1 = 0; i1 < this.numBands; i1++) {
/*  884 */           arrayOfDouble[(i++)] = getSampleDouble(n, m, i1, paramDataBuffer);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  889 */     return arrayOfDouble;
/*      */   }
/*      */ 
/*      */   public abstract int getSample(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer);
/*      */ 
/*      */   public float getSampleFloat(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*      */   {
/*  930 */     float f = getSample(paramInt1, paramInt2, paramInt3, paramDataBuffer);
/*  931 */     return f;
/*      */   }
/*      */ 
/*      */   public double getSampleDouble(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*      */   {
/*  953 */     double d = getSample(paramInt1, paramInt2, paramInt3, paramDataBuffer);
/*  954 */     return d;
/*      */   }
/*      */ 
/*      */   public int[] getSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/*  981 */     int i = 0;
/*  982 */     int j = paramInt1 + paramInt3;
/*  983 */     int k = paramInt2 + paramInt4;
/*      */ 
/*  985 */     if ((paramInt1 < 0) || (j < paramInt1) || (j > this.width) || (paramInt2 < 0) || (k < paramInt2) || (k > this.height))
/*      */     {
/*  988 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */     int[] arrayOfInt;
/*  991 */     if (paramArrayOfInt != null)
/*  992 */       arrayOfInt = paramArrayOfInt;
/*      */     else {
/*  994 */       arrayOfInt = new int[paramInt3 * paramInt4];
/*      */     }
/*  996 */     for (int m = paramInt2; m < k; m++) {
/*  997 */       for (int n = paramInt1; n < j; n++) {
/*  998 */         arrayOfInt[(i++)] = getSample(n, m, paramInt5, paramDataBuffer);
/*      */       }
/*      */     }
/*      */ 
/* 1002 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public float[] getSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, float[] paramArrayOfFloat, DataBuffer paramDataBuffer)
/*      */   {
/* 1030 */     int i = 0;
/* 1031 */     int j = paramInt1 + paramInt3;
/* 1032 */     int k = paramInt2 + paramInt4;
/*      */ 
/* 1034 */     if ((paramInt1 < 0) || (j < paramInt1) || (j > this.width) || (paramInt2 < 0) || (k < paramInt2) || (k > this.height))
/*      */     {
/* 1037 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates");
/*      */     }
/*      */     float[] arrayOfFloat;
/* 1040 */     if (paramArrayOfFloat != null)
/* 1041 */       arrayOfFloat = paramArrayOfFloat;
/*      */     else {
/* 1043 */       arrayOfFloat = new float[paramInt3 * paramInt4];
/*      */     }
/* 1045 */     for (int m = paramInt2; m < k; m++) {
/* 1046 */       for (int n = paramInt1; n < j; n++) {
/* 1047 */         arrayOfFloat[(i++)] = getSampleFloat(n, m, paramInt5, paramDataBuffer);
/*      */       }
/*      */     }
/*      */ 
/* 1051 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   public double[] getSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, double[] paramArrayOfDouble, DataBuffer paramDataBuffer)
/*      */   {
/* 1079 */     int i = 0;
/* 1080 */     int j = paramInt1 + paramInt3;
/* 1081 */     int k = paramInt2 + paramInt4;
/*      */ 
/* 1083 */     if ((paramInt1 < 0) || (j < paramInt1) || (j > this.width) || (paramInt2 < 0) || (k < paramInt2) || (k > this.height))
/*      */     {
/* 1086 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates");
/*      */     }
/*      */     double[] arrayOfDouble;
/* 1089 */     if (paramArrayOfDouble != null)
/* 1090 */       arrayOfDouble = paramArrayOfDouble;
/*      */     else {
/* 1092 */       arrayOfDouble = new double[paramInt3 * paramInt4];
/*      */     }
/* 1094 */     for (int m = paramInt2; m < k; m++) {
/* 1095 */       for (int n = paramInt1; n < j; n++) {
/* 1096 */         arrayOfDouble[(i++)] = getSampleDouble(n, m, paramInt5, paramDataBuffer);
/*      */       }
/*      */     }
/*      */ 
/* 1100 */     return arrayOfDouble;
/*      */   }
/*      */ 
/*      */   public void setPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/* 1119 */     for (int i = 0; i < this.numBands; i++)
/* 1120 */       setSample(paramInt1, paramInt2, i, paramArrayOfInt[i], paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public void setPixel(int paramInt1, int paramInt2, float[] paramArrayOfFloat, DataBuffer paramDataBuffer)
/*      */   {
/* 1139 */     for (int i = 0; i < this.numBands; i++)
/* 1140 */       setSample(paramInt1, paramInt2, i, paramArrayOfFloat[i], paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public void setPixel(int paramInt1, int paramInt2, double[] paramArrayOfDouble, DataBuffer paramDataBuffer)
/*      */   {
/* 1158 */     for (int i = 0; i < this.numBands; i++)
/* 1159 */       setSample(paramInt1, paramInt2, i, paramArrayOfDouble[i], paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/* 1181 */     int i = 0;
/* 1182 */     int j = paramInt1 + paramInt3;
/* 1183 */     int k = paramInt2 + paramInt4;
/*      */ 
/* 1185 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (j < 0) || (j > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (k < 0) || (k > this.height))
/*      */     {
/* 1188 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */ 
/* 1191 */     for (int m = paramInt2; m < k; m++)
/* 1192 */       for (int n = paramInt1; n < j; n++)
/* 1193 */         for (int i1 = 0; i1 < this.numBands; i1++)
/* 1194 */           setSample(n, m, i1, paramArrayOfInt[(i++)], paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float[] paramArrayOfFloat, DataBuffer paramDataBuffer)
/*      */   {
/* 1219 */     int i = 0;
/* 1220 */     int j = paramInt1 + paramInt3;
/* 1221 */     int k = paramInt2 + paramInt4;
/*      */ 
/* 1223 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (j < 0) || (j > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (k < 0) || (k > this.height))
/*      */     {
/* 1226 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */ 
/* 1229 */     for (int m = paramInt2; m < k; m++)
/* 1230 */       for (int n = paramInt1; n < j; n++)
/* 1231 */         for (int i1 = 0; i1 < this.numBands; i1++)
/* 1232 */           setSample(n, m, i1, paramArrayOfFloat[(i++)], paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double[] paramArrayOfDouble, DataBuffer paramDataBuffer)
/*      */   {
/* 1257 */     int i = 0;
/* 1258 */     int j = paramInt1 + paramInt3;
/* 1259 */     int k = paramInt2 + paramInt4;
/*      */ 
/* 1261 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (j < 0) || (j > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (k < 0) || (k > this.height))
/*      */     {
/* 1264 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */ 
/* 1267 */     for (int m = paramInt2; m < k; m++)
/* 1268 */       for (int n = paramInt1; n < j; n++)
/* 1269 */         for (int i1 = 0; i1 < this.numBands; i1++)
/* 1270 */           setSample(n, m, i1, paramArrayOfDouble[(i++)], paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public abstract void setSample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, DataBuffer paramDataBuffer);
/*      */ 
/*      */   public void setSample(int paramInt1, int paramInt2, int paramInt3, float paramFloat, DataBuffer paramDataBuffer)
/*      */   {
/* 1319 */     int i = (int)paramFloat;
/*      */ 
/* 1321 */     setSample(paramInt1, paramInt2, paramInt3, i, paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public void setSample(int paramInt1, int paramInt2, int paramInt3, double paramDouble, DataBuffer paramDataBuffer)
/*      */   {
/* 1347 */     int i = (int)paramDouble;
/*      */ 
/* 1349 */     setSample(paramInt1, paramInt2, paramInt3, i, paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public void setSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/* 1374 */     int i = 0;
/* 1375 */     int j = paramInt1 + paramInt3;
/* 1376 */     int k = paramInt2 + paramInt4;
/* 1377 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (j < 0) || (j > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (k < 0) || (k > this.height))
/*      */     {
/* 1380 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */ 
/* 1383 */     for (int m = paramInt2; m < k; m++)
/* 1384 */       for (int n = paramInt1; n < j; n++)
/* 1385 */         setSample(n, m, paramInt5, paramArrayOfInt[(i++)], paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public void setSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, float[] paramArrayOfFloat, DataBuffer paramDataBuffer)
/*      */   {
/* 1411 */     int i = 0;
/* 1412 */     int j = paramInt1 + paramInt3;
/* 1413 */     int k = paramInt2 + paramInt4;
/*      */ 
/* 1415 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (j < 0) || (j > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (k < 0) || (k > this.height))
/*      */     {
/* 1418 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */ 
/* 1421 */     for (int m = paramInt2; m < k; m++)
/* 1422 */       for (int n = paramInt1; n < j; n++)
/* 1423 */         setSample(n, m, paramInt5, paramArrayOfFloat[(i++)], paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public void setSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, double[] paramArrayOfDouble, DataBuffer paramDataBuffer)
/*      */   {
/* 1449 */     int i = 0;
/* 1450 */     int j = paramInt1 + paramInt3;
/* 1451 */     int k = paramInt2 + paramInt4;
/*      */ 
/* 1454 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (j < 0) || (j > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (k < 0) || (k > this.height))
/*      */     {
/* 1457 */       throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
/*      */     }
/*      */ 
/* 1460 */     for (int m = paramInt2; m < k; m++)
/* 1461 */       for (int n = paramInt1; n < j; n++)
/* 1462 */         setSample(n, m, paramInt5, paramArrayOfDouble[(i++)], paramDataBuffer);
/*      */   }
/*      */ 
/*      */   public abstract SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2);
/*      */ 
/*      */   public abstract SampleModel createSubsetSampleModel(int[] paramArrayOfInt);
/*      */ 
/*      */   public abstract DataBuffer createDataBuffer();
/*      */ 
/*      */   public abstract int[] getSampleSize();
/*      */ 
/*      */   public abstract int getSampleSize(int paramInt);
/*      */ 
/*      */   static
/*      */   {
/*  104 */     ColorModel.loadLibraries();
/*  105 */     initIDs();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.SampleModel
 * JD-Core Version:    0.6.2
 */