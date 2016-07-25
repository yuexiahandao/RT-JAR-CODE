/*    */ package com.sun.corba.se.spi.activation.LocatorPackage;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ServerLocationPerORBHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public ServerLocationPerORB value = null;
/*    */ 
/*    */   public ServerLocationPerORBHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServerLocationPerORBHolder(ServerLocationPerORB paramServerLocationPerORB)
/*    */   {
/* 20 */     this.value = paramServerLocationPerORB;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = ServerLocationPerORBHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     ServerLocationPerORBHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return ServerLocationPerORBHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORBHolder
 * JD-Core Version:    0.6.2
 */