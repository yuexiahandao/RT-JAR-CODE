/*     */ package com.sun.org.apache.xalan.internal.xsltc.runtime.output;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.trax.SAX2DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXEventWriter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXStreamWriter;
/*     */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*     */ import com.sun.org.apache.xml.internal.serializer.ToHTMLSAXHandler;
/*     */ import com.sun.org.apache.xml.internal.serializer.ToHTMLStream;
/*     */ import com.sun.org.apache.xml.internal.serializer.ToTextSAXHandler;
/*     */ import com.sun.org.apache.xml.internal.serializer.ToTextStream;
/*     */ import com.sun.org.apache.xml.internal.serializer.ToUnknownStream;
/*     */ import com.sun.org.apache.xml.internal.serializer.ToXMLSAXHandler;
/*     */ import com.sun.org.apache.xml.internal.serializer.ToXMLStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public class TransletOutputHandlerFactory
/*     */ {
/*     */   public static final int STREAM = 0;
/*     */   public static final int SAX = 1;
/*     */   public static final int DOM = 2;
/*     */   public static final int STAX = 3;
/*  62 */   private String _encoding = "utf-8";
/*  63 */   private String _method = null;
/*  64 */   private int _outputType = 0;
/*  65 */   private OutputStream _ostream = System.out;
/*  66 */   private Writer _writer = null;
/*  67 */   private Node _node = null;
/*  68 */   private Node _nextSibling = null;
/*  69 */   private XMLEventWriter _xmlStAXEventWriter = null;
/*  70 */   private XMLStreamWriter _xmlStAXStreamWriter = null;
/*  71 */   private int _indentNumber = -1;
/*  72 */   private ContentHandler _handler = null;
/*  73 */   private LexicalHandler _lexHandler = null;
/*     */   private boolean _useServicesMechanism;
/*     */ 
/*     */   public static TransletOutputHandlerFactory newInstance()
/*     */   {
/*  78 */     return new TransletOutputHandlerFactory(true);
/*     */   }
/*     */   public static TransletOutputHandlerFactory newInstance(boolean useServicesMechanism) {
/*  81 */     return new TransletOutputHandlerFactory(useServicesMechanism);
/*     */   }
/*     */ 
/*     */   public TransletOutputHandlerFactory(boolean useServicesMechanism) {
/*  85 */     this._useServicesMechanism = useServicesMechanism;
/*     */   }
/*     */   public void setOutputType(int outputType) {
/*  88 */     this._outputType = outputType;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding) {
/*  92 */     if (encoding != null)
/*  93 */       this._encoding = encoding;
/*     */   }
/*     */ 
/*     */   public void setOutputMethod(String method)
/*     */   {
/*  98 */     this._method = method;
/*     */   }
/*     */ 
/*     */   public void setOutputStream(OutputStream ostream) {
/* 102 */     this._ostream = ostream;
/*     */   }
/*     */ 
/*     */   public void setWriter(Writer writer) {
/* 106 */     this._writer = writer;
/*     */   }
/*     */ 
/*     */   public void setHandler(ContentHandler handler) {
/* 110 */     this._handler = handler;
/*     */   }
/*     */ 
/*     */   public void setLexicalHandler(LexicalHandler lex) {
/* 114 */     this._lexHandler = lex;
/*     */   }
/*     */ 
/*     */   public void setNode(Node node) {
/* 118 */     this._node = node;
/*     */   }
/*     */ 
/*     */   public Node getNode() {
/* 122 */     return (this._handler instanceof SAX2DOM) ? ((SAX2DOM)this._handler).getDOM() : null;
/*     */   }
/*     */ 
/*     */   public void setNextSibling(Node nextSibling)
/*     */   {
/* 127 */     this._nextSibling = nextSibling;
/*     */   }
/*     */ 
/*     */   public XMLEventWriter getXMLEventWriter() {
/* 131 */     return (this._handler instanceof SAX2StAXEventWriter) ? ((SAX2StAXEventWriter)this._handler).getEventWriter() : null;
/*     */   }
/*     */ 
/*     */   public void setXMLEventWriter(XMLEventWriter eventWriter) {
/* 135 */     this._xmlStAXEventWriter = eventWriter;
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter getXMLStreamWriter() {
/* 139 */     return (this._handler instanceof SAX2StAXStreamWriter) ? ((SAX2StAXStreamWriter)this._handler).getStreamWriter() : null;
/*     */   }
/*     */ 
/*     */   public void setXMLStreamWriter(XMLStreamWriter streamWriter) {
/* 143 */     this._xmlStAXStreamWriter = streamWriter;
/*     */   }
/*     */ 
/*     */   public void setIndentNumber(int value) {
/* 147 */     this._indentNumber = value;
/*     */   }
/*     */ 
/*     */   public SerializationHandler getSerializationHandler()
/*     */     throws IOException, ParserConfigurationException
/*     */   {
/* 153 */     SerializationHandler result = null;
/* 154 */     switch (this._outputType)
/*     */     {
/*     */     case 0:
/* 158 */       if (this._method == null)
/*     */       {
/* 160 */         result = new ToUnknownStream();
/*     */       }
/* 162 */       else if (this._method.equalsIgnoreCase("xml"))
/*     */       {
/* 165 */         result = new ToXMLStream();
/*     */       }
/* 168 */       else if (this._method.equalsIgnoreCase("html"))
/*     */       {
/* 171 */         result = new ToHTMLStream();
/*     */       }
/* 174 */       else if (this._method.equalsIgnoreCase("text"))
/*     */       {
/* 177 */         result = new ToTextStream();
/*     */       }
/*     */ 
/* 181 */       if ((result != null) && (this._indentNumber >= 0))
/*     */       {
/* 183 */         result.setIndentAmount(this._indentNumber);
/*     */       }
/*     */ 
/* 186 */       result.setEncoding(this._encoding);
/*     */ 
/* 188 */       if (this._writer != null)
/*     */       {
/* 190 */         result.setWriter(this._writer);
/*     */       }
/*     */       else
/*     */       {
/* 194 */         result.setOutputStream(this._ostream);
/*     */       }
/* 196 */       return result;
/*     */     case 2:
/* 199 */       this._handler = (this._node != null ? new SAX2DOM(this._node, this._nextSibling, this._useServicesMechanism) : new SAX2DOM(this._useServicesMechanism));
/* 200 */       this._lexHandler = ((LexicalHandler)this._handler);
/*     */     case 3:
/* 203 */       if (this._xmlStAXEventWriter != null)
/* 204 */         this._handler = new SAX2StAXEventWriter(this._xmlStAXEventWriter);
/* 205 */       else if (this._xmlStAXStreamWriter != null) {
/* 206 */         this._handler = new SAX2StAXStreamWriter(this._xmlStAXStreamWriter);
/*     */       }
/* 208 */       this._lexHandler = ((LexicalHandler)this._handler);
/*     */     case 1:
/* 211 */       if (this._method == null)
/*     */       {
/* 213 */         this._method = "xml";
/*     */       }
/*     */ 
/* 216 */       if (this._method.equalsIgnoreCase("xml"))
/*     */       {
/* 219 */         if (this._lexHandler == null)
/*     */         {
/* 221 */           result = new ToXMLSAXHandler(this._handler, this._encoding);
/*     */         }
/*     */         else
/*     */         {
/* 225 */           result = new ToXMLSAXHandler(this._handler, this._lexHandler, this._encoding);
/*     */         }
/*     */ 
/*     */       }
/* 233 */       else if (this._method.equalsIgnoreCase("html"))
/*     */       {
/* 236 */         if (this._lexHandler == null)
/*     */         {
/* 238 */           result = new ToHTMLSAXHandler(this._handler, this._encoding);
/*     */         }
/*     */         else
/*     */         {
/* 242 */           result = new ToHTMLSAXHandler(this._handler, this._lexHandler, this._encoding);
/*     */         }
/*     */ 
/*     */       }
/* 250 */       else if (this._method.equalsIgnoreCase("text"))
/*     */       {
/* 253 */         if (this._lexHandler == null)
/*     */         {
/* 255 */           result = new ToTextSAXHandler(this._handler, this._encoding);
/*     */         }
/*     */         else
/*     */         {
/* 259 */           result = new ToTextSAXHandler(this._handler, this._lexHandler, this._encoding);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 267 */       return result;
/*     */     }
/* 269 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory
 * JD-Core Version:    0.6.2
 */