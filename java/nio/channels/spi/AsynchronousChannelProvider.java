/*     */ package java.nio.channels.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.AsynchronousChannelGroup;
/*     */ import java.nio.channels.AsynchronousServerSocketChannel;
/*     */ import java.nio.channels.AsynchronousSocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import sun.nio.ch.DefaultAsynchronousChannelProvider;
/*     */ 
/*     */ public abstract class AsynchronousChannelProvider
/*     */ {
/*     */   private static Void checkPermission()
/*     */   {
/*  55 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  56 */     if (localSecurityManager != null)
/*  57 */       localSecurityManager.checkPermission(new RuntimePermission("asynchronousChannelProvider"));
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   private AsynchronousChannelProvider(Void paramVoid)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected AsynchronousChannelProvider()
/*     */   {
/*  70 */     this(checkPermission());
/*     */   }
/*     */ 
/*     */   public static AsynchronousChannelProvider provider()
/*     */   {
/* 166 */     return ProviderHolder.provider;
/*     */   }
/*     */ 
/*     */   public abstract AsynchronousChannelGroup openAsynchronousChannelGroup(int paramInt, ThreadFactory paramThreadFactory)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract AsynchronousChannelGroup openAsynchronousChannelGroup(ExecutorService paramExecutorService, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract AsynchronousServerSocketChannel openAsynchronousServerSocketChannel(AsynchronousChannelGroup paramAsynchronousChannelGroup)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract AsynchronousSocketChannel openAsynchronousSocketChannel(AsynchronousChannelGroup paramAsynchronousChannelGroup)
/*     */     throws IOException;
/*     */ 
/*     */   private static class ProviderHolder
/*     */   {
/*  75 */     static final AsynchronousChannelProvider provider = load();
/*     */ 
/*     */     private static AsynchronousChannelProvider load() {
/*  78 */       return (AsynchronousChannelProvider)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public AsynchronousChannelProvider run()
/*     */         {
/*  82 */           AsynchronousChannelProvider localAsynchronousChannelProvider = AsynchronousChannelProvider.ProviderHolder.access$000();
/*  83 */           if (localAsynchronousChannelProvider != null)
/*  84 */             return localAsynchronousChannelProvider;
/*  85 */           localAsynchronousChannelProvider = AsynchronousChannelProvider.ProviderHolder.access$100();
/*  86 */           if (localAsynchronousChannelProvider != null)
/*  87 */             return localAsynchronousChannelProvider;
/*  88 */           return DefaultAsynchronousChannelProvider.create();
/*     */         } } );
/*     */     }
/*     */ 
/*     */     private static AsynchronousChannelProvider loadProviderFromProperty() {
/*  93 */       String str = System.getProperty("java.nio.channels.spi.AsynchronousChannelProvider");
/*  94 */       if (str == null)
/*  95 */         return null;
/*     */       try {
/*  97 */         Class localClass = Class.forName(str, true, ClassLoader.getSystemClassLoader());
/*     */ 
/*  99 */         return (AsynchronousChannelProvider)localClass.newInstance();
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 101 */         throw new ServiceConfigurationError(null, localClassNotFoundException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 103 */         throw new ServiceConfigurationError(null, localIllegalAccessException);
/*     */       } catch (InstantiationException localInstantiationException) {
/* 105 */         throw new ServiceConfigurationError(null, localInstantiationException);
/*     */       } catch (SecurityException localSecurityException) {
/* 107 */         throw new ServiceConfigurationError(null, localSecurityException);
/*     */       }
/*     */     }
/*     */ 
/*     */     private static AsynchronousChannelProvider loadProviderAsService() {
/* 112 */       ServiceLoader localServiceLoader = ServiceLoader.load(AsynchronousChannelProvider.class, ClassLoader.getSystemClassLoader());
/*     */ 
/* 115 */       Iterator localIterator = localServiceLoader.iterator();
/*     */       do
/*     */         try {
/* 118 */           return localIterator.hasNext() ? (AsynchronousChannelProvider)localIterator.next() : null; } catch (ServiceConfigurationError localServiceConfigurationError) {
/*     */         }
/* 120 */       while ((localServiceConfigurationError.getCause() instanceof SecurityException));
/*     */ 
/* 124 */       throw localServiceConfigurationError;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.spi.AsynchronousChannelProvider
 * JD-Core Version:    0.6.2
 */