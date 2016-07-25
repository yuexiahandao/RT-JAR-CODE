/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class NormalDataCollector extends DataCollectorBase
/*    */ {
/*    */   private String[] args;
/*    */ 
/*    */   public NormalDataCollector(String[] paramArrayOfString, Properties paramProperties, String paramString1, String paramString2)
/*    */   {
/* 41 */     super(paramProperties, paramString1, paramString2);
/* 42 */     this.args = paramArrayOfString;
/*    */   }
/*    */ 
/*    */   public boolean isApplet()
/*    */   {
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */   protected void collect()
/*    */   {
/* 52 */     checkPropertyDefaults();
/*    */ 
/* 54 */     findPropertiesFromFile();
/* 55 */     findPropertiesFromSystem();
/* 56 */     findPropertiesFromProperties();
/* 57 */     findPropertiesFromArgs(this.args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.NormalDataCollector
 * JD-Core Version:    0.6.2
 */