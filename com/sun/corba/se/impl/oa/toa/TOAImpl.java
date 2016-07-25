/*     */ package com.sun.corba.se.impl.oa.toa;
/*     */ 
/*     */ import com.sun.corba.se.impl.ior.JIDLObjectKeyTemplate;
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.impl.oa.NullServantImpl;
/*     */ import com.sun.corba.se.impl.oa.poa.Policies;
/*     */ import com.sun.corba.se.impl.protocol.JIDLLocalCRDImpl;
/*     */ import com.sun.corba.se.pept.protocol.ClientDelegate;
/*     */ import com.sun.corba.se.spi.copyobject.CopierManager;
/*     */ import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.oa.OADestroyed;
/*     */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapterBase;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceFactory;
/*     */ 
/*     */ public class TOAImpl extends ObjectAdapterBase
/*     */   implements TOA
/*     */ {
/*     */   private TransientObjectManager servants;
/*     */ 
/*     */   public TOAImpl(ORB paramORB, TransientObjectManager paramTransientObjectManager, String paramString)
/*     */   {
/*  83 */     super(paramORB);
/*  84 */     this.servants = paramTransientObjectManager;
/*     */ 
/*  87 */     int i = getORB().getTransientServerId();
/*  88 */     int j = 2;
/*     */ 
/*  90 */     JIDLObjectKeyTemplate localJIDLObjectKeyTemplate = new JIDLObjectKeyTemplate(paramORB, j, i);
/*     */ 
/*  93 */     Policies localPolicies = Policies.defaultPolicies;
/*     */ 
/*  96 */     initializeTemplate(localJIDLObjectKeyTemplate, true, localPolicies, paramString, null, localJIDLObjectKeyTemplate.getObjectAdapterId());
/*     */   }
/*     */ 
/*     */   public ObjectCopierFactory getObjectCopierFactory()
/*     */   {
/* 108 */     CopierManager localCopierManager = getORB().getCopierManager();
/* 109 */     return localCopierManager.getDefaultObjectCopierFactory();
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object getLocalServant(byte[] paramArrayOfByte)
/*     */   {
/* 114 */     return (org.omg.CORBA.Object)this.servants.lookupServant(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void getInvocationServant(OAInvocationInfo paramOAInvocationInfo)
/*     */   {
/* 125 */     java.lang.Object localObject = this.servants.lookupServant(paramOAInvocationInfo.id());
/* 126 */     if (localObject == null)
/*     */     {
/* 129 */       localObject = new NullServantImpl(lifecycleWrapper().nullServant());
/* 130 */     }paramOAInvocationInfo.setServant(localObject);
/*     */   }
/*     */ 
/*     */   public void returnServant()
/*     */   {
/*     */   }
/*     */ 
/*     */   public String[] getInterfaces(java.lang.Object paramObject, byte[] paramArrayOfByte)
/*     */   {
/* 142 */     return StubAdapter.getTypeIds(paramObject);
/*     */   }
/*     */ 
/*     */   public Policy getEffectivePolicy(int paramInt)
/*     */   {
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   public int getManagerId()
/*     */   {
/* 156 */     return -1;
/*     */   }
/*     */ 
/*     */   public short getState()
/*     */   {
/* 161 */     return 1;
/*     */   }
/*     */ 
/*     */   public void enter()
/*     */     throws OADestroyed
/*     */   {
/*     */   }
/*     */ 
/*     */   public void exit()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void connect(org.omg.CORBA.Object paramObject)
/*     */   {
/* 178 */     byte[] arrayOfByte = this.servants.storeServant(paramObject, null);
/*     */ 
/* 181 */     String str = StubAdapter.getTypeIds(paramObject)[0];
/*     */ 
/* 184 */     ObjectReferenceFactory localObjectReferenceFactory = getCurrentFactory();
/* 185 */     org.omg.CORBA.Object localObject = localObjectReferenceFactory.make_object(str, arrayOfByte);
/*     */ 
/* 190 */     Delegate localDelegate = StubAdapter.getDelegate(localObject);
/*     */ 
/* 192 */     CorbaContactInfoList localCorbaContactInfoList = (CorbaContactInfoList)((ClientDelegate)localDelegate).getContactInfoList();
/*     */ 
/* 194 */     LocalClientRequestDispatcher localLocalClientRequestDispatcher = localCorbaContactInfoList.getLocalClientRequestDispatcher();
/*     */ 
/* 197 */     if ((localLocalClientRequestDispatcher instanceof JIDLLocalCRDImpl)) {
/* 198 */       JIDLLocalCRDImpl localJIDLLocalCRDImpl = (JIDLLocalCRDImpl)localLocalClientRequestDispatcher;
/* 199 */       localJIDLLocalCRDImpl.setServant(paramObject);
/*     */     } else {
/* 201 */       throw new RuntimeException("TOAImpl.connect can not be called on " + localLocalClientRequestDispatcher);
/*     */     }
/*     */ 
/* 205 */     StubAdapter.setDelegate(paramObject, localDelegate);
/*     */   }
/*     */ 
/*     */   public void disconnect(org.omg.CORBA.Object paramObject)
/*     */   {
/* 211 */     Delegate localDelegate = StubAdapter.getDelegate(paramObject);
/*     */ 
/* 213 */     CorbaContactInfoList localCorbaContactInfoList = (CorbaContactInfoList)((ClientDelegate)localDelegate).getContactInfoList();
/*     */ 
/* 215 */     LocalClientRequestDispatcher localLocalClientRequestDispatcher = localCorbaContactInfoList.getLocalClientRequestDispatcher();
/*     */ 
/* 218 */     if ((localLocalClientRequestDispatcher instanceof JIDLLocalCRDImpl)) {
/* 219 */       JIDLLocalCRDImpl localJIDLLocalCRDImpl = (JIDLLocalCRDImpl)localLocalClientRequestDispatcher;
/* 220 */       byte[] arrayOfByte = localJIDLLocalCRDImpl.getObjectId();
/* 221 */       this.servants.deleteServant(arrayOfByte);
/* 222 */       localJIDLLocalCRDImpl.unexport();
/*     */     } else {
/* 224 */       throw new RuntimeException("TOAImpl.disconnect can not be called on " + localLocalClientRequestDispatcher);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.toa.TOAImpl
 * JD-Core Version:    0.6.2
 */