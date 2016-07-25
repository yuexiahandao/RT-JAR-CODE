/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.impl.oa.NullServantImpl;
/*     */ import org.omg.PortableServer.ForwardRequest;
/*     */ import org.omg.PortableServer.POAPackage.NoServant;
/*     */ import org.omg.PortableServer.POAPackage.ObjectNotActive;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ import org.omg.PortableServer.ServantManager;
/*     */ 
/*     */ public class POAPolicyMediatorImpl_R_AOM extends POAPolicyMediatorBase_R
/*     */ {
/*     */   POAPolicyMediatorImpl_R_AOM(Policies paramPolicies, POAImpl paramPOAImpl)
/*     */   {
/*  55 */     super(paramPolicies, paramPOAImpl);
/*     */ 
/*  58 */     if (!paramPolicies.useActiveMapOnly())
/*  59 */       throw paramPOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
/*     */   }
/*     */ 
/*     */   protected Object internalGetServant(byte[] paramArrayOfByte, String paramString)
/*     */     throws ForwardRequest
/*     */   {
/*  65 */     Object localObject = internalIdToServant(paramArrayOfByte);
/*  66 */     if (localObject == null) {
/*  67 */       localObject = new NullServantImpl(this.poa.invocationWrapper().nullServant());
/*     */     }
/*  69 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void etherealizeAll()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ServantManager getServantManager()
/*     */     throws WrongPolicy
/*     */   {
/*  79 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public void setServantManager(ServantManager paramServantManager)
/*     */     throws WrongPolicy
/*     */   {
/*  85 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public Servant getDefaultServant() throws NoServant, WrongPolicy
/*     */   {
/*  90 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public void setDefaultServant(Servant paramServant) throws WrongPolicy
/*     */   {
/*  95 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public Servant idToServant(byte[] paramArrayOfByte)
/*     */     throws WrongPolicy, ObjectNotActive
/*     */   {
/* 101 */     Servant localServant = internalIdToServant(paramArrayOfByte);
/*     */ 
/* 103 */     if (localServant == null) {
/* 104 */       throw new ObjectNotActive();
/*     */     }
/* 106 */     return localServant;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAPolicyMediatorImpl_R_AOM
 * JD-Core Version:    0.6.2
 */