/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.POASystemException;
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.oa.OADestroyed;
/*    */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*    */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*    */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.protocol.ForwardException;
/*    */ 
/*    */ public abstract class ServantCacheLocalCRDBase extends LocalClientRequestDispatcherBase
/*    */ {
/*    */   private OAInvocationInfo cachedInfo;
/*    */   protected POASystemException wrapper;
/*    */ 
/*    */   protected ServantCacheLocalCRDBase(ORB paramORB, int paramInt, IOR paramIOR)
/*    */   {
/* 59 */     super(paramORB, paramInt, paramIOR);
/* 60 */     this.wrapper = POASystemException.get(paramORB, "rpc.protocol");
/*    */   }
/*    */ 
/*    */   protected synchronized OAInvocationInfo getCachedInfo()
/*    */   {
/* 66 */     if (!this.servantIsLocal) {
/* 67 */       throw this.wrapper.servantMustBeLocal();
/*    */     }
/* 69 */     if (this.cachedInfo == null) {
/* 70 */       ObjectAdapter localObjectAdapter = this.oaf.find(this.oaid);
/* 71 */       this.cachedInfo = localObjectAdapter.makeInvocationInfo(this.objectId);
/*    */ 
/* 74 */       this.orb.pushInvocationInfo(this.cachedInfo);
/*    */       try
/*    */       {
/* 77 */         localObjectAdapter.enter();
/* 78 */         localObjectAdapter.getInvocationServant(this.cachedInfo);
/*    */       } catch (ForwardException localForwardException) {
/* 80 */         throw this.wrapper.illegalForwardRequest(localForwardException);
/*    */       }
/*    */       catch (OADestroyed localOADestroyed)
/*    */       {
/* 84 */         throw this.wrapper.adapterDestroyed(localOADestroyed);
/*    */       } finally {
/* 86 */         localObjectAdapter.returnServant();
/* 87 */         localObjectAdapter.exit();
/* 88 */         this.orb.popInvocationInfo();
/*    */       }
/*    */     }
/*    */ 
/* 92 */     return this.cachedInfo;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.ServantCacheLocalCRDBase
 * JD-Core Version:    0.6.2
 */