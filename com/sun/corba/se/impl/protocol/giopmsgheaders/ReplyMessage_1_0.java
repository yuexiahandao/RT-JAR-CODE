/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDRInputStream;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*     */ import java.io.IOException;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.SystemException;
/*     */ 
/*     */ public final class ReplyMessage_1_0 extends Message_1_0
/*     */   implements ReplyMessage
/*     */ {
/*  57 */   private ORB orb = null;
/*  58 */   private ORBUtilSystemException wrapper = null;
/*  59 */   private ServiceContexts service_contexts = null;
/*  60 */   private int request_id = 0;
/*  61 */   private int reply_status = 0;
/*  62 */   private IOR ior = null;
/*  63 */   private String exClassName = null;
/*  64 */   private int minorCode = 0;
/*  65 */   private CompletionStatus completionStatus = null;
/*     */ 
/*     */   ReplyMessage_1_0(ORB paramORB)
/*     */   {
/*  70 */     this.orb = paramORB;
/*  71 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */   }
/*     */ 
/*     */   ReplyMessage_1_0(ORB paramORB, ServiceContexts paramServiceContexts, int paramInt1, int paramInt2, IOR paramIOR)
/*     */   {
/*  77 */     super(1195986768, false, (byte)1, 0);
/*  78 */     this.orb = paramORB;
/*  79 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/*  81 */     this.service_contexts = paramServiceContexts;
/*  82 */     this.request_id = paramInt1;
/*  83 */     this.reply_status = paramInt2;
/*  84 */     this.ior = paramIOR;
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/*  90 */     return this.request_id;
/*     */   }
/*     */ 
/*     */   public int getReplyStatus() {
/*  94 */     return this.reply_status;
/*     */   }
/*     */ 
/*     */   public short getAddrDisposition() {
/*  98 */     return 0;
/*     */   }
/*     */ 
/*     */   public ServiceContexts getServiceContexts() {
/* 102 */     return this.service_contexts;
/*     */   }
/*     */ 
/*     */   public void setServiceContexts(ServiceContexts paramServiceContexts) {
/* 106 */     this.service_contexts = paramServiceContexts;
/*     */   }
/*     */ 
/*     */   public SystemException getSystemException(String paramString) {
/* 110 */     return MessageBase.getSystemException(this.exClassName, this.minorCode, this.completionStatus, paramString, this.wrapper);
/*     */   }
/*     */ 
/*     */   public IOR getIOR()
/*     */   {
/* 115 */     return this.ior;
/*     */   }
/*     */ 
/*     */   public void setIOR(IOR paramIOR) {
/* 119 */     this.ior = paramIOR;
/*     */   }
/*     */ 
/*     */   public void read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 125 */     super.read(paramInputStream);
/* 126 */     this.service_contexts = new ServiceContexts((org.omg.CORBA_2_3.portable.InputStream)paramInputStream);
/*     */ 
/* 128 */     this.request_id = paramInputStream.read_ulong();
/* 129 */     this.reply_status = paramInputStream.read_long();
/* 130 */     isValidReplyStatus(this.reply_status);
/*     */     Object localObject;
/* 134 */     if (this.reply_status == 2)
/*     */     {
/* 136 */       localObject = paramInputStream.read_string();
/* 137 */       this.exClassName = ORBUtility.classNameOf((String)localObject);
/* 138 */       this.minorCode = paramInputStream.read_long();
/* 139 */       int i = paramInputStream.read_long();
/*     */ 
/* 141 */       switch (i) {
/*     */       case 0:
/* 143 */         this.completionStatus = CompletionStatus.COMPLETED_YES;
/* 144 */         break;
/*     */       case 1:
/* 146 */         this.completionStatus = CompletionStatus.COMPLETED_NO;
/* 147 */         break;
/*     */       case 2:
/* 149 */         this.completionStatus = CompletionStatus.COMPLETED_MAYBE;
/* 150 */         break;
/*     */       default:
/* 152 */         throw this.wrapper.badCompletionStatusInReply(CompletionStatus.COMPLETED_MAYBE, new Integer(i));
/*     */       }
/*     */ 
/*     */     }
/* 156 */     else if (this.reply_status != 1)
/*     */     {
/* 158 */       if (this.reply_status == 3) {
/* 159 */         localObject = (CDRInputStream)paramInputStream;
/* 160 */         this.ior = IORFactories.makeIOR((org.omg.CORBA_2_3.portable.InputStream)localObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(org.omg.CORBA.portable.OutputStream paramOutputStream)
/*     */   {
/* 167 */     super.write(paramOutputStream);
/* 168 */     if (this.service_contexts != null) {
/* 169 */       this.service_contexts.write((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream, GIOPVersion.V1_0);
/*     */     }
/*     */     else
/*     */     {
/* 173 */       ServiceContexts.writeNullServiceContext((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream);
/*     */     }
/*     */ 
/* 176 */     paramOutputStream.write_ulong(this.request_id);
/* 177 */     paramOutputStream.write_long(this.reply_status);
/*     */   }
/*     */ 
/*     */   public static void isValidReplyStatus(int paramInt)
/*     */   {
/* 183 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 188 */       break;
/*     */     default:
/* 190 */       ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get("rpc.protocol");
/*     */ 
/* 192 */       throw localORBUtilSystemException.illegalReplyStatus(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void callback(MessageHandler paramMessageHandler)
/*     */     throws IOException
/*     */   {
/* 199 */     paramMessageHandler.handleInput(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage_1_0
 * JD-Core Version:    0.6.2
 */