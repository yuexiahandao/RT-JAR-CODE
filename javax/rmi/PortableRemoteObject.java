/*     */ package javax.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.GetPropertyAction;
/*     */ import java.net.MalformedURLException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import java.security.AccessController;
/*     */ import java.util.Properties;
/*     */ import javax.rmi.CORBA.PortableRemoteObjectDelegate;
/*     */ import org.omg.CORBA.INITIALIZE;
/*     */ 
/*     */ public class PortableRemoteObject
/*     */ {
/*  74 */   private static final PortableRemoteObjectDelegate proDelegate = (PortableRemoteObjectDelegate)createDelegate("javax.rmi.CORBA.PortableRemoteObjectClass");
/*     */   private static final String PortableRemoteObjectClassKey = "javax.rmi.CORBA.PortableRemoteObjectClass";
/*     */ 
/*     */   protected PortableRemoteObject()
/*     */     throws RemoteException
/*     */   {
/*  83 */     if (proDelegate != null)
/*  84 */       exportObject((Remote)this);
/*     */   }
/*     */ 
/*     */   public static void exportObject(Remote paramRemote)
/*     */     throws RemoteException
/*     */   {
/*  99 */     if (proDelegate != null)
/* 100 */       proDelegate.exportObject(paramRemote);
/*     */   }
/*     */ 
/*     */   public static Remote toStub(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/* 115 */     if (proDelegate != null) {
/* 116 */       return proDelegate.toStub(paramRemote);
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   public static void unexportObject(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/* 131 */     if (proDelegate != null)
/* 132 */       proDelegate.unexportObject(paramRemote);
/*     */   }
/*     */ 
/*     */   public static Object narrow(Object paramObject, Class paramClass)
/*     */     throws ClassCastException
/*     */   {
/* 149 */     if (proDelegate != null) {
/* 150 */       return proDelegate.narrow(paramObject, paramClass);
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   public static void connect(Remote paramRemote1, Remote paramRemote2)
/*     */     throws RemoteException
/*     */   {
/* 171 */     if (proDelegate != null)
/* 172 */       proDelegate.connect(paramRemote1, paramRemote2);
/*     */   }
/*     */ 
/*     */   private static Object createDelegate(String paramString)
/*     */   {
/* 182 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction(paramString));
/*     */ 
/* 184 */     if (str == null) {
/* 185 */       Properties localProperties = getORBPropertiesFile();
/* 186 */       if (localProperties != null) {
/* 187 */         str = localProperties.getProperty(paramString);
/*     */       }
/*     */     }
/* 190 */     if (str == null)
/* 191 */       return new com.sun.corba.se.impl.javax.rmi.PortableRemoteObject();
/*     */     INITIALIZE localINITIALIZE;
/*     */     try
/*     */     {
/* 195 */       return loadDelegateClass(str).newInstance();
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 197 */       localINITIALIZE = new INITIALIZE("Cannot instantiate " + str);
/* 198 */       localINITIALIZE.initCause(localClassNotFoundException);
/* 199 */       throw localINITIALIZE;
/*     */     } catch (Exception localException) {
/* 201 */       localINITIALIZE = new INITIALIZE("Error while instantiating" + str);
/* 202 */       localINITIALIZE.initCause(localException);
/* 203 */     }throw localINITIALIZE;
/*     */   }
/*     */ 
/*     */   private static Class loadDelegateClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 211 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 212 */       return Class.forName(paramString, false, localClassLoader);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1)
/*     */     {
/*     */       try
/*     */       {
/* 218 */         return RMIClassLoader.loadClass(paramString);
/*     */       } catch (MalformedURLException localMalformedURLException) {
/* 220 */         String str = "Could not load " + paramString + ": " + localMalformedURLException.toString();
/* 221 */         ClassNotFoundException localClassNotFoundException2 = new ClassNotFoundException(str);
/* 222 */         throw localClassNotFoundException2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Properties getORBPropertiesFile()
/*     */   {
/* 230 */     return (Properties)AccessController.doPrivileged(new GetORBPropertiesFileAction());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.PortableRemoteObject
 * JD-Core Version:    0.6.2
 */