/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.util.Vector;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.TextUI;
/*     */ 
/*     */ public class DefaultHighlighter extends LayeredHighlighter
/*     */ {
/* 350 */   private static final Highlighter.Highlight[] noHighlights = new Highlighter.Highlight[0];
/*     */ 
/* 352 */   private Vector<HighlightInfo> highlights = new Vector();
/*     */   private JTextComponent component;
/*     */   private boolean drawsLayeredHighlights;
/* 355 */   private SafeDamager safeDamager = new SafeDamager();
/*     */ 
/* 364 */   public static final LayeredHighlighter.LayerPainter DefaultPainter = new DefaultHighlightPainter(null);
/*     */ 
/*     */   public DefaultHighlighter()
/*     */   {
/*  45 */     this.drawsLayeredHighlights = true;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/*  57 */     int i = this.highlights.size();
/*  58 */     for (int j = 0; j < i; j++) {
/*  59 */       HighlightInfo localHighlightInfo = (HighlightInfo)this.highlights.elementAt(j);
/*  60 */       if (!(localHighlightInfo instanceof LayeredHighlightInfo))
/*     */       {
/*  62 */         Rectangle localRectangle = this.component.getBounds();
/*  63 */         Insets localInsets = this.component.getInsets();
/*  64 */         localRectangle.x = localInsets.left;
/*  65 */         localRectangle.y = localInsets.top;
/*  66 */         localRectangle.width -= localInsets.left + localInsets.right;
/*  67 */         localRectangle.height -= localInsets.top + localInsets.bottom;
/*  68 */         for (; j < i; j++) {
/*  69 */           localHighlightInfo = (HighlightInfo)this.highlights.elementAt(j);
/*  70 */           if (!(localHighlightInfo instanceof LayeredHighlightInfo)) {
/*  71 */             Highlighter.HighlightPainter localHighlightPainter = localHighlightInfo.getPainter();
/*  72 */             localHighlightPainter.paint(paramGraphics, localHighlightInfo.getStartOffset(), localHighlightInfo.getEndOffset(), localRectangle, this.component);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void install(JTextComponent paramJTextComponent)
/*     */   {
/*  89 */     this.component = paramJTextComponent;
/*  90 */     removeAllHighlights();
/*     */   }
/*     */ 
/*     */   public void deinstall(JTextComponent paramJTextComponent)
/*     */   {
/* 101 */     this.component = null;
/*     */   }
/*     */ 
/*     */   public Object addHighlight(int paramInt1, int paramInt2, Highlighter.HighlightPainter paramHighlightPainter)
/*     */     throws BadLocationException
/*     */   {
/* 116 */     if (paramInt1 < 0) {
/* 117 */       throw new BadLocationException("Invalid start offset", paramInt1);
/*     */     }
/*     */ 
/* 120 */     if (paramInt2 < paramInt1) {
/* 121 */       throw new BadLocationException("Invalid end offset", paramInt2);
/*     */     }
/*     */ 
/* 124 */     Document localDocument = this.component.getDocument();
/* 125 */     HighlightInfo localHighlightInfo = (getDrawsLayeredHighlights()) && ((paramHighlightPainter instanceof LayeredHighlighter.LayerPainter)) ? new LayeredHighlightInfo() : new HighlightInfo();
/*     */ 
/* 128 */     localHighlightInfo.painter = paramHighlightPainter;
/* 129 */     localHighlightInfo.p0 = localDocument.createPosition(paramInt1);
/* 130 */     localHighlightInfo.p1 = localDocument.createPosition(paramInt2);
/* 131 */     this.highlights.addElement(localHighlightInfo);
/* 132 */     safeDamageRange(paramInt1, paramInt2);
/* 133 */     return localHighlightInfo;
/*     */   }
/*     */ 
/*     */   public void removeHighlight(Object paramObject)
/*     */   {
/*     */     Object localObject;
/* 142 */     if ((paramObject instanceof LayeredHighlightInfo)) {
/* 143 */       localObject = (LayeredHighlightInfo)paramObject;
/* 144 */       if ((((LayeredHighlightInfo)localObject).width > 0) && (((LayeredHighlightInfo)localObject).height > 0))
/* 145 */         this.component.repaint(((LayeredHighlightInfo)localObject).x, ((LayeredHighlightInfo)localObject).y, ((LayeredHighlightInfo)localObject).width, ((LayeredHighlightInfo)localObject).height);
/*     */     }
/*     */     else
/*     */     {
/* 149 */       localObject = (HighlightInfo)paramObject;
/* 150 */       safeDamageRange(((HighlightInfo)localObject).p0, ((HighlightInfo)localObject).p1);
/*     */     }
/* 152 */     this.highlights.removeElement(paramObject);
/*     */   }
/*     */ 
/*     */   public void removeAllHighlights()
/*     */   {
/* 159 */     TextUI localTextUI = this.component.getUI();
/*     */     int i;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/* 160 */     if (getDrawsLayeredHighlights()) {
/* 161 */       i = this.highlights.size();
/* 162 */       if (i != 0) {
/* 163 */         j = 0;
/* 164 */         k = 0;
/* 165 */         m = 0;
/* 166 */         int n = 0;
/* 167 */         int i1 = -1;
/* 168 */         int i2 = -1;
/* 169 */         for (int i3 = 0; i3 < i; i3++) {
/* 170 */           HighlightInfo localHighlightInfo2 = (HighlightInfo)this.highlights.elementAt(i3);
/* 171 */           if ((localHighlightInfo2 instanceof LayeredHighlightInfo)) {
/* 172 */             LayeredHighlightInfo localLayeredHighlightInfo = (LayeredHighlightInfo)localHighlightInfo2;
/* 173 */             j = Math.min(j, localLayeredHighlightInfo.x);
/* 174 */             k = Math.min(k, localLayeredHighlightInfo.y);
/* 175 */             m = Math.max(m, localLayeredHighlightInfo.x + localLayeredHighlightInfo.width);
/* 176 */             n = Math.max(n, localLayeredHighlightInfo.y + localLayeredHighlightInfo.height);
/*     */           }
/* 179 */           else if (i1 == -1) {
/* 180 */             i1 = localHighlightInfo2.p0.getOffset();
/* 181 */             i2 = localHighlightInfo2.p1.getOffset();
/*     */           }
/*     */           else {
/* 184 */             i1 = Math.min(i1, localHighlightInfo2.p0.getOffset());
/* 185 */             i2 = Math.max(i2, localHighlightInfo2.p1.getOffset());
/*     */           }
/*     */         }
/*     */ 
/* 189 */         if ((j != m) && (k != n)) {
/* 190 */           this.component.repaint(j, k, m - j, n - k);
/*     */         }
/* 192 */         if (i1 != -1)
/*     */           try {
/* 194 */             safeDamageRange(i1, i2);
/*     */           } catch (BadLocationException localBadLocationException2) {
/*     */           }
/* 197 */         this.highlights.removeAllElements();
/*     */       }
/*     */     }
/* 200 */     else if (localTextUI != null) {
/* 201 */       i = this.highlights.size();
/* 202 */       if (i != 0) {
/* 203 */         j = 2147483647;
/* 204 */         k = 0;
/* 205 */         for (m = 0; m < i; m++) {
/* 206 */           HighlightInfo localHighlightInfo1 = (HighlightInfo)this.highlights.elementAt(m);
/* 207 */           j = Math.min(j, localHighlightInfo1.p0.getOffset());
/* 208 */           k = Math.max(k, localHighlightInfo1.p1.getOffset());
/*     */         }
/*     */         try {
/* 211 */           safeDamageRange(j, k);
/*     */         } catch (BadLocationException localBadLocationException1) {
/*     */         }
/* 214 */         this.highlights.removeAllElements();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void changeHighlight(Object paramObject, int paramInt1, int paramInt2)
/*     */     throws BadLocationException
/*     */   {
/* 228 */     if (paramInt1 < 0) {
/* 229 */       throw new BadLocationException("Invalid beginning of the range", paramInt1);
/*     */     }
/*     */ 
/* 232 */     if (paramInt2 < paramInt1) {
/* 233 */       throw new BadLocationException("Invalid end of the range", paramInt2);
/*     */     }
/*     */ 
/* 236 */     Document localDocument = this.component.getDocument();
/*     */     Object localObject;
/* 237 */     if ((paramObject instanceof LayeredHighlightInfo)) {
/* 238 */       localObject = (LayeredHighlightInfo)paramObject;
/* 239 */       if ((((LayeredHighlightInfo)localObject).width > 0) && (((LayeredHighlightInfo)localObject).height > 0)) {
/* 240 */         this.component.repaint(((LayeredHighlightInfo)localObject).x, ((LayeredHighlightInfo)localObject).y, ((LayeredHighlightInfo)localObject).width, ((LayeredHighlightInfo)localObject).height);
/*     */       }
/*     */ 
/* 244 */       ((LayeredHighlightInfo)localObject).width = (((LayeredHighlightInfo)localObject).height = 0);
/* 245 */       ((LayeredHighlightInfo)localObject).p0 = localDocument.createPosition(paramInt1);
/* 246 */       ((LayeredHighlightInfo)localObject).p1 = localDocument.createPosition(paramInt2);
/* 247 */       safeDamageRange(Math.min(paramInt1, paramInt2), Math.max(paramInt1, paramInt2));
/*     */     }
/*     */     else {
/* 250 */       localObject = (HighlightInfo)paramObject;
/* 251 */       int i = ((HighlightInfo)localObject).p0.getOffset();
/* 252 */       int j = ((HighlightInfo)localObject).p1.getOffset();
/* 253 */       if (paramInt1 == i) {
/* 254 */         safeDamageRange(Math.min(j, paramInt2), Math.max(j, paramInt2));
/*     */       }
/* 256 */       else if (paramInt2 == j) {
/* 257 */         safeDamageRange(Math.min(paramInt1, i), Math.max(paramInt1, i));
/*     */       }
/*     */       else {
/* 260 */         safeDamageRange(i, j);
/* 261 */         safeDamageRange(paramInt1, paramInt2);
/*     */       }
/* 263 */       ((HighlightInfo)localObject).p0 = localDocument.createPosition(paramInt1);
/* 264 */       ((HighlightInfo)localObject).p1 = localDocument.createPosition(paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Highlighter.Highlight[] getHighlights()
/*     */   {
/* 276 */     int i = this.highlights.size();
/* 277 */     if (i == 0) {
/* 278 */       return noHighlights;
/*     */     }
/* 280 */     Highlighter.Highlight[] arrayOfHighlight = new Highlighter.Highlight[i];
/* 281 */     this.highlights.copyInto(arrayOfHighlight);
/* 282 */     return arrayOfHighlight;
/*     */   }
/*     */ 
/*     */   public void paintLayeredHighlights(Graphics paramGraphics, int paramInt1, int paramInt2, Shape paramShape, JTextComponent paramJTextComponent, View paramView)
/*     */   {
/* 300 */     for (int i = this.highlights.size() - 1; i >= 0; i--) {
/* 301 */       HighlightInfo localHighlightInfo = (HighlightInfo)this.highlights.elementAt(i);
/* 302 */       if ((localHighlightInfo instanceof LayeredHighlightInfo)) {
/* 303 */         LayeredHighlightInfo localLayeredHighlightInfo = (LayeredHighlightInfo)localHighlightInfo;
/* 304 */         int j = localLayeredHighlightInfo.getStartOffset();
/* 305 */         int k = localLayeredHighlightInfo.getEndOffset();
/* 306 */         if (((paramInt1 < j) && (paramInt2 > j)) || ((paramInt1 >= j) && (paramInt1 < k)))
/*     */         {
/* 308 */           localLayeredHighlightInfo.paintLayeredHighlights(paramGraphics, paramInt1, paramInt2, paramShape, paramJTextComponent, paramView);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void safeDamageRange(Position paramPosition1, Position paramPosition2)
/*     */   {
/* 320 */     this.safeDamager.damageRange(paramPosition1, paramPosition2);
/*     */   }
/*     */ 
/*     */   private void safeDamageRange(int paramInt1, int paramInt2)
/*     */     throws BadLocationException
/*     */   {
/* 328 */     Document localDocument = this.component.getDocument();
/* 329 */     safeDamageRange(localDocument.createPosition(paramInt1), localDocument.createPosition(paramInt2));
/*     */   }
/*     */ 
/*     */   public void setDrawsLayeredHighlights(boolean paramBoolean)
/*     */   {
/* 341 */     this.drawsLayeredHighlights = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getDrawsLayeredHighlights() {
/* 345 */     return this.drawsLayeredHighlights;
/*     */   }
/*     */ 
/*     */   public static class DefaultHighlightPainter extends LayeredHighlighter.LayerPainter
/*     */   {
/*     */     private Color color;
/*     */ 
/*     */     public DefaultHighlightPainter(Color paramColor)
/*     */     {
/* 380 */       this.color = paramColor;
/*     */     }
/*     */ 
/*     */     public Color getColor()
/*     */     {
/* 389 */       return this.color;
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics, int paramInt1, int paramInt2, Shape paramShape, JTextComponent paramJTextComponent)
/*     */     {
/* 404 */       Rectangle localRectangle1 = paramShape.getBounds();
/*     */       try
/*     */       {
/* 407 */         TextUI localTextUI = paramJTextComponent.getUI();
/* 408 */         Rectangle localRectangle2 = localTextUI.modelToView(paramJTextComponent, paramInt1);
/* 409 */         Rectangle localRectangle3 = localTextUI.modelToView(paramJTextComponent, paramInt2);
/*     */ 
/* 412 */         Color localColor = getColor();
/*     */ 
/* 414 */         if (localColor == null) {
/* 415 */           paramGraphics.setColor(paramJTextComponent.getSelectionColor());
/*     */         }
/*     */         else {
/* 418 */           paramGraphics.setColor(localColor);
/*     */         }
/* 420 */         if (localRectangle2.y == localRectangle3.y)
/*     */         {
/* 422 */           Rectangle localRectangle4 = localRectangle2.union(localRectangle3);
/* 423 */           paramGraphics.fillRect(localRectangle4.x, localRectangle4.y, localRectangle4.width, localRectangle4.height);
/*     */         }
/*     */         else {
/* 426 */           int i = localRectangle1.x + localRectangle1.width - localRectangle2.x;
/* 427 */           paramGraphics.fillRect(localRectangle2.x, localRectangle2.y, i, localRectangle2.height);
/* 428 */           if (localRectangle2.y + localRectangle2.height != localRectangle3.y) {
/* 429 */             paramGraphics.fillRect(localRectangle1.x, localRectangle2.y + localRectangle2.height, localRectangle1.width, localRectangle3.y - (localRectangle2.y + localRectangle2.height));
/*     */           }
/*     */ 
/* 432 */           paramGraphics.fillRect(localRectangle1.x, localRectangle3.y, localRectangle3.x - localRectangle1.x, localRectangle3.height);
/*     */         }
/*     */       }
/*     */       catch (BadLocationException localBadLocationException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*     */     public Shape paintLayer(Graphics paramGraphics, int paramInt1, int paramInt2, Shape paramShape, JTextComponent paramJTextComponent, View paramView)
/*     */     {
/* 454 */       Color localColor = getColor();
/*     */ 
/* 456 */       if (localColor == null) {
/* 457 */         paramGraphics.setColor(paramJTextComponent.getSelectionColor());
/*     */       }
/*     */       else
/* 460 */         paramGraphics.setColor(localColor);
/*     */       Rectangle localRectangle;
/* 465 */       if ((paramInt1 == paramView.getStartOffset()) && (paramInt2 == paramView.getEndOffset()))
/*     */       {
/* 468 */         if ((paramShape instanceof Rectangle)) {
/* 469 */           localRectangle = (Rectangle)paramShape;
/*     */         }
/*     */         else {
/* 472 */           localRectangle = paramShape.getBounds();
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         try
/*     */         {
/* 479 */           Shape localShape = paramView.modelToView(paramInt1, Position.Bias.Forward, paramInt2, Position.Bias.Backward, paramShape);
/*     */ 
/* 482 */           localRectangle = (localShape instanceof Rectangle) ? (Rectangle)localShape : localShape.getBounds();
/*     */         }
/*     */         catch (BadLocationException localBadLocationException)
/*     */         {
/* 486 */           localRectangle = null;
/*     */         }
/*     */       }
/*     */ 
/* 490 */       if (localRectangle != null)
/*     */       {
/* 493 */         localRectangle.width = Math.max(localRectangle.width, 1);
/*     */ 
/* 495 */         paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */       }
/*     */ 
/* 498 */       return localRectangle;
/*     */     }
/*     */   }
/*     */   class HighlightInfo implements Highlighter.Highlight {
/*     */     Position p0;
/*     */     Position p1;
/*     */     Highlighter.HighlightPainter painter;
/*     */ 
/*     */     HighlightInfo() {  }
/*     */ 
/* 509 */     public int getStartOffset() { return this.p0.getOffset(); }
/*     */ 
/*     */     public int getEndOffset()
/*     */     {
/* 513 */       return this.p1.getOffset();
/*     */     }
/*     */ 
/*     */     public Highlighter.HighlightPainter getPainter() {
/* 517 */       return this.painter;
/*     */     }
/*     */   }
/*     */ 
/*     */   class LayeredHighlightInfo extends DefaultHighlighter.HighlightInfo {
/*     */     int x;
/*     */     int y;
/*     */     int width;
/*     */     int height;
/*     */ 
/* 530 */     LayeredHighlightInfo() { super(); }
/*     */ 
/*     */     void union(Shape paramShape) {
/* 533 */       if (paramShape == null)
/*     */         return;
/*     */       Rectangle localRectangle;
/* 537 */       if ((paramShape instanceof Rectangle)) {
/* 538 */         localRectangle = (Rectangle)paramShape;
/*     */       }
/*     */       else {
/* 541 */         localRectangle = paramShape.getBounds();
/*     */       }
/* 543 */       if ((this.width == 0) || (this.height == 0)) {
/* 544 */         this.x = localRectangle.x;
/* 545 */         this.y = localRectangle.y;
/* 546 */         this.width = localRectangle.width;
/* 547 */         this.height = localRectangle.height;
/*     */       }
/*     */       else {
/* 550 */         this.width = Math.max(this.x + this.width, localRectangle.x + localRectangle.width);
/* 551 */         this.height = Math.max(this.y + this.height, localRectangle.y + localRectangle.height);
/* 552 */         this.x = Math.min(this.x, localRectangle.x);
/* 553 */         this.width -= this.x;
/* 554 */         this.y = Math.min(this.y, localRectangle.y);
/* 555 */         this.height -= this.y;
/*     */       }
/*     */     }
/*     */ 
/*     */     void paintLayeredHighlights(Graphics paramGraphics, int paramInt1, int paramInt2, Shape paramShape, JTextComponent paramJTextComponent, View paramView)
/*     */     {
/* 566 */       int i = getStartOffset();
/* 567 */       int j = getEndOffset();
/*     */ 
/* 569 */       paramInt1 = Math.max(i, paramInt1);
/* 570 */       paramInt2 = Math.min(j, paramInt2);
/*     */ 
/* 573 */       union(((LayeredHighlighter.LayerPainter)this.painter).paintLayer(paramGraphics, paramInt1, paramInt2, paramShape, paramJTextComponent, paramView));
/*     */     }
/*     */   }
/*     */ 
/*     */   class SafeDamager
/*     */     implements Runnable
/*     */   {
/* 592 */     private Vector<Position> p0 = new Vector(10);
/* 593 */     private Vector<Position> p1 = new Vector(10);
/* 594 */     private Document lastDoc = null;
/*     */ 
/*     */     SafeDamager() {
/*     */     }
/*     */ 
/*     */     public synchronized void run() {
/* 600 */       if (DefaultHighlighter.this.component != null) {
/* 601 */         TextUI localTextUI = DefaultHighlighter.this.component.getUI();
/* 602 */         if ((localTextUI != null) && (this.lastDoc == DefaultHighlighter.this.component.getDocument()))
/*     */         {
/* 605 */           int i = this.p0.size();
/* 606 */           for (int j = 0; j < i; j++) {
/* 607 */             localTextUI.damageRange(DefaultHighlighter.this.component, ((Position)this.p0.get(j)).getOffset(), ((Position)this.p1.get(j)).getOffset());
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 613 */       this.p0.clear();
/* 614 */       this.p1.clear();
/*     */ 
/* 617 */       this.lastDoc = null;
/*     */     }
/*     */ 
/*     */     public synchronized void damageRange(Position paramPosition1, Position paramPosition2)
/*     */     {
/* 631 */       if (DefaultHighlighter.this.component == null) {
/* 632 */         this.p0.clear();
/* 633 */         this.lastDoc = null;
/* 634 */         return;
/*     */       }
/*     */ 
/* 637 */       boolean bool = this.p0.isEmpty();
/* 638 */       Document localDocument = DefaultHighlighter.this.component.getDocument();
/* 639 */       if (localDocument != this.lastDoc) {
/* 640 */         if (!this.p0.isEmpty()) {
/* 641 */           this.p0.clear();
/* 642 */           this.p1.clear();
/*     */         }
/* 644 */         this.lastDoc = localDocument;
/*     */       }
/* 646 */       this.p0.add(paramPosition1);
/* 647 */       this.p1.add(paramPosition2);
/*     */ 
/* 649 */       if (bool)
/* 650 */         SwingUtilities.invokeLater(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.DefaultHighlighter
 * JD-Core Version:    0.6.2
 */