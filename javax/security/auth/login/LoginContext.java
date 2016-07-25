/*     */ package javax.security.auth.login;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.Security;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.AuthPermission;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.PendingException;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class LoginContext
/*     */ {
/*     */   private static final String INIT_METHOD = "initialize";
/*     */   private static final String LOGIN_METHOD = "login";
/*     */   private static final String COMMIT_METHOD = "commit";
/*     */   private static final String ABORT_METHOD = "abort";
/*     */   private static final String LOGOUT_METHOD = "logout";
/*     */   private static final String OTHER = "other";
/*     */   private static final String DEFAULT_HANDLER = "auth.login.defaultCallbackHandler";
/* 213 */   private Subject subject = null;
/* 214 */   private boolean subjectProvided = false;
/* 215 */   private boolean loginSucceeded = false;
/*     */   private CallbackHandler callbackHandler;
/* 217 */   private Map state = new HashMap();
/*     */   private Configuration config;
/* 220 */   private AccessControlContext creatorAcc = null;
/*     */   private ModuleInfo[] moduleStack;
/* 222 */   private ClassLoader contextClassLoader = null;
/* 223 */   private static final Class[] PARAMS = new Class[0];
/*     */ 
/* 228 */   private int moduleIndex = 0;
/* 229 */   private LoginException firstError = null;
/* 230 */   private LoginException firstRequiredError = null;
/* 231 */   private boolean success = false;
/*     */ 
/* 233 */   private static final Debug debug = Debug.getInstance("logincontext", "\t[LoginContext]");
/*     */ 
/*     */   private void init(String paramString)
/*     */     throws LoginException
/*     */   {
/* 238 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 239 */     if ((localSecurityManager != null) && (this.creatorAcc == null)) {
/* 240 */       localSecurityManager.checkPermission(new AuthPermission("createLoginContext." + paramString));
/*     */     }
/*     */ 
/* 244 */     if (paramString == null) {
/* 245 */       throw new LoginException(ResourcesMgr.getString("Invalid.null.input.name"));
/*     */     }
/*     */ 
/* 249 */     if (this.config == null) {
/* 250 */       this.config = ((Configuration)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Configuration run() {
/* 253 */           return Configuration.getConfiguration();
/*     */         }
/*     */ 
/*     */       }));
/*     */     }
/*     */ 
/* 259 */     AppConfigurationEntry[] arrayOfAppConfigurationEntry = this.config.getAppConfigurationEntry(paramString);
/* 260 */     if (arrayOfAppConfigurationEntry == null)
/*     */     {
/* 262 */       if ((localSecurityManager != null) && (this.creatorAcc == null)) {
/* 263 */         localSecurityManager.checkPermission(new AuthPermission("createLoginContext.other"));
/*     */       }
/*     */ 
/* 267 */       arrayOfAppConfigurationEntry = this.config.getAppConfigurationEntry("other");
/* 268 */       if (arrayOfAppConfigurationEntry == null) {
/* 269 */         MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("No.LoginModules.configured.for.name"));
/*     */ 
/* 271 */         Object[] arrayOfObject = { paramString };
/* 272 */         throw new LoginException(localMessageFormat.format(arrayOfObject));
/*     */       }
/*     */     }
/* 275 */     this.moduleStack = new ModuleInfo[arrayOfAppConfigurationEntry.length];
/* 276 */     for (int i = 0; i < arrayOfAppConfigurationEntry.length; i++)
/*     */     {
/* 278 */       this.moduleStack[i] = new ModuleInfo(new AppConfigurationEntry(arrayOfAppConfigurationEntry[i].getLoginModuleName(), arrayOfAppConfigurationEntry[i].getControlFlag(), arrayOfAppConfigurationEntry[i].getOptions()), null);
/*     */     }
/*     */ 
/* 286 */     this.contextClassLoader = ((ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ClassLoader run() {
/* 289 */         ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 291 */         if (localClassLoader == null)
/*     */         {
/* 294 */           localClassLoader = ClassLoader.getSystemClassLoader();
/*     */         }
/*     */ 
/* 297 */         return localClassLoader;
/*     */       }
/*     */     }));
/*     */   }
/*     */ 
/*     */   private void loadDefaultCallbackHandler()
/*     */     throws LoginException
/*     */   {
/*     */     try
/*     */     {
/* 307 */       final ClassLoader localClassLoader = this.contextClassLoader;
/*     */ 
/* 309 */       this.callbackHandler = ((CallbackHandler)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public CallbackHandler run() throws Exception {
/* 312 */           String str = Security.getProperty("auth.login.defaultCallbackHandler");
/*     */ 
/* 314 */           if ((str == null) || (str.length() == 0))
/* 315 */             return null;
/* 316 */           Class localClass = Class.forName(str, true, localClassLoader).asSubclass(CallbackHandler.class);
/*     */ 
/* 319 */           return (CallbackHandler)localClass.newInstance();
/*     */         } } ));
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 323 */       throw new LoginException(localPrivilegedActionException.getException().toString());
/*     */     }
/*     */ 
/* 327 */     if ((this.callbackHandler != null) && (this.creatorAcc == null))
/* 328 */       this.callbackHandler = new SecureCallbackHandler(AccessController.getContext(), this.callbackHandler);
/*     */   }
/*     */ 
/*     */   public LoginContext(String paramString)
/*     */     throws LoginException
/*     */   {
/* 356 */     init(paramString);
/* 357 */     loadDefaultCallbackHandler();
/*     */   }
/*     */ 
/*     */   public LoginContext(String paramString, Subject paramSubject)
/*     */     throws LoginException
/*     */   {
/* 389 */     init(paramString);
/* 390 */     if (paramSubject == null) {
/* 391 */       throw new LoginException(ResourcesMgr.getString("invalid.null.Subject.provided"));
/*     */     }
/* 393 */     this.subject = paramSubject;
/* 394 */     this.subjectProvided = true;
/* 395 */     loadDefaultCallbackHandler();
/*     */   }
/*     */ 
/*     */   public LoginContext(String paramString, CallbackHandler paramCallbackHandler)
/*     */     throws LoginException
/*     */   {
/* 425 */     init(paramString);
/* 426 */     if (paramCallbackHandler == null) {
/* 427 */       throw new LoginException(ResourcesMgr.getString("invalid.null.CallbackHandler.provided"));
/*     */     }
/* 429 */     this.callbackHandler = new SecureCallbackHandler(AccessController.getContext(), paramCallbackHandler);
/*     */   }
/*     */ 
/*     */   public LoginContext(String paramString, Subject paramSubject, CallbackHandler paramCallbackHandler)
/*     */     throws LoginException
/*     */   {
/* 466 */     this(paramString, paramSubject);
/* 467 */     if (paramCallbackHandler == null) {
/* 468 */       throw new LoginException(ResourcesMgr.getString("invalid.null.CallbackHandler.provided"));
/*     */     }
/* 470 */     this.callbackHandler = new SecureCallbackHandler(AccessController.getContext(), paramCallbackHandler);
/*     */   }
/*     */ 
/*     */   public LoginContext(String paramString, Subject paramSubject, CallbackHandler paramCallbackHandler, Configuration paramConfiguration)
/*     */     throws LoginException
/*     */   {
/* 515 */     this.config = paramConfiguration;
/* 516 */     if (paramConfiguration != null) {
/* 517 */       this.creatorAcc = AccessController.getContext();
/*     */     }
/*     */ 
/* 520 */     init(paramString);
/* 521 */     if (paramSubject != null) {
/* 522 */       this.subject = paramSubject;
/* 523 */       this.subjectProvided = true;
/*     */     }
/* 525 */     if (paramCallbackHandler == null)
/* 526 */       loadDefaultCallbackHandler();
/* 527 */     else if (this.creatorAcc == null) {
/* 528 */       this.callbackHandler = new SecureCallbackHandler(AccessController.getContext(), paramCallbackHandler);
/*     */     }
/*     */     else
/*     */     {
/* 532 */       this.callbackHandler = paramCallbackHandler;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void login()
/*     */     throws LoginException
/*     */   {
/* 587 */     this.loginSucceeded = false;
/*     */ 
/* 589 */     if (this.subject == null) {
/* 590 */       this.subject = new Subject();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 595 */       invokePriv("login");
/* 596 */       invokePriv("commit");
/* 597 */       this.loginSucceeded = true;
/*     */     } catch (LoginException localLoginException1) {
/*     */       try {
/* 600 */         invokePriv("abort");
/*     */       } catch (LoginException localLoginException2) {
/* 602 */         throw localLoginException1;
/*     */       }
/* 604 */       throw localLoginException1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void logout()
/*     */     throws LoginException
/*     */   {
/* 630 */     if (this.subject == null) {
/* 631 */       throw new LoginException(ResourcesMgr.getString("null.subject.logout.called.before.login"));
/*     */     }
/*     */ 
/* 636 */     invokePriv("logout");
/*     */   }
/*     */ 
/*     */   public Subject getSubject()
/*     */   {
/* 654 */     if ((!this.loginSucceeded) && (!this.subjectProvided))
/* 655 */       return null;
/* 656 */     return this.subject;
/*     */   }
/*     */ 
/*     */   private void clearState() {
/* 660 */     this.moduleIndex = 0;
/* 661 */     this.firstError = null;
/* 662 */     this.firstRequiredError = null;
/* 663 */     this.success = false;
/*     */   }
/*     */ 
/*     */   private void throwException(LoginException paramLoginException1, LoginException paramLoginException2)
/*     */     throws LoginException
/*     */   {
/* 670 */     clearState();
/*     */ 
/* 673 */     LoginException localLoginException = paramLoginException1 != null ? paramLoginException1 : paramLoginException2;
/* 674 */     throw localLoginException;
/*     */   }
/*     */ 
/*     */   private void invokePriv(final String paramString)
/*     */     throws LoginException
/*     */   {
/*     */     try
/*     */     {
/* 687 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws LoginException {
/* 690 */           LoginContext.this.invoke(paramString);
/* 691 */           return null;
/*     */         }
/*     */       }
/*     */       , this.creatorAcc);
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException)
/*     */     {
/* 695 */       throw ((LoginException)localPrivilegedActionException.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void invoke(String paramString)
/*     */     throws LoginException
/*     */   {
/* 704 */     for (int i = this.moduleIndex; i < this.moduleStack.length; this.moduleIndex += 1)
/*     */     {
/*     */       try {
/* 707 */         int j = 0;
/* 708 */         localObject1 = null;
/*     */ 
/* 710 */         if (this.moduleStack[i].module != null) {
/* 711 */           localObject1 = this.moduleStack[i].module.getClass().getMethods();
/*     */         }
/*     */         else
/*     */         {
/* 718 */           localObject2 = Class.forName(this.moduleStack[i].entry.getLoginModuleName(), true, this.contextClassLoader);
/*     */ 
/* 723 */           Constructor localConstructor = ((Class)localObject2).getConstructor(PARAMS);
/* 724 */           Object[] arrayOfObject1 = new Object[0];
/* 725 */           this.moduleStack[i].module = localConstructor.newInstance(arrayOfObject1);
/*     */ 
/* 728 */           localObject1 = this.moduleStack[i].module.getClass().getMethods();
/* 729 */           for (j = 0; (j < localObject1.length) && 
/* 730 */             (!localObject1[j].getName().equals("initialize")); j++);
/* 735 */           Object[] arrayOfObject2 = { this.subject, this.callbackHandler, this.state, this.moduleStack[i].entry.getOptions() };
/*     */ 
/* 744 */           localObject1[j].invoke(this.moduleStack[i].module, arrayOfObject2);
/*     */         }
/*     */ 
/* 748 */         for (j = 0; (j < localObject1.length) && 
/* 749 */           (!localObject1[j].getName().equals(paramString)); j++);
/* 755 */         localObject2 = new Object[0];
/*     */ 
/* 762 */         boolean bool = ((Boolean)localObject1[j].invoke(this.moduleStack[i].module, (Object[])localObject2)).booleanValue();
/*     */ 
/* 765 */         if (bool == true)
/*     */         {
/* 768 */           if ((!paramString.equals("abort")) && (!paramString.equals("logout")) && (this.moduleStack[i].entry.getControlFlag() == AppConfigurationEntry.LoginModuleControlFlag.SUFFICIENT) && (this.firstRequiredError == null))
/*     */           {
/* 775 */             clearState();
/*     */ 
/* 777 */             if (debug != null)
/* 778 */               debug.println(paramString + " SUFFICIENT success");
/* 779 */             return;
/*     */           }
/*     */ 
/* 782 */           if (debug != null)
/* 783 */             debug.println(paramString + " success");
/* 784 */           this.success = true;
/*     */         }
/* 786 */         else if (debug != null) {
/* 787 */           debug.println(paramString + " ignored");
/*     */         }
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {
/* 791 */         localObject1 = new MessageFormat(ResourcesMgr.getString("unable.to.instantiate.LoginModule.module.because.it.does.not.provide.a.no.argument.constructor"));
/*     */ 
/* 793 */         localObject2 = new Object[] { this.moduleStack[i].entry.getLoginModuleName() };
/* 794 */         throwException(null, new LoginException(((MessageFormat)localObject1).format(localObject2)));
/*     */       } catch (InstantiationException localInstantiationException) {
/* 796 */         throwException(null, new LoginException(ResourcesMgr.getString("unable.to.instantiate.LoginModule.") + localInstantiationException.getMessage()));
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException)
/*     */       {
/* 800 */         throwException(null, new LoginException(ResourcesMgr.getString("unable.to.find.LoginModule.class.") + localClassNotFoundException.getMessage()));
/*     */       }
/*     */       catch (IllegalAccessException localIllegalAccessException)
/*     */       {
/* 804 */         throwException(null, new LoginException(ResourcesMgr.getString("unable.to.access.LoginModule.") + localIllegalAccessException.getMessage()));
/*     */       }
/*     */       catch (InvocationTargetException localInvocationTargetException)
/*     */       {
/*     */         Object localObject1;
/*     */         Object localObject2;
/* 813 */         if (((localInvocationTargetException.getCause() instanceof PendingException)) && (paramString.equals("login")))
/*     */         {
/* 837 */           throw ((PendingException)localInvocationTargetException.getCause());
/*     */         }
/* 839 */         if ((localInvocationTargetException.getCause() instanceof LoginException))
/*     */         {
/* 841 */           localObject1 = (LoginException)localInvocationTargetException.getCause();
/*     */         }
/* 843 */         else if ((localInvocationTargetException.getCause() instanceof SecurityException))
/*     */         {
/* 848 */           localObject1 = new LoginException("Security Exception");
/* 849 */           ((LoginException)localObject1).initCause(new SecurityException());
/* 850 */           if (debug != null) {
/* 851 */             debug.println("original security exception with detail msg replaced by new exception with empty detail msg");
/*     */ 
/* 854 */             debug.println("original security exception: " + localInvocationTargetException.getCause().toString());
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 860 */           localObject2 = new StringWriter();
/* 861 */           localInvocationTargetException.getCause().printStackTrace(new PrintWriter((Writer)localObject2));
/*     */ 
/* 863 */           ((StringWriter)localObject2).flush();
/* 864 */           localObject1 = new LoginException(((StringWriter)localObject2).toString());
/*     */         }
/*     */ 
/* 867 */         if (this.moduleStack[i].entry.getControlFlag() == AppConfigurationEntry.LoginModuleControlFlag.REQUISITE)
/*     */         {
/* 870 */           if (debug != null) {
/* 871 */             debug.println(paramString + " REQUISITE failure");
/*     */           }
/*     */ 
/* 874 */           if ((paramString.equals("abort")) || (paramString.equals("logout")))
/*     */           {
/* 876 */             if (this.firstRequiredError == null)
/* 877 */               this.firstRequiredError = ((LoginException)localObject1);
/*     */           }
/* 879 */           else throwException(this.firstRequiredError, (LoginException)localObject1);
/*     */ 
/*     */         }
/* 882 */         else if (this.moduleStack[i].entry.getControlFlag() == AppConfigurationEntry.LoginModuleControlFlag.REQUIRED)
/*     */         {
/* 885 */           if (debug != null) {
/* 886 */             debug.println(paramString + " REQUIRED failure");
/*     */           }
/*     */ 
/* 889 */           if (this.firstRequiredError == null)
/* 890 */             this.firstRequiredError = ((LoginException)localObject1);
/*     */         }
/*     */         else
/*     */         {
/* 894 */           if (debug != null) {
/* 895 */             debug.println(paramString + " OPTIONAL failure");
/*     */           }
/*     */ 
/* 898 */           if (this.firstError == null)
/* 899 */             this.firstError = ((LoginException)localObject1);
/*     */         }
/*     */       }
/* 704 */       i++;
/*     */     }
/*     */ 
/* 905 */     if (this.firstRequiredError != null)
/*     */     {
/* 907 */       throwException(this.firstRequiredError, null);
/* 908 */     } else if ((!this.success) && (this.firstError != null))
/*     */     {
/* 910 */       throwException(this.firstError, null);
/* 911 */     } else if (!this.success)
/*     */     {
/* 913 */       throwException(new LoginException(ResourcesMgr.getString("Login.Failure.all.modules.ignored")), null);
/*     */     }
/*     */     else
/*     */     {
/* 919 */       clearState();
/* 920 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ModuleInfo
/*     */   {
/*     */     AppConfigurationEntry entry;
/*     */     Object module;
/*     */ 
/*     */     ModuleInfo(AppConfigurationEntry paramAppConfigurationEntry, Object paramObject)
/*     */     {
/* 970 */       this.entry = paramAppConfigurationEntry;
/* 971 */       this.module = paramObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SecureCallbackHandler
/*     */     implements CallbackHandler
/*     */   {
/*     */     private final AccessControlContext acc;
/*     */     private final CallbackHandler ch;
/*     */ 
/*     */     SecureCallbackHandler(AccessControlContext paramAccessControlContext, CallbackHandler paramCallbackHandler)
/*     */     {
/* 936 */       this.acc = paramAccessControlContext;
/* 937 */       this.ch = paramCallbackHandler;
/*     */     }
/*     */ 
/*     */     public void handle(final Callback[] paramArrayOfCallback) throws IOException, UnsupportedCallbackException
/*     */     {
/*     */       try {
/* 943 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Void run() throws IOException, UnsupportedCallbackException
/*     */           {
/* 947 */             LoginContext.SecureCallbackHandler.this.ch.handle(paramArrayOfCallback);
/* 948 */             return null;
/*     */           }
/*     */         }
/*     */         , this.acc);
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException)
/*     */       {
/* 952 */         if ((localPrivilegedActionException.getException() instanceof IOException)) {
/* 953 */           throw ((IOException)localPrivilegedActionException.getException());
/*     */         }
/* 955 */         throw ((UnsupportedCallbackException)localPrivilegedActionException.getException());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.login.LoginContext
 * JD-Core Version:    0.6.2
 */