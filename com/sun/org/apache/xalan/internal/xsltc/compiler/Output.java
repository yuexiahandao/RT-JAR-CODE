/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.serializer.Encodings;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ final class Output extends TopLevelElement
/*     */ {
/*     */   private String _version;
/*     */   private String _method;
/*     */   private String _encoding;
/*  58 */   private boolean _omitHeader = false;
/*     */   private String _standalone;
/*     */   private String _doctypePublic;
/*     */   private String _doctypeSystem;
/*     */   private String _cdata;
/*  63 */   private boolean _indent = false;
/*     */   private String _mediaType;
/*     */   private String _indentamount;
/*  68 */   private boolean _disabled = false;
/*     */   private static final String STRING_SIG = "Ljava/lang/String;";
/*     */   private static final String XML_VERSION = "1.0";
/*     */   private static final String HTML_VERSION = "4.0";
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  79 */     indent(indent);
/*  80 */     Util.println("Output " + this._method);
/*     */   }
/*     */ 
/*     */   public void disable()
/*     */   {
/*  89 */     this._disabled = true;
/*     */   }
/*     */ 
/*     */   public boolean enabled() {
/*  93 */     return !this._disabled;
/*     */   }
/*     */ 
/*     */   public String getCdata() {
/*  97 */     return this._cdata;
/*     */   }
/*     */ 
/*     */   public String getOutputMethod() {
/* 101 */     return this._method;
/*     */   }
/*     */ 
/*     */   private void transferAttribute(Output previous, String qname) {
/* 105 */     if ((!hasAttribute(qname)) && (previous.hasAttribute(qname)))
/* 106 */       addAttribute(qname, previous.getAttribute(qname));
/*     */   }
/*     */ 
/*     */   public void mergeOutput(Output previous)
/*     */   {
/* 112 */     transferAttribute(previous, "version");
/* 113 */     transferAttribute(previous, "method");
/* 114 */     transferAttribute(previous, "encoding");
/* 115 */     transferAttribute(previous, "doctype-system");
/* 116 */     transferAttribute(previous, "doctype-public");
/* 117 */     transferAttribute(previous, "media-type");
/* 118 */     transferAttribute(previous, "indent");
/* 119 */     transferAttribute(previous, "omit-xml-declaration");
/* 120 */     transferAttribute(previous, "standalone");
/*     */ 
/* 123 */     if (previous.hasAttribute("cdata-section-elements"))
/*     */     {
/* 125 */       addAttribute("cdata-section-elements", previous.getAttribute("cdata-section-elements") + ' ' + getAttribute("cdata-section-elements"));
/*     */     }
/*     */ 
/* 131 */     String prefix = lookupPrefix("http://xml.apache.org/xalan");
/* 132 */     if (prefix != null) {
/* 133 */       transferAttribute(previous, prefix + ':' + "indent-amount");
/*     */     }
/* 135 */     prefix = lookupPrefix("http://xml.apache.org/xslt");
/* 136 */     if (prefix != null)
/* 137 */       transferAttribute(previous, prefix + ':' + "indent-amount");
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 145 */     Properties outputProperties = new Properties();
/*     */ 
/* 148 */     parser.setOutput(this);
/*     */ 
/* 151 */     if (this._disabled) return;
/*     */ 
/* 153 */     String attrib = null;
/*     */ 
/* 156 */     this._version = getAttribute("version");
/* 157 */     if (this._version.equals("")) {
/* 158 */       this._version = null;
/*     */     }
/*     */     else {
/* 161 */       outputProperties.setProperty("version", this._version);
/*     */     }
/*     */ 
/* 165 */     this._method = getAttribute("method");
/* 166 */     if (this._method.equals("")) {
/* 167 */       this._method = null;
/*     */     }
/* 169 */     if (this._method != null) {
/* 170 */       this._method = this._method.toLowerCase();
/* 171 */       if ((this._method.equals("xml")) || (this._method.equals("html")) || (this._method.equals("text")) || ((XML11Char.isXML11ValidQName(this._method)) && (this._method.indexOf(":") > 0)))
/*     */       {
/* 175 */         outputProperties.setProperty("method", this._method);
/*     */       }
/* 177 */       else reportError(this, parser, "INVALID_METHOD_IN_OUTPUT", this._method);
/*     */ 
/*     */     }
/*     */ 
/* 182 */     this._encoding = getAttribute("encoding");
/* 183 */     if (this._encoding.equals("")) {
/* 184 */       this._encoding = null;
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 190 */         String canonicalEncoding = Encodings.convertMime2JavaEncoding(this._encoding);
/* 191 */         writer = new OutputStreamWriter(System.out, canonicalEncoding);
/*     */       }
/*     */       catch (UnsupportedEncodingException e)
/*     */       {
/*     */         OutputStreamWriter writer;
/* 195 */         ErrorMsg msg = new ErrorMsg("UNSUPPORTED_ENCODING", this._encoding, this);
/*     */ 
/* 197 */         parser.reportError(4, msg);
/*     */       }
/* 199 */       outputProperties.setProperty("encoding", this._encoding);
/*     */     }
/*     */ 
/* 203 */     attrib = getAttribute("omit-xml-declaration");
/* 204 */     if (!attrib.equals("")) {
/* 205 */       if (attrib.equals("yes")) {
/* 206 */         this._omitHeader = true;
/*     */       }
/* 208 */       outputProperties.setProperty("omit-xml-declaration", attrib);
/*     */     }
/*     */ 
/* 212 */     this._standalone = getAttribute("standalone");
/* 213 */     if (this._standalone.equals("")) {
/* 214 */       this._standalone = null;
/*     */     }
/*     */     else {
/* 217 */       outputProperties.setProperty("standalone", this._standalone);
/*     */     }
/*     */ 
/* 221 */     this._doctypeSystem = getAttribute("doctype-system");
/* 222 */     if (this._doctypeSystem.equals("")) {
/* 223 */       this._doctypeSystem = null;
/*     */     }
/*     */     else {
/* 226 */       outputProperties.setProperty("doctype-system", this._doctypeSystem);
/*     */     }
/*     */ 
/* 230 */     this._doctypePublic = getAttribute("doctype-public");
/* 231 */     if (this._doctypePublic.equals("")) {
/* 232 */       this._doctypePublic = null;
/*     */     }
/*     */     else {
/* 235 */       outputProperties.setProperty("doctype-public", this._doctypePublic);
/*     */     }
/*     */ 
/* 239 */     this._cdata = getAttribute("cdata-section-elements");
/* 240 */     if (this._cdata.equals("")) {
/* 241 */       this._cdata = null;
/*     */     }
/*     */     else {
/* 244 */       StringBuffer expandedNames = new StringBuffer();
/* 245 */       StringTokenizer tokens = new StringTokenizer(this._cdata);
/*     */ 
/* 248 */       while (tokens.hasMoreTokens()) {
/* 249 */         String qname = tokens.nextToken();
/* 250 */         if (!XML11Char.isXML11ValidQName(qname)) {
/* 251 */           ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", qname, this);
/* 252 */           parser.reportError(3, err);
/*     */         }
/* 254 */         expandedNames.append(parser.getQName(qname).toString()).append(' ');
/*     */       }
/*     */ 
/* 257 */       this._cdata = expandedNames.toString();
/* 258 */       outputProperties.setProperty("cdata-section-elements", this._cdata);
/*     */     }
/*     */ 
/* 263 */     attrib = getAttribute("indent");
/* 264 */     if (!attrib.equals("")) {
/* 265 */       if (attrib.equals("yes")) {
/* 266 */         this._indent = true;
/*     */       }
/* 268 */       outputProperties.setProperty("indent", attrib);
/*     */     }
/* 270 */     else if ((this._method != null) && (this._method.equals("html"))) {
/* 271 */       this._indent = true;
/*     */     }
/*     */ 
/* 275 */     this._indentamount = getAttribute(lookupPrefix("http://xml.apache.org/xalan"), "indent-amount");
/*     */ 
/* 278 */     if (this._indentamount.equals("")) {
/* 279 */       this._indentamount = getAttribute(lookupPrefix("http://xml.apache.org/xslt"), "indent-amount");
/*     */     }
/*     */ 
/* 282 */     if (!this._indentamount.equals("")) {
/* 283 */       outputProperties.setProperty("indent_amount", this._indentamount);
/*     */     }
/*     */ 
/* 287 */     this._mediaType = getAttribute("media-type");
/* 288 */     if (this._mediaType.equals("")) {
/* 289 */       this._mediaType = null;
/*     */     }
/*     */     else {
/* 292 */       outputProperties.setProperty("media-type", this._mediaType);
/*     */     }
/*     */ 
/* 296 */     if (this._method != null) {
/* 297 */       if (this._method.equals("html")) {
/* 298 */         if (this._version == null) {
/* 299 */           this._version = "4.0";
/*     */         }
/* 301 */         if (this._mediaType == null) {
/* 302 */           this._mediaType = "text/html";
/*     */         }
/*     */       }
/* 305 */       else if ((this._method.equals("text")) && 
/* 306 */         (this._mediaType == null)) {
/* 307 */         this._mediaType = "text/plain";
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 313 */     parser.getCurrentStylesheet().setOutputProperties(outputProperties);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 323 */     if (this._disabled) return;
/*     */ 
/* 325 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 326 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 328 */     int field = 0;
/* 329 */     il.append(classGen.loadTranslet());
/*     */ 
/* 332 */     if ((this._version != null) && (!this._version.equals("1.0"))) {
/* 333 */       field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_version", "Ljava/lang/String;");
/* 334 */       il.append(DUP);
/* 335 */       il.append(new PUSH(cpg, this._version));
/* 336 */       il.append(new PUTFIELD(field));
/*     */     }
/*     */ 
/* 340 */     if (this._method != null) {
/* 341 */       field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_method", "Ljava/lang/String;");
/* 342 */       il.append(DUP);
/* 343 */       il.append(new PUSH(cpg, this._method));
/* 344 */       il.append(new PUTFIELD(field));
/*     */     }
/*     */ 
/* 348 */     if (this._encoding != null) {
/* 349 */       field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_encoding", "Ljava/lang/String;");
/* 350 */       il.append(DUP);
/* 351 */       il.append(new PUSH(cpg, this._encoding));
/* 352 */       il.append(new PUTFIELD(field));
/*     */     }
/*     */ 
/* 356 */     if (this._omitHeader) {
/* 357 */       field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_omitHeader", "Z");
/* 358 */       il.append(DUP);
/* 359 */       il.append(new PUSH(cpg, this._omitHeader));
/* 360 */       il.append(new PUTFIELD(field));
/*     */     }
/*     */ 
/* 364 */     if (this._standalone != null) {
/* 365 */       field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_standalone", "Ljava/lang/String;");
/* 366 */       il.append(DUP);
/* 367 */       il.append(new PUSH(cpg, this._standalone));
/* 368 */       il.append(new PUTFIELD(field));
/*     */     }
/*     */ 
/* 372 */     field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_doctypeSystem", "Ljava/lang/String;");
/* 373 */     il.append(DUP);
/* 374 */     il.append(new PUSH(cpg, this._doctypeSystem));
/* 375 */     il.append(new PUTFIELD(field));
/* 376 */     field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_doctypePublic", "Ljava/lang/String;");
/* 377 */     il.append(DUP);
/* 378 */     il.append(new PUSH(cpg, this._doctypePublic));
/* 379 */     il.append(new PUTFIELD(field));
/*     */ 
/* 382 */     if (this._mediaType != null) {
/* 383 */       field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_mediaType", "Ljava/lang/String;");
/* 384 */       il.append(DUP);
/* 385 */       il.append(new PUSH(cpg, this._mediaType));
/* 386 */       il.append(new PUTFIELD(field));
/*     */     }
/*     */ 
/* 390 */     if (this._indent) {
/* 391 */       field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_indent", "Z");
/* 392 */       il.append(DUP);
/* 393 */       il.append(new PUSH(cpg, this._indent));
/* 394 */       il.append(new PUTFIELD(field));
/*     */     }
/*     */ 
/* 398 */     if ((this._indentamount != null) && (!this._indentamount.equals(""))) {
/* 399 */       field = cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "_indentamount", "I");
/* 400 */       il.append(DUP);
/* 401 */       il.append(new PUSH(cpg, Integer.parseInt(this._indentamount)));
/* 402 */       il.append(new PUTFIELD(field));
/*     */     }
/*     */ 
/* 406 */     if (this._cdata != null) {
/* 407 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "addCdataElement", "(Ljava/lang/String;)V");
/*     */ 
/* 411 */       StringTokenizer tokens = new StringTokenizer(this._cdata);
/* 412 */       while (tokens.hasMoreTokens()) {
/* 413 */         il.append(DUP);
/* 414 */         il.append(new PUSH(cpg, tokens.nextToken()));
/* 415 */         il.append(new INVOKEVIRTUAL(index));
/*     */       }
/*     */     }
/* 418 */     il.append(POP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Output
 * JD-Core Version:    0.6.2
 */