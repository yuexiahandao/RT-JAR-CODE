/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.logging.POASystemException;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.oa.OADestroyed;
/*     */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.ForwardException;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.portable.ServantObject;
/*     */ 
/*     */ public class POALocalCRDImpl extends LocalClientRequestDispatcherBase
/*     */ {
/*     */   private ORBUtilSystemException wrapper;
/*     */   private POASystemException poaWrapper;
/*     */ 
/*     */   public POALocalCRDImpl(ORB paramORB, int paramInt, IOR paramIOR)
/*     */   {
/*  61 */     super(paramORB, paramInt, paramIOR);
/*  62 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/*  64 */     this.poaWrapper = POASystemException.get(paramORB, "rpc.protocol");
/*     */   }
/*     */ 
/*     */   private OAInvocationInfo servantEnter(ObjectAdapter paramObjectAdapter)
/*     */     throws OADestroyed
/*     */   {
/*  70 */     paramObjectAdapter.enter();
/*     */ 
/*  72 */     OAInvocationInfo localOAInvocationInfo = paramObjectAdapter.makeInvocationInfo(this.objectId);
/*  73 */     this.orb.pushInvocationInfo(localOAInvocationInfo);
/*     */ 
/*  75 */     return localOAInvocationInfo;
/*     */   }
/*     */ 
/*     */   private void servantExit(ObjectAdapter paramObjectAdapter)
/*     */   {
/*     */     try {
/*  81 */       paramObjectAdapter.returnServant();
/*     */     } finally {
/*  83 */       paramObjectAdapter.exit();
/*  84 */       this.orb.popInvocationInfo();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServantObject servant_preinvoke(org.omg.CORBA.Object paramObject, String paramString, Class paramClass)
/*     */   {
/*  99 */     ObjectAdapter localObjectAdapter = this.oaf.find(this.oaid);
/* 100 */     OAInvocationInfo localOAInvocationInfo = null;
/*     */     try
/*     */     {
/* 103 */       localOAInvocationInfo = servantEnter(localObjectAdapter);
/* 104 */       localOAInvocationInfo.setOperation(paramString);
/*     */     }
/*     */     catch (OADestroyed localOADestroyed)
/*     */     {
/* 108 */       return servant_preinvoke(paramObject, paramString, paramClass);
/*     */     }
/*     */     try
/*     */     {
/*     */       try {
/* 113 */         localObjectAdapter.getInvocationServant(localOAInvocationInfo);
/* 114 */         if (!checkForCompatibleServant(localOAInvocationInfo, paramClass))
/* 115 */           return null;
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 119 */         servantExit(localObjectAdapter);
/* 120 */         throw localThrowable1;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (ForwardException localForwardException)
/*     */     {
/* 129 */       RuntimeException localRuntimeException = new RuntimeException("deal with this.");
/* 130 */       localRuntimeException.initCause(localForwardException);
/* 131 */       throw localRuntimeException;
/*     */     }
/*     */     catch (ThreadDeath localThreadDeath)
/*     */     {
/* 139 */       throw this.wrapper.runtimeexception(localThreadDeath);
/*     */     } catch (Throwable localThrowable2) {
/* 141 */       if ((localThrowable2 instanceof SystemException)) {
/* 142 */         throw ((SystemException)localThrowable2);
/*     */       }
/* 144 */       throw this.poaWrapper.localServantLookup(localThrowable2);
/*     */     }
/*     */ 
/* 147 */     if (!checkForCompatibleServant(localOAInvocationInfo, paramClass)) {
/* 148 */       servantExit(localObjectAdapter);
/* 149 */       return null;
/*     */     }
/*     */ 
/* 152 */     return localOAInvocationInfo;
/*     */   }
/*     */ 
/*     */   public void servant_postinvoke(org.omg.CORBA.Object paramObject, ServantObject paramServantObject)
/*     */   {
/* 158 */     ObjectAdapter localObjectAdapter = this.orb.peekInvocationInfo().oa();
/* 159 */     servantExit(localObjectAdapter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.POALocalCRDImpl
 * JD-Core Version:    0.6.2
 */