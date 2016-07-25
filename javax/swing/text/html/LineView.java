/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Toolkit;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.StyledDocument;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ class LineView extends ParagraphView
/*     */ {
/*     */   int tabBase;
/*     */ 
/*     */   public LineView(Element paramElement)
/*     */   {
/*  52 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   public boolean isVisible()
/*     */   {
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   public float getMinimumSpan(int paramInt)
/*     */   {
/*  74 */     return getPreferredSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public int getResizeWeight(int paramInt)
/*     */   {
/*  84 */     switch (paramInt) {
/*     */     case 0:
/*  86 */       return 1;
/*     */     case 1:
/*  88 */       return 0;
/*     */     }
/*  90 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */   }
/*     */ 
/*     */   public float getAlignment(int paramInt)
/*     */   {
/* 101 */     if (paramInt == 0) {
/* 102 */       return 0.0F;
/*     */     }
/* 104 */     return super.getAlignment(paramInt);
/*     */   }
/*     */ 
/*     */   protected void layout(int paramInt1, int paramInt2)
/*     */   {
/* 123 */     super.layout(2147483646, paramInt2);
/*     */   }
/*     */ 
/*     */   public float nextTabStop(float paramFloat, int paramInt)
/*     */   {
/* 153 */     if ((getTabSet() == null) && (StyleConstants.getAlignment(getAttributes()) == 0))
/*     */     {
/* 156 */       return getPreTab(paramFloat, paramInt);
/*     */     }
/* 158 */     return super.nextTabStop(paramFloat, paramInt);
/*     */   }
/*     */ 
/*     */   protected float getPreTab(float paramFloat, int paramInt)
/*     */   {
/* 165 */     Document localDocument = getDocument();
/* 166 */     View localView = getViewAtPosition(paramInt, null);
/* 167 */     if (((localDocument instanceof StyledDocument)) && (localView != null))
/*     */     {
/* 169 */       Font localFont = ((StyledDocument)localDocument).getFont(localView.getAttributes());
/* 170 */       Container localContainer = getContainer();
/* 171 */       FontMetrics localFontMetrics = localContainer != null ? localContainer.getFontMetrics(localFont) : Toolkit.getDefaultToolkit().getFontMetrics(localFont);
/*     */ 
/* 173 */       int i = getCharactersPerTab() * localFontMetrics.charWidth('W');
/* 174 */       int j = (int)getTabBase();
/* 175 */       return (((int)paramFloat - j) / i + 1) * i + j;
/*     */     }
/* 177 */     return 10.0F + paramFloat;
/*     */   }
/*     */ 
/*     */   protected int getCharactersPerTab()
/*     */   {
/* 184 */     return 8;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.LineView
 * JD-Core Version:    0.6.2
 */