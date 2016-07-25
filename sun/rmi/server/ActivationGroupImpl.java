/*     */ package sun.rmi.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.ServerSocket;
/*     */ import java.rmi.MarshalledObject;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.activation.Activatable;
/*     */ import java.rmi.activation.ActivationDesc;
/*     */ import java.rmi.activation.ActivationException;
/*     */ import java.rmi.activation.ActivationGroup;
/*     */ import java.rmi.activation.ActivationGroupID;
/*     */ import java.rmi.activation.ActivationID;
/*     */ import java.rmi.activation.UnknownObjectException;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.rmi.server.RMISocketFactory;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import sun.rmi.registry.RegistryImpl;
/*     */ 
/*     */ public class ActivationGroupImpl extends ActivationGroup
/*     */ {
/*     */   private static final long serialVersionUID = 5758693559430427303L;
/*  68 */   private final Hashtable<ActivationID, ActiveEntry> active = new Hashtable();
/*     */ 
/*  70 */   private boolean groupInactive = false;
/*     */   private final ActivationGroupID groupID;
/*  72 */   private final List<ActivationID> lockedIDs = new ArrayList();
/*     */ 
/*     */   public ActivationGroupImpl(ActivationGroupID paramActivationGroupID, MarshalledObject<?> paramMarshalledObject)
/*     */     throws RemoteException
/*     */   {
/*  83 */     super(paramActivationGroupID);
/*  84 */     this.groupID = paramActivationGroupID;
/*     */ 
/*  90 */     unexportObject(this, true);
/*  91 */     ServerSocketFactoryImpl localServerSocketFactoryImpl = new ServerSocketFactoryImpl(null);
/*  92 */     UnicastRemoteObject.exportObject(this, 0, null, localServerSocketFactoryImpl);
/*     */ 
/*  94 */     if (System.getSecurityManager() == null)
/*     */       try
/*     */       {
/*  97 */         System.setSecurityManager(new SecurityManager());
/*     */       }
/*     */       catch (Exception localException) {
/* 100 */         throw new RemoteException("unable to set security manager", localException);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void acquireLock(ActivationID paramActivationID)
/*     */   {
/*     */     while (true)
/*     */     {
/*     */       ActivationID localActivationID1;
/* 145 */       synchronized (this.lockedIDs) {
/* 146 */         int i = this.lockedIDs.indexOf(paramActivationID);
/* 147 */         if (i < 0) {
/* 148 */           this.lockedIDs.add(paramActivationID);
/* 149 */           return;
/*     */         }
/* 151 */         localActivationID1 = (ActivationID)this.lockedIDs.get(i);
/*     */       }
/*     */ 
/* 155 */       synchronized (localActivationID1) {
/* 156 */         synchronized (this.lockedIDs) {
/* 157 */           int j = this.lockedIDs.indexOf(localActivationID1);
/* 158 */           if (j < 0) continue;
/* 159 */           ActivationID localActivationID2 = (ActivationID)this.lockedIDs.get(j);
/* 160 */           if (localActivationID2 != localActivationID1)
/*     */           {
/* 164 */             continue;
/*     */           }
/*     */         }
/*     */         try {
/* 168 */           localActivationID1.wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void releaseLock(ActivationID paramActivationID)
/*     */   {
/* 181 */     synchronized (this.lockedIDs) {
/* 182 */       paramActivationID = (ActivationID)this.lockedIDs.remove(this.lockedIDs.indexOf(paramActivationID));
/*     */     }
/*     */ 
/* 185 */     synchronized (paramActivationID) {
/* 186 */       paramActivationID.notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   public MarshalledObject<? extends Remote> newInstance(final ActivationID paramActivationID, final ActivationDesc paramActivationDesc)
/*     */     throws ActivationException, RemoteException
/*     */   {
/* 211 */     RegistryImpl.checkAccess("ActivationInstantiator.newInstance");
/*     */ 
/* 213 */     if (!this.groupID.equals(paramActivationDesc.getGroupID()))
/* 214 */       throw new ActivationException("newInstance in wrong group");
/*     */     try
/*     */     {
/* 217 */       acquireLock(paramActivationID);
/* 218 */       synchronized (this) {
/* 219 */         if (this.groupInactive == true) {
/* 220 */           throw new InactiveGroupException("group is inactive");
/*     */         }
/*     */       }
/* 223 */       ??? = (ActiveEntry)this.active.get(paramActivationID);
/* 224 */       if (??? != null) {
/* 225 */         return ((ActiveEntry)???).mobj;
/*     */       }
/* 227 */       Object localObject2 = paramActivationDesc.getClassName();
/*     */ 
/* 229 */       final Class localClass = RMIClassLoader.loadClass(paramActivationDesc.getLocation(), (String)localObject2).asSubclass(Remote.class);
/*     */ 
/* 232 */       Remote localRemote = null;
/*     */ 
/* 234 */       final Thread localThread = Thread.currentThread();
/* 235 */       final ClassLoader localClassLoader1 = localThread.getContextClassLoader();
/* 236 */       ClassLoader localClassLoader2 = localClass.getClassLoader();
/* 237 */       final ClassLoader localClassLoader3 = covers(localClassLoader2, localClassLoader1) ? localClassLoader2 : localClassLoader1;
/*     */       try
/*     */       {
/* 251 */         localRemote = (Remote)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Remote run()
/*     */             throws InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
/*     */           {
/* 257 */             Constructor localConstructor = localClass.getDeclaredConstructor(new Class[] { ActivationID.class, MarshalledObject.class });
/*     */ 
/* 260 */             localConstructor.setAccessible(true);
/*     */             try
/*     */             {
/* 268 */               localThread.setContextClassLoader(localClassLoader3);
/* 269 */               return (Remote)localConstructor.newInstance(new Object[] { paramActivationID, paramActivationDesc.getData() });
/*     */             }
/*     */             finally {
/* 272 */               localThread.setContextClassLoader(localClassLoader1);
/*     */             }
/*     */           } } );
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException) {
/* 277 */         Exception localException2 = localPrivilegedActionException.getException();
/*     */ 
/* 280 */         if ((localException2 instanceof InstantiationException))
/* 281 */           throw ((InstantiationException)localException2);
/* 282 */         if ((localException2 instanceof NoSuchMethodException))
/* 283 */           throw ((NoSuchMethodException)localException2);
/* 284 */         if ((localException2 instanceof IllegalAccessException))
/* 285 */           throw ((IllegalAccessException)localException2);
/* 286 */         if ((localException2 instanceof InvocationTargetException))
/* 287 */           throw ((InvocationTargetException)localException2);
/* 288 */         if ((localException2 instanceof RuntimeException))
/* 289 */           throw ((RuntimeException)localException2);
/* 290 */         if ((localException2 instanceof Error)) {
/* 291 */           throw ((Error)localException2);
/*     */         }
/*     */       }
/*     */ 
/* 295 */       ??? = new ActiveEntry(localRemote);
/* 296 */       this.active.put(paramActivationID, ???);
/* 297 */       return ((ActiveEntry)???).mobj;
/*     */     }
/*     */     catch (NoSuchMethodException|NoSuchMethodError localNoSuchMethodException)
/*     */     {
/* 304 */       throw new ActivationException("Activatable object must provide an activation constructor", localNoSuchMethodException);
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException)
/*     */     {
/* 309 */       throw new ActivationException("exception in object constructor", localInvocationTargetException.getTargetException());
/*     */     }
/*     */     catch (Exception localException1)
/*     */     {
/* 313 */       throw new ActivationException("unable to activate object", localException1);
/*     */     } finally {
/* 315 */       releaseLock(paramActivationID);
/* 316 */       checkInactiveGroup();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean inactiveObject(ActivationID paramActivationID)
/*     */     throws ActivationException, UnknownObjectException, RemoteException
/*     */   {
/*     */     try
/*     */     {
/* 362 */       acquireLock(paramActivationID);
/* 363 */       synchronized (this) {
/* 364 */         if (this.groupInactive == true) {
/* 365 */           throw new ActivationException("group is inactive");
/*     */         }
/*     */       }
/* 368 */       ??? = (ActiveEntry)this.active.get(paramActivationID);
/* 369 */       if (??? == null)
/*     */       {
/* 371 */         throw new UnknownObjectException("object not active");
/*     */       }
/*     */       try
/*     */       {
/* 375 */         if (!Activatable.unexportObject(((ActiveEntry)???).impl, false))
/* 376 */           return false;
/*     */       }
/*     */       catch (NoSuchObjectException localNoSuchObjectException) {
/*     */       }
/*     */       try {
/* 381 */         super.inactiveObject(paramActivationID);
/*     */       }
/*     */       catch (UnknownObjectException localUnknownObjectException) {
/*     */       }
/* 385 */       this.active.remove(paramActivationID);
/*     */     }
/*     */     finally {
/* 388 */       releaseLock(paramActivationID);
/* 389 */       checkInactiveGroup();
/*     */     }
/*     */ 
/* 392 */     return true;
/*     */   }
/*     */ 
/*     */   private void checkInactiveGroup()
/*     */   {
/* 400 */     int i = 0;
/* 401 */     synchronized (this) {
/* 402 */       if ((this.active.size() == 0) && (this.lockedIDs.size() == 0) && (!this.groupInactive))
/*     */       {
/* 405 */         this.groupInactive = true;
/* 406 */         i = 1;
/*     */       }
/*     */     }
/*     */ 
/* 410 */     if (i != 0) {
/*     */       try {
/* 412 */         super.inactiveGroup();
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/*     */       try {
/* 417 */         UnicastRemoteObject.unexportObject(this, true);
/*     */       }
/*     */       catch (NoSuchObjectException localNoSuchObjectException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void activeObject(ActivationID paramActivationID, Remote paramRemote)
/*     */     throws ActivationException, UnknownObjectException, RemoteException
/*     */   {
/*     */     try
/*     */     {
/* 442 */       acquireLock(paramActivationID);
/* 443 */       synchronized (this) {
/* 444 */         if (this.groupInactive == true)
/* 445 */           throw new ActivationException("group is inactive");
/*     */       }
/* 447 */       if (!this.active.contains(paramActivationID)) {
/* 448 */         ??? = new ActiveEntry(paramRemote);
/* 449 */         this.active.put(paramActivationID, ???);
/*     */         try
/*     */         {
/* 452 */           super.activeObject(paramActivationID, ((ActiveEntry)???).mobj);
/*     */         } catch (RemoteException localRemoteException) {
/*     */         }
/*     */       }
/*     */     }
/*     */     finally {
/* 458 */       releaseLock(paramActivationID);
/* 459 */       checkInactiveGroup();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean covers(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
/*     */   {
/* 487 */     if (paramClassLoader2 == null)
/* 488 */       return true;
/* 489 */     if (paramClassLoader1 == null)
/* 490 */       return false;
/*     */     do
/*     */     {
/* 493 */       if (paramClassLoader1 == paramClassLoader2) {
/* 494 */         return true;
/*     */       }
/* 496 */       paramClassLoader1 = paramClassLoader1.getParent();
/* 497 */     }while (paramClassLoader1 != null);
/* 498 */     return false;
/*     */   }
/*     */ 
/*     */   private static class ActiveEntry
/*     */   {
/*     */     Remote impl;
/*     */     MarshalledObject<Remote> mobj;
/*     */ 
/*     */     ActiveEntry(Remote paramRemote)
/*     */       throws ActivationException
/*     */     {
/* 471 */       this.impl = paramRemote;
/*     */       try {
/* 473 */         this.mobj = new MarshalledObject(paramRemote);
/*     */       } catch (IOException localIOException) {
/* 475 */         throw new ActivationException("failed to marshal remote object", localIOException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ServerSocketFactoryImpl
/*     */     implements RMIServerSocketFactory
/*     */   {
/*     */     public ServerSocket createServerSocket(int paramInt)
/*     */       throws IOException
/*     */     {
/* 114 */       RMISocketFactory localRMISocketFactory = RMISocketFactory.getSocketFactory();
/* 115 */       if (localRMISocketFactory == null) {
/* 116 */         localRMISocketFactory = RMISocketFactory.getDefaultSocketFactory();
/*     */       }
/* 118 */       return localRMISocketFactory.createServerSocket(paramInt);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.ActivationGroupImpl
 * JD-Core Version:    0.6.2
 */