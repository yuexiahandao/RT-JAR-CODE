/*     */ package java.rmi.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import sun.rmi.server.UnicastServerRef;
/*     */ import sun.rmi.server.UnicastServerRef2;
/*     */ import sun.rmi.transport.ObjectTable;
/*     */ 
/*     */ public class UnicastRemoteObject extends RemoteServer
/*     */ {
/* 112 */   private int port = 0;
/*     */ 
/* 117 */   private RMIClientSocketFactory csf = null;
/*     */ 
/* 123 */   private RMIServerSocketFactory ssf = null;
/*     */   private static final long serialVersionUID = 4974527148936298033L;
/*     */ 
/*     */   protected UnicastRemoteObject()
/*     */     throws RemoteException
/*     */   {
/* 136 */     this(0);
/*     */   }
/*     */ 
/*     */   protected UnicastRemoteObject(int paramInt)
/*     */     throws RemoteException
/*     */   {
/* 149 */     this.port = paramInt;
/* 150 */     exportObject(this, paramInt);
/*     */   }
/*     */ 
/*     */   protected UnicastRemoteObject(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */     throws RemoteException
/*     */   {
/* 169 */     this.port = paramInt;
/* 170 */     this.csf = paramRMIClientSocketFactory;
/* 171 */     this.ssf = paramRMIServerSocketFactory;
/* 172 */     exportObject(this, paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 181 */     paramObjectInputStream.defaultReadObject();
/* 182 */     reexport();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/*     */     try
/*     */     {
/* 197 */       UnicastRemoteObject localUnicastRemoteObject = (UnicastRemoteObject)super.clone();
/* 198 */       localUnicastRemoteObject.reexport();
/* 199 */       return localUnicastRemoteObject;
/*     */     } catch (RemoteException localRemoteException) {
/* 201 */       throw new ServerCloneException("Clone failed", localRemoteException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void reexport()
/*     */     throws RemoteException
/*     */   {
/* 212 */     if ((this.csf == null) && (this.ssf == null))
/* 213 */       exportObject(this, this.port);
/*     */     else
/* 215 */       exportObject(this, this.port, this.csf, this.ssf);
/*     */   }
/*     */ 
/*     */   public static RemoteStub exportObject(Remote paramRemote)
/*     */     throws RemoteException
/*     */   {
/* 237 */     return (RemoteStub)exportObject(paramRemote, new UnicastServerRef(true));
/*     */   }
/*     */ 
/*     */   public static Remote exportObject(Remote paramRemote, int paramInt)
/*     */     throws RemoteException
/*     */   {
/* 252 */     return exportObject(paramRemote, new UnicastServerRef(paramInt));
/*     */   }
/*     */ 
/*     */   public static Remote exportObject(Remote paramRemote, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */     throws RemoteException
/*     */   {
/* 273 */     return exportObject(paramRemote, new UnicastServerRef2(paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory));
/*     */   }
/*     */ 
/*     */   public static boolean unexportObject(Remote paramRemote, boolean paramBoolean)
/*     */     throws NoSuchObjectException
/*     */   {
/* 297 */     return ObjectTable.unexportObject(paramRemote, paramBoolean);
/*     */   }
/*     */ 
/*     */   private static Remote exportObject(Remote paramRemote, UnicastServerRef paramUnicastServerRef)
/*     */     throws RemoteException
/*     */   {
/* 307 */     if ((paramRemote instanceof UnicastRemoteObject)) {
/* 308 */       ((UnicastRemoteObject)paramRemote).ref = paramUnicastServerRef;
/*     */     }
/* 310 */     return paramUnicastServerRef.exportObject(paramRemote, null, false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.UnicastRemoteObject
 * JD-Core Version:    0.6.2
 */