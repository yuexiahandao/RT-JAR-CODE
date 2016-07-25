/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ShortSeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public short[] value = null;
/*    */ 
/*    */   public ShortSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ShortSeqHolder(short[] paramArrayOfShort)
/*    */   {
/* 48 */     this.value = paramArrayOfShort;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = ShortSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     ShortSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return ShortSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ShortSeqHolder
 * JD-Core Version:    0.6.2
 */