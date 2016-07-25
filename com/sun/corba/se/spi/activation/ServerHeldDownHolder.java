/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ServerHeldDownHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public ServerHeldDown value = null;
/*    */ 
/*    */   public ServerHeldDownHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServerHeldDownHolder(ServerHeldDown paramServerHeldDown)
/*    */   {
/* 20 */     this.value = paramServerHeldDown;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = ServerHeldDownHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     ServerHeldDownHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return ServerHeldDownHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerHeldDownHolder
 * JD-Core Version:    0.6.2
 */