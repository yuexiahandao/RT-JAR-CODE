/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Component.BaselineResizeBehavior;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.BoundedRangeModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.FieldView;
/*     */ import javax.swing.text.GlyphView;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.ParagraphView;
/*     */ import javax.swing.text.Position.Bias;
/*     */ import javax.swing.text.View;
/*     */ import javax.swing.text.ViewFactory;
/*     */ 
/*     */ public class BasicTextFieldUI extends BasicTextUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  63 */     return new BasicTextFieldUI();
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix()
/*     */   {
/*  81 */     return "TextField";
/*     */   }
/*     */ 
/*     */   public View create(Element paramElement)
/*     */   {
/*  91 */     Document localDocument = paramElement.getDocument();
/*  92 */     Object localObject = localDocument.getProperty("i18n");
/*  93 */     if (Boolean.TRUE.equals(localObject))
/*     */     {
/*  96 */       String str = paramElement.getName();
/*  97 */       if (str != null) {
/*  98 */         if (str.equals("content"))
/*  99 */           return new GlyphView(paramElement);
/* 100 */         if (str.equals("paragraph")) {
/* 101 */           return new I18nFieldView(paramElement);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 106 */     return new FieldView(paramElement);
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 118 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/* 119 */     View localView1 = getRootView((JTextComponent)paramJComponent);
/* 120 */     if (localView1.getViewCount() > 0) {
/* 121 */       Insets localInsets = paramJComponent.getInsets();
/* 122 */       paramInt2 = paramInt2 - localInsets.top - localInsets.bottom;
/* 123 */       if (paramInt2 > 0) {
/* 124 */         int i = localInsets.top;
/* 125 */         View localView2 = localView1.getView(0);
/* 126 */         int j = (int)localView2.getPreferredSpan(1);
/*     */         int k;
/* 127 */         if (paramInt2 != j) {
/* 128 */           k = paramInt2 - j;
/* 129 */           i += k / 2;
/*     */         }
/* 131 */         if ((localView2 instanceof I18nFieldView)) {
/* 132 */           k = BasicHTML.getBaseline(localView2, paramInt1 - localInsets.left - localInsets.right, paramInt2);
/*     */ 
/* 135 */           if (k < 0) {
/* 136 */             return -1;
/*     */           }
/* 138 */           i += k;
/*     */         }
/*     */         else {
/* 141 */           FontMetrics localFontMetrics = paramJComponent.getFontMetrics(paramJComponent.getFont());
/* 142 */           i += localFontMetrics.getAscent();
/*     */         }
/* 144 */         return i;
/*     */       }
/*     */     }
/* 147 */     return -1;
/*     */   }
/*     */ 
/*     */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*     */   {
/* 160 */     super.getBaselineResizeBehavior(paramJComponent);
/* 161 */     return Component.BaselineResizeBehavior.CENTER_OFFSET;
/*     */   }
/*     */ 
/*     */   static class I18nFieldView extends ParagraphView
/*     */   {
/*     */     I18nFieldView(Element paramElement)
/*     */     {
/* 172 */       super();
/*     */     }
/*     */ 
/*     */     public int getFlowSpan(int paramInt)
/*     */     {
/* 182 */       return 2147483647;
/*     */     }
/*     */ 
/*     */     protected void setJustification(int paramInt)
/*     */     {
/*     */     }
/*     */ 
/*     */     static boolean isLeftToRight(Component paramComponent)
/*     */     {
/* 191 */       return paramComponent.getComponentOrientation().isLeftToRight();
/*     */     }
/*     */ 
/*     */     Shape adjustAllocation(Shape paramShape)
/*     */     {
/* 209 */       if (paramShape != null) {
/* 210 */         Rectangle localRectangle = paramShape.getBounds();
/* 211 */         int i = (int)getPreferredSpan(1);
/* 212 */         int j = (int)getPreferredSpan(0);
/* 213 */         if (localRectangle.height != i) {
/* 214 */           int k = localRectangle.height - i;
/* 215 */           localRectangle.y += k / 2;
/* 216 */           localRectangle.height -= k;
/*     */         }
/*     */ 
/* 220 */         Container localContainer = getContainer();
/* 221 */         if ((localContainer instanceof JTextField)) {
/* 222 */           JTextField localJTextField = (JTextField)localContainer;
/* 223 */           BoundedRangeModel localBoundedRangeModel = localJTextField.getHorizontalVisibility();
/* 224 */           int m = Math.max(j, localRectangle.width);
/* 225 */           int n = localBoundedRangeModel.getValue();
/* 226 */           int i1 = Math.min(m, localRectangle.width - 1);
/* 227 */           if (n + i1 > m) {
/* 228 */             n = m - i1;
/*     */           }
/* 230 */           localBoundedRangeModel.setRangeProperties(n, i1, localBoundedRangeModel.getMinimum(), m, false);
/*     */ 
/* 232 */           if (j < localRectangle.width)
/*     */           {
/* 234 */             int i2 = localRectangle.width - 1 - j;
/*     */ 
/* 236 */             int i3 = ((JTextField)localContainer).getHorizontalAlignment();
/* 237 */             if (isLeftToRight(localContainer)) {
/* 238 */               if (i3 == 10) {
/* 239 */                 i3 = 2;
/*     */               }
/* 241 */               else if (i3 == 11) {
/* 242 */                 i3 = 4;
/*     */               }
/*     */ 
/*     */             }
/* 246 */             else if (i3 == 10) {
/* 247 */               i3 = 4;
/*     */             }
/* 249 */             else if (i3 == 11) {
/* 250 */               i3 = 2;
/*     */             }
/*     */ 
/* 254 */             switch (i3) {
/*     */             case 0:
/* 256 */               localRectangle.x += i2 / 2;
/* 257 */               localRectangle.width -= i2;
/* 258 */               break;
/*     */             case 4:
/* 260 */               localRectangle.x += i2;
/* 261 */               localRectangle.width -= i2;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 266 */             localRectangle.width = j;
/* 267 */             localRectangle.x -= localBoundedRangeModel.getValue();
/*     */           }
/*     */         }
/* 270 */         return localRectangle;
/*     */       }
/* 272 */       return null;
/*     */     }
/*     */ 
/*     */     void updateVisibilityModel()
/*     */     {
/* 284 */       Container localContainer = getContainer();
/* 285 */       if ((localContainer instanceof JTextField)) {
/* 286 */         JTextField localJTextField = (JTextField)localContainer;
/* 287 */         BoundedRangeModel localBoundedRangeModel = localJTextField.getHorizontalVisibility();
/* 288 */         int i = (int)getPreferredSpan(0);
/* 289 */         int j = localBoundedRangeModel.getExtent();
/* 290 */         int k = Math.max(i, j);
/* 291 */         j = j == 0 ? k : j;
/* 292 */         int m = k - j;
/* 293 */         int n = localBoundedRangeModel.getValue();
/* 294 */         if (n + j > k) {
/* 295 */           n = k - j;
/*     */         }
/* 297 */         m = Math.max(0, Math.min(m, n));
/* 298 */         localBoundedRangeModel.setRangeProperties(m, j, 0, k, false);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics, Shape paramShape)
/*     */     {
/* 315 */       Rectangle localRectangle = (Rectangle)paramShape;
/* 316 */       paramGraphics.clipRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/* 317 */       super.paint(paramGraphics, adjustAllocation(paramShape));
/*     */     }
/*     */ 
/*     */     public int getResizeWeight(int paramInt)
/*     */     {
/* 328 */       if (paramInt == 0) {
/* 329 */         return 1;
/*     */       }
/* 331 */       return 0;
/*     */     }
/*     */ 
/*     */     public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */       throws BadLocationException
/*     */     {
/* 346 */       return super.modelToView(paramInt, adjustAllocation(paramShape), paramBias);
/*     */     }
/*     */ 
/*     */     public Shape modelToView(int paramInt1, Position.Bias paramBias1, int paramInt2, Position.Bias paramBias2, Shape paramShape)
/*     */       throws BadLocationException
/*     */     {
/* 372 */       return super.modelToView(paramInt1, paramBias1, paramInt2, paramBias2, adjustAllocation(paramShape));
/*     */     }
/*     */ 
/*     */     public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */     {
/* 387 */       return super.viewToModel(paramFloat1, paramFloat2, adjustAllocation(paramShape), paramArrayOfBias);
/*     */     }
/*     */ 
/*     */     public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */     {
/* 400 */       super.insertUpdate(paramDocumentEvent, adjustAllocation(paramShape), paramViewFactory);
/* 401 */       updateVisibilityModel();
/*     */     }
/*     */ 
/*     */     public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */     {
/* 414 */       super.removeUpdate(paramDocumentEvent, adjustAllocation(paramShape), paramViewFactory);
/* 415 */       updateVisibilityModel();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicTextFieldUI
 * JD-Core Version:    0.6.2
 */