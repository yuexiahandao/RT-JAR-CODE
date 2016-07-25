/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ 
/*     */ public class HorizBagLayout
/*     */   implements LayoutManager
/*     */ {
/*     */   int hgap;
/*     */ 
/*     */   public HorizBagLayout()
/*     */   {
/*  44 */     this(0);
/*     */   }
/*     */ 
/*     */   public HorizBagLayout(int paramInt)
/*     */   {
/*  52 */     this.hgap = paramInt;
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(String paramString, Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/*  78 */     Dimension localDimension1 = new Dimension();
/*     */ 
/*  80 */     for (int i = 0; i < paramContainer.countComponents(); i++) {
/*  81 */       Component localComponent = paramContainer.getComponent(i);
/*  82 */       if (localComponent.isVisible()) {
/*  83 */         Dimension localDimension2 = localComponent.minimumSize();
/*  84 */         localDimension1.width += localDimension2.width + this.hgap;
/*  85 */         localDimension1.height = Math.max(localDimension2.height, localDimension1.height);
/*     */       }
/*     */     }
/*     */ 
/*  89 */     Insets localInsets = paramContainer.insets();
/*  90 */     localDimension1.width += localInsets.left + localInsets.right;
/*  91 */     localDimension1.height += localInsets.top + localInsets.bottom;
/*     */ 
/*  93 */     return localDimension1;
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/* 104 */     Dimension localDimension1 = new Dimension();
/*     */ 
/* 106 */     for (int i = 0; i < paramContainer.countComponents(); i++) {
/* 107 */       Component localComponent = paramContainer.getComponent(i);
/* 108 */       if (localComponent.isVisible()) {
/* 109 */         Dimension localDimension2 = localComponent.preferredSize();
/* 110 */         localDimension1.width += localDimension2.width + this.hgap;
/* 111 */         localDimension1.height = Math.max(localDimension1.height, localDimension2.height);
/*     */       }
/*     */     }
/*     */ 
/* 115 */     Insets localInsets = paramContainer.insets();
/* 116 */     localDimension1.width += localInsets.left + localInsets.right;
/* 117 */     localDimension1.height += localInsets.top + localInsets.bottom;
/*     */ 
/* 119 */     return localDimension1;
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 130 */     Insets localInsets = paramContainer.insets();
/* 131 */     int i = localInsets.top;
/* 132 */     int j = paramContainer.size().height - localInsets.bottom;
/* 133 */     int k = localInsets.left;
/* 134 */     int m = paramContainer.size().width - localInsets.right;
/*     */ 
/* 136 */     for (int n = 0; n < paramContainer.countComponents(); n++) {
/* 137 */       Component localComponent = paramContainer.getComponent(n);
/* 138 */       if (localComponent.isVisible()) {
/* 139 */         int i1 = localComponent.size().width;
/* 140 */         localComponent.resize(i1, j - i);
/* 141 */         Dimension localDimension = localComponent.preferredSize();
/* 142 */         localComponent.reshape(k, i, localDimension.width, j - i);
/* 143 */         k += localDimension.width + this.hgap;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 152 */     return getClass().getName() + "[hgap=" + this.hgap + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.HorizBagLayout
 * JD-Core Version:    0.6.2
 */