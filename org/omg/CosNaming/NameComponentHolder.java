/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class NameComponentHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public NameComponent value = null;
/*    */ 
/*    */   public NameComponentHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NameComponentHolder(NameComponent paramNameComponent)
/*    */   {
/* 20 */     this.value = paramNameComponent;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = NameComponentHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     NameComponentHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return NameComponentHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NameComponentHolder
 * JD-Core Version:    0.6.2
 */