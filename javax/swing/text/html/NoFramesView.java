/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ class NoFramesView extends BlockView
/*     */ {
/*     */   boolean visible;
/*     */ 
/*     */   public NoFramesView(Element paramElement, int paramInt)
/*     */   {
/*  50 */     super(paramElement, paramInt);
/*  51 */     this.visible = false;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/*  65 */     Container localContainer = getContainer();
/*  66 */     if ((localContainer != null) && (this.visible != ((JTextComponent)localContainer).isEditable()))
/*     */     {
/*  68 */       this.visible = ((JTextComponent)localContainer).isEditable();
/*     */     }
/*     */ 
/*  71 */     if (!isVisible()) {
/*  72 */       return;
/*     */     }
/*  74 */     super.paint(paramGraphics, paramShape);
/*     */   }
/*     */ 
/*     */   public void setParent(View paramView)
/*     */   {
/*  89 */     if (paramView != null) {
/*  90 */       Container localContainer = paramView.getContainer();
/*  91 */       if (localContainer != null) {
/*  92 */         this.visible = ((JTextComponent)localContainer).isEditable();
/*     */       }
/*     */     }
/*  95 */     super.setParent(paramView);
/*     */   }
/*     */ 
/*     */   public boolean isVisible()
/*     */   {
/* 103 */     return this.visible;
/*     */   }
/*     */ 
/*     */   protected void layout(int paramInt1, int paramInt2)
/*     */   {
/* 112 */     if (!isVisible()) {
/* 113 */       return;
/*     */     }
/* 115 */     super.layout(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt)
/*     */   {
/* 132 */     if (!this.visible) {
/* 133 */       return 0.0F;
/*     */     }
/* 135 */     return super.getPreferredSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getMinimumSpan(int paramInt)
/*     */   {
/* 149 */     if (!this.visible) {
/* 150 */       return 0.0F;
/*     */     }
/* 152 */     return super.getMinimumSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getMaximumSpan(int paramInt)
/*     */   {
/* 166 */     if (!this.visible) {
/* 167 */       return 0.0F;
/*     */     }
/* 169 */     return super.getMaximumSpan(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.NoFramesView
 * JD-Core Version:    0.6.2
 */