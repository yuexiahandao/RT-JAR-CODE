/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneDivider;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneDivider.DragController;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneDivider.MouseHandler;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*     */ 
/*     */ public class MotifSplitPaneDivider extends BasicSplitPaneDivider
/*     */ {
/*  54 */   private static final Cursor defaultCursor = Cursor.getPredefinedCursor(0);
/*     */   public static final int minimumThumbSize = 6;
/*     */   public static final int defaultDividerSize = 18;
/*     */   protected static final int pad = 6;
/*  63 */   private int hThumbOffset = 30;
/*  64 */   private int vThumbOffset = 40;
/*  65 */   protected int hThumbWidth = 12;
/*  66 */   protected int hThumbHeight = 18;
/*  67 */   protected int vThumbWidth = 18;
/*  68 */   protected int vThumbHeight = 12;
/*     */   protected Color highlightColor;
/*     */   protected Color shadowColor;
/*     */   protected Color focusedColor;
/*     */ 
/*     */   public MotifSplitPaneDivider(BasicSplitPaneUI paramBasicSplitPaneUI)
/*     */   {
/*  78 */     super(paramBasicSplitPaneUI);
/*  79 */     this.highlightColor = UIManager.getColor("SplitPane.highlight");
/*  80 */     this.shadowColor = UIManager.getColor("SplitPane.shadow");
/*  81 */     this.focusedColor = UIManager.getColor("SplitPane.activeThumb");
/*  82 */     setDividerSize(this.hThumbWidth + 6);
/*     */   }
/*     */ 
/*     */   public void setDividerSize(int paramInt)
/*     */   {
/*  90 */     Insets localInsets = getInsets();
/*  91 */     int i = 0;
/*  92 */     if (getBasicSplitPaneUI().getOrientation() == 1)
/*     */     {
/*  94 */       if (localInsets != null) {
/*  95 */         i = localInsets.left + localInsets.right;
/*     */       }
/*     */     }
/*  98 */     else if (localInsets != null) {
/*  99 */       i = localInsets.top + localInsets.bottom;
/*     */     }
/* 101 */     if (paramInt < 12 + i) {
/* 102 */       setDividerSize(12 + i);
/*     */     } else {
/* 104 */       this.vThumbHeight = (this.hThumbWidth = paramInt - 6 - i);
/* 105 */       super.setDividerSize(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/* 115 */     Color localColor = getBackground();
/* 116 */     Dimension localDimension = getSize();
/*     */ 
/* 119 */     paramGraphics.setColor(getBackground());
/* 120 */     paramGraphics.fillRect(0, 0, localDimension.width, localDimension.height);
/*     */     int i;
/*     */     int j;
/*     */     int k;
/* 122 */     if (getBasicSplitPaneUI().getOrientation() == 1)
/*     */     {
/* 124 */       i = localDimension.width / 2;
/* 125 */       j = i - this.hThumbWidth / 2;
/* 126 */       k = this.hThumbOffset;
/*     */ 
/* 129 */       paramGraphics.setColor(this.shadowColor);
/* 130 */       paramGraphics.drawLine(i - 1, 0, i - 1, localDimension.height);
/*     */ 
/* 132 */       paramGraphics.setColor(this.highlightColor);
/* 133 */       paramGraphics.drawLine(i, 0, i, localDimension.height);
/*     */ 
/* 136 */       paramGraphics.setColor(this.splitPane.hasFocus() ? this.focusedColor : getBackground());
/*     */ 
/* 138 */       paramGraphics.fillRect(j + 1, k + 1, this.hThumbWidth - 2, this.hThumbHeight - 1);
/*     */ 
/* 140 */       paramGraphics.setColor(this.highlightColor);
/* 141 */       paramGraphics.drawLine(j, k, j + this.hThumbWidth - 1, k);
/* 142 */       paramGraphics.drawLine(j, k + 1, j, k + this.hThumbHeight - 1);
/*     */ 
/* 144 */       paramGraphics.setColor(this.shadowColor);
/* 145 */       paramGraphics.drawLine(j + 1, k + this.hThumbHeight - 1, j + this.hThumbWidth - 1, k + this.hThumbHeight - 1);
/*     */ 
/* 147 */       paramGraphics.drawLine(j + this.hThumbWidth - 1, k + 1, j + this.hThumbWidth - 1, k + this.hThumbHeight - 2);
/*     */     }
/*     */     else
/*     */     {
/* 151 */       i = localDimension.height / 2;
/* 152 */       j = localDimension.width - this.vThumbOffset;
/* 153 */       k = localDimension.height / 2 - this.vThumbHeight / 2;
/*     */ 
/* 156 */       paramGraphics.setColor(this.shadowColor);
/* 157 */       paramGraphics.drawLine(0, i - 1, localDimension.width, i - 1);
/*     */ 
/* 159 */       paramGraphics.setColor(this.highlightColor);
/* 160 */       paramGraphics.drawLine(0, i, localDimension.width, i);
/*     */ 
/* 163 */       paramGraphics.setColor(this.splitPane.hasFocus() ? this.focusedColor : getBackground());
/*     */ 
/* 165 */       paramGraphics.fillRect(j + 1, k + 1, this.vThumbWidth - 1, this.vThumbHeight - 1);
/*     */ 
/* 167 */       paramGraphics.setColor(this.highlightColor);
/* 168 */       paramGraphics.drawLine(j, k, j + this.vThumbWidth, k);
/* 169 */       paramGraphics.drawLine(j, k + 1, j, k + this.vThumbHeight);
/*     */ 
/* 171 */       paramGraphics.setColor(this.shadowColor);
/* 172 */       paramGraphics.drawLine(j + 1, k + this.vThumbHeight, j + this.vThumbWidth, k + this.vThumbHeight);
/*     */ 
/* 174 */       paramGraphics.drawLine(j + this.vThumbWidth, k + 1, j + this.vThumbWidth, k + this.vThumbHeight - 1);
/*     */     }
/*     */ 
/* 177 */     super.paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 185 */     return getPreferredSize();
/*     */   }
/*     */ 
/*     */   public void setBasicSplitPaneUI(BasicSplitPaneUI paramBasicSplitPaneUI)
/*     */   {
/* 193 */     if (this.splitPane != null) {
/* 194 */       this.splitPane.removePropertyChangeListener(this);
/* 195 */       if (this.mouseHandler != null) {
/* 196 */         this.splitPane.removeMouseListener(this.mouseHandler);
/* 197 */         this.splitPane.removeMouseMotionListener(this.mouseHandler);
/* 198 */         removeMouseListener(this.mouseHandler);
/* 199 */         removeMouseMotionListener(this.mouseHandler);
/* 200 */         this.mouseHandler = null;
/*     */       }
/*     */     }
/* 203 */     this.splitPaneUI = paramBasicSplitPaneUI;
/* 204 */     if (paramBasicSplitPaneUI != null) {
/* 205 */       this.splitPane = paramBasicSplitPaneUI.getSplitPane();
/* 206 */       if (this.splitPane != null) {
/* 207 */         if (this.mouseHandler == null) this.mouseHandler = new MotifMouseHandler(null);
/* 208 */         this.splitPane.addMouseListener(this.mouseHandler);
/* 209 */         this.splitPane.addMouseMotionListener(this.mouseHandler);
/* 210 */         addMouseListener(this.mouseHandler);
/* 211 */         addMouseMotionListener(this.mouseHandler);
/* 212 */         this.splitPane.addPropertyChangeListener(this);
/* 213 */         if (this.splitPane.isOneTouchExpandable())
/* 214 */           oneTouchExpandableChanged();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 219 */       this.splitPane = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isInThumb(int paramInt1, int paramInt2)
/*     */   {
/* 228 */     Dimension localDimension = getSize();
/*     */     int n;
/*     */     int i;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/* 234 */     if (getBasicSplitPaneUI().getOrientation() == 1)
/*     */     {
/* 236 */       n = localDimension.width / 2;
/* 237 */       i = n - this.hThumbWidth / 2;
/* 238 */       j = this.hThumbOffset;
/* 239 */       k = this.hThumbWidth;
/* 240 */       m = this.hThumbHeight;
/*     */     }
/*     */     else {
/* 243 */       n = localDimension.height / 2;
/* 244 */       i = localDimension.width - this.vThumbOffset;
/* 245 */       j = localDimension.height / 2 - this.vThumbHeight / 2;
/* 246 */       k = this.vThumbWidth;
/* 247 */       m = this.vThumbHeight;
/*     */     }
/* 249 */     return (paramInt1 >= i) && (paramInt1 < i + k) && (paramInt2 >= j) && (paramInt2 < j + m);
/*     */   }
/*     */ 
/*     */   private BasicSplitPaneDivider.DragController getDragger()
/*     */   {
/* 259 */     return this.dragger;
/*     */   }
/*     */ 
/*     */   private JSplitPane getSplitPane() {
/* 263 */     return this.splitPane;
/*     */   }
/*     */ 
/*     */   private class MotifMouseHandler extends BasicSplitPaneDivider.MouseHandler
/*     */   {
/*     */     private MotifMouseHandler()
/*     */     {
/* 272 */       super();
/*     */     }
/*     */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 275 */       if ((paramMouseEvent.getSource() == MotifSplitPaneDivider.this) && (MotifSplitPaneDivider.this.getDragger() == null) && (MotifSplitPaneDivider.this.getSplitPane().isEnabled()) && (MotifSplitPaneDivider.this.isInThumb(paramMouseEvent.getX(), paramMouseEvent.getY())))
/*     */       {
/* 278 */         super.mousePressed(paramMouseEvent);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent) {
/* 283 */       if (MotifSplitPaneDivider.this.getDragger() != null) {
/* 284 */         return;
/*     */       }
/* 286 */       if (!MotifSplitPaneDivider.this.isInThumb(paramMouseEvent.getX(), paramMouseEvent.getY())) {
/* 287 */         if (MotifSplitPaneDivider.this.getCursor() != MotifSplitPaneDivider.defaultCursor) {
/* 288 */           MotifSplitPaneDivider.this.setCursor(MotifSplitPaneDivider.defaultCursor);
/*     */         }
/* 290 */         return;
/*     */       }
/* 292 */       super.mouseMoved(paramMouseEvent);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifSplitPaneDivider
 * JD-Core Version:    0.6.2
 */