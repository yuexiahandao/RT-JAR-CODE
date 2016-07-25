/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDRInputStream;
/*     */ import com.sun.corba.se.impl.logging.IORSystemException;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*     */ import org.omg.CORBA.OctetSeqHolder;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ 
/*     */ public final class OldJIDLObjectKeyTemplate extends OldObjectKeyTemplateBase
/*     */ {
/*     */   public static final byte NULL_PATCH_VERSION = 0;
/*  56 */   byte patchVersion = 0;
/*     */ 
/*     */   public OldJIDLObjectKeyTemplate(ORB paramORB, int paramInt1, int paramInt2, InputStream paramInputStream, OctetSeqHolder paramOctetSeqHolder)
/*     */   {
/*  61 */     this(paramORB, paramInt1, paramInt2, paramInputStream);
/*     */ 
/*  63 */     paramOctetSeqHolder.value = readObjectKey(paramInputStream);
/*     */ 
/*  80 */     if ((paramInt1 == -1347695873) && (paramOctetSeqHolder.value.length > ((CDRInputStream)paramInputStream).getPosition()))
/*     */     {
/*  83 */       this.patchVersion = paramInputStream.read_octet();
/*     */ 
/*  85 */       if (this.patchVersion == 1)
/*  86 */         setORBVersion(ORBVersionFactory.getJDK1_3_1_01());
/*  87 */       else if (this.patchVersion > 1)
/*  88 */         setORBVersion(ORBVersionFactory.getORBVersion());
/*     */       else
/*  90 */         throw this.wrapper.invalidJdk131PatchLevel(new Integer(this.patchVersion));
/*     */     }
/*     */   }
/*     */ 
/*     */   public OldJIDLObjectKeyTemplate(ORB paramORB, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  97 */     super(paramORB, paramInt1, paramInt2, paramInt3, "", JIDL_OAID);
/*     */   }
/*     */ 
/*     */   public OldJIDLObjectKeyTemplate(ORB paramORB, int paramInt1, int paramInt2, InputStream paramInputStream)
/*     */   {
/* 102 */     this(paramORB, paramInt1, paramInt2, paramInputStream.read_long());
/*     */   }
/*     */ 
/*     */   protected void writeTemplate(OutputStream paramOutputStream)
/*     */   {
/* 107 */     paramOutputStream.write_long(getMagic());
/* 108 */     paramOutputStream.write_long(getSubcontractId());
/* 109 */     paramOutputStream.write_long(getServerId());
/*     */   }
/*     */ 
/*     */   public void write(ObjectId paramObjectId, OutputStream paramOutputStream)
/*     */   {
/* 114 */     super.write(paramObjectId, paramOutputStream);
/*     */ 
/* 116 */     if (this.patchVersion != 0)
/* 117 */       paramOutputStream.write_octet(this.patchVersion);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate
 * JD-Core Version:    0.6.2
 */