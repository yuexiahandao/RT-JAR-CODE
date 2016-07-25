/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.AnyImpl;
/*     */ import com.sun.corba.se.impl.encoding.EncapsInputStream;
/*     */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.LocalObject;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.IOP.Codec;
/*     */ import org.omg.IOP.CodecPackage.FormatMismatch;
/*     */ import org.omg.IOP.CodecPackage.InvalidTypeForEncoding;
/*     */ import org.omg.IOP.CodecPackage.TypeMismatch;
/*     */ import sun.corba.EncapsInputStreamFactory;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ 
/*     */ public final class CDREncapsCodec extends LocalObject
/*     */   implements Codec
/*     */ {
/*     */   private org.omg.CORBA.ORB orb;
/*     */   ORBUtilSystemException wrapper;
/*     */   private GIOPVersion giopVersion;
/*     */ 
/*     */   public CDREncapsCodec(org.omg.CORBA.ORB paramORB, int paramInt1, int paramInt2)
/*     */   {
/*  80 */     this.orb = paramORB;
/*  81 */     this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB)paramORB, "rpc.protocol");
/*     */ 
/*  84 */     this.giopVersion = GIOPVersion.getInstance((byte)paramInt1, (byte)paramInt2);
/*     */   }
/*     */ 
/*     */   public byte[] encode(Any paramAny)
/*     */     throws InvalidTypeForEncoding
/*     */   {
/*  93 */     if (paramAny == null)
/*  94 */       throw this.wrapper.nullParam();
/*  95 */     return encodeImpl(paramAny, true);
/*     */   }
/*     */ 
/*     */   public Any decode(byte[] paramArrayOfByte)
/*     */     throws FormatMismatch
/*     */   {
/* 105 */     if (paramArrayOfByte == null)
/* 106 */       throw this.wrapper.nullParam();
/* 107 */     return decodeImpl(paramArrayOfByte, null);
/*     */   }
/*     */ 
/*     */   public byte[] encode_value(Any paramAny)
/*     */     throws InvalidTypeForEncoding
/*     */   {
/* 117 */     if (paramAny == null)
/* 118 */       throw this.wrapper.nullParam();
/* 119 */     return encodeImpl(paramAny, false);
/*     */   }
/*     */ 
/*     */   public Any decode_value(byte[] paramArrayOfByte, TypeCode paramTypeCode)
/*     */     throws FormatMismatch, TypeMismatch
/*     */   {
/* 130 */     if (paramArrayOfByte == null)
/* 131 */       throw this.wrapper.nullParam();
/* 132 */     if (paramTypeCode == null)
/* 133 */       throw this.wrapper.nullParam();
/* 134 */     return decodeImpl(paramArrayOfByte, paramTypeCode);
/*     */   }
/*     */ 
/*     */   private byte[] encodeImpl(Any paramAny, boolean paramBoolean)
/*     */     throws InvalidTypeForEncoding
/*     */   {
/* 146 */     if (paramAny == null) {
/* 147 */       throw this.wrapper.nullParam();
/*     */     }
/*     */ 
/* 160 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((com.sun.corba.se.spi.orb.ORB)this.orb, this.giopVersion);
/*     */ 
/* 165 */     localEncapsOutputStream.putEndian();
/*     */ 
/* 168 */     if (paramBoolean) {
/* 169 */       localEncapsOutputStream.write_TypeCode(paramAny.type());
/*     */     }
/*     */ 
/* 173 */     paramAny.write_value(localEncapsOutputStream);
/*     */ 
/* 175 */     return localEncapsOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   private Any decodeImpl(byte[] paramArrayOfByte, TypeCode paramTypeCode)
/*     */     throws FormatMismatch
/*     */   {
/* 187 */     if (paramArrayOfByte == null) {
/* 188 */       throw this.wrapper.nullParam();
/*     */     }
/* 190 */     AnyImpl localAnyImpl = null;
/*     */     try
/*     */     {
/* 198 */       EncapsInputStream localEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(this.orb, paramArrayOfByte, paramArrayOfByte.length, this.giopVersion);
/*     */ 
/* 202 */       localEncapsInputStream.consumeEndian();
/*     */ 
/* 205 */       if (paramTypeCode == null) {
/* 206 */         paramTypeCode = localEncapsInputStream.read_TypeCode();
/*     */       }
/*     */ 
/* 210 */       localAnyImpl = new AnyImpl((com.sun.corba.se.spi.orb.ORB)this.orb);
/* 211 */       localAnyImpl.read_value(localEncapsInputStream, paramTypeCode);
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 215 */       throw new FormatMismatch();
/*     */     }
/*     */ 
/* 218 */     return localAnyImpl;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.CDREncapsCodec
 * JD-Core Version:    0.6.2
 */