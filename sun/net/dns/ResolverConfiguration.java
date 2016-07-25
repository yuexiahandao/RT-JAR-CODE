/*     */ package sun.net.dns;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class ResolverConfiguration
/*     */ {
/*  42 */   private static final Object lock = new Object();
/*     */   private static ResolverConfiguration provider;
/*     */ 
/*     */   public static ResolverConfiguration open()
/*     */   {
/*  54 */     synchronized (lock) {
/*  55 */       if (provider == null) {
/*  56 */         provider = new ResolverConfigurationImpl();
/*     */       }
/*  58 */       return provider;
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract List<String> searchlist();
/*     */ 
/*     */   public abstract List<String> nameservers();
/*     */ 
/*     */   public abstract Options options();
/*     */ 
/*     */   public static abstract class Options
/*     */   {
/*     */     public int attempts()
/*     */     {
/*  97 */       return -1;
/*     */     }
/*     */ 
/*     */     public int retrans()
/*     */     {
/* 112 */       return -1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.dns.ResolverConfiguration
 * JD-Core Version:    0.6.2
 */