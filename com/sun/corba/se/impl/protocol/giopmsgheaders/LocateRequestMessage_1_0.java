/*    */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.ObjectKey;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import java.io.IOException;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class LocateRequestMessage_1_0 extends Message_1_0
/*    */   implements LocateRequestMessage
/*    */ {
/* 44 */   private ORB orb = null;
/* 45 */   private int request_id = 0;
/* 46 */   private byte[] object_key = null;
/* 47 */   private ObjectKey objectKey = null;
/*    */ 
/*    */   LocateRequestMessage_1_0(ORB paramORB)
/*    */   {
/* 52 */     this.orb = paramORB;
/*    */   }
/*    */ 
/*    */   LocateRequestMessage_1_0(ORB paramORB, int paramInt, byte[] paramArrayOfByte) {
/* 56 */     super(1195986768, false, (byte)3, 0);
/* 57 */     this.orb = paramORB;
/* 58 */     this.request_id = paramInt;
/* 59 */     this.object_key = paramArrayOfByte;
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 65 */     return this.request_id;
/*    */   }
/*    */ 
/*    */   public ObjectKey getObjectKey() {
/* 69 */     if (this.objectKey == null)
/*    */     {
/* 71 */       this.objectKey = MessageBase.extractObjectKey(this.object_key, this.orb);
/*    */     }
/*    */ 
/* 74 */     return this.objectKey;
/*    */   }
/*    */ 
/*    */   public void read(InputStream paramInputStream)
/*    */   {
/* 80 */     super.read(paramInputStream);
/* 81 */     this.request_id = paramInputStream.read_ulong();
/* 82 */     int i = paramInputStream.read_long();
/* 83 */     this.object_key = new byte[i];
/* 84 */     paramInputStream.read_octet_array(this.object_key, 0, i);
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream) {
/* 88 */     super.write(paramOutputStream);
/* 89 */     paramOutputStream.write_ulong(this.request_id);
/* 90 */     nullCheck(this.object_key);
/* 91 */     paramOutputStream.write_long(this.object_key.length);
/* 92 */     paramOutputStream.write_octet_array(this.object_key, 0, this.object_key.length);
/*    */   }
/*    */ 
/*    */   public void callback(MessageHandler paramMessageHandler)
/*    */     throws IOException
/*    */   {
/* 98 */     paramMessageHandler.handleInput(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage_1_0
 * JD-Core Version:    0.6.2
 */