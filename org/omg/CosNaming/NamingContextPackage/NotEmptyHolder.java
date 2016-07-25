/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class NotEmptyHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public NotEmpty value = null;
/*    */ 
/*    */   public NotEmptyHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NotEmptyHolder(NotEmpty paramNotEmpty)
/*    */   {
/* 20 */     this.value = paramNotEmpty;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = NotEmptyHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     NotEmptyHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return NotEmptyHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.NotEmptyHolder
 * JD-Core Version:    0.6.2
 */