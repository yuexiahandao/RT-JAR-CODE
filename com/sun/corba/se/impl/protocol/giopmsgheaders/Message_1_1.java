/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public class Message_1_1 extends MessageBase
/*     */ {
/*     */   static final int UPPER_THREE_BYTES_OF_INT_MASK = 255;
/*  49 */   private static ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.protocol");
/*     */ 
/*  53 */   int magic = 0;
/*  54 */   GIOPVersion GIOP_version = null;
/*  55 */   byte flags = 0;
/*  56 */   byte message_type = 0;
/*  57 */   int message_size = 0;
/*     */ 
/*     */   Message_1_1()
/*     */   {
/*     */   }
/*     */ 
/*     */   Message_1_1(int paramInt1, GIOPVersion paramGIOPVersion, byte paramByte1, byte paramByte2, int paramInt2)
/*     */   {
/*  66 */     this.magic = paramInt1;
/*  67 */     this.GIOP_version = paramGIOPVersion;
/*  68 */     this.flags = paramByte1;
/*  69 */     this.message_type = paramByte2;
/*  70 */     this.message_size = paramInt2;
/*     */   }
/*     */ 
/*     */   public GIOPVersion getGIOPVersion()
/*     */   {
/*  76 */     return this.GIOP_version;
/*     */   }
/*     */ 
/*     */   public int getType() {
/*  80 */     return this.message_type;
/*     */   }
/*     */ 
/*     */   public int getSize() {
/*  84 */     return this.message_size;
/*     */   }
/*     */ 
/*     */   public boolean isLittleEndian() {
/*  88 */     return (this.flags & 0x1) == 1;
/*     */   }
/*     */ 
/*     */   public boolean moreFragmentsToFollow() {
/*  92 */     return (this.flags & 0x2) == 2;
/*     */   }
/*     */ 
/*     */   public void setThreadPoolToUse(int paramInt)
/*     */   {
/* 105 */     int i = paramInt << 2;
/* 106 */     i &= 255;
/* 107 */     i |= this.flags;
/* 108 */     this.flags = ((byte)i);
/*     */   }
/*     */ 
/*     */   public void setSize(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/* 113 */     this.message_size = paramInt;
/*     */ 
/* 119 */     int i = paramInt - 12;
/* 120 */     if (!isLittleEndian()) {
/* 121 */       paramByteBuffer.put(8, (byte)(i >>> 24 & 0xFF));
/* 122 */       paramByteBuffer.put(9, (byte)(i >>> 16 & 0xFF));
/* 123 */       paramByteBuffer.put(10, (byte)(i >>> 8 & 0xFF));
/* 124 */       paramByteBuffer.put(11, (byte)(i >>> 0 & 0xFF));
/*     */     } else {
/* 126 */       paramByteBuffer.put(8, (byte)(i >>> 0 & 0xFF));
/* 127 */       paramByteBuffer.put(9, (byte)(i >>> 8 & 0xFF));
/* 128 */       paramByteBuffer.put(10, (byte)(i >>> 16 & 0xFF));
/* 129 */       paramByteBuffer.put(11, (byte)(i >>> 24 & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   public FragmentMessage createFragmentMessage()
/*     */   {
/* 140 */     switch (this.message_type) {
/*     */     case 2:
/*     */     case 5:
/*     */     case 6:
/* 144 */       throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
/*     */     case 3:
/*     */     case 4:
/* 148 */       if (this.GIOP_version.equals(GIOPVersion.V1_1)) {
/* 149 */         throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 163 */     if (this.GIOP_version.equals(GIOPVersion.V1_1))
/* 164 */       return new FragmentMessage_1_1(this);
/* 165 */     if (this.GIOP_version.equals(GIOPVersion.V1_2)) {
/* 166 */       return new FragmentMessage_1_2(this);
/*     */     }
/*     */ 
/* 169 */     throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public void read(InputStream paramInputStream)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream)
/*     */   {
/* 189 */     paramOutputStream.write_long(this.magic);
/* 190 */     nullCheck(this.GIOP_version);
/* 191 */     this.GIOP_version.write(paramOutputStream);
/* 192 */     paramOutputStream.write_octet(this.flags);
/* 193 */     paramOutputStream.write_octet(this.message_type);
/* 194 */     paramOutputStream.write_ulong(this.message_size);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1
 * JD-Core Version:    0.6.2
 */