/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ class SmartGridLayout
/*     */   implements LayoutManager, Serializable
/*     */ {
/*  43 */   int rows = 2;
/*  44 */   int columns = 2;
/*  45 */   int xGap = 2;
/*  46 */   int yGap = 2;
/*  47 */   int componentCount = 0;
/*     */   Component[][] layoutGrid;
/*     */ 
/*     */   public SmartGridLayout(int paramInt1, int paramInt2)
/*     */   {
/*  52 */     this.rows = paramInt2;
/*  53 */     this.columns = paramInt1;
/*  54 */     this.layoutGrid = new Component[paramInt1][paramInt2];
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/*  61 */     buildLayoutGrid(paramContainer);
/*     */ 
/*  63 */     int[] arrayOfInt1 = new int[this.rows];
/*  64 */     int[] arrayOfInt2 = new int[this.columns];
/*     */ 
/*  66 */     for (int i = 0; i < this.rows; i++) {
/*  67 */       arrayOfInt1[i] = computeRowHeight(i);
/*     */     }
/*     */ 
/*  70 */     for (i = 0; i < this.columns; i++) {
/*  71 */       arrayOfInt2[i] = computeColumnWidth(i);
/*     */     }
/*     */ 
/*  75 */     Insets localInsets = paramContainer.getInsets();
/*     */     int j;
/*     */     int k;
/*     */     int m;
/*     */     int n;
/*     */     Component localComponent;
/*  77 */     if (paramContainer.getComponentOrientation().isLeftToRight()) {
/*  78 */       j = localInsets.left;
/*  79 */       for (k = 0; k < this.columns; k++) {
/*  80 */         m = localInsets.top;
/*     */ 
/*  82 */         for (n = 0; n < this.rows; n++) {
/*  83 */           localComponent = this.layoutGrid[k][n];
/*     */ 
/*  85 */           localComponent.setBounds(j, m, arrayOfInt2[k], arrayOfInt1[n]);
/*     */ 
/*  87 */           m += arrayOfInt1[n] + this.yGap;
/*     */         }
/*  89 */         j += arrayOfInt2[k] + this.xGap;
/*     */       }
/*     */     } else {
/*  92 */       j = paramContainer.getWidth() - localInsets.right;
/*  93 */       for (k = 0; k < this.columns; k++) {
/*  94 */         m = localInsets.top;
/*  95 */         j -= arrayOfInt2[k];
/*     */ 
/*  97 */         for (n = 0; n < this.rows; n++) {
/*  98 */           localComponent = this.layoutGrid[k][n];
/*     */ 
/* 100 */           localComponent.setBounds(j, m, arrayOfInt2[k], arrayOfInt1[n]);
/*     */ 
/* 102 */           m += arrayOfInt1[n] + this.yGap;
/*     */         }
/* 104 */         j -= this.xGap;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/* 114 */     buildLayoutGrid(paramContainer);
/* 115 */     Insets localInsets = paramContainer.getInsets();
/*     */ 
/* 119 */     int i = 0;
/* 120 */     int j = 0;
/*     */ 
/* 122 */     for (int k = 0; k < this.rows; k++) {
/* 123 */       i += computeRowHeight(k);
/*     */     }
/*     */ 
/* 126 */     for (k = 0; k < this.columns; k++) {
/* 127 */       j += computeColumnWidth(k);
/*     */     }
/*     */ 
/* 130 */     i += this.yGap * (this.rows - 1) + localInsets.top + localInsets.bottom;
/* 131 */     j += this.xGap * (this.columns - 1) + localInsets.right + localInsets.left;
/*     */ 
/* 133 */     return new Dimension(j, i);
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/* 139 */     return minimumLayoutSize(paramContainer);
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(String paramString, Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component paramComponent) {
/*     */   }
/*     */ 
/*     */   private void buildLayoutGrid(Container paramContainer) {
/* 150 */     Component[] arrayOfComponent = paramContainer.getComponents();
/*     */ 
/* 152 */     for (int i = 0; i < arrayOfComponent.length; i++)
/*     */     {
/* 154 */       int j = 0;
/* 155 */       int k = 0;
/*     */ 
/* 157 */       if (i != 0) {
/* 158 */         k = i % this.columns;
/* 159 */         j = (i - k) / this.columns;
/*     */       }
/*     */ 
/* 164 */       this.layoutGrid[k][j] = arrayOfComponent[i];
/*     */     }
/*     */   }
/*     */ 
/*     */   private int computeColumnWidth(int paramInt) {
/* 169 */     int i = 1;
/* 170 */     for (int j = 0; j < this.rows; j++) {
/* 171 */       int k = this.layoutGrid[paramInt][j].getPreferredSize().width;
/* 172 */       if (k > i) {
/* 173 */         i = k;
/*     */       }
/*     */     }
/* 176 */     return i;
/*     */   }
/*     */ 
/*     */   private int computeRowHeight(int paramInt) {
/* 180 */     int i = 1;
/* 181 */     for (int j = 0; j < this.columns; j++) {
/* 182 */       int k = this.layoutGrid[j][paramInt].getPreferredSize().height;
/* 183 */       if (k > i) {
/* 184 */         i = k;
/*     */       }
/*     */     }
/* 187 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.SmartGridLayout
 * JD-Core Version:    0.6.2
 */