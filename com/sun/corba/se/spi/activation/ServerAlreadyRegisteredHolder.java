/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ServerAlreadyRegisteredHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public ServerAlreadyRegistered value = null;
/*    */ 
/*    */   public ServerAlreadyRegisteredHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServerAlreadyRegisteredHolder(ServerAlreadyRegistered paramServerAlreadyRegistered)
/*    */   {
/* 20 */     this.value = paramServerAlreadyRegistered;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = ServerAlreadyRegisteredHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     ServerAlreadyRegisteredHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return ServerAlreadyRegisteredHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerAlreadyRegisteredHolder
 * JD-Core Version:    0.6.2
 */