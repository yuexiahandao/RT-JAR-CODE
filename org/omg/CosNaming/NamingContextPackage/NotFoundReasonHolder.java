/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class NotFoundReasonHolder
/*    */   implements Streamable
/*    */ {
/* 16 */   public NotFoundReason value = null;
/*    */ 
/*    */   public NotFoundReasonHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NotFoundReasonHolder(NotFoundReason paramNotFoundReason)
/*    */   {
/* 24 */     this.value = paramNotFoundReason;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 29 */     this.value = NotFoundReasonHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 34 */     NotFoundReasonHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 39 */     return NotFoundReasonHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.NotFoundReasonHolder
 * JD-Core Version:    0.6.2
 */