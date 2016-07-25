/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class WStringSeqHolder
/*    */   implements Streamable
/*    */ {
/* 15 */   public String[] value = null;
/*    */ 
/*    */   public WStringSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public WStringSeqHolder(String[] paramArrayOfString)
/*    */   {
/* 23 */     this.value = paramArrayOfString;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 28 */     this.value = WStringSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 33 */     WStringSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 38 */     return WStringSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.WStringSeqHolder
 * JD-Core Version:    0.6.2
 */