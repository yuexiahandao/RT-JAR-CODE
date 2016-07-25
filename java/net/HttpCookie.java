/*      */ package java.net;
/*      */ 
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import sun.misc.JavaNetHttpCookieAccess;
/*      */ import sun.misc.SharedSecrets;
/*      */ 
/*      */ public final class HttpCookie
/*      */   implements Cloneable
/*      */ {
/*      */   private String name;
/*      */   private String value;
/*      */   private String comment;
/*      */   private String commentURL;
/*      */   private boolean toDiscard;
/*      */   private String domain;
/*   78 */   private long maxAge = -1L;
/*      */   private String path;
/*      */   private String portlist;
/*      */   private boolean secure;
/*      */   private boolean httpOnly;
/*   83 */   private int version = 1;
/*      */   private final String header;
/*   93 */   private long whenCreated = 0L;
/*      */   private static final long MAX_AGE_UNSPECIFIED = -1L;
/*  107 */   private static final String[] COOKIE_DATE_FORMATS = { "EEE',' dd-MMM-yyyy HH:mm:ss 'GMT'", "EEE',' dd MMM yyyy HH:mm:ss 'GMT'", "EEE MMM dd yyyy HH:mm:ss 'GMT'Z", "EEE',' dd-MMM-yy HH:mm:ss 'GMT'", "EEE',' dd MMM yy HH:mm:ss 'GMT'", "EEE MMM dd yy HH:mm:ss 'GMT'Z" };
/*      */   private static final String SET_COOKIE = "set-cookie:";
/*      */   private static final String SET_COOKIE2 = "set-cookie2:";
/*      */   private static final String tspecials = ",;";
/*  985 */   static Map<String, CookieAttributeAssignor> assignors = null;
/*      */ 
/* 1123 */   static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*      */ 
/*      */   public HttpCookie(String paramString1, String paramString2)
/*      */   {
/*  157 */     this(paramString1, paramString2, null);
/*      */   }
/*      */ 
/*      */   private HttpCookie(String paramString1, String paramString2, String paramString3) {
/*  161 */     paramString1 = paramString1.trim();
/*  162 */     if ((paramString1.length() == 0) || (!isToken(paramString1)) || (paramString1.charAt(0) == '$')) {
/*  163 */       throw new IllegalArgumentException("Illegal cookie name");
/*      */     }
/*      */ 
/*  166 */     this.name = paramString1;
/*  167 */     this.value = paramString2;
/*  168 */     this.toDiscard = false;
/*  169 */     this.secure = false;
/*      */ 
/*  171 */     this.whenCreated = System.currentTimeMillis();
/*  172 */     this.portlist = null;
/*  173 */     this.header = paramString3;
/*      */   }
/*      */ 
/*      */   public static List<HttpCookie> parse(String paramString)
/*      */   {
/*  195 */     return parse(paramString, false);
/*      */   }
/*      */ 
/*      */   private static List<HttpCookie> parse(String paramString, boolean paramBoolean)
/*      */   {
/*  204 */     int i = guessCookieVersion(paramString);
/*      */ 
/*  207 */     if (startsWithIgnoreCase(paramString, "set-cookie2:"))
/*  208 */       paramString = paramString.substring("set-cookie2:".length());
/*  209 */     else if (startsWithIgnoreCase(paramString, "set-cookie:")) {
/*  210 */       paramString = paramString.substring("set-cookie:".length());
/*      */     }
/*      */ 
/*  214 */     ArrayList localArrayList = new ArrayList();
/*      */     Object localObject;
/*  218 */     if (i == 0)
/*      */     {
/*  220 */       localObject = parseInternal(paramString, paramBoolean);
/*  221 */       ((HttpCookie)localObject).setVersion(0);
/*  222 */       localArrayList.add(localObject);
/*      */     }
/*      */     else
/*      */     {
/*  227 */       localObject = splitMultiCookies(paramString);
/*  228 */       for (String str : (List)localObject) {
/*  229 */         HttpCookie localHttpCookie = parseInternal(str, paramBoolean);
/*  230 */         localHttpCookie.setVersion(1);
/*  231 */         localArrayList.add(localHttpCookie);
/*      */       }
/*      */     }
/*      */ 
/*  235 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public boolean hasExpired()
/*      */   {
/*  251 */     if (this.maxAge == 0L) return true;
/*      */ 
/*  256 */     if (this.maxAge == -1L) return false;
/*      */ 
/*  258 */     long l = (System.currentTimeMillis() - this.whenCreated) / 1000L;
/*  259 */     if (l > this.maxAge) {
/*  260 */       return true;
/*      */     }
/*  262 */     return false;
/*      */   }
/*      */ 
/*      */   public void setComment(String paramString)
/*      */   {
/*  280 */     this.comment = paramString;
/*      */   }
/*      */ 
/*      */   public String getComment()
/*      */   {
/*  298 */     return this.comment;
/*      */   }
/*      */ 
/*      */   public void setCommentURL(String paramString)
/*      */   {
/*  316 */     this.commentURL = paramString;
/*      */   }
/*      */ 
/*      */   public String getCommentURL()
/*      */   {
/*  334 */     return this.commentURL;
/*      */   }
/*      */ 
/*      */   public void setDiscard(boolean paramBoolean)
/*      */   {
/*  348 */     this.toDiscard = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getDiscard()
/*      */   {
/*  363 */     return this.toDiscard;
/*      */   }
/*      */ 
/*      */   public void setPortlist(String paramString)
/*      */   {
/*  377 */     this.portlist = paramString;
/*      */   }
/*      */ 
/*      */   public String getPortlist()
/*      */   {
/*  392 */     return this.portlist;
/*      */   }
/*      */ 
/*      */   public void setDomain(String paramString)
/*      */   {
/*  416 */     if (paramString != null)
/*  417 */       this.domain = paramString.toLowerCase();
/*      */     else
/*  419 */       this.domain = paramString;
/*      */   }
/*      */ 
/*      */   public String getDomain()
/*      */   {
/*  437 */     return this.domain;
/*      */   }
/*      */ 
/*      */   public void setMaxAge(long paramLong)
/*      */   {
/*  463 */     this.maxAge = paramLong;
/*      */   }
/*      */ 
/*      */   public long getMaxAge()
/*      */   {
/*  484 */     return this.maxAge;
/*      */   }
/*      */ 
/*      */   public void setPath(String paramString)
/*      */   {
/*  512 */     this.path = paramString;
/*      */   }
/*      */ 
/*      */   public String getPath()
/*      */   {
/*  532 */     return this.path;
/*      */   }
/*      */ 
/*      */   public void setSecure(boolean paramBoolean)
/*      */   {
/*  554 */     this.secure = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getSecure()
/*      */   {
/*  573 */     return this.secure;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  589 */     return this.name;
/*      */   }
/*      */ 
/*      */   public void setValue(String paramString)
/*      */   {
/*  615 */     this.value = paramString;
/*      */   }
/*      */ 
/*      */   public String getValue()
/*      */   {
/*  632 */     return this.value;
/*      */   }
/*      */ 
/*      */   public int getVersion()
/*      */   {
/*  655 */     return this.version;
/*      */   }
/*      */ 
/*      */   public void setVersion(int paramInt)
/*      */   {
/*  678 */     if ((paramInt != 0) && (paramInt != 1)) {
/*  679 */       throw new IllegalArgumentException("cookie version should be 0 or 1");
/*      */     }
/*      */ 
/*  682 */     this.version = paramInt;
/*      */   }
/*      */ 
/*      */   public boolean isHttpOnly()
/*      */   {
/*  695 */     return this.httpOnly;
/*      */   }
/*      */ 
/*      */   public void setHttpOnly(boolean paramBoolean)
/*      */   {
/*  709 */     this.httpOnly = paramBoolean;
/*      */   }
/*      */ 
/*      */   public static boolean domainMatches(String paramString1, String paramString2)
/*      */   {
/*  762 */     if ((paramString1 == null) || (paramString2 == null)) {
/*  763 */       return false;
/*      */     }
/*      */ 
/*  766 */     boolean bool = ".local".equalsIgnoreCase(paramString1);
/*  767 */     int i = paramString1.indexOf('.');
/*  768 */     if (i == 0)
/*  769 */       i = paramString1.indexOf('.', 1);
/*  770 */     if ((!bool) && ((i == -1) || (i == paramString1.length() - 1)))
/*      */     {
/*  772 */       return false;
/*      */     }
/*      */ 
/*  775 */     int j = paramString2.indexOf('.');
/*  776 */     if ((j == -1) && (bool)) {
/*  777 */       return true;
/*      */     }
/*  779 */     int k = paramString1.length();
/*  780 */     int m = paramString2.length() - k;
/*  781 */     if (m == 0)
/*      */     {
/*  783 */       return paramString2.equalsIgnoreCase(paramString1);
/*      */     }
/*  785 */     if (m > 0)
/*      */     {
/*  787 */       String str1 = paramString2.substring(0, m);
/*  788 */       String str2 = paramString2.substring(m);
/*      */ 
/*  790 */       return (str1.indexOf('.') == -1) && (str2.equalsIgnoreCase(paramString1));
/*      */     }
/*  792 */     if (m == -1)
/*      */     {
/*  794 */       return (paramString1.charAt(0) == '.') && (paramString2.equalsIgnoreCase(paramString1.substring(1)));
/*      */     }
/*      */ 
/*  798 */     return false;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  811 */     if (getVersion() > 0) {
/*  812 */       return toRFC2965HeaderString();
/*      */     }
/*  814 */     return toNetscapeHeaderString();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  832 */     if (paramObject == this)
/*  833 */       return true;
/*  834 */     if (!(paramObject instanceof HttpCookie))
/*  835 */       return false;
/*  836 */     HttpCookie localHttpCookie = (HttpCookie)paramObject;
/*      */ 
/*  842 */     return (equalsIgnoreCase(getName(), localHttpCookie.getName())) && (equalsIgnoreCase(getDomain(), localHttpCookie.getDomain())) && (Objects.equals(getPath(), localHttpCookie.getPath()));
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  863 */     int i = this.name.toLowerCase().hashCode();
/*  864 */     int j = this.domain != null ? this.domain.toLowerCase().hashCode() : 0;
/*  865 */     int k = this.path != null ? this.path.hashCode() : 0;
/*      */ 
/*  867 */     return i + j + k;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  878 */       return super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  880 */       throw new RuntimeException(localCloneNotSupportedException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean isToken(String paramString)
/*      */   {
/*  904 */     int i = paramString.length();
/*      */ 
/*  906 */     for (int j = 0; j < i; j++) {
/*  907 */       int k = paramString.charAt(j);
/*      */ 
/*  909 */       if ((k < 32) || (k >= 127) || (",;".indexOf(k) != -1))
/*  910 */         return false;
/*      */     }
/*  912 */     return true;
/*      */   }
/*      */ 
/*      */   private static HttpCookie parseInternal(String paramString, boolean paramBoolean)
/*      */   {
/*  929 */     HttpCookie localHttpCookie = null;
/*  930 */     String str1 = null;
/*      */ 
/*  932 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ";");
/*      */     String str2;
/*      */     String str3;
/*      */     try
/*      */     {
/*  937 */       str1 = localStringTokenizer.nextToken();
/*  938 */       int i = str1.indexOf('=');
/*  939 */       if (i != -1) {
/*  940 */         str2 = str1.substring(0, i).trim();
/*  941 */         str3 = str1.substring(i + 1).trim();
/*  942 */         if (paramBoolean) {
/*  943 */           localHttpCookie = new HttpCookie(str2, stripOffSurroundingQuote(str3), paramString);
/*      */         }
/*      */         else
/*      */         {
/*  947 */           localHttpCookie = new HttpCookie(str2, stripOffSurroundingQuote(str3));
/*      */         }
/*      */       }
/*      */       else {
/*  951 */         throw new IllegalArgumentException("Invalid cookie name-value pair");
/*      */       }
/*      */     } catch (NoSuchElementException localNoSuchElementException) {
/*  954 */       throw new IllegalArgumentException("Empty cookie header string");
/*      */     }
/*      */ 
/*  958 */     while (localStringTokenizer.hasMoreTokens()) {
/*  959 */       str1 = localStringTokenizer.nextToken();
/*  960 */       int j = str1.indexOf('=');
/*      */ 
/*  962 */       if (j != -1) {
/*  963 */         str2 = str1.substring(0, j).trim();
/*  964 */         str3 = str1.substring(j + 1).trim();
/*      */       } else {
/*  966 */         str2 = str1.trim();
/*  967 */         str3 = null;
/*      */       }
/*      */ 
/*  971 */       assignAttribute(localHttpCookie, str2, str3);
/*      */     }
/*      */ 
/*  974 */     return localHttpCookie;
/*      */   }
/*      */ 
/*      */   private static void assignAttribute(HttpCookie paramHttpCookie, String paramString1, String paramString2)
/*      */   {
/* 1061 */     paramString2 = stripOffSurroundingQuote(paramString2);
/*      */ 
/* 1063 */     CookieAttributeAssignor localCookieAttributeAssignor = (CookieAttributeAssignor)assignors.get(paramString1.toLowerCase());
/* 1064 */     if (localCookieAttributeAssignor != null)
/* 1065 */       localCookieAttributeAssignor.assign(paramHttpCookie, paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   private String header()
/*      */   {
/* 1090 */     return this.header;
/*      */   }
/*      */ 
/*      */   private String toNetscapeHeaderString()
/*      */   {
/* 1098 */     StringBuilder localStringBuilder = new StringBuilder();
/*      */ 
/* 1100 */     localStringBuilder.append(getName() + "=" + getValue());
/*      */ 
/* 1102 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private String toRFC2965HeaderString()
/*      */   {
/* 1110 */     StringBuilder localStringBuilder = new StringBuilder();
/*      */ 
/* 1112 */     localStringBuilder.append(getName()).append("=\"").append(getValue()).append('"');
/* 1113 */     if (getPath() != null)
/* 1114 */       localStringBuilder.append(";$Path=\"").append(getPath()).append('"');
/* 1115 */     if (getDomain() != null)
/* 1116 */       localStringBuilder.append(";$Domain=\"").append(getDomain()).append('"');
/* 1117 */     if (getPortlist() != null) {
/* 1118 */       localStringBuilder.append(";$Port=\"").append(getPortlist()).append('"');
/*      */     }
/* 1120 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private long expiryDate2DeltaSeconds(String paramString)
/*      */   {
/* 1133 */     GregorianCalendar localGregorianCalendar = new GregorianCalendar(GMT);
/* 1134 */     for (int i = 0; i < COOKIE_DATE_FORMATS.length; i++) {
/* 1135 */       SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(COOKIE_DATE_FORMATS[i], Locale.US);
/*      */ 
/* 1137 */       localGregorianCalendar.set(1970, 0, 1, 0, 0, 0);
/* 1138 */       localSimpleDateFormat.setTimeZone(GMT);
/* 1139 */       localSimpleDateFormat.setLenient(false);
/* 1140 */       localSimpleDateFormat.set2DigitYearStart(localGregorianCalendar.getTime());
/*      */       try {
/* 1142 */         localGregorianCalendar.setTime(localSimpleDateFormat.parse(paramString));
/* 1143 */         if (!COOKIE_DATE_FORMATS[i].contains("yyyy"))
/*      */         {
/* 1146 */           int j = localGregorianCalendar.get(1);
/* 1147 */           j %= 100;
/* 1148 */           if (j < 70)
/* 1149 */             j += 2000;
/*      */           else {
/* 1151 */             j += 1900;
/*      */           }
/* 1153 */           localGregorianCalendar.set(1, j);
/*      */         }
/* 1155 */         return (localGregorianCalendar.getTimeInMillis() - this.whenCreated) / 1000L;
/*      */       }
/*      */       catch (Exception localException) {
/*      */       }
/*      */     }
/* 1160 */     return 0L;
/*      */   }
/*      */ 
/*      */   private static int guessCookieVersion(String paramString)
/*      */   {
/* 1168 */     int i = 0;
/*      */ 
/* 1170 */     paramString = paramString.toLowerCase();
/* 1171 */     if (paramString.indexOf("expires=") != -1)
/*      */     {
/* 1173 */       i = 0;
/* 1174 */     } else if (paramString.indexOf("version=") != -1)
/*      */     {
/* 1176 */       i = 1;
/* 1177 */     } else if (paramString.indexOf("max-age") != -1)
/*      */     {
/* 1179 */       i = 1;
/* 1180 */     } else if (startsWithIgnoreCase(paramString, "set-cookie2:"))
/*      */     {
/* 1182 */       i = 1;
/*      */     }
/*      */ 
/* 1185 */     return i;
/*      */   }
/*      */ 
/*      */   private static String stripOffSurroundingQuote(String paramString) {
/* 1189 */     if ((paramString != null) && (paramString.length() > 2) && (paramString.charAt(0) == '"') && (paramString.charAt(paramString.length() - 1) == '"'))
/*      */     {
/* 1191 */       return paramString.substring(1, paramString.length() - 1);
/*      */     }
/* 1193 */     if ((paramString != null) && (paramString.length() > 2) && (paramString.charAt(0) == '\'') && (paramString.charAt(paramString.length() - 1) == '\''))
/*      */     {
/* 1195 */       return paramString.substring(1, paramString.length() - 1);
/*      */     }
/* 1197 */     return paramString;
/*      */   }
/*      */ 
/*      */   private static boolean equalsIgnoreCase(String paramString1, String paramString2) {
/* 1201 */     if (paramString1 == paramString2) return true;
/* 1202 */     if ((paramString1 != null) && (paramString2 != null)) {
/* 1203 */       return paramString1.equalsIgnoreCase(paramString2);
/*      */     }
/* 1205 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean startsWithIgnoreCase(String paramString1, String paramString2) {
/* 1209 */     if ((paramString1 == null) || (paramString2 == null)) return false;
/*      */ 
/* 1211 */     if ((paramString1.length() >= paramString2.length()) && (paramString2.equalsIgnoreCase(paramString1.substring(0, paramString2.length()))))
/*      */     {
/* 1213 */       return true;
/*      */     }
/*      */ 
/* 1216 */     return false;
/*      */   }
/*      */ 
/*      */   private static List<String> splitMultiCookies(String paramString)
/*      */   {
/* 1231 */     ArrayList localArrayList = new ArrayList();
/* 1232 */     int i = 0;
/*      */ 
/* 1235 */     int j = 0; for (int k = 0; j < paramString.length(); j++) {
/* 1236 */       int m = paramString.charAt(j);
/* 1237 */       if (m == 34) i++;
/* 1238 */       if ((m == 44) && (i % 2 == 0)) {
/* 1239 */         localArrayList.add(paramString.substring(k, j));
/* 1240 */         k = j + 1;
/*      */       }
/*      */     }
/*      */ 
/* 1244 */     localArrayList.add(paramString.substring(k));
/*      */ 
/* 1246 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  987 */     assignors = new HashMap();
/*  988 */     assignors.put("comment", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/*  990 */         if (paramAnonymousHttpCookie.getComment() == null) paramAnonymousHttpCookie.setComment(paramAnonymousString2);
/*      */       }
/*      */     });
/*  993 */     assignors.put("commenturl", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/*  995 */         if (paramAnonymousHttpCookie.getCommentURL() == null) paramAnonymousHttpCookie.setCommentURL(paramAnonymousString2);
/*      */       }
/*      */     });
/*  998 */     assignors.put("discard", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/* 1000 */         paramAnonymousHttpCookie.setDiscard(true);
/*      */       }
/*      */     });
/* 1003 */     assignors.put("domain", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/* 1005 */         if (paramAnonymousHttpCookie.getDomain() == null) paramAnonymousHttpCookie.setDomain(paramAnonymousString2);
/*      */       }
/*      */     });
/* 1008 */     assignors.put("max-age", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/*      */         try {
/* 1011 */           long l = Long.parseLong(paramAnonymousString2);
/* 1012 */           if (paramAnonymousHttpCookie.getMaxAge() == -1L) paramAnonymousHttpCookie.setMaxAge(l); 
/*      */         }
/* 1014 */         catch (NumberFormatException localNumberFormatException) { throw new IllegalArgumentException("Illegal cookie max-age attribute"); }
/*      */ 
/*      */       }
/*      */     });
/* 1018 */     assignors.put("path", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/* 1020 */         if (paramAnonymousHttpCookie.getPath() == null) paramAnonymousHttpCookie.setPath(paramAnonymousString2);
/*      */       }
/*      */     });
/* 1023 */     assignors.put("port", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/* 1025 */         if (paramAnonymousHttpCookie.getPortlist() == null) paramAnonymousHttpCookie.setPortlist(paramAnonymousString2 == null ? "" : paramAnonymousString2);
/*      */       }
/*      */     });
/* 1028 */     assignors.put("secure", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/* 1030 */         paramAnonymousHttpCookie.setSecure(true);
/*      */       }
/*      */     });
/* 1033 */     assignors.put("httponly", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/* 1035 */         paramAnonymousHttpCookie.setHttpOnly(true);
/*      */       }
/*      */     });
/* 1038 */     assignors.put("version", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/*      */         try {
/* 1041 */           int i = Integer.parseInt(paramAnonymousString2);
/* 1042 */           paramAnonymousHttpCookie.setVersion(i);
/*      */         }
/*      */         catch (NumberFormatException localNumberFormatException)
/*      */         {
/*      */         }
/*      */       }
/*      */     });
/* 1048 */     assignors.put("expires", new CookieAttributeAssignor() {
/*      */       public void assign(HttpCookie paramAnonymousHttpCookie, String paramAnonymousString1, String paramAnonymousString2) {
/* 1050 */         if (paramAnonymousHttpCookie.getMaxAge() == -1L)
/* 1051 */           paramAnonymousHttpCookie.setMaxAge(paramAnonymousHttpCookie.expiryDate2DeltaSeconds(paramAnonymousString2));
/*      */       }
/*      */     });
/* 1072 */     SharedSecrets.setJavaNetHttpCookieAccess(new JavaNetHttpCookieAccess()
/*      */     {
/*      */       public List<HttpCookie> parse(String paramAnonymousString) {
/* 1075 */         return HttpCookie.parse(paramAnonymousString, true);
/*      */       }
/*      */ 
/*      */       public String header(HttpCookie paramAnonymousHttpCookie) {
/* 1079 */         return paramAnonymousHttpCookie.header;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   static abstract interface CookieAttributeAssignor
/*      */   {
/*      */     public abstract void assign(HttpCookie paramHttpCookie, String paramString1, String paramString2);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.HttpCookie
 * JD-Core Version:    0.6.2
 */