/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import org.omg.CORBA.Object;
/*    */ import org.omg.CORBA.portable.ServantObject;
/*    */ 
/*    */ public class InfoOnlyServantCacheLocalCRDImpl extends ServantCacheLocalCRDBase
/*    */ {
/*    */   public InfoOnlyServantCacheLocalCRDImpl(ORB paramORB, int paramInt, IOR paramIOR)
/*    */   {
/* 44 */     super(paramORB, paramInt, paramIOR);
/*    */   }
/*    */ 
/*    */   public ServantObject servant_preinvoke(Object paramObject, String paramString, Class paramClass)
/*    */   {
/* 50 */     OAInvocationInfo localOAInvocationInfo1 = getCachedInfo();
/* 51 */     if (!checkForCompatibleServant(localOAInvocationInfo1, paramClass)) {
/* 52 */       return null;
/*    */     }
/*    */ 
/* 57 */     OAInvocationInfo localOAInvocationInfo2 = new OAInvocationInfo(localOAInvocationInfo1, paramString);
/* 58 */     this.orb.pushInvocationInfo(localOAInvocationInfo2);
/*    */ 
/* 60 */     return localOAInvocationInfo2;
/*    */   }
/*    */ 
/*    */   public void servant_postinvoke(Object paramObject, ServantObject paramServantObject)
/*    */   {
/* 66 */     this.orb.popInvocationInfo();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.InfoOnlyServantCacheLocalCRDImpl
 * JD-Core Version:    0.6.2
 */