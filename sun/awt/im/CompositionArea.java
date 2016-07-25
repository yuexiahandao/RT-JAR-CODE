/*     */ package sun.awt.im;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.InputMethodEvent;
/*     */ import java.awt.event.InputMethodListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.TextHitInfo;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.im.InputMethodRequests;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.LineBorder;
/*     */ 
/*     */ public final class CompositionArea extends JPanel
/*     */   implements InputMethodListener
/*     */ {
/*     */   private CompositionAreaHandler handler;
/*     */   private TextLayout composedTextLayout;
/*  65 */   private TextHitInfo caret = null;
/*     */   private JFrame compositionWindow;
/*     */   private static final int TEXT_ORIGIN_X = 5;
/*     */   private static final int TEXT_ORIGIN_Y = 15;
/*     */   private static final int PASSIVE_WIDTH = 480;
/*     */   private static final int WIDTH_MARGIN = 10;
/*     */   private static final int HEIGHT_MARGIN = 3;
/*     */ 
/*     */   CompositionArea()
/*     */   {
/*  75 */     String str = Toolkit.getProperty("AWT.CompositionWindowTitle", "Input Window");
/*  76 */     this.compositionWindow = ((JFrame)InputMethodContext.createInputMethodWindow(str, null, true));
/*     */ 
/*  79 */     setOpaque(true);
/*  80 */     setBorder(LineBorder.createGrayLineBorder());
/*  81 */     setForeground(Color.black);
/*  82 */     setBackground(Color.white);
/*     */ 
/*  86 */     enableInputMethods(true);
/*  87 */     enableEvents(8L);
/*     */ 
/*  89 */     this.compositionWindow.getContentPane().add(this);
/*  90 */     this.compositionWindow.addWindowListener(new FrameWindowAdapter());
/*  91 */     addInputMethodListener(this);
/*  92 */     this.compositionWindow.enableInputMethods(false);
/*  93 */     this.compositionWindow.pack();
/*  94 */     Dimension localDimension1 = this.compositionWindow.getSize();
/*  95 */     Dimension localDimension2 = getToolkit().getScreenSize();
/*  96 */     this.compositionWindow.setLocation(localDimension2.width - localDimension1.width - 20, localDimension2.height - localDimension1.height - 100);
/*     */ 
/*  98 */     this.compositionWindow.setVisible(false);
/*     */   }
/*     */ 
/*     */   synchronized void setHandlerInfo(CompositionAreaHandler paramCompositionAreaHandler, InputContext paramInputContext)
/*     */   {
/* 106 */     this.handler = paramCompositionAreaHandler;
/* 107 */     ((InputMethodWindow)this.compositionWindow).setInputContext(paramInputContext);
/*     */   }
/*     */ 
/*     */   public InputMethodRequests getInputMethodRequests()
/*     */   {
/* 114 */     return this.handler;
/*     */   }
/*     */ 
/*     */   private Rectangle getCaretRectangle(TextHitInfo paramTextHitInfo)
/*     */   {
/* 119 */     int i = 0;
/* 120 */     TextLayout localTextLayout = this.composedTextLayout;
/* 121 */     if (localTextLayout != null) {
/* 122 */       i = Math.round(localTextLayout.getCaretInfo(paramTextHitInfo)[0]);
/*     */     }
/* 124 */     Graphics localGraphics = getGraphics();
/* 125 */     FontMetrics localFontMetrics = null;
/*     */     try {
/* 127 */       localFontMetrics = localGraphics.getFontMetrics();
/*     */     } finally {
/* 129 */       localGraphics.dispose();
/*     */     }
/* 131 */     return new Rectangle(5 + i, 15 - localFontMetrics.getAscent(), 0, localFontMetrics.getAscent() + localFontMetrics.getDescent());
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/* 137 */     super.paint(paramGraphics);
/* 138 */     paramGraphics.setColor(getForeground());
/* 139 */     TextLayout localTextLayout = this.composedTextLayout;
/* 140 */     if (localTextLayout != null) {
/* 141 */       localTextLayout.draw((Graphics2D)paramGraphics, 5.0F, 15.0F);
/*     */     }
/* 143 */     if (this.caret != null) {
/* 144 */       Rectangle localRectangle = getCaretRectangle(this.caret);
/* 145 */       paramGraphics.setXORMode(getBackground());
/* 146 */       paramGraphics.fillRect(localRectangle.x, localRectangle.y, 1, localRectangle.height);
/* 147 */       paramGraphics.setPaintMode();
/*     */     }
/*     */   }
/*     */ 
/*     */   void setCompositionAreaVisible(boolean paramBoolean)
/*     */   {
/* 153 */     this.compositionWindow.setVisible(paramBoolean);
/*     */   }
/*     */ 
/*     */   boolean isCompositionAreaVisible()
/*     */   {
/* 158 */     return this.compositionWindow.isVisible();
/*     */   }
/*     */ 
/*     */   public void inputMethodTextChanged(InputMethodEvent paramInputMethodEvent)
/*     */   {
/* 170 */     this.handler.inputMethodTextChanged(paramInputMethodEvent);
/*     */   }
/*     */ 
/*     */   public void caretPositionChanged(InputMethodEvent paramInputMethodEvent) {
/* 174 */     this.handler.caretPositionChanged(paramInputMethodEvent);
/*     */   }
/*     */ 
/*     */   void setText(AttributedCharacterIterator paramAttributedCharacterIterator, TextHitInfo paramTextHitInfo)
/*     */   {
/* 182 */     this.composedTextLayout = null;
/* 183 */     if (paramAttributedCharacterIterator == null)
/*     */     {
/* 185 */       this.compositionWindow.setVisible(false);
/* 186 */       this.caret = null;
/*     */     }
/*     */     else
/*     */     {
/* 191 */       if (!this.compositionWindow.isVisible()) {
/* 192 */         this.compositionWindow.setVisible(true);
/*     */       }
/*     */ 
/* 195 */       Graphics localGraphics = getGraphics();
/*     */ 
/* 197 */       if (localGraphics == null) {
/* 198 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 202 */         updateWindowLocation();
/*     */ 
/* 204 */         FontRenderContext localFontRenderContext = ((Graphics2D)localGraphics).getFontRenderContext();
/* 205 */         this.composedTextLayout = new TextLayout(paramAttributedCharacterIterator, localFontRenderContext);
/* 206 */         Rectangle2D localRectangle2D1 = this.composedTextLayout.getBounds();
/*     */ 
/* 208 */         this.caret = paramTextHitInfo;
/*     */ 
/* 211 */         FontMetrics localFontMetrics = localGraphics.getFontMetrics();
/* 212 */         Rectangle2D localRectangle2D2 = localFontMetrics.getMaxCharBounds(localGraphics);
/* 213 */         int i = (int)localRectangle2D2.getHeight() + 3;
/* 214 */         int j = i + this.compositionWindow.getInsets().top + this.compositionWindow.getInsets().bottom;
/*     */ 
/* 217 */         InputMethodRequests localInputMethodRequests = this.handler.getClientInputMethodRequests();
/* 218 */         int k = localInputMethodRequests == null ? 480 : (int)localRectangle2D1.getWidth() + 10;
/* 219 */         int m = k + this.compositionWindow.getInsets().left + this.compositionWindow.getInsets().right;
/*     */ 
/* 221 */         setPreferredSize(new Dimension(k, i));
/* 222 */         this.compositionWindow.setSize(new Dimension(m, j));
/*     */ 
/* 225 */         paint(localGraphics);
/*     */       }
/*     */       finally {
/* 228 */         localGraphics.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void setCaret(TextHitInfo paramTextHitInfo)
/*     */   {
/* 238 */     this.caret = paramTextHitInfo;
/* 239 */     if (this.compositionWindow.isVisible()) {
/* 240 */       Graphics localGraphics = getGraphics();
/*     */       try {
/* 242 */         paint(localGraphics);
/*     */       } finally {
/* 244 */         localGraphics.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void updateWindowLocation()
/*     */   {
/* 255 */     InputMethodRequests localInputMethodRequests = this.handler.getClientInputMethodRequests();
/* 256 */     if (localInputMethodRequests == null)
/*     */     {
/* 258 */       return;
/*     */     }
/*     */ 
/* 261 */     Point localPoint = new Point();
/*     */ 
/* 263 */     Rectangle localRectangle = localInputMethodRequests.getTextLocation(null);
/* 264 */     Dimension localDimension1 = Toolkit.getDefaultToolkit().getScreenSize();
/* 265 */     Dimension localDimension2 = this.compositionWindow.getSize();
/*     */ 
/* 268 */     if (localRectangle.x + localDimension2.width > localDimension1.width)
/* 269 */       localPoint.x = (localDimension1.width - localDimension2.width);
/*     */     else {
/* 271 */       localPoint.x = localRectangle.x;
/*     */     }
/*     */ 
/* 274 */     if (localRectangle.y + localRectangle.height + 2 + localDimension2.height > localDimension1.height)
/* 275 */       localPoint.y = (localRectangle.y - 2 - localDimension2.height);
/*     */     else {
/* 277 */       localPoint.y = (localRectangle.y + localRectangle.height + 2);
/*     */     }
/*     */ 
/* 280 */     this.compositionWindow.setLocation(localPoint);
/*     */   }
/*     */ 
/*     */   Rectangle getTextLocation(TextHitInfo paramTextHitInfo)
/*     */   {
/* 285 */     Rectangle localRectangle = getCaretRectangle(paramTextHitInfo);
/* 286 */     Point localPoint = getLocationOnScreen();
/* 287 */     localRectangle.translate(localPoint.x, localPoint.y);
/* 288 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   TextHitInfo getLocationOffset(int paramInt1, int paramInt2) {
/* 292 */     TextLayout localTextLayout = this.composedTextLayout;
/* 293 */     if (localTextLayout == null) {
/* 294 */       return null;
/*     */     }
/* 296 */     Point localPoint = getLocationOnScreen();
/* 297 */     paramInt1 -= localPoint.x + 5;
/* 298 */     paramInt2 -= localPoint.y + 15;
/* 299 */     if (localTextLayout.getBounds().contains(paramInt1, paramInt2)) {
/* 300 */       return localTextLayout.hitTestChar(paramInt1, paramInt2);
/*     */     }
/* 302 */     return null;
/*     */   }
/*     */ 
/*     */   void setCompositionAreaUndecorated(boolean paramBoolean)
/*     */   {
/* 309 */     if (this.compositionWindow.isDisplayable()) {
/* 310 */       this.compositionWindow.removeNotify();
/*     */     }
/* 312 */     this.compositionWindow.setUndecorated(paramBoolean);
/* 313 */     this.compositionWindow.pack();
/*     */   }
/*     */ 
/*     */   class FrameWindowAdapter extends WindowAdapter
/*     */   {
/*     */     FrameWindowAdapter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void windowActivated(WindowEvent paramWindowEvent)
/*     */     {
/* 164 */       CompositionArea.this.requestFocus();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.CompositionArea
 * JD-Core Version:    0.6.2
 */