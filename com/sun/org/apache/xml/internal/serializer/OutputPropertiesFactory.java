/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Messages;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Utils;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public final class OutputPropertiesFactory
/*     */ {
/*     */   private static final String S_BUILTIN_EXTENSIONS_URL = "http://xml.apache.org/xalan";
/*     */   private static final String S_BUILTIN_OLD_EXTENSIONS_URL = "http://xml.apache.org/xslt";
/*     */   public static final String S_BUILTIN_EXTENSIONS_UNIVERSAL = "{http://xml.apache.org/xalan}";
/*     */   public static final String S_KEY_INDENT_AMOUNT = "{http://xml.apache.org/xalan}indent-amount";
/*     */   public static final String S_KEY_LINE_SEPARATOR = "{http://xml.apache.org/xalan}line-separator";
/*     */   public static final String S_KEY_CONTENT_HANDLER = "{http://xml.apache.org/xalan}content-handler";
/*     */   public static final String S_KEY_ENTITIES = "{http://xml.apache.org/xalan}entities";
/*     */   public static final String S_USE_URL_ESCAPING = "{http://xml.apache.org/xalan}use-url-escaping";
/*     */   public static final String S_OMIT_META_TAG = "{http://xml.apache.org/xalan}omit-meta-tag";
/*     */   public static final String S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL = "{http://xml.apache.org/xslt}";
/* 176 */   public static final int S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL_LEN = "{http://xml.apache.org/xslt}".length();
/*     */   public static final String ORACLE_IS_STANDALONE = "http://www.oracle.com/xml/is-standalone";
/*     */   private static final String S_XSLT_PREFIX = "xslt.output.";
/* 204 */   private static final int S_XSLT_PREFIX_LEN = "xslt.output.".length();
/*     */   private static final String S_XALAN_PREFIX = "org.apache.xslt.";
/* 206 */   private static final int S_XALAN_PREFIX_LEN = "org.apache.xslt.".length();
/*     */ 
/* 209 */   private static Integer m_synch_object = new Integer(1);
/*     */   private static final String PROP_DIR = "com/sun/org/apache/xml/internal/serializer/";
/*     */   private static final String PROP_FILE_XML = "output_xml.properties";
/*     */   private static final String PROP_FILE_TEXT = "output_text.properties";
/*     */   private static final String PROP_FILE_HTML = "output_html.properties";
/*     */   private static final String PROP_FILE_UNKNOWN = "output_unknown.properties";
/* 227 */   private static Properties m_xml_properties = null;
/*     */ 
/* 230 */   private static Properties m_html_properties = null;
/*     */ 
/* 233 */   private static Properties m_text_properties = null;
/*     */ 
/* 236 */   private static Properties m_unknown_properties = null;
/*     */ 
/* 239 */   private static final Class ACCESS_CONTROLLER_CLASS = findAccessControllerClass();
/*     */ 
/*     */   private static Class findAccessControllerClass()
/*     */   {
/*     */     try
/*     */     {
/* 251 */       return Class.forName("java.security.AccessController");
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */ 
/* 259 */     return null;
/*     */   }
/*     */ 
/*     */   public static final Properties getDefaultMethodProperties(String method)
/*     */   {
/* 277 */     String fileName = null;
/* 278 */     Properties defaultProperties = null;
/*     */     try
/*     */     {
/* 283 */       synchronized (m_synch_object)
/*     */       {
/* 285 */         if (null == m_xml_properties)
/*     */         {
/* 287 */           fileName = "output_xml.properties";
/* 288 */           m_xml_properties = loadPropertiesFile(fileName, null);
/*     */         }
/*     */       }
/*     */ 
/* 292 */       if (method.equals("xml"))
/*     */       {
/* 294 */         defaultProperties = m_xml_properties;
/*     */       }
/* 296 */       else if (method.equals("html"))
/*     */       {
/* 298 */         if (null == m_html_properties)
/*     */         {
/* 300 */           fileName = "output_html.properties";
/* 301 */           m_html_properties = loadPropertiesFile(fileName, m_xml_properties);
/*     */         }
/*     */ 
/* 305 */         defaultProperties = m_html_properties;
/*     */       }
/* 307 */       else if (method.equals("text"))
/*     */       {
/* 309 */         if (null == m_text_properties)
/*     */         {
/* 311 */           fileName = "output_text.properties";
/* 312 */           m_text_properties = loadPropertiesFile(fileName, m_xml_properties);
/*     */ 
/* 314 */           if (null == m_text_properties.getProperty("encoding"))
/*     */           {
/* 317 */             String mimeEncoding = Encodings.getMimeEncoding(null);
/* 318 */             m_text_properties.put("encoding", mimeEncoding);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 324 */         defaultProperties = m_text_properties;
/*     */       }
/* 326 */       else if (method.equals(""))
/*     */       {
/* 328 */         if (null == m_unknown_properties)
/*     */         {
/* 330 */           fileName = "output_unknown.properties";
/* 331 */           m_unknown_properties = loadPropertiesFile(fileName, m_xml_properties);
/*     */         }
/*     */ 
/* 335 */         defaultProperties = m_unknown_properties;
/*     */       }
/*     */       else
/*     */       {
/* 340 */         defaultProperties = m_xml_properties;
/*     */       }
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 345 */       throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_METHOD_PROPERTY", new Object[] { fileName, method }), ioe);
/*     */     }
/*     */ 
/* 353 */     return new Properties(defaultProperties);
/*     */   }
/*     */ 
/*     */   private static Properties loadPropertiesFile(String resourceName, Properties defaults)
/*     */     throws IOException
/*     */   {
/* 378 */     Properties props = new Properties(defaults);
/*     */ 
/* 380 */     InputStream is = null;
/* 381 */     BufferedInputStream bis = null;
/*     */     try
/*     */     {
/* 385 */       if (ACCESS_CONTROLLER_CLASS != null)
/*     */       {
/* 387 */         is = (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run()
/*     */           {
/* 391 */             return OutputPropertiesFactory.class.getResourceAsStream(this.val$resourceName);
/*     */           }
/*     */ 
/*     */         });
/*     */       }
/*     */       else
/*     */       {
/* 399 */         is = OutputPropertiesFactory.class.getResourceAsStream(resourceName);
/*     */       }
/*     */ 
/* 403 */       bis = new BufferedInputStream(is);
/* 404 */       props.load(bis);
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 408 */       if (defaults == null)
/*     */       {
/* 410 */         throw ioe;
/*     */       }
/*     */ 
/* 414 */       throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_RESOURCE", new Object[] { resourceName }), ioe);
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/* 425 */       if (defaults == null)
/*     */       {
/* 427 */         throw se;
/*     */       }
/*     */ 
/* 431 */       throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_RESOURCE", new Object[] { resourceName }), se);
/*     */     }
/*     */     finally
/*     */     {
/* 441 */       if (bis != null)
/*     */       {
/* 443 */         bis.close();
/*     */       }
/* 445 */       if (is != null)
/*     */       {
/* 447 */         is.close();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 465 */     Enumeration keys = ((Properties)props.clone()).keys();
/* 466 */     while (keys.hasMoreElements())
/*     */     {
/* 468 */       String key = (String)keys.nextElement();
/*     */ 
/* 472 */       String value = null;
/*     */       try
/*     */       {
/* 475 */         value = SecuritySupport.getSystemProperty(key);
/*     */       }
/*     */       catch (SecurityException se)
/*     */       {
/*     */       }
/*     */ 
/* 481 */       if (value == null) {
/* 482 */         value = (String)props.get(key);
/*     */       }
/* 484 */       String newKey = fixupPropertyString(key, true);
/* 485 */       String newValue = null;
/*     */       try
/*     */       {
/* 488 */         newValue = SecuritySupport.getSystemProperty(newKey);
/*     */       }
/*     */       catch (SecurityException se)
/*     */       {
/*     */       }
/*     */ 
/* 494 */       if (newValue == null)
/* 495 */         newValue = fixupPropertyString(value, false);
/*     */       else {
/* 497 */         newValue = fixupPropertyString(newValue, false);
/*     */       }
/* 499 */       if ((key != newKey) || (value != newValue))
/*     */       {
/* 501 */         props.remove(key);
/* 502 */         props.put(newKey, newValue);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 507 */     return props;
/*     */   }
/*     */ 
/*     */   private static String fixupPropertyString(String s, boolean doClipping)
/*     */   {
/* 520 */     if ((doClipping) && (s.startsWith("xslt.output.")))
/*     */     {
/* 522 */       s = s.substring(S_XSLT_PREFIX_LEN);
/*     */     }
/* 524 */     if (s.startsWith("org.apache.xslt."))
/*     */     {
/* 526 */       s = "{http://xml.apache.org/xalan}" + s.substring(S_XALAN_PREFIX_LEN);
/*     */     }
/*     */     int index;
/* 530 */     if ((index = s.indexOf("\\u003a")) > 0)
/*     */     {
/* 532 */       String temp = s.substring(index + 6);
/* 533 */       s = s.substring(0, index) + ":" + temp;
/*     */     }
/*     */ 
/* 536 */     return s;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory
 * JD-Core Version:    0.6.2
 */