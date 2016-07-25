/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ServiceContextListHolder
/*    */   implements Streamable
/*    */ {
/* 15 */   public ServiceContext[] value = null;
/*    */ 
/*    */   public ServiceContextListHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServiceContextListHolder(ServiceContext[] paramArrayOfServiceContext)
/*    */   {
/* 23 */     this.value = paramArrayOfServiceContext;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 28 */     this.value = ServiceContextListHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 33 */     ServiceContextListHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 38 */     return ServiceContextListHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.ServiceContextListHolder
 * JD-Core Version:    0.6.2
 */