/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class WCharSeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public char[] value = null;
/*    */ 
/*    */   public WCharSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public WCharSeqHolder(char[] paramArrayOfChar)
/*    */   {
/* 48 */     this.value = paramArrayOfChar;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = WCharSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     WCharSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return WCharSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.WCharSeqHolder
 * JD-Core Version:    0.6.2
 */