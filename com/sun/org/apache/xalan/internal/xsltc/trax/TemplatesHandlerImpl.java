/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.SourceLoader;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Templates;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.sax.TemplatesHandler;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class TemplatesHandlerImpl
/*     */   implements ContentHandler, TemplatesHandler, SourceLoader
/*     */ {
/*     */   private String _systemId;
/*     */   private int _indentNumber;
/*  70 */   private URIResolver _uriResolver = null;
/*     */ 
/*  76 */   private TransformerFactoryImpl _tfactory = null;
/*     */ 
/*  81 */   private Parser _parser = null;
/*     */ 
/*  86 */   private TemplatesImpl _templates = null;
/*     */ 
/*     */   protected TemplatesHandlerImpl(int indentNumber, TransformerFactoryImpl tfactory)
/*     */   {
/*  94 */     this._indentNumber = indentNumber;
/*  95 */     this._tfactory = tfactory;
/*     */ 
/*  98 */     XSLTC xsltc = new XSLTC(tfactory.useServicesMechnism(), tfactory.getFeatureManager());
/*  99 */     if (tfactory.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 100 */       xsltc.setSecureProcessing(true);
/*     */     }
/* 102 */     xsltc.setProperty("http://javax.xml.XMLConstants/property/accessExternalStylesheet", (String)tfactory.getAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet"));
/*     */ 
/* 104 */     xsltc.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", (String)tfactory.getAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD"));
/*     */ 
/* 106 */     xsltc.setProperty("http://apache.org/xml/properties/security-manager", tfactory.getAttribute("http://apache.org/xml/properties/security-manager"));
/*     */ 
/* 110 */     if ("true".equals(tfactory.getAttribute("enable-inlining")))
/* 111 */       xsltc.setTemplateInlining(true);
/*     */     else {
/* 113 */       xsltc.setTemplateInlining(false);
/*     */     }
/* 115 */     this._parser = xsltc.getParser();
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 125 */     return this._systemId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String id)
/*     */   {
/* 135 */     this._systemId = id;
/*     */   }
/*     */ 
/*     */   public void setURIResolver(URIResolver resolver)
/*     */   {
/* 142 */     this._uriResolver = resolver;
/*     */   }
/*     */ 
/*     */   public Templates getTemplates()
/*     */   {
/* 155 */     return this._templates;
/*     */   }
/*     */ 
/*     */   public InputSource loadSource(String href, String context, XSLTC xsltc)
/*     */   {
/*     */     try
/*     */     {
/* 170 */       Source source = this._uriResolver.resolve(href, context);
/* 171 */       if (source != null) {
/* 172 */         return Util.getInputSource(xsltc, source);
/*     */       }
/*     */     }
/*     */     catch (TransformerException e)
/*     */     {
/*     */     }
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/* 187 */     XSLTC xsltc = this._parser.getXSLTC();
/* 188 */     xsltc.init();
/* 189 */     xsltc.setOutputType(2);
/* 190 */     this._parser.startDocument();
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 197 */     this._parser.endDocument();
/*     */     try
/*     */     {
/* 201 */       XSLTC xsltc = this._parser.getXSLTC();
/*     */       String transletName;
/* 205 */       if (this._systemId != null) {
/* 206 */         transletName = Util.baseName(this._systemId);
/*     */       }
/*     */       else {
/* 209 */         transletName = (String)this._tfactory.getAttribute("translet-name");
/*     */       }
/* 211 */       xsltc.setClassName(transletName);
/*     */ 
/* 214 */       String transletName = xsltc.getClassName();
/*     */ 
/* 216 */       Stylesheet stylesheet = null;
/* 217 */       SyntaxTreeNode root = this._parser.getDocumentRoot();
/*     */ 
/* 220 */       if ((!this._parser.errorsFound()) && (root != null))
/*     */       {
/* 222 */         stylesheet = this._parser.makeStylesheet(root);
/* 223 */         stylesheet.setSystemId(this._systemId);
/* 224 */         stylesheet.setParentStylesheet(null);
/*     */ 
/* 226 */         if (xsltc.getTemplateInlining())
/* 227 */           stylesheet.setTemplateInlining(true);
/*     */         else {
/* 229 */           stylesheet.setTemplateInlining(false);
/*     */         }
/*     */ 
/* 232 */         if (this._uriResolver != null) {
/* 233 */           stylesheet.setSourceLoader(this);
/*     */         }
/*     */ 
/* 236 */         this._parser.setCurrentStylesheet(stylesheet);
/*     */ 
/* 239 */         xsltc.setStylesheet(stylesheet);
/*     */ 
/* 242 */         this._parser.createAST(stylesheet);
/*     */       }
/*     */ 
/* 246 */       if ((!this._parser.errorsFound()) && (stylesheet != null)) {
/* 247 */         stylesheet.setMultiDocument(xsltc.isMultiDocument());
/* 248 */         stylesheet.setHasIdCall(xsltc.hasIdCall());
/*     */ 
/* 251 */         synchronized (xsltc.getClass()) {
/* 252 */           stylesheet.translate();
/*     */         }
/*     */       }
/*     */ 
/* 256 */       if (!this._parser.errorsFound())
/*     */       {
/* 258 */         byte[][] bytecodes = xsltc.getBytecodes();
/* 259 */         if (bytecodes != null) {
/* 260 */           this._templates = new TemplatesImpl(xsltc.getBytecodes(), transletName, this._parser.getOutputProperties(), this._indentNumber, this._tfactory);
/*     */ 
/* 265 */           if (this._uriResolver != null)
/* 266 */             this._templates.setURIResolver(this._uriResolver);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 271 */         StringBuffer errorMessage = new StringBuffer();
/* 272 */         Vector errors = this._parser.getErrors();
/* 273 */         int count = errors.size();
/* 274 */         for (int i = 0; i < count; i++) {
/* 275 */           if (errorMessage.length() > 0)
/* 276 */             errorMessage.append('\n');
/* 277 */           errorMessage.append(errors.elementAt(i).toString());
/*     */         }
/* 279 */         throw new SAXException("JAXP_COMPILE_ERR", new TransformerException(errorMessage.toString()));
/*     */       }
/*     */     }
/*     */     catch (CompilerException e) {
/* 283 */       throw new SAXException("JAXP_COMPILE_ERR", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */   {
/* 291 */     this._parser.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */   {
/* 298 */     this._parser.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localname, String qname, Attributes attributes)
/*     */     throws SAXException
/*     */   {
/* 307 */     this._parser.startElement(uri, localname, qname, attributes);
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localname, String qname)
/*     */   {
/* 314 */     this._parser.endElement(uri, localname, qname);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */   {
/* 321 */     this._parser.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String name, String value)
/*     */   {
/* 328 */     this._parser.processingInstruction(name, value);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */   {
/* 335 */     this._parser.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */   {
/* 342 */     this._parser.skippedEntity(name);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 349 */     setSystemId(locator.getSystemId());
/* 350 */     this._parser.setDocumentLocator(locator);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesHandlerImpl
 * JD-Core Version:    0.6.2
 */