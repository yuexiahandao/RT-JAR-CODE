/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.text.ComponentView;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ class EditableView extends ComponentView
/*     */ {
/*     */   private boolean isVisible;
/*     */ 
/*     */   EditableView(Element paramElement)
/*     */   {
/*  48 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   public float getMinimumSpan(int paramInt) {
/*  52 */     if (this.isVisible) {
/*  53 */       return super.getMinimumSpan(paramInt);
/*     */     }
/*  55 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt) {
/*  59 */     if (this.isVisible) {
/*  60 */       return super.getPreferredSpan(paramInt);
/*     */     }
/*  62 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public float getMaximumSpan(int paramInt) {
/*  66 */     if (this.isVisible) {
/*  67 */       return super.getMaximumSpan(paramInt);
/*     */     }
/*  69 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape) {
/*  73 */     Component localComponent = getComponent();
/*  74 */     Container localContainer = getContainer();
/*     */ 
/*  76 */     if ((localContainer != null) && (this.isVisible != ((JTextComponent)localContainer).isEditable()))
/*     */     {
/*  78 */       this.isVisible = ((JTextComponent)localContainer).isEditable();
/*  79 */       preferenceChanged(null, true, true);
/*  80 */       localContainer.repaint();
/*     */     }
/*     */ 
/*  93 */     if (this.isVisible) {
/*  94 */       super.paint(paramGraphics, paramShape);
/*     */     }
/*     */     else {
/*  97 */       setSize(0.0F, 0.0F);
/*     */     }
/*  99 */     if (localComponent != null)
/* 100 */       localComponent.setFocusable(this.isVisible);
/*     */   }
/*     */ 
/*     */   public void setParent(View paramView)
/*     */   {
/* 105 */     if (paramView != null) {
/* 106 */       Container localContainer = paramView.getContainer();
/* 107 */       if (localContainer != null) {
/* 108 */         if ((localContainer instanceof JTextComponent))
/* 109 */           this.isVisible = ((JTextComponent)localContainer).isEditable();
/*     */         else {
/* 111 */           this.isVisible = false;
/*     */         }
/*     */       }
/*     */     }
/* 115 */     super.setParent(paramView);
/*     */   }
/*     */ 
/*     */   public boolean isVisible()
/*     */   {
/* 122 */     return this.isVisible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.EditableView
 * JD-Core Version:    0.6.2
 */