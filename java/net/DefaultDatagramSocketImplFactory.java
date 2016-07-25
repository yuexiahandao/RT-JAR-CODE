/*     */ package java.net;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Properties;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class DefaultDatagramSocketImplFactory
/*     */ {
/*  48 */   static Class prefixImplClass = null;
/*     */   private static float version;
/*  54 */   private static boolean preferIPv4Stack = false;
/*     */ 
/*  57 */   private static boolean useDualStackImpl = false;
/*     */   private static String exclBindProp;
/*  63 */   private static boolean exclusiveBind = true;
/*     */ 
/*     */   static DatagramSocketImpl createDatagramSocketImpl(boolean paramBoolean)
/*     */     throws SocketException
/*     */   {
/* 122 */     if (prefixImplClass != null) {
/*     */       try {
/* 124 */         return (DatagramSocketImpl)prefixImplClass.newInstance();
/*     */       } catch (Exception localException) {
/* 126 */         throw new SocketException("can't instantiate DatagramSocketImpl");
/*     */       }
/*     */     }
/* 129 */     if (paramBoolean)
/* 130 */       exclusiveBind = false;
/* 131 */     if ((useDualStackImpl) && (!paramBoolean)) {
/* 132 */       return new DualStackPlainDatagramSocketImpl(exclusiveBind);
/*     */     }
/* 134 */     return new TwoStacksPlainDatagramSocketImpl(exclusiveBind);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  68 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  71 */         DefaultDatagramSocketImplFactory.access$002(0.0F);
/*     */         try {
/*  73 */           DefaultDatagramSocketImplFactory.access$002(Float.parseFloat(System.getProperties().getProperty("os.version")));
/*     */ 
/*  75 */           DefaultDatagramSocketImplFactory.access$102(Boolean.parseBoolean(System.getProperties().getProperty("java.net.preferIPv4Stack")));
/*     */ 
/*  79 */           DefaultDatagramSocketImplFactory.access$202(System.getProperty("sun.net.useExclusiveBind"));
/*     */         }
/*     */         catch (NumberFormatException localNumberFormatException) {
/*  82 */           if (!$assertionsDisabled) throw new AssertionError(localNumberFormatException);
/*     */         }
/*  84 */         return null;
/*     */       }
/*     */     });
/*  89 */     if ((version >= 6.0D) && (!preferIPv4Stack)) {
/*  90 */       useDualStackImpl = true;
/*     */     }
/*  92 */     if (exclBindProp != null)
/*     */     {
/*  94 */       exclusiveBind = exclBindProp.length() == 0 ? true : Boolean.parseBoolean(exclBindProp);
/*     */     }
/*  96 */     else if (version < 6.0D) {
/*  97 */       exclusiveBind = false;
/*     */     }
/*     */ 
/* 101 */     String str = null;
/*     */     try {
/* 103 */       str = (String)AccessController.doPrivileged(new GetPropertyAction("impl.prefix", null));
/*     */ 
/* 105 */       if (str != null)
/* 106 */         prefixImplClass = Class.forName("java.net." + str + "DatagramSocketImpl");
/*     */     } catch (Exception localException) {
/* 108 */       System.err.println("Can't find class: java.net." + str + "DatagramSocketImpl: check impl.prefix property");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.DefaultDatagramSocketImplFactory
 * JD-Core Version:    0.6.2
 */