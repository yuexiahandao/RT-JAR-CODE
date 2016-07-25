/*     */ package sun.rmi.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.net.MalformedURLException;
/*     */ import java.rmi.ConnectException;
/*     */ import java.rmi.ConnectIOException;
/*     */ import java.rmi.MarshalException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.ServerError;
/*     */ import java.rmi.ServerException;
/*     */ import java.rmi.StubNotFoundException;
/*     */ import java.rmi.UnknownHostException;
/*     */ import java.rmi.UnmarshalException;
/*     */ import java.rmi.activation.ActivateFailedException;
/*     */ import java.rmi.activation.ActivationDesc;
/*     */ import java.rmi.activation.ActivationException;
/*     */ import java.rmi.activation.ActivationID;
/*     */ import java.rmi.activation.UnknownObjectException;
/*     */ import java.rmi.server.Operation;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import java.rmi.server.RemoteCall;
/*     */ import java.rmi.server.RemoteObject;
/*     */ import java.rmi.server.RemoteObjectInvocationHandler;
/*     */ import java.rmi.server.RemoteRef;
/*     */ import java.rmi.server.RemoteStub;
/*     */ 
/*     */ public class ActivatableRef
/*     */   implements RemoteRef
/*     */ {
/*     */   private static final long serialVersionUID = 7579060052569229166L;
/*     */   protected ActivationID id;
/*     */   protected RemoteRef ref;
/*  50 */   transient boolean force = false;
/*     */   private static final int MAX_RETRIES = 3;
/*     */   private static final String versionComplaint = "activation requires 1.2 stubs";
/*     */ 
/*     */   public ActivatableRef()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ActivatableRef(ActivationID paramActivationID, RemoteRef paramRemoteRef)
/*     */   {
/*  67 */     this.id = paramActivationID;
/*  68 */     this.ref = paramRemoteRef;
/*     */   }
/*     */ 
/*     */   public static Remote getStub(ActivationDesc paramActivationDesc, ActivationID paramActivationID)
/*     */     throws StubNotFoundException
/*     */   {
/*  80 */     String str = paramActivationDesc.getClassName();
/*     */     try
/*     */     {
/*  83 */       Class localClass = RMIClassLoader.loadClass(paramActivationDesc.getLocation(), str);
/*     */ 
/*  85 */       ActivatableRef localActivatableRef = new ActivatableRef(paramActivationID, null);
/*  86 */       return Util.createProxy(localClass, localActivatableRef, false);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/*  89 */       throw new StubNotFoundException("class implements an illegal remote interface", localIllegalArgumentException);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/*  93 */       throw new StubNotFoundException("unable to load class: " + str, localClassNotFoundException);
/*     */     }
/*     */     catch (MalformedURLException localMalformedURLException) {
/*  96 */       throw new StubNotFoundException("malformed URL", localMalformedURLException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object invoke(Remote paramRemote, Method paramMethod, Object[] paramArrayOfObject, long paramLong)
/*     */     throws Exception
/*     */   {
/* 122 */     boolean bool = false;
/*     */ 
/* 124 */     Object localObject2 = null;
/*     */     Object localObject1;
/* 130 */     synchronized (this) {
/* 131 */       if (this.ref == null) {
/* 132 */         localObject1 = activate(bool);
/* 133 */         bool = true;
/*     */       } else {
/* 135 */         localObject1 = this.ref;
/*     */       }
/*     */     }
/*     */ 
/* 139 */     for (int i = 3; i > 0; i--)
/*     */     {
/*     */       try {
/* 142 */         return ((RemoteRef)localObject1).invoke(paramRemote, paramMethod, paramArrayOfObject, paramLong);
/*     */       }
/*     */       catch (NoSuchObjectException localNoSuchObjectException)
/*     */       {
/* 147 */         localObject2 = localNoSuchObjectException;
/*     */       }
/*     */       catch (ConnectException localConnectException)
/*     */       {
/* 152 */         localObject2 = localConnectException;
/*     */       }
/*     */       catch (UnknownHostException localUnknownHostException)
/*     */       {
/* 157 */         localObject2 = localUnknownHostException;
/*     */       }
/*     */       catch (ConnectIOException localConnectIOException)
/*     */       {
/* 163 */         localObject2 = localConnectIOException;
/*     */       }
/*     */       catch (MarshalException localMarshalException)
/*     */       {
/* 169 */         throw localMarshalException;
/*     */       }
/*     */       catch (ServerError localServerError)
/*     */       {
/* 174 */         throw localServerError;
/*     */       }
/*     */       catch (ServerException localServerException)
/*     */       {
/* 179 */         throw localServerException;
/*     */       }
/*     */       catch (RemoteException localRemoteException)
/*     */       {
/* 194 */         synchronized (this) {
/* 195 */           if (localObject1 == this.ref) {
/* 196 */             this.ref = null;
/*     */           }
/*     */         }
/*     */ 
/* 200 */         throw localRemoteException;
/*     */       }
/*     */ 
/* 203 */       if (i > 1)
/*     */       {
/* 207 */         synchronized (this) {
/* 208 */           if ((((RemoteRef)localObject1).remoteEquals(this.ref)) || (this.ref == null)) {
/* 209 */             ??? = activate(bool);
/*     */ 
/* 211 */             if ((((RemoteRef)???).remoteEquals((RemoteRef)localObject1)) && ((localObject2 instanceof NoSuchObjectException)) && (!bool))
/*     */             {
/* 219 */               ??? = activate(true);
/*     */             }
/*     */ 
/* 222 */             localObject1 = ???;
/* 223 */             bool = true;
/*     */           } else {
/* 225 */             localObject1 = this.ref;
/* 226 */             bool = false;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 235 */     throw localObject2;
/*     */   }
/*     */ 
/*     */   private synchronized RemoteRef getRef()
/*     */     throws RemoteException
/*     */   {
/* 244 */     if (this.ref == null) {
/* 245 */       this.ref = activate(false);
/*     */     }
/*     */ 
/* 248 */     return this.ref;
/*     */   }
/*     */ 
/*     */   private RemoteRef activate(boolean paramBoolean)
/*     */     throws RemoteException
/*     */   {
/* 260 */     assert (Thread.holdsLock(this));
/*     */ 
/* 262 */     this.ref = null;
/*     */     try
/*     */     {
/* 276 */       Remote localRemote = this.id.activate(paramBoolean);
/* 277 */       ActivatableRef localActivatableRef = null;
/*     */ 
/* 279 */       if ((localRemote instanceof RemoteStub)) {
/* 280 */         localActivatableRef = (ActivatableRef)((RemoteStub)localRemote).getRef();
/*     */       }
/*     */       else
/*     */       {
/* 288 */         RemoteObjectInvocationHandler localRemoteObjectInvocationHandler = (RemoteObjectInvocationHandler)Proxy.getInvocationHandler(localRemote);
/*     */ 
/* 291 */         localActivatableRef = (ActivatableRef)localRemoteObjectInvocationHandler.getRef();
/*     */       }
/* 293 */       this.ref = localActivatableRef.ref;
/* 294 */       return this.ref;
/*     */     }
/*     */     catch (ConnectException localConnectException) {
/* 297 */       throw new ConnectException("activation failed", localConnectException);
/*     */     } catch (RemoteException localRemoteException) {
/* 299 */       throw new ConnectIOException("activation failed", localRemoteException);
/*     */     } catch (UnknownObjectException localUnknownObjectException) {
/* 301 */       throw new NoSuchObjectException("object not registered");
/*     */     } catch (ActivationException localActivationException) {
/* 303 */       throw new ActivateFailedException("activation failed", localActivationException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized RemoteCall newCall(RemoteObject paramRemoteObject, Operation[] paramArrayOfOperation, int paramInt, long paramLong)
/*     */     throws RemoteException
/*     */   {
/* 317 */     throw new UnsupportedOperationException("activation requires 1.2 stubs");
/*     */   }
/*     */ 
/*     */   public void invoke(RemoteCall paramRemoteCall)
/*     */     throws Exception
/*     */   {
/* 326 */     throw new UnsupportedOperationException("activation requires 1.2 stubs");
/*     */   }
/*     */ 
/*     */   public void done(RemoteCall paramRemoteCall)
/*     */     throws RemoteException
/*     */   {
/* 334 */     throw new UnsupportedOperationException("activation requires 1.2 stubs");
/*     */   }
/*     */ 
/*     */   public String getRefClass(ObjectOutput paramObjectOutput)
/*     */   {
/* 342 */     return "ActivatableRef";
/*     */   }
/*     */ 
/*     */   public void writeExternal(ObjectOutput paramObjectOutput)
/*     */     throws IOException
/*     */   {
/* 350 */     RemoteRef localRemoteRef = this.ref;
/*     */ 
/* 352 */     paramObjectOutput.writeObject(this.id);
/* 353 */     if (localRemoteRef == null) {
/* 354 */       paramObjectOutput.writeUTF("");
/*     */     } else {
/* 356 */       paramObjectOutput.writeUTF(localRemoteRef.getRefClass(paramObjectOutput));
/* 357 */       localRemoteRef.writeExternal(paramObjectOutput);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readExternal(ObjectInput paramObjectInput)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 369 */     this.id = ((ActivationID)paramObjectInput.readObject());
/* 370 */     this.ref = null;
/* 371 */     String str = paramObjectInput.readUTF();
/*     */ 
/* 373 */     if (str.equals("")) return;
/*     */     try
/*     */     {
/* 376 */       Class localClass = Class.forName("sun.rmi.server." + str);
/*     */ 
/* 378 */       this.ref = ((RemoteRef)localClass.newInstance());
/* 379 */       this.ref.readExternal(paramObjectInput);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 381 */       throw new UnmarshalException("Unable to create remote reference", localInstantiationException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 384 */       throw new UnmarshalException("Illegal access creating remote reference");
/*     */     }
/*     */   }
/*     */ 
/*     */   public String remoteToString()
/*     */   {
/* 393 */     return Util.getUnqualifiedName(getClass()) + " [remoteRef: " + this.ref + "]";
/*     */   }
/*     */ 
/*     */   public int remoteHashCode()
/*     */   {
/* 401 */     return this.id.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean remoteEquals(RemoteRef paramRemoteRef)
/*     */   {
/* 407 */     if ((paramRemoteRef instanceof ActivatableRef))
/* 408 */       return this.id.equals(((ActivatableRef)paramRemoteRef).id);
/* 409 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.ActivatableRef
 * JD-Core Version:    0.6.2
 */