/*     */ package java.nio.channels.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ProtocolFamily;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.nio.channels.Pipe;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import sun.nio.ch.DefaultSelectorProvider;
/*     */ 
/*     */ public abstract class SelectorProvider
/*     */ {
/*  71 */   private static final Object lock = new Object();
/*  72 */   private static SelectorProvider provider = null;
/*     */ 
/*     */   protected SelectorProvider()
/*     */   {
/*  82 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  83 */     if (localSecurityManager != null)
/*  84 */       localSecurityManager.checkPermission(new RuntimePermission("selectorProvider"));
/*     */   }
/*     */ 
/*     */   private static boolean loadProviderFromProperty() {
/*  88 */     String str = System.getProperty("java.nio.channels.spi.SelectorProvider");
/*  89 */     if (str == null)
/*  90 */       return false;
/*     */     try {
/*  92 */       Class localClass = Class.forName(str, true, ClassLoader.getSystemClassLoader());
/*     */ 
/*  94 */       provider = (SelectorProvider)localClass.newInstance();
/*  95 */       return true;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  97 */       throw new ServiceConfigurationError(null, localClassNotFoundException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  99 */       throw new ServiceConfigurationError(null, localIllegalAccessException);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 101 */       throw new ServiceConfigurationError(null, localInstantiationException);
/*     */     } catch (SecurityException localSecurityException) {
/* 103 */       throw new ServiceConfigurationError(null, localSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean loadProviderAsService()
/*     */   {
/* 109 */     ServiceLoader localServiceLoader = ServiceLoader.load(SelectorProvider.class, ClassLoader.getSystemClassLoader());
/*     */ 
/* 112 */     Iterator localIterator = localServiceLoader.iterator();
/*     */     do
/*     */       try {
/* 115 */         if (!localIterator.hasNext())
/* 116 */           return false;
/* 117 */         provider = (SelectorProvider)localIterator.next();
/* 118 */         return true; } catch (ServiceConfigurationError localServiceConfigurationError) {
/*     */       }
/* 120 */     while ((localServiceConfigurationError.getCause() instanceof SecurityException));
/*     */ 
/* 124 */     throw localServiceConfigurationError;
/*     */   }
/*     */ 
/*     */   public static SelectorProvider provider()
/*     */   {
/* 165 */     synchronized (lock) {
/* 166 */       if (provider != null)
/* 167 */         return provider;
/* 168 */       return (SelectorProvider)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public SelectorProvider run() {
/* 171 */           if (SelectorProvider.access$000())
/* 172 */             return SelectorProvider.provider;
/* 173 */           if (SelectorProvider.access$200())
/* 174 */             return SelectorProvider.provider;
/* 175 */           SelectorProvider.access$102(DefaultSelectorProvider.create());
/* 176 */           return SelectorProvider.provider;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract DatagramChannel openDatagramChannel()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract DatagramChannel openDatagramChannel(ProtocolFamily paramProtocolFamily)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Pipe openPipe()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract AbstractSelector openSelector()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract ServerSocketChannel openServerSocketChannel()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SocketChannel openSocketChannel()
/*     */     throws IOException;
/*     */ 
/*     */   public Channel inheritedChannel()
/*     */     throws IOException
/*     */   {
/* 299 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.spi.SelectorProvider
 * JD-Core Version:    0.6.2
 */