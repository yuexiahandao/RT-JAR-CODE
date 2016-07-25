/*     */ package com.sun.xml.internal.ws.client.sei;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.client.RequestContext;
/*     */ import com.sun.xml.internal.ws.client.ResponseContextReceiver;
/*     */ import com.sun.xml.internal.ws.encoding.soap.DeserializationException;
/*     */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*     */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ 
/*     */ final class SyncMethodHandler extends SEIMethodHandler
/*     */ {
/*     */   private final ResponseBuilder responseBuilder;
/*     */ 
/*     */   SyncMethodHandler(SEIStub owner, JavaMethodImpl method)
/*     */   {
/*  73 */     super(owner, method);
/*  74 */     this.responseBuilder = buildResponseBuilder(method, ValueSetterFactory.SYNC);
/*     */   }
/*     */ 
/*     */   Object invoke(Object proxy, Object[] args) throws Throwable {
/*  78 */     return invoke(proxy, args, this.owner.requestContext, this.owner); } 
/*     */   Object invoke(Object proxy, Object[] args, RequestContext rc, ResponseContextReceiver receiver) throws Throwable { // Byte code:
/*     */     //   0: new 73	com/sun/xml/internal/ws/api/message/Packet
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: aload_2
/*     */     //   6: invokevirtual 168	com/sun/xml/internal/ws/client/sei/SyncMethodHandler:createRequestMessage	([Ljava/lang/Object;)Lcom/sun/xml/internal/ws/api/message/Message;
/*     */     //   9: invokespecial 163	com/sun/xml/internal/ws/api/message/Packet:<init>	(Lcom/sun/xml/internal/ws/api/message/Message;)V
/*     */     //   12: astore 5
/*     */     //   14: aload 5
/*     */     //   16: aload_0
/*     */     //   17: getfield 156	com/sun/xml/internal/ws/client/sei/SyncMethodHandler:soapAction	Ljava/lang/String;
/*     */     //   20: putfield 150	com/sun/xml/internal/ws/api/message/Packet:soapAction	Ljava/lang/String;
/*     */     //   23: aload 5
/*     */     //   25: aload_0
/*     */     //   26: getfield 152	com/sun/xml/internal/ws/client/sei/SyncMethodHandler:isOneWay	Z
/*     */     //   29: ifne +7 -> 36
/*     */     //   32: iconst_1
/*     */     //   33: goto +4 -> 37
/*     */     //   36: iconst_0
/*     */     //   37: invokestatic 175	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
/*     */     //   40: putfield 149	com/sun/xml/internal/ws/api/message/Packet:expectReply	Ljava/lang/Boolean;
/*     */     //   43: aload 5
/*     */     //   45: invokevirtual 162	com/sun/xml/internal/ws/api/message/Packet:getMessage	()Lcom/sun/xml/internal/ws/api/message/Message;
/*     */     //   48: aload_0
/*     */     //   49: getfield 152	com/sun/xml/internal/ws/client/sei/SyncMethodHandler:isOneWay	Z
/*     */     //   52: invokevirtual 161	com/sun/xml/internal/ws/api/message/Message:assertOneWay	(Z)V
/*     */     //   55: aload 5
/*     */     //   57: aload_0
/*     */     //   58: getfield 155	com/sun/xml/internal/ws/client/sei/SyncMethodHandler:javaMethod	Lcom/sun/xml/internal/ws/model/JavaMethodImpl;
/*     */     //   61: invokevirtual 174	com/sun/xml/internal/ws/model/JavaMethodImpl:getOperation	()Lcom/sun/xml/internal/ws/api/model/wsdl/WSDLBoundOperation;
/*     */     //   64: invokeinterface 176 1 0
/*     */     //   69: invokevirtual 164	com/sun/xml/internal/ws/api/message/Packet:setWSDLOperation	(Ljavax/xml/namespace/QName;)V
/*     */     //   72: aload_0
/*     */     //   73: getfield 154	com/sun/xml/internal/ws/client/sei/SyncMethodHandler:owner	Lcom/sun/xml/internal/ws/client/sei/SEIStub;
/*     */     //   76: aload 5
/*     */     //   78: aload_3
/*     */     //   79: aload 4
/*     */     //   81: invokevirtual 167	com/sun/xml/internal/ws/client/sei/SEIStub:doProcess	(Lcom/sun/xml/internal/ws/api/message/Packet;Lcom/sun/xml/internal/ws/client/RequestContext;Lcom/sun/xml/internal/ws/client/ResponseContextReceiver;)Lcom/sun/xml/internal/ws/api/message/Packet;
/*     */     //   84: astore 6
/*     */     //   86: aload 6
/*     */     //   88: invokevirtual 162	com/sun/xml/internal/ws/api/message/Packet:getMessage	()Lcom/sun/xml/internal/ws/api/message/Message;
/*     */     //   91: astore 7
/*     */     //   93: aload 7
/*     */     //   95: ifnonnull +5 -> 100
/*     */     //   98: aconst_null
/*     */     //   99: areturn
/*     */     //   100: aload 7
/*     */     //   102: invokevirtual 160	com/sun/xml/internal/ws/api/message/Message:isFault	()Z
/*     */     //   105: ifeq +20 -> 125
/*     */     //   108: aload 7
/*     */     //   110: invokestatic 172	com/sun/xml/internal/ws/fault/SOAPFaultBuilder:create	(Lcom/sun/xml/internal/ws/api/message/Message;)Lcom/sun/xml/internal/ws/fault/SOAPFaultBuilder;
/*     */     //   113: astore 8
/*     */     //   115: aload 8
/*     */     //   117: aload_0
/*     */     //   118: getfield 157	com/sun/xml/internal/ws/client/sei/SyncMethodHandler:checkedExceptions	Ljava/util/Map;
/*     */     //   121: invokevirtual 173	com/sun/xml/internal/ws/fault/SOAPFaultBuilder:createException	(Ljava/util/Map;)Ljava/lang/Throwable;
/*     */     //   124: athrow
/*     */     //   125: aload_0
/*     */     //   126: getfield 153	com/sun/xml/internal/ws/client/sei/SyncMethodHandler:responseBuilder	Lcom/sun/xml/internal/ws/client/sei/ResponseBuilder;
/*     */     //   129: aload 7
/*     */     //   131: aload_2
/*     */     //   132: invokevirtual 165	com/sun/xml/internal/ws/client/sei/ResponseBuilder:readResponse	(Lcom/sun/xml/internal/ws/api/message/Message;[Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   135: areturn
/*     */     //   136: astore 8
/*     */     //   138: new 83	com/sun/xml/internal/ws/encoding/soap/DeserializationException
/*     */     //   141: dup
/*     */     //   142: ldc 1
/*     */     //   144: iconst_1
/*     */     //   145: anewarray 87	java/lang/Object
/*     */     //   148: dup
/*     */     //   149: iconst_0
/*     */     //   150: aload 8
/*     */     //   152: aastore
/*     */     //   153: invokespecial 171	com/sun/xml/internal/ws/encoding/soap/DeserializationException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   156: athrow
/*     */     //   157: astore 8
/*     */     //   159: new 83	com/sun/xml/internal/ws/encoding/soap/DeserializationException
/*     */     //   162: dup
/*     */     //   163: ldc 1
/*     */     //   165: iconst_1
/*     */     //   166: anewarray 87	java/lang/Object
/*     */     //   169: dup
/*     */     //   170: iconst_0
/*     */     //   171: aload 8
/*     */     //   173: aastore
/*     */     //   174: invokespecial 171	com/sun/xml/internal/ws/encoding/soap/DeserializationException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   177: athrow
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   100	135	136	javax/xml/bind/JAXBException
/*     */     //   100	135	157	javax/xml/stream/XMLStreamException } 
/* 120 */   ValueGetterFactory getValueGetterFactory() { return ValueGetterFactory.SYNC; }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.SyncMethodHandler
 * JD-Core Version:    0.6.2
 */