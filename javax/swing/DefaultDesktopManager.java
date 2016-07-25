/*     */ package javax.swing;
/*     */ 
/*     */ import com.sun.awt.AWTUtilities;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.io.Serializable;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.WindowAccessor;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class DefaultDesktopManager
/*     */   implements DesktopManager, Serializable
/*     */ {
/*     */   static final String HAS_BEEN_ICONIFIED_PROPERTY = "wasIconOnce";
/*     */   static final int DEFAULT_DRAG_MODE = 0;
/*     */   static final int OUTLINE_DRAG_MODE = 1;
/*     */   static final int FASTER_DRAG_MODE = 2;
/*  57 */   int dragMode = 0;
/*     */ 
/*  59 */   private transient Rectangle currentBounds = null;
/*  60 */   private transient Graphics desktopGraphics = null;
/*  61 */   private transient Rectangle desktopBounds = null;
/*  62 */   private transient Rectangle[] floatingItems = new Rectangle[0];
/*     */   private transient boolean didDrag;
/* 343 */   private transient Point currentLoc = null;
/*     */ 
/*     */   public void openFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/*  76 */     if (paramJInternalFrame.getDesktopIcon().getParent() != null) {
/*  77 */       paramJInternalFrame.getDesktopIcon().getParent().add(paramJInternalFrame);
/*  78 */       removeIconFor(paramJInternalFrame);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void closeFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/*  88 */     JDesktopPane localJDesktopPane = paramJInternalFrame.getDesktopPane();
/*  89 */     if (localJDesktopPane == null) {
/*  90 */       return;
/*     */     }
/*  92 */     boolean bool = paramJInternalFrame.isSelected();
/*  93 */     Container localContainer = paramJInternalFrame.getParent();
/*  94 */     JInternalFrame localJInternalFrame = null;
/*  95 */     if (bool) {
/*  96 */       localJInternalFrame = localJDesktopPane.getNextFrame(paramJInternalFrame);
/*     */       try { paramJInternalFrame.setSelected(false); } catch (PropertyVetoException localPropertyVetoException1) {  }
/*     */     }
/*  99 */     if (localContainer != null) {
/* 100 */       localContainer.remove(paramJInternalFrame);
/* 101 */       localContainer.repaint(paramJInternalFrame.getX(), paramJInternalFrame.getY(), paramJInternalFrame.getWidth(), paramJInternalFrame.getHeight());
/*     */     }
/* 103 */     removeIconFor(paramJInternalFrame);
/* 104 */     if (paramJInternalFrame.getNormalBounds() != null)
/* 105 */       paramJInternalFrame.setNormalBounds(null);
/* 106 */     if (wasIcon(paramJInternalFrame))
/* 107 */       setWasIcon(paramJInternalFrame, null);
/* 108 */     if (localJInternalFrame != null) try {
/* 109 */         localJInternalFrame.setSelected(true);
/*     */       } catch (PropertyVetoException localPropertyVetoException2) {
/*     */       } else if ((bool) && (localJDesktopPane.getComponentCount() == 0))
/*     */     {
/* 113 */       localJDesktopPane.requestFocus();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void maximizeFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/* 122 */     if (paramJInternalFrame.isIcon())
/*     */     {
/*     */       try
/*     */       {
/* 126 */         paramJInternalFrame.setIcon(false);
/*     */       } catch (PropertyVetoException localPropertyVetoException1) {
/*     */       }
/*     */     } else {
/* 130 */       paramJInternalFrame.setNormalBounds(paramJInternalFrame.getBounds());
/* 131 */       Rectangle localRectangle = paramJInternalFrame.getParent().getBounds();
/* 132 */       setBoundsForFrame(paramJInternalFrame, 0, 0, localRectangle.width, localRectangle.height);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 138 */       paramJInternalFrame.setSelected(true);
/*     */     }
/*     */     catch (PropertyVetoException localPropertyVetoException2)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void minimizeFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/* 150 */     if (paramJInternalFrame.isIcon()) {
/* 151 */       iconifyFrame(paramJInternalFrame);
/* 152 */       return;
/*     */     }
/*     */ 
/* 155 */     if (paramJInternalFrame.getNormalBounds() != null) {
/* 156 */       Rectangle localRectangle = paramJInternalFrame.getNormalBounds();
/* 157 */       paramJInternalFrame.setNormalBounds(null);
/*     */       try { paramJInternalFrame.setSelected(true); } catch (PropertyVetoException localPropertyVetoException) {
/* 159 */       }setBoundsForFrame(paramJInternalFrame, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void iconifyFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/* 170 */     Container localContainer = paramJInternalFrame.getParent();
/* 171 */     JDesktopPane localJDesktopPane = paramJInternalFrame.getDesktopPane();
/* 172 */     boolean bool = paramJInternalFrame.isSelected();
/* 173 */     JInternalFrame.JDesktopIcon localJDesktopIcon = paramJInternalFrame.getDesktopIcon();
/*     */     Object localObject;
/* 174 */     if (!wasIcon(paramJInternalFrame)) {
/* 175 */       localObject = getBoundsForIconOf(paramJInternalFrame);
/* 176 */       localJDesktopIcon.setBounds(((Rectangle)localObject).x, ((Rectangle)localObject).y, ((Rectangle)localObject).width, ((Rectangle)localObject).height);
/*     */ 
/* 178 */       localJDesktopIcon.revalidate();
/* 179 */       setWasIcon(paramJInternalFrame, Boolean.TRUE);
/*     */     }
/*     */ 
/* 182 */     if ((localContainer == null) || (localJDesktopPane == null)) {
/* 183 */       return;
/*     */     }
/*     */ 
/* 186 */     if ((localContainer instanceof JLayeredPane)) {
/* 187 */       localObject = (JLayeredPane)localContainer;
/* 188 */       int i = JLayeredPane.getLayer(paramJInternalFrame);
/* 189 */       JLayeredPane.putLayer(localJDesktopIcon, i);
/*     */     }
/*     */ 
/* 195 */     if (!paramJInternalFrame.isMaximum()) {
/* 196 */       paramJInternalFrame.setNormalBounds(paramJInternalFrame.getBounds());
/*     */     }
/* 198 */     localJDesktopPane.setComponentOrderCheckingEnabled(false);
/* 199 */     localContainer.remove(paramJInternalFrame);
/* 200 */     localContainer.add(localJDesktopIcon);
/* 201 */     localJDesktopPane.setComponentOrderCheckingEnabled(true);
/* 202 */     localContainer.repaint(paramJInternalFrame.getX(), paramJInternalFrame.getY(), paramJInternalFrame.getWidth(), paramJInternalFrame.getHeight());
/* 203 */     if ((bool) && 
/* 204 */       (localJDesktopPane.selectFrame(true) == null))
/*     */     {
/* 206 */       paramJInternalFrame.restoreSubcomponentFocus();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deiconifyFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/* 217 */     JInternalFrame.JDesktopIcon localJDesktopIcon = paramJInternalFrame.getDesktopIcon();
/* 218 */     Container localContainer = localJDesktopIcon.getParent();
/* 219 */     JDesktopPane localJDesktopPane = paramJInternalFrame.getDesktopPane();
/* 220 */     if ((localContainer != null) && (localJDesktopPane != null)) {
/* 221 */       localContainer.add(paramJInternalFrame);
/*     */ 
/* 224 */       if (paramJInternalFrame.isMaximum()) {
/* 225 */         Rectangle localRectangle = localContainer.getBounds();
/* 226 */         if ((paramJInternalFrame.getWidth() != localRectangle.width) || (paramJInternalFrame.getHeight() != localRectangle.height))
/*     */         {
/* 228 */           setBoundsForFrame(paramJInternalFrame, 0, 0, localRectangle.width, localRectangle.height);
/*     */         }
/*     */       }
/*     */ 
/* 232 */       removeIconFor(paramJInternalFrame);
/* 233 */       if (paramJInternalFrame.isSelected()) {
/* 234 */         paramJInternalFrame.moveToFront();
/* 235 */         paramJInternalFrame.restoreSubcomponentFocus();
/*     */       }
/*     */       else {
/*     */         try {
/* 239 */           paramJInternalFrame.setSelected(true);
/*     */         }
/*     */         catch (PropertyVetoException localPropertyVetoException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void activateFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/* 253 */     Container localContainer = paramJInternalFrame.getParent();
/*     */ 
/* 255 */     JDesktopPane localJDesktopPane = paramJInternalFrame.getDesktopPane();
/* 256 */     JInternalFrame localJInternalFrame = localJDesktopPane == null ? null : localJDesktopPane.getSelectedFrame();
/*     */ 
/* 259 */     if (localContainer == null)
/*     */     {
/* 261 */       localContainer = paramJInternalFrame.getDesktopIcon().getParent();
/* 262 */       if (localContainer == null) {
/* 263 */         return;
/*     */       }
/*     */     }
/* 266 */     if (localJInternalFrame == null) {
/* 267 */       if (localJDesktopPane != null) localJDesktopPane.setSelectedFrame(paramJInternalFrame); 
/*     */     }
/* 268 */     else if (localJInternalFrame != paramJInternalFrame)
/*     */     {
/* 271 */       if (localJInternalFrame.isSelected())
/*     */         try {
/* 273 */           localJInternalFrame.setSelected(false);
/*     */         }
/*     */         catch (PropertyVetoException localPropertyVetoException) {
/*     */         }
/* 277 */       if (localJDesktopPane != null) localJDesktopPane.setSelectedFrame(paramJInternalFrame);
/*     */     }
/* 279 */     paramJInternalFrame.moveToFront();
/*     */   }
/*     */ 
/*     */   public void deactivateFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/* 284 */     JDesktopPane localJDesktopPane = paramJInternalFrame.getDesktopPane();
/* 285 */     JInternalFrame localJInternalFrame = localJDesktopPane == null ? null : localJDesktopPane.getSelectedFrame();
/*     */ 
/* 287 */     if (localJInternalFrame == paramJInternalFrame)
/* 288 */       localJDesktopPane.setSelectedFrame(null);
/*     */   }
/*     */ 
/*     */   public void beginDraggingFrame(JComponent paramJComponent)
/*     */   {
/* 293 */     setupDragMode(paramJComponent);
/*     */ 
/* 295 */     if (this.dragMode == 2) {
/* 296 */       Container localContainer = paramJComponent.getParent();
/* 297 */       this.floatingItems = findFloatingItems(paramJComponent);
/* 298 */       this.currentBounds = paramJComponent.getBounds();
/* 299 */       if ((localContainer instanceof JComponent)) {
/* 300 */         this.desktopBounds = ((JComponent)localContainer).getVisibleRect();
/*     */       }
/*     */       else {
/* 303 */         this.desktopBounds = localContainer.getBounds();
/* 304 */         this.desktopBounds.x = (this.desktopBounds.y = 0);
/*     */       }
/* 306 */       this.desktopGraphics = JComponent.safelyGetGraphics(localContainer);
/* 307 */       ((JInternalFrame)paramJComponent).isDragging = true;
/* 308 */       this.didDrag = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setupDragMode(JComponent paramJComponent)
/*     */   {
/* 314 */     JDesktopPane localJDesktopPane = getDesktopPane(paramJComponent);
/* 315 */     Container localContainer = paramJComponent.getParent();
/* 316 */     this.dragMode = 0;
/* 317 */     if (localJDesktopPane != null) {
/* 318 */       String str = (String)localJDesktopPane.getClientProperty("JDesktopPane.dragMode");
/* 319 */       Window localWindow = SwingUtilities.getWindowAncestor(paramJComponent);
/* 320 */       if ((localWindow != null) && (!AWTUtilities.isWindowOpaque(localWindow)))
/* 321 */         this.dragMode = 0;
/* 322 */       else if ((str != null) && (str.equals("outline")))
/* 323 */         this.dragMode = 1;
/* 324 */       else if ((str != null) && (str.equals("faster")) && ((paramJComponent instanceof JInternalFrame)) && (((JInternalFrame)paramJComponent).isOpaque()) && ((localContainer == null) || (localContainer.isOpaque())))
/*     */       {
/* 328 */         this.dragMode = 2;
/*     */       }
/* 330 */       else if (localJDesktopPane.getDragMode() == 1)
/* 331 */         this.dragMode = 1;
/* 332 */       else if ((localJDesktopPane.getDragMode() == 0) && ((paramJComponent instanceof JInternalFrame)) && (((JInternalFrame)paramJComponent).isOpaque()))
/*     */       {
/* 335 */         this.dragMode = 2;
/*     */       }
/* 337 */       else this.dragMode = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void dragFrame(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 353 */     if (this.dragMode == 1) {
/* 354 */       JDesktopPane localJDesktopPane = getDesktopPane(paramJComponent);
/* 355 */       if (localJDesktopPane != null) {
/* 356 */         Graphics localGraphics = JComponent.safelyGetGraphics(localJDesktopPane);
/*     */ 
/* 358 */         localGraphics.setXORMode(Color.white);
/* 359 */         if (this.currentLoc != null) {
/* 360 */           localGraphics.drawRect(this.currentLoc.x, this.currentLoc.y, paramJComponent.getWidth() - 1, paramJComponent.getHeight() - 1);
/*     */         }
/*     */ 
/* 363 */         localGraphics.drawRect(paramInt1, paramInt2, paramJComponent.getWidth() - 1, paramJComponent.getHeight() - 1);
/*     */ 
/* 373 */         SurfaceData localSurfaceData = ((SunGraphics2D)localGraphics).getSurfaceData();
/*     */ 
/* 376 */         if (!localSurfaceData.isSurfaceLost()) {
/* 377 */           this.currentLoc = new Point(paramInt1, paramInt2);
/*     */         }
/*     */ 
/* 380 */         localGraphics.dispose();
/*     */       }
/* 382 */     } else if (this.dragMode == 2) {
/* 383 */       dragFrameFaster(paramJComponent, paramInt1, paramInt2);
/*     */     } else {
/* 385 */       setBoundsForFrame(paramJComponent, paramInt1, paramInt2, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endDraggingFrame(JComponent paramJComponent)
/*     */   {
/* 391 */     if ((this.dragMode == 1) && (this.currentLoc != null)) {
/* 392 */       setBoundsForFrame(paramJComponent, this.currentLoc.x, this.currentLoc.y, paramJComponent.getWidth(), paramJComponent.getHeight());
/* 393 */       this.currentLoc = null;
/* 394 */     } else if (this.dragMode == 2) {
/* 395 */       this.currentBounds = null;
/* 396 */       if (this.desktopGraphics != null) {
/* 397 */         this.desktopGraphics.dispose();
/* 398 */         this.desktopGraphics = null;
/*     */       }
/* 400 */       this.desktopBounds = null;
/* 401 */       ((JInternalFrame)paramJComponent).isDragging = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void beginResizingFrame(JComponent paramJComponent, int paramInt)
/*     */   {
/* 407 */     setupDragMode(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void resizeFrame(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 420 */     if ((this.dragMode == 0) || (this.dragMode == 2)) {
/* 421 */       setBoundsForFrame(paramJComponent, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     } else {
/* 423 */       JDesktopPane localJDesktopPane = getDesktopPane(paramJComponent);
/* 424 */       if (localJDesktopPane != null) {
/* 425 */         Graphics localGraphics = JComponent.safelyGetGraphics(localJDesktopPane);
/*     */ 
/* 427 */         localGraphics.setXORMode(Color.white);
/* 428 */         if (this.currentBounds != null) {
/* 429 */           localGraphics.drawRect(this.currentBounds.x, this.currentBounds.y, this.currentBounds.width - 1, this.currentBounds.height - 1);
/*     */         }
/* 431 */         localGraphics.drawRect(paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/*     */ 
/* 434 */         SurfaceData localSurfaceData = ((SunGraphics2D)localGraphics).getSurfaceData();
/*     */ 
/* 436 */         if (!localSurfaceData.isSurfaceLost()) {
/* 437 */           this.currentBounds = new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */         }
/*     */ 
/* 440 */         localGraphics.setPaintMode();
/* 441 */         localGraphics.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endResizingFrame(JComponent paramJComponent)
/*     */   {
/* 449 */     if ((this.dragMode == 1) && (this.currentBounds != null)) {
/* 450 */       setBoundsForFrame(paramJComponent, this.currentBounds.x, this.currentBounds.y, this.currentBounds.width, this.currentBounds.height);
/* 451 */       this.currentBounds = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setBoundsForFrame(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 458 */     paramJComponent.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 460 */     paramJComponent.revalidate();
/*     */   }
/*     */ 
/*     */   protected void removeIconFor(JInternalFrame paramJInternalFrame)
/*     */   {
/* 465 */     JInternalFrame.JDesktopIcon localJDesktopIcon = paramJInternalFrame.getDesktopIcon();
/* 466 */     Container localContainer = localJDesktopIcon.getParent();
/* 467 */     if (localContainer != null) {
/* 468 */       localContainer.remove(localJDesktopIcon);
/* 469 */       localContainer.repaint(localJDesktopIcon.getX(), localJDesktopIcon.getY(), localJDesktopIcon.getWidth(), localJDesktopIcon.getHeight());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Rectangle getBoundsForIconOf(JInternalFrame paramJInternalFrame)
/*     */   {
/* 482 */     JInternalFrame.JDesktopIcon localJDesktopIcon1 = paramJInternalFrame.getDesktopIcon();
/* 483 */     Dimension localDimension = localJDesktopIcon1.getPreferredSize();
/*     */ 
/* 488 */     Container localContainer = paramJInternalFrame.getParent();
/* 489 */     if (localContainer == null) {
/* 490 */       localContainer = paramJInternalFrame.getDesktopIcon().getParent();
/*     */     }
/*     */ 
/* 493 */     if (localContainer == null)
/*     */     {
/* 495 */       return new Rectangle(0, 0, localDimension.width, localDimension.height);
/*     */     }
/*     */ 
/* 498 */     Rectangle localRectangle1 = localContainer.getBounds();
/* 499 */     Component[] arrayOfComponent = localContainer.getComponents();
/*     */ 
/* 507 */     Rectangle localRectangle2 = null;
/* 508 */     JInternalFrame.JDesktopIcon localJDesktopIcon2 = null;
/*     */ 
/* 510 */     int i = 0;
/* 511 */     int j = localRectangle1.height - localDimension.height;
/* 512 */     int k = localDimension.width;
/* 513 */     int m = localDimension.height;
/*     */ 
/* 515 */     int n = 0;
/*     */ 
/* 517 */     while (n == 0)
/*     */     {
/* 519 */       localRectangle2 = new Rectangle(i, j, k, m);
/*     */ 
/* 521 */       n = 1;
/*     */ 
/* 523 */       for (int i1 = 0; i1 < arrayOfComponent.length; i1++)
/*     */       {
/* 529 */         if ((arrayOfComponent[i1] instanceof JInternalFrame)) {
/* 530 */           localJDesktopIcon2 = ((JInternalFrame)arrayOfComponent[i1]).getDesktopIcon();
/*     */         } else {
/* 532 */           if (!(arrayOfComponent[i1] instanceof JInternalFrame.JDesktopIcon)) continue;
/* 533 */           localJDesktopIcon2 = (JInternalFrame.JDesktopIcon)arrayOfComponent[i1];
/*     */         }
/*     */ 
/* 546 */         if ((!localJDesktopIcon2.equals(localJDesktopIcon1)) && 
/* 547 */           (localRectangle2.intersects(localJDesktopIcon2.getBounds()))) {
/* 548 */           n = 0;
/* 549 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 554 */       if (localJDesktopIcon2 == null)
/*     */       {
/* 558 */         return localRectangle2;
/*     */       }
/* 560 */       i += localJDesktopIcon2.getBounds().width;
/*     */ 
/* 562 */       if (i + k > localRectangle1.width) {
/* 563 */         i = 0;
/* 564 */         j -= m;
/*     */       }
/*     */     }
/*     */ 
/* 568 */     return localRectangle2;
/*     */   }
/*     */ 
/*     */   protected void setPreviousBounds(JInternalFrame paramJInternalFrame, Rectangle paramRectangle)
/*     */   {
/* 577 */     paramJInternalFrame.setNormalBounds(paramRectangle);
/*     */   }
/*     */ 
/*     */   protected Rectangle getPreviousBounds(JInternalFrame paramJInternalFrame)
/*     */   {
/* 587 */     return paramJInternalFrame.getNormalBounds();
/*     */   }
/*     */ 
/*     */   protected void setWasIcon(JInternalFrame paramJInternalFrame, Boolean paramBoolean)
/*     */   {
/* 595 */     if (paramBoolean != null)
/* 596 */       paramJInternalFrame.putClientProperty("wasIconOnce", paramBoolean);
/*     */   }
/*     */ 
/*     */   protected boolean wasIcon(JInternalFrame paramJInternalFrame)
/*     */   {
/* 610 */     return paramJInternalFrame.getClientProperty("wasIconOnce") == Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   JDesktopPane getDesktopPane(JComponent paramJComponent)
/*     */   {
/* 615 */     JDesktopPane localJDesktopPane = null;
/* 616 */     Container localContainer = paramJComponent.getParent();
/*     */ 
/* 619 */     while (localJDesktopPane == null) {
/* 620 */       if ((localContainer instanceof JDesktopPane)) {
/* 621 */         localJDesktopPane = (JDesktopPane)localContainer;
/*     */       } else {
/* 623 */         if (localContainer == null)
/*     */         {
/*     */           break;
/*     */         }
/* 627 */         localContainer = localContainer.getParent();
/*     */       }
/*     */     }
/*     */ 
/* 631 */     return localJDesktopPane;
/*     */   }
/*     */ 
/*     */   private void dragFrameFaster(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 639 */     Rectangle localRectangle1 = new Rectangle(this.currentBounds.x, this.currentBounds.y, this.currentBounds.width, this.currentBounds.height);
/*     */ 
/* 645 */     this.currentBounds.x = paramInt1;
/* 646 */     this.currentBounds.y = paramInt2;
/*     */ 
/* 648 */     if (this.didDrag)
/*     */     {
/* 650 */       emergencyCleanup(paramJComponent);
/*     */     }
/*     */     else {
/* 653 */       this.didDrag = true;
/*     */ 
/* 656 */       ((JInternalFrame)paramJComponent).danger = false;
/*     */     }
/*     */ 
/* 659 */     boolean bool = isFloaterCollision(localRectangle1, this.currentBounds);
/*     */ 
/* 661 */     JComponent localJComponent = (JComponent)paramJComponent.getParent();
/* 662 */     Rectangle localRectangle2 = localRectangle1.intersection(this.desktopBounds);
/*     */ 
/* 664 */     RepaintManager localRepaintManager = RepaintManager.currentManager(paramJComponent);
/*     */ 
/* 666 */     localRepaintManager.beginPaint();
/*     */     try {
/* 668 */       if (!bool) {
/* 669 */         localRepaintManager.copyArea(localJComponent, this.desktopGraphics, localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height, paramInt1 - localRectangle1.x, paramInt2 - localRectangle1.y, true);
/*     */       }
/*     */ 
/* 678 */       paramJComponent.setBounds(this.currentBounds);
/*     */ 
/* 680 */       if (bool)
/*     */       {
/* 684 */         ((JInternalFrame)paramJComponent).isDragging = false;
/* 685 */         localJComponent.paintImmediately(this.currentBounds);
/* 686 */         ((JInternalFrame)paramJComponent).isDragging = true;
/*     */       }
/*     */ 
/* 691 */       localRepaintManager.markCompletelyClean(localJComponent);
/* 692 */       localRepaintManager.markCompletelyClean(paramJComponent);
/*     */ 
/* 697 */       localObject1 = null;
/* 698 */       if (localRectangle1.intersects(this.currentBounds)) {
/* 699 */         localObject1 = SwingUtilities.computeDifference(localRectangle1, this.currentBounds);
/*     */       }
/*     */       else {
/* 702 */         localObject1 = new Rectangle[1];
/* 703 */         localObject1[0] = localRectangle1;
/*     */       }
/*     */ 
/* 707 */       for (int i = 0; i < localObject1.length; i++) {
/* 708 */         localJComponent.paintImmediately(localObject1[i]);
/*     */       }
/*     */ 
/* 712 */       if (!localRectangle2.equals(localRectangle1)) {
/* 713 */         localObject1 = SwingUtilities.computeDifference(localRectangle1, this.desktopBounds);
/*     */ 
/* 715 */         for (i = 0; i < localObject1.length; i++) {
/* 716 */           localObject1[i].x += paramInt1 - localRectangle1.x;
/* 717 */           localObject1[i].y += paramInt2 - localRectangle1.y;
/* 718 */           ((JInternalFrame)paramJComponent).isDragging = false;
/*     */ 
/* 720 */           localJComponent.paintImmediately(localObject1[i]);
/* 721 */           ((JInternalFrame)paramJComponent).isDragging = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     finally {
/* 726 */       localRepaintManager.endPaint();
/*     */     }
/*     */ 
/* 730 */     Object localObject1 = SwingUtilities.getWindowAncestor(paramJComponent);
/* 731 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 732 */     if ((!((Window)localObject1).isOpaque()) && ((localToolkit instanceof SunToolkit)) && (((SunToolkit)localToolkit).needUpdateWindow()))
/*     */     {
/* 736 */       AWTAccessor.getWindowAccessor().updateWindow((Window)localObject1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isFloaterCollision(Rectangle paramRectangle1, Rectangle paramRectangle2) {
/* 741 */     if (this.floatingItems.length == 0)
/*     */     {
/* 743 */       return false;
/*     */     }
/*     */ 
/* 746 */     for (int i = 0; i < this.floatingItems.length; i++) {
/* 747 */       boolean bool1 = paramRectangle1.intersects(this.floatingItems[i]);
/* 748 */       if (bool1) {
/* 749 */         return true;
/*     */       }
/* 751 */       boolean bool2 = paramRectangle2.intersects(this.floatingItems[i]);
/* 752 */       if (bool2) {
/* 753 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 757 */     return false;
/*     */   }
/*     */ 
/*     */   private Rectangle[] findFloatingItems(JComponent paramJComponent) {
/* 761 */     Container localContainer = paramJComponent.getParent();
/* 762 */     Component[] arrayOfComponent = localContainer.getComponents();
/* 763 */     int i = 0;
/* 764 */     for (i = 0; (i < arrayOfComponent.length) && 
/* 765 */       (arrayOfComponent[i] != paramJComponent); i++);
/* 770 */     Rectangle[] arrayOfRectangle = new Rectangle[i];
/* 771 */     for (i = 0; i < arrayOfRectangle.length; i++) {
/* 772 */       arrayOfRectangle[i] = arrayOfComponent[i].getBounds();
/*     */     }
/*     */ 
/* 775 */     return arrayOfRectangle;
/*     */   }
/*     */ 
/*     */   private void emergencyCleanup(final JComponent paramJComponent)
/*     */   {
/* 786 */     if (((JInternalFrame)paramJComponent).danger)
/*     */     {
/* 788 */       SwingUtilities.invokeLater(new Runnable()
/*     */       {
/*     */         public void run() {
/* 791 */           ((JInternalFrame)paramJComponent).isDragging = false;
/* 792 */           paramJComponent.paintImmediately(0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 797 */           ((JInternalFrame)paramJComponent).isDragging = true;
/*     */         }
/*     */       });
/* 801 */       ((JInternalFrame)paramJComponent).danger = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultDesktopManager
 * JD-Core Version:    0.6.2
 */