/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.AWTKeyStroke;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Set;
/*     */ import javax.swing.SwingUtilities;
/*     */ 
/*     */ public class ComponentView extends View
/*     */ {
/*     */   private Component createdC;
/*     */   private Invalidator c;
/*     */ 
/*     */   public ComponentView(Element paramElement)
/*     */   {
/*  78 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   protected Component createComponent()
/*     */   {
/*  90 */     AttributeSet localAttributeSet = getElement().getAttributes();
/*  91 */     Component localComponent = StyleConstants.getComponent(localAttributeSet);
/*  92 */     return localComponent;
/*     */   }
/*     */ 
/*     */   public final Component getComponent()
/*     */   {
/*  99 */     return this.createdC;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/* 114 */     if (this.c != null) {
/* 115 */       Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*     */ 
/* 117 */       this.c.setBounds(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt)
/*     */   {
/* 135 */     if ((paramInt != 0) && (paramInt != 1)) {
/* 136 */       throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */     }
/* 138 */     if (this.c != null) {
/* 139 */       Dimension localDimension = this.c.getPreferredSize();
/* 140 */       if (paramInt == 0) {
/* 141 */         return localDimension.width;
/*     */       }
/* 143 */       return localDimension.height;
/*     */     }
/*     */ 
/* 146 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public float getMinimumSpan(int paramInt)
/*     */   {
/* 163 */     if ((paramInt != 0) && (paramInt != 1)) {
/* 164 */       throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */     }
/* 166 */     if (this.c != null) {
/* 167 */       Dimension localDimension = this.c.getMinimumSize();
/* 168 */       if (paramInt == 0) {
/* 169 */         return localDimension.width;
/*     */       }
/* 171 */       return localDimension.height;
/*     */     }
/*     */ 
/* 174 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public float getMaximumSpan(int paramInt)
/*     */   {
/* 191 */     if ((paramInt != 0) && (paramInt != 1)) {
/* 192 */       throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */     }
/* 194 */     if (this.c != null) {
/* 195 */       Dimension localDimension = this.c.getMaximumSize();
/* 196 */       if (paramInt == 0) {
/* 197 */         return localDimension.width;
/*     */       }
/* 199 */       return localDimension.height;
/*     */     }
/*     */ 
/* 202 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public float getAlignment(int paramInt)
/*     */   {
/* 218 */     if (this.c != null) {
/* 219 */       switch (paramInt) {
/*     */       case 0:
/* 221 */         return this.c.getAlignmentX();
/*     */       case 1:
/* 223 */         return this.c.getAlignmentY();
/*     */       }
/*     */     }
/* 226 */     return super.getAlignment(paramInt);
/*     */   }
/*     */ 
/*     */   public void setParent(View paramView)
/*     */   {
/* 252 */     super.setParent(paramView);
/* 253 */     if (SwingUtilities.isEventDispatchThread()) {
/* 254 */       setComponentParent();
/*     */     } else {
/* 256 */       Runnable local1 = new Runnable() {
/*     */         public void run() {
/* 258 */           Document localDocument = ComponentView.this.getDocument();
/*     */           try {
/* 260 */             if ((localDocument instanceof AbstractDocument)) {
/* 261 */               ((AbstractDocument)localDocument).readLock();
/*     */             }
/* 263 */             ComponentView.this.setComponentParent();
/* 264 */             Container localContainer = ComponentView.this.getContainer();
/* 265 */             if (localContainer != null) {
/* 266 */               ComponentView.this.preferenceChanged(null, true, true);
/* 267 */               localContainer.repaint();
/*     */             }
/*     */           } finally {
/* 270 */             if ((localDocument instanceof AbstractDocument))
/* 271 */               ((AbstractDocument)localDocument).readUnlock();
/*     */           }
/*     */         }
/*     */       };
/* 276 */       SwingUtilities.invokeLater(local1);
/*     */     }
/*     */   }
/*     */ 
/*     */   void setComponentParent()
/*     */   {
/* 285 */     View localView = getParent();
/*     */     Container localContainer;
/* 286 */     if (localView != null) {
/* 287 */       localContainer = getContainer();
/* 288 */       if (localContainer != null) {
/* 289 */         if (this.c == null)
/*     */         {
/* 291 */           Component localComponent = createComponent();
/* 292 */           if (localComponent != null) {
/* 293 */             this.createdC = localComponent;
/* 294 */             this.c = new Invalidator(localComponent);
/*     */           }
/*     */         }
/* 297 */         if ((this.c != null) && 
/* 298 */           (this.c.getParent() == null))
/*     */         {
/* 301 */           localContainer.add(this.c, this);
/* 302 */           localContainer.addPropertyChangeListener("enabled", this.c);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/* 307 */     else if (this.c != null) {
/* 308 */       localContainer = this.c.getParent();
/* 309 */       if (localContainer != null)
/*     */       {
/* 311 */         localContainer.remove(this.c);
/* 312 */         localContainer.removePropertyChangeListener("enabled", this.c);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */     throws BadLocationException
/*     */   {
/* 330 */     int i = getStartOffset();
/* 331 */     int j = getEndOffset();
/* 332 */     if ((paramInt >= i) && (paramInt <= j)) {
/* 333 */       Rectangle localRectangle = paramShape.getBounds();
/* 334 */       if (paramInt == j) {
/* 335 */         localRectangle.x += localRectangle.width;
/*     */       }
/* 337 */       localRectangle.width = 0;
/* 338 */       return localRectangle;
/*     */     }
/* 340 */     throw new BadLocationException(paramInt + " not in range " + i + "," + j, paramInt);
/*     */   }
/*     */ 
/*     */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */   {
/* 355 */     Rectangle localRectangle = (Rectangle)paramShape;
/* 356 */     if (paramFloat1 < localRectangle.x + localRectangle.width / 2) {
/* 357 */       paramArrayOfBias[0] = Position.Bias.Forward;
/* 358 */       return getStartOffset();
/*     */     }
/* 360 */     paramArrayOfBias[0] = Position.Bias.Backward;
/* 361 */     return getEndOffset();
/*     */   }
/*     */ 
/*     */   class Invalidator extends Container
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     Dimension min;
/*     */     Dimension pref;
/*     */     Dimension max;
/*     */     float yalign;
/*     */     float xalign;
/*     */ 
/*     */     Invalidator(Component arg2)
/*     */     {
/* 386 */       setLayout(null);
/*     */       Component localComponent;
/* 387 */       add(localComponent);
/* 388 */       cacheChildSizes();
/*     */     }
/*     */ 
/*     */     public void invalidate()
/*     */     {
/* 398 */       super.invalidate();
/* 399 */       if (getParent() != null)
/* 400 */         ComponentView.this.preferenceChanged(null, true, true);
/*     */     }
/*     */ 
/*     */     public void doLayout()
/*     */     {
/* 405 */       cacheChildSizes();
/*     */     }
/*     */ 
/*     */     public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 409 */       super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/* 410 */       if (getComponentCount() > 0) {
/* 411 */         getComponent(0).setSize(paramInt3, paramInt4);
/*     */       }
/* 413 */       cacheChildSizes();
/*     */     }
/*     */ 
/*     */     public void validateIfNecessary() {
/* 417 */       if (!isValid())
/* 418 */         validate();
/*     */     }
/*     */ 
/*     */     private void cacheChildSizes()
/*     */     {
/* 423 */       if (getComponentCount() > 0) {
/* 424 */         Component localComponent = getComponent(0);
/* 425 */         this.min = localComponent.getMinimumSize();
/* 426 */         this.pref = localComponent.getPreferredSize();
/* 427 */         this.max = localComponent.getMaximumSize();
/* 428 */         this.yalign = localComponent.getAlignmentY();
/* 429 */         this.xalign = localComponent.getAlignmentX();
/*     */       } else {
/* 431 */         this.min = (this.pref = this.max = new Dimension(0, 0));
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setVisible(boolean paramBoolean)
/*     */     {
/* 444 */       super.setVisible(paramBoolean);
/* 445 */       if (getComponentCount() > 0)
/* 446 */         getComponent(0).setVisible(paramBoolean);
/*     */     }
/*     */ 
/*     */     public boolean isShowing()
/*     */     {
/* 456 */       return true;
/*     */     }
/*     */ 
/*     */     public Dimension getMinimumSize() {
/* 460 */       validateIfNecessary();
/* 461 */       return this.min;
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize() {
/* 465 */       validateIfNecessary();
/* 466 */       return this.pref;
/*     */     }
/*     */ 
/*     */     public Dimension getMaximumSize() {
/* 470 */       validateIfNecessary();
/* 471 */       return this.max;
/*     */     }
/*     */ 
/*     */     public float getAlignmentX() {
/* 475 */       validateIfNecessary();
/* 476 */       return this.xalign;
/*     */     }
/*     */ 
/*     */     public float getAlignmentY() {
/* 480 */       validateIfNecessary();
/* 481 */       return this.yalign;
/*     */     }
/*     */ 
/*     */     public Set<AWTKeyStroke> getFocusTraversalKeys(int paramInt) {
/* 485 */       return KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(paramInt);
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 490 */       Boolean localBoolean = (Boolean)paramPropertyChangeEvent.getNewValue();
/* 491 */       if (getComponentCount() > 0)
/* 492 */         getComponent(0).setEnabled(localBoolean.booleanValue());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.ComponentView
 * JD-Core Version:    0.6.2
 */