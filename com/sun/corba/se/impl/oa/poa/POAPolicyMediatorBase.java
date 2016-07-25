/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBConstants;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import org.omg.PortableServer.ForwardRequest;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ 
/*     */ public abstract class POAPolicyMediatorBase
/*     */   implements POAPolicyMediator
/*     */ {
/*     */   protected POAImpl poa;
/*     */   protected ORB orb;
/*     */   private int sysIdCounter;
/*     */   private Policies policies;
/*     */   private DelegateImpl delegateImpl;
/*     */   private int serverid;
/*     */   private int scid;
/*     */   protected boolean isImplicit;
/*     */   protected boolean isUnique;
/*     */   protected boolean isSystemId;
/*     */ 
/*     */   public final Policies getPolicies()
/*     */   {
/*  64 */     return this.policies;
/*     */   }
/*     */ 
/*     */   public final int getScid()
/*     */   {
/*  69 */     return this.scid;
/*     */   }
/*     */ 
/*     */   public final int getServerId()
/*     */   {
/*  74 */     return this.serverid;
/*     */   }
/*     */ 
/*     */   POAPolicyMediatorBase(Policies paramPolicies, POAImpl paramPOAImpl)
/*     */   {
/*  79 */     if (paramPolicies.isSingleThreaded()) {
/*  80 */       throw paramPOAImpl.invocationWrapper().singleThreadNotSupported();
/*     */     }
/*  82 */     POAManagerImpl localPOAManagerImpl = (POAManagerImpl)paramPOAImpl.the_POAManager();
/*  83 */     POAFactory localPOAFactory = localPOAManagerImpl.getFactory();
/*  84 */     this.delegateImpl = ((DelegateImpl)localPOAFactory.getDelegateImpl());
/*  85 */     this.policies = paramPolicies;
/*  86 */     this.poa = paramPOAImpl;
/*  87 */     this.orb = paramPOAImpl.getORB();
/*     */ 
/*  89 */     switch (paramPolicies.servantCachingLevel()) {
/*     */     case 0:
/*  91 */       this.scid = 32;
/*  92 */       break;
/*     */     case 1:
/*  94 */       this.scid = 36;
/*  95 */       break;
/*     */     case 2:
/*  97 */       this.scid = 40;
/*  98 */       break;
/*     */     case 3:
/* 100 */       this.scid = 44;
/*     */     }
/*     */ 
/* 104 */     if (paramPolicies.isTransient()) {
/* 105 */       this.serverid = this.orb.getTransientServerId();
/*     */     } else {
/* 107 */       this.serverid = this.orb.getORBData().getPersistentServerId();
/* 108 */       this.scid = ORBConstants.makePersistent(this.scid);
/*     */     }
/*     */ 
/* 111 */     this.isImplicit = paramPolicies.isImplicitlyActivated();
/* 112 */     this.isUnique = paramPolicies.isUniqueIds();
/* 113 */     this.isSystemId = paramPolicies.isSystemAssignedIds();
/*     */ 
/* 115 */     this.sysIdCounter = 0;
/*     */   }
/*     */ 
/*     */   public final Object getInvocationServant(byte[] paramArrayOfByte, String paramString)
/*     */     throws ForwardRequest
/*     */   {
/* 121 */     Object localObject = internalGetServant(paramArrayOfByte, paramString);
/*     */ 
/* 123 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected final void setDelegate(Servant paramServant, byte[] paramArrayOfByte)
/*     */   {
/* 133 */     paramServant._set_delegate(this.delegateImpl);
/*     */   }
/*     */ 
/*     */   public synchronized byte[] newSystemId() throws WrongPolicy
/*     */   {
/* 138 */     if (!this.isSystemId) {
/* 139 */       throw new WrongPolicy();
/*     */     }
/* 141 */     byte[] arrayOfByte = new byte[8];
/* 142 */     ORBUtility.intToBytes(++this.sysIdCounter, arrayOfByte, 0);
/* 143 */     ORBUtility.intToBytes(this.poa.getPOAId(), arrayOfByte, 4);
/* 144 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   protected abstract Object internalGetServant(byte[] paramArrayOfByte, String paramString)
/*     */     throws ForwardRequest;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAPolicyMediatorBase
 * JD-Core Version:    0.6.2
 */