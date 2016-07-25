/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.org.omg.SendingContext.CodeBase;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import sun.corba.EncapsInputStreamFactory;
/*     */ 
/*     */ public class EncapsInputStream extends CDRInputStream
/*     */ {
/*     */   private ORBUtilSystemException wrapper;
/*     */   private CodeBase codeBase;
/*     */ 
/*     */   public EncapsInputStream(org.omg.CORBA.ORB paramORB, byte[] paramArrayOfByte, int paramInt, boolean paramBoolean, GIOPVersion paramGIOPVersion)
/*     */   {
/*  66 */     super(paramORB, ByteBuffer.wrap(paramArrayOfByte), paramInt, paramBoolean, paramGIOPVersion, (byte)0, BufferManagerFactory.newBufferManagerRead(0, (byte)0, (com.sun.corba.se.spi.orb.ORB)paramORB));
/*     */ 
/*  73 */     this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB)paramORB, "rpc.encoding");
/*     */ 
/*  76 */     performORBVersionSpecificInit();
/*     */   }
/*     */ 
/*     */   public EncapsInputStream(org.omg.CORBA.ORB paramORB, ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean, GIOPVersion paramGIOPVersion)
/*     */   {
/*  82 */     super(paramORB, paramByteBuffer, paramInt, paramBoolean, paramGIOPVersion, (byte)0, BufferManagerFactory.newBufferManagerRead(0, (byte)0, (com.sun.corba.se.spi.orb.ORB)paramORB));
/*     */ 
/*  89 */     performORBVersionSpecificInit();
/*     */   }
/*     */ 
/*     */   public EncapsInputStream(org.omg.CORBA.ORB paramORB, byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  98 */     this(paramORB, paramArrayOfByte, paramInt, GIOPVersion.V1_2);
/*     */   }
/*     */ 
/*     */   public EncapsInputStream(EncapsInputStream paramEncapsInputStream)
/*     */   {
/* 104 */     super(paramEncapsInputStream);
/*     */ 
/* 106 */     this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB)paramEncapsInputStream.orb(), "rpc.encoding");
/*     */ 
/* 109 */     performORBVersionSpecificInit();
/*     */   }
/*     */ 
/*     */   public EncapsInputStream(org.omg.CORBA.ORB paramORB, byte[] paramArrayOfByte, int paramInt, GIOPVersion paramGIOPVersion)
/*     */   {
/* 120 */     this(paramORB, paramArrayOfByte, paramInt, false, paramGIOPVersion);
/*     */   }
/*     */ 
/*     */   public EncapsInputStream(org.omg.CORBA.ORB paramORB, byte[] paramArrayOfByte, int paramInt, GIOPVersion paramGIOPVersion, CodeBase paramCodeBase)
/*     */   {
/* 134 */     super(paramORB, ByteBuffer.wrap(paramArrayOfByte), paramInt, false, paramGIOPVersion, (byte)0, BufferManagerFactory.newBufferManagerRead(0, (byte)0, (com.sun.corba.se.spi.orb.ORB)paramORB));
/*     */ 
/* 144 */     this.codeBase = paramCodeBase;
/*     */ 
/* 146 */     performORBVersionSpecificInit();
/*     */   }
/*     */ 
/*     */   public CDRInputStream dup() {
/* 150 */     return EncapsInputStreamFactory.newEncapsInputStream(this);
/*     */   }
/*     */ 
/*     */   protected CodeSetConversion.BTCConverter createCharBTCConverter() {
/* 154 */     return CodeSetConversion.impl().getBTCConverter(OSFCodeSetRegistry.ISO_8859_1);
/*     */   }
/*     */ 
/*     */   protected CodeSetConversion.BTCConverter createWCharBTCConverter()
/*     */   {
/* 159 */     if (getGIOPVersion().equals(GIOPVersion.V1_0)) {
/* 160 */       throw this.wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 164 */     if (getGIOPVersion().equals(GIOPVersion.V1_1)) {
/* 165 */       return CodeSetConversion.impl().getBTCConverter(OSFCodeSetRegistry.UTF_16, isLittleEndian());
/*     */     }
/*     */ 
/* 175 */     return CodeSetConversion.impl().getBTCConverter(OSFCodeSetRegistry.UTF_16, false);
/*     */   }
/*     */ 
/*     */   public CodeBase getCodeBase()
/*     */   {
/* 180 */     return this.codeBase;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.EncapsInputStream
 * JD-Core Version:    0.6.2
 */