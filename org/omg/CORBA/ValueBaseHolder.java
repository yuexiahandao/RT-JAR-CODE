/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class ValueBaseHolder
/*     */   implements Streamable
/*     */ {
/*     */   public Serializable value;
/*     */ 
/*     */   public ValueBaseHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ValueBaseHolder(Serializable paramSerializable)
/*     */   {
/*  75 */     this.value = paramSerializable;
/*     */   }
/*     */ 
/*     */   public void _read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/*  85 */     this.value = ((org.omg.CORBA_2_3.portable.InputStream)paramInputStream).read_value();
/*     */   }
/*     */ 
/*     */   public void _write(org.omg.CORBA.portable.OutputStream paramOutputStream)
/*     */   {
/*  94 */     ((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream).write_value(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 104 */     return ORB.init().get_primitive_tc(TCKind.tk_value);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ValueBaseHolder
 * JD-Core Version:    0.6.2
 */