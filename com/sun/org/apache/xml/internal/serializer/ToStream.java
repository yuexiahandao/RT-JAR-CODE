/*      */ package com.sun.org.apache.xml.internal.serializer;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xml.internal.serializer.utils.Messages;
/*      */ import com.sun.org.apache.xml.internal.serializer.utils.Utils;
/*      */ import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.Properties;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import javax.xml.transform.ErrorListener;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public abstract class ToStream extends SerializerBase
/*      */ {
/*      */   private static final String COMMENT_BEGIN = "<!--";
/*      */   private static final String COMMENT_END = "-->";
/*   62 */   protected BoolStack m_disableOutputEscapingStates = new BoolStack();
/*      */ 
/*   76 */   EncodingInfo m_encodingInfo = new EncodingInfo(null, null);
/*      */   Method m_canConvertMeth;
/*   89 */   boolean m_triedToGetConverter = false;
/*      */ 
/*   96 */   Object m_charToByteConverter = null;
/*      */ 
/*  108 */   protected BoolStack m_preserves = new BoolStack();
/*      */ 
/*  118 */   protected boolean m_ispreserve = false;
/*      */ 
/*  128 */   protected boolean m_isprevtext = false;
/*      */ 
/*  134 */   protected int m_maxCharacter = Encodings.getLastPrintable();
/*      */ 
/*  143 */   protected char[] m_lineSep = SecuritySupport.getSystemProperty("line.separator").toCharArray();
/*      */ 
/*  149 */   protected boolean m_lineSepUse = true;
/*      */ 
/*  155 */   protected int m_lineSepLen = this.m_lineSep.length;
/*      */   protected CharInfo m_charInfo;
/*  164 */   boolean m_shouldFlush = true;
/*      */ 
/*  169 */   protected boolean m_spaceBeforeClose = false;
/*      */   boolean m_startNewLine;
/*  182 */   protected boolean m_inDoctype = false;
/*      */ 
/*  187 */   boolean m_isUTF8 = false;
/*      */   protected Properties m_format;
/*  195 */   protected boolean m_cdataStartCalled = false;
/*      */ 
/*  201 */   private boolean m_expandDTDEntities = true;
/*      */ 
/*  268 */   private boolean m_escaping = true;
/*      */ 
/*      */   protected void closeCDATA()
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  220 */       this.m_writer.write("]]>");
/*      */ 
/*  222 */       this.m_cdataTagOpen = false;
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  226 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void serialize(Node node)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  242 */       TreeWalker walker = new TreeWalker(this);
/*      */ 
/*  245 */       walker.traverse(node);
/*      */     }
/*      */     catch (SAXException se)
/*      */     {
/*  249 */       throw new WrappedRuntimeException(se);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final boolean isUTF16Surrogate(char c)
/*      */   {
/*  262 */     return (c & 0xFC00) == 55296;
/*      */   }
/*      */ 
/*      */   protected final void flushWriter()
/*      */     throws SAXException
/*      */   {
/*  277 */     Writer writer = this.m_writer;
/*  278 */     if (null != writer)
/*      */     {
/*      */       try
/*      */       {
/*  282 */         if ((writer instanceof WriterToUTF8Buffered))
/*      */         {
/*  284 */           if (this.m_shouldFlush)
/*  285 */             ((WriterToUTF8Buffered)writer).flush();
/*      */           else
/*  287 */             ((WriterToUTF8Buffered)writer).flushBuffer();
/*      */         }
/*  289 */         if ((writer instanceof WriterToASCI))
/*      */         {
/*  291 */           if (this.m_shouldFlush) {
/*  292 */             writer.flush();
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  299 */           writer.flush();
/*      */         }
/*      */       }
/*      */       catch (IOException ioe)
/*      */       {
/*  304 */         throw new SAXException(ioe);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public OutputStream getOutputStream()
/*      */   {
/*  318 */     if ((this.m_writer instanceof WriterToUTF8Buffered))
/*  319 */       return ((WriterToUTF8Buffered)this.m_writer).getOutputStream();
/*  320 */     if ((this.m_writer instanceof WriterToASCI)) {
/*  321 */       return ((WriterToASCI)this.m_writer).getOutputStream();
/*      */     }
/*  323 */     return null;
/*      */   }
/*      */ 
/*      */   public void elementDecl(String name, String model)
/*      */     throws SAXException
/*      */   {
/*  344 */     if (this.m_inExternalDTD)
/*  345 */       return;
/*      */     try
/*      */     {
/*  348 */       Writer writer = this.m_writer;
/*  349 */       DTDprolog();
/*      */ 
/*  351 */       writer.write("<!ELEMENT ");
/*  352 */       writer.write(name);
/*  353 */       writer.write(32);
/*  354 */       writer.write(model);
/*  355 */       writer.write(62);
/*  356 */       writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  360 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void internalEntityDecl(String name, String value)
/*      */     throws SAXException
/*      */   {
/*  382 */     if (this.m_inExternalDTD)
/*  383 */       return;
/*      */     try
/*      */     {
/*  386 */       DTDprolog();
/*  387 */       outputEntityDecl(name, value);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  391 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   void outputEntityDecl(String name, String value)
/*      */     throws IOException
/*      */   {
/*  406 */     Writer writer = this.m_writer;
/*  407 */     writer.write("<!ENTITY ");
/*  408 */     writer.write(name);
/*  409 */     writer.write(" \"");
/*  410 */     writer.write(value);
/*  411 */     writer.write("\">");
/*  412 */     writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */   }
/*      */ 
/*      */   protected final void outputLineSep()
/*      */     throws IOException
/*      */   {
/*  423 */     this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */   }
/*      */ 
/*      */   public void setOutputFormat(Properties format)
/*      */   {
/*  438 */     boolean shouldFlush = this.m_shouldFlush;
/*      */ 
/*  440 */     init(this.m_writer, format, false, false);
/*      */ 
/*  442 */     this.m_shouldFlush = shouldFlush;
/*      */   }
/*      */ 
/*      */   private synchronized void init(Writer writer, Properties format, boolean defaultProperties, boolean shouldFlush)
/*      */   {
/*  462 */     this.m_shouldFlush = shouldFlush;
/*      */ 
/*  467 */     if ((this.m_tracer != null) && (!(writer instanceof SerializerTraceWriter)))
/*      */     {
/*  469 */       this.m_writer = new SerializerTraceWriter(writer, this.m_tracer);
/*      */     }
/*  471 */     else this.m_writer = writer;
/*      */ 
/*  474 */     this.m_format = format;
/*      */ 
/*  479 */     setCdataSectionElements("cdata-section-elements", format);
/*      */ 
/*  481 */     setIndentAmount(OutputPropertyUtils.getIntProperty("{http://xml.apache.org/xalan}indent-amount", format));
/*      */ 
/*  485 */     setIndent(OutputPropertyUtils.getBooleanProperty("indent", format));
/*      */ 
/*  489 */     String sep = format.getProperty("{http://xml.apache.org/xalan}line-separator");
/*      */ 
/*  491 */     if (sep != null) {
/*  492 */       this.m_lineSep = sep.toCharArray();
/*  493 */       this.m_lineSepLen = sep.length();
/*      */     }
/*      */ 
/*  497 */     boolean shouldNotWriteXMLHeader = OutputPropertyUtils.getBooleanProperty("omit-xml-declaration", format);
/*      */ 
/*  501 */     setOmitXMLDeclaration(shouldNotWriteXMLHeader);
/*  502 */     setDoctypeSystem(format.getProperty("doctype-system"));
/*  503 */     String doctypePublic = format.getProperty("doctype-public");
/*  504 */     setDoctypePublic(doctypePublic);
/*      */ 
/*  507 */     if (format.get("standalone") != null)
/*      */     {
/*  509 */       String val = format.getProperty("standalone");
/*  510 */       if (defaultProperties)
/*  511 */         setStandaloneInternal(val);
/*      */       else {
/*  513 */         setStandalone(val);
/*      */       }
/*      */     }
/*  516 */     setMediaType(format.getProperty("media-type"));
/*      */ 
/*  518 */     if (null != doctypePublic)
/*      */     {
/*  520 */       if (doctypePublic.startsWith("-//W3C//DTD XHTML")) {
/*  521 */         this.m_spaceBeforeClose = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  527 */     String version = getVersion();
/*  528 */     if (null == version)
/*      */     {
/*  530 */       version = format.getProperty("version");
/*  531 */       setVersion(version);
/*      */     }
/*      */ 
/*  535 */     String encoding = getEncoding();
/*  536 */     if (null == encoding)
/*      */     {
/*  538 */       encoding = Encodings.getMimeEncoding(format.getProperty("encoding"));
/*      */ 
/*  541 */       setEncoding(encoding);
/*      */     }
/*      */ 
/*  544 */     this.m_isUTF8 = encoding.equals("UTF-8");
/*      */ 
/*  548 */     String entitiesFileName = (String)format.get("{http://xml.apache.org/xalan}entities");
/*      */ 
/*  551 */     if (null != entitiesFileName)
/*      */     {
/*  554 */       String method = (String)format.get("method");
/*      */ 
/*  557 */       this.m_charInfo = CharInfo.getCharInfo(entitiesFileName, method);
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void init(Writer writer, Properties format)
/*      */   {
/*  571 */     init(writer, format, false, false);
/*      */   }
/*      */ 
/*      */   protected synchronized void init(OutputStream output, Properties format, boolean defaultProperties)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*  592 */     String encoding = getEncoding();
/*  593 */     if (encoding == null)
/*      */     {
/*  596 */       encoding = Encodings.getMimeEncoding(format.getProperty("encoding"));
/*      */ 
/*  599 */       setEncoding(encoding);
/*      */     }
/*      */ 
/*  602 */     if (encoding.equalsIgnoreCase("UTF-8"))
/*      */     {
/*  604 */       this.m_isUTF8 = true;
/*      */ 
/*  621 */       init(new WriterToUTF8Buffered(output), format, defaultProperties, true);
/*      */     }
/*  629 */     else if ((encoding.equals("WINDOWS-1250")) || (encoding.equals("US-ASCII")) || (encoding.equals("ASCII")))
/*      */     {
/*  634 */       init(new WriterToASCI(output), format, defaultProperties, true);
/*      */     }
/*      */     else
/*      */     {
/*      */       Writer osw;
/*      */       try
/*      */       {
/*  642 */         osw = Encodings.getWriter(output, encoding);
/*      */       }
/*      */       catch (UnsupportedEncodingException uee)
/*      */       {
/*  646 */         System.out.println("Warning: encoding \"" + encoding + "\" not supported" + ", using " + "UTF-8");
/*      */ 
/*  653 */         encoding = "UTF-8";
/*  654 */         setEncoding(encoding);
/*  655 */         osw = Encodings.getWriter(output, encoding);
/*      */       }
/*      */ 
/*  658 */       init(osw, format, defaultProperties, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Properties getOutputFormat()
/*      */   {
/*  670 */     return this.m_format;
/*      */   }
/*      */ 
/*      */   public void setWriter(Writer writer)
/*      */   {
/*  684 */     if ((this.m_tracer != null) && (!(writer instanceof SerializerTraceWriter)))
/*      */     {
/*  686 */       this.m_writer = new SerializerTraceWriter(writer, this.m_tracer);
/*      */     }
/*  688 */     else this.m_writer = writer;
/*      */   }
/*      */ 
/*      */   public boolean setLineSepUse(boolean use_sytem_line_break)
/*      */   {
/*  705 */     boolean oldValue = this.m_lineSepUse;
/*  706 */     this.m_lineSepUse = use_sytem_line_break;
/*  707 */     return oldValue;
/*      */   }
/*      */ 
/*      */   public void setOutputStream(OutputStream output)
/*      */   {
/*      */     try
/*      */     {
/*      */       Properties format;
/*      */       Properties format;
/*  727 */       if (null == this.m_format) {
/*  728 */         format = OutputPropertiesFactory.getDefaultMethodProperties("xml");
/*      */       }
/*      */       else
/*      */       {
/*  732 */         format = this.m_format;
/*  733 */       }init(output, format, true);
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean setEscaping(boolean escape)
/*      */   {
/*  747 */     boolean temp = this.m_escaping;
/*  748 */     this.m_escaping = escape;
/*  749 */     return temp;
/*      */   }
/*      */ 
/*      */   protected void indent(int depth)
/*      */     throws IOException
/*      */   {
/*  765 */     if (this.m_startNewLine) {
/*  766 */       outputLineSep();
/*      */     }
/*      */ 
/*  771 */     if (this.m_indentAmount > 0)
/*  772 */       printSpace(depth * this.m_indentAmount);
/*      */   }
/*      */ 
/*      */   protected void indent()
/*      */     throws IOException
/*      */   {
/*  782 */     indent(this.m_elemContext.m_currentElemDepth);
/*      */   }
/*      */ 
/*      */   private void printSpace(int n)
/*      */     throws IOException
/*      */   {
/*  792 */     Writer writer = this.m_writer;
/*  793 */     for (int i = 0; i < n; i++)
/*      */     {
/*  795 */       writer.write(32);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value)
/*      */     throws SAXException
/*      */   {
/*  828 */     if (this.m_inExternalDTD)
/*  829 */       return;
/*      */     try
/*      */     {
/*  832 */       Writer writer = this.m_writer;
/*  833 */       DTDprolog();
/*      */ 
/*  835 */       writer.write("<!ATTLIST ");
/*  836 */       writer.write(eName);
/*  837 */       writer.write(32);
/*      */ 
/*  839 */       writer.write(aName);
/*  840 */       writer.write(32);
/*  841 */       writer.write(type);
/*  842 */       if (valueDefault != null)
/*      */       {
/*  844 */         writer.write(32);
/*  845 */         writer.write(valueDefault);
/*      */       }
/*      */ 
/*  850 */       writer.write(62);
/*  851 */       writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  855 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Writer getWriter()
/*      */   {
/*  866 */     return this.m_writer;
/*      */   }
/*      */ 
/*      */   public void externalEntityDecl(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  891 */       DTDprolog();
/*      */ 
/*  893 */       this.m_writer.write("<!ENTITY ");
/*  894 */       this.m_writer.write(name);
/*  895 */       if (publicId != null) {
/*  896 */         this.m_writer.write(" PUBLIC \"");
/*  897 */         this.m_writer.write(publicId);
/*      */       }
/*      */       else
/*      */       {
/*  901 */         this.m_writer.write(" SYSTEM \"");
/*  902 */         this.m_writer.write(systemId);
/*      */       }
/*  904 */       this.m_writer.write("\" >");
/*  905 */       this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */     }
/*      */     catch (IOException e) {
/*  908 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean escapingNotNeeded(char ch)
/*      */   {
/*      */     boolean ret;
/*      */     boolean ret;
/*  919 */     if (ch < '')
/*      */     {
/*      */       boolean ret;
/*  923 */       if ((ch >= ' ') || ('\n' == ch) || ('\r' == ch) || ('\t' == ch))
/*  924 */         ret = true;
/*      */       else
/*  926 */         ret = false;
/*      */     }
/*      */     else {
/*  929 */       ret = this.m_encodingInfo.isInEncoding(ch);
/*      */     }
/*  931 */     return ret;
/*      */   }
/*      */ 
/*      */   protected int writeUTF16Surrogate(char c, char[] ch, int i, int end)
/*      */     throws IOException
/*      */   {
/*  961 */     int codePoint = 0;
/*  962 */     if (i + 1 >= end)
/*      */     {
/*  964 */       throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(c) }));
/*      */     }
/*      */ 
/*  970 */     char high = c;
/*  971 */     char low = ch[(i + 1)];
/*  972 */     if (!Encodings.isLowUTF16Surrogate(low)) {
/*  973 */       throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(c) + " " + Integer.toHexString(low) }));
/*      */     }
/*      */ 
/*  982 */     Writer writer = this.m_writer;
/*      */ 
/*  985 */     if (this.m_encodingInfo.isInEncoding(c, low))
/*      */     {
/*  988 */       writer.write(ch, i, 2);
/*      */     }
/*      */     else
/*      */     {
/*  994 */       String encoding = getEncoding();
/*  995 */       if (encoding != null)
/*      */       {
/*  999 */         codePoint = Encodings.toCodePoint(high, low);
/*      */ 
/* 1001 */         writer.write(38);
/* 1002 */         writer.write(35);
/* 1003 */         writer.write(Integer.toString(codePoint));
/* 1004 */         writer.write(59);
/*      */       }
/*      */       else
/*      */       {
/* 1009 */         writer.write(ch, i, 2);
/*      */       }
/*      */     }
/*      */ 
/* 1013 */     return codePoint;
/*      */   }
/*      */ 
/*      */   protected int accumDefaultEntity(Writer writer, char ch, int i, char[] chars, int len, boolean fromTextNode, boolean escLF)
/*      */     throws IOException
/*      */   {
/* 1043 */     if ((!escLF) && ('\n' == ch))
/*      */     {
/* 1045 */       writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */     }
/* 1051 */     else if (((fromTextNode) && (this.m_charInfo.isSpecialTextChar(ch))) || ((!fromTextNode) && (this.m_charInfo.isSpecialAttrChar(ch))))
/*      */     {
/* 1053 */       String outputStringForChar = this.m_charInfo.getOutputStringForChar(ch);
/*      */ 
/* 1055 */       if (null != outputStringForChar)
/*      */       {
/* 1057 */         writer.write(outputStringForChar);
/*      */       }
/*      */       else
/* 1060 */         return i;
/*      */     }
/*      */     else {
/* 1063 */       return i;
/*      */     }
/*      */ 
/* 1066 */     return i + 1;
/*      */   }
/*      */ 
/*      */   void writeNormalizedChars(char[] ch, int start, int length, boolean isCData, boolean useSystemLineSeparator)
/*      */     throws IOException, SAXException
/*      */   {
/* 1090 */     Writer writer = this.m_writer;
/* 1091 */     int end = start + length;
/*      */ 
/* 1093 */     for (int i = start; i < end; i++)
/*      */     {
/* 1095 */       char c = ch[i];
/*      */ 
/* 1097 */       if (('\n' == c) && (useSystemLineSeparator))
/*      */       {
/* 1099 */         writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */       }
/* 1101 */       else if ((isCData) && (!escapingNotNeeded(c)))
/*      */       {
/* 1104 */         if (this.m_cdataTagOpen) {
/* 1105 */           closeCDATA();
/*      */         }
/*      */ 
/* 1108 */         if (Encodings.isHighUTF16Surrogate(c))
/*      */         {
/* 1110 */           writeUTF16Surrogate(c, ch, i, end);
/* 1111 */           i++;
/*      */         }
/*      */         else
/*      */         {
/* 1115 */           writer.write("&#");
/*      */ 
/* 1117 */           String intStr = Integer.toString(c);
/*      */ 
/* 1119 */           writer.write(intStr);
/* 1120 */           writer.write(59);
/*      */         }
/*      */ 
/*      */       }
/* 1130 */       else if ((isCData) && (i < end - 2) && (']' == c) && (']' == ch[(i + 1)]) && ('>' == ch[(i + 2)]))
/*      */       {
/* 1137 */         writer.write("]]]]><![CDATA[>");
/*      */ 
/* 1139 */         i += 2;
/*      */       }
/* 1143 */       else if (escapingNotNeeded(c))
/*      */       {
/* 1145 */         if ((isCData) && (!this.m_cdataTagOpen))
/*      */         {
/* 1147 */           writer.write("<![CDATA[");
/* 1148 */           this.m_cdataTagOpen = true;
/*      */         }
/* 1150 */         writer.write(c);
/*      */       }
/* 1154 */       else if (Encodings.isHighUTF16Surrogate(c))
/*      */       {
/* 1156 */         if (this.m_cdataTagOpen)
/* 1157 */           closeCDATA();
/* 1158 */         writeUTF16Surrogate(c, ch, i, end);
/* 1159 */         i++;
/*      */       }
/*      */       else
/*      */       {
/* 1163 */         if (this.m_cdataTagOpen)
/* 1164 */           closeCDATA();
/* 1165 */         writer.write("&#");
/*      */ 
/* 1167 */         String intStr = Integer.toString(c);
/*      */ 
/* 1169 */         writer.write(intStr);
/* 1170 */         writer.write(59);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endNonEscaping()
/*      */     throws SAXException
/*      */   {
/* 1186 */     this.m_disableOutputEscapingStates.pop();
/*      */   }
/*      */ 
/*      */   public void startNonEscaping()
/*      */     throws SAXException
/*      */   {
/* 1201 */     this.m_disableOutputEscapingStates.push(true);
/*      */   }
/*      */ 
/*      */   protected void cdata(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/* 1237 */       int old_start = start;
/* 1238 */       if (this.m_elemContext.m_startTagOpen)
/*      */       {
/* 1240 */         closeStartTag();
/* 1241 */         this.m_elemContext.m_startTagOpen = false;
/*      */       }
/* 1243 */       this.m_ispreserve = true;
/*      */ 
/* 1245 */       if (shouldIndent()) {
/* 1246 */         indent();
/*      */       }
/* 1248 */       boolean writeCDataBrackets = (length >= 1) && (escapingNotNeeded(ch[start]));
/*      */ 
/* 1255 */       if ((writeCDataBrackets) && (!this.m_cdataTagOpen))
/*      */       {
/* 1257 */         this.m_writer.write("<![CDATA[");
/* 1258 */         this.m_cdataTagOpen = true;
/*      */       }
/*      */ 
/* 1262 */       if (isEscapingDisabled())
/*      */       {
/* 1264 */         charactersRaw(ch, start, length);
/*      */       }
/*      */       else {
/* 1267 */         writeNormalizedChars(ch, start, length, true, this.m_lineSepUse);
/*      */       }
/*      */ 
/* 1273 */       if (writeCDataBrackets)
/*      */       {
/* 1280 */         if (ch[(start + length - 1)] == ']') {
/* 1281 */           closeCDATA();
/*      */         }
/*      */       }
/*      */ 
/* 1285 */       if (this.m_tracer != null)
/* 1286 */         super.fireCDATAEvent(ch, old_start, length);
/*      */     }
/*      */     catch (IOException ioe)
/*      */     {
/* 1290 */       throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), ioe);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isEscapingDisabled()
/*      */   {
/* 1306 */     return this.m_disableOutputEscapingStates.peekOrFalse();
/*      */   }
/*      */ 
/*      */   protected void charactersRaw(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 1323 */     if (this.m_inEntityRef)
/* 1324 */       return;
/*      */     try
/*      */     {
/* 1327 */       if (this.m_elemContext.m_startTagOpen)
/*      */       {
/* 1329 */         closeStartTag();
/* 1330 */         this.m_elemContext.m_startTagOpen = false;
/*      */       }
/*      */ 
/* 1333 */       this.m_ispreserve = true;
/*      */ 
/* 1335 */       this.m_writer.write(ch, start, length);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 1339 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void characters(char[] chars, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 1378 */     if ((length == 0) || ((this.m_inEntityRef) && (!this.m_expandDTDEntities)))
/* 1379 */       return;
/* 1380 */     if (this.m_elemContext.m_startTagOpen)
/*      */     {
/* 1382 */       closeStartTag();
/* 1383 */       this.m_elemContext.m_startTagOpen = false;
/*      */     }
/* 1385 */     else if (this.m_needToCallStartDocument)
/*      */     {
/* 1387 */       startDocumentInternal();
/*      */     }
/*      */ 
/* 1390 */     if ((this.m_cdataStartCalled) || (this.m_elemContext.m_isCdataSection))
/*      */     {
/* 1395 */       cdata(chars, start, length);
/*      */ 
/* 1397 */       return;
/*      */     }
/*      */ 
/* 1400 */     if (this.m_cdataTagOpen) {
/* 1401 */       closeCDATA();
/*      */     }
/*      */ 
/* 1404 */     if ((this.m_disableOutputEscapingStates.peekOrFalse()) || (!this.m_escaping))
/*      */     {
/* 1406 */       charactersRaw(chars, start, length);
/*      */ 
/* 1409 */       if (this.m_tracer != null) {
/* 1410 */         super.fireCharEvent(chars, start, length);
/*      */       }
/* 1412 */       return;
/*      */     }
/*      */ 
/* 1415 */     if (this.m_elemContext.m_startTagOpen)
/*      */     {
/* 1417 */       closeStartTag();
/* 1418 */       this.m_elemContext.m_startTagOpen = false;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1431 */       int end = start + length;
/* 1432 */       int lastDirty = start - 1;
/*      */       char ch1;
/* 1433 */       for (int i = start; 
/* 1434 */         (i < end) && (((ch1 = chars[i]) == ' ') || ((ch1 == '\n') && (this.m_lineSepUse)) || (ch1 == '\r') || (ch1 == '\t')); 
/* 1439 */         i++)
/*      */       {
/* 1446 */         if (!this.m_charInfo.isTextASCIIClean(ch1))
/*      */         {
/* 1448 */           lastDirty = processDirty(chars, end, i, ch1, lastDirty, true);
/* 1449 */           i = lastDirty;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1455 */       if (i < end) {
/* 1456 */         this.m_ispreserve = true;
/*      */       }
/*      */ 
/* 1462 */       boolean isXML10 = "1.0".equals(getVersion());
/*      */ 
/* 1464 */       for (; i < end; i++)
/*      */       {
/*      */         char ch2;
/* 1473 */         while ((i < end) && ((ch2 = chars[i]) < '') && (this.m_charInfo.isTextASCIIClean(ch2)))
/* 1474 */           i++;
/* 1475 */         if (i == end)
/*      */         {
/*      */           break;
/*      */         }
/* 1479 */         char ch = chars[i];
/*      */ 
/* 1484 */         if (((isCharacterInC0orC1Range(ch)) || ((!isXML10) && (isNELorLSEPCharacter(ch))) || (!escapingNotNeeded(ch)) || (this.m_charInfo.isSpecialTextChar(ch))) && ('"' != ch))
/*      */         {
/* 1493 */           lastDirty = processDirty(chars, end, i, ch, lastDirty, true);
/* 1494 */           i = lastDirty;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1500 */       int startClean = lastDirty + 1;
/* 1501 */       if (i > startClean)
/*      */       {
/* 1503 */         int lengthClean = i - startClean;
/* 1504 */         this.m_writer.write(chars, startClean, lengthClean);
/*      */       }
/*      */ 
/* 1508 */       this.m_isprevtext = true;
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 1512 */       throw new SAXException(e);
/*      */     }
/*      */ 
/* 1516 */     if (this.m_tracer != null)
/* 1517 */       super.fireCharEvent(chars, start, length);
/*      */   }
/*      */ 
/*      */   private static boolean isCharacterInC0orC1Range(char ch)
/*      */   {
/* 1531 */     if ((ch == '\t') || (ch == '\n') || (ch == '\r')) {
/* 1532 */       return false;
/*      */     }
/* 1534 */     return ((ch >= '') && (ch <= '')) || ((ch >= '\001') && (ch <= '\037'));
/*      */   }
/*      */ 
/*      */   private static boolean isNELorLSEPCharacter(char ch)
/*      */   {
/* 1546 */     return (ch == '') || (ch == ' ');
/*      */   }
/*      */ 
/*      */   private int processDirty(char[] chars, int end, int i, char ch, int lastDirty, boolean fromTextNode)
/*      */     throws IOException
/*      */   {
/* 1569 */     int startClean = lastDirty + 1;
/*      */ 
/* 1572 */     if (i > startClean)
/*      */     {
/* 1574 */       int lengthClean = i - startClean;
/* 1575 */       this.m_writer.write(chars, startClean, lengthClean);
/*      */     }
/*      */ 
/* 1579 */     if (('\n' == ch) && (fromTextNode))
/*      */     {
/* 1581 */       this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */     }
/*      */     else
/*      */     {
/* 1585 */       startClean = accumDefaultEscape(this.m_writer, ch, i, chars, end, fromTextNode, false);
/*      */ 
/* 1594 */       i = startClean - 1;
/*      */     }
/*      */ 
/* 1598 */     return i;
/*      */   }
/*      */ 
/*      */   public void characters(String s)
/*      */     throws SAXException
/*      */   {
/* 1610 */     if ((this.m_inEntityRef) && (!this.m_expandDTDEntities))
/* 1611 */       return;
/* 1612 */     int length = s.length();
/* 1613 */     if (length > this.m_charsBuff.length)
/*      */     {
/* 1615 */       this.m_charsBuff = new char[length * 2 + 1];
/*      */     }
/* 1617 */     s.getChars(0, length, this.m_charsBuff, 0);
/* 1618 */     characters(this.m_charsBuff, 0, length);
/*      */   }
/*      */ 
/*      */   protected int accumDefaultEscape(Writer writer, char ch, int i, char[] chars, int len, boolean fromTextNode, boolean escLF)
/*      */     throws IOException
/*      */   {
/* 1649 */     int pos = accumDefaultEntity(writer, ch, i, chars, len, fromTextNode, escLF);
/*      */ 
/* 1651 */     if (i == pos)
/*      */     {
/* 1653 */       if (Encodings.isHighUTF16Surrogate(ch))
/*      */       {
/* 1659 */         int codePoint = 0;
/*      */ 
/* 1661 */         if (i + 1 >= len)
/*      */         {
/* 1663 */           throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(ch) }));
/*      */         }
/*      */ 
/* 1673 */         char next = chars[(++i)];
/*      */ 
/* 1675 */         if (!Encodings.isLowUTF16Surrogate(next)) {
/* 1676 */           throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[] { Integer.toHexString(ch) + " " + Integer.toHexString(next) }));
/*      */         }
/*      */ 
/* 1687 */         codePoint = Encodings.toCodePoint(ch, next);
/*      */ 
/* 1690 */         writer.write("&#");
/* 1691 */         writer.write(Integer.toString(codePoint));
/* 1692 */         writer.write(59);
/* 1693 */         pos += 2;
/*      */       }
/*      */       else
/*      */       {
/* 1702 */         if ((isCharacterInC0orC1Range(ch)) || (("1.1".equals(getVersion())) && (isNELorLSEPCharacter(ch))))
/*      */         {
/* 1705 */           writer.write("&#");
/* 1706 */           writer.write(Integer.toString(ch));
/* 1707 */           writer.write(59);
/*      */         }
/* 1709 */         else if (((!escapingNotNeeded(ch)) || ((fromTextNode) && (this.m_charInfo.isSpecialTextChar(ch))) || ((!fromTextNode) && (this.m_charInfo.isSpecialAttrChar(ch)))) && (this.m_elemContext.m_currentElemDepth > 0))
/*      */         {
/* 1714 */           writer.write("&#");
/* 1715 */           writer.write(Integer.toString(ch));
/* 1716 */           writer.write(59);
/*      */         }
/*      */         else
/*      */         {
/* 1720 */           writer.write(ch);
/*      */         }
/* 1722 */         pos++;
/*      */       }
/*      */     }
/*      */ 
/* 1726 */     return pos;
/*      */   }
/*      */ 
/*      */   public void startElement(String namespaceURI, String localName, String name, Attributes atts)
/*      */     throws SAXException
/*      */   {
/* 1758 */     if (this.m_inEntityRef) {
/* 1759 */       return;
/*      */     }
/* 1761 */     if (this.m_needToCallStartDocument)
/*      */     {
/* 1763 */       startDocumentInternal();
/* 1764 */       this.m_needToCallStartDocument = false;
/*      */     }
/* 1766 */     else if (this.m_cdataTagOpen) {
/* 1767 */       closeCDATA();
/*      */     }
/*      */     try {
/* 1770 */       if ((true == this.m_needToOutputDocTypeDecl) && (null != getDoctypeSystem()))
/*      */       {
/* 1773 */         outputDocTypeDecl(name, true);
/*      */       }
/*      */ 
/* 1776 */       this.m_needToOutputDocTypeDecl = false;
/*      */ 
/* 1781 */       if (this.m_elemContext.m_startTagOpen)
/*      */       {
/* 1783 */         closeStartTag();
/* 1784 */         this.m_elemContext.m_startTagOpen = false;
/*      */       }
/*      */ 
/* 1787 */       if (namespaceURI != null) {
/* 1788 */         ensurePrefixIsDeclared(namespaceURI, name);
/*      */       }
/* 1790 */       this.m_ispreserve = false;
/*      */ 
/* 1792 */       if ((shouldIndent()) && (this.m_startNewLine))
/*      */       {
/* 1794 */         indent();
/*      */       }
/*      */ 
/* 1797 */       this.m_startNewLine = true;
/*      */ 
/* 1799 */       Writer writer = this.m_writer;
/* 1800 */       writer.write(60);
/* 1801 */       writer.write(name);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 1805 */       throw new SAXException(e);
/*      */     }
/*      */ 
/* 1809 */     if (atts != null) {
/* 1810 */       addAttributes(atts);
/*      */     }
/* 1812 */     this.m_elemContext = this.m_elemContext.push(namespaceURI, localName, name);
/* 1813 */     this.m_isprevtext = false;
/*      */ 
/* 1815 */     if (this.m_tracer != null)
/* 1816 */       firePseudoAttributes();
/*      */   }
/*      */ 
/*      */   public void startElement(String elementNamespaceURI, String elementLocalName, String elementName)
/*      */     throws SAXException
/*      */   {
/* 1848 */     startElement(elementNamespaceURI, elementLocalName, elementName, null);
/*      */   }
/*      */ 
/*      */   public void startElement(String elementName) throws SAXException
/*      */   {
/* 1853 */     startElement(null, null, elementName, null);
/*      */   }
/*      */ 
/*      */   void outputDocTypeDecl(String name, boolean closeDecl)
/*      */     throws SAXException
/*      */   {
/* 1866 */     if (this.m_cdataTagOpen)
/* 1867 */       closeCDATA();
/*      */     try
/*      */     {
/* 1870 */       Writer writer = this.m_writer;
/* 1871 */       writer.write("<!DOCTYPE ");
/* 1872 */       writer.write(name);
/*      */ 
/* 1874 */       String doctypePublic = getDoctypePublic();
/* 1875 */       if (null != doctypePublic)
/*      */       {
/* 1877 */         writer.write(" PUBLIC \"");
/* 1878 */         writer.write(doctypePublic);
/* 1879 */         writer.write(34);
/*      */       }
/*      */ 
/* 1882 */       String doctypeSystem = getDoctypeSystem();
/* 1883 */       if (null != doctypeSystem)
/*      */       {
/* 1885 */         if (null == doctypePublic)
/* 1886 */           writer.write(" SYSTEM \"");
/*      */         else {
/* 1888 */           writer.write(" \"");
/*      */         }
/* 1890 */         writer.write(doctypeSystem);
/*      */ 
/* 1892 */         if (closeDecl)
/*      */         {
/* 1894 */           writer.write("\">");
/* 1895 */           writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/* 1896 */           closeDecl = false;
/*      */         }
/*      */         else {
/* 1899 */           writer.write(34);
/*      */         }
/*      */       }
/* 1901 */       boolean dothis = false;
/* 1902 */       if (dothis)
/*      */       {
/* 1906 */         if (closeDecl)
/*      */         {
/* 1908 */           writer.write(62);
/* 1909 */           writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 1915 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void processAttributes(Writer writer, int nAttrs)
/*      */     throws IOException, SAXException
/*      */   {
/* 1938 */     String encoding = getEncoding();
/* 1939 */     for (int i = 0; i < nAttrs; i++)
/*      */     {
/* 1942 */       String name = this.m_attributes.getQName(i);
/* 1943 */       String value = this.m_attributes.getValue(i);
/* 1944 */       writer.write(32);
/* 1945 */       writer.write(name);
/* 1946 */       writer.write("=\"");
/* 1947 */       writeAttrString(writer, value, encoding);
/* 1948 */       writer.write(34);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeAttrString(Writer writer, String string, String encoding)
/*      */     throws IOException
/*      */   {
/* 1967 */     int len = string.length();
/* 1968 */     if (len > this.m_attrBuff.length)
/*      */     {
/* 1970 */       this.m_attrBuff = new char[len * 2 + 1];
/*      */     }
/* 1972 */     string.getChars(0, len, this.m_attrBuff, 0);
/* 1973 */     char[] stringChars = this.m_attrBuff;
/*      */ 
/* 1975 */     for (int i = 0; i < len; )
/*      */     {
/* 1977 */       char ch = stringChars[i];
/* 1978 */       if ((escapingNotNeeded(ch)) && (!this.m_charInfo.isSpecialAttrChar(ch)))
/*      */       {
/* 1980 */         writer.write(ch);
/* 1981 */         i++;
/*      */       }
/*      */       else
/*      */       {
/* 1993 */         i = accumDefaultEscape(writer, ch, i, stringChars, len, false, true);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(String namespaceURI, String localName, String name)
/*      */     throws SAXException
/*      */   {
/* 2019 */     if (this.m_inEntityRef) {
/* 2020 */       return;
/*      */     }
/*      */ 
/* 2024 */     this.m_prefixMap.popNamespaces(this.m_elemContext.m_currentElemDepth, null);
/*      */     try
/*      */     {
/* 2028 */       Writer writer = this.m_writer;
/* 2029 */       if (this.m_elemContext.m_startTagOpen)
/*      */       {
/* 2031 */         if (this.m_tracer != null)
/* 2032 */           super.fireStartElem(this.m_elemContext.m_elementName);
/* 2033 */         int nAttrs = this.m_attributes.getLength();
/* 2034 */         if (nAttrs > 0)
/*      */         {
/* 2036 */           processAttributes(this.m_writer, nAttrs);
/*      */ 
/* 2038 */           this.m_attributes.clear();
/*      */         }
/* 2040 */         if (this.m_spaceBeforeClose)
/* 2041 */           writer.write(" />");
/*      */         else {
/* 2043 */           writer.write("/>");
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2052 */         if (this.m_cdataTagOpen) {
/* 2053 */           closeCDATA();
/*      */         }
/* 2055 */         if (shouldIndent())
/* 2056 */           indent(this.m_elemContext.m_currentElemDepth - 1);
/* 2057 */         writer.write(60);
/* 2058 */         writer.write(47);
/* 2059 */         writer.write(name);
/* 2060 */         writer.write(62);
/*      */       }
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 2065 */       throw new SAXException(e);
/*      */     }
/*      */ 
/* 2068 */     if ((!this.m_elemContext.m_startTagOpen) && (this.m_doIndent))
/*      */     {
/* 2070 */       this.m_ispreserve = (this.m_preserves.isEmpty() ? false : this.m_preserves.pop());
/*      */     }
/*      */ 
/* 2073 */     this.m_isprevtext = false;
/*      */ 
/* 2076 */     if (this.m_tracer != null)
/* 2077 */       super.fireEndElem(name);
/* 2078 */     this.m_elemContext = this.m_elemContext.m_prev;
/*      */   }
/*      */ 
/*      */   public void endElement(String name)
/*      */     throws SAXException
/*      */   {
/* 2089 */     endElement(null, null, name);
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/* 2111 */     startPrefixMapping(prefix, uri, true);
/*      */   }
/*      */ 
/*      */   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
/*      */     throws SAXException
/*      */   {
/*      */     int pushDepth;
/*      */     int pushDepth;
/* 2150 */     if (shouldFlush)
/*      */     {
/* 2152 */       flushPending();
/*      */ 
/* 2154 */       pushDepth = this.m_elemContext.m_currentElemDepth + 1;
/*      */     }
/*      */     else
/*      */     {
/* 2159 */       pushDepth = this.m_elemContext.m_currentElemDepth;
/*      */     }
/* 2161 */     boolean pushed = this.m_prefixMap.pushNamespace(prefix, uri, pushDepth);
/*      */ 
/* 2163 */     if (pushed)
/*      */     {
/* 2171 */       if ("".equals(prefix))
/*      */       {
/* 2173 */         String name = "xmlns";
/* 2174 */         addAttributeAlways("http://www.w3.org/2000/xmlns/", name, name, "CDATA", uri, false);
/*      */       }
/* 2178 */       else if (!"".equals(uri))
/*      */       {
/* 2181 */         String name = "xmlns:" + prefix;
/*      */ 
/* 2187 */         addAttributeAlways("http://www.w3.org/2000/xmlns/", prefix, name, "CDATA", uri, false);
/*      */       }
/*      */     }
/*      */ 
/* 2191 */     return pushed;
/*      */   }
/*      */ 
/*      */   public void comment(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 2207 */     int start_old = start;
/* 2208 */     if (this.m_inEntityRef)
/* 2209 */       return;
/* 2210 */     if (this.m_elemContext.m_startTagOpen)
/*      */     {
/* 2212 */       closeStartTag();
/* 2213 */       this.m_elemContext.m_startTagOpen = false;
/*      */     }
/* 2215 */     else if (this.m_needToCallStartDocument)
/*      */     {
/* 2217 */       startDocumentInternal();
/* 2218 */       this.m_needToCallStartDocument = false;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 2223 */       if ((shouldIndent()) && (this.m_isStandalone)) {
/* 2224 */         indent();
/*      */       }
/* 2226 */       int limit = start + length;
/* 2227 */       boolean wasDash = false;
/* 2228 */       if (this.m_cdataTagOpen) {
/* 2229 */         closeCDATA();
/*      */       }
/* 2231 */       if ((shouldIndent()) && (!this.m_isStandalone)) {
/* 2232 */         indent();
/*      */       }
/* 2234 */       Writer writer = this.m_writer;
/* 2235 */       writer.write("<!--");
/*      */ 
/* 2237 */       for (int i = start; i < limit; i++)
/*      */       {
/* 2239 */         if ((wasDash) && (ch[i] == '-'))
/*      */         {
/* 2241 */           writer.write(ch, start, i - start);
/* 2242 */           writer.write(" -");
/* 2243 */           start = i + 1;
/*      */         }
/* 2245 */         wasDash = ch[i] == '-';
/*      */       }
/*      */ 
/* 2249 */       if (length > 0)
/*      */       {
/* 2252 */         int remainingChars = limit - start;
/* 2253 */         if (remainingChars > 0) {
/* 2254 */           writer.write(ch, start, remainingChars);
/*      */         }
/* 2256 */         if (ch[(limit - 1)] == '-')
/* 2257 */           writer.write(32);
/*      */       }
/* 2259 */       writer.write("-->");
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 2263 */       throw new SAXException(e);
/*      */     }
/*      */ 
/* 2275 */     this.m_startNewLine = true;
/*      */ 
/* 2277 */     if (this.m_tracer != null)
/* 2278 */       super.fireCommentEvent(ch, start_old, length);
/*      */   }
/*      */ 
/*      */   public void endCDATA()
/*      */     throws SAXException
/*      */   {
/* 2289 */     if (this.m_cdataTagOpen)
/* 2290 */       closeCDATA();
/* 2291 */     this.m_cdataStartCalled = false;
/*      */   }
/*      */ 
/*      */   public void endDTD()
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/* 2305 */       if (this.m_needToCallStartDocument) {
/* 2306 */         return;
/*      */       }
/*      */ 
/* 2309 */       if (this.m_needToOutputDocTypeDecl)
/*      */       {
/* 2311 */         outputDocTypeDecl(this.m_elemContext.m_elementName, false);
/* 2312 */         this.m_needToOutputDocTypeDecl = false;
/*      */       }
/* 2314 */       Writer writer = this.m_writer;
/* 2315 */       if (!this.m_inDoctype) {
/* 2316 */         writer.write("]>");
/*      */       }
/*      */       else {
/* 2319 */         writer.write(62);
/*      */       }
/*      */ 
/* 2322 */       writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 2326 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endPrefixMapping(String prefix)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 2361 */     if (0 == length)
/* 2362 */       return;
/* 2363 */     characters(ch, start, length);
/*      */   }
/*      */ 
/*      */   public void skippedEntity(String name)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startCDATA()
/*      */     throws SAXException
/*      */   {
/* 2389 */     this.m_cdataStartCalled = true;
/*      */   }
/*      */ 
/*      */   public void startEntity(String name)
/*      */     throws SAXException
/*      */   {
/* 2409 */     if (name.equals("[dtd]")) {
/* 2410 */       this.m_inExternalDTD = true;
/*      */     }
/* 2412 */     if ((!this.m_expandDTDEntities) && (!this.m_inExternalDTD))
/*      */     {
/* 2417 */       startNonEscaping();
/* 2418 */       characters("&" + name + ';');
/* 2419 */       endNonEscaping();
/*      */     }
/*      */ 
/* 2422 */     this.m_inEntityRef = true;
/*      */   }
/*      */ 
/*      */   protected void closeStartTag()
/*      */     throws SAXException
/*      */   {
/* 2433 */     if (this.m_elemContext.m_startTagOpen)
/*      */     {
/*      */       try
/*      */       {
/* 2438 */         if (this.m_tracer != null)
/* 2439 */           super.fireStartElem(this.m_elemContext.m_elementName);
/* 2440 */         int nAttrs = this.m_attributes.getLength();
/* 2441 */         if (nAttrs > 0)
/*      */         {
/* 2443 */           processAttributes(this.m_writer, nAttrs);
/*      */ 
/* 2445 */           this.m_attributes.clear();
/*      */         }
/* 2447 */         this.m_writer.write(62);
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/* 2451 */         throw new SAXException(e);
/*      */       }
/*      */ 
/* 2458 */       if (this.m_cdataSectionElements != null) {
/* 2459 */         this.m_elemContext.m_isCdataSection = isCdataSection();
/*      */       }
/* 2461 */       if (this.m_doIndent)
/*      */       {
/* 2463 */         this.m_isprevtext = false;
/* 2464 */         this.m_preserves.push(this.m_ispreserve);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startDTD(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/* 2489 */     setDoctypeSystem(systemId);
/* 2490 */     setDoctypePublic(publicId);
/*      */ 
/* 2492 */     this.m_elemContext.m_elementName = name;
/* 2493 */     this.m_inDoctype = true;
/*      */   }
/*      */ 
/*      */   public int getIndentAmount()
/*      */   {
/* 2502 */     return this.m_indentAmount;
/*      */   }
/*      */ 
/*      */   public void setIndentAmount(int m_indentAmount)
/*      */   {
/* 2512 */     this.m_indentAmount = m_indentAmount;
/*      */   }
/*      */ 
/*      */   protected boolean shouldIndent()
/*      */   {
/* 2523 */     return (this.m_doIndent) && (!this.m_ispreserve) && (!this.m_isprevtext) && ((this.m_elemContext.m_currentElemDepth > 0) || (this.m_isStandalone));
/*      */   }
/*      */ 
/*      */   private void setCdataSectionElements(String key, Properties props)
/*      */   {
/* 2545 */     String s = props.getProperty(key);
/*      */ 
/* 2547 */     if (null != s)
/*      */     {
/* 2550 */       Vector v = new Vector();
/* 2551 */       int l = s.length();
/* 2552 */       boolean inCurly = false;
/* 2553 */       StringBuffer buf = new StringBuffer();
/*      */ 
/* 2558 */       for (int i = 0; i < l; i++)
/*      */       {
/* 2560 */         char c = s.charAt(i);
/*      */ 
/* 2562 */         if (Character.isWhitespace(c))
/*      */         {
/* 2564 */           if (!inCurly)
/*      */           {
/* 2566 */             if (buf.length() <= 0)
/*      */               continue;
/* 2568 */             addCdataSectionElement(buf.toString(), v);
/* 2569 */             buf.setLength(0); continue;
/*      */           }
/*      */ 
/*      */         }
/* 2574 */         else if ('{' == c)
/* 2575 */           inCurly = true;
/* 2576 */         else if ('}' == c) {
/* 2577 */           inCurly = false;
/*      */         }
/* 2579 */         buf.append(c);
/*      */       }
/*      */ 
/* 2582 */       if (buf.length() > 0)
/*      */       {
/* 2584 */         addCdataSectionElement(buf.toString(), v);
/* 2585 */         buf.setLength(0);
/*      */       }
/*      */ 
/* 2588 */       setCdataSectionElements(v);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addCdataSectionElement(String URI_and_localName, Vector v)
/*      */   {
/* 2603 */     StringTokenizer tokenizer = new StringTokenizer(URI_and_localName, "{}", false);
/*      */ 
/* 2605 */     String s1 = tokenizer.nextToken();
/* 2606 */     String s2 = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
/*      */ 
/* 2608 */     if (null == s2)
/*      */     {
/* 2611 */       v.addElement(null);
/* 2612 */       v.addElement(s1);
/*      */     }
/*      */     else
/*      */     {
/* 2617 */       v.addElement(s1);
/* 2618 */       v.addElement(s2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setCdataSectionElements(Vector URI_and_localNames)
/*      */   {
/* 2631 */     this.m_cdataSectionElements = URI_and_localNames;
/*      */   }
/*      */ 
/*      */   protected String ensureAttributesNamespaceIsDeclared(String ns, String localName, String rawName)
/*      */     throws SAXException
/*      */   {
/* 2650 */     if ((ns != null) && (ns.length() > 0))
/*      */     {
/* 2654 */       int index = 0;
/* 2655 */       String prefixFromRawName = (index = rawName.indexOf(":")) < 0 ? "" : rawName.substring(0, index);
/*      */ 
/* 2660 */       if (index > 0)
/*      */       {
/* 2663 */         String uri = this.m_prefixMap.lookupNamespace(prefixFromRawName);
/* 2664 */         if ((uri != null) && (uri.equals(ns)))
/*      */         {
/* 2668 */           return null;
/*      */         }
/*      */ 
/* 2674 */         startPrefixMapping(prefixFromRawName, ns, false);
/* 2675 */         addAttribute("http://www.w3.org/2000/xmlns/", prefixFromRawName, "xmlns:" + prefixFromRawName, "CDATA", ns, false);
/*      */ 
/* 2681 */         return prefixFromRawName;
/*      */       }
/*      */ 
/* 2688 */       String prefix = this.m_prefixMap.lookupPrefix(ns);
/* 2689 */       if (prefix == null)
/*      */       {
/* 2693 */         prefix = this.m_prefixMap.generateNextPrefix();
/* 2694 */         startPrefixMapping(prefix, ns, false);
/* 2695 */         addAttribute("http://www.w3.org/2000/xmlns/", prefix, "xmlns:" + prefix, "CDATA", ns, false);
/*      */       }
/*      */ 
/* 2703 */       return prefix;
/*      */     }
/*      */ 
/* 2707 */     return null;
/*      */   }
/*      */ 
/*      */   void ensurePrefixIsDeclared(String ns, String rawName)
/*      */     throws SAXException
/*      */   {
/* 2714 */     if ((ns != null) && (ns.length() > 0))
/*      */     {
/*      */       int index;
/* 2717 */       boolean no_prefix = (index = rawName.indexOf(":")) < 0;
/* 2718 */       String prefix = no_prefix ? "" : rawName.substring(0, index);
/*      */ 
/* 2720 */       if (null != prefix)
/*      */       {
/* 2722 */         String foundURI = this.m_prefixMap.lookupNamespace(prefix);
/*      */ 
/* 2724 */         if ((null == foundURI) || (!foundURI.equals(ns)))
/*      */         {
/* 2726 */           startPrefixMapping(prefix, ns);
/*      */ 
/* 2731 */           addAttributeAlways("http://www.w3.org/2000/xmlns/", no_prefix ? "xmlns" : prefix, "xmlns:" + prefix, "CDATA", ns, false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void flushPending()
/*      */     throws SAXException
/*      */   {
/* 2750 */     if (this.m_needToCallStartDocument)
/*      */     {
/* 2752 */       startDocumentInternal();
/* 2753 */       this.m_needToCallStartDocument = false;
/*      */     }
/* 2755 */     if (this.m_elemContext.m_startTagOpen)
/*      */     {
/* 2757 */       closeStartTag();
/* 2758 */       this.m_elemContext.m_startTagOpen = false;
/*      */     }
/*      */ 
/* 2761 */     if (this.m_cdataTagOpen)
/*      */     {
/* 2763 */       closeCDATA();
/* 2764 */       this.m_cdataTagOpen = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setContentHandler(ContentHandler ch)
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean addAttributeAlways(String uri, String localName, String rawName, String type, String value, boolean xslAttribute)
/*      */   {
/* 2805 */     int index = this.m_attributes.getIndex(rawName);
/*      */     boolean was_added;
/* 2810 */     if (index >= 0)
/*      */     {
/* 2812 */       String old_value = null;
/* 2813 */       if (this.m_tracer != null)
/*      */       {
/* 2815 */         old_value = this.m_attributes.getValue(index);
/* 2816 */         if (value.equals(old_value)) {
/* 2817 */           old_value = null;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2824 */       this.m_attributes.setValue(index, value);
/* 2825 */       boolean was_added = false;
/* 2826 */       if (old_value != null) {
/* 2827 */         firePseudoAttributes();
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2834 */       if (xslAttribute)
/*      */       {
/* 2849 */         int colonIndex = rawName.indexOf(':');
/* 2850 */         if (colonIndex > 0)
/*      */         {
/* 2852 */           String prefix = rawName.substring(0, colonIndex);
/* 2853 */           NamespaceMappings.MappingRecord existing_mapping = this.m_prefixMap.getMappingFromPrefix(prefix);
/*      */ 
/* 2858 */           if ((existing_mapping != null) && (existing_mapping.m_declarationDepth == this.m_elemContext.m_currentElemDepth) && (!existing_mapping.m_uri.equals(uri)))
/*      */           {
/* 2872 */             prefix = this.m_prefixMap.lookupPrefix(uri);
/* 2873 */             if (prefix == null)
/*      */             {
/* 2884 */               prefix = this.m_prefixMap.generateNextPrefix();
/*      */             }
/*      */ 
/* 2887 */             rawName = prefix + ':' + localName;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 2898 */           prefixUsed = ensureAttributesNamespaceIsDeclared(uri, localName, rawName);
/*      */         }
/*      */         catch (SAXException e)
/*      */         {
/*      */           String prefixUsed;
/* 2907 */           e.printStackTrace();
/*      */         }
/*      */       }
/* 2910 */       this.m_attributes.addAttribute(uri, localName, rawName, type, value);
/* 2911 */       was_added = true;
/* 2912 */       if (this.m_tracer != null) {
/* 2913 */         firePseudoAttributes();
/*      */       }
/*      */     }
/* 2916 */     return was_added;
/*      */   }
/*      */ 
/*      */   protected void firePseudoAttributes()
/*      */   {
/* 2927 */     if (this.m_tracer != null)
/*      */     {
/*      */       try
/*      */       {
/* 2932 */         this.m_writer.flush();
/*      */ 
/* 2935 */         StringBuffer sb = new StringBuffer();
/* 2936 */         int nAttrs = this.m_attributes.getLength();
/* 2937 */         if (nAttrs > 0)
/*      */         {
/* 2941 */           Writer writer = new WritertoStringBuffer(sb);
/*      */ 
/* 2944 */           processAttributes(writer, nAttrs);
/*      */         }
/*      */ 
/* 2949 */         sb.append('>');
/*      */ 
/* 2953 */         char[] ch = sb.toString().toCharArray();
/* 2954 */         this.m_tracer.fireGenerateEvent(11, ch, 0, ch.length);
/*      */       }
/*      */       catch (IOException ioe)
/*      */       {
/*      */       }
/*      */       catch (SAXException se)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setTransformer(Transformer transformer)
/*      */   {
/* 3020 */     super.setTransformer(transformer);
/* 3021 */     if ((this.m_tracer != null) && (!(this.m_writer instanceof SerializerTraceWriter)))
/*      */     {
/* 3023 */       this.m_writer = new SerializerTraceWriter(this.m_writer, this.m_tracer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean reset()
/*      */   {
/* 3036 */     boolean wasReset = false;
/* 3037 */     if (super.reset())
/*      */     {
/* 3039 */       resetToStream();
/* 3040 */       wasReset = true;
/*      */     }
/* 3042 */     return wasReset;
/*      */   }
/*      */ 
/*      */   private void resetToStream()
/*      */   {
/* 3051 */     this.m_cdataStartCalled = false;
/*      */ 
/* 3060 */     this.m_disableOutputEscapingStates.clear();
/*      */ 
/* 3062 */     this.m_escaping = true;
/*      */ 
/* 3065 */     this.m_inDoctype = false;
/* 3066 */     this.m_ispreserve = false;
/* 3067 */     this.m_ispreserve = false;
/* 3068 */     this.m_isprevtext = false;
/* 3069 */     this.m_isUTF8 = false;
/* 3070 */     this.m_preserves.clear();
/* 3071 */     this.m_shouldFlush = true;
/* 3072 */     this.m_spaceBeforeClose = false;
/* 3073 */     this.m_startNewLine = false;
/* 3074 */     this.m_lineSepUse = true;
/*      */ 
/* 3077 */     this.m_expandDTDEntities = true;
/*      */   }
/*      */ 
/*      */   public void setEncoding(String encoding)
/*      */   {
/* 3087 */     String old = getEncoding();
/* 3088 */     super.setEncoding(encoding);
/* 3089 */     if ((old == null) || (!old.equals(encoding)))
/*      */     {
/* 3091 */       this.m_encodingInfo = Encodings.getEncodingInfo(encoding);
/*      */ 
/* 3093 */       if ((encoding != null) && (this.m_encodingInfo.name == null))
/*      */       {
/* 3097 */         String msg = Utils.messages.createMessage("ER_ENCODING_NOT_SUPPORTED", new Object[] { encoding });
/*      */         try
/*      */         {
/* 3102 */           Transformer tran = super.getTransformer();
/* 3103 */           if (tran != null) {
/* 3104 */             ErrorListener errHandler = tran.getErrorListener();
/*      */ 
/* 3106 */             if ((null != errHandler) && (this.m_sourceLocator != null))
/* 3107 */               errHandler.warning(new TransformerException(msg, this.m_sourceLocator));
/*      */             else
/* 3109 */               System.out.println(msg);
/*      */           }
/*      */           else {
/* 3112 */             System.out.println(msg);
/*      */           }
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void notationDecl(String name, String pubID, String sysID)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/* 3309 */       DTDprolog();
/*      */ 
/* 3311 */       this.m_writer.write("<!NOTATION ");
/* 3312 */       this.m_writer.write(name);
/* 3313 */       if (pubID != null) {
/* 3314 */         this.m_writer.write(" PUBLIC \"");
/* 3315 */         this.m_writer.write(pubID);
/*      */       }
/*      */       else
/*      */       {
/* 3319 */         this.m_writer.write(" SYSTEM \"");
/* 3320 */         this.m_writer.write(sysID);
/*      */       }
/* 3322 */       this.m_writer.write("\" >");
/* 3323 */       this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */     }
/*      */     catch (IOException e) {
/* 3326 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String name, String pubID, String sysID, String notationName)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/* 3339 */       DTDprolog();
/*      */ 
/* 3341 */       this.m_writer.write("<!ENTITY ");
/* 3342 */       this.m_writer.write(name);
/* 3343 */       if (pubID != null) {
/* 3344 */         this.m_writer.write(" PUBLIC \"");
/* 3345 */         this.m_writer.write(pubID);
/*      */       }
/*      */       else
/*      */       {
/* 3349 */         this.m_writer.write(" SYSTEM \"");
/* 3350 */         this.m_writer.write(sysID);
/*      */       }
/* 3352 */       this.m_writer.write("\" NDATA ");
/* 3353 */       this.m_writer.write(notationName);
/* 3354 */       this.m_writer.write(" >");
/* 3355 */       this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*      */     }
/*      */     catch (IOException e) {
/* 3358 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void DTDprolog()
/*      */     throws SAXException, IOException
/*      */   {
/* 3368 */     Writer writer = this.m_writer;
/* 3369 */     if (this.m_needToOutputDocTypeDecl)
/*      */     {
/* 3371 */       outputDocTypeDecl(this.m_elemContext.m_elementName, false);
/* 3372 */       this.m_needToOutputDocTypeDecl = false;
/*      */     }
/* 3374 */     if (this.m_inDoctype)
/*      */     {
/* 3376 */       writer.write(" [");
/* 3377 */       writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/* 3378 */       this.m_inDoctype = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDTDEntityExpansion(boolean expand)
/*      */   {
/* 3387 */     this.m_expandDTDEntities = expand;
/*      */   }
/*      */ 
/*      */   static final class BoolStack
/*      */   {
/*      */     private boolean[] m_values;
/*      */     private int m_allocatedSize;
/*      */     private int m_index;
/*      */ 
/*      */     public BoolStack()
/*      */     {
/* 3148 */       this(32);
/*      */     }
/*      */ 
/*      */     public BoolStack(int size)
/*      */     {
/* 3159 */       this.m_allocatedSize = size;
/* 3160 */       this.m_values = new boolean[size];
/* 3161 */       this.m_index = -1;
/*      */     }
/*      */ 
/*      */     public final int size()
/*      */     {
/* 3171 */       return this.m_index + 1;
/*      */     }
/*      */ 
/*      */     public final void clear()
/*      */     {
/* 3180 */       this.m_index = -1;
/*      */     }
/*      */ 
/*      */     public final boolean push(boolean val)
/*      */     {
/* 3193 */       if (this.m_index == this.m_allocatedSize - 1) {
/* 3194 */         grow();
/*      */       }
/* 3196 */       return this.m_values[(++this.m_index)] = val;
/*      */     }
/*      */ 
/*      */     public final boolean pop()
/*      */     {
/* 3208 */       return this.m_values[(this.m_index--)];
/*      */     }
/*      */ 
/*      */     public final boolean popAndTop()
/*      */     {
/* 3221 */       this.m_index -= 1;
/*      */ 
/* 3223 */       return this.m_index >= 0 ? this.m_values[this.m_index] : false;
/*      */     }
/*      */ 
/*      */     public final void setTop(boolean b)
/*      */     {
/* 3234 */       this.m_values[this.m_index] = b;
/*      */     }
/*      */ 
/*      */     public final boolean peek()
/*      */     {
/* 3246 */       return this.m_values[this.m_index];
/*      */     }
/*      */ 
/*      */     public final boolean peekOrFalse()
/*      */     {
/* 3257 */       return this.m_index > -1 ? this.m_values[this.m_index] : false;
/*      */     }
/*      */ 
/*      */     public final boolean peekOrTrue()
/*      */     {
/* 3268 */       return this.m_index > -1 ? this.m_values[this.m_index] : true;
/*      */     }
/*      */ 
/*      */     public boolean isEmpty()
/*      */     {
/* 3279 */       return this.m_index == -1;
/*      */     }
/*      */ 
/*      */     private void grow()
/*      */     {
/* 3289 */       this.m_allocatedSize *= 2;
/*      */ 
/* 3291 */       boolean[] newVector = new boolean[this.m_allocatedSize];
/*      */ 
/* 3293 */       System.arraycopy(this.m_values, 0, newVector, 0, this.m_index + 1);
/*      */ 
/* 3295 */       this.m_values = newVector;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class WritertoStringBuffer extends Writer
/*      */   {
/*      */     private final StringBuffer m_stringbuf;
/*      */ 
/*      */     WritertoStringBuffer(StringBuffer sb)
/*      */     {
/* 2985 */       this.m_stringbuf = sb;
/*      */     }
/*      */ 
/*      */     public void write(char[] arg0, int arg1, int arg2) throws IOException
/*      */     {
/* 2990 */       this.m_stringbuf.append(arg0, arg1, arg2);
/*      */     }
/*      */ 
/*      */     public void flush()
/*      */       throws IOException
/*      */     {
/*      */     }
/*      */ 
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/*      */     }
/*      */ 
/*      */     public void write(int i)
/*      */     {
/* 3007 */       this.m_stringbuf.append((char)i);
/*      */     }
/*      */ 
/*      */     public void write(String s)
/*      */     {
/* 3012 */       this.m_stringbuf.append(s);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ToStream
 * JD-Core Version:    0.6.2
 */