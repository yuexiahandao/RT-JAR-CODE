/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class LongLongSeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public long[] value = null;
/*    */ 
/*    */   public LongLongSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public LongLongSeqHolder(long[] paramArrayOfLong)
/*    */   {
/* 48 */     this.value = paramArrayOfLong;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = LongLongSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     LongLongSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return LongLongSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.LongLongSeqHolder
 * JD-Core Version:    0.6.2
 */