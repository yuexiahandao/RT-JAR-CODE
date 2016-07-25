/*     */ package javax.swing.plaf;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Position.Bias;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ public abstract class TextUI extends ComponentUI
/*     */ {
/*     */   public abstract Rectangle modelToView(JTextComponent paramJTextComponent, int paramInt)
/*     */     throws BadLocationException;
/*     */ 
/*     */   public abstract Rectangle modelToView(JTextComponent paramJTextComponent, int paramInt, Position.Bias paramBias)
/*     */     throws BadLocationException;
/*     */ 
/*     */   public abstract int viewToModel(JTextComponent paramJTextComponent, Point paramPoint);
/*     */ 
/*     */   public abstract int viewToModel(JTextComponent paramJTextComponent, Point paramPoint, Position.Bias[] paramArrayOfBias);
/*     */ 
/*     */   public abstract int getNextVisualPositionFrom(JTextComponent paramJTextComponent, int paramInt1, Position.Bias paramBias, int paramInt2, Position.Bias[] paramArrayOfBias)
/*     */     throws BadLocationException;
/*     */ 
/*     */   public abstract void damageRange(JTextComponent paramJTextComponent, int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract void damageRange(JTextComponent paramJTextComponent, int paramInt1, int paramInt2, Position.Bias paramBias1, Position.Bias paramBias2);
/*     */ 
/*     */   public abstract EditorKit getEditorKit(JTextComponent paramJTextComponent);
/*     */ 
/*     */   public abstract View getRootView(JTextComponent paramJTextComponent);
/*     */ 
/*     */   public String getToolTipText(JTextComponent paramJTextComponent, Point paramPoint)
/*     */   {
/* 163 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.TextUI
 * JD-Core Version:    0.6.2
 */