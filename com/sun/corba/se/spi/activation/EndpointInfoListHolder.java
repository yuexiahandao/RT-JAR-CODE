/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class EndpointInfoListHolder
/*    */   implements Streamable
/*    */ {
/* 13 */   public EndPointInfo[] value = null;
/*    */ 
/*    */   public EndpointInfoListHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public EndpointInfoListHolder(EndPointInfo[] paramArrayOfEndPointInfo)
/*    */   {
/* 21 */     this.value = paramArrayOfEndPointInfo;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 26 */     this.value = EndpointInfoListHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 31 */     EndpointInfoListHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 36 */     return EndpointInfoListHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.EndpointInfoListHolder
 * JD-Core Version:    0.6.2
 */