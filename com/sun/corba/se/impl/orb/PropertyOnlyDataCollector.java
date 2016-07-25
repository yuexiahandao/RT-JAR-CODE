/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class PropertyOnlyDataCollector extends DataCollectorBase
/*    */ {
/*    */   public PropertyOnlyDataCollector(Properties paramProperties, String paramString1, String paramString2)
/*    */   {
/* 41 */     super(paramProperties, paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   public boolean isApplet()
/*    */   {
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   protected void collect()
/*    */   {
/* 51 */     checkPropertyDefaults();
/*    */ 
/* 53 */     findPropertiesFromProperties();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.PropertyOnlyDataCollector
 * JD-Core Version:    0.6.2
 */