/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class DoubleHolder
/*     */   implements Streamable
/*     */ {
/*     */   public double value;
/*     */ 
/*     */   public DoubleHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DoubleHolder(double paramDouble)
/*     */   {
/*  76 */     this.value = paramDouble;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  86 */     this.value = paramInputStream.read_double();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  96 */     paramOutputStream.write_double(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 105 */     return ORB.init().get_primitive_tc(TCKind.tk_double);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DoubleHolder
 * JD-Core Version:    0.6.2
 */