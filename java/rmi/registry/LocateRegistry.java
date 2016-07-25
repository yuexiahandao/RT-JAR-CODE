/*     */ package java.rmi.registry;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import sun.rmi.registry.RegistryImpl;
/*     */ import sun.rmi.server.UnicastRef;
/*     */ import sun.rmi.server.UnicastRef2;
/*     */ import sun.rmi.server.Util;
/*     */ import sun.rmi.transport.LiveRef;
/*     */ import sun.rmi.transport.tcp.TCPEndpoint;
/*     */ 
/*     */ public final class LocateRegistry
/*     */ {
/*     */   public static Registry getRegistry()
/*     */     throws RemoteException
/*     */   {
/*  75 */     return getRegistry(null, 1099);
/*     */   }
/*     */ 
/*     */   public static Registry getRegistry(int paramInt)
/*     */     throws RemoteException
/*     */   {
/*  90 */     return getRegistry(null, paramInt);
/*     */   }
/*     */ 
/*     */   public static Registry getRegistry(String paramString)
/*     */     throws RemoteException
/*     */   {
/* 106 */     return getRegistry(paramString, 1099);
/*     */   }
/*     */ 
/*     */   public static Registry getRegistry(String paramString, int paramInt)
/*     */     throws RemoteException
/*     */   {
/* 123 */     return getRegistry(paramString, paramInt, null);
/*     */   }
/*     */ 
/*     */   public static Registry getRegistry(String paramString, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory)
/*     */     throws RemoteException
/*     */   {
/* 148 */     Object localObject = null;
/*     */ 
/* 150 */     if (paramInt <= 0) {
/* 151 */       paramInt = 1099;
/*     */     }
/* 153 */     if ((paramString == null) || (paramString.length() == 0))
/*     */     {
/*     */       try
/*     */       {
/* 158 */         paramString = InetAddress.getLocalHost().getHostAddress();
/*     */       }
/*     */       catch (Exception localException) {
/* 161 */         paramString = "";
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 175 */     LiveRef localLiveRef = new LiveRef(new ObjID(0), new TCPEndpoint(paramString, paramInt, paramRMIClientSocketFactory, null), false);
/*     */ 
/* 179 */     UnicastRef2 localUnicastRef2 = paramRMIClientSocketFactory == null ? new UnicastRef(localLiveRef) : new UnicastRef2(localLiveRef);
/*     */ 
/* 182 */     return (Registry)Util.createProxy(RegistryImpl.class, localUnicastRef2, false);
/*     */   }
/*     */ 
/*     */   public static Registry createRegistry(int paramInt)
/*     */     throws RemoteException
/*     */   {
/* 203 */     return new RegistryImpl(paramInt);
/*     */   }
/*     */ 
/*     */   public static Registry createRegistry(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */     throws RemoteException
/*     */   {
/* 239 */     return new RegistryImpl(paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.registry.LocateRegistry
 * JD-Core Version:    0.6.2
 */