/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicScrollPaneUI;
/*     */ 
/*     */ public class MetalScrollPaneUI extends BasicScrollPaneUI
/*     */ {
/*     */   private PropertyChangeListener scrollBarSwapListener;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  59 */     return new MetalScrollPaneUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  64 */     super.installUI(paramJComponent);
/*     */ 
/*  66 */     JScrollPane localJScrollPane = (JScrollPane)paramJComponent;
/*  67 */     updateScrollbarsFreeStanding();
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent) {
/*  71 */     super.uninstallUI(paramJComponent);
/*     */ 
/*  73 */     JScrollPane localJScrollPane = (JScrollPane)paramJComponent;
/*  74 */     JScrollBar localJScrollBar1 = localJScrollPane.getHorizontalScrollBar();
/*  75 */     JScrollBar localJScrollBar2 = localJScrollPane.getVerticalScrollBar();
/*  76 */     if (localJScrollBar1 != null) {
/*  77 */       localJScrollBar1.putClientProperty("JScrollBar.isFreeStanding", null);
/*     */     }
/*  79 */     if (localJScrollBar2 != null)
/*  80 */       localJScrollBar2.putClientProperty("JScrollBar.isFreeStanding", null);
/*     */   }
/*     */ 
/*     */   public void installListeners(JScrollPane paramJScrollPane)
/*     */   {
/*  85 */     super.installListeners(paramJScrollPane);
/*  86 */     this.scrollBarSwapListener = createScrollBarSwapListener();
/*  87 */     paramJScrollPane.addPropertyChangeListener(this.scrollBarSwapListener);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JComponent paramJComponent)
/*     */   {
/*  94 */     super.uninstallListeners(paramJComponent);
/*  95 */     paramJComponent.removePropertyChangeListener(this.scrollBarSwapListener);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void uninstallListeners(JScrollPane paramJScrollPane)
/*     */   {
/* 103 */     super.uninstallListeners(paramJScrollPane);
/* 104 */     paramJScrollPane.removePropertyChangeListener(this.scrollBarSwapListener);
/*     */   }
/*     */ 
/*     */   private void updateScrollbarsFreeStanding()
/*     */   {
/* 114 */     if (this.scrollpane == null) {
/* 115 */       return;
/*     */     }
/* 117 */     Border localBorder = this.scrollpane.getBorder();
/*     */     Boolean localBoolean;
/* 120 */     if ((localBorder instanceof MetalBorders.ScrollPaneBorder)) {
/* 121 */       localBoolean = Boolean.FALSE;
/*     */     }
/*     */     else {
/* 124 */       localBoolean = Boolean.TRUE;
/*     */     }
/* 126 */     JScrollBar localJScrollBar = this.scrollpane.getHorizontalScrollBar();
/* 127 */     if (localJScrollBar != null) {
/* 128 */       localJScrollBar.putClientProperty("JScrollBar.isFreeStanding", localBoolean);
/*     */     }
/*     */ 
/* 131 */     localJScrollBar = this.scrollpane.getVerticalScrollBar();
/* 132 */     if (localJScrollBar != null)
/* 133 */       localJScrollBar.putClientProperty("JScrollBar.isFreeStanding", localBoolean);
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createScrollBarSwapListener()
/*     */   {
/* 139 */     return new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/* 141 */         String str = paramAnonymousPropertyChangeEvent.getPropertyName();
/* 142 */         if ((str.equals("verticalScrollBar")) || (str.equals("horizontalScrollBar")))
/*     */         {
/* 144 */           JScrollBar localJScrollBar1 = (JScrollBar)paramAnonymousPropertyChangeEvent.getOldValue();
/* 145 */           if (localJScrollBar1 != null) {
/* 146 */             localJScrollBar1.putClientProperty("JScrollBar.isFreeStanding", null);
/*     */           }
/*     */ 
/* 149 */           JScrollBar localJScrollBar2 = (JScrollBar)paramAnonymousPropertyChangeEvent.getNewValue();
/* 150 */           if (localJScrollBar2 != null) {
/* 151 */             localJScrollBar2.putClientProperty("JScrollBar.isFreeStanding", Boolean.FALSE);
/*     */           }
/*     */ 
/*     */         }
/* 156 */         else if ("border".equals(str)) {
/* 157 */           MetalScrollPaneUI.this.updateScrollbarsFreeStanding();
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalScrollPaneUI
 * JD-Core Version:    0.6.2
 */