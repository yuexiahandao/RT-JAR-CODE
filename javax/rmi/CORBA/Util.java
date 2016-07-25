/*     */ package javax.rmi.CORBA;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.GetPropertyAction;
/*     */ import java.net.MalformedURLException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import java.security.AccessController;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.INITIALIZE;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public class Util
/*     */ {
/*  67 */   private static final UtilDelegate utilDelegate = (UtilDelegate)createDelegate("javax.rmi.CORBA.UtilClass");
/*     */   private static final String UtilClassKey = "javax.rmi.CORBA.UtilClass";
/*     */ 
/*     */   public static RemoteException mapSystemException(SystemException paramSystemException)
/*     */   {
/*  79 */     if (utilDelegate != null) {
/*  80 */       return utilDelegate.mapSystemException(paramSystemException);
/*     */     }
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */   public static void writeAny(OutputStream paramOutputStream, Object paramObject)
/*     */   {
/*  92 */     if (utilDelegate != null)
/*  93 */       utilDelegate.writeAny(paramOutputStream, paramObject);
/*     */   }
/*     */ 
/*     */   public static Object readAny(InputStream paramInputStream)
/*     */   {
/* 104 */     if (utilDelegate != null) {
/* 105 */       return utilDelegate.readAny(paramInputStream);
/*     */     }
/* 107 */     return null;
/*     */   }
/*     */ 
/*     */   public static void writeRemoteObject(OutputStream paramOutputStream, Object paramObject)
/*     */   {
/* 123 */     if (utilDelegate != null)
/* 124 */       utilDelegate.writeRemoteObject(paramOutputStream, paramObject);
/*     */   }
/*     */ 
/*     */   public static void writeAbstractObject(OutputStream paramOutputStream, Object paramObject)
/*     */   {
/* 143 */     if (utilDelegate != null)
/* 144 */       utilDelegate.writeAbstractObject(paramOutputStream, paramObject);
/*     */   }
/*     */ 
/*     */   public static void registerTarget(Tie paramTie, Remote paramRemote)
/*     */   {
/* 157 */     if (utilDelegate != null)
/* 158 */       utilDelegate.registerTarget(paramTie, paramRemote);
/*     */   }
/*     */ 
/*     */   public static void unexportObject(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/* 173 */     if (utilDelegate != null)
/* 174 */       utilDelegate.unexportObject(paramRemote);
/*     */   }
/*     */ 
/*     */   public static Tie getTie(Remote paramRemote)
/*     */   {
/* 185 */     if (utilDelegate != null) {
/* 186 */       return utilDelegate.getTie(paramRemote);
/*     */     }
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */   public static ValueHandler createValueHandler()
/*     */   {
/* 199 */     if (utilDelegate != null) {
/* 200 */       return utilDelegate.createValueHandler();
/*     */     }
/* 202 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getCodebase(Class paramClass)
/*     */   {
/* 211 */     if (utilDelegate != null) {
/* 212 */       return utilDelegate.getCodebase(paramClass);
/*     */     }
/* 214 */     return null;
/*     */   }
/*     */ 
/*     */   public static Class loadClass(String paramString1, String paramString2, ClassLoader paramClassLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/* 247 */     if (utilDelegate != null) {
/* 248 */       return utilDelegate.loadClass(paramString1, paramString2, paramClassLoader);
/*     */     }
/* 250 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean isLocal(Stub paramStub)
/*     */     throws RemoteException
/*     */   {
/* 276 */     if (utilDelegate != null) {
/* 277 */       return utilDelegate.isLocal(paramStub);
/*     */     }
/*     */ 
/* 280 */     return false;
/*     */   }
/*     */ 
/*     */   public static RemoteException wrapException(Throwable paramThrowable)
/*     */   {
/* 291 */     if (utilDelegate != null) {
/* 292 */       return utilDelegate.wrapException(paramThrowable);
/*     */     }
/*     */ 
/* 295 */     return null;
/*     */   }
/*     */ 
/*     */   public static Object[] copyObjects(Object[] paramArrayOfObject, ORB paramORB)
/*     */     throws RemoteException
/*     */   {
/* 310 */     if (utilDelegate != null) {
/* 311 */       return utilDelegate.copyObjects(paramArrayOfObject, paramORB);
/*     */     }
/*     */ 
/* 314 */     return null;
/*     */   }
/*     */ 
/*     */   public static Object copyObject(Object paramObject, ORB paramORB)
/*     */     throws RemoteException
/*     */   {
/* 328 */     if (utilDelegate != null) {
/* 329 */       return utilDelegate.copyObject(paramObject, paramORB);
/*     */     }
/* 331 */     return null;
/*     */   }
/*     */ 
/*     */   private static Object createDelegate(String paramString)
/*     */   {
/* 339 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction(paramString));
/*     */ 
/* 341 */     if (str == null) {
/* 342 */       Properties localProperties = getORBPropertiesFile();
/* 343 */       if (localProperties != null) {
/* 344 */         str = localProperties.getProperty(paramString);
/*     */       }
/*     */     }
/*     */ 
/* 348 */     if (str == null)
/* 349 */       return new com.sun.corba.se.impl.javax.rmi.CORBA.Util();
/*     */     INITIALIZE localINITIALIZE;
/*     */     try
/*     */     {
/* 353 */       return loadDelegateClass(str).newInstance();
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 355 */       localINITIALIZE = new INITIALIZE("Cannot instantiate " + str);
/* 356 */       localINITIALIZE.initCause(localClassNotFoundException);
/* 357 */       throw localINITIALIZE;
/*     */     } catch (Exception localException) {
/* 359 */       localINITIALIZE = new INITIALIZE("Error while instantiating" + str);
/* 360 */       localINITIALIZE.initCause(localException);
/* 361 */     }throw localINITIALIZE;
/*     */   }
/*     */ 
/*     */   private static Class loadDelegateClass(String paramString) throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 368 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 369 */       return Class.forName(paramString, false, localClassLoader);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1)
/*     */     {
/*     */       try
/*     */       {
/* 375 */         return RMIClassLoader.loadClass(paramString);
/*     */       } catch (MalformedURLException localMalformedURLException) {
/* 377 */         String str = "Could not load " + paramString + ": " + localMalformedURLException.toString();
/* 378 */         ClassNotFoundException localClassNotFoundException2 = new ClassNotFoundException(str);
/* 379 */         throw localClassNotFoundException2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Properties getORBPropertiesFile()
/*     */   {
/* 387 */     return (Properties)AccessController.doPrivileged(new GetORBPropertiesFileAction());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.CORBA.Util
 * JD-Core Version:    0.6.2
 */