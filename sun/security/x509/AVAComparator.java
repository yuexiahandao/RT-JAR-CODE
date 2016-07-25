/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ 
/*     */ class AVAComparator
/*     */   implements Comparator<AVA>
/*     */ {
/* 482 */   private static final Comparator<AVA> INSTANCE = new AVAComparator();
/*     */ 
/*     */   static Comparator<AVA> getInstance()
/*     */   {
/* 489 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public int compare(AVA paramAVA1, AVA paramAVA2)
/*     */   {
/* 497 */     boolean bool1 = paramAVA1.hasRFC2253Keyword();
/* 498 */     boolean bool2 = paramAVA2.hasRFC2253Keyword();
/*     */ 
/* 500 */     if (bool1 == bool2) {
/* 501 */       return paramAVA1.toRFC2253CanonicalString().compareTo(paramAVA2.toRFC2253CanonicalString());
/*     */     }
/*     */ 
/* 504 */     if (bool1) {
/* 505 */       return -1;
/*     */     }
/* 507 */     return 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.AVAComparator
 * JD-Core Version:    0.6.2
 */