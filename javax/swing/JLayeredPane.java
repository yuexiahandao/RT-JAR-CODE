/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class JLayeredPane extends JComponent
/*     */   implements Accessible
/*     */ {
/* 160 */   public static final Integer DEFAULT_LAYER = new Integer(0);
/*     */ 
/* 162 */   public static final Integer PALETTE_LAYER = new Integer(100);
/*     */ 
/* 164 */   public static final Integer MODAL_LAYER = new Integer(200);
/*     */ 
/* 166 */   public static final Integer POPUP_LAYER = new Integer(300);
/*     */ 
/* 168 */   public static final Integer DRAG_LAYER = new Integer(400);
/*     */ 
/* 175 */   public static final Integer FRAME_CONTENT_LAYER = new Integer(-30000);
/*     */   public static final String LAYER_PROPERTY = "layeredContainerLayer";
/*     */   private Hashtable<Component, Integer> componentToLayer;
/* 181 */   private boolean optimizedDrawingPossible = true;
/*     */ 
/*     */   public JLayeredPane()
/*     */   {
/* 189 */     setLayout(null);
/*     */   }
/*     */ 
/*     */   private void validateOptimizedDrawing() {
/* 193 */     int i = 0;
/* 194 */     synchronized (getTreeLock())
/*     */     {
/* 197 */       for (Component localComponent : getComponents()) {
/* 198 */         Integer localInteger = null;
/*     */ 
/* 200 */         if ((SunToolkit.isInstanceOf(localComponent, "javax.swing.JInternalFrame")) || (((localComponent instanceof JComponent)) && ((localInteger = (Integer)((JComponent)localComponent).getClientProperty("layeredContainerLayer")) != null)))
/*     */         {
/* 205 */           if ((localInteger == null) || (!localInteger.equals(FRAME_CONTENT_LAYER)))
/*     */           {
/* 207 */             i = 1;
/* 208 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 213 */     if (i != 0)
/* 214 */       this.optimizedDrawingPossible = false;
/*     */     else
/* 216 */       this.optimizedDrawingPossible = true;
/*     */   }
/*     */ 
/*     */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*     */   {
/*     */     int i;
/* 223 */     if ((paramObject instanceof Integer)) {
/* 224 */       i = ((Integer)paramObject).intValue();
/* 225 */       setLayer(paramComponent, i);
/*     */     } else {
/* 227 */       i = getLayer(paramComponent);
/*     */     }
/* 229 */     int j = insertIndexForLayer(i, paramInt);
/* 230 */     super.addImpl(paramComponent, paramObject, j);
/* 231 */     paramComponent.validate();
/* 232 */     paramComponent.repaint();
/* 233 */     validateOptimizedDrawing();
/*     */   }
/*     */ 
/*     */   public void remove(int paramInt)
/*     */   {
/* 244 */     Component localComponent = getComponent(paramInt);
/* 245 */     super.remove(paramInt);
/* 246 */     if ((localComponent != null) && (!(localComponent instanceof JComponent))) {
/* 247 */       getComponentToLayer().remove(localComponent);
/*     */     }
/* 249 */     validateOptimizedDrawing();
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 258 */     Component[] arrayOfComponent = getComponents();
/* 259 */     Hashtable localHashtable = getComponentToLayer();
/* 260 */     for (int i = arrayOfComponent.length - 1; i >= 0; i--) {
/* 261 */       Component localComponent = arrayOfComponent[i];
/* 262 */       if ((localComponent != null) && (!(localComponent instanceof JComponent))) {
/* 263 */         localHashtable.remove(localComponent);
/*     */       }
/*     */     }
/* 266 */     super.removeAll();
/*     */   }
/*     */ 
/*     */   public boolean isOptimizedDrawingEnabled()
/*     */   {
/* 277 */     return this.optimizedDrawingPossible;
/*     */   }
/*     */ 
/*     */   public static void putLayer(JComponent paramJComponent, int paramInt)
/*     */   {
/* 297 */     Integer localInteger = new Integer(paramInt);
/* 298 */     paramJComponent.putClientProperty("layeredContainerLayer", localInteger);
/*     */   }
/*     */ 
/*     */   public static int getLayer(JComponent paramJComponent)
/*     */   {
/*     */     Integer localInteger;
/* 310 */     if ((localInteger = (Integer)paramJComponent.getClientProperty("layeredContainerLayer")) != null)
/* 311 */       return localInteger.intValue();
/* 312 */     return DEFAULT_LAYER.intValue();
/*     */   }
/*     */ 
/*     */   public static JLayeredPane getLayeredPaneAbove(Component paramComponent)
/*     */   {
/* 328 */     if (paramComponent == null) return null;
/*     */ 
/* 330 */     Container localContainer = paramComponent.getParent();
/* 331 */     while ((localContainer != null) && (!(localContainer instanceof JLayeredPane)))
/* 332 */       localContainer = localContainer.getParent();
/* 333 */     return (JLayeredPane)localContainer;
/*     */   }
/*     */ 
/*     */   public void setLayer(Component paramComponent, int paramInt)
/*     */   {
/* 345 */     setLayer(paramComponent, paramInt, -1);
/*     */   }
/*     */ 
/*     */   public void setLayer(Component paramComponent, int paramInt1, int paramInt2)
/*     */   {
/* 360 */     Integer localInteger = getObjectForLayer(paramInt1);
/*     */ 
/* 362 */     if ((paramInt1 == getLayer(paramComponent)) && (paramInt2 == getPosition(paramComponent))) {
/* 363 */       repaint(paramComponent.getBounds());
/* 364 */       return;
/*     */     }
/*     */ 
/* 368 */     if ((paramComponent instanceof JComponent))
/* 369 */       ((JComponent)paramComponent).putClientProperty("layeredContainerLayer", localInteger);
/*     */     else {
/* 371 */       getComponentToLayer().put(paramComponent, localInteger);
/*     */     }
/* 373 */     if ((paramComponent.getParent() == null) || (paramComponent.getParent() != this)) {
/* 374 */       repaint(paramComponent.getBounds());
/* 375 */       return;
/*     */     }
/*     */ 
/* 378 */     int i = insertIndexForLayer(paramComponent, paramInt1, paramInt2);
/*     */ 
/* 380 */     setComponentZOrder(paramComponent, i);
/* 381 */     repaint(paramComponent.getBounds());
/*     */   }
/*     */ 
/*     */   public int getLayer(Component paramComponent)
/*     */   {
/*     */     Integer localInteger;
/* 392 */     if ((paramComponent instanceof JComponent))
/* 393 */       localInteger = (Integer)((JComponent)paramComponent).getClientProperty("layeredContainerLayer");
/*     */     else {
/* 395 */       localInteger = (Integer)getComponentToLayer().get(paramComponent);
/*     */     }
/* 397 */     if (localInteger == null)
/* 398 */       return DEFAULT_LAYER.intValue();
/* 399 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public int getIndexOf(Component paramComponent)
/*     */   {
/* 414 */     int j = getComponentCount();
/* 415 */     for (int i = 0; i < j; i++) {
/* 416 */       if (paramComponent == getComponent(i))
/* 417 */         return i;
/*     */     }
/* 419 */     return -1;
/*     */   }
/*     */ 
/*     */   public void moveToFront(Component paramComponent)
/*     */   {
/* 429 */     setPosition(paramComponent, 0);
/*     */   }
/*     */ 
/*     */   public void moveToBack(Component paramComponent)
/*     */   {
/* 440 */     setPosition(paramComponent, -1);
/*     */   }
/*     */ 
/*     */   public void setPosition(Component paramComponent, int paramInt)
/*     */   {
/* 458 */     setLayer(paramComponent, getLayer(paramComponent), paramInt);
/*     */   }
/*     */ 
/*     */   public int getPosition(Component paramComponent)
/*     */   {
/* 472 */     int n = 0;
/*     */ 
/* 474 */     getComponentCount();
/* 475 */     int m = getIndexOf(paramComponent);
/*     */ 
/* 477 */     if (m == -1) {
/* 478 */       return -1;
/*     */     }
/* 480 */     int j = getLayer(paramComponent);
/* 481 */     for (int i = m - 1; i >= 0; i--) {
/* 482 */       int k = getLayer(getComponent(i));
/* 483 */       if (k == j)
/* 484 */         n++;
/*     */       else
/* 486 */         return n;
/*     */     }
/* 488 */     return n;
/*     */   }
/*     */ 
/*     */   public int highestLayer()
/*     */   {
/* 498 */     if (getComponentCount() > 0)
/* 499 */       return getLayer(getComponent(0));
/* 500 */     return 0;
/*     */   }
/*     */ 
/*     */   public int lowestLayer()
/*     */   {
/* 510 */     int i = getComponentCount();
/* 511 */     if (i > 0)
/* 512 */       return getLayer(getComponent(i - 1));
/* 513 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getComponentCountInLayer(int paramInt)
/*     */   {
/* 524 */     int m = 0;
/*     */ 
/* 526 */     int j = getComponentCount();
/* 527 */     for (int i = 0; i < j; i++) {
/* 528 */       int k = getLayer(getComponent(i));
/* 529 */       if (k == paramInt)
/* 530 */         m++;
/*     */       else {
/* 532 */         if ((m > 0) || (k < paramInt)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 537 */     return m;
/*     */   }
/*     */ 
/*     */   public Component[] getComponentsInLayer(int paramInt)
/*     */   {
/* 548 */     int m = 0;
/*     */ 
/* 551 */     Component[] arrayOfComponent = new Component[getComponentCountInLayer(paramInt)];
/* 552 */     int j = getComponentCount();
/* 553 */     for (int i = 0; i < j; i++) {
/* 554 */       int k = getLayer(getComponent(i));
/* 555 */       if (k == paramInt)
/* 556 */         arrayOfComponent[(m++)] = getComponent(i);
/*     */       else {
/* 558 */         if ((m > 0) || (k < paramInt)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 563 */     return arrayOfComponent;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/* 572 */     if (isOpaque()) {
/* 573 */       Rectangle localRectangle = paramGraphics.getClipBounds();
/* 574 */       Color localColor = getBackground();
/* 575 */       if (localColor == null)
/* 576 */         localColor = Color.lightGray;
/* 577 */       paramGraphics.setColor(localColor);
/* 578 */       if (localRectangle != null) {
/* 579 */         paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */       }
/*     */       else {
/* 582 */         paramGraphics.fillRect(0, 0, getWidth(), getHeight());
/*     */       }
/*     */     }
/* 585 */     super.paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   protected Hashtable<Component, Integer> getComponentToLayer()
/*     */   {
/* 598 */     if (this.componentToLayer == null)
/* 599 */       this.componentToLayer = new Hashtable(4);
/* 600 */     return this.componentToLayer;
/*     */   }
/*     */ 
/*     */   protected Integer getObjectForLayer(int paramInt)
/*     */   {
/*     */     Integer localInteger;
/* 611 */     switch (paramInt) {
/*     */     case 0:
/* 613 */       localInteger = DEFAULT_LAYER;
/* 614 */       break;
/*     */     case 100:
/* 616 */       localInteger = PALETTE_LAYER;
/* 617 */       break;
/*     */     case 200:
/* 619 */       localInteger = MODAL_LAYER;
/* 620 */       break;
/*     */     case 300:
/* 622 */       localInteger = POPUP_LAYER;
/* 623 */       break;
/*     */     case 400:
/* 625 */       localInteger = DRAG_LAYER;
/* 626 */       break;
/*     */     default:
/* 628 */       localInteger = new Integer(paramInt);
/*     */     }
/* 630 */     return localInteger;
/*     */   }
/*     */ 
/*     */   protected int insertIndexForLayer(int paramInt1, int paramInt2)
/*     */   {
/* 644 */     return insertIndexForLayer(null, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private int insertIndexForLayer(Component paramComponent, int paramInt1, int paramInt2)
/*     */   {
/* 662 */     int m = -1;
/* 663 */     int n = -1;
/* 664 */     int i1 = getComponentCount();
/*     */ 
/* 666 */     ArrayList localArrayList = new ArrayList(i1);
/*     */ 
/* 668 */     for (int i2 = 0; i2 < i1; i2++) {
/* 669 */       if (getComponent(i2) != paramComponent) {
/* 670 */         localArrayList.add(getComponent(i2));
/*     */       }
/*     */     }
/*     */ 
/* 674 */     int j = localArrayList.size();
/* 675 */     for (int i = 0; i < j; i++) {
/* 676 */       int k = getLayer((Component)localArrayList.get(i));
/* 677 */       if ((m == -1) && (k == paramInt1)) {
/* 678 */         m = i;
/*     */       }
/* 680 */       if (k < paramInt1) {
/* 681 */         if (i == 0)
/*     */         {
/* 684 */           m = 0;
/* 685 */           n = 0; break;
/*     */         }
/* 687 */         n = i;
/*     */ 
/* 689 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 696 */     if ((m == -1) && (n == -1)) {
/* 697 */       return j;
/*     */     }
/*     */ 
/* 700 */     if ((m != -1) && (n == -1)) {
/* 701 */       n = j;
/*     */     }
/* 703 */     if ((n != -1) && (m == -1)) {
/* 704 */       m = n;
/*     */     }
/*     */ 
/* 707 */     if (paramInt2 == -1) {
/* 708 */       return n;
/*     */     }
/*     */ 
/* 712 */     if ((paramInt2 > -1) && (m + paramInt2 <= n)) {
/* 713 */       return m + paramInt2;
/*     */     }
/*     */ 
/* 716 */     return n;
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 729 */     String str = this.optimizedDrawingPossible ? "true" : "false";
/*     */ 
/* 732 */     return super.paramString() + ",optimizedDrawingPossible=" + str;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 750 */     if (this.accessibleContext == null) {
/* 751 */       this.accessibleContext = new AccessibleJLayeredPane();
/*     */     }
/* 753 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJLayeredPane extends JComponent.AccessibleJComponent
/*     */   {
/*     */     protected AccessibleJLayeredPane()
/*     */     {
/* 771 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 781 */       return AccessibleRole.LAYERED_PANE;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JLayeredPane
 * JD-Core Version:    0.6.2
 */