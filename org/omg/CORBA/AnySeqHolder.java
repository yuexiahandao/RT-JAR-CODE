/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class AnySeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public Any[] value = null;
/*    */ 
/*    */   public AnySeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public AnySeqHolder(Any[] paramArrayOfAny)
/*    */   {
/* 48 */     this.value = paramArrayOfAny;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = AnySeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     AnySeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return AnySeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.AnySeqHolder
 * JD-Core Version:    0.6.2
 */