/*     */ package com.sun.xml.internal.ws.message.jaxb;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.MarshallerImpl;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ 
/*     */ final class MarshallerBridge extends Bridge
/*     */ {
/*     */   public MarshallerBridge(JAXBRIContext context)
/*     */   {
/*  54 */     super((JAXBContextImpl)context);
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller m, Object object, XMLStreamWriter output) throws JAXBException {
/*  58 */     m.setProperty("jaxb.fragment", Boolean.valueOf(true));
/*     */     try {
/*  60 */       m.marshal(object, output);
/*     */     } finally {
/*  62 */       m.setProperty("jaxb.fragment", Boolean.valueOf(false));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller m, Object object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
/*  67 */     m.setProperty("jaxb.fragment", Boolean.valueOf(true));
/*     */     try {
/*  69 */       ((MarshallerImpl)m).marshal(object, output, nsContext);
/*     */     } finally {
/*  71 */       m.setProperty("jaxb.fragment", Boolean.valueOf(false));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller m, Object object, Node output) throws JAXBException {
/*  76 */     m.setProperty("jaxb.fragment", Boolean.valueOf(true));
/*     */     try {
/*  78 */       m.marshal(object, output);
/*     */     } finally {
/*  80 */       m.setProperty("jaxb.fragment", Boolean.valueOf(false));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller m, Object object, ContentHandler contentHandler) throws JAXBException {
/*  85 */     m.setProperty("jaxb.fragment", Boolean.valueOf(true));
/*     */     try {
/*  87 */       m.marshal(object, contentHandler);
/*     */     } finally {
/*  89 */       m.setProperty("jaxb.fragment", Boolean.valueOf(false));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void marshal(Marshaller m, Object object, Result result) throws JAXBException {
/*  94 */     m.setProperty("jaxb.fragment", Boolean.valueOf(true));
/*     */     try {
/*  96 */       m.marshal(object, result);
/*     */     } finally {
/*  98 */       m.setProperty("jaxb.fragment", Boolean.valueOf(false));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object unmarshal(Unmarshaller u, XMLStreamReader in) {
/* 103 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Object unmarshal(Unmarshaller u, Source in) {
/* 107 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Object unmarshal(Unmarshaller u, InputStream in) {
/* 111 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Object unmarshal(Unmarshaller u, Node n) {
/* 115 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public TypeReference getTypeReference() {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.jaxb.MarshallerBridge
 * JD-Core Version:    0.6.2
 */