/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class FloatSeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public float[] value = null;
/*    */ 
/*    */   public FloatSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public FloatSeqHolder(float[] paramArrayOfFloat)
/*    */   {
/* 48 */     this.value = paramArrayOfFloat;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = FloatSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     FloatSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return FloatSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.FloatSeqHolder
 * JD-Core Version:    0.6.2
 */