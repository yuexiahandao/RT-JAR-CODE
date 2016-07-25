/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.html.HTMLDocument;
/*     */ 
/*     */ public class OutputFormat
/*     */ {
/*     */   private String _method;
/*     */   private String _version;
/* 140 */   private int _indent = 0;
/*     */ 
/* 147 */   private String _encoding = "UTF-8";
/*     */ 
/* 152 */   private EncodingInfo _encodingInfo = null;
/*     */ 
/* 155 */   private boolean _allowJavaNames = false;
/*     */   private String _mediaType;
/*     */   private String _doctypeSystem;
/*     */   private String _doctypePublic;
/* 178 */   private boolean _omitXmlDeclaration = false;
/*     */ 
/* 184 */   private boolean _omitDoctype = false;
/*     */ 
/* 190 */   private boolean _omitComments = false;
/*     */ 
/* 196 */   private boolean _stripComments = false;
/*     */ 
/* 202 */   private boolean _standalone = false;
/*     */   private String[] _cdataElements;
/*     */   private String[] _nonEscapingElements;
/* 222 */   private String _lineSeparator = "\n";
/*     */ 
/* 228 */   private int _lineWidth = 72;
/*     */ 
/* 235 */   private boolean _preserve = false;
/*     */ 
/* 240 */   private boolean _preserveEmptyAttributes = false;
/*     */ 
/*     */   public OutputFormat()
/*     */   {
/*     */   }
/*     */ 
/*     */   public OutputFormat(String method, String encoding, boolean indenting)
/*     */   {
/* 265 */     setMethod(method);
/* 266 */     setEncoding(encoding);
/* 267 */     setIndenting(indenting);
/*     */   }
/*     */ 
/*     */   public OutputFormat(Document doc)
/*     */   {
/* 281 */     setMethod(whichMethod(doc));
/* 282 */     setDoctype(whichDoctypePublic(doc), whichDoctypeSystem(doc));
/* 283 */     setMediaType(whichMediaType(getMethod()));
/*     */   }
/*     */ 
/*     */   public OutputFormat(Document doc, String encoding, boolean indenting)
/*     */   {
/* 303 */     this(doc);
/* 304 */     setEncoding(encoding);
/* 305 */     setIndenting(indenting);
/*     */   }
/*     */ 
/*     */   public String getMethod()
/*     */   {
/* 321 */     return this._method;
/*     */   }
/*     */ 
/*     */   public void setMethod(String method)
/*     */   {
/* 333 */     this._method = method;
/*     */   }
/*     */ 
/*     */   public String getVersion()
/*     */   {
/* 348 */     return this._version;
/*     */   }
/*     */ 
/*     */   public void setVersion(String version)
/*     */   {
/* 362 */     this._version = version;
/*     */   }
/*     */ 
/*     */   public int getIndent()
/*     */   {
/* 376 */     return this._indent;
/*     */   }
/*     */ 
/*     */   public boolean getIndenting()
/*     */   {
/* 385 */     return this._indent > 0;
/*     */   }
/*     */ 
/*     */   public void setIndent(int indent)
/*     */   {
/* 399 */     if (indent < 0)
/* 400 */       this._indent = 0;
/*     */     else
/* 402 */       this._indent = indent;
/*     */   }
/*     */ 
/*     */   public void setIndenting(boolean on)
/*     */   {
/* 417 */     if (on) {
/* 418 */       this._indent = 4;
/* 419 */       this._lineWidth = 72;
/*     */     } else {
/* 421 */       this._indent = 0;
/* 422 */       this._lineWidth = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 435 */     return this._encoding;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 450 */     this._encoding = encoding;
/* 451 */     this._encodingInfo = null;
/*     */   }
/*     */ 
/*     */   public void setEncoding(EncodingInfo encInfo)
/*     */   {
/* 459 */     this._encoding = encInfo.getIANAName();
/* 460 */     this._encodingInfo = encInfo;
/*     */   }
/*     */ 
/*     */   public EncodingInfo getEncodingInfo()
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 469 */     if (this._encodingInfo == null)
/* 470 */       this._encodingInfo = Encodings.getEncodingInfo(this._encoding, this._allowJavaNames);
/* 471 */     return this._encodingInfo;
/*     */   }
/*     */ 
/*     */   public void setAllowJavaNames(boolean allow)
/*     */   {
/* 478 */     this._allowJavaNames = allow;
/*     */   }
/*     */ 
/*     */   public boolean setAllowJavaNames()
/*     */   {
/* 485 */     return this._allowJavaNames;
/*     */   }
/*     */ 
/*     */   public String getMediaType()
/*     */   {
/* 497 */     return this._mediaType;
/*     */   }
/*     */ 
/*     */   public void setMediaType(String mediaType)
/*     */   {
/* 509 */     this._mediaType = mediaType;
/*     */   }
/*     */ 
/*     */   public void setDoctype(String publicId, String systemId)
/*     */   {
/* 526 */     this._doctypePublic = publicId;
/* 527 */     this._doctypeSystem = systemId;
/*     */   }
/*     */ 
/*     */   public String getDoctypePublic()
/*     */   {
/* 537 */     return this._doctypePublic;
/*     */   }
/*     */ 
/*     */   public String getDoctypeSystem()
/*     */   {
/* 547 */     return this._doctypeSystem;
/*     */   }
/*     */ 
/*     */   public boolean getOmitComments()
/*     */   {
/* 557 */     return this._omitComments;
/*     */   }
/*     */ 
/*     */   public void setOmitComments(boolean omit)
/*     */   {
/* 568 */     this._omitComments = omit;
/*     */   }
/*     */ 
/*     */   public boolean getOmitDocumentType()
/*     */   {
/* 578 */     return this._omitDoctype;
/*     */   }
/*     */ 
/*     */   public void setOmitDocumentType(boolean omit)
/*     */   {
/* 589 */     this._omitDoctype = omit;
/*     */   }
/*     */ 
/*     */   public boolean getOmitXMLDeclaration()
/*     */   {
/* 599 */     return this._omitXmlDeclaration;
/*     */   }
/*     */ 
/*     */   public void setOmitXMLDeclaration(boolean omit)
/*     */   {
/* 610 */     this._omitXmlDeclaration = omit;
/*     */   }
/*     */ 
/*     */   public boolean getStandalone()
/*     */   {
/* 620 */     return this._standalone;
/*     */   }
/*     */ 
/*     */   public void setStandalone(boolean standalone)
/*     */   {
/* 633 */     this._standalone = standalone;
/*     */   }
/*     */ 
/*     */   public String[] getCDataElements()
/*     */   {
/* 644 */     return this._cdataElements;
/*     */   }
/*     */ 
/*     */   public boolean isCDataElement(String tagName)
/*     */   {
/* 659 */     if (this._cdataElements == null)
/* 660 */       return false;
/* 661 */     for (int i = 0; i < this._cdataElements.length; i++)
/* 662 */       if (this._cdataElements[i].equals(tagName))
/* 663 */         return true;
/* 664 */     return false;
/*     */   }
/*     */ 
/*     */   public void setCDataElements(String[] cdataElements)
/*     */   {
/* 676 */     this._cdataElements = cdataElements;
/*     */   }
/*     */ 
/*     */   public String[] getNonEscapingElements()
/*     */   {
/* 687 */     return this._nonEscapingElements;
/*     */   }
/*     */ 
/*     */   public boolean isNonEscapingElement(String tagName)
/*     */   {
/* 702 */     if (this._nonEscapingElements == null) {
/* 703 */       return false;
/*     */     }
/* 705 */     for (int i = 0; i < this._nonEscapingElements.length; i++)
/* 706 */       if (this._nonEscapingElements[i].equals(tagName))
/* 707 */         return true;
/* 708 */     return false;
/*     */   }
/*     */ 
/*     */   public void setNonEscapingElements(String[] nonEscapingElements)
/*     */   {
/* 720 */     this._nonEscapingElements = nonEscapingElements;
/*     */   }
/*     */ 
/*     */   public String getLineSeparator()
/*     */   {
/* 734 */     return this._lineSeparator;
/*     */   }
/*     */ 
/*     */   public void setLineSeparator(String lineSeparator)
/*     */   {
/* 749 */     if (lineSeparator == null)
/* 750 */       this._lineSeparator = "\n";
/*     */     else
/* 752 */       this._lineSeparator = lineSeparator;
/*     */   }
/*     */ 
/*     */   public boolean getPreserveSpace()
/*     */   {
/* 765 */     return this._preserve;
/*     */   }
/*     */ 
/*     */   public void setPreserveSpace(boolean preserve)
/*     */   {
/* 778 */     this._preserve = preserve;
/*     */   }
/*     */ 
/*     */   public int getLineWidth()
/*     */   {
/* 790 */     return this._lineWidth;
/*     */   }
/*     */ 
/*     */   public void setLineWidth(int lineWidth)
/*     */   {
/* 805 */     if (lineWidth <= 0)
/* 806 */       this._lineWidth = 0;
/*     */     else
/* 808 */       this._lineWidth = lineWidth;
/*     */   }
/*     */ 
/*     */   public boolean getPreserveEmptyAttributes()
/*     */   {
/* 815 */     return this._preserveEmptyAttributes;
/*     */   }
/*     */ 
/*     */   public void setPreserveEmptyAttributes(boolean preserve)
/*     */   {
/* 820 */     this._preserveEmptyAttributes = preserve;
/*     */   }
/*     */ 
/*     */   public char getLastPrintable()
/*     */   {
/* 829 */     if ((getEncoding() != null) && (getEncoding().equalsIgnoreCase("ASCII")))
/*     */     {
/* 831 */       return 'ÿ';
/*     */     }
/* 833 */     return 65535;
/*     */   }
/*     */ 
/*     */   public static String whichMethod(Document doc)
/*     */   {
/* 856 */     if ((doc instanceof HTMLDocument)) {
/* 857 */       return "html";
/*     */     }
/*     */ 
/* 865 */     Node node = doc.getFirstChild();
/* 866 */     while (node != null)
/*     */     {
/* 868 */       if (node.getNodeType() == 1) {
/* 869 */         if (node.getNodeName().equalsIgnoreCase("html"))
/* 870 */           return "html";
/* 871 */         if (node.getNodeName().equalsIgnoreCase("root")) {
/* 872 */           return "fop";
/*     */         }
/* 874 */         return "xml";
/*     */       }
/* 876 */       if (node.getNodeType() == 3)
/*     */       {
/* 880 */         String value = node.getNodeValue();
/* 881 */         for (int i = 0; i < value.length(); i++)
/* 882 */           if ((value.charAt(i) != ' ') && (value.charAt(i) != '\n') && (value.charAt(i) != '\t') && (value.charAt(i) != '\r'))
/*     */           {
/* 884 */             return "xml";
/*     */           }
/*     */       }
/* 886 */       node = node.getNextSibling();
/*     */     }
/*     */ 
/* 889 */     return "xml";
/*     */   }
/*     */ 
/*     */   public static String whichDoctypePublic(Document doc)
/*     */   {
/* 902 */     DocumentType doctype = doc.getDoctype();
/* 903 */     if (doctype != null)
/*     */     {
/*     */       try
/*     */       {
/* 907 */         return doctype.getPublicId();
/*     */       } catch (Error except) {
/*     */       }
/*     */     }
/* 911 */     if ((doc instanceof HTMLDocument))
/* 912 */       return "-//W3C//DTD XHTML 1.0 Strict//EN";
/* 913 */     return null;
/*     */   }
/*     */ 
/*     */   public static String whichDoctypeSystem(Document doc)
/*     */   {
/* 926 */     DocumentType doctype = doc.getDoctype();
/* 927 */     if (doctype != null)
/*     */     {
/*     */       try
/*     */       {
/* 931 */         return doctype.getSystemId();
/*     */       } catch (Error except) {
/*     */       }
/*     */     }
/* 935 */     if ((doc instanceof HTMLDocument))
/* 936 */       return "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
/* 937 */     return null;
/*     */   }
/*     */ 
/*     */   public static String whichMediaType(String method)
/*     */   {
/* 947 */     if (method.equalsIgnoreCase("xml"))
/* 948 */       return "text/xml";
/* 949 */     if (method.equalsIgnoreCase("html"))
/* 950 */       return "text/html";
/* 951 */     if (method.equalsIgnoreCase("xhtml"))
/* 952 */       return "text/html";
/* 953 */     if (method.equalsIgnoreCase("text"))
/* 954 */       return "text/plain";
/* 955 */     if (method.equalsIgnoreCase("fop"))
/* 956 */       return "application/pdf";
/* 957 */     return null;
/*     */   }
/*     */ 
/*     */   public static class DTD
/*     */   {
/*     */     public static final String HTMLPublicId = "-//W3C//DTD HTML 4.01//EN";
/*     */     public static final String HTMLSystemId = "http://www.w3.org/TR/html4/strict.dtd";
/*     */     public static final String XHTMLPublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
/*     */     public static final String XHTMLSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
/*     */   }
/*     */ 
/*     */   public static class Defaults
/*     */   {
/*     */     public static final int Indent = 4;
/*     */     public static final String Encoding = "UTF-8";
/*     */     public static final int LineWidth = 72;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.OutputFormat
 * JD-Core Version:    0.6.2
 */