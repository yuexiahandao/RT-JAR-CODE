/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.BoundedRangeModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class FieldView extends PlainView
/*     */ {
/*     */   public FieldView(Element paramElement)
/*     */   {
/*  51 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   protected FontMetrics getFontMetrics()
/*     */   {
/*  61 */     Container localContainer = getContainer();
/*  62 */     return localContainer.getFontMetrics(localContainer.getFont());
/*     */   }
/*     */ 
/*     */   protected Shape adjustAllocation(Shape paramShape)
/*     */   {
/*  80 */     if (paramShape != null) {
/*  81 */       Rectangle localRectangle = paramShape.getBounds();
/*  82 */       int i = (int)getPreferredSpan(1);
/*  83 */       int j = (int)getPreferredSpan(0);
/*  84 */       if (localRectangle.height != i) {
/*  85 */         int k = localRectangle.height - i;
/*  86 */         localRectangle.y += k / 2;
/*  87 */         localRectangle.height -= k;
/*     */       }
/*     */ 
/*  91 */       Container localContainer = getContainer();
/*  92 */       if ((localContainer instanceof JTextField)) {
/*  93 */         JTextField localJTextField = (JTextField)localContainer;
/*  94 */         BoundedRangeModel localBoundedRangeModel = localJTextField.getHorizontalVisibility();
/*  95 */         int m = Math.max(j, localRectangle.width);
/*  96 */         int n = localBoundedRangeModel.getValue();
/*  97 */         int i1 = Math.min(m, localRectangle.width - 1);
/*  98 */         if (n + i1 > m) {
/*  99 */           n = m - i1;
/*     */         }
/* 101 */         localBoundedRangeModel.setRangeProperties(n, i1, localBoundedRangeModel.getMinimum(), m, false);
/*     */ 
/* 103 */         if (j < localRectangle.width)
/*     */         {
/* 105 */           int i2 = localRectangle.width - 1 - j;
/*     */ 
/* 107 */           int i3 = ((JTextField)localContainer).getHorizontalAlignment();
/* 108 */           if (Utilities.isLeftToRight(localContainer)) {
/* 109 */             if (i3 == 10) {
/* 110 */               i3 = 2;
/*     */             }
/* 112 */             else if (i3 == 11) {
/* 113 */               i3 = 4;
/*     */             }
/*     */ 
/*     */           }
/* 117 */           else if (i3 == 10) {
/* 118 */             i3 = 4;
/*     */           }
/* 120 */           else if (i3 == 11) {
/* 121 */             i3 = 2;
/*     */           }
/*     */ 
/* 125 */           switch (i3) {
/*     */           case 0:
/* 127 */             localRectangle.x += i2 / 2;
/* 128 */             localRectangle.width -= i2;
/* 129 */             break;
/*     */           case 4:
/* 131 */             localRectangle.x += i2;
/* 132 */             localRectangle.width -= i2;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 137 */           localRectangle.width = j;
/* 138 */           localRectangle.x -= localBoundedRangeModel.getValue();
/*     */         }
/*     */       }
/* 141 */       return localRectangle;
/*     */     }
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   void updateVisibilityModel()
/*     */   {
/* 155 */     Container localContainer = getContainer();
/* 156 */     if ((localContainer instanceof JTextField)) {
/* 157 */       JTextField localJTextField = (JTextField)localContainer;
/* 158 */       BoundedRangeModel localBoundedRangeModel = localJTextField.getHorizontalVisibility();
/* 159 */       int i = (int)getPreferredSpan(0);
/* 160 */       int j = localBoundedRangeModel.getExtent();
/* 161 */       int k = Math.max(i, j);
/* 162 */       j = j == 0 ? k : j;
/* 163 */       int m = k - j;
/* 164 */       int n = localBoundedRangeModel.getValue();
/* 165 */       if (n + j > k) {
/* 166 */         n = k - j;
/*     */       }
/* 168 */       m = Math.max(0, Math.min(m, n));
/* 169 */       localBoundedRangeModel.setRangeProperties(m, j, 0, k, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/* 186 */     Rectangle localRectangle = (Rectangle)paramShape;
/* 187 */     paramGraphics.clipRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/* 188 */     super.paint(paramGraphics, paramShape);
/*     */   }
/*     */ 
/*     */   Shape adjustPaintRegion(Shape paramShape)
/*     */   {
/* 195 */     return adjustAllocation(paramShape);
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt)
/*     */   {
/* 209 */     switch (paramInt) { case 0:
/* 211 */       Segment localSegment = SegmentCache.getSharedSegment();
/* 212 */       Document localDocument = getDocument();
/*     */       int i;
/*     */       try { FontMetrics localFontMetrics = getFontMetrics();
/* 216 */         localDocument.getText(0, localDocument.getLength(), localSegment);
/* 217 */         i = Utilities.getTabbedTextWidth(localSegment, localFontMetrics, 0, this, 0);
/* 218 */         if (localSegment.count > 0) {
/* 219 */           Container localContainer = getContainer();
/* 220 */           this.firstLineOffset = SwingUtilities2.getLeftSideBearing((localContainer instanceof JComponent) ? (JComponent)localContainer : null, localFontMetrics, localSegment.array[localSegment.offset]);
/*     */ 
/* 224 */           this.firstLineOffset = Math.max(0, -this.firstLineOffset);
/*     */         }
/*     */         else {
/* 227 */           this.firstLineOffset = 0;
/*     */         }
/*     */       } catch (BadLocationException localBadLocationException) {
/* 230 */         i = 0;
/*     */       }
/* 232 */       SegmentCache.releaseSharedSegment(localSegment);
/* 233 */       return i + this.firstLineOffset;
/*     */     }
/* 235 */     return super.getPreferredSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public int getResizeWeight(int paramInt)
/*     */   {
/* 247 */     if (paramInt == 0) {
/* 248 */       return 1;
/*     */     }
/* 250 */     return 0;
/*     */   }
/*     */ 
/*     */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */     throws BadLocationException
/*     */   {
/* 265 */     return super.modelToView(paramInt, adjustAllocation(paramShape), paramBias);
/*     */   }
/*     */ 
/*     */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */   {
/* 280 */     return super.viewToModel(paramFloat1, paramFloat2, adjustAllocation(paramShape), paramArrayOfBias);
/*     */   }
/*     */ 
/*     */   public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 293 */     super.insertUpdate(paramDocumentEvent, adjustAllocation(paramShape), paramViewFactory);
/* 294 */     updateVisibilityModel();
/*     */   }
/*     */ 
/*     */   public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 307 */     super.removeUpdate(paramDocumentEvent, adjustAllocation(paramShape), paramViewFactory);
/* 308 */     updateVisibilityModel();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.FieldView
 * JD-Core Version:    0.6.2
 */