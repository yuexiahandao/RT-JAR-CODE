/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDRInputStream;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.IOException;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class LocateReplyMessage_1_1 extends Message_1_1
/*     */   implements LocateReplyMessage
/*     */ {
/*  54 */   private ORB orb = null;
/*  55 */   private int request_id = 0;
/*  56 */   private int reply_status = 0;
/*  57 */   private IOR ior = null;
/*     */ 
/*     */   LocateReplyMessage_1_1(ORB paramORB)
/*     */   {
/*  62 */     this.orb = paramORB;
/*     */   }
/*     */ 
/*     */   LocateReplyMessage_1_1(ORB paramORB, int paramInt1, int paramInt2, IOR paramIOR)
/*     */   {
/*  67 */     super(1195986768, GIOPVersion.V1_1, (byte)0, (byte)4, 0);
/*     */ 
/*  69 */     this.orb = paramORB;
/*  70 */     this.request_id = paramInt1;
/*  71 */     this.reply_status = paramInt2;
/*  72 */     this.ior = paramIOR;
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/*  78 */     return this.request_id;
/*     */   }
/*     */ 
/*     */   public int getReplyStatus() {
/*  82 */     return this.reply_status;
/*     */   }
/*     */ 
/*     */   public short getAddrDisposition() {
/*  86 */     return 0;
/*     */   }
/*     */ 
/*     */   public SystemException getSystemException(String paramString) {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public IOR getIOR() {
/*  94 */     return this.ior;
/*     */   }
/*     */ 
/*     */   public void read(InputStream paramInputStream)
/*     */   {
/* 100 */     super.read(paramInputStream);
/* 101 */     this.request_id = paramInputStream.read_ulong();
/* 102 */     this.reply_status = paramInputStream.read_long();
/* 103 */     isValidReplyStatus(this.reply_status);
/*     */ 
/* 106 */     if (this.reply_status == 2) {
/* 107 */       CDRInputStream localCDRInputStream = (CDRInputStream)paramInputStream;
/* 108 */       this.ior = IORFactories.makeIOR(localCDRInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream)
/*     */   {
/* 115 */     super.write(paramOutputStream);
/* 116 */     paramOutputStream.write_ulong(this.request_id);
/* 117 */     paramOutputStream.write_long(this.reply_status);
/*     */   }
/*     */ 
/*     */   public static void isValidReplyStatus(int paramInt)
/*     */   {
/* 123 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/* 127 */       break;
/*     */     default:
/* 129 */       ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get("rpc.protocol");
/*     */ 
/* 131 */       throw localORBUtilSystemException.illegalReplyStatus(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void callback(MessageHandler paramMessageHandler)
/*     */     throws IOException
/*     */   {
/* 138 */     paramMessageHandler.handleInput(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage_1_1
 * JD-Core Version:    0.6.2
 */