/*     */ package com.sun.org.apache.xalan.internal.xsltc.cmdline;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*     */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public final class Transform
/*     */ {
/*     */   private SerializationHandler _handler;
/*     */   private String _fileName;
/*     */   private String _className;
/*     */   private String _jarFileSrc;
/*  67 */   private boolean _isJarFileSpecified = false;
/*  68 */   private Vector _params = null;
/*     */   private boolean _uri;
/*     */   private boolean _debug;
/*     */   private int _iterations;
/*     */ 
/*     */   public Transform(String className, String fileName, boolean uri, boolean debug, int iterations)
/*     */   {
/*  74 */     this._fileName = fileName;
/*  75 */     this._className = className;
/*  76 */     this._uri = uri;
/*  77 */     this._debug = debug;
/*  78 */     this._iterations = iterations;
/*     */   }
/*     */   public String getFileName() {
/*  81 */     return this._fileName; } 
/*  82 */   public String getClassName() { return this._className; }
/*     */ 
/*     */   public void setParameters(Vector params) {
/*  85 */     this._params = params;
/*     */   }
/*     */ 
/*     */   private void setJarFileInputSrc(boolean flag, String jarFile)
/*     */   {
/*  94 */     this._isJarFileSpecified = flag;
/*     */ 
/*  96 */     this._jarFileSrc = jarFile;
/*     */   }
/*     */ 
/*     */   private void doTransform() {
/*     */     try {
/* 101 */       Class clazz = ObjectFactory.findProviderClass(this._className, true);
/* 102 */       AbstractTranslet translet = (AbstractTranslet)clazz.newInstance();
/* 103 */       translet.postInitialization();
/*     */ 
/* 106 */       SAXParserFactory factory = SAXParserFactory.newInstance();
/*     */       try {
/* 108 */         factory.setFeature("http://xml.org/sax/features/namespaces", true);
/*     */       }
/*     */       catch (Exception e) {
/* 111 */         factory.setNamespaceAware(true);
/*     */       }
/* 113 */       SAXParser parser = factory.newSAXParser();
/* 114 */       XMLReader reader = parser.getXMLReader();
/*     */ 
/* 117 */       XSLTCDTMManager dtmManager = (XSLTCDTMManager)XSLTCDTMManager.getDTMManagerClass().newInstance();
/*     */       DTMWSFilter wsfilter;
/*     */       DTMWSFilter wsfilter;
/* 122 */       if ((translet != null) && ((translet instanceof StripFilter)))
/* 123 */         wsfilter = new DOMWSFilter(translet);
/*     */       else {
/* 125 */         wsfilter = null;
/*     */       }
/*     */ 
/* 128 */       DOMEnhancedForDTM dom = (DOMEnhancedForDTM)dtmManager.getDTM(new SAXSource(reader, new InputSource(this._fileName)), false, wsfilter, true, false, translet.hasIdCall());
/*     */ 
/* 133 */       dom.setDocumentURI(this._fileName);
/* 134 */       translet.prepassDocument(dom);
/*     */ 
/* 137 */       int n = this._params.size();
/* 138 */       for (int i = 0; i < n; i++) {
/* 139 */         Parameter param = (Parameter)this._params.elementAt(i);
/* 140 */         translet.addParameter(param._name, param._value);
/*     */       }
/*     */ 
/* 144 */       TransletOutputHandlerFactory tohFactory = TransletOutputHandlerFactory.newInstance();
/*     */ 
/* 146 */       tohFactory.setOutputType(0);
/* 147 */       tohFactory.setEncoding(translet._encoding);
/* 148 */       tohFactory.setOutputMethod(translet._method);
/*     */ 
/* 150 */       if (this._iterations == -1) {
/* 151 */         translet.transform(dom, tohFactory.getSerializationHandler());
/*     */       }
/* 153 */       else if (this._iterations > 0) {
/* 154 */         long mm = System.currentTimeMillis();
/* 155 */         for (int i = 0; i < this._iterations; i++) {
/* 156 */           translet.transform(dom, tohFactory.getSerializationHandler());
/*     */         }
/*     */ 
/* 159 */         mm = System.currentTimeMillis() - mm;
/*     */ 
/* 161 */         System.err.println("\n<!--");
/* 162 */         System.err.println("  transform  = " + mm / this._iterations + " ms");
/*     */ 
/* 165 */         System.err.println("  throughput = " + 1000.0D / (mm / this._iterations) + " tps");
/*     */ 
/* 169 */         System.err.println("-->");
/*     */       }
/*     */     }
/*     */     catch (TransletException e) {
/* 173 */       if (this._debug) e.printStackTrace();
/* 174 */       System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + e.getMessage());
/*     */     }
/*     */     catch (RuntimeException e)
/*     */     {
/* 178 */       if (this._debug) e.printStackTrace();
/* 179 */       System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + e.getMessage());
/*     */     }
/*     */     catch (FileNotFoundException e)
/*     */     {
/* 183 */       if (this._debug) e.printStackTrace();
/* 184 */       ErrorMsg err = new ErrorMsg("FILE_NOT_FOUND_ERR", this._fileName);
/* 185 */       System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + err.toString());
/*     */     }
/*     */     catch (MalformedURLException e)
/*     */     {
/* 189 */       if (this._debug) e.printStackTrace();
/* 190 */       ErrorMsg err = new ErrorMsg("INVALID_URI_ERR", this._fileName);
/* 191 */       System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + err.toString());
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/* 195 */       if (this._debug) e.printStackTrace();
/* 196 */       ErrorMsg err = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
/* 197 */       System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + err.toString());
/*     */     }
/*     */     catch (UnknownHostException e)
/*     */     {
/* 201 */       if (this._debug) e.printStackTrace();
/* 202 */       ErrorMsg err = new ErrorMsg("INVALID_URI_ERR", this._fileName);
/* 203 */       System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + err.toString());
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/* 207 */       Exception ex = e.getException();
/* 208 */       if (this._debug) {
/* 209 */         if (ex != null) ex.printStackTrace();
/* 210 */         e.printStackTrace();
/*     */       }
/* 212 */       System.err.print(new ErrorMsg("RUNTIME_ERROR_KEY"));
/* 213 */       if (ex != null)
/* 214 */         System.err.println(ex.getMessage());
/*     */       else
/* 216 */         System.err.println(e.getMessage());
/*     */     }
/*     */     catch (Exception e) {
/* 219 */       if (this._debug) e.printStackTrace();
/* 220 */       System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void printUsage()
/*     */   {
/* 226 */     System.err.println(new ErrorMsg("TRANSFORM_USAGE_STR"));
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/*     */     try {
/* 231 */       if (args.length > 0)
/*     */       {
/* 233 */         int iterations = -1;
/* 234 */         boolean uri = false; boolean debug = false;
/* 235 */         boolean isJarFileSpecified = false;
/* 236 */         String jarFile = null;
/*     */ 
/* 239 */         for (int i = 0; (i < args.length) && (args[i].charAt(0) == '-'); i++) {
/* 240 */           if (args[i].equals("-u")) {
/* 241 */             uri = true;
/*     */           }
/* 243 */           else if (args[i].equals("-x")) {
/* 244 */             debug = true;
/*     */           }
/* 246 */           else if (args[i].equals("-j")) {
/* 247 */             isJarFileSpecified = true;
/* 248 */             jarFile = args[(++i)];
/*     */           }
/* 250 */           else if (args[i].equals("-n")) {
/*     */             try {
/* 252 */               iterations = Integer.parseInt(args[(++i)]);
/*     */             }
/*     */             catch (NumberFormatException e)
/*     */             {
/*     */             }
/*     */           }
/*     */           else {
/* 259 */             printUsage();
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 264 */         if (args.length - i < 2) printUsage();
/*     */ 
/* 267 */         Transform handler = new Transform(args[(i + 1)], args[i], uri, debug, iterations);
/*     */ 
/* 269 */         handler.setJarFileInputSrc(isJarFileSpecified, jarFile);
/*     */ 
/* 272 */         Vector params = new Vector();
/* 273 */         for (i += 2; i < args.length; i++) {
/* 274 */           int equal = args[i].indexOf('=');
/* 275 */           if (equal > 0) {
/* 276 */             String name = args[i].substring(0, equal);
/* 277 */             String value = args[i].substring(equal + 1);
/* 278 */             params.addElement(new Parameter(name, value));
/*     */           }
/*     */           else {
/* 281 */             printUsage();
/*     */           }
/*     */         }
/*     */ 
/* 285 */         if (i == args.length) {
/* 286 */           handler.setParameters(params);
/* 287 */           handler.doTransform();
/*     */         }
/*     */       } else {
/* 290 */         printUsage();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 294 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.cmdline.Transform
 * JD-Core Version:    0.6.2
 */