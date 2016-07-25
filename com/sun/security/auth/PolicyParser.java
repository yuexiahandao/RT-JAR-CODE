/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.StreamTokenizer;
/*     */ import java.io.Writer;
/*     */ import java.security.AccessController;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Vector;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.PropertyExpander;
/*     */ import sun.security.util.PropertyExpander.ExpandException;
/*     */ 
/*     */ @Deprecated
/*     */ class PolicyParser
/*     */ {
/*  88 */   private static final ResourceBundle rb = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public ResourceBundle run()
/*     */     {
/*  92 */       return ResourceBundle.getBundle("sun.security.util.AuthResources");
/*     */     }
/*     */   });
/*     */   private Vector<GrantEntry> grantEntries;
/* 100 */   private static final Debug debug = Debug.getInstance("parser", "\t[Auth Policy Parser]");
/*     */   private StreamTokenizer st;
/*     */   private int lookahead;
/*     */   private int linenum;
/* 105 */   private boolean expandProp = false;
/* 106 */   private String keyStoreUrlString = null;
/* 107 */   private String keyStoreType = null;
/*     */ 
/*     */   private String expand(String paramString)
/*     */     throws PropertyExpander.ExpandException
/*     */   {
/* 112 */     if (this.expandProp) {
/* 113 */       return PropertyExpander.expand(paramString);
/*     */     }
/* 115 */     return paramString;
/*     */   }
/*     */ 
/*     */   public PolicyParser()
/*     */   {
/* 122 */     this.grantEntries = new Vector();
/*     */   }
/*     */ 
/*     */   public PolicyParser(boolean paramBoolean)
/*     */   {
/* 127 */     this();
/* 128 */     this.expandProp = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void read(Reader paramReader)
/*     */     throws PolicyParser.ParsingException, IOException
/*     */   {
/* 147 */     if (!(paramReader instanceof BufferedReader)) {
/* 148 */       paramReader = new BufferedReader(paramReader);
/*     */     }
/*     */ 
/* 158 */     this.st = new StreamTokenizer(paramReader);
/*     */ 
/* 160 */     this.st.resetSyntax();
/* 161 */     this.st.wordChars(97, 122);
/* 162 */     this.st.wordChars(65, 90);
/* 163 */     this.st.wordChars(46, 46);
/* 164 */     this.st.wordChars(48, 57);
/* 165 */     this.st.wordChars(95, 95);
/* 166 */     this.st.wordChars(36, 36);
/* 167 */     this.st.wordChars(160, 255);
/* 168 */     this.st.whitespaceChars(0, 32);
/* 169 */     this.st.commentChar(47);
/* 170 */     this.st.quoteChar(39);
/* 171 */     this.st.quoteChar(34);
/* 172 */     this.st.lowerCaseMode(false);
/* 173 */     this.st.ordinaryChar(47);
/* 174 */     this.st.slashSlashComments(true);
/* 175 */     this.st.slashStarComments(true);
/*     */ 
/* 186 */     this.lookahead = this.st.nextToken();
/* 187 */     while (this.lookahead != -1) {
/* 188 */       if (peek("grant")) {
/* 189 */         GrantEntry localGrantEntry = parseGrantEntry();
/*     */ 
/* 191 */         if (localGrantEntry != null)
/* 192 */           add(localGrantEntry);
/* 193 */       } else if ((peek("keystore")) && (this.keyStoreUrlString == null))
/*     */       {
/* 196 */         parseKeyStoreEntry();
/*     */       }
/*     */ 
/* 200 */       match(";");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void add(GrantEntry paramGrantEntry)
/*     */   {
/* 206 */     this.grantEntries.addElement(paramGrantEntry);
/*     */   }
/*     */ 
/*     */   public void replace(GrantEntry paramGrantEntry1, GrantEntry paramGrantEntry2)
/*     */   {
/* 211 */     this.grantEntries.setElementAt(paramGrantEntry2, this.grantEntries.indexOf(paramGrantEntry1));
/*     */   }
/*     */ 
/*     */   public boolean remove(GrantEntry paramGrantEntry)
/*     */   {
/* 216 */     return this.grantEntries.removeElement(paramGrantEntry);
/*     */   }
/*     */ 
/*     */   public String getKeyStoreUrl()
/*     */   {
/*     */     try
/*     */     {
/* 225 */       if ((this.keyStoreUrlString != null) && (this.keyStoreUrlString.length() != 0))
/* 226 */         return expand(this.keyStoreUrlString).replace(File.separatorChar, '/');
/*     */     }
/*     */     catch (PropertyExpander.ExpandException localExpandException)
/*     */     {
/* 230 */       return null;
/*     */     }
/* 232 */     return null;
/*     */   }
/*     */ 
/*     */   public void setKeyStoreUrl(String paramString) {
/* 236 */     this.keyStoreUrlString = paramString;
/*     */   }
/*     */ 
/*     */   public String getKeyStoreType() {
/* 240 */     return this.keyStoreType;
/*     */   }
/*     */ 
/*     */   public void setKeyStoreType(String paramString) {
/* 244 */     this.keyStoreType = paramString;
/*     */   }
/*     */ 
/*     */   public Enumeration<GrantEntry> grantElements()
/*     */   {
/* 254 */     return this.grantEntries.elements();
/*     */   }
/*     */ 
/*     */   public void write(Writer paramWriter)
/*     */   {
/* 263 */     PrintWriter localPrintWriter = new PrintWriter(new BufferedWriter(paramWriter));
/*     */ 
/* 265 */     Enumeration localEnumeration = grantElements();
/*     */ 
/* 267 */     localPrintWriter.println("/* AUTOMATICALLY GENERATED ON " + new Date() + "*/");
/*     */ 
/* 269 */     localPrintWriter.println("/* DO NOT EDIT */");
/* 270 */     localPrintWriter.println();
/*     */ 
/* 274 */     if (this.keyStoreUrlString != null) {
/* 275 */       writeKeyStoreEntry(localPrintWriter);
/*     */     }
/*     */ 
/* 279 */     while (localEnumeration.hasMoreElements()) {
/* 280 */       GrantEntry localGrantEntry = (GrantEntry)localEnumeration.nextElement();
/* 281 */       localGrantEntry.write(localPrintWriter);
/* 282 */       localPrintWriter.println();
/*     */     }
/* 284 */     localPrintWriter.flush();
/*     */   }
/*     */ 
/*     */   private void parseKeyStoreEntry()
/*     */     throws PolicyParser.ParsingException, IOException
/*     */   {
/* 291 */     match("keystore");
/* 292 */     this.keyStoreUrlString = match("quoted string");
/*     */ 
/* 295 */     if (!peek(",")) {
/* 296 */       return;
/*     */     }
/* 298 */     match(",");
/*     */ 
/* 300 */     if (peek("\""))
/* 301 */       this.keyStoreType = match("quoted string");
/*     */     else
/* 303 */       throw new ParsingException(this.st.lineno(), rb.getString("expected.keystore.type"));
/*     */   }
/*     */ 
/*     */   private void writeKeyStoreEntry(PrintWriter paramPrintWriter)
/*     */   {
/* 312 */     paramPrintWriter.print("keystore \"");
/* 313 */     paramPrintWriter.print(this.keyStoreUrlString);
/* 314 */     paramPrintWriter.print('"');
/* 315 */     if ((this.keyStoreType != null) && (this.keyStoreType.length() > 0))
/* 316 */       paramPrintWriter.print(", \"" + this.keyStoreType + "\"");
/* 317 */     paramPrintWriter.println(";");
/* 318 */     paramPrintWriter.println();
/*     */   }
/*     */ 
/*     */   private GrantEntry parseGrantEntry()
/*     */     throws PolicyParser.ParsingException, IOException
/*     */   {
/* 327 */     GrantEntry localGrantEntry = new GrantEntry();
/* 328 */     LinkedList localLinkedList = null;
/* 329 */     int i = 0;
/*     */ 
/* 331 */     match("grant");
/*     */     Object localObject;
/* 333 */     while (!peek("{"))
/*     */     {
/* 335 */       if (peekAndMatch("Codebase")) {
/* 336 */         localGrantEntry.codeBase = match("quoted string");
/* 337 */         peekAndMatch(",");
/* 338 */       } else if (peekAndMatch("SignedBy")) {
/* 339 */         localGrantEntry.signedBy = match("quoted string");
/* 340 */         peekAndMatch(",");
/* 341 */       } else if (peekAndMatch("Principal")) {
/* 342 */         if (localLinkedList == null) {
/* 343 */           localLinkedList = new LinkedList();
/*     */         }
/*     */ 
/* 348 */         if (peek("*")) {
/* 349 */           match("*");
/* 350 */           localObject = "WILDCARD_PRINCIPAL_CLASS";
/*     */         } else {
/* 352 */           localObject = match("principal type");
/*     */         }
/*     */         String str;
/* 357 */         if (peek("*")) {
/* 358 */           match("*");
/* 359 */           str = "WILDCARD_PRINCIPAL_NAME";
/*     */         } else {
/* 361 */           str = match("quoted string");
/*     */         }
/*     */ 
/* 365 */         if ((((String)localObject).equals("WILDCARD_PRINCIPAL_CLASS")) && (!str.equals("WILDCARD_PRINCIPAL_NAME")))
/*     */         {
/* 367 */           if (debug != null) {
/* 368 */             debug.println("disallowing principal that has WILDCARD class but no WILDCARD name");
/*     */           }
/* 370 */           throw new ParsingException(this.st.lineno(), rb.getString("can.not.specify.Principal.with.a.wildcard.class.without.a.wildcard.name"));
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/* 377 */           str = expand(str);
/* 378 */           localLinkedList.add(new PrincipalEntry((String)localObject, str));
/*     */         }
/*     */         catch (PropertyExpander.ExpandException localExpandException3)
/*     */         {
/* 384 */           if (debug != null) {
/* 385 */             debug.println("principal name expansion failed: " + str);
/*     */           }
/* 387 */           i = 1;
/*     */         }
/* 389 */         peekAndMatch(",");
/*     */       } else {
/* 391 */         throw new ParsingException(this.st.lineno(), rb.getString("expected.codeBase.or.SignedBy"));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 398 */     if (localLinkedList == null) {
/* 399 */       throw new ParsingException(this.st.lineno(), rb.getString("only.Principal.based.grant.entries.permitted"));
/*     */     }
/*     */ 
/* 404 */     localGrantEntry.principals = localLinkedList;
/* 405 */     match("{");
/*     */ 
/* 407 */     while (!peek("}")) {
/* 408 */       if (peek("Permission")) {
/*     */         try {
/* 410 */           localObject = parsePermissionEntry();
/* 411 */           localGrantEntry.add((PermissionEntry)localObject);
/*     */         }
/*     */         catch (PropertyExpander.ExpandException localExpandException1) {
/* 414 */           skipEntry();
/*     */         }
/* 416 */         match(";");
/*     */       } else {
/* 418 */         throw new ParsingException(this.st.lineno(), rb.getString("expected.permission.entry"));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 423 */     match("}");
/*     */     try
/*     */     {
/* 426 */       if (localGrantEntry.codeBase != null)
/* 427 */         localGrantEntry.codeBase = expand(localGrantEntry.codeBase).replace(File.separatorChar, '/');
/* 428 */       localGrantEntry.signedBy = expand(localGrantEntry.signedBy);
/*     */     } catch (PropertyExpander.ExpandException localExpandException2) {
/* 430 */       return null;
/*     */     }
/*     */ 
/* 433 */     return i == 1 ? null : localGrantEntry;
/*     */   }
/*     */ 
/*     */   private PermissionEntry parsePermissionEntry()
/*     */     throws PolicyParser.ParsingException, IOException, PropertyExpander.ExpandException
/*     */   {
/* 442 */     PermissionEntry localPermissionEntry = new PermissionEntry();
/*     */ 
/* 445 */     match("Permission");
/* 446 */     localPermissionEntry.permission = match("permission type");
/*     */ 
/* 448 */     if (peek("\""))
/*     */     {
/* 450 */       localPermissionEntry.name = expand(match("quoted string"));
/*     */     }
/*     */ 
/* 453 */     if (!peek(",")) {
/* 454 */       return localPermissionEntry;
/*     */     }
/* 456 */     match(",");
/*     */ 
/* 458 */     if (peek("\"")) {
/* 459 */       localPermissionEntry.action = expand(match("quoted string"));
/* 460 */       if (!peek(",")) {
/* 461 */         return localPermissionEntry;
/*     */       }
/* 463 */       match(",");
/*     */     }
/*     */ 
/* 466 */     if (peekAndMatch("SignedBy")) {
/* 467 */       localPermissionEntry.signedBy = expand(match("quoted string"));
/*     */     }
/* 469 */     return localPermissionEntry;
/*     */   }
/*     */ 
/*     */   private boolean peekAndMatch(String paramString)
/*     */     throws PolicyParser.ParsingException, IOException
/*     */   {
/* 475 */     if (peek(paramString)) {
/* 476 */       match(paramString);
/* 477 */       return true;
/*     */     }
/* 479 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean peek(String paramString)
/*     */   {
/* 484 */     boolean bool = false;
/*     */ 
/* 486 */     switch (this.lookahead)
/*     */     {
/*     */     case -3:
/* 489 */       if (paramString.equalsIgnoreCase(this.st.sval))
/* 490 */         bool = true; break;
/*     */     case 44:
/* 493 */       if (paramString.equalsIgnoreCase(","))
/* 494 */         bool = true; break;
/*     */     case 123:
/* 497 */       if (paramString.equalsIgnoreCase("{"))
/* 498 */         bool = true; break;
/*     */     case 125:
/* 501 */       if (paramString.equalsIgnoreCase("}"))
/* 502 */         bool = true; break;
/*     */     case 34:
/* 505 */       if (paramString.equalsIgnoreCase("\""))
/* 506 */         bool = true; break;
/*     */     case 42:
/* 509 */       if (paramString.equalsIgnoreCase("*"))
/* 510 */         bool = true; break;
/*     */     }
/*     */ 
/* 515 */     return bool;
/*     */   }
/*     */ 
/*     */   private String match(String paramString)
/*     */     throws PolicyParser.ParsingException, IOException
/*     */   {
/* 521 */     String str = null;
/*     */ 
/* 523 */     switch (this.lookahead) {
/*     */     case -2:
/* 525 */       throw new ParsingException(this.st.lineno(), paramString, rb.getString("number.") + String.valueOf(this.st.nval));
/*     */     case -1:
/* 529 */       MessageFormat localMessageFormat = new MessageFormat(rb.getString("expected.expect.read.end.of.file."));
/*     */ 
/* 531 */       Object[] arrayOfObject = { paramString };
/* 532 */       throw new ParsingException(localMessageFormat.format(arrayOfObject));
/*     */     case -3:
/* 534 */       if (paramString.equalsIgnoreCase(this.st.sval)) {
/* 535 */         this.lookahead = this.st.nextToken();
/* 536 */       } else if (paramString.equalsIgnoreCase("permission type")) {
/* 537 */         str = this.st.sval;
/* 538 */         this.lookahead = this.st.nextToken();
/* 539 */       } else if (paramString.equalsIgnoreCase("principal type")) {
/* 540 */         str = this.st.sval;
/* 541 */         this.lookahead = this.st.nextToken();
/*     */       } else {
/* 543 */         throw new ParsingException(this.st.lineno(), paramString, this.st.sval);
/*     */       }
/*     */       break;
/*     */     case 34:
/* 547 */       if (paramString.equalsIgnoreCase("quoted string")) {
/* 548 */         str = this.st.sval;
/* 549 */         this.lookahead = this.st.nextToken();
/* 550 */       } else if (paramString.equalsIgnoreCase("permission type")) {
/* 551 */         str = this.st.sval;
/* 552 */         this.lookahead = this.st.nextToken();
/* 553 */       } else if (paramString.equalsIgnoreCase("principal type")) {
/* 554 */         str = this.st.sval;
/* 555 */         this.lookahead = this.st.nextToken();
/*     */       } else {
/* 557 */         throw new ParsingException(this.st.lineno(), paramString, this.st.sval);
/*     */       }
/*     */       break;
/*     */     case 44:
/* 561 */       if (paramString.equalsIgnoreCase(","))
/* 562 */         this.lookahead = this.st.nextToken();
/*     */       else
/* 564 */         throw new ParsingException(this.st.lineno(), paramString, ",");
/*     */       break;
/*     */     case 123:
/* 567 */       if (paramString.equalsIgnoreCase("{"))
/* 568 */         this.lookahead = this.st.nextToken();
/*     */       else
/* 570 */         throw new ParsingException(this.st.lineno(), paramString, "{");
/*     */       break;
/*     */     case 125:
/* 573 */       if (paramString.equalsIgnoreCase("}"))
/* 574 */         this.lookahead = this.st.nextToken();
/*     */       else
/* 576 */         throw new ParsingException(this.st.lineno(), paramString, "}");
/*     */       break;
/*     */     case 59:
/* 579 */       if (paramString.equalsIgnoreCase(";"))
/* 580 */         this.lookahead = this.st.nextToken();
/*     */       else
/* 582 */         throw new ParsingException(this.st.lineno(), paramString, ";");
/*     */       break;
/*     */     case 42:
/* 585 */       if (paramString.equalsIgnoreCase("*"))
/* 586 */         this.lookahead = this.st.nextToken();
/*     */       else
/* 588 */         throw new ParsingException(this.st.lineno(), paramString, "*");
/*     */       break;
/*     */     default:
/* 591 */       throw new ParsingException(this.st.lineno(), paramString, new String(new char[] { (char)this.lookahead }));
/*     */     }
/*     */ 
/* 594 */     return str;
/*     */   }
/*     */ 
/*     */   private void skipEntry()
/*     */     throws PolicyParser.ParsingException, IOException
/*     */   {
/* 604 */     while (this.lookahead != 59) {
/* 605 */       switch (this.lookahead) {
/*     */       case -2:
/* 607 */         throw new ParsingException(this.st.lineno(), ";", rb.getString("number.") + String.valueOf(this.st.nval));
/*     */       case -1:
/* 611 */         throw new ParsingException(rb.getString("expected.read.end.of.file"));
/*     */       }
/*     */ 
/* 614 */       this.lookahead = this.st.nextToken();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/* 958 */     PolicyParser localPolicyParser = new PolicyParser(true);
/* 959 */     localPolicyParser.read(new FileReader(paramArrayOfString[0]));
/* 960 */     FileWriter localFileWriter = new FileWriter(paramArrayOfString[1]);
/* 961 */     localPolicyParser.write(localFileWriter);
/* 962 */     localFileWriter.close();
/*     */   }
/*     */ 
/*     */   static class GrantEntry
/*     */   {
/*     */     public String signedBy;
/*     */     public String codeBase;
/*     */     public LinkedList<PolicyParser.PrincipalEntry> principals;
/*     */     public Vector<PolicyParser.PermissionEntry> permissionEntries;
/*     */ 
/*     */     public GrantEntry()
/*     */     {
/* 657 */       this.permissionEntries = new Vector();
/*     */     }
/*     */ 
/*     */     public GrantEntry(String paramString1, String paramString2) {
/* 661 */       this.codeBase = paramString2;
/* 662 */       this.signedBy = paramString1;
/* 663 */       this.permissionEntries = new Vector();
/*     */     }
/*     */ 
/*     */     public void add(PolicyParser.PermissionEntry paramPermissionEntry)
/*     */     {
/* 668 */       this.permissionEntries.addElement(paramPermissionEntry);
/*     */     }
/*     */ 
/*     */     public boolean remove(PolicyParser.PermissionEntry paramPermissionEntry)
/*     */     {
/* 673 */       return this.permissionEntries.removeElement(paramPermissionEntry);
/*     */     }
/*     */ 
/*     */     public boolean contains(PolicyParser.PermissionEntry paramPermissionEntry)
/*     */     {
/* 678 */       return this.permissionEntries.contains(paramPermissionEntry);
/*     */     }
/*     */ 
/*     */     public Enumeration<PolicyParser.PermissionEntry> permissionElements()
/*     */     {
/* 685 */       return this.permissionEntries.elements();
/*     */     }
/*     */ 
/*     */     public void write(PrintWriter paramPrintWriter)
/*     */     {
/* 690 */       paramPrintWriter.print("grant");
/* 691 */       if (this.signedBy != null) {
/* 692 */         paramPrintWriter.print(" signedBy \"");
/* 693 */         paramPrintWriter.print(this.signedBy);
/* 694 */         paramPrintWriter.print('"');
/* 695 */         if (this.codeBase != null)
/* 696 */           paramPrintWriter.print(", ");
/*     */       }
/* 698 */       if (this.codeBase != null) {
/* 699 */         paramPrintWriter.print(" codeBase \"");
/* 700 */         paramPrintWriter.print(this.codeBase);
/* 701 */         paramPrintWriter.print('"');
/* 702 */         if ((this.principals != null) && (this.principals.size() > 0))
/* 703 */           paramPrintWriter.print(",\n");
/*     */       }
/*     */       Object localObject2;
/* 705 */       if ((this.principals != null) && (this.principals.size() > 0)) {
/* 706 */         localObject1 = this.principals.listIterator();
/* 707 */         while (((ListIterator)localObject1).hasNext()) {
/* 708 */           paramPrintWriter.print("\tPrincipal ");
/* 709 */           localObject2 = (PolicyParser.PrincipalEntry)((ListIterator)localObject1).next();
/* 710 */           paramPrintWriter.print(((PolicyParser.PrincipalEntry)localObject2).principalClass + " \"" + ((PolicyParser.PrincipalEntry)localObject2).principalName + "\"");
/*     */ 
/* 712 */           if (((ListIterator)localObject1).hasNext())
/* 713 */             paramPrintWriter.print(",\n");
/*     */         }
/*     */       }
/* 716 */       paramPrintWriter.println(" {");
/* 717 */       Object localObject1 = this.permissionEntries.elements();
/* 718 */       while (((Enumeration)localObject1).hasMoreElements()) {
/* 719 */         localObject2 = (PolicyParser.PermissionEntry)((Enumeration)localObject1).nextElement();
/* 720 */         paramPrintWriter.write("  ");
/* 721 */         ((PolicyParser.PermissionEntry)localObject2).write(paramPrintWriter);
/*     */       }
/* 723 */       paramPrintWriter.println("};");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ParsingException extends GeneralSecurityException
/*     */   {
/*     */     private static final long serialVersionUID = 8240970523155877400L;
/*     */ 
/*     */     public ParsingException(String paramString)
/*     */     {
/* 943 */       super();
/*     */     }
/*     */ 
/*     */     public ParsingException(int paramInt, String paramString) {
/* 947 */       super();
/*     */     }
/*     */ 
/*     */     public ParsingException(int paramInt, String paramString1, String paramString2) {
/* 951 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   static class PermissionEntry
/*     */   {
/*     */     public String permission;
/*     */     public String name;
/*     */     public String action;
/*     */     public String signedBy;
/*     */ 
/*     */     public PermissionEntry()
/*     */     {
/*     */     }
/*     */ 
/*     */     public PermissionEntry(String paramString1, String paramString2, String paramString3)
/*     */     {
/* 831 */       this.permission = paramString1;
/* 832 */       this.name = paramString2;
/* 833 */       this.action = paramString3;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 841 */       int i = this.permission.hashCode();
/* 842 */       if (this.name != null) i ^= this.name.hashCode();
/* 843 */       if (this.action != null) i ^= this.action.hashCode();
/* 844 */       return i;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 848 */       if (paramObject == this) {
/* 849 */         return true;
/*     */       }
/* 851 */       if (!(paramObject instanceof PermissionEntry)) {
/* 852 */         return false;
/*     */       }
/* 854 */       PermissionEntry localPermissionEntry = (PermissionEntry)paramObject;
/*     */ 
/* 856 */       if (this.permission == null) {
/* 857 */         if (localPermissionEntry.permission != null) return false;
/*     */       }
/* 859 */       else if (!this.permission.equals(localPermissionEntry.permission)) return false;
/*     */ 
/* 862 */       if (this.name == null) {
/* 863 */         if (localPermissionEntry.name != null) return false;
/*     */       }
/* 865 */       else if (!this.name.equals(localPermissionEntry.name)) return false;
/*     */ 
/* 868 */       if (this.action == null) {
/* 869 */         if (localPermissionEntry.action != null) return false;
/*     */       }
/* 871 */       else if (!this.action.equals(localPermissionEntry.action)) return false;
/*     */ 
/* 874 */       if (this.signedBy == null) {
/* 875 */         if (localPermissionEntry.signedBy != null) return false;
/*     */       }
/* 877 */       else if (!this.signedBy.equals(localPermissionEntry.signedBy)) return false;
/*     */ 
/* 881 */       return true;
/*     */     }
/*     */ 
/*     */     public void write(PrintWriter paramPrintWriter) {
/* 885 */       paramPrintWriter.print("permission ");
/* 886 */       paramPrintWriter.print(this.permission);
/* 887 */       if (this.name != null) {
/* 888 */         paramPrintWriter.print(" \"");
/*     */ 
/* 891 */         if (this.name.indexOf("\"") != -1) {
/* 892 */           int i = 0;
/* 893 */           char[] arrayOfChar1 = this.name.toCharArray();
/*     */ 
/* 896 */           for (int j = 0; j < arrayOfChar1.length; j++) {
/* 897 */             if (arrayOfChar1[j] == '"') {
/* 898 */               i++;
/*     */             }
/*     */           }
/*     */ 
/* 902 */           char[] arrayOfChar2 = new char[arrayOfChar1.length + i];
/* 903 */           int k = 0; for (int m = 0; k < arrayOfChar1.length; k++) {
/* 904 */             if (arrayOfChar1[k] != '"') {
/* 905 */               arrayOfChar2[(m++)] = arrayOfChar1[k];
/*     */             } else {
/* 907 */               arrayOfChar2[(m++)] = '\\';
/* 908 */               arrayOfChar2[(m++)] = arrayOfChar1[k];
/*     */             }
/*     */           }
/* 911 */           this.name = new String(arrayOfChar2);
/*     */         }
/* 913 */         paramPrintWriter.print(this.name);
/* 914 */         paramPrintWriter.print('"');
/*     */       }
/* 916 */       if (this.action != null) {
/* 917 */         paramPrintWriter.print(", \"");
/* 918 */         paramPrintWriter.print(this.action);
/* 919 */         paramPrintWriter.print('"');
/*     */       }
/* 921 */       if (this.signedBy != null) {
/* 922 */         paramPrintWriter.print(", signedBy \"");
/* 923 */         paramPrintWriter.print(this.signedBy);
/* 924 */         paramPrintWriter.print('"');
/*     */       }
/* 926 */       paramPrintWriter.println(";");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class PrincipalEntry
/*     */   {
/*     */     static final String WILDCARD_CLASS = "WILDCARD_PRINCIPAL_CLASS";
/*     */     static final String WILDCARD_NAME = "WILDCARD_PRINCIPAL_NAME";
/*     */     String principalClass;
/*     */     String principalName;
/*     */ 
/*     */     public PrincipalEntry(String paramString1, String paramString2)
/*     */     {
/* 750 */       if ((paramString1 == null) || (paramString2 == null)) {
/* 751 */         throw new NullPointerException("null principalClass or principalName");
/*     */       }
/* 753 */       this.principalClass = paramString1;
/* 754 */       this.principalName = paramString2;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 769 */       if (this == paramObject) {
/* 770 */         return true;
/*     */       }
/* 772 */       if (!(paramObject instanceof PrincipalEntry)) {
/* 773 */         return false;
/*     */       }
/* 775 */       PrincipalEntry localPrincipalEntry = (PrincipalEntry)paramObject;
/* 776 */       if ((this.principalClass.equals(localPrincipalEntry.principalClass)) && (this.principalName.equals(localPrincipalEntry.principalName)))
/*     */       {
/* 778 */         return true;
/*     */       }
/*     */ 
/* 781 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 792 */       return this.principalClass.hashCode();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.PolicyParser
 * JD-Core Version:    0.6.2
 */