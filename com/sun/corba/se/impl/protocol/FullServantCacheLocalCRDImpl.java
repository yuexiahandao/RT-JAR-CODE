/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.POASystemException;
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.oa.OADestroyed;
/*    */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*    */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import org.omg.CORBA.Object;
/*    */ import org.omg.CORBA.portable.ServantObject;
/*    */ 
/*    */ public class FullServantCacheLocalCRDImpl extends ServantCacheLocalCRDBase
/*    */ {
/*    */   public FullServantCacheLocalCRDImpl(ORB paramORB, int paramInt, IOR paramIOR)
/*    */   {
/* 49 */     super(paramORB, paramInt, paramIOR);
/*    */   }
/*    */ 
/*    */   public ServantObject servant_preinvoke(Object paramObject, String paramString, Class paramClass)
/*    */   {
/* 55 */     OAInvocationInfo localOAInvocationInfo1 = getCachedInfo();
/* 56 */     if (!checkForCompatibleServant(localOAInvocationInfo1, paramClass)) {
/* 57 */       return null;
/*    */     }
/*    */ 
/* 62 */     OAInvocationInfo localOAInvocationInfo2 = new OAInvocationInfo(localOAInvocationInfo1, paramString);
/* 63 */     this.orb.pushInvocationInfo(localOAInvocationInfo2);
/*    */     try
/*    */     {
/* 66 */       localOAInvocationInfo2.oa().enter();
/*    */     } catch (OADestroyed localOADestroyed) {
/* 68 */       throw this.wrapper.preinvokePoaDestroyed(localOADestroyed);
/*    */     }
/*    */ 
/* 71 */     return localOAInvocationInfo2;
/*    */   }
/*    */ 
/*    */   public void servant_postinvoke(Object paramObject, ServantObject paramServantObject)
/*    */   {
/* 77 */     OAInvocationInfo localOAInvocationInfo = getCachedInfo();
/* 78 */     localOAInvocationInfo.oa().exit();
/* 79 */     this.orb.popInvocationInfo();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.FullServantCacheLocalCRDImpl
 * JD-Core Version:    0.6.2
 */