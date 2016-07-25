/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDRInputStream;
/*     */ import com.sun.corba.se.impl.encoding.CDROutputStream;
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
/*     */ public final class ReplyMessage_1_2 extends Message_1_2
/*     */   implements ReplyMessage
/*     */ {
/*  62 */   private ORB orb = null;
/*  63 */   private ORBUtilSystemException wrapper = null;
/*  64 */   private int reply_status = 0;
/*  65 */   private ServiceContexts service_contexts = null;
/*  66 */   private IOR ior = null;
/*  67 */   private String exClassName = null;
/*  68 */   private int minorCode = 0;
/*  69 */   private CompletionStatus completionStatus = null;
/*  70 */   private short addrDisposition = 0;
/*     */ 
/*     */   ReplyMessage_1_2(ORB paramORB)
/*     */   {
/*  75 */     this.orb = paramORB;
/*  76 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */   }
/*     */ 
/*     */   ReplyMessage_1_2(ORB paramORB, int paramInt1, int paramInt2, ServiceContexts paramServiceContexts, IOR paramIOR)
/*     */   {
/*  82 */     super(1195986768, GIOPVersion.V1_2, (byte)0, (byte)1, 0);
/*     */ 
/*  84 */     this.orb = paramORB;
/*  85 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/*  87 */     this.request_id = paramInt1;
/*  88 */     this.reply_status = paramInt2;
/*  89 */     this.service_contexts = paramServiceContexts;
/*  90 */     this.ior = paramIOR;
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/*  96 */     return this.request_id;
/*     */   }
/*     */ 
/*     */   public int getReplyStatus() {
/* 100 */     return this.reply_status;
/*     */   }
/*     */ 
/*     */   public short getAddrDisposition() {
/* 104 */     return this.addrDisposition;
/*     */   }
/*     */ 
/*     */   public ServiceContexts getServiceContexts() {
/* 108 */     return this.service_contexts;
/*     */   }
/*     */ 
/*     */   public void setServiceContexts(ServiceContexts paramServiceContexts) {
/* 112 */     this.service_contexts = paramServiceContexts;
/*     */   }
/*     */ 
/*     */   public SystemException getSystemException(String paramString) {
/* 116 */     return MessageBase.getSystemException(this.exClassName, this.minorCode, this.completionStatus, paramString, this.wrapper);
/*     */   }
/*     */ 
/*     */   public IOR getIOR()
/*     */   {
/* 121 */     return this.ior;
/*     */   }
/*     */ 
/*     */   public void setIOR(IOR paramIOR) {
/* 125 */     this.ior = paramIOR;
/*     */   }
/*     */ 
/*     */   public void read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 131 */     super.read(paramInputStream);
/* 132 */     this.request_id = paramInputStream.read_ulong();
/* 133 */     this.reply_status = paramInputStream.read_long();
/* 134 */     isValidReplyStatus(this.reply_status);
/* 135 */     this.service_contexts = new ServiceContexts((org.omg.CORBA_2_3.portable.InputStream)paramInputStream);
/*     */ 
/* 143 */     ((CDRInputStream)paramInputStream).setHeaderPadding(true);
/*     */     Object localObject;
/* 148 */     if (this.reply_status == 2)
/*     */     {
/* 150 */       localObject = paramInputStream.read_string();
/* 151 */       this.exClassName = ORBUtility.classNameOf((String)localObject);
/* 152 */       this.minorCode = paramInputStream.read_long();
/* 153 */       int i = paramInputStream.read_long();
/*     */ 
/* 155 */       switch (i) {
/*     */       case 0:
/* 157 */         this.completionStatus = CompletionStatus.COMPLETED_YES;
/* 158 */         break;
/*     */       case 1:
/* 160 */         this.completionStatus = CompletionStatus.COMPLETED_NO;
/* 161 */         break;
/*     */       case 2:
/* 163 */         this.completionStatus = CompletionStatus.COMPLETED_MAYBE;
/* 164 */         break;
/*     */       default:
/* 166 */         throw this.wrapper.badCompletionStatusInReply(CompletionStatus.COMPLETED_MAYBE, new Integer(i));
/*     */       }
/*     */ 
/*     */     }
/* 170 */     else if (this.reply_status != 1)
/*     */     {
/* 172 */       if ((this.reply_status == 3) || (this.reply_status == 4))
/*     */       {
/* 174 */         localObject = (CDRInputStream)paramInputStream;
/* 175 */         this.ior = IORFactories.makeIOR((org.omg.CORBA_2_3.portable.InputStream)localObject);
/* 176 */       } else if (this.reply_status == 5)
/*     */       {
/* 180 */         this.addrDisposition = AddressingDispositionHelper.read(paramInputStream);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(org.omg.CORBA.portable.OutputStream paramOutputStream)
/*     */   {
/* 188 */     super.write(paramOutputStream);
/* 189 */     paramOutputStream.write_ulong(this.request_id);
/* 190 */     paramOutputStream.write_long(this.reply_status);
/* 191 */     if (this.service_contexts != null) {
/* 192 */       this.service_contexts.write((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream, GIOPVersion.V1_2);
/*     */     }
/*     */     else
/*     */     {
/* 196 */       ServiceContexts.writeNullServiceContext((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream);
/*     */     }
/*     */ 
/* 205 */     ((CDROutputStream)paramOutputStream).setHeaderPadding(true);
/*     */   }
/*     */ 
/*     */   public static void isValidReplyStatus(int paramInt)
/*     */   {
/* 212 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/* 219 */       break;
/*     */     default:
/* 221 */       ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get("rpc.protocol");
/*     */ 
/* 223 */       throw localORBUtilSystemException.illegalReplyStatus(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void callback(MessageHandler paramMessageHandler)
/*     */     throws IOException
/*     */   {
/* 230 */     paramMessageHandler.handleInput(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage_1_2
 * JD-Core Version:    0.6.2
 */