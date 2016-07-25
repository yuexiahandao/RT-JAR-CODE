/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicComboBoxEditor;
/*     */ 
/*     */ public class MetalComboBoxEditor extends BasicComboBoxEditor
/*     */ {
/*  86 */   protected static Insets editorBorderInsets = new Insets(2, 2, 2, 0);
/*     */ 
/*     */   public MetalComboBoxEditor()
/*     */   {
/*  55 */     this.editor = new JTextField("", 9)
/*     */     {
/*     */       public void setText(String paramAnonymousString) {
/*  58 */         if (getText().equals(paramAnonymousString)) {
/*  59 */           return;
/*     */         }
/*  61 */         super.setText(paramAnonymousString);
/*     */       }
/*     */ 
/*     */       public Dimension getPreferredSize()
/*     */       {
/*  67 */         Dimension localDimension = super.getPreferredSize();
/*  68 */         localDimension.height += 4;
/*  69 */         return localDimension;
/*     */       }
/*     */       public Dimension getMinimumSize() {
/*  72 */         Dimension localDimension = super.getMinimumSize();
/*  73 */         localDimension.height += 4;
/*  74 */         return localDimension;
/*     */       }
/*     */     };
/*  78 */     this.editor.setBorder(new EditorBorder());
/*     */   }
/*     */ 
/*     */   class EditorBorder extends AbstractBorder
/*     */   {
/*     */     EditorBorder()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/*  90 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/*  92 */       if (MetalLookAndFeel.usingOcean()) {
/*  93 */         paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/*  94 */         paramGraphics.drawRect(0, 0, paramInt3, paramInt4 - 1);
/*  95 */         paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/*  96 */         paramGraphics.drawRect(1, 1, paramInt3 - 2, paramInt4 - 3);
/*     */       }
/*     */       else {
/*  99 */         paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 100 */         paramGraphics.drawLine(0, 0, paramInt3 - 1, 0);
/* 101 */         paramGraphics.drawLine(0, 0, 0, paramInt4 - 2);
/* 102 */         paramGraphics.drawLine(0, paramInt4 - 2, paramInt3 - 1, paramInt4 - 2);
/* 103 */         paramGraphics.setColor(MetalLookAndFeel.getControlHighlight());
/* 104 */         paramGraphics.drawLine(1, 1, paramInt3 - 1, 1);
/* 105 */         paramGraphics.drawLine(1, 1, 1, paramInt4 - 1);
/* 106 */         paramGraphics.drawLine(1, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/* 107 */         paramGraphics.setColor(MetalLookAndFeel.getControl());
/* 108 */         paramGraphics.drawLine(1, paramInt4 - 2, 1, paramInt4 - 2);
/*     */       }
/*     */ 
/* 111 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 115 */       paramInsets.set(2, 2, 2, 0);
/* 116 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class UIResource extends MetalComboBoxEditor
/*     */     implements UIResource
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalComboBoxEditor
 * JD-Core Version:    0.6.2
 */