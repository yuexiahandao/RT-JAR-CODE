/*     */ package sun.rmi.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.MarshalException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.ServerError;
/*     */ import java.rmi.ServerException;
/*     */ import java.rmi.UnmarshalException;
/*     */ import java.rmi.server.ExportException;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.rmi.server.RemoteCall;
/*     */ import java.rmi.server.RemoteRef;
/*     */ import java.rmi.server.RemoteStub;
/*     */ import java.rmi.server.ServerNotActiveException;
/*     */ import java.rmi.server.ServerRef;
/*     */ import java.rmi.server.Skeleton;
/*     */ import java.rmi.server.SkeletonNotFoundException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.transport.LiveRef;
/*     */ import sun.rmi.transport.Target;
/*     */ import sun.rmi.transport.tcp.TCPTransport;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ 
/*     */ public class UnicastServerRef extends UnicastRef
/*     */   implements ServerRef, Dispatcher
/*     */ {
/*  74 */   public static final boolean logCalls = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("java.rmi.server.logCalls"))).booleanValue();
/*     */ 
/*  78 */   public static final Log callLog = Log.getLog("sun.rmi.server.call", "RMI", logCalls);
/*     */   private static final long serialVersionUID = -7384275867073752268L;
/*  85 */   private static final boolean wantExceptionLog = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.rmi.server.exceptionTrace"))).booleanValue();
/*     */ 
/*  89 */   private boolean forceStubUse = false;
/*     */ 
/*  95 */   private static final boolean suppressStackTraces = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.rmi.server.suppressStackTraces"))).booleanValue();
/*     */   private transient Skeleton skel;
/* 107 */   private transient Map<Long, Method> hashToMethod_Map = null;
/*     */ 
/* 113 */   private static final WeakClassHashMap<Map<Long, Method>> hashToMethod_Maps = new HashToMethod_Maps();
/*     */ 
/* 117 */   private static final Map<Class<?>, ?> withoutSkeletons = Collections.synchronizedMap(new WeakHashMap());
/*     */ 
/*     */   public UnicastServerRef()
/*     */   {
/*     */   }
/*     */ 
/*     */   public UnicastServerRef(LiveRef paramLiveRef)
/*     */   {
/* 131 */     super(paramLiveRef);
/*     */   }
/*     */ 
/*     */   public UnicastServerRef(int paramInt)
/*     */   {
/* 139 */     super(new LiveRef(paramInt));
/*     */   }
/*     */ 
/*     */   public UnicastServerRef(boolean paramBoolean)
/*     */   {
/* 156 */     this(0);
/* 157 */     this.forceStubUse = paramBoolean;
/*     */   }
/*     */ 
/*     */   public RemoteStub exportObject(Remote paramRemote, Object paramObject)
/*     */     throws RemoteException
/*     */   {
/* 177 */     this.forceStubUse = true;
/* 178 */     return (RemoteStub)exportObject(paramRemote, paramObject, false);
/*     */   }
/*     */ 
/*     */   public Remote exportObject(Remote paramRemote, Object paramObject, boolean paramBoolean)
/*     */     throws RemoteException
/*     */   {
/* 192 */     Class localClass = paramRemote.getClass();
/*     */     Remote localRemote;
/*     */     try
/*     */     {
/* 196 */       localRemote = Util.createProxy(localClass, getClientRef(), this.forceStubUse);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 198 */       throw new ExportException("remote object implements illegal remote interface", localIllegalArgumentException);
/*     */     }
/*     */ 
/* 201 */     if ((localRemote instanceof RemoteStub)) {
/* 202 */       setSkeleton(paramRemote);
/*     */     }
/*     */ 
/* 205 */     Target localTarget = new Target(paramRemote, this, localRemote, this.ref.getObjID(), paramBoolean);
/*     */ 
/* 207 */     this.ref.exportObject(localTarget);
/* 208 */     this.hashToMethod_Map = ((Map)hashToMethod_Maps.get(localClass));
/* 209 */     return localRemote;
/*     */   }
/*     */ 
/*     */   public String getClientHost()
/*     */     throws ServerNotActiveException
/*     */   {
/* 220 */     return TCPTransport.getClientHost();
/*     */   }
/*     */ 
/*     */   public void setSkeleton(Remote paramRemote)
/*     */     throws RemoteException
/*     */   {
/* 227 */     if (!withoutSkeletons.containsKey(paramRemote.getClass()))
/*     */       try {
/* 229 */         this.skel = Util.createSkeleton(paramRemote);
/*     */       }
/*     */       catch (SkeletonNotFoundException localSkeletonNotFoundException)
/*     */       {
/* 237 */         withoutSkeletons.put(paramRemote.getClass(), null);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void dispatch(Remote paramRemote, RemoteCall paramRemoteCall)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*     */       ObjectInput localObjectInput;
/*     */       long l;
/*     */       try
/*     */       {
/* 263 */         localObjectInput = paramRemoteCall.getInputStream();
/* 264 */         int i = localObjectInput.readInt();
/* 265 */         if (i >= 0) {
/* 266 */           if (this.skel != null) { oldDispatch(paramRemote, paramRemoteCall, i);
/*     */             return;
/*     */           }
/* 270 */           throw new UnmarshalException("skeleton class not found but required for client version");
/*     */         }
/*     */ 
/* 275 */         l = localObjectInput.readLong();
/*     */       } catch (Exception localException) {
/* 277 */         throw new UnmarshalException("error unmarshalling call header", localException);
/*     */       }
/*     */ 
/* 288 */       localObject2 = (MarshalInputStream)localObjectInput;
/* 289 */       ((MarshalInputStream)localObject2).skipDefaultResolveClass();
/*     */ 
/* 291 */       Method localMethod = (Method)this.hashToMethod_Map.get(Long.valueOf(l));
/* 292 */       if (localMethod == null) {
/* 293 */         throw new UnmarshalException("unrecognized method hash: method not supported by remote object");
/*     */       }
/*     */ 
/* 298 */       logCall(paramRemote, localMethod);
/*     */ 
/* 301 */       Class[] arrayOfClass = localMethod.getParameterTypes();
/* 302 */       Object[] arrayOfObject = new Object[arrayOfClass.length];
/*     */       try
/*     */       {
/* 305 */         unmarshalCustomCallData(localObjectInput);
/* 306 */         for (int j = 0; j < arrayOfClass.length; j++)
/* 307 */           arrayOfObject[j] = unmarshalValue(arrayOfClass[j], localObjectInput);
/*     */       }
/*     */       catch (IOException localIOException1) {
/* 310 */         throw new UnmarshalException("error unmarshalling arguments", localIOException1);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {
/* 313 */         throw new UnmarshalException("error unmarshalling arguments", localClassNotFoundException);
/*     */       }
/*     */       finally {
/* 316 */         paramRemoteCall.releaseInputStream();
/*     */       }
/*     */ 
/*     */       Object localObject3;
/*     */       try
/*     */       {
/* 322 */         localObject3 = localMethod.invoke(paramRemote, arrayOfObject);
/*     */       } catch (InvocationTargetException localInvocationTargetException) {
/* 324 */         throw localInvocationTargetException.getTargetException();
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 329 */         ObjectOutput localObjectOutput = paramRemoteCall.getResultStream(true);
/* 330 */         Class localClass = localMethod.getReturnType();
/* 331 */         if (localClass != Void.TYPE)
/* 332 */           marshalValue(localClass, localObject3, localObjectOutput);
/*     */       }
/*     */       catch (IOException localIOException2) {
/* 335 */         throw new MarshalException("error marshalling return", localIOException2);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 346 */       logCallException(localThrowable);
/*     */ 
/* 348 */       Object localObject2 = paramRemoteCall.getResultStream(false);
/*     */       Object localObject1;
/* 349 */       if ((localThrowable instanceof Error)) {
/* 350 */         localObject1 = new ServerError("Error occurred in server thread", (Error)localThrowable);
/*     */       }
/* 352 */       else if ((localObject1 instanceof RemoteException)) {
/* 353 */         localObject1 = new ServerException("RemoteException occurred in server thread", (Exception)localObject1);
/*     */       }
/*     */ 
/* 357 */       if (suppressStackTraces) {
/* 358 */         clearStackTraces((Throwable)localObject1);
/*     */       }
/* 360 */       ((ObjectOutput)localObject2).writeObject(localObject1);
/*     */     } finally {
/* 362 */       paramRemoteCall.releaseInputStream();
/* 363 */       paramRemoteCall.releaseOutputStream();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void unmarshalCustomCallData(ObjectInput paramObjectInput)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void oldDispatch(Remote paramRemote, RemoteCall paramRemoteCall, int paramInt)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*     */       ObjectInput localObjectInput;
/*     */       long l;
/*     */       try
/*     */       {
/* 392 */         localObjectInput = paramRemoteCall.getInputStream();
/*     */         try {
/* 394 */           Class localClass = Class.forName("sun.rmi.transport.DGCImpl_Skel");
/* 395 */           if (localClass.isAssignableFrom(this.skel.getClass()))
/* 396 */             ((MarshalInputStream)localObjectInput).useCodebaseOnly();
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/*     */         }
/* 399 */         l = localObjectInput.readLong();
/*     */       } catch (Exception localException) {
/* 401 */         throw new UnmarshalException("error unmarshalling call header", localException);
/*     */       }
/*     */ 
/* 406 */       logCall(paramRemote, this.skel.getOperations()[paramInt]);
/* 407 */       unmarshalCustomCallData(localObjectInput);
/*     */ 
/* 409 */       this.skel.dispatch(paramRemote, paramRemoteCall, paramInt, l);
/*     */     }
/*     */     catch (Throwable localThrowable) {
/* 412 */       logCallException(localThrowable);
/*     */ 
/* 414 */       ObjectOutput localObjectOutput = paramRemoteCall.getResultStream(false);
/*     */       Object localObject1;
/* 415 */       if ((localThrowable instanceof Error)) {
/* 416 */         localObject1 = new ServerError("Error occurred in server thread", (Error)localThrowable);
/*     */       }
/* 418 */       else if ((localObject1 instanceof RemoteException)) {
/* 419 */         localObject1 = new ServerException("RemoteException occurred in server thread", (Exception)localObject1);
/*     */       }
/*     */ 
/* 423 */       if (suppressStackTraces) {
/* 424 */         clearStackTraces((Throwable)localObject1);
/*     */       }
/* 426 */       localObjectOutput.writeObject(localObject1);
/*     */     } finally {
/* 428 */       paramRemoteCall.releaseInputStream();
/* 429 */       paramRemoteCall.releaseOutputStream();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void clearStackTraces(Throwable paramThrowable)
/*     */   {
/* 439 */     StackTraceElement[] arrayOfStackTraceElement = new StackTraceElement[0];
/* 440 */     while (paramThrowable != null) {
/* 441 */       paramThrowable.setStackTrace(arrayOfStackTraceElement);
/* 442 */       paramThrowable = paramThrowable.getCause();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void logCall(Remote paramRemote, Object paramObject)
/*     */   {
/* 451 */     if (callLog.isLoggable(Log.VERBOSE)) {
/*     */       String str;
/*     */       try {
/* 454 */         str = getClientHost();
/*     */       } catch (ServerNotActiveException localServerNotActiveException) {
/* 456 */         str = "(local)";
/*     */       }
/* 458 */       callLog.log(Log.VERBOSE, "[" + str + ": " + paramRemote.getClass().getName() + this.ref.getObjID().toString() + ": " + paramObject + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void logCallException(Throwable paramThrowable)
/*     */   {
/*     */     Object localObject1;
/* 470 */     if (callLog.isLoggable(Log.BRIEF)) {
/* 471 */       localObject1 = "";
/*     */       try {
/* 473 */         localObject1 = "[" + getClientHost() + "] ";
/*     */       } catch (ServerNotActiveException localServerNotActiveException) {
/*     */       }
/* 476 */       callLog.log(Log.BRIEF, (String)localObject1 + "exception: ", paramThrowable);
/*     */     }
/*     */ 
/* 480 */     if (wantExceptionLog) {
/* 481 */       localObject1 = System.err;
/* 482 */       synchronized (localObject1) {
/* 483 */         ((PrintStream)localObject1).println();
/* 484 */         ((PrintStream)localObject1).println("Exception dispatching call to " + this.ref.getObjID() + " in thread \"" + Thread.currentThread().getName() + "\" at " + new Date() + ":");
/*     */ 
/* 488 */         paramThrowable.printStackTrace((PrintStream)localObject1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getRefClass(ObjectOutput paramObjectOutput)
/*     */   {
/* 497 */     return "UnicastServerRef";
/*     */   }
/*     */ 
/*     */   protected RemoteRef getClientRef()
/*     */   {
/* 507 */     return new UnicastRef(this.ref);
/*     */   }
/*     */ 
/*     */   public void writeExternal(ObjectOutput paramObjectOutput)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void readExternal(ObjectInput paramObjectInput)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 525 */     this.ref = null;
/* 526 */     this.skel = null;
/*     */   }
/*     */ 
/*     */   private static class HashToMethod_Maps extends WeakClassHashMap<Map<Long, Method>>
/*     */   {
/*     */     protected Map<Long, Method> computeValue(Class<?> paramClass)
/*     */     {
/* 540 */       HashMap localHashMap = new HashMap();
/* 541 */       for (Object localObject = paramClass; 
/* 542 */         localObject != null; 
/* 543 */         localObject = ((Class)localObject).getSuperclass())
/*     */       {
/* 545 */         for (Class localClass : ((Class)localObject).getInterfaces()) {
/* 546 */           if (Remote.class.isAssignableFrom(localClass)) {
/* 547 */             for (Method localMethod1 : localClass.getMethods()) {
/* 548 */               final Method localMethod2 = localMethod1;
/*     */ 
/* 554 */               AccessController.doPrivileged(new PrivilegedAction()
/*     */               {
/*     */                 public Void run() {
/* 557 */                   localMethod2.setAccessible(true);
/* 558 */                   return null;
/*     */                 }
/*     */               });
/* 561 */               localHashMap.put(Long.valueOf(Util.computeMethodHash(localMethod2)), localMethod2);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 566 */       return localHashMap;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.UnicastServerRef
 * JD-Core Version:    0.6.2
 */