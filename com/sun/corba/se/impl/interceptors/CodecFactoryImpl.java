/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import org.omg.CORBA.LocalObject;
/*     */ import org.omg.IOP.Codec;
/*     */ import org.omg.IOP.CodecFactory;
/*     */ import org.omg.IOP.CodecFactoryPackage.UnknownEncoding;
/*     */ import org.omg.IOP.Encoding;
/*     */ 
/*     */ public final class CodecFactoryImpl extends LocalObject
/*     */   implements CodecFactory
/*     */ {
/*     */   private org.omg.CORBA.ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */   private static final int MAX_MINOR_VERSION_SUPPORTED = 2;
/*  59 */   private Codec[] codecs = new Codec[3];
/*     */ 
/*     */   public CodecFactoryImpl(org.omg.CORBA.ORB paramORB)
/*     */   {
/*  66 */     this.orb = paramORB;
/*  67 */     this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB)paramORB, "rpc.protocol");
/*     */ 
/*  76 */     for (int i = 0; i <= 2; i++)
/*  77 */       this.codecs[i] = new CDREncapsCodec(paramORB, 1, i);
/*     */   }
/*     */ 
/*     */   public Codec create_codec(Encoding paramEncoding)
/*     */     throws UnknownEncoding
/*     */   {
/*  92 */     if (paramEncoding == null) nullParam();
/*     */ 
/*  94 */     Codec localCodec = null;
/*     */ 
/*  97 */     if ((paramEncoding.format == 0) && (paramEncoding.major_version == 1))
/*     */     {
/* 100 */       if ((paramEncoding.minor_version >= 0) && (paramEncoding.minor_version <= 2))
/*     */       {
/* 103 */         localCodec = this.codecs[paramEncoding.minor_version];
/*     */       }
/*     */     }
/*     */ 
/* 107 */     if (localCodec == null) {
/* 108 */       throw new UnknownEncoding();
/*     */     }
/*     */ 
/* 111 */     return localCodec;
/*     */   }
/*     */ 
/*     */   private void nullParam()
/*     */   {
/* 120 */     throw this.wrapper.nullParam();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.CodecFactoryImpl
 * JD-Core Version:    0.6.2
 */