/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.spi.LocaleNameProvider;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.util.LocaleServiceProviderPool;
/*      */ import sun.util.LocaleServiceProviderPool.LocalizedObjectGetter;
/*      */ import sun.util.locale.BaseLocale;
/*      */ import sun.util.locale.InternalLocaleBuilder;
/*      */ import sun.util.locale.LanguageTag;
/*      */ import sun.util.locale.LocaleExtensions;
/*      */ import sun.util.locale.LocaleObjectCache;
/*      */ import sun.util.locale.LocaleSyntaxException;
/*      */ import sun.util.locale.LocaleUtils;
/*      */ import sun.util.locale.ParseStatus;
/*      */ import sun.util.locale.UnicodeLocaleExtension;
/*      */ import sun.util.resources.LocaleData;
/*      */ import sun.util.resources.OpenListResourceBundle;
/*      */ 
/*      */ public final class Locale
/*      */   implements Cloneable, Serializable
/*      */ {
/*  411 */   private static final Cache LOCALECACHE = new Cache(null);
/*      */ 
/*  415 */   public static final Locale ENGLISH = createConstant("en", "");
/*      */ 
/*  419 */   public static final Locale FRENCH = createConstant("fr", "");
/*      */ 
/*  423 */   public static final Locale GERMAN = createConstant("de", "");
/*      */ 
/*  427 */   public static final Locale ITALIAN = createConstant("it", "");
/*      */ 
/*  431 */   public static final Locale JAPANESE = createConstant("ja", "");
/*      */ 
/*  435 */   public static final Locale KOREAN = createConstant("ko", "");
/*      */ 
/*  439 */   public static final Locale CHINESE = createConstant("zh", "");
/*      */ 
/*  443 */   public static final Locale SIMPLIFIED_CHINESE = createConstant("zh", "CN");
/*      */ 
/*  447 */   public static final Locale TRADITIONAL_CHINESE = createConstant("zh", "TW");
/*      */ 
/*  451 */   public static final Locale FRANCE = createConstant("fr", "FR");
/*      */ 
/*  455 */   public static final Locale GERMANY = createConstant("de", "DE");
/*      */ 
/*  459 */   public static final Locale ITALY = createConstant("it", "IT");
/*      */ 
/*  463 */   public static final Locale JAPAN = createConstant("ja", "JP");
/*      */ 
/*  467 */   public static final Locale KOREA = createConstant("ko", "KR");
/*      */ 
/*  471 */   public static final Locale CHINA = SIMPLIFIED_CHINESE;
/*      */ 
/*  475 */   public static final Locale PRC = SIMPLIFIED_CHINESE;
/*      */ 
/*  479 */   public static final Locale TAIWAN = TRADITIONAL_CHINESE;
/*      */ 
/*  483 */   public static final Locale UK = createConstant("en", "GB");
/*      */ 
/*  487 */   public static final Locale US = createConstant("en", "US");
/*      */ 
/*  491 */   public static final Locale CANADA = createConstant("en", "CA");
/*      */ 
/*  495 */   public static final Locale CANADA_FRENCH = createConstant("fr", "CA");
/*      */ 
/*  505 */   public static final Locale ROOT = createConstant("", "");
/*      */   public static final char PRIVATE_USE_EXTENSION = 'x';
/*      */   public static final char UNICODE_LOCALE_EXTENSION = 'u';
/*      */   static final long serialVersionUID = 9149081749638150636L;
/*      */   private static final int DISPLAY_LANGUAGE = 0;
/*      */   private static final int DISPLAY_COUNTRY = 1;
/*      */   private static final int DISPLAY_VARIANT = 2;
/*      */   private static final int DISPLAY_SCRIPT = 3;
/*      */   private transient BaseLocale baseLocale;
/*      */   private transient LocaleExtensions localeExtensions;
/* 1917 */   private volatile transient int hashCodeValue = 0;
/*      */ 
/* 1919 */   private static Locale defaultLocale = null;
/* 1920 */   private static Locale defaultDisplayLocale = null;
/* 1921 */   private static Locale defaultFormatLocale = null;
/*      */ 
/* 2026 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("language", String.class), new ObjectStreamField("country", String.class), new ObjectStreamField("variant", String.class), new ObjectStreamField("hashcode", Integer.TYPE), new ObjectStreamField("script", String.class), new ObjectStreamField("extensions", String.class) };
/*      */ 
/* 2100 */   private static volatile String[] isoLanguages = null;
/*      */ 
/* 2102 */   private static volatile String[] isoCountries = null;
/*      */ 
/*      */   private Locale(BaseLocale paramBaseLocale, LocaleExtensions paramLocaleExtensions)
/*      */   {
/*  541 */     this.baseLocale = paramBaseLocale;
/*  542 */     this.localeExtensions = paramLocaleExtensions;
/*      */   }
/*      */ 
/*      */   public Locale(String paramString1, String paramString2, String paramString3)
/*      */   {
/*  572 */     if ((paramString1 == null) || (paramString2 == null) || (paramString3 == null)) {
/*  573 */       throw new NullPointerException();
/*      */     }
/*  575 */     this.baseLocale = BaseLocale.getInstance(convertOldISOCodes(paramString1), "", paramString2, paramString3);
/*  576 */     this.localeExtensions = getCompatibilityExtensions(paramString1, "", paramString2, paramString3);
/*      */   }
/*      */ 
/*      */   public Locale(String paramString1, String paramString2)
/*      */   {
/*  602 */     this(paramString1, paramString2, "");
/*      */   }
/*      */ 
/*      */   public Locale(String paramString)
/*      */   {
/*  626 */     this(paramString, "", "");
/*      */   }
/*      */ 
/*      */   private static Locale createConstant(String paramString1, String paramString2)
/*      */   {
/*  634 */     BaseLocale localBaseLocale = BaseLocale.createInstance(paramString1, paramString2);
/*  635 */     return getInstance(localBaseLocale, null);
/*      */   }
/*      */ 
/*      */   static Locale getInstance(String paramString1, String paramString2, String paramString3)
/*      */   {
/*  653 */     return getInstance(paramString1, "", paramString2, paramString3, null);
/*      */   }
/*      */ 
/*      */   static Locale getInstance(String paramString1, String paramString2, String paramString3, String paramString4, LocaleExtensions paramLocaleExtensions)
/*      */   {
/*  658 */     if ((paramString1 == null) || (paramString2 == null) || (paramString3 == null) || (paramString4 == null)) {
/*  659 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  662 */     if (paramLocaleExtensions == null) {
/*  663 */       paramLocaleExtensions = getCompatibilityExtensions(paramString1, paramString2, paramString3, paramString4);
/*      */     }
/*      */ 
/*  666 */     BaseLocale localBaseLocale = BaseLocale.getInstance(paramString1, paramString2, paramString3, paramString4);
/*  667 */     return getInstance(localBaseLocale, paramLocaleExtensions);
/*      */   }
/*      */ 
/*      */   static Locale getInstance(BaseLocale paramBaseLocale, LocaleExtensions paramLocaleExtensions) {
/*  671 */     LocaleKey localLocaleKey = new LocaleKey(paramBaseLocale, paramLocaleExtensions, null);
/*  672 */     return (Locale)LOCALECACHE.get(localLocaleKey);
/*      */   }
/*      */ 
/*      */   public static Locale getDefault()
/*      */   {
/*  741 */     if (defaultLocale == null) {
/*  742 */       initDefault();
/*      */     }
/*  744 */     return defaultLocale;
/*      */   }
/*      */ 
/*      */   public static Locale getDefault(Category paramCategory)
/*      */   {
/*  766 */     switch (1.$SwitchMap$java$util$Locale$Category[paramCategory.ordinal()]) {
/*      */     case 1:
/*  768 */       if (defaultDisplayLocale == null) {
/*  769 */         initDefault(paramCategory);
/*      */       }
/*  771 */       return defaultDisplayLocale;
/*      */     case 2:
/*  773 */       if (defaultFormatLocale == null) {
/*  774 */         initDefault(paramCategory);
/*      */       }
/*  776 */       return defaultFormatLocale;
/*      */     }
/*  778 */     if (!$assertionsDisabled) throw new AssertionError("Unknown Category");
/*      */ 
/*  780 */     return getDefault();
/*      */   }
/*      */ 
/*      */   private static void initDefault()
/*      */   {
/*  785 */     String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("user.language", "en"));
/*      */ 
/*  788 */     String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("user.region"));
/*      */     String str4;
/*      */     String str5;
/*      */     String str3;
/*  790 */     if (str2 != null)
/*      */     {
/*  792 */       int i = str2.indexOf('_');
/*  793 */       if (i >= 0) {
/*  794 */         str4 = str2.substring(0, i);
/*  795 */         str5 = str2.substring(i + 1);
/*      */       } else {
/*  797 */         str4 = str2;
/*  798 */         str5 = "";
/*      */       }
/*  800 */       str3 = "";
/*      */     } else {
/*  802 */       str3 = (String)AccessController.doPrivileged(new GetPropertyAction("user.script", ""));
/*      */ 
/*  804 */       str4 = (String)AccessController.doPrivileged(new GetPropertyAction("user.country", ""));
/*      */ 
/*  806 */       str5 = (String)AccessController.doPrivileged(new GetPropertyAction("user.variant", ""));
/*      */     }
/*      */ 
/*  809 */     defaultLocale = getInstance(str1, str3, str4, str5, null);
/*      */   }
/*      */ 
/*      */   private static void initDefault(Category paramCategory)
/*      */   {
/*  814 */     if (defaultLocale == null) {
/*  815 */       initDefault();
/*      */     }
/*      */ 
/*  818 */     Locale localLocale = getInstance((String)AccessController.doPrivileged(new GetPropertyAction(paramCategory.languageKey, defaultLocale.getLanguage())), (String)AccessController.doPrivileged(new GetPropertyAction(paramCategory.scriptKey, defaultLocale.getScript())), (String)AccessController.doPrivileged(new GetPropertyAction(paramCategory.countryKey, defaultLocale.getCountry())), (String)AccessController.doPrivileged(new GetPropertyAction(paramCategory.variantKey, defaultLocale.getVariant())), null);
/*      */ 
/*  829 */     switch (1.$SwitchMap$java$util$Locale$Category[paramCategory.ordinal()]) {
/*      */     case 1:
/*  831 */       defaultDisplayLocale = localLocale;
/*  832 */       break;
/*      */     case 2:
/*  834 */       defaultFormatLocale = localLocale;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static synchronized void setDefault(Locale paramLocale)
/*      */   {
/*  868 */     setDefault(Category.DISPLAY, paramLocale);
/*  869 */     setDefault(Category.FORMAT, paramLocale);
/*  870 */     defaultLocale = paramLocale;
/*      */   }
/*      */ 
/*      */   public static synchronized void setDefault(Category paramCategory, Locale paramLocale)
/*      */   {
/*  903 */     if (paramCategory == null)
/*  904 */       throw new NullPointerException("Category cannot be NULL");
/*  905 */     if (paramLocale == null) {
/*  906 */       throw new NullPointerException("Can't set default locale to NULL");
/*      */     }
/*  908 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  909 */     if (localSecurityManager != null) localSecurityManager.checkPermission(new PropertyPermission("user.language", "write"));
/*      */ 
/*  911 */     switch (1.$SwitchMap$java$util$Locale$Category[paramCategory.ordinal()]) {
/*      */     case 1:
/*  913 */       defaultDisplayLocale = paramLocale;
/*  914 */       break;
/*      */     case 2:
/*  916 */       defaultFormatLocale = paramLocale;
/*  917 */       break;
/*      */     default:
/*  919 */       if (!$assertionsDisabled) throw new AssertionError("Unknown Category");
/*      */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Locale[] getAvailableLocales()
/*      */   {
/*  934 */     return LocaleServiceProviderPool.getAllAvailableLocales();
/*      */   }
/*      */ 
/*      */   public static String[] getISOCountries()
/*      */   {
/*  947 */     if (isoCountries == null) {
/*  948 */       isoCountries = getISO2Table("ADANDAEAREAFAFGAGATGAIAIAALALBAMARMANANTAOAGOAQATAARARGASASMATAUTAUAUSAWABWAXALAAZAZEBABIHBBBRBBDBGDBEBELBFBFABGBGRBHBHRBIBDIBJBENBLBLMBMBMUBNBRNBOBOLBQBESBRBRABSBHSBTBTNBVBVTBWBWABYBLRBZBLZCACANCCCCKCDCODCFCAFCGCOGCHCHECICIVCKCOKCLCHLCMCMRCNCHNCOCOLCRCRICUCUBCVCPVCWCUWCXCXRCYCYPCZCZEDEDEUDJDJIDKDNKDMDMADODOMDZDZAECECUEEESTEGEGYEHESHERERIESESPETETHFIFINFJFJIFKFLKFMFSMFOFROFRFRAGAGABGBGBRGDGRDGEGEOGFGUFGGGGYGHGHAGIGIBGLGRLGMGMBGNGINGPGLPGQGNQGRGRCGSSGSGTGTMGUGUMGWGNBGYGUYHKHKGHMHMDHNHNDHRHRVHTHTIHUHUNIDIDNIEIRLILISRIMIMNININDIOIOTIQIRQIRIRNISISLITITAJEJEYJMJAMJOJORJPJPNKEKENKGKGZKHKHMKIKIRKMCOMKNKNAKPPRKKRKORKWKWTKYCYMKZKAZLALAOLBLBNLCLCALILIELKLKALRLBRLSLSOLTLTULULUXLVLVALYLBYMAMARMCMCOMDMDAMEMNEMFMAFMGMDGMHMHLMKMKDMLMLIMMMMRMNMNGMOMACMPMNPMQMTQMRMRTMSMSRMTMLTMUMUSMVMDVMWMWIMXMEXMYMYSMZMOZNANAMNCNCLNENERNFNFKNGNGANINICNLNLDNONORNPNPLNRNRUNUNIUNZNZLOMOMNPAPANPEPERPFPYFPGPNGPHPHLPKPAKPLPOLPMSPMPNPCNPRPRIPSPSEPTPRTPWPLWPYPRYQAQATREREUROROURSSRBRURUSRWRWASASAUSBSLBSCSYCSDSDNSESWESGSGPSHSHNSISVNSJSJMSKSVKSLSLESMSMRSNSENSOSOMSRSURSSSSDSTSTPSVSLVSXSXMSYSYRSZSWZTCTCATDTCDTFATFTGTGOTHTHATJTJKTKTKLTLTLSTMTKMTNTUNTOTONTRTURTTTTOTVTUVTWTWNTZTZAUAUKRUGUGAUMUMIUSUSAUYURYUZUZBVAVATVCVCTVEVENVGVGBVIVIRVNVNMVUVUTWFWLFWSWSMYEYEMYTMYTZAZAFZMZMBZWZWE");
/*      */     }
/*  950 */     String[] arrayOfString = new String[isoCountries.length];
/*  951 */     System.arraycopy(isoCountries, 0, arrayOfString, 0, isoCountries.length);
/*  952 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public static String[] getISOLanguages()
/*      */   {
/*  970 */     if (isoLanguages == null) {
/*  971 */       isoLanguages = getISO2Table("aaaarababkaeaveafafrakakaamamhanargararaasasmavavaayaymazazebabakbebelbgbulbhbihbibisbmbambnbenbobodbrbrebsboscacatcechechchacocoscrcrecscescuchucvchvcycymdadandedeudvdivdzdzoeeeweelellenengeoepoesspaetesteueusfafasfffulfifinfjfijfofaofrfrafyfrygaglegdglaglglggngrngugujgvglvhahauhehebhihinhohmohrhrvhthathuhunhyhyehzheriainaidindieileigiboiiiiiikipkinindioidoisislititaiuikuiwhebjajpnjiyidjvjavkakatkgkonkikikkjkuakkkazklkalkmkhmknkankokorkrkaukskaskukurkvkomkwcorkykirlalatlbltzlgluglilimlnlinlolaoltlitlulublvlavmgmlgmhmahmimrimkmkdmlmalmnmonmomolmrmarmsmsamtmltmymyananaunbnobndndenenepngndonlnldnnnnononornrnblnvnavnynyaocociojojiomormororiososspapanpipliplpolpspusptporququermrohrnrunroronrurusrwkinsasanscsrdsdsndsesmesgsagsisinskslkslslvsmsmosnsnasosomsqsqisrsrpsssswstsotsusunsvsweswswatatamteteltgtgkththatitirtktuktltgltntsntotontrturtstsotttattwtwitytahuguigukukrururduzuzbvevenvivievovolwawlnwowolxhxhoyiyidyoyorzazhazhzhozuzul");
/*      */     }
/*  973 */     String[] arrayOfString = new String[isoLanguages.length];
/*  974 */     System.arraycopy(isoLanguages, 0, arrayOfString, 0, isoLanguages.length);
/*  975 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   private static final String[] getISO2Table(String paramString) {
/*  979 */     int i = paramString.length() / 5;
/*  980 */     String[] arrayOfString = new String[i];
/*  981 */     int j = 0; for (int k = 0; j < i; k += 5) {
/*  982 */       arrayOfString[j] = paramString.substring(k, k + 2);
/*      */ 
/*  981 */       j++;
/*      */     }
/*      */ 
/*  984 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public String getLanguage()
/*      */   {
/* 1007 */     return this.baseLocale.getLanguage();
/*      */   }
/*      */ 
/*      */   public String getScript()
/*      */   {
/* 1021 */     return this.baseLocale.getScript();
/*      */   }
/*      */ 
/*      */   public String getCountry()
/*      */   {
/* 1033 */     return this.baseLocale.getRegion();
/*      */   }
/*      */ 
/*      */   public String getVariant()
/*      */   {
/* 1043 */     return this.baseLocale.getVariant();
/*      */   }
/*      */ 
/*      */   public String getExtension(char paramChar)
/*      */   {
/* 1062 */     if (!LocaleExtensions.isValidKey(paramChar)) {
/* 1063 */       throw new IllegalArgumentException("Ill-formed extension key: " + paramChar);
/*      */     }
/* 1065 */     return this.localeExtensions == null ? null : this.localeExtensions.getExtensionValue(Character.valueOf(paramChar));
/*      */   }
/*      */ 
/*      */   public Set<Character> getExtensionKeys()
/*      */   {
/* 1078 */     if (this.localeExtensions == null) {
/* 1079 */       return Collections.emptySet();
/*      */     }
/* 1081 */     return this.localeExtensions.getKeys();
/*      */   }
/*      */ 
/*      */   public Set<String> getUnicodeLocaleAttributes()
/*      */   {
/* 1093 */     if (this.localeExtensions == null) {
/* 1094 */       return Collections.emptySet();
/*      */     }
/* 1096 */     return this.localeExtensions.getUnicodeLocaleAttributes();
/*      */   }
/*      */ 
/*      */   public String getUnicodeLocaleType(String paramString)
/*      */   {
/* 1114 */     if (!UnicodeLocaleExtension.isKey(paramString)) {
/* 1115 */       throw new IllegalArgumentException("Ill-formed Unicode locale key: " + paramString);
/*      */     }
/* 1117 */     return this.localeExtensions == null ? null : this.localeExtensions.getUnicodeLocaleType(paramString);
/*      */   }
/*      */ 
/*      */   public Set<String> getUnicodeLocaleKeys()
/*      */   {
/* 1129 */     if (this.localeExtensions == null) {
/* 1130 */       return Collections.emptySet();
/*      */     }
/* 1132 */     return this.localeExtensions.getUnicodeLocaleKeys();
/*      */   }
/*      */ 
/*      */   BaseLocale getBaseLocale()
/*      */   {
/* 1141 */     return this.baseLocale;
/*      */   }
/*      */ 
/*      */   LocaleExtensions getLocaleExtensions()
/*      */   {
/* 1151 */     return this.localeExtensions;
/*      */   }
/*      */ 
/*      */   public final String toString()
/*      */   {
/* 1198 */     int i = this.baseLocale.getLanguage().length() != 0 ? 1 : 0;
/* 1199 */     int j = this.baseLocale.getScript().length() != 0 ? 1 : 0;
/* 1200 */     int k = this.baseLocale.getRegion().length() != 0 ? 1 : 0;
/* 1201 */     int m = this.baseLocale.getVariant().length() != 0 ? 1 : 0;
/* 1202 */     int n = (this.localeExtensions != null) && (this.localeExtensions.getID().length() != 0) ? 1 : 0;
/*      */ 
/* 1204 */     StringBuilder localStringBuilder = new StringBuilder(this.baseLocale.getLanguage());
/* 1205 */     if ((k != 0) || ((i != 0) && ((m != 0) || (j != 0) || (n != 0)))) {
/* 1206 */       localStringBuilder.append('_').append(this.baseLocale.getRegion());
/*      */     }
/*      */ 
/* 1209 */     if ((m != 0) && ((i != 0) || (k != 0))) {
/* 1210 */       localStringBuilder.append('_').append(this.baseLocale.getVariant());
/*      */     }
/*      */ 
/* 1214 */     if ((j != 0) && ((i != 0) || (k != 0))) {
/* 1215 */       localStringBuilder.append("_#").append(this.baseLocale.getScript());
/*      */     }
/*      */ 
/* 1219 */     if ((n != 0) && ((i != 0) || (k != 0))) {
/* 1220 */       localStringBuilder.append('_');
/* 1221 */       if (j == 0) {
/* 1222 */         localStringBuilder.append('#');
/*      */       }
/* 1224 */       localStringBuilder.append(this.localeExtensions.getID());
/*      */     }
/*      */ 
/* 1227 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public String toLanguageTag()
/*      */   {
/* 1298 */     LanguageTag localLanguageTag = LanguageTag.parseLocale(this.baseLocale, this.localeExtensions);
/* 1299 */     StringBuilder localStringBuilder = new StringBuilder();
/*      */ 
/* 1301 */     String str1 = localLanguageTag.getLanguage();
/* 1302 */     if (str1.length() > 0) {
/* 1303 */       localStringBuilder.append(LanguageTag.canonicalizeLanguage(str1));
/*      */     }
/*      */ 
/* 1306 */     str1 = localLanguageTag.getScript();
/* 1307 */     if (str1.length() > 0) {
/* 1308 */       localStringBuilder.append("-");
/* 1309 */       localStringBuilder.append(LanguageTag.canonicalizeScript(str1));
/*      */     }
/*      */ 
/* 1312 */     str1 = localLanguageTag.getRegion();
/* 1313 */     if (str1.length() > 0) {
/* 1314 */       localStringBuilder.append("-");
/* 1315 */       localStringBuilder.append(LanguageTag.canonicalizeRegion(str1));
/*      */     }
/*      */ 
/* 1318 */     List localList = localLanguageTag.getVariants();
/* 1319 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) { str2 = (String)localIterator.next();
/* 1320 */       localStringBuilder.append("-");
/*      */ 
/* 1322 */       localStringBuilder.append(str2);
/*      */     }
/* 1326 */     String str2;
/* 1325 */     localList = localLanguageTag.getExtensions();
/* 1326 */     for (localIterator = localList.iterator(); localIterator.hasNext(); ) { str2 = (String)localIterator.next();
/* 1327 */       localStringBuilder.append("-");
/* 1328 */       localStringBuilder.append(LanguageTag.canonicalizeExtension(str2));
/*      */     }
/*      */ 
/* 1331 */     str1 = localLanguageTag.getPrivateuse();
/* 1332 */     if (str1.length() > 0) {
/* 1333 */       if (localStringBuilder.length() > 0) {
/* 1334 */         localStringBuilder.append("-");
/*      */       }
/* 1336 */       localStringBuilder.append("x").append("-");
/*      */ 
/* 1338 */       localStringBuilder.append(str1);
/*      */     }
/*      */ 
/* 1341 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public static Locale forLanguageTag(String paramString)
/*      */   {
/* 1465 */     LanguageTag localLanguageTag = LanguageTag.parse(paramString, null);
/* 1466 */     InternalLocaleBuilder localInternalLocaleBuilder = new InternalLocaleBuilder();
/* 1467 */     localInternalLocaleBuilder.setLanguageTag(localLanguageTag);
/* 1468 */     BaseLocale localBaseLocale = localInternalLocaleBuilder.getBaseLocale();
/* 1469 */     LocaleExtensions localLocaleExtensions = localInternalLocaleBuilder.getLocaleExtensions();
/* 1470 */     if ((localLocaleExtensions == null) && (localBaseLocale.getVariant().length() > 0)) {
/* 1471 */       localLocaleExtensions = getCompatibilityExtensions(localBaseLocale.getLanguage(), localBaseLocale.getScript(), localBaseLocale.getRegion(), localBaseLocale.getVariant());
/*      */     }
/*      */ 
/* 1474 */     return getInstance(localBaseLocale, localLocaleExtensions);
/*      */   }
/*      */ 
/*      */   public String getISO3Language()
/*      */     throws MissingResourceException
/*      */   {
/* 1492 */     String str1 = this.baseLocale.getLanguage();
/* 1493 */     if (str1.length() == 3) {
/* 1494 */       return str1;
/*      */     }
/*      */ 
/* 1497 */     String str2 = getISO3Code(str1, "aaaarababkaeaveafafrakakaamamhanargararaasasmavavaayaymazazebabakbebelbgbulbhbihbibisbmbambnbenbobodbrbrebsboscacatcechechchacocoscrcrecscescuchucvchvcycymdadandedeudvdivdzdzoeeeweelellenengeoepoesspaetesteueusfafasfffulfifinfjfijfofaofrfrafyfrygaglegdglaglglggngrngugujgvglvhahauhehebhihinhohmohrhrvhthathuhunhyhyehzheriainaidindieileigiboiiiiiikipkinindioidoisislititaiuikuiwhebjajpnjiyidjvjavkakatkgkonkikikkjkuakkkazklkalkmkhmknkankokorkrkaukskaskukurkvkomkwcorkykirlalatlbltzlgluglilimlnlinlolaoltlitlulublvlavmgmlgmhmahmimrimkmkdmlmalmnmonmomolmrmarmsmsamtmltmymyananaunbnobndndenenepngndonlnldnnnnononornrnblnvnavnynyaocociojojiomormororiososspapanpipliplpolpspusptporququermrohrnrunroronrurusrwkinsasanscsrdsdsndsesmesgsagsisinskslkslslvsmsmosnsnasosomsqsqisrsrpsssswstsotsusunsvsweswswatatamteteltgtgkththatitirtktuktltgltntsntotontrturtstsotttattwtwitytahuguigukukrururduzuzbvevenvivievovolwawlnwowolxhxhoyiyidyoyorzazhazhzhozuzul");
/* 1498 */     if (str2 == null) {
/* 1499 */       throw new MissingResourceException("Couldn't find 3-letter language code for " + str1, "FormatData_" + toString(), "ShortLanguage");
/*      */     }
/*      */ 
/* 1502 */     return str2;
/*      */   }
/*      */ 
/*      */   public String getISO3Country()
/*      */     throws MissingResourceException
/*      */   {
/* 1519 */     String str = getISO3Code(this.baseLocale.getRegion(), "ADANDAEAREAFAFGAGATGAIAIAALALBAMARMANANTAOAGOAQATAARARGASASMATAUTAUAUSAWABWAXALAAZAZEBABIHBBBRBBDBGDBEBELBFBFABGBGRBHBHRBIBDIBJBENBLBLMBMBMUBNBRNBOBOLBQBESBRBRABSBHSBTBTNBVBVTBWBWABYBLRBZBLZCACANCCCCKCDCODCFCAFCGCOGCHCHECICIVCKCOKCLCHLCMCMRCNCHNCOCOLCRCRICUCUBCVCPVCWCUWCXCXRCYCYPCZCZEDEDEUDJDJIDKDNKDMDMADODOMDZDZAECECUEEESTEGEGYEHESHERERIESESPETETHFIFINFJFJIFKFLKFMFSMFOFROFRFRAGAGABGBGBRGDGRDGEGEOGFGUFGGGGYGHGHAGIGIBGLGRLGMGMBGNGINGPGLPGQGNQGRGRCGSSGSGTGTMGUGUMGWGNBGYGUYHKHKGHMHMDHNHNDHRHRVHTHTIHUHUNIDIDNIEIRLILISRIMIMNININDIOIOTIQIRQIRIRNISISLITITAJEJEYJMJAMJOJORJPJPNKEKENKGKGZKHKHMKIKIRKMCOMKNKNAKPPRKKRKORKWKWTKYCYMKZKAZLALAOLBLBNLCLCALILIELKLKALRLBRLSLSOLTLTULULUXLVLVALYLBYMAMARMCMCOMDMDAMEMNEMFMAFMGMDGMHMHLMKMKDMLMLIMMMMRMNMNGMOMACMPMNPMQMTQMRMRTMSMSRMTMLTMUMUSMVMDVMWMWIMXMEXMYMYSMZMOZNANAMNCNCLNENERNFNFKNGNGANINICNLNLDNONORNPNPLNRNRUNUNIUNZNZLOMOMNPAPANPEPERPFPYFPGPNGPHPHLPKPAKPLPOLPMSPMPNPCNPRPRIPSPSEPTPRTPWPLWPYPRYQAQATREREUROROURSSRBRURUSRWRWASASAUSBSLBSCSYCSDSDNSESWESGSGPSHSHNSISVNSJSJMSKSVKSLSLESMSMRSNSENSOSOMSRSURSSSSDSTSTPSVSLVSXSXMSYSYRSZSWZTCTCATDTCDTFATFTGTGOTHTHATJTJKTKTKLTLTLSTMTKMTNTUNTOTONTRTURTTTTOTVTUVTWTWNTZTZAUAUKRUGUGAUMUMIUSUSAUYURYUZUZBVAVATVCVCTVEVENVGVGBVIVIRVNVNMVUVUTWFWLFWSWSMYEYEMYTMYTZAZAFZMZMBZWZWE");
/* 1520 */     if (str == null) {
/* 1521 */       throw new MissingResourceException("Couldn't find 3-letter country code for " + this.baseLocale.getRegion(), "FormatData_" + toString(), "ShortCountry");
/*      */     }
/*      */ 
/* 1524 */     return str;
/*      */   }
/*      */ 
/*      */   private static final String getISO3Code(String paramString1, String paramString2) {
/* 1528 */     int i = paramString1.length();
/* 1529 */     if (i == 0) {
/* 1530 */       return "";
/*      */     }
/*      */ 
/* 1533 */     int j = paramString2.length();
/* 1534 */     int k = j;
/* 1535 */     if (i == 2)
/*      */     {
/* 1536 */       int m = paramString1.charAt(0);
/* 1537 */       int n = paramString1.charAt(1);
/* 1538 */       for (k = 0; (k < j) && (
/* 1539 */         (paramString2.charAt(k) != m) || (paramString2.charAt(k + 1) != n)); k += 5);
/*      */     }
/*      */ 
/* 1545 */     return k < j ? paramString2.substring(k + 2, k + 5) : null;
/*      */   }
/*      */ 
/*      */   public final String getDisplayLanguage()
/*      */   {
/* 1561 */     return getDisplayLanguage(getDefault(Category.DISPLAY));
/*      */   }
/*      */ 
/*      */   public String getDisplayLanguage(Locale paramLocale)
/*      */   {
/* 1580 */     return getDisplayString(this.baseLocale.getLanguage(), paramLocale, 0);
/*      */   }
/*      */ 
/*      */   public String getDisplayScript()
/*      */   {
/* 1592 */     return getDisplayScript(getDefault());
/*      */   }
/*      */ 
/*      */   public String getDisplayScript(Locale paramLocale)
/*      */   {
/* 1606 */     return getDisplayString(this.baseLocale.getScript(), paramLocale, 3);
/*      */   }
/*      */ 
/*      */   public final String getDisplayCountry()
/*      */   {
/* 1622 */     return getDisplayCountry(getDefault(Category.DISPLAY));
/*      */   }
/*      */ 
/*      */   public String getDisplayCountry(Locale paramLocale)
/*      */   {
/* 1641 */     return getDisplayString(this.baseLocale.getRegion(), paramLocale, 1);
/*      */   }
/*      */ 
/*      */   private String getDisplayString(String paramString, Locale paramLocale, int paramInt) {
/* 1645 */     if (paramString.length() == 0) {
/* 1646 */       return "";
/*      */     }
/*      */ 
/* 1649 */     if (paramLocale == null) {
/* 1650 */       throw new NullPointerException();
/*      */     }
/*      */     try
/*      */     {
/* 1654 */       OpenListResourceBundle localOpenListResourceBundle = LocaleData.getLocaleNames(paramLocale);
/* 1655 */       String str1 = paramInt == 2 ? "%%" + paramString : paramString;
/* 1656 */       String str2 = null;
/*      */ 
/* 1660 */       LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(LocaleNameProvider.class);
/*      */ 
/* 1662 */       if (localLocaleServiceProviderPool.hasProviders()) {
/* 1663 */         str2 = (String)localLocaleServiceProviderPool.getLocalizedObject(LocaleNameGetter.INSTANCE, paramLocale, localOpenListResourceBundle, str1, new Object[] { Integer.valueOf(paramInt), paramString });
/*      */       }
/*      */ 
/* 1669 */       if (str2 == null) {
/* 1670 */         str2 = localOpenListResourceBundle.getString(str1);
/*      */       }
/*      */ 
/* 1673 */       if (str2 != null) {
/* 1674 */         return str2;
/*      */       }
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/* 1680 */     return paramString;
/*      */   }
/*      */ 
/*      */   public final String getDisplayVariant()
/*      */   {
/* 1689 */     return getDisplayVariant(getDefault(Category.DISPLAY));
/*      */   }
/*      */ 
/*      */   public String getDisplayVariant(Locale paramLocale)
/*      */   {
/* 1700 */     if (this.baseLocale.getVariant().length() == 0) {
/* 1701 */       return "";
/*      */     }
/* 1703 */     OpenListResourceBundle localOpenListResourceBundle = LocaleData.getLocaleNames(paramLocale);
/*      */ 
/* 1705 */     String[] arrayOfString = getDisplayVariantArray(localOpenListResourceBundle, paramLocale);
/*      */ 
/* 1709 */     String str1 = null;
/* 1710 */     String str2 = null;
/*      */     try {
/* 1712 */       str1 = localOpenListResourceBundle.getString("ListPattern");
/* 1713 */       str2 = localOpenListResourceBundle.getString("ListCompositionPattern");
/*      */     } catch (MissingResourceException localMissingResourceException) {
/*      */     }
/* 1716 */     return formatList(arrayOfString, str1, str2);
/*      */   }
/*      */ 
/*      */   public final String getDisplayName()
/*      */   {
/* 1737 */     return getDisplayName(getDefault(Category.DISPLAY));
/*      */   }
/*      */ 
/*      */   public String getDisplayName(Locale paramLocale)
/*      */   {
/* 1761 */     OpenListResourceBundle localOpenListResourceBundle = LocaleData.getLocaleNames(paramLocale);
/*      */ 
/* 1763 */     String str1 = getDisplayLanguage(paramLocale);
/* 1764 */     String str2 = getDisplayScript(paramLocale);
/* 1765 */     String str3 = getDisplayCountry(paramLocale);
/* 1766 */     String[] arrayOfString1 = getDisplayVariantArray(localOpenListResourceBundle, paramLocale);
/*      */ 
/* 1769 */     String str4 = null;
/* 1770 */     String str5 = null;
/* 1771 */     String str6 = null;
/*      */     try {
/* 1773 */       str4 = localOpenListResourceBundle.getString("DisplayNamePattern");
/* 1774 */       str5 = localOpenListResourceBundle.getString("ListPattern");
/* 1775 */       str6 = localOpenListResourceBundle.getString("ListCompositionPattern");
/*      */     }
/*      */     catch (MissingResourceException localMissingResourceException)
/*      */     {
/*      */     }
/*      */ 
/* 1782 */     String str7 = null;
/* 1783 */     String[] arrayOfString2 = null;
/*      */ 
/* 1789 */     if ((str1.length() == 0) && (str2.length() == 0) && (str3.length() == 0)) {
/* 1790 */       if (arrayOfString1.length == 0) {
/* 1791 */         return "";
/*      */       }
/* 1793 */       return formatList(arrayOfString1, str5, str6);
/*      */     }
/*      */ 
/* 1796 */     ArrayList localArrayList = new ArrayList(4);
/* 1797 */     if (str1.length() != 0) {
/* 1798 */       localArrayList.add(str1);
/*      */     }
/* 1800 */     if (str2.length() != 0) {
/* 1801 */       localArrayList.add(str2);
/*      */     }
/* 1803 */     if (str3.length() != 0) {
/* 1804 */       localArrayList.add(str3);
/*      */     }
/* 1806 */     if (arrayOfString1.length != 0) {
/* 1807 */       for (String str8 : arrayOfString1) {
/* 1808 */         localArrayList.add(str8);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1813 */     str7 = (String)localArrayList.get(0);
/*      */ 
/* 1816 */     int i = localArrayList.size();
/* 1817 */     arrayOfString2 = i > 1 ? (String[])localArrayList.subList(1, i).toArray(new String[i - 1]) : new String[0];
/*      */ 
/* 1825 */     Object[] arrayOfObject = { new Integer(arrayOfString2.length != 0 ? 2 : 1), str7, arrayOfString2.length != 0 ? formatList(arrayOfString2, str5, str6) : null };
/*      */ 
/* 1835 */     if (str4 != null) {
/* 1836 */       return new MessageFormat(str4).format(arrayOfObject);
/*      */     }
/*      */ 
/* 1842 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1843 */     localStringBuilder.append((String)arrayOfObject[1]);
/* 1844 */     if (arrayOfObject.length > 2) {
/* 1845 */       localStringBuilder.append(" (");
/* 1846 */       localStringBuilder.append((String)arrayOfObject[2]);
/* 1847 */       localStringBuilder.append(')');
/*      */     }
/* 1849 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 1859 */       return (Locale)super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/* 1862 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1873 */     int i = this.hashCodeValue;
/* 1874 */     if (i == 0) {
/* 1875 */       i = this.baseLocale.hashCode();
/* 1876 */       if (this.localeExtensions != null) {
/* 1877 */         i ^= this.localeExtensions.hashCode();
/*      */       }
/* 1879 */       this.hashCodeValue = i;
/*      */     }
/* 1881 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1895 */     if (this == paramObject)
/* 1896 */       return true;
/* 1897 */     if (!(paramObject instanceof Locale))
/* 1898 */       return false;
/* 1899 */     BaseLocale localBaseLocale = ((Locale)paramObject).baseLocale;
/* 1900 */     if (!this.baseLocale.equals(localBaseLocale)) {
/* 1901 */       return false;
/*      */     }
/* 1903 */     if (this.localeExtensions == null) {
/* 1904 */       return ((Locale)paramObject).localeExtensions == null;
/*      */     }
/* 1906 */     return this.localeExtensions.equals(((Locale)paramObject).localeExtensions);
/*      */   }
/*      */ 
/*      */   private String[] getDisplayVariantArray(OpenListResourceBundle paramOpenListResourceBundle, Locale paramLocale)
/*      */   {
/* 1930 */     StringTokenizer localStringTokenizer = new StringTokenizer(this.baseLocale.getVariant(), "_");
/* 1931 */     String[] arrayOfString = new String[localStringTokenizer.countTokens()];
/*      */ 
/* 1935 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 1936 */       arrayOfString[i] = getDisplayString(localStringTokenizer.nextToken(), paramLocale, 2);
/*      */     }
/*      */ 
/* 1940 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   private static String formatList(String[] paramArrayOfString, String paramString1, String paramString2)
/*      */   {
/* 1957 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 1958 */       localObject = new StringBuffer();
/* 1959 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1960 */         if (i > 0) ((StringBuffer)localObject).append(',');
/* 1961 */         ((StringBuffer)localObject).append(paramArrayOfString[i]);
/*      */       }
/* 1963 */       return ((StringBuffer)localObject).toString();
/*      */     }
/*      */ 
/* 1967 */     if (paramArrayOfString.length > 3) {
/* 1968 */       localObject = new MessageFormat(paramString2);
/* 1969 */       paramArrayOfString = composeList((MessageFormat)localObject, paramArrayOfString);
/*      */     }
/*      */ 
/* 1973 */     Object localObject = new Object[paramArrayOfString.length + 1];
/* 1974 */     System.arraycopy(paramArrayOfString, 0, localObject, 1, paramArrayOfString.length);
/* 1975 */     localObject[0] = new Integer(paramArrayOfString.length);
/*      */ 
/* 1978 */     MessageFormat localMessageFormat = new MessageFormat(paramString1);
/* 1979 */     return localMessageFormat.format(localObject);
/*      */   }
/*      */ 
/*      */   private static String[] composeList(MessageFormat paramMessageFormat, String[] paramArrayOfString)
/*      */   {
/* 1992 */     if (paramArrayOfString.length <= 3) return paramArrayOfString;
/*      */ 
/* 1995 */     String[] arrayOfString1 = { paramArrayOfString[0], paramArrayOfString[1] };
/* 1996 */     String str = paramMessageFormat.format(arrayOfString1);
/*      */ 
/* 1999 */     String[] arrayOfString2 = new String[paramArrayOfString.length - 1];
/* 2000 */     System.arraycopy(paramArrayOfString, 2, arrayOfString2, 1, arrayOfString2.length - 1);
/* 2001 */     arrayOfString2[0] = str;
/*      */ 
/* 2004 */     return composeList(paramMessageFormat, arrayOfString2);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 2042 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 2043 */     localPutField.put("language", this.baseLocale.getLanguage());
/* 2044 */     localPutField.put("script", this.baseLocale.getScript());
/* 2045 */     localPutField.put("country", this.baseLocale.getRegion());
/* 2046 */     localPutField.put("variant", this.baseLocale.getVariant());
/* 2047 */     localPutField.put("extensions", this.localeExtensions == null ? "" : this.localeExtensions.getID());
/* 2048 */     localPutField.put("hashcode", -1);
/* 2049 */     paramObjectOutputStream.writeFields();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2061 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 2062 */     String str1 = (String)localGetField.get("language", "");
/* 2063 */     String str2 = (String)localGetField.get("script", "");
/* 2064 */     String str3 = (String)localGetField.get("country", "");
/* 2065 */     String str4 = (String)localGetField.get("variant", "");
/* 2066 */     String str5 = (String)localGetField.get("extensions", "");
/* 2067 */     this.baseLocale = BaseLocale.getInstance(convertOldISOCodes(str1), str2, str3, str4);
/* 2068 */     if (str5.length() > 0)
/*      */       try {
/* 2070 */         InternalLocaleBuilder localInternalLocaleBuilder = new InternalLocaleBuilder();
/* 2071 */         localInternalLocaleBuilder.setExtensions(str5);
/* 2072 */         this.localeExtensions = localInternalLocaleBuilder.getLocaleExtensions();
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2074 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage());
/*      */       }
/*      */     else
/* 2077 */       this.localeExtensions = null;
/*      */   }
/*      */ 
/*      */   private Object readResolve()
/*      */     throws ObjectStreamException
/*      */   {
/* 2096 */     return getInstance(this.baseLocale.getLanguage(), this.baseLocale.getScript(), this.baseLocale.getRegion(), this.baseLocale.getVariant(), this.localeExtensions);
/*      */   }
/*      */ 
/*      */   private static String convertOldISOCodes(String paramString)
/*      */   {
/* 2107 */     paramString = LocaleUtils.toLowerString(paramString).intern();
/* 2108 */     if (paramString == "he")
/* 2109 */       return "iw";
/* 2110 */     if (paramString == "yi")
/* 2111 */       return "ji";
/* 2112 */     if (paramString == "id") {
/* 2113 */       return "in";
/*      */     }
/* 2115 */     return paramString;
/*      */   }
/*      */ 
/*      */   private static LocaleExtensions getCompatibilityExtensions(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/* 2123 */     LocaleExtensions localLocaleExtensions = null;
/*      */ 
/* 2125 */     if ((LocaleUtils.caseIgnoreMatch(paramString1, "ja")) && (paramString2.length() == 0) && (LocaleUtils.caseIgnoreMatch(paramString3, "jp")) && ("JP".equals(paramString4)))
/*      */     {
/* 2130 */       localLocaleExtensions = LocaleExtensions.CALENDAR_JAPANESE;
/* 2131 */     } else if ((LocaleUtils.caseIgnoreMatch(paramString1, "th")) && (paramString2.length() == 0) && (LocaleUtils.caseIgnoreMatch(paramString3, "th")) && ("TH".equals(paramString4)))
/*      */     {
/* 2136 */       localLocaleExtensions = LocaleExtensions.NUMBER_THAI;
/*      */     }
/* 2138 */     return localLocaleExtensions;
/*      */   }
/*      */ 
/*      */   public static final class Builder
/*      */   {
/*      */     private final InternalLocaleBuilder localeBuilder;
/*      */ 
/*      */     public Builder()
/*      */     {
/* 2258 */       this.localeBuilder = new InternalLocaleBuilder();
/*      */     }
/*      */ 
/*      */     public Builder setLocale(Locale paramLocale)
/*      */     {
/*      */       try
/*      */       {
/* 2283 */         this.localeBuilder.setLocale(paramLocale.baseLocale, paramLocale.localeExtensions);
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2285 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage(), localLocaleSyntaxException.getErrorIndex());
/*      */       }
/* 2287 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder setLanguageTag(String paramString)
/*      */     {
/* 2308 */       ParseStatus localParseStatus = new ParseStatus();
/* 2309 */       LanguageTag localLanguageTag = LanguageTag.parse(paramString, localParseStatus);
/* 2310 */       if (localParseStatus.isError()) {
/* 2311 */         throw new IllformedLocaleException(localParseStatus.getErrorMessage(), localParseStatus.getErrorIndex());
/*      */       }
/* 2313 */       this.localeBuilder.setLanguageTag(localLanguageTag);
/* 2314 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder setLanguage(String paramString)
/*      */     {
/*      */       try
/*      */       {
/* 2332 */         this.localeBuilder.setLanguage(paramString);
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2334 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage(), localLocaleSyntaxException.getErrorIndex());
/*      */       }
/* 2336 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder setScript(String paramString)
/*      */     {
/*      */       try
/*      */       {
/* 2353 */         this.localeBuilder.setScript(paramString);
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2355 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage(), localLocaleSyntaxException.getErrorIndex());
/*      */       }
/* 2357 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder setRegion(String paramString)
/*      */     {
/*      */       try
/*      */       {
/* 2378 */         this.localeBuilder.setRegion(paramString);
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2380 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage(), localLocaleSyntaxException.getErrorIndex());
/*      */       }
/* 2382 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder setVariant(String paramString)
/*      */     {
/*      */       try
/*      */       {
/* 2405 */         this.localeBuilder.setVariant(paramString);
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2407 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage(), localLocaleSyntaxException.getErrorIndex());
/*      */       }
/* 2409 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder setExtension(char paramChar, String paramString)
/*      */     {
/*      */       try
/*      */       {
/* 2437 */         this.localeBuilder.setExtension(paramChar, paramString);
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2439 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage(), localLocaleSyntaxException.getErrorIndex());
/*      */       }
/* 2441 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder setUnicodeLocaleKeyword(String paramString1, String paramString2)
/*      */     {
/*      */       try
/*      */       {
/* 2467 */         this.localeBuilder.setUnicodeLocaleKeyword(paramString1, paramString2);
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2469 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage(), localLocaleSyntaxException.getErrorIndex());
/*      */       }
/* 2471 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder addUnicodeLocaleAttribute(String paramString)
/*      */     {
/*      */       try
/*      */       {
/* 2488 */         this.localeBuilder.addUnicodeLocaleAttribute(paramString);
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2490 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage(), localLocaleSyntaxException.getErrorIndex());
/*      */       }
/* 2492 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder removeUnicodeLocaleAttribute(String paramString)
/*      */     {
/*      */       try
/*      */       {
/* 2511 */         this.localeBuilder.removeUnicodeLocaleAttribute(paramString);
/*      */       } catch (LocaleSyntaxException localLocaleSyntaxException) {
/* 2513 */         throw new IllformedLocaleException(localLocaleSyntaxException.getMessage(), localLocaleSyntaxException.getErrorIndex());
/*      */       }
/* 2515 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder clear()
/*      */     {
/* 2524 */       this.localeBuilder.clear();
/* 2525 */       return this;
/*      */     }
/*      */ 
/*      */     public Builder clearExtensions()
/*      */     {
/* 2536 */       this.localeBuilder.clearExtensions();
/* 2537 */       return this;
/*      */     }
/*      */ 
/*      */     public Locale build()
/*      */     {
/* 2551 */       BaseLocale localBaseLocale = this.localeBuilder.getBaseLocale();
/* 2552 */       LocaleExtensions localLocaleExtensions = this.localeBuilder.getLocaleExtensions();
/* 2553 */       if ((localLocaleExtensions == null) && (localBaseLocale.getVariant().length() > 0)) {
/* 2554 */         localLocaleExtensions = Locale.getCompatibilityExtensions(localBaseLocale.getLanguage(), localBaseLocale.getScript(), localBaseLocale.getRegion(), localBaseLocale.getVariant());
/*      */       }
/*      */ 
/* 2557 */       return Locale.getInstance(localBaseLocale, localLocaleExtensions);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Cache extends LocaleObjectCache<Locale.LocaleKey, Locale>
/*      */   {
/*      */     protected Locale createObject(Locale.LocaleKey paramLocaleKey)
/*      */     {
/*  681 */       return new Locale(Locale.LocaleKey.access$200(paramLocaleKey), Locale.LocaleKey.access$300(paramLocaleKey), null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum Category
/*      */   {
/* 2189 */     DISPLAY("user.language.display", "user.script.display", "user.country.display", "user.variant.display"), 
/*      */ 
/* 2198 */     FORMAT("user.language.format", "user.script.format", "user.country.format", "user.variant.format");
/*      */ 
/*      */     final String languageKey;
/*      */     final String scriptKey;
/*      */     final String countryKey;
/*      */     final String variantKey;
/*      */ 
/* 2204 */     private Category(String paramString1, String paramString2, String paramString3, String paramString4) { this.languageKey = paramString1;
/* 2205 */       this.scriptKey = paramString2;
/* 2206 */       this.countryKey = paramString3;
/* 2207 */       this.variantKey = paramString4;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class LocaleKey
/*      */   {
/*      */     private final BaseLocale base;
/*      */     private final LocaleExtensions exts;
/*      */     private final int hash;
/*      */ 
/*      */     private LocaleKey(BaseLocale paramBaseLocale, LocaleExtensions paramLocaleExtensions)
/*      */     {
/*  691 */       this.base = paramBaseLocale;
/*  692 */       this.exts = paramLocaleExtensions;
/*      */ 
/*  695 */       int i = this.base.hashCode();
/*  696 */       if (this.exts != null) {
/*  697 */         i ^= this.exts.hashCode();
/*      */       }
/*  699 */       this.hash = i;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  704 */       if (this == paramObject) {
/*  705 */         return true;
/*      */       }
/*  707 */       if (!(paramObject instanceof LocaleKey)) {
/*  708 */         return false;
/*      */       }
/*  710 */       LocaleKey localLocaleKey = (LocaleKey)paramObject;
/*  711 */       if ((this.hash != localLocaleKey.hash) || (!this.base.equals(localLocaleKey.base))) {
/*  712 */         return false;
/*      */       }
/*  714 */       if (this.exts == null) {
/*  715 */         return localLocaleKey.exts == null;
/*      */       }
/*  717 */       return this.exts.equals(localLocaleKey.exts);
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  722 */       return this.hash;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LocaleNameGetter
/*      */     implements LocaleServiceProviderPool.LocalizedObjectGetter<LocaleNameProvider, String>
/*      */   {
/* 2147 */     private static final LocaleNameGetter INSTANCE = new LocaleNameGetter();
/*      */ 
/*      */     public String getObject(LocaleNameProvider paramLocaleNameProvider, Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*      */     {
/* 2153 */       assert (paramArrayOfObject.length == 2);
/* 2154 */       int i = ((Integer)paramArrayOfObject[0]).intValue();
/* 2155 */       String str = (String)paramArrayOfObject[1];
/*      */ 
/* 2157 */       switch (i) {
/*      */       case 0:
/* 2159 */         return paramLocaleNameProvider.getDisplayLanguage(str, paramLocale);
/*      */       case 1:
/* 2161 */         return paramLocaleNameProvider.getDisplayCountry(str, paramLocale);
/*      */       case 2:
/* 2163 */         return paramLocaleNameProvider.getDisplayVariant(str, paramLocale);
/*      */       case 3:
/* 2165 */         return paramLocaleNameProvider.getDisplayScript(str, paramLocale);
/*      */       }
/* 2167 */       if (!$assertionsDisabled) throw new AssertionError();
/*      */ 
/* 2170 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Locale
 * JD-Core Version:    0.6.2
 */