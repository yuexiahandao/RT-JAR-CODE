/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.message.DOMMessage;
/*     */ import com.sun.xml.internal.ws.message.EmptyMessageImpl;
/*     */ import com.sun.xml.internal.ws.message.jaxb.JAXBMessage;
/*     */ import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.util.JAXBSource;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.ws.LogicalMessage;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ class LogicalMessageImpl
/*     */   implements LogicalMessage
/*     */ {
/*     */   private Packet packet;
/*     */   protected JAXBContext defaultJaxbContext;
/*  72 */   private ImmutableLM lm = null;
/*     */ 
/*     */   public LogicalMessageImpl(JAXBContext defaultJaxbContext, Packet packet)
/*     */   {
/*  78 */     this.packet = packet;
/*  79 */     this.defaultJaxbContext = defaultJaxbContext;
/*     */   }
/*     */ 
/*     */   public Source getPayload() {
/*  83 */     if (this.lm == null) {
/*  84 */       Source payload = this.packet.getMessage().copy().readPayloadAsSource();
/*  85 */       if ((payload instanceof DOMSource)) {
/*  86 */         this.lm = createLogicalMessageImpl(payload);
/*     */       }
/*  88 */       return payload;
/*     */     }
/*  90 */     return this.lm.getPayload();
/*     */   }
/*     */ 
/*     */   public void setPayload(Source payload)
/*     */   {
/*  95 */     this.lm = createLogicalMessageImpl(payload);
/*     */   }
/*     */ 
/*     */   private ImmutableLM createLogicalMessageImpl(Source payload) {
/*  99 */     if (payload == null)
/* 100 */       this.lm = new EmptyLogicalMessageImpl();
/* 101 */     else if ((payload instanceof DOMSource))
/* 102 */       this.lm = new DOMLogicalMessageImpl((DOMSource)payload);
/*     */     else {
/* 104 */       this.lm = new SourceLogicalMessageImpl(payload);
/*     */     }
/* 106 */     return this.lm;
/*     */   }
/*     */ 
/*     */   public Object getPayload(JAXBContext context) {
/* 110 */     if (context == null) {
/* 111 */       context = this.defaultJaxbContext;
/*     */     }
/* 113 */     if (context == null)
/* 114 */       throw new WebServiceException("JAXBContext parameter cannot be null");
/*     */     Object o;
/* 117 */     if (this.lm == null) {
/*     */       try {
/* 119 */         o = this.packet.getMessage().copy().readPayloadAsJAXB(context.createUnmarshaller());
/*     */       } catch (JAXBException e) {
/* 121 */         throw new WebServiceException(e);
/*     */       }
/*     */     } else {
/* 124 */       o = this.lm.getPayload(context);
/* 125 */       this.lm = new JAXBLogicalMessageImpl(context, o);
/*     */     }
/* 127 */     return o;
/*     */   }
/*     */ 
/*     */   public void setPayload(Object payload, JAXBContext context) {
/* 131 */     if (context == null) {
/* 132 */       context = this.defaultJaxbContext;
/*     */     }
/* 134 */     if (payload == null)
/* 135 */       this.lm = new EmptyLogicalMessageImpl();
/*     */     else
/* 137 */       this.lm = new JAXBLogicalMessageImpl(context, payload);
/*     */   }
/*     */ 
/*     */   public boolean isPayloadModifed()
/*     */   {
/* 142 */     return this.lm != null;
/*     */   }
/*     */ 
/*     */   public Message getMessage(HeaderList headers, AttachmentSet attachments, WSBinding binding)
/*     */   {
/* 154 */     assert (isPayloadModifed());
/* 155 */     if (isPayloadModifed()) {
/* 156 */       return this.lm.getMessage(headers, attachments, binding);
/*     */     }
/* 158 */     return this.packet.getMessage();
/*     */   }
/*     */ 
/*     */   private class DOMLogicalMessageImpl extends LogicalMessageImpl.SourceLogicalMessageImpl
/*     */   {
/*     */     private DOMSource dom;
/*     */ 
/*     */     public DOMLogicalMessageImpl(DOMSource dom)
/*     */     {
/* 175 */       super(dom);
/* 176 */       this.dom = dom;
/*     */     }
/*     */ 
/*     */     public Source getPayload()
/*     */     {
/* 181 */       return this.dom;
/*     */     }
/*     */ 
/*     */     public Message getMessage(HeaderList headers, AttachmentSet attachments, WSBinding binding) {
/* 185 */       Node n = this.dom.getNode();
/* 186 */       if (n.getNodeType() == 9) {
/* 187 */         n = ((Document)n).getDocumentElement();
/*     */       }
/* 189 */       return new DOMMessage(binding.getSOAPVersion(), headers, (Element)n, attachments);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class EmptyLogicalMessageImpl extends LogicalMessageImpl.ImmutableLM {
/* 194 */     public EmptyLogicalMessageImpl() { super(null); }
/*     */ 
/*     */ 
/*     */     public Source getPayload()
/*     */     {
/* 200 */       return null;
/*     */     }
/*     */ 
/*     */     public Object getPayload(JAXBContext context)
/*     */     {
/* 205 */       return null;
/*     */     }
/*     */ 
/*     */     public Message getMessage(HeaderList headers, AttachmentSet attachments, WSBinding binding) {
/* 209 */       return new EmptyMessageImpl(headers, attachments, binding.getSOAPVersion()); }  } 
/*     */   private abstract class ImmutableLM { private ImmutableLM() {  } 
/*     */     public abstract Source getPayload();
/*     */ 
/*     */     public abstract Object getPayload(JAXBContext paramJAXBContext);
/*     */ 
/*     */     public abstract Message getMessage(HeaderList paramHeaderList, AttachmentSet paramAttachmentSet, WSBinding paramWSBinding); } 
/*     */   private class JAXBLogicalMessageImpl extends LogicalMessageImpl.ImmutableLM { private JAXBContext ctxt;
/*     */     private Object o;
/*     */ 
/* 217 */     public JAXBLogicalMessageImpl(JAXBContext ctxt, Object o) { super(null);
/* 218 */       this.ctxt = ctxt;
/* 219 */       this.o = o;
/*     */     }
/*     */ 
/*     */     public Source getPayload()
/*     */     {
/* 225 */       JAXBContext context = this.ctxt;
/* 226 */       if (context == null)
/* 227 */         context = LogicalMessageImpl.this.defaultJaxbContext;
/*     */       try
/*     */       {
/* 230 */         return new JAXBSource(context, this.o);
/*     */       } catch (JAXBException e) {
/* 232 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object getPayload(JAXBContext context)
/*     */     {
/*     */       try
/*     */       {
/* 242 */         Source payloadSrc = getPayload();
/* 243 */         if (payloadSrc == null)
/* 244 */           return null;
/* 245 */         Unmarshaller unmarshaller = context.createUnmarshaller();
/* 246 */         return unmarshaller.unmarshal(payloadSrc);
/*     */       } catch (JAXBException e) {
/* 248 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Message getMessage(HeaderList headers, AttachmentSet attachments, WSBinding binding) {
/* 253 */       return JAXBMessage.create((JAXBRIContext)this.ctxt, this.o, binding.getSOAPVersion(), headers, attachments);
/*     */     } }
/*     */ 
/*     */   private class SourceLogicalMessageImpl extends LogicalMessageImpl.ImmutableLM {
/*     */     private Source payloadSrc;
/*     */ 
/*     */     public SourceLogicalMessageImpl(Source source) {
/* 260 */       super(null);
/* 261 */       this.payloadSrc = source;
/*     */     }
/*     */ 
/*     */     public Source getPayload() {
/* 265 */       assert (!(this.payloadSrc instanceof DOMSource));
/*     */       try {
/* 267 */         Transformer transformer = XmlUtil.newTransformer();
/* 268 */         DOMResult domResult = new DOMResult();
/* 269 */         transformer.transform(this.payloadSrc, domResult);
/* 270 */         DOMSource dom = new DOMSource(domResult.getNode());
/* 271 */         LogicalMessageImpl.this.lm = new LogicalMessageImpl.DOMLogicalMessageImpl(LogicalMessageImpl.this, dom);
/* 272 */         this.payloadSrc = null;
/* 273 */         return dom;
/*     */       } catch (TransformerException te) {
/* 275 */         throw new WebServiceException(te);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object getPayload(JAXBContext context) {
/*     */       try {
/* 281 */         Source payloadSrc = getPayload();
/* 282 */         if (payloadSrc == null)
/* 283 */           return null;
/* 284 */         Unmarshaller unmarshaller = context.createUnmarshaller();
/* 285 */         return unmarshaller.unmarshal(payloadSrc);
/*     */       } catch (JAXBException e) {
/* 287 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Message getMessage(HeaderList headers, AttachmentSet attachments, WSBinding binding)
/*     */     {
/* 293 */       assert (this.payloadSrc != null);
/* 294 */       return new PayloadSourceMessage(headers, this.payloadSrc, attachments, binding.getSOAPVersion());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.LogicalMessageImpl
 * JD-Core Version:    0.6.2
 */