/*     */ package java.rmi.activation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.rmi.MarshalledObject;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.UnmarshalException;
/*     */ import java.rmi.server.RemoteObject;
/*     */ import java.rmi.server.RemoteObjectInvocationHandler;
/*     */ import java.rmi.server.RemoteRef;
/*     */ import java.rmi.server.UID;
/*     */ 
/*     */ public class ActivationID
/*     */   implements Serializable
/*     */ {
/*     */   private transient Activator activator;
/*  79 */   private transient UID uid = new UID();
/*     */   private static final long serialVersionUID = -4608673054848209235L;
/*     */ 
/*     */   public ActivationID(Activator paramActivator)
/*     */   {
/*  96 */     this.activator = paramActivator;
/*     */   }
/*     */ 
/*     */   public Remote activate(boolean paramBoolean)
/*     */     throws ActivationException, UnknownObjectException, RemoteException
/*     */   {
/*     */     try
/*     */     {
/* 115 */       MarshalledObject localMarshalledObject = this.activator.activate(this, paramBoolean);
/*     */ 
/* 117 */       return (Remote)localMarshalledObject.get();
/*     */     } catch (RemoteException localRemoteException) {
/* 119 */       throw localRemoteException;
/*     */     } catch (IOException localIOException) {
/* 121 */       throw new UnmarshalException("activation failed", localIOException);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 123 */       throw new UnmarshalException("activation failed", localClassNotFoundException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 136 */     return this.uid.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 152 */     if ((paramObject instanceof ActivationID)) {
/* 153 */       ActivationID localActivationID = (ActivationID)paramObject;
/* 154 */       return (this.uid.equals(localActivationID.uid)) && (this.activator.equals(localActivationID.activator));
/*     */     }
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 208 */     paramObjectOutputStream.writeObject(this.uid);
/*     */     RemoteRef localRemoteRef;
/* 211 */     if ((this.activator instanceof RemoteObject)) {
/* 212 */       localRemoteRef = ((RemoteObject)this.activator).getRef();
/* 213 */     } else if (Proxy.isProxyClass(this.activator.getClass())) {
/* 214 */       InvocationHandler localInvocationHandler = Proxy.getInvocationHandler(this.activator);
/* 215 */       if (!(localInvocationHandler instanceof RemoteObjectInvocationHandler)) {
/* 216 */         throw new InvalidObjectException("unexpected invocation handler");
/*     */       }
/*     */ 
/* 219 */       localRemoteRef = ((RemoteObjectInvocationHandler)localInvocationHandler).getRef();
/*     */     }
/*     */     else {
/* 222 */       throw new InvalidObjectException("unexpected activator type");
/*     */     }
/* 224 */     paramObjectOutputStream.writeUTF(localRemoteRef.getRefClass(paramObjectOutputStream));
/* 225 */     localRemoteRef.writeExternal(paramObjectOutputStream);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 267 */     this.uid = ((UID)paramObjectInputStream.readObject());
/*     */     try
/*     */     {
/* 270 */       Class localClass = Class.forName("sun.rmi.server." + paramObjectInputStream.readUTF()).asSubclass(RemoteRef.class);
/*     */ 
/* 273 */       RemoteRef localRemoteRef = (RemoteRef)localClass.newInstance();
/* 274 */       localRemoteRef.readExternal(paramObjectInputStream);
/* 275 */       this.activator = ((Activator)Proxy.newProxyInstance(null, new Class[] { Activator.class }, new RemoteObjectInvocationHandler(localRemoteRef)));
/*     */     }
/*     */     catch (InstantiationException localInstantiationException)
/*     */     {
/* 281 */       throw ((IOException)new InvalidObjectException("Unable to create remote reference").initCause(localInstantiationException));
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException)
/*     */     {
/* 285 */       throw ((IOException)new InvalidObjectException("Unable to create remote reference").initCause(localIllegalAccessException));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.ActivationID
 * JD-Core Version:    0.6.2
 */