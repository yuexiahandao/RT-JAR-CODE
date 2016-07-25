/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import org.omg.CORBA.Object;
/*    */ import org.omg.CORBA.portable.ServantObject;
/*    */ 
/*    */ public class MinimalServantCacheLocalCRDImpl extends ServantCacheLocalCRDBase
/*    */ {
/*    */   public MinimalServantCacheLocalCRDImpl(ORB paramORB, int paramInt, IOR paramIOR)
/*    */   {
/* 44 */     super(paramORB, paramInt, paramIOR);
/*    */   }
/*    */ 
/*    */   public ServantObject servant_preinvoke(Object paramObject, String paramString, Class paramClass)
/*    */   {
/* 50 */     OAInvocationInfo localOAInvocationInfo = getCachedInfo();
/* 51 */     if (checkForCompatibleServant(localOAInvocationInfo, paramClass)) {
/* 52 */       return localOAInvocationInfo;
/*    */     }
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */   public void servant_postinvoke(Object paramObject, ServantObject paramServantObject)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.MinimalServantCacheLocalCRDImpl
 * JD-Core Version:    0.6.2
 */