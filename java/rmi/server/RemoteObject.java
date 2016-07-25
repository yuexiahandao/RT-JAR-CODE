/*     */ package java.rmi.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.rmi.MarshalException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import sun.rmi.server.Util;
/*     */ import sun.rmi.transport.ObjectTable;
/*     */ 
/*     */ public abstract class RemoteObject
/*     */   implements Remote, Serializable
/*     */ {
/*     */   protected transient RemoteRef ref;
/*     */   private static final long serialVersionUID = -3215090123894869218L;
/*     */ 
/*     */   protected RemoteObject()
/*     */   {
/*  56 */     this.ref = null;
/*     */   }
/*     */ 
/*     */   protected RemoteObject(RemoteRef paramRemoteRef)
/*     */   {
/*  65 */     this.ref = paramRemoteRef;
/*     */   }
/*     */ 
/*     */   public RemoteRef getRef()
/*     */   {
/*  84 */     return this.ref;
/*     */   }
/*     */ 
/*     */   public static Remote toStub(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/*  98 */     if (((paramRemote instanceof RemoteStub)) || ((paramRemote != null) && (Proxy.isProxyClass(paramRemote.getClass())) && ((Proxy.getInvocationHandler(paramRemote) instanceof RemoteObjectInvocationHandler))))
/*     */     {
/* 104 */       return paramRemote;
/*     */     }
/* 106 */     return ObjectTable.getStub(paramRemote);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 118 */     return this.ref == null ? super.hashCode() : this.ref.remoteHashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 135 */     if ((paramObject instanceof RemoteObject)) {
/* 136 */       if (this.ref == null) {
/* 137 */         return paramObject == this;
/*     */       }
/* 139 */       return this.ref.remoteEquals(((RemoteObject)paramObject).ref);
/*     */     }
/* 141 */     if (paramObject != null)
/*     */     {
/* 148 */       return paramObject.equals(this);
/*     */     }
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 158 */     String str = Util.getUnqualifiedName(getClass());
/* 159 */     return str + "[" + this.ref.remoteToString() + "]";
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 363 */     if (this.ref == null) {
/* 364 */       throw new MarshalException("Invalid remote object");
/*     */     }
/* 366 */     String str = this.ref.getRefClass(paramObjectOutputStream);
/* 367 */     if ((str == null) || (str.length() == 0))
/*     */     {
/* 372 */       paramObjectOutputStream.writeUTF("");
/* 373 */       paramObjectOutputStream.writeObject(this.ref);
/*     */     }
/*     */     else
/*     */     {
/* 379 */       paramObjectOutputStream.writeUTF(str);
/* 380 */       this.ref.writeExternal(paramObjectOutputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 424 */     String str1 = paramObjectInputStream.readUTF();
/* 425 */     if ((str1 == null) || (str1.length() == 0))
/*     */     {
/* 430 */       this.ref = ((RemoteRef)paramObjectInputStream.readObject());
/*     */     }
/*     */     else
/*     */     {
/* 437 */       String str2 = "sun.rmi.server." + str1;
/*     */ 
/* 439 */       Class localClass = Class.forName(str2);
/*     */       try {
/* 441 */         this.ref = ((RemoteRef)localClass.newInstance());
/*     */       }
/*     */       catch (InstantiationException localInstantiationException)
/*     */       {
/* 449 */         throw new ClassNotFoundException(str2, localInstantiationException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 451 */         throw new ClassNotFoundException(str2, localIllegalAccessException);
/*     */       } catch (ClassCastException localClassCastException) {
/* 453 */         throw new ClassNotFoundException(str2, localClassCastException);
/*     */       }
/* 455 */       this.ref.readExternal(paramObjectInputStream);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.RemoteObject
 * JD-Core Version:    0.6.2
 */