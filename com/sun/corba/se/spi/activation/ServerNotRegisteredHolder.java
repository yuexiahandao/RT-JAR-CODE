/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ServerNotRegisteredHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public ServerNotRegistered value = null;
/*    */ 
/*    */   public ServerNotRegisteredHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServerNotRegisteredHolder(ServerNotRegistered paramServerNotRegistered)
/*    */   {
/* 20 */     this.value = paramServerNotRegistered;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = ServerNotRegisteredHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     ServerNotRegisteredHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return ServerNotRegisteredHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerNotRegisteredHolder
 * JD-Core Version:    0.6.2
 */