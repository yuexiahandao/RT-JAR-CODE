/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Insets;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ public class VariableGridLayout extends GridLayout
/*     */ {
/*  42 */   BitSet rowsSet = new BitSet();
/*  43 */   double[] rowFractions = null;
/*     */ 
/*  45 */   BitSet colsSet = new BitSet();
/*  46 */   double[] colFractions = null;
/*     */   int rows;
/*     */   int cols;
/*     */   int hgap;
/*     */   int vgap;
/*     */ 
/*     */   public VariableGridLayout(int paramInt1, int paramInt2)
/*     */   {
/*  59 */     this(paramInt1, paramInt2, 0, 0);
/*     */ 
/*  61 */     if (paramInt1 != 0) {
/*  62 */       this.rowsSet = new BitSet(paramInt1);
/*  63 */       stdRowFractions(paramInt1);
/*     */     }
/*     */ 
/*  66 */     if (paramInt2 != 0) {
/*  67 */       this.colsSet = new BitSet(paramInt2);
/*  68 */       stdColFractions(paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public VariableGridLayout(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  82 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/*  84 */     this.rows = paramInt1;
/*  85 */     this.cols = paramInt2;
/*  86 */     this.hgap = paramInt3;
/*  87 */     this.vgap = paramInt4;
/*     */ 
/*  89 */     if (paramInt1 != 0) {
/*  90 */       this.rowsSet = new BitSet(paramInt1);
/*  91 */       stdRowFractions(paramInt1);
/*     */     }
/*     */ 
/*  94 */     if (paramInt2 != 0) {
/*  95 */       this.colsSet = new BitSet(paramInt2);
/*  96 */       stdColFractions(paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   void stdRowFractions(int paramInt) {
/* 101 */     this.rowFractions = new double[paramInt];
/* 102 */     for (int i = 0; i < paramInt; i++)
/* 103 */       this.rowFractions[i] = (1.0D / paramInt);
/*     */   }
/*     */ 
/*     */   void stdColFractions(int paramInt)
/*     */   {
/* 108 */     this.colFractions = new double[paramInt];
/* 109 */     for (int i = 0; i < paramInt; i++)
/* 110 */       this.colFractions[i] = (1.0D / paramInt);
/*     */   }
/*     */ 
/*     */   public void setRowFraction(int paramInt, double paramDouble)
/*     */   {
/* 115 */     this.rowsSet.set(paramInt);
/* 116 */     this.rowFractions[paramInt] = paramDouble;
/*     */   }
/*     */ 
/*     */   public void setColFraction(int paramInt, double paramDouble) {
/* 120 */     this.colsSet.set(paramInt);
/* 121 */     this.colFractions[paramInt] = paramDouble;
/*     */   }
/*     */ 
/*     */   public double getRowFraction(int paramInt) {
/* 125 */     return this.rowFractions[paramInt];
/*     */   }
/*     */ 
/*     */   public double getColFraction(int paramInt) {
/* 129 */     return this.colFractions[paramInt];
/*     */   }
/*     */ 
/*     */   void allocateExtraSpace(double[] paramArrayOfDouble, BitSet paramBitSet)
/*     */   {
/* 134 */     double d1 = 0.0D;
/* 135 */     int i = 0;
/*     */ 
/* 137 */     for (int j = 0; j < paramArrayOfDouble.length; j++) {
/* 138 */       if (paramBitSet.get(j))
/* 139 */         d1 += paramArrayOfDouble[j];
/*     */       else {
/* 141 */         i++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 146 */     if (i != 0) {
/* 147 */       double d2 = (1.0D - d1) / i;
/* 148 */       for (j = 0; j < paramArrayOfDouble.length; j++)
/* 149 */         if (!paramBitSet.get(j)) {
/* 150 */           paramArrayOfDouble[j] = d2;
/* 151 */           paramBitSet.set(j);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   void allocateExtraSpace()
/*     */   {
/* 159 */     allocateExtraSpace(this.rowFractions, this.rowsSet);
/* 160 */     allocateExtraSpace(this.colFractions, this.colsSet);
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 169 */     Insets localInsets = paramContainer.insets();
/* 170 */     int i = paramContainer.countComponents();
/* 171 */     int j = this.rows;
/* 172 */     int k = this.cols;
/*     */ 
/* 174 */     if (j > 0)
/* 175 */       k = (i + j - 1) / j;
/*     */     else {
/* 177 */       j = (i + k - 1) / k;
/*     */     }
/*     */ 
/* 180 */     if (this.rows == 0) {
/* 181 */       stdRowFractions(j);
/*     */     }
/* 183 */     if (this.cols == 0) {
/* 184 */       stdColFractions(k);
/*     */     }
/*     */ 
/* 187 */     Dimension localDimension = paramContainer.size();
/* 188 */     int m = localDimension.width - (localInsets.left + localInsets.right);
/* 189 */     int n = localDimension.height - (localInsets.top + localInsets.bottom);
/*     */ 
/* 191 */     m -= (k - 1) * this.hgap;
/* 192 */     n -= (j - 1) * this.vgap;
/*     */ 
/* 194 */     allocateExtraSpace();
/*     */ 
/* 196 */     int i1 = 0; for (int i2 = localInsets.left; i1 < k; i1++) {
/* 197 */       int i3 = (int)(getColFraction(i1) * m);
/* 198 */       int i4 = 0; for (int i5 = localInsets.top; i4 < j; i4++) {
/* 199 */         int i6 = i4 * k + i1;
/* 200 */         int i7 = (int)(getRowFraction(i4) * n);
/*     */ 
/* 202 */         if (i6 < i) {
/* 203 */           paramContainer.getComponent(i6).reshape(i2, i5, i3, i7);
/*     */         }
/* 205 */         i5 += i7 + this.vgap;
/*     */       }
/* 207 */       i2 += i3 + this.hgap;
/*     */     }
/*     */   }
/*     */ 
/*     */   static String fracsToString(double[] paramArrayOfDouble) {
/* 212 */     String str = "[" + paramArrayOfDouble.length + "]";
/*     */ 
/* 214 */     for (int i = 0; i < paramArrayOfDouble.length; i++) {
/* 215 */       str = str + "<" + paramArrayOfDouble[i] + ">";
/*     */     }
/* 217 */     return str;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 224 */     return getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap + ",rows=" + this.rows + ",cols=" + this.cols + ",rowFracs=" + fracsToString(this.rowFractions) + ",colFracs=" + fracsToString(this.colFractions) + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.VariableGridLayout
 * JD-Core Version:    0.6.2
 */