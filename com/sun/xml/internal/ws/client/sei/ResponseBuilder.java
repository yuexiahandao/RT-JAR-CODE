/*     */ package com.sun.xml.internal.ws.client.sei;
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
/*     */ abstract class ResponseBuilder
/*     */ {
/* 111 */   public static ResponseBuilder NONE = new None(null);
/*     */ 
/* 125 */   private static final Map<Class, Object> primitiveUninitializedValues = new HashMap();
/*     */ 
/*     */   abstract Object readResponse(Message paramMessage, Object[] paramArrayOfObject)
/*     */     throws JAXBException, XMLStreamException;
/*     */ 
/*     */   public static Object getVMUninitializedValue(Type type)
/*     */   {
/* 122 */     return primitiveUninitializedValues.get(type);
/*     */   }
/*     */ 
/*     */   public static final String getWSDLPartName(Attachment att)
/*     */   {
/* 379 */     String cId = att.getContentId();
/*     */ 
/* 381 */     int index = cId.lastIndexOf('@', cId.length());
/* 382 */     if (index == -1) {
/* 383 */       return null;
/*     */     }
/* 385 */     String localPart = cId.substring(0, index);
/* 386 */     index = localPart.lastIndexOf('=', localPart.length());
/* 387 */     if (index == -1)
/* 388 */       return null;
/*     */     try
/*     */     {
/* 391 */       return URLDecoder.decode(localPart.substring(0, index), "UTF-8");
/*     */     } catch (UnsupportedEncodingException e) {
/* 393 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isXMLMimeType(String mimeType)
/*     */   {
/* 691 */     return (mimeType.equals("text/xml")) || (mimeType.equals("application/xml"));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 128 */     Map m = primitiveUninitializedValues;
/* 129 */     m.put(Integer.TYPE, Integer.valueOf(0));
/* 130 */     m.put(Character.TYPE, Character.valueOf('\000'));
/* 131 */     m.put(Byte.TYPE, Byte.valueOf((byte)0));
/* 132 */     m.put(Short.TYPE, Short.valueOf((short)0));
/* 133 */     m.put(Long.TYPE, Long.valueOf(0L));
/* 134 */     m.put(Float.TYPE, Float.valueOf(0.0F));
/* 135 */     m.put(Double.TYPE, Double.valueOf(0.0D));
/*     */   }
/*     */ 
/*     */   static abstract class AttachmentBuilder extends ResponseBuilder
/*     */   {
/*     */     protected final ValueSetter setter;
/*     */     protected final ParameterImpl param;
/*     */     private final String pname;
/*     */     private final String pname1;
/*     */ 
/*     */     AttachmentBuilder(ParameterImpl param, ValueSetter setter)
/*     */     {
/* 205 */       this.setter = setter;
/* 206 */       this.param = param;
/* 207 */       this.pname = param.getPartName();
/* 208 */       this.pname1 = ("<" + this.pname);
/*     */     }
/*     */ 
/*     */     public static ResponseBuilder createAttachmentBuilder(ParameterImpl param, ValueSetter setter)
/*     */     {
/* 221 */       Class type = (Class)param.getTypeReference().type;
/* 222 */       if (DataHandler.class.isAssignableFrom(type))
/* 223 */         return new ResponseBuilder.DataHandlerBuilder(param, setter);
/* 224 */       if ([B.class == type)
/* 225 */         return new ResponseBuilder.ByteArrayBuilder(param, setter);
/* 226 */       if (Source.class.isAssignableFrom(type))
/* 227 */         return new ResponseBuilder.SourceBuilder(param, setter);
/* 228 */       if (Image.class.isAssignableFrom(type))
/* 229 */         return new ResponseBuilder.ImageBuilder(param, setter);
/* 230 */       if (InputStream.class == type)
/* 231 */         return new ResponseBuilder.InputStreamBuilder(param, setter);
/* 232 */       if (ResponseBuilder.isXMLMimeType(param.getBinding().getMimeType()))
/* 233 */         return new ResponseBuilder.JAXBBuilder(param, setter);
/* 234 */       if (String.class.isAssignableFrom(type)) {
/* 235 */         return new ResponseBuilder.StringBuilder(param, setter);
/*     */       }
/* 237 */       throw new UnsupportedOperationException("Unexpected Attachment type =" + type);
/*     */     }
/*     */ 
/*     */     public Object readResponse(Message msg, Object[] args)
/*     */       throws JAXBException, XMLStreamException
/*     */     {
/* 243 */       for (Attachment att : msg.getAttachments()) {
/* 244 */         String part = getWSDLPartName(att);
/* 245 */         if (part != null)
/*     */         {
/* 248 */           if ((part.equals(this.pname)) || (part.equals(this.pname1)))
/* 249 */             return mapAttachment(att, args);
/*     */         }
/*     */       }
/* 252 */       return null;
/*     */     }
/*     */ 
/*     */     abstract Object mapAttachment(Attachment paramAttachment, Object[] paramArrayOfObject)
/*     */       throws JAXBException;
/*     */   }
/*     */ 
/*     */   static final class Body extends ResponseBuilder
/*     */   {
/*     */     private final Bridge<?> bridge;
/*     */     private final ValueSetter setter;
/*     */ 
/*     */     public Body(Bridge<?> bridge, ValueSetter setter)
/*     */     {
/* 475 */       this.bridge = bridge;
/* 476 */       this.setter = setter;
/*     */     }
/*     */ 
/*     */     public Object readResponse(Message msg, Object[] args) throws JAXBException {
/* 480 */       return this.setter.put(msg.readPayloadAsJAXB(this.bridge), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class ByteArrayBuilder extends ResponseBuilder.AttachmentBuilder
/*     */   {
/*     */     ByteArrayBuilder(ParameterImpl param, ValueSetter setter)
/*     */     {
/* 288 */       super(setter);
/*     */     }
/*     */ 
/*     */     Object mapAttachment(Attachment att, Object[] args) {
/* 292 */       return this.setter.put(att.asByteArray(), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Composite extends ResponseBuilder
/*     */   {
/*     */     private final ResponseBuilder[] builders;
/*     */ 
/*     */     public Composite(ResponseBuilder[] builders)
/*     */     {
/* 174 */       this.builders = builders;
/*     */     }
/*     */ 
/*     */     public Composite(Collection<? extends ResponseBuilder> builders) {
/* 178 */       this((ResponseBuilder[])builders.toArray(new ResponseBuilder[builders.size()]));
/*     */     }
/*     */ 
/*     */     public Object readResponse(Message msg, Object[] args) throws JAXBException, XMLStreamException {
/* 182 */       Object retVal = null;
/* 183 */       for (ResponseBuilder builder : this.builders) {
/* 184 */         Object r = builder.readResponse(msg, args);
/*     */ 
/* 186 */         if (r != null) {
/* 187 */           assert (retVal == null);
/* 188 */           retVal = r;
/*     */         }
/*     */       }
/* 191 */       return retVal;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class DataHandlerBuilder extends ResponseBuilder.AttachmentBuilder
/*     */   {
/*     */     DataHandlerBuilder(ParameterImpl param, ValueSetter setter)
/*     */     {
/* 260 */       super(setter);
/*     */     }
/*     */ 
/*     */     Object mapAttachment(Attachment att, Object[] args) {
/* 264 */       return this.setter.put(att.asDataHandler(), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class DocLit extends ResponseBuilder
/*     */   {
/*     */     private final PartBuilder[] parts;
/*     */     private final Bridge wrapper;
/*     */     private final QName wrapperName;
/*     */ 
/*     */     public DocLit(WrapperParameter wp, ValueSetterFactory setterFactory)
/*     */     {
/* 499 */       this.wrapperName = wp.getName();
/* 500 */       this.wrapper = wp.getBridge();
/* 501 */       Class wrapperType = (Class)this.wrapper.getTypeReference().type;
/*     */ 
/* 503 */       List parts = new ArrayList();
/*     */ 
/* 505 */       List children = wp.getWrapperChildren();
/* 506 */       for (ParameterImpl p : children) {
/* 507 */         if (!p.isIN())
/*     */         {
/* 509 */           QName name = p.getName();
/*     */           try {
/* 511 */             parts.add(new PartBuilder(wp.getOwner().getJAXBContext().getElementPropertyAccessor(wrapperType, name.getNamespaceURI(), p.getName().getLocalPart()), setterFactory.get(p)));
/*     */ 
/* 520 */             if ((!$assertionsDisabled) && (p.getBinding() != ParameterBinding.BODY)) throw new AssertionError(); 
/*     */           }
/* 522 */           catch (JAXBException e) { throw new WebServiceException(wrapperType + " do not have a property of the name " + name, e); }
/*     */ 
/*     */         }
/*     */       }
/*     */ 
/* 527 */       this.parts = ((PartBuilder[])parts.toArray(new PartBuilder[parts.size()]));
/*     */     }
/*     */ 
/*     */     public Object readResponse(Message msg, Object[] args) throws JAXBException, XMLStreamException {
/* 531 */       Object retVal = null;
/*     */ 
/* 533 */       if (this.parts.length > 0) {
/* 534 */         if (!msg.hasPayload()) {
/* 535 */           throw new WebServiceException("No payload. Expecting payload with " + this.wrapperName + " element");
/*     */         }
/* 537 */         XMLStreamReader reader = msg.readPayload();
/* 538 */         XMLStreamReaderUtil.verifyTag(reader, this.wrapperName);
/* 539 */         Object wrapperBean = this.wrapper.unmarshal(reader, msg.getAttachments() != null ? new AttachmentUnmarshallerImpl(msg.getAttachments()) : null);
/*     */         try
/*     */         {
/* 543 */           for (PartBuilder part : this.parts) {
/* 544 */             Object o = part.readResponse(args, wrapperBean);
/*     */ 
/* 547 */             if (o != null) {
/* 548 */               assert (retVal == null);
/* 549 */               retVal = o;
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (AccessorException e) {
/* 554 */           throw new WebServiceException(e);
/*     */         }
/*     */ 
/* 558 */         reader.close();
/* 559 */         XMLStreamReaderFactory.recycle(reader);
/*     */       } else {
/* 561 */         msg.consume();
/*     */       }
/*     */ 
/* 564 */       return retVal;
/*     */     }
/*     */ 
/*     */     static final class PartBuilder
/*     */     {
/*     */       private final RawAccessor accessor;
/*     */       private final ValueSetter setter;
/*     */ 
/*     */       public PartBuilder(RawAccessor accessor, ValueSetter setter)
/*     */       {
/* 582 */         this.accessor = accessor;
/* 583 */         this.setter = setter;
/* 584 */         assert ((accessor != null) && (setter != null));
/*     */       }
/*     */ 
/*     */       final Object readResponse(Object[] args, Object wrapperBean) throws AccessorException {
/* 588 */         Object obj = this.accessor.get(wrapperBean);
/* 589 */         return this.setter.put(obj, args);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Header extends ResponseBuilder
/*     */   {
/*     */     private final Bridge<?> bridge;
/*     */     private final ValueSetter setter;
/*     */     private final QName headerName;
/*     */     private final SOAPVersion soapVersion;
/*     */ 
/*     */     public Header(SOAPVersion soapVersion, QName name, Bridge<?> bridge, ValueSetter setter)
/*     */     {
/* 418 */       this.soapVersion = soapVersion;
/* 419 */       this.headerName = name;
/* 420 */       this.bridge = bridge;
/* 421 */       this.setter = setter;
/*     */     }
/*     */ 
/*     */     public Header(SOAPVersion soapVersion, ParameterImpl param, ValueSetter setter) {
/* 425 */       this(soapVersion, param.getTypeReference().tagName, param.getBridge(), setter);
/*     */ 
/* 429 */       assert (param.getOutBinding() == ParameterBinding.HEADER);
/*     */     }
/*     */ 
/*     */     private SOAPFaultException createDuplicateHeaderException() {
/*     */       try {
/* 434 */         SOAPFault fault = this.soapVersion.saajSoapFactory.createFault(ServerMessages.DUPLICATE_PORT_KNOWN_HEADER(this.headerName), this.soapVersion.faultCodeServer);
/*     */ 
/* 436 */         return new SOAPFaultException(fault);
/*     */       } catch (SOAPException e) {
/* 438 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object readResponse(Message msg, Object[] args) throws JAXBException {
/* 443 */       Header header = null;
/* 444 */       Iterator it = msg.getHeaders().getHeaders(this.headerName, true);
/*     */ 
/* 446 */       if (it.hasNext()) {
/* 447 */         header = (Header)it.next();
/* 448 */         if (it.hasNext()) {
/* 449 */           throw createDuplicateHeaderException();
/*     */         }
/*     */       }
/*     */ 
/* 453 */       if (header != null) {
/* 454 */         return this.setter.put(header.readAsJAXB(this.bridge), args);
/*     */       }
/*     */ 
/* 457 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class ImageBuilder extends ResponseBuilder.AttachmentBuilder
/*     */   {
/*     */     ImageBuilder(ParameterImpl param, ValueSetter setter)
/*     */     {
/* 308 */       super(setter);
/*     */     }
/*     */     Object mapAttachment(Attachment att, Object[] args) {
/* 313 */       InputStream is = null;
/*     */       Image image;
/*     */       try {
/* 315 */         is = att.asInputStream();
/* 316 */         image = ImageIO.read(is);
/*     */       } catch (IOException ioe) {
/* 318 */         throw new WebServiceException(ioe);
/*     */       } finally {
/* 320 */         if (is != null) {
/*     */           try {
/* 322 */             is.close();
/*     */           } catch (IOException ioe) {
/* 324 */             throw new WebServiceException(ioe);
/*     */           }
/*     */         }
/*     */       }
/* 328 */       return this.setter.put(image, args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class InputStreamBuilder extends ResponseBuilder.AttachmentBuilder {
/*     */     InputStreamBuilder(ParameterImpl param, ValueSetter setter) {
/* 334 */       super(setter);
/*     */     }
/*     */ 
/*     */     Object mapAttachment(Attachment att, Object[] args) {
/* 338 */       return this.setter.put(att.asInputStream(), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class JAXBBuilder extends ResponseBuilder.AttachmentBuilder {
/*     */     JAXBBuilder(ParameterImpl param, ValueSetter setter) {
/* 344 */       super(setter);
/*     */     }
/*     */ 
/*     */     Object mapAttachment(Attachment att, Object[] args) throws JAXBException {
/* 348 */       Object obj = this.param.getBridge().unmarshal(att.asInputStream());
/* 349 */       return this.setter.put(obj, args);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class None extends ResponseBuilder
/*     */   {
/*     */     public Object readResponse(Message msg, Object[] args)
/*     */     {
/* 102 */       msg.consume();
/* 103 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class NullSetter extends ResponseBuilder
/*     */   {
/*     */     private final ValueSetter setter;
/*     */     private final Object nullValue;
/*     */ 
/*     */     public NullSetter(ValueSetter setter, Object nullValue)
/*     */     {
/* 146 */       assert (setter != null);
/* 147 */       this.nullValue = nullValue;
/* 148 */       this.setter = setter;
/*     */     }
/*     */     public Object readResponse(Message msg, Object[] args) {
/* 151 */       return this.setter.put(this.nullValue, args);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class RpcLit extends ResponseBuilder
/*     */   {
/* 604 */     private final Map<QName, PartBuilder> parts = new HashMap();
/*     */     private QName wrapperName;
/*     */ 
/*     */     public RpcLit(WrapperParameter wp, ValueSetterFactory setterFactory)
/*     */     {
/* 609 */       assert (wp.getTypeReference().type == CompositeStructure.class);
/*     */ 
/* 611 */       this.wrapperName = wp.getName();
/* 612 */       List children = wp.getWrapperChildren();
/* 613 */       for (ParameterImpl p : children) {
/* 614 */         this.parts.put(p.getName(), new PartBuilder(p.getBridge(), setterFactory.get(p)));
/*     */ 
/* 619 */         assert (p.getBinding() == ParameterBinding.BODY);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object readResponse(Message msg, Object[] args) throws JAXBException, XMLStreamException {
/* 624 */       Object retVal = null;
/*     */ 
/* 626 */       if (!msg.hasPayload()) {
/* 627 */         throw new WebServiceException("No payload. Expecting payload with " + this.wrapperName + " element");
/*     */       }
/* 629 */       XMLStreamReader reader = msg.readPayload();
/* 630 */       XMLStreamReaderUtil.verifyTag(reader, this.wrapperName);
/* 631 */       reader.nextTag();
/*     */ 
/* 633 */       while (reader.getEventType() == 1)
/*     */       {
/* 635 */         PartBuilder part = (PartBuilder)this.parts.get(reader.getName());
/* 636 */         if (part == null)
/*     */         {
/* 638 */           XMLStreamReaderUtil.skipElement(reader);
/* 639 */           reader.nextTag();
/*     */         } else {
/* 641 */           Object o = part.readResponse(args, reader, msg.getAttachments());
/*     */ 
/* 643 */           if (o != null) {
/* 644 */             assert (retVal == null);
/* 645 */             retVal = o;
/*     */           }
/*     */         }
/*     */ 
/* 649 */         if ((reader.getEventType() != 1) && (reader.getEventType() != 2))
/*     */         {
/* 651 */           XMLStreamReaderUtil.nextElementContent(reader);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 656 */       reader.close();
/* 657 */       XMLStreamReaderFactory.recycle(reader);
/*     */ 
/* 659 */       return retVal;
/*     */     }
/*     */ 
/*     */     static final class PartBuilder
/*     */     {
/*     */       private final Bridge bridge;
/*     */       private final ValueSetter setter;
/*     */ 
/*     */       public PartBuilder(Bridge bridge, ValueSetter setter)
/*     */       {
/* 677 */         this.bridge = bridge;
/* 678 */         this.setter = setter;
/*     */       }
/*     */ 
/*     */       final Object readResponse(Object[] args, XMLStreamReader r, AttachmentSet att) throws JAXBException {
/* 682 */         Object obj = this.bridge.unmarshal(r, att != null ? new AttachmentUnmarshallerImpl(att) : null);
/* 683 */         return this.setter.put(obj, args);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class SourceBuilder extends ResponseBuilder.AttachmentBuilder
/*     */   {
/*     */     SourceBuilder(ParameterImpl param, ValueSetter setter)
/*     */     {
/* 298 */       super(setter);
/*     */     }
/*     */ 
/*     */     Object mapAttachment(Attachment att, Object[] args) {
/* 302 */       return this.setter.put(att.asSource(), args);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class StringBuilder extends ResponseBuilder.AttachmentBuilder
/*     */   {
/*     */     StringBuilder(ParameterImpl param, ValueSetter setter)
/*     */     {
/* 270 */       super(setter);
/*     */     }
/*     */ 
/*     */     Object mapAttachment(Attachment att, Object[] args) {
/* 274 */       att.getContentType();
/* 275 */       StringDataContentHandler sdh = new StringDataContentHandler();
/*     */       try {
/* 277 */         String str = (String)sdh.getContent(new DataHandlerDataSource(att.asDataHandler()));
/* 278 */         return this.setter.put(str, args);
/*     */       } catch (Exception e) {
/* 280 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.ResponseBuilder
 * JD-Core Version:    0.6.2
 */