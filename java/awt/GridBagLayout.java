/*      */ package java.awt;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ public class GridBagLayout
/*      */   implements LayoutManager2, Serializable
/*      */ {
/*      */   static final int EMPIRICMULTIPLIER = 2;
/*      */   protected static final int MAXGRIDSIZE = 512;
/*      */   protected static final int MINSIZE = 1;
/*      */   protected static final int PREFERREDSIZE = 2;
/*      */   protected Hashtable<Component, GridBagConstraints> comptable;
/*      */   protected GridBagConstraints defaultConstraints;
/*      */   protected GridBagLayoutInfo layoutInfo;
/*      */   public int[] columnWidths;
/*      */   public int[] rowHeights;
/*      */   public double[] columnWeights;
/*      */   public double[] rowWeights;
/*      */   private Component componentAdjusting;
/* 2014 */   transient boolean rightToLeft = false;
/*      */   static final long serialVersionUID = 8838754796412211005L;
/*      */ 
/*      */   public GridBagLayout()
/*      */   {
/*  489 */     this.comptable = new Hashtable();
/*  490 */     this.defaultConstraints = new GridBagConstraints();
/*      */   }
/*      */ 
/*      */   public void setConstraints(Component paramComponent, GridBagConstraints paramGridBagConstraints)
/*      */   {
/*  499 */     this.comptable.put(paramComponent, (GridBagConstraints)paramGridBagConstraints.clone());
/*      */   }
/*      */ 
/*      */   public GridBagConstraints getConstraints(Component paramComponent)
/*      */   {
/*  511 */     GridBagConstraints localGridBagConstraints = (GridBagConstraints)this.comptable.get(paramComponent);
/*  512 */     if (localGridBagConstraints == null) {
/*  513 */       setConstraints(paramComponent, this.defaultConstraints);
/*  514 */       localGridBagConstraints = (GridBagConstraints)this.comptable.get(paramComponent);
/*      */     }
/*  516 */     return (GridBagConstraints)localGridBagConstraints.clone();
/*      */   }
/*      */ 
/*      */   protected GridBagConstraints lookupConstraints(Component paramComponent)
/*      */   {
/*  533 */     GridBagConstraints localGridBagConstraints = (GridBagConstraints)this.comptable.get(paramComponent);
/*  534 */     if (localGridBagConstraints == null) {
/*  535 */       setConstraints(paramComponent, this.defaultConstraints);
/*  536 */       localGridBagConstraints = (GridBagConstraints)this.comptable.get(paramComponent);
/*      */     }
/*  538 */     return localGridBagConstraints;
/*      */   }
/*      */ 
/*      */   private void removeConstraints(Component paramComponent)
/*      */   {
/*  546 */     this.comptable.remove(paramComponent);
/*      */   }
/*      */ 
/*      */   public Point getLayoutOrigin()
/*      */   {
/*  562 */     Point localPoint = new Point(0, 0);
/*  563 */     if (this.layoutInfo != null) {
/*  564 */       localPoint.x = this.layoutInfo.startx;
/*  565 */       localPoint.y = this.layoutInfo.starty;
/*      */     }
/*  567 */     return localPoint;
/*      */   }
/*      */ 
/*      */   public int[][] getLayoutDimensions()
/*      */   {
/*  580 */     if (this.layoutInfo == null) {
/*  581 */       return new int[2][0];
/*      */     }
/*  583 */     int[][] arrayOfInt = new int[2][];
/*  584 */     arrayOfInt[0] = new int[this.layoutInfo.width];
/*  585 */     arrayOfInt[1] = new int[this.layoutInfo.height];
/*      */ 
/*  587 */     System.arraycopy(this.layoutInfo.minWidth, 0, arrayOfInt[0], 0, this.layoutInfo.width);
/*  588 */     System.arraycopy(this.layoutInfo.minHeight, 0, arrayOfInt[1], 0, this.layoutInfo.height);
/*      */ 
/*  590 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public double[][] getLayoutWeights()
/*      */   {
/*  606 */     if (this.layoutInfo == null) {
/*  607 */       return new double[2][0];
/*      */     }
/*  609 */     double[][] arrayOfDouble = new double[2][];
/*  610 */     arrayOfDouble[0] = new double[this.layoutInfo.width];
/*  611 */     arrayOfDouble[1] = new double[this.layoutInfo.height];
/*      */ 
/*  613 */     System.arraycopy(this.layoutInfo.weightX, 0, arrayOfDouble[0], 0, this.layoutInfo.width);
/*  614 */     System.arraycopy(this.layoutInfo.weightY, 0, arrayOfDouble[1], 0, this.layoutInfo.height);
/*      */ 
/*  616 */     return arrayOfDouble;
/*      */   }
/*      */ 
/*      */   public Point location(int paramInt1, int paramInt2)
/*      */   {
/*  647 */     Point localPoint = new Point(0, 0);
/*      */ 
/*  650 */     if (this.layoutInfo == null) {
/*  651 */       return localPoint;
/*      */     }
/*  653 */     int j = this.layoutInfo.startx;
/*  654 */     if (!this.rightToLeft) {
/*  655 */       for (i = 0; i < this.layoutInfo.width; i++) {
/*  656 */         j += this.layoutInfo.minWidth[i];
/*  657 */         if (j > paramInt1)
/*      */           break;
/*      */       }
/*      */     }
/*  661 */     for (int i = this.layoutInfo.width - 1; (i >= 0) && 
/*  662 */       (j <= paramInt1); i--)
/*      */     {
/*  664 */       j += this.layoutInfo.minWidth[i];
/*      */     }
/*  666 */     i++;
/*      */ 
/*  668 */     localPoint.x = i;
/*      */ 
/*  670 */     j = this.layoutInfo.starty;
/*  671 */     for (i = 0; i < this.layoutInfo.height; i++) {
/*  672 */       j += this.layoutInfo.minHeight[i];
/*  673 */       if (j > paramInt2)
/*      */         break;
/*      */     }
/*  676 */     localPoint.y = i;
/*      */ 
/*  678 */     return localPoint;
/*      */   }
/*      */ 
/*      */   public void addLayoutComponent(String paramString, Component paramComponent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void addLayoutComponent(Component paramComponent, Object paramObject)
/*      */   {
/*  699 */     if ((paramObject instanceof GridBagConstraints))
/*  700 */       setConstraints(paramComponent, (GridBagConstraints)paramObject);
/*  701 */     else if (paramObject != null)
/*  702 */       throw new IllegalArgumentException("cannot add to layout: constraints must be a GridBagConstraint");
/*      */   }
/*      */ 
/*      */   public void removeLayoutComponent(Component paramComponent)
/*      */   {
/*  715 */     removeConstraints(paramComponent);
/*      */   }
/*      */ 
/*      */   public Dimension preferredLayoutSize(Container paramContainer)
/*      */   {
/*  730 */     GridBagLayoutInfo localGridBagLayoutInfo = getLayoutInfo(paramContainer, 2);
/*  731 */     return getMinSize(paramContainer, localGridBagLayoutInfo);
/*      */   }
/*      */ 
/*      */   public Dimension minimumLayoutSize(Container paramContainer)
/*      */   {
/*  744 */     GridBagLayoutInfo localGridBagLayoutInfo = getLayoutInfo(paramContainer, 1);
/*  745 */     return getMinSize(paramContainer, localGridBagLayoutInfo);
/*      */   }
/*      */ 
/*      */   public Dimension maximumLayoutSize(Container paramContainer)
/*      */   {
/*  758 */     return new Dimension(2147483647, 2147483647);
/*      */   }
/*      */ 
/*      */   public float getLayoutAlignmentX(Container paramContainer)
/*      */   {
/*  771 */     return 0.5F;
/*      */   }
/*      */ 
/*      */   public float getLayoutAlignmentY(Container paramContainer)
/*      */   {
/*  784 */     return 0.5F;
/*      */   }
/*      */ 
/*      */   public void invalidateLayout(Container paramContainer)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void layoutContainer(Container paramContainer)
/*      */   {
/*  806 */     arrangeGrid(paramContainer);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  814 */     return getClass().getName();
/*      */   }
/*      */ 
/*      */   protected GridBagLayoutInfo getLayoutInfo(Container paramContainer, int paramInt)
/*      */   {
/*  910 */     return GetLayoutInfo(paramContainer, paramInt);
/*      */   }
/*      */ 
/*      */   private long[] preInitMaximumArraySizes(Container paramContainer)
/*      */   {
/*  920 */     Component[] arrayOfComponent = paramContainer.getComponents();
/*      */ 
/*  925 */     int n = 0;
/*  926 */     int i1 = 0;
/*  927 */     long[] arrayOfLong = new long[2];
/*      */ 
/*  929 */     for (int i2 = 0; i2 < arrayOfComponent.length; i2++) {
/*  930 */       Component localComponent = arrayOfComponent[i2];
/*  931 */       if (localComponent.isVisible())
/*      */       {
/*  935 */         GridBagConstraints localGridBagConstraints = lookupConstraints(localComponent);
/*  936 */         int i = localGridBagConstraints.gridx;
/*  937 */         int j = localGridBagConstraints.gridy;
/*  938 */         int k = localGridBagConstraints.gridwidth;
/*  939 */         int m = localGridBagConstraints.gridheight;
/*      */ 
/*  946 */         if (i < 0) {
/*  947 */           i1++; i = i1;
/*      */         }
/*  949 */         if (j < 0) {
/*  950 */           n++; j = n;
/*      */         }
/*      */ 
/*  955 */         if (k <= 0) {
/*  956 */           k = 1;
/*      */         }
/*  958 */         if (m <= 0) {
/*  959 */           m = 1;
/*      */         }
/*      */ 
/*  962 */         n = Math.max(j + m, n);
/*  963 */         i1 = Math.max(i + k, i1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  969 */     arrayOfLong[0] = n;
/*  970 */     arrayOfLong[1] = i1;
/*  971 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   protected GridBagLayoutInfo GetLayoutInfo(Container paramContainer, int paramInt)
/*      */   {
/*  983 */     synchronized (paramContainer.getTreeLock())
/*      */     {
/*  988 */       Component[] arrayOfComponent = paramContainer.getComponents();
/*      */ 
/* 1000 */       int i5 = 0;
/* 1001 */       int i6 = 0;
/* 1002 */       int i7 = 1;
/* 1003 */       int i8 = 1;
/*      */ 
/* 1006 */       int i11 = 0;
/* 1007 */       int i12 = 0;
/*      */       int j;
/* 1017 */       int i = j = 0;
/*      */       int i10;
/* 1018 */       int i9 = i10 = -1;
/* 1019 */       long[] arrayOfLong = preInitMaximumArraySizes(paramContainer);
/*      */ 
/* 1028 */       i11 = 2L * arrayOfLong[0] > 2147483647L ? 2147483647 : 2 * (int)arrayOfLong[0];
/* 1029 */       i12 = 2L * arrayOfLong[1] > 2147483647L ? 2147483647 : 2 * (int)arrayOfLong[1];
/*      */ 
/* 1031 */       if (this.rowHeights != null) {
/* 1032 */         i11 = Math.max(i11, this.rowHeights.length);
/*      */       }
/* 1034 */       if (this.columnWidths != null) {
/* 1035 */         i12 = Math.max(i12, this.columnWidths.length);
/*      */       }
/*      */ 
/* 1038 */       int[] arrayOfInt1 = new int[i11];
/* 1039 */       int[] arrayOfInt2 = new int[i12];
/*      */ 
/* 1041 */       int i14 = 0;
/*      */       Component localComponent;
/*      */       GridBagConstraints localGridBagConstraints;
/*      */       int i1;
/*      */       int i2;
/* 1042 */       for (int k = 0; k < arrayOfComponent.length; k++) {
/* 1043 */         localComponent = arrayOfComponent[k];
/* 1044 */         if (localComponent.isVisible())
/*      */         {
/* 1046 */           localGridBagConstraints = lookupConstraints(localComponent);
/*      */ 
/* 1048 */           i5 = localGridBagConstraints.gridx;
/* 1049 */           i6 = localGridBagConstraints.gridy;
/* 1050 */           i7 = localGridBagConstraints.gridwidth;
/* 1051 */           if (i7 <= 0)
/* 1052 */             i7 = 1;
/* 1053 */           i8 = localGridBagConstraints.gridheight;
/* 1054 */           if (i8 <= 0) {
/* 1055 */             i8 = 1;
/*      */           }
/*      */ 
/* 1058 */           if ((i5 < 0) && (i6 < 0)) {
/* 1059 */             if (i9 >= 0)
/* 1060 */               i6 = i9;
/* 1061 */             else if (i10 >= 0)
/* 1062 */               i5 = i10;
/*      */             else
/* 1064 */               i6 = 0;
/*      */           }
/* 1066 */           if (i5 < 0) {
/* 1067 */             i1 = 0;
/* 1068 */             for (m = i6; m < i6 + i8; m++) {
/* 1069 */               i1 = Math.max(i1, arrayOfInt1[m]);
/*      */             }
/*      */ 
/* 1072 */             i5 = i1 - i5 - 1;
/* 1073 */             if (i5 < 0)
/* 1074 */               i5 = 0;
/*      */           }
/* 1076 */           else if (i6 < 0) {
/* 1077 */             i2 = 0;
/* 1078 */             for (m = i5; m < i5 + i7; m++) {
/* 1079 */               i2 = Math.max(i2, arrayOfInt2[m]);
/*      */             }
/* 1081 */             i6 = i2 - i6 - 1;
/* 1082 */             if (i6 < 0) {
/* 1083 */               i6 = 0;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1089 */           i1 = i5 + i7;
/* 1090 */           if (i < i1) {
/* 1091 */             i = i1;
/*      */           }
/* 1093 */           i2 = i6 + i8;
/* 1094 */           if (j < i2) {
/* 1095 */             j = i2;
/*      */           }
/*      */ 
/* 1099 */           for (m = i5; m < i5 + i7; m++) {
/* 1100 */             arrayOfInt2[m] = i2;
/*      */           }
/* 1102 */           for (m = i6; m < i6 + i8; m++)
/* 1103 */             arrayOfInt1[m] = i1;
/*      */           Dimension localDimension;
/* 1108 */           if (paramInt == 2)
/* 1109 */             localDimension = localComponent.getPreferredSize();
/*      */           else
/* 1111 */             localDimension = localComponent.getMinimumSize();
/* 1112 */           localGridBagConstraints.minWidth = localDimension.width;
/* 1113 */           localGridBagConstraints.minHeight = localDimension.height;
/* 1114 */           if (calculateBaseline(localComponent, localGridBagConstraints, localDimension)) {
/* 1115 */             i14 = 1;
/*      */           }
/*      */ 
/* 1120 */           if ((localGridBagConstraints.gridheight == 0) && (localGridBagConstraints.gridwidth == 0)) {
/* 1121 */             i9 = i10 = -1;
/*      */           }
/*      */ 
/* 1124 */           if ((localGridBagConstraints.gridheight == 0) && (i9 < 0)) {
/* 1125 */             i10 = i5 + i7;
/*      */           }
/* 1128 */           else if ((localGridBagConstraints.gridwidth == 0) && (i10 < 0)) {
/* 1129 */             i9 = i6 + i8;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1136 */       if ((this.columnWidths != null) && (i < this.columnWidths.length))
/* 1137 */         i = this.columnWidths.length;
/* 1138 */       if ((this.rowHeights != null) && (j < this.rowHeights.length)) {
/* 1139 */         j = this.rowHeights.length;
/*      */       }
/* 1141 */       GridBagLayoutInfo localGridBagLayoutInfo = new GridBagLayoutInfo(i, j);
/*      */ 
/* 1152 */       i9 = i10 = -1;
/*      */ 
/* 1154 */       Arrays.fill(arrayOfInt1, 0);
/* 1155 */       Arrays.fill(arrayOfInt2, 0);
/*      */ 
/* 1157 */       int[] arrayOfInt3 = null;
/* 1158 */       int[] arrayOfInt4 = null;
/* 1159 */       short[] arrayOfShort = null;
/*      */ 
/* 1161 */       if (i14 != 0) {
/* 1162 */         localGridBagLayoutInfo.maxAscent = (arrayOfInt3 = new int[j]);
/* 1163 */         localGridBagLayoutInfo.maxDescent = (arrayOfInt4 = new int[j]);
/* 1164 */         localGridBagLayoutInfo.baselineType = (arrayOfShort = new short[j]);
/* 1165 */         localGridBagLayoutInfo.hasBaseline = true;
/*      */       }
/*      */       int i3;
/* 1169 */       for (k = 0; k < arrayOfComponent.length; k++) {
/* 1170 */         localComponent = arrayOfComponent[k];
/* 1171 */         if (localComponent.isVisible())
/*      */         {
/* 1173 */           localGridBagConstraints = lookupConstraints(localComponent);
/*      */ 
/* 1175 */           i5 = localGridBagConstraints.gridx;
/* 1176 */           i6 = localGridBagConstraints.gridy;
/* 1177 */           i7 = localGridBagConstraints.gridwidth;
/* 1178 */           i8 = localGridBagConstraints.gridheight;
/*      */ 
/* 1181 */           if ((i5 < 0) && (i6 < 0)) {
/* 1182 */             if (i9 >= 0)
/* 1183 */               i6 = i9;
/* 1184 */             else if (i10 >= 0)
/* 1185 */               i5 = i10;
/*      */             else {
/* 1187 */               i6 = 0;
/*      */             }
/*      */           }
/* 1190 */           if (i5 < 0) {
/* 1191 */             if (i8 <= 0) {
/* 1192 */               i8 += localGridBagLayoutInfo.height - i6;
/* 1193 */               if (i8 < 1) {
/* 1194 */                 i8 = 1;
/*      */               }
/*      */             }
/* 1197 */             i1 = 0;
/* 1198 */             for (m = i6; m < i6 + i8; m++) {
/* 1199 */               i1 = Math.max(i1, arrayOfInt1[m]);
/*      */             }
/* 1201 */             i5 = i1 - i5 - 1;
/* 1202 */             if (i5 < 0)
/* 1203 */               i5 = 0;
/*      */           }
/* 1205 */           else if (i6 < 0) {
/* 1206 */             if (i7 <= 0) {
/* 1207 */               i7 += localGridBagLayoutInfo.width - i5;
/* 1208 */               if (i7 < 1) {
/* 1209 */                 i7 = 1;
/*      */               }
/*      */             }
/* 1212 */             i2 = 0;
/* 1213 */             for (m = i5; m < i5 + i7; m++) {
/* 1214 */               i2 = Math.max(i2, arrayOfInt2[m]);
/*      */             }
/*      */ 
/* 1217 */             i6 = i2 - i6 - 1;
/* 1218 */             if (i6 < 0) {
/* 1219 */               i6 = 0;
/*      */             }
/*      */           }
/* 1222 */           if (i7 <= 0) {
/* 1223 */             i7 += localGridBagLayoutInfo.width - i5;
/* 1224 */             if (i7 < 1) {
/* 1225 */               i7 = 1;
/*      */             }
/*      */           }
/* 1228 */           if (i8 <= 0) {
/* 1229 */             i8 += localGridBagLayoutInfo.height - i6;
/* 1230 */             if (i8 < 1) {
/* 1231 */               i8 = 1;
/*      */             }
/*      */           }
/* 1234 */           i1 = i5 + i7;
/* 1235 */           i2 = i6 + i8;
/*      */ 
/* 1237 */           for (m = i5; m < i5 + i7; m++) arrayOfInt2[m] = i2;
/* 1238 */           for (m = i6; m < i6 + i8; m++) arrayOfInt1[m] = i1;
/*      */ 
/* 1241 */           if ((localGridBagConstraints.gridheight == 0) && (localGridBagConstraints.gridwidth == 0))
/* 1242 */             i9 = i10 = -1;
/* 1243 */           if ((localGridBagConstraints.gridheight == 0) && (i9 < 0))
/* 1244 */             i10 = i5 + i7;
/* 1245 */           else if ((localGridBagConstraints.gridwidth == 0) && (i10 < 0)) {
/* 1246 */             i9 = i6 + i8;
/*      */           }
/*      */ 
/* 1249 */           localGridBagConstraints.tempX = i5;
/* 1250 */           localGridBagConstraints.tempY = i6;
/* 1251 */           localGridBagConstraints.tempWidth = i7;
/* 1252 */           localGridBagConstraints.tempHeight = i8;
/*      */ 
/* 1254 */           int i13 = localGridBagConstraints.anchor;
/* 1255 */           if (i14 != 0) {
/* 1256 */             switch (i13) {
/*      */             case 256:
/*      */             case 512:
/*      */             case 768:
/* 1260 */               if (localGridBagConstraints.ascent >= 0) {
/* 1261 */                 if (i8 == 1) {
/* 1262 */                   arrayOfInt3[i6] = Math.max(arrayOfInt3[i6], localGridBagConstraints.ascent);
/*      */ 
/* 1265 */                   arrayOfInt4[i6] = Math.max(arrayOfInt4[i6], localGridBagConstraints.descent);
/*      */                 }
/* 1270 */                 else if (localGridBagConstraints.baselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_DESCENT)
/*      */                 {
/* 1273 */                   arrayOfInt4[(i6 + i8 - 1)] = Math.max(arrayOfInt4[(i6 + i8 - 1)], localGridBagConstraints.descent);
/*      */                 }
/*      */                 else
/*      */                 {
/* 1279 */                   arrayOfInt3[i6] = Math.max(arrayOfInt3[i6], localGridBagConstraints.ascent);
/*      */                 }
/*      */ 
/* 1283 */                 if (localGridBagConstraints.baselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_DESCENT)
/*      */                 {
/*      */                   int tmp1469_1468 = (i6 + i8 - 1);
/*      */                   short[] tmp1469_1460 = arrayOfShort; tmp1469_1460[tmp1469_1468] = ((short)(tmp1469_1460[tmp1469_1468] | 1 << localGridBagConstraints.baselineResizeBehavior.ordinal()));
/*      */                 }
/*      */                 else
/*      */                 {
/* 1290 */                   int tmp1491_1489 = i6;
/*      */                   short[] tmp1491_1487 = arrayOfShort; tmp1491_1487[tmp1491_1489] = ((short)(tmp1491_1487[tmp1491_1489] | 1 << localGridBagConstraints.baselineResizeBehavior.ordinal())); }  } break;
/*      */             case 1024:
/*      */             case 1280:
/*      */             case 1536:
/* 1302 */               i3 = localGridBagConstraints.minHeight + localGridBagConstraints.insets.top + localGridBagConstraints.ipady;
/*      */ 
/* 1305 */               arrayOfInt3[i6] = Math.max(arrayOfInt3[i6], i3);
/*      */ 
/* 1307 */               arrayOfInt4[i6] = Math.max(arrayOfInt4[i6], localGridBagConstraints.insets.bottom);
/*      */ 
/* 1309 */               break;
/*      */             case 1792:
/*      */             case 2048:
/*      */             case 2304:
/* 1317 */               i3 = localGridBagConstraints.minHeight + localGridBagConstraints.insets.bottom + localGridBagConstraints.ipady;
/*      */ 
/* 1319 */               arrayOfInt4[i6] = Math.max(arrayOfInt4[i6], i3);
/*      */ 
/* 1321 */               arrayOfInt3[i6] = Math.max(arrayOfInt3[i6], localGridBagConstraints.insets.top);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1328 */       localGridBagLayoutInfo.weightX = new double[i12];
/* 1329 */       localGridBagLayoutInfo.weightY = new double[i11];
/* 1330 */       localGridBagLayoutInfo.minWidth = new int[i12];
/* 1331 */       localGridBagLayoutInfo.minHeight = new int[i11];
/*      */ 
/* 1337 */       if (this.columnWidths != null)
/* 1338 */         System.arraycopy(this.columnWidths, 0, localGridBagLayoutInfo.minWidth, 0, this.columnWidths.length);
/* 1339 */       if (this.rowHeights != null)
/* 1340 */         System.arraycopy(this.rowHeights, 0, localGridBagLayoutInfo.minHeight, 0, this.rowHeights.length);
/* 1341 */       if (this.columnWeights != null)
/* 1342 */         System.arraycopy(this.columnWeights, 0, localGridBagLayoutInfo.weightX, 0, Math.min(localGridBagLayoutInfo.weightX.length, this.columnWeights.length));
/* 1343 */       if (this.rowWeights != null) {
/* 1344 */         System.arraycopy(this.rowWeights, 0, localGridBagLayoutInfo.weightY, 0, Math.min(localGridBagLayoutInfo.weightY.length, this.rowWeights.length));
/*      */       }
/*      */ 
/* 1352 */       int i4 = 2147483647;
/*      */ 
/* 1354 */       int m = 1;
/*      */ 
/* 1356 */       for (; m != 2147483647; 
/* 1356 */         i4 = 2147483647) {
/* 1357 */         for (k = 0; k < arrayOfComponent.length; k++) {
/* 1358 */           localComponent = arrayOfComponent[k];
/* 1359 */           if (localComponent.isVisible())
/*      */           {
/* 1361 */             localGridBagConstraints = lookupConstraints(localComponent);
/*      */             double d1;
/*      */             int n;
/*      */             double d2;
/*      */             double d3;
/* 1363 */             if (localGridBagConstraints.tempWidth == m) {
/* 1364 */               i1 = localGridBagConstraints.tempX + localGridBagConstraints.tempWidth;
/*      */ 
/* 1373 */               d1 = localGridBagConstraints.weightx;
/* 1374 */               for (n = localGridBagConstraints.tempX; n < i1; n++)
/* 1375 */                 d1 -= localGridBagLayoutInfo.weightX[n];
/* 1376 */               if (d1 > 0.0D) {
/* 1377 */                 d2 = 0.0D;
/* 1378 */                 for (n = localGridBagConstraints.tempX; n < i1; n++)
/* 1379 */                   d2 += localGridBagLayoutInfo.weightX[n];
/* 1380 */                 for (n = localGridBagConstraints.tempX; (d2 > 0.0D) && (n < i1); n++) {
/* 1381 */                   d3 = localGridBagLayoutInfo.weightX[n];
/* 1382 */                   double d4 = d3 * d1 / d2;
/* 1383 */                   localGridBagLayoutInfo.weightX[n] += d4;
/* 1384 */                   d1 -= d4;
/* 1385 */                   d2 -= d3;
/*      */                 }
/*      */ 
/* 1388 */                 localGridBagLayoutInfo.weightX[(i1 - 1)] += d1;
/*      */               }
/*      */ 
/* 1399 */               i3 = localGridBagConstraints.minWidth + localGridBagConstraints.ipadx + localGridBagConstraints.insets.left + localGridBagConstraints.insets.right;
/*      */ 
/* 1403 */               for (n = localGridBagConstraints.tempX; n < i1; n++)
/* 1404 */                 i3 -= localGridBagLayoutInfo.minWidth[n];
/* 1405 */               if (i3 > 0) {
/* 1406 */                 d2 = 0.0D;
/* 1407 */                 for (n = localGridBagConstraints.tempX; n < i1; n++)
/* 1408 */                   d2 += localGridBagLayoutInfo.weightX[n];
/* 1409 */                 for (n = localGridBagConstraints.tempX; (d2 > 0.0D) && (n < i1); n++) {
/* 1410 */                   d3 = localGridBagLayoutInfo.weightX[n];
/* 1411 */                   int i15 = (int)(d3 * i3 / d2);
/* 1412 */                   localGridBagLayoutInfo.minWidth[n] += i15;
/* 1413 */                   i3 -= i15;
/* 1414 */                   d2 -= d3;
/*      */                 }
/*      */ 
/* 1417 */                 localGridBagLayoutInfo.minWidth[(i1 - 1)] += i3;
/*      */               }
/*      */             }
/* 1420 */             else if ((localGridBagConstraints.tempWidth > m) && (localGridBagConstraints.tempWidth < i4)) {
/* 1421 */               i4 = localGridBagConstraints.tempWidth;
/*      */             }
/*      */ 
/* 1424 */             if (localGridBagConstraints.tempHeight == m) {
/* 1425 */               i2 = localGridBagConstraints.tempY + localGridBagConstraints.tempHeight;
/*      */ 
/* 1434 */               d1 = localGridBagConstraints.weighty;
/* 1435 */               for (n = localGridBagConstraints.tempY; n < i2; n++)
/* 1436 */                 d1 -= localGridBagLayoutInfo.weightY[n];
/* 1437 */               if (d1 > 0.0D) {
/* 1438 */                 d2 = 0.0D;
/* 1439 */                 for (n = localGridBagConstraints.tempY; n < i2; n++)
/* 1440 */                   d2 += localGridBagLayoutInfo.weightY[n];
/* 1441 */                 for (n = localGridBagConstraints.tempY; (d2 > 0.0D) && (n < i2); n++) {
/* 1442 */                   d3 = localGridBagLayoutInfo.weightY[n];
/* 1443 */                   double d5 = d3 * d1 / d2;
/* 1444 */                   localGridBagLayoutInfo.weightY[n] += d5;
/* 1445 */                   d1 -= d5;
/* 1446 */                   d2 -= d3;
/*      */                 }
/*      */ 
/* 1449 */                 localGridBagLayoutInfo.weightY[(i2 - 1)] += d1;
/*      */               }
/*      */ 
/* 1460 */               i3 = -1;
/* 1461 */               if (i14 != 0) {
/* 1462 */                 switch (localGridBagConstraints.anchor) {
/*      */                 case 256:
/*      */                 case 512:
/*      */                 case 768:
/* 1466 */                   if (localGridBagConstraints.ascent >= 0)
/* 1467 */                     if (localGridBagConstraints.tempHeight == 1) {
/* 1468 */                       i3 = arrayOfInt3[localGridBagConstraints.tempY] + arrayOfInt4[localGridBagConstraints.tempY];
/*      */                     }
/* 1472 */                     else if (localGridBagConstraints.baselineResizeBehavior != Component.BaselineResizeBehavior.CONSTANT_DESCENT)
/*      */                     {
/* 1475 */                       i3 = arrayOfInt3[localGridBagConstraints.tempY] + localGridBagConstraints.descent;
/*      */                     }
/*      */                     else
/*      */                     {
/* 1480 */                       i3 = localGridBagConstraints.ascent + arrayOfInt4[(localGridBagConstraints.tempY + localGridBagConstraints.tempHeight - 1)]; }  break;
/*      */                 case 1024:
/*      */                 case 1280:
/*      */                 case 1536:
/* 1489 */                   i3 = localGridBagConstraints.insets.top + localGridBagConstraints.minHeight + localGridBagConstraints.ipady + arrayOfInt4[localGridBagConstraints.tempY];
/*      */ 
/* 1493 */                   break;
/*      */                 case 1792:
/*      */                 case 2048:
/*      */                 case 2304:
/* 1497 */                   i3 = arrayOfInt3[localGridBagConstraints.tempY] + localGridBagConstraints.minHeight + localGridBagConstraints.insets.bottom + localGridBagConstraints.ipady;
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/* 1504 */               if (i3 == -1) {
/* 1505 */                 i3 = localGridBagConstraints.minHeight + localGridBagConstraints.ipady + localGridBagConstraints.insets.top + localGridBagConstraints.insets.bottom;
/*      */               }
/*      */ 
/* 1510 */               for (n = localGridBagConstraints.tempY; n < i2; n++)
/* 1511 */                 i3 -= localGridBagLayoutInfo.minHeight[n];
/* 1512 */               if (i3 > 0) {
/* 1513 */                 d2 = 0.0D;
/* 1514 */                 for (n = localGridBagConstraints.tempY; n < i2; n++)
/* 1515 */                   d2 += localGridBagLayoutInfo.weightY[n];
/* 1516 */                 for (n = localGridBagConstraints.tempY; (d2 > 0.0D) && (n < i2); n++) {
/* 1517 */                   d3 = localGridBagLayoutInfo.weightY[n];
/* 1518 */                   int i16 = (int)(d3 * i3 / d2);
/* 1519 */                   localGridBagLayoutInfo.minHeight[n] += i16;
/* 1520 */                   i3 -= i16;
/* 1521 */                   d2 -= d3;
/*      */                 }
/*      */ 
/* 1524 */                 localGridBagLayoutInfo.minHeight[(i2 - 1)] += i3;
/*      */               }
/*      */             }
/* 1527 */             else if ((localGridBagConstraints.tempHeight > m) && (localGridBagConstraints.tempHeight < i4))
/*      */             {
/* 1529 */               i4 = localGridBagConstraints.tempHeight;
/*      */             }
/*      */           }
/*      */         }
/* 1356 */         m = i4;
/*      */       }
/*      */ 
/* 1532 */       return localGridBagLayoutInfo;
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean calculateBaseline(Component paramComponent, GridBagConstraints paramGridBagConstraints, Dimension paramDimension)
/*      */   {
/* 1546 */     int i = paramGridBagConstraints.anchor;
/* 1547 */     if ((i == 256) || (i == 512) || (i == 768))
/*      */     {
/* 1551 */       int j = paramDimension.width + paramGridBagConstraints.ipadx;
/* 1552 */       int k = paramDimension.height + paramGridBagConstraints.ipady;
/* 1553 */       paramGridBagConstraints.ascent = paramComponent.getBaseline(j, k);
/* 1554 */       if (paramGridBagConstraints.ascent >= 0)
/*      */       {
/* 1556 */         int m = paramGridBagConstraints.ascent;
/*      */ 
/* 1558 */         paramGridBagConstraints.descent = (k - paramGridBagConstraints.ascent + paramGridBagConstraints.insets.bottom);
/*      */ 
/* 1560 */         paramGridBagConstraints.ascent += paramGridBagConstraints.insets.top;
/* 1561 */         paramGridBagConstraints.baselineResizeBehavior = paramComponent.getBaselineResizeBehavior();
/*      */ 
/* 1563 */         paramGridBagConstraints.centerPadding = 0;
/* 1564 */         if (paramGridBagConstraints.baselineResizeBehavior == Component.BaselineResizeBehavior.CENTER_OFFSET)
/*      */         {
/* 1571 */           int n = paramComponent.getBaseline(j, k + 1);
/* 1572 */           paramGridBagConstraints.centerOffset = (m - k / 2);
/* 1573 */           if (k % 2 == 0) {
/* 1574 */             if (m != n) {
/* 1575 */               paramGridBagConstraints.centerPadding = 1;
/*      */             }
/*      */           }
/* 1578 */           else if (m == n) {
/* 1579 */             paramGridBagConstraints.centerOffset -= 1;
/* 1580 */             paramGridBagConstraints.centerPadding = 1;
/*      */           }
/*      */         }
/*      */       }
/* 1584 */       return true;
/*      */     }
/*      */ 
/* 1587 */     paramGridBagConstraints.ascent = -1;
/* 1588 */     return false;
/*      */   }
/*      */ 
/*      */   protected void adjustForGravity(GridBagConstraints paramGridBagConstraints, Rectangle paramRectangle)
/*      */   {
/* 1604 */     AdjustForGravity(paramGridBagConstraints, paramRectangle);
/*      */   }
/*      */ 
/*      */   protected void AdjustForGravity(GridBagConstraints paramGridBagConstraints, Rectangle paramRectangle)
/*      */   {
/* 1619 */     int k = paramRectangle.y;
/* 1620 */     int m = paramRectangle.height;
/*      */ 
/* 1622 */     if (!this.rightToLeft)
/* 1623 */       paramRectangle.x += paramGridBagConstraints.insets.left;
/*      */     else {
/* 1625 */       paramRectangle.x -= paramRectangle.width - paramGridBagConstraints.insets.right;
/*      */     }
/* 1627 */     paramRectangle.width -= paramGridBagConstraints.insets.left + paramGridBagConstraints.insets.right;
/* 1628 */     paramRectangle.y += paramGridBagConstraints.insets.top;
/* 1629 */     paramRectangle.height -= paramGridBagConstraints.insets.top + paramGridBagConstraints.insets.bottom;
/*      */ 
/* 1631 */     int i = 0;
/* 1632 */     if ((paramGridBagConstraints.fill != 2) && (paramGridBagConstraints.fill != 1) && (paramRectangle.width > paramGridBagConstraints.minWidth + paramGridBagConstraints.ipadx))
/*      */     {
/* 1635 */       i = paramRectangle.width - (paramGridBagConstraints.minWidth + paramGridBagConstraints.ipadx);
/* 1636 */       paramRectangle.width = (paramGridBagConstraints.minWidth + paramGridBagConstraints.ipadx);
/*      */     }
/*      */ 
/* 1639 */     int j = 0;
/* 1640 */     if ((paramGridBagConstraints.fill != 3) && (paramGridBagConstraints.fill != 1) && (paramRectangle.height > paramGridBagConstraints.minHeight + paramGridBagConstraints.ipady))
/*      */     {
/* 1643 */       j = paramRectangle.height - (paramGridBagConstraints.minHeight + paramGridBagConstraints.ipady);
/* 1644 */       paramRectangle.height = (paramGridBagConstraints.minHeight + paramGridBagConstraints.ipady);
/*      */     }
/*      */ 
/* 1647 */     switch (paramGridBagConstraints.anchor) {
/*      */     case 256:
/* 1649 */       paramRectangle.x += i / 2;
/* 1650 */       alignOnBaseline(paramGridBagConstraints, paramRectangle, k, m);
/* 1651 */       break;
/*      */     case 512:
/* 1653 */       if (this.rightToLeft) {
/* 1654 */         paramRectangle.x += i;
/*      */       }
/* 1656 */       alignOnBaseline(paramGridBagConstraints, paramRectangle, k, m);
/* 1657 */       break;
/*      */     case 768:
/* 1659 */       if (!this.rightToLeft) {
/* 1660 */         paramRectangle.x += i;
/*      */       }
/* 1662 */       alignOnBaseline(paramGridBagConstraints, paramRectangle, k, m);
/* 1663 */       break;
/*      */     case 1024:
/* 1665 */       paramRectangle.x += i / 2;
/* 1666 */       alignAboveBaseline(paramGridBagConstraints, paramRectangle, k, m);
/* 1667 */       break;
/*      */     case 1280:
/* 1669 */       if (this.rightToLeft) {
/* 1670 */         paramRectangle.x += i;
/*      */       }
/* 1672 */       alignAboveBaseline(paramGridBagConstraints, paramRectangle, k, m);
/* 1673 */       break;
/*      */     case 1536:
/* 1675 */       if (!this.rightToLeft) {
/* 1676 */         paramRectangle.x += i;
/*      */       }
/* 1678 */       alignAboveBaseline(paramGridBagConstraints, paramRectangle, k, m);
/* 1679 */       break;
/*      */     case 1792:
/* 1681 */       paramRectangle.x += i / 2;
/* 1682 */       alignBelowBaseline(paramGridBagConstraints, paramRectangle, k, m);
/* 1683 */       break;
/*      */     case 2048:
/* 1685 */       if (this.rightToLeft) {
/* 1686 */         paramRectangle.x += i;
/*      */       }
/* 1688 */       alignBelowBaseline(paramGridBagConstraints, paramRectangle, k, m);
/* 1689 */       break;
/*      */     case 2304:
/* 1691 */       if (!this.rightToLeft) {
/* 1692 */         paramRectangle.x += i;
/*      */       }
/* 1694 */       alignBelowBaseline(paramGridBagConstraints, paramRectangle, k, m);
/* 1695 */       break;
/*      */     case 10:
/* 1697 */       paramRectangle.x += i / 2;
/* 1698 */       paramRectangle.y += j / 2;
/* 1699 */       break;
/*      */     case 11:
/*      */     case 19:
/* 1702 */       paramRectangle.x += i / 2;
/* 1703 */       break;
/*      */     case 12:
/* 1705 */       paramRectangle.x += i;
/* 1706 */       break;
/*      */     case 13:
/* 1708 */       paramRectangle.x += i;
/* 1709 */       paramRectangle.y += j / 2;
/* 1710 */       break;
/*      */     case 14:
/* 1712 */       paramRectangle.x += i;
/* 1713 */       paramRectangle.y += j;
/* 1714 */       break;
/*      */     case 15:
/*      */     case 20:
/* 1717 */       paramRectangle.x += i / 2;
/* 1718 */       paramRectangle.y += j;
/* 1719 */       break;
/*      */     case 16:
/* 1721 */       paramRectangle.y += j;
/* 1722 */       break;
/*      */     case 17:
/* 1724 */       paramRectangle.y += j / 2;
/* 1725 */       break;
/*      */     case 18:
/* 1727 */       break;
/*      */     case 21:
/* 1729 */       if (this.rightToLeft) {
/* 1730 */         paramRectangle.x += i;
/*      */       }
/* 1732 */       paramRectangle.y += j / 2;
/* 1733 */       break;
/*      */     case 22:
/* 1735 */       if (!this.rightToLeft) {
/* 1736 */         paramRectangle.x += i;
/*      */       }
/* 1738 */       paramRectangle.y += j / 2;
/* 1739 */       break;
/*      */     case 23:
/* 1741 */       if (this.rightToLeft)
/* 1742 */         paramRectangle.x += i; break;
/*      */     case 24:
/* 1746 */       if (!this.rightToLeft)
/* 1747 */         paramRectangle.x += i; break;
/*      */     case 25:
/* 1751 */       if (this.rightToLeft) {
/* 1752 */         paramRectangle.x += i;
/*      */       }
/* 1754 */       paramRectangle.y += j;
/* 1755 */       break;
/*      */     case 26:
/* 1757 */       if (!this.rightToLeft) {
/* 1758 */         paramRectangle.x += i;
/*      */       }
/* 1760 */       paramRectangle.y += j;
/* 1761 */       break;
/*      */     default:
/* 1763 */       throw new IllegalArgumentException("illegal anchor value");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void alignOnBaseline(GridBagConstraints paramGridBagConstraints, Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 1778 */     if (paramGridBagConstraints.ascent >= 0)
/*      */     {
/*      */       int i;
/* 1779 */       if (paramGridBagConstraints.baselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_DESCENT)
/*      */       {
/* 1787 */         i = paramInt1 + paramInt2 - this.layoutInfo.maxDescent[(paramGridBagConstraints.tempY + paramGridBagConstraints.tempHeight - 1)] + paramGridBagConstraints.descent - paramGridBagConstraints.insets.bottom;
/*      */ 
/* 1790 */         if (!paramGridBagConstraints.isVerticallyResizable())
/*      */         {
/* 1793 */           paramRectangle.y = (i - paramGridBagConstraints.minHeight);
/* 1794 */           paramRectangle.height = paramGridBagConstraints.minHeight;
/*      */         }
/*      */         else
/*      */         {
/* 1800 */           paramRectangle.height = (i - paramInt1 - paramGridBagConstraints.insets.top);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1807 */         int j = paramGridBagConstraints.ascent;
/* 1808 */         if (this.layoutInfo.hasConstantDescent(paramGridBagConstraints.tempY))
/*      */         {
/* 1811 */           i = paramInt2 - this.layoutInfo.maxDescent[paramGridBagConstraints.tempY];
/*      */         }
/*      */         else
/*      */         {
/* 1815 */           i = this.layoutInfo.maxAscent[paramGridBagConstraints.tempY];
/*      */         }
/*      */         int k;
/*      */         int m;
/* 1817 */         if (paramGridBagConstraints.baselineResizeBehavior == Component.BaselineResizeBehavior.OTHER)
/*      */         {
/* 1822 */           k = 0;
/* 1823 */           j = this.componentAdjusting.getBaseline(paramRectangle.width, paramRectangle.height);
/* 1824 */           if (j >= 0)
/*      */           {
/* 1828 */             j += paramGridBagConstraints.insets.top;
/*      */           }
/* 1830 */           if ((j >= 0) && (j <= i))
/*      */           {
/* 1833 */             if (i + (paramRectangle.height - j - paramGridBagConstraints.insets.top) <= paramInt2 - paramGridBagConstraints.insets.bottom)
/*      */             {
/* 1836 */               k = 1;
/*      */             }
/* 1838 */             else if (paramGridBagConstraints.isVerticallyResizable())
/*      */             {
/* 1841 */               m = this.componentAdjusting.getBaseline(paramRectangle.width, paramInt2 - paramGridBagConstraints.insets.bottom - i + j);
/*      */ 
/* 1844 */               if (m >= 0) {
/* 1845 */                 m += paramGridBagConstraints.insets.top;
/*      */               }
/* 1847 */               if ((m >= 0) && (m <= j))
/*      */               {
/* 1849 */                 paramRectangle.height = (paramInt2 - paramGridBagConstraints.insets.bottom - i + j);
/*      */ 
/* 1851 */                 j = m;
/* 1852 */                 k = 1;
/*      */               }
/*      */             }
/*      */           }
/* 1856 */           if (k == 0)
/*      */           {
/* 1858 */             j = paramGridBagConstraints.ascent;
/* 1859 */             paramRectangle.width = paramGridBagConstraints.minWidth;
/* 1860 */             paramRectangle.height = paramGridBagConstraints.minHeight;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1866 */         paramRectangle.y = (paramInt1 + i - j + paramGridBagConstraints.insets.top);
/* 1867 */         if (paramGridBagConstraints.isVerticallyResizable()) {
/* 1868 */           switch (1.$SwitchMap$java$awt$Component$BaselineResizeBehavior[paramGridBagConstraints.baselineResizeBehavior.ordinal()]) {
/*      */           case 1:
/* 1870 */             paramRectangle.height = Math.max(paramGridBagConstraints.minHeight, paramInt1 + paramInt2 - paramRectangle.y - paramGridBagConstraints.insets.bottom);
/*      */ 
/* 1872 */             break;
/*      */           case 2:
/* 1875 */             k = paramRectangle.y - paramInt1 - paramGridBagConstraints.insets.top;
/* 1876 */             m = paramInt1 + paramInt2 - paramRectangle.y - paramGridBagConstraints.minHeight - paramGridBagConstraints.insets.bottom;
/*      */ 
/* 1878 */             int n = Math.min(k, m);
/* 1879 */             n += n;
/* 1880 */             if ((n > 0) && ((paramGridBagConstraints.minHeight + paramGridBagConstraints.centerPadding + n) / 2 + paramGridBagConstraints.centerOffset != i))
/*      */             {
/* 1884 */               n--;
/*      */             }
/* 1886 */             paramRectangle.height = (paramGridBagConstraints.minHeight + n);
/* 1887 */             paramRectangle.y = (paramInt1 + i - (paramRectangle.height + paramGridBagConstraints.centerPadding) / 2 - paramGridBagConstraints.centerOffset);
/*      */ 
/* 1891 */             break;
/*      */           case 3:
/* 1894 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1902 */       centerVertically(paramGridBagConstraints, paramRectangle, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void alignAboveBaseline(GridBagConstraints paramGridBagConstraints, Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 1913 */     if (this.layoutInfo.hasBaseline(paramGridBagConstraints.tempY))
/*      */     {
/*      */       int i;
/* 1915 */       if (this.layoutInfo.hasConstantDescent(paramGridBagConstraints.tempY))
/*      */       {
/* 1917 */         i = paramInt1 + paramInt2 - this.layoutInfo.maxDescent[paramGridBagConstraints.tempY];
/*      */       }
/*      */       else
/*      */       {
/* 1921 */         i = paramInt1 + this.layoutInfo.maxAscent[paramGridBagConstraints.tempY];
/*      */       }
/* 1923 */       if (paramGridBagConstraints.isVerticallyResizable())
/*      */       {
/* 1926 */         paramRectangle.y = (paramInt1 + paramGridBagConstraints.insets.top);
/* 1927 */         paramRectangle.height = (i - paramRectangle.y);
/*      */       }
/*      */       else
/*      */       {
/* 1931 */         paramRectangle.height = (paramGridBagConstraints.minHeight + paramGridBagConstraints.ipady);
/* 1932 */         paramRectangle.y = (i - paramRectangle.height);
/*      */       }
/*      */     }
/*      */     else {
/* 1936 */       centerVertically(paramGridBagConstraints, paramRectangle, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void alignBelowBaseline(GridBagConstraints paramGridBagConstraints, Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 1945 */     if (this.layoutInfo.hasBaseline(paramGridBagConstraints.tempY)) {
/* 1946 */       if (this.layoutInfo.hasConstantDescent(paramGridBagConstraints.tempY))
/*      */       {
/* 1948 */         paramRectangle.y = (paramInt1 + paramInt2 - this.layoutInfo.maxDescent[paramGridBagConstraints.tempY]);
/*      */       }
/*      */       else
/*      */       {
/* 1952 */         paramRectangle.y = (paramInt1 + this.layoutInfo.maxAscent[paramGridBagConstraints.tempY]);
/*      */       }
/* 1954 */       if (paramGridBagConstraints.isVerticallyResizable())
/* 1955 */         paramRectangle.height = (paramInt1 + paramInt2 - paramRectangle.y - paramGridBagConstraints.insets.bottom);
/*      */     }
/*      */     else
/*      */     {
/* 1959 */       centerVertically(paramGridBagConstraints, paramRectangle, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void centerVertically(GridBagConstraints paramGridBagConstraints, Rectangle paramRectangle, int paramInt)
/*      */   {
/* 1965 */     if (!paramGridBagConstraints.isVerticallyResizable())
/* 1966 */       paramRectangle.y += Math.max(0, (paramInt - paramGridBagConstraints.insets.top - paramGridBagConstraints.insets.bottom - paramGridBagConstraints.minHeight - paramGridBagConstraints.ipady) / 2);
/*      */   }
/*      */ 
/*      */   protected Dimension getMinSize(Container paramContainer, GridBagLayoutInfo paramGridBagLayoutInfo)
/*      */   {
/* 1985 */     return GetMinSize(paramContainer, paramGridBagLayoutInfo);
/*      */   }
/*      */ 
/*      */   protected Dimension GetMinSize(Container paramContainer, GridBagLayoutInfo paramGridBagLayoutInfo)
/*      */   {
/* 1997 */     Dimension localDimension = new Dimension();
/*      */ 
/* 1999 */     Insets localInsets = paramContainer.getInsets();
/*      */ 
/* 2001 */     int j = 0;
/* 2002 */     for (int i = 0; i < paramGridBagLayoutInfo.width; i++)
/* 2003 */       j += paramGridBagLayoutInfo.minWidth[i];
/* 2004 */     localDimension.width = (j + localInsets.left + localInsets.right);
/*      */ 
/* 2006 */     j = 0;
/* 2007 */     for (i = 0; i < paramGridBagLayoutInfo.height; i++)
/* 2008 */       j += paramGridBagLayoutInfo.minHeight[i];
/* 2009 */     localDimension.height = (j + localInsets.top + localInsets.bottom);
/*      */ 
/* 2011 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected void arrangeGrid(Container paramContainer)
/*      */   {
/* 2025 */     ArrangeGrid(paramContainer);
/*      */   }
/*      */ 
/*      */   protected void ArrangeGrid(Container paramContainer)
/*      */   {
/* 2040 */     Insets localInsets = paramContainer.getInsets();
/* 2041 */     Component[] arrayOfComponent = paramContainer.getComponents();
/*      */ 
/* 2043 */     Rectangle localRectangle = new Rectangle();
/*      */ 
/* 2048 */     this.rightToLeft = (!paramContainer.getComponentOrientation().isLeftToRight());
/*      */ 
/* 2054 */     if ((arrayOfComponent.length == 0) && ((this.columnWidths == null) || (this.columnWidths.length == 0)) && ((this.rowHeights == null) || (this.rowHeights.length == 0)))
/*      */     {
/* 2057 */       return;
/*      */     }
/*      */ 
/* 2065 */     GridBagLayoutInfo localGridBagLayoutInfo = getLayoutInfo(paramContainer, 2);
/* 2066 */     Dimension localDimension = getMinSize(paramContainer, localGridBagLayoutInfo);
/*      */ 
/* 2068 */     if ((paramContainer.width < localDimension.width) || (paramContainer.height < localDimension.height)) {
/* 2069 */       localGridBagLayoutInfo = getLayoutInfo(paramContainer, 1);
/* 2070 */       localDimension = getMinSize(paramContainer, localGridBagLayoutInfo);
/*      */     }
/*      */ 
/* 2073 */     this.layoutInfo = localGridBagLayoutInfo;
/* 2074 */     localRectangle.width = localDimension.width;
/* 2075 */     localRectangle.height = localDimension.height;
/*      */ 
/* 2097 */     int k = paramContainer.width - localRectangle.width;
/*      */     double d;
/*      */     int j;
/*      */     int n;
/* 2098 */     if (k != 0) {
/* 2099 */       d = 0.0D;
/* 2100 */       for (j = 0; j < localGridBagLayoutInfo.width; j++)
/* 2101 */         d += localGridBagLayoutInfo.weightX[j];
/* 2102 */       if (d > 0.0D) {
/* 2103 */         for (j = 0; j < localGridBagLayoutInfo.width; j++) {
/* 2104 */           n = (int)(k * localGridBagLayoutInfo.weightX[j] / d);
/* 2105 */           localGridBagLayoutInfo.minWidth[j] += n;
/* 2106 */           localRectangle.width += n;
/* 2107 */           if (localGridBagLayoutInfo.minWidth[j] < 0) {
/* 2108 */             localRectangle.width -= localGridBagLayoutInfo.minWidth[j];
/* 2109 */             localGridBagLayoutInfo.minWidth[j] = 0;
/*      */           }
/*      */         }
/*      */       }
/* 2113 */       k = paramContainer.width - localRectangle.width;
/*      */     }
/*      */     else
/*      */     {
/* 2117 */       k = 0;
/*      */     }
/*      */ 
/* 2120 */     int m = paramContainer.height - localRectangle.height;
/* 2121 */     if (m != 0) {
/* 2122 */       d = 0.0D;
/* 2123 */       for (j = 0; j < localGridBagLayoutInfo.height; j++)
/* 2124 */         d += localGridBagLayoutInfo.weightY[j];
/* 2125 */       if (d > 0.0D) {
/* 2126 */         for (j = 0; j < localGridBagLayoutInfo.height; j++) {
/* 2127 */           n = (int)(m * localGridBagLayoutInfo.weightY[j] / d);
/* 2128 */           localGridBagLayoutInfo.minHeight[j] += n;
/* 2129 */           localRectangle.height += n;
/* 2130 */           if (localGridBagLayoutInfo.minHeight[j] < 0) {
/* 2131 */             localRectangle.height -= localGridBagLayoutInfo.minHeight[j];
/* 2132 */             localGridBagLayoutInfo.minHeight[j] = 0;
/*      */           }
/*      */         }
/*      */       }
/* 2136 */       m = paramContainer.height - localRectangle.height;
/*      */     }
/*      */     else
/*      */     {
/* 2140 */       m = 0;
/*      */     }
/*      */ 
/* 2155 */     localGridBagLayoutInfo.startx = (k / 2 + localInsets.left);
/* 2156 */     localGridBagLayoutInfo.starty = (m / 2 + localInsets.top);
/*      */ 
/* 2158 */     for (int i = 0; i < arrayOfComponent.length; i++) {
/* 2159 */       Component localComponent = arrayOfComponent[i];
/* 2160 */       if (localComponent.isVisible())
/*      */       {
/* 2163 */         GridBagConstraints localGridBagConstraints = lookupConstraints(localComponent);
/*      */ 
/* 2165 */         if (!this.rightToLeft) {
/* 2166 */           localRectangle.x = localGridBagLayoutInfo.startx;
/* 2167 */           for (j = 0; j < localGridBagConstraints.tempX; j++)
/* 2168 */             localRectangle.x += localGridBagLayoutInfo.minWidth[j];
/*      */         }
/* 2170 */         localRectangle.x = (paramContainer.width - (k / 2 + localInsets.right));
/* 2171 */         for (j = 0; j < localGridBagConstraints.tempX; j++) {
/* 2172 */           localRectangle.x -= localGridBagLayoutInfo.minWidth[j];
/*      */         }
/*      */ 
/* 2175 */         localRectangle.y = localGridBagLayoutInfo.starty;
/* 2176 */         for (j = 0; j < localGridBagConstraints.tempY; j++) {
/* 2177 */           localRectangle.y += localGridBagLayoutInfo.minHeight[j];
/*      */         }
/* 2179 */         localRectangle.width = 0;
/* 2180 */         for (j = localGridBagConstraints.tempX; 
/* 2181 */           j < localGridBagConstraints.tempX + localGridBagConstraints.tempWidth; 
/* 2182 */           j++) {
/* 2183 */           localRectangle.width += localGridBagLayoutInfo.minWidth[j];
/*      */         }
/*      */ 
/* 2186 */         localRectangle.height = 0;
/* 2187 */         for (j = localGridBagConstraints.tempY; 
/* 2188 */           j < localGridBagConstraints.tempY + localGridBagConstraints.tempHeight; 
/* 2189 */           j++) {
/* 2190 */           localRectangle.height += localGridBagLayoutInfo.minHeight[j];
/*      */         }
/*      */ 
/* 2193 */         this.componentAdjusting = localComponent;
/* 2194 */         adjustForGravity(localGridBagConstraints, localRectangle);
/*      */ 
/* 2198 */         if (localRectangle.x < 0) {
/* 2199 */           localRectangle.width += localRectangle.x;
/* 2200 */           localRectangle.x = 0;
/*      */         }
/*      */ 
/* 2203 */         if (localRectangle.y < 0) {
/* 2204 */           localRectangle.height += localRectangle.y;
/* 2205 */           localRectangle.y = 0;
/*      */         }
/*      */ 
/* 2214 */         if ((localRectangle.width <= 0) || (localRectangle.height <= 0)) {
/* 2215 */           localComponent.setBounds(0, 0, 0, 0);
/*      */         }
/* 2218 */         else if ((localComponent.x != localRectangle.x) || (localComponent.y != localRectangle.y) || (localComponent.width != localRectangle.width) || (localComponent.height != localRectangle.height))
/*      */         {
/* 2220 */           localComponent.setBounds(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.GridBagLayout
 * JD-Core Version:    0.6.2
 */