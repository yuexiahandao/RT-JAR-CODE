/*    */ package sun.text;
/*    */ 
/*    */ import java.text.Normalizer.Form;
/*    */ import sun.text.normalizer.NormalizerBase;
/*    */ import sun.text.normalizer.NormalizerImpl;
/*    */ 
/*    */ public final class Normalizer
/*    */ {
/*    */   public static final int UNICODE_3_2 = 262432;
/*    */ 
/*    */   public static String normalize(CharSequence paramCharSequence, Normalizer.Form paramForm, int paramInt)
/*    */   {
/* 66 */     return NormalizerBase.normalize(paramCharSequence.toString(), paramForm, paramInt);
/*    */   }
/*    */ 
/*    */   public static boolean isNormalized(CharSequence paramCharSequence, Normalizer.Form paramForm, int paramInt)
/*    */   {
/* 87 */     return NormalizerBase.isNormalized(paramCharSequence.toString(), paramForm, paramInt);
/*    */   }
/*    */ 
/*    */   public static final int getCombiningClass(int paramInt)
/*    */   {
/* 96 */     return NormalizerImpl.getCombiningClass(paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.Normalizer
 * JD-Core Version:    0.6.2
 */