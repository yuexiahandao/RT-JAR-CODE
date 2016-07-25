/*    */ package com.sun.corba.se.impl.oa.poa;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.POASystemException;
/*    */ 
/*    */ abstract class POAPolicyMediatorFactory
/*    */ {
/*    */   static POAPolicyMediator create(Policies paramPolicies, POAImpl paramPOAImpl)
/*    */   {
/* 34 */     if (paramPolicies.retainServants()) {
/* 35 */       if (paramPolicies.useActiveMapOnly())
/* 36 */         return new POAPolicyMediatorImpl_R_AOM(paramPolicies, paramPOAImpl);
/* 37 */       if (paramPolicies.useDefaultServant())
/* 38 */         return new POAPolicyMediatorImpl_R_UDS(paramPolicies, paramPOAImpl);
/* 39 */       if (paramPolicies.useServantManager()) {
/* 40 */         return new POAPolicyMediatorImpl_R_USM(paramPolicies, paramPOAImpl);
/*    */       }
/* 42 */       throw paramPOAImpl.invocationWrapper().pmfCreateRetain();
/*    */     }
/* 44 */     if (paramPolicies.useDefaultServant())
/* 45 */       return new POAPolicyMediatorImpl_NR_UDS(paramPolicies, paramPOAImpl);
/* 46 */     if (paramPolicies.useServantManager()) {
/* 47 */       return new POAPolicyMediatorImpl_NR_USM(paramPolicies, paramPOAImpl);
/*    */     }
/* 49 */     throw paramPOAImpl.invocationWrapper().pmfCreateNonRetain();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.POAPolicyMediatorFactory
 * JD-Core Version:    0.6.2
 */