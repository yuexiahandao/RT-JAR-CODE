/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*     */ import java.io.IOException;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.Principal;
/*     */ 
/*     */ public final class RequestMessage_1_1 extends Message_1_1
/*     */   implements RequestMessage
/*     */ {
/*  48 */   private ORB orb = null;
/*  49 */   private ORBUtilSystemException wrapper = null;
/*  50 */   private ServiceContexts service_contexts = null;
/*  51 */   private int request_id = 0;
/*  52 */   private boolean response_expected = false;
/*  53 */   private byte[] reserved = null;
/*  54 */   private byte[] object_key = null;
/*  55 */   private String operation = null;
/*  56 */   private Principal requesting_principal = null;
/*  57 */   private ObjectKey objectKey = null;
/*     */ 
/*     */   RequestMessage_1_1(ORB paramORB)
/*     */   {
/*  62 */     this.orb = paramORB;
/*  63 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */   }
/*     */ 
/*     */   RequestMessage_1_1(ORB paramORB, ServiceContexts paramServiceContexts, int paramInt, boolean paramBoolean, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString, Principal paramPrincipal)
/*     */   {
/*  71 */     super(1195986768, GIOPVersion.V1_1, (byte)0, (byte)0, 0);
/*     */ 
/*  73 */     this.orb = paramORB;
/*  74 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/*  76 */     this.service_contexts = paramServiceContexts;
/*  77 */     this.request_id = paramInt;
/*  78 */     this.response_expected = paramBoolean;
/*  79 */     this.reserved = paramArrayOfByte1;
/*  80 */     this.object_key = paramArrayOfByte2;
/*  81 */     this.operation = paramString;
/*  82 */     this.requesting_principal = paramPrincipal;
/*     */   }
/*     */ 
/*     */   public ServiceContexts getServiceContexts()
/*     */   {
/*  88 */     return this.service_contexts;
/*     */   }
/*     */ 
/*     */   public int getRequestId() {
/*  92 */     return this.request_id;
/*     */   }
/*     */ 
/*     */   public boolean isResponseExpected() {
/*  96 */     return this.response_expected;
/*     */   }
/*     */ 
/*     */   public byte[] getReserved() {
/* 100 */     return this.reserved;
/*     */   }
/*     */ 
/*     */   public ObjectKey getObjectKey() {
/* 104 */     if (this.objectKey == null)
/*     */     {
/* 106 */       this.objectKey = MessageBase.extractObjectKey(this.object_key, this.orb);
/*     */     }
/*     */ 
/* 109 */     return this.objectKey;
/*     */   }
/*     */ 
/*     */   public String getOperation() {
/* 113 */     return this.operation;
/*     */   }
/*     */ 
/*     */   public Principal getPrincipal() {
/* 117 */     return this.requesting_principal;
/*     */   }
/*     */ 
/*     */   public void read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 123 */     super.read(paramInputStream);
/* 124 */     this.service_contexts = new ServiceContexts((org.omg.CORBA_2_3.portable.InputStream)paramInputStream);
/*     */ 
/* 126 */     this.request_id = paramInputStream.read_ulong();
/* 127 */     this.response_expected = paramInputStream.read_boolean();
/* 128 */     this.reserved = new byte[3];
/* 129 */     for (int i = 0; i < 3; i++) {
/* 130 */       this.reserved[i] = paramInputStream.read_octet();
/*     */     }
/* 132 */     i = paramInputStream.read_long();
/* 133 */     this.object_key = new byte[i];
/* 134 */     paramInputStream.read_octet_array(this.object_key, 0, i);
/* 135 */     this.operation = paramInputStream.read_string();
/* 136 */     this.requesting_principal = paramInputStream.read_Principal();
/*     */   }
/*     */ 
/*     */   public void write(org.omg.CORBA.portable.OutputStream paramOutputStream) {
/* 140 */     super.write(paramOutputStream);
/* 141 */     if (this.service_contexts != null) {
/* 142 */       this.service_contexts.write((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream, GIOPVersion.V1_1);
/*     */     }
/*     */     else
/*     */     {
/* 146 */       ServiceContexts.writeNullServiceContext((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream);
/*     */     }
/*     */ 
/* 149 */     paramOutputStream.write_ulong(this.request_id);
/* 150 */     paramOutputStream.write_boolean(this.response_expected);
/* 151 */     nullCheck(this.reserved);
/* 152 */     if (this.reserved.length != 3) {
/* 153 */       throw this.wrapper.badReservedLength(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 156 */     for (int i = 0; i < 3; i++) {
/* 157 */       paramOutputStream.write_octet(this.reserved[i]);
/*     */     }
/* 159 */     nullCheck(this.object_key);
/* 160 */     paramOutputStream.write_long(this.object_key.length);
/* 161 */     paramOutputStream.write_octet_array(this.object_key, 0, this.object_key.length);
/* 162 */     paramOutputStream.write_string(this.operation);
/* 163 */     if (this.requesting_principal != null)
/* 164 */       paramOutputStream.write_Principal(this.requesting_principal);
/*     */     else
/* 166 */       paramOutputStream.write_long(0);
/*     */   }
/*     */ 
/*     */   public void callback(MessageHandler paramMessageHandler)
/*     */     throws IOException
/*     */   {
/* 173 */     paramMessageHandler.handleInput(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage_1_1
 * JD-Core Version:    0.6.2
 */