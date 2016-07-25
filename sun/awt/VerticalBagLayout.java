/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ 
/*     */ public class VerticalBagLayout
/*     */   implements LayoutManager
/*     */ {
/*     */   int vgap;
/*     */ 
/*     */   public VerticalBagLayout()
/*     */   {
/*  45 */     this(0);
/*     */   }
/*     */ 
/*     */   public VerticalBagLayout(int paramInt)
/*     */   {
/*  53 */     this.vgap = paramInt;
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
/*  79 */     Dimension localDimension1 = new Dimension();
/*  80 */     int i = paramContainer.countComponents();
/*     */ 
/*  82 */     for (int j = 0; j < i; j++) {
/*  83 */       Component localComponent = paramContainer.getComponent(j);
/*  84 */       if (localComponent.isVisible()) {
/*  85 */         Dimension localDimension2 = localComponent.minimumSize();
/*  86 */         localDimension1.width = Math.max(localDimension2.width, localDimension1.width);
/*  87 */         localDimension1.height += localDimension2.height + this.vgap;
/*     */       }
/*     */     }
/*     */ 
/*  91 */     Insets localInsets = paramContainer.insets();
/*  92 */     localDimension1.width += localInsets.left + localInsets.right;
/*  93 */     localDimension1.height += localInsets.top + localInsets.bottom;
/*     */ 
/*  95 */     return localDimension1;
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/* 106 */     Dimension localDimension1 = new Dimension();
/* 107 */     int i = paramContainer.countComponents();
/*     */ 
/* 109 */     for (int j = 0; j < i; j++) {
/* 110 */       Component localComponent = paramContainer.getComponent(j);
/*     */ 
/* 112 */       Dimension localDimension2 = localComponent.preferredSize();
/* 113 */       localDimension1.width = Math.max(localDimension2.width, localDimension1.width);
/* 114 */       localDimension1.height += localDimension2.height + this.vgap;
/*     */     }
/*     */ 
/* 118 */     Insets localInsets = paramContainer.insets();
/* 119 */     localDimension1.width += localInsets.left + localInsets.right;
/* 120 */     localDimension1.height += localInsets.top + localInsets.bottom;
/*     */ 
/* 122 */     return localDimension1;
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 133 */     Insets localInsets = paramContainer.insets();
/* 134 */     int i = localInsets.top;
/* 135 */     int j = paramContainer.size().height - localInsets.bottom;
/* 136 */     int k = localInsets.left;
/* 137 */     int m = paramContainer.size().width - localInsets.right;
/* 138 */     int n = paramContainer.countComponents();
/*     */ 
/* 140 */     for (int i1 = 0; i1 < n; i1++) {
/* 141 */       Component localComponent = paramContainer.getComponent(i1);
/* 142 */       if (localComponent.isVisible()) {
/* 143 */         int i2 = localComponent.size().height;
/* 144 */         localComponent.resize(m - k, i2);
/* 145 */         Dimension localDimension = localComponent.preferredSize();
/* 146 */         localComponent.reshape(k, i, m - k, localDimension.height);
/* 147 */         i += localDimension.height + this.vgap;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 156 */     return getClass().getName() + "[vgap=" + this.vgap + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.VerticalBagLayout
 * JD-Core Version:    0.6.2
 */