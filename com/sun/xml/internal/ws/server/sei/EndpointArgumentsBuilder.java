/*     */ package com.sun.xml.internal.ws.server.sei;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.CompositeStructure;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.RawAccessor;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import com.sun.xml.internal.ws.encoding.DataHandlerDataSource;
/*     */ import com.sun.xml.internal.ws.encoding.StringDataContentHandler;
/*     */ import com.sun.xml.internal.ws.message.AttachmentUnmarshallerImpl;
/*     */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*     */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*     */ import com.sun.xml.internal.ws.model.WrapperParameter;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import java.awt.Image;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.jws.WebParam.Mode;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFactory;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.soap.SOAPFaultException;
/*     */ 
/*     */ abstract class EndpointArgumentsBuilder
/*     */ {
/* 108 */   public static EndpointArgumentsBuilder NONE = new None(null);
/*     */ 
/* 122 */   private static final Map<Class, Object> primitiveUninitializedValues = new HashMap();
/*     */ 
/*     */   abstract void readRequest(Message paramMessage, Object[] paramArrayOfObject)
/*     */     throws JAXBException, XMLStreamException;
/*     */ 
/*     */   public static Object getVMUninitializedValue(Type type)
/*     */   {
/* 119 */     return primitiveUninitializedValues.get(type);
/*     */   }
/*     */ 
/*     */   public static final String getWSDLPartName(Attachment att)
/*     */   {
/* 375 */     String cId = att.getContentId();
/*     */ 
/* 377 */     int index = cId.lastIndexOf('@', cId.length());
/* 378 */     if (index == -1) {
/* 379 */       return null;
/*     */     }
/* 381 */     String localPart = cId.substring(0, index);
/* 382 */     index = localPart.lastIndexOf('=', localPart.length());
/* 383 */     if (index == -1)
/* 384 */       return null;
/*     */     try
/*     */     {
/* 387 */       return URLDecoder.decode(localPart.substring(0, index), "UTF-8");
/*     */     } catch (UnsupportedEncodingException e) {
/* 389 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isXMLMimeType(String mimeType)
/*     */   {
/* 672 */     return (mimeType.equals("text/xml")) || (mimeType.equals("application/xml"));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 125 */     Map m = primitiveUninitializedValues;
/* 126 */     m.put(Integer.TYPE, Integer.valueOf(0));
/* 127 */     m.put(Character.TYPE, Character.valueOf('\000'));
/* 128 */     m.put(Byte.TYPE, Byte.valueOf((byte)0));
/* 129 */     m.put(Short.TYPE, Short.valueOf((short)0));
/* 130 */     m.put(Long.TYPE, Long.valueOf(0L));
/* 131 */     m.put(Float.TYPE, Float.valueOf(0.0F));
/* 132 */     m.put(Double.TYPE, Double.valueOf(0.0D));
/*     */   }
/*     */ 
/*     */   static abstract class AttachmentBuilder extends EndpointArgumentsBuilder
/*     */   {
/*     */     protected final EndpointValueSetter setter;
/*     */     protected final ParameterImpl param;
/*     */     protected final String pname;
/*     */     protected final String pname1;
/*     */ 
/*     */     AttachmentBuilder(ParameterImpl param, EndpointValueSetter setter)
/*     */     {
/* 196 */       this.setter = setter;
/* 197 */       this.param = param;
/* 198 */       this.pname = param.getPartName();
/* 199 */       this.pname1 = ("<" + this.pname);
/*     */     }
/*     */ 
/*     */     public static EndpointArgumentsBuilder createAttachmentBuilder(ParameterImpl param, EndpointValueSetter setter)
/*     */     {
/* 212 */       Class type = (Class)param.getTypeReference().type;
/* 213 */       if (DataHandler.class.isAssignableFrom(type))
/* 214 */         return new EndpointArgumentsBuilder.DataHandlerBuilder(param, setter);
/* 215 */       if ([B.class == type)
/* 216 */         return new EndpointArgumentsBuilder.ByteArrayBuilder(param, setter);
/* 217 */       if (Source.class.isAssignableFrom(type))
/* 218 */         return new EndpointArgumentsBuilder.SourceBuilder(param, setter);
/* 219 */       if (Image.class.isAssignableFrom(type))
/* 220 */         return new EndpointArgumentsBuilder.ImageBuilder(param, setter);
/* 221 */       if (InputStream.class == type)
/* 222 */         return new EndpointArgumentsBuilder.InputStreamBuilder(param, setter);
/* 223 */       if (EndpointArgumentsBuilder.isXMLMimeType(param.getBinding().getMimeType()))
/* 224 */         return new EndpointArgumentsBuilder.JAXBBuilder(param, setter);
/* 225 */       if (String.class.isAssignableFrom(type)) {
/* 226 */         return new EndpointArgumentsBuilder.StringBuilder(param, setter);
/*     */       }
/* 228 */       throw new UnsupportedOperationException("Unknown Type=" + type + " Attachment is not mapped.");
/*     */     }
/*     */ 
/*     */     public void readRequest(Message msg, Object[] args) throws JAXBException, XMLStreamException
/*     */     {
/* 233 */       boolean foundAttachment = false;
/*     */ 
/* 235 */       for (Attachment att : msg.getAttachments()) {
/* 236 */         String part = getWSDLPartName(att);
/* 237 */         if (part != null)
/*     */         {
/* 240 */           if ((part.equals(this.pname)) || (part.equals(this.pname1))) {
/* 241 */             foundAttachment = true;
/* 242 */             mapAttachment(att, args);
/* 243 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 246 */       if (!foundAttachment)
/* 247 */         throw new WebServiceException("Missing Attachment for " + this.pname);
/*     */     }
/*     */ 
/*     */     abstract void mapAttachment(Attachment paramAttachment, Object[] paramArrayOfObject)
/*     */       throws JAXBException;
/*     */   }
/*     */ 
/*     */   static final class Body extends EndpointArgumentsBuilder
/*     */   {
/*     */     private final Bridge<?> bridge;
/*     */     private final EndpointValueSetter setter;
/*     */ 
/*     */     public Body(Bridge<?> bridge, EndpointValueSetter setter)
/*     */     {
/* 472 */       this.bridge = bridge;
/* 473 */       this.setter = setter;
/*     */     }
/*     */ 
/*     */     public void readRequest(Message msg, Object[] args) throws JAXBException {
/* 477 */       this.setter.put(msg.readPayloadAsJAXB(this.bridge), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class ByteArrayBuilder extends EndpointArgumentsBuilder.AttachmentBuilder
/*     */   {
/*     */     ByteArrayBuilder(ParameterImpl param, EndpointValueSetter setter)
/*     */     {
/* 266 */       super(setter);
/*     */     }
/*     */ 
/*     */     void mapAttachment(Attachment att, Object[] args) {
/* 270 */       this.setter.put(att.asByteArray(), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Composite extends EndpointArgumentsBuilder
/*     */   {
/*     */     private final EndpointArgumentsBuilder[] builders;
/*     */ 
/*     */     public Composite(EndpointArgumentsBuilder[] builders)
/*     */     {
/* 171 */       this.builders = builders;
/*     */     }
/*     */ 
/*     */     public Composite(Collection<? extends EndpointArgumentsBuilder> builders) {
/* 175 */       this((EndpointArgumentsBuilder[])builders.toArray(new EndpointArgumentsBuilder[builders.size()]));
/*     */     }
/*     */ 
/*     */     public void readRequest(Message msg, Object[] args) throws JAXBException, XMLStreamException {
/* 179 */       for (EndpointArgumentsBuilder builder : this.builders)
/* 180 */         builder.readRequest(msg, args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class DataHandlerBuilder extends EndpointArgumentsBuilder.AttachmentBuilder
/*     */   {
/*     */     DataHandlerBuilder(ParameterImpl param, EndpointValueSetter setter)
/*     */     {
/* 256 */       super(setter);
/*     */     }
/*     */ 
/*     */     void mapAttachment(Attachment att, Object[] args) {
/* 260 */       this.setter.put(att.asDataHandler(), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class DocLit extends EndpointArgumentsBuilder
/*     */   {
/*     */     private final PartBuilder[] parts;
/*     */     private final Bridge wrapper;
/*     */     private final QName wrapperName;
/*     */ 
/*     */     public DocLit(WrapperParameter wp, WebParam.Mode skipMode)
/*     */     {
/* 495 */       this.wrapperName = wp.getName();
/* 496 */       this.wrapper = wp.getBridge();
/* 497 */       Class wrapperType = (Class)this.wrapper.getTypeReference().type;
/*     */ 
/* 499 */       List parts = new ArrayList();
/*     */ 
/* 501 */       List children = wp.getWrapperChildren();
/* 502 */       for (ParameterImpl p : children) {
/* 503 */         if (p.getMode() != skipMode)
/*     */         {
/* 510 */           QName name = p.getName();
/*     */           try {
/* 512 */             parts.add(new PartBuilder(wp.getOwner().getJAXBContext().getElementPropertyAccessor(wrapperType, name.getNamespaceURI(), p.getName().getLocalPart()), EndpointValueSetter.get(p)));
/*     */ 
/* 521 */             if ((!$assertionsDisabled) && (p.getBinding() != ParameterBinding.BODY)) throw new AssertionError(); 
/*     */           }
/* 523 */           catch (JAXBException e) { throw new WebServiceException(wrapperType + " do not have a property of the name " + name, e); }
/*     */ 
/*     */         }
/*     */       }
/*     */ 
/* 528 */       this.parts = ((PartBuilder[])parts.toArray(new PartBuilder[parts.size()]));
/*     */     }
/*     */ 
/*     */     public void readRequest(Message msg, Object[] args) throws JAXBException, XMLStreamException
/*     */     {
/* 533 */       if (this.parts.length > 0) {
/* 534 */         if (!msg.hasPayload()) {
/* 535 */           throw new WebServiceException("No payload. Expecting payload with " + this.wrapperName + " element");
/*     */         }
/* 537 */         XMLStreamReader reader = msg.readPayload();
/* 538 */         XMLStreamReaderUtil.verifyTag(reader, this.wrapperName);
/* 539 */         Object wrapperBean = this.wrapper.unmarshal(reader, msg.getAttachments() != null ? new AttachmentUnmarshallerImpl(msg.getAttachments()) : null);
/*     */         try
/*     */         {
/* 543 */           for (PartBuilder part : this.parts)
/* 544 */             part.readRequest(args, wrapperBean);
/*     */         }
/*     */         catch (AccessorException e)
/*     */         {
/* 548 */           throw new WebServiceException(e);
/*     */         }
/*     */ 
/* 552 */         reader.close();
/* 553 */         XMLStreamReaderFactory.recycle(reader);
/*     */       } else {
/* 555 */         msg.consume();
/*     */       }
/*     */     }
/*     */ 
/*     */     static final class PartBuilder
/*     */     {
/*     */       private final RawAccessor accessor;
/*     */       private final EndpointValueSetter setter;
/*     */ 
/*     */       public PartBuilder(RawAccessor accessor, EndpointValueSetter setter)
/*     */       {
/* 574 */         this.accessor = accessor;
/* 575 */         this.setter = setter;
/* 576 */         assert ((accessor != null) && (setter != null));
/*     */       }
/*     */ 
/*     */       final void readRequest(Object[] args, Object wrapperBean) throws AccessorException {
/* 580 */         Object obj = this.accessor.get(wrapperBean);
/* 581 */         this.setter.put(obj, args);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Header extends EndpointArgumentsBuilder
/*     */   {
/*     */     private final Bridge<?> bridge;
/*     */     private final EndpointValueSetter setter;
/*     */     private final QName headerName;
/*     */     private final SOAPVersion soapVersion;
/*     */ 
/*     */     public Header(SOAPVersion soapVersion, QName name, Bridge<?> bridge, EndpointValueSetter setter)
/*     */     {
/* 414 */       this.soapVersion = soapVersion;
/* 415 */       this.headerName = name;
/* 416 */       this.bridge = bridge;
/* 417 */       this.setter = setter;
/*     */     }
/*     */ 
/*     */     public Header(SOAPVersion soapVersion, ParameterImpl param, EndpointValueSetter setter) {
/* 421 */       this(soapVersion, param.getTypeReference().tagName, param.getBridge(), setter);
/*     */ 
/* 426 */       assert (param.getOutBinding() == ParameterBinding.HEADER);
/*     */     }
/*     */ 
/*     */     private SOAPFaultException createDuplicateHeaderException() {
/*     */       try {
/* 431 */         SOAPFault fault = this.soapVersion.saajSoapFactory.createFault(ServerMessages.DUPLICATE_PORT_KNOWN_HEADER(this.headerName), this.soapVersion.faultCodeClient);
/*     */ 
/* 433 */         return new SOAPFaultException(fault);
/*     */       } catch (SOAPException e) {
/* 435 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void readRequest(Message msg, Object[] args) throws JAXBException {
/* 440 */       Header header = null;
/* 441 */       Iterator it = msg.getHeaders().getHeaders(this.headerName, true);
/*     */ 
/* 443 */       if (it.hasNext()) {
/* 444 */         header = (Header)it.next();
/* 445 */         if (it.hasNext()) {
/* 446 */           throw createDuplicateHeaderException();
/*     */         }
/*     */       }
/*     */ 
/* 450 */       if (header != null)
/* 451 */         this.setter.put(header.readAsJAXB(this.bridge), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class ImageBuilder extends EndpointArgumentsBuilder.AttachmentBuilder
/*     */   {
/*     */     ImageBuilder(ParameterImpl param, EndpointValueSetter setter)
/*     */     {
/* 286 */       super(setter);
/*     */     }
/*     */     void mapAttachment(Attachment att, Object[] args) {
/* 291 */       InputStream is = null;
/*     */       Image image;
/*     */       try {
/* 293 */         is = att.asInputStream();
/* 294 */         image = ImageIO.read(is);
/*     */       } catch (IOException ioe) {
/* 296 */         throw new WebServiceException(ioe);
/*     */       } finally {
/* 298 */         if (is != null) {
/*     */           try {
/* 300 */             is.close();
/*     */           } catch (IOException ioe) {
/* 302 */             throw new WebServiceException(ioe);
/*     */           }
/*     */         }
/*     */       }
/* 306 */       this.setter.put(image, args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class InputStreamBuilder extends EndpointArgumentsBuilder.AttachmentBuilder {
/*     */     InputStreamBuilder(ParameterImpl param, EndpointValueSetter setter) {
/* 312 */       super(setter);
/*     */     }
/*     */ 
/*     */     void mapAttachment(Attachment att, Object[] args) {
/* 316 */       this.setter.put(att.asInputStream(), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class JAXBBuilder extends EndpointArgumentsBuilder.AttachmentBuilder {
/*     */     JAXBBuilder(ParameterImpl param, EndpointValueSetter setter) {
/* 322 */       super(setter);
/*     */     }
/*     */ 
/*     */     void mapAttachment(Attachment att, Object[] args) throws JAXBException {
/* 326 */       Object obj = this.param.getBridge().unmarshal(att.asInputStream());
/* 327 */       this.setter.put(obj, args);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class None extends EndpointArgumentsBuilder
/*     */   {
/*     */     public void readRequest(Message msg, Object[] args)
/*     */     {
/* 100 */       msg.consume();
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class NullSetter extends EndpointArgumentsBuilder
/*     */   {
/*     */     private final EndpointValueSetter setter;
/*     */     private final Object nullValue;
/*     */ 
/*     */     public NullSetter(EndpointValueSetter setter, Object nullValue)
/*     */     {
/* 143 */       assert (setter != null);
/* 144 */       this.nullValue = nullValue;
/* 145 */       this.setter = setter;
/*     */     }
/*     */     public void readRequest(Message msg, Object[] args) {
/* 148 */       this.setter.put(this.nullValue, args);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class RpcLit extends EndpointArgumentsBuilder
/*     */   {
/* 596 */     private final Map<QName, PartBuilder> parts = new HashMap();
/*     */     private QName wrapperName;
/*     */ 
/*     */     public RpcLit(WrapperParameter wp)
/*     */     {
/* 601 */       assert (wp.getTypeReference().type == CompositeStructure.class);
/*     */ 
/* 603 */       this.wrapperName = wp.getName();
/* 604 */       List children = wp.getWrapperChildren();
/* 605 */       for (ParameterImpl p : children) {
/* 606 */         this.parts.put(p.getName(), new PartBuilder(p.getBridge(), EndpointValueSetter.get(p)));
/*     */ 
/* 611 */         assert (p.getBinding() == ParameterBinding.BODY);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void readRequest(Message msg, Object[] args) throws JAXBException, XMLStreamException {
/* 616 */       if (!msg.hasPayload()) {
/* 617 */         throw new WebServiceException("No payload. Expecting payload with " + this.wrapperName + " element");
/*     */       }
/* 619 */       XMLStreamReader reader = msg.readPayload();
/* 620 */       XMLStreamReaderUtil.verifyTag(reader, this.wrapperName);
/* 621 */       reader.nextTag();
/*     */ 
/* 623 */       while (reader.getEventType() == 1)
/*     */       {
/* 625 */         PartBuilder part = (PartBuilder)this.parts.get(reader.getName());
/* 626 */         if (part == null)
/*     */         {
/* 628 */           XMLStreamReaderUtil.skipElement(reader);
/* 629 */           reader.nextTag();
/*     */         } else {
/* 631 */           part.readRequest(args, reader, msg.getAttachments());
/*     */         }
/*     */ 
/* 634 */         if ((reader.getEventType() != 1) && (reader.getEventType() != 2))
/*     */         {
/* 636 */           XMLStreamReaderUtil.nextElementContent(reader);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 641 */       reader.close();
/* 642 */       XMLStreamReaderFactory.recycle(reader);
/*     */     }
/*     */ 
/*     */     static final class PartBuilder
/*     */     {
/*     */       private final Bridge bridge;
/*     */       private final EndpointValueSetter setter;
/*     */ 
/*     */       public PartBuilder(Bridge bridge, EndpointValueSetter setter)
/*     */       {
/* 660 */         this.bridge = bridge;
/* 661 */         this.setter = setter;
/*     */       }
/*     */ 
/*     */       final void readRequest(Object[] args, XMLStreamReader r, AttachmentSet att) throws JAXBException {
/* 665 */         Object obj = this.bridge.unmarshal(r, att != null ? new AttachmentUnmarshallerImpl(att) : null);
/* 666 */         this.setter.put(obj, args);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class SourceBuilder extends EndpointArgumentsBuilder.AttachmentBuilder
/*     */   {
/*     */     SourceBuilder(ParameterImpl param, EndpointValueSetter setter)
/*     */     {
/* 276 */       super(setter);
/*     */     }
/*     */ 
/*     */     void mapAttachment(Attachment att, Object[] args) {
/* 280 */       this.setter.put(att.asSource(), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class StringBuilder extends EndpointArgumentsBuilder.AttachmentBuilder
/*     */   {
/*     */     StringBuilder(ParameterImpl param, EndpointValueSetter setter)
/*     */     {
/* 333 */       super(setter);
/*     */     }
/*     */ 
/*     */     void mapAttachment(Attachment att, Object[] args) {
/* 337 */       att.getContentType();
/* 338 */       StringDataContentHandler sdh = new StringDataContentHandler();
/*     */       try {
/* 340 */         String str = (String)sdh.getContent(new DataHandlerDataSource(att.asDataHandler()));
/* 341 */         this.setter.put(str, args);
/*     */       } catch (Exception e) {
/* 343 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder
 * JD-Core Version:    0.6.2
 */