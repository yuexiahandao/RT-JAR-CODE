/*     */ package java.rmi.activation;
/*     */ 
/*     */ import java.rmi.MarshalledObject;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.rmi.server.RemoteServer;
/*     */ import sun.rmi.server.ActivatableRef;
/*     */ import sun.rmi.server.ActivatableServerRef;
/*     */ import sun.rmi.transport.ObjectTable;
/*     */ 
/*     */ public abstract class Activatable extends RemoteServer
/*     */ {
/*     */   private ActivationID id;
/*     */   private static final long serialVersionUID = -3120617863591563455L;
/*     */ 
/*     */   protected Activatable(String paramString, MarshalledObject<?> paramMarshalledObject, boolean paramBoolean, int paramInt)
/*     */     throws ActivationException, RemoteException
/*     */   {
/* 105 */     this.id = exportObject(this, paramString, paramMarshalledObject, paramBoolean, paramInt);
/*     */   }
/*     */ 
/*     */   protected Activatable(String paramString, MarshalledObject<?> paramMarshalledObject, boolean paramBoolean, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */     throws ActivationException, RemoteException
/*     */   {
/* 157 */     this.id = exportObject(this, paramString, paramMarshalledObject, paramBoolean, paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*     */   }
/*     */ 
/*     */   protected Activatable(ActivationID paramActivationID, int paramInt)
/*     */     throws RemoteException
/*     */   {
/* 184 */     this.id = paramActivationID;
/* 185 */     exportObject(this, paramActivationID, paramInt);
/*     */   }
/*     */ 
/*     */   protected Activatable(ActivationID paramActivationID, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */     throws RemoteException
/*     */   {
/* 217 */     this.id = paramActivationID;
/* 218 */     exportObject(this, paramActivationID, paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*     */   }
/*     */ 
/*     */   protected ActivationID getID()
/*     */   {
/* 229 */     return this.id;
/*     */   }
/*     */ 
/*     */   public static Remote register(ActivationDesc paramActivationDesc)
/*     */     throws UnknownGroupException, ActivationException, RemoteException
/*     */   {
/* 248 */     ActivationID localActivationID = ActivationGroup.getSystem().registerObject(paramActivationDesc);
/*     */ 
/* 250 */     return ActivatableRef.getStub(paramActivationDesc, localActivationID);
/*     */   }
/*     */ 
/*     */   public static boolean inactive(ActivationID paramActivationID)
/*     */     throws UnknownObjectException, ActivationException, RemoteException
/*     */   {
/* 281 */     return ActivationGroup.currentGroup().inactiveObject(paramActivationID);
/*     */   }
/*     */ 
/*     */   public static void unregister(ActivationID paramActivationID)
/*     */     throws UnknownObjectException, ActivationException, RemoteException
/*     */   {
/* 298 */     ActivationGroup.getSystem().unregisterObject(paramActivationID);
/*     */   }
/*     */ 
/*     */   public static ActivationID exportObject(Remote paramRemote, String paramString, MarshalledObject<?> paramMarshalledObject, boolean paramBoolean, int paramInt)
/*     */     throws ActivationException, RemoteException
/*     */   {
/* 346 */     return exportObject(paramRemote, paramString, paramMarshalledObject, paramBoolean, paramInt, null, null);
/*     */   }
/*     */ 
/*     */   public static ActivationID exportObject(Remote paramRemote, String paramString, MarshalledObject<?> paramMarshalledObject, boolean paramBoolean, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */     throws ActivationException, RemoteException
/*     */   {
/* 421 */     ActivationDesc localActivationDesc = new ActivationDesc(paramRemote.getClass().getName(), paramString, paramMarshalledObject, paramBoolean);
/*     */ 
/* 426 */     ActivationSystem localActivationSystem = ActivationGroup.getSystem();
/* 427 */     ActivationID localActivationID = localActivationSystem.registerObject(localActivationDesc);
/*     */     try
/*     */     {
/* 433 */       exportObject(paramRemote, localActivationID, paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*     */     }
/*     */     catch (RemoteException localRemoteException)
/*     */     {
/*     */       try
/*     */       {
/* 440 */         localActivationSystem.unregisterObject(localActivationID);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */ 
/* 446 */       throw localRemoteException;
/*     */     }
/*     */ 
/* 454 */     ActivationGroup.currentGroup().activeObject(localActivationID, paramRemote);
/*     */ 
/* 456 */     return localActivationID;
/*     */   }
/*     */ 
/*     */   public static Remote exportObject(Remote paramRemote, ActivationID paramActivationID, int paramInt)
/*     */     throws RemoteException
/*     */   {
/* 483 */     return exportObject(paramRemote, new ActivatableServerRef(paramActivationID, paramInt));
/*     */   }
/*     */ 
/*     */   public static Remote exportObject(Remote paramRemote, ActivationID paramActivationID, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */     throws RemoteException
/*     */   {
/* 515 */     return exportObject(paramRemote, new ActivatableServerRef(paramActivationID, paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory));
/*     */   }
/*     */ 
/*     */   public static boolean unexportObject(Remote paramRemote, boolean paramBoolean)
/*     */     throws NoSuchObjectException
/*     */   {
/* 539 */     return ObjectTable.unexportObject(paramRemote, paramBoolean);
/*     */   }
/*     */ 
/*     */   private static Remote exportObject(Remote paramRemote, ActivatableServerRef paramActivatableServerRef)
/*     */     throws RemoteException
/*     */   {
/* 549 */     if ((paramRemote instanceof Activatable)) {
/* 550 */       ((Activatable)paramRemote).ref = paramActivatableServerRef;
/*     */     }
/*     */ 
/* 553 */     return paramActivatableServerRef.exportObject(paramRemote, null, false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.Activatable
 * JD-Core Version:    0.6.2
 */