/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.LineBreakMeasurer;
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.AttributedCharacterIterator.Attribute;
/*     */ import java.text.BreakIterator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import sun.font.BidiUtils;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ class TextLayoutStrategy extends FlowView.FlowStrategy
/*     */ {
/*     */   private LineBreakMeasurer measurer;
/*     */   private AttributedSegment text;
/*     */ 
/*     */   public TextLayoutStrategy()
/*     */   {
/*  53 */     this.text = new AttributedSegment();
/*     */   }
/*     */ 
/*     */   public void insertUpdate(FlowView paramFlowView, DocumentEvent paramDocumentEvent, Rectangle paramRectangle)
/*     */   {
/*  70 */     sync(paramFlowView);
/*  71 */     super.insertUpdate(paramFlowView, paramDocumentEvent, paramRectangle);
/*     */   }
/*     */ 
/*     */   public void removeUpdate(FlowView paramFlowView, DocumentEvent paramDocumentEvent, Rectangle paramRectangle)
/*     */   {
/*  83 */     sync(paramFlowView);
/*  84 */     super.removeUpdate(paramFlowView, paramDocumentEvent, paramRectangle);
/*     */   }
/*     */ 
/*     */   public void changedUpdate(FlowView paramFlowView, DocumentEvent paramDocumentEvent, Rectangle paramRectangle)
/*     */   {
/*  97 */     sync(paramFlowView);
/*  98 */     super.changedUpdate(paramFlowView, paramDocumentEvent, paramRectangle);
/*     */   }
/*     */ 
/*     */   public void layout(FlowView paramFlowView)
/*     */   {
/* 110 */     super.layout(paramFlowView);
/*     */   }
/*     */ 
/*     */   protected int layoutRow(FlowView paramFlowView, int paramInt1, int paramInt2)
/*     */   {
/* 127 */     int i = super.layoutRow(paramFlowView, paramInt1, paramInt2);
/* 128 */     View localView1 = paramFlowView.getView(paramInt1);
/* 129 */     Document localDocument = paramFlowView.getDocument();
/* 130 */     Object localObject = localDocument.getProperty("i18n");
/* 131 */     if ((localObject != null) && (localObject.equals(Boolean.TRUE))) {
/* 132 */       int j = localView1.getViewCount();
/* 133 */       if (j > 1) {
/* 134 */         AbstractDocument localAbstractDocument = (AbstractDocument)paramFlowView.getDocument();
/* 135 */         Element localElement1 = localAbstractDocument.getBidiRootElement();
/* 136 */         byte[] arrayOfByte = new byte[j];
/* 137 */         View[] arrayOfView = new View[j];
/*     */ 
/* 139 */         for (int k = 0; k < j; k++) {
/* 140 */           View localView2 = localView1.getView(k);
/* 141 */           int m = localElement1.getElementIndex(localView2.getStartOffset());
/* 142 */           Element localElement2 = localElement1.getElement(m);
/* 143 */           arrayOfByte[k] = ((byte)StyleConstants.getBidiLevel(localElement2.getAttributes()));
/* 144 */           arrayOfView[k] = localView2;
/*     */         }
/*     */ 
/* 147 */         BidiUtils.reorderVisually(arrayOfByte, arrayOfView);
/* 148 */         localView1.replace(0, j, arrayOfView);
/*     */       }
/*     */     }
/* 151 */     return i;
/*     */   }
/*     */ 
/*     */   protected void adjustRow(FlowView paramFlowView, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected View createView(FlowView paramFlowView, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 180 */     View localView1 = getLogicalView(paramFlowView);
/* 181 */     View localView2 = paramFlowView.getView(paramInt3);
/* 182 */     boolean bool = this.viewBuffer.size() != 0;
/* 183 */     int i = localView1.getViewIndex(paramInt1, Position.Bias.Forward);
/* 184 */     View localView3 = localView1.getView(i);
/*     */ 
/* 186 */     int j = getLimitingOffset(localView3, paramInt1, paramInt2, bool);
/* 187 */     if (j == paramInt1)
/* 188 */       return null;
/*     */     View localView4;
/* 192 */     if ((paramInt1 == localView3.getStartOffset()) && (j == localView3.getEndOffset()))
/*     */     {
/* 194 */       localView4 = localView3;
/*     */     }
/*     */     else {
/* 197 */       localView4 = localView3.createFragment(paramInt1, j);
/*     */     }
/*     */ 
/* 200 */     if (((localView4 instanceof GlyphView)) && (this.measurer != null))
/*     */     {
/* 204 */       int k = 0;
/* 205 */       int m = localView4.getStartOffset();
/* 206 */       int n = localView4.getEndOffset();
/* 207 */       if (n - m == 1)
/*     */       {
/* 209 */         localObject = ((GlyphView)localView4).getText(m, n);
/* 210 */         int i1 = ((Segment)localObject).first();
/* 211 */         if (i1 == 9) {
/* 212 */           k = 1;
/*     */         }
/*     */       }
/* 215 */       Object localObject = k != 0 ? null : this.measurer.nextLayout(paramInt2, this.text.toIteratorIndex(j), bool);
/*     */ 
/* 218 */       if (localObject != null) {
/* 219 */         ((GlyphView)localView4).setGlyphPainter(new GlyphPainter2((TextLayout)localObject));
/*     */       }
/*     */     }
/* 222 */     return localView4;
/*     */   }
/*     */ 
/*     */   int getLimitingOffset(View paramView, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 238 */     int i = paramView.getEndOffset();
/*     */ 
/* 241 */     Document localDocument = paramView.getDocument();
/*     */     Object localObject;
/* 242 */     if ((localDocument instanceof AbstractDocument)) {
/* 243 */       localObject = (AbstractDocument)localDocument;
/* 244 */       Element localElement1 = ((AbstractDocument)localObject).getBidiRootElement();
/* 245 */       if (localElement1.getElementCount() > 1) {
/* 246 */         int m = localElement1.getElementIndex(paramInt1);
/* 247 */         Element localElement2 = localElement1.getElement(m);
/* 248 */         i = Math.min(localElement2.getEndOffset(), i);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 253 */     if ((paramView instanceof GlyphView)) {
/* 254 */       localObject = ((GlyphView)paramView).getText(paramInt1, i);
/* 255 */       k = ((Segment)localObject).first();
/* 256 */       if (k == 9)
/*     */       {
/* 259 */         i = paramInt1 + 1;
/*     */       }
/* 261 */       else for (k = ((Segment)localObject).next(); k != 65535; k = ((Segment)localObject).next()) {
/* 262 */           if (k == 9)
/*     */           {
/* 264 */             i = paramInt1 + ((Segment)localObject).getIndex() - ((Segment)localObject).getBeginIndex();
/* 265 */             break;
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */     }
/*     */ 
/* 272 */     int j = this.text.toIteratorIndex(i);
/* 273 */     if (this.measurer != null) {
/* 274 */       k = this.text.toIteratorIndex(paramInt1);
/* 275 */       if (this.measurer.getPosition() != k) {
/* 276 */         this.measurer.setPosition(k);
/*     */       }
/* 278 */       j = this.measurer.nextOffset(paramInt2, j, paramBoolean);
/*     */     }
/* 280 */     int k = this.text.toModelPosition(j);
/* 281 */     return k;
/*     */   }
/*     */ 
/*     */   void sync(FlowView paramFlowView)
/*     */   {
/* 291 */     View localView1 = getLogicalView(paramFlowView);
/* 292 */     this.text.setView(localView1);
/*     */ 
/* 294 */     Container localContainer1 = paramFlowView.getContainer();
/* 295 */     FontRenderContext localFontRenderContext = SwingUtilities2.getFontRenderContext(localContainer1);
/*     */ 
/* 298 */     Container localContainer2 = paramFlowView.getContainer();
/*     */     BreakIterator localBreakIterator;
/* 299 */     if (localContainer2 != null)
/* 300 */       localBreakIterator = BreakIterator.getLineInstance(localContainer2.getLocale());
/*     */     else {
/* 302 */       localBreakIterator = BreakIterator.getLineInstance();
/*     */     }
/*     */ 
/* 305 */     Object localObject = null;
/* 306 */     if ((localContainer2 instanceof JComponent)) {
/* 307 */       localObject = ((JComponent)localContainer2).getClientProperty(TextAttribute.NUMERIC_SHAPING);
/*     */     }
/*     */ 
/* 310 */     this.text.setShaper(localObject);
/*     */ 
/* 312 */     this.measurer = new LineBreakMeasurer(this.text, localBreakIterator, localFontRenderContext);
/*     */ 
/* 316 */     int i = localView1.getViewCount();
/* 317 */     for (int j = 0; j < i; j++) {
/* 318 */       View localView2 = localView1.getView(j);
/* 319 */       if ((localView2 instanceof GlyphView)) {
/* 320 */         int k = localView2.getStartOffset();
/* 321 */         int m = localView2.getEndOffset();
/* 322 */         this.measurer.setPosition(this.text.toIteratorIndex(k));
/* 323 */         TextLayout localTextLayout = this.measurer.nextLayout(3.4028235E+38F, this.text.toIteratorIndex(m), false);
/*     */ 
/* 326 */         ((GlyphView)localView2).setGlyphPainter(new GlyphPainter2(localTextLayout));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 331 */     this.measurer.setPosition(this.text.getBeginIndex());
/*     */   }
/*     */ 
/*     */   static class AttributedSegment extends Segment
/*     */     implements AttributedCharacterIterator
/*     */   {
/*     */     View v;
/* 546 */     static Set<AttributedCharacterIterator.Attribute> keys = new HashSet();
/*     */ 
/* 552 */     private Object shaper = null;
/*     */ 
/*     */     View getView()
/*     */     {
/* 351 */       return this.v;
/*     */     }
/*     */ 
/*     */     void setView(View paramView) {
/* 355 */       this.v = paramView;
/* 356 */       Document localDocument = paramView.getDocument();
/* 357 */       int i = paramView.getStartOffset();
/* 358 */       int j = paramView.getEndOffset();
/*     */       try {
/* 360 */         localDocument.getText(i, j - i, this);
/*     */       } catch (BadLocationException localBadLocationException) {
/* 362 */         throw new IllegalArgumentException("Invalid view");
/*     */       }
/* 364 */       first();
/*     */     }
/*     */ 
/*     */     int getFontBoundary(int paramInt1, int paramInt2)
/*     */     {
/* 377 */       View localView = this.v.getView(paramInt1);
/* 378 */       Font localFont1 = getFont(paramInt1);
/* 379 */       for (paramInt1 += paramInt2; (paramInt1 >= 0) && (paramInt1 < this.v.getViewCount()); 
/* 380 */         paramInt1 += paramInt2) {
/* 381 */         Font localFont2 = getFont(paramInt1);
/* 382 */         if (localFont2 != localFont1)
/*     */         {
/*     */           break;
/*     */         }
/* 386 */         localView = this.v.getView(paramInt1);
/*     */       }
/* 388 */       return paramInt2 < 0 ? localView.getStartOffset() : localView.getEndOffset();
/*     */     }
/*     */ 
/*     */     Font getFont(int paramInt)
/*     */     {
/* 395 */       View localView = this.v.getView(paramInt);
/* 396 */       if ((localView instanceof GlyphView)) {
/* 397 */         return ((GlyphView)localView).getFont();
/*     */       }
/* 399 */       return null;
/*     */     }
/*     */ 
/*     */     int toModelPosition(int paramInt) {
/* 403 */       return this.v.getStartOffset() + (paramInt - getBeginIndex());
/*     */     }
/*     */ 
/*     */     int toIteratorIndex(int paramInt) {
/* 407 */       return paramInt - this.v.getStartOffset() + getBeginIndex();
/*     */     }
/*     */ 
/*     */     private void setShaper(Object paramObject) {
/* 411 */       this.shaper = paramObject;
/*     */     }
/*     */ 
/*     */     public int getRunStart()
/*     */     {
/* 421 */       int i = toModelPosition(getIndex());
/* 422 */       int j = this.v.getViewIndex(i, Position.Bias.Forward);
/* 423 */       View localView = this.v.getView(j);
/* 424 */       return toIteratorIndex(localView.getStartOffset());
/*     */     }
/*     */ 
/*     */     public int getRunStart(AttributedCharacterIterator.Attribute paramAttribute)
/*     */     {
/* 432 */       if ((paramAttribute instanceof TextAttribute)) {
/* 433 */         int i = toModelPosition(getIndex());
/* 434 */         int j = this.v.getViewIndex(i, Position.Bias.Forward);
/* 435 */         if (paramAttribute == TextAttribute.FONT) {
/* 436 */           return toIteratorIndex(getFontBoundary(j, -1));
/*     */         }
/*     */       }
/* 439 */       return getBeginIndex();
/*     */     }
/*     */ 
/*     */     public int getRunStart(Set<? extends AttributedCharacterIterator.Attribute> paramSet)
/*     */     {
/* 447 */       int i = getBeginIndex();
/* 448 */       Object[] arrayOfObject = paramSet.toArray();
/* 449 */       for (int j = 0; j < arrayOfObject.length; j++) {
/* 450 */         TextAttribute localTextAttribute = (TextAttribute)arrayOfObject[j];
/* 451 */         i = Math.max(getRunStart(localTextAttribute), i);
/*     */       }
/* 453 */       return Math.min(getIndex(), i);
/*     */     }
/*     */ 
/*     */     public int getRunLimit()
/*     */     {
/* 461 */       int i = toModelPosition(getIndex());
/* 462 */       int j = this.v.getViewIndex(i, Position.Bias.Forward);
/* 463 */       View localView = this.v.getView(j);
/* 464 */       return toIteratorIndex(localView.getEndOffset());
/*     */     }
/*     */ 
/*     */     public int getRunLimit(AttributedCharacterIterator.Attribute paramAttribute)
/*     */     {
/* 472 */       if ((paramAttribute instanceof TextAttribute)) {
/* 473 */         int i = toModelPosition(getIndex());
/* 474 */         int j = this.v.getViewIndex(i, Position.Bias.Forward);
/* 475 */         if (paramAttribute == TextAttribute.FONT) {
/* 476 */           return toIteratorIndex(getFontBoundary(j, 1));
/*     */         }
/*     */       }
/* 479 */       return getEndIndex();
/*     */     }
/*     */ 
/*     */     public int getRunLimit(Set<? extends AttributedCharacterIterator.Attribute> paramSet)
/*     */     {
/* 487 */       int i = getEndIndex();
/* 488 */       Object[] arrayOfObject = paramSet.toArray();
/* 489 */       for (int j = 0; j < arrayOfObject.length; j++) {
/* 490 */         TextAttribute localTextAttribute = (TextAttribute)arrayOfObject[j];
/* 491 */         i = Math.min(getRunLimit(localTextAttribute), i);
/*     */       }
/* 493 */       return Math.max(getIndex(), i);
/*     */     }
/*     */ 
/*     */     public Map<AttributedCharacterIterator.Attribute, Object> getAttributes()
/*     */     {
/* 501 */       Object[] arrayOfObject = keys.toArray();
/* 502 */       Hashtable localHashtable = new Hashtable();
/* 503 */       for (int i = 0; i < arrayOfObject.length; i++) {
/* 504 */         TextAttribute localTextAttribute = (TextAttribute)arrayOfObject[i];
/* 505 */         Object localObject = getAttribute(localTextAttribute);
/* 506 */         if (localObject != null) {
/* 507 */           localHashtable.put(localTextAttribute, localObject);
/*     */         }
/*     */       }
/* 510 */       return localHashtable;
/*     */     }
/*     */ 
/*     */     public Object getAttribute(AttributedCharacterIterator.Attribute paramAttribute)
/*     */     {
/* 519 */       int i = toModelPosition(getIndex());
/* 520 */       int j = this.v.getViewIndex(i, Position.Bias.Forward);
/* 521 */       if (paramAttribute == TextAttribute.FONT)
/* 522 */         return getFont(j);
/* 523 */       if (paramAttribute == TextAttribute.RUN_DIRECTION) {
/* 524 */         return this.v.getDocument().getProperty(TextAttribute.RUN_DIRECTION);
/*     */       }
/* 526 */       if (paramAttribute == TextAttribute.NUMERIC_SHAPING) {
/* 527 */         return this.shaper;
/*     */       }
/* 529 */       return null;
/*     */     }
/*     */ 
/*     */     public Set<AttributedCharacterIterator.Attribute> getAllAttributeKeys()
/*     */     {
/* 538 */       return keys;
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/* 547 */       keys.add(TextAttribute.FONT);
/* 548 */       keys.add(TextAttribute.RUN_DIRECTION);
/* 549 */       keys.add(TextAttribute.NUMERIC_SHAPING);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.TextLayoutStrategy
 * JD-Core Version:    0.6.2
 */