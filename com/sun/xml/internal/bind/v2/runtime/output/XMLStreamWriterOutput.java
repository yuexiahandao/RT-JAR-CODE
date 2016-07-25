/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XMLStreamWriterOutput extends XmlOutputAbstractImpl
/*     */ {
/*     */   private final XMLStreamWriter out;
/*  78 */   protected final char[] buf = new char[256];
/*     */ 
/* 158 */   private static final Class FI_STAX_WRITER_CLASS = initFIStAXWriterClass();
/* 159 */   private static final Constructor<? extends XmlOutput> FI_OUTPUT_CTOR = initFastInfosetOutputClass();
/*     */ 
/* 189 */   private static final Class STAXEX_WRITER_CLASS = initStAXExWriterClass();
/* 190 */   private static final Constructor<? extends XmlOutput> STAXEX_OUTPUT_CTOR = initStAXExOutputClass();
/*     */ 
/*     */   public static XmlOutput create(XMLStreamWriter out, JAXBContextImpl context)
/*     */   {
/*  57 */     Class writerClass = out.getClass();
/*  58 */     if (writerClass == FI_STAX_WRITER_CLASS)
/*     */       try {
/*  60 */         return (XmlOutput)FI_OUTPUT_CTOR.newInstance(new Object[] { out, context });
/*     */       }
/*     */       catch (Exception e) {
/*     */       }
/*  64 */     if ((STAXEX_WRITER_CLASS != null) && (STAXEX_WRITER_CLASS.isAssignableFrom(writerClass))) {
/*     */       try {
/*  66 */         return (XmlOutput)STAXEX_OUTPUT_CTOR.newInstance(new Object[] { out });
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */       }
/*     */     }
/*  72 */     return new XMLStreamWriterOutput(out);
/*     */   }
/*     */ 
/*     */   protected XMLStreamWriterOutput(XMLStreamWriter out)
/*     */   {
/*  81 */     this.out = out;
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/*  87 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*  88 */     if (!fragment)
/*  89 */       this.out.writeStartDocument();
/*     */   }
/*     */ 
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException
/*     */   {
/*  94 */     if (!fragment) {
/*  95 */       this.out.writeEndDocument();
/*  96 */       this.out.flush();
/*     */     }
/*  98 */     super.endDocument(fragment);
/*     */   }
/*     */ 
/*     */   public void beginStartTag(int prefix, String localName) throws IOException, XMLStreamException {
/* 102 */     this.out.writeStartElement(this.nsContext.getPrefix(prefix), localName, this.nsContext.getNamespaceURI(prefix));
/*     */ 
/* 107 */     NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
/* 108 */     if (nse.count() > 0)
/* 109 */       for (int i = nse.count() - 1; i >= 0; i--) {
/* 110 */         String uri = nse.getNsUri(i);
/* 111 */         if ((uri.length() != 0) || (nse.getBase() != 1))
/*     */         {
/* 113 */           this.out.writeNamespace(nse.getPrefix(i), uri);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public void attribute(int prefix, String localName, String value) throws IOException, XMLStreamException {
/* 119 */     if (prefix == -1)
/* 120 */       this.out.writeAttribute(localName, value);
/*     */     else
/* 122 */       this.out.writeAttribute(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName, value);
/*     */   }
/*     */ 
/*     */   public void endStartTag()
/*     */     throws IOException, SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endTag(int prefix, String localName)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/* 133 */     this.out.writeEndElement();
/*     */   }
/*     */ 
/*     */   public void text(String value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 137 */     if (needsSeparatingWhitespace)
/* 138 */       this.out.writeCharacters(" ");
/* 139 */     this.out.writeCharacters(value);
/*     */   }
/*     */ 
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 143 */     if (needsSeparatingWhitespace) {
/* 144 */       this.out.writeCharacters(" ");
/*     */     }
/* 146 */     int len = value.length();
/* 147 */     if (len < this.buf.length) {
/* 148 */       value.writeTo(this.buf, 0);
/* 149 */       this.out.writeCharacters(this.buf, 0, len);
/*     */     } else {
/* 151 */       this.out.writeCharacters(value.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Class initFIStAXWriterClass()
/*     */   {
/*     */     try
/*     */     {
/* 163 */       Class llfisw = Class.forName("com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter");
/* 164 */       Class sds = Class.forName("com.sun.xml.internal.fastinfoset.stax.StAXDocumentSerializer");
/*     */ 
/* 166 */       if (llfisw.isAssignableFrom(sds)) {
/* 167 */         return sds;
/*     */       }
/* 169 */       return null; } catch (Throwable e) {
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   private static Constructor<? extends XmlOutput> initFastInfosetOutputClass()
/*     */   {
/*     */     try {
/* 177 */       if (FI_STAX_WRITER_CLASS == null)
/* 178 */         return null;
/* 179 */       Class c = Class.forName("com.sun.xml.internal.bind.v2.runtime.output.FastInfosetStreamWriterOutput");
/* 180 */       return c.getConstructor(new Class[] { FI_STAX_WRITER_CLASS, JAXBContextImpl.class }); } catch (Throwable e) {
/*     */     }
/* 182 */     return null;
/*     */   }
/*     */ 
/*     */   private static Class initStAXExWriterClass()
/*     */   {
/*     */     try
/*     */     {
/* 194 */       return Class.forName("com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx"); } catch (Throwable e) {
/*     */     }
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */   private static Constructor<? extends XmlOutput> initStAXExOutputClass()
/*     */   {
/*     */     try {
/* 202 */       Class c = Class.forName("com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput");
/* 203 */       return c.getConstructor(new Class[] { STAXEX_WRITER_CLASS }); } catch (Throwable e) {
/*     */     }
/* 205 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.XMLStreamWriterOutput
 * JD-Core Version:    0.6.2
 */