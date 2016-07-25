/*     */ package sun.rmi.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import java.security.AccessControlException;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import sun.misc.VM;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class MarshalInputStream extends ObjectInputStream
/*     */ {
/*  66 */   private static final boolean useCodebaseOnlyProperty = !((String)AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.useCodebaseOnly", "true"))).equalsIgnoreCase("false");
/*     */ 
/*  73 */   protected static Map<String, Class<?>> permittedSunClasses = new HashMap(3);
/*     */ 
/*  77 */   private boolean skipDefaultResolveClass = false;
/*     */ 
/*  80 */   private final Map<Object, Runnable> doneCallbacks = new HashMap(3);
/*     */ 
/*  87 */   private boolean useCodebaseOnly = useCodebaseOnlyProperty;
/*     */ 
/*     */   public MarshalInputStream(InputStream paramInputStream)
/*     */     throws IOException, StreamCorruptedException
/*     */   {
/* 124 */     super(paramInputStream);
/*     */   }
/*     */ 
/*     */   public Runnable getDoneCallback(Object paramObject)
/*     */   {
/* 133 */     return (Runnable)this.doneCallbacks.get(paramObject);
/*     */   }
/*     */ 
/*     */   public void setDoneCallback(Object paramObject, Runnable paramRunnable)
/*     */   {
/* 143 */     this.doneCallbacks.put(paramObject, paramRunnable);
/*     */   }
/*     */ 
/*     */   public void done()
/*     */   {
/* 156 */     Iterator localIterator = this.doneCallbacks.values().iterator();
/* 157 */     while (localIterator.hasNext()) {
/* 158 */       Runnable localRunnable = (Runnable)localIterator.next();
/* 159 */       localRunnable.run();
/*     */     }
/* 161 */     this.doneCallbacks.clear();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 168 */     done();
/* 169 */     super.close();
/*     */   }
/*     */ 
/*     */   protected Class<?> resolveClass(ObjectStreamClass paramObjectStreamClass)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 184 */     Object localObject = readLocation();
/*     */ 
/* 186 */     String str1 = paramObjectStreamClass.getName();
/*     */ 
/* 198 */     ClassLoader localClassLoader = this.skipDefaultResolveClass ? null : latestUserDefinedLoader();
/*     */ 
/* 208 */     String str2 = null;
/* 209 */     if ((!this.useCodebaseOnly) && ((localObject instanceof String))) {
/* 210 */       str2 = (String)localObject;
/*     */     }
/*     */     try
/*     */     {
/* 214 */       return RMIClassLoader.loadClass(str2, str1, localClassLoader);
/*     */     }
/*     */     catch (AccessControlException localAccessControlException) {
/* 217 */       return checkSunClass(str1, localAccessControlException);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1)
/*     */     {
/*     */       try
/*     */       {
/* 224 */         if ((Character.isLowerCase(str1.charAt(0))) && (str1.indexOf('.') == -1))
/*     */         {
/* 227 */           return super.resolveClass(paramObjectStreamClass);
/*     */         }
/*     */       } catch (ClassNotFoundException localClassNotFoundException2) {
/*     */       }
/* 231 */       throw localClassNotFoundException1;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Class<?> resolveProxyClass(String[] paramArrayOfString)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 245 */     Object localObject = readLocation();
/*     */ 
/* 247 */     ClassLoader localClassLoader = this.skipDefaultResolveClass ? null : latestUserDefinedLoader();
/*     */ 
/* 250 */     String str = null;
/* 251 */     if ((!this.useCodebaseOnly) && ((localObject instanceof String))) {
/* 252 */       str = (String)localObject;
/*     */     }
/*     */ 
/* 255 */     return RMIClassLoader.loadProxyClass(str, paramArrayOfString, localClassLoader);
/*     */   }
/*     */ 
/*     */   private static ClassLoader latestUserDefinedLoader()
/*     */   {
/* 264 */     return VM.latestUserDefinedLoader();
/*     */   }
/*     */ 
/*     */   private Class<?> checkSunClass(String paramString, AccessControlException paramAccessControlException)
/*     */     throws AccessControlException
/*     */   {
/* 275 */     Permission localPermission = paramAccessControlException.getPermission();
/* 276 */     String str = null;
/* 277 */     if (localPermission != null) {
/* 278 */       str = localPermission.getName();
/*     */     }
/*     */ 
/* 281 */     Class localClass = (Class)permittedSunClasses.get(paramString);
/*     */ 
/* 284 */     if ((str == null) || (localClass == null) || ((!str.equals("accessClassInPackage.sun.rmi.server")) && (!str.equals("accessClassInPackage.sun.rmi.registry"))))
/*     */     {
/* 289 */       throw paramAccessControlException;
/*     */     }
/*     */ 
/* 292 */     return localClass;
/*     */   }
/*     */ 
/*     */   protected Object readLocation()
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 303 */     return readObject();
/*     */   }
/*     */ 
/*     */   void skipDefaultResolveClass()
/*     */   {
/* 311 */     this.skipDefaultResolveClass = true;
/*     */   }
/*     */ 
/*     */   void useCodebaseOnly()
/*     */   {
/* 319 */     this.useCodebaseOnly = true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 105 */       String str1 = "sun.rmi.server.Activation$ActivationSystemImpl_Stub";
/*     */ 
/* 107 */       String str2 = "sun.rmi.registry.RegistryImpl_Stub";
/*     */ 
/* 109 */       permittedSunClasses.put(str1, Class.forName(str1));
/* 110 */       permittedSunClasses.put(str2, Class.forName(str2));
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 113 */       throw new NoClassDefFoundError("Missing system class: " + localClassNotFoundException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.MarshalInputStream
 * JD-Core Version:    0.6.2
 */