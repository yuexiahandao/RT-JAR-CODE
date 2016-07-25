/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocument.Schema;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocument.WSDL;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocumentSource;
/*     */ import com.sun.xml.internal.ws.wsdl.writer.WSDLResolver;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.ws.Holder;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ final class WSDLGenResolver
/*     */   implements WSDLResolver
/*     */ {
/*     */   private final List<SDDocumentImpl> docs;
/*  55 */   private final List<SDDocumentSource> newDocs = new ArrayList();
/*     */   private SDDocumentSource concreteWsdlSource;
/*     */   private SDDocumentImpl abstractWsdl;
/*     */   private SDDocumentImpl concreteWsdl;
/*  64 */   private final Map<String, List<SDDocumentImpl>> nsMapping = new HashMap();
/*     */   private final QName serviceName;
/*     */   private final QName portTypeName;
/*     */ 
/*     */   public WSDLGenResolver(@NotNull List<SDDocumentImpl> docs, QName serviceName, QName portTypeName)
/*     */   {
/*  70 */     this.docs = docs;
/*  71 */     this.serviceName = serviceName;
/*  72 */     this.portTypeName = portTypeName;
/*     */ 
/*  74 */     for (SDDocumentImpl doc : docs) {
/*  75 */       if (doc.isWSDL()) {
/*  76 */         SDDocument.WSDL wsdl = (SDDocument.WSDL)doc;
/*  77 */         if (wsdl.hasPortType())
/*  78 */           this.abstractWsdl = doc;
/*     */       }
/*  80 */       if (doc.isSchema()) {
/*  81 */         SDDocument.Schema schema = (SDDocument.Schema)doc;
/*  82 */         List sysIds = (List)this.nsMapping.get(schema.getTargetNamespace());
/*  83 */         if (sysIds == null) {
/*  84 */           sysIds = new ArrayList();
/*  85 */           this.nsMapping.put(schema.getTargetNamespace(), sysIds);
/*     */         }
/*  87 */         sysIds.add(doc);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Result getWSDL(String filename)
/*     */   {
/*  98 */     URL url = createURL(filename);
/*  99 */     MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
/* 100 */     xsb.setSystemId(url.toExternalForm());
/* 101 */     this.concreteWsdlSource = SDDocumentSource.create(url, xsb);
/* 102 */     this.newDocs.add(this.concreteWsdlSource);
/* 103 */     XMLStreamBufferResult r = new XMLStreamBufferResult(xsb);
/* 104 */     r.setSystemId(filename);
/* 105 */     return r;
/*     */   }
/*     */ 
/*     */   private URL createURL(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 117 */       return new URL("file:///" + filename);
/*     */     }
/*     */     catch (MalformedURLException e)
/*     */     {
/* 121 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Result getAbstractWSDL(Holder<String> filename)
/*     */   {
/* 134 */     if (this.abstractWsdl != null) {
/* 135 */       filename.value = this.abstractWsdl.getURL().toString();
/* 136 */       return null;
/*     */     }
/* 138 */     URL url = createURL((String)filename.value);
/* 139 */     MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
/* 140 */     xsb.setSystemId(url.toExternalForm());
/* 141 */     SDDocumentSource abstractWsdlSource = SDDocumentSource.create(url, xsb);
/* 142 */     this.newDocs.add(abstractWsdlSource);
/* 143 */     XMLStreamBufferResult r = new XMLStreamBufferResult(xsb);
/* 144 */     r.setSystemId((String)filename.value);
/* 145 */     return r;
/*     */   }
/*     */ 
/*     */   public Result getSchemaOutput(String namespace, Holder<String> filename)
/*     */   {
/* 157 */     List schemas = (List)this.nsMapping.get(namespace);
/* 158 */     if (schemas != null) {
/* 159 */       if (schemas.size() > 1) {
/* 160 */         throw new ServerRtException("server.rt.err", new Object[] { "More than one schema for the target namespace " + namespace });
/*     */       }
/*     */ 
/* 163 */       filename.value = ((SDDocumentImpl)schemas.get(0)).getURL().toExternalForm();
/* 164 */       return null;
/*     */     }
/*     */ 
/* 167 */     URL url = createURL((String)filename.value);
/* 168 */     MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
/* 169 */     xsb.setSystemId(url.toExternalForm());
/* 170 */     SDDocumentSource sd = SDDocumentSource.create(url, xsb);
/* 171 */     this.newDocs.add(sd);
/*     */ 
/* 173 */     XMLStreamBufferResult r = new XMLStreamBufferResult(xsb);
/* 174 */     r.setSystemId((String)filename.value);
/* 175 */     return r;
/*     */   }
/*     */ 
/*     */   public SDDocumentImpl updateDocs()
/*     */   {
/* 187 */     for (SDDocumentSource doc : this.newDocs) {
/* 188 */       SDDocumentImpl docImpl = SDDocumentImpl.create(doc, this.serviceName, this.portTypeName);
/* 189 */       if (doc == this.concreteWsdlSource) {
/* 190 */         this.concreteWsdl = docImpl;
/*     */       }
/* 192 */       this.docs.add(docImpl);
/*     */     }
/* 194 */     return this.concreteWsdl;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.WSDLGenResolver
 * JD-Core Version:    0.6.2
 */