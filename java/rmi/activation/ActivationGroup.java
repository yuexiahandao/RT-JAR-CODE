/*     */ package java.rmi.activation;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.rmi.MarshalledObject;
/*     */ import java.rmi.Naming;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import java.security.AccessController;
/*     */ import sun.rmi.server.ActivationGroupImpl;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ 
/*     */ public abstract class ActivationGroup extends UnicastRemoteObject
/*     */   implements ActivationInstantiator
/*     */ {
/*     */   private ActivationGroupID groupID;
/*     */   private ActivationMonitor monitor;
/*     */   private long incarnation;
/*     */   private static ActivationGroup currGroup;
/*     */   private static ActivationGroupID currGroupID;
/*     */   private static ActivationSystem currSystem;
/* 124 */   private static boolean canCreate = true;
/*     */   private static final long serialVersionUID = -7696947875314805420L;
/*     */ 
/*     */   protected ActivationGroup(ActivationGroupID paramActivationGroupID)
/*     */     throws RemoteException
/*     */   {
/* 143 */     this.groupID = paramActivationGroupID;
/*     */   }
/*     */ 
/*     */   public boolean inactiveObject(ActivationID paramActivationID)
/*     */     throws ActivationException, UnknownObjectException, RemoteException
/*     */   {
/* 187 */     getMonitor().inactiveObject(paramActivationID);
/* 188 */     return true;
/*     */   }
/*     */ 
/*     */   public abstract void activeObject(ActivationID paramActivationID, Remote paramRemote)
/*     */     throws ActivationException, UnknownObjectException, RemoteException;
/*     */ 
/*     */   public static synchronized ActivationGroup createGroup(ActivationGroupID paramActivationGroupID, ActivationGroupDesc paramActivationGroupDesc, long paramLong)
/*     */     throws ActivationException
/*     */   {
/* 279 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 280 */     if (localSecurityManager != null) {
/* 281 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 283 */     if (currGroup != null) {
/* 284 */       throw new ActivationException("group already exists");
/*     */     }
/* 286 */     if (!canCreate) {
/* 287 */       throw new ActivationException("group deactivated and cannot be recreated");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 292 */       String str = paramActivationGroupDesc.getClassName();
/*     */ 
/* 294 */       ActivationGroupImpl localActivationGroupImpl = ActivationGroupImpl.class;
/*     */       Object localObject1;
/* 296 */       if ((str == null) || (str.equals(localActivationGroupImpl.getName())))
/*     */       {
/* 299 */         localObject1 = localActivationGroupImpl;
/*     */       }
/*     */       else {
/*     */         try {
/* 303 */           localObject2 = RMIClassLoader.loadClass(paramActivationGroupDesc.getLocation(), str);
/*     */         }
/*     */         catch (Exception localException2) {
/* 306 */           throw new ActivationException("Could not load group implementation class", localException2);
/*     */         }
/*     */ 
/* 309 */         if (ActivationGroup.class.isAssignableFrom((Class)localObject2))
/* 310 */           localObject1 = ((Class)localObject2).asSubclass(ActivationGroup.class);
/*     */         else {
/* 312 */           throw new ActivationException("group not correct class: " + ((Class)localObject2).getName());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 318 */       Object localObject2 = ((Class)localObject1).getConstructor(new Class[] { ActivationGroupID.class, MarshalledObject.class });
/*     */ 
/* 321 */       ActivationGroup localActivationGroup = (ActivationGroup)((Constructor)localObject2).newInstance(new Object[] { paramActivationGroupID, paramActivationGroupDesc.getData() });
/*     */ 
/* 323 */       currSystem = paramActivationGroupID.getSystem();
/* 324 */       localActivationGroup.incarnation = paramLong;
/* 325 */       localActivationGroup.monitor = currSystem.activeGroup(paramActivationGroupID, localActivationGroup, paramLong);
/*     */ 
/* 327 */       currGroup = localActivationGroup;
/* 328 */       currGroupID = paramActivationGroupID;
/* 329 */       canCreate = false;
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 331 */       localInvocationTargetException.getTargetException().printStackTrace();
/* 332 */       throw new ActivationException("exception in group constructor", localInvocationTargetException.getTargetException());
/*     */     }
/*     */     catch (ActivationException localActivationException)
/*     */     {
/* 336 */       throw localActivationException;
/*     */     }
/*     */     catch (Exception localException1) {
/* 339 */       throw new ActivationException("exception creating group", localException1);
/*     */     }
/*     */ 
/* 342 */     return currGroup;
/*     */   }
/*     */ 
/*     */   public static synchronized ActivationGroupID currentGroupID()
/*     */   {
/* 352 */     return currGroupID;
/*     */   }
/*     */ 
/*     */   static synchronized ActivationGroupID internalCurrentGroupID()
/*     */     throws ActivationException
/*     */   {
/* 369 */     if (currGroupID == null) {
/* 370 */       throw new ActivationException("nonexistent group");
/*     */     }
/* 372 */     return currGroupID;
/*     */   }
/*     */ 
/*     */   public static synchronized void setSystem(ActivationSystem paramActivationSystem)
/*     */     throws ActivationException
/*     */   {
/* 404 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 405 */     if (localSecurityManager != null) {
/* 406 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 408 */     if (currSystem != null) {
/* 409 */       throw new ActivationException("activation system already set");
/*     */     }
/* 411 */     currSystem = paramActivationSystem;
/*     */   }
/*     */ 
/*     */   public static synchronized ActivationSystem getSystem()
/*     */     throws ActivationException
/*     */   {
/* 437 */     if (currSystem == null) {
/*     */       try {
/* 439 */         int i = ((Integer)AccessController.doPrivileged(new GetIntegerAction("java.rmi.activation.port", 1098))).intValue();
/*     */ 
/* 442 */         currSystem = (ActivationSystem)Naming.lookup("//:" + i + "/java.rmi.activation.ActivationSystem");
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 446 */         throw new ActivationException("unable to obtain ActivationSystem", localException);
/*     */       }
/*     */     }
/*     */ 
/* 450 */     return currSystem;
/*     */   }
/*     */ 
/*     */   protected void activeObject(ActivationID paramActivationID, MarshalledObject<? extends Remote> paramMarshalledObject)
/*     */     throws ActivationException, UnknownObjectException, RemoteException
/*     */   {
/* 470 */     getMonitor().activeObject(paramActivationID, paramMarshalledObject);
/*     */   }
/*     */ 
/*     */   protected void inactiveGroup()
/*     */     throws UnknownGroupException, RemoteException
/*     */   {
/*     */     try
/*     */     {
/* 488 */       getMonitor().inactiveGroup(this.groupID, this.incarnation);
/*     */     } finally {
/* 490 */       destroyGroup();
/*     */     }
/*     */   }
/*     */ 
/*     */   private ActivationMonitor getMonitor()
/*     */     throws RemoteException
/*     */   {
/* 498 */     synchronized (ActivationGroup.class) {
/* 499 */       if (this.monitor != null) {
/* 500 */         return this.monitor;
/*     */       }
/*     */     }
/* 503 */     throw new RemoteException("monitor not received");
/*     */   }
/*     */ 
/*     */   private static synchronized void destroyGroup()
/*     */   {
/* 510 */     currGroup = null;
/* 511 */     currGroupID = null;
/*     */   }
/*     */ 
/*     */   static synchronized ActivationGroup currentGroup()
/*     */     throws ActivationException
/*     */   {
/* 522 */     if (currGroup == null) {
/* 523 */       throw new ActivationException("group is not active");
/*     */     }
/* 525 */     return currGroup;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.ActivationGroup
 * JD-Core Version:    0.6.2
 */