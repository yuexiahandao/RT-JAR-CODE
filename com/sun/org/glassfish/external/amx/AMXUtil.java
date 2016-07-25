/*    */ package com.sun.org.glassfish.external.amx;
/*    */ 
/*    */ import com.sun.org.glassfish.external.arc.Stability;
/*    */ import com.sun.org.glassfish.external.arc.Taxonomy;
/*    */ import javax.management.ObjectName;
/*    */ 
/*    */ @Taxonomy(stability=Stability.UNCOMMITTED)
/*    */ public final class AMXUtil
/*    */ {
/*    */   public static ObjectName newObjectName(String s)
/*    */   {
/*    */     try
/*    */     {
/* 47 */       return new ObjectName(s);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 51 */       throw new RuntimeException("bad ObjectName", e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static ObjectName newObjectName(String domain, String props)
/*    */   {
/* 64 */     return newObjectName(domain + ":" + props);
/*    */   }
/*    */ 
/*    */   public static ObjectName getMBeanServerDelegateObjectName()
/*    */   {
/* 72 */     return newObjectName("JMImplementation:type=MBeanServerDelegate");
/*    */   }
/*    */ 
/*    */   public static String prop(String key, String value)
/*    */   {
/* 77 */     return key + "=" + value;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.amx.AMXUtil
 * JD-Core Version:    0.6.2
 */