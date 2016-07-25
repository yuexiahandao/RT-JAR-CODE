/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class BindingListHolder
/*    */   implements Streamable
/*    */ {
/* 17 */   public Binding[] value = null;
/*    */ 
/*    */   public BindingListHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public BindingListHolder(Binding[] paramArrayOfBinding)
/*    */   {
/* 25 */     this.value = paramArrayOfBinding;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 30 */     this.value = BindingListHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 35 */     BindingListHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 40 */     return BindingListHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.BindingListHolder
 * JD-Core Version:    0.6.2
 */