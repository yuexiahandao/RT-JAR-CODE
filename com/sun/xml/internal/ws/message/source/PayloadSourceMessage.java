/*    */ package com.sun.xml.internal.ws.message.source;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*    */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*    */ import com.sun.xml.internal.ws.message.AttachmentSetImpl;
/*    */ import com.sun.xml.internal.ws.message.stream.PayloadStreamReaderMessage;
/*    */ import com.sun.xml.internal.ws.streaming.SourceReaderFactory;
/*    */ import javax.xml.transform.Source;
/*    */ 
/*    */ public class PayloadSourceMessage extends PayloadStreamReaderMessage
/*    */ {
/*    */   public PayloadSourceMessage(@Nullable HeaderList headers, @NotNull Source payload, @NotNull AttachmentSet attSet, @NotNull SOAPVersion soapVersion)
/*    */   {
/* 51 */     super(headers, SourceReaderFactory.createSourceReader(payload, true), attSet, soapVersion);
/*    */   }
/*    */ 
/*    */   public PayloadSourceMessage(Source s, SOAPVersion soapVer)
/*    */   {
/* 56 */     this(null, s, new AttachmentSetImpl(), soapVer);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.source.PayloadSourceMessage
 * JD-Core Version:    0.6.2
 */