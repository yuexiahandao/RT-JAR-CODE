/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.XMLStreamWriterOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class BridgeImpl<T> extends InternalBridge<T>
/*     */ {
/*     */   private final Name tagName;
/*     */   private final JaxBeanInfo<T> bi;
/*     */   private final TypeReference typeRef;
/*     */ 
/*     */   public BridgeImpl(JAXBContextImpl context, Name tagName, JaxBeanInfo<T> bi, TypeReference typeRef)
/*     */   {
/*  71 */     super(context);
/*  72 */     this.tagName = tagName;
/*  73 */     this.bi = bi;
/*  74 */     this.typeRef = typeRef;
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller _m, T t, XMLStreamWriter output) throws JAXBException {
/*  78 */     MarshallerImpl m = (MarshallerImpl)_m;
/*  79 */     m.write(this.tagName, this.bi, t, XMLStreamWriterOutput.create(output, this.context), new StAXPostInitAction(output, m.serializer));
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller _m, T t, OutputStream output, NamespaceContext nsContext) throws JAXBException {
/*  83 */     MarshallerImpl m = (MarshallerImpl)_m;
/*     */ 
/*  85 */     Runnable pia = null;
/*  86 */     if (nsContext != null) {
/*  87 */       pia = new StAXPostInitAction(nsContext, m.serializer);
/*     */     }
/*  89 */     m.write(this.tagName, this.bi, t, m.createWriter(output), pia);
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller _m, T t, Node output) throws JAXBException {
/*  93 */     MarshallerImpl m = (MarshallerImpl)_m;
/*  94 */     m.write(this.tagName, this.bi, t, new SAXOutput(new SAX2DOMEx(output)), new DomPostInitAction(output, m.serializer));
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller _m, T t, ContentHandler contentHandler) throws JAXBException {
/*  98 */     MarshallerImpl m = (MarshallerImpl)_m;
/*  99 */     m.write(this.tagName, this.bi, t, new SAXOutput(contentHandler), null);
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller _m, T t, Result result) throws JAXBException {
/* 103 */     MarshallerImpl m = (MarshallerImpl)_m;
/* 104 */     m.write(this.tagName, this.bi, t, m.createXmlOutput(result), m.createPostInitAction(result));
/*     */   }
/*     */   @NotNull
/*     */   public T unmarshal(Unmarshaller _u, XMLStreamReader in) throws JAXBException {
/* 108 */     UnmarshallerImpl u = (UnmarshallerImpl)_u;
/* 109 */     return ((JAXBElement)u.unmarshal0(in, this.bi)).getValue();
/*     */   }
/*     */   @NotNull
/*     */   public T unmarshal(Unmarshaller _u, Source in) throws JAXBException {
/* 113 */     UnmarshallerImpl u = (UnmarshallerImpl)_u;
/* 114 */     return ((JAXBElement)u.unmarshal0(in, this.bi)).getValue();
/*     */   }
/*     */   @NotNull
/*     */   public T unmarshal(Unmarshaller _u, InputStream in) throws JAXBException {
/* 118 */     UnmarshallerImpl u = (UnmarshallerImpl)_u;
/* 119 */     return ((JAXBElement)u.unmarshal0(in, this.bi)).getValue();
/*     */   }
/*     */   @NotNull
/*     */   public T unmarshal(Unmarshaller _u, Node n) throws JAXBException {
/* 123 */     UnmarshallerImpl u = (UnmarshallerImpl)_u;
/* 124 */     return ((JAXBElement)u.unmarshal0(n, this.bi)).getValue();
/*     */   }
/*     */ 
/*     */   public TypeReference getTypeReference() {
/* 128 */     return this.typeRef;
/*     */   }
/*     */ 
/*     */   public void marshal(T value, XMLSerializer out) throws IOException, SAXException, XMLStreamException {
/* 132 */     out.startElement(this.tagName, null);
/* 133 */     if (value == null)
/* 134 */       out.writeXsiNilTrue();
/*     */     else {
/* 136 */       out.childAsXsiType(value, null, this.bi, false);
/*     */     }
/* 138 */     out.endElement();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.BridgeImpl
 * JD-Core Version:    0.6.2
 */