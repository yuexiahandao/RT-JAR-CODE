/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.text.Element;
/*     */ 
/*     */ public class ListView extends BlockView
/*     */ {
/*     */   private StyleSheet.ListPainter listPainter;
/*     */ 
/*     */   public ListView(Element paramElement)
/*     */   {
/*  44 */     super(paramElement, 1);
/*     */   }
/*     */ 
/*     */   public float getAlignment(int paramInt)
/*     */   {
/*  54 */     switch (paramInt) {
/*     */     case 0:
/*  56 */       return 0.5F;
/*     */     case 1:
/*  58 */       return 0.5F;
/*     */     }
/*  60 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/*  73 */     super.paint(paramGraphics, paramShape);
/*  74 */     Rectangle localRectangle1 = paramShape.getBounds();
/*  75 */     Rectangle localRectangle2 = paramGraphics.getClipBounds();
/*     */ 
/*  80 */     if (localRectangle2.x + localRectangle2.width < localRectangle1.x + getLeftInset()) {
/*  81 */       Rectangle localRectangle3 = localRectangle1;
/*  82 */       localRectangle1 = getInsideAllocation(paramShape);
/*  83 */       int i = getViewCount();
/*  84 */       int j = localRectangle2.y + localRectangle2.height;
/*  85 */       for (int k = 0; k < i; k++) {
/*  86 */         localRectangle3.setBounds(localRectangle1);
/*  87 */         childAllocation(k, localRectangle3);
/*  88 */         if (localRectangle3.y >= j) break;
/*  89 */         if (localRectangle3.y + localRectangle3.height >= localRectangle2.y)
/*  90 */           this.listPainter.paint(paramGraphics, localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height, this, k);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintChild(Graphics paramGraphics, Rectangle paramRectangle, int paramInt)
/*     */   {
/* 112 */     this.listPainter.paint(paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, this, paramInt);
/* 113 */     super.paintChild(paramGraphics, paramRectangle, paramInt);
/*     */   }
/*     */ 
/*     */   protected void setPropertiesFromAttributes() {
/* 117 */     super.setPropertiesFromAttributes();
/* 118 */     this.listPainter = getStyleSheet().getListPainter(getAttributes());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.ListView
 * JD-Core Version:    0.6.2
 */