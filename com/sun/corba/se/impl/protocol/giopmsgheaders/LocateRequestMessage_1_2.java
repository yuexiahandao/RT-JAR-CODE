/*    */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.ObjectKey;
/*    */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import java.io.IOException;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class LocateRequestMessage_1_2 extends Message_1_2
/*    */   implements LocateRequestMessage
/*    */ {
/* 44 */   private ORB orb = null;
/* 45 */   private ObjectKey objectKey = null;
/* 46 */   private TargetAddress target = null;
/*    */ 
/*    */   LocateRequestMessage_1_2(ORB paramORB)
/*    */   {
/* 51 */     this.orb = paramORB;
/*    */   }
/*    */ 
/*    */   LocateRequestMessage_1_2(ORB paramORB, int paramInt, TargetAddress paramTargetAddress) {
/* 55 */     super(1195986768, GIOPVersion.V1_2, (byte)0, (byte)3, 0);
/*    */ 
/* 57 */     this.orb = paramORB;
/* 58 */     this.request_id = paramInt;
/* 59 */     this.target = paramTargetAddress;
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
/* 71 */       this.objectKey = MessageBase.extractObjectKey(this.target, this.orb);
/*    */     }
/*    */ 
/* 74 */     return this.objectKey;
/*    */   }
/*    */ 
/*    */   public void read(InputStream paramInputStream)
/*    */   {
/* 80 */     super.read(paramInputStream);
/* 81 */     this.request_id = paramInputStream.read_ulong();
/* 82 */     this.target = TargetAddressHelper.read(paramInputStream);
/* 83 */     getObjectKey();
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream) {
/* 87 */     super.write(paramOutputStream);
/* 88 */     paramOutputStream.write_ulong(this.request_id);
/* 89 */     nullCheck(this.target);
/* 90 */     TargetAddressHelper.write(paramOutputStream, this.target);
/*    */   }
/*    */ 
/*    */   public void callback(MessageHandler paramMessageHandler)
/*    */     throws IOException
/*    */   {
/* 96 */     paramMessageHandler.handleInput(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage_1_2
 * JD-Core Version:    0.6.2
 */