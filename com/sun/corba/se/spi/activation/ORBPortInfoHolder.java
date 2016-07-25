/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ORBPortInfoHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public ORBPortInfo value = null;
/*    */ 
/*    */   public ORBPortInfoHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ORBPortInfoHolder(ORBPortInfo paramORBPortInfo)
/*    */   {
/* 20 */     this.value = paramORBPortInfo;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = ORBPortInfoHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     ORBPortInfoHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return ORBPortInfoHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ORBPortInfoHolder
 * JD-Core Version:    0.6.2
 */