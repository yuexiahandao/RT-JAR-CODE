/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import org.omg.PortableServer.ForwardRequest;
/*     */ import org.omg.PortableServer.POAPackage.NoServant;
/*     */ import org.omg.PortableServer.POAPackage.ObjectNotActive;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ import org.omg.PortableServer.ServantManager;
/*     */ 
/*     */ public class POAPolicyMediatorImpl_R_UDS extends POAPolicyMediatorBase_R
/*     */ {
/*     */   private Servant defaultServant;
/*     */ 
/*     */   POAPolicyMediatorImpl_R_UDS(Policies paramPolicies, POAImpl paramPOAImpl)
/*     */   {
/*  53 */     super(paramPolicies, paramPOAImpl);
/*  54 */     this.defaultServant = null;
/*     */ 
/*  57 */     if (!paramPolicies.useDefaultServant())
/*  58 */       throw paramPOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
/*     */   }
/*     */ 
/*     */   protected Object internalGetServant(byte[] paramArrayOfByte, String paramString)
/*     */     throws ForwardRequest
/*     */   {
/*  64 */     Servant localServant = internalIdToServant(paramArrayOfByte);
/*  65 */     if (localServant == null) {
/*  66 */       localServant = this.defaultServant;
/*     */     }
/*  68 */     if (localServant == null) {
/*  69 */       throw this.poa.invocationWrapper().poaNoDefaultServant();
/*     */     }
/*  71 */     return localServant;
/*     */   }
/*     */ 
/*     */   public void etherealizeAll()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ServantManager getServantManager()
/*     */     throws WrongPolicy
/*     */   {
/*  81 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public void setServantManager(ServantManager paramServantManager) throws WrongPolicy
/*     */   {
/*  86 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public Servant getDefaultServant() throws NoServant, WrongPolicy
/*     */   {
/*  91 */     if (this.defaultServant == null) {
/*  92 */       throw new NoServant();
/*     */     }
/*  94 */     return this.defaultServant;
/*     */   }
/*     */ 
/*     */   public void setDefaultServant(Servant paramServant) throws WrongPolicy
/*     */   {
/*  99 */     this.defaultServant = paramServant;
/* 100 */     setDelegate(this.defaultServant, "DefaultServant".getBytes());
/*     */   }
/*     */ 
/*     */   public Servant idToServant(byte[] paramArrayOfByte)
/*     */     throws WrongPolicy, ObjectNotActive
/*     */   {
/* 106 */     ActiveObjectMap.Key localKey = new ActiveObjectMap.Key(paramArrayOfByte);
/* 107 */     Servant localServant = internalKeyToServant(localKey);
/*     */ 
/* 109 */     if ((localServant == null) && 
/* 110 */       (this.defaultServant != null)) {
/* 111 */       localServant = this.defaultServant;
/*     */     }
/* 113 */     if (localServant == null) {
/* 114 */       throw new ObjectNotActive();
/*     */     }
/* 116 */     return localServant;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAPolicyMediatorImpl_R_UDS
 * JD-Core Version:    0.6.2
 */