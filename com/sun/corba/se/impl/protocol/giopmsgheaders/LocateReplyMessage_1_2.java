/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDRInputStream;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.IOException;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class LocateReplyMessage_1_2 extends Message_1_2
/*     */   implements LocateReplyMessage
/*     */ {
/*  59 */   private ORB orb = null;
/*  60 */   private ORBUtilSystemException wrapper = null;
/*  61 */   private int reply_status = 0;
/*  62 */   private IOR ior = null;
/*  63 */   private String exClassName = null;
/*  64 */   private int minorCode = 0;
/*  65 */   private CompletionStatus completionStatus = null;
/*  66 */   private short addrDisposition = 0;
/*     */ 
/*     */   LocateReplyMessage_1_2(ORB paramORB)
/*     */   {
/*  71 */     this.orb = paramORB;
/*  72 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */   }
/*     */ 
/*     */   LocateReplyMessage_1_2(ORB paramORB, int paramInt1, int paramInt2, IOR paramIOR)
/*     */   {
/*  78 */     super(1195986768, GIOPVersion.V1_2, (byte)0, (byte)4, 0);
/*     */ 
/*  80 */     this.orb = paramORB;
/*  81 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/*  83 */     this.request_id = paramInt1;
/*  84 */     this.reply_status = paramInt2;
/*  85 */     this.ior = paramIOR;
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/*  91 */     return this.request_id;
/*     */   }
/*     */ 
/*     */   public int getReplyStatus() {
/*  95 */     return this.reply_status;
/*     */   }
/*     */ 
/*     */   public short getAddrDisposition() {
/*  99 */     return this.addrDisposition;
/*     */   }
/*     */ 
/*     */   public SystemException getSystemException(String paramString) {
/* 103 */     return MessageBase.getSystemException(this.exClassName, this.minorCode, this.completionStatus, paramString, this.wrapper);
/*     */   }
/*     */ 
/*     */   public IOR getIOR()
/*     */   {
/* 108 */     return this.ior;
/*     */   }
/*     */ 
/*     */   public void read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 114 */     super.read(paramInputStream);
/* 115 */     this.request_id = paramInputStream.read_ulong();
/* 116 */     this.reply_status = paramInputStream.read_long();
/* 117 */     isValidReplyStatus(this.reply_status);
/*     */     Object localObject;
/* 125 */     if (this.reply_status == 4)
/*     */     {
/* 127 */       localObject = paramInputStream.read_string();
/* 128 */       this.exClassName = ORBUtility.classNameOf((String)localObject);
/* 129 */       this.minorCode = paramInputStream.read_long();
/* 130 */       int i = paramInputStream.read_long();
/*     */ 
/* 132 */       switch (i) {
/*     */       case 0:
/* 134 */         this.completionStatus = CompletionStatus.COMPLETED_YES;
/* 135 */         break;
/*     */       case 1:
/* 137 */         this.completionStatus = CompletionStatus.COMPLETED_NO;
/* 138 */         break;
/*     */       case 2:
/* 140 */         this.completionStatus = CompletionStatus.COMPLETED_MAYBE;
/* 141 */         break;
/*     */       default:
/* 143 */         throw this.wrapper.badCompletionStatusInLocateReply(CompletionStatus.COMPLETED_MAYBE, new Integer(i));
/*     */       }
/*     */     }
/* 146 */     else if ((this.reply_status == 2) || (this.reply_status == 3))
/*     */     {
/* 148 */       localObject = (CDRInputStream)paramInputStream;
/* 149 */       this.ior = IORFactories.makeIOR((org.omg.CORBA_2_3.portable.InputStream)localObject);
/* 150 */     } else if (this.reply_status == 5)
/*     */     {
/* 154 */       this.addrDisposition = AddressingDispositionHelper.read(paramInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream)
/*     */   {
/* 162 */     super.write(paramOutputStream);
/* 163 */     paramOutputStream.write_ulong(this.request_id);
/* 164 */     paramOutputStream.write_long(this.reply_status);
/*     */   }
/*     */ 
/*     */   public static void isValidReplyStatus(int paramInt)
/*     */   {
/* 174 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/* 181 */       break;
/*     */     default:
/* 183 */       ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get("rpc.protocol");
/*     */ 
/* 185 */       throw localORBUtilSystemException.illegalReplyStatus(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void callback(MessageHandler paramMessageHandler)
/*     */     throws IOException
/*     */   {
/* 192 */     paramMessageHandler.handleInput(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage_1_2
 * JD-Core Version:    0.6.2
 */