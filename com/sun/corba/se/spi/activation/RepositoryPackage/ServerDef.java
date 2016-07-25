/*    */ package com.sun.corba.se.spi.activation.RepositoryPackage;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class ServerDef
/*    */   implements IDLEntity
/*    */ {
/* 13 */   public String applicationName = null;
/*    */ 
/* 16 */   public String serverName = null;
/*    */ 
/* 19 */   public String serverClassPath = null;
/*    */ 
/* 22 */   public String serverArgs = null;
/* 23 */   public String serverVmArgs = null;
/*    */ 
/*    */   public ServerDef()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServerDef(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
/*    */   {
/* 31 */     this.applicationName = paramString1;
/* 32 */     this.serverName = paramString2;
/* 33 */     this.serverClassPath = paramString3;
/* 34 */     this.serverArgs = paramString4;
/* 35 */     this.serverVmArgs = paramString5;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef
 * JD-Core Version:    0.6.2
 */