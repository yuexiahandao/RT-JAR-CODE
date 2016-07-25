/*    */ package com.sun.corba.se.spi.oa;
/*    */ 
/*    */ import com.sun.corba.se.impl.oa.poa.POAFactory;
/*    */ import com.sun.corba.se.impl.oa.toa.TOAFactory;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ 
/*    */ public class OADefault
/*    */ {
/*    */   public static ObjectAdapterFactory makePOAFactory(ORB paramORB)
/*    */   {
/* 39 */     POAFactory localPOAFactory = new POAFactory();
/* 40 */     localPOAFactory.init(paramORB);
/* 41 */     return localPOAFactory;
/*    */   }
/*    */ 
/*    */   public static ObjectAdapterFactory makeTOAFactory(ORB paramORB)
/*    */   {
/* 46 */     TOAFactory localTOAFactory = new TOAFactory();
/* 47 */     localTOAFactory.init(paramORB);
/* 48 */     return localTOAFactory;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.oa.OADefault
 * JD-Core Version:    0.6.2
 */