/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class BooleanSeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public boolean[] value = null;
/*    */ 
/*    */   public BooleanSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public BooleanSeqHolder(boolean[] paramArrayOfBoolean)
/*    */   {
/* 48 */     this.value = paramArrayOfBoolean;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = BooleanSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     BooleanSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return BooleanSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.BooleanSeqHolder
 * JD-Core Version:    0.6.2
 */