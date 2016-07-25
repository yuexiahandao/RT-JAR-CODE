/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.MarshalException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*     */ final class BridgeAdapter<OnWire, InMemory> extends InternalBridge<InMemory>
/*     */ {
/*     */   private final InternalBridge<OnWire> core;
/*     */   private final Class<? extends XmlAdapter<OnWire, InMemory>> adapter;
/*     */ 
/*     */   public BridgeAdapter(InternalBridge<OnWire> core, Class<? extends XmlAdapter<OnWire, InMemory>> adapter)
/*     */   {
/*  64 */     super(core.getContext());
/*  65 */     this.core = core;
/*  66 */     this.adapter = adapter;
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller m, InMemory inMemory, XMLStreamWriter output) throws JAXBException {
/*  70 */     this.core.marshal(m, adaptM(m, inMemory), output);
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller m, InMemory inMemory, OutputStream output, NamespaceContext nsc) throws JAXBException {
/*  74 */     this.core.marshal(m, adaptM(m, inMemory), output, nsc);
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller m, InMemory inMemory, Node output) throws JAXBException {
/*  78 */     this.core.marshal(m, adaptM(m, inMemory), output);
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller context, InMemory inMemory, ContentHandler contentHandler) throws JAXBException {
/*  82 */     this.core.marshal(context, adaptM(context, inMemory), contentHandler);
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller context, InMemory inMemory, Result result) throws JAXBException {
/*  86 */     this.core.marshal(context, adaptM(context, inMemory), result);
/*     */   }
/*     */ 
/*     */   private OnWire adaptM(Marshaller m, InMemory v) throws JAXBException {
/*  90 */     XMLSerializer serializer = ((MarshallerImpl)m).serializer;
/*  91 */     serializer.setThreadAffinity();
/*  92 */     serializer.pushCoordinator();
/*     */     try {
/*  94 */       return _adaptM(serializer, v);
/*     */     } finally {
/*  96 */       serializer.popCoordinator();
/*  97 */       serializer.resetThreadAffinity();
/*     */     }
/*     */   }
/*     */ 
/*     */   private OnWire _adaptM(XMLSerializer serializer, InMemory v) throws MarshalException {
/* 102 */     XmlAdapter a = serializer.getAdapter(this.adapter);
/*     */     try {
/* 104 */       return a.marshal(v);
/*     */     } catch (Exception e) {
/* 106 */       serializer.handleError(e, v, null);
/* 107 */       throw new MarshalException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public InMemory unmarshal(Unmarshaller u, XMLStreamReader in) throws JAXBException {
/* 113 */     return adaptU(u, this.core.unmarshal(u, in));
/*     */   }
/*     */   @NotNull
/*     */   public InMemory unmarshal(Unmarshaller u, Source in) throws JAXBException {
/* 117 */     return adaptU(u, this.core.unmarshal(u, in));
/*     */   }
/*     */   @NotNull
/*     */   public InMemory unmarshal(Unmarshaller u, InputStream in) throws JAXBException {
/* 121 */     return adaptU(u, this.core.unmarshal(u, in));
/*     */   }
/*     */   @NotNull
/*     */   public InMemory unmarshal(Unmarshaller u, Node n) throws JAXBException {
/* 125 */     return adaptU(u, this.core.unmarshal(u, n));
/*     */   }
/*     */ 
/*     */   public TypeReference getTypeReference() {
/* 129 */     return this.core.getTypeReference();
/*     */   }
/*     */   @NotNull
/*     */   private InMemory adaptU(Unmarshaller _u, OnWire v) throws JAXBException {
/* 133 */     UnmarshallerImpl u = (UnmarshallerImpl)_u;
/* 134 */     XmlAdapter a = u.coordinator.getAdapter(this.adapter);
/* 135 */     u.coordinator.setThreadAffinity();
/* 136 */     u.coordinator.pushCoordinator();
/*     */     try {
/* 138 */       return a.unmarshal(v);
/*     */     } catch (Exception e) {
/* 140 */       throw new UnmarshalException(e);
/*     */     } finally {
/* 142 */       u.coordinator.popCoordinator();
/* 143 */       u.coordinator.resetThreadAffinity();
/*     */     }
/*     */   }
/*     */ 
/*     */   void marshal(InMemory o, XMLSerializer out) throws IOException, SAXException, XMLStreamException {
/*     */     try {
/* 149 */       this.core.marshal(_adaptM(XMLSerializer.getInstance(), o), out);
/*     */     }
/*     */     catch (MarshalException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.BridgeAdapter
 * JD-Core Version:    0.6.2
 */