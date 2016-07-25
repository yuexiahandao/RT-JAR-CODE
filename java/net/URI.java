/*      */ package java.net;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.CharacterCodingException;
/*      */ import java.nio.charset.CharsetDecoder;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.CoderResult;
/*      */ import java.nio.charset.CodingErrorAction;
/*      */ import java.text.Normalizer;
/*      */ import java.text.Normalizer.Form;
/*      */ import sun.nio.cs.ThreadLocalCoders;
/*      */ 
/*      */ public final class URI
/*      */   implements Comparable<URI>, Serializable
/*      */ {
/*      */   static final long serialVersionUID = -6052424284110960213L;
/*      */   private transient String scheme;
/*      */   private transient String fragment;
/*      */   private transient String authority;
/*      */   private transient String userInfo;
/*      */   private transient String host;
/*  494 */   private transient int port = -1;
/*      */   private transient String path;
/*      */   private transient String query;
/*      */   private volatile transient String schemeSpecificPart;
/*      */   private volatile transient int hash;
/*  505 */   private volatile transient String decodedUserInfo = null;
/*  506 */   private volatile transient String decodedAuthority = null;
/*  507 */   private volatile transient String decodedPath = null;
/*  508 */   private volatile transient String decodedQuery = null;
/*  509 */   private volatile transient String decodedFragment = null;
/*  510 */   private volatile transient String decodedSchemeSpecificPart = null;
/*      */   private volatile String string;
/* 2510 */   private static final long L_DIGIT = lowMask('0', '9');
/*      */   private static final long H_DIGIT = 0L;
/*      */   private static final long L_UPALPHA = 0L;
/* 2517 */   private static final long H_UPALPHA = highMask('A', 'Z');
/*      */   private static final long L_LOWALPHA = 0L;
/* 2523 */   private static final long H_LOWALPHA = highMask('a', 'z');
/*      */   private static final long L_ALPHA = 0L;
/* 2527 */   private static final long H_ALPHA = H_LOWALPHA | H_UPALPHA;
/*      */ 
/* 2530 */   private static final long L_ALPHANUM = L_DIGIT | 0L;
/* 2531 */   private static final long H_ALPHANUM = 0L | H_ALPHA;
/*      */ 
/* 2535 */   private static final long L_HEX = L_DIGIT;
/* 2536 */   private static final long H_HEX = highMask('A', 'F') | highMask('a', 'f');
/*      */ 
/* 2540 */   private static final long L_MARK = lowMask("-_.!~*'()");
/* 2541 */   private static final long H_MARK = highMask("-_.!~*'()");
/*      */ 
/* 2544 */   private static final long L_UNRESERVED = L_ALPHANUM | L_MARK;
/* 2545 */   private static final long H_UNRESERVED = H_ALPHANUM | H_MARK;
/*      */ 
/* 2550 */   private static final long L_RESERVED = lowMask(";/?:@&=+$,[]");
/* 2551 */   private static final long H_RESERVED = highMask(";/?:@&=+$,[]");
/*      */   private static final long L_ESCAPED = 1L;
/*      */   private static final long H_ESCAPED = 0L;
/* 2559 */   private static final long L_URIC = L_RESERVED | L_UNRESERVED | 1L;
/* 2560 */   private static final long H_URIC = H_RESERVED | H_UNRESERVED | 0L;
/*      */ 
/* 2564 */   private static final long L_PCHAR = L_UNRESERVED | 1L | lowMask(":@&=+$,");
/*      */ 
/* 2566 */   private static final long H_PCHAR = H_UNRESERVED | 0L | highMask(":@&=+$,");
/*      */ 
/* 2570 */   private static final long L_PATH = L_PCHAR | lowMask(";/");
/* 2571 */   private static final long H_PATH = H_PCHAR | highMask(";/");
/*      */ 
/* 2574 */   private static final long L_DASH = lowMask("-");
/* 2575 */   private static final long H_DASH = highMask("-");
/*      */ 
/* 2578 */   private static final long L_DOT = lowMask(".");
/* 2579 */   private static final long H_DOT = highMask(".");
/*      */ 
/* 2583 */   private static final long L_USERINFO = L_UNRESERVED | 1L | lowMask(";:&=+$,");
/*      */ 
/* 2585 */   private static final long H_USERINFO = H_UNRESERVED | 0L | highMask(";:&=+$,");
/*      */ 
/* 2590 */   private static final long L_REG_NAME = L_UNRESERVED | 1L | lowMask("$,;:@&=+");
/*      */ 
/* 2592 */   private static final long H_REG_NAME = H_UNRESERVED | 0L | highMask("$,;:@&=+");
/*      */ 
/* 2596 */   private static final long L_SERVER = L_USERINFO | L_ALPHANUM | L_DASH | lowMask(".:@[]");
/*      */ 
/* 2598 */   private static final long H_SERVER = H_USERINFO | H_ALPHANUM | H_DASH | highMask(".:@[]");
/*      */ 
/* 2603 */   private static final long L_SERVER_PERCENT = L_SERVER | lowMask("%");
/*      */ 
/* 2605 */   private static final long H_SERVER_PERCENT = H_SERVER | highMask("%");
/*      */ 
/* 2607 */   private static final long L_LEFT_BRACKET = lowMask("[");
/* 2608 */   private static final long H_LEFT_BRACKET = highMask("[");
/*      */ 
/* 2611 */   private static final long L_SCHEME = 0L | L_DIGIT | lowMask("+-.");
/* 2612 */   private static final long H_SCHEME = H_ALPHA | 0L | highMask("+-.");
/*      */ 
/* 2616 */   private static final long L_URIC_NO_SLASH = L_UNRESERVED | 1L | lowMask(";?:@&=+$,");
/*      */ 
/* 2618 */   private static final long H_URIC_NO_SLASH = H_UNRESERVED | 0L | highMask(";?:@&=+$,");
/*      */ 
/* 2624 */   private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*      */ 
/*      */   private URI()
/*      */   {
/*      */   }
/*      */ 
/*      */   public URI(String paramString)
/*      */     throws URISyntaxException
/*      */   {
/*  595 */     new Parser(paramString).parse(false);
/*      */   }
/*      */ 
/*      */   public URI(String paramString1, String paramString2, String paramString3, int paramInt, String paramString4, String paramString5, String paramString6)
/*      */     throws URISyntaxException
/*      */   {
/*  676 */     String str = toString(paramString1, null, null, paramString2, paramString3, paramInt, paramString4, paramString5, paramString6);
/*      */ 
/*  679 */     checkPath(str, paramString1, paramString4);
/*  680 */     new Parser(str).parse(true);
/*      */   }
/*      */ 
/*      */   public URI(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
/*      */     throws URISyntaxException
/*      */   {
/*  749 */     String str = toString(paramString1, null, paramString2, null, null, -1, paramString3, paramString4, paramString5);
/*      */ 
/*  752 */     checkPath(str, paramString1, paramString3);
/*  753 */     new Parser(str).parse(false);
/*      */   }
/*      */ 
/*      */   public URI(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws URISyntaxException
/*      */   {
/*  781 */     this(paramString1, null, paramString2, -1, paramString3, null, paramString4);
/*      */   }
/*      */ 
/*      */   public URI(String paramString1, String paramString2, String paramString3)
/*      */     throws URISyntaxException
/*      */   {
/*  824 */     new Parser(toString(paramString1, paramString2, null, null, null, -1, null, null, paramString3)).parse(false);
/*      */   }
/*      */ 
/*      */   public static URI create(String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  857 */       return new URI(paramString);
/*      */     } catch (URISyntaxException localURISyntaxException) {
/*  859 */       throw new IllegalArgumentException(localURISyntaxException.getMessage(), localURISyntaxException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public URI parseServerAuthority()
/*      */     throws URISyntaxException
/*      */   {
/*  919 */     if ((this.host != null) || (this.authority == null))
/*  920 */       return this;
/*  921 */     defineString();
/*  922 */     new Parser(this.string).parse(true);
/*  923 */     return this;
/*      */   }
/*      */ 
/*      */   public URI normalize()
/*      */   {
/*  964 */     return normalize(this);
/*      */   }
/*      */ 
/*      */   public URI resolve(URI paramURI)
/*      */   {
/* 1022 */     return resolve(this, paramURI);
/*      */   }
/*      */ 
/*      */   public URI resolve(String paramString)
/*      */   {
/* 1043 */     return resolve(create(paramString));
/*      */   }
/*      */ 
/*      */   public URI relativize(URI paramURI)
/*      */   {
/* 1073 */     return relativize(this, paramURI);
/*      */   }
/*      */ 
/*      */   public URL toURL()
/*      */     throws MalformedURLException
/*      */   {
/* 1094 */     if (!isAbsolute())
/* 1095 */       throw new IllegalArgumentException("URI is not absolute");
/* 1096 */     return new URL(toString());
/*      */   }
/*      */ 
/*      */   public String getScheme()
/*      */   {
/* 1115 */     return this.scheme;
/*      */   }
/*      */ 
/*      */   public boolean isAbsolute()
/*      */   {
/* 1126 */     return this.scheme != null;
/*      */   }
/*      */ 
/*      */   public boolean isOpaque()
/*      */   {
/* 1140 */     return this.path == null;
/*      */   }
/*      */ 
/*      */   public String getRawSchemeSpecificPart()
/*      */   {
/* 1154 */     defineSchemeSpecificPart();
/* 1155 */     return this.schemeSpecificPart;
/*      */   }
/*      */ 
/*      */   public String getSchemeSpecificPart()
/*      */   {
/* 1170 */     if (this.decodedSchemeSpecificPart == null)
/* 1171 */       this.decodedSchemeSpecificPart = decode(getRawSchemeSpecificPart());
/* 1172 */     return this.decodedSchemeSpecificPart;
/*      */   }
/*      */ 
/*      */   public String getRawAuthority()
/*      */   {
/* 1189 */     return this.authority;
/*      */   }
/*      */ 
/*      */   public String getAuthority()
/*      */   {
/* 1203 */     if (this.decodedAuthority == null)
/* 1204 */       this.decodedAuthority = decode(this.authority);
/* 1205 */     return this.decodedAuthority;
/*      */   }
/*      */ 
/*      */   public String getRawUserInfo()
/*      */   {
/* 1219 */     return this.userInfo;
/*      */   }
/*      */ 
/*      */   public String getUserInfo()
/*      */   {
/* 1233 */     if ((this.decodedUserInfo == null) && (this.userInfo != null))
/* 1234 */       this.decodedUserInfo = decode(this.userInfo);
/* 1235 */     return this.decodedUserInfo;
/*      */   }
/*      */ 
/*      */   public String getHost()
/*      */   {
/* 1275 */     return this.host;
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/* 1288 */     return this.port;
/*      */   }
/*      */ 
/*      */   public String getRawPath()
/*      */   {
/* 1303 */     return this.path;
/*      */   }
/*      */ 
/*      */   public String getPath()
/*      */   {
/* 1317 */     if ((this.decodedPath == null) && (this.path != null))
/* 1318 */       this.decodedPath = decode(this.path);
/* 1319 */     return this.decodedPath;
/*      */   }
/*      */ 
/*      */   public String getRawQuery()
/*      */   {
/* 1332 */     return this.query;
/*      */   }
/*      */ 
/*      */   public String getQuery()
/*      */   {
/* 1346 */     if ((this.decodedQuery == null) && (this.query != null))
/* 1347 */       this.decodedQuery = decode(this.query);
/* 1348 */     return this.decodedQuery;
/*      */   }
/*      */ 
/*      */   public String getRawFragment()
/*      */   {
/* 1361 */     return this.fragment;
/*      */   }
/*      */ 
/*      */   public String getFragment()
/*      */   {
/* 1375 */     if ((this.decodedFragment == null) && (this.fragment != null))
/* 1376 */       this.decodedFragment = decode(this.fragment);
/* 1377 */     return this.decodedFragment;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1421 */     if (paramObject == this)
/* 1422 */       return true;
/* 1423 */     if (!(paramObject instanceof URI))
/* 1424 */       return false;
/* 1425 */     URI localURI = (URI)paramObject;
/* 1426 */     if (isOpaque() != localURI.isOpaque()) return false;
/* 1427 */     if (!equalIgnoringCase(this.scheme, localURI.scheme)) return false;
/* 1428 */     if (!equal(this.fragment, localURI.fragment)) return false;
/*      */ 
/* 1431 */     if (isOpaque()) {
/* 1432 */       return equal(this.schemeSpecificPart, localURI.schemeSpecificPart);
/*      */     }
/*      */ 
/* 1435 */     if (!equal(this.path, localURI.path)) return false;
/* 1436 */     if (!equal(this.query, localURI.query)) return false;
/*      */ 
/* 1439 */     if (this.authority == localURI.authority) return true;
/* 1440 */     if (this.host != null)
/*      */     {
/* 1442 */       if (!equal(this.userInfo, localURI.userInfo)) return false;
/* 1443 */       if (!equalIgnoringCase(this.host, localURI.host)) return false;
/* 1444 */       if (this.port != localURI.port) return false; 
/*      */     }
/* 1445 */     else if (this.authority != null)
/*      */     {
/* 1447 */       if (!equal(this.authority, localURI.authority)) return false; 
/*      */     }
/* 1448 */     else if (this.authority != localURI.authority) {
/* 1449 */       return false;
/*      */     }
/*      */ 
/* 1452 */     return true;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1463 */     if (this.hash != 0)
/* 1464 */       return this.hash;
/* 1465 */     int i = hashIgnoringCase(0, this.scheme);
/* 1466 */     i = hash(i, this.fragment);
/* 1467 */     if (isOpaque()) {
/* 1468 */       i = hash(i, this.schemeSpecificPart);
/*      */     } else {
/* 1470 */       i = hash(i, this.path);
/* 1471 */       i = hash(i, this.query);
/* 1472 */       if (this.host != null) {
/* 1473 */         i = hash(i, this.userInfo);
/* 1474 */         i = hashIgnoringCase(i, this.host);
/* 1475 */         i += 1949 * this.port;
/*      */       } else {
/* 1477 */         i = hash(i, this.authority);
/*      */       }
/*      */     }
/* 1480 */     this.hash = i;
/* 1481 */     return i;
/*      */   }
/*      */ 
/*      */   public int compareTo(URI paramURI)
/*      */   {
/*      */     int i;
/* 1555 */     if ((i = compareIgnoringCase(this.scheme, paramURI.scheme)) != 0) {
/* 1556 */       return i;
/*      */     }
/* 1558 */     if (isOpaque()) {
/* 1559 */       if (paramURI.isOpaque())
/*      */       {
/* 1561 */         if ((i = compare(this.schemeSpecificPart, paramURI.schemeSpecificPart)) != 0)
/*      */         {
/* 1563 */           return i;
/* 1564 */         }return compare(this.fragment, paramURI.fragment);
/*      */       }
/* 1566 */       return 1;
/* 1567 */     }if (paramURI.isOpaque()) {
/* 1568 */       return -1;
/*      */     }
/*      */ 
/* 1572 */     if ((this.host != null) && (paramURI.host != null))
/*      */     {
/* 1574 */       if ((i = compare(this.userInfo, paramURI.userInfo)) != 0)
/* 1575 */         return i;
/* 1576 */       if ((i = compareIgnoringCase(this.host, paramURI.host)) != 0)
/* 1577 */         return i;
/* 1578 */       if ((i = this.port - paramURI.port) != 0) {
/* 1579 */         return i;
/*      */       }
/*      */ 
/*      */     }
/* 1587 */     else if ((i = compare(this.authority, paramURI.authority)) != 0) { return i; }
/*      */ 
/*      */ 
/* 1590 */     if ((i = compare(this.path, paramURI.path)) != 0) return i;
/* 1591 */     if ((i = compare(this.query, paramURI.query)) != 0) return i;
/* 1592 */     return compare(this.fragment, paramURI.fragment);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1610 */     defineString();
/* 1611 */     return this.string;
/*      */   }
/*      */ 
/*      */   public String toASCIIString()
/*      */   {
/* 1628 */     defineString();
/* 1629 */     return encode(this.string);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1649 */     defineString();
/* 1650 */     paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1666 */     this.port = -1;
/* 1667 */     paramObjectInputStream.defaultReadObject();
/*      */     try {
/* 1669 */       new Parser(this.string).parse(false);
/*      */     } catch (URISyntaxException localURISyntaxException) {
/* 1671 */       InvalidObjectException localInvalidObjectException = new InvalidObjectException("Invalid URI");
/* 1672 */       localInvalidObjectException.initCause(localURISyntaxException);
/* 1673 */       throw localInvalidObjectException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int toLower(char paramChar)
/*      */   {
/* 1692 */     if ((paramChar >= 'A') && (paramChar <= 'Z'))
/* 1693 */       return paramChar + ' ';
/* 1694 */     return paramChar;
/*      */   }
/*      */ 
/*      */   private static boolean equal(String paramString1, String paramString2) {
/* 1698 */     if (paramString1 == paramString2) return true;
/* 1699 */     if ((paramString1 != null) && (paramString2 != null)) {
/* 1700 */       if (paramString1.length() != paramString2.length())
/* 1701 */         return false;
/* 1702 */       if (paramString1.indexOf('%') < 0)
/* 1703 */         return paramString1.equals(paramString2);
/* 1704 */       int i = paramString1.length();
/* 1705 */       for (int j = 0; j < i; ) {
/* 1706 */         int k = paramString1.charAt(j);
/* 1707 */         int m = paramString2.charAt(j);
/* 1708 */         if (k != 37) {
/* 1709 */           if (k != m)
/* 1710 */             return false;
/* 1711 */           j++;
/*      */         }
/*      */         else {
/* 1714 */           if (m != 37)
/* 1715 */             return false;
/* 1716 */           j++;
/* 1717 */           if (toLower(paramString1.charAt(j)) != toLower(paramString2.charAt(j)))
/* 1718 */             return false;
/* 1719 */           j++;
/* 1720 */           if (toLower(paramString1.charAt(j)) != toLower(paramString2.charAt(j)))
/* 1721 */             return false;
/* 1722 */           j++;
/*      */         }
/*      */       }
/* 1724 */       return true;
/*      */     }
/* 1726 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean equalIgnoringCase(String paramString1, String paramString2)
/*      */   {
/* 1731 */     if (paramString1 == paramString2) return true;
/* 1732 */     if ((paramString1 != null) && (paramString2 != null)) {
/* 1733 */       int i = paramString1.length();
/* 1734 */       if (paramString2.length() != i)
/* 1735 */         return false;
/* 1736 */       for (int j = 0; j < i; j++) {
/* 1737 */         if (toLower(paramString1.charAt(j)) != toLower(paramString2.charAt(j)))
/* 1738 */           return false;
/*      */       }
/* 1740 */       return true;
/*      */     }
/* 1742 */     return false;
/*      */   }
/*      */ 
/*      */   private static int hash(int paramInt, String paramString) {
/* 1746 */     if (paramString == null) return paramInt;
/* 1747 */     return paramInt * 127 + paramString.hashCode();
/*      */   }
/*      */ 
/*      */   private static int hashIgnoringCase(int paramInt, String paramString)
/*      */   {
/* 1752 */     if (paramString == null) return paramInt;
/* 1753 */     int i = paramInt;
/* 1754 */     int j = paramString.length();
/* 1755 */     for (int k = 0; k < j; k++)
/* 1756 */       i = 31 * i + toLower(paramString.charAt(k));
/* 1757 */     return i;
/*      */   }
/*      */ 
/*      */   private static int compare(String paramString1, String paramString2) {
/* 1761 */     if (paramString1 == paramString2) return 0;
/* 1762 */     if (paramString1 != null) {
/* 1763 */       if (paramString2 != null) {
/* 1764 */         return paramString1.compareTo(paramString2);
/*      */       }
/* 1766 */       return 1;
/*      */     }
/* 1768 */     return -1;
/*      */   }
/*      */ 
/*      */   private static int compareIgnoringCase(String paramString1, String paramString2)
/*      */   {
/* 1774 */     if (paramString1 == paramString2) return 0;
/* 1775 */     if (paramString1 != null) {
/* 1776 */       if (paramString2 != null) {
/* 1777 */         int i = paramString1.length();
/* 1778 */         int j = paramString2.length();
/* 1779 */         int k = i < j ? i : j;
/* 1780 */         for (int m = 0; m < k; m++) {
/* 1781 */           int n = toLower(paramString1.charAt(m)) - toLower(paramString2.charAt(m));
/* 1782 */           if (n != 0)
/* 1783 */             return n;
/*      */         }
/* 1785 */         return i - j;
/*      */       }
/* 1787 */       return 1;
/*      */     }
/* 1789 */     return -1;
/*      */   }
/*      */ 
/*      */   private static void checkPath(String paramString1, String paramString2, String paramString3)
/*      */     throws URISyntaxException
/*      */   {
/* 1801 */     if ((paramString2 != null) && 
/* 1802 */       (paramString3 != null) && (paramString3.length() > 0) && (paramString3.charAt(0) != '/'))
/*      */     {
/* 1804 */       throw new URISyntaxException(paramString1, "Relative path in absolute URI");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void appendAuthority(StringBuffer paramStringBuffer, String paramString1, String paramString2, String paramString3, int paramInt)
/*      */   {
/*      */     int i;
/* 1815 */     if (paramString3 != null) {
/* 1816 */       paramStringBuffer.append("//");
/* 1817 */       if (paramString2 != null) {
/* 1818 */         paramStringBuffer.append(quote(paramString2, L_USERINFO, H_USERINFO));
/* 1819 */         paramStringBuffer.append('@');
/*      */       }
/* 1821 */       i = (paramString3.indexOf(':') >= 0) && (!paramString3.startsWith("[")) && (!paramString3.endsWith("]")) ? 1 : 0;
/*      */ 
/* 1824 */       if (i != 0) paramStringBuffer.append('[');
/* 1825 */       paramStringBuffer.append(paramString3);
/* 1826 */       if (i != 0) paramStringBuffer.append(']');
/* 1827 */       if (paramInt != -1) {
/* 1828 */         paramStringBuffer.append(':');
/* 1829 */         paramStringBuffer.append(paramInt);
/*      */       }
/* 1831 */     } else if (paramString1 != null) {
/* 1832 */       paramStringBuffer.append("//");
/* 1833 */       if (paramString1.startsWith("["))
/*      */       {
/* 1835 */         i = paramString1.indexOf("]");
/* 1836 */         String str1 = paramString1; String str2 = "";
/* 1837 */         if ((i != -1) && (paramString1.indexOf(":") != -1))
/*      */         {
/* 1839 */           if (i == paramString1.length()) {
/* 1840 */             str2 = paramString1;
/* 1841 */             str1 = "";
/*      */           } else {
/* 1843 */             str2 = paramString1.substring(0, i + 1);
/* 1844 */             str1 = paramString1.substring(i + 1);
/*      */           }
/*      */         }
/* 1847 */         paramStringBuffer.append(str2);
/* 1848 */         paramStringBuffer.append(quote(str1, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
/*      */       }
/*      */       else
/*      */       {
/* 1852 */         paramStringBuffer.append(quote(paramString1, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void appendSchemeSpecificPart(StringBuffer paramStringBuffer, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt, String paramString5, String paramString6)
/*      */   {
/* 1868 */     if (paramString1 != null)
/*      */     {
/* 1872 */       if (paramString1.startsWith("//[")) {
/* 1873 */         int i = paramString1.indexOf("]");
/* 1874 */         if ((i != -1) && (paramString1.indexOf(":") != -1))
/*      */         {
/*      */           String str2;
/*      */           String str1;
/* 1876 */           if (i == paramString1.length()) {
/* 1877 */             str2 = paramString1;
/* 1878 */             str1 = "";
/*      */           } else {
/* 1880 */             str2 = paramString1.substring(0, i + 1);
/* 1881 */             str1 = paramString1.substring(i + 1);
/*      */           }
/* 1883 */           paramStringBuffer.append(str2);
/* 1884 */           paramStringBuffer.append(quote(str1, L_URIC, H_URIC));
/*      */         }
/*      */       } else {
/* 1887 */         paramStringBuffer.append(quote(paramString1, L_URIC, H_URIC));
/*      */       }
/*      */     } else {
/* 1890 */       appendAuthority(paramStringBuffer, paramString2, paramString3, paramString4, paramInt);
/* 1891 */       if (paramString5 != null)
/* 1892 */         paramStringBuffer.append(quote(paramString5, L_PATH, H_PATH));
/* 1893 */       if (paramString6 != null) {
/* 1894 */         paramStringBuffer.append('?');
/* 1895 */         paramStringBuffer.append(quote(paramString6, L_URIC, H_URIC));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void appendFragment(StringBuffer paramStringBuffer, String paramString) {
/* 1901 */     if (paramString != null) {
/* 1902 */       paramStringBuffer.append('#');
/* 1903 */       paramStringBuffer.append(quote(paramString, L_URIC, H_URIC));
/*      */     }
/*      */   }
/*      */ 
/*      */   private String toString(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, int paramInt, String paramString6, String paramString7, String paramString8)
/*      */   {
/* 1917 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1918 */     if (paramString1 != null) {
/* 1919 */       localStringBuffer.append(paramString1);
/* 1920 */       localStringBuffer.append(':');
/*      */     }
/* 1922 */     appendSchemeSpecificPart(localStringBuffer, paramString2, paramString3, paramString4, paramString5, paramInt, paramString6, paramString7);
/*      */ 
/* 1925 */     appendFragment(localStringBuffer, paramString8);
/* 1926 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private void defineSchemeSpecificPart() {
/* 1930 */     if (this.schemeSpecificPart != null) return;
/* 1931 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1932 */     appendSchemeSpecificPart(localStringBuffer, null, getAuthority(), getUserInfo(), this.host, this.port, getPath(), getQuery());
/*      */ 
/* 1934 */     if (localStringBuffer.length() == 0) return;
/* 1935 */     this.schemeSpecificPart = localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private void defineString() {
/* 1939 */     if (this.string != null) return;
/*      */ 
/* 1941 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1942 */     if (this.scheme != null) {
/* 1943 */       localStringBuffer.append(this.scheme);
/* 1944 */       localStringBuffer.append(':');
/*      */     }
/* 1946 */     if (isOpaque()) {
/* 1947 */       localStringBuffer.append(this.schemeSpecificPart);
/*      */     } else {
/* 1949 */       if (this.host != null) {
/* 1950 */         localStringBuffer.append("//");
/* 1951 */         if (this.userInfo != null) {
/* 1952 */           localStringBuffer.append(this.userInfo);
/* 1953 */           localStringBuffer.append('@');
/*      */         }
/* 1955 */         int i = (this.host.indexOf(':') >= 0) && (!this.host.startsWith("[")) && (!this.host.endsWith("]")) ? 1 : 0;
/*      */ 
/* 1958 */         if (i != 0) localStringBuffer.append('[');
/* 1959 */         localStringBuffer.append(this.host);
/* 1960 */         if (i != 0) localStringBuffer.append(']');
/* 1961 */         if (this.port != -1) {
/* 1962 */           localStringBuffer.append(':');
/* 1963 */           localStringBuffer.append(this.port);
/*      */         }
/* 1965 */       } else if (this.authority != null) {
/* 1966 */         localStringBuffer.append("//");
/* 1967 */         localStringBuffer.append(this.authority);
/*      */       }
/* 1969 */       if (this.path != null)
/* 1970 */         localStringBuffer.append(this.path);
/* 1971 */       if (this.query != null) {
/* 1972 */         localStringBuffer.append('?');
/* 1973 */         localStringBuffer.append(this.query);
/*      */       }
/*      */     }
/* 1976 */     if (this.fragment != null) {
/* 1977 */       localStringBuffer.append('#');
/* 1978 */       localStringBuffer.append(this.fragment);
/*      */     }
/* 1980 */     this.string = localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static String resolvePath(String paramString1, String paramString2, boolean paramBoolean)
/*      */   {
/* 1990 */     int i = paramString1.lastIndexOf('/');
/* 1991 */     int j = paramString2.length();
/* 1992 */     String str = "";
/*      */ 
/* 1994 */     if (j == 0)
/*      */     {
/* 1996 */       if (i >= 0)
/* 1997 */         str = paramString1.substring(0, i + 1);
/*      */     } else {
/* 1999 */       localObject = new StringBuffer(paramString1.length() + j);
/*      */ 
/* 2001 */       if (i >= 0) {
/* 2002 */         ((StringBuffer)localObject).append(paramString1.substring(0, i + 1));
/*      */       }
/* 2004 */       ((StringBuffer)localObject).append(paramString2);
/* 2005 */       str = ((StringBuffer)localObject).toString();
/*      */     }
/*      */ 
/* 2009 */     Object localObject = normalize(str);
/*      */ 
/* 2014 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static URI resolve(URI paramURI1, URI paramURI2)
/*      */   {
/* 2021 */     if ((paramURI2.isOpaque()) || (paramURI1.isOpaque())) {
/* 2022 */       return paramURI2;
/*      */     }
/*      */ 
/* 2025 */     if ((paramURI2.scheme == null) && (paramURI2.authority == null) && (paramURI2.path.equals("")) && (paramURI2.fragment != null) && (paramURI2.query == null))
/*      */     {
/* 2028 */       if ((paramURI1.fragment != null) && (paramURI2.fragment.equals(paramURI1.fragment)))
/*      */       {
/* 2030 */         return paramURI1;
/*      */       }
/* 2032 */       localURI = new URI();
/* 2033 */       localURI.scheme = paramURI1.scheme;
/* 2034 */       localURI.authority = paramURI1.authority;
/* 2035 */       localURI.userInfo = paramURI1.userInfo;
/* 2036 */       localURI.host = paramURI1.host;
/* 2037 */       localURI.port = paramURI1.port;
/* 2038 */       localURI.path = paramURI1.path;
/* 2039 */       localURI.fragment = paramURI2.fragment;
/* 2040 */       localURI.query = paramURI1.query;
/* 2041 */       return localURI;
/*      */     }
/*      */ 
/* 2045 */     if (paramURI2.scheme != null) {
/* 2046 */       return paramURI2;
/*      */     }
/* 2048 */     URI localURI = new URI();
/* 2049 */     localURI.scheme = paramURI1.scheme;
/* 2050 */     localURI.query = paramURI2.query;
/* 2051 */     localURI.fragment = paramURI2.fragment;
/*      */ 
/* 2054 */     if (paramURI2.authority == null) {
/* 2055 */       localURI.authority = paramURI1.authority;
/* 2056 */       localURI.host = paramURI1.host;
/* 2057 */       localURI.userInfo = paramURI1.userInfo;
/* 2058 */       localURI.port = paramURI1.port;
/*      */ 
/* 2060 */       String str = paramURI2.path == null ? "" : paramURI2.path;
/* 2061 */       if ((str.length() > 0) && (str.charAt(0) == '/'))
/*      */       {
/* 2063 */         localURI.path = paramURI2.path;
/*      */       }
/*      */       else
/* 2066 */         localURI.path = resolvePath(paramURI1.path, str, paramURI1.isAbsolute());
/*      */     }
/*      */     else {
/* 2069 */       localURI.authority = paramURI2.authority;
/* 2070 */       localURI.host = paramURI2.host;
/* 2071 */       localURI.userInfo = paramURI2.userInfo;
/* 2072 */       localURI.host = paramURI2.host;
/* 2073 */       localURI.port = paramURI2.port;
/* 2074 */       localURI.path = paramURI2.path;
/*      */     }
/*      */ 
/* 2078 */     return localURI;
/*      */   }
/*      */ 
/*      */   private static URI normalize(URI paramURI)
/*      */   {
/* 2085 */     if ((paramURI.isOpaque()) || (paramURI.path == null) || (paramURI.path.length() == 0)) {
/* 2086 */       return paramURI;
/*      */     }
/* 2088 */     String str = normalize(paramURI.path);
/* 2089 */     if (str == paramURI.path) {
/* 2090 */       return paramURI;
/*      */     }
/* 2092 */     URI localURI = new URI();
/* 2093 */     localURI.scheme = paramURI.scheme;
/* 2094 */     localURI.fragment = paramURI.fragment;
/* 2095 */     localURI.authority = paramURI.authority;
/* 2096 */     localURI.userInfo = paramURI.userInfo;
/* 2097 */     localURI.host = paramURI.host;
/* 2098 */     localURI.port = paramURI.port;
/* 2099 */     localURI.path = str;
/* 2100 */     localURI.query = paramURI.query;
/* 2101 */     return localURI;
/*      */   }
/*      */ 
/*      */   private static URI relativize(URI paramURI1, URI paramURI2)
/*      */   {
/* 2112 */     if ((paramURI2.isOpaque()) || (paramURI1.isOpaque()))
/* 2113 */       return paramURI2;
/* 2114 */     if ((!equalIgnoringCase(paramURI1.scheme, paramURI2.scheme)) || (!equal(paramURI1.authority, paramURI2.authority)))
/*      */     {
/* 2116 */       return paramURI2;
/*      */     }
/* 2118 */     String str1 = normalize(paramURI1.path);
/* 2119 */     String str2 = normalize(paramURI2.path);
/* 2120 */     if (!str1.equals(str2)) {
/* 2121 */       if (!str1.endsWith("/"))
/* 2122 */         str1 = str1 + "/";
/* 2123 */       if (!str2.startsWith(str1)) {
/* 2124 */         return paramURI2;
/*      */       }
/*      */     }
/* 2127 */     URI localURI = new URI();
/* 2128 */     localURI.path = str2.substring(str1.length());
/* 2129 */     localURI.query = paramURI2.query;
/* 2130 */     localURI.fragment = paramURI2.fragment;
/* 2131 */     return localURI;
/*      */   }
/*      */ 
/*      */   private static int needsNormalization(String paramString)
/*      */   {
/* 2160 */     int i = 1;
/* 2161 */     int j = 0;
/* 2162 */     int k = paramString.length() - 1;
/* 2163 */     int m = 0;
/*      */ 
/* 2166 */     while ((m <= k) && 
/* 2167 */       (paramString.charAt(m) == '/')) {
/* 2168 */       m++;
/*      */     }
/* 2170 */     if (m > 1) i = 0;
/*      */     while (true)
/*      */     {
/* 2173 */       if (m > k) {
/*      */         break label174;
/*      */       }
/* 2176 */       if ((paramString.charAt(m) == '.') && ((m == k) || (paramString.charAt(m + 1) == '/') || ((paramString.charAt(m + 1) == '.') && ((m + 1 == k) || (paramString.charAt(m + 2) == '/')))))
/*      */       {
/* 2182 */         i = 0;
/*      */       }
/* 2184 */       j++;
/*      */ 
/* 2187 */       if (m <= k) {
/* 2188 */         if (paramString.charAt(m++) != '/')
/*      */         {
/*      */           break;
/*      */         }
/* 2192 */         while ((m <= k) && 
/* 2193 */           (paramString.charAt(m) == '/')) {
/* 2194 */           i = 0;
/* 2195 */           m++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2202 */     label174: return i != 0 ? -1 : j;
/*      */   }
/*      */ 
/*      */   private static void split(char[] paramArrayOfChar, int[] paramArrayOfInt)
/*      */   {
/* 2217 */     int i = paramArrayOfChar.length - 1;
/* 2218 */     int j = 0;
/* 2219 */     int k = 0;
/*      */ 
/* 2222 */     while ((j <= i) && 
/* 2223 */       (paramArrayOfChar[j] == '/')) { paramArrayOfChar[j] = '\000';
/* 2225 */       j++; continue;
/*      */       break label52; } while (true) { if (j > i) {
/*      */         break label103;
/*      */       }
/* 2231 */       paramArrayOfInt[(k++)] = (j++);
/*      */ 
/* 2234 */       label52: if (j <= i) {
/* 2235 */         if (paramArrayOfChar[(j++)] != '/')
/*      */           break;
/* 2237 */         paramArrayOfChar[(j - 1)] = '\000';
/*      */ 
/* 2240 */         while ((j <= i) && 
/* 2241 */           (paramArrayOfChar[j] == '/')) {
/* 2242 */           paramArrayOfChar[(j++)] = '\000';
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2248 */     label103: if (k != paramArrayOfInt.length)
/* 2249 */       throw new InternalError();
/*      */   }
/*      */ 
/*      */   private static int join(char[] paramArrayOfChar, int[] paramArrayOfInt)
/*      */   {
/* 2266 */     int i = paramArrayOfInt.length;
/* 2267 */     int j = paramArrayOfChar.length - 1;
/* 2268 */     int k = 0;
/*      */ 
/* 2270 */     if (paramArrayOfChar[k] == 0)
/*      */     {
/* 2272 */       paramArrayOfChar[(k++)] = '/';
/*      */     }
/*      */ 
/* 2275 */     for (int m = 0; m < i; m++) {
/* 2276 */       int n = paramArrayOfInt[m];
/* 2277 */       if (n != -1)
/*      */       {
/* 2281 */         if (k == n)
/*      */         {
/* 2283 */           while ((k <= j) && (paramArrayOfChar[k] != 0))
/* 2284 */             k++;
/* 2285 */           if (k <= j)
/*      */           {
/* 2287 */             paramArrayOfChar[(k++)] = '/';
/*      */           }
/* 2289 */         } else if (k < n)
/*      */         {
/* 2291 */           while ((n <= j) && (paramArrayOfChar[n] != 0))
/* 2292 */             paramArrayOfChar[(k++)] = paramArrayOfChar[(n++)];
/* 2293 */           if (n <= j)
/*      */           {
/* 2295 */             paramArrayOfChar[(k++)] = '/';
/*      */           }
/*      */         } else {
/* 2298 */           throw new InternalError();
/*      */         }
/*      */       }
/*      */     }
/* 2301 */     return k;
/*      */   }
/*      */ 
/*      */   private static void removeDots(char[] paramArrayOfChar, int[] paramArrayOfInt)
/*      */   {
/* 2309 */     int i = paramArrayOfInt.length;
/* 2310 */     int j = paramArrayOfChar.length - 1;
/*      */ 
/* 2312 */     for (int k = 0; k < i; k++) {
/* 2313 */       int m = 0;
/*      */       int n;
/*      */       do {
/* 2317 */         n = paramArrayOfInt[k];
/* 2318 */         if (paramArrayOfChar[n] == '.') {
/* 2319 */           if (n == j) {
/* 2320 */             m = 1;
/* 2321 */             break;
/* 2322 */           }if (paramArrayOfChar[(n + 1)] == 0) {
/* 2323 */             m = 1;
/* 2324 */             break;
/* 2325 */           }if ((paramArrayOfChar[(n + 1)] == '.') && ((n + 1 == j) || (paramArrayOfChar[(n + 2)] == 0)))
/*      */           {
/* 2328 */             m = 2;
/* 2329 */             break;
/*      */           }
/*      */         }
/* 2332 */         k++;
/* 2333 */       }while (k < i);
/* 2334 */       if ((k > i) || (m == 0)) {
/*      */         break;
/*      */       }
/* 2337 */       if (m == 1)
/*      */       {
/* 2339 */         paramArrayOfInt[k] = -1;
/*      */       }
/*      */       else
/*      */       {
/* 2345 */         for (n = k - 1; (n >= 0) && 
/* 2346 */           (paramArrayOfInt[n] == -1); n--);
/* 2348 */         if (n >= 0) {
/* 2349 */           int i1 = paramArrayOfInt[n];
/* 2350 */           if ((paramArrayOfChar[i1] != '.') || (paramArrayOfChar[(i1 + 1)] != '.') || (paramArrayOfChar[(i1 + 2)] != 0))
/*      */           {
/* 2353 */             paramArrayOfInt[k] = -1;
/* 2354 */             paramArrayOfInt[n] = -1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void maybeAddLeadingDot(char[] paramArrayOfChar, int[] paramArrayOfInt)
/*      */   {
/* 2367 */     if (paramArrayOfChar[0] == 0)
/*      */     {
/* 2369 */       return;
/*      */     }
/* 2371 */     int i = paramArrayOfInt.length;
/* 2372 */     int j = 0;
/* 2373 */     while ((j < i) && 
/* 2374 */       (paramArrayOfInt[j] < 0))
/*      */     {
/* 2376 */       j++;
/*      */     }
/* 2378 */     if ((j >= i) || (j == 0))
/*      */     {
/* 2381 */       return;
/*      */     }
/* 2383 */     int k = paramArrayOfInt[j];
/* 2384 */     while ((k < paramArrayOfChar.length) && (paramArrayOfChar[k] != ':') && (paramArrayOfChar[k] != 0)) k++;
/* 2385 */     if ((k >= paramArrayOfChar.length) || (paramArrayOfChar[k] == 0))
/*      */     {
/* 2387 */       return;
/*      */     }
/*      */ 
/* 2391 */     paramArrayOfChar[0] = '.';
/* 2392 */     paramArrayOfChar[1] = '\000';
/* 2393 */     paramArrayOfInt[0] = 0;
/*      */   }
/*      */ 
/*      */   private static String normalize(String paramString)
/*      */   {
/* 2406 */     int i = needsNormalization(paramString);
/* 2407 */     if (i < 0)
/*      */     {
/* 2409 */       return paramString;
/*      */     }
/* 2411 */     char[] arrayOfChar = paramString.toCharArray();
/*      */ 
/* 2414 */     int[] arrayOfInt = new int[i];
/* 2415 */     split(arrayOfChar, arrayOfInt);
/*      */ 
/* 2418 */     removeDots(arrayOfChar, arrayOfInt);
/*      */ 
/* 2421 */     maybeAddLeadingDot(arrayOfChar, arrayOfInt);
/*      */ 
/* 2424 */     String str = new String(arrayOfChar, 0, join(arrayOfChar, arrayOfInt));
/* 2425 */     if (str.equals(paramString))
/*      */     {
/* 2427 */       return paramString;
/*      */     }
/* 2429 */     return str;
/*      */   }
/*      */ 
/*      */   private static long lowMask(String paramString)
/*      */   {
/* 2450 */     int i = paramString.length();
/* 2451 */     long l = 0L;
/* 2452 */     for (int j = 0; j < i; j++) {
/* 2453 */       int k = paramString.charAt(j);
/* 2454 */       if (k < 64)
/* 2455 */         l |= 1L << k;
/*      */     }
/* 2457 */     return l;
/*      */   }
/*      */ 
/*      */   private static long highMask(String paramString)
/*      */   {
/* 2462 */     int i = paramString.length();
/* 2463 */     long l = 0L;
/* 2464 */     for (int j = 0; j < i; j++) {
/* 2465 */       int k = paramString.charAt(j);
/* 2466 */       if ((k >= 64) && (k < 128))
/* 2467 */         l |= 1L << k - 64;
/*      */     }
/* 2469 */     return l;
/*      */   }
/*      */ 
/*      */   private static long lowMask(char paramChar1, char paramChar2)
/*      */   {
/* 2475 */     long l = 0L;
/* 2476 */     int i = Math.max(Math.min(paramChar1, 63), 0);
/* 2477 */     int j = Math.max(Math.min(paramChar2, 63), 0);
/* 2478 */     for (int k = i; k <= j; k++)
/* 2479 */       l |= 1L << k;
/* 2480 */     return l;
/*      */   }
/*      */ 
/*      */   private static long highMask(char paramChar1, char paramChar2)
/*      */   {
/* 2486 */     long l = 0L;
/* 2487 */     int i = Math.max(Math.min(paramChar1, 127), 64) - 64;
/* 2488 */     int j = Math.max(Math.min(paramChar2, 127), 64) - 64;
/* 2489 */     for (int k = i; k <= j; k++)
/* 2490 */       l |= 1L << k;
/* 2491 */     return l;
/*      */   }
/*      */ 
/*      */   private static boolean match(char paramChar, long paramLong1, long paramLong2)
/*      */   {
/* 2496 */     if (paramChar == 0)
/* 2497 */       return false;
/* 2498 */     if (paramChar < '@')
/* 2499 */       return (1L << paramChar & paramLong1) != 0L;
/* 2500 */     if (paramChar < '')
/* 2501 */       return (1L << paramChar - '@' & paramLong2) != 0L;
/* 2502 */     return false;
/*      */   }
/*      */ 
/*      */   private static void appendEscape(StringBuffer paramStringBuffer, byte paramByte)
/*      */   {
/* 2630 */     paramStringBuffer.append('%');
/* 2631 */     paramStringBuffer.append(hexDigits[(paramByte >> 4 & 0xF)]);
/* 2632 */     paramStringBuffer.append(hexDigits[(paramByte >> 0 & 0xF)]);
/*      */   }
/*      */ 
/*      */   private static void appendEncoded(StringBuffer paramStringBuffer, char paramChar) {
/* 2636 */     ByteBuffer localByteBuffer = null;
/*      */     try {
/* 2638 */       localByteBuffer = ThreadLocalCoders.encoderFor("UTF-8").encode(CharBuffer.wrap("" + paramChar));
/*      */     }
/*      */     catch (CharacterCodingException localCharacterCodingException) {
/* 2641 */       if (!$assertionsDisabled) throw new AssertionError();
/*      */     }
/* 2643 */     while (localByteBuffer.hasRemaining()) {
/* 2644 */       int i = localByteBuffer.get() & 0xFF;
/* 2645 */       if (i >= 128)
/* 2646 */         appendEscape(paramStringBuffer, (byte)i);
/*      */       else
/* 2648 */         paramStringBuffer.append((char)i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String quote(String paramString, long paramLong1, long paramLong2)
/*      */   {
/* 2656 */     int i = paramString.length();
/* 2657 */     StringBuffer localStringBuffer = null;
/* 2658 */     int j = (paramLong1 & 1L) != 0L ? 1 : 0;
/* 2659 */     for (int k = 0; k < paramString.length(); k++) {
/* 2660 */       char c = paramString.charAt(k);
/* 2661 */       if (c < '') {
/* 2662 */         if (!match(c, paramLong1, paramLong2)) {
/* 2663 */           if (localStringBuffer == null) {
/* 2664 */             localStringBuffer = new StringBuffer();
/* 2665 */             localStringBuffer.append(paramString.substring(0, k));
/*      */           }
/* 2667 */           appendEscape(localStringBuffer, (byte)c);
/*      */         }
/* 2669 */         else if (localStringBuffer != null) {
/* 2670 */           localStringBuffer.append(c);
/*      */         }
/* 2672 */       } else if ((j != 0) && ((Character.isSpaceChar(c)) || (Character.isISOControl(c))))
/*      */       {
/* 2675 */         if (localStringBuffer == null) {
/* 2676 */           localStringBuffer = new StringBuffer();
/* 2677 */           localStringBuffer.append(paramString.substring(0, k));
/*      */         }
/* 2679 */         appendEncoded(localStringBuffer, c);
/*      */       }
/* 2681 */       else if (localStringBuffer != null) {
/* 2682 */         localStringBuffer.append(c);
/*      */       }
/*      */     }
/* 2685 */     return localStringBuffer == null ? paramString : localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static String encode(String paramString)
/*      */   {
/* 2692 */     int i = paramString.length();
/* 2693 */     if (i == 0) {
/* 2694 */       return paramString;
/*      */     }
/*      */ 
/* 2697 */     int j = 0;
/* 2698 */     while (paramString.charAt(j) < '')
/*      */     {
/* 2700 */       j++; if (j >= i) {
/* 2701 */         return paramString;
/*      */       }
/*      */     }
/* 2704 */     String str = Normalizer.normalize(paramString, Normalizer.Form.NFC);
/* 2705 */     ByteBuffer localByteBuffer = null;
/*      */     try {
/* 2707 */       localByteBuffer = ThreadLocalCoders.encoderFor("UTF-8").encode(CharBuffer.wrap(str));
/*      */     }
/*      */     catch (CharacterCodingException localCharacterCodingException) {
/* 2710 */       if (!$assertionsDisabled) throw new AssertionError();
/*      */     }
/*      */ 
/* 2713 */     StringBuffer localStringBuffer = new StringBuffer();
/* 2714 */     while (localByteBuffer.hasRemaining()) {
/* 2715 */       int k = localByteBuffer.get() & 0xFF;
/* 2716 */       if (k >= 128)
/* 2717 */         appendEscape(localStringBuffer, (byte)k);
/*      */       else
/* 2719 */         localStringBuffer.append((char)k);
/*      */     }
/* 2721 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static int decode(char paramChar) {
/* 2725 */     if ((paramChar >= '0') && (paramChar <= '9'))
/* 2726 */       return paramChar - '0';
/* 2727 */     if ((paramChar >= 'a') && (paramChar <= 'f'))
/* 2728 */       return paramChar - 'a' + 10;
/* 2729 */     if ((paramChar >= 'A') && (paramChar <= 'F'))
/* 2730 */       return paramChar - 'A' + 10;
/* 2731 */     if (!$assertionsDisabled) throw new AssertionError();
/* 2732 */     return -1;
/*      */   }
/*      */ 
/*      */   private static byte decode(char paramChar1, char paramChar2) {
/* 2736 */     return (byte)((decode(paramChar1) & 0xF) << 4 | (decode(paramChar2) & 0xF) << 0);
/*      */   }
/*      */ 
/*      */   private static String decode(String paramString)
/*      */   {
/* 2748 */     if (paramString == null)
/* 2749 */       return paramString;
/* 2750 */     int i = paramString.length();
/* 2751 */     if (i == 0)
/* 2752 */       return paramString;
/* 2753 */     if (paramString.indexOf('%') < 0) {
/* 2754 */       return paramString;
/*      */     }
/* 2756 */     StringBuffer localStringBuffer = new StringBuffer(i);
/* 2757 */     ByteBuffer localByteBuffer = ByteBuffer.allocate(i);
/* 2758 */     CharBuffer localCharBuffer = CharBuffer.allocate(i);
/* 2759 */     CharsetDecoder localCharsetDecoder = ThreadLocalCoders.decoderFor("UTF-8").onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*      */ 
/* 2764 */     char c = paramString.charAt(0);
/* 2765 */     int j = 0;
/*      */ 
/* 2767 */     for (int k = 0; k < i; ) {
/* 2768 */       assert (c == paramString.charAt(k));
/* 2769 */       if (c == '[')
/* 2770 */         j = 1;
/* 2771 */       else if ((j != 0) && (c == ']')) {
/* 2772 */         j = 0;
/*      */       }
/* 2774 */       if ((c != '%') || (j != 0)) {
/* 2775 */         localStringBuffer.append(c);
/* 2776 */         k++; if (k >= i)
/*      */           break;
/* 2778 */         c = paramString.charAt(k);
/*      */       }
/*      */       else {
/* 2781 */         localByteBuffer.clear();
/* 2782 */         int m = k;
/*      */         while (true) {
/* 2784 */           assert (i - k >= 2);
/* 2785 */           localByteBuffer.put(decode(paramString.charAt(++k), paramString.charAt(++k)));
/* 2786 */           k++; if (k < i)
/*      */           {
/* 2788 */             c = paramString.charAt(k);
/* 2789 */             if (c != '%')
/* 2790 */               break; 
/*      */           }
/*      */         }
/* 2792 */         localByteBuffer.flip();
/* 2793 */         localCharBuffer.clear();
/* 2794 */         localCharsetDecoder.reset();
/* 2795 */         CoderResult localCoderResult = localCharsetDecoder.decode(localByteBuffer, localCharBuffer, true);
/* 2796 */         assert (localCoderResult.isUnderflow());
/* 2797 */         localCoderResult = localCharsetDecoder.flush(localCharBuffer);
/* 2798 */         assert (localCoderResult.isUnderflow());
/* 2799 */         localStringBuffer.append(localCharBuffer.flip().toString());
/*      */       }
/*      */     }
/* 2802 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private class Parser
/*      */   {
/*      */     private String input;
/* 2815 */     private boolean requireServerAuthority = false;
/*      */ 
/* 3425 */     private int ipv6byteCount = 0;
/*      */ 
/*      */     Parser(String arg2)
/*      */     {
/*      */       String str;
/* 2818 */       this.input = str;
/* 2819 */       URI.this.string = str;
/*      */     }
/*      */ 
/*      */     private void fail(String paramString)
/*      */       throws URISyntaxException
/*      */     {
/* 2825 */       throw new URISyntaxException(this.input, paramString);
/*      */     }
/*      */ 
/*      */     private void fail(String paramString, int paramInt) throws URISyntaxException {
/* 2829 */       throw new URISyntaxException(this.input, paramString, paramInt);
/*      */     }
/*      */ 
/*      */     private void failExpecting(String paramString, int paramInt)
/*      */       throws URISyntaxException
/*      */     {
/* 2835 */       fail("Expected " + paramString, paramInt);
/*      */     }
/*      */ 
/*      */     private void failExpecting(String paramString1, String paramString2, int paramInt)
/*      */       throws URISyntaxException
/*      */     {
/* 2841 */       fail("Expected " + paramString1 + " following " + paramString2, paramInt);
/*      */     }
/*      */ 
/*      */     private String substring(int paramInt1, int paramInt2)
/*      */     {
/* 2850 */       return this.input.substring(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     private char charAt(int paramInt)
/*      */     {
/* 2857 */       return this.input.charAt(paramInt);
/*      */     }
/*      */ 
/*      */     private boolean at(int paramInt1, int paramInt2, char paramChar)
/*      */     {
/* 2863 */       return (paramInt1 < paramInt2) && (charAt(paramInt1) == paramChar);
/*      */     }
/*      */ 
/*      */     private boolean at(int paramInt1, int paramInt2, String paramString)
/*      */     {
/* 2870 */       int i = paramInt1;
/* 2871 */       int j = paramString.length();
/* 2872 */       if (j > paramInt2 - i)
/* 2873 */         return false;
/* 2874 */       int k = 0;
/* 2875 */       while ((k < j) && 
/* 2876 */         (charAt(i++) == paramString.charAt(k)))
/*      */       {
/* 2879 */         k++;
/*      */       }
/* 2881 */       return k == j;
/*      */     }
/*      */ 
/*      */     private int scan(int paramInt1, int paramInt2, char paramChar)
/*      */     {
/* 2916 */       if ((paramInt1 < paramInt2) && (charAt(paramInt1) == paramChar))
/* 2917 */         return paramInt1 + 1;
/* 2918 */       return paramInt1;
/*      */     }
/*      */ 
/*      */     private int scan(int paramInt1, int paramInt2, String paramString1, String paramString2)
/*      */     {
/* 2929 */       int i = paramInt1;
/* 2930 */       while (i < paramInt2) {
/* 2931 */         int j = charAt(i);
/* 2932 */         if (paramString1.indexOf(j) >= 0)
/* 2933 */           return -1;
/* 2934 */         if (paramString2.indexOf(j) >= 0)
/*      */           break;
/* 2936 */         i++;
/*      */       }
/* 2938 */       return i;
/*      */     }
/*      */ 
/*      */     private int scanEscape(int paramInt1, int paramInt2, char paramChar)
/*      */       throws URISyntaxException
/*      */     {
/* 2950 */       int i = paramInt1;
/* 2951 */       char c = paramChar;
/* 2952 */       if (c == '%')
/*      */       {
/* 2954 */         if ((i + 3 <= paramInt2) && (URI.match(charAt(i + 1), URI.L_HEX, URI.H_HEX)) && (URI.match(charAt(i + 2), URI.L_HEX, URI.H_HEX)))
/*      */         {
/* 2957 */           return i + 3;
/*      */         }
/* 2959 */         fail("Malformed escape pair", i);
/* 2960 */       } else if ((c > '') && (!Character.isSpaceChar(c)) && (!Character.isISOControl(c)))
/*      */       {
/* 2964 */         return i + 1;
/*      */       }
/* 2966 */       return i;
/*      */     }
/*      */ 
/*      */     private int scan(int paramInt1, int paramInt2, long paramLong1, long paramLong2)
/*      */       throws URISyntaxException
/*      */     {
/* 2974 */       int i = paramInt1;
/* 2975 */       while (i < paramInt2) {
/* 2976 */         char c = charAt(i);
/* 2977 */         if (URI.match(c, paramLong1, paramLong2)) {
/* 2978 */           i++;
/*      */         }
/* 2981 */         else if ((paramLong1 & 1L) != 0L) {
/* 2982 */           int j = scanEscape(i, paramInt2, c);
/* 2983 */           if (j > i) {
/* 2984 */             i = j;
/*      */           }
/*      */           else;
/*      */         }
/*      */       }
/*      */ 
/* 2990 */       return i;
/*      */     }
/*      */ 
/*      */     private void checkChars(int paramInt1, int paramInt2, long paramLong1, long paramLong2, String paramString)
/*      */       throws URISyntaxException
/*      */     {
/* 3000 */       int i = scan(paramInt1, paramInt2, paramLong1, paramLong2);
/* 3001 */       if (i < paramInt2)
/* 3002 */         fail("Illegal character in " + paramString, i);
/*      */     }
/*      */ 
/*      */     private void checkChar(int paramInt, long paramLong1, long paramLong2, String paramString)
/*      */       throws URISyntaxException
/*      */     {
/* 3012 */       checkChars(paramInt, paramInt + 1, paramLong1, paramLong2, paramString);
/*      */     }
/*      */ 
/*      */     void parse(boolean paramBoolean)
/*      */       throws URISyntaxException
/*      */     {
/* 3021 */       this.requireServerAuthority = paramBoolean;
/*      */ 
/* 3023 */       int j = this.input.length();
/* 3024 */       int k = scan(0, j, "/?#", ":");
/*      */       int i;
/* 3025 */       if ((k >= 0) && (at(k, j, ':'))) {
/* 3026 */         if (k == 0)
/* 3027 */           failExpecting("scheme name", 0);
/* 3028 */         checkChar(0, 0L, URI.H_ALPHA, "scheme name");
/* 3029 */         checkChars(1, k, URI.L_SCHEME, URI.H_SCHEME, "scheme name");
/* 3030 */         URI.this.scheme = substring(0, k);
/* 3031 */         k++;
/* 3032 */         i = k;
/* 3033 */         if (at(k, j, '/')) {
/* 3034 */           k = parseHierarchical(k, j);
/*      */         } else {
/* 3036 */           int m = scan(k, j, "", "#");
/* 3037 */           if (m <= k)
/* 3038 */             failExpecting("scheme-specific part", k);
/* 3039 */           checkChars(k, m, URI.L_URIC, URI.H_URIC, "opaque part");
/* 3040 */           k = m;
/*      */         }
/*      */       } else {
/* 3043 */         i = 0;
/* 3044 */         k = parseHierarchical(0, j);
/*      */       }
/* 3046 */       URI.this.schemeSpecificPart = substring(i, k);
/* 3047 */       if (at(k, j, '#')) {
/* 3048 */         checkChars(k + 1, j, URI.L_URIC, URI.H_URIC, "fragment");
/* 3049 */         URI.this.fragment = substring(k + 1, j);
/* 3050 */         k = j;
/*      */       }
/* 3052 */       if (k < j)
/* 3053 */         fail("end of URI", k);
/*      */     }
/*      */ 
/*      */     private int parseHierarchical(int paramInt1, int paramInt2)
/*      */       throws URISyntaxException
/*      */     {
/* 3073 */       int i = paramInt1;
/* 3074 */       if ((at(i, paramInt2, '/')) && (at(i + 1, paramInt2, '/'))) {
/* 3075 */         i += 2;
/* 3076 */         j = scan(i, paramInt2, "", "/?#");
/* 3077 */         if (j > i)
/* 3078 */           i = parseAuthority(i, j);
/* 3079 */         else if (j >= paramInt2)
/*      */         {
/* 3083 */           failExpecting("authority", i);
/*      */         }
/*      */       }
/* 3085 */       int j = scan(i, paramInt2, "", "?#");
/* 3086 */       checkChars(i, j, URI.L_PATH, URI.H_PATH, "path");
/* 3087 */       URI.this.path = substring(i, j);
/* 3088 */       i = j;
/* 3089 */       if (at(i, paramInt2, '?')) {
/* 3090 */         i++;
/* 3091 */         j = scan(i, paramInt2, "", "#");
/* 3092 */         checkChars(i, j, URI.L_URIC, URI.H_URIC, "query");
/* 3093 */         URI.this.query = substring(i, j);
/* 3094 */         i = j;
/*      */       }
/* 3096 */       return i;
/*      */     }
/*      */ 
/*      */     private int parseAuthority(int paramInt1, int paramInt2)
/*      */       throws URISyntaxException
/*      */     {
/* 3110 */       int i = paramInt1;
/* 3111 */       int j = i;
/* 3112 */       Object localObject = null;
/*      */       int k;
/* 3117 */       if (scan(i, paramInt2, "", "]") > i)
/*      */       {
/* 3119 */         k = scan(i, paramInt2, URI.L_SERVER_PERCENT, URI.H_SERVER_PERCENT) == paramInt2 ? 1 : 0;
/*      */       }
/* 3121 */       else k = scan(i, paramInt2, URI.L_SERVER, URI.H_SERVER) == paramInt2 ? 1 : 0;
/*      */ 
/* 3123 */       int m = scan(i, paramInt2, URI.L_REG_NAME, URI.H_REG_NAME) == paramInt2 ? 1 : 0;
/*      */ 
/* 3125 */       if ((m != 0) && (k == 0))
/*      */       {
/* 3127 */         URI.this.authority = substring(i, paramInt2);
/* 3128 */         return paramInt2;
/*      */       }
/*      */ 
/* 3131 */       if (k != 0)
/*      */       {
/*      */         try
/*      */         {
/* 3136 */           j = parseServer(i, paramInt2);
/* 3137 */           if (j < paramInt2)
/* 3138 */             failExpecting("end of authority", j);
/* 3139 */           URI.this.authority = substring(i, paramInt2);
/*      */         }
/*      */         catch (URISyntaxException localURISyntaxException) {
/* 3142 */           URI.this.userInfo = null;
/* 3143 */           URI.this.host = null;
/* 3144 */           URI.this.port = -1;
/* 3145 */           if (this.requireServerAuthority)
/*      */           {
/* 3148 */             throw localURISyntaxException;
/*      */           }
/*      */ 
/* 3152 */           localObject = localURISyntaxException;
/* 3153 */           j = i;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3158 */       if (j < paramInt2) {
/* 3159 */         if (m != 0)
/*      */         {
/* 3161 */           URI.this.authority = substring(i, paramInt2); } else {
/* 3162 */           if (localObject != null)
/*      */           {
/* 3165 */             throw localObject;
/*      */           }
/* 3167 */           fail("Illegal character in authority", j);
/*      */         }
/*      */       }
/*      */ 
/* 3171 */       return paramInt2;
/*      */     }
/*      */ 
/*      */     private int parseServer(int paramInt1, int paramInt2)
/*      */       throws URISyntaxException
/*      */     {
/* 3180 */       int i = paramInt1;
/*      */ 
/* 3184 */       int j = scan(i, paramInt2, "/?#", "@");
/* 3185 */       if ((j >= i) && (at(j, paramInt2, '@'))) {
/* 3186 */         checkChars(i, j, URI.L_USERINFO, URI.H_USERINFO, "user info");
/* 3187 */         URI.this.userInfo = substring(i, j);
/* 3188 */         i = j + 1;
/*      */       }
/*      */ 
/* 3192 */       if (at(i, paramInt2, '['))
/*      */       {
/* 3194 */         i++;
/* 3195 */         j = scan(i, paramInt2, "/?#", "]");
/* 3196 */         if ((j > i) && (at(j, paramInt2, ']')))
/*      */         {
/* 3198 */           int k = scan(i, j, "", "%");
/* 3199 */           if (k > i) {
/* 3200 */             parseIPv6Reference(i, k);
/* 3201 */             if (k + 1 == j) {
/* 3202 */               fail("scope id expected");
/*      */             }
/* 3204 */             checkChars(k + 1, j, URI.L_ALPHANUM, URI.H_ALPHANUM, "scope id");
/*      */           }
/*      */           else {
/* 3207 */             parseIPv6Reference(i, j);
/*      */           }
/* 3209 */           URI.this.host = substring(i - 1, j + 1);
/* 3210 */           i = j + 1;
/*      */         } else {
/* 3212 */           failExpecting("closing bracket for IPv6 address", j);
/*      */         }
/*      */       } else {
/* 3215 */         j = parseIPv4Address(i, paramInt2);
/* 3216 */         if (j <= i)
/* 3217 */           j = parseHostname(i, paramInt2);
/* 3218 */         i = j;
/*      */       }
/*      */ 
/* 3222 */       if (at(i, paramInt2, ':')) {
/* 3223 */         i++;
/* 3224 */         j = scan(i, paramInt2, "", "/");
/* 3225 */         if (j > i) {
/* 3226 */           checkChars(i, j, URI.L_DIGIT, 0L, "port number");
/*      */           try {
/* 3228 */             URI.this.port = Integer.parseInt(substring(i, j));
/*      */           } catch (NumberFormatException localNumberFormatException) {
/* 3230 */             fail("Malformed port number", i);
/*      */           }
/* 3232 */           i = j;
/*      */         }
/*      */       }
/* 3235 */       if (i < paramInt2) {
/* 3236 */         failExpecting("port number", i);
/*      */       }
/* 3238 */       return i;
/*      */     }
/*      */ 
/*      */     private int scanByte(int paramInt1, int paramInt2)
/*      */       throws URISyntaxException
/*      */     {
/* 3246 */       int i = paramInt1;
/* 3247 */       int j = scan(i, paramInt2, URI.L_DIGIT, 0L);
/* 3248 */       if (j <= i) return j;
/* 3249 */       if (Integer.parseInt(substring(i, j)) > 255) return i;
/* 3250 */       return j;
/*      */     }
/*      */ 
/*      */     private int scanIPv4Address(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */       throws URISyntaxException
/*      */     {
/* 3271 */       int i = paramInt1;
/*      */ 
/* 3273 */       int k = scan(i, paramInt2, URI.L_DIGIT | URI.L_DOT, 0L | URI.H_DOT);
/* 3274 */       if ((k <= i) || ((paramBoolean) && (k != paramInt2)))
/* 3275 */         return -1;
/* 3279 */       int j;
/* 3279 */       if ((j = scanByte(i, k)) > i) { i = j;
/* 3280 */         if ((j = scan(i, k, '.')) > i) { i = j;
/* 3281 */           if ((j = scanByte(i, k)) > i) { i = j;
/* 3282 */             if ((j = scan(i, k, '.')) > i) { i = j;
/* 3283 */               if ((j = scanByte(i, k)) > i) { i = j;
/* 3284 */                 if ((j = scan(i, k, '.')) > i) { i = j;
/* 3285 */                   if ((j = scanByte(i, k)) > i) { i = j;
/* 3286 */                     if (j >= k)
/* 3287 */                       return j; } } } } } }
/*      */       }
/* 3289 */       fail("Malformed IPv4 address", j);
/* 3290 */       return -1;
/*      */     }
/*      */ 
/*      */     private int takeIPv4Address(int paramInt1, int paramInt2, String paramString)
/*      */       throws URISyntaxException
/*      */     {
/* 3299 */       int i = scanIPv4Address(paramInt1, paramInt2, true);
/* 3300 */       if (i <= paramInt1)
/* 3301 */         failExpecting(paramString, paramInt1);
/* 3302 */       return i;
/*      */     }
/*      */ 
/*      */     private int parseIPv4Address(int paramInt1, int paramInt2)
/*      */     {
/*      */       int i;
/*      */       try
/*      */       {
/* 3313 */         i = scanIPv4Address(paramInt1, paramInt2, false);
/*      */       } catch (URISyntaxException localURISyntaxException) {
/* 3315 */         return -1;
/*      */       } catch (NumberFormatException localNumberFormatException) {
/* 3317 */         return -1;
/*      */       }
/*      */ 
/* 3320 */       if ((i > paramInt1) && (i < paramInt2))
/*      */       {
/* 3324 */         if (charAt(i) != ':') {
/* 3325 */           i = -1;
/*      */         }
/*      */       }
/*      */ 
/* 3329 */       if (i > paramInt1) {
/* 3330 */         URI.this.host = substring(paramInt1, i);
/*      */       }
/* 3332 */       return i;
/*      */     }
/*      */ 
/*      */     private int parseHostname(int paramInt1, int paramInt2)
/*      */       throws URISyntaxException
/*      */     {
/* 3342 */       int i = paramInt1;
/*      */ 
/* 3344 */       int k = -1;
/*      */       do
/*      */       {
/* 3348 */         int j = scan(i, paramInt2, URI.L_ALPHANUM, URI.H_ALPHANUM);
/* 3349 */         if (j <= i)
/*      */           break;
/* 3351 */         k = i;
/* 3352 */         if (j > i) {
/* 3353 */           i = j;
/* 3354 */           j = scan(i, paramInt2, URI.L_ALPHANUM | URI.L_DASH, URI.H_ALPHANUM | URI.H_DASH);
/* 3355 */           if (j > i) {
/* 3356 */             if (charAt(j - 1) == '-')
/* 3357 */               fail("Illegal character in hostname", j - 1);
/* 3358 */             i = j;
/*      */           }
/*      */         }
/* 3361 */         j = scan(i, paramInt2, '.');
/* 3362 */         if (j <= i)
/*      */           break;
/* 3364 */         i = j;
/* 3365 */       }while (i < paramInt2);
/*      */ 
/* 3367 */       if ((i < paramInt2) && (!at(i, paramInt2, ':'))) {
/* 3368 */         fail("Illegal character in hostname", i);
/*      */       }
/* 3370 */       if (k < 0) {
/* 3371 */         failExpecting("hostname", paramInt1);
/*      */       }
/*      */ 
/* 3375 */       if ((k > paramInt1) && (!URI.match(charAt(k), 0L, URI.H_ALPHA))) {
/* 3376 */         fail("Illegal character in hostname", k);
/*      */       }
/*      */ 
/* 3379 */       URI.this.host = substring(paramInt1, i);
/* 3380 */       return i;
/*      */     }
/*      */ 
/*      */     private int parseIPv6Reference(int paramInt1, int paramInt2)
/*      */       throws URISyntaxException
/*      */     {
/* 3430 */       int i = paramInt1;
/*      */ 
/* 3432 */       int k = 0;
/*      */ 
/* 3434 */       int j = scanHexSeq(i, paramInt2);
/*      */ 
/* 3436 */       if (j > i) {
/* 3437 */         i = j;
/* 3438 */         if (at(i, paramInt2, "::")) {
/* 3439 */           k = 1;
/* 3440 */           i = scanHexPost(i + 2, paramInt2);
/* 3441 */         } else if (at(i, paramInt2, ':')) {
/* 3442 */           i = takeIPv4Address(i + 1, paramInt2, "IPv4 address");
/* 3443 */           this.ipv6byteCount += 4;
/*      */         }
/* 3445 */       } else if (at(i, paramInt2, "::")) {
/* 3446 */         k = 1;
/* 3447 */         i = scanHexPost(i + 2, paramInt2);
/*      */       }
/* 3449 */       if (i < paramInt2)
/* 3450 */         fail("Malformed IPv6 address", paramInt1);
/* 3451 */       if (this.ipv6byteCount > 16)
/* 3452 */         fail("IPv6 address too long", paramInt1);
/* 3453 */       if ((k == 0) && (this.ipv6byteCount < 16))
/* 3454 */         fail("IPv6 address too short", paramInt1);
/* 3455 */       if ((k != 0) && (this.ipv6byteCount == 16)) {
/* 3456 */         fail("Malformed IPv6 address", paramInt1);
/*      */       }
/* 3458 */       return i;
/*      */     }
/*      */ 
/*      */     private int scanHexPost(int paramInt1, int paramInt2)
/*      */       throws URISyntaxException
/*      */     {
/* 3464 */       int i = paramInt1;
/*      */ 
/* 3467 */       if (i == paramInt2) {
/* 3468 */         return i;
/*      */       }
/* 3470 */       int j = scanHexSeq(i, paramInt2);
/* 3471 */       if (j > i) {
/* 3472 */         i = j;
/* 3473 */         if (at(i, paramInt2, ':')) {
/* 3474 */           i++;
/* 3475 */           i = takeIPv4Address(i, paramInt2, "hex digits or IPv4 address");
/* 3476 */           this.ipv6byteCount += 4;
/*      */         }
/*      */       } else {
/* 3479 */         i = takeIPv4Address(i, paramInt2, "hex digits or IPv4 address");
/* 3480 */         this.ipv6byteCount += 4;
/*      */       }
/* 3482 */       return i;
/*      */     }
/*      */ 
/*      */     private int scanHexSeq(int paramInt1, int paramInt2)
/*      */       throws URISyntaxException
/*      */     {
/* 3490 */       int i = paramInt1;
/*      */ 
/* 3493 */       int j = scan(i, paramInt2, URI.L_HEX, URI.H_HEX);
/* 3494 */       if (j <= i)
/* 3495 */         return -1;
/* 3496 */       if (at(j, paramInt2, '.'))
/* 3497 */         return -1;
/* 3498 */       if (j > i + 4)
/* 3499 */         fail("IPv6 hexadecimal digit sequence too long", i);
/* 3500 */       this.ipv6byteCount += 2;
/* 3501 */       i = j;
/* 3502 */       while ((i < paramInt2) && 
/* 3503 */         (at(i, paramInt2, ':')))
/*      */       {
/* 3505 */         if (at(i + 1, paramInt2, ':'))
/*      */           break;
/* 3507 */         i++;
/* 3508 */         j = scan(i, paramInt2, URI.L_HEX, URI.H_HEX);
/* 3509 */         if (j <= i)
/* 3510 */           failExpecting("digits for an IPv6 address", i);
/* 3511 */         if (at(j, paramInt2, '.')) {
/* 3512 */           i--;
/* 3513 */           break;
/*      */         }
/* 3515 */         if (j > i + 4)
/* 3516 */           fail("IPv6 hexadecimal digit sequence too long", i);
/* 3517 */         this.ipv6byteCount += 2;
/* 3518 */         i = j;
/*      */       }
/*      */ 
/* 3521 */       return i;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.URI
 * JD-Core Version:    0.6.2
 */