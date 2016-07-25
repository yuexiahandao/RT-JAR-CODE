/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class InvalidNameHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public InvalidName value = null;
/*    */ 
/*    */   public InvalidNameHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidNameHolder(InvalidName paramInvalidName)
/*    */   {
/* 20 */     this.value = paramInvalidName;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = InvalidNameHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     InvalidNameHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return InvalidNameHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.InvalidNameHolder
 * JD-Core Version:    0.6.2
 */