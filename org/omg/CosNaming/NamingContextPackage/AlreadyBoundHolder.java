/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class AlreadyBoundHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public AlreadyBound value = null;
/*    */ 
/*    */   public AlreadyBoundHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public AlreadyBoundHolder(AlreadyBound paramAlreadyBound)
/*    */   {
/* 20 */     this.value = paramAlreadyBound;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = AlreadyBoundHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     AlreadyBoundHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return AlreadyBoundHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.AlreadyBoundHolder
 * JD-Core Version:    0.6.2
 */