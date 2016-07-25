/*    */ package com.sun.xml.internal.ws.encoding;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Attachment;
/*    */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*    */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*    */ import com.sun.xml.internal.ws.message.MimeAttachmentSet;
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.WritableByteChannel;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public final class SwACodec extends MimeCodec
/*    */ {
/*    */   public SwACodec(SOAPVersion version, WSBinding binding, Codec rootCodec)
/*    */   {
/* 49 */     super(version, binding);
/* 50 */     this.rootCodec = rootCodec;
/*    */   }
/*    */ 
/*    */   private SwACodec(SwACodec that) {
/* 54 */     super(that);
/* 55 */     this.rootCodec = that.rootCodec.copy();
/*    */   }
/*    */ 
/*    */   protected void decode(MimeMultipartParser mpp, Packet packet)
/*    */     throws IOException
/*    */   {
/* 61 */     Attachment root = mpp.getRootPart();
/* 62 */     if ((this.rootCodec instanceof RootOnlyCodec)) {
/* 63 */       ((RootOnlyCodec)this.rootCodec).decode(root.asInputStream(), root.getContentType(), packet, new MimeAttachmentSet(mpp));
/*    */     } else {
/* 65 */       this.rootCodec.decode(root.asInputStream(), root.getContentType(), packet);
/* 66 */       Map atts = mpp.getAttachmentParts();
/* 67 */       for (Map.Entry att : atts.entrySet())
/* 68 */         packet.getMessage().getAttachments().add((Attachment)att.getValue());
/*    */     }
/*    */   }
/*    */ 
/*    */   public ContentType encode(Packet packet, WritableByteChannel buffer)
/*    */   {
/* 75 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   public SwACodec copy() {
/* 79 */     return new SwACodec(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.SwACodec
 * JD-Core Version:    0.6.2
 */