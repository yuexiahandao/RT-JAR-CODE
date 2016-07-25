/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ORBPortInfoListHolder
/*    */   implements Streamable
/*    */ {
/* 13 */   public ORBPortInfo[] value = null;
/*    */ 
/*    */   public ORBPortInfoListHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ORBPortInfoListHolder(ORBPortInfo[] paramArrayOfORBPortInfo)
/*    */   {
/* 21 */     this.value = paramArrayOfORBPortInfo;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 26 */     this.value = ORBPortInfoListHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 31 */     ORBPortInfoListHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 36 */     return ORBPortInfoListHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ORBPortInfoListHolder
 * JD-Core Version:    0.6.2
 */