/*      */ package sun.security.provider;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.StreamTokenizer;
/*      */ import java.io.Writer;
/*      */ import java.security.GeneralSecurityException;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.LinkedList;
/*      */ import java.util.ListIterator;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import javax.security.auth.x500.X500Principal;
/*      */ import sun.net.www.ParseUtil;
/*      */ import sun.security.util.Debug;
/*      */ import sun.security.util.PropertyExpander;
/*      */ import sun.security.util.PropertyExpander.ExpandException;
/*      */ import sun.security.util.ResourcesMgr;
/*      */ 
/*      */ public class PolicyParser
/*      */ {
/*      */   public static final String REPLACE_NAME = "PolicyParser.REPLACE_NAME";
/*      */   private static final String EXTDIRS_PROPERTY = "java.ext.dirs";
/*      */   private static final String OLD_EXTDIRS_EXPANSION = "${java.ext.dirs}";
/*      */   static final String EXTDIRS_EXPANSION = "${{java.ext.dirs}}";
/*      */   private Vector<GrantEntry> grantEntries;
/*  104 */   private static final Debug debug = Debug.getInstance("parser", "\t[Policy Parser]");
/*      */   private StreamTokenizer st;
/*      */   private int lookahead;
/*  108 */   private boolean expandProp = false;
/*  109 */   private String keyStoreUrlString = null;
/*  110 */   private String keyStoreType = null;
/*  111 */   private String keyStoreProvider = null;
/*  112 */   private String storePassURL = null;
/*      */ 
/*      */   private String expand(String paramString)
/*      */     throws PropertyExpander.ExpandException
/*      */   {
/*  117 */     return expand(paramString, false);
/*      */   }
/*      */ 
/*      */   private String expand(String paramString, boolean paramBoolean)
/*      */     throws PropertyExpander.ExpandException
/*      */   {
/*  123 */     if (!this.expandProp) {
/*  124 */       return paramString;
/*      */     }
/*  126 */     return PropertyExpander.expand(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   public PolicyParser()
/*      */   {
/*  135 */     this.grantEntries = new Vector();
/*      */   }
/*      */ 
/*      */   public PolicyParser(boolean paramBoolean)
/*      */   {
/*  140 */     this();
/*  141 */     this.expandProp = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void read(Reader paramReader)
/*      */     throws PolicyParser.ParsingException, IOException
/*      */   {
/*  160 */     if (!(paramReader instanceof BufferedReader)) {
/*  161 */       paramReader = new BufferedReader(paramReader);
/*      */     }
/*      */ 
/*  171 */     this.st = new StreamTokenizer(paramReader);
/*      */ 
/*  173 */     this.st.resetSyntax();
/*  174 */     this.st.wordChars(97, 122);
/*  175 */     this.st.wordChars(65, 90);
/*  176 */     this.st.wordChars(46, 46);
/*  177 */     this.st.wordChars(48, 57);
/*  178 */     this.st.wordChars(95, 95);
/*  179 */     this.st.wordChars(36, 36);
/*  180 */     this.st.wordChars(160, 255);
/*  181 */     this.st.whitespaceChars(0, 32);
/*  182 */     this.st.commentChar(47);
/*  183 */     this.st.quoteChar(39);
/*  184 */     this.st.quoteChar(34);
/*  185 */     this.st.lowerCaseMode(false);
/*  186 */     this.st.ordinaryChar(47);
/*  187 */     this.st.slashSlashComments(true);
/*  188 */     this.st.slashStarComments(true);
/*      */ 
/*  199 */     this.lookahead = this.st.nextToken();
/*  200 */     while (this.lookahead != -1) {
/*  201 */       if (peek("grant")) {
/*  202 */         GrantEntry localGrantEntry = parseGrantEntry();
/*      */ 
/*  204 */         if (localGrantEntry != null)
/*  205 */           add(localGrantEntry);
/*  206 */       } else if ((peek("keystore")) && (this.keyStoreUrlString == null))
/*      */       {
/*  209 */         parseKeyStoreEntry();
/*  210 */       } else if ((peek("keystorePasswordURL")) && (this.storePassURL == null))
/*      */       {
/*  213 */         parseStorePassURL();
/*      */       }
/*      */ 
/*  217 */       match(";");
/*      */     }
/*      */ 
/*  220 */     if ((this.keyStoreUrlString == null) && (this.storePassURL != null))
/*  221 */       throw new ParsingException(ResourcesMgr.getString("keystorePasswordURL.can.not.be.specified.without.also.specifying.keystore"));
/*      */   }
/*      */ 
/*      */   public void add(GrantEntry paramGrantEntry)
/*      */   {
/*  228 */     this.grantEntries.addElement(paramGrantEntry);
/*      */   }
/*      */ 
/*      */   public void replace(GrantEntry paramGrantEntry1, GrantEntry paramGrantEntry2)
/*      */   {
/*  233 */     this.grantEntries.setElementAt(paramGrantEntry2, this.grantEntries.indexOf(paramGrantEntry1));
/*      */   }
/*      */ 
/*      */   public boolean remove(GrantEntry paramGrantEntry)
/*      */   {
/*  238 */     return this.grantEntries.removeElement(paramGrantEntry);
/*      */   }
/*      */ 
/*      */   public String getKeyStoreUrl()
/*      */   {
/*      */     try
/*      */     {
/*  247 */       if ((this.keyStoreUrlString != null) && (this.keyStoreUrlString.length() != 0))
/*  248 */         return expand(this.keyStoreUrlString, true).replace(File.separatorChar, '/');
/*      */     }
/*      */     catch (PropertyExpander.ExpandException localExpandException)
/*      */     {
/*  252 */       if (debug != null) {
/*  253 */         debug.println(localExpandException.toString());
/*      */       }
/*  255 */       return null;
/*      */     }
/*  257 */     return null;
/*      */   }
/*      */ 
/*      */   public void setKeyStoreUrl(String paramString) {
/*  261 */     this.keyStoreUrlString = paramString;
/*      */   }
/*      */ 
/*      */   public String getKeyStoreType() {
/*  265 */     return this.keyStoreType;
/*      */   }
/*      */ 
/*      */   public void setKeyStoreType(String paramString) {
/*  269 */     this.keyStoreType = paramString;
/*      */   }
/*      */ 
/*      */   public String getKeyStoreProvider() {
/*  273 */     return this.keyStoreProvider;
/*      */   }
/*      */ 
/*      */   public void setKeyStoreProvider(String paramString) {
/*  277 */     this.keyStoreProvider = paramString;
/*      */   }
/*      */ 
/*      */   public String getStorePassURL() {
/*      */     try {
/*  282 */       if ((this.storePassURL != null) && (this.storePassURL.length() != 0))
/*  283 */         return expand(this.storePassURL, true).replace(File.separatorChar, '/');
/*      */     }
/*      */     catch (PropertyExpander.ExpandException localExpandException)
/*      */     {
/*  287 */       if (debug != null) {
/*  288 */         debug.println(localExpandException.toString());
/*      */       }
/*  290 */       return null;
/*      */     }
/*  292 */     return null;
/*      */   }
/*      */ 
/*      */   public void setStorePassURL(String paramString) {
/*  296 */     this.storePassURL = paramString;
/*      */   }
/*      */ 
/*      */   public Enumeration<GrantEntry> grantElements()
/*      */   {
/*  306 */     return this.grantEntries.elements();
/*      */   }
/*      */ 
/*      */   public void write(Writer paramWriter)
/*      */   {
/*  315 */     PrintWriter localPrintWriter = new PrintWriter(new BufferedWriter(paramWriter));
/*      */ 
/*  317 */     Enumeration localEnumeration = grantElements();
/*      */ 
/*  319 */     localPrintWriter.println("/* AUTOMATICALLY GENERATED ON " + new Date() + "*/");
/*      */ 
/*  321 */     localPrintWriter.println("/* DO NOT EDIT */");
/*  322 */     localPrintWriter.println();
/*      */ 
/*  326 */     if (this.keyStoreUrlString != null) {
/*  327 */       writeKeyStoreEntry(localPrintWriter);
/*      */     }
/*  329 */     if (this.storePassURL != null) {
/*  330 */       writeStorePassURL(localPrintWriter);
/*      */     }
/*      */ 
/*  334 */     while (localEnumeration.hasMoreElements()) {
/*  335 */       GrantEntry localGrantEntry = (GrantEntry)localEnumeration.nextElement();
/*  336 */       localGrantEntry.write(localPrintWriter);
/*  337 */       localPrintWriter.println();
/*      */     }
/*  339 */     localPrintWriter.flush();
/*      */   }
/*      */ 
/*      */   private void parseKeyStoreEntry()
/*      */     throws PolicyParser.ParsingException, IOException
/*      */   {
/*  346 */     match("keystore");
/*  347 */     this.keyStoreUrlString = match("quoted string");
/*      */ 
/*  350 */     if (!peek(",")) {
/*  351 */       return;
/*      */     }
/*  353 */     match(",");
/*      */ 
/*  355 */     if (peek("\""))
/*  356 */       this.keyStoreType = match("quoted string");
/*      */     else {
/*  358 */       throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("expected.keystore.type"));
/*      */     }
/*      */ 
/*  363 */     if (!peek(",")) {
/*  364 */       return;
/*      */     }
/*  366 */     match(",");
/*      */ 
/*  368 */     if (peek("\""))
/*  369 */       this.keyStoreProvider = match("quoted string");
/*      */     else
/*  371 */       throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("expected.keystore.provider"));
/*      */   }
/*      */ 
/*      */   private void parseStorePassURL()
/*      */     throws PolicyParser.ParsingException, IOException
/*      */   {
/*  377 */     match("keyStorePasswordURL");
/*  378 */     this.storePassURL = match("quoted string");
/*      */   }
/*      */ 
/*      */   private void writeKeyStoreEntry(PrintWriter paramPrintWriter)
/*      */   {
/*  385 */     paramPrintWriter.print("keystore \"");
/*  386 */     paramPrintWriter.print(this.keyStoreUrlString);
/*  387 */     paramPrintWriter.print('"');
/*  388 */     if ((this.keyStoreType != null) && (this.keyStoreType.length() > 0))
/*  389 */       paramPrintWriter.print(", \"" + this.keyStoreType + "\"");
/*  390 */     if ((this.keyStoreProvider != null) && (this.keyStoreProvider.length() > 0))
/*  391 */       paramPrintWriter.print(", \"" + this.keyStoreProvider + "\"");
/*  392 */     paramPrintWriter.println(";");
/*  393 */     paramPrintWriter.println();
/*      */   }
/*      */ 
/*      */   private void writeStorePassURL(PrintWriter paramPrintWriter) {
/*  397 */     paramPrintWriter.print("keystorePasswordURL \"");
/*  398 */     paramPrintWriter.print(this.storePassURL);
/*  399 */     paramPrintWriter.print('"');
/*  400 */     paramPrintWriter.println(";");
/*  401 */     paramPrintWriter.println();
/*      */   }
/*      */ 
/*      */   private GrantEntry parseGrantEntry()
/*      */     throws PolicyParser.ParsingException, IOException
/*      */   {
/*  410 */     GrantEntry localGrantEntry = new GrantEntry();
/*  411 */     LinkedList localLinkedList = null;
/*  412 */     int i = 0;
/*      */ 
/*  414 */     match("grant");
/*      */     Object localObject1;
/*      */     Object localObject3;
/*      */     Object localObject2;
/*  416 */     while (!peek("{"))
/*      */     {
/*  418 */       if (peekAndMatch("Codebase")) {
/*  419 */         if (localGrantEntry.codeBase != null) {
/*  420 */           throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("multiple.Codebase.expressions"));
/*      */         }
/*      */ 
/*  424 */         localGrantEntry.codeBase = match("quoted string");
/*  425 */         peekAndMatch(",");
/*  426 */       } else if (peekAndMatch("SignedBy")) {
/*  427 */         if (localGrantEntry.signedBy != null) {
/*  428 */           throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("multiple.SignedBy.expressions"));
/*      */         }
/*      */ 
/*  432 */         localGrantEntry.signedBy = match("quoted string");
/*      */ 
/*  435 */         localObject1 = new StringTokenizer(localGrantEntry.signedBy, ",", true);
/*      */ 
/*  437 */         int k = 0;
/*  438 */         int m = 0;
/*  439 */         while (((StringTokenizer)localObject1).hasMoreTokens()) {
/*  440 */           localObject3 = ((StringTokenizer)localObject1).nextToken().trim();
/*  441 */           if (((String)localObject3).equals(","))
/*  442 */             m++;
/*  443 */           else if (((String)localObject3).length() > 0)
/*  444 */             k++;
/*      */         }
/*  446 */         if (k <= m) {
/*  447 */           throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("SignedBy.has.empty.alias"));
/*      */         }
/*      */ 
/*  452 */         peekAndMatch(",");
/*  453 */       } else if (peekAndMatch("Principal")) {
/*  454 */         if (localLinkedList == null) {
/*  455 */           localLinkedList = new LinkedList();
/*      */         }
/*      */ 
/*  461 */         if (peek("\""))
/*      */         {
/*  464 */           localObject1 = "PolicyParser.REPLACE_NAME";
/*  465 */           localObject2 = match("principal type");
/*      */         }
/*      */         else {
/*  468 */           if (peek("*")) {
/*  469 */             match("*");
/*  470 */             localObject1 = "WILDCARD_PRINCIPAL_CLASS";
/*      */           } else {
/*  472 */             localObject1 = match("principal type");
/*      */           }
/*      */ 
/*  476 */           if (peek("*")) {
/*  477 */             match("*");
/*  478 */             localObject2 = "WILDCARD_PRINCIPAL_NAME";
/*      */           } else {
/*  480 */             localObject2 = match("quoted string");
/*      */           }
/*      */ 
/*  484 */           if ((((String)localObject1).equals("WILDCARD_PRINCIPAL_CLASS")) && (!((String)localObject2).equals("WILDCARD_PRINCIPAL_NAME")))
/*      */           {
/*  486 */             if (debug != null) {
/*  487 */               debug.println("disallowing principal that has WILDCARD class but no WILDCARD name");
/*      */             }
/*      */ 
/*  490 */             throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("can.not.specify.Principal.with.a.wildcard.class.without.a.wildcard.name"));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  498 */           localObject2 = expand((String)localObject2);
/*      */ 
/*  500 */           if ((((String)localObject1).equals("javax.security.auth.x500.X500Principal")) && (!((String)localObject2).equals("WILDCARD_PRINCIPAL_NAME")))
/*      */           {
/*  508 */             X500Principal localX500Principal = new X500Principal(new X500Principal((String)localObject2).toString());
/*      */ 
/*  510 */             localObject2 = localX500Principal.getName();
/*      */           }
/*      */ 
/*  513 */           localLinkedList.add(new PrincipalEntry((String)localObject1, (String)localObject2));
/*      */         }
/*      */         catch (PropertyExpander.ExpandException localExpandException3)
/*      */         {
/*  519 */           if (debug != null) {
/*  520 */             debug.println("principal name expansion failed: " + (String)localObject2);
/*      */           }
/*      */ 
/*  523 */           i = 1;
/*      */         }
/*  525 */         peekAndMatch(",");
/*      */       }
/*      */       else {
/*  528 */         throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("expected.codeBase.or.SignedBy.or.Principal"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  534 */     if (localLinkedList != null) localGrantEntry.principals = localLinkedList;
/*  535 */     match("{");
/*      */ 
/*  537 */     while (!peek("}")) {
/*  538 */       if (peek("Permission")) {
/*      */         try {
/*  540 */           localObject1 = parsePermissionEntry();
/*  541 */           localGrantEntry.add((PermissionEntry)localObject1);
/*      */         }
/*      */         catch (PropertyExpander.ExpandException localExpandException1) {
/*  544 */           if (debug != null) {
/*  545 */             debug.println(localExpandException1.toString());
/*      */           }
/*  547 */           skipEntry();
/*      */         }
/*  549 */         match(";");
/*      */       } else {
/*  551 */         throw new ParsingException(this.st.lineno(), ResourcesMgr.getString("expected.permission.entry"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  557 */     match("}");
/*      */     try
/*      */     {
/*  560 */       if (localGrantEntry.signedBy != null) localGrantEntry.signedBy = expand(localGrantEntry.signedBy);
/*  561 */       if (localGrantEntry.codeBase != null)
/*      */       {
/*  564 */         if (localGrantEntry.codeBase.equals("${java.ext.dirs}"))
/*  565 */           localGrantEntry.codeBase = "${{java.ext.dirs}}";
/*      */         int j;
/*  568 */         if ((j = localGrantEntry.codeBase.indexOf("${{java.ext.dirs}}")) < 0) {
/*  569 */           localGrantEntry.codeBase = expand(localGrantEntry.codeBase, true).replace(File.separatorChar, '/');
/*      */         }
/*      */         else
/*      */         {
/*  575 */           localObject2 = parseExtDirs(localGrantEntry.codeBase, j);
/*  576 */           if ((localObject2 != null) && (localObject2.length > 0)) {
/*  577 */             for (int n = 0; n < localObject2.length; n++) {
/*  578 */               localObject3 = (GrantEntry)localGrantEntry.clone();
/*  579 */               ((GrantEntry)localObject3).codeBase = localObject2[n];
/*  580 */               add((GrantEntry)localObject3);
/*      */ 
/*  582 */               if (debug != null) {
/*  583 */                 debug.println("creating policy entry for expanded java.ext.dirs path:\n\t\t" + localObject2[n]);
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  589 */           i = 1;
/*      */         }
/*      */       }
/*      */     } catch (PropertyExpander.ExpandException localExpandException2) {
/*  593 */       if (debug != null) {
/*  594 */         debug.println(localExpandException2.toString());
/*      */       }
/*  596 */       return null;
/*      */     }
/*      */ 
/*  599 */     return i == 1 ? null : localGrantEntry;
/*      */   }
/*      */ 
/*      */   private PermissionEntry parsePermissionEntry()
/*      */     throws PolicyParser.ParsingException, IOException, PropertyExpander.ExpandException
/*      */   {
/*  608 */     PermissionEntry localPermissionEntry = new PermissionEntry();
/*      */ 
/*  611 */     match("Permission");
/*  612 */     localPermissionEntry.permission = match("permission type");
/*      */ 
/*  614 */     if (peek("\""))
/*      */     {
/*  616 */       localPermissionEntry.name = expand(match("quoted string"));
/*      */     }
/*      */ 
/*  619 */     if (!peek(",")) {
/*  620 */       return localPermissionEntry;
/*      */     }
/*  622 */     match(",");
/*      */ 
/*  624 */     if (peek("\"")) {
/*  625 */       localPermissionEntry.action = expand(match("quoted string"));
/*  626 */       if (!peek(",")) {
/*  627 */         return localPermissionEntry;
/*      */       }
/*  629 */       match(",");
/*      */     }
/*      */ 
/*  632 */     if (peekAndMatch("SignedBy")) {
/*  633 */       localPermissionEntry.signedBy = expand(match("quoted string"));
/*      */     }
/*  635 */     return localPermissionEntry;
/*      */   }
/*      */ 
/*      */   static String[] parseExtDirs(String paramString, int paramInt)
/*      */   {
/*  641 */     String str1 = System.getProperty("java.ext.dirs");
/*  642 */     String str2 = paramInt > 0 ? paramString.substring(0, paramInt) : "file:";
/*  643 */     int i = paramInt + "${{java.ext.dirs}}".length();
/*  644 */     String str3 = i < paramString.length() ? paramString.substring(i) : (String)null;
/*      */ 
/*  647 */     String[] arrayOfString = null;
/*      */ 
/*  649 */     if (str1 != null) {
/*  650 */       StringTokenizer localStringTokenizer = new StringTokenizer(str1, File.pathSeparator);
/*      */ 
/*  652 */       int j = localStringTokenizer.countTokens();
/*  653 */       arrayOfString = new String[j];
/*  654 */       for (int k = 0; k < j; k++) {
/*  655 */         File localFile = new File(localStringTokenizer.nextToken());
/*  656 */         arrayOfString[k] = ParseUtil.encodePath(localFile.getAbsolutePath());
/*      */ 
/*  659 */         if (!arrayOfString[k].startsWith("/")) {
/*  660 */           arrayOfString[k] = ("/" + arrayOfString[k]);
/*      */         }
/*      */ 
/*  663 */         String str4 = str3 == null ? "/*" : arrayOfString[k].endsWith("/") ? "*" : str3;
/*      */ 
/*  667 */         arrayOfString[k] = (str2 + arrayOfString[k] + str4);
/*      */       }
/*      */     }
/*  670 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   private boolean peekAndMatch(String paramString)
/*      */     throws PolicyParser.ParsingException, IOException
/*      */   {
/*  676 */     if (peek(paramString)) {
/*  677 */       match(paramString);
/*  678 */       return true;
/*      */     }
/*  680 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean peek(String paramString)
/*      */   {
/*  685 */     boolean bool = false;
/*      */ 
/*  687 */     switch (this.lookahead)
/*      */     {
/*      */     case -3:
/*  690 */       if (paramString.equalsIgnoreCase(this.st.sval))
/*  691 */         bool = true; break;
/*      */     case 44:
/*  694 */       if (paramString.equalsIgnoreCase(","))
/*  695 */         bool = true; break;
/*      */     case 123:
/*  698 */       if (paramString.equalsIgnoreCase("{"))
/*  699 */         bool = true; break;
/*      */     case 125:
/*  702 */       if (paramString.equalsIgnoreCase("}"))
/*  703 */         bool = true; break;
/*      */     case 34:
/*  706 */       if (paramString.equalsIgnoreCase("\""))
/*  707 */         bool = true; break;
/*      */     case 42:
/*  710 */       if (paramString.equalsIgnoreCase("*"))
/*  711 */         bool = true; break;
/*      */     }
/*      */ 
/*  716 */     return bool;
/*      */   }
/*      */ 
/*      */   private String match(String paramString)
/*      */     throws PolicyParser.ParsingException, IOException
/*      */   {
/*  722 */     String str = null;
/*      */ 
/*  724 */     switch (this.lookahead) {
/*      */     case -2:
/*  726 */       throw new ParsingException(this.st.lineno(), paramString, ResourcesMgr.getString("number.") + String.valueOf(this.st.nval));
/*      */     case -1:
/*  730 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("expected.expect.read.end.of.file."));
/*      */ 
/*  733 */       Object[] arrayOfObject = { paramString };
/*  734 */       throw new ParsingException(localMessageFormat.format(arrayOfObject));
/*      */     case -3:
/*  736 */       if (paramString.equalsIgnoreCase(this.st.sval)) {
/*  737 */         this.lookahead = this.st.nextToken();
/*  738 */       } else if (paramString.equalsIgnoreCase("permission type")) {
/*  739 */         str = this.st.sval;
/*  740 */         this.lookahead = this.st.nextToken();
/*  741 */       } else if (paramString.equalsIgnoreCase("principal type")) {
/*  742 */         str = this.st.sval;
/*  743 */         this.lookahead = this.st.nextToken();
/*      */       } else {
/*  745 */         throw new ParsingException(this.st.lineno(), paramString, this.st.sval);
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 34:
/*  750 */       if (paramString.equalsIgnoreCase("quoted string")) {
/*  751 */         str = this.st.sval;
/*  752 */         this.lookahead = this.st.nextToken();
/*  753 */       } else if (paramString.equalsIgnoreCase("permission type")) {
/*  754 */         str = this.st.sval;
/*  755 */         this.lookahead = this.st.nextToken();
/*  756 */       } else if (paramString.equalsIgnoreCase("principal type")) {
/*  757 */         str = this.st.sval;
/*  758 */         this.lookahead = this.st.nextToken();
/*      */       } else {
/*  760 */         throw new ParsingException(this.st.lineno(), paramString, this.st.sval);
/*      */       }
/*      */       break;
/*      */     case 44:
/*  764 */       if (paramString.equalsIgnoreCase(","))
/*  765 */         this.lookahead = this.st.nextToken();
/*      */       else
/*  767 */         throw new ParsingException(this.st.lineno(), paramString, ",");
/*      */       break;
/*      */     case 123:
/*  770 */       if (paramString.equalsIgnoreCase("{"))
/*  771 */         this.lookahead = this.st.nextToken();
/*      */       else
/*  773 */         throw new ParsingException(this.st.lineno(), paramString, "{");
/*      */       break;
/*      */     case 125:
/*  776 */       if (paramString.equalsIgnoreCase("}"))
/*  777 */         this.lookahead = this.st.nextToken();
/*      */       else
/*  779 */         throw new ParsingException(this.st.lineno(), paramString, "}");
/*      */       break;
/*      */     case 59:
/*  782 */       if (paramString.equalsIgnoreCase(";"))
/*  783 */         this.lookahead = this.st.nextToken();
/*      */       else
/*  785 */         throw new ParsingException(this.st.lineno(), paramString, ";");
/*      */       break;
/*      */     case 42:
/*  788 */       if (paramString.equalsIgnoreCase("*"))
/*  789 */         this.lookahead = this.st.nextToken();
/*      */       else
/*  791 */         throw new ParsingException(this.st.lineno(), paramString, "*");
/*      */       break;
/*      */     default:
/*  794 */       throw new ParsingException(this.st.lineno(), paramString, new String(new char[] { (char)this.lookahead }));
/*      */     }
/*      */ 
/*  797 */     return str;
/*      */   }
/*      */ 
/*      */   private void skipEntry()
/*      */     throws PolicyParser.ParsingException, IOException
/*      */   {
/*  805 */     while (this.lookahead != 59) {
/*  806 */       switch (this.lookahead) {
/*      */       case -2:
/*  808 */         throw new ParsingException(this.st.lineno(), ";", ResourcesMgr.getString("number.") + String.valueOf(this.st.nval));
/*      */       case -1:
/*  812 */         throw new ParsingException(ResourcesMgr.getString("expected.read.end.of.file."));
/*      */       }
/*      */ 
/*  815 */       this.lookahead = this.st.nextToken();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void main(String[] paramArrayOfString)
/*      */     throws Exception
/*      */   {
/* 1219 */     FileReader localFileReader = null;
/* 1220 */     FileWriter localFileWriter = null;
/*      */     try {
/* 1222 */       PolicyParser localPolicyParser = new PolicyParser(true);
/* 1223 */       localFileReader = new FileReader(paramArrayOfString[0]);
/* 1224 */       localPolicyParser.read(localFileReader);
/* 1225 */       localFileWriter = new FileWriter(paramArrayOfString[1]);
/* 1226 */       localPolicyParser.write(localFileWriter);
/*      */     } finally {
/* 1228 */       if (localFileReader != null) {
/* 1229 */         localFileReader.close();
/*      */       }
/*      */ 
/* 1232 */       if (localFileWriter != null)
/* 1233 */         localFileWriter.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class GrantEntry
/*      */   {
/*      */     public String signedBy;
/*      */     public String codeBase;
/*      */     public LinkedList<PolicyParser.PrincipalEntry> principals;
/*      */     public Vector<PolicyParser.PermissionEntry> permissionEntries;
/*      */ 
/*      */     public GrantEntry()
/*      */     {
/*  858 */       this.principals = new LinkedList();
/*  859 */       this.permissionEntries = new Vector();
/*      */     }
/*      */ 
/*      */     public GrantEntry(String paramString1, String paramString2) {
/*  863 */       this.codeBase = paramString2;
/*  864 */       this.signedBy = paramString1;
/*  865 */       this.principals = new LinkedList();
/*  866 */       this.permissionEntries = new Vector();
/*      */     }
/*      */ 
/*      */     public void add(PolicyParser.PermissionEntry paramPermissionEntry)
/*      */     {
/*  871 */       this.permissionEntries.addElement(paramPermissionEntry);
/*      */     }
/*      */ 
/*      */     public boolean remove(PolicyParser.PrincipalEntry paramPrincipalEntry)
/*      */     {
/*  876 */       return this.principals.remove(paramPrincipalEntry);
/*      */     }
/*      */ 
/*      */     public boolean remove(PolicyParser.PermissionEntry paramPermissionEntry)
/*      */     {
/*  881 */       return this.permissionEntries.removeElement(paramPermissionEntry);
/*      */     }
/*      */ 
/*      */     public boolean contains(PolicyParser.PrincipalEntry paramPrincipalEntry)
/*      */     {
/*  886 */       return this.principals.contains(paramPrincipalEntry);
/*      */     }
/*      */ 
/*      */     public boolean contains(PolicyParser.PermissionEntry paramPermissionEntry)
/*      */     {
/*  891 */       return this.permissionEntries.contains(paramPermissionEntry);
/*      */     }
/*      */ 
/*      */     public Enumeration<PolicyParser.PermissionEntry> permissionElements()
/*      */     {
/*  898 */       return this.permissionEntries.elements();
/*      */     }
/*      */ 
/*      */     public void write(PrintWriter paramPrintWriter)
/*      */     {
/*  903 */       paramPrintWriter.print("grant");
/*  904 */       if (this.signedBy != null) {
/*  905 */         paramPrintWriter.print(" signedBy \"");
/*  906 */         paramPrintWriter.print(this.signedBy);
/*  907 */         paramPrintWriter.print('"');
/*  908 */         if (this.codeBase != null)
/*  909 */           paramPrintWriter.print(", ");
/*      */       }
/*  911 */       if (this.codeBase != null) {
/*  912 */         paramPrintWriter.print(" codeBase \"");
/*  913 */         paramPrintWriter.print(this.codeBase);
/*  914 */         paramPrintWriter.print('"');
/*  915 */         if ((this.principals != null) && (this.principals.size() > 0))
/*  916 */           paramPrintWriter.print(",\n");
/*      */       }
/*      */       Object localObject2;
/*  918 */       if ((this.principals != null) && (this.principals.size() > 0)) {
/*  919 */         localObject1 = this.principals.listIterator();
/*  920 */         while (((ListIterator)localObject1).hasNext()) {
/*  921 */           paramPrintWriter.print("      ");
/*  922 */           localObject2 = (PolicyParser.PrincipalEntry)((ListIterator)localObject1).next();
/*  923 */           ((PolicyParser.PrincipalEntry)localObject2).write(paramPrintWriter);
/*  924 */           if (((ListIterator)localObject1).hasNext())
/*  925 */             paramPrintWriter.print(",\n");
/*      */         }
/*      */       }
/*  928 */       paramPrintWriter.println(" {");
/*  929 */       Object localObject1 = this.permissionEntries.elements();
/*  930 */       while (((Enumeration)localObject1).hasMoreElements()) {
/*  931 */         localObject2 = (PolicyParser.PermissionEntry)((Enumeration)localObject1).nextElement();
/*  932 */         paramPrintWriter.write("  ");
/*  933 */         ((PolicyParser.PermissionEntry)localObject2).write(paramPrintWriter);
/*      */       }
/*  935 */       paramPrintWriter.println("};");
/*      */     }
/*      */ 
/*      */     public Object clone() {
/*  939 */       GrantEntry localGrantEntry = new GrantEntry();
/*  940 */       localGrantEntry.codeBase = this.codeBase;
/*  941 */       localGrantEntry.signedBy = this.signedBy;
/*  942 */       localGrantEntry.principals = new LinkedList(this.principals);
/*  943 */       localGrantEntry.permissionEntries = new Vector(this.permissionEntries);
/*      */ 
/*  945 */       return localGrantEntry;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ParsingException extends GeneralSecurityException
/*      */   {
/*      */     private static final long serialVersionUID = -4330692689482574072L;
/*      */     private String i18nMessage;
/*      */ 
/*      */     public ParsingException(String paramString)
/*      */     {
/* 1192 */       super();
/* 1193 */       this.i18nMessage = paramString;
/*      */     }
/*      */ 
/*      */     public ParsingException(int paramInt, String paramString) {
/* 1197 */       super();
/* 1198 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("line.number.msg"));
/*      */ 
/* 1200 */       Object[] arrayOfObject = { new Integer(paramInt), paramString };
/* 1201 */       this.i18nMessage = localMessageFormat.format(arrayOfObject);
/*      */     }
/*      */ 
/*      */     public ParsingException(int paramInt, String paramString1, String paramString2) {
/* 1205 */       super();
/*      */ 
/* 1207 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("line.number.expected.expect.found.actual."));
/*      */ 
/* 1209 */       Object[] arrayOfObject = { new Integer(paramInt), paramString1, paramString2 };
/* 1210 */       this.i18nMessage = localMessageFormat.format(arrayOfObject);
/*      */     }
/*      */ 
/*      */     public String getLocalizedMessage() {
/* 1214 */       return this.i18nMessage;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class PermissionEntry
/*      */   {
/*      */     public String permission;
/*      */     public String name;
/*      */     public String action;
/*      */     public String signedBy;
/*      */ 
/*      */     public PermissionEntry()
/*      */     {
/*      */     }
/*      */ 
/*      */     public PermissionEntry(String paramString1, String paramString2, String paramString3)
/*      */     {
/* 1095 */       this.permission = paramString1;
/* 1096 */       this.name = paramString2;
/* 1097 */       this.action = paramString3;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1105 */       int i = this.permission.hashCode();
/* 1106 */       if (this.name != null) i ^= this.name.hashCode();
/* 1107 */       if (this.action != null) i ^= this.action.hashCode();
/* 1108 */       return i;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 1112 */       if (paramObject == this) {
/* 1113 */         return true;
/*      */       }
/* 1115 */       if (!(paramObject instanceof PermissionEntry)) {
/* 1116 */         return false;
/*      */       }
/* 1118 */       PermissionEntry localPermissionEntry = (PermissionEntry)paramObject;
/*      */ 
/* 1120 */       if (this.permission == null) {
/* 1121 */         if (localPermissionEntry.permission != null) return false;
/*      */       }
/* 1123 */       else if (!this.permission.equals(localPermissionEntry.permission)) return false;
/*      */ 
/* 1126 */       if (this.name == null) {
/* 1127 */         if (localPermissionEntry.name != null) return false;
/*      */       }
/* 1129 */       else if (!this.name.equals(localPermissionEntry.name)) return false;
/*      */ 
/* 1132 */       if (this.action == null) {
/* 1133 */         if (localPermissionEntry.action != null) return false;
/*      */       }
/* 1135 */       else if (!this.action.equals(localPermissionEntry.action)) return false;
/*      */ 
/* 1138 */       if (this.signedBy == null) {
/* 1139 */         if (localPermissionEntry.signedBy != null) return false;
/*      */       }
/* 1141 */       else if (!this.signedBy.equals(localPermissionEntry.signedBy)) return false;
/*      */ 
/* 1145 */       return true;
/*      */     }
/*      */ 
/*      */     public void write(PrintWriter paramPrintWriter) {
/* 1149 */       paramPrintWriter.print("permission ");
/* 1150 */       paramPrintWriter.print(this.permission);
/* 1151 */       if (this.name != null) {
/* 1152 */         paramPrintWriter.print(" \"");
/*      */ 
/* 1160 */         paramPrintWriter.print(this.name.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\""));
/* 1161 */         paramPrintWriter.print('"');
/*      */       }
/* 1163 */       if (this.action != null) {
/* 1164 */         paramPrintWriter.print(", \"");
/* 1165 */         paramPrintWriter.print(this.action);
/* 1166 */         paramPrintWriter.print('"');
/*      */       }
/* 1168 */       if (this.signedBy != null) {
/* 1169 */         paramPrintWriter.print(", signedBy \"");
/* 1170 */         paramPrintWriter.print(this.signedBy);
/* 1171 */         paramPrintWriter.print('"');
/*      */       }
/* 1173 */       paramPrintWriter.println(";");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class PrincipalEntry
/*      */   {
/*      */     public static final String WILDCARD_CLASS = "WILDCARD_PRINCIPAL_CLASS";
/*      */     public static final String WILDCARD_NAME = "WILDCARD_PRINCIPAL_NAME";
/*      */     String principalClass;
/*      */     String principalName;
/*      */ 
/*      */     public PrincipalEntry(String paramString1, String paramString2)
/*      */     {
/*  971 */       if ((paramString1 == null) || (paramString2 == null)) {
/*  972 */         throw new NullPointerException(ResourcesMgr.getString("null.principalClass.or.principalName"));
/*      */       }
/*  974 */       this.principalClass = paramString1;
/*  975 */       this.principalName = paramString2;
/*      */     }
/*      */ 
/*      */     public String getPrincipalClass() {
/*  979 */       return this.principalClass;
/*      */     }
/*      */ 
/*      */     public String getPrincipalName() {
/*  983 */       return this.principalName;
/*      */     }
/*      */ 
/*      */     public String getDisplayClass() {
/*  987 */       if (this.principalClass.equals("WILDCARD_PRINCIPAL_CLASS"))
/*  988 */         return "*";
/*  989 */       if (this.principalClass.equals("PolicyParser.REPLACE_NAME")) {
/*  990 */         return "";
/*      */       }
/*  992 */       return this.principalClass;
/*      */     }
/*      */ 
/*      */     public String getDisplayName() {
/*  996 */       return getDisplayName(false);
/*      */     }
/*      */ 
/*      */     public String getDisplayName(boolean paramBoolean) {
/* 1000 */       if (this.principalName.equals("WILDCARD_PRINCIPAL_NAME")) {
/* 1001 */         return "*";
/*      */       }
/*      */ 
/* 1004 */       if (paramBoolean) return "\"" + this.principalName + "\"";
/* 1005 */       return this.principalName;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1010 */       if (!this.principalClass.equals("PolicyParser.REPLACE_NAME")) {
/* 1011 */         return getDisplayClass() + "/" + getDisplayName();
/*      */       }
/* 1013 */       return getDisplayName();
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1029 */       if (this == paramObject) {
/* 1030 */         return true;
/*      */       }
/* 1032 */       if (!(paramObject instanceof PrincipalEntry)) {
/* 1033 */         return false;
/*      */       }
/* 1035 */       PrincipalEntry localPrincipalEntry = (PrincipalEntry)paramObject;
/* 1036 */       if ((this.principalClass.equals(localPrincipalEntry.principalClass)) && (this.principalName.equals(localPrincipalEntry.principalName)))
/*      */       {
/* 1038 */         return true;
/*      */       }
/*      */ 
/* 1041 */       return false;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1052 */       return this.principalClass.hashCode();
/*      */     }
/*      */     public void write(PrintWriter paramPrintWriter) {
/* 1055 */       paramPrintWriter.print("principal " + getDisplayClass() + " " + getDisplayName(true));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.PolicyParser
 * JD-Core Version:    0.6.2
 */