/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ServiceContextHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public ServiceContext value = null;
/*    */ 
/*    */   public ServiceContextHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ServiceContextHolder(ServiceContext paramServiceContext)
/*    */   {
/* 20 */     this.value = paramServiceContext;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = ServiceContextHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     ServiceContextHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return ServiceContextHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.ServiceContextHolder
 * JD-Core Version:    0.6.2
 */