/*     */ package javax.swing.text;
/*     */ 
/*     */ public class DocumentFilter
/*     */ {
/*     */   public void remove(FilterBypass paramFilterBypass, int paramInt1, int paramInt2)
/*     */     throws BadLocationException
/*     */   {
/*  79 */     paramFilterBypass.remove(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void insertString(FilterBypass paramFilterBypass, int paramInt, String paramString, AttributeSet paramAttributeSet)
/*     */     throws BadLocationException
/*     */   {
/* 100 */     paramFilterBypass.insertString(paramInt, paramString, paramAttributeSet);
/*     */   }
/*     */ 
/*     */   public void replace(FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*     */     throws BadLocationException
/*     */   {
/* 120 */     paramFilterBypass.replace(paramInt1, paramInt2, paramString, paramAttributeSet);
/*     */   }
/*     */ 
/*     */   public static abstract class FilterBypass
/*     */   {
/*     */     public abstract Document getDocument();
/*     */ 
/*     */     public abstract void remove(int paramInt1, int paramInt2)
/*     */       throws BadLocationException;
/*     */ 
/*     */     public abstract void insertString(int paramInt, String paramString, AttributeSet paramAttributeSet)
/*     */       throws BadLocationException;
/*     */ 
/*     */     public abstract void replace(int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*     */       throws BadLocationException;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.DocumentFilter
 * JD-Core Version:    0.6.2
 */