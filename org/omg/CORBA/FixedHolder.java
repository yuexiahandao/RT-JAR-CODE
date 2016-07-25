/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class FixedHolder
/*    */   implements Streamable
/*    */ {
/*    */   public BigDecimal value;
/*    */ 
/*    */   public FixedHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public FixedHolder(BigDecimal paramBigDecimal)
/*    */   {
/* 65 */     this.value = paramBigDecimal;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 75 */     this.value = paramInputStream.read_fixed();
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 85 */     paramOutputStream.write_fixed(this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 95 */     return ORB.init().get_primitive_tc(TCKind.tk_fixed);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.FixedHolder
 * JD-Core Version:    0.6.2
 */