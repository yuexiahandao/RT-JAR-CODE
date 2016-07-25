/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*     */ import java.io.IOException;
/*     */ import org.w3c.dom.Document;
/*     */ 
/*     */ public class SchemaDOMParser extends DefaultXMLDocumentHandler
/*     */ {
/*     */   public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   public static final String GENERATE_SYNTHETIC_ANNOTATION = "http://apache.org/xml/features/generate-synthetic-annotations";
/*     */   protected XMLLocator fLocator;
/*  70 */   protected NamespaceContext fNamespaceContext = null;
/*     */   SchemaDOM schemaDOM;
/*     */   XMLParserConfiguration config;
/*     */   private ElementImpl fCurrentAnnotationElement;
/*  92 */   private int fAnnotationDepth = -1;
/*     */ 
/*  95 */   private int fInnerAnnotationDepth = -1;
/*     */ 
/*  97 */   private int fDepth = -1;
/*     */   XMLErrorReporter fErrorReporter;
/* 102 */   private boolean fGenerateSyntheticAnnotation = false;
/* 103 */   private BooleanStack fHasNonSchemaAttributes = new BooleanStack();
/* 104 */   private BooleanStack fSawAnnotation = new BooleanStack();
/* 105 */   private XMLAttributes fEmptyAttr = new XMLAttributesImpl();
/*     */ 
/*     */   public SchemaDOMParser(XMLParserConfiguration config)
/*     */   {
/*  82 */     this.config = config;
/*  83 */     config.setDocumentHandler(this);
/*  84 */     config.setDTDHandler(this);
/*  85 */     config.setDTDContentModelHandler(this);
/*     */   }
/*     */ 
/*     */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 114 */     this.fErrorReporter = ((XMLErrorReporter)this.config.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/* 115 */     this.fGenerateSyntheticAnnotation = this.config.getFeature("http://apache.org/xml/features/generate-synthetic-annotations");
/* 116 */     this.fHasNonSchemaAttributes.clear();
/* 117 */     this.fSawAnnotation.clear();
/* 118 */     this.schemaDOM = new SchemaDOM();
/* 119 */     this.fCurrentAnnotationElement = null;
/* 120 */     this.fAnnotationDepth = -1;
/* 121 */     this.fInnerAnnotationDepth = -1;
/* 122 */     this.fDepth = -1;
/* 123 */     this.fLocator = locator;
/* 124 */     this.fNamespaceContext = namespaceContext;
/* 125 */     this.schemaDOM.setDocumentURI(locator.getExpandedSystemId());
/*     */   }
/*     */ 
/*     */   public void endDocument(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void comment(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 150 */     if (this.fAnnotationDepth > -1)
/* 151 */       this.schemaDOM.comment(text);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 175 */     if (this.fAnnotationDepth > -1)
/* 176 */       this.schemaDOM.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void characters(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 191 */     if (this.fInnerAnnotationDepth == -1) {
/* 192 */       for (int i = text.offset; i < text.offset + text.length; i++)
/*     */       {
/* 194 */         if (!XMLChar.isSpace(text.ch[i]))
/*     */         {
/* 196 */           String txt = new String(text.ch, i, text.length + text.offset - i);
/*     */ 
/* 198 */           this.fErrorReporter.reportError(this.fLocator, "http://www.w3.org/TR/xml-schema-1", "s4s-elt-character", new Object[] { txt }, (short)1);
/*     */ 
/* 203 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 213 */       this.schemaDOM.characters(text);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 232 */     this.fDepth += 1;
/*     */ 
/* 238 */     if (this.fAnnotationDepth == -1) {
/* 239 */       if ((element.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (element.localpart == SchemaSymbols.ELT_ANNOTATION))
/*     */       {
/* 241 */         if (this.fGenerateSyntheticAnnotation) {
/* 242 */           if (this.fSawAnnotation.size() > 0) {
/* 243 */             this.fSawAnnotation.pop();
/*     */           }
/* 245 */           this.fSawAnnotation.push(true);
/*     */         }
/* 247 */         this.fAnnotationDepth = this.fDepth;
/* 248 */         this.schemaDOM.startAnnotation(element, attributes, this.fNamespaceContext);
/* 249 */         this.fCurrentAnnotationElement = this.schemaDOM.startElement(element, attributes, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
/*     */ 
/* 253 */         return;
/*     */       }
/* 255 */       if ((element.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (this.fGenerateSyntheticAnnotation)) {
/* 256 */         this.fSawAnnotation.push(false);
/* 257 */         this.fHasNonSchemaAttributes.push(hasNonSchemaAttributes(element, attributes));
/*     */       }
/*     */     }
/* 260 */     else if (this.fDepth == this.fAnnotationDepth + 1) {
/* 261 */       this.fInnerAnnotationDepth = this.fDepth;
/* 262 */       this.schemaDOM.startAnnotationElement(element, attributes);
/*     */     }
/*     */     else {
/* 265 */       this.schemaDOM.startAnnotationElement(element, attributes);
/*     */ 
/* 267 */       return;
/*     */     }
/* 269 */     this.schemaDOM.startElement(element, attributes, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
/*     */   }
/*     */ 
/*     */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 290 */     if ((this.fGenerateSyntheticAnnotation) && (this.fAnnotationDepth == -1) && (element.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (element.localpart != SchemaSymbols.ELT_ANNOTATION) && (hasNonSchemaAttributes(element, attributes)))
/*     */     {
/* 293 */       this.schemaDOM.startElement(element, attributes, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
/*     */ 
/* 298 */       attributes.removeAllAttributes();
/* 299 */       String schemaPrefix = this.fNamespaceContext.getPrefix(SchemaSymbols.URI_SCHEMAFORSCHEMA);
/* 300 */       String annRawName = schemaPrefix + ':' + SchemaSymbols.ELT_ANNOTATION;
/* 301 */       this.schemaDOM.startAnnotation(annRawName, attributes, this.fNamespaceContext);
/* 302 */       String elemRawName = schemaPrefix + ':' + SchemaSymbols.ELT_DOCUMENTATION;
/* 303 */       this.schemaDOM.startAnnotationElement(elemRawName, attributes);
/* 304 */       this.schemaDOM.charactersRaw("SYNTHETIC_ANNOTATION");
/* 305 */       this.schemaDOM.endSyntheticAnnotationElement(elemRawName, false);
/* 306 */       this.schemaDOM.endSyntheticAnnotationElement(annRawName, true);
/*     */ 
/* 308 */       this.schemaDOM.endElement();
/*     */ 
/* 310 */       return;
/*     */     }
/*     */ 
/* 323 */     if (this.fAnnotationDepth == -1)
/*     */     {
/* 325 */       if ((element.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (element.localpart == SchemaSymbols.ELT_ANNOTATION))
/*     */       {
/* 327 */         this.schemaDOM.startAnnotation(element, attributes, this.fNamespaceContext);
/*     */       }
/*     */     }
/*     */     else {
/* 331 */       this.schemaDOM.startAnnotationElement(element, attributes);
/*     */     }
/*     */ 
/* 334 */     ElementImpl newElem = this.schemaDOM.emptyElement(element, attributes, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
/*     */ 
/* 339 */     if (this.fAnnotationDepth == -1)
/*     */     {
/* 341 */       if ((element.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (element.localpart == SchemaSymbols.ELT_ANNOTATION))
/*     */       {
/* 343 */         this.schemaDOM.endAnnotation(element, newElem);
/*     */       }
/*     */     }
/*     */     else
/* 347 */       this.schemaDOM.endAnnotationElement(element);
/*     */   }
/*     */ 
/*     */   public void endElement(QName element, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 365 */     if (this.fAnnotationDepth > -1) {
/* 366 */       if (this.fInnerAnnotationDepth == this.fDepth) {
/* 367 */         this.fInnerAnnotationDepth = -1;
/* 368 */         this.schemaDOM.endAnnotationElement(element);
/* 369 */         this.schemaDOM.endElement();
/* 370 */       } else if (this.fAnnotationDepth == this.fDepth) {
/* 371 */         this.fAnnotationDepth = -1;
/* 372 */         this.schemaDOM.endAnnotation(element, this.fCurrentAnnotationElement);
/* 373 */         this.schemaDOM.endElement();
/*     */       } else {
/* 375 */         this.schemaDOM.endAnnotationElement(element);
/*     */       }
/*     */     } else {
/* 378 */       if ((element.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (this.fGenerateSyntheticAnnotation)) {
/* 379 */         boolean value = this.fHasNonSchemaAttributes.pop();
/* 380 */         boolean sawann = this.fSawAnnotation.pop();
/* 381 */         if ((value) && (!sawann)) {
/* 382 */           String schemaPrefix = this.fNamespaceContext.getPrefix(SchemaSymbols.URI_SCHEMAFORSCHEMA);
/* 383 */           String annRawName = schemaPrefix + ':' + SchemaSymbols.ELT_ANNOTATION;
/* 384 */           this.schemaDOM.startAnnotation(annRawName, this.fEmptyAttr, this.fNamespaceContext);
/* 385 */           String elemRawName = schemaPrefix + ':' + SchemaSymbols.ELT_DOCUMENTATION;
/* 386 */           this.schemaDOM.startAnnotationElement(elemRawName, this.fEmptyAttr);
/* 387 */           this.schemaDOM.charactersRaw("SYNTHETIC_ANNOTATION");
/* 388 */           this.schemaDOM.endSyntheticAnnotationElement(elemRawName, false);
/* 389 */           this.schemaDOM.endSyntheticAnnotationElement(annRawName, true);
/*     */         }
/*     */       }
/* 392 */       this.schemaDOM.endElement();
/*     */     }
/* 394 */     this.fDepth -= 1;
/*     */   }
/*     */ 
/*     */   private boolean hasNonSchemaAttributes(QName element, XMLAttributes attributes)
/*     */   {
/* 403 */     int length = attributes.getLength();
/* 404 */     for (int i = 0; i < length; i++) {
/* 405 */       String uri = attributes.getURI(i);
/* 406 */       if ((uri != null) && (uri != SchemaSymbols.URI_SCHEMAFORSCHEMA) && (uri != NamespaceContext.XMLNS_URI) && ((uri != NamespaceContext.XML_URI) || (attributes.getQName(i) != SchemaSymbols.ATT_XML_LANG) || (element.localpart != SchemaSymbols.ELT_SCHEMA)))
/*     */       {
/* 410 */         return true;
/*     */       }
/*     */     }
/* 413 */     return false;
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 432 */     if (this.fAnnotationDepth != -1)
/* 433 */       this.schemaDOM.characters(text);
/*     */   }
/*     */ 
/*     */   public void startCDATA(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 447 */     if (this.fAnnotationDepth != -1)
/* 448 */       this.schemaDOM.startAnnotationCDATA();
/*     */   }
/*     */ 
/*     */   public void endCDATA(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 462 */     if (this.fAnnotationDepth != -1)
/* 463 */       this.schemaDOM.endAnnotationCDATA();
/*     */   }
/*     */ 
/*     */   public Document getDocument()
/*     */   {
/* 476 */     return this.schemaDOM;
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state)
/*     */   {
/* 485 */     this.config.setFeature(featureId, state);
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String featureId)
/*     */   {
/* 494 */     return this.config.getFeature(featureId);
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */   {
/* 503 */     this.config.setProperty(propertyId, value);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String propertyId)
/*     */   {
/* 512 */     return this.config.getProperty(propertyId);
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(XMLEntityResolver er)
/*     */   {
/* 520 */     this.config.setEntityResolver(er);
/*     */   }
/*     */ 
/*     */   public void parse(XMLInputSource inputSource)
/*     */     throws IOException
/*     */   {
/* 530 */     this.config.parse(inputSource);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 537 */     ((SchemaParsingConfig)this.config).reset();
/*     */   }
/*     */ 
/*     */   public void resetNodePool()
/*     */   {
/* 544 */     ((SchemaParsingConfig)this.config).resetNodePool();
/*     */   }
/*     */ 
/*     */   private static final class BooleanStack
/*     */   {
/*     */     private int fDepth;
/*     */     private boolean[] fData;
/*     */ 
/*     */     public int size()
/*     */     {
/* 576 */       return this.fDepth;
/*     */     }
/*     */ 
/*     */     public void push(boolean value)
/*     */     {
/* 581 */       ensureCapacity(this.fDepth + 1);
/* 582 */       this.fData[(this.fDepth++)] = value;
/*     */     }
/*     */ 
/*     */     public boolean pop()
/*     */     {
/* 587 */       return this.fData[(--this.fDepth)];
/*     */     }
/*     */ 
/*     */     public void clear()
/*     */     {
/* 592 */       this.fDepth = 0;
/*     */     }
/*     */ 
/*     */     private void ensureCapacity(int size)
/*     */     {
/* 601 */       if (this.fData == null) {
/* 602 */         this.fData = new boolean[32];
/*     */       }
/* 604 */       else if (this.fData.length <= size) {
/* 605 */         boolean[] newdata = new boolean[this.fData.length * 2];
/* 606 */         System.arraycopy(this.fData, 0, newdata, 0, this.fData.length);
/* 607 */         this.fData = newdata;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaDOMParser
 * JD-Core Version:    0.6.2
 */