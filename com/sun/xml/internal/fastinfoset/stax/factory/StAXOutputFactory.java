/*     */ package com.sun.xml.internal.fastinfoset.stax.factory;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentSerializer;
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXManager;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.StAXEventWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLOutputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ 
/*     */ public class StAXOutputFactory extends XMLOutputFactory
/*     */ {
/*  48 */   private StAXManager _manager = null;
/*     */ 
/*     */   public StAXOutputFactory()
/*     */   {
/*  52 */     this._manager = new StAXManager(2);
/*     */   }
/*     */ 
/*     */   public XMLEventWriter createXMLEventWriter(Result result) throws XMLStreamException {
/*  56 */     return new StAXEventWriter(createXMLStreamWriter(result));
/*     */   }
/*     */ 
/*     */   public XMLEventWriter createXMLEventWriter(Writer writer) throws XMLStreamException {
/*  60 */     return new StAXEventWriter(createXMLStreamWriter(writer));
/*     */   }
/*     */ 
/*     */   public XMLEventWriter createXMLEventWriter(OutputStream outputStream) throws XMLStreamException {
/*  64 */     return new StAXEventWriter(createXMLStreamWriter(outputStream));
/*     */   }
/*     */ 
/*     */   public XMLEventWriter createXMLEventWriter(OutputStream outputStream, String encoding) throws XMLStreamException {
/*  68 */     return new StAXEventWriter(createXMLStreamWriter(outputStream, encoding));
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException {
/*  72 */     if ((result instanceof StreamResult)) {
/*  73 */       StreamResult streamResult = (StreamResult)result;
/*  74 */       if (streamResult.getWriter() != null)
/*  75 */         return createXMLStreamWriter(streamResult.getWriter());
/*  76 */       if (streamResult.getOutputStream() != null)
/*  77 */         return createXMLStreamWriter(streamResult.getOutputStream());
/*  78 */       if (streamResult.getSystemId() != null)
/*     */         try {
/*  80 */           FileWriter writer = new FileWriter(new File(streamResult.getSystemId()));
/*  81 */           return createXMLStreamWriter(writer);
/*     */         } catch (IOException ie) {
/*  83 */           throw new XMLStreamException(ie);
/*     */         }
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/*  90 */         FileWriter writer = new FileWriter(new File(result.getSystemId()));
/*  91 */         return createXMLStreamWriter(writer);
/*     */       } catch (IOException ie) {
/*  93 */         throw new XMLStreamException(ie);
/*     */       }
/*     */     }
/*  96 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter createXMLStreamWriter(Writer writer)
/*     */     throws XMLStreamException
/*     */   {
/* 103 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter createXMLStreamWriter(OutputStream outputStream) throws XMLStreamException {
/* 107 */     return new StAXDocumentSerializer(outputStream, new StAXManager(this._manager));
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter createXMLStreamWriter(OutputStream outputStream, String encoding) throws XMLStreamException {
/* 111 */     StAXDocumentSerializer serializer = new StAXDocumentSerializer(outputStream, new StAXManager(this._manager));
/* 112 */     serializer.setEncoding(encoding);
/* 113 */     return serializer;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/* 117 */     if (name == null) {
/* 118 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.propertyNotSupported", new Object[] { null }));
/*     */     }
/* 120 */     if (this._manager.containsProperty(name))
/* 121 */       return this._manager.getProperty(name);
/* 122 */     throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.propertyNotSupported", new Object[] { name }));
/*     */   }
/*     */ 
/*     */   public boolean isPropertySupported(String name) {
/* 126 */     if (name == null) {
/* 127 */       return false;
/*     */     }
/* 129 */     return this._manager.containsProperty(name);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value) throws IllegalArgumentException {
/* 133 */     this._manager.setProperty(name, value);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.factory.StAXOutputFactory
 * JD-Core Version:    0.6.2
 */