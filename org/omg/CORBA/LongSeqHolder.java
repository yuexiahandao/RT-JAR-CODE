/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class LongSeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public int[] value = null;
/*    */ 
/*    */   public LongSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public LongSeqHolder(int[] paramArrayOfInt)
/*    */   {
/* 48 */     this.value = paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = LongSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     LongSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return LongSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.LongSeqHolder
 * JD-Core Version:    0.6.2
 */