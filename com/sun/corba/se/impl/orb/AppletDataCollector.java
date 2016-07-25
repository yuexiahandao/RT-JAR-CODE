/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import java.applet.Applet;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class AppletDataCollector extends DataCollectorBase
/*    */ {
/*    */   private Applet applet;
/*    */ 
/*    */   AppletDataCollector(Applet paramApplet, Properties paramProperties, String paramString1, String paramString2)
/*    */   {
/* 37 */     super(paramProperties, paramString1, paramString2);
/* 38 */     this.applet = paramApplet;
/*    */   }
/*    */ 
/*    */   public boolean isApplet()
/*    */   {
/* 43 */     return true;
/*    */   }
/*    */ 
/*    */   protected void collect()
/*    */   {
/* 48 */     checkPropertyDefaults();
/*    */ 
/* 50 */     findPropertiesFromFile();
/*    */ 
/* 55 */     findPropertiesFromProperties();
/* 56 */     findPropertiesFromApplet(this.applet);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.AppletDataCollector
 * JD-Core Version:    0.6.2
 */