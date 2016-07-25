/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class DoubleSeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public double[] value = null;
/*    */ 
/*    */   public DoubleSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public DoubleSeqHolder(double[] paramArrayOfDouble)
/*    */   {
/* 48 */     this.value = paramArrayOfDouble;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = DoubleSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     DoubleSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return DoubleSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DoubleSeqHolder
 * JD-Core Version:    0.6.2
 */