/*     */ package com.sun.corba.se.spi.oa;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.impl.oa.poa.Policies;
/*     */ import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.IORTemplate;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.PIHandler;
/*     */ import com.sun.corba.se.spi.transport.CorbaTransportManager;
/*     */ import org.omg.CORBA.LocalObject;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceFactory;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceTemplate;
/*     */ 
/*     */ public abstract class ObjectAdapterBase extends LocalObject
/*     */   implements ObjectAdapter
/*     */ {
/*     */   private ORB orb;
/*     */   private final POASystemException _iorWrapper;
/*     */   private final POASystemException _invocationWrapper;
/*     */   private final POASystemException _lifecycleWrapper;
/*     */   private final OMGSystemException _omgInvocationWrapper;
/*     */   private final OMGSystemException _omgLifecycleWrapper;
/*     */   private IORTemplate iortemp;
/*     */   private byte[] adapterId;
/*     */   private ObjectReferenceTemplate adapterTemplate;
/*     */   private ObjectReferenceFactory currentFactory;
/*     */ 
/*     */   public ObjectAdapterBase(ORB paramORB)
/*     */   {
/*  70 */     this.orb = paramORB;
/*  71 */     this._iorWrapper = POASystemException.get(paramORB, "oa.ior");
/*     */ 
/*  73 */     this._lifecycleWrapper = POASystemException.get(paramORB, "oa.lifecycle");
/*     */ 
/*  75 */     this._omgLifecycleWrapper = OMGSystemException.get(paramORB, "oa.lifecycle");
/*     */ 
/*  77 */     this._invocationWrapper = POASystemException.get(paramORB, "oa.invocation");
/*     */ 
/*  79 */     this._omgInvocationWrapper = OMGSystemException.get(paramORB, "oa.invocation");
/*     */   }
/*     */ 
/*     */   public final POASystemException iorWrapper()
/*     */   {
/*  85 */     return this._iorWrapper;
/*     */   }
/*     */ 
/*     */   public final POASystemException lifecycleWrapper()
/*     */   {
/*  90 */     return this._lifecycleWrapper;
/*     */   }
/*     */ 
/*     */   public final OMGSystemException omgLifecycleWrapper()
/*     */   {
/*  95 */     return this._omgLifecycleWrapper;
/*     */   }
/*     */ 
/*     */   public final POASystemException invocationWrapper()
/*     */   {
/* 100 */     return this._invocationWrapper;
/*     */   }
/*     */ 
/*     */   public final OMGSystemException omgInvocationWrapper()
/*     */   {
/* 105 */     return this._omgInvocationWrapper;
/*     */   }
/*     */ 
/*     */   public final void initializeTemplate(ObjectKeyTemplate paramObjectKeyTemplate, boolean paramBoolean, Policies paramPolicies, String paramString1, String paramString2, ObjectAdapterId paramObjectAdapterId)
/*     */   {
/* 116 */     this.adapterId = paramObjectKeyTemplate.getAdapterId();
/*     */ 
/* 118 */     this.iortemp = IORFactories.makeIORTemplate(paramObjectKeyTemplate);
/*     */ 
/* 122 */     this.orb.getCorbaTransportManager().addToIORTemplate(this.iortemp, paramPolicies, paramString1, paramString2, paramObjectAdapterId);
/*     */ 
/* 126 */     this.adapterTemplate = IORFactories.makeObjectReferenceTemplate(this.orb, this.iortemp);
/*     */ 
/* 128 */     this.currentFactory = this.adapterTemplate;
/*     */ 
/* 130 */     if (paramBoolean) {
/* 131 */       PIHandler localPIHandler = this.orb.getPIHandler();
/* 132 */       if (localPIHandler != null)
/*     */       {
/* 134 */         localPIHandler.objectAdapterCreated(this);
/*     */       }
/*     */     }
/* 137 */     this.iortemp.makeImmutable();
/*     */   }
/*     */ 
/*     */   public final org.omg.CORBA.Object makeObject(String paramString, byte[] paramArrayOfByte)
/*     */   {
/* 142 */     return this.currentFactory.make_object(paramString, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public final byte[] getAdapterId()
/*     */   {
/* 147 */     return this.adapterId;
/*     */   }
/*     */ 
/*     */   public final ORB getORB()
/*     */   {
/* 152 */     return this.orb;
/*     */   }
/*     */ 
/*     */   public abstract Policy getEffectivePolicy(int paramInt);
/*     */ 
/*     */   public final IORTemplate getIORTemplate()
/*     */   {
/* 159 */     return this.iortemp;
/*     */   }
/*     */ 
/*     */   public abstract int getManagerId();
/*     */ 
/*     */   public abstract short getState();
/*     */ 
/*     */   public final ObjectReferenceTemplate getAdapterTemplate()
/*     */   {
/* 168 */     return this.adapterTemplate;
/*     */   }
/*     */ 
/*     */   public final ObjectReferenceFactory getCurrentFactory()
/*     */   {
/* 173 */     return this.currentFactory;
/*     */   }
/*     */ 
/*     */   public final void setCurrentFactory(ObjectReferenceFactory paramObjectReferenceFactory)
/*     */   {
/* 178 */     this.currentFactory = paramObjectReferenceFactory;
/*     */   }
/*     */ 
/*     */   public abstract org.omg.CORBA.Object getLocalServant(byte[] paramArrayOfByte);
/*     */ 
/*     */   public abstract void getInvocationServant(OAInvocationInfo paramOAInvocationInfo);
/*     */ 
/*     */   public abstract void returnServant();
/*     */ 
/*     */   public abstract void enter()
/*     */     throws OADestroyed;
/*     */ 
/*     */   public abstract void exit();
/*     */ 
/*     */   protected abstract ObjectCopierFactory getObjectCopierFactory();
/*     */ 
/*     */   public OAInvocationInfo makeInvocationInfo(byte[] paramArrayOfByte)
/*     */   {
/* 197 */     OAInvocationInfo localOAInvocationInfo = new OAInvocationInfo(this, paramArrayOfByte);
/* 198 */     localOAInvocationInfo.setCopierFactory(getObjectCopierFactory());
/* 199 */     return localOAInvocationInfo;
/*     */   }
/*     */ 
/*     */   public abstract String[] getInterfaces(java.lang.Object paramObject, byte[] paramArrayOfByte);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.oa.ObjectAdapterBase
 * JD-Core Version:    0.6.2
 */