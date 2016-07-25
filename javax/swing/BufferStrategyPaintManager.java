/*     */ package javax.swing;
/*     */ 
/*     */ import com.sun.java.swing.SwingUtilities3;
/*     */ import java.awt.AWTException;
/*     */ import java.awt.BufferCapabilities;
/*     */ import java.awt.BufferCapabilities.FlipContents;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.ImageCapabilities;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.awt.image.BufferStrategy;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import sun.awt.SubRegionShowable;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.pipe.hw.ExtendedBufferCapabilities;
/*     */ import sun.java2d.pipe.hw.ExtendedBufferCapabilities.VSyncType;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ class BufferStrategyPaintManager extends RepaintManager.PaintManager
/*     */ {
/*     */   private static Method COMPONENT_CREATE_BUFFER_STRATEGY_METHOD;
/*     */   private static Method COMPONENT_GET_BUFFER_STRATEGY_METHOD;
/*  81 */   private static final PlatformLogger LOGGER = PlatformLogger.getLogger("javax.swing.BufferStrategyPaintManager");
/*     */   private ArrayList<BufferInfo> bufferInfos;
/*     */   private boolean painting;
/*     */   private boolean showing;
/*     */   private int accumulatedX;
/*     */   private int accumulatedY;
/*     */   private int accumulatedMaxX;
/*     */   private int accumulatedMaxY;
/*     */   private JComponent rootJ;
/*     */   private Container root;
/*     */   private int xOffset;
/*     */   private int yOffset;
/*     */   private Graphics bsg;
/*     */   private BufferStrategy bufferStrategy;
/*     */   private BufferInfo bufferInfo;
/*     */   private boolean disposeBufferOnEnd;
/*     */ 
/*     */   private static Method getGetBufferStrategyMethod()
/*     */   {
/* 154 */     if (COMPONENT_GET_BUFFER_STRATEGY_METHOD == null) {
/* 155 */       getMethods();
/*     */     }
/* 157 */     return COMPONENT_GET_BUFFER_STRATEGY_METHOD;
/*     */   }
/*     */ 
/*     */   private static Method getCreateBufferStrategyMethod() {
/* 161 */     if (COMPONENT_CREATE_BUFFER_STRATEGY_METHOD == null) {
/* 162 */       getMethods();
/*     */     }
/* 164 */     return COMPONENT_CREATE_BUFFER_STRATEGY_METHOD;
/*     */   }
/*     */ 
/*     */   private static void getMethods() {
/* 168 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/* 172 */           BufferStrategyPaintManager.access$002(Component.class.getDeclaredMethod("createBufferStrategy", new Class[] { Integer.TYPE, BufferCapabilities.class }));
/*     */ 
/* 176 */           BufferStrategyPaintManager.COMPONENT_CREATE_BUFFER_STRATEGY_METHOD.setAccessible(true);
/*     */ 
/* 178 */           BufferStrategyPaintManager.access$102(Component.class.getDeclaredMethod("getBufferStrategy", new Class[0]));
/*     */ 
/* 180 */           BufferStrategyPaintManager.COMPONENT_GET_BUFFER_STRATEGY_METHOD.setAccessible(true);
/*     */         } catch (SecurityException localSecurityException) {
/* 182 */           if (!$assertionsDisabled) throw new AssertionError(); 
/*     */         }
/* 184 */         catch (NoSuchMethodException localNoSuchMethodException) { if (!$assertionsDisabled) throw new AssertionError();
/*     */         }
/* 186 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   BufferStrategyPaintManager() {
/* 192 */     this.bufferInfos = new ArrayList(1);
/*     */   }
/*     */ 
/*     */   protected void dispose()
/*     */   {
/* 206 */     SwingUtilities.invokeLater(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         ArrayList localArrayList;
/* 209 */         synchronized (BufferStrategyPaintManager.this) {
/* 210 */           while (BufferStrategyPaintManager.this.showing)
/*     */             try {
/* 212 */               BufferStrategyPaintManager.this.wait();
/*     */             }
/*     */             catch (InterruptedException localInterruptedException) {
/*     */             }
/* 216 */           localArrayList = BufferStrategyPaintManager.this.bufferInfos;
/* 217 */           BufferStrategyPaintManager.this.bufferInfos = null;
/*     */         }
/* 219 */         BufferStrategyPaintManager.this.dispose(localArrayList);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void dispose(List<BufferInfo> paramList) {
/* 225 */     if (LOGGER.isLoggable(400)) {
/* 226 */       LOGGER.finer("BufferStrategyPaintManager disposed", new RuntimeException());
/*     */     }
/*     */ 
/* 229 */     if (paramList != null)
/* 230 */       for (BufferInfo localBufferInfo : paramList)
/* 231 */         localBufferInfo.dispose();
/*     */   }
/*     */ 
/*     */   public boolean show(Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 242 */     synchronized (this) {
/* 243 */       if (this.painting)
/*     */       {
/* 246 */         return false;
/*     */       }
/* 248 */       this.showing = true;
/*     */     }
/*     */     try {
/* 251 */       ??? = getBufferInfo(paramContainer);
/*     */       BufferStrategy localBufferStrategy;
/* 253 */       if ((??? != null) && (???.isInSync()) && ((localBufferStrategy = ???.getBufferStrategy(false)) != null))
/*     */       {
/* 255 */         SubRegionShowable localSubRegionShowable = (SubRegionShowable)localBufferStrategy;
/*     */ 
/* 257 */         boolean bool1 = ???.getPaintAllOnExpose();
/* 258 */         ???.setPaintAllOnExpose(false);
/* 259 */         if (localSubRegionShowable.showIfNotLost(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4)) {
/* 260 */           return !bool1;
/*     */         }
/*     */ 
/* 266 */         this.bufferInfo.setContentsLostDuringExpose(true);
/*     */       }
/*     */     }
/*     */     finally {
/* 270 */       synchronized (this) {
/* 271 */         this.showing = false;
/* 272 */         notifyAll();
/*     */       }
/*     */     }
/* 275 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean paint(JComponent paramJComponent1, JComponent paramJComponent2, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 281 */     if (prepare(paramJComponent1, true, paramInt1, paramInt2, paramInt3, paramInt4)) {
/* 282 */       if (((paramGraphics instanceof SunGraphics2D)) && (((SunGraphics2D)paramGraphics).getDestination() == this.root))
/*     */       {
/* 287 */         int i = ((SunGraphics2D)this.bsg).constrainX;
/* 288 */         int j = ((SunGraphics2D)this.bsg).constrainY;
/* 289 */         if ((i != 0) || (j != 0)) {
/* 290 */           this.bsg.translate(-i, -j);
/*     */         }
/* 292 */         ((SunGraphics2D)this.bsg).constrain(this.xOffset + i, this.yOffset + j, paramInt1 + paramInt3, paramInt2 + paramInt4);
/*     */ 
/* 294 */         this.bsg.setClip(paramInt1, paramInt2, paramInt3, paramInt4);
/* 295 */         paramJComponent1.paintToOffscreen(this.bsg, paramInt1, paramInt2, paramInt3, paramInt4, paramInt1 + paramInt3, paramInt2 + paramInt4);
/*     */ 
/* 297 */         accumulate(this.xOffset + paramInt1, this.yOffset + paramInt2, paramInt3, paramInt4);
/* 298 */         return true;
/*     */       }
/*     */ 
/* 303 */       this.bufferInfo.setInSync(false);
/*     */     }
/*     */ 
/* 308 */     if (LOGGER.isLoggable(400)) {
/* 309 */       LOGGER.finer("prepare failed");
/*     */     }
/* 311 */     return super.paint(paramJComponent1, paramJComponent2, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void copyArea(JComponent paramJComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*     */   {
/* 322 */     if ((prepare(paramJComponent, false, 0, 0, 0, 0)) && (this.bufferInfo.isInSync())) {
/* 323 */       if (paramBoolean) {
/* 324 */         Rectangle localRectangle = paramJComponent.getVisibleRect();
/* 325 */         int i = this.xOffset + paramInt1;
/* 326 */         int j = this.yOffset + paramInt2;
/* 327 */         this.bsg.clipRect(this.xOffset + localRectangle.x, this.yOffset + localRectangle.y, localRectangle.width, localRectangle.height);
/*     */ 
/* 330 */         this.bsg.copyArea(i, j, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */       }
/*     */       else {
/* 333 */         this.bsg.copyArea(this.xOffset + paramInt1, this.yOffset + paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */       }
/*     */ 
/* 336 */       accumulate(paramInt1 + this.xOffset + paramInt5, paramInt2 + this.yOffset + paramInt6, paramInt3, paramInt4);
/*     */     } else {
/* 338 */       if (LOGGER.isLoggable(400)) {
/* 339 */         LOGGER.finer("copyArea: prepare failed or not in sync");
/*     */       }
/*     */ 
/* 344 */       if (!flushAccumulatedRegion())
/*     */       {
/* 347 */         this.rootJ.repaint();
/*     */       }
/* 349 */       else super.copyArea(paramJComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void beginPaint()
/*     */   {
/* 355 */     synchronized (this) {
/* 356 */       this.painting = true;
/*     */ 
/* 359 */       while (this.showing)
/*     */         try {
/* 361 */           wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {
/*     */         }
/*     */     }
/* 366 */     if (LOGGER.isLoggable(300)) {
/* 367 */       LOGGER.finest("beginPaint");
/*     */     }
/*     */ 
/* 370 */     resetAccumulated();
/*     */   }
/*     */ 
/*     */   public void endPaint() {
/* 374 */     if (LOGGER.isLoggable(300)) {
/* 375 */       LOGGER.finest("endPaint: region " + this.accumulatedX + " " + this.accumulatedY + " " + this.accumulatedMaxX + " " + this.accumulatedMaxY);
/*     */     }
/*     */ 
/* 379 */     if ((this.painting) && 
/* 380 */       (!flushAccumulatedRegion())) {
/* 381 */       if (!isRepaintingRoot()) {
/* 382 */         repaintRoot(this.rootJ);
/*     */       }
/*     */       else
/*     */       {
/* 386 */         resetDoubleBufferPerWindow();
/*     */ 
/* 388 */         this.rootJ.repaint();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 393 */     BufferInfo localBufferInfo = null;
/* 394 */     synchronized (this) {
/* 395 */       this.painting = false;
/* 396 */       if (this.disposeBufferOnEnd) {
/* 397 */         this.disposeBufferOnEnd = false;
/* 398 */         localBufferInfo = this.bufferInfo;
/* 399 */         this.bufferInfos.remove(localBufferInfo);
/*     */       }
/*     */     }
/* 402 */     if (localBufferInfo != null)
/* 403 */       localBufferInfo.dispose();
/*     */   }
/*     */ 
/*     */   private boolean flushAccumulatedRegion()
/*     */   {
/* 413 */     boolean bool1 = true;
/* 414 */     if (this.accumulatedX != 2147483647) {
/* 415 */       SubRegionShowable localSubRegionShowable = (SubRegionShowable)this.bufferStrategy;
/* 416 */       boolean bool2 = this.bufferStrategy.contentsLost();
/* 417 */       if (!bool2) {
/* 418 */         localSubRegionShowable.show(this.accumulatedX, this.accumulatedY, this.accumulatedMaxX, this.accumulatedMaxY);
/*     */ 
/* 420 */         bool2 = this.bufferStrategy.contentsLost();
/*     */       }
/* 422 */       if (bool2) {
/* 423 */         if (LOGGER.isLoggable(400)) {
/* 424 */           LOGGER.finer("endPaint: contents lost");
/*     */         }
/*     */ 
/* 427 */         this.bufferInfo.setInSync(false);
/* 428 */         bool1 = false;
/*     */       }
/*     */     }
/* 431 */     resetAccumulated();
/* 432 */     return bool1;
/*     */   }
/*     */ 
/*     */   private void resetAccumulated() {
/* 436 */     this.accumulatedX = 2147483647;
/* 437 */     this.accumulatedY = 2147483647;
/* 438 */     this.accumulatedMaxX = 0;
/* 439 */     this.accumulatedMaxY = 0;
/*     */   }
/*     */ 
/*     */   public void doubleBufferingChanged(final JRootPane paramJRootPane)
/*     */   {
/* 449 */     if (((!paramJRootPane.isDoubleBuffered()) || (!paramJRootPane.getUseTrueDoubleBuffering())) && (paramJRootPane.getParent() != null))
/*     */     {
/* 452 */       if (!SwingUtilities.isEventDispatchThread()) {
/* 453 */         Runnable local3 = new Runnable() {
/*     */           public void run() {
/* 455 */             BufferStrategyPaintManager.this.doubleBufferingChanged0(paramJRootPane);
/*     */           }
/*     */         };
/* 458 */         SwingUtilities.invokeLater(local3);
/*     */       }
/*     */       else {
/* 461 */         doubleBufferingChanged0(paramJRootPane);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void doubleBufferingChanged0(JRootPane paramJRootPane)
/*     */   {
/*     */     BufferInfo localBufferInfo;
/* 472 */     synchronized (this)
/*     */     {
/* 475 */       while (this.showing)
/*     */         try {
/* 477 */           wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {
/*     */         }
/* 481 */       localBufferInfo = getBufferInfo(paramJRootPane.getParent());
/* 482 */       if ((this.painting) && (this.bufferInfo == localBufferInfo))
/*     */       {
/* 487 */         this.disposeBufferOnEnd = true;
/* 488 */         localBufferInfo = null;
/* 489 */       } else if (localBufferInfo != null) {
/* 490 */         this.bufferInfos.remove(localBufferInfo);
/*     */       }
/*     */     }
/* 493 */     if (localBufferInfo != null)
/* 494 */       localBufferInfo.dispose();
/*     */   }
/*     */ 
/*     */   private boolean prepare(JComponent paramJComponent, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 505 */     if (this.bsg != null) {
/* 506 */       this.bsg.dispose();
/* 507 */       this.bsg = null;
/*     */     }
/* 509 */     this.bufferStrategy = null;
/* 510 */     if (fetchRoot(paramJComponent)) {
/* 511 */       int i = 0;
/* 512 */       BufferInfo localBufferInfo = getBufferInfo(this.root);
/* 513 */       if (localBufferInfo == null) {
/* 514 */         i = 1;
/* 515 */         localBufferInfo = new BufferInfo(this.root);
/* 516 */         this.bufferInfos.add(localBufferInfo);
/* 517 */         if (LOGGER.isLoggable(400)) {
/* 518 */           LOGGER.finer("prepare: new BufferInfo: " + this.root);
/*     */         }
/*     */       }
/* 521 */       this.bufferInfo = localBufferInfo;
/* 522 */       if (!localBufferInfo.hasBufferStrategyChanged()) {
/* 523 */         this.bufferStrategy = localBufferInfo.getBufferStrategy(true);
/* 524 */         if (this.bufferStrategy != null) {
/* 525 */           this.bsg = this.bufferStrategy.getDrawGraphics();
/* 526 */           if (this.bufferStrategy.contentsRestored()) {
/* 527 */             i = 1;
/* 528 */             if (LOGGER.isLoggable(400)) {
/* 529 */               LOGGER.finer("prepare: contents restored in prepare");
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 537 */           return false;
/*     */         }
/* 539 */         if (localBufferInfo.getContentsLostDuringExpose()) {
/* 540 */           i = 1;
/* 541 */           localBufferInfo.setContentsLostDuringExpose(false);
/* 542 */           if (LOGGER.isLoggable(400)) {
/* 543 */             LOGGER.finer("prepare: contents lost on expose");
/*     */           }
/*     */         }
/* 546 */         if ((paramBoolean) && (paramJComponent == this.rootJ) && (paramInt1 == 0) && (paramInt2 == 0) && (paramJComponent.getWidth() == paramInt3) && (paramJComponent.getHeight() == paramInt4))
/*     */         {
/* 548 */           localBufferInfo.setInSync(true);
/*     */         }
/* 550 */         else if (i != 0)
/*     */         {
/* 555 */           localBufferInfo.setInSync(false);
/* 556 */           if (!isRepaintingRoot()) {
/* 557 */             repaintRoot(this.rootJ);
/*     */           }
/*     */           else
/*     */           {
/* 561 */             resetDoubleBufferPerWindow();
/*     */           }
/*     */         }
/* 564 */         return this.bufferInfos != null;
/*     */       }
/*     */     }
/* 567 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean fetchRoot(JComponent paramJComponent) {
/* 571 */     int i = 0;
/* 572 */     this.rootJ = paramJComponent;
/* 573 */     this.root = paramJComponent;
/* 574 */     this.xOffset = (this.yOffset = 0);
/* 575 */     while ((this.root != null) && (!(this.root instanceof Window)) && (!SunToolkit.isInstanceOf(this.root, "java.applet.Applet")))
/*     */     {
/* 578 */       this.xOffset += this.root.getX();
/* 579 */       this.yOffset += this.root.getY();
/* 580 */       this.root = this.root.getParent();
/* 581 */       if (this.root != null) {
/* 582 */         if ((this.root instanceof JComponent)) {
/* 583 */           this.rootJ = ((JComponent)this.root);
/*     */         }
/* 585 */         else if (!this.root.isLightweight()) {
/* 586 */           if (i == 0) {
/* 587 */             i = 1;
/*     */           }
/*     */           else
/*     */           {
/* 600 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 605 */     if (((this.root instanceof RootPaneContainer)) && ((this.rootJ instanceof JRootPane)))
/*     */     {
/* 610 */       if ((this.rootJ.isDoubleBuffered()) && (((JRootPane)this.rootJ).getUseTrueDoubleBuffering()))
/*     */       {
/* 616 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 620 */     return false;
/*     */   }
/*     */ 
/*     */   private void resetDoubleBufferPerWindow()
/*     */   {
/* 627 */     if (this.bufferInfos != null) {
/* 628 */       dispose(this.bufferInfos);
/* 629 */       this.bufferInfos = null;
/* 630 */       this.repaintManager.setPaintManager(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private BufferInfo getBufferInfo(Container paramContainer)
/*     */   {
/* 639 */     for (int i = this.bufferInfos.size() - 1; i >= 0; i--) {
/* 640 */       BufferInfo localBufferInfo = (BufferInfo)this.bufferInfos.get(i);
/* 641 */       Container localContainer = localBufferInfo.getRoot();
/* 642 */       if (localContainer == null)
/*     */       {
/* 644 */         this.bufferInfos.remove(i);
/* 645 */         if (LOGGER.isLoggable(400)) {
/* 646 */           LOGGER.finer("BufferInfo pruned, root null");
/*     */         }
/*     */       }
/* 649 */       else if (localContainer == paramContainer) {
/* 650 */         return localBufferInfo;
/*     */       }
/*     */     }
/* 653 */     return null;
/*     */   }
/*     */ 
/*     */   private void accumulate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 657 */     this.accumulatedX = Math.min(paramInt1, this.accumulatedX);
/* 658 */     this.accumulatedY = Math.min(paramInt2, this.accumulatedY);
/* 659 */     this.accumulatedMaxX = Math.max(this.accumulatedMaxX, paramInt1 + paramInt3);
/* 660 */     this.accumulatedMaxY = Math.max(this.accumulatedMaxY, paramInt2 + paramInt4);
/*     */   }
/*     */ 
/*     */   private class BufferInfo extends ComponentAdapter
/*     */     implements WindowListener
/*     */   {
/*     */     private WeakReference<BufferStrategy> weakBS;
/*     */     private WeakReference<Container> root;
/*     */     private boolean inSync;
/*     */     private boolean contentsLostDuringExpose;
/*     */     private boolean paintAllOnExpose;
/*     */ 
/*     */     public BufferInfo(Container arg2)
/*     */     {
/*     */       Object localObject;
/* 692 */       this.root = new WeakReference(localObject);
/* 693 */       localObject.addComponentListener(this);
/* 694 */       if ((localObject instanceof Window))
/* 695 */         ((Window)localObject).addWindowListener(this);
/*     */     }
/*     */ 
/*     */     public void setPaintAllOnExpose(boolean paramBoolean)
/*     */     {
/* 700 */       this.paintAllOnExpose = paramBoolean;
/*     */     }
/*     */ 
/*     */     public boolean getPaintAllOnExpose() {
/* 704 */       return this.paintAllOnExpose;
/*     */     }
/*     */ 
/*     */     public void setContentsLostDuringExpose(boolean paramBoolean) {
/* 708 */       this.contentsLostDuringExpose = paramBoolean;
/*     */     }
/*     */ 
/*     */     public boolean getContentsLostDuringExpose() {
/* 712 */       return this.contentsLostDuringExpose;
/*     */     }
/*     */ 
/*     */     public void setInSync(boolean paramBoolean) {
/* 716 */       this.inSync = paramBoolean;
/*     */     }
/*     */ 
/*     */     public boolean isInSync()
/*     */     {
/* 725 */       return this.inSync;
/*     */     }
/*     */ 
/*     */     public Container getRoot()
/*     */     {
/* 732 */       return this.root == null ? null : (Container)this.root.get();
/*     */     }
/*     */ 
/*     */     public BufferStrategy getBufferStrategy(boolean paramBoolean)
/*     */     {
/* 745 */       BufferStrategy localBufferStrategy = this.weakBS == null ? null : (BufferStrategy)this.weakBS.get();
/* 746 */       if ((localBufferStrategy == null) && (paramBoolean)) {
/* 747 */         localBufferStrategy = createBufferStrategy();
/* 748 */         if (localBufferStrategy != null) {
/* 749 */           this.weakBS = new WeakReference(localBufferStrategy);
/*     */         }
/* 751 */         if (BufferStrategyPaintManager.LOGGER.isLoggable(400)) {
/* 752 */           BufferStrategyPaintManager.LOGGER.finer("getBufferStrategy: created bs: " + localBufferStrategy);
/*     */         }
/*     */       }
/* 755 */       return localBufferStrategy;
/*     */     }
/*     */ 
/*     */     public boolean hasBufferStrategyChanged()
/*     */     {
/* 763 */       Container localContainer = getRoot();
/* 764 */       if (localContainer != null) {
/* 765 */         BufferStrategy localBufferStrategy1 = null;
/* 766 */         BufferStrategy localBufferStrategy2 = null;
/*     */ 
/* 768 */         localBufferStrategy1 = getBufferStrategy(false);
/* 769 */         if ((localContainer instanceof Window))
/* 770 */           localBufferStrategy2 = ((Window)localContainer).getBufferStrategy();
/*     */         else {
/*     */           try
/*     */           {
/* 774 */             localBufferStrategy2 = (BufferStrategy)BufferStrategyPaintManager.access$700().invoke(localContainer, new Object[0]);
/*     */           }
/*     */           catch (InvocationTargetException localInvocationTargetException) {
/* 777 */             if (!$assertionsDisabled) throw new AssertionError(); 
/*     */           }
/* 779 */           catch (IllegalArgumentException localIllegalArgumentException) { if (!$assertionsDisabled) throw new AssertionError();  } catch (IllegalAccessException localIllegalAccessException)
/*     */           {
/* 781 */             if (!$assertionsDisabled) throw new AssertionError();
/*     */           }
/*     */         }
/* 784 */         if (localBufferStrategy2 != localBufferStrategy1)
/*     */         {
/* 786 */           if (localBufferStrategy1 != null) {
/* 787 */             localBufferStrategy1.dispose();
/*     */           }
/* 789 */           this.weakBS = null;
/* 790 */           return true;
/*     */         }
/*     */       }
/* 793 */       return false;
/*     */     }
/*     */ 
/*     */     private BufferStrategy createBufferStrategy()
/*     */     {
/* 802 */       Container localContainer = getRoot();
/* 803 */       if (localContainer == null) {
/* 804 */         return null;
/*     */       }
/* 806 */       BufferStrategy localBufferStrategy = null;
/* 807 */       if (SwingUtilities3.isVsyncRequested(localContainer)) {
/* 808 */         localBufferStrategy = createBufferStrategy(localContainer, true);
/* 809 */         if (BufferStrategyPaintManager.LOGGER.isLoggable(400)) {
/* 810 */           BufferStrategyPaintManager.LOGGER.finer("createBufferStrategy: using vsynced strategy");
/*     */         }
/*     */       }
/* 813 */       if (localBufferStrategy == null) {
/* 814 */         localBufferStrategy = createBufferStrategy(localContainer, false);
/*     */       }
/* 816 */       if (!(localBufferStrategy instanceof SubRegionShowable))
/*     */       {
/* 822 */         localBufferStrategy = null;
/*     */       }
/* 824 */       return localBufferStrategy;
/*     */     }
/*     */ 
/*     */     private BufferStrategy createBufferStrategy(Container paramContainer, boolean paramBoolean)
/*     */     {
/*     */       Object localObject;
/* 833 */       if (paramBoolean) {
/* 834 */         localObject = new ExtendedBufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(true), BufferCapabilities.FlipContents.COPIED, ExtendedBufferCapabilities.VSyncType.VSYNC_ON);
/*     */       }
/*     */       else
/*     */       {
/* 839 */         localObject = new BufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(true), null);
/*     */       }
/*     */ 
/* 843 */       BufferStrategy localBufferStrategy = null;
/* 844 */       if (SunToolkit.isInstanceOf(paramContainer, "java.applet.Applet"))
/*     */         try {
/* 846 */           BufferStrategyPaintManager.access$800().invoke(paramContainer, new Object[] { Integer.valueOf(2), localObject });
/* 847 */           localBufferStrategy = (BufferStrategy)BufferStrategyPaintManager.access$700().invoke(paramContainer, new Object[0]);
/*     */         }
/*     */         catch (InvocationTargetException localInvocationTargetException)
/*     */         {
/* 851 */           if (BufferStrategyPaintManager.LOGGER.isLoggable(400))
/* 852 */             BufferStrategyPaintManager.LOGGER.finer("createBufferStratety failed", localInvocationTargetException);
/*     */         }
/*     */         catch (IllegalArgumentException localIllegalArgumentException)
/*     */         {
/* 856 */           if (!$assertionsDisabled) throw new AssertionError(); 
/*     */         }
/* 858 */         catch (IllegalAccessException localIllegalAccessException) { if (!$assertionsDisabled) throw new AssertionError();
/*     */         }
/*     */       else {
/*     */         try
/*     */         {
/* 863 */           ((Window)paramContainer).createBufferStrategy(2, (BufferCapabilities)localObject);
/* 864 */           localBufferStrategy = ((Window)paramContainer).getBufferStrategy();
/*     */         }
/*     */         catch (AWTException localAWTException) {
/* 867 */           if (BufferStrategyPaintManager.LOGGER.isLoggable(400)) {
/* 868 */             BufferStrategyPaintManager.LOGGER.finer("createBufferStratety failed", localAWTException);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 873 */       return localBufferStrategy;
/*     */     }
/*     */ 
/*     */     public void dispose()
/*     */     {
/* 880 */       Container localContainer = getRoot();
/* 881 */       if (BufferStrategyPaintManager.LOGGER.isLoggable(400)) {
/* 882 */         BufferStrategyPaintManager.LOGGER.finer("disposed BufferInfo for: " + localContainer);
/*     */       }
/* 884 */       if (localContainer != null) {
/* 885 */         localContainer.removeComponentListener(this);
/* 886 */         if ((localContainer instanceof Window)) {
/* 887 */           ((Window)localContainer).removeWindowListener(this);
/*     */         }
/* 889 */         BufferStrategy localBufferStrategy = getBufferStrategy(false);
/* 890 */         if (localBufferStrategy != null) {
/* 891 */           localBufferStrategy.dispose();
/*     */         }
/*     */       }
/* 894 */       this.root = null;
/* 895 */       this.weakBS = null;
/*     */     }
/*     */ 
/*     */     public void componentHidden(ComponentEvent paramComponentEvent)
/*     */     {
/* 905 */       Container localContainer = getRoot();
/* 906 */       if ((localContainer != null) && (localContainer.isVisible()))
/*     */       {
/* 915 */         localContainer.repaint();
/*     */       }
/*     */       else
/* 918 */         setPaintAllOnExpose(true);
/*     */     }
/*     */ 
/*     */     public void windowIconified(WindowEvent paramWindowEvent)
/*     */     {
/* 923 */       setPaintAllOnExpose(true);
/*     */     }
/*     */ 
/*     */     public void windowClosed(WindowEvent paramWindowEvent)
/*     */     {
/* 929 */       synchronized (BufferStrategyPaintManager.this) {
/* 930 */         while (BufferStrategyPaintManager.this.showing)
/*     */           try {
/* 932 */             BufferStrategyPaintManager.this.wait();
/*     */           }
/*     */           catch (InterruptedException localInterruptedException) {
/*     */           }
/* 936 */         BufferStrategyPaintManager.this.bufferInfos.remove(this);
/*     */       }
/* 938 */       dispose();
/*     */     }
/*     */ 
/*     */     public void windowOpened(WindowEvent paramWindowEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void windowClosing(WindowEvent paramWindowEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void windowDeiconified(WindowEvent paramWindowEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void windowActivated(WindowEvent paramWindowEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void windowDeactivated(WindowEvent paramWindowEvent)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.BufferStrategyPaintManager
 * JD-Core Version:    0.6.2
 */