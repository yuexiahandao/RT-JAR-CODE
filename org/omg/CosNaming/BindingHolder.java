/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class BindingHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public Binding value = null;
/*    */ 
/*    */   public BindingHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public BindingHolder(Binding paramBinding)
/*    */   {
/* 20 */     this.value = paramBinding;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = BindingHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     BindingHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return BindingHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.BindingHolder
 * JD-Core Version:    0.6.2
 */