/*    */ package com.sun.corba.se.spi.activation.InitialNameServicePackage;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class NameAlreadyBoundHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public NameAlreadyBound value = null;
/*    */ 
/*    */   public NameAlreadyBoundHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NameAlreadyBoundHolder(NameAlreadyBound paramNameAlreadyBound)
/*    */   {
/* 20 */     this.value = paramNameAlreadyBound;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = NameAlreadyBoundHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     NameAlreadyBoundHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return NameAlreadyBoundHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBoundHolder
 * JD-Core Version:    0.6.2
 */