/*     */ package sun.text.normalizer;
/*     */ 
/*     */ public class ReplaceableString
/*     */   implements Replaceable
/*     */ {
/*     */   private StringBuffer buf;
/*     */ 
/*     */   public ReplaceableString(String paramString)
/*     */   {
/*  64 */     this.buf = new StringBuffer(paramString);
/*     */   }
/*     */ 
/*     */   public ReplaceableString(StringBuffer paramStringBuffer)
/*     */   {
/*  78 */     this.buf = paramStringBuffer;
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/*  87 */     return this.buf.length();
/*     */   }
/*     */ 
/*     */   public char charAt(int paramInt)
/*     */   {
/*  98 */     return this.buf.charAt(paramInt);
/*     */   }
/*     */ 
/*     */   public void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
/*     */   {
/* 121 */     Utility.getChars(this.buf, paramInt1, paramInt2, paramArrayOfChar, paramInt3);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.ReplaceableString
 * JD-Core Version:    0.6.2
 */