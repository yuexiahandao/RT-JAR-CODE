/*    */ package com.sun.corba.se.impl.naming.namingutil;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class IIOPEndpointInfo
/*    */ {
/*    */   private int major;
/*    */   private int minor;
/*    */   private String host;
/*    */   private int port;
/*    */ 
/*    */   IIOPEndpointInfo()
/*    */   {
/* 48 */     this.major = 1;
/* 49 */     this.minor = 0;
/*    */ 
/* 51 */     this.host = "localhost";
/*    */ 
/* 53 */     this.port = 2089;
/*    */   }
/*    */ 
/*    */   public void setHost(String paramString) {
/* 57 */     this.host = paramString;
/*    */   }
/*    */ 
/*    */   public String getHost() {
/* 61 */     return this.host;
/*    */   }
/*    */ 
/*    */   public void setPort(int paramInt) {
/* 65 */     this.port = paramInt;
/*    */   }
/*    */ 
/*    */   public int getPort() {
/* 69 */     return this.port;
/*    */   }
/*    */ 
/*    */   public void setVersion(int paramInt1, int paramInt2) {
/* 73 */     this.major = paramInt1;
/* 74 */     this.minor = paramInt2;
/*    */   }
/*    */ 
/*    */   public int getMajor() {
/* 78 */     return this.major;
/*    */   }
/*    */ 
/*    */   public int getMinor() {
/* 82 */     return this.minor;
/*    */   }
/*    */ 
/*    */   public void dump()
/*    */   {
/* 88 */     System.out.println(" Major -> " + this.major + " Minor -> " + this.minor);
/* 89 */     System.out.println("host -> " + this.host);
/* 90 */     System.out.println("port -> " + this.port);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.namingutil.IIOPEndpointInfo
 * JD-Core Version:    0.6.2
 */