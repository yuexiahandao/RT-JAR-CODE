/*    */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class FragmentMessage_1_1 extends Message_1_1
/*    */   implements FragmentMessage
/*    */ {
/*    */   FragmentMessage_1_1()
/*    */   {
/*    */   }
/*    */ 
/*    */   FragmentMessage_1_1(Message_1_1 paramMessage_1_1)
/*    */   {
/* 42 */     this.magic = paramMessage_1_1.magic;
/* 43 */     this.GIOP_version = paramMessage_1_1.GIOP_version;
/* 44 */     this.flags = paramMessage_1_1.flags;
/* 45 */     this.message_type = 7;
/* 46 */     this.message_size = 0;
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 52 */     return -1;
/*    */   }
/*    */ 
/*    */   public int getHeaderLength() {
/* 56 */     return 12;
/*    */   }
/*    */ 
/*    */   public void read(InputStream paramInputStream)
/*    */   {
/* 66 */     super.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream)
/*    */   {
/* 71 */     super.write(paramOutputStream);
/*    */   }
/*    */ 
/*    */   public void callback(MessageHandler paramMessageHandler)
/*    */     throws IOException
/*    */   {
/* 77 */     paramMessageHandler.handleInput(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage_1_1
 * JD-Core Version:    0.6.2
 */