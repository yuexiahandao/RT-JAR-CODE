/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutput;
/*     */ import java.rmi.MarshalException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.LogStream;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.rmi.server.RemoteCall;
/*     */ import java.rmi.server.RemoteServer;
/*     */ import java.rmi.server.ServerNotActiveException;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permissions;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.server.Dispatcher;
/*     */ import sun.rmi.server.UnicastServerRef;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class Transport
/*     */ {
/*  57 */   static final int logLevel = LogStream.parseLevel(getLogLevel());
/*     */ 
/*  65 */   static final Log transportLog = Log.getLog("sun.rmi.transport.misc", "transport", logLevel);
/*     */ 
/*  69 */   private static final ThreadLocal<Transport> currentTransport = new ThreadLocal();
/*     */ 
/*  72 */   private static final ObjID dgcID = new ObjID(2);
/*     */ 
/*  80 */   private static final AccessControlContext SETCCL_ACC = new AccessControlContext(arrayOfProtectionDomain);
/*     */ 
/*     */   private static String getLogLevel()
/*     */   {
/*  60 */     return (String)AccessController.doPrivileged(new GetPropertyAction("sun.rmi.transport.logLevel"));
/*     */   }
/*     */ 
/*     */   public abstract Channel getChannel(Endpoint paramEndpoint);
/*     */ 
/*     */   public abstract void free(Endpoint paramEndpoint);
/*     */ 
/*     */   public void exportObject(Target paramTarget)
/*     */     throws RemoteException
/*     */   {
/* 104 */     paramTarget.setExportedTransport(this);
/* 105 */     ObjectTable.putTarget(paramTarget);
/*     */   }
/*     */ 
/*     */   protected void targetUnexported()
/*     */   {
/*     */   }
/*     */ 
/*     */   static Transport currentTransport()
/*     */   {
/* 120 */     return (Transport)currentTransport.get();
/*     */   }
/*     */ 
/*     */   protected abstract void checkAcceptPermission(AccessControlContext paramAccessControlContext);
/*     */ 
/*     */   private static void setContextClassLoader(ClassLoader paramClassLoader)
/*     */   {
/* 135 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/* 138 */         Thread.currentThread().setContextClassLoader(this.val$ccl);
/* 139 */         return null;
/*     */       }
/*     */     }
/*     */     , SETCCL_ACC);
/*     */   }
/*     */ 
/*     */   public boolean serviceCall(final RemoteCall paramRemoteCall)
/*     */   {
/*     */     try
/*     */     {
/*     */       try
/*     */       {
/* 168 */         localObject1 = ObjID.read(paramRemoteCall.getInputStream());
/*     */       } catch (IOException localIOException2) {
/* 170 */         throw new MarshalException("unable to read objID", localIOException2);
/*     */       }
/*     */ 
/* 174 */       Transport localTransport = ((ObjID)localObject1).equals(dgcID) ? null : this;
/* 175 */       Target localTarget = ObjectTable.getTarget(new ObjectEndpoint((ObjID)localObject1, localTransport));
/*     */       final Remote localRemote;
/* 178 */       if ((localTarget == null) || ((localRemote = localTarget.getImpl()) == null)) {
/* 179 */         throw new NoSuchObjectException("no such object in table");
/*     */       }
/*     */ 
/* 182 */       final Dispatcher localDispatcher = localTarget.getDispatcher();
/* 183 */       localTarget.incrementCallCount();
/*     */       try
/*     */       {
/* 186 */         transportLog.log(Log.VERBOSE, "call dispatcher");
/*     */ 
/* 188 */         final AccessControlContext localAccessControlContext = localTarget.getAccessControlContext();
/*     */ 
/* 190 */         ClassLoader localClassLoader1 = localTarget.getContextClassLoader();
/*     */ 
/* 192 */         ClassLoader localClassLoader2 = Thread.currentThread().getContextClassLoader();
/*     */         try
/*     */         {
/* 195 */           setContextClassLoader(localClassLoader1);
/* 196 */           currentTransport.set(this);
/*     */           try {
/* 198 */             AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */             {
/*     */               public Void run() throws IOException {
/* 201 */                 Transport.this.checkAcceptPermission(localAccessControlContext);
/* 202 */                 localDispatcher.dispatch(localRemote, paramRemoteCall);
/* 203 */                 return null;
/*     */               }
/*     */             }
/*     */             , localAccessControlContext);
/*     */           }
/*     */           catch (PrivilegedActionException localPrivilegedActionException)
/*     */           {
/* 207 */             throw ((IOException)localPrivilegedActionException.getException());
/*     */           }
/*     */         } finally {
/* 210 */           setContextClassLoader(localClassLoader2);
/* 211 */           currentTransport.set(null);
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException3) {
/* 215 */         transportLog.log(Log.BRIEF, "exception thrown by dispatcher: ", localIOException3);
/*     */ 
/* 217 */         return false;
/*     */       } finally {
/* 219 */         localTarget.decrementCallCount();
/*     */       }
/*     */     }
/*     */     catch (RemoteException localRemoteException)
/*     */     {
/*     */       Object localObject1;
/* 225 */       if (UnicastServerRef.callLog.isLoggable(Log.BRIEF))
/*     */       {
/* 227 */         localObject1 = "";
/*     */         try {
/* 229 */           localObject1 = "[" + RemoteServer.getClientHost() + "] ";
/*     */         }
/*     */         catch (ServerNotActiveException localServerNotActiveException) {
/*     */         }
/* 233 */         String str = (String)localObject1 + "exception: ";
/* 234 */         UnicastServerRef.callLog.log(Log.BRIEF, str, localRemoteException);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 246 */         localObject1 = paramRemoteCall.getResultStream(false);
/* 247 */         UnicastServerRef.clearStackTraces(localRemoteException);
/* 248 */         ((ObjectOutput)localObject1).writeObject(localRemoteException);
/* 249 */         paramRemoteCall.releaseOutputStream();
/*     */       }
/*     */       catch (IOException localIOException1) {
/* 252 */         transportLog.log(Log.BRIEF, "exception thrown marshalling exception: ", localIOException1);
/*     */ 
/* 254 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  77 */     Permissions localPermissions = new Permissions();
/*  78 */     localPermissions.add(new RuntimePermission("setContextClassLoader"));
/*  79 */     ProtectionDomain[] arrayOfProtectionDomain = { new ProtectionDomain(null, localPermissions) };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.Transport
 * JD-Core Version:    0.6.2
 */