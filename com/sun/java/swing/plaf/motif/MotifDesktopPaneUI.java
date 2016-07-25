/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.DefaultDesktopManager;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicDesktopPaneUI;
/*     */ 
/*     */ public class MotifDesktopPaneUI extends BasicDesktopPaneUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  55 */     return new MotifDesktopPaneUI();
/*     */   }
/*     */ 
/*     */   protected void installDesktopManager()
/*     */   {
/*  62 */     this.desktopManager = this.desktop.getDesktopManager();
/*  63 */     if (this.desktopManager == null) {
/*  64 */       this.desktopManager = new MotifDesktopManager(null);
/*  65 */       this.desktop.setDesktopManager(this.desktopManager);
/*  66 */       ((MotifDesktopManager)this.desktopManager).adjustIcons(this.desktop);
/*     */     }
/*     */   }
/*     */ 
/*  70 */   public Insets getInsets(JComponent paramJComponent) { return new Insets(0, 0, 0, 0); }
/*     */ 
/*     */   private class DragPane extends JComponent {
/*     */     private DragPane() {
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics) {
/*  77 */       paramGraphics.setColor(Color.darkGray);
/*  78 */       paramGraphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
/*     */     }
/*     */   }
/*     */   private class MotifDesktopManager extends DefaultDesktopManager implements Serializable, UIResource {
/*     */     JComponent dragPane;
/*  87 */     boolean usingDragPane = false;
/*     */     private transient JLayeredPane layeredPaneForDragPane;
/*     */     int iconWidth;
/*     */     int iconHeight;
/*     */ 
/*     */     private MotifDesktopManager() {
/*     */     }
/*  94 */     public void setBoundsForFrame(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4) { if (!this.usingDragPane)
/*     */       {
/*  96 */         int i = (paramJComponent.getWidth() != paramInt3) || (paramJComponent.getHeight() != paramInt4) ? 1 : 0;
/*  97 */         Rectangle localRectangle2 = paramJComponent.getBounds();
/*  98 */         paramJComponent.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*  99 */         SwingUtilities.computeUnion(paramInt1, paramInt2, paramInt3, paramInt4, localRectangle2);
/* 100 */         paramJComponent.getParent().repaint(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height);
/* 101 */         if (i != 0)
/* 102 */           paramJComponent.validate();
/*     */       }
/*     */       else {
/* 105 */         Rectangle localRectangle1 = this.dragPane.getBounds();
/* 106 */         this.dragPane.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/* 107 */         SwingUtilities.computeUnion(paramInt1, paramInt2, paramInt3, paramInt4, localRectangle1);
/* 108 */         this.dragPane.getParent().repaint(localRectangle1.x, localRectangle1.y, localRectangle1.width, localRectangle1.height);
/*     */       } }
/*     */ 
/*     */     public void beginDraggingFrame(JComponent paramJComponent)
/*     */     {
/* 113 */       this.usingDragPane = false;
/* 114 */       if ((paramJComponent.getParent() instanceof JLayeredPane)) {
/* 115 */         if (this.dragPane == null)
/* 116 */           this.dragPane = new MotifDesktopPaneUI.DragPane(MotifDesktopPaneUI.this, null);
/* 117 */         this.layeredPaneForDragPane = ((JLayeredPane)paramJComponent.getParent());
/* 118 */         this.layeredPaneForDragPane.setLayer(this.dragPane, 2147483647);
/* 119 */         this.dragPane.setBounds(paramJComponent.getX(), paramJComponent.getY(), paramJComponent.getWidth(), paramJComponent.getHeight());
/* 120 */         this.layeredPaneForDragPane.add(this.dragPane);
/* 121 */         this.usingDragPane = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void dragFrame(JComponent paramJComponent, int paramInt1, int paramInt2) {
/* 126 */       setBoundsForFrame(paramJComponent, paramInt1, paramInt2, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */     }
/*     */ 
/*     */     public void endDraggingFrame(JComponent paramJComponent) {
/* 130 */       if (this.usingDragPane) {
/* 131 */         this.layeredPaneForDragPane.remove(this.dragPane);
/* 132 */         this.usingDragPane = false;
/* 133 */         if ((paramJComponent instanceof JInternalFrame)) {
/* 134 */           setBoundsForFrame(paramJComponent, this.dragPane.getX(), this.dragPane.getY(), this.dragPane.getWidth(), this.dragPane.getHeight());
/*     */         }
/* 136 */         else if ((paramJComponent instanceof JInternalFrame.JDesktopIcon))
/* 137 */           adjustBoundsForIcon((JInternalFrame.JDesktopIcon)paramJComponent, this.dragPane.getX(), this.dragPane.getY());
/*     */       }
/*     */     }
/*     */ 
/*     */     public void beginResizingFrame(JComponent paramJComponent, int paramInt)
/*     */     {
/* 144 */       this.usingDragPane = false;
/* 145 */       if ((paramJComponent.getParent() instanceof JLayeredPane)) {
/* 146 */         if (this.dragPane == null)
/* 147 */           this.dragPane = new MotifDesktopPaneUI.DragPane(MotifDesktopPaneUI.this, null);
/* 148 */         JLayeredPane localJLayeredPane = (JLayeredPane)paramJComponent.getParent();
/* 149 */         localJLayeredPane.setLayer(this.dragPane, 2147483647);
/* 150 */         this.dragPane.setBounds(paramJComponent.getX(), paramJComponent.getY(), paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 152 */         localJLayeredPane.add(this.dragPane);
/* 153 */         this.usingDragPane = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void resizeFrame(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 159 */       setBoundsForFrame(paramJComponent, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     public void endResizingFrame(JComponent paramJComponent) {
/* 163 */       if (this.usingDragPane) {
/* 164 */         JLayeredPane localJLayeredPane = (JLayeredPane)paramJComponent.getParent();
/* 165 */         localJLayeredPane.remove(this.dragPane);
/* 166 */         this.usingDragPane = false;
/* 167 */         setBoundsForFrame(paramJComponent, this.dragPane.getX(), this.dragPane.getY(), this.dragPane.getWidth(), this.dragPane.getHeight());
/*     */       }
/*     */     }
/*     */ 
/*     */     public void iconifyFrame(JInternalFrame paramJInternalFrame)
/*     */     {
/* 173 */       JInternalFrame.JDesktopIcon localJDesktopIcon = paramJInternalFrame.getDesktopIcon();
/* 174 */       Point localPoint = localJDesktopIcon.getLocation();
/* 175 */       adjustBoundsForIcon(localJDesktopIcon, localPoint.x, localPoint.y);
/* 176 */       super.iconifyFrame(paramJInternalFrame);
/*     */     }
/*     */ 
/*     */     protected void adjustIcons(JDesktopPane paramJDesktopPane)
/*     */     {
/* 185 */       JInternalFrame.JDesktopIcon localJDesktopIcon = new JInternalFrame.JDesktopIcon(new JInternalFrame());
/*     */ 
/* 187 */       Dimension localDimension = localJDesktopIcon.getPreferredSize();
/* 188 */       this.iconWidth = localDimension.width;
/* 189 */       this.iconHeight = localDimension.height;
/*     */ 
/* 191 */       JInternalFrame[] arrayOfJInternalFrame = paramJDesktopPane.getAllFrames();
/* 192 */       for (int i = 0; i < arrayOfJInternalFrame.length; i++) {
/* 193 */         localJDesktopIcon = arrayOfJInternalFrame[i].getDesktopIcon();
/* 194 */         Point localPoint = localJDesktopIcon.getLocation();
/* 195 */         adjustBoundsForIcon(localJDesktopIcon, localPoint.x, localPoint.y);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void adjustBoundsForIcon(JInternalFrame.JDesktopIcon paramJDesktopIcon, int paramInt1, int paramInt2)
/*     */     {
/* 205 */       JDesktopPane localJDesktopPane = paramJDesktopIcon.getDesktopPane();
/*     */ 
/* 207 */       int i = localJDesktopPane.getHeight();
/* 208 */       int j = this.iconWidth;
/* 209 */       int k = this.iconHeight;
/* 210 */       localJDesktopPane.repaint(paramInt1, paramInt2, j, k);
/* 211 */       paramInt1 = paramInt1 < 0 ? 0 : paramInt1;
/* 212 */       paramInt2 = paramInt2 < 0 ? 0 : paramInt2;
/*     */ 
/* 217 */       paramInt2 = paramInt2 >= i ? i - 1 : paramInt2;
/*     */ 
/* 220 */       int m = paramInt1 / j * j;
/* 221 */       int n = i % k;
/* 222 */       int i1 = (paramInt2 - n) / k * k + n;
/*     */ 
/* 225 */       int i2 = paramInt1 - m;
/* 226 */       int i3 = paramInt2 - i1;
/*     */ 
/* 229 */       paramInt1 = i2 < j / 2 ? m : m + j;
/* 230 */       paramInt2 = i1 + k < i ? i1 + k : i3 < k / 2 ? i1 : i1;
/*     */ 
/* 232 */       while (getIconAt(localJDesktopPane, paramJDesktopIcon, paramInt1, paramInt2) != null) {
/* 233 */         paramInt1 += j;
/*     */       }
/*     */ 
/* 237 */       if (paramInt1 > localJDesktopPane.getWidth()) {
/* 238 */         return;
/*     */       }
/* 240 */       if (paramJDesktopIcon.getParent() != null)
/* 241 */         setBoundsForFrame(paramJDesktopIcon, paramInt1, paramInt2, j, k);
/*     */       else
/* 243 */         paramJDesktopIcon.setLocation(paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     protected JInternalFrame.JDesktopIcon getIconAt(JDesktopPane paramJDesktopPane, JInternalFrame.JDesktopIcon paramJDesktopIcon, int paramInt1, int paramInt2)
/*     */     {
/* 250 */       Object localObject = null;
/* 251 */       Component[] arrayOfComponent = paramJDesktopPane.getComponents();
/*     */ 
/* 253 */       for (int i = 0; i < arrayOfComponent.length; i++) {
/* 254 */         Component localComponent = arrayOfComponent[i];
/* 255 */         if (((localComponent instanceof JInternalFrame.JDesktopIcon)) && (localComponent != paramJDesktopIcon))
/*     */         {
/* 258 */           Point localPoint = localComponent.getLocation();
/* 259 */           if ((localPoint.x == paramInt1) && (localPoint.y == paramInt2)) {
/* 260 */             return (JInternalFrame.JDesktopIcon)localComponent;
/*     */           }
/*     */         }
/*     */       }
/* 264 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifDesktopPaneUI
 * JD-Core Version:    0.6.2
 */