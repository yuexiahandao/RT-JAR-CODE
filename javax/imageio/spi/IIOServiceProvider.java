/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.util.Locale;
/*     */ 
/*     */ public abstract class IIOServiceProvider
/*     */   implements RegisterableService
/*     */ {
/*     */   protected String vendorName;
/*     */   protected String version;
/*     */ 
/*     */   public IIOServiceProvider(String paramString1, String paramString2)
/*     */   {
/*  75 */     if (paramString1 == null) {
/*  76 */       throw new IllegalArgumentException("vendorName == null!");
/*     */     }
/*  78 */     if (paramString2 == null) {
/*  79 */       throw new IllegalArgumentException("version == null!");
/*     */     }
/*  81 */     this.vendorName = paramString1;
/*  82 */     this.version = paramString2;
/*     */   }
/*     */ 
/*     */   public IIOServiceProvider()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onRegistration(ServiceRegistry paramServiceRegistry, Class<?> paramClass)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onDeregistration(ServiceRegistry paramServiceRegistry, Class<?> paramClass)
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getVendorName()
/*     */   {
/* 139 */     return this.vendorName;
/*     */   }
/*     */ 
/*     */   public String getVersion()
/*     */   {
/* 156 */     return this.version;
/*     */   }
/*     */ 
/*     */   public abstract String getDescription(Locale paramLocale);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.IIOServiceProvider
 * JD-Core Version:    0.6.2
 */