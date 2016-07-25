/*    */ package org.omg.CosNaming.NamingContextExtPackage;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class InvalidAddressHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public InvalidAddress value = null;
/*    */ 
/*    */   public InvalidAddressHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidAddressHolder(InvalidAddress paramInvalidAddress)
/*    */   {
/* 20 */     this.value = paramInvalidAddress;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = InvalidAddressHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     InvalidAddressHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return InvalidAddressHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextExtPackage.InvalidAddressHolder
 * JD-Core Version:    0.6.2
 */