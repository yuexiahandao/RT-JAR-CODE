/*     */ package sun.font;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public final class FontManagerFactory
/*     */ {
/*  50 */   private static FontManager instance = null;
/*     */   private static final String DEFAULT_CLASS;
/*     */ 
/*     */   public static synchronized FontManager getInstance()
/*     */   {
/*  70 */     if (instance != null) {
/*  71 */       return instance;
/*     */     }
/*     */ 
/*  74 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/*  78 */           String str = System.getProperty("sun.font.fontmanager", FontManagerFactory.DEFAULT_CLASS);
/*     */ 
/*  81 */           localObject = ClassLoader.getSystemClassLoader();
/*  82 */           Class localClass = Class.forName(str, true, (ClassLoader)localObject);
/*  83 */           FontManagerFactory.access$102((FontManager)localClass.newInstance());
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/*  85 */           localObject = new InternalError();
/*  86 */           ((InternalError)localObject).initCause(localClassNotFoundException);
/*  87 */           throw ((Throwable)localObject);
/*     */         }
/*     */         catch (InstantiationException localInstantiationException) {
/*  90 */           localObject = new InternalError();
/*  91 */           ((InternalError)localObject).initCause(localInstantiationException);
/*  92 */           throw ((Throwable)localObject);
/*     */         }
/*     */         catch (IllegalAccessException localIllegalAccessException) {
/*  95 */           Object localObject = new InternalError();
/*  96 */           ((InternalError)localObject).initCause(localIllegalAccessException);
/*  97 */           throw ((Throwable)localObject);
/*     */         }
/*  99 */         return null;
/*     */       }
/*     */     });
/* 103 */     return instance;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  54 */     if (FontUtilities.isWindows)
/*  55 */       DEFAULT_CLASS = "sun.awt.Win32FontManager";
/*  56 */     else if (FontUtilities.isMacOSX)
/*  57 */       DEFAULT_CLASS = "sun.font.CFontManager";
/*     */     else
/*  59 */       DEFAULT_CLASS = "sun.awt.X11FontManager";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FontManagerFactory
 * JD-Core Version:    0.6.2
 */