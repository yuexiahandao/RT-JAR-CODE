/*      */ package com.sun.org.apache.xalan.internal.xslt;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*      */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*      */ import java.io.File;
/*      */ import java.io.FileWriter;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.Attributes;
/*      */ 
/*      */ public class EnvironmentCheck
/*      */ {
/*      */   public static final String ERROR = "ERROR.";
/*      */   public static final String WARNING = "WARNING.";
/*      */   public static final String ERROR_FOUND = "At least one error was found!";
/*      */   public static final String VERSION = "version.";
/*      */   public static final String FOUNDCLASSES = "foundclasses.";
/*      */   public static final String CLASS_PRESENT = "present-unknown-version";
/*      */   public static final String CLASS_NOTPRESENT = "not-present";
/*  333 */   public String[] jarNames = { "xalan.jar", "xalansamples.jar", "xalanj1compat.jar", "xalanservlet.jar", "serializer.jar", "xerces.jar", "xercesImpl.jar", "testxsl.jar", "crimson.jar", "lotusxsl.jar", "jaxp.jar", "parser.jar", "dom.jar", "sax.jar", "xml.jar", "xml-apis.jar", "xsltc.jar" };
/*      */ 
/* 1191 */   private static Hashtable jarVersions = new Hashtable();
/*      */ 
/* 1293 */   protected PrintWriter outWriter = new PrintWriter(System.out, true);
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/*  109 */     PrintWriter sendOutputTo = new PrintWriter(System.out, true);
/*      */ 
/*  112 */     for (int i = 0; i < args.length; i++)
/*      */     {
/*  114 */       if ("-out".equalsIgnoreCase(args[i]))
/*      */       {
/*  116 */         i++;
/*      */ 
/*  118 */         if (i < args.length)
/*      */         {
/*      */           try
/*      */           {
/*  122 */             sendOutputTo = new PrintWriter(new FileWriter(args[i], true));
/*      */           }
/*      */           catch (Exception e)
/*      */           {
/*  126 */             System.err.println("# WARNING: -out " + args[i] + " threw " + e.toString());
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  132 */           System.err.println("# WARNING: -out argument should have a filename, output sent to console");
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  138 */     EnvironmentCheck app = new EnvironmentCheck();
/*  139 */     app.checkEnvironment(sendOutputTo);
/*      */   }
/*      */ 
/*      */   public boolean checkEnvironment(PrintWriter pw)
/*      */   {
/*  170 */     if (null != pw) {
/*  171 */       this.outWriter = pw;
/*      */     }
/*      */ 
/*  174 */     Hashtable hash = getEnvironmentHash();
/*      */ 
/*  177 */     boolean environmentHasErrors = writeEnvironmentReport(hash);
/*      */ 
/*  179 */     if (environmentHasErrors)
/*      */     {
/*  183 */       logMsg("# WARNING: Potential problems found in your environment!");
/*  184 */       logMsg("#    Check any 'ERROR' items above against the Xalan FAQs");
/*  185 */       logMsg("#    to correct potential problems with your classes/jars");
/*  186 */       logMsg("#    http://xml.apache.org/xalan-j/faq.html");
/*  187 */       if (null != this.outWriter)
/*  188 */         this.outWriter.flush();
/*  189 */       return false;
/*      */     }
/*      */ 
/*  193 */     logMsg("# YAHOO! Your environment seems to be OK.");
/*  194 */     if (null != this.outWriter)
/*  195 */       this.outWriter.flush();
/*  196 */     return true;
/*      */   }
/*      */ 
/*      */   public Hashtable getEnvironmentHash()
/*      */   {
/*  223 */     Hashtable hash = new Hashtable();
/*      */ 
/*  228 */     checkJAXPVersion(hash);
/*  229 */     checkProcessorVersion(hash);
/*  230 */     checkParserVersion(hash);
/*  231 */     checkAntVersion(hash);
/*  232 */     if (!checkDOML3(hash)) {
/*  233 */       checkDOMVersion(hash);
/*      */     }
/*  235 */     checkSAXVersion(hash);
/*  236 */     checkSystemProperties(hash);
/*      */ 
/*  238 */     return hash;
/*      */   }
/*      */ 
/*      */   protected boolean writeEnvironmentReport(Hashtable h)
/*      */   {
/*  258 */     if (null == h)
/*      */     {
/*  260 */       logMsg("# ERROR: writeEnvironmentReport called with null Hashtable");
/*  261 */       return false;
/*      */     }
/*      */ 
/*  264 */     boolean errors = false;
/*      */ 
/*  266 */     logMsg("#---- BEGIN writeEnvironmentReport($Revision: 1.10 $): Useful stuff found: ----");
/*      */ 
/*  270 */     Enumeration keys = h.keys();
/*  271 */     while (keys.hasMoreElements())
/*      */     {
/*  275 */       Object key = keys.nextElement();
/*  276 */       String keyStr = (String)key;
/*      */       try
/*      */       {
/*  280 */         if (keyStr.startsWith("foundclasses."))
/*      */         {
/*  282 */           Vector v = (Vector)h.get(keyStr);
/*  283 */           errors |= logFoundJars(v, keyStr);
/*      */         }
/*      */         else
/*      */         {
/*  292 */           if (keyStr.startsWith("ERROR."))
/*      */           {
/*  294 */             errors = true;
/*      */           }
/*  296 */           logMsg(keyStr + "=" + h.get(keyStr));
/*      */         }
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  301 */         logMsg("Reading-" + key + "= threw: " + e.toString());
/*      */       }
/*      */     }
/*      */ 
/*  305 */     logMsg("#----- END writeEnvironmentReport: Useful properties found: -----");
/*      */ 
/*  308 */     return errors;
/*      */   }
/*      */ 
/*      */   protected boolean logFoundJars(Vector v, String desc)
/*      */   {
/*  363 */     if ((null == v) || (v.size() < 1)) {
/*  364 */       return false;
/*      */     }
/*  366 */     boolean errors = false;
/*      */ 
/*  368 */     logMsg("#---- BEGIN Listing XML-related jars in: " + desc + " ----");
/*      */ 
/*  370 */     for (int i = 0; i < v.size(); i++)
/*      */     {
/*  372 */       Hashtable subhash = (Hashtable)v.elementAt(i);
/*      */ 
/*  374 */       Enumeration keys = subhash.keys();
/*  375 */       while (keys.hasMoreElements())
/*      */       {
/*  379 */         Object key = keys.nextElement();
/*  380 */         String keyStr = (String)key;
/*      */         try
/*      */         {
/*  383 */           if (keyStr.startsWith("ERROR."))
/*      */           {
/*  385 */             errors = true;
/*      */           }
/*  387 */           logMsg(keyStr + "=" + subhash.get(keyStr));
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/*  392 */           errors = true;
/*  393 */           logMsg("Reading-" + key + "= threw: " + e.toString());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  398 */     logMsg("#----- END Listing XML-related jars in: " + desc + " -----");
/*      */ 
/*  400 */     return errors;
/*      */   }
/*      */ 
/*      */   public void appendEnvironmentReport(Node container, Document factory, Hashtable h)
/*      */   {
/*  418 */     if ((null == container) || (null == factory))
/*      */     {
/*  420 */       return;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  425 */       Element envCheckNode = factory.createElement("EnvironmentCheck");
/*  426 */       envCheckNode.setAttribute("version", "$Revision: 1.10 $");
/*  427 */       container.appendChild(envCheckNode);
/*      */ 
/*  429 */       if (null == h)
/*      */       {
/*  431 */         Element statusNode = factory.createElement("status");
/*  432 */         statusNode.setAttribute("result", "ERROR");
/*  433 */         statusNode.appendChild(factory.createTextNode("appendEnvironmentReport called with null Hashtable!"));
/*  434 */         envCheckNode.appendChild(statusNode);
/*  435 */         return;
/*      */       }
/*      */ 
/*  438 */       boolean errors = false;
/*      */ 
/*  440 */       Element hashNode = factory.createElement("environment");
/*  441 */       envCheckNode.appendChild(hashNode);
/*      */ 
/*  443 */       Enumeration keys = h.keys();
/*  444 */       while (keys.hasMoreElements())
/*      */       {
/*  448 */         Object key = keys.nextElement();
/*  449 */         String keyStr = (String)key;
/*      */         try
/*      */         {
/*  453 */           if (keyStr.startsWith("foundclasses."))
/*      */           {
/*  455 */             Vector v = (Vector)h.get(keyStr);
/*      */ 
/*  457 */             errors |= appendFoundJars(hashNode, factory, v, keyStr);
/*      */           }
/*      */           else
/*      */           {
/*  466 */             if (keyStr.startsWith("ERROR."))
/*      */             {
/*  468 */               errors = true;
/*      */             }
/*  470 */             Element node = factory.createElement("item");
/*  471 */             node.setAttribute("key", keyStr);
/*  472 */             node.appendChild(factory.createTextNode((String)h.get(keyStr)));
/*  473 */             hashNode.appendChild(node);
/*      */           }
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/*  478 */           errors = true;
/*  479 */           Element node = factory.createElement("item");
/*  480 */           node.setAttribute("key", keyStr);
/*  481 */           node.appendChild(factory.createTextNode("ERROR. Reading " + key + " threw: " + e.toString()));
/*  482 */           hashNode.appendChild(node);
/*      */         }
/*      */       }
/*      */ 
/*  486 */       Element statusNode = factory.createElement("status");
/*  487 */       statusNode.setAttribute("result", errors ? "ERROR" : "OK");
/*  488 */       envCheckNode.appendChild(statusNode);
/*      */     }
/*      */     catch (Exception e2)
/*      */     {
/*  492 */       System.err.println("appendEnvironmentReport threw: " + e2.toString());
/*  493 */       e2.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean appendFoundJars(Node container, Document factory, Vector v, String desc)
/*      */   {
/*  516 */     if ((null == v) || (v.size() < 1)) {
/*  517 */       return false;
/*      */     }
/*  519 */     boolean errors = false;
/*      */ 
/*  521 */     for (int i = 0; i < v.size(); i++)
/*      */     {
/*  523 */       Hashtable subhash = (Hashtable)v.elementAt(i);
/*      */ 
/*  525 */       Enumeration keys = subhash.keys();
/*  526 */       while (keys.hasMoreElements())
/*      */       {
/*  530 */         Object key = keys.nextElement();
/*      */         try
/*      */         {
/*  533 */           String keyStr = (String)key;
/*  534 */           if (keyStr.startsWith("ERROR."))
/*      */           {
/*  536 */             errors = true;
/*      */           }
/*  538 */           Element node = factory.createElement("foundJar");
/*  539 */           node.setAttribute("name", keyStr.substring(0, keyStr.indexOf("-")));
/*  540 */           node.setAttribute("desc", keyStr.substring(keyStr.indexOf("-") + 1));
/*  541 */           node.appendChild(factory.createTextNode((String)subhash.get(keyStr)));
/*  542 */           container.appendChild(node);
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/*  546 */           errors = true;
/*  547 */           Element node = factory.createElement("foundJar");
/*  548 */           node.appendChild(factory.createTextNode("ERROR. Reading " + key + " threw: " + e.toString()));
/*  549 */           container.appendChild(node);
/*      */         }
/*      */       }
/*      */     }
/*  553 */     return errors;
/*      */   }
/*      */ 
/*      */   protected void checkSystemProperties(Hashtable h)
/*      */   {
/*  572 */     if (null == h) {
/*  573 */       h = new Hashtable();
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  578 */       String javaVersion = SecuritySupport.getSystemProperty("java.version");
/*      */ 
/*  580 */       h.put("java.version", javaVersion);
/*      */     }
/*      */     catch (SecurityException se)
/*      */     {
/*  586 */       h.put("java.version", "WARNING: SecurityException thrown accessing system version properties");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  597 */       String cp = SecuritySupport.getSystemProperty("java.class.path");
/*      */ 
/*  599 */       h.put("java.class.path", cp);
/*      */ 
/*  601 */       Vector classpathJars = checkPathForJars(cp, this.jarNames);
/*      */ 
/*  603 */       if (null != classpathJars) {
/*  604 */         h.put("foundclasses.java.class.path", classpathJars);
/*      */       }
/*      */ 
/*  607 */       String othercp = SecuritySupport.getSystemProperty("sun.boot.class.path");
/*      */ 
/*  609 */       if (null != othercp)
/*      */       {
/*  611 */         h.put("sun.boot.class.path", othercp);
/*      */ 
/*  613 */         classpathJars = checkPathForJars(othercp, this.jarNames);
/*      */ 
/*  615 */         if (null != classpathJars) {
/*  616 */           h.put("foundclasses.sun.boot.class.path", classpathJars);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  621 */       othercp = SecuritySupport.getSystemProperty("java.ext.dirs");
/*      */ 
/*  623 */       if (null != othercp)
/*      */       {
/*  625 */         h.put("java.ext.dirs", othercp);
/*      */ 
/*  627 */         classpathJars = checkPathForJars(othercp, this.jarNames);
/*      */ 
/*  629 */         if (null != classpathJars) {
/*  630 */           h.put("foundclasses.java.ext.dirs", classpathJars);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SecurityException se2)
/*      */     {
/*  640 */       h.put("java.class.path", "WARNING: SecurityException thrown accessing system classpath properties");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Vector checkPathForJars(String cp, String[] jars)
/*      */   {
/*  666 */     if ((null == cp) || (null == jars) || (0 == cp.length()) || (0 == jars.length))
/*      */     {
/*  668 */       return null;
/*      */     }
/*  670 */     Vector v = new Vector();
/*  671 */     StringTokenizer st = new StringTokenizer(cp, File.pathSeparator);
/*      */ 
/*  673 */     while (st.hasMoreTokens())
/*      */     {
/*  677 */       String filename = st.nextToken();
/*      */ 
/*  679 */       for (int i = 0; i < jars.length; i++)
/*      */       {
/*  681 */         if (filename.indexOf(jars[i]) > -1)
/*      */         {
/*  683 */           File f = new File(filename);
/*      */ 
/*  685 */           if (f.exists())
/*      */           {
/*      */             try
/*      */             {
/*  692 */               Hashtable h = new Hashtable(2);
/*      */ 
/*  694 */               h.put(jars[i] + "-path", f.getAbsolutePath());
/*      */ 
/*  701 */               if (!"xalan.jar".equalsIgnoreCase(jars[i])) {
/*  702 */                 h.put(jars[i] + "-apparent.version", getApparentVersion(jars[i], f.length()));
/*      */               }
/*      */ 
/*  705 */               v.addElement(h);
/*      */             }
/*      */             catch (Exception e)
/*      */             {
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  715 */             Hashtable h = new Hashtable(2);
/*      */ 
/*  717 */             h.put(jars[i] + "-path", "WARNING. Classpath entry: " + filename + " does not exist");
/*      */ 
/*  719 */             h.put(jars[i] + "-apparent.version", "not-present");
/*  720 */             v.addElement(h);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  726 */     return v;
/*      */   }
/*      */ 
/*      */   protected String getApparentVersion(String jarName, long jarSize)
/*      */   {
/*  752 */     String foundSize = (String)jarVersions.get(new Long(jarSize));
/*      */ 
/*  754 */     if ((null != foundSize) && (foundSize.startsWith(jarName)))
/*      */     {
/*  756 */       return foundSize;
/*      */     }
/*      */ 
/*  760 */     if (("xerces.jar".equalsIgnoreCase(jarName)) || ("xercesImpl.jar".equalsIgnoreCase(jarName)))
/*      */     {
/*  768 */       return jarName + " " + "WARNING." + "present-unknown-version";
/*      */     }
/*      */ 
/*  774 */     return jarName + " " + "present-unknown-version";
/*      */   }
/*      */ 
/*      */   protected void checkJAXPVersion(Hashtable h)
/*      */   {
/*  791 */     if (null == h) {
/*  792 */       h = new Hashtable();
/*      */     }
/*  794 */     Class clazz = null;
/*      */     try
/*      */     {
/*  798 */       String JAXP1_CLASS = "javax.xml.stream.XMLStreamConstants";
/*      */ 
/*  800 */       clazz = ObjectFactory.findProviderClass("javax.xml.stream.XMLStreamConstants", true);
/*      */ 
/*  803 */       h.put("version.JAXP", "1.4");
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  807 */       h.put("ERROR.version.JAXP", "1.3");
/*  808 */       h.put("ERROR.", "At least one error was found!");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkProcessorVersion(Hashtable h)
/*      */   {
/*  822 */     if (null == h) {
/*  823 */       h = new Hashtable();
/*      */     }
/*      */     try
/*      */     {
/*  827 */       String XALAN1_VERSION_CLASS = "com.sun.org.apache.xalan.internal.xslt.XSLProcessorVersion";
/*      */ 
/*  830 */       Class clazz = ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.xslt.XSLProcessorVersion", true);
/*      */ 
/*  833 */       StringBuffer buf = new StringBuffer();
/*  834 */       Field f = clazz.getField("PRODUCT");
/*      */ 
/*  836 */       buf.append(f.get(null));
/*  837 */       buf.append(';');
/*      */ 
/*  839 */       f = clazz.getField("LANGUAGE");
/*      */ 
/*  841 */       buf.append(f.get(null));
/*  842 */       buf.append(';');
/*      */ 
/*  844 */       f = clazz.getField("S_VERSION");
/*      */ 
/*  846 */       buf.append(f.get(null));
/*  847 */       buf.append(';');
/*  848 */       h.put("version.xalan1", buf.toString());
/*      */     }
/*      */     catch (Exception e1)
/*      */     {
/*  852 */       h.put("version.xalan1", "not-present");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  859 */       String XALAN2_VERSION_CLASS = "com.sun.org.apache.xalan.internal.processor.XSLProcessorVersion";
/*      */ 
/*  862 */       Class clazz = ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.processor.XSLProcessorVersion", true);
/*      */ 
/*  865 */       StringBuffer buf = new StringBuffer();
/*  866 */       Field f = clazz.getField("S_VERSION");
/*  867 */       buf.append(f.get(null));
/*      */ 
/*  869 */       h.put("version.xalan2x", buf.toString());
/*      */     }
/*      */     catch (Exception e2)
/*      */     {
/*  873 */       h.put("version.xalan2x", "not-present");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  878 */       String XALAN2_2_VERSION_CLASS = "com.sun.org.apache.xalan.internal.Version";
/*      */ 
/*  880 */       String XALAN2_2_VERSION_METHOD = "getVersion";
/*  881 */       Class[] noArgs = new Class[0];
/*      */ 
/*  883 */       Class clazz = ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.Version", true);
/*      */ 
/*  885 */       Method method = clazz.getMethod("getVersion", noArgs);
/*  886 */       Object returnValue = method.invoke(null, new Object[0]);
/*      */ 
/*  888 */       h.put("version.xalan2_2", (String)returnValue);
/*      */     }
/*      */     catch (Exception e2)
/*      */     {
/*  892 */       h.put("version.xalan2_2", "not-present");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkParserVersion(Hashtable h)
/*      */   {
/*  908 */     if (null == h) {
/*  909 */       h = new Hashtable();
/*      */     }
/*      */     try
/*      */     {
/*  913 */       String XERCES1_VERSION_CLASS = "com.sun.org.apache.xerces.internal.framework.Version";
/*      */ 
/*  915 */       Class clazz = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.framework.Version", true);
/*      */ 
/*  918 */       Field f = clazz.getField("fVersion");
/*  919 */       String parserVersion = (String)f.get(null);
/*      */ 
/*  921 */       h.put("version.xerces1", parserVersion);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  925 */       h.put("version.xerces1", "not-present");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  931 */       String XERCES2_VERSION_CLASS = "com.sun.org.apache.xerces.internal.impl.Version";
/*      */ 
/*  933 */       Class clazz = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.impl.Version", true);
/*      */ 
/*  936 */       Field f = clazz.getField("fVersion");
/*  937 */       String parserVersion = (String)f.get(null);
/*      */ 
/*  939 */       h.put("version.xerces2", parserVersion);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  943 */       h.put("version.xerces2", "not-present");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  948 */       String CRIMSON_CLASS = "org.apache.crimson.parser.Parser2";
/*      */ 
/*  950 */       Class clazz = ObjectFactory.findProviderClass("org.apache.crimson.parser.Parser2", true);
/*      */ 
/*  953 */       h.put("version.crimson", "present-unknown-version");
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  957 */       h.put("version.crimson", "not-present");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkAntVersion(Hashtable h)
/*      */   {
/*  969 */     if (null == h) {
/*  970 */       h = new Hashtable();
/*      */     }
/*      */     try
/*      */     {
/*  974 */       String ANT_VERSION_CLASS = "org.apache.tools.ant.Main";
/*  975 */       String ANT_VERSION_METHOD = "getAntVersion";
/*  976 */       Class[] noArgs = new Class[0];
/*      */ 
/*  978 */       Class clazz = ObjectFactory.findProviderClass("org.apache.tools.ant.Main", true);
/*      */ 
/*  980 */       Method method = clazz.getMethod("getAntVersion", noArgs);
/*  981 */       Object returnValue = method.invoke(null, new Object[0]);
/*      */ 
/*  983 */       h.put("version.ant", (String)returnValue);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  987 */       h.put("version.ant", "not-present");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean checkDOML3(Hashtable h)
/*      */   {
/*  999 */     if (null == h) {
/* 1000 */       h = new Hashtable();
/*      */     }
/* 1002 */     String DOM_CLASS = "org.w3c.dom.Document";
/* 1003 */     String DOM_LEVEL3_METHOD = "getDoctype";
/*      */     try
/*      */     {
/* 1007 */       Class clazz = ObjectFactory.findProviderClass("org.w3c.dom.Document", true);
/*      */ 
/* 1009 */       Method method = clazz.getMethod("getDoctype", (Class[])null);
/*      */ 
/* 1013 */       h.put("version.DOM", "3.0");
/* 1014 */       return true;
/*      */     }
/*      */     catch (Exception e) {
/*      */     }
/* 1018 */     return false;
/*      */   }
/*      */ 
/*      */   protected void checkDOMVersion(Hashtable h)
/*      */   {
/* 1034 */     if (null == h) {
/* 1035 */       h = new Hashtable();
/*      */     }
/* 1037 */     String DOM_LEVEL2_CLASS = "org.w3c.dom.Document";
/* 1038 */     String DOM_LEVEL2_METHOD = "createElementNS";
/* 1039 */     String DOM_LEVEL3_METHOD = "getDoctype";
/* 1040 */     String DOM_LEVEL2WD_CLASS = "org.w3c.dom.Node";
/* 1041 */     String DOM_LEVEL2WD_METHOD = "supported";
/* 1042 */     String DOM_LEVEL2FD_CLASS = "org.w3c.dom.Node";
/* 1043 */     String DOM_LEVEL2FD_METHOD = "isSupported";
/* 1044 */     Class[] twoStringArgs = { String.class, String.class };
/*      */     try
/*      */     {
/* 1049 */       Class clazz = ObjectFactory.findProviderClass("org.w3c.dom.Document", true);
/*      */ 
/* 1051 */       Method method = clazz.getMethod("createElementNS", twoStringArgs);
/*      */ 
/* 1055 */       h.put("version.DOM", "2.0");
/*      */       try
/*      */       {
/* 1061 */         clazz = ObjectFactory.findProviderClass("org.w3c.dom.Node", true);
/*      */ 
/* 1063 */         method = clazz.getMethod("supported", twoStringArgs);
/*      */ 
/* 1065 */         h.put("ERROR.version.DOM.draftlevel", "2.0wd");
/* 1066 */         h.put("ERROR.", "At least one error was found!");
/*      */       }
/*      */       catch (Exception e2)
/*      */       {
/*      */         try
/*      */         {
/* 1073 */           clazz = ObjectFactory.findProviderClass("org.w3c.dom.Node", true);
/*      */ 
/* 1075 */           method = clazz.getMethod("isSupported", twoStringArgs);
/*      */ 
/* 1077 */           h.put("version.DOM.draftlevel", "2.0fd");
/*      */         }
/*      */         catch (Exception e3)
/*      */         {
/* 1081 */           h.put("ERROR.version.DOM.draftlevel", "2.0unknown");
/* 1082 */           h.put("ERROR.", "At least one error was found!");
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1088 */       h.put("ERROR.version.DOM", "ERROR attempting to load DOM level 2 class: " + e.toString());
/*      */ 
/* 1090 */       h.put("ERROR.", "At least one error was found!");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkSAXVersion(Hashtable h)
/*      */   {
/* 1110 */     if (null == h) {
/* 1111 */       h = new Hashtable();
/*      */     }
/* 1113 */     String SAX_VERSION1_CLASS = "org.xml.sax.Parser";
/* 1114 */     String SAX_VERSION1_METHOD = "parse";
/* 1115 */     String SAX_VERSION2_CLASS = "org.xml.sax.XMLReader";
/* 1116 */     String SAX_VERSION2_METHOD = "parse";
/* 1117 */     String SAX_VERSION2BETA_CLASSNF = "org.xml.sax.helpers.AttributesImpl";
/* 1118 */     String SAX_VERSION2BETA_METHODNF = "setAttributes";
/* 1119 */     Class[] oneStringArg = { String.class };
/*      */ 
/* 1121 */     Class[] attributesArg = { Attributes.class };
/*      */     try
/*      */     {
/* 1127 */       Class clazz = ObjectFactory.findProviderClass("org.xml.sax.helpers.AttributesImpl", true);
/*      */ 
/* 1129 */       Method method = clazz.getMethod("setAttributes", attributesArg);
/*      */ 
/* 1133 */       h.put("version.SAX", "2.0");
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1138 */       h.put("ERROR.version.SAX", "ERROR attempting to load SAX version 2 class: " + e.toString());
/*      */ 
/* 1140 */       h.put("ERROR.", "At least one error was found!");
/*      */       try
/*      */       {
/* 1144 */         Class clazz = ObjectFactory.findProviderClass("org.xml.sax.XMLReader", true);
/*      */ 
/* 1146 */         Method method = clazz.getMethod("parse", oneStringArg);
/*      */ 
/* 1151 */         h.put("version.SAX-backlevel", "2.0beta2-or-earlier");
/*      */       }
/*      */       catch (Exception e2)
/*      */       {
/* 1156 */         h.put("ERROR.version.SAX", "ERROR attempting to load SAX version 2 class: " + e.toString());
/*      */ 
/* 1158 */         h.put("ERROR.", "At least one error was found!");
/*      */         try
/*      */         {
/* 1162 */           Class clazz = ObjectFactory.findProviderClass("org.xml.sax.Parser", true);
/*      */ 
/* 1164 */           Method method = clazz.getMethod("parse", oneStringArg);
/*      */ 
/* 1169 */           h.put("version.SAX-backlevel", "1.0");
/*      */         }
/*      */         catch (Exception e3)
/*      */         {
/* 1175 */           h.put("ERROR.version.SAX-backlevel", "ERROR attempting to load SAX version 1 class: " + e3.toString());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void logMsg(String s)
/*      */   {
/* 1301 */     this.outWriter.println(s);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1202 */     jarVersions.put(new Long(857192L), "xalan.jar from xalan-j_1_1");
/* 1203 */     jarVersions.put(new Long(440237L), "xalan.jar from xalan-j_1_2");
/* 1204 */     jarVersions.put(new Long(436094L), "xalan.jar from xalan-j_1_2_1");
/* 1205 */     jarVersions.put(new Long(426249L), "xalan.jar from xalan-j_1_2_2");
/* 1206 */     jarVersions.put(new Long(702536L), "xalan.jar from xalan-j_2_0_0");
/* 1207 */     jarVersions.put(new Long(720930L), "xalan.jar from xalan-j_2_0_1");
/* 1208 */     jarVersions.put(new Long(732330L), "xalan.jar from xalan-j_2_1_0");
/* 1209 */     jarVersions.put(new Long(872241L), "xalan.jar from xalan-j_2_2_D10");
/* 1210 */     jarVersions.put(new Long(882739L), "xalan.jar from xalan-j_2_2_D11");
/* 1211 */     jarVersions.put(new Long(923866L), "xalan.jar from xalan-j_2_2_0");
/* 1212 */     jarVersions.put(new Long(905872L), "xalan.jar from xalan-j_2_3_D1");
/* 1213 */     jarVersions.put(new Long(906122L), "xalan.jar from xalan-j_2_3_0");
/* 1214 */     jarVersions.put(new Long(906248L), "xalan.jar from xalan-j_2_3_1");
/* 1215 */     jarVersions.put(new Long(983377L), "xalan.jar from xalan-j_2_4_D1");
/* 1216 */     jarVersions.put(new Long(997276L), "xalan.jar from xalan-j_2_4_0");
/* 1217 */     jarVersions.put(new Long(1031036L), "xalan.jar from xalan-j_2_4_1");
/*      */ 
/* 1220 */     jarVersions.put(new Long(596540L), "xsltc.jar from xalan-j_2_2_0");
/* 1221 */     jarVersions.put(new Long(590247L), "xsltc.jar from xalan-j_2_3_D1");
/* 1222 */     jarVersions.put(new Long(589914L), "xsltc.jar from xalan-j_2_3_0");
/* 1223 */     jarVersions.put(new Long(589915L), "xsltc.jar from xalan-j_2_3_1");
/* 1224 */     jarVersions.put(new Long(1306667L), "xsltc.jar from xalan-j_2_4_D1");
/* 1225 */     jarVersions.put(new Long(1328227L), "xsltc.jar from xalan-j_2_4_0");
/* 1226 */     jarVersions.put(new Long(1344009L), "xsltc.jar from xalan-j_2_4_1");
/* 1227 */     jarVersions.put(new Long(1348361L), "xsltc.jar from xalan-j_2_5_D1");
/*      */ 
/* 1230 */     jarVersions.put(new Long(1268634L), "xsltc.jar-bundled from xalan-j_2_3_0");
/*      */ 
/* 1232 */     jarVersions.put(new Long(100196L), "xml-apis.jar from xalan-j_2_2_0 or xalan-j_2_3_D1");
/* 1233 */     jarVersions.put(new Long(108484L), "xml-apis.jar from xalan-j_2_3_0, or xalan-j_2_3_1 from xml-commons-1.0.b2");
/* 1234 */     jarVersions.put(new Long(109049L), "xml-apis.jar from xalan-j_2_4_0 from xml-commons RIVERCOURT1 branch");
/* 1235 */     jarVersions.put(new Long(113749L), "xml-apis.jar from xalan-j_2_4_1 from factoryfinder-build of xml-commons RIVERCOURT1");
/* 1236 */     jarVersions.put(new Long(124704L), "xml-apis.jar from tck-jaxp-1_2_0 branch of xml-commons");
/* 1237 */     jarVersions.put(new Long(124724L), "xml-apis.jar from tck-jaxp-1_2_0 branch of xml-commons, tag: xml-commons-external_1_2_01");
/* 1238 */     jarVersions.put(new Long(194205L), "xml-apis.jar from head branch of xml-commons, tag: xml-commons-external_1_3_02");
/*      */ 
/* 1242 */     jarVersions.put(new Long(424490L), "xalan.jar from Xerces Tools releases - ERROR:DO NOT USE!");
/*      */ 
/* 1244 */     jarVersions.put(new Long(1591855L), "xerces.jar from xalan-j_1_1 from xerces-1...");
/* 1245 */     jarVersions.put(new Long(1498679L), "xerces.jar from xalan-j_1_2 from xerces-1_2_0.bin");
/* 1246 */     jarVersions.put(new Long(1484896L), "xerces.jar from xalan-j_1_2_1 from xerces-1_2_1.bin");
/* 1247 */     jarVersions.put(new Long(804460L), "xerces.jar from xalan-j_1_2_2 from xerces-1_2_2.bin");
/* 1248 */     jarVersions.put(new Long(1499244L), "xerces.jar from xalan-j_2_0_0 from xerces-1_2_3.bin");
/* 1249 */     jarVersions.put(new Long(1605266L), "xerces.jar from xalan-j_2_0_1 from xerces-1_3_0.bin");
/* 1250 */     jarVersions.put(new Long(904030L), "xerces.jar from xalan-j_2_1_0 from xerces-1_4.bin");
/* 1251 */     jarVersions.put(new Long(904030L), "xerces.jar from xerces-1_4_0.bin");
/* 1252 */     jarVersions.put(new Long(1802885L), "xerces.jar from xerces-1_4_2.bin");
/* 1253 */     jarVersions.put(new Long(1734594L), "xerces.jar from Xerces-J-bin.2.0.0.beta3");
/* 1254 */     jarVersions.put(new Long(1808883L), "xerces.jar from xalan-j_2_2_D10,D11,D12 or xerces-1_4_3.bin");
/* 1255 */     jarVersions.put(new Long(1812019L), "xerces.jar from xalan-j_2_2_0");
/* 1256 */     jarVersions.put(new Long(1720292L), "xercesImpl.jar from xalan-j_2_3_D1");
/* 1257 */     jarVersions.put(new Long(1730053L), "xercesImpl.jar from xalan-j_2_3_0 or xalan-j_2_3_1 from xerces-2_0_0");
/* 1258 */     jarVersions.put(new Long(1728861L), "xercesImpl.jar from xalan-j_2_4_D1 from xerces-2_0_1");
/* 1259 */     jarVersions.put(new Long(972027L), "xercesImpl.jar from xalan-j_2_4_0 from xerces-2_1");
/* 1260 */     jarVersions.put(new Long(831587L), "xercesImpl.jar from xalan-j_2_4_1 from xerces-2_2");
/* 1261 */     jarVersions.put(new Long(891817L), "xercesImpl.jar from xalan-j_2_5_D1 from xerces-2_3");
/* 1262 */     jarVersions.put(new Long(895924L), "xercesImpl.jar from xerces-2_4");
/* 1263 */     jarVersions.put(new Long(1010806L), "xercesImpl.jar from Xerces-J-bin.2.6.2");
/* 1264 */     jarVersions.put(new Long(1203860L), "xercesImpl.jar from Xerces-J-bin.2.7.1");
/*      */ 
/* 1266 */     jarVersions.put(new Long(37485L), "xalanj1compat.jar from xalan-j_2_0_0");
/* 1267 */     jarVersions.put(new Long(38100L), "xalanj1compat.jar from xalan-j_2_0_1");
/*      */ 
/* 1269 */     jarVersions.put(new Long(18779L), "xalanservlet.jar from xalan-j_2_0_0");
/* 1270 */     jarVersions.put(new Long(21453L), "xalanservlet.jar from xalan-j_2_0_1");
/* 1271 */     jarVersions.put(new Long(24826L), "xalanservlet.jar from xalan-j_2_3_1 or xalan-j_2_4_1");
/* 1272 */     jarVersions.put(new Long(24831L), "xalanservlet.jar from xalan-j_2_4_1");
/*      */ 
/* 1276 */     jarVersions.put(new Long(5618L), "jaxp.jar from jaxp1.0.1");
/* 1277 */     jarVersions.put(new Long(136133L), "parser.jar from jaxp1.0.1");
/* 1278 */     jarVersions.put(new Long(28404L), "jaxp.jar from jaxp-1.1");
/* 1279 */     jarVersions.put(new Long(187162L), "crimson.jar from jaxp-1.1");
/* 1280 */     jarVersions.put(new Long(801714L), "xalan.jar from jaxp-1.1");
/* 1281 */     jarVersions.put(new Long(196399L), "crimson.jar from crimson-1.1.1");
/* 1282 */     jarVersions.put(new Long(33323L), "jaxp.jar from crimson-1.1.1 or jakarta-ant-1.4.1b1");
/* 1283 */     jarVersions.put(new Long(152717L), "crimson.jar from crimson-1.1.2beta2");
/* 1284 */     jarVersions.put(new Long(88143L), "xml-apis.jar from crimson-1.1.2beta2");
/* 1285 */     jarVersions.put(new Long(206384L), "crimson.jar from crimson-1.1.3 or jakarta-ant-1.4.1b1");
/*      */ 
/* 1288 */     jarVersions.put(new Long(136198L), "parser.jar from jakarta-ant-1.3 or 1.2");
/* 1289 */     jarVersions.put(new Long(5537L), "jaxp.jar from jakarta-ant-1.3 or 1.2");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xslt.EnvironmentCheck
 * JD-Core Version:    0.6.2
 */