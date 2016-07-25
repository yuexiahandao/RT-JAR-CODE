/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import org.omg.PortableServer.ForwardRequest;
/*     */ import org.omg.PortableServer.POAPackage.NoServant;
/*     */ import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
/*     */ import org.omg.PortableServer.POAPackage.ObjectNotActive;
/*     */ import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
/*     */ import org.omg.PortableServer.POAPackage.ServantNotActive;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ import org.omg.PortableServer.ServantManager;
/*     */ 
/*     */ public class POAPolicyMediatorImpl_NR_UDS extends POAPolicyMediatorBase
/*     */ {
/*     */   private Servant defaultServant;
/*     */ 
/*     */   POAPolicyMediatorImpl_NR_UDS(Policies paramPolicies, POAImpl paramPOAImpl)
/*     */   {
/*  52 */     super(paramPolicies, paramPOAImpl);
/*     */ 
/*  55 */     if (paramPolicies.retainServants()) {
/*  56 */       throw paramPOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
/*     */     }
/*  58 */     if (!paramPolicies.useDefaultServant()) {
/*  59 */       throw paramPOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
/*     */     }
/*  61 */     this.defaultServant = null;
/*     */   }
/*     */ 
/*     */   protected Object internalGetServant(byte[] paramArrayOfByte, String paramString)
/*     */     throws ForwardRequest
/*     */   {
/*  67 */     if (this.defaultServant == null) {
/*  68 */       throw this.poa.invocationWrapper().poaNoDefaultServant();
/*     */     }
/*  70 */     return this.defaultServant;
/*     */   }
/*     */ 
/*     */   public void returnServant()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void etherealizeAll()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void clearAOM()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ServantManager getServantManager()
/*     */     throws WrongPolicy
/*     */   {
/*  90 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public void setServantManager(ServantManager paramServantManager) throws WrongPolicy
/*     */   {
/*  95 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public Servant getDefaultServant() throws NoServant, WrongPolicy
/*     */   {
/* 100 */     if (this.defaultServant == null)
/* 101 */       throw new NoServant();
/* 102 */     return this.defaultServant;
/*     */   }
/*     */ 
/*     */   public void setDefaultServant(Servant paramServant) throws WrongPolicy
/*     */   {
/* 107 */     this.defaultServant = paramServant;
/* 108 */     setDelegate(this.defaultServant, "DefaultServant".getBytes());
/*     */   }
/*     */ 
/*     */   public final void activateObject(byte[] paramArrayOfByte, Servant paramServant)
/*     */     throws WrongPolicy, ServantAlreadyActive, ObjectAlreadyActive
/*     */   {
/* 114 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public Servant deactivateObject(byte[] paramArrayOfByte) throws ObjectNotActive, WrongPolicy
/*     */   {
/* 119 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public byte[] servantToId(Servant paramServant) throws ServantNotActive, WrongPolicy
/*     */   {
/* 124 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public Servant idToServant(byte[] paramArrayOfByte)
/*     */     throws WrongPolicy, ObjectNotActive
/*     */   {
/* 130 */     if (this.defaultServant != null) {
/* 131 */       return this.defaultServant;
/*     */     }
/* 133 */     throw new ObjectNotActive();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAPolicyMediatorImpl_NR_UDS
 * JD-Core Version:    0.6.2
 */