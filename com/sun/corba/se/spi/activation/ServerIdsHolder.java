/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ServerIdsHolder
/*    */   implements Streamable
/*    */ {
/* 13 */   public int[] value = null;
/*    */ 
/*    */   public ServerIdsHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServerIdsHolder(int[] paramArrayOfInt)
/*    */   {
/* 21 */     this.value = paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 26 */     this.value = ServerIdsHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 31 */     ServerIdsHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 36 */     return ServerIdsHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerIdsHolder
 * JD-Core Version:    0.6.2
 */