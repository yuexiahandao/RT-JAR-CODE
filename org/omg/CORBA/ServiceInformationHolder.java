/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class ServiceInformationHolder
/*     */   implements Streamable
/*     */ {
/*     */   public ServiceInformation value;
/*     */ 
/*     */   public ServiceInformationHolder()
/*     */   {
/*  60 */     this(null);
/*     */   }
/*     */ 
/*     */   public ServiceInformationHolder(ServiceInformation paramServiceInformation)
/*     */   {
/*  73 */     this.value = paramServiceInformation;
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  85 */     ServiceInformationHelper.write(paramOutputStream, this.value);
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  96 */     this.value = ServiceInformationHelper.read(paramInputStream);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 108 */     return ServiceInformationHelper.type();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ServiceInformationHolder
 * JD-Core Version:    0.6.2
 */