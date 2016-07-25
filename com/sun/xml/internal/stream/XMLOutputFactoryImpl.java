/*     */ package com.sun.xml.internal.stream;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.PropertyManager;
/*     */ import com.sun.xml.internal.stream.writers.XMLDOMWriterImpl;
/*     */ import com.sun.xml.internal.stream.writers.XMLEventWriterImpl;
/*     */ import com.sun.xml.internal.stream.writers.XMLStreamWriterImpl;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLOutputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.stax.StAXResult;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ 
/*     */ public class XMLOutputFactoryImpl extends XMLOutputFactory
/*     */ {
/*  56 */   private PropertyManager fPropertyManager = new PropertyManager(2);
/*     */ 
/*  59 */   private XMLStreamWriterImpl fStreamWriter = null;
/*     */ 
/*  64 */   boolean fReuseInstance = false;
/*     */   private static final boolean DEBUG = false;
/*     */   private boolean fPropertyChanged;
/*     */ 
/*     */   public XMLEventWriter createXMLEventWriter(OutputStream outputStream)
/*     */     throws XMLStreamException
/*     */   {
/*  71 */     return createXMLEventWriter(outputStream, null);
/*     */   }
/*     */ 
/*     */   public XMLEventWriter createXMLEventWriter(OutputStream outputStream, String encoding) throws XMLStreamException {
/*  75 */     return new XMLEventWriterImpl(createXMLStreamWriter(outputStream, encoding));
/*     */   }
/*     */ 
/*     */   public XMLEventWriter createXMLEventWriter(Result result) throws XMLStreamException
/*     */   {
/*  80 */     if (((result instanceof StAXResult)) && (((StAXResult)result).getXMLEventWriter() != null)) {
/*  81 */       return ((StAXResult)result).getXMLEventWriter();
/*     */     }
/*  83 */     return new XMLEventWriterImpl(createXMLStreamWriter(result));
/*     */   }
/*     */ 
/*     */   public XMLEventWriter createXMLEventWriter(Writer writer) throws XMLStreamException {
/*  87 */     return new XMLEventWriterImpl(createXMLStreamWriter(writer));
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException
/*     */   {
/*  92 */     if ((result instanceof StreamResult))
/*  93 */       return createXMLStreamWriter((StreamResult)result, null);
/*  94 */     if ((result instanceof DOMResult))
/*  95 */       return new XMLDOMWriterImpl((DOMResult)result);
/*  96 */     if ((result instanceof StAXResult)) {
/*  97 */       if (((StAXResult)result).getXMLStreamWriter() != null) {
/*  98 */         return ((StAXResult)result).getXMLStreamWriter();
/*     */       }
/* 100 */       throw new UnsupportedOperationException("Result of type " + result + " is not supported");
/*     */     }
/*     */ 
/* 103 */     if (result.getSystemId() != null)
/*     */     {
/* 105 */       return createXMLStreamWriter(new StreamResult(result.getSystemId()));
/*     */     }
/* 107 */     throw new UnsupportedOperationException("Result of type " + result + " is not supported. " + "Supported result types are: DOMResult, StAXResult and StreamResult.");
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter createXMLStreamWriter(Writer writer)
/*     */     throws XMLStreamException
/*     */   {
/* 115 */     return createXMLStreamWriter(toStreamResult(null, writer, null), null);
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter createXMLStreamWriter(OutputStream outputStream) throws XMLStreamException {
/* 119 */     return createXMLStreamWriter(outputStream, null);
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter createXMLStreamWriter(OutputStream outputStream, String encoding) throws XMLStreamException {
/* 123 */     return createXMLStreamWriter(toStreamResult(outputStream, null, null), encoding);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/* 127 */     if (name == null) {
/* 128 */       throw new IllegalArgumentException("Property not supported");
/*     */     }
/* 130 */     if (this.fPropertyManager.containsProperty(name))
/* 131 */       return this.fPropertyManager.getProperty(name);
/* 132 */     throw new IllegalArgumentException("Property not supported");
/*     */   }
/*     */ 
/*     */   public boolean isPropertySupported(String name) {
/* 136 */     if (name == null) {
/* 137 */       return false;
/*     */     }
/*     */ 
/* 140 */     return this.fPropertyManager.containsProperty(name);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value) throws IllegalArgumentException
/*     */   {
/* 145 */     if ((name == null) || (value == null) || (!this.fPropertyManager.containsProperty(name))) {
/* 146 */       throw new IllegalArgumentException("Property " + name + "is not supported");
/*     */     }
/* 148 */     if ((name == "reuse-instance") || (name.equals("reuse-instance"))) {
/* 149 */       this.fReuseInstance = ((Boolean)value).booleanValue();
/*     */ 
/* 154 */       if (this.fReuseInstance) {
/* 155 */         throw new IllegalArgumentException("Property " + name + " is not supported: XMLStreamWriters are not Thread safe");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 162 */       this.fPropertyChanged = true;
/*     */     }
/* 164 */     this.fPropertyManager.setProperty(name, value);
/*     */   }
/*     */ 
/*     */   StreamResult toStreamResult(OutputStream os, Writer writer, String systemId)
/*     */   {
/* 170 */     StreamResult sr = new StreamResult();
/* 171 */     sr.setOutputStream(os);
/* 172 */     sr.setWriter(writer);
/* 173 */     sr.setSystemId(systemId);
/* 174 */     return sr;
/*     */   }
/*     */ 
/*     */   XMLStreamWriter createXMLStreamWriter(StreamResult sr, String encoding) throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 181 */       if ((this.fReuseInstance) && (this.fStreamWriter != null) && (this.fStreamWriter.canReuse()) && (!this.fPropertyChanged)) {
/* 182 */         this.fStreamWriter.reset();
/* 183 */         this.fStreamWriter.setOutput(sr, encoding);
/*     */ 
/* 185 */         return this.fStreamWriter;
/*     */       }
/* 187 */       return this.fStreamWriter = new XMLStreamWriterImpl(sr, encoding, new PropertyManager(this.fPropertyManager));
/*     */     } catch (IOException io) {
/* 189 */       throw new XMLStreamException(io);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.XMLOutputFactoryImpl
 * JD-Core Version:    0.6.2
 */