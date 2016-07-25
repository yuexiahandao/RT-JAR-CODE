/*     */ package com.sun.corba.se.impl.naming.pcosnaming;
/*     */ 
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.File;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CosNaming.NamingContext;
/*     */ import org.omg.CosNaming.NamingContextHelper;
/*     */ import org.omg.PortableServer.IdAssignmentPolicyValue;
/*     */ import org.omg.PortableServer.LifespanPolicyValue;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.POAManager;
/*     */ import org.omg.PortableServer.POAPackage.WrongAdapter;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.RequestProcessingPolicyValue;
/*     */ import org.omg.PortableServer.ServantRetentionPolicyValue;
/*     */ 
/*     */ public class NameService
/*     */ {
/*  52 */   private NamingContext rootContext = null;
/*  53 */   private POA nsPOA = null;
/*     */   private ServantManagerImpl contextMgr;
/*     */   private ORB theorb;
/*     */ 
/*     */   public NameService(ORB paramORB, File paramFile)
/*     */     throws Exception
/*     */   {
/*  66 */     this.theorb = paramORB;
/*     */ 
/*  78 */     POA localPOA = (POA)paramORB.resolve_initial_references("RootPOA");
/*     */ 
/*  80 */     localPOA.the_POAManager().activate();
/*     */ 
/*  87 */     int i = 0;
/*  88 */     Policy[] arrayOfPolicy = new Policy[4];
/*  89 */     arrayOfPolicy[(i++)] = localPOA.create_lifespan_policy(LifespanPolicyValue.PERSISTENT);
/*     */ 
/*  91 */     arrayOfPolicy[(i++)] = localPOA.create_request_processing_policy(RequestProcessingPolicyValue.USE_SERVANT_MANAGER);
/*     */ 
/*  93 */     arrayOfPolicy[(i++)] = localPOA.create_id_assignment_policy(IdAssignmentPolicyValue.USER_ID);
/*     */ 
/*  95 */     arrayOfPolicy[(i++)] = localPOA.create_servant_retention_policy(ServantRetentionPolicyValue.NON_RETAIN);
/*     */ 
/*  99 */     this.nsPOA = localPOA.create_POA("NameService", null, arrayOfPolicy);
/* 100 */     this.nsPOA.the_POAManager().activate();
/*     */ 
/* 103 */     this.contextMgr = new ServantManagerImpl(paramORB, paramFile, this);
/*     */ 
/* 107 */     String str = ServantManagerImpl.getRootObjectKey();
/*     */ 
/* 109 */     NamingContextImpl localNamingContextImpl = new NamingContextImpl(paramORB, str, this, this.contextMgr);
/*     */ 
/* 111 */     localNamingContextImpl = this.contextMgr.addContext(str, localNamingContextImpl);
/* 112 */     localNamingContextImpl.setServantManagerImpl(this.contextMgr);
/* 113 */     localNamingContextImpl.setORB(paramORB);
/* 114 */     localNamingContextImpl.setRootNameService(this);
/*     */ 
/* 116 */     this.nsPOA.set_servant_manager(this.contextMgr);
/* 117 */     this.rootContext = NamingContextHelper.narrow(this.nsPOA.create_reference_with_id(str.getBytes(), NamingContextHelper.id()));
/*     */   }
/*     */ 
/*     */   public NamingContext initialNamingContext()
/*     */   {
/* 127 */     return this.rootContext;
/*     */   }
/*     */ 
/*     */   POA getNSPOA()
/*     */   {
/* 135 */     return this.nsPOA;
/*     */   }
/*     */ 
/*     */   public NamingContext NewContext()
/*     */     throws SystemException
/*     */   {
/*     */     try
/*     */     {
/* 151 */       String str = this.contextMgr.getNewObjectKey();
/*     */ 
/* 155 */       java.lang.Object localObject = new NamingContextImpl(this.theorb, str, this, this.contextMgr);
/*     */ 
/* 158 */       NamingContextImpl localNamingContextImpl = this.contextMgr.addContext(str, (NamingContextImpl)localObject);
/*     */ 
/* 160 */       if (localNamingContextImpl != null)
/*     */       {
/* 162 */         localObject = localNamingContextImpl;
/*     */       }
/*     */ 
/* 166 */       ((NamingContextImpl)localObject).setServantManagerImpl(this.contextMgr);
/* 167 */       ((NamingContextImpl)localObject).setORB(this.theorb);
/* 168 */       ((NamingContextImpl)localObject).setRootNameService(this);
/* 169 */       return NamingContextHelper.narrow(this.nsPOA.create_reference_with_id(str.getBytes(), NamingContextHelper.id()));
/*     */     }
/*     */     catch (SystemException localSystemException)
/*     */     {
/* 177 */       throw localSystemException;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 183 */     return null;
/*     */   }
/*     */ 
/*     */   org.omg.CORBA.Object getObjectReferenceFromKey(String paramString)
/*     */   {
/* 193 */     org.omg.CORBA.Object localObject = null;
/*     */     try
/*     */     {
/* 196 */       localObject = this.nsPOA.create_reference_with_id(paramString.getBytes(), NamingContextHelper.id());
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 200 */       localObject = null;
/*     */     }
/* 202 */     return localObject;
/*     */   }
/*     */ 
/*     */   String getObjectKey(org.omg.CORBA.Object paramObject)
/*     */   {
/*     */     byte[] arrayOfByte;
/*     */     try
/*     */     {
/* 215 */       arrayOfByte = this.nsPOA.reference_to_id(paramObject);
/*     */     }
/*     */     catch (WrongAdapter localWrongAdapter)
/*     */     {
/* 219 */       return null;
/*     */     }
/*     */     catch (WrongPolicy localWrongPolicy)
/*     */     {
/* 223 */       return null;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 227 */       return null;
/*     */     }
/* 229 */     String str = new String(arrayOfByte);
/* 230 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.pcosnaming.NameService
 * JD-Core Version:    0.6.2
 */