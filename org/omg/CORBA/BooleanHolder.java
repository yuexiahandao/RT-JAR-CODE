/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class BooleanHolder
/*     */   implements Streamable
/*     */ {
/*     */   public boolean value;
/*     */ 
/*     */   public BooleanHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public BooleanHolder(boolean paramBoolean)
/*     */   {
/*  75 */     this.value = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  86 */     this.value = paramInputStream.read_boolean();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  96 */     paramOutputStream.write_boolean(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 107 */     return ORB.init().get_primitive_tc(TCKind.tk_boolean);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.BooleanHolder
 * JD-Core Version:    0.6.2
 */