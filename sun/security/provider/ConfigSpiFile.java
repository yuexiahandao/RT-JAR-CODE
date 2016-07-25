/*     */ package sun.security.provider;
/*     */ 
/*     */ import com.sun.security.auth.login.ConfigFile;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.URIParameter;
/*     */ import javax.security.auth.login.AppConfigurationEntry;
/*     */ import javax.security.auth.login.Configuration.Parameters;
/*     */ import javax.security.auth.login.ConfigurationSpi;
/*     */ 
/*     */ public final class ConfigSpiFile extends ConfigurationSpi
/*     */ {
/*     */   private ConfigFile cf;
/*     */ 
/*     */   public ConfigSpiFile(final Configuration.Parameters paramParameters)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  61 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Void run() {
/*  63 */           if (paramParameters == null) {
/*  64 */             ConfigSpiFile.this.cf = new ConfigFile();
/*     */           } else {
/*  66 */             if (!(paramParameters instanceof URIParameter)) {
/*  67 */               throw new IllegalArgumentException("Unrecognized parameter: " + paramParameters);
/*     */             }
/*     */ 
/*  70 */             URIParameter localURIParameter = (URIParameter)paramParameters;
/*     */ 
/*  72 */             ConfigSpiFile.this.cf = new ConfigFile(localURIParameter.getURI());
/*     */           }
/*  74 */           return null;
/*     */         }
/*     */ 
/*     */       });
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/*  84 */       Throwable localThrowable = localSecurityException.getCause();
/*  85 */       if ((localThrowable != null) && ((localThrowable instanceof IOException))) {
/*  86 */         throw ((IOException)localThrowable);
/*     */       }
/*     */ 
/*  90 */       throw localSecurityException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected AppConfigurationEntry[] engineGetAppConfigurationEntry(String paramString)
/*     */   {
/*  99 */     return this.cf.getAppConfigurationEntry(paramString);
/*     */   }
/*     */ 
/*     */   protected void engineRefresh() {
/* 103 */     this.cf.refresh();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.ConfigSpiFile
 * JD-Core Version:    0.6.2
 */