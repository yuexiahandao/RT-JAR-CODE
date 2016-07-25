/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*     */ import java.io.IOException;
/*     */ import org.omg.CORBA.Principal;
/*     */ 
/*     */ public final class RequestMessage_1_0 extends Message_1_0
/*     */   implements RequestMessage
/*     */ {
/*  45 */   private ORB orb = null;
/*  46 */   private ServiceContexts service_contexts = null;
/*  47 */   private int request_id = 0;
/*  48 */   private boolean response_expected = false;
/*  49 */   private byte[] object_key = null;
/*  50 */   private String operation = null;
/*  51 */   private Principal requesting_principal = null;
/*  52 */   private ObjectKey objectKey = null;
/*     */ 
/*     */   RequestMessage_1_0(ORB paramORB)
/*     */   {
/*  57 */     this.orb = paramORB;
/*     */   }
/*     */ 
/*     */   RequestMessage_1_0(ORB paramORB, ServiceContexts paramServiceContexts, int paramInt, boolean paramBoolean, byte[] paramArrayOfByte, String paramString, Principal paramPrincipal)
/*     */   {
/*  63 */     super(1195986768, false, (byte)0, 0);
/*  64 */     this.orb = paramORB;
/*  65 */     this.service_contexts = paramServiceContexts;
/*  66 */     this.request_id = paramInt;
/*  67 */     this.response_expected = paramBoolean;
/*  68 */     this.object_key = paramArrayOfByte;
/*  69 */     this.operation = paramString;
/*  70 */     this.requesting_principal = paramPrincipal;
/*     */   }
/*     */ 
/*     */   public ServiceContexts getServiceContexts()
/*     */   {
/*  76 */     return this.service_contexts;
/*     */   }
/*     */ 
/*     */   public int getRequestId() {
/*  80 */     return this.request_id;
/*     */   }
/*     */ 
/*     */   public boolean isResponseExpected() {
/*  84 */     return this.response_expected;
/*     */   }
/*     */ 
/*     */   public byte[] getReserved()
/*     */   {
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public ObjectKey getObjectKey() {
/*  93 */     if (this.objectKey == null)
/*     */     {
/*  95 */       this.objectKey = MessageBase.extractObjectKey(this.object_key, this.orb);
/*     */     }
/*     */ 
/*  98 */     return this.objectKey;
/*     */   }
/*     */ 
/*     */   public String getOperation() {
/* 102 */     return this.operation;
/*     */   }
/*     */ 
/*     */   public Principal getPrincipal() {
/* 106 */     return this.requesting_principal;
/*     */   }
/*     */ 
/*     */   public void setThreadPoolToUse(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 121 */     super.read(paramInputStream);
/* 122 */     this.service_contexts = new ServiceContexts((org.omg.CORBA_2_3.portable.InputStream)paramInputStream);
/*     */ 
/* 124 */     this.request_id = paramInputStream.read_ulong();
/* 125 */     this.response_expected = paramInputStream.read_boolean();
/* 126 */     int i = paramInputStream.read_long();
/* 127 */     this.object_key = new byte[i];
/* 128 */     paramInputStream.read_octet_array(this.object_key, 0, i);
/* 129 */     this.operation = paramInputStream.read_string();
/* 130 */     this.requesting_principal = paramInputStream.read_Principal();
/*     */   }
/*     */ 
/*     */   public void write(org.omg.CORBA.portable.OutputStream paramOutputStream) {
/* 134 */     super.write(paramOutputStream);
/* 135 */     if (this.service_contexts != null) {
/* 136 */       this.service_contexts.write((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream, GIOPVersion.V1_0);
/*     */     }
/*     */     else
/*     */     {
/* 140 */       ServiceContexts.writeNullServiceContext((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream);
/*     */     }
/*     */ 
/* 143 */     paramOutputStream.write_ulong(this.request_id);
/* 144 */     paramOutputStream.write_boolean(this.response_expected);
/* 145 */     nullCheck(this.object_key);
/* 146 */     paramOutputStream.write_long(this.object_key.length);
/* 147 */     paramOutputStream.write_octet_array(this.object_key, 0, this.object_key.length);
/* 148 */     paramOutputStream.write_string(this.operation);
/* 149 */     if (this.requesting_principal != null)
/* 150 */       paramOutputStream.write_Principal(this.requesting_principal);
/*     */     else
/* 152 */       paramOutputStream.write_long(0);
/*     */   }
/*     */ 
/*     */   public void callback(MessageHandler paramMessageHandler)
/*     */     throws IOException
/*     */   {
/* 159 */     paramMessageHandler.handleInput(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage_1_0
 * JD-Core Version:    0.6.2
 */