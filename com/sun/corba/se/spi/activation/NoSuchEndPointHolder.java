/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class NoSuchEndPointHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public NoSuchEndPoint value = null;
/*    */ 
/*    */   public NoSuchEndPointHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NoSuchEndPointHolder(NoSuchEndPoint paramNoSuchEndPoint)
/*    */   {
/* 20 */     this.value = paramNoSuchEndPoint;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = NoSuchEndPointHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     NoSuchEndPointHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return NoSuchEndPointHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.NoSuchEndPointHolder
 * JD-Core Version:    0.6.2
 */