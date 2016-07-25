/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class NotFoundHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public NotFound value = null;
/*    */ 
/*    */   public NotFoundHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NotFoundHolder(NotFound paramNotFound)
/*    */   {
/* 20 */     this.value = paramNotFound;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = NotFoundHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     NotFoundHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return NotFoundHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.NotFoundHolder
 * JD-Core Version:    0.6.2
 */