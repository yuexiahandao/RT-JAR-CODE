/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Point;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class ViewportLayout
/*     */   implements LayoutManager, Serializable
/*     */ {
/*  61 */   static ViewportLayout SHARED_INSTANCE = new ViewportLayout();
/*     */ 
/*     */   public void addLayoutComponent(String paramString, Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/*  87 */     Component localComponent = ((JViewport)paramContainer).getView();
/*  88 */     if (localComponent == null) {
/*  89 */       return new Dimension(0, 0);
/*     */     }
/*  91 */     if ((localComponent instanceof Scrollable)) {
/*  92 */       return ((Scrollable)localComponent).getPreferredScrollableViewportSize();
/*     */     }
/*     */ 
/*  95 */     return localComponent.getPreferredSize();
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/* 110 */     return new Dimension(4, 4);
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 124 */     JViewport localJViewport = (JViewport)paramContainer;
/* 125 */     Component localComponent = localJViewport.getView();
/* 126 */     Scrollable localScrollable = null;
/*     */ 
/* 128 */     if (localComponent == null) {
/* 129 */       return;
/*     */     }
/* 131 */     if ((localComponent instanceof Scrollable)) {
/* 132 */       localScrollable = (Scrollable)localComponent;
/*     */     }
/*     */ 
/* 139 */     Insets localInsets = localJViewport.getInsets();
/* 140 */     Dimension localDimension1 = localComponent.getPreferredSize();
/* 141 */     Dimension localDimension2 = localJViewport.getSize();
/* 142 */     Dimension localDimension3 = localJViewport.toViewCoordinates(localDimension2);
/* 143 */     Dimension localDimension4 = new Dimension(localDimension1);
/*     */ 
/* 145 */     if (localScrollable != null) {
/* 146 */       if (localScrollable.getScrollableTracksViewportWidth()) {
/* 147 */         localDimension4.width = localDimension2.width;
/*     */       }
/* 149 */       if (localScrollable.getScrollableTracksViewportHeight()) {
/* 150 */         localDimension4.height = localDimension2.height;
/*     */       }
/*     */     }
/*     */ 
/* 154 */     Point localPoint = localJViewport.getViewPosition();
/*     */ 
/* 161 */     if ((localScrollable == null) || (localJViewport.getParent() == null) || (localJViewport.getParent().getComponentOrientation().isLeftToRight()))
/*     */     {
/* 164 */       if (localPoint.x + localDimension3.width > localDimension4.width) {
/* 165 */         localPoint.x = Math.max(0, localDimension4.width - localDimension3.width);
/*     */       }
/*     */     }
/* 168 */     else if (localDimension3.width > localDimension4.width)
/* 169 */       localPoint.x = (localDimension4.width - localDimension3.width);
/*     */     else {
/* 171 */       localPoint.x = Math.max(0, Math.min(localDimension4.width - localDimension3.width, localPoint.x));
/*     */     }
/*     */ 
/* 179 */     if (localPoint.y + localDimension3.height > localDimension4.height) {
/* 180 */       localPoint.y = Math.max(0, localDimension4.height - localDimension3.height);
/*     */     }
/*     */ 
/* 191 */     if (localScrollable == null) {
/* 192 */       if ((localPoint.x == 0) && (localDimension2.width > localDimension1.width)) {
/* 193 */         localDimension4.width = localDimension2.width;
/*     */       }
/* 195 */       if ((localPoint.y == 0) && (localDimension2.height > localDimension1.height)) {
/* 196 */         localDimension4.height = localDimension2.height;
/*     */       }
/*     */     }
/* 199 */     localJViewport.setViewPosition(localPoint);
/* 200 */     localJViewport.setViewSize(localDimension4);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ViewportLayout
 * JD-Core Version:    0.6.2
 */