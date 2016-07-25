/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Insets;
/*     */ 
/*     */ public class OrientableFlowLayout extends FlowLayout
/*     */ {
/*     */   public static final int HORIZONTAL = 0;
/*     */   public static final int VERTICAL = 1;
/*     */   public static final int TOP = 0;
/*     */   public static final int BOTTOM = 2;
/*     */   int orientation;
/*     */   int vAlign;
/*     */   int vHGap;
/*     */   int vVGap;
/*     */ 
/*     */   public OrientableFlowLayout()
/*     */   {
/*  70 */     this(0, 1, 1, 5, 5, 5, 5);
/*     */   }
/*     */ 
/*     */   public OrientableFlowLayout(int paramInt)
/*     */   {
/*  80 */     this(paramInt, 1, 1, 5, 5, 5, 5);
/*     */   }
/*     */ 
/*     */   public OrientableFlowLayout(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  92 */     this(paramInt1, paramInt2, paramInt3, 5, 5, 5, 5);
/*     */   }
/*     */ 
/*     */   public OrientableFlowLayout(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */   {
/* 108 */     super(paramInt2, paramInt4, paramInt5);
/* 109 */     this.orientation = paramInt1;
/* 110 */     this.vAlign = paramInt3;
/* 111 */     this.vHGap = paramInt6;
/* 112 */     this.vVGap = paramInt7;
/*     */   }
/*     */ 
/*     */   public synchronized void orientHorizontally()
/*     */   {
/* 119 */     this.orientation = 0;
/*     */   }
/*     */ 
/*     */   public synchronized void orientVertically()
/*     */   {
/* 126 */     this.orientation = 1;
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/* 139 */     if (this.orientation == 0) {
/* 140 */       return super.preferredLayoutSize(paramContainer);
/*     */     }
/*     */ 
/* 143 */     Dimension localDimension1 = new Dimension(0, 0);
/*     */ 
/* 145 */     int i = paramContainer.countComponents();
/* 146 */     for (int j = 0; j < i; j++) {
/* 147 */       Component localComponent = paramContainer.getComponent(j);
/* 148 */       if (localComponent.isVisible()) {
/* 149 */         Dimension localDimension2 = localComponent.preferredSize();
/* 150 */         localDimension1.width = Math.max(localDimension1.width, localDimension2.width);
/* 151 */         if (j > 0) {
/* 152 */           localDimension1.height += this.vVGap;
/*     */         }
/* 154 */         localDimension1.height += localDimension2.height;
/*     */       }
/*     */     }
/*     */ 
/* 158 */     Insets localInsets = paramContainer.insets();
/* 159 */     localDimension1.width += localInsets.left + localInsets.right + this.vHGap * 2;
/* 160 */     localDimension1.height += localInsets.top + localInsets.bottom + this.vVGap * 2;
/*     */ 
/* 162 */     return localDimension1;
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/* 174 */     if (this.orientation == 0) {
/* 175 */       return super.minimumLayoutSize(paramContainer);
/*     */     }
/*     */ 
/* 178 */     Dimension localDimension1 = new Dimension(0, 0);
/*     */ 
/* 180 */     int i = paramContainer.countComponents();
/* 181 */     for (int j = 0; j < i; j++) {
/* 182 */       Component localComponent = paramContainer.getComponent(j);
/* 183 */       if (localComponent.isVisible()) {
/* 184 */         Dimension localDimension2 = localComponent.minimumSize();
/* 185 */         localDimension1.width = Math.max(localDimension1.width, localDimension2.width);
/* 186 */         if (j > 0) {
/* 187 */           localDimension1.height += this.vVGap;
/*     */         }
/* 189 */         localDimension1.height += localDimension2.height;
/*     */       }
/*     */     }
/*     */ 
/* 193 */     Insets localInsets = paramContainer.insets();
/* 194 */     localDimension1.width += localInsets.left + localInsets.right + this.vHGap * 2;
/* 195 */     localDimension1.height += localInsets.top + localInsets.bottom + this.vVGap * 2;
/*     */ 
/* 197 */     return localDimension1;
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 210 */     if (this.orientation == 0) {
/* 211 */       super.layoutContainer(paramContainer);
/*     */     }
/*     */     else {
/* 214 */       Insets localInsets = paramContainer.insets();
/* 215 */       Dimension localDimension1 = paramContainer.size();
/* 216 */       int i = localDimension1.height - (localInsets.top + localInsets.bottom + this.vVGap * 2);
/* 217 */       int j = localInsets.left + this.vHGap;
/* 218 */       int k = 0;
/* 219 */       int m = 0;
/* 220 */       int n = 0;
/*     */ 
/* 222 */       int i1 = paramContainer.countComponents();
/* 223 */       for (int i2 = 0; i2 < i1; i2++) {
/* 224 */         Component localComponent = paramContainer.getComponent(i2);
/* 225 */         if (localComponent.isVisible()) {
/* 226 */           Dimension localDimension2 = localComponent.preferredSize();
/* 227 */           localComponent.resize(localDimension2.width, localDimension2.height);
/*     */ 
/* 229 */           if ((k == 0) || (k + localDimension2.height <= i)) {
/* 230 */             if (k > 0) {
/* 231 */               k += this.vVGap;
/*     */             }
/* 233 */             k += localDimension2.height;
/* 234 */             m = Math.max(m, localDimension2.width);
/*     */           }
/*     */           else {
/* 237 */             moveComponents(paramContainer, j, localInsets.top + this.vVGap, m, i - k, n, i2);
/*     */ 
/* 244 */             j += this.vHGap + m;
/* 245 */             k = localDimension2.width;
/* 246 */             m = localDimension2.width;
/* 247 */             n = i2;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 252 */       moveComponents(paramContainer, j, localInsets.top + this.vVGap, m, i - k, n, i1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void moveComponents(Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 274 */     switch (this.vAlign) {
/*     */     case 0:
/* 276 */       break;
/*     */     case 1:
/* 278 */       paramInt2 += paramInt4 / 2;
/* 279 */       break;
/*     */     case 2:
/* 281 */       paramInt2 += paramInt4;
/*     */     }
/*     */ 
/* 284 */     for (int i = paramInt5; i < paramInt6; i++) {
/* 285 */       Component localComponent = paramContainer.getComponent(i);
/* 286 */       Dimension localDimension = localComponent.size();
/* 287 */       if (localComponent.isVisible()) {
/* 288 */         localComponent.move(paramInt1 + (paramInt3 - localDimension.width) / 2, paramInt2);
/* 289 */         paramInt2 += this.vVGap + localDimension.height;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 298 */     String str = "";
/* 299 */     switch (this.orientation) {
/*     */     case 0:
/* 301 */       str = "orientation=horizontal, ";
/* 302 */       break;
/*     */     case 1:
/* 304 */       str = "orientation=vertical, ";
/*     */     }
/*     */ 
/* 308 */     return getClass().getName() + "[" + str + super.toString() + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.OrientableFlowLayout
 * JD-Core Version:    0.6.2
 */