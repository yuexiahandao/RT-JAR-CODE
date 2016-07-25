/*      */ package sun.security.krb5;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.net.InetAddress;
/*      */ import java.net.UnknownHostException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import sun.net.dns.ResolverConfiguration;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.security.krb5.internal.Krb5;
/*      */ import sun.security.krb5.internal.crypto.EType;
/*      */ 
/*      */ public class Config
/*      */ {
/*   61 */   private static Config singleton = null;
/*      */   private Hashtable<String, Object> stanzaTable;
/*   68 */   private static boolean DEBUG = Krb5.DEBUG;
/*      */   private static final int BASE16_0 = 1;
/*      */   private static final int BASE16_1 = 16;
/*      */   private static final int BASE16_2 = 256;
/*      */   private static final int BASE16_3 = 4096;
/*      */   private final String defaultRealm;
/*      */   private final String defaultKDC;
/*      */ 
/*      */   private static native String getWindowsDirectory(boolean paramBoolean);
/*      */ 
/*      */   public static synchronized Config getInstance()
/*      */     throws KrbException
/*      */   {
/*   95 */     if (singleton == null) {
/*   96 */       singleton = new Config();
/*      */     }
/*   98 */     return singleton;
/*      */   }
/*      */ 
/*      */   public static synchronized void refresh()
/*      */     throws KrbException
/*      */   {
/*  112 */     singleton = new Config();
/*  113 */     KdcComm.initStatic();
/*      */   }
/*      */ 
/*      */   private static boolean isMacosLionOrBetter()
/*      */   {
/*  119 */     String str1 = getProperty("os.name");
/*  120 */     if (!str1.contains("OS X")) {
/*  121 */       return false;
/*      */     }
/*      */ 
/*  124 */     String str2 = getProperty("os.version");
/*  125 */     String[] arrayOfString = str2.split("\\.");
/*      */ 
/*  128 */     if (!arrayOfString[0].equals("10")) return false;
/*  129 */     if (arrayOfString.length < 2) return false;
/*      */ 
/*      */     try
/*      */     {
/*  133 */       int i = Integer.parseInt(arrayOfString[1]);
/*  134 */       if (i >= 7) return true;
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*      */     }
/*  139 */     return false;
/*      */   }
/*      */ 
/*      */   private Config()
/*      */     throws KrbException
/*      */   {
/*  149 */     String str1 = getProperty("java.security.krb5.kdc");
/*  150 */     if (str1 != null)
/*      */     {
/*  152 */       this.defaultKDC = str1.replace(':', ' ');
/*      */     }
/*  154 */     else this.defaultKDC = null;
/*      */ 
/*  156 */     this.defaultRealm = getProperty("java.security.krb5.realm");
/*  157 */     if (((this.defaultKDC == null) && (this.defaultRealm != null)) || ((this.defaultRealm == null) && (this.defaultKDC != null)))
/*      */     {
/*  159 */       throw new KrbException("System property java.security.krb5.kdc and java.security.krb5.realm both must be set or neither must be set.");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  168 */       String str2 = getJavaFileName();
/*      */       Vector localVector;
/*  169 */       if (str2 != null) {
/*  170 */         localVector = loadConfigFile(str2);
/*  171 */         this.stanzaTable = parseStanzaTable(localVector);
/*  172 */         if (DEBUG)
/*  173 */           System.out.println("Loaded from Java config");
/*      */       }
/*      */       else {
/*  176 */         int i = 0;
/*  177 */         if (isMacosLionOrBetter())
/*      */           try {
/*  179 */             this.stanzaTable = SCDynamicStoreConfig.getConfig();
/*  180 */             if (DEBUG) {
/*  181 */               System.out.println("Loaded from SCDynamicStoreConfig");
/*      */             }
/*  183 */             i = 1;
/*      */           }
/*      */           catch (IOException localIOException2)
/*      */           {
/*      */           }
/*  188 */         if (i == 0) {
/*  189 */           str2 = getNativeFileName();
/*  190 */           localVector = loadConfigFile(str2);
/*  191 */           this.stanzaTable = parseStanzaTable(localVector);
/*  192 */           if (DEBUG)
/*  193 */             System.out.println("Loaded from native config");
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException1)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getDefaultIntValue(String paramString)
/*      */   {
/*  210 */     String str = null;
/*  211 */     int i = -2147483648;
/*  212 */     str = getDefault(paramString);
/*  213 */     if (str != null) {
/*      */       try {
/*  215 */         i = parseIntValue(str);
/*      */       } catch (NumberFormatException localNumberFormatException) {
/*  217 */         if (DEBUG) {
/*  218 */           System.out.println("Exception in getting value of " + paramString + " " + localNumberFormatException.getMessage());
/*      */ 
/*  221 */           System.out.println("Setting " + paramString + " to minimum value");
/*      */         }
/*      */ 
/*  224 */         i = -2147483648;
/*      */       }
/*      */     }
/*  227 */     return i;
/*      */   }
/*      */ 
/*      */   public int getDefaultIntValue(String paramString1, String paramString2)
/*      */   {
/*  241 */     String str = null;
/*  242 */     int i = -2147483648;
/*  243 */     str = getDefault(paramString1, paramString2);
/*  244 */     if (str != null) {
/*      */       try {
/*  246 */         i = parseIntValue(str);
/*      */       } catch (NumberFormatException localNumberFormatException) {
/*  248 */         if (DEBUG) {
/*  249 */           System.out.println("Exception in getting value of " + paramString1 + " in section " + paramString2 + " " + localNumberFormatException.getMessage());
/*      */ 
/*  252 */           System.out.println("Setting " + paramString1 + " to minimum value");
/*      */         }
/*      */ 
/*  255 */         i = -2147483648;
/*      */       }
/*      */     }
/*  258 */     return i;
/*      */   }
/*      */ 
/*      */   public String getDefault(String paramString)
/*      */   {
/*  267 */     if (this.stanzaTable == null) {
/*  268 */       return null;
/*      */     }
/*  270 */     return getDefault(paramString, this.stanzaTable);
/*      */   }
/*      */ 
/*      */   private String getDefault(String paramString, Hashtable paramHashtable)
/*      */   {
/*  283 */     String str1 = null;
/*      */     Enumeration localEnumeration;
/*  285 */     if (this.stanzaTable != null) {
/*  286 */       for (localEnumeration = paramHashtable.keys(); localEnumeration.hasMoreElements(); ) {
/*  287 */         String str2 = (String)localEnumeration.nextElement();
/*  288 */         Object localObject = paramHashtable.get(str2);
/*  289 */         if ((localObject instanceof Hashtable)) {
/*  290 */           str1 = getDefault(paramString, (Hashtable)localObject);
/*  291 */           if (str1 != null)
/*  292 */             return str1;
/*      */         }
/*  294 */         else if (str2.equalsIgnoreCase(paramString)) {
/*  295 */           if ((localObject instanceof String))
/*  296 */             return (String)paramHashtable.get(str2);
/*  297 */           if ((localObject instanceof Vector)) {
/*  298 */             str1 = "";
/*  299 */             int i = ((Vector)localObject).size();
/*  300 */             for (int j = 0; j < i; j++) {
/*  301 */               if (j == i - 1) {
/*  302 */                 str1 = str1 + (String)((Vector)localObject).elementAt(j);
/*      */               }
/*      */               else {
/*  305 */                 str1 = str1 + (String)((Vector)localObject).elementAt(j) + " ";
/*      */               }
/*      */             }
/*      */ 
/*  309 */             return str1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  314 */     return str1;
/*      */   }
/*      */ 
/*      */   public String getDefault(String paramString1, String paramString2)
/*      */   {
/*  327 */     String str2 = null;
/*      */     Enumeration localEnumeration;
/*  330 */     if (this.stanzaTable != null) {
/*  331 */       for (localEnumeration = this.stanzaTable.keys(); localEnumeration.hasMoreElements(); ) {
/*  332 */         String str1 = (String)localEnumeration.nextElement();
/*  333 */         Hashtable localHashtable1 = (Hashtable)this.stanzaTable.get(str1);
/*  334 */         if (str1.equalsIgnoreCase(paramString2)) {
/*  335 */           if (localHashtable1.containsKey(paramString1))
/*  336 */             return (String)localHashtable1.get(paramString1);
/*      */         }
/*  338 */         else if (localHashtable1.containsKey(paramString2)) {
/*  339 */           Object localObject1 = localHashtable1.get(paramString2);
/*  340 */           if ((localObject1 instanceof Hashtable)) {
/*  341 */             Hashtable localHashtable2 = (Hashtable)localObject1;
/*  342 */             if (localHashtable2.containsKey(paramString1)) {
/*  343 */               Object localObject2 = localHashtable2.get(paramString1);
/*  344 */               if ((localObject2 instanceof Vector)) {
/*  345 */                 str2 = "";
/*  346 */                 int i = ((Vector)localObject2).size();
/*  347 */                 for (int j = 0; j < i; j++) {
/*  348 */                   if (j == i - 1) {
/*  349 */                     str2 = str2 + (String)((Vector)localObject2).elementAt(j);
/*      */                   }
/*      */                   else {
/*  352 */                     str2 = str2 + (String)((Vector)localObject2).elementAt(j) + " ";
/*      */                   }
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/*  358 */                 str2 = (String)localObject2;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  365 */     return str2;
/*      */   }
/*      */ 
/*      */   public boolean getDefaultBooleanValue(String paramString)
/*      */   {
/*  375 */     String str = null;
/*  376 */     if (this.stanzaTable == null)
/*  377 */       str = null;
/*      */     else {
/*  379 */       str = getDefault(paramString, this.stanzaTable);
/*      */     }
/*  381 */     if ((str != null) && (str.equalsIgnoreCase("true"))) {
/*  382 */       return true;
/*      */     }
/*  384 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean getDefaultBooleanValue(String paramString1, String paramString2)
/*      */   {
/*  398 */     String str = getDefault(paramString1, paramString2);
/*  399 */     if ((str != null) && (str.equalsIgnoreCase("true"))) {
/*  400 */       return true;
/*      */     }
/*  402 */     return false;
/*      */   }
/*      */ 
/*      */   private int parseIntValue(String paramString)
/*      */     throws NumberFormatException
/*      */   {
/*  418 */     int i = 0;
/*      */     String str;
/*  419 */     if (paramString.startsWith("+")) {
/*  420 */       str = paramString.substring(1);
/*  421 */       return Integer.parseInt(str);
/*  422 */     }if (paramString.startsWith("0x")) {
/*  423 */       str = paramString.substring(2);
/*  424 */       char[] arrayOfChar = str.toCharArray();
/*  425 */       if (arrayOfChar.length > 8) {
/*  426 */         throw new NumberFormatException();
/*      */       }
/*  428 */       for (int j = 0; j < arrayOfChar.length; j++) {
/*  429 */         int k = arrayOfChar.length - j - 1;
/*  430 */         switch (arrayOfChar[j]) {
/*      */         case '0':
/*  432 */           i += 0;
/*  433 */           break;
/*      */         case '1':
/*  435 */           i += 1 * getBase(k);
/*  436 */           break;
/*      */         case '2':
/*  438 */           i += 2 * getBase(k);
/*  439 */           break;
/*      */         case '3':
/*  441 */           i += 3 * getBase(k);
/*  442 */           break;
/*      */         case '4':
/*  444 */           i += 4 * getBase(k);
/*  445 */           break;
/*      */         case '5':
/*  447 */           i += 5 * getBase(k);
/*  448 */           break;
/*      */         case '6':
/*  450 */           i += 6 * getBase(k);
/*  451 */           break;
/*      */         case '7':
/*  453 */           i += 7 * getBase(k);
/*  454 */           break;
/*      */         case '8':
/*  456 */           i += 8 * getBase(k);
/*  457 */           break;
/*      */         case '9':
/*  459 */           i += 9 * getBase(k);
/*  460 */           break;
/*      */         case 'A':
/*      */         case 'a':
/*  463 */           i += 10 * getBase(k);
/*  464 */           break;
/*      */         case 'B':
/*      */         case 'b':
/*  467 */           i += 11 * getBase(k);
/*  468 */           break;
/*      */         case 'C':
/*      */         case 'c':
/*  471 */           i += 12 * getBase(k);
/*  472 */           break;
/*      */         case 'D':
/*      */         case 'd':
/*  475 */           i += 13 * getBase(k);
/*  476 */           break;
/*      */         case 'E':
/*      */         case 'e':
/*  479 */           i += 14 * getBase(k);
/*  480 */           break;
/*      */         case 'F':
/*      */         case 'f':
/*  483 */           i += 15 * getBase(k);
/*  484 */           break;
/*      */         case ':':
/*      */         case ';':
/*      */         case '<':
/*      */         case '=':
/*      */         case '>':
/*      */         case '?':
/*      */         case '@':
/*      */         case 'G':
/*      */         case 'H':
/*      */         case 'I':
/*      */         case 'J':
/*      */         case 'K':
/*      */         case 'L':
/*      */         case 'M':
/*      */         case 'N':
/*      */         case 'O':
/*      */         case 'P':
/*      */         case 'Q':
/*      */         case 'R':
/*      */         case 'S':
/*      */         case 'T':
/*      */         case 'U':
/*      */         case 'V':
/*      */         case 'W':
/*      */         case 'X':
/*      */         case 'Y':
/*      */         case 'Z':
/*      */         case '[':
/*      */         case '\\':
/*      */         case ']':
/*      */         case '^':
/*      */         case '_':
/*      */         case '`':
/*      */         default:
/*  486 */           throw new NumberFormatException("Invalid numerical format");
/*      */         }
/*      */       }
/*      */ 
/*  490 */       if (i < 0)
/*  491 */         throw new NumberFormatException("Data overflow.");
/*      */     }
/*      */     else {
/*  494 */       i = Integer.parseInt(paramString);
/*      */     }
/*  496 */     return i;
/*      */   }
/*      */ 
/*      */   private int getBase(int paramInt) {
/*  500 */     int i = 16;
/*  501 */     switch (paramInt) {
/*      */     case 0:
/*  503 */       i = 1;
/*  504 */       break;
/*      */     case 1:
/*  506 */       i = 16;
/*  507 */       break;
/*      */     case 2:
/*  509 */       i = 256;
/*  510 */       break;
/*      */     case 3:
/*  512 */       i = 4096;
/*  513 */       break;
/*      */     default:
/*  515 */       for (int j = 1; j < paramInt; j++) {
/*  516 */         i *= 16;
/*      */       }
/*      */     }
/*  519 */     return i;
/*      */   }
/*      */ 
/*      */   private String find(String paramString1, String paramString2)
/*      */   {
/*      */     String str;
/*  527 */     if ((this.stanzaTable != null) && ((str = (String)((Hashtable)this.stanzaTable.get(paramString1)).get(paramString2)) != null))
/*      */     {
/*  530 */       return str;
/*      */     }
/*  532 */     return "";
/*      */   }
/*      */ 
/*      */   private Vector<String> loadConfigFile(final String paramString)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  570 */       if (!paramString.equals("")) {
/*  571 */         BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader((InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public FileInputStream run() throws IOException
/*      */           {
/*  575 */             return new FileInputStream(paramString);
/*      */           }
/*      */         })));
/*  579 */         Vector localVector = new Vector();
/*  580 */         Object localObject = null;
/*      */         String str1;
/*  581 */         while ((str1 = localBufferedReader.readLine()) != null)
/*      */         {
/*  584 */           if ((!str1.startsWith("#")) && (!str1.trim().isEmpty())) {
/*  585 */             String str2 = str1.trim();
/*      */ 
/*  600 */             if (str2.equals("{")) {
/*  601 */               if (localObject == null) {
/*  602 */                 throw new IOException("Config file should not start with \"{\"");
/*      */               }
/*      */ 
/*  605 */               localObject = (String)localObject + " " + str2;
/*      */             } else {
/*  607 */               if (localObject != null) {
/*  608 */                 localVector.addElement(localObject);
/*      */               }
/*  610 */               localObject = str2;
/*      */             }
/*      */           }
/*      */         }
/*  614 */         if (localObject != null) {
/*  615 */           localVector.addElement(localObject);
/*      */         }
/*      */ 
/*  618 */         localBufferedReader.close();
/*  619 */         return localVector;
/*      */       }
/*  621 */       return null;
/*      */     } catch (PrivilegedActionException localPrivilegedActionException) {
/*  623 */       throw ((IOException)localPrivilegedActionException.getException());
/*      */     }
/*      */   }
/*      */ 
/*      */   private Hashtable<String, Object> parseStanzaTable(Vector<String> paramVector)
/*      */     throws KrbException
/*      */   {
/*  636 */     if (paramVector == null) {
/*  637 */       throw new KrbException("I/O error while reading configuration file.");
/*      */     }
/*      */ 
/*  640 */     Hashtable localHashtable1 = new Hashtable();
/*  641 */     for (int i = 0; i < paramVector.size(); i++) {
/*  642 */       String str1 = ((String)paramVector.elementAt(i)).trim();
/*      */       int j;
/*      */       Hashtable localHashtable2;
/*  643 */       if (str1.equalsIgnoreCase("[realms]")) {
/*  644 */         for (j = i + 1; j < paramVector.size() + 1; j++)
/*      */         {
/*  646 */           if ((j == paramVector.size()) || (((String)paramVector.elementAt(j)).startsWith("[")))
/*      */           {
/*  648 */             localHashtable2 = new Hashtable();
/*      */ 
/*  650 */             localHashtable2 = parseRealmField(paramVector, i + 1, j);
/*  651 */             localHashtable1.put("realms", localHashtable2);
/*  652 */             i = j - 1;
/*  653 */             break;
/*      */           }
/*      */         }
/*  656 */       } else if (str1.equalsIgnoreCase("[capaths]")) {
/*  657 */         for (j = i + 1; j < paramVector.size() + 1; j++)
/*      */         {
/*  659 */           if ((j == paramVector.size()) || (((String)paramVector.elementAt(j)).startsWith("[")))
/*      */           {
/*  661 */             localHashtable2 = new Hashtable();
/*      */ 
/*  663 */             localHashtable2 = parseRealmField(paramVector, i + 1, j);
/*  664 */             localHashtable1.put("capaths", localHashtable2);
/*  665 */             i = j - 1;
/*  666 */             break;
/*      */           }
/*      */         }
/*  669 */       } else if ((str1.startsWith("[")) && (str1.endsWith("]"))) {
/*  670 */         String str2 = str1.substring(1, str1.length() - 1);
/*  671 */         for (int k = i + 1; k < paramVector.size() + 1; k++)
/*      */         {
/*  673 */           if ((k == paramVector.size()) || (((String)paramVector.elementAt(k)).startsWith("[")))
/*      */           {
/*  675 */             Hashtable localHashtable3 = parseField(paramVector, i + 1, k);
/*      */ 
/*  677 */             localHashtable1.put(str2, localHashtable3);
/*  678 */             i = k - 1;
/*  679 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  684 */     return localHashtable1;
/*      */   }
/*      */ 
/*      */   private String getJavaFileName()
/*      */   {
/*  698 */     String str = getProperty("java.security.krb5.conf");
/*  699 */     if (str == null) {
/*  700 */       str = getProperty("java.home") + File.separator + "lib" + File.separator + "security" + File.separator + "krb5.conf";
/*      */ 
/*  703 */       if (!fileExists(str)) {
/*  704 */         str = null;
/*      */       }
/*      */     }
/*  707 */     if (DEBUG) {
/*  708 */       System.out.println("Java config name: " + str);
/*      */     }
/*  710 */     return str;
/*      */   }
/*      */ 
/*      */   private String getNativeFileName()
/*      */   {
/*  731 */     Object localObject = null;
/*  732 */     String str1 = getProperty("os.name");
/*  733 */     if (str1.startsWith("Windows")) {
/*      */       try {
/*  735 */         Credentials.ensureLoaded();
/*      */       }
/*      */       catch (Exception localException) {
/*      */       }
/*  739 */       if (Credentials.alreadyLoaded) {
/*  740 */         String str2 = getWindowsDirectory(false);
/*  741 */         if (str2 != null) {
/*  742 */           if (str2.endsWith("\\"))
/*  743 */             str2 = str2 + "krb5.ini";
/*      */           else {
/*  745 */             str2 = str2 + "\\krb5.ini";
/*      */           }
/*  747 */           if (fileExists(str2)) {
/*  748 */             localObject = str2;
/*      */           }
/*      */         }
/*  751 */         if (localObject == null) {
/*  752 */           str2 = getWindowsDirectory(true);
/*  753 */           if (str2 != null) {
/*  754 */             if (str2.endsWith("\\"))
/*  755 */               str2 = str2 + "krb5.ini";
/*      */             else {
/*  757 */               str2 = str2 + "\\krb5.ini";
/*      */             }
/*  759 */             localObject = str2;
/*      */           }
/*      */         }
/*      */       }
/*  763 */       if (localObject == null)
/*  764 */         localObject = "c:\\winnt\\krb5.ini";
/*      */     }
/*  766 */     else if (str1.startsWith("SunOS")) {
/*  767 */       localObject = "/etc/krb5/krb5.conf";
/*  768 */     } else if (str1.contains("OS X")) {
/*  769 */       localObject = findMacosConfigFile();
/*      */     } else {
/*  771 */       localObject = "/etc/krb5.conf";
/*      */     }
/*  773 */     if (DEBUG) {
/*  774 */       System.out.println("Native config name: " + (String)localObject);
/*      */     }
/*  776 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static String getProperty(String paramString) {
/*  780 */     return (String)AccessController.doPrivileged(new GetPropertyAction(paramString));
/*      */   }
/*      */ 
/*      */   private String findMacosConfigFile()
/*      */   {
/*  785 */     String str1 = getProperty("user.home");
/*      */ 
/*  787 */     String str2 = str1 + "/Library/Preferences/edu.mit.Kerberos";
/*      */ 
/*  789 */     if (fileExists(str2)) {
/*  790 */       return str2;
/*      */     }
/*      */ 
/*  793 */     if (fileExists("/Library/Preferences/edu.mit.Kerberos")) {
/*  794 */       return "/Library/Preferences/edu.mit.Kerberos";
/*      */     }
/*      */ 
/*  797 */     return "/etc/krb5.conf";
/*      */   }
/*      */ 
/*      */   private static String trimmed(String paramString) {
/*  801 */     paramString = paramString.trim();
/*  802 */     if (((paramString.charAt(0) == '"') && (paramString.charAt(paramString.length() - 1) == '"')) || ((paramString.charAt(0) == '\'') && (paramString.charAt(paramString.length() - 1) == '\'')))
/*      */     {
/*  804 */       paramString = paramString.substring(1, paramString.length() - 1).trim();
/*      */     }
/*  806 */     return paramString;
/*      */   }
/*      */ 
/*      */   private Hashtable<String, String> parseField(Vector<String> paramVector, int paramInt1, int paramInt2)
/*      */   {
/*  812 */     Hashtable localHashtable = new Hashtable();
/*      */ 
/*  814 */     for (int i = paramInt1; i < paramInt2; i++) {
/*  815 */       String str1 = (String)paramVector.elementAt(i);
/*  816 */       for (int j = 0; j < str1.length(); j++) {
/*  817 */         if (str1.charAt(j) == '=') {
/*  818 */           String str2 = str1.substring(0, j).trim();
/*  819 */           String str3 = trimmed(str1.substring(j + 1));
/*  820 */           localHashtable.put(str2, str3);
/*  821 */           break;
/*      */         }
/*      */       }
/*      */     }
/*  825 */     return localHashtable;
/*      */   }
/*      */ 
/*      */   private Hashtable<String, Hashtable<String, Vector<String>>> parseRealmField(Vector<String> paramVector, int paramInt1, int paramInt2)
/*      */   {
/*  834 */     Hashtable localHashtable1 = new Hashtable();
/*      */ 
/*  836 */     for (int i = paramInt1; i < paramInt2; i++) {
/*  837 */       String str1 = ((String)paramVector.elementAt(i)).trim();
/*  838 */       if (str1.endsWith("{")) {
/*  839 */         String str2 = "";
/*  840 */         for (int j = 0; j < str1.length(); j++) {
/*  841 */           if (str1.charAt(j) == '=') {
/*  842 */             str2 = str1.substring(0, j).trim();
/*      */ 
/*  844 */             break;
/*      */           }
/*      */         }
/*  847 */         for (j = i + 1; j < paramInt2; j++) {
/*  848 */           int k = 0;
/*  849 */           str1 = ((String)paramVector.elementAt(j)).trim();
/*  850 */           for (int m = 0; m < str1.length(); m++) {
/*  851 */             if (str1.charAt(m) == '}') {
/*  852 */               k = 1;
/*  853 */               break;
/*      */             }
/*      */           }
/*  856 */           if (k == 1) {
/*  857 */             Hashtable localHashtable2 = parseRealmFieldEx(paramVector, i + 1, j);
/*  858 */             localHashtable1.put(str2, localHashtable2);
/*  859 */             i = j;
/*  860 */             k = 0;
/*  861 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  867 */     return localHashtable1;
/*      */   }
/*      */ 
/*      */   private Hashtable<String, Vector<String>> parseRealmFieldEx(Vector<String> paramVector, int paramInt1, int paramInt2)
/*      */   {
/*  874 */     Hashtable localHashtable = new Hashtable();
/*  875 */     Vector localVector1 = new Vector();
/*  876 */     Vector localVector2 = new Vector();
/*  877 */     String str1 = "";
/*      */ 
/*  879 */     for (int i = paramInt1; i < paramInt2; i++) {
/*  880 */       str1 = (String)paramVector.elementAt(i);
/*  881 */       for (int j = 0; j < str1.length(); j++) {
/*  882 */         if (str1.charAt(j) == '=')
/*      */         {
/*  884 */           String str2 = str1.substring(0, j).trim();
/*  885 */           if (!exists(str2, localVector1)) {
/*  886 */             localVector1.addElement(str2);
/*  887 */             localVector2 = new Vector();
/*      */           } else {
/*  889 */             localVector2 = (Vector)localHashtable.get(str2);
/*      */           }
/*  891 */           localVector2.addElement(trimmed(str1.substring(j + 1)));
/*  892 */           localHashtable.put(str2, localVector2);
/*  893 */           break;
/*      */         }
/*      */       }
/*      */     }
/*  897 */     return localHashtable;
/*      */   }
/*      */ 
/*      */   private boolean exists(String paramString, Vector paramVector)
/*      */   {
/*  904 */     boolean bool = false;
/*  905 */     for (int i = 0; i < paramVector.size(); i++) {
/*  906 */       if (((String)paramVector.elementAt(i)).equals(paramString)) {
/*  907 */         bool = true;
/*      */       }
/*      */     }
/*  910 */     return bool;
/*      */   }
/*      */ 
/*      */   public void listTable()
/*      */   {
/*  918 */     listTable(this.stanzaTable);
/*      */   }
/*      */ 
/*      */   private void listTable(Hashtable paramHashtable) {
/*  922 */     Vector localVector = new Vector();
/*      */     Enumeration localEnumeration;
/*  924 */     if (this.stanzaTable != null) {
/*  925 */       for (localEnumeration = paramHashtable.keys(); localEnumeration.hasMoreElements(); ) {
/*  926 */         String str = (String)localEnumeration.nextElement();
/*  927 */         Object localObject = paramHashtable.get(str);
/*  928 */         if (paramHashtable == this.stanzaTable) {
/*  929 */           System.out.println("[" + str + "]");
/*      */         }
/*  931 */         if ((localObject instanceof Hashtable)) {
/*  932 */           if (paramHashtable != this.stanzaTable)
/*  933 */             System.out.println("\t" + str + " = {");
/*  934 */           listTable((Hashtable)localObject);
/*  935 */           if (paramHashtable != this.stanzaTable)
/*  936 */             System.out.println("\t}");
/*      */         }
/*  938 */         else if ((localObject instanceof String)) {
/*  939 */           System.out.println("\t" + str + " = " + (String)paramHashtable.get(str));
/*      */         }
/*  941 */         else if ((localObject instanceof Vector)) {
/*  942 */           localVector = (Vector)localObject;
/*  943 */           for (int i = 0; i < localVector.size(); i++) {
/*  944 */             System.out.println("\t" + str + " = " + (String)localVector.elementAt(i));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*  950 */       System.out.println("Configuration file not found.");
/*      */   }
/*      */ 
/*      */   public int[] defaultEtype(String paramString)
/*      */   {
/*  960 */     String str1 = getDefault(paramString, "libdefaults");
/*  961 */     String str2 = " ";
/*      */     int[] arrayOfInt;
/*      */     int i;
/*  964 */     if (str1 == null) {
/*  965 */       if (DEBUG) {
/*  966 */         System.out.println("Using builtin default etypes for " + paramString);
/*      */       }
/*      */ 
/*  969 */       arrayOfInt = EType.getBuiltInDefaults();
/*      */     } else {
/*  971 */       for (i = 0; i < str1.length(); i++) {
/*  972 */         if (str1.substring(i, i + 1).equals(","))
/*      */         {
/*  975 */           str2 = ",";
/*  976 */           break;
/*      */         }
/*      */       }
/*  979 */       StringTokenizer localStringTokenizer = new StringTokenizer(str1, str2);
/*  980 */       i = localStringTokenizer.countTokens();
/*  981 */       ArrayList localArrayList = new ArrayList(i);
/*      */ 
/*  983 */       for (int k = 0; k < i; k++) {
/*  984 */         int j = getType(localStringTokenizer.nextToken());
/*  985 */         if ((j != -1) && (EType.isSupported(j)))
/*      */         {
/*  987 */           localArrayList.add(Integer.valueOf(j));
/*      */         }
/*      */       }
/*  990 */       if (localArrayList.size() == 0) {
/*  991 */         if (DEBUG) {
/*  992 */           System.out.println("no supported default etypes for " + paramString);
/*      */         }
/*      */ 
/*  995 */         return null;
/*      */       }
/*  997 */       arrayOfInt = new int[localArrayList.size()];
/*  998 */       for (k = 0; k < arrayOfInt.length; k++) {
/*  999 */         arrayOfInt[k] = ((Integer)localArrayList.get(k)).intValue();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1004 */     if (DEBUG) {
/* 1005 */       System.out.print("default etypes for " + paramString + ":");
/* 1006 */       for (i = 0; i < arrayOfInt.length; i++) {
/* 1007 */         System.out.print(" " + arrayOfInt[i]);
/*      */       }
/* 1009 */       System.out.println(".");
/*      */     }
/* 1011 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public int getType(String paramString)
/*      */   {
/* 1026 */     int i = -1;
/* 1027 */     if (paramString == null) {
/* 1028 */       return i;
/*      */     }
/* 1030 */     if ((paramString.startsWith("d")) || (paramString.startsWith("D"))) {
/* 1031 */       if (paramString.equalsIgnoreCase("des-cbc-crc"))
/* 1032 */         i = 1;
/* 1033 */       else if (paramString.equalsIgnoreCase("des-cbc-md5"))
/* 1034 */         i = 3;
/* 1035 */       else if (paramString.equalsIgnoreCase("des-mac"))
/* 1036 */         i = 4;
/* 1037 */       else if (paramString.equalsIgnoreCase("des-mac-k"))
/* 1038 */         i = 5;
/* 1039 */       else if (paramString.equalsIgnoreCase("des-cbc-md4"))
/* 1040 */         i = 2;
/* 1041 */       else if ((paramString.equalsIgnoreCase("des3-cbc-sha1")) || (paramString.equalsIgnoreCase("des3-hmac-sha1")) || (paramString.equalsIgnoreCase("des3-cbc-sha1-kd")) || (paramString.equalsIgnoreCase("des3-cbc-hmac-sha1-kd")))
/*      */       {
/* 1045 */         i = 16;
/*      */       }
/* 1047 */     } else if ((paramString.startsWith("a")) || (paramString.startsWith("A")))
/*      */     {
/* 1049 */       if ((paramString.equalsIgnoreCase("aes128-cts")) || (paramString.equalsIgnoreCase("aes128-cts-hmac-sha1-96")))
/*      */       {
/* 1051 */         i = 17;
/* 1052 */       } else if ((paramString.equalsIgnoreCase("aes256-cts")) || (paramString.equalsIgnoreCase("aes256-cts-hmac-sha1-96")))
/*      */       {
/* 1054 */         i = 18;
/*      */       }
/* 1056 */       else if ((paramString.equalsIgnoreCase("arcfour-hmac")) || (paramString.equalsIgnoreCase("arcfour-hmac-md5")))
/*      */       {
/* 1058 */         i = 23;
/*      */       }
/*      */     }
/* 1061 */     else if (paramString.equalsIgnoreCase("rc4-hmac"))
/* 1062 */       i = 23;
/* 1063 */     else if (paramString.equalsIgnoreCase("CRC32"))
/* 1064 */       i = 1;
/* 1065 */     else if ((paramString.startsWith("r")) || (paramString.startsWith("R"))) {
/* 1066 */       if (paramString.equalsIgnoreCase("rsa-md5"))
/* 1067 */         i = 7;
/* 1068 */       else if (paramString.equalsIgnoreCase("rsa-md5-des"))
/* 1069 */         i = 8;
/*      */     }
/* 1071 */     else if (paramString.equalsIgnoreCase("hmac-sha1-des3-kd"))
/* 1072 */       i = 12;
/* 1073 */     else if (paramString.equalsIgnoreCase("hmac-sha1-96-aes128"))
/* 1074 */       i = 15;
/* 1075 */     else if (paramString.equalsIgnoreCase("hmac-sha1-96-aes256"))
/* 1076 */       i = 16;
/* 1077 */     else if ((paramString.equalsIgnoreCase("hmac-md5-rc4")) || (paramString.equalsIgnoreCase("hmac-md5-arcfour")) || (paramString.equalsIgnoreCase("hmac-md5-enc")))
/*      */     {
/* 1080 */       i = -138;
/* 1081 */     } else if (paramString.equalsIgnoreCase("NULL")) {
/* 1082 */       i = 0;
/*      */     }
/*      */ 
/* 1085 */     return i;
/*      */   }
/*      */ 
/*      */   public void resetDefaultRealm(String paramString)
/*      */   {
/* 1095 */     if (DEBUG)
/* 1096 */       System.out.println(">>> Config try resetting default kdc " + paramString);
/*      */   }
/*      */ 
/*      */   public boolean useAddresses()
/*      */   {
/* 1105 */     boolean bool = false;
/*      */ 
/* 1107 */     String str = getDefault("no_addresses", "libdefaults");
/* 1108 */     bool = (str != null) && (str.equalsIgnoreCase("false"));
/* 1109 */     if (!bool)
/*      */     {
/* 1111 */       str = getDefault("noaddresses", "libdefaults");
/* 1112 */       bool = (str != null) && (str.equalsIgnoreCase("false"));
/*      */     }
/* 1114 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean useDNS(String paramString)
/*      */   {
/* 1121 */     String str = getDefault(paramString, "libdefaults");
/* 1122 */     if (str == null) {
/* 1123 */       str = getDefault("dns_fallback", "libdefaults");
/* 1124 */       if ("false".equalsIgnoreCase(str)) {
/* 1125 */         return false;
/*      */       }
/* 1127 */       return true;
/*      */     }
/*      */ 
/* 1130 */     return str.equalsIgnoreCase("true");
/*      */   }
/*      */ 
/*      */   public boolean useDNS_KDC()
/*      */   {
/* 1138 */     return useDNS("dns_lookup_kdc");
/*      */   }
/*      */ 
/*      */   public boolean useDNS_Realm()
/*      */   {
/* 1145 */     return useDNS("dns_lookup_realm");
/*      */   }
/*      */ 
/*      */   public String getDefaultRealm()
/*      */     throws KrbException
/*      */   {
/* 1154 */     if (this.defaultRealm != null) {
/* 1155 */       return this.defaultRealm;
/*      */     }
/* 1157 */     Object localObject = null;
/* 1158 */     String str = getDefault("default_realm", "libdefaults");
/* 1159 */     if ((str == null) && (useDNS_Realm())) {
/*      */       try
/*      */       {
/* 1162 */         str = getRealmFromDNS();
/*      */       } catch (KrbException localKrbException1) {
/* 1164 */         localObject = localKrbException1;
/*      */       }
/*      */     }
/* 1167 */     if (str == null) {
/* 1168 */       str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public String run()
/*      */         {
/* 1172 */           String str = System.getProperty("os.name");
/* 1173 */           if (str.startsWith("Windows")) {
/* 1174 */             return System.getenv("USERDNSDOMAIN");
/*      */           }
/* 1176 */           return null;
/*      */         }
/*      */       });
/*      */     }
/* 1180 */     if (str == null) {
/* 1181 */       KrbException localKrbException2 = new KrbException("Cannot locate default realm");
/* 1182 */       if (localObject != null) {
/* 1183 */         localKrbException2.initCause(localObject);
/*      */       }
/* 1185 */       throw localKrbException2;
/*      */     }
/* 1187 */     return str;
/*      */   }
/*      */ 
/*      */   public String getKDCList(String paramString)
/*      */     throws KrbException
/*      */   {
/* 1198 */     if (paramString == null) {
/* 1199 */       paramString = getDefaultRealm();
/*      */     }
/* 1201 */     if (paramString.equalsIgnoreCase(this.defaultRealm)) {
/* 1202 */       return this.defaultKDC;
/*      */     }
/* 1204 */     Object localObject = null;
/* 1205 */     String str = getDefault("kdc", paramString);
/* 1206 */     if ((str == null) && (useDNS_KDC())) {
/*      */       try
/*      */       {
/* 1209 */         str = getKDCFromDNS(paramString);
/*      */       } catch (KrbException localKrbException1) {
/* 1211 */         localObject = localKrbException1;
/*      */       }
/*      */     }
/* 1214 */     if (str == null) {
/* 1215 */       str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public String run()
/*      */         {
/* 1219 */           String str1 = System.getProperty("os.name");
/* 1220 */           if (str1.startsWith("Windows")) {
/* 1221 */             String str2 = System.getenv("LOGONSERVER");
/* 1222 */             if ((str2 != null) && (str2.startsWith("\\\\")))
/*      */             {
/* 1224 */               str2 = str2.substring(2);
/*      */             }
/* 1226 */             return str2;
/*      */           }
/* 1228 */           return null;
/*      */         }
/*      */       });
/*      */     }
/* 1232 */     if (str == null) {
/* 1233 */       if (this.defaultKDC != null) {
/* 1234 */         return this.defaultKDC;
/*      */       }
/* 1236 */       KrbException localKrbException2 = new KrbException("Cannot locate KDC");
/* 1237 */       if (localObject != null) {
/* 1238 */         localKrbException2.initCause(localObject);
/*      */       }
/* 1240 */       throw localKrbException2;
/*      */     }
/* 1242 */     return str;
/*      */   }
/*      */ 
/*      */   private String getRealmFromDNS()
/*      */     throws KrbException
/*      */   {
/* 1252 */     String str1 = null;
/* 1253 */     String str2 = null;
/*      */     Object localObject;
/*      */     try
/*      */     {
/* 1255 */       str2 = InetAddress.getLocalHost().getCanonicalHostName();
/*      */     } catch (UnknownHostException localUnknownHostException) {
/* 1257 */       localObject = new KrbException(60, "Unable to locate Kerberos realm: " + localUnknownHostException.getMessage());
/*      */ 
/* 1259 */       ((KrbException)localObject).initCause(localUnknownHostException);
/* 1260 */       throw ((Throwable)localObject);
/*      */     }
/*      */ 
/* 1263 */     String str3 = PrincipalName.mapHostToRealm(str2);
/* 1264 */     if (str3 == null)
/*      */     {
/* 1266 */       localObject = ResolverConfiguration.open().searchlist();
/* 1267 */       for (String str4 : (List)localObject) {
/* 1268 */         str1 = checkRealm(str4);
/* 1269 */         if (str1 != null)
/*      */           break;
/*      */       }
/*      */     }
/*      */     else {
/* 1274 */       str1 = checkRealm(str3);
/*      */     }
/* 1276 */     if (str1 == null) {
/* 1277 */       throw new KrbException(60, "Unable to locate Kerberos realm");
/*      */     }
/*      */ 
/* 1280 */     return str1;
/*      */   }
/*      */ 
/*      */   private static String checkRealm(String paramString)
/*      */   {
/* 1288 */     if (DEBUG) {
/* 1289 */       System.out.println("getRealmFromDNS: trying " + paramString);
/*      */     }
/* 1291 */     String[] arrayOfString = null;
/* 1292 */     String str = paramString;
/* 1293 */     while ((arrayOfString == null) && (str != null))
/*      */     {
/* 1295 */       arrayOfString = KrbServiceLocator.getKerberosService(str);
/* 1296 */       str = Realm.parseRealmComponent(str);
/*      */     }
/*      */ 
/* 1299 */     if (arrayOfString != null) {
/* 1300 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 1301 */         if (arrayOfString[i].equalsIgnoreCase(paramString)) {
/* 1302 */           return arrayOfString[i];
/*      */         }
/*      */       }
/*      */     }
/* 1306 */     return null;
/*      */   }
/*      */ 
/*      */   private String getKDCFromDNS(String paramString)
/*      */     throws KrbException
/*      */   {
/* 1317 */     String str = "";
/* 1318 */     String[] arrayOfString = null;
/*      */ 
/* 1320 */     if (DEBUG) {
/* 1321 */       System.out.println("getKDCFromDNS using UDP");
/*      */     }
/* 1323 */     arrayOfString = KrbServiceLocator.getKerberosService(paramString, "_udp");
/* 1324 */     if (arrayOfString == null)
/*      */     {
/* 1326 */       if (DEBUG) {
/* 1327 */         System.out.println("getKDCFromDNS using TCP");
/*      */       }
/* 1329 */       arrayOfString = KrbServiceLocator.getKerberosService(paramString, "_tcp");
/*      */     }
/* 1331 */     if (arrayOfString == null)
/*      */     {
/* 1333 */       throw new KrbException(60, "Unable to locate KDC for realm " + paramString);
/*      */     }
/*      */ 
/* 1336 */     if (arrayOfString.length == 0) {
/* 1337 */       return null;
/*      */     }
/* 1339 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 1340 */       str = str + arrayOfString[i].trim() + " ";
/*      */     }
/* 1342 */     str = str.trim();
/* 1343 */     if (str.equals("")) {
/* 1344 */       return null;
/*      */     }
/* 1346 */     return str;
/*      */   }
/*      */ 
/*      */   private boolean fileExists(String paramString) {
/* 1350 */     return ((Boolean)AccessController.doPrivileged(new FileExistsAction(paramString))).booleanValue();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1383 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1384 */     toStringInternal("", this.stanzaTable, localStringBuffer);
/* 1385 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static void toStringInternal(String paramString, Object paramObject, StringBuffer paramStringBuffer) {
/* 1389 */     if ((paramObject instanceof String))
/*      */     {
/* 1391 */       paramStringBuffer.append(paramObject).append('\n');
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject1;
/*      */       Object localObject2;
/* 1392 */       if ((paramObject instanceof Hashtable))
/*      */       {
/* 1394 */         localObject1 = (Hashtable)paramObject;
/* 1395 */         paramStringBuffer.append("{\n");
/* 1396 */         for (Iterator localIterator = ((Hashtable)localObject1).keySet().iterator(); localIterator.hasNext(); ) { localObject2 = localIterator.next();
/*      */ 
/* 1398 */           paramStringBuffer.append(paramString).append("    ").append(localObject2).append(" = ");
/*      */ 
/* 1400 */           toStringInternal(paramString + "    ", ((Hashtable)localObject1).get(localObject2), paramStringBuffer);
/*      */         }
/* 1402 */         paramStringBuffer.append(paramString).append("}\n");
/* 1403 */       } else if ((paramObject instanceof Vector))
/*      */       {
/* 1405 */         localObject1 = (Vector)paramObject;
/* 1406 */         paramStringBuffer.append("[");
/* 1407 */         int i = 1;
/* 1408 */         for (Object localObject3 : ((Vector)localObject1).toArray()) {
/* 1409 */           if (i == 0) paramStringBuffer.append(",");
/* 1410 */           paramStringBuffer.append(localObject3);
/* 1411 */           i = 0;
/*      */         }
/* 1413 */         paramStringBuffer.append("]\n");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class FileExistsAction
/*      */     implements PrivilegedAction<Boolean>
/*      */   {
/*      */     private String fileName;
/*      */ 
/*      */     public FileExistsAction(String paramString)
/*      */     {
/* 1360 */       this.fileName = paramString;
/*      */     }
/*      */ 
/*      */     public Boolean run() {
/* 1364 */       return Boolean.valueOf(new File(this.fileName).exists());
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.Config
 * JD-Core Version:    0.6.2
 */