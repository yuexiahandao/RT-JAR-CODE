/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public class Message_1_0 extends MessageBase
/*     */ {
/*  45 */   private static ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.protocol");
/*     */ 
/*  49 */   int magic = 0;
/*  50 */   GIOPVersion GIOP_version = null;
/*  51 */   boolean byte_order = false;
/*  52 */   byte message_type = 0;
/*  53 */   int message_size = 0;
/*     */ 
/*     */   Message_1_0()
/*     */   {
/*     */   }
/*     */ 
/*     */   Message_1_0(int paramInt1, boolean paramBoolean, byte paramByte, int paramInt2)
/*     */   {
/*  62 */     this.magic = paramInt1;
/*  63 */     this.GIOP_version = GIOPVersion.V1_0;
/*  64 */     this.byte_order = paramBoolean;
/*  65 */     this.message_type = paramByte;
/*  66 */     this.message_size = paramInt2;
/*     */   }
/*     */ 
/*     */   public GIOPVersion getGIOPVersion()
/*     */   {
/*  72 */     return this.GIOP_version;
/*     */   }
/*     */ 
/*     */   public int getType() {
/*  76 */     return this.message_type;
/*     */   }
/*     */ 
/*     */   public int getSize() {
/*  80 */     return this.message_size;
/*     */   }
/*     */ 
/*     */   public boolean isLittleEndian() {
/*  84 */     return this.byte_order;
/*     */   }
/*     */ 
/*     */   public boolean moreFragmentsToFollow() {
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   public void setSize(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/*  94 */     this.message_size = paramInt;
/*     */ 
/*  99 */     int i = paramInt - 12;
/* 100 */     if (!isLittleEndian()) {
/* 101 */       paramByteBuffer.put(8, (byte)(i >>> 24 & 0xFF));
/* 102 */       paramByteBuffer.put(9, (byte)(i >>> 16 & 0xFF));
/* 103 */       paramByteBuffer.put(10, (byte)(i >>> 8 & 0xFF));
/* 104 */       paramByteBuffer.put(11, (byte)(i >>> 0 & 0xFF));
/*     */     } else {
/* 106 */       paramByteBuffer.put(8, (byte)(i >>> 0 & 0xFF));
/* 107 */       paramByteBuffer.put(9, (byte)(i >>> 8 & 0xFF));
/* 108 */       paramByteBuffer.put(10, (byte)(i >>> 16 & 0xFF));
/* 109 */       paramByteBuffer.put(11, (byte)(i >>> 24 & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   public FragmentMessage createFragmentMessage() {
/* 114 */     throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public void read(InputStream paramInputStream)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream)
/*     */   {
/* 135 */     paramOutputStream.write_long(this.magic);
/* 136 */     nullCheck(this.GIOP_version);
/* 137 */     this.GIOP_version.write(paramOutputStream);
/* 138 */     paramOutputStream.write_boolean(this.byte_order);
/* 139 */     paramOutputStream.write_octet(this.message_type);
/* 140 */     paramOutputStream.write_ulong(this.message_size);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_0
 * JD-Core Version:    0.6.2
 */