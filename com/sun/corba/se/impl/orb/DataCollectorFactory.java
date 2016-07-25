/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.DataCollector;
/*    */ import java.applet.Applet;
/*    */ import java.net.URL;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public abstract class DataCollectorFactory
/*    */ {
/*    */   public static DataCollector create(Applet paramApplet, Properties paramProperties, String paramString)
/*    */   {
/* 40 */     String str = paramString;
/*    */ 
/* 42 */     if (paramApplet != null) {
/* 43 */       URL localURL = paramApplet.getCodeBase();
/*    */ 
/* 45 */       if (localURL != null) {
/* 46 */         str = localURL.getHost();
/*    */       }
/*    */     }
/* 49 */     return new AppletDataCollector(paramApplet, paramProperties, paramString, str);
/*    */   }
/*    */ 
/*    */   public static DataCollector create(String[] paramArrayOfString, Properties paramProperties, String paramString)
/*    */   {
/* 56 */     return new NormalDataCollector(paramArrayOfString, paramProperties, paramString, paramString);
/*    */   }
/*    */ 
/*    */   public static DataCollector create(Properties paramProperties, String paramString)
/*    */   {
/* 63 */     return new PropertyOnlyDataCollector(paramProperties, paramString, paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.DataCollectorFactory
 * JD-Core Version:    0.6.2
 */