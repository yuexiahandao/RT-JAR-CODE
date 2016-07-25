/*      */ package javax.swing.text.rtf;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.StreamTokenizer;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Dictionary;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.MutableAttributeSet;
/*      */ import javax.swing.text.SimpleAttributeSet;
/*      */ import javax.swing.text.Style;
/*      */ import javax.swing.text.StyleConstants;
/*      */ import javax.swing.text.StyledDocument;
/*      */ import javax.swing.text.TabStop;
/*      */ 
/*      */ class RTFReader extends RTFParser
/*      */ {
/*      */   StyledDocument target;
/*      */   Dictionary<Object, Object> parserState;
/*      */   Destination rtfDestination;
/*      */   MutableAttributeSet documentAttributes;
/*      */   Dictionary<Integer, String> fontTable;
/*      */   Color[] colorTable;
/*      */   Style[] characterStyles;
/*      */   Style[] paragraphStyles;
/*      */   Style[] sectionStyles;
/*      */   int rtfversion;
/*      */   boolean ignoreGroupIfUnknownKeyword;
/*      */   int skippingCharacters;
/*   91 */   private static Dictionary<String, RTFAttribute> straightforwardAttributes = RTFAttributes.attributesByKeyword();
/*      */   private MockAttributeSet mockery;
/*   99 */   static Dictionary<String, String> textKeywords = null;
/*      */   static final String TabAlignmentKey = "tab_alignment";
/*      */   static final String TabLeaderKey = "tab_leader";
/*  135 */   static Dictionary<String, char[]> characterSets = new Hashtable();
/*      */   static boolean useNeXTForAnsi;
/*      */ 
/*      */   public RTFReader(StyledDocument paramStyledDocument)
/*      */   {
/*  150 */     this.target = paramStyledDocument;
/*  151 */     this.parserState = new Hashtable();
/*  152 */     this.fontTable = new Hashtable();
/*      */ 
/*  154 */     this.rtfversion = -1;
/*      */ 
/*  156 */     this.mockery = new MockAttributeSet();
/*  157 */     this.documentAttributes = new SimpleAttributeSet();
/*      */   }
/*      */ 
/*      */   public void handleBinaryBlob(byte[] paramArrayOfByte)
/*      */   {
/*  167 */     if (this.skippingCharacters > 0)
/*      */     {
/*  169 */       this.skippingCharacters -= 1;
/*  170 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void handleText(String paramString)
/*      */   {
/*  182 */     if (this.skippingCharacters > 0) {
/*  183 */       if (this.skippingCharacters >= paramString.length()) {
/*  184 */         this.skippingCharacters -= paramString.length();
/*  185 */         return;
/*      */       }
/*  187 */       paramString = paramString.substring(this.skippingCharacters);
/*  188 */       this.skippingCharacters = 0;
/*      */     }
/*      */ 
/*  192 */     if (this.rtfDestination != null) {
/*  193 */       this.rtfDestination.handleText(paramString);
/*  194 */       return;
/*      */     }
/*      */ 
/*  197 */     warning("Text with no destination. oops.");
/*      */   }
/*      */ 
/*      */   Color defaultColor()
/*      */   {
/*  203 */     return Color.black;
/*      */   }
/*      */ 
/*      */   public void begingroup()
/*      */   {
/*  213 */     if (this.skippingCharacters > 0)
/*      */     {
/*  215 */       this.skippingCharacters = 0;
/*      */     }
/*      */ 
/*  220 */     Object localObject = this.parserState.get("_savedState");
/*  221 */     if (localObject != null)
/*  222 */       this.parserState.remove("_savedState");
/*  223 */     Dictionary localDictionary = (Dictionary)((Hashtable)this.parserState).clone();
/*  224 */     if (localObject != null)
/*  225 */       localDictionary.put("_savedState", localObject);
/*  226 */     this.parserState.put("_savedState", localDictionary);
/*      */ 
/*  228 */     if (this.rtfDestination != null)
/*  229 */       this.rtfDestination.begingroup();
/*      */   }
/*      */ 
/*      */   public void endgroup()
/*      */   {
/*  240 */     if (this.skippingCharacters > 0)
/*      */     {
/*  242 */       this.skippingCharacters = 0;
/*      */     }
/*      */ 
/*  245 */     Dictionary localDictionary1 = (Dictionary)this.parserState.get("_savedState");
/*  246 */     Destination localDestination = (Destination)localDictionary1.get("dst");
/*  247 */     if (localDestination != this.rtfDestination) {
/*  248 */       this.rtfDestination.close();
/*  249 */       this.rtfDestination = localDestination;
/*      */     }
/*  251 */     Dictionary localDictionary2 = this.parserState;
/*  252 */     this.parserState = localDictionary1;
/*  253 */     if (this.rtfDestination != null)
/*  254 */       this.rtfDestination.endgroup(localDictionary2);
/*      */   }
/*      */ 
/*      */   protected void setRTFDestination(Destination paramDestination)
/*      */   {
/*  261 */     Dictionary localDictionary = (Dictionary)this.parserState.get("_savedState");
/*  262 */     if ((localDictionary != null) && 
/*  263 */       (this.rtfDestination != localDictionary.get("dst"))) {
/*  264 */       warning("Warning, RTF destination overridden, invalid RTF.");
/*  265 */       this.rtfDestination.close();
/*      */     }
/*      */ 
/*  268 */     this.rtfDestination = paramDestination;
/*  269 */     this.parserState.put("dst", this.rtfDestination);
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  280 */     Enumeration localEnumeration = this.documentAttributes.getAttributeNames();
/*  281 */     while (localEnumeration.hasMoreElements()) {
/*  282 */       Object localObject = localEnumeration.nextElement();
/*  283 */       this.target.putProperty(localObject, this.documentAttributes.getAttribute(localObject));
/*      */     }
/*      */ 
/*  289 */     warning("RTF filter done.");
/*      */ 
/*  291 */     super.close();
/*      */   }
/*      */ 
/*      */   public boolean handleKeyword(String paramString)
/*      */   {
/*  305 */     boolean bool = this.ignoreGroupIfUnknownKeyword;
/*      */ 
/*  307 */     if (this.skippingCharacters > 0) {
/*  308 */       this.skippingCharacters -= 1;
/*  309 */       return true;
/*      */     }
/*      */ 
/*  312 */     this.ignoreGroupIfUnknownKeyword = false;
/*      */     String str;
/*  314 */     if ((str = (String)textKeywords.get(paramString)) != null) {
/*  315 */       handleText(str);
/*  316 */       return true;
/*      */     }
/*      */ 
/*  319 */     if (paramString.equals("fonttbl")) {
/*  320 */       setRTFDestination(new FonttblDestination());
/*  321 */       return true;
/*      */     }
/*      */ 
/*  324 */     if (paramString.equals("colortbl")) {
/*  325 */       setRTFDestination(new ColortblDestination());
/*  326 */       return true;
/*      */     }
/*      */ 
/*  329 */     if (paramString.equals("stylesheet")) {
/*  330 */       setRTFDestination(new StylesheetDestination());
/*  331 */       return true;
/*      */     }
/*      */ 
/*  334 */     if (paramString.equals("info")) {
/*  335 */       setRTFDestination(new InfoDestination());
/*  336 */       return false;
/*      */     }
/*      */ 
/*  339 */     if (paramString.equals("mac")) {
/*  340 */       setCharacterSet("mac");
/*  341 */       return true;
/*      */     }
/*      */ 
/*  344 */     if (paramString.equals("ansi")) {
/*  345 */       if (useNeXTForAnsi)
/*  346 */         setCharacterSet("NeXT");
/*      */       else
/*  348 */         setCharacterSet("ansi");
/*  349 */       return true;
/*      */     }
/*      */ 
/*  352 */     if (paramString.equals("next")) {
/*  353 */       setCharacterSet("NeXT");
/*  354 */       return true;
/*      */     }
/*      */ 
/*  357 */     if (paramString.equals("pc")) {
/*  358 */       setCharacterSet("cpg437");
/*  359 */       return true;
/*      */     }
/*      */ 
/*  362 */     if (paramString.equals("pca")) {
/*  363 */       setCharacterSet("cpg850");
/*  364 */       return true;
/*      */     }
/*      */ 
/*  367 */     if (paramString.equals("*")) {
/*  368 */       this.ignoreGroupIfUnknownKeyword = true;
/*  369 */       return true;
/*      */     }
/*      */ 
/*  372 */     if ((this.rtfDestination != null) && 
/*  373 */       (this.rtfDestination.handleKeyword(paramString))) {
/*  374 */       return true;
/*      */     }
/*      */ 
/*  380 */     if ((paramString.equals("aftncn")) || (paramString.equals("aftnsep")) || (paramString.equals("aftnsepc")) || (paramString.equals("annotation")) || (paramString.equals("atnauthor")) || (paramString.equals("atnicn")) || (paramString.equals("atnid")) || (paramString.equals("atnref")) || (paramString.equals("atntime")) || (paramString.equals("atrfend")) || (paramString.equals("atrfstart")) || (paramString.equals("bkmkend")) || (paramString.equals("bkmkstart")) || (paramString.equals("datafield")) || (paramString.equals("do")) || (paramString.equals("dptxbxtext")) || (paramString.equals("falt")) || (paramString.equals("field")) || (paramString.equals("file")) || (paramString.equals("filetbl")) || (paramString.equals("fname")) || (paramString.equals("fontemb")) || (paramString.equals("fontfile")) || (paramString.equals("footer")) || (paramString.equals("footerf")) || (paramString.equals("footerl")) || (paramString.equals("footerr")) || (paramString.equals("footnote")) || (paramString.equals("ftncn")) || (paramString.equals("ftnsep")) || (paramString.equals("ftnsepc")) || (paramString.equals("header")) || (paramString.equals("headerf")) || (paramString.equals("headerl")) || (paramString.equals("headerr")) || (paramString.equals("keycode")) || (paramString.equals("nextfile")) || (paramString.equals("object")) || (paramString.equals("pict")) || (paramString.equals("pn")) || (paramString.equals("pnseclvl")) || (paramString.equals("pntxtb")) || (paramString.equals("pntxta")) || (paramString.equals("revtbl")) || (paramString.equals("rxe")) || (paramString.equals("tc")) || (paramString.equals("template")) || (paramString.equals("txe")) || (paramString.equals("xe")))
/*      */     {
/*  429 */       bool = true;
/*      */     }
/*      */ 
/*  432 */     if (bool) {
/*  433 */       setRTFDestination(new DiscardingDestination());
/*      */     }
/*      */ 
/*  436 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean handleKeyword(String paramString, int paramInt)
/*      */   {
/*  450 */     boolean bool = this.ignoreGroupIfUnknownKeyword;
/*      */ 
/*  452 */     if (this.skippingCharacters > 0) {
/*  453 */       this.skippingCharacters -= 1;
/*  454 */       return true;
/*      */     }
/*      */ 
/*  457 */     this.ignoreGroupIfUnknownKeyword = false;
/*      */ 
/*  459 */     if (paramString.equals("uc"))
/*      */     {
/*  461 */       this.parserState.put("UnicodeSkip", Integer.valueOf(paramInt));
/*  462 */       return true;
/*      */     }
/*  464 */     if (paramString.equals("u")) {
/*  465 */       if (paramInt < 0)
/*  466 */         paramInt += 65536;
/*  467 */       handleText((char)paramInt);
/*  468 */       Number localNumber = (Number)this.parserState.get("UnicodeSkip");
/*  469 */       if (localNumber != null)
/*  470 */         this.skippingCharacters = localNumber.intValue();
/*      */       else {
/*  472 */         this.skippingCharacters = 1;
/*      */       }
/*  474 */       return true;
/*      */     }
/*      */ 
/*  477 */     if (paramString.equals("rtf")) {
/*  478 */       this.rtfversion = paramInt;
/*  479 */       setRTFDestination(new DocumentDestination());
/*  480 */       return true;
/*      */     }
/*      */ 
/*  483 */     if ((paramString.startsWith("NeXT")) || (paramString.equals("private")))
/*      */     {
/*  485 */       bool = true;
/*      */     }
/*  487 */     if ((this.rtfDestination != null) && 
/*  488 */       (this.rtfDestination.handleKeyword(paramString, paramInt))) {
/*  489 */       return true;
/*      */     }
/*      */ 
/*  494 */     if (bool) {
/*  495 */       setRTFDestination(new DiscardingDestination());
/*      */     }
/*      */ 
/*  498 */     return false;
/*      */   }
/*      */ 
/*      */   private void setTargetAttribute(String paramString, Object paramObject)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setCharacterSet(String paramString)
/*      */   {
/*      */     Object localObject;
/*      */     try
/*      */     {
/*  517 */       localObject = getCharacterSet(paramString);
/*      */     } catch (Exception localException) {
/*  519 */       warning("Exception loading RTF character set \"" + paramString + "\": " + localException);
/*  520 */       localObject = null;
/*      */     }
/*      */ 
/*  523 */     if (localObject != null) {
/*  524 */       this.translationTable = ((char[])localObject);
/*      */     } else {
/*  526 */       warning("Unknown RTF character set \"" + paramString + "\"");
/*  527 */       if (!paramString.equals("ansi")) {
/*      */         try {
/*  529 */           this.translationTable = ((char[])getCharacterSet("ansi"));
/*      */         } catch (IOException localIOException) {
/*  531 */           throw new InternalError("RTFReader: Unable to find character set resources (" + localIOException + ")");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  536 */     setTargetAttribute("rtfCharacterSet", paramString);
/*      */   }
/*      */ 
/*      */   public static void defineCharacterSet(String paramString, char[] paramArrayOfChar)
/*      */   {
/*  544 */     if (paramArrayOfChar.length < 256)
/*  545 */       throw new IllegalArgumentException("Translation table must have 256 entries.");
/*  546 */     characterSets.put(paramString, paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   public static Object getCharacterSet(String paramString)
/*      */     throws IOException
/*      */   {
/*  559 */     char[] arrayOfChar = (char[])characterSets.get(paramString);
/*  560 */     if (arrayOfChar == null)
/*      */     {
/*  562 */       InputStream localInputStream = (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public InputStream run() {
/*  565 */           return RTFReader.class.getResourceAsStream("charsets/" + this.val$name + ".txt");
/*      */         }
/*      */       });
/*  569 */       arrayOfChar = readCharset(localInputStream);
/*  570 */       defineCharacterSet(paramString, arrayOfChar);
/*      */     }
/*  572 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   static char[] readCharset(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*  584 */     char[] arrayOfChar = new char[256];
/*      */ 
/*  586 */     StreamTokenizer localStreamTokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(paramInputStream, "ISO-8859-1")));
/*      */ 
/*  589 */     localStreamTokenizer.eolIsSignificant(false);
/*  590 */     localStreamTokenizer.commentChar(35);
/*  591 */     localStreamTokenizer.slashSlashComments(true);
/*  592 */     localStreamTokenizer.slashStarComments(true);
/*      */ 
/*  594 */     int i = 0;
/*  595 */     while (i < 256) {
/*      */       int j;
/*      */       try {
/*  598 */         j = localStreamTokenizer.nextToken();
/*      */       } catch (Exception localException) {
/*  600 */         throw new IOException("Unable to read from character set file (" + localException + ")");
/*      */       }
/*  602 */       if (j != -2)
/*      */       {
/*  604 */         throw new IOException("Unexpected token in character set file");
/*      */       }
/*      */ 
/*  607 */       arrayOfChar[i] = ((char)(int)localStreamTokenizer.nval);
/*  608 */       i++;
/*      */     }
/*      */ 
/*  611 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   static char[] readCharset(URL paramURL)
/*      */     throws IOException
/*      */   {
/*  617 */     return readCharset(paramURL.openStream());
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  101 */     textKeywords = new Hashtable();
/*  102 */     textKeywords.put("\\", "\\");
/*  103 */     textKeywords.put("{", "{");
/*  104 */     textKeywords.put("}", "}");
/*  105 */     textKeywords.put(" ", " ");
/*  106 */     textKeywords.put("~", " ");
/*  107 */     textKeywords.put("_", "‑");
/*  108 */     textKeywords.put("bullet", "•");
/*  109 */     textKeywords.put("emdash", "—");
/*  110 */     textKeywords.put("emspace", " ");
/*  111 */     textKeywords.put("endash", "–");
/*  112 */     textKeywords.put("enspace", " ");
/*  113 */     textKeywords.put("ldblquote", "“");
/*  114 */     textKeywords.put("lquote", "‘");
/*  115 */     textKeywords.put("ltrmark", "‎");
/*  116 */     textKeywords.put("rdblquote", "”");
/*  117 */     textKeywords.put("rquote", "’");
/*  118 */     textKeywords.put("rtlmark", "‏");
/*  119 */     textKeywords.put("tab", "\t");
/*  120 */     textKeywords.put("zwj", "‍");
/*  121 */     textKeywords.put("zwnj", "‌");
/*      */ 
/*  125 */     textKeywords.put("-", "‧");
/*      */ 
/*  133 */     useNeXTForAnsi = false;
/*      */   }
/*      */ 
/*      */   abstract class AttributeTrackingDestination
/*      */     implements RTFReader.Destination
/*      */   {
/*      */     MutableAttributeSet characterAttributes;
/*      */     MutableAttributeSet paragraphAttributes;
/*      */     MutableAttributeSet sectionAttributes;
/*      */ 
/*      */     public AttributeTrackingDestination()
/*      */     {
/* 1060 */       this.characterAttributes = rootCharacterAttributes();
/* 1061 */       RTFReader.this.parserState.put("chr", this.characterAttributes);
/* 1062 */       this.paragraphAttributes = rootParagraphAttributes();
/* 1063 */       RTFReader.this.parserState.put("pgf", this.paragraphAttributes);
/* 1064 */       this.sectionAttributes = rootSectionAttributes();
/* 1065 */       RTFReader.this.parserState.put("sec", this.sectionAttributes);
/*      */     }
/*      */ 
/*      */     public abstract void handleText(String paramString);
/*      */ 
/*      */     public void handleBinaryBlob(byte[] paramArrayOfByte)
/*      */     {
/* 1075 */       RTFReader.this.warning("Unexpected binary data in RTF file.");
/*      */     }
/*      */ 
/*      */     public void begingroup()
/*      */     {
/* 1080 */       MutableAttributeSet localMutableAttributeSet1 = currentTextAttributes();
/* 1081 */       MutableAttributeSet localMutableAttributeSet2 = currentParagraphAttributes();
/* 1082 */       AttributeSet localAttributeSet = currentSectionAttributes();
/*      */ 
/* 1090 */       this.characterAttributes = new SimpleAttributeSet();
/* 1091 */       this.characterAttributes.addAttributes(localMutableAttributeSet1);
/* 1092 */       RTFReader.this.parserState.put("chr", this.characterAttributes);
/*      */ 
/* 1094 */       this.paragraphAttributes = new SimpleAttributeSet();
/* 1095 */       this.paragraphAttributes.addAttributes(localMutableAttributeSet2);
/* 1096 */       RTFReader.this.parserState.put("pgf", this.paragraphAttributes);
/*      */ 
/* 1098 */       this.sectionAttributes = new SimpleAttributeSet();
/* 1099 */       this.sectionAttributes.addAttributes(localAttributeSet);
/* 1100 */       RTFReader.this.parserState.put("sec", this.sectionAttributes);
/*      */     }
/*      */ 
/*      */     public void endgroup(Dictionary paramDictionary)
/*      */     {
/* 1105 */       this.characterAttributes = ((MutableAttributeSet)RTFReader.this.parserState.get("chr"));
/* 1106 */       this.paragraphAttributes = ((MutableAttributeSet)RTFReader.this.parserState.get("pgf"));
/* 1107 */       this.sectionAttributes = ((MutableAttributeSet)RTFReader.this.parserState.get("sec"));
/*      */     }
/*      */ 
/*      */     public void close()
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean handleKeyword(String paramString)
/*      */     {
/* 1116 */       if (paramString.equals("ulnone")) {
/* 1117 */         return handleKeyword("ul", 0);
/*      */       }
/*      */ 
/* 1121 */       RTFAttribute localRTFAttribute = (RTFAttribute)RTFReader.straightforwardAttributes.get(paramString);
/* 1122 */       if (localRTFAttribute != null)
/*      */       {
/*      */         boolean bool;
/* 1125 */         switch (localRTFAttribute.domain()) {
/*      */         case 0:
/* 1127 */           bool = localRTFAttribute.set(this.characterAttributes);
/* 1128 */           break;
/*      */         case 1:
/* 1130 */           bool = localRTFAttribute.set(this.paragraphAttributes);
/* 1131 */           break;
/*      */         case 2:
/* 1133 */           bool = localRTFAttribute.set(this.sectionAttributes);
/* 1134 */           break;
/*      */         case 4:
/* 1136 */           RTFReader.this.mockery.backing = RTFReader.this.parserState;
/* 1137 */           bool = localRTFAttribute.set(RTFReader.this.mockery);
/* 1138 */           RTFReader.this.mockery.backing = null;
/* 1139 */           break;
/*      */         case 3:
/* 1141 */           bool = localRTFAttribute.set(RTFReader.this.documentAttributes);
/* 1142 */           break;
/*      */         default:
/* 1145 */           bool = false;
/*      */         }
/*      */ 
/* 1148 */         if (bool) {
/* 1149 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1154 */       if (paramString.equals("plain")) {
/* 1155 */         resetCharacterAttributes();
/* 1156 */         return true;
/*      */       }
/*      */ 
/* 1159 */       if (paramString.equals("pard")) {
/* 1160 */         resetParagraphAttributes();
/* 1161 */         return true;
/*      */       }
/*      */ 
/* 1164 */       if (paramString.equals("sectd")) {
/* 1165 */         resetSectionAttributes();
/* 1166 */         return true;
/*      */       }
/*      */ 
/* 1169 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean handleKeyword(String paramString, int paramInt)
/*      */     {
/* 1174 */       int i = paramInt != 0 ? 1 : 0;
/*      */ 
/* 1176 */       if (paramString.equals("fc")) {
/* 1177 */         paramString = "cf";
/*      */       }
/* 1179 */       if (paramString.equals("f")) {
/* 1180 */         RTFReader.this.parserState.put(paramString, Integer.valueOf(paramInt));
/* 1181 */         return true;
/*      */       }
/* 1183 */       if (paramString.equals("cf")) {
/* 1184 */         RTFReader.this.parserState.put(paramString, Integer.valueOf(paramInt));
/* 1185 */         return true;
/*      */       }
/*      */ 
/* 1189 */       RTFAttribute localRTFAttribute = (RTFAttribute)RTFReader.straightforwardAttributes.get(paramString);
/*      */       boolean bool;
/* 1190 */       if (localRTFAttribute != null)
/*      */       {
/* 1193 */         switch (localRTFAttribute.domain()) {
/*      */         case 0:
/* 1195 */           bool = localRTFAttribute.set(this.characterAttributes, paramInt);
/* 1196 */           break;
/*      */         case 1:
/* 1198 */           bool = localRTFAttribute.set(this.paragraphAttributes, paramInt);
/* 1199 */           break;
/*      */         case 2:
/* 1201 */           bool = localRTFAttribute.set(this.sectionAttributes, paramInt);
/* 1202 */           break;
/*      */         case 4:
/* 1204 */           RTFReader.this.mockery.backing = RTFReader.this.parserState;
/* 1205 */           bool = localRTFAttribute.set(RTFReader.this.mockery, paramInt);
/* 1206 */           RTFReader.this.mockery.backing = null;
/* 1207 */           break;
/*      */         case 3:
/* 1209 */           bool = localRTFAttribute.set(RTFReader.this.documentAttributes, paramInt);
/* 1210 */           break;
/*      */         default:
/* 1213 */           bool = false;
/*      */         }
/*      */ 
/* 1216 */         if (bool) {
/* 1217 */           return true;
/*      */         }
/*      */       }
/*      */ 
/* 1221 */       if (paramString.equals("fs")) {
/* 1222 */         StyleConstants.setFontSize(this.characterAttributes, paramInt / 2);
/* 1223 */         return true;
/*      */       }
/*      */ 
/* 1228 */       if (paramString.equals("sl")) {
/* 1229 */         if (paramInt == 1000) {
/* 1230 */           this.characterAttributes.removeAttribute(StyleConstants.LineSpacing);
/*      */         }
/*      */         else
/*      */         {
/* 1236 */           StyleConstants.setLineSpacing(this.characterAttributes, paramInt / 20.0F);
/*      */         }
/*      */ 
/* 1239 */         return true;
/*      */       }
/*      */ 
/* 1244 */       if ((paramString.equals("tx")) || (paramString.equals("tb"))) {
/* 1245 */         float f = paramInt / 20.0F;
/*      */ 
/* 1249 */         bool = false;
/* 1250 */         Number localNumber = (Number)RTFReader.this.parserState.get("tab_alignment");
/*      */         int j;
/* 1251 */         if (localNumber != null)
/* 1252 */           j = localNumber.intValue();
/* 1253 */         int k = 0;
/* 1254 */         localNumber = (Number)RTFReader.this.parserState.get("tab_leader");
/* 1255 */         if (localNumber != null)
/* 1256 */           k = localNumber.intValue();
/* 1257 */         if (paramString.equals("tb")) {
/* 1258 */           j = 5;
/*      */         }
/* 1260 */         RTFReader.this.parserState.remove("tab_alignment");
/* 1261 */         RTFReader.this.parserState.remove("tab_leader");
/*      */ 
/* 1263 */         TabStop localTabStop = new TabStop(f, j, k);
/*      */ 
/* 1267 */         Object localObject = (Dictionary)RTFReader.this.parserState.get("_tabs");
/*      */         Integer localInteger;
/* 1268 */         if (localObject == null) {
/* 1269 */           localObject = new Hashtable();
/* 1270 */           RTFReader.this.parserState.put("_tabs", localObject);
/* 1271 */           localInteger = Integer.valueOf(1);
/*      */         } else {
/* 1273 */           localInteger = (Integer)((Dictionary)localObject).get("stop count");
/* 1274 */           localInteger = Integer.valueOf(1 + localInteger.intValue());
/*      */         }
/* 1276 */         ((Dictionary)localObject).put(localInteger, localTabStop);
/* 1277 */         ((Dictionary)localObject).put("stop count", localInteger);
/* 1278 */         RTFReader.this.parserState.remove("_tabs_immutable");
/*      */ 
/* 1280 */         return true;
/*      */       }
/*      */ 
/* 1283 */       if ((paramString.equals("s")) && (RTFReader.this.paragraphStyles != null))
/*      */       {
/* 1285 */         RTFReader.this.parserState.put("paragraphStyle", RTFReader.this.paragraphStyles[paramInt]);
/* 1286 */         return true;
/*      */       }
/*      */ 
/* 1289 */       if ((paramString.equals("cs")) && (RTFReader.this.characterStyles != null))
/*      */       {
/* 1291 */         RTFReader.this.parserState.put("characterStyle", RTFReader.this.characterStyles[paramInt]);
/* 1292 */         return true;
/*      */       }
/*      */ 
/* 1295 */       if ((paramString.equals("ds")) && (RTFReader.this.sectionStyles != null))
/*      */       {
/* 1297 */         RTFReader.this.parserState.put("sectionStyle", RTFReader.this.sectionStyles[paramInt]);
/* 1298 */         return true;
/*      */       }
/*      */ 
/* 1301 */       return false;
/*      */     }
/*      */ 
/*      */     protected MutableAttributeSet rootCharacterAttributes()
/*      */     {
/* 1308 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/*      */ 
/* 1312 */       StyleConstants.setItalic(localSimpleAttributeSet, false);
/* 1313 */       StyleConstants.setBold(localSimpleAttributeSet, false);
/* 1314 */       StyleConstants.setUnderline(localSimpleAttributeSet, false);
/* 1315 */       StyleConstants.setForeground(localSimpleAttributeSet, RTFReader.this.defaultColor());
/*      */ 
/* 1317 */       return localSimpleAttributeSet;
/*      */     }
/*      */ 
/*      */     protected MutableAttributeSet rootParagraphAttributes()
/*      */     {
/* 1324 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/*      */ 
/* 1326 */       StyleConstants.setLeftIndent(localSimpleAttributeSet, 0.0F);
/* 1327 */       StyleConstants.setRightIndent(localSimpleAttributeSet, 0.0F);
/* 1328 */       StyleConstants.setFirstLineIndent(localSimpleAttributeSet, 0.0F);
/*      */ 
/* 1331 */       localSimpleAttributeSet.setResolveParent(RTFReader.this.target.getStyle("default"));
/*      */ 
/* 1333 */       return localSimpleAttributeSet;
/*      */     }
/*      */ 
/*      */     protected MutableAttributeSet rootSectionAttributes()
/*      */     {
/* 1340 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/*      */ 
/* 1342 */       return localSimpleAttributeSet;
/*      */     }
/*      */ 
/*      */     MutableAttributeSet currentTextAttributes()
/*      */     {
/* 1353 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet(this.characterAttributes);
/*      */ 
/* 1362 */       Integer localInteger1 = (Integer)RTFReader.this.parserState.get("f");
/*      */       String str;
/* 1365 */       if (localInteger1 != null)
/* 1366 */         str = (String)RTFReader.this.fontTable.get(localInteger1);
/*      */       else
/* 1368 */         str = null;
/* 1369 */       if (str != null)
/* 1370 */         StyleConstants.setFontFamily(localSimpleAttributeSet, str);
/*      */       else
/* 1372 */         localSimpleAttributeSet.removeAttribute(StyleConstants.FontFamily);
/*      */       Integer localInteger2;
/* 1374 */       if (RTFReader.this.colorTable != null) {
/* 1375 */         localInteger2 = (Integer)RTFReader.this.parserState.get("cf");
/* 1376 */         if (localInteger2 != null) {
/* 1377 */           localObject = RTFReader.this.colorTable[localInteger2.intValue()];
/* 1378 */           StyleConstants.setForeground(localSimpleAttributeSet, (Color)localObject);
/*      */         }
/*      */         else {
/* 1381 */           localSimpleAttributeSet.removeAttribute(StyleConstants.Foreground);
/*      */         }
/*      */       }
/*      */ 
/* 1385 */       if (RTFReader.this.colorTable != null) {
/* 1386 */         localInteger2 = (Integer)RTFReader.this.parserState.get("cb");
/* 1387 */         if (localInteger2 != null) {
/* 1388 */           localObject = RTFReader.this.colorTable[localInteger2.intValue()];
/* 1389 */           localSimpleAttributeSet.addAttribute(StyleConstants.Background, localObject);
/*      */         }
/*      */         else
/*      */         {
/* 1393 */           localSimpleAttributeSet.removeAttribute(StyleConstants.Background);
/*      */         }
/*      */       }
/*      */ 
/* 1397 */       Object localObject = (Style)RTFReader.this.parserState.get("characterStyle");
/* 1398 */       if (localObject != null) {
/* 1399 */         localSimpleAttributeSet.setResolveParent((AttributeSet)localObject);
/*      */       }
/*      */ 
/* 1403 */       return localSimpleAttributeSet;
/*      */     }
/*      */ 
/*      */     MutableAttributeSet currentParagraphAttributes()
/*      */     {
/* 1416 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet(this.paragraphAttributes);
/*      */ 
/* 1423 */       TabStop[] arrayOfTabStop = (TabStop[])RTFReader.this.parserState.get("_tabs_immutable");
/* 1424 */       if (arrayOfTabStop == null) {
/* 1425 */         localObject = (Dictionary)RTFReader.this.parserState.get("_tabs");
/* 1426 */         if (localObject != null) {
/* 1427 */           int i = ((Integer)((Dictionary)localObject).get("stop count")).intValue();
/* 1428 */           arrayOfTabStop = new TabStop[i];
/* 1429 */           for (int j = 1; j <= i; j++)
/* 1430 */             arrayOfTabStop[(j - 1)] = ((TabStop)((Dictionary)localObject).get(Integer.valueOf(j)));
/* 1431 */           RTFReader.this.parserState.put("_tabs_immutable", arrayOfTabStop);
/*      */         }
/*      */       }
/* 1434 */       if (arrayOfTabStop != null) {
/* 1435 */         localSimpleAttributeSet.addAttribute("tabs", arrayOfTabStop);
/*      */       }
/* 1437 */       Object localObject = (Style)RTFReader.this.parserState.get("paragraphStyle");
/* 1438 */       if (localObject != null) {
/* 1439 */         localSimpleAttributeSet.setResolveParent((AttributeSet)localObject);
/*      */       }
/* 1441 */       return localSimpleAttributeSet;
/*      */     }
/*      */ 
/*      */     public AttributeSet currentSectionAttributes()
/*      */     {
/* 1452 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet(this.sectionAttributes);
/*      */ 
/* 1454 */       Style localStyle = (Style)RTFReader.this.parserState.get("sectionStyle");
/* 1455 */       if (localStyle != null) {
/* 1456 */         localSimpleAttributeSet.setResolveParent(localStyle);
/*      */       }
/* 1458 */       return localSimpleAttributeSet;
/*      */     }
/*      */ 
/*      */     protected void resetCharacterAttributes()
/*      */     {
/* 1466 */       handleKeyword("f", 0);
/* 1467 */       handleKeyword("cf", 0);
/*      */ 
/* 1469 */       handleKeyword("fs", 24);
/*      */ 
/* 1471 */       Enumeration localEnumeration = RTFReader.straightforwardAttributes.elements();
/* 1472 */       while (localEnumeration.hasMoreElements()) {
/* 1473 */         RTFAttribute localRTFAttribute = (RTFAttribute)localEnumeration.nextElement();
/* 1474 */         if (localRTFAttribute.domain() == 0) {
/* 1475 */           localRTFAttribute.setDefault(this.characterAttributes);
/*      */         }
/*      */       }
/* 1478 */       handleKeyword("sl", 1000);
/*      */ 
/* 1480 */       RTFReader.this.parserState.remove("characterStyle");
/*      */     }
/*      */ 
/*      */     protected void resetParagraphAttributes()
/*      */     {
/* 1488 */       RTFReader.this.parserState.remove("_tabs");
/* 1489 */       RTFReader.this.parserState.remove("_tabs_immutable");
/* 1490 */       RTFReader.this.parserState.remove("paragraphStyle");
/*      */ 
/* 1492 */       StyleConstants.setAlignment(this.paragraphAttributes, 0);
/*      */ 
/* 1495 */       Enumeration localEnumeration = RTFReader.straightforwardAttributes.elements();
/* 1496 */       while (localEnumeration.hasMoreElements()) {
/* 1497 */         RTFAttribute localRTFAttribute = (RTFAttribute)localEnumeration.nextElement();
/* 1498 */         if (localRTFAttribute.domain() == 1)
/* 1499 */           localRTFAttribute.setDefault(this.characterAttributes);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void resetSectionAttributes()
/*      */     {
/* 1508 */       Enumeration localEnumeration = RTFReader.straightforwardAttributes.elements();
/* 1509 */       while (localEnumeration.hasMoreElements()) {
/* 1510 */         RTFAttribute localRTFAttribute = (RTFAttribute)localEnumeration.nextElement();
/* 1511 */         if (localRTFAttribute.domain() == 2) {
/* 1512 */           localRTFAttribute.setDefault(this.characterAttributes);
/*      */         }
/*      */       }
/* 1515 */       RTFReader.this.parserState.remove("sectionStyle");
/*      */     }
/*      */   }
/*      */ 
/*      */   class ColortblDestination
/*      */     implements RTFReader.Destination
/*      */   {
/*      */     int red;
/*      */     int green;
/*      */     int blue;
/*      */     Vector<Color> proTemTable;
/*      */ 
/*      */     public ColortblDestination()
/*      */     {
/*  765 */       this.red = 0;
/*  766 */       this.green = 0;
/*  767 */       this.blue = 0;
/*  768 */       this.proTemTable = new Vector();
/*      */     }
/*      */ 
/*      */     public void handleText(String paramString)
/*      */     {
/*  775 */       for (int i = 0; i < paramString.length(); i++)
/*  776 */         if (paramString.charAt(i) == ';')
/*      */         {
/*  778 */           Color localColor = new Color(this.red, this.green, this.blue);
/*  779 */           this.proTemTable.addElement(localColor);
/*      */         }
/*      */     }
/*      */ 
/*      */     public void close()
/*      */     {
/*  786 */       int i = this.proTemTable.size();
/*  787 */       RTFReader.this.warning("Done reading color table, " + i + " entries.");
/*  788 */       RTFReader.this.colorTable = new Color[i];
/*  789 */       this.proTemTable.copyInto(RTFReader.this.colorTable);
/*      */     }
/*      */ 
/*      */     public boolean handleKeyword(String paramString, int paramInt)
/*      */     {
/*  794 */       if (paramString.equals("red"))
/*  795 */         this.red = paramInt;
/*  796 */       else if (paramString.equals("green"))
/*  797 */         this.green = paramInt;
/*  798 */       else if (paramString.equals("blue"))
/*  799 */         this.blue = paramInt;
/*      */       else {
/*  801 */         return false;
/*      */       }
/*  803 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean handleKeyword(String paramString) {
/*  807 */       return false;
/*      */     }
/*      */ 
/*      */     public void begingroup()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void endgroup(Dictionary paramDictionary)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleBinaryBlob(byte[] paramArrayOfByte)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface Destination
/*      */   {
/*      */     public abstract void handleBinaryBlob(byte[] paramArrayOfByte);
/*      */ 
/*      */     public abstract void handleText(String paramString);
/*      */ 
/*      */     public abstract boolean handleKeyword(String paramString);
/*      */ 
/*      */     public abstract boolean handleKeyword(String paramString, int paramInt);
/*      */ 
/*      */     public abstract void begingroup();
/*      */ 
/*      */     public abstract void endgroup(Dictionary paramDictionary);
/*      */ 
/*      */     public abstract void close();
/*      */   }
/*      */ 
/*      */   class DiscardingDestination
/*      */     implements RTFReader.Destination
/*      */   {
/*      */     DiscardingDestination()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleBinaryBlob(byte[] paramArrayOfByte)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleText(String paramString)
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean handleKeyword(String paramString)
/*      */     {
/*  656 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean handleKeyword(String paramString, int paramInt)
/*      */     {
/*  662 */       return true;
/*      */     }
/*      */ 
/*      */     public void begingroup()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void endgroup(Dictionary paramDictionary)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void close()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   class DocumentDestination extends RTFReader.TextHandlingDestination
/*      */     implements RTFReader.Destination
/*      */   {
/*      */     DocumentDestination()
/*      */     {
/* 1604 */       super();
/*      */     }
/*      */ 
/*      */     public void deliverText(String paramString, AttributeSet paramAttributeSet)
/*      */     {
/*      */       try
/*      */       {
/* 1611 */         RTFReader.this.target.insertString(RTFReader.this.target.getLength(), paramString, currentTextAttributes());
/*      */       }
/*      */       catch (BadLocationException localBadLocationException)
/*      */       {
/* 1617 */         throw new InternalError(localBadLocationException.getMessage());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void finishParagraph(AttributeSet paramAttributeSet1, AttributeSet paramAttributeSet2)
/*      */     {
/* 1624 */       int i = RTFReader.this.target.getLength();
/*      */       try {
/* 1626 */         RTFReader.this.target.insertString(i, "\n", paramAttributeSet2);
/* 1627 */         RTFReader.this.target.setParagraphAttributes(i, 1, paramAttributeSet1, true);
/*      */       }
/*      */       catch (BadLocationException localBadLocationException)
/*      */       {
/* 1631 */         throw new InternalError(localBadLocationException.getMessage());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void endSection()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   class FonttblDestination
/*      */     implements RTFReader.Destination
/*      */   {
/*      */     int nextFontNumber;
/*  687 */     Integer fontNumberKey = null;
/*      */     String nextFontFamily;
/*      */ 
/*      */     FonttblDestination()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleBinaryBlob(byte[] paramArrayOfByte)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void handleText(String paramString)
/*      */     {
/*  695 */       int i = paramString.indexOf(';');
/*      */       String str;
/*  698 */       if (i > -1)
/*  699 */         str = paramString.substring(0, i);
/*      */       else {
/*  701 */         str = paramString;
/*      */       }
/*      */ 
/*  706 */       if ((this.nextFontNumber == -1) && (this.fontNumberKey != null))
/*      */       {
/*  709 */         str = (String)RTFReader.this.fontTable.get(this.fontNumberKey) + str;
/*      */       }
/*  711 */       else this.fontNumberKey = Integer.valueOf(this.nextFontNumber);
/*      */ 
/*  713 */       RTFReader.this.fontTable.put(this.fontNumberKey, str);
/*      */ 
/*  715 */       this.nextFontNumber = -1;
/*  716 */       this.nextFontFamily = null;
/*      */     }
/*      */ 
/*      */     public boolean handleKeyword(String paramString)
/*      */     {
/*  721 */       if (paramString.charAt(0) == 'f') {
/*  722 */         this.nextFontFamily = paramString.substring(1);
/*  723 */         return true;
/*      */       }
/*      */ 
/*  726 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean handleKeyword(String paramString, int paramInt)
/*      */     {
/*  731 */       if (paramString.equals("f")) {
/*  732 */         this.nextFontNumber = paramInt;
/*  733 */         return true;
/*      */       }
/*      */ 
/*  736 */       return false;
/*      */     }
/*      */ 
/*      */     public void begingroup()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void endgroup(Dictionary paramDictionary) {
/*      */     }
/*      */ 
/*      */     public void close() {
/*  747 */       Enumeration localEnumeration = RTFReader.this.fontTable.keys();
/*  748 */       RTFReader.this.warning("Done reading font table.");
/*  749 */       while (localEnumeration.hasMoreElements()) {
/*  750 */         Integer localInteger = (Integer)localEnumeration.nextElement();
/*  751 */         RTFReader.this.warning("Number " + localInteger + ": " + (String)RTFReader.this.fontTable.get(localInteger));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class InfoDestination extends RTFReader.DiscardingDestination
/*      */     implements RTFReader.Destination
/*      */   {
/*      */     InfoDestination()
/*      */     {
/* 1035 */       super();
/*      */     }
/*      */   }
/*      */ 
/*      */   class StylesheetDestination extends RTFReader.DiscardingDestination
/*      */     implements RTFReader.Destination
/*      */   {
/*      */     Dictionary<Integer, StyleDefiningDestination> definedStyles;
/*      */ 
/*      */     public StylesheetDestination()
/*      */     {
/*  826 */       super();
/*  827 */       this.definedStyles = new Hashtable();
/*      */     }
/*      */ 
/*      */     public void begingroup()
/*      */     {
/*  832 */       RTFReader.this.setRTFDestination(new StyleDefiningDestination());
/*      */     }
/*      */ 
/*      */     public void close()
/*      */     {
/*  837 */       Vector localVector1 = new Vector();
/*  838 */       Vector localVector2 = new Vector();
/*  839 */       Vector localVector3 = new Vector();
/*  840 */       Enumeration localEnumeration = this.definedStyles.elements();
/*      */       Object localObject;
/*  841 */       while (localEnumeration.hasMoreElements())
/*      */       {
/*  844 */         localObject = (StyleDefiningDestination)localEnumeration.nextElement();
/*  845 */         Style localStyle = ((StyleDefiningDestination)localObject).realize();
/*  846 */         RTFReader.this.warning("Style " + ((StyleDefiningDestination)localObject).number + " (" + ((StyleDefiningDestination)localObject).styleName + "): " + localStyle);
/*  847 */         String str = (String)localStyle.getAttribute("style:type");
/*      */         Vector localVector4;
/*  849 */         if (str.equals("section"))
/*  850 */           localVector4 = localVector3;
/*  851 */         else if (str.equals("character"))
/*  852 */           localVector4 = localVector1;
/*      */         else {
/*  854 */           localVector4 = localVector2;
/*      */         }
/*  856 */         if (localVector4.size() <= ((StyleDefiningDestination)localObject).number)
/*  857 */           localVector4.setSize(((StyleDefiningDestination)localObject).number + 1);
/*  858 */         localVector4.setElementAt(localStyle, ((StyleDefiningDestination)localObject).number);
/*      */       }
/*  860 */       if (!localVector1.isEmpty()) {
/*  861 */         localObject = new Style[localVector1.size()];
/*  862 */         localVector1.copyInto((Object[])localObject);
/*  863 */         RTFReader.this.characterStyles = ((Style[])localObject);
/*      */       }
/*  865 */       if (!localVector2.isEmpty()) {
/*  866 */         localObject = new Style[localVector2.size()];
/*  867 */         localVector2.copyInto((Object[])localObject);
/*  868 */         RTFReader.this.paragraphStyles = ((Style[])localObject);
/*      */       }
/*  870 */       if (!localVector3.isEmpty()) {
/*  871 */         localObject = new Style[localVector3.size()];
/*  872 */         localVector3.copyInto((Object[])localObject);
/*  873 */         RTFReader.this.sectionStyles = ((Style[])localObject);
/*      */       }
/*      */     }
/*      */ 
/*      */     class StyleDefiningDestination extends RTFReader.AttributeTrackingDestination
/*      */       implements RTFReader.Destination
/*      */     {
/*  901 */       final int STYLENUMBER_NONE = 222;
/*      */       boolean additive;
/*      */       boolean characterStyle;
/*      */       boolean sectionStyle;
/*      */       public String styleName;
/*      */       public int number;
/*      */       int basedOn;
/*      */       int nextStyle;
/*      */       boolean hidden;
/*      */       Style realizedStyle;
/*      */ 
/*      */       public StyleDefiningDestination()
/*      */       {
/*  914 */         super();
/*  915 */         this.additive = false;
/*  916 */         this.characterStyle = false;
/*  917 */         this.sectionStyle = false;
/*  918 */         this.styleName = null;
/*  919 */         this.number = 0;
/*  920 */         this.basedOn = 222;
/*  921 */         this.nextStyle = 222;
/*  922 */         this.hidden = false;
/*      */       }
/*      */ 
/*      */       public void handleText(String paramString)
/*      */       {
/*  927 */         if (this.styleName != null)
/*  928 */           this.styleName += paramString;
/*      */         else
/*  930 */           this.styleName = paramString;
/*      */       }
/*      */ 
/*      */       public void close() {
/*  934 */         int i = this.styleName == null ? 0 : this.styleName.indexOf(';');
/*  935 */         if (i > 0)
/*  936 */           this.styleName = this.styleName.substring(0, i);
/*  937 */         RTFReader.StylesheetDestination.this.definedStyles.put(Integer.valueOf(this.number), this);
/*  938 */         super.close();
/*      */       }
/*      */ 
/*      */       public boolean handleKeyword(String paramString)
/*      */       {
/*  943 */         if (paramString.equals("additive")) {
/*  944 */           this.additive = true;
/*  945 */           return true;
/*      */         }
/*  947 */         if (paramString.equals("shidden")) {
/*  948 */           this.hidden = true;
/*  949 */           return true;
/*      */         }
/*  951 */         return super.handleKeyword(paramString);
/*      */       }
/*      */ 
/*      */       public boolean handleKeyword(String paramString, int paramInt)
/*      */       {
/*  956 */         if (paramString.equals("s")) {
/*  957 */           this.characterStyle = false;
/*  958 */           this.sectionStyle = false;
/*  959 */           this.number = paramInt;
/*  960 */         } else if (paramString.equals("cs")) {
/*  961 */           this.characterStyle = true;
/*  962 */           this.sectionStyle = false;
/*  963 */           this.number = paramInt;
/*  964 */         } else if (paramString.equals("ds")) {
/*  965 */           this.characterStyle = false;
/*  966 */           this.sectionStyle = true;
/*  967 */           this.number = paramInt;
/*  968 */         } else if (paramString.equals("sbasedon")) {
/*  969 */           this.basedOn = paramInt;
/*  970 */         } else if (paramString.equals("snext")) {
/*  971 */           this.nextStyle = paramInt;
/*      */         } else {
/*  973 */           return super.handleKeyword(paramString, paramInt);
/*      */         }
/*  975 */         return true;
/*      */       }
/*      */ 
/*      */       public Style realize()
/*      */       {
/*  980 */         Style localStyle1 = null;
/*  981 */         Style localStyle2 = null;
/*      */ 
/*  983 */         if (this.realizedStyle != null)
/*  984 */           return this.realizedStyle;
/*      */         StyleDefiningDestination localStyleDefiningDestination;
/*  986 */         if (this.basedOn != 222)
/*      */         {
/*  988 */           localStyleDefiningDestination = (StyleDefiningDestination)RTFReader.StylesheetDestination.this.definedStyles.get(Integer.valueOf(this.basedOn));
/*  989 */           if ((localStyleDefiningDestination != null) && (localStyleDefiningDestination != this)) {
/*  990 */             localStyle1 = localStyleDefiningDestination.realize();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  997 */         this.realizedStyle = RTFReader.this.target.addStyle(this.styleName, localStyle1);
/*      */ 
/*  999 */         if (this.characterStyle) {
/* 1000 */           this.realizedStyle.addAttributes(currentTextAttributes());
/* 1001 */           this.realizedStyle.addAttribute("style:type", "character");
/*      */         }
/* 1003 */         else if (this.sectionStyle) {
/* 1004 */           this.realizedStyle.addAttributes(currentSectionAttributes());
/* 1005 */           this.realizedStyle.addAttribute("style:type", "section");
/*      */         }
/*      */         else {
/* 1008 */           this.realizedStyle.addAttributes(currentParagraphAttributes());
/* 1009 */           this.realizedStyle.addAttribute("style:type", "paragraph");
/*      */         }
/*      */ 
/* 1013 */         if (this.nextStyle != 222)
/*      */         {
/* 1015 */           localStyleDefiningDestination = (StyleDefiningDestination)RTFReader.StylesheetDestination.this.definedStyles.get(Integer.valueOf(this.nextStyle));
/* 1016 */           if (localStyleDefiningDestination != null) {
/* 1017 */             localStyle2 = localStyleDefiningDestination.realize();
/*      */           }
/*      */         }
/*      */ 
/* 1021 */         if (localStyle2 != null)
/* 1022 */           this.realizedStyle.addAttribute("style:nextStyle", localStyle2);
/* 1023 */         this.realizedStyle.addAttribute("style:additive", Boolean.valueOf(this.additive));
/*      */ 
/* 1025 */         this.realizedStyle.addAttribute("style:hidden", Boolean.valueOf(this.hidden));
/*      */ 
/* 1028 */         return this.realizedStyle;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   abstract class TextHandlingDestination extends RTFReader.AttributeTrackingDestination
/*      */     implements RTFReader.Destination
/*      */   {
/*      */     boolean inParagraph;
/*      */ 
/*      */     public TextHandlingDestination()
/*      */     {
/* 1538 */       super();
/* 1539 */       this.inParagraph = false;
/*      */     }
/*      */ 
/*      */     public void handleText(String paramString)
/*      */     {
/* 1544 */       if (!this.inParagraph) {
/* 1545 */         beginParagraph();
/*      */       }
/* 1547 */       deliverText(paramString, currentTextAttributes());
/*      */     }
/*      */ 
/*      */     abstract void deliverText(String paramString, AttributeSet paramAttributeSet);
/*      */ 
/*      */     public void close()
/*      */     {
/* 1554 */       if (this.inParagraph) {
/* 1555 */         endParagraph();
/*      */       }
/* 1557 */       super.close();
/*      */     }
/*      */ 
/*      */     public boolean handleKeyword(String paramString)
/*      */     {
/* 1562 */       if ((paramString.equals("\r")) || (paramString.equals("\n"))) {
/* 1563 */         paramString = "par";
/*      */       }
/*      */ 
/* 1566 */       if (paramString.equals("par"))
/*      */       {
/* 1568 */         endParagraph();
/* 1569 */         return true;
/*      */       }
/*      */ 
/* 1572 */       if (paramString.equals("sect"))
/*      */       {
/* 1574 */         endSection();
/* 1575 */         return true;
/*      */       }
/*      */ 
/* 1578 */       return super.handleKeyword(paramString);
/*      */     }
/*      */ 
/*      */     protected void beginParagraph()
/*      */     {
/* 1583 */       this.inParagraph = true;
/*      */     }
/*      */ 
/*      */     protected void endParagraph()
/*      */     {
/* 1588 */       MutableAttributeSet localMutableAttributeSet1 = currentParagraphAttributes();
/* 1589 */       MutableAttributeSet localMutableAttributeSet2 = currentTextAttributes();
/* 1590 */       finishParagraph(localMutableAttributeSet1, localMutableAttributeSet2);
/* 1591 */       this.inParagraph = false;
/*      */     }
/*      */ 
/*      */     abstract void finishParagraph(AttributeSet paramAttributeSet1, AttributeSet paramAttributeSet2);
/*      */ 
/*      */     abstract void endSection();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.rtf.RTFReader
 * JD-Core Version:    0.6.2
 */