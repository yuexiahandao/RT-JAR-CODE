/*    */ package com.sun.corba.se.impl.protocol;
/*    */ 
/*    */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
/*    */ import org.omg.CORBA.portable.ServantObject;
/*    */ 
/*    */ public class NotLocalLocalCRDImpl
/*    */   implements LocalClientRequestDispatcher
/*    */ {
/*    */   public boolean useLocalInvocation(org.omg.CORBA.Object paramObject)
/*    */   {
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean is_local(org.omg.CORBA.Object paramObject)
/*    */   {
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   public ServantObject servant_preinvoke(org.omg.CORBA.Object paramObject, String paramString, Class paramClass)
/*    */   {
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */   public void servant_postinvoke(org.omg.CORBA.Object paramObject, ServantObject paramServantObject)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.NotLocalLocalCRDImpl
 * JD-Core Version:    0.6.2
 */