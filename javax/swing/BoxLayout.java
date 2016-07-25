/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.AWTError;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager2;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class BoxLayout
/*     */   implements LayoutManager2, Serializable
/*     */ {
/*     */   public static final int X_AXIS = 0;
/*     */   public static final int Y_AXIS = 1;
/*     */   public static final int LINE_AXIS = 2;
/*     */   public static final int PAGE_AXIS = 3;
/*     */   private int axis;
/*     */   private Container target;
/*     */   private transient SizeRequirements[] xChildren;
/*     */   private transient SizeRequirements[] yChildren;
/*     */   private transient SizeRequirements xTotal;
/*     */   private transient SizeRequirements yTotal;
/*     */   private transient PrintStream dbg;
/*     */ 
/*     */   @ConstructorProperties({"target", "axis"})
/*     */   public BoxLayout(Container paramContainer, int paramInt)
/*     */   {
/* 179 */     if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2) && (paramInt != 3))
/*     */     {
/* 181 */       throw new AWTError("Invalid axis");
/*     */     }
/* 183 */     this.axis = paramInt;
/* 184 */     this.target = paramContainer;
/*     */   }
/*     */ 
/*     */   BoxLayout(Container paramContainer, int paramInt, PrintStream paramPrintStream)
/*     */   {
/* 202 */     this(paramContainer, paramInt);
/* 203 */     this.dbg = paramPrintStream;
/*     */   }
/*     */ 
/*     */   public final Container getTarget()
/*     */   {
/* 214 */     return this.target;
/*     */   }
/*     */ 
/*     */   public final int getAxis()
/*     */   {
/* 230 */     return this.axis;
/*     */   }
/*     */ 
/*     */   public synchronized void invalidateLayout(Container paramContainer)
/*     */   {
/* 248 */     checkContainer(paramContainer);
/* 249 */     this.xChildren = null;
/* 250 */     this.yChildren = null;
/* 251 */     this.xTotal = null;
/* 252 */     this.yTotal = null;
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(String paramString, Component paramComponent)
/*     */   {
/* 262 */     invalidateLayout(paramComponent.getParent());
/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component paramComponent)
/*     */   {
/* 271 */     invalidateLayout(paramComponent.getParent());
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(Component paramComponent, Object paramObject)
/*     */   {
/* 281 */     invalidateLayout(paramComponent.getParent());
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/*     */     Dimension localDimension;
/* 298 */     synchronized (this) {
/* 299 */       checkContainer(paramContainer);
/* 300 */       checkRequests();
/* 301 */       localDimension = new Dimension(this.xTotal.preferred, this.yTotal.preferred);
/*     */     }
/*     */ 
/* 304 */     ??? = paramContainer.getInsets();
/* 305 */     localDimension.width = ((int)Math.min(localDimension.width + ((Insets)???).left + ((Insets)???).right, 2147483647L));
/* 306 */     localDimension.height = ((int)Math.min(localDimension.height + ((Insets)???).top + ((Insets)???).bottom, 2147483647L));
/* 307 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/*     */     Dimension localDimension;
/* 323 */     synchronized (this) {
/* 324 */       checkContainer(paramContainer);
/* 325 */       checkRequests();
/* 326 */       localDimension = new Dimension(this.xTotal.minimum, this.yTotal.minimum);
/*     */     }
/*     */ 
/* 329 */     ??? = paramContainer.getInsets();
/* 330 */     localDimension.width = ((int)Math.min(localDimension.width + ((Insets)???).left + ((Insets)???).right, 2147483647L));
/* 331 */     localDimension.height = ((int)Math.min(localDimension.height + ((Insets)???).top + ((Insets)???).bottom, 2147483647L));
/* 332 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension maximumLayoutSize(Container paramContainer)
/*     */   {
/*     */     Dimension localDimension;
/* 348 */     synchronized (this) {
/* 349 */       checkContainer(paramContainer);
/* 350 */       checkRequests();
/* 351 */       localDimension = new Dimension(this.xTotal.maximum, this.yTotal.maximum);
/*     */     }
/*     */ 
/* 354 */     ??? = paramContainer.getInsets();
/* 355 */     localDimension.width = ((int)Math.min(localDimension.width + ((Insets)???).left + ((Insets)???).right, 2147483647L));
/* 356 */     localDimension.height = ((int)Math.min(localDimension.height + ((Insets)???).top + ((Insets)???).bottom, 2147483647L));
/* 357 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public synchronized float getLayoutAlignmentX(Container paramContainer)
/*     */   {
/* 372 */     checkContainer(paramContainer);
/* 373 */     checkRequests();
/* 374 */     return this.xTotal.alignment;
/*     */   }
/*     */ 
/*     */   public synchronized float getLayoutAlignmentY(Container paramContainer)
/*     */   {
/* 389 */     checkContainer(paramContainer);
/* 390 */     checkRequests();
/* 391 */     return this.yTotal.alignment;
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 404 */     checkContainer(paramContainer);
/* 405 */     Object localObject1 = paramContainer.getComponentCount();
/* 406 */     int[] arrayOfInt1 = new int[localObject1];
/* 407 */     int[] arrayOfInt2 = new int[localObject1];
/* 408 */     int[] arrayOfInt3 = new int[localObject1];
/* 409 */     int[] arrayOfInt4 = new int[localObject1];
/*     */ 
/* 411 */     Dimension localDimension = paramContainer.getSize();
/* 412 */     Insets localInsets = paramContainer.getInsets();
/* 413 */     localDimension.width -= localInsets.left + localInsets.right;
/* 414 */     localDimension.height -= localInsets.top + localInsets.bottom;
/*     */ 
/* 417 */     ComponentOrientation localComponentOrientation = paramContainer.getComponentOrientation();
/* 418 */     int i = resolveAxis(this.axis, localComponentOrientation);
/* 419 */     boolean bool = i != this.axis ? localComponentOrientation.isLeftToRight() : true;
/*     */ 
/* 423 */     synchronized (this) {
/* 424 */       checkRequests();
/*     */ 
/* 426 */       if (i == 0) {
/* 427 */         SizeRequirements.calculateTiledPositions(localDimension.width, this.xTotal, this.xChildren, arrayOfInt1, arrayOfInt2, bool);
/*     */ 
/* 430 */         SizeRequirements.calculateAlignedPositions(localDimension.height, this.yTotal, this.yChildren, arrayOfInt3, arrayOfInt4);
/*     */       }
/*     */       else
/*     */       {
/* 434 */         SizeRequirements.calculateAlignedPositions(localDimension.width, this.xTotal, this.xChildren, arrayOfInt1, arrayOfInt2, bool);
/*     */ 
/* 437 */         SizeRequirements.calculateTiledPositions(localDimension.height, this.yTotal, this.yChildren, arrayOfInt3, arrayOfInt4);
/*     */       }
/*     */     }
/*     */     Component localComponent;
/* 444 */     for (??? = 0; ??? < localObject1; ???++) {
/* 445 */       localComponent = paramContainer.getComponent(???);
/* 446 */       localComponent.setBounds((int)Math.min(localInsets.left + arrayOfInt1[???], 2147483647L), (int)Math.min(localInsets.top + arrayOfInt3[???], 2147483647L), arrayOfInt2[???], arrayOfInt4[???]);
/*     */     }
/*     */ 
/* 451 */     if (this.dbg != null)
/* 452 */       for (??? = 0; ??? < localObject1; ???++) {
/* 453 */         localComponent = paramContainer.getComponent(???);
/* 454 */         this.dbg.println(localComponent.toString());
/* 455 */         this.dbg.println("X: " + this.xChildren[???]);
/* 456 */         this.dbg.println("Y: " + this.yChildren[???]);
/*     */       }
/*     */   }
/*     */ 
/*     */   void checkContainer(Container paramContainer)
/*     */   {
/* 463 */     if (this.target != paramContainer)
/* 464 */       throw new AWTError("BoxLayout can't be shared");
/*     */   }
/*     */ 
/*     */   void checkRequests()
/*     */   {
/* 469 */     if ((this.xChildren == null) || (this.yChildren == null))
/*     */     {
/* 472 */       int i = this.target.getComponentCount();
/* 473 */       this.xChildren = new SizeRequirements[i];
/* 474 */       this.yChildren = new SizeRequirements[i];
/* 475 */       for (int j = 0; j < i; j++) {
/* 476 */         Component localComponent = this.target.getComponent(j);
/* 477 */         if (!localComponent.isVisible()) {
/* 478 */           this.xChildren[j] = new SizeRequirements(0, 0, 0, localComponent.getAlignmentX());
/* 479 */           this.yChildren[j] = new SizeRequirements(0, 0, 0, localComponent.getAlignmentY());
/*     */         }
/*     */         else {
/* 482 */           Dimension localDimension1 = localComponent.getMinimumSize();
/* 483 */           Dimension localDimension2 = localComponent.getPreferredSize();
/* 484 */           Dimension localDimension3 = localComponent.getMaximumSize();
/* 485 */           this.xChildren[j] = new SizeRequirements(localDimension1.width, localDimension2.width, localDimension3.width, localComponent.getAlignmentX());
/*     */ 
/* 488 */           this.yChildren[j] = new SizeRequirements(localDimension1.height, localDimension2.height, localDimension3.height, localComponent.getAlignmentY());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 494 */       j = resolveAxis(this.axis, this.target.getComponentOrientation());
/*     */ 
/* 496 */       if (j == 0) {
/* 497 */         this.xTotal = SizeRequirements.getTiledSizeRequirements(this.xChildren);
/* 498 */         this.yTotal = SizeRequirements.getAlignedSizeRequirements(this.yChildren);
/*     */       } else {
/* 500 */         this.xTotal = SizeRequirements.getAlignedSizeRequirements(this.xChildren);
/* 501 */         this.yTotal = SizeRequirements.getTiledSizeRequirements(this.yChildren);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int resolveAxis(int paramInt, ComponentOrientation paramComponentOrientation)
/*     */   {
/*     */     int i;
/* 518 */     if (paramInt == 2)
/* 519 */       i = paramComponentOrientation.isHorizontal() ? 0 : 1;
/* 520 */     else if (paramInt == 3)
/* 521 */       i = paramComponentOrientation.isHorizontal() ? 1 : 0;
/*     */     else {
/* 523 */       i = paramInt;
/*     */     }
/* 525 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.BoxLayout
 * JD-Core Version:    0.6.2
 */