/*     */ package com.sun.jmx.remote.protocol.iiop;
/*     */ 
/*     */ import com.sun.jmx.remote.internal.IIOPProxy;
/*     */ import java.io.SerializablePermission;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permissions;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Properties;
/*     */ import javax.rmi.CORBA.Stub;
/*     */ import javax.rmi.PortableRemoteObject;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ 
/*     */ public class IIOPProxyImpl
/*     */   implements IIOPProxy
/*     */ {
/*  60 */   private static final AccessControlContext STUB_ACC = new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, localPermissions) });
/*     */ 
/*     */   public boolean isStub(java.lang.Object paramObject)
/*     */   {
/*  71 */     return paramObject instanceof Stub;
/*     */   }
/*     */ 
/*     */   public java.lang.Object getDelegate(java.lang.Object paramObject)
/*     */   {
/*  76 */     return ((Stub)paramObject)._get_delegate();
/*     */   }
/*     */ 
/*     */   public void setDelegate(java.lang.Object paramObject1, java.lang.Object paramObject2)
/*     */   {
/*  81 */     ((Stub)paramObject1)._set_delegate((Delegate)paramObject2);
/*     */   }
/*     */ 
/*     */   public java.lang.Object getOrb(java.lang.Object paramObject)
/*     */   {
/*     */     try {
/*  87 */       return ((Stub)paramObject)._orb();
/*     */     } catch (BAD_OPERATION localBAD_OPERATION) {
/*  89 */       throw new UnsupportedOperationException(localBAD_OPERATION);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void connect(java.lang.Object paramObject1, java.lang.Object paramObject2)
/*     */     throws RemoteException
/*     */   {
/*  97 */     ((Stub)paramObject1).connect((ORB)paramObject2);
/*     */   }
/*     */ 
/*     */   public boolean isOrb(java.lang.Object paramObject)
/*     */   {
/* 102 */     return paramObject instanceof ORB;
/*     */   }
/*     */ 
/*     */   public java.lang.Object createOrb(String[] paramArrayOfString, Properties paramProperties)
/*     */   {
/* 107 */     return ORB.init(paramArrayOfString, paramProperties);
/*     */   }
/*     */ 
/*     */   public java.lang.Object stringToObject(java.lang.Object paramObject, String paramString)
/*     */   {
/* 112 */     return ((ORB)paramObject).string_to_object(paramString);
/*     */   }
/*     */ 
/*     */   public String objectToString(java.lang.Object paramObject1, java.lang.Object paramObject2)
/*     */   {
/* 117 */     return ((ORB)paramObject1).object_to_string((org.omg.CORBA.Object)paramObject2);
/*     */   }
/*     */ 
/*     */   public <T> T narrow(java.lang.Object paramObject, Class<T> paramClass)
/*     */   {
/* 123 */     return PortableRemoteObject.narrow(paramObject, paramClass);
/*     */   }
/*     */ 
/*     */   public void exportObject(Remote paramRemote) throws RemoteException
/*     */   {
/* 128 */     PortableRemoteObject.exportObject(paramRemote);
/*     */   }
/*     */ 
/*     */   public void unexportObject(Remote paramRemote) throws NoSuchObjectException
/*     */   {
/* 133 */     PortableRemoteObject.unexportObject(paramRemote);
/*     */   }
/*     */ 
/*     */   public Remote toStub(final Remote paramRemote) throws NoSuchObjectException
/*     */   {
/* 138 */     if (System.getSecurityManager() == null)
/* 139 */       return PortableRemoteObject.toStub(paramRemote);
/*     */     try
/*     */     {
/* 142 */       return (Remote)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Remote run() throws Exception
/*     */         {
/* 146 */           return PortableRemoteObject.toStub(paramRemote);
/*     */         }
/*     */       }
/*     */       , STUB_ACC);
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException)
/*     */     {
/* 150 */       if ((localPrivilegedActionException.getException() instanceof NoSuchObjectException)) {
/* 151 */         throw ((NoSuchObjectException)localPrivilegedActionException.getException());
/*     */       }
/* 153 */       throw new RuntimeException("Unexpected exception type", localPrivilegedActionException.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  58 */     Permissions localPermissions = new Permissions();
/*  59 */     localPermissions.add(new SerializablePermission("enableSubclassImplementation"));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.protocol.iiop.IIOPProxyImpl
 * JD-Core Version:    0.6.2
 */