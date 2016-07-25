/*    */ package com.sun.xml.internal.ws.api.pipe;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ 
/*    */ public abstract class Codecs
/*    */ {
/*    */   @NotNull
/*    */   public static SOAPBindingCodec createSOAPBindingCodec(WSBinding binding, StreamSOAPCodec xmlEnvelopeCodec)
/*    */   {
/* 67 */     return new com.sun.xml.internal.ws.encoding.SOAPBindingCodec(binding, xmlEnvelopeCodec);
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public static StreamSOAPCodec createSOAPEnvelopeXmlCodec(@NotNull SOAPVersion version)
/*    */   {
/* 80 */     return com.sun.xml.internal.ws.encoding.StreamSOAPCodec.create(version);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.Codecs
 * JD-Core Version:    0.6.2
 */