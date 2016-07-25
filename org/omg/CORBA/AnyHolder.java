/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class AnyHolder
/*     */   implements Streamable
/*     */ {
/*     */   public Any value;
/*     */ 
/*     */   public AnyHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AnyHolder(Any paramAny)
/*     */   {
/*  74 */     this.value = paramAny;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  84 */     this.value = paramInputStream.read_any();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  94 */     paramOutputStream.write_any(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 105 */     return ORB.init().get_primitive_tc(TCKind.tk_any);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.AnyHolder
 * JD-Core Version:    0.6.2
 */