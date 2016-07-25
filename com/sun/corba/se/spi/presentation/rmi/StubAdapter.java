/*     */ package com.sun.corba.se.spi.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.oa.poa.POAManagerImpl;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.rmi.CORBA.Stub;
/*     */ import javax.rmi.CORBA.Tie;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.POAManager;
/*     */ import org.omg.PortableServer.POAPackage.ServantNotActive;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ 
/*     */ public abstract class StubAdapter
/*     */ {
/*  63 */   private static ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.presentation");
/*     */ 
/*     */   public static boolean isStubClass(Class paramClass)
/*     */   {
/*  68 */     return (ObjectImpl.class.isAssignableFrom(paramClass)) || (DynamicStub.class.isAssignableFrom(paramClass));
/*     */   }
/*     */ 
/*     */   public static boolean isStub(java.lang.Object paramObject)
/*     */   {
/*  74 */     return ((paramObject instanceof DynamicStub)) || ((paramObject instanceof ObjectImpl));
/*     */   }
/*     */ 
/*     */   public static void setDelegate(java.lang.Object paramObject, Delegate paramDelegate)
/*     */   {
/*  80 */     if ((paramObject instanceof DynamicStub))
/*  81 */       ((DynamicStub)paramObject).setDelegate(paramDelegate);
/*  82 */     else if ((paramObject instanceof ObjectImpl))
/*  83 */       ((ObjectImpl)paramObject)._set_delegate(paramDelegate);
/*     */     else
/*  85 */       throw wrapper.setDelegateRequiresStub();
/*     */   }
/*     */ 
/*     */   public static org.omg.CORBA.Object activateServant(Servant paramServant)
/*     */   {
/*  92 */     POA localPOA = paramServant._default_POA();
/*  93 */     org.omg.CORBA.Object localObject = null;
/*     */     try
/*     */     {
/*  96 */       localObject = localPOA.servant_to_reference(paramServant);
/*     */     } catch (ServantNotActive localServantNotActive) {
/*  98 */       throw wrapper.getDelegateServantNotActive(localServantNotActive);
/*     */     } catch (WrongPolicy localWrongPolicy) {
/* 100 */       throw wrapper.getDelegateWrongPolicy(localWrongPolicy);
/*     */     }
/*     */ 
/* 105 */     POAManager localPOAManager = localPOA.the_POAManager();
/* 106 */     if ((localPOAManager instanceof POAManagerImpl)) {
/* 107 */       POAManagerImpl localPOAManagerImpl = (POAManagerImpl)localPOAManager;
/* 108 */       localPOAManagerImpl.implicitActivation();
/*     */     }
/*     */ 
/* 111 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static org.omg.CORBA.Object activateTie(Tie paramTie)
/*     */   {
/* 125 */     if ((paramTie instanceof ObjectImpl))
/* 126 */       return paramTie.thisObject();
/* 127 */     if ((paramTie instanceof Servant)) {
/* 128 */       Servant localServant = (Servant)paramTie;
/* 129 */       return activateServant(localServant);
/*     */     }
/* 131 */     throw wrapper.badActivateTieCall();
/*     */   }
/*     */ 
/*     */   public static Delegate getDelegate(java.lang.Object paramObject)
/*     */   {
/* 141 */     if ((paramObject instanceof DynamicStub))
/* 142 */       return ((DynamicStub)paramObject).getDelegate();
/* 143 */     if ((paramObject instanceof ObjectImpl))
/* 144 */       return ((ObjectImpl)paramObject)._get_delegate();
/* 145 */     if ((paramObject instanceof Tie)) {
/* 146 */       Tie localTie = (Tie)paramObject;
/* 147 */       org.omg.CORBA.Object localObject = activateTie(localTie);
/* 148 */       return getDelegate(localObject);
/*     */     }
/* 150 */     throw wrapper.getDelegateRequiresStub();
/*     */   }
/*     */ 
/*     */   public static org.omg.CORBA.ORB getORB(java.lang.Object paramObject)
/*     */   {
/* 155 */     if ((paramObject instanceof DynamicStub))
/* 156 */       return ((DynamicStub)paramObject).getORB();
/* 157 */     if ((paramObject instanceof ObjectImpl)) {
/* 158 */       return ((ObjectImpl)paramObject)._orb();
/*     */     }
/* 160 */     throw wrapper.getOrbRequiresStub();
/*     */   }
/*     */ 
/*     */   public static String[] getTypeIds(java.lang.Object paramObject)
/*     */   {
/* 165 */     if ((paramObject instanceof DynamicStub))
/* 166 */       return ((DynamicStub)paramObject).getTypeIds();
/* 167 */     if ((paramObject instanceof ObjectImpl)) {
/* 168 */       return ((ObjectImpl)paramObject)._ids();
/*     */     }
/* 170 */     throw wrapper.getTypeIdsRequiresStub();
/*     */   }
/*     */ 
/*     */   public static void connect(java.lang.Object paramObject, org.omg.CORBA.ORB paramORB)
/*     */     throws RemoteException
/*     */   {
/* 176 */     if ((paramObject instanceof DynamicStub)) {
/* 177 */       ((DynamicStub)paramObject).connect((com.sun.corba.se.spi.orb.ORB)paramORB);
/*     */     }
/* 179 */     else if ((paramObject instanceof Stub))
/* 180 */       ((Stub)paramObject).connect(paramORB);
/* 181 */     else if ((paramObject instanceof ObjectImpl))
/* 182 */       paramORB.connect((org.omg.CORBA.Object)paramObject);
/*     */     else
/* 184 */       throw wrapper.connectRequiresStub();
/*     */   }
/*     */ 
/*     */   public static boolean isLocal(java.lang.Object paramObject)
/*     */   {
/* 189 */     if ((paramObject instanceof DynamicStub))
/* 190 */       return ((DynamicStub)paramObject).isLocal();
/* 191 */     if ((paramObject instanceof ObjectImpl)) {
/* 192 */       return ((ObjectImpl)paramObject)._is_local();
/*     */     }
/* 194 */     throw wrapper.isLocalRequiresStub();
/*     */   }
/*     */ 
/*     */   public static OutputStream request(java.lang.Object paramObject, String paramString, boolean paramBoolean)
/*     */   {
/* 200 */     if ((paramObject instanceof DynamicStub)) {
/* 201 */       return ((DynamicStub)paramObject).request(paramString, paramBoolean);
/*     */     }
/* 203 */     if ((paramObject instanceof ObjectImpl)) {
/* 204 */       return ((ObjectImpl)paramObject)._request(paramString, paramBoolean);
/*     */     }
/*     */ 
/* 207 */     throw wrapper.requestRequiresStub();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.presentation.rmi.StubAdapter
 * JD-Core Version:    0.6.2
 */