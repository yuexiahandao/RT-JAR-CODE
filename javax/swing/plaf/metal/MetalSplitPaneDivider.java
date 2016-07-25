/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneDivider;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*     */ 
/*     */ class MetalSplitPaneDivider extends BasicSplitPaneDivider
/*     */ {
/*  51 */   private MetalBumps bumps = new MetalBumps(10, 10, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), MetalLookAndFeel.getControl());
/*     */ 
/*  56 */   private MetalBumps focusBumps = new MetalBumps(10, 10, MetalLookAndFeel.getPrimaryControlHighlight(), MetalLookAndFeel.getPrimaryControlDarkShadow(), UIManager.getColor("SplitPane.dividerFocusColor"));
/*     */ 
/*  61 */   private int inset = 2;
/*     */ 
/*  63 */   private Color controlColor = MetalLookAndFeel.getControl();
/*  64 */   private Color primaryControlColor = UIManager.getColor("SplitPane.dividerFocusColor");
/*     */ 
/*     */   public MetalSplitPaneDivider(BasicSplitPaneUI paramBasicSplitPaneUI)
/*     */   {
/*  68 */     super(paramBasicSplitPaneUI);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/*     */     MetalBumps localMetalBumps;
/*  73 */     if (this.splitPane.hasFocus()) {
/*  74 */       localMetalBumps = this.focusBumps;
/*  75 */       paramGraphics.setColor(this.primaryControlColor);
/*     */     }
/*     */     else {
/*  78 */       localMetalBumps = this.bumps;
/*  79 */       paramGraphics.setColor(this.controlColor);
/*     */     }
/*  81 */     Rectangle localRectangle = paramGraphics.getClipBounds();
/*  82 */     Insets localInsets = getInsets();
/*  83 */     paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*  84 */     Dimension localDimension = getSize();
/*  85 */     localDimension.width -= this.inset * 2;
/*  86 */     localDimension.height -= this.inset * 2;
/*  87 */     int i = this.inset;
/*  88 */     int j = this.inset;
/*  89 */     if (localInsets != null) {
/*  90 */       localDimension.width -= localInsets.left + localInsets.right;
/*  91 */       localDimension.height -= localInsets.top + localInsets.bottom;
/*  92 */       i += localInsets.left;
/*  93 */       j += localInsets.top;
/*     */     }
/*  95 */     localMetalBumps.setBumpArea(localDimension);
/*  96 */     localMetalBumps.paintIcon(this, paramGraphics, i, j);
/*  97 */     super.paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   protected JButton createLeftOneTouchButton()
/*     */   {
/* 105 */     JButton local1 = new JButton()
/*     */     {
/* 107 */       int[][] buffer = { { 0, 0, 0, 2, 2, 0, 0, 0, 0 }, { 0, 0, 2, 1, 1, 1, 0, 0, 0 }, { 0, 2, 1, 1, 1, 1, 1, 0, 0 }, { 2, 1, 1, 1, 1, 1, 1, 1, 0 }, { 0, 3, 3, 3, 3, 3, 3, 3, 3 } };
/*     */ 
/*     */       public void setBorder(Border paramAnonymousBorder)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void paint(Graphics paramAnonymousGraphics)
/*     */       {
/* 117 */         JSplitPane localJSplitPane = MetalSplitPaneDivider.this.getSplitPaneFromSuper();
/* 118 */         if (localJSplitPane != null) {
/* 119 */           int i = MetalSplitPaneDivider.this.getOneTouchSizeFromSuper();
/* 120 */           int j = MetalSplitPaneDivider.this.getOrientationFromSuper();
/* 121 */           int k = Math.min(MetalSplitPaneDivider.this.getDividerSize(), i);
/*     */ 
/* 125 */           Color[] arrayOfColor = { getBackground(), MetalLookAndFeel.getPrimaryControlDarkShadow(), MetalLookAndFeel.getPrimaryControlInfo(), MetalLookAndFeel.getPrimaryControlHighlight() };
/*     */ 
/* 132 */           paramAnonymousGraphics.setColor(getBackground());
/* 133 */           if (isOpaque()) {
/* 134 */             paramAnonymousGraphics.fillRect(0, 0, getWidth(), getHeight());
/*     */           }
/*     */ 
/* 139 */           if (getModel().isPressed())
/*     */           {
/* 141 */             arrayOfColor[1] = arrayOfColor[2];
/*     */           }
/*     */           int m;
/*     */           int n;
/* 143 */           if (j == 0)
/*     */           {
/* 145 */             for (m = 1; m <= this.buffer[0].length; m++) {
/* 146 */               for (n = 1; n < k; n++) {
/* 147 */                 if (this.buffer[(n - 1)][(m - 1)] != 0)
/*     */                 {
/* 151 */                   paramAnonymousGraphics.setColor(arrayOfColor[this.buffer[(n - 1)][(m - 1)]]);
/*     */ 
/* 154 */                   paramAnonymousGraphics.drawLine(m, n, m, n);
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 165 */             for (m = 1; m <= this.buffer[0].length; m++)
/* 166 */               for (n = 1; n < k; n++)
/* 167 */                 if (this.buffer[(n - 1)][(m - 1)] != 0)
/*     */                 {
/* 175 */                   paramAnonymousGraphics.setColor(arrayOfColor[this.buffer[(n - 1)][(m - 1)]]);
/*     */ 
/* 179 */                   paramAnonymousGraphics.drawLine(n, m, n, m);
/*     */                 }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */       public boolean isFocusTraversable()
/*     */       {
/* 188 */         return false;
/*     */       }
/*     */     };
/* 191 */     local1.setRequestFocusEnabled(false);
/* 192 */     local1.setCursor(Cursor.getPredefinedCursor(0));
/* 193 */     local1.setFocusPainted(false);
/* 194 */     local1.setBorderPainted(false);
/* 195 */     maybeMakeButtonOpaque(local1);
/* 196 */     return local1;
/*     */   }
/*     */ 
/*     */   private void maybeMakeButtonOpaque(JComponent paramJComponent)
/*     */   {
/* 203 */     Object localObject = UIManager.get("SplitPane.oneTouchButtonsOpaque");
/* 204 */     if (localObject != null)
/* 205 */       paramJComponent.setOpaque(((Boolean)localObject).booleanValue());
/*     */   }
/*     */ 
/*     */   protected JButton createRightOneTouchButton()
/*     */   {
/* 214 */     JButton local2 = new JButton()
/*     */     {
/* 216 */       int[][] buffer = { { 2, 2, 2, 2, 2, 2, 2, 2 }, { 0, 1, 1, 1, 1, 1, 1, 3 }, { 0, 0, 1, 1, 1, 1, 3, 0 }, { 0, 0, 0, 1, 1, 3, 0, 0 }, { 0, 0, 0, 0, 3, 0, 0, 0 } };
/*     */ 
/*     */       public void setBorder(Border paramAnonymousBorder)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void paint(Graphics paramAnonymousGraphics)
/*     */       {
/* 226 */         JSplitPane localJSplitPane = MetalSplitPaneDivider.this.getSplitPaneFromSuper();
/* 227 */         if (localJSplitPane != null) {
/* 228 */           int i = MetalSplitPaneDivider.this.getOneTouchSizeFromSuper();
/* 229 */           int j = MetalSplitPaneDivider.this.getOrientationFromSuper();
/* 230 */           int k = Math.min(MetalSplitPaneDivider.this.getDividerSize(), i);
/*     */ 
/* 234 */           Color[] arrayOfColor = { getBackground(), MetalLookAndFeel.getPrimaryControlDarkShadow(), MetalLookAndFeel.getPrimaryControlInfo(), MetalLookAndFeel.getPrimaryControlHighlight() };
/*     */ 
/* 241 */           paramAnonymousGraphics.setColor(getBackground());
/* 242 */           if (isOpaque()) {
/* 243 */             paramAnonymousGraphics.fillRect(0, 0, getWidth(), getHeight());
/*     */           }
/*     */ 
/* 248 */           if (getModel().isPressed())
/*     */           {
/* 250 */             arrayOfColor[1] = arrayOfColor[2];
/*     */           }
/*     */           int m;
/*     */           int n;
/* 252 */           if (j == 0)
/*     */           {
/* 254 */             for (m = 1; m <= this.buffer[0].length; m++) {
/* 255 */               for (n = 1; n < k; n++) {
/* 256 */                 if (this.buffer[(n - 1)][(m - 1)] != 0)
/*     */                 {
/* 260 */                   paramAnonymousGraphics.setColor(arrayOfColor[this.buffer[(n - 1)][(m - 1)]]);
/*     */ 
/* 263 */                   paramAnonymousGraphics.drawLine(m, n, m, n);
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 274 */             for (m = 1; m <= this.buffer[0].length; m++)
/* 275 */               for (n = 1; n < k; n++)
/* 276 */                 if (this.buffer[(n - 1)][(m - 1)] != 0)
/*     */                 {
/* 284 */                   paramAnonymousGraphics.setColor(arrayOfColor[this.buffer[(n - 1)][(m - 1)]]);
/*     */ 
/* 288 */                   paramAnonymousGraphics.drawLine(n, m, n, m);
/*     */                 }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */       public boolean isFocusTraversable()
/*     */       {
/* 297 */         return false;
/*     */       }
/*     */     };
/* 300 */     local2.setCursor(Cursor.getPredefinedCursor(0));
/* 301 */     local2.setFocusPainted(false);
/* 302 */     local2.setBorderPainted(false);
/* 303 */     local2.setRequestFocusEnabled(false);
/* 304 */     maybeMakeButtonOpaque(local2);
/* 305 */     return local2;
/*     */   }
/*     */ 
/*     */   int getOneTouchSizeFromSuper()
/*     */   {
/* 394 */     return 6;
/*     */   }
/*     */ 
/*     */   int getOneTouchOffsetFromSuper() {
/* 398 */     return 2;
/*     */   }
/*     */ 
/*     */   int getOrientationFromSuper() {
/* 402 */     return this.orientation;
/*     */   }
/*     */ 
/*     */   JSplitPane getSplitPaneFromSuper() {
/* 406 */     return this.splitPane;
/*     */   }
/*     */ 
/*     */   JButton getLeftButtonFromSuper() {
/* 410 */     return this.leftButton;
/*     */   }
/*     */ 
/*     */   JButton getRightButtonFromSuper() {
/* 414 */     return this.rightButton;
/*     */   }
/*     */ 
/*     */   public class MetalDividerLayout
/*     */     implements LayoutManager
/*     */   {
/*     */     public MetalDividerLayout()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer)
/*     */     {
/* 323 */       JButton localJButton1 = MetalSplitPaneDivider.this.getLeftButtonFromSuper();
/* 324 */       JButton localJButton2 = MetalSplitPaneDivider.this.getRightButtonFromSuper();
/* 325 */       JSplitPane localJSplitPane = MetalSplitPaneDivider.this.getSplitPaneFromSuper();
/* 326 */       int i = MetalSplitPaneDivider.this.getOrientationFromSuper();
/* 327 */       int j = MetalSplitPaneDivider.this.getOneTouchSizeFromSuper();
/* 328 */       int k = MetalSplitPaneDivider.this.getOneTouchOffsetFromSuper();
/* 329 */       Insets localInsets = MetalSplitPaneDivider.this.getInsets();
/*     */ 
/* 335 */       if ((localJButton1 != null) && (localJButton2 != null) && (paramContainer == MetalSplitPaneDivider.this))
/*     */       {
/* 337 */         if (localJSplitPane.isOneTouchExpandable())
/*     */         {
/*     */           int m;
/*     */           int n;
/* 338 */           if (i == 0) {
/* 339 */             m = localInsets != null ? localInsets.top : 0;
/* 340 */             n = MetalSplitPaneDivider.this.getDividerSize();
/*     */ 
/* 342 */             if (localInsets != null) {
/* 343 */               n -= localInsets.top + localInsets.bottom;
/*     */             }
/* 345 */             n = Math.min(n, j);
/* 346 */             localJButton1.setBounds(k, m, n * 2, n);
/*     */ 
/* 348 */             localJButton2.setBounds(k + j * 2, m, n * 2, n);
/*     */           }
/*     */           else
/*     */           {
/* 353 */             m = MetalSplitPaneDivider.this.getDividerSize();
/* 354 */             n = localInsets != null ? localInsets.left : 0;
/*     */ 
/* 356 */             if (localInsets != null) {
/* 357 */               m -= localInsets.left + localInsets.right;
/*     */             }
/* 359 */             m = Math.min(m, j);
/* 360 */             localJButton1.setBounds(n, k, m, m * 2);
/*     */ 
/* 362 */             localJButton2.setBounds(n, k + j * 2, m, m * 2);
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 368 */           localJButton1.setBounds(-5, -5, 1, 1);
/* 369 */           localJButton2.setBounds(-5, -5, 1, 1);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public Dimension minimumLayoutSize(Container paramContainer) {
/* 375 */       return new Dimension(0, 0);
/*     */     }
/*     */ 
/*     */     public Dimension preferredLayoutSize(Container paramContainer) {
/* 379 */       return new Dimension(0, 0);
/*     */     }
/*     */ 
/*     */     public void removeLayoutComponent(Component paramComponent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void addLayoutComponent(String paramString, Component paramComponent)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalSplitPaneDivider
 * JD-Core Version:    0.6.2
 */