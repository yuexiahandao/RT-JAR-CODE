/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.impl.oa.NullServantImpl;
/*     */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.PortableServer.ForwardRequest;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.POAPackage.NoServant;
/*     */ import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
/*     */ import org.omg.PortableServer.POAPackage.ObjectNotActive;
/*     */ import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
/*     */ import org.omg.PortableServer.POAPackage.ServantNotActive;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ import org.omg.PortableServer.ServantLocator;
/*     */ import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;
/*     */ import org.omg.PortableServer.ServantManager;
/*     */ 
/*     */ public class POAPolicyMediatorImpl_NR_USM extends POAPolicyMediatorBase
/*     */ {
/*     */   private ServantLocator locator;
/*     */ 
/*     */   POAPolicyMediatorImpl_NR_USM(Policies paramPolicies, POAImpl paramPOAImpl)
/*     */   {
/*  58 */     super(paramPolicies, paramPOAImpl);
/*     */ 
/*  61 */     if (paramPolicies.retainServants()) {
/*  62 */       throw paramPOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
/*     */     }
/*  64 */     if (!paramPolicies.useServantManager()) {
/*  65 */       throw paramPOAImpl.invocationWrapper().policyMediatorBadPolicyInFactory();
/*     */     }
/*  67 */     this.locator = null;
/*     */   }
/*     */ 
/*     */   protected Object internalGetServant(byte[] paramArrayOfByte, String paramString)
/*     */     throws ForwardRequest
/*     */   {
/*  73 */     if (this.locator == null) {
/*  74 */       throw this.poa.invocationWrapper().poaNoServantManager();
/*     */     }
/*  76 */     CookieHolder localCookieHolder = this.orb.peekInvocationInfo().getCookieHolder();
/*     */     Object localObject1;
/*     */     try
/*     */     {
/*  81 */       this.poa.unlock();
/*     */ 
/*  83 */       localObject1 = this.locator.preinvoke(paramArrayOfByte, this.poa, paramString, localCookieHolder);
/*  84 */       if (localObject1 == null)
/*  85 */         localObject1 = new NullServantImpl(this.poa.omgInvocationWrapper().nullServantReturned());
/*     */       else
/*  87 */         setDelegate((Servant)localObject1, paramArrayOfByte);
/*     */     } finally {
/*  89 */       this.poa.lock();
/*     */     }
/*     */ 
/*  92 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public void returnServant()
/*     */   {
/*  97 */     OAInvocationInfo localOAInvocationInfo = this.orb.peekInvocationInfo();
/*  98 */     if (this.locator == null)
/*  99 */       return;
/*     */     try
/*     */     {
/* 102 */       this.poa.unlock();
/* 103 */       this.locator.postinvoke(localOAInvocationInfo.id(), (POA)localOAInvocationInfo.oa(), localOAInvocationInfo.getOperation(), localOAInvocationInfo.getCookieHolder().value, (Servant)localOAInvocationInfo.getServantContainer());
/*     */     }
/*     */     finally
/*     */     {
/* 107 */       this.poa.lock();
/*     */     }
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
/* 123 */     return this.locator;
/*     */   }
/*     */ 
/*     */   public void setServantManager(ServantManager paramServantManager) throws WrongPolicy
/*     */   {
/* 128 */     if (this.locator != null) {
/* 129 */       throw this.poa.invocationWrapper().servantManagerAlreadySet();
/*     */     }
/* 131 */     if ((paramServantManager instanceof ServantLocator))
/* 132 */       this.locator = ((ServantLocator)paramServantManager);
/*     */     else
/* 134 */       throw this.poa.invocationWrapper().servantManagerBadType();
/*     */   }
/*     */ 
/*     */   public Servant getDefaultServant() throws NoServant, WrongPolicy
/*     */   {
/* 139 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public void setDefaultServant(Servant paramServant) throws WrongPolicy
/*     */   {
/* 144 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public final void activateObject(byte[] paramArrayOfByte, Servant paramServant)
/*     */     throws WrongPolicy, ServantAlreadyActive, ObjectAlreadyActive
/*     */   {
/* 150 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public Servant deactivateObject(byte[] paramArrayOfByte) throws ObjectNotActive, WrongPolicy
/*     */   {
/* 155 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public byte[] servantToId(Servant paramServant) throws ServantNotActive, WrongPolicy
/*     */   {
/* 160 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   public Servant idToServant(byte[] paramArrayOfByte)
/*     */     throws WrongPolicy, ObjectNotActive
/*     */   {
/* 166 */     throw new WrongPolicy();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAPolicyMediatorImpl_NR_USM
 * JD-Core Version:    0.6.2
 */