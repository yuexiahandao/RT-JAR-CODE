/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xerces.internal.parsers.SAXParser;
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public class IncrementalSAXSource_Xerces
/*     */   implements IncrementalSAXSource
/*     */ {
/*  57 */   Method fParseSomeSetup = null;
/*  58 */   Method fParseSome = null;
/*  59 */   Object fPullParserConfig = null;
/*  60 */   Method fConfigSetInput = null;
/*  61 */   Method fConfigParse = null;
/*  62 */   Method fSetInputSource = null;
/*  63 */   Constructor fConfigInputSourceCtor = null;
/*  64 */   Method fConfigSetByteStream = null;
/*  65 */   Method fConfigSetCharStream = null;
/*  66 */   Method fConfigSetEncoding = null;
/*  67 */   Method fReset = null;
/*     */   SAXParser fIncrementalParser;
/*  73 */   private boolean fParseInProgress = false;
/*     */ 
/* 364 */   private static final Object[] noparms = new Object[0];
/* 365 */   private static final Object[] parmsfalse = { Boolean.FALSE };
/*     */ 
/*     */   public IncrementalSAXSource_Xerces()
/*     */     throws NoSuchMethodException
/*     */   {
/*     */     try
/*     */     {
/* 103 */       Class xniConfigClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration", true);
/*     */ 
/* 106 */       Class[] args1 = { xniConfigClass };
/* 107 */       Constructor ctor = SAXParser.class.getConstructor(args1);
/*     */ 
/* 112 */       Class xniStdConfigClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.parsers.StandardParserConfiguration", true);
/*     */ 
/* 115 */       this.fPullParserConfig = xniStdConfigClass.newInstance();
/* 116 */       Object[] args2 = { this.fPullParserConfig };
/* 117 */       this.fIncrementalParser = ((SAXParser)ctor.newInstance(args2));
/*     */ 
/* 122 */       Class fXniInputSourceClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource", true);
/*     */ 
/* 125 */       Class[] args3 = { fXniInputSourceClass };
/* 126 */       this.fConfigSetInput = xniStdConfigClass.getMethod("setInputSource", args3);
/*     */ 
/* 128 */       Class[] args4 = { String.class, String.class, String.class };
/* 129 */       this.fConfigInputSourceCtor = fXniInputSourceClass.getConstructor(args4);
/* 130 */       Class[] args5 = { InputStream.class };
/* 131 */       this.fConfigSetByteStream = fXniInputSourceClass.getMethod("setByteStream", args5);
/* 132 */       Class[] args6 = { Reader.class };
/* 133 */       this.fConfigSetCharStream = fXniInputSourceClass.getMethod("setCharacterStream", args6);
/* 134 */       Class[] args7 = { String.class };
/* 135 */       this.fConfigSetEncoding = fXniInputSourceClass.getMethod("setEncoding", args7);
/*     */ 
/* 137 */       Class[] argsb = { Boolean.TYPE };
/* 138 */       this.fConfigParse = xniStdConfigClass.getMethod("parse", argsb);
/* 139 */       Class[] noargs = new Class[0];
/* 140 */       this.fReset = this.fIncrementalParser.getClass().getMethod("reset", noargs);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 148 */       IncrementalSAXSource_Xerces dummy = new IncrementalSAXSource_Xerces(new SAXParser());
/* 149 */       this.fParseSomeSetup = dummy.fParseSomeSetup;
/* 150 */       this.fParseSome = dummy.fParseSome;
/* 151 */       this.fIncrementalParser = dummy.fIncrementalParser;
/*     */     }
/*     */   }
/*     */ 
/*     */   public IncrementalSAXSource_Xerces(SAXParser parser)
/*     */     throws NoSuchMethodException
/*     */   {
/* 174 */     this.fIncrementalParser = parser;
/* 175 */     Class me = parser.getClass();
/* 176 */     Class[] parms = { InputSource.class };
/* 177 */     this.fParseSomeSetup = me.getMethod("parseSomeSetup", parms);
/* 178 */     parms = new Class[0];
/* 179 */     this.fParseSome = me.getMethod("parseSome", parms);
/*     */   }
/*     */ 
/*     */   public static IncrementalSAXSource createIncrementalSAXSource()
/*     */   {
/*     */     try
/*     */     {
/* 191 */       return new IncrementalSAXSource_Xerces();
/*     */     }
/*     */     catch (NoSuchMethodException e)
/*     */     {
/* 197 */       IncrementalSAXSource_Filter iss = new IncrementalSAXSource_Filter();
/* 198 */       iss.setXMLReader(new SAXParser());
/* 199 */       return iss;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static IncrementalSAXSource createIncrementalSAXSource(SAXParser parser)
/*     */   {
/*     */     try
/*     */     {
/* 207 */       return new IncrementalSAXSource_Xerces(parser);
/*     */     }
/*     */     catch (NoSuchMethodException e)
/*     */     {
/* 213 */       IncrementalSAXSource_Filter iss = new IncrementalSAXSource_Filter();
/* 214 */       iss.setXMLReader(parser);
/* 215 */       return iss;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler)
/*     */   {
/* 228 */     this.fIncrementalParser.setContentHandler(handler);
/*     */   }
/*     */ 
/*     */   public void setLexicalHandler(LexicalHandler handler)
/*     */   {
/*     */     try
/*     */     {
/* 239 */       this.fIncrementalParser.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
/*     */     }
/*     */     catch (SAXNotRecognizedException e)
/*     */     {
/*     */     }
/*     */     catch (SAXNotSupportedException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(DTDHandler handler)
/*     */   {
/* 257 */     this.fIncrementalParser.setDTDHandler(handler);
/*     */   }
/*     */ 
/*     */   public void startParse(InputSource source)
/*     */     throws SAXException
/*     */   {
/* 269 */     if (this.fIncrementalParser == null)
/* 270 */       throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_NEEDS_SAXPARSER", null));
/* 271 */     if (this.fParseInProgress) {
/* 272 */       throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_WHILE_PARSING", null));
/*     */     }
/* 274 */     boolean ok = false;
/*     */     try
/*     */     {
/* 278 */       ok = parseSomeSetup(source);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 282 */       throw new SAXException(ex);
/*     */     }
/*     */ 
/* 285 */     if (!ok)
/* 286 */       throw new SAXException(XMLMessages.createXMLMessage("ER_COULD_NOT_INIT_PARSER", null));
/*     */   }
/*     */ 
/*     */   public Object deliverMoreNodes(boolean parsemore)
/*     */   {
/* 304 */     if (!parsemore)
/*     */     {
/* 306 */       this.fParseInProgress = false;
/* 307 */       return Boolean.FALSE;
/*     */     }
/*     */     Object arg;
/*     */     try
/*     */     {
/* 312 */       boolean keepgoing = parseSome();
/* 313 */       arg = keepgoing ? Boolean.TRUE : Boolean.FALSE;
/*     */     } catch (SAXException ex) {
/* 315 */       arg = ex;
/*     */     } catch (IOException ex) {
/* 317 */       arg = ex;
/*     */     } catch (Exception ex) {
/* 319 */       arg = new SAXException(ex);
/*     */     }
/* 321 */     return arg;
/*     */   }
/*     */ 
/*     */   private boolean parseSomeSetup(InputSource source)
/*     */     throws SAXException, IOException, IllegalAccessException, InvocationTargetException, InstantiationException
/*     */   {
/* 330 */     if (this.fConfigSetInput != null)
/*     */     {
/* 334 */       Object[] parms1 = { source.getPublicId(), source.getSystemId(), null };
/* 335 */       Object xmlsource = this.fConfigInputSourceCtor.newInstance(parms1);
/* 336 */       Object[] parmsa = { source.getByteStream() };
/* 337 */       this.fConfigSetByteStream.invoke(xmlsource, parmsa);
/* 338 */       parmsa[0] = source.getCharacterStream();
/* 339 */       this.fConfigSetCharStream.invoke(xmlsource, parmsa);
/* 340 */       parmsa[0] = source.getEncoding();
/* 341 */       this.fConfigSetEncoding.invoke(xmlsource, parmsa);
/*     */ 
/* 347 */       Object[] noparms = new Object[0];
/* 348 */       this.fReset.invoke(this.fIncrementalParser, noparms);
/*     */ 
/* 350 */       parmsa[0] = xmlsource;
/* 351 */       this.fConfigSetInput.invoke(this.fPullParserConfig, parmsa);
/*     */ 
/* 354 */       return parseSome();
/*     */     }
/*     */ 
/* 358 */     Object[] parm = { source };
/* 359 */     Object ret = this.fParseSomeSetup.invoke(this.fIncrementalParser, parm);
/* 360 */     return ((Boolean)ret).booleanValue();
/*     */   }
/*     */ 
/*     */   private boolean parseSome()
/*     */     throws SAXException, IOException, IllegalAccessException, InvocationTargetException
/*     */   {
/* 371 */     if (this.fConfigSetInput != null)
/*     */     {
/* 373 */       Object ret = (Boolean)this.fConfigParse.invoke(this.fPullParserConfig, parmsfalse);
/* 374 */       return ((Boolean)ret).booleanValue();
/*     */     }
/*     */ 
/* 378 */     Object ret = this.fParseSome.invoke(this.fIncrementalParser, noparms);
/* 379 */     return ((Boolean)ret).booleanValue();
/*     */   }
/*     */ 
/*     */   public static void _main(String[] args)
/*     */   {
/* 390 */     System.out.println("Starting...");
/*     */ 
/* 392 */     CoroutineManager co = new CoroutineManager();
/* 393 */     int appCoroutineID = co.co_joinCoroutineSet(-1);
/* 394 */     if (appCoroutineID == -1)
/*     */     {
/* 396 */       System.out.println("ERROR: Couldn't allocate coroutine number.\n");
/* 397 */       return;
/*     */     }
/* 399 */     IncrementalSAXSource parser = createIncrementalSAXSource();
/*     */ 
/* 404 */     XMLSerializer trace = new XMLSerializer(System.out, null);
/* 405 */     parser.setContentHandler(trace);
/* 406 */     parser.setLexicalHandler(trace);
/*     */ 
/* 410 */     for (int arg = 0; arg < args.length; arg++)
/*     */     {
/*     */       try
/*     */       {
/* 414 */         InputSource source = new InputSource(args[arg]);
/* 415 */         Object result = null;
/* 416 */         boolean more = true;
/* 417 */         parser.startParse(source);
/* 418 */         for (result = parser.deliverMoreNodes(more); 
/* 419 */           result == Boolean.TRUE; 
/* 420 */           result = parser.deliverMoreNodes(more))
/*     */         {
/* 422 */           System.out.println("\nSome parsing successful, trying more.\n");
/*     */ 
/* 425 */           if ((arg + 1 < args.length) && ("!".equals(args[(arg + 1)])))
/*     */           {
/* 427 */             arg++;
/* 428 */             more = false;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 433 */         if (((result instanceof Boolean)) && ((Boolean)result == Boolean.FALSE))
/*     */         {
/* 435 */           System.out.println("\nParser ended (EOF or on request).\n");
/*     */         }
/* 437 */         else if (result == null) {
/* 438 */           System.out.println("\nUNEXPECTED: Parser says shut down prematurely.\n");
/*     */         }
/* 440 */         else if ((result instanceof Exception)) {
/* 441 */           throw new WrappedRuntimeException((Exception)result);
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (SAXException e)
/*     */       {
/* 450 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Xerces
 * JD-Core Version:    0.6.2
 */