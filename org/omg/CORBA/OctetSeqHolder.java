/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class OctetSeqHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public byte[] value = null;
/*    */ 
/*    */   public OctetSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public OctetSeqHolder(byte[] paramArrayOfByte)
/*    */   {
/* 48 */     this.value = paramArrayOfByte;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = OctetSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     OctetSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return OctetSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.OctetSeqHolder
 * JD-Core Version:    0.6.2
 */