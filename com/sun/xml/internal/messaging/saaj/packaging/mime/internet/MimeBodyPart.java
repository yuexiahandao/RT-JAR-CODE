/*      */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*      */ 
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
/*      */ import com.sun.xml.internal.messaging.saaj.packaging.mime.util.OutputUtil;
/*      */ import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
/*      */ import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
/*      */ import com.sun.xml.internal.org.jvnet.mimepull.Header;
/*      */ import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.util.List;
/*      */ import javax.activation.DataHandler;
/*      */ import javax.activation.DataSource;
/*      */ 
/*      */ public final class MimeBodyPart
/*      */ {
/*      */   public static final String ATTACHMENT = "attachment";
/*      */   public static final String INLINE = "inline";
/*  100 */   private static boolean setDefaultTextCharset = true;
/*      */   private DataHandler dh;
/*      */   private byte[] content;
/*      */   private int contentLength;
/*  130 */   private int start = 0;
/*      */   private InputStream contentStream;
/*      */   private final InternetHeaders headers;
/*      */   private MimeMultipart parent;
/*      */   private MIMEPart mimePart;
/*      */ 
/*      */   public MimeBodyPart()
/*      */   {
/*  166 */     this.headers = new InternetHeaders();
/*      */   }
/*      */ 
/*      */   public MimeBodyPart(InputStream is)
/*      */     throws MessagingException
/*      */   {
/*  185 */     if ((!(is instanceof ByteArrayInputStream)) && (!(is instanceof BufferedInputStream)) && (!(is instanceof SharedInputStream)))
/*      */     {
/*  188 */       is = new BufferedInputStream(is);
/*      */     }
/*  190 */     this.headers = new InternetHeaders(is);
/*      */ 
/*  192 */     if ((is instanceof SharedInputStream)) {
/*  193 */       SharedInputStream sis = (SharedInputStream)is;
/*  194 */       this.contentStream = sis.newStream(sis.getPosition(), -1L);
/*      */     } else {
/*      */       try {
/*  197 */         ByteOutputStream bos = new ByteOutputStream();
/*  198 */         bos.write(is);
/*  199 */         this.content = bos.getBytes();
/*  200 */         this.contentLength = bos.getCount();
/*      */       } catch (IOException ioex) {
/*  202 */         throw new MessagingException("Error reading input stream", ioex);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public MimeBodyPart(InternetHeaders headers, byte[] content, int len)
/*      */   {
/*  218 */     this.headers = headers;
/*  219 */     this.content = content;
/*  220 */     this.contentLength = len;
/*      */   }
/*      */ 
/*      */   public MimeBodyPart(InternetHeaders headers, byte[] content, int start, int len)
/*      */   {
/*  225 */     this.headers = headers;
/*  226 */     this.content = content;
/*  227 */     this.start = start;
/*  228 */     this.contentLength = len;
/*      */   }
/*      */ 
/*      */   public MimeBodyPart(MIMEPart part) {
/*  232 */     this.mimePart = part;
/*  233 */     this.headers = new InternetHeaders();
/*  234 */     List hdrs = this.mimePart.getAllHeaders();
/*  235 */     for (Header hd : hdrs)
/*  236 */       this.headers.addHeader(hd.getName(), hd.getValue());
/*      */   }
/*      */ 
/*      */   public MimeMultipart getParent()
/*      */   {
/*  244 */     return this.parent;
/*      */   }
/*      */ 
/*      */   public void setParent(MimeMultipart parent)
/*      */   {
/*  256 */     this.parent = parent;
/*      */   }
/*      */ 
/*      */   public int getSize()
/*      */   {
/*  277 */     if (this.mimePart != null) {
/*      */       try {
/*  279 */         return this.mimePart.read().available();
/*      */       } catch (IOException ex) {
/*  281 */         return -1;
/*      */       }
/*      */     }
/*  284 */     if (this.content != null)
/*  285 */       return this.contentLength;
/*  286 */     if (this.contentStream != null)
/*      */       try {
/*  288 */         int size = this.contentStream.available();
/*      */ 
/*  291 */         if (size > 0)
/*  292 */           return size;
/*      */       }
/*      */       catch (IOException ex)
/*      */       {
/*      */       }
/*  297 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getLineCount()
/*      */   {
/*  313 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getContentType()
/*      */   {
/*  328 */     if (this.mimePart != null) {
/*  329 */       return this.mimePart.getContentType();
/*      */     }
/*  331 */     String s = getHeader("Content-Type", null);
/*  332 */     if (s == null) {
/*  333 */       s = "text/plain";
/*      */     }
/*  335 */     return s;
/*      */   }
/*      */ 
/*      */   public boolean isMimeType(String mimeType)
/*      */   {
/*      */     boolean result;
/*      */     try
/*      */     {
/*  356 */       ContentType ct = new ContentType(getContentType());
/*  357 */       result = ct.match(mimeType);
/*      */     } catch (ParseException ex) {
/*  359 */       result = getContentType().equalsIgnoreCase(mimeType);
/*      */     }
/*  361 */     return result;
/*      */   }
/*      */ 
/*      */   public String getDisposition()
/*      */     throws MessagingException
/*      */   {
/*  378 */     String s = getHeader("Content-Disposition", null);
/*      */ 
/*  380 */     if (s == null) {
/*  381 */       return null;
/*      */     }
/*  383 */     ContentDisposition cd = new ContentDisposition(s);
/*  384 */     return cd.getDisposition();
/*      */   }
/*      */ 
/*      */   public void setDisposition(String disposition)
/*      */     throws MessagingException
/*      */   {
/*  396 */     if (disposition == null) {
/*  397 */       removeHeader("Content-Disposition");
/*      */     } else {
/*  399 */       String s = getHeader("Content-Disposition", null);
/*  400 */       if (s != null)
/*      */       {
/*  406 */         ContentDisposition cd = new ContentDisposition(s);
/*  407 */         cd.setDisposition(disposition);
/*  408 */         disposition = cd.toString();
/*      */       }
/*  410 */       setHeader("Content-Disposition", disposition);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getEncoding()
/*      */     throws MessagingException
/*      */   {
/*  426 */     String s = getHeader("Content-Transfer-Encoding", null);
/*      */ 
/*  428 */     if (s == null) {
/*  429 */       return null;
/*      */     }
/*  431 */     s = s.trim();
/*      */ 
/*  434 */     if ((s.equalsIgnoreCase("7bit")) || (s.equalsIgnoreCase("8bit")) || (s.equalsIgnoreCase("quoted-printable")) || (s.equalsIgnoreCase("base64")))
/*      */     {
/*  437 */       return s;
/*      */     }
/*      */ 
/*  440 */     HeaderTokenizer h = new HeaderTokenizer(s, "()<>@,;:\\\"\t []/?=");
/*      */     HeaderTokenizer.Token tk;
/*      */     int tkType;
/*      */     do {
/*  446 */       tk = h.next();
/*  447 */       tkType = tk.getType();
/*  448 */       if (tkType == -4) break;
/*      */     }
/*  450 */     while (tkType != -1);
/*  451 */     return tk.getValue();
/*      */ 
/*  455 */     return s;
/*      */   }
/*      */ 
/*      */   public String getContentID()
/*      */   {
/*  467 */     return getHeader("Content-ID", null);
/*      */   }
/*      */ 
/*      */   public void setContentID(String cid)
/*      */   {
/*  480 */     if (cid == null)
/*  481 */       removeHeader("Content-ID");
/*      */     else
/*  483 */       setHeader("Content-ID", cid);
/*      */   }
/*      */ 
/*      */   public String getContentMD5()
/*      */   {
/*  495 */     return getHeader("Content-MD5", null);
/*      */   }
/*      */ 
/*      */   public void setContentMD5(String md5)
/*      */   {
/*  505 */     setHeader("Content-MD5", md5);
/*      */   }
/*      */ 
/*      */   public String[] getContentLanguage()
/*      */     throws MessagingException
/*      */   {
/*  518 */     String s = getHeader("Content-Language", null);
/*      */ 
/*  520 */     if (s == null) {
/*  521 */       return null;
/*      */     }
/*      */ 
/*  524 */     HeaderTokenizer h = new HeaderTokenizer(s, "()<>@,;:\\\"\t []/?=");
/*  525 */     FinalArrayList v = new FinalArrayList();
/*      */     while (true)
/*      */     {
/*  531 */       HeaderTokenizer.Token tk = h.next();
/*  532 */       int tkType = tk.getType();
/*  533 */       if (tkType == -4)
/*      */         break;
/*  535 */       if (tkType == -1) v.add(tk.getValue());
/*      */ 
/*      */     }
/*      */ 
/*  540 */     if (v.size() == 0) {
/*  541 */       return null;
/*      */     }
/*  543 */     return (String[])v.toArray(new String[v.size()]);
/*      */   }
/*      */ 
/*      */   public void setContentLanguage(String[] languages)
/*      */   {
/*  553 */     StringBuffer sb = new StringBuffer(languages[0]);
/*  554 */     for (int i = 1; i < languages.length; i++)
/*  555 */       sb.append(',').append(languages[i]);
/*  556 */     setHeader("Content-Language", sb.toString());
/*      */   }
/*      */ 
/*      */   public String getDescription()
/*      */   {
/*  575 */     String rawvalue = getHeader("Content-Description", null);
/*      */ 
/*  577 */     if (rawvalue == null)
/*  578 */       return null;
/*      */     try
/*      */     {
/*  581 */       return MimeUtility.decodeText(MimeUtility.unfold(rawvalue)); } catch (UnsupportedEncodingException ex) {
/*      */     }
/*  583 */     return rawvalue;
/*      */   }
/*      */ 
/*      */   public void setDescription(String description)
/*      */     throws MessagingException
/*      */   {
/*  611 */     setDescription(description, null);
/*      */   }
/*      */ 
/*      */   public void setDescription(String description, String charset)
/*      */     throws MessagingException
/*      */   {
/*  640 */     if (description == null) {
/*  641 */       removeHeader("Content-Description");
/*  642 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  646 */       setHeader("Content-Description", MimeUtility.fold(21, MimeUtility.encodeText(description, charset, null)));
/*      */     }
/*      */     catch (UnsupportedEncodingException uex) {
/*  649 */       throw new MessagingException("Encoding error", uex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getFileName()
/*      */     throws MessagingException
/*      */   {
/*  665 */     String filename = null;
/*  666 */     String s = getHeader("Content-Disposition", null);
/*      */ 
/*  668 */     if (s != null)
/*      */     {
/*  670 */       ContentDisposition cd = new ContentDisposition(s);
/*  671 */       filename = cd.getParameter("filename");
/*      */     }
/*  673 */     if (filename == null)
/*      */     {
/*  675 */       s = getHeader("Content-Type", null);
/*  676 */       if (s != null)
/*      */         try {
/*  678 */           ContentType ct = new ContentType(s);
/*  679 */           filename = ct.getParameter("name");
/*      */         } catch (ParseException pex) {
/*      */         }
/*      */     }
/*  683 */     return filename;
/*      */   }
/*      */ 
/*      */   public void setFileName(String filename)
/*      */     throws MessagingException
/*      */   {
/*  697 */     String s = getHeader("Content-Disposition", null);
/*  698 */     ContentDisposition cd = new ContentDisposition(s == null ? "attachment" : s);
/*      */ 
/*  700 */     cd.setParameter("filename", filename);
/*  701 */     setHeader("Content-Disposition", cd.toString());
/*      */ 
/*  709 */     s = getHeader("Content-Type", null);
/*  710 */     if (s != null)
/*      */       try {
/*  712 */         ContentType cType = new ContentType(s);
/*  713 */         cType.setParameter("name", filename);
/*  714 */         setHeader("Content-Type", cType.toString());
/*      */       }
/*      */       catch (ParseException pex)
/*      */       {
/*      */       }
/*      */   }
/*      */ 
/*      */   public InputStream getInputStream()
/*      */     throws IOException
/*      */   {
/*  735 */     return getDataHandler().getInputStream();
/*      */   }
/*      */ 
/*      */   InputStream getContentStream()
/*      */     throws MessagingException
/*      */   {
/*  747 */     if (this.mimePart != null) {
/*  748 */       return this.mimePart.read();
/*      */     }
/*  750 */     if (this.contentStream != null)
/*  751 */       return ((SharedInputStream)this.contentStream).newStream(0L, -1L);
/*  752 */     if (this.content != null) {
/*  753 */       return new ByteArrayInputStream(this.content, this.start, this.contentLength);
/*      */     }
/*  755 */     throw new MessagingException("No content");
/*      */   }
/*      */ 
/*      */   public InputStream getRawInputStream()
/*      */     throws MessagingException
/*      */   {
/*  774 */     return getContentStream();
/*      */   }
/*      */ 
/*      */   public DataHandler getDataHandler()
/*      */   {
/*  784 */     if (this.mimePart != null)
/*      */     {
/*  786 */       return new DataHandler(new DataSource()
/*      */       {
/*      */         public InputStream getInputStream() throws IOException {
/*  789 */           return MimeBodyPart.this.mimePart.read();
/*      */         }
/*      */ 
/*      */         public OutputStream getOutputStream() throws IOException {
/*  793 */           throw new UnsupportedOperationException("getOutputStream cannot be supported : You have enabled LazyAttachments Option");
/*      */         }
/*      */ 
/*      */         public String getContentType() {
/*  797 */           return MimeBodyPart.this.mimePart.getContentType();
/*      */         }
/*      */ 
/*      */         public String getName() {
/*  801 */           return "MIMEPart Wrapped DataSource";
/*      */         }
/*      */       });
/*      */     }
/*  805 */     if (this.dh == null)
/*  806 */       this.dh = new DataHandler(new MimePartDataSource(this));
/*  807 */     return this.dh;
/*      */   }
/*      */ 
/*      */   public Object getContent()
/*      */     throws IOException
/*      */   {
/*  828 */     return getDataHandler().getContent();
/*      */   }
/*      */ 
/*      */   public void setDataHandler(DataHandler dh)
/*      */   {
/*  840 */     if (this.mimePart != null) {
/*  841 */       this.mimePart = null;
/*      */     }
/*  843 */     this.dh = dh;
/*  844 */     this.content = null;
/*  845 */     this.contentStream = null;
/*  846 */     removeHeader("Content-Type");
/*  847 */     removeHeader("Content-Transfer-Encoding");
/*      */   }
/*      */ 
/*      */   public void setContent(Object o, String type)
/*      */   {
/*  866 */     if (this.mimePart != null) {
/*  867 */       this.mimePart = null;
/*      */     }
/*  869 */     if ((o instanceof MimeMultipart))
/*  870 */       setContent((MimeMultipart)o);
/*      */     else
/*  872 */       setDataHandler(new DataHandler(o, type));
/*      */   }
/*      */ 
/*      */   public void setText(String text)
/*      */   {
/*  893 */     setText(text, null);
/*      */   }
/*      */ 
/*      */   public void setText(String text, String charset)
/*      */   {
/*  904 */     if (charset == null) {
/*  905 */       if (MimeUtility.checkAscii(text) != 1)
/*  906 */         charset = MimeUtility.getDefaultMIMECharset();
/*      */       else
/*  908 */         charset = "us-ascii";
/*      */     }
/*  910 */     setContent(text, "text/plain; charset=" + MimeUtility.quote(charset, "()<>@,;:\\\"\t []/?="));
/*      */   }
/*      */ 
/*      */   public void setContent(MimeMultipart mp)
/*      */   {
/*  922 */     if (this.mimePart != null) {
/*  923 */       this.mimePart = null;
/*      */     }
/*  925 */     setDataHandler(new DataHandler(mp, mp.getContentType().toString()));
/*  926 */     mp.setParent(this);
/*      */   }
/*      */ 
/*      */   public void writeTo(OutputStream os)
/*      */     throws IOException, MessagingException
/*      */   {
/*  942 */     List hdrLines = this.headers.getAllHeaderLines();
/*  943 */     int sz = hdrLines.size();
/*  944 */     for (int i = 0; i < sz; i++) {
/*  945 */       OutputUtil.writeln((String)hdrLines.get(i), os);
/*      */     }
/*      */ 
/*  948 */     OutputUtil.writeln(os);
/*      */ 
/*  952 */     if (this.contentStream != null) {
/*  953 */       ((SharedInputStream)this.contentStream).writeTo(0L, -1L, os);
/*      */     }
/*  955 */     else if (this.content != null) {
/*  956 */       os.write(this.content, this.start, this.contentLength);
/*      */     }
/*  958 */     else if (this.dh != null)
/*      */     {
/*  960 */       OutputStream wos = MimeUtility.encode(os, getEncoding());
/*  961 */       getDataHandler().writeTo(wos);
/*  962 */       if (os != wos)
/*  963 */         wos.flush();
/*  964 */     } else if (this.mimePart != null) {
/*  965 */       OutputStream wos = MimeUtility.encode(os, getEncoding());
/*  966 */       getDataHandler().writeTo(wos);
/*  967 */       if (os != wos)
/*  968 */         wos.flush();
/*      */     } else {
/*  970 */       throw new MessagingException("no content");
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] getHeader(String name)
/*      */   {
/*  984 */     return this.headers.getHeader(name);
/*      */   }
/*      */ 
/*      */   public String getHeader(String name, String delimiter)
/*      */   {
/*  999 */     return this.headers.getHeader(name, delimiter);
/*      */   }
/*      */ 
/*      */   public void setHeader(String name, String value)
/*      */   {
/* 1014 */     this.headers.setHeader(name, value);
/*      */   }
/*      */ 
/*      */   public void addHeader(String name, String value)
/*      */   {
/* 1028 */     this.headers.addHeader(name, value);
/*      */   }
/*      */ 
/*      */   public void removeHeader(String name)
/*      */   {
/* 1035 */     this.headers.removeHeader(name);
/*      */   }
/*      */ 
/*      */   public FinalArrayList getAllHeaders()
/*      */   {
/* 1043 */     return this.headers.getAllHeaders();
/*      */   }
/*      */ 
/*      */   public void addHeaderLine(String line)
/*      */   {
/* 1051 */     this.headers.addHeaderLine(line);
/*      */   }
/*      */ 
/*      */   protected void updateHeaders()
/*      */     throws MessagingException
/*      */   {
/* 1077 */     DataHandler dh = getDataHandler();
/* 1078 */     if (dh == null)
/* 1079 */       return;
/*      */     try
/*      */     {
/* 1082 */       String type = dh.getContentType();
/* 1083 */       boolean composite = false;
/* 1084 */       boolean needCTHeader = getHeader("Content-Type") == null;
/*      */ 
/* 1086 */       ContentType cType = new ContentType(type);
/* 1087 */       if (cType.match("multipart/*"))
/*      */       {
/* 1089 */         composite = true;
/* 1090 */         Object o = dh.getContent();
/* 1091 */         ((MimeMultipart)o).updateHeaders();
/* 1092 */       } else if (cType.match("message/rfc822")) {
/* 1093 */         composite = true;
/*      */       }
/*      */ 
/* 1098 */       if (!composite) {
/* 1099 */         if (getHeader("Content-Transfer-Encoding") == null) {
/* 1100 */           setEncoding(MimeUtility.getEncoding(dh));
/*      */         }
/* 1102 */         if ((needCTHeader) && (setDefaultTextCharset) && (cType.match("text/*")) && (cType.getParameter("charset") == null))
/*      */         {
/* 1116 */           String enc = getEncoding();
/*      */           String charset;
/*      */           String charset;
/* 1117 */           if ((enc != null) && (enc.equalsIgnoreCase("7bit")))
/* 1118 */             charset = "us-ascii";
/*      */           else
/* 1120 */             charset = MimeUtility.getDefaultMIMECharset();
/* 1121 */           cType.setParameter("charset", charset);
/* 1122 */           type = cType.toString();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1129 */       if (needCTHeader)
/*      */       {
/* 1136 */         String s = getHeader("Content-Disposition", null);
/* 1137 */         if (s != null)
/*      */         {
/* 1139 */           ContentDisposition cd = new ContentDisposition(s);
/* 1140 */           String filename = cd.getParameter("filename");
/* 1141 */           if (filename != null) {
/* 1142 */             cType.setParameter("name", filename);
/* 1143 */             type = cType.toString();
/*      */           }
/*      */         }
/*      */ 
/* 1147 */         setHeader("Content-Type", type);
/*      */       }
/*      */     } catch (IOException ex) {
/* 1150 */       throw new MessagingException("IOException updating headers", ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setEncoding(String encoding) {
/* 1155 */     setHeader("Content-Transfer-Encoding", encoding);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  104 */       String s = System.getProperty("mail.mime.setdefaulttextcharset");
/*      */ 
/*  106 */       setDefaultTextCharset = (s == null) || (!s.equalsIgnoreCase("false"));
/*      */     }
/*      */     catch (SecurityException sex)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart
 * JD-Core Version:    0.6.2
 */