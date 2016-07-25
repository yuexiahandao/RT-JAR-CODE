/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class ObjectHolder
/*     */   implements Streamable
/*     */ {
/*     */   public Object value;
/*     */ 
/*     */   public ObjectHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ObjectHolder(Object paramObject)
/*     */   {
/*  77 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  88 */     this.value = paramInputStream.read_Object();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  98 */     paramOutputStream.write_Object(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 109 */     return ORB.init().get_primitive_tc(TCKind.tk_objref);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ObjectHolder
 * JD-Core Version:    0.6.2
 */