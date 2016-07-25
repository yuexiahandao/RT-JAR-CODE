/*     */ package sun.net.ftp;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import sun.net.ftp.impl.DefaultFtpClientProvider;
/*     */ 
/*     */ public abstract class FtpClientProvider
/*     */ {
/*  48 */   private static final Object lock = new Object();
/*  49 */   private static FtpClientProvider provider = null;
/*     */ 
/*     */   public abstract FtpClient createFtpClient();
/*     */ 
/*     */   protected FtpClientProvider()
/*     */   {
/*  58 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  59 */     if (localSecurityManager != null)
/*  60 */       localSecurityManager.checkPermission(new RuntimePermission("ftpClientProvider"));
/*     */   }
/*     */ 
/*     */   private static boolean loadProviderFromProperty()
/*     */   {
/*  65 */     String str = System.getProperty("sun.net.ftpClientProvider");
/*  66 */     if (str == null)
/*  67 */       return false;
/*     */     try
/*     */     {
/*  70 */       Class localClass = Class.forName(str, true, null);
/*  71 */       provider = (FtpClientProvider)localClass.newInstance();
/*  72 */       return true;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  74 */       throw new ServiceConfigurationError(localClassNotFoundException.toString());
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  76 */       throw new ServiceConfigurationError(localIllegalAccessException.toString());
/*     */     } catch (InstantiationException localInstantiationException) {
/*  78 */       throw new ServiceConfigurationError(localInstantiationException.toString());
/*     */     } catch (SecurityException localSecurityException) {
/*  80 */       throw new ServiceConfigurationError(localSecurityException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean loadProviderAsService()
/*     */   {
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   public static FtpClientProvider provider()
/*     */   {
/* 138 */     synchronized (lock) {
/* 139 */       if (provider != null) {
/* 140 */         return provider;
/*     */       }
/* 142 */       return (FtpClientProvider)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run()
/*     */         {
/* 146 */           if (FtpClientProvider.access$000()) {
/* 147 */             return FtpClientProvider.provider;
/*     */           }
/* 149 */           if (FtpClientProvider.access$200()) {
/* 150 */             return FtpClientProvider.provider;
/*     */           }
/* 152 */           FtpClientProvider.access$102(new DefaultFtpClientProvider());
/* 153 */           return FtpClientProvider.provider;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ftp.FtpClientProvider
 * JD-Core Version:    0.6.2
 */