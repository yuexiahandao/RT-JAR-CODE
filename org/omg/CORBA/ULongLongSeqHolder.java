/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ULongLongSeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public long[] value = null;
/*    */ 
/*    */   public ULongLongSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ULongLongSeqHolder(long[] paramArrayOfLong)
/*    */   {
/* 48 */     this.value = paramArrayOfLong;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = ULongLongSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     ULongLongSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return ULongLongSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ULongLongSeqHolder
 * JD-Core Version:    0.6.2
 */