/*     */ package javax.swing.text;
/*     */ 
/*     */ import javax.swing.plaf.TextUI;
/*     */ 
/*     */ public class NavigationFilter
/*     */ {
/*     */   public void setDot(FilterBypass paramFilterBypass, int paramInt, Position.Bias paramBias)
/*     */   {
/*  64 */     paramFilterBypass.setDot(paramInt, paramBias);
/*     */   }
/*     */ 
/*     */   public void moveDot(FilterBypass paramFilterBypass, int paramInt, Position.Bias paramBias)
/*     */   {
/*  79 */     paramFilterBypass.moveDot(paramInt, paramBias);
/*     */   }
/*     */ 
/*     */   public int getNextVisualPositionFrom(JTextComponent paramJTextComponent, int paramInt1, Position.Bias paramBias, int paramInt2, Position.Bias[] paramArrayOfBias)
/*     */     throws BadLocationException
/*     */   {
/* 111 */     return paramJTextComponent.getUI().getNextVisualPositionFrom(paramJTextComponent, paramInt1, paramBias, paramInt2, paramArrayOfBias);
/*     */   }
/*     */ 
/*     */   public static abstract class FilterBypass
/*     */   {
/*     */     public abstract Caret getCaret();
/*     */ 
/*     */     public abstract void setDot(int paramInt, Position.Bias paramBias);
/*     */ 
/*     */     public abstract void moveDot(int paramInt, Position.Bias paramBias);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.NavigationFilter
 * JD-Core Version:    0.6.2
 */