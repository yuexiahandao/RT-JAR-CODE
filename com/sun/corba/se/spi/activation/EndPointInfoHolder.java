/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class EndPointInfoHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public EndPointInfo value = null;
/*    */ 
/*    */   public EndPointInfoHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public EndPointInfoHolder(EndPointInfo paramEndPointInfo)
/*    */   {
/* 20 */     this.value = paramEndPointInfo;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = EndPointInfoHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     EndPointInfoHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return EndPointInfoHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.EndPointInfoHolder
 * JD-Core Version:    0.6.2
 */