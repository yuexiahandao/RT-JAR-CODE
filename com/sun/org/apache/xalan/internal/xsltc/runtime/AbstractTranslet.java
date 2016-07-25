/*     */ package com.sun.org.apache.xalan.internal.xsltc.runtime;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.FactoryImpl;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMCache;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.Translet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.DOMAdapter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.KeyIndex;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Templates;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ 
/*     */ public abstract class AbstractTranslet
/*     */   implements Translet
/*     */ {
/*  66 */   public String _version = "1.0";
/*  67 */   public String _method = null;
/*  68 */   public String _encoding = "UTF-8";
/*  69 */   public boolean _omitHeader = false;
/*  70 */   public String _standalone = null;
/*     */ 
/*  72 */   public boolean _isStandalone = false;
/*  73 */   public String _doctypePublic = null;
/*  74 */   public String _doctypeSystem = null;
/*  75 */   public boolean _indent = false;
/*  76 */   public String _mediaType = null;
/*  77 */   public Vector _cdata = null;
/*  78 */   public int _indentamount = -1;
/*     */   public static final int FIRST_TRANSLET_VERSION = 100;
/*     */   public static final int VER_SPLIT_NAMES_ARRAY = 101;
/*     */   public static final int CURRENT_TRANSLET_VERSION = 101;
/*  89 */   protected int transletVersion = 100;
/*     */   protected String[] namesArray;
/*     */   protected String[] urisArray;
/*     */   protected int[] typesArray;
/*     */   protected String[] namespaceArray;
/*  98 */   protected Templates _templates = null;
/*     */ 
/* 101 */   protected boolean _hasIdCall = false;
/*     */ 
/* 104 */   protected StringValueHandler stringValueHandler = new StringValueHandler();
/*     */   private static final String EMPTYSTRING = "";
/*     */   private static final String ID_INDEX_NAME = "##id";
/*     */   private boolean _useServicesMechanism;
/* 117 */   private String _accessExternalStylesheet = "all";
/*     */ 
/* 151 */   protected int pbase = 0; protected int pframe = 0;
/* 152 */   protected ArrayList paramsStack = new ArrayList();
/*     */ 
/* 248 */   private MessageHandler _msgHandler = null;
/*     */ 
/* 274 */   public Hashtable _formatSymbols = null;
/*     */ 
/* 428 */   private Hashtable _keyIndexes = null;
/* 429 */   private KeyIndex _emptyKeyIndex = null;
/* 430 */   private int _indexSize = 0;
/* 431 */   private int _currentRootForKeys = 0;
/*     */ 
/* 531 */   private DOMCache _domCache = null;
/*     */ 
/* 709 */   private Hashtable _auxClasses = null;
/*     */ 
/* 784 */   protected DOMImplementation _domImplementation = null;
/*     */ 
/*     */   public void printInternalState()
/*     */   {
/* 123 */     System.out.println("-------------------------------------");
/* 124 */     System.out.println("AbstractTranslet this = " + this);
/* 125 */     System.out.println("pbase = " + this.pbase);
/* 126 */     System.out.println("vframe = " + this.pframe);
/* 127 */     System.out.println("paramsStack.size() = " + this.paramsStack.size());
/* 128 */     System.out.println("namesArray.size = " + this.namesArray.length);
/* 129 */     System.out.println("namespaceArray.size = " + this.namespaceArray.length);
/* 130 */     System.out.println("");
/* 131 */     System.out.println("Total memory = " + Runtime.getRuntime().totalMemory());
/*     */   }
/*     */ 
/*     */   public final DOMAdapter makeDOMAdapter(DOM dom)
/*     */     throws TransletException
/*     */   {
/* 141 */     setRootForKeys(dom.getDocument());
/* 142 */     return new DOMAdapter(dom, this.namesArray, this.urisArray, this.typesArray, this.namespaceArray);
/*     */   }
/*     */ 
/*     */   public final void pushParamFrame()
/*     */   {
/* 158 */     this.paramsStack.add(this.pframe, new Integer(this.pbase));
/* 159 */     this.pbase = (++this.pframe);
/*     */   }
/*     */ 
/*     */   public final void popParamFrame()
/*     */   {
/* 166 */     if (this.pbase > 0) {
/* 167 */       int oldpbase = ((Integer)this.paramsStack.get(--this.pbase)).intValue();
/* 168 */       for (int i = this.pframe - 1; i >= this.pbase; i--) {
/* 169 */         this.paramsStack.remove(i);
/*     */       }
/* 171 */       this.pframe = this.pbase; this.pbase = oldpbase;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Object addParameter(String name, Object value)
/*     */   {
/* 184 */     name = BasisLibrary.mapQNameToJavaName(name);
/* 185 */     return addParameter(name, value, false);
/*     */   }
/*     */ 
/*     */   public final Object addParameter(String name, Object value, boolean isDefault)
/*     */   {
/* 198 */     for (int i = this.pframe - 1; i >= this.pbase; i--) {
/* 199 */       Parameter param = (Parameter)this.paramsStack.get(i);
/*     */ 
/* 201 */       if (param._name.equals(name))
/*     */       {
/* 204 */         if ((param._isDefault) || (!isDefault)) {
/* 205 */           param._value = value;
/* 206 */           param._isDefault = isDefault;
/* 207 */           return value;
/*     */         }
/* 209 */         return param._value;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 214 */     this.paramsStack.add(this.pframe++, new Parameter(name, value, isDefault));
/* 215 */     return value;
/*     */   }
/*     */ 
/*     */   public void clearParameters()
/*     */   {
/* 222 */     this.pbase = (this.pframe = 0);
/* 223 */     this.paramsStack.clear();
/*     */   }
/*     */ 
/*     */   public final Object getParameter(String name)
/*     */   {
/* 232 */     name = BasisLibrary.mapQNameToJavaName(name);
/*     */ 
/* 234 */     for (int i = this.pframe - 1; i >= this.pbase; i--) {
/* 235 */       Parameter param = (Parameter)this.paramsStack.get(i);
/* 236 */       if (param._name.equals(name)) return param._value;
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */   public final void setMessageHandler(MessageHandler handler)
/*     */   {
/* 254 */     this._msgHandler = handler;
/*     */   }
/*     */ 
/*     */   public final void displayMessage(String msg)
/*     */   {
/* 261 */     if (this._msgHandler == null) {
/* 262 */       System.err.println(msg);
/*     */     }
/*     */     else
/* 265 */       this._msgHandler.displayMessage(msg);
/*     */   }
/*     */ 
/*     */   public void addDecimalFormat(String name, DecimalFormatSymbols symbols)
/*     */   {
/* 282 */     if (this._formatSymbols == null) this._formatSymbols = new Hashtable();
/*     */ 
/* 285 */     if (name == null) name = "";
/*     */ 
/* 288 */     DecimalFormat df = new DecimalFormat();
/* 289 */     if (symbols != null) {
/* 290 */       df.setDecimalFormatSymbols(symbols);
/*     */     }
/* 292 */     this._formatSymbols.put(name, df);
/*     */   }
/*     */ 
/*     */   public final DecimalFormat getDecimalFormat(String name)
/*     */   {
/* 300 */     if (this._formatSymbols != null)
/*     */     {
/* 302 */       if (name == null) name = "";
/*     */ 
/* 304 */       DecimalFormat df = (DecimalFormat)this._formatSymbols.get(name);
/* 305 */       if (df == null) df = (DecimalFormat)this._formatSymbols.get("");
/* 306 */       return df;
/*     */     }
/* 308 */     return null;
/*     */   }
/*     */ 
/*     */   public final void prepassDocument(DOM document)
/*     */   {
/* 318 */     setIndexSize(document.getSize());
/* 319 */     buildIDIndex(document);
/*     */   }
/*     */ 
/*     */   private final void buildIDIndex(DOM document)
/*     */   {
/* 328 */     setRootForKeys(document.getDocument());
/*     */ 
/* 330 */     if ((document instanceof DOMEnhancedForDTM)) {
/* 331 */       DOMEnhancedForDTM enhancedDOM = (DOMEnhancedForDTM)document;
/*     */ 
/* 336 */       if (enhancedDOM.hasDOMSource()) {
/* 337 */         buildKeyIndex("##id", document);
/* 338 */         return;
/*     */       }
/*     */ 
/* 341 */       Hashtable elementsByID = enhancedDOM.getElementsWithIDs();
/*     */ 
/* 343 */       if (elementsByID == null) {
/* 344 */         return;
/*     */       }
/*     */ 
/* 350 */       Enumeration idValues = elementsByID.keys();
/* 351 */       boolean hasIDValues = false;
/*     */ 
/* 353 */       while (idValues.hasMoreElements()) {
/* 354 */         Object idValue = idValues.nextElement();
/* 355 */         int element = document.getNodeHandle(((Integer)elementsByID.get(idValue)).intValue());
/*     */ 
/* 360 */         buildKeyIndex("##id", element, idValue);
/* 361 */         hasIDValues = true;
/*     */       }
/*     */ 
/* 364 */       if (hasIDValues)
/* 365 */         setKeyIndexDom("##id", document);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void postInitialization()
/*     */   {
/* 378 */     if (this.transletVersion < 101) {
/* 379 */       int arraySize = this.namesArray.length;
/* 380 */       String[] newURIsArray = new String[arraySize];
/* 381 */       String[] newNamesArray = new String[arraySize];
/* 382 */       int[] newTypesArray = new int[arraySize];
/*     */ 
/* 384 */       for (int i = 0; i < arraySize; i++) {
/* 385 */         String name = this.namesArray[i];
/* 386 */         int colonIndex = name.lastIndexOf(':');
/* 387 */         int lNameStartIdx = colonIndex + 1;
/*     */ 
/* 389 */         if (colonIndex > -1) {
/* 390 */           newURIsArray[i] = name.substring(0, colonIndex);
/*     */         }
/*     */ 
/* 395 */         if (name.charAt(lNameStartIdx) == '@') {
/* 396 */           lNameStartIdx++;
/* 397 */           newTypesArray[i] = 2;
/* 398 */         } else if (name.charAt(lNameStartIdx) == '?') {
/* 399 */           lNameStartIdx++;
/* 400 */           newTypesArray[i] = 13;
/*     */         } else {
/* 402 */           newTypesArray[i] = 1;
/*     */         }
/* 404 */         newNamesArray[i] = (lNameStartIdx == 0 ? name : name.substring(lNameStartIdx));
/*     */       }
/*     */ 
/* 409 */       this.namesArray = newNamesArray;
/* 410 */       this.urisArray = newURIsArray;
/* 411 */       this.typesArray = newTypesArray;
/*     */     }
/*     */ 
/* 417 */     if (this.transletVersion > 101)
/* 418 */       BasisLibrary.runTimeError("UNKNOWN_TRANSLET_VERSION_ERR", getClass().getName());
/*     */   }
/*     */ 
/*     */   public void setIndexSize(int size)
/*     */   {
/* 438 */     if (size > this._indexSize) this._indexSize = size;
/*     */   }
/*     */ 
/*     */   public KeyIndex createKeyIndex()
/*     */   {
/* 445 */     return new KeyIndex(this._indexSize);
/*     */   }
/*     */ 
/*     */   public void buildKeyIndex(String name, int node, Object value)
/*     */   {
/* 455 */     if (this._keyIndexes == null) this._keyIndexes = new Hashtable();
/*     */ 
/* 457 */     KeyIndex index = (KeyIndex)this._keyIndexes.get(name);
/* 458 */     if (index == null) {
/* 459 */       this._keyIndexes.put(name, index = new KeyIndex(this._indexSize));
/*     */     }
/* 461 */     index.add(value, node, this._currentRootForKeys);
/*     */   }
/*     */ 
/*     */   public void buildKeyIndex(String name, DOM dom)
/*     */   {
/* 470 */     if (this._keyIndexes == null) this._keyIndexes = new Hashtable();
/*     */ 
/* 472 */     KeyIndex index = (KeyIndex)this._keyIndexes.get(name);
/* 473 */     if (index == null) {
/* 474 */       this._keyIndexes.put(name, index = new KeyIndex(this._indexSize));
/*     */     }
/* 476 */     index.setDom(dom, dom.getDocument());
/*     */   }
/*     */ 
/*     */   public KeyIndex getKeyIndex(String name)
/*     */   {
/* 485 */     if (this._keyIndexes == null) {
/* 486 */       return this._emptyKeyIndex = new KeyIndex(1);
/*     */     }
/*     */ 
/* 492 */     KeyIndex index = (KeyIndex)this._keyIndexes.get(name);
/*     */ 
/* 495 */     if (index == null) {
/* 496 */       return this._emptyKeyIndex = new KeyIndex(1);
/*     */     }
/*     */ 
/* 501 */     return index;
/*     */   }
/*     */ 
/*     */   private void setRootForKeys(int root) {
/* 505 */     this._currentRootForKeys = root;
/*     */   }
/*     */ 
/*     */   public void buildKeys(DOM document, DTMAxisIterator iterator, SerializationHandler handler, int root)
/*     */     throws TransletException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setKeyIndexDom(String name, DOM document)
/*     */   {
/* 523 */     getKeyIndex(name).setDom(document, document.getDocument());
/*     */   }
/*     */ 
/*     */   public void setDOMCache(DOMCache cache)
/*     */   {
/* 538 */     this._domCache = cache;
/*     */   }
/*     */ 
/*     */   public DOMCache getDOMCache()
/*     */   {
/* 546 */     return this._domCache;
/*     */   }
/*     */ 
/*     */   public SerializationHandler openOutputHandler(String filename, boolean append)
/*     */     throws TransletException
/*     */   {
/*     */     try
/*     */     {
/* 558 */       TransletOutputHandlerFactory factory = TransletOutputHandlerFactory.newInstance();
/*     */ 
/* 561 */       String dirStr = new File(filename).getParent();
/* 562 */       if ((null != dirStr) && (dirStr.length() > 0)) {
/* 563 */         File dir = new File(dirStr);
/* 564 */         dir.mkdirs();
/*     */       }
/*     */ 
/* 567 */       factory.setEncoding(this._encoding);
/* 568 */       factory.setOutputMethod(this._method);
/* 569 */       factory.setOutputStream(new BufferedOutputStream(new FileOutputStream(filename, append)));
/* 570 */       factory.setOutputType(0);
/*     */ 
/* 572 */       SerializationHandler handler = factory.getSerializationHandler();
/*     */ 
/* 575 */       transferOutputSettings(handler);
/* 576 */       handler.startDocument();
/* 577 */       return handler;
/*     */     }
/*     */     catch (Exception e) {
/* 580 */       throw new TransletException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SerializationHandler openOutputHandler(String filename)
/*     */     throws TransletException
/*     */   {
/* 587 */     return openOutputHandler(filename, false);
/*     */   }
/*     */ 
/*     */   public void closeOutputHandler(SerializationHandler handler) {
/*     */     try {
/* 592 */       handler.endDocument();
/* 593 */       handler.close();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void transform(DOM paramDOM, DTMAxisIterator paramDTMAxisIterator, SerializationHandler paramSerializationHandler)
/*     */     throws TransletException;
/*     */ 
/*     */   public final void transform(DOM document, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/*     */     try
/*     */     {
/* 617 */       transform(document, document.getIterator(), handler);
/*     */     } finally {
/* 619 */       this._keyIndexes = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void characters(String string, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/* 630 */     if (string != null)
/*     */       try
/*     */       {
/* 633 */         handler.characters(string);
/*     */       } catch (Exception e) {
/* 635 */         throw new TransletException(e);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void addCdataElement(String name)
/*     */   {
/* 644 */     if (this._cdata == null) {
/* 645 */       this._cdata = new Vector();
/*     */     }
/*     */ 
/* 648 */     int lastColon = name.lastIndexOf(':');
/*     */ 
/* 650 */     if (lastColon > 0) {
/* 651 */       String uri = name.substring(0, lastColon);
/* 652 */       String localName = name.substring(lastColon + 1);
/* 653 */       this._cdata.addElement(uri);
/* 654 */       this._cdata.addElement(localName);
/*     */     } else {
/* 656 */       this._cdata.addElement(null);
/* 657 */       this._cdata.addElement(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void transferOutputSettings(SerializationHandler handler)
/*     */   {
/* 665 */     if (this._method != null) {
/* 666 */       if (this._method.equals("xml")) {
/* 667 */         if (this._standalone != null) {
/* 668 */           handler.setStandalone(this._standalone);
/*     */         }
/* 670 */         if (this._omitHeader) {
/* 671 */           handler.setOmitXMLDeclaration(true);
/*     */         }
/* 673 */         handler.setCdataSectionElements(this._cdata);
/* 674 */         if (this._version != null) {
/* 675 */           handler.setVersion(this._version);
/*     */         }
/* 677 */         handler.setIndent(this._indent);
/* 678 */         handler.setIndentAmount(this._indentamount);
/* 679 */         if (this._doctypeSystem != null) {
/* 680 */           handler.setDoctype(this._doctypeSystem, this._doctypePublic);
/*     */         }
/* 682 */         handler.setIsStandalone(this._isStandalone);
/*     */       }
/* 684 */       else if (this._method.equals("html")) {
/* 685 */         handler.setIndent(this._indent);
/* 686 */         handler.setDoctype(this._doctypeSystem, this._doctypePublic);
/* 687 */         if (this._mediaType != null)
/* 688 */           handler.setMediaType(this._mediaType);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 693 */       handler.setCdataSectionElements(this._cdata);
/* 694 */       if (this._version != null) {
/* 695 */         handler.setVersion(this._version);
/*     */       }
/* 697 */       if (this._standalone != null) {
/* 698 */         handler.setStandalone(this._standalone);
/*     */       }
/* 700 */       if (this._omitHeader) {
/* 701 */         handler.setOmitXMLDeclaration(true);
/*     */       }
/* 703 */       handler.setIndent(this._indent);
/* 704 */       handler.setDoctype(this._doctypeSystem, this._doctypePublic);
/* 705 */       handler.setIsStandalone(this._isStandalone);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addAuxiliaryClass(Class auxClass)
/*     */   {
/* 712 */     if (this._auxClasses == null) this._auxClasses = new Hashtable();
/* 713 */     this._auxClasses.put(auxClass.getName(), auxClass);
/*     */   }
/*     */ 
/*     */   public void setAuxiliaryClasses(Hashtable auxClasses) {
/* 717 */     this._auxClasses = auxClasses;
/*     */   }
/*     */ 
/*     */   public Class getAuxiliaryClass(String className) {
/* 721 */     if (this._auxClasses == null) return null;
/* 722 */     return (Class)this._auxClasses.get(className);
/*     */   }
/*     */ 
/*     */   public String[] getNamesArray()
/*     */   {
/* 727 */     return this.namesArray;
/*     */   }
/*     */ 
/*     */   public String[] getUrisArray() {
/* 731 */     return this.urisArray;
/*     */   }
/*     */ 
/*     */   public int[] getTypesArray() {
/* 735 */     return this.typesArray;
/*     */   }
/*     */ 
/*     */   public String[] getNamespaceArray() {
/* 739 */     return this.namespaceArray;
/*     */   }
/*     */ 
/*     */   public boolean hasIdCall() {
/* 743 */     return this._hasIdCall;
/*     */   }
/*     */ 
/*     */   public Templates getTemplates() {
/* 747 */     return this._templates;
/*     */   }
/*     */ 
/*     */   public void setTemplates(Templates templates) {
/* 751 */     this._templates = templates;
/*     */   }
/*     */ 
/*     */   public boolean useServicesMechnism()
/*     */   {
/* 757 */     return this._useServicesMechanism;
/*     */   }
/*     */ 
/*     */   public void setServicesMechnism(boolean flag)
/*     */   {
/* 764 */     this._useServicesMechanism = flag;
/*     */   }
/*     */ 
/*     */   public String getAllowedProtocols()
/*     */   {
/* 771 */     return this._accessExternalStylesheet;
/*     */   }
/*     */ 
/*     */   public void setAllowedProtocols(String protocols)
/*     */   {
/* 778 */     this._accessExternalStylesheet = protocols;
/*     */   }
/*     */ 
/*     */   public Document newDocument(String uri, String qname)
/*     */     throws ParserConfigurationException
/*     */   {
/* 789 */     if (this._domImplementation == null) {
/* 790 */       DocumentBuilderFactory dbf = FactoryImpl.getDOMFactory(this._useServicesMechanism);
/* 791 */       this._domImplementation = dbf.newDocumentBuilder().getDOMImplementation();
/*     */     }
/* 793 */     return this._domImplementation.createDocument(uri, qname, null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet
 * JD-Core Version:    0.6.2
 */