/*     */ package com.sun.xml.internal.stream.buffer;
/*     */ 
/*     */ import com.sun.xml.internal.stream.buffer.sax.SAXBufferCreator;
/*     */ import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferCreator;
/*     */ import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class MutableXMLStreamBuffer extends XMLStreamBuffer
/*     */ {
/*     */   public static final int DEFAULT_ARRAY_SIZE = 512;
/*     */ 
/*     */   public MutableXMLStreamBuffer()
/*     */   {
/*  72 */     this(512);
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/*  80 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public MutableXMLStreamBuffer(int size)
/*     */   {
/*  93 */     this._structure = new FragmentedArray(new byte[size]);
/*  94 */     this._structureStrings = new FragmentedArray(new String[size]);
/*  95 */     this._contentCharactersBuffer = new FragmentedArray(new char[4096]);
/*  96 */     this._contentObjects = new FragmentedArray(new Object[size]);
/*     */ 
/* 100 */     ((byte[])this._structure.getArray())[0] = -112;
/*     */   }
/*     */ 
/*     */   public void createFromXMLStreamReader(XMLStreamReader reader)
/*     */     throws XMLStreamException
/*     */   {
/* 117 */     reset();
/* 118 */     StreamReaderBufferCreator c = new StreamReaderBufferCreator(this);
/* 119 */     c.create(reader);
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter createFromXMLStreamWriter()
/*     */   {
/* 133 */     reset();
/* 134 */     return new StreamWriterBufferCreator(this);
/*     */   }
/*     */ 
/*     */   public SAXBufferCreator createFromSAXBufferCreator()
/*     */   {
/* 150 */     reset();
/* 151 */     SAXBufferCreator c = new SAXBufferCreator();
/* 152 */     c.setBuffer(this);
/* 153 */     return c;
/*     */   }
/*     */ 
/*     */   public void createFromXMLReader(XMLReader reader, InputStream in)
/*     */     throws SAXException, IOException
/*     */   {
/* 172 */     createFromXMLReader(reader, in, null);
/*     */   }
/*     */ 
/*     */   public void createFromXMLReader(XMLReader reader, InputStream in, String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 193 */     reset();
/* 194 */     SAXBufferCreator c = new SAXBufferCreator(this);
/*     */ 
/* 196 */     reader.setContentHandler(c);
/* 197 */     reader.setDTDHandler(c);
/* 198 */     reader.setProperty("http://xml.org/sax/properties/lexical-handler", c);
/*     */ 
/* 200 */     c.create(reader, in, systemId);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 215 */     this._structurePtr = (this._structureStringsPtr = this._contentCharactersBufferPtr = this._contentObjectsPtr = 0);
/*     */ 
/* 222 */     ((byte[])this._structure.getArray())[0] = -112;
/*     */ 
/* 225 */     this._contentObjects.setNext(null);
/* 226 */     Object[] o = (Object[])this._contentObjects.getArray();
/* 227 */     for (int i = 0; (i < o.length) && 
/* 228 */       (o[i] != null); i++)
/*     */     {
/* 229 */       o[i] = null;
/*     */     }
/*     */ 
/* 235 */     this.treeCount = 0;
/*     */   }
/*     */ 
/*     */   protected void setHasInternedStrings(boolean hasInternedStrings)
/*     */   {
/* 245 */     this._hasInternedStrings = hasInternedStrings;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer
 * JD-Core Version:    0.6.2
 */