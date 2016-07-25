/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.ior.TaggedProfile;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*     */ import org.omg.CORBA.portable.ServantObject;
/*     */ 
/*     */ public abstract class LocalClientRequestDispatcherBase
/*     */   implements LocalClientRequestDispatcher
/*     */ {
/*     */   protected ORB orb;
/*     */   int scid;
/*     */   protected boolean servantIsLocal;
/*     */   protected ObjectAdapterFactory oaf;
/*     */   protected ObjectAdapterId oaid;
/*     */   protected byte[] objectId;
/*  55 */   private static final ThreadLocal isNextCallValid = new ThreadLocal() {
/*     */     protected synchronized java.lang.Object initialValue() {
/*  57 */       return Boolean.TRUE;
/*     */     }
/*  55 */   };
/*     */ 
/*     */   protected LocalClientRequestDispatcherBase(ORB paramORB, int paramInt, IOR paramIOR)
/*     */   {
/*  63 */     this.orb = paramORB;
/*     */ 
/*  65 */     IIOPProfile localIIOPProfile = paramIOR.getProfile();
/*  66 */     this.servantIsLocal = ((paramORB.getORBData().isLocalOptimizationAllowed()) && (localIIOPProfile.isLocal()));
/*     */ 
/*  69 */     ObjectKeyTemplate localObjectKeyTemplate = localIIOPProfile.getObjectKeyTemplate();
/*  70 */     this.scid = localObjectKeyTemplate.getSubcontractId();
/*  71 */     RequestDispatcherRegistry localRequestDispatcherRegistry = paramORB.getRequestDispatcherRegistry();
/*  72 */     this.oaf = localRequestDispatcherRegistry.getObjectAdapterFactory(paramInt);
/*  73 */     this.oaid = localObjectKeyTemplate.getObjectAdapterId();
/*  74 */     ObjectId localObjectId = localIIOPProfile.getObjectId();
/*  75 */     this.objectId = localObjectId.getId();
/*     */   }
/*     */ 
/*     */   public byte[] getObjectId()
/*     */   {
/*  80 */     return this.objectId;
/*     */   }
/*     */ 
/*     */   public boolean is_local(org.omg.CORBA.Object paramObject)
/*     */   {
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean useLocalInvocation(org.omg.CORBA.Object paramObject)
/*     */   {
/* 105 */     if (isNextCallValid.get() == Boolean.TRUE) {
/* 106 */       return this.servantIsLocal;
/*     */     }
/* 108 */     isNextCallValid.set(Boolean.TRUE);
/*     */ 
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean checkForCompatibleServant(ServantObject paramServantObject, Class paramClass)
/*     */   {
/* 120 */     if (paramServantObject == null) {
/* 121 */       return false;
/*     */     }
/*     */ 
/* 126 */     if (!paramClass.isInstance(paramServantObject.servant)) {
/* 127 */       isNextCallValid.set(Boolean.FALSE);
/*     */ 
/* 132 */       return false;
/*     */     }
/*     */ 
/* 135 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.LocalClientRequestDispatcherBase
 * JD-Core Version:    0.6.2
 */