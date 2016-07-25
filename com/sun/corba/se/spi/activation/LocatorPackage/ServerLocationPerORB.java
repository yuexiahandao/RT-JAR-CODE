/*    */ package com.sun.corba.se.spi.activation.LocatorPackage;
/*    */ 
/*    */ import com.sun.corba.se.spi.activation.EndPointInfo;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class ServerLocationPerORB
/*    */   implements IDLEntity
/*    */ {
/* 13 */   public String hostname = null;
/* 14 */   public EndPointInfo[] ports = null;
/*    */ 
/*    */   public ServerLocationPerORB()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServerLocationPerORB(String paramString, EndPointInfo[] paramArrayOfEndPointInfo)
/*    */   {
/* 22 */     this.hostname = paramString;
/* 23 */     this.ports = paramArrayOfEndPointInfo;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB
 * JD-Core Version:    0.6.2
 */