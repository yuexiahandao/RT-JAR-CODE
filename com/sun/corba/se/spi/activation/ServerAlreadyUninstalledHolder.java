/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ServerAlreadyUninstalledHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public ServerAlreadyUninstalled value = null;
/*    */ 
/*    */   public ServerAlreadyUninstalledHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServerAlreadyUninstalledHolder(ServerAlreadyUninstalled paramServerAlreadyUninstalled)
/*    */   {
/* 20 */     this.value = paramServerAlreadyUninstalled;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = ServerAlreadyUninstalledHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     ServerAlreadyUninstalledHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return ServerAlreadyUninstalledHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerAlreadyUninstalledHolder
 * JD-Core Version:    0.6.2
 */