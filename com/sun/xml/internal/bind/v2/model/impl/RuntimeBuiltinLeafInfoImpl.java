/*      */ package com.sun.xml.internal.bind.v2.model.impl;
/*      */ 
/*      */ import com.sun.istack.internal.ByteArrayDataSource;
/*      */ import com.sun.xml.internal.bind.DatatypeConverterImpl;
/*      */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*      */ import com.sun.xml.internal.bind.api.AccessorException;
/*      */ import com.sun.xml.internal.bind.v2.TODO;
/*      */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeBuiltinLeafInfo;
/*      */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*      */ import com.sun.xml.internal.bind.v2.runtime.NamespaceContext2;
/*      */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*      */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*      */ import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
/*      */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
/*      */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*      */ import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;
/*      */ import com.sun.xml.internal.bind.v2.util.DataSourceSource;
/*      */ import java.awt.Component;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.MediaTracker;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Type;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.UUID;
/*      */ import javax.activation.DataHandler;
/*      */ import javax.activation.DataSource;
/*      */ import javax.activation.MimeType;
/*      */ import javax.activation.MimeTypeParseException;
/*      */ import javax.imageio.ImageIO;
/*      */ import javax.imageio.ImageWriter;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ import javax.xml.bind.MarshalException;
/*      */ import javax.xml.bind.helpers.ValidationEventImpl;
/*      */ import javax.xml.datatype.DatatypeConstants;
/*      */ import javax.xml.datatype.DatatypeFactory;
/*      */ import javax.xml.datatype.Duration;
/*      */ import javax.xml.datatype.XMLGregorianCalendar;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public abstract class RuntimeBuiltinLeafInfoImpl<T> extends BuiltinLeafInfoImpl<Type, Class>
/*      */   implements RuntimeBuiltinLeafInfo, Transducer<T>
/*      */ {
/*  176 */   public static final Map<Type, RuntimeBuiltinLeafInfoImpl<?>> LEAVES = new HashMap();
/*      */   public static final RuntimeBuiltinLeafInfoImpl<String> STRING;
/*      */   private static final String DATE = "date";
/*      */   public static final List<RuntimeBuiltinLeafInfoImpl<?>> builtinBeanInfos;
/*      */   public static final String MAP_ANYURI_TO_URI = "mapAnyUriToUri";
/*      */   private static final Map<QName, String> xmlGregorianCalendarFormatString;
/*      */   private static final Map<QName, Integer> xmlGregorianCalendarFieldRef;
/*      */ 
/*      */   private RuntimeBuiltinLeafInfoImpl(Class type, QName[] typeNames)
/*      */   {
/*  106 */     super(type, typeNames);
/*  107 */     LEAVES.put(type, this);
/*      */   }
/*      */ 
/*      */   public final Class getClazz() {
/*  111 */     return (Class)getType();
/*      */   }
/*      */ 
/*      */   public final Transducer getTransducer()
/*      */   {
/*  116 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean useNamespace() {
/*  120 */     return false;
/*      */   }
/*      */ 
/*      */   public final boolean isDefault() {
/*  124 */     return true;
/*      */   }
/*      */ 
/*      */   public void declareNamespace(T o, XMLSerializer w) throws AccessorException {
/*      */   }
/*      */ 
/*      */   public QName getTypeName(T instance) {
/*  131 */     return null;
/*      */   }
/*      */ 
/*      */   private static QName createXS(String typeName)
/*      */   {
/*  179 */     return new QName("http://www.w3.org/2001/XMLSchema", typeName);
/*      */   }
/*      */ 
/*      */   private static byte[] decodeBase64(CharSequence text)
/*      */   {
/*  881 */     if ((text instanceof Base64Data)) {
/*  882 */       Base64Data base64Data = (Base64Data)text;
/*  883 */       return base64Data.getExact();
/*      */     }
/*  885 */     return DatatypeConverterImpl._parseBase64Binary(text.toString());
/*      */   }
/*      */ 
/*      */   private static void checkXmlGregorianCalendarFieldRef(QName type, XMLGregorianCalendar cal)
/*      */     throws MarshalException
/*      */   {
/*  891 */     StringBuilder buf = new StringBuilder();
/*  892 */     int bitField = ((Integer)xmlGregorianCalendarFieldRef.get(type)).intValue();
/*  893 */     int l = 1;
/*  894 */     int pos = 0;
/*  895 */     while (bitField != 0) {
/*  896 */       int bit = bitField & 0x1;
/*  897 */       bitField >>>= 4;
/*  898 */       pos++;
/*      */ 
/*  900 */       if (bit == 1) {
/*  901 */         switch (pos) {
/*      */         case 1:
/*  903 */           if (cal.getSecond() == -2147483648)
/*  904 */             buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_SEC); break;
/*      */         case 2:
/*  908 */           if (cal.getMinute() == -2147483648)
/*  909 */             buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_MIN); break;
/*      */         case 3:
/*  913 */           if (cal.getHour() == -2147483648)
/*  914 */             buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_HR); break;
/*      */         case 4:
/*  918 */           if (cal.getDay() == -2147483648)
/*  919 */             buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_DAY); break;
/*      */         case 5:
/*  923 */           if (cal.getMonth() == -2147483648)
/*  924 */             buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_MONTH); break;
/*      */         case 6:
/*  928 */           if (cal.getYear() == -2147483648) {
/*  929 */             buf.append("  ").append(Messages.XMLGREGORIANCALENDAR_YEAR);
/*      */           }
/*      */           break;
/*      */         case 7:
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  937 */     if (buf.length() > 0)
/*  938 */       throw new MarshalException(Messages.XMLGREGORIANCALENDAR_INVALID.format(new Object[] { type.getLocalPart() }) + buf.toString());
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  199 */     String MAP_ANYURI_TO_URI_VALUE = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public String run()
/*      */       {
/*  203 */         return System.getProperty("mapAnyUriToUri");
/*      */       }
/*      */     });
/*  207 */     QName[] qnames = { createXS("string"), createXS("anySimpleType"), createXS("normalizedString"), createXS("token"), createXS("language"), createXS("Name"), createXS("NCName"), createXS("NMTOKEN"), MAP_ANYURI_TO_URI_VALUE == null ? new QName[] { createXS("string"), createXS("anySimpleType"), createXS("normalizedString"), createXS("anyURI"), createXS("token"), createXS("language"), createXS("Name"), createXS("NCName"), createXS("NMTOKEN"), createXS("ENTITY") } : createXS("ENTITY") };
/*      */ 
/*  229 */     STRING = new StringImplImpl(String.class, qnames);
/*      */ 
/*  231 */     ArrayList secondaryList = new ArrayList();
/*      */ 
/*  249 */     secondaryList.add(new StringImpl(Character.class, new QName[] { createXS("unsignedShort") })
/*      */     {
/*      */       public Character parse(CharSequence text)
/*      */       {
/*  253 */         return Character.valueOf((char)DatatypeConverterImpl._parseInt(text));
/*      */       }
/*      */       public String print(Character v) {
/*  256 */         return Integer.toString(v.charValue());
/*      */       }
/*      */     });
/*  259 */     secondaryList.add(new StringImpl(Calendar.class, new QName[] { DatatypeConstants.DATETIME })
/*      */     {
/*      */       public Calendar parse(CharSequence text) {
/*  262 */         return DatatypeConverterImpl._parseDateTime(text.toString());
/*      */       }
/*      */       public String print(Calendar v) {
/*  265 */         return DatatypeConverterImpl._printDateTime(v);
/*      */       }
/*      */     });
/*  268 */     secondaryList.add(new StringImpl(GregorianCalendar.class, new QName[] { DatatypeConstants.DATETIME })
/*      */     {
/*      */       public GregorianCalendar parse(CharSequence text) {
/*  271 */         return DatatypeConverterImpl._parseDateTime(text.toString());
/*      */       }
/*      */       public String print(GregorianCalendar v) {
/*  274 */         return DatatypeConverterImpl._printDateTime(v);
/*      */       }
/*      */     });
/*  277 */     secondaryList.add(new StringImpl(Date.class, new QName[] { DatatypeConstants.DATETIME })
/*      */     {
/*      */       public Date parse(CharSequence text) {
/*  280 */         return DatatypeConverterImpl._parseDateTime(text.toString()).getTime();
/*      */       }
/*      */       public String print(Date v) {
/*  283 */         XMLSerializer xs = XMLSerializer.getInstance();
/*  284 */         QName type = xs.getSchemaType();
/*  285 */         GregorianCalendar cal = new GregorianCalendar(0, 0, 0);
/*  286 */         cal.setTime(v);
/*  287 */         if ((type != null) && ("http://www.w3.org/2001/XMLSchema".equals(type.getNamespaceURI())) && ("date".equals(type.getLocalPart())))
/*      */         {
/*  289 */           return DatatypeConverterImpl._printDate(cal);
/*      */         }
/*  291 */         return DatatypeConverterImpl._printDateTime(cal);
/*      */       }
/*      */     });
/*  295 */     secondaryList.add(new StringImpl(File.class, new QName[] { createXS("string") })
/*      */     {
/*      */       public File parse(CharSequence text) {
/*  298 */         return new File(WhiteSpaceProcessor.trim(text).toString());
/*      */       }
/*      */       public String print(File v) {
/*  301 */         return v.getPath();
/*      */       }
/*      */     });
/*  304 */     secondaryList.add(new StringImpl(URL.class, new QName[] { createXS("anyURI") })
/*      */     {
/*      */       public URL parse(CharSequence text) throws SAXException {
/*  307 */         TODO.checkSpec("JSR222 Issue #42");
/*      */         try {
/*  309 */           return new URL(WhiteSpaceProcessor.trim(text).toString());
/*      */         } catch (MalformedURLException e) {
/*  311 */           UnmarshallingContext.getInstance().handleError(e);
/*  312 */         }return null;
/*      */       }
/*      */ 
/*      */       public String print(URL v) {
/*  316 */         return v.toExternalForm();
/*      */       }
/*      */     });
/*  319 */     if (MAP_ANYURI_TO_URI_VALUE == null) {
/*  320 */       secondaryList.add(new StringImpl(URI.class, new QName[] { createXS("string") })
/*      */       {
/*      */         public URI parse(CharSequence text) throws SAXException {
/*      */           try {
/*  324 */             return new URI(text.toString());
/*      */           } catch (URISyntaxException e) {
/*  326 */             UnmarshallingContext.getInstance().handleError(e);
/*  327 */           }return null;
/*      */         }
/*      */ 
/*      */         public String print(URI v)
/*      */         {
/*  332 */           return v.toString();
/*      */         }
/*      */       });
/*      */     }
/*  336 */     secondaryList.add(new StringImpl(Class.class, new QName[] { createXS("string") })
/*      */     {
/*      */       public Class parse(CharSequence text) throws SAXException {
/*  339 */         TODO.checkSpec("JSR222 Issue #42");
/*      */         try {
/*  341 */           String name = WhiteSpaceProcessor.trim(text).toString();
/*  342 */           ClassLoader cl = UnmarshallingContext.getInstance().classLoader;
/*  343 */           if (cl == null) {
/*  344 */             cl = Thread.currentThread().getContextClassLoader();
/*      */           }
/*  346 */           if (cl != null) {
/*  347 */             return cl.loadClass(name);
/*      */           }
/*  349 */           return Class.forName(name);
/*      */         } catch (ClassNotFoundException e) {
/*  351 */           UnmarshallingContext.getInstance().handleError(e);
/*  352 */         }return null;
/*      */       }
/*      */ 
/*      */       public String print(Class v) {
/*  356 */         return v.getName();
/*      */       }
/*      */     });
/*  364 */     secondaryList.add(new PcdataImpl(Image.class, new QName[] { createXS("base64Binary") })
/*      */     {
/*      */       public Image parse(CharSequence text)
/*      */         throws SAXException
/*      */       {
/*      */         try
/*      */         {
/*      */           InputStream is;
/*      */           InputStream is;
/*  369 */           if ((text instanceof Base64Data))
/*  370 */             is = ((Base64Data)text).getInputStream();
/*      */           else {
/*  372 */             is = new ByteArrayInputStream(RuntimeBuiltinLeafInfoImpl.decodeBase64(text));
/*      */           }
/*      */ 
/*      */           try
/*      */           {
/*  379 */             return ImageIO.read(is);
/*      */           } finally {
/*  381 */             is.close();
/*      */           }
/*      */         } catch (IOException e) {
/*  384 */           UnmarshallingContext.getInstance().handleError(e);
/*  385 */         }return null;
/*      */       }
/*      */ 
/*      */       private BufferedImage convertToBufferedImage(Image image) throws IOException
/*      */       {
/*  390 */         if ((image instanceof BufferedImage)) {
/*  391 */           return (BufferedImage)image;
/*      */         }
/*      */ 
/*  394 */         MediaTracker tracker = new MediaTracker(new Component()
/*      */         {
/*      */         });
/*  395 */         tracker.addImage(image, 0);
/*      */         try {
/*  397 */           tracker.waitForAll();
/*      */         } catch (InterruptedException e) {
/*  399 */           throw new IOException(e.getMessage());
/*      */         }
/*  401 */         BufferedImage bufImage = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
/*      */ 
/*  406 */         Graphics g = bufImage.createGraphics();
/*  407 */         g.drawImage(image, 0, 0, null);
/*  408 */         return bufImage;
/*      */       }
/*      */ 
/*      */       public Base64Data print(Image v)
/*      */       {
/*  413 */         ByteArrayOutputStreamEx imageData = new ByteArrayOutputStreamEx();
/*  414 */         XMLSerializer xs = XMLSerializer.getInstance();
/*      */ 
/*  416 */         String mimeType = xs.getXMIMEContentType();
/*  417 */         if ((mimeType == null) || (mimeType.startsWith("image/*")))
/*      */         {
/*  423 */           mimeType = "image/png";
/*      */         }
/*      */         try {
/*  426 */           Iterator itr = ImageIO.getImageWritersByMIMEType(mimeType);
/*  427 */           if (itr.hasNext()) {
/*  428 */             ImageWriter w = (ImageWriter)itr.next();
/*  429 */             ImageOutputStream os = ImageIO.createImageOutputStream(imageData);
/*  430 */             w.setOutput(os);
/*  431 */             w.write(convertToBufferedImage(v));
/*  432 */             os.close();
/*  433 */             w.dispose();
/*      */           }
/*      */           else {
/*  436 */             xs.handleEvent(new ValidationEventImpl(1, Messages.NO_IMAGE_WRITER.format(new Object[] { mimeType }), xs.getCurrentLocation(null)));
/*      */ 
/*  441 */             throw new RuntimeException("no encoder for MIME type " + mimeType);
/*      */           }
/*      */         } catch (IOException e) {
/*  444 */           xs.handleError(e);
/*      */ 
/*  446 */           throw new RuntimeException(e);
/*      */         }
/*  448 */         Base64Data bd = new Base64Data();
/*  449 */         imageData.set(bd, mimeType);
/*  450 */         return bd;
/*      */       }
/*      */     });
/*  453 */     secondaryList.add(new PcdataImpl(DataHandler.class, new QName[] { createXS("base64Binary") })
/*      */     {
/*      */       public DataHandler parse(CharSequence text) {
/*  456 */         if ((text instanceof Base64Data)) {
/*  457 */           return ((Base64Data)text).getDataHandler();
/*      */         }
/*  459 */         return new DataHandler(new ByteArrayDataSource(RuntimeBuiltinLeafInfoImpl.decodeBase64(text), UnmarshallingContext.getInstance().getXMIMEContentType()));
/*      */       }
/*      */ 
/*      */       public Base64Data print(DataHandler v)
/*      */       {
/*  464 */         Base64Data bd = new Base64Data();
/*  465 */         bd.set(v);
/*  466 */         return bd;
/*      */       }
/*      */     });
/*  469 */     secondaryList.add(new PcdataImpl(Source.class, new QName[] { createXS("base64Binary") })
/*      */     {
/*      */       public Source parse(CharSequence text) throws SAXException {
/*      */         try {
/*  473 */           if ((text instanceof Base64Data)) {
/*  474 */             return new DataSourceSource(((Base64Data)text).getDataHandler());
/*      */           }
/*  476 */           return new DataSourceSource(new ByteArrayDataSource(RuntimeBuiltinLeafInfoImpl.decodeBase64(text), UnmarshallingContext.getInstance().getXMIMEContentType()));
/*      */         }
/*      */         catch (MimeTypeParseException e) {
/*  479 */           UnmarshallingContext.getInstance().handleError(e);
/*  480 */         }return null;
/*      */       }
/*      */ 
/*      */       public Base64Data print(Source v)
/*      */       {
/*  485 */         XMLSerializer xs = XMLSerializer.getInstance();
/*  486 */         Base64Data bd = new Base64Data();
/*      */ 
/*  488 */         String contentType = xs.getXMIMEContentType();
/*  489 */         MimeType mt = null;
/*  490 */         if (contentType != null) {
/*      */           try {
/*  492 */             mt = new MimeType(contentType);
/*      */           } catch (MimeTypeParseException e) {
/*  494 */             xs.handleError(e);
/*      */           }
/*      */         }
/*      */ 
/*  498 */         if ((v instanceof DataSourceSource))
/*      */         {
/*  501 */           DataSource ds = ((DataSourceSource)v).getDataSource();
/*      */ 
/*  503 */           String dsct = ds.getContentType();
/*  504 */           if ((dsct != null) && ((contentType == null) || (contentType.equals(dsct)))) {
/*  505 */             bd.set(new DataHandler(ds));
/*  506 */             return bd;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  513 */         String charset = null;
/*  514 */         if (mt != null)
/*  515 */           charset = mt.getParameter("charset");
/*  516 */         if (charset == null)
/*  517 */           charset = "UTF-8";
/*      */         try
/*      */         {
/*  520 */           ByteArrayOutputStreamEx baos = new ByteArrayOutputStreamEx();
/*  521 */           Transformer tr = xs.getIdentityTransformer();
/*  522 */           String defaultEncoding = tr.getOutputProperty("encoding");
/*  523 */           tr.setOutputProperty("encoding", charset);
/*  524 */           tr.transform(v, new StreamResult(new OutputStreamWriter(baos, charset)));
/*  525 */           tr.setOutputProperty("encoding", defaultEncoding);
/*  526 */           baos.set(bd, "application/xml; charset=" + charset);
/*  527 */           return bd;
/*      */         }
/*      */         catch (TransformerException e) {
/*  530 */           xs.handleError(e);
/*      */         } catch (UnsupportedEncodingException e) {
/*  532 */           xs.handleError(e);
/*      */         }
/*      */ 
/*  536 */         bd.set(new byte[0], "application/xml");
/*  537 */         return bd;
/*      */       }
/*      */     });
/*  540 */     secondaryList.add(new StringImpl(XMLGregorianCalendar.class, new QName[] { createXS("anySimpleType"), DatatypeConstants.DATE, DatatypeConstants.DATETIME, DatatypeConstants.TIME, DatatypeConstants.GMONTH, DatatypeConstants.GDAY, DatatypeConstants.GYEAR, DatatypeConstants.GYEARMONTH, DatatypeConstants.GMONTHDAY })
/*      */     {
/*      */       public String print(XMLGregorianCalendar cal)
/*      */       {
/*  553 */         XMLSerializer xs = XMLSerializer.getInstance();
/*      */ 
/*  555 */         QName type = xs.getSchemaType();
/*  556 */         if (type != null) {
/*      */           try {
/*  558 */             RuntimeBuiltinLeafInfoImpl.checkXmlGregorianCalendarFieldRef(type, cal);
/*  559 */             String format = (String)RuntimeBuiltinLeafInfoImpl.xmlGregorianCalendarFormatString.get(type);
/*  560 */             if (format != null)
/*  561 */               return format(format, cal);
/*      */           }
/*      */           catch (MarshalException e)
/*      */           {
/*  565 */             xs.handleEvent(new ValidationEventImpl(0, e.getMessage(), xs.getCurrentLocation(null)));
/*      */ 
/*  567 */             return "";
/*      */           }
/*      */         }
/*  570 */         return cal.toXMLFormat();
/*      */       }
/*      */ 
/*      */       public XMLGregorianCalendar parse(CharSequence lexical) throws SAXException {
/*      */         try {
/*  575 */           return DatatypeConverterImpl.getDatatypeFactory().newXMLGregorianCalendar(lexical.toString().trim());
/*      */         }
/*      */         catch (Exception e) {
/*  578 */           UnmarshallingContext.getInstance().handleError(e);
/*  579 */         }return null;
/*      */       }
/*      */ 
/*      */       private String format(String format, XMLGregorianCalendar value)
/*      */       {
/*  585 */         StringBuilder buf = new StringBuilder();
/*  586 */         int fidx = 0; int flen = format.length();
/*      */ 
/*  588 */         while (fidx < flen) {
/*  589 */           char fch = format.charAt(fidx++);
/*  590 */           if (fch != '%') {
/*  591 */             buf.append(fch);
/*      */           }
/*      */           else
/*      */           {
/*  595 */             switch (format.charAt(fidx++)) {
/*      */             case 'Y':
/*  597 */               printNumber(buf, value.getEonAndYear(), 4);
/*  598 */               break;
/*      */             case 'M':
/*  600 */               printNumber(buf, value.getMonth(), 2);
/*  601 */               break;
/*      */             case 'D':
/*  603 */               printNumber(buf, value.getDay(), 2);
/*  604 */               break;
/*      */             case 'h':
/*  606 */               printNumber(buf, value.getHour(), 2);
/*  607 */               break;
/*      */             case 'm':
/*  609 */               printNumber(buf, value.getMinute(), 2);
/*  610 */               break;
/*      */             case 's':
/*  612 */               printNumber(buf, value.getSecond(), 2);
/*  613 */               if (value.getFractionalSecond() != null) {
/*  614 */                 String frac = value.getFractionalSecond().toPlainString();
/*      */ 
/*  616 */                 buf.append(frac.substring(1, frac.length()));
/*  617 */               }break;
/*      */             case 'z':
/*  620 */               int offset = value.getTimezone();
/*  621 */               if (offset == 0) {
/*  622 */                 buf.append('Z');
/*  623 */               } else if (offset != -2147483648) {
/*  624 */                 if (offset < 0) {
/*  625 */                   buf.append('-');
/*  626 */                   offset *= -1;
/*      */                 } else {
/*  628 */                   buf.append('+');
/*      */                 }
/*  630 */                 printNumber(buf, offset / 60, 2);
/*  631 */                 buf.append(':');
/*  632 */                 printNumber(buf, offset % 60, 2); } break;
/*      */             default:
/*  636 */               throw new InternalError();
/*      */             }
/*      */           }
/*      */         }
/*  640 */         return buf.toString();
/*      */       }
/*      */       private void printNumber(StringBuilder out, BigInteger number, int nDigits) {
/*  643 */         String s = number.toString();
/*  644 */         for (int i = s.length(); i < nDigits; i++)
/*  645 */           out.append('0');
/*  646 */         out.append(s);
/*      */       }
/*      */       private void printNumber(StringBuilder out, int number, int nDigits) {
/*  649 */         String s = String.valueOf(number);
/*  650 */         for (int i = s.length(); i < nDigits; i++)
/*  651 */           out.append('0');
/*  652 */         out.append(s);
/*      */       }
/*      */ 
/*      */       public QName getTypeName(XMLGregorianCalendar cal) {
/*  656 */         return cal.getXMLSchemaType();
/*      */       }
/*      */     });
/*  660 */     ArrayList primaryList = new ArrayList();
/*      */ 
/*  665 */     primaryList.add(STRING);
/*  666 */     primaryList.add(new StringImpl(Boolean.class, new QName[] { createXS("boolean") })
/*      */     {
/*      */       public Boolean parse(CharSequence text)
/*      */       {
/*  670 */         return DatatypeConverterImpl._parseBoolean(text);
/*      */       }
/*      */ 
/*      */       public String print(Boolean v) {
/*  674 */         return v.toString();
/*      */       }
/*      */     });
/*  677 */     primaryList.add(new PcdataImpl([B.class, new QName[] { createXS("base64Binary"), createXS("hexBinary") })
/*      */     {
/*      */       public byte[] parse(CharSequence text)
/*      */       {
/*  682 */         return RuntimeBuiltinLeafInfoImpl.decodeBase64(text);
/*      */       }
/*      */ 
/*      */       public Base64Data print(byte[] v) {
/*  686 */         XMLSerializer w = XMLSerializer.getInstance();
/*  687 */         Base64Data bd = new Base64Data();
/*  688 */         String mimeType = w.getXMIMEContentType();
/*  689 */         bd.set(v, mimeType);
/*  690 */         return bd;
/*      */       }
/*      */     });
/*  693 */     primaryList.add(new StringImpl(Byte.class, new QName[] { createXS("byte") })
/*      */     {
/*      */       public Byte parse(CharSequence text)
/*      */       {
/*  697 */         return Byte.valueOf(DatatypeConverterImpl._parseByte(text));
/*      */       }
/*      */ 
/*      */       public String print(Byte v) {
/*  701 */         return DatatypeConverterImpl._printByte(v.byteValue());
/*      */       }
/*      */     });
/*  704 */     primaryList.add(new StringImpl(Short.class, new QName[] { createXS("short"), createXS("unsignedByte") })
/*      */     {
/*      */       public Short parse(CharSequence text)
/*      */       {
/*  709 */         return Short.valueOf(DatatypeConverterImpl._parseShort(text));
/*      */       }
/*      */ 
/*      */       public String print(Short v) {
/*  713 */         return DatatypeConverterImpl._printShort(v.shortValue());
/*      */       }
/*      */     });
/*  716 */     primaryList.add(new StringImpl(Integer.class, new QName[] { createXS("int"), createXS("unsignedShort") })
/*      */     {
/*      */       public Integer parse(CharSequence text)
/*      */       {
/*  721 */         return Integer.valueOf(DatatypeConverterImpl._parseInt(text));
/*      */       }
/*      */ 
/*      */       public String print(Integer v) {
/*  725 */         return DatatypeConverterImpl._printInt(v.intValue());
/*      */       }
/*      */     });
/*  728 */     primaryList.add(new StringImpl(Long.class, new QName[] { createXS("long"), createXS("unsignedInt") })
/*      */     {
/*      */       public Long parse(CharSequence text)
/*      */       {
/*  734 */         return Long.valueOf(DatatypeConverterImpl._parseLong(text));
/*      */       }
/*      */ 
/*      */       public String print(Long v) {
/*  738 */         return DatatypeConverterImpl._printLong(v.longValue());
/*      */       }
/*      */     });
/*  741 */     primaryList.add(new StringImpl(Float.class, new QName[] { createXS("float") })
/*      */     {
/*      */       public Float parse(CharSequence text)
/*      */       {
/*  746 */         return Float.valueOf(DatatypeConverterImpl._parseFloat(text.toString()));
/*      */       }
/*      */ 
/*      */       public String print(Float v) {
/*  750 */         return DatatypeConverterImpl._printFloat(v.floatValue());
/*      */       }
/*      */     });
/*  753 */     primaryList.add(new StringImpl(Double.class, new QName[] { createXS("double") })
/*      */     {
/*      */       public Double parse(CharSequence text)
/*      */       {
/*  758 */         return Double.valueOf(DatatypeConverterImpl._parseDouble(text));
/*      */       }
/*      */ 
/*      */       public String print(Double v) {
/*  762 */         return DatatypeConverterImpl._printDouble(v.doubleValue());
/*      */       }
/*      */     });
/*  765 */     primaryList.add(new StringImpl(BigInteger.class, new QName[] { createXS("integer"), createXS("positiveInteger"), createXS("negativeInteger"), createXS("nonPositiveInteger"), createXS("nonNegativeInteger"), createXS("unsignedLong") })
/*      */     {
/*      */       public BigInteger parse(CharSequence text)
/*      */       {
/*  775 */         return DatatypeConverterImpl._parseInteger(text);
/*      */       }
/*      */ 
/*      */       public String print(BigInteger v) {
/*  779 */         return DatatypeConverterImpl._printInteger(v);
/*      */       }
/*      */     });
/*  782 */     primaryList.add(new StringImpl(BigDecimal.class, new QName[] { createXS("decimal") })
/*      */     {
/*      */       public BigDecimal parse(CharSequence text)
/*      */       {
/*  787 */         return DatatypeConverterImpl._parseDecimal(text.toString());
/*      */       }
/*      */ 
/*      */       public String print(BigDecimal v) {
/*  791 */         return DatatypeConverterImpl._printDecimal(v);
/*      */       }
/*      */     });
/*  795 */     primaryList.add(new StringImpl(QName.class, new QName[] { createXS("QName") })
/*      */     {
/*      */       public QName parse(CharSequence text) throws SAXException
/*      */       {
/*      */         try
/*      */         {
/*  801 */           return DatatypeConverterImpl._parseQName(text.toString(), UnmarshallingContext.getInstance());
/*      */         } catch (IllegalArgumentException e) {
/*  803 */           UnmarshallingContext.getInstance().handleError(e);
/*  804 */         }return null;
/*      */       }
/*      */ 
/*      */       public String print(QName v)
/*      */       {
/*  809 */         return DatatypeConverterImpl._printQName(v, XMLSerializer.getInstance().getNamespaceContext());
/*      */       }
/*      */ 
/*      */       public boolean useNamespace()
/*      */       {
/*  814 */         return true;
/*      */       }
/*      */ 
/*      */       public void declareNamespace(QName v, XMLSerializer w)
/*      */       {
/*  819 */         w.getNamespaceContext().declareNamespace(v.getNamespaceURI(), v.getPrefix(), false);
/*      */       }
/*      */     });
/*  822 */     if (MAP_ANYURI_TO_URI_VALUE != null) {
/*  823 */       primaryList.add(new StringImpl(URI.class, new QName[] { createXS("anyURI") })
/*      */       {
/*      */         public URI parse(CharSequence text) throws SAXException {
/*      */           try {
/*  827 */             return new URI(text.toString());
/*      */           } catch (URISyntaxException e) {
/*  829 */             UnmarshallingContext.getInstance().handleError(e);
/*  830 */           }return null;
/*      */         }
/*      */ 
/*      */         public String print(URI v)
/*      */         {
/*  835 */           return v.toString();
/*      */         }
/*      */       });
/*      */     }
/*  839 */     primaryList.add(new StringImpl(Duration.class, new QName[] { createXS("duration") })
/*      */     {
/*      */       public String print(Duration duration) {
/*  842 */         return duration.toString();
/*      */       }
/*      */ 
/*      */       public Duration parse(CharSequence lexical) {
/*  846 */         TODO.checkSpec("JSR222 Issue #42");
/*  847 */         return DatatypeConverterImpl.getDatatypeFactory().newDuration(lexical.toString());
/*      */       }
/*      */     });
/*  851 */     primaryList.add(new StringImpl(Void.class, new QName[0])
/*      */     {
/*      */       public String print(Void value)
/*      */       {
/*  857 */         return "";
/*      */       }
/*      */ 
/*      */       public Void parse(CharSequence lexical) {
/*  861 */         return null;
/*      */       }
/*      */     });
/*  865 */     List l = new ArrayList(secondaryList.size() + primaryList.size() + 1);
/*  866 */     l.addAll(secondaryList);
/*      */     try
/*      */     {
/*  870 */       l.add(new UUIDImpl());
/*      */     }
/*      */     catch (LinkageError e)
/*      */     {
/*      */     }
/*  875 */     l.addAll(primaryList);
/*      */ 
/*  877 */     builtinBeanInfos = Collections.unmodifiableList(l);
/*      */ 
/*  947 */     xmlGregorianCalendarFormatString = new HashMap();
/*      */ 
/*  950 */     Map m = xmlGregorianCalendarFormatString;
/*      */ 
/*  952 */     m.put(DatatypeConstants.DATETIME, "%Y-%M-%DT%h:%m:%s%z");
/*  953 */     m.put(DatatypeConstants.DATE, "%Y-%M-%D%z");
/*  954 */     m.put(DatatypeConstants.TIME, "%h:%m:%s%z");
/*  955 */     m.put(DatatypeConstants.GMONTH, "--%M--%z");
/*  956 */     m.put(DatatypeConstants.GDAY, "---%D%z");
/*  957 */     m.put(DatatypeConstants.GYEAR, "%Y%z");
/*  958 */     m.put(DatatypeConstants.GYEARMONTH, "%Y-%M%z");
/*  959 */     m.put(DatatypeConstants.GMONTHDAY, "--%M-%D%z");
/*      */ 
/*  972 */     xmlGregorianCalendarFieldRef = new HashMap();
/*      */ 
/*  975 */     Map f = xmlGregorianCalendarFieldRef;
/*  976 */     f.put(DatatypeConstants.DATETIME, Integer.valueOf(17895697));
/*  977 */     f.put(DatatypeConstants.DATE, Integer.valueOf(17895424));
/*  978 */     f.put(DatatypeConstants.TIME, Integer.valueOf(16777489));
/*  979 */     f.put(DatatypeConstants.GDAY, Integer.valueOf(16781312));
/*  980 */     f.put(DatatypeConstants.GMONTH, Integer.valueOf(16842752));
/*  981 */     f.put(DatatypeConstants.GYEAR, Integer.valueOf(17825792));
/*  982 */     f.put(DatatypeConstants.GYEARMONTH, Integer.valueOf(17891328));
/*  983 */     f.put(DatatypeConstants.GMONTHDAY, Integer.valueOf(16846848));
/*      */   }
/*      */ 
/*      */   private static abstract class PcdataImpl<T> extends RuntimeBuiltinLeafInfoImpl<T>
/*      */   {
/*      */     protected PcdataImpl(Class type, QName[] typeNames)
/*      */     {
/*  158 */       super(typeNames, null);
/*      */     }
/*      */ 
/*      */     public abstract Pcdata print(T paramT) throws AccessorException;
/*      */ 
/*      */     public final void writeText(XMLSerializer w, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/*  164 */       w.text(print(o), fieldName);
/*      */     }
/*      */ 
/*      */     public final void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/*  168 */       w.leafElement(tagName, print(o), fieldName);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract class StringImpl<T> extends RuntimeBuiltinLeafInfoImpl<T>
/*      */   {
/*      */     protected StringImpl(Class type, QName[] typeNames)
/*      */     {
/*  139 */       super(typeNames, null);
/*      */     }
/*      */ 
/*      */     public abstract String print(T paramT) throws AccessorException;
/*      */ 
/*      */     public void writeText(XMLSerializer w, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/*  145 */       w.text(print(o), fieldName);
/*      */     }
/*      */ 
/*      */     public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/*  149 */       w.leafElement(tagName, print(o), fieldName);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class StringImplImpl extends RuntimeBuiltinLeafInfoImpl.StringImpl<String>
/*      */   {
/*      */     public StringImplImpl(Class type, QName[] typeNames)
/*      */     {
/* 1014 */       super(typeNames);
/*      */     }
/*      */ 
/*      */     public String parse(CharSequence text) {
/* 1018 */       return text.toString();
/*      */     }
/*      */ 
/*      */     public String print(String s) {
/* 1022 */       return s;
/*      */     }
/*      */ 
/*      */     public final void writeText(XMLSerializer w, String o, String fieldName) throws IOException, SAXException, XMLStreamException
/*      */     {
/* 1027 */       w.text(o, fieldName);
/*      */     }
/*      */ 
/*      */     public final void writeLeafElement(XMLSerializer w, Name tagName, String o, String fieldName) throws IOException, SAXException, XMLStreamException
/*      */     {
/* 1032 */       w.leafElement(tagName, o, fieldName);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class UUIDImpl extends RuntimeBuiltinLeafInfoImpl.StringImpl<UUID>
/*      */   {
/*      */     public UUIDImpl()
/*      */     {
/*  993 */       super(new QName[] { RuntimeBuiltinLeafInfoImpl.createXS("string") });
/*      */     }
/*      */ 
/*      */     public UUID parse(CharSequence text) throws SAXException {
/*  997 */       TODO.checkSpec("JSR222 Issue #42");
/*      */       try {
/*  999 */         return UUID.fromString(WhiteSpaceProcessor.trim(text).toString());
/*      */       } catch (IllegalArgumentException e) {
/* 1001 */         UnmarshallingContext.getInstance().handleError(e);
/* 1002 */       }return null;
/*      */     }
/*      */ 
/*      */     public String print(UUID v)
/*      */     {
/* 1007 */       return v.toString();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl
 * JD-Core Version:    0.6.2
 */