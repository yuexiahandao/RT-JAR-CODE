/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.AWTError;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager2;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class OverlayLayout
/*     */   implements LayoutManager2, Serializable
/*     */ {
/*     */   private Container target;
/*     */   private SizeRequirements[] xChildren;
/*     */   private SizeRequirements[] yChildren;
/*     */   private SizeRequirements xTotal;
/*     */   private SizeRequirements yTotal;
/*     */ 
/*     */   @ConstructorProperties({"target"})
/*     */   public OverlayLayout(Container paramContainer)
/*     */   {
/*  65 */     this.target = paramContainer;
/*     */   }
/*     */ 
/*     */   public final Container getTarget()
/*     */   {
/*  76 */     return this.target;
/*     */   }
/*     */ 
/*     */   public void invalidateLayout(Container paramContainer)
/*     */   {
/*  86 */     checkContainer(paramContainer);
/*  87 */     this.xChildren = null;
/*  88 */     this.yChildren = null;
/*  89 */     this.xTotal = null;
/*  90 */     this.yTotal = null;
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(String paramString, Component paramComponent)
/*     */   {
/* 101 */     invalidateLayout(paramComponent.getParent());
/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component paramComponent)
/*     */   {
/* 111 */     invalidateLayout(paramComponent.getParent());
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(Component paramComponent, Object paramObject)
/*     */   {
/* 123 */     invalidateLayout(paramComponent.getParent());
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/* 137 */     checkContainer(paramContainer);
/* 138 */     checkRequests();
/*     */ 
/* 140 */     Dimension localDimension = new Dimension(this.xTotal.preferred, this.yTotal.preferred);
/* 141 */     Insets localInsets = paramContainer.getInsets();
/* 142 */     localDimension.width += localInsets.left + localInsets.right;
/* 143 */     localDimension.height += localInsets.top + localInsets.bottom;
/* 144 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/* 157 */     checkContainer(paramContainer);
/* 158 */     checkRequests();
/*     */ 
/* 160 */     Dimension localDimension = new Dimension(this.xTotal.minimum, this.yTotal.minimum);
/* 161 */     Insets localInsets = paramContainer.getInsets();
/* 162 */     localDimension.width += localInsets.left + localInsets.right;
/* 163 */     localDimension.height += localInsets.top + localInsets.bottom;
/* 164 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension maximumLayoutSize(Container paramContainer)
/*     */   {
/* 179 */     checkContainer(paramContainer);
/* 180 */     checkRequests();
/*     */ 
/* 182 */     Dimension localDimension = new Dimension(this.xTotal.maximum, this.yTotal.maximum);
/* 183 */     Insets localInsets = paramContainer.getInsets();
/* 184 */     localDimension.width += localInsets.left + localInsets.right;
/* 185 */     localDimension.height += localInsets.top + localInsets.bottom;
/* 186 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public float getLayoutAlignmentX(Container paramContainer)
/*     */   {
/* 196 */     checkContainer(paramContainer);
/* 197 */     checkRequests();
/* 198 */     return this.xTotal.alignment;
/*     */   }
/*     */ 
/*     */   public float getLayoutAlignmentY(Container paramContainer)
/*     */   {
/* 208 */     checkContainer(paramContainer);
/* 209 */     checkRequests();
/* 210 */     return this.yTotal.alignment;
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 222 */     checkContainer(paramContainer);
/* 223 */     checkRequests();
/*     */ 
/* 225 */     int i = paramContainer.getComponentCount();
/* 226 */     int[] arrayOfInt1 = new int[i];
/* 227 */     int[] arrayOfInt2 = new int[i];
/* 228 */     int[] arrayOfInt3 = new int[i];
/* 229 */     int[] arrayOfInt4 = new int[i];
/*     */ 
/* 232 */     Dimension localDimension = paramContainer.getSize();
/* 233 */     Insets localInsets = paramContainer.getInsets();
/* 234 */     localDimension.width -= localInsets.left + localInsets.right;
/* 235 */     localDimension.height -= localInsets.top + localInsets.bottom;
/* 236 */     SizeRequirements.calculateAlignedPositions(localDimension.width, this.xTotal, this.xChildren, arrayOfInt1, arrayOfInt2);
/*     */ 
/* 239 */     SizeRequirements.calculateAlignedPositions(localDimension.height, this.yTotal, this.yChildren, arrayOfInt3, arrayOfInt4);
/*     */ 
/* 244 */     for (int j = 0; j < i; j++) {
/* 245 */       Component localComponent = paramContainer.getComponent(j);
/* 246 */       localComponent.setBounds(localInsets.left + arrayOfInt1[j], localInsets.top + arrayOfInt3[j], arrayOfInt2[j], arrayOfInt4[j]);
/*     */     }
/*     */   }
/*     */ 
/*     */   void checkContainer(Container paramContainer)
/*     */   {
/* 252 */     if (this.target != paramContainer)
/* 253 */       throw new AWTError("OverlayLayout can't be shared");
/*     */   }
/*     */ 
/*     */   void checkRequests()
/*     */   {
/* 258 */     if ((this.xChildren == null) || (this.yChildren == null))
/*     */     {
/* 261 */       int i = this.target.getComponentCount();
/* 262 */       this.xChildren = new SizeRequirements[i];
/* 263 */       this.yChildren = new SizeRequirements[i];
/* 264 */       for (int j = 0; j < i; j++) {
/* 265 */         Component localComponent = this.target.getComponent(j);
/* 266 */         Dimension localDimension1 = localComponent.getMinimumSize();
/* 267 */         Dimension localDimension2 = localComponent.getPreferredSize();
/* 268 */         Dimension localDimension3 = localComponent.getMaximumSize();
/* 269 */         this.xChildren[j] = new SizeRequirements(localDimension1.width, localDimension2.width, localDimension3.width, localComponent.getAlignmentX());
/*     */ 
/* 272 */         this.yChildren[j] = new SizeRequirements(localDimension1.height, localDimension2.height, localDimension3.height, localComponent.getAlignmentY());
/*     */       }
/*     */ 
/* 277 */       this.xTotal = SizeRequirements.getAlignedSizeRequirements(this.xChildren);
/* 278 */       this.yTotal = SizeRequirements.getAlignedSizeRequirements(this.yChildren);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.OverlayLayout
 * JD-Core Version:    0.6.2
 */