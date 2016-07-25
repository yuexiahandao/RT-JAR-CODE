/*     */ package com.sun.jndi.toolkit.corba;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import javax.naming.ConfigurationException;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ public class CorbaUtils
/*     */ {
/* 240 */   private static Method toStubMethod = null;
/* 241 */   private static Method connectMethod = null;
/* 242 */   private static Class corbaStubClass = null;
/*     */ 
/*     */   public static org.omg.CORBA.Object remoteToCorba(Remote paramRemote, ORB paramORB)
/*     */     throws ClassNotFoundException, ConfigurationException
/*     */   {
/*  85 */     synchronized (CorbaUtils.class) {
/*  86 */       if (toStubMethod == null) {
/*  87 */         initMethodHandles();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     java.lang.Object localObject2;
/*     */     ConfigurationException localConfigurationException;
/*     */     try
/*     */     {
/*  98 */       ??? = toStubMethod.invoke(null, new java.lang.Object[] { paramRemote });
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException1) {
/* 101 */       localObject2 = localInvocationTargetException1.getTargetException();
/*     */ 
/* 104 */       localConfigurationException = new ConfigurationException("Problem with PortableRemoteObject.toStub(); object not exported or stub not found");
/*     */ 
/* 106 */       localConfigurationException.setRootCause((Throwable)localObject2);
/* 107 */       throw localConfigurationException;
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException1) {
/* 110 */       localObject2 = new ConfigurationException("Cannot invoke javax.rmi.PortableRemoteObject.toStub(java.rmi.Remote)");
/*     */ 
/* 113 */       ((ConfigurationException)localObject2).setRootCause(localIllegalAccessException1);
/* 114 */       throw ((Throwable)localObject2);
/*     */     }
/*     */ 
/* 119 */     if (!corbaStubClass.isInstance(???)) {
/* 120 */       return null;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 126 */       connectMethod.invoke(???, new java.lang.Object[] { paramORB });
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException2) {
/* 129 */       localObject2 = localInvocationTargetException2.getTargetException();
/*     */ 
/* 132 */       if (!(localObject2 instanceof RemoteException)) {
/* 133 */         localConfigurationException = new ConfigurationException("Problem invoking javax.rmi.CORBA.Stub.connect()");
/*     */ 
/* 135 */         localConfigurationException.setRootCause((Throwable)localObject2);
/* 136 */         throw localConfigurationException;
/*     */       }
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException2)
/*     */     {
/* 141 */       localObject2 = new ConfigurationException("Cannot invoke javax.rmi.CORBA.Stub.connect()");
/*     */ 
/* 143 */       ((ConfigurationException)localObject2).setRootCause(localIllegalAccessException2);
/* 144 */       throw ((Throwable)localObject2);
/*     */     }
/*     */ 
/* 147 */     return (org.omg.CORBA.Object)???;
/*     */   }
/*     */ 
/*     */   public static ORB getOrb(String paramString, int paramInt, Hashtable paramHashtable)
/*     */   {
/*     */     Properties localProperties;
/*     */     java.lang.Object localObject1;
/* 166 */     if (paramHashtable != null) {
/* 167 */       if ((paramHashtable instanceof Properties))
/*     */       {
/* 169 */         localProperties = (Properties)paramHashtable.clone();
/*     */       }
/*     */       else
/*     */       {
/* 173 */         localProperties = new Properties();
/* 174 */         for (localObject1 = paramHashtable.keys(); ((Enumeration)localObject1).hasMoreElements(); ) {
/* 175 */           String str = (String)((Enumeration)localObject1).nextElement();
/* 176 */           java.lang.Object localObject2 = paramHashtable.get(str);
/* 177 */           if ((localObject2 instanceof String))
/* 178 */             localProperties.put(str, localObject2);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 183 */       localProperties = new Properties();
/*     */     }
/*     */ 
/* 186 */     if (paramString != null) {
/* 187 */       localProperties.put("org.omg.CORBA.ORBInitialHost", paramString);
/*     */     }
/* 189 */     if (paramInt >= 0) {
/* 190 */       localProperties.put("org.omg.CORBA.ORBInitialPort", "" + paramInt);
/*     */     }
/*     */ 
/* 194 */     if (paramHashtable != null) {
/* 195 */       localObject1 = paramHashtable.get("java.naming.applet");
/* 196 */       if (localObject1 != null)
/*     */       {
/* 198 */         return initAppletORB(localObject1, localProperties);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 203 */     return ORB.init(new String[0], localProperties);
/*     */   }
/*     */ 
/*     */   private static ORB initAppletORB(java.lang.Object paramObject, Properties paramProperties)
/*     */   {
/*     */     try
/*     */     {
/* 212 */       Class localClass = Class.forName("java.applet.Applet", true, null);
/* 213 */       if (!localClass.isInstance(paramObject)) {
/* 214 */         throw new ClassCastException(paramObject.getClass().getName());
/*     */       }
/*     */ 
/* 218 */       localObject = ORB.class.getMethod("init", new Class[] { localClass, Properties.class });
/* 219 */       return (ORB)((Method)localObject).invoke(null, new java.lang.Object[] { paramObject, paramProperties });
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 223 */       throw new ClassCastException(paramObject.getClass().getName());
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 225 */       throw new AssertionError(localNoSuchMethodException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 227 */       java.lang.Object localObject = localInvocationTargetException.getCause();
/* 228 */       if ((localObject instanceof RuntimeException))
/* 229 */         throw ((RuntimeException)localObject);
/* 230 */       if ((localObject instanceof Error)) {
/* 231 */         throw ((Error)localObject);
/*     */       }
/* 233 */       throw new AssertionError(localInvocationTargetException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 235 */       throw new AssertionError(localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void initMethodHandles()
/*     */     throws ClassNotFoundException
/*     */   {
/* 249 */     corbaStubClass = Class.forName("javax.rmi.CORBA.Stub");
/*     */     try
/*     */     {
/* 254 */       connectMethod = corbaStubClass.getMethod("connect", new Class[] { ORB.class });
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException1) {
/* 257 */       throw new IllegalStateException("No method definition for javax.rmi.CORBA.Stub.connect(org.omg.CORBA.ORB)");
/*     */     }
/*     */ 
/* 262 */     Class localClass = Class.forName("javax.rmi.PortableRemoteObject");
/*     */     try
/*     */     {
/* 266 */       toStubMethod = localClass.getMethod("toStub", new Class[] { Remote.class });
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException2)
/*     */     {
/* 270 */       throw new IllegalStateException("No method definition for javax.rmi.PortableRemoteObject.toStub(java.rmi.Remote)");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.corba.CorbaUtils
 * JD-Core Version:    0.6.2
 */