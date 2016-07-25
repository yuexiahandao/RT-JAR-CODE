/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.IOException;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class LocateRequestMessage_1_1 extends Message_1_1
/*     */   implements LocateRequestMessage
/*     */ {
/*  45 */   private ORB orb = null;
/*  46 */   private int request_id = 0;
/*  47 */   private byte[] object_key = null;
/*  48 */   private ObjectKey objectKey = null;
/*     */ 
/*     */   LocateRequestMessage_1_1(ORB paramORB)
/*     */   {
/*  53 */     this.orb = paramORB;
/*     */   }
/*     */ 
/*     */   LocateRequestMessage_1_1(ORB paramORB, int paramInt, byte[] paramArrayOfByte) {
/*  57 */     super(1195986768, GIOPVersion.V1_1, (byte)0, (byte)3, 0);
/*     */ 
/*  59 */     this.orb = paramORB;
/*  60 */     this.request_id = paramInt;
/*  61 */     this.object_key = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/*  67 */     return this.request_id;
/*     */   }
/*     */ 
/*     */   public ObjectKey getObjectKey() {
/*  71 */     if (this.objectKey == null)
/*     */     {
/*  73 */       this.objectKey = MessageBase.extractObjectKey(this.object_key, this.orb);
/*     */     }
/*     */ 
/*  76 */     return this.objectKey;
/*     */   }
/*     */ 
/*     */   public void read(InputStream paramInputStream)
/*     */   {
/*  82 */     super.read(paramInputStream);
/*  83 */     this.request_id = paramInputStream.read_ulong();
/*  84 */     int i = paramInputStream.read_long();
/*  85 */     this.object_key = new byte[i];
/*  86 */     paramInputStream.read_octet_array(this.object_key, 0, i);
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream) {
/*  90 */     super.write(paramOutputStream);
/*  91 */     paramOutputStream.write_ulong(this.request_id);
/*  92 */     nullCheck(this.object_key);
/*  93 */     paramOutputStream.write_long(this.object_key.length);
/*  94 */     paramOutputStream.write_octet_array(this.object_key, 0, this.object_key.length);
/*     */   }
/*     */ 
/*     */   public void callback(MessageHandler paramMessageHandler)
/*     */     throws IOException
/*     */   {
/* 100 */     paramMessageHandler.handleInput(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage_1_1
 * JD-Core Version:    0.6.2
 */