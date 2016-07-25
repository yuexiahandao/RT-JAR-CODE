/*    */ package sun.management.jmxremote;
/*    */ 
/*    */ import java.rmi.AccessException;
/*    */ import java.rmi.NotBoundException;
/*    */ import java.rmi.Remote;
/*    */ import java.rmi.RemoteException;
/*    */ import java.rmi.server.RMIClientSocketFactory;
/*    */ import java.rmi.server.RMIServerSocketFactory;
/*    */ import sun.rmi.registry.RegistryImpl;
/*    */ 
/*    */ public class SingleEntryRegistry extends RegistryImpl
/*    */ {
/*    */   private final String name;
/*    */   private final Remote object;
/*    */   private static final long serialVersionUID = -4897238949499730950L;
/*    */ 
/*    */   SingleEntryRegistry(int paramInt, String paramString, Remote paramRemote)
/*    */     throws RemoteException
/*    */   {
/* 48 */     super(paramInt);
/* 49 */     this.name = paramString;
/* 50 */     this.object = paramRemote;
/*    */   }
/*    */ 
/*    */   SingleEntryRegistry(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory, String paramString, Remote paramRemote)
/*    */     throws RemoteException
/*    */   {
/* 59 */     super(paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/* 60 */     this.name = paramString;
/* 61 */     this.object = paramRemote;
/*    */   }
/*    */ 
/*    */   public String[] list() {
/* 65 */     return new String[] { this.name };
/*    */   }
/*    */ 
/*    */   public Remote lookup(String paramString) throws NotBoundException {
/* 69 */     if (paramString.equals(this.name))
/* 70 */       return this.object;
/* 71 */     throw new NotBoundException("Not bound: \"" + paramString + "\" (only " + "bound name is \"" + this.name + "\")");
/*    */   }
/*    */ 
/*    */   public void bind(String paramString, Remote paramRemote) throws AccessException
/*    */   {
/* 76 */     throw new AccessException("Cannot modify this registry");
/*    */   }
/*    */ 
/*    */   public void rebind(String paramString, Remote paramRemote) throws AccessException {
/* 80 */     throw new AccessException("Cannot modify this registry");
/*    */   }
/*    */ 
/*    */   public void unbind(String paramString) throws AccessException {
/* 84 */     throw new AccessException("Cannot modify this registry");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jmxremote.SingleEntryRegistry
 * JD-Core Version:    0.6.2
 */