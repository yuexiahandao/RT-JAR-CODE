/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class StringHolder
/*     */   implements Streamable
/*     */ {
/*     */   public String value;
/*     */ 
/*     */   public StringHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StringHolder(String paramString)
/*     */   {
/*  76 */     this.value = paramString;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  86 */     this.value = paramInputStream.read_string();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  96 */     paramOutputStream.write_string(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 107 */     return ORB.init().get_primitive_tc(TCKind.tk_string);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.StringHolder
 * JD-Core Version:    0.6.2
 */