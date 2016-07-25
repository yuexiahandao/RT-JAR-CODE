/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class NameHolder
/*    */   implements Streamable
/*    */ {
/* 17 */   public NameComponent[] value = null;
/*    */ 
/*    */   public NameHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NameHolder(NameComponent[] paramArrayOfNameComponent)
/*    */   {
/* 25 */     this.value = paramArrayOfNameComponent;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 30 */     this.value = NameHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 35 */     NameHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 40 */     return NameHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NameHolder
 * JD-Core Version:    0.6.2
 */