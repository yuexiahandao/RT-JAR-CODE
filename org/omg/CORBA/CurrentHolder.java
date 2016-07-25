/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class CurrentHolder
/*    */   implements Streamable
/*    */ {
/* 39 */   public Current value = null;
/*    */ 
/*    */   public CurrentHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public CurrentHolder(Current paramCurrent)
/*    */   {
/* 47 */     this.value = paramCurrent;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 52 */     this.value = CurrentHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 57 */     CurrentHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 62 */     return CurrentHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.CurrentHolder
 * JD-Core Version:    0.6.2
 */