/*     */ package sun.security.util;
/*     */ 
/*     */ import java.net.NetPermission;
/*     */ import java.net.SocketPermission;
/*     */ import java.security.AccessController;
/*     */ import java.security.AllPermission;
/*     */ import java.security.BasicPermission;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.SecurityPermission;
/*     */ 
/*     */ public final class SecurityConstants
/*     */ {
/*     */   public static final String FILE_DELETE_ACTION = "delete";
/*     */   public static final String FILE_EXECUTE_ACTION = "execute";
/*     */   public static final String FILE_READ_ACTION = "read";
/*     */   public static final String FILE_WRITE_ACTION = "write";
/*     */   public static final String FILE_READLINK_ACTION = "readlink";
/*     */   public static final String SOCKET_RESOLVE_ACTION = "resolve";
/*     */   public static final String SOCKET_CONNECT_ACTION = "connect";
/*     */   public static final String SOCKET_LISTEN_ACTION = "listen";
/*     */   public static final String SOCKET_ACCEPT_ACTION = "accept";
/*     */   public static final String SOCKET_CONNECT_ACCEPT_ACTION = "connect,accept";
/*     */   public static final String PROPERTY_RW_ACTION = "read,write";
/*     */   public static final String PROPERTY_READ_ACTION = "read";
/*     */   public static final String PROPERTY_WRITE_ACTION = "write";
/*  71 */   public static final AllPermission ALL_PERMISSION = new AllPermission();
/*     */ 
/* 187 */   public static final NetPermission SPECIFY_HANDLER_PERMISSION = new NetPermission("specifyStreamHandler");
/*     */ 
/* 191 */   public static final NetPermission SET_PROXYSELECTOR_PERMISSION = new NetPermission("setProxySelector");
/*     */ 
/* 195 */   public static final NetPermission GET_PROXYSELECTOR_PERMISSION = new NetPermission("getProxySelector");
/*     */ 
/* 199 */   public static final NetPermission SET_COOKIEHANDLER_PERMISSION = new NetPermission("setCookieHandler");
/*     */ 
/* 203 */   public static final NetPermission GET_COOKIEHANDLER_PERMISSION = new NetPermission("getCookieHandler");
/*     */ 
/* 207 */   public static final NetPermission SET_RESPONSECACHE_PERMISSION = new NetPermission("setResponseCache");
/*     */ 
/* 211 */   public static final NetPermission GET_RESPONSECACHE_PERMISSION = new NetPermission("getResponseCache");
/*     */ 
/* 215 */   public static final RuntimePermission CREATE_CLASSLOADER_PERMISSION = new RuntimePermission("createClassLoader");
/*     */ 
/* 219 */   public static final RuntimePermission CHECK_MEMBER_ACCESS_PERMISSION = new RuntimePermission("accessDeclaredMembers");
/*     */ 
/* 223 */   public static final RuntimePermission MODIFY_THREAD_PERMISSION = new RuntimePermission("modifyThread");
/*     */ 
/* 227 */   public static final RuntimePermission MODIFY_THREADGROUP_PERMISSION = new RuntimePermission("modifyThreadGroup");
/*     */ 
/* 231 */   public static final RuntimePermission GET_PD_PERMISSION = new RuntimePermission("getProtectionDomain");
/*     */ 
/* 235 */   public static final RuntimePermission GET_CLASSLOADER_PERMISSION = new RuntimePermission("getClassLoader");
/*     */ 
/* 239 */   public static final RuntimePermission STOP_THREAD_PERMISSION = new RuntimePermission("stopThread");
/*     */ 
/* 243 */   public static final RuntimePermission GET_STACK_TRACE_PERMISSION = new RuntimePermission("getStackTrace");
/*     */ 
/* 247 */   public static final SecurityPermission CREATE_ACC_PERMISSION = new SecurityPermission("createAccessControlContext");
/*     */ 
/* 251 */   public static final SecurityPermission GET_COMBINER_PERMISSION = new SecurityPermission("getDomainCombiner");
/*     */ 
/* 255 */   public static final SecurityPermission GET_POLICY_PERMISSION = new SecurityPermission("getPolicy");
/*     */ 
/* 259 */   public static final SocketPermission LOCAL_LISTEN_PERMISSION = new SocketPermission("localhost:0", "listen");
/*     */ 
/*     */   public static class AWT
/*     */   {
/*     */     private static final String AWTFactory = "sun.awt.AWTPermissionFactory";
/* 113 */     private static final PermissionFactory<?> factory = permissionFactory();
/*     */ 
/* 146 */     public static final Permission TOPLEVEL_WINDOW_PERMISSION = newAWTPermission("showWindowWithoutWarningBanner");
/*     */ 
/* 150 */     public static final Permission ACCESS_CLIPBOARD_PERMISSION = newAWTPermission("accessClipboard");
/*     */ 
/* 154 */     public static final Permission CHECK_AWT_EVENTQUEUE_PERMISSION = newAWTPermission("accessEventQueue");
/*     */ 
/* 158 */     public static final Permission TOOLKIT_MODALITY_PERMISSION = newAWTPermission("toolkitModality");
/*     */ 
/* 162 */     public static final Permission READ_DISPLAY_PIXELS_PERMISSION = newAWTPermission("readDisplayPixels");
/*     */ 
/* 166 */     public static final Permission CREATE_ROBOT_PERMISSION = newAWTPermission("createRobot");
/*     */ 
/* 170 */     public static final Permission WATCH_MOUSE_PERMISSION = newAWTPermission("watchMousePointer");
/*     */ 
/* 174 */     public static final Permission SET_WINDOW_ALWAYS_ON_TOP_PERMISSION = newAWTPermission("setWindowAlwaysOnTop");
/*     */ 
/* 178 */     public static final Permission ALL_AWT_EVENTS_PERMISSION = newAWTPermission("listenToAllAWTEvents");
/*     */ 
/* 182 */     public static final Permission ACCESS_SYSTEM_TRAY_PERMISSION = newAWTPermission("accessSystemTray");
/*     */ 
/*     */     private static PermissionFactory<?> permissionFactory()
/*     */     {
/* 116 */       Class localClass = (Class)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Class<?> run() {
/*     */           try {
/* 120 */             return Class.forName("sun.awt.AWTPermissionFactory", true, null);
/*     */           } catch (ClassNotFoundException localClassNotFoundException) {
/*     */           }
/* 123 */           return null;
/*     */         }
/*     */       });
/* 126 */       if (localClass != null) {
/*     */         try
/*     */         {
/* 129 */           return (PermissionFactory)localClass.newInstance();
/*     */         } catch (InstantiationException localInstantiationException) {
/* 131 */           throw new InternalError(localInstantiationException.getMessage());
/*     */         } catch (IllegalAccessException localIllegalAccessException) {
/* 133 */           throw new InternalError(localIllegalAccessException.getMessage());
/*     */         }
/*     */       }
/*     */ 
/* 137 */       return new SecurityConstants.FakeAWTPermissionFactory(null);
/*     */     }
/*     */ 
/*     */     private static Permission newAWTPermission(String paramString)
/*     */     {
/* 142 */       return factory.newPermission(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class FakeAWTPermission extends BasicPermission
/*     */   {
/*     */     private static final long serialVersionUID = -1L;
/*     */ 
/*     */     public FakeAWTPermission(String paramString)
/*     */     {
/*  79 */       super();
/*     */     }
/*     */     public String toString() {
/*  82 */       return "(\"java.awt.AWTPermission\" \"" + getName() + "\")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class FakeAWTPermissionFactory
/*     */     implements PermissionFactory<SecurityConstants.FakeAWTPermission>
/*     */   {
/*     */     public SecurityConstants.FakeAWTPermission newPermission(String paramString)
/*     */     {
/*  94 */       return new SecurityConstants.FakeAWTPermission(paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.SecurityConstants
 * JD-Core Version:    0.6.2
 */