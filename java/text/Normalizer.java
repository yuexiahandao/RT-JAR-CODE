/*     */ package java.text;
/*     */ 
/*     */ import sun.text.normalizer.NormalizerBase;
/*     */ 
/*     */ public final class Normalizer
/*     */ {
/*     */   public static String normalize(CharSequence paramCharSequence, Form paramForm)
/*     */   {
/* 164 */     return NormalizerBase.normalize(paramCharSequence.toString(), paramForm);
/*     */   }
/*     */ 
/*     */   public static boolean isNormalized(CharSequence paramCharSequence, Form paramForm)
/*     */   {
/* 181 */     return NormalizerBase.isNormalized(paramCharSequence.toString(), paramForm);
/*     */   }
/*     */ 
/*     */   public static enum Form
/*     */   {
/* 131 */     NFD, 
/*     */ 
/* 136 */     NFC, 
/*     */ 
/* 141 */     NFKD, 
/*     */ 
/* 146 */     NFKC;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.Normalizer
 * JD-Core Version:    0.6.2
 */