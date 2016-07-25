/*     */ package javax.management.remote;
/*     */ 
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.BitSet;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class JMXServiceURL
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8173364409860779292L;
/*     */   private static final String INVALID_INSTANCE_MSG = "Trying to deserialize an invalid instance of JMXServiceURL";
/* 522 */   private static final Exception randomException = new Exception();
/*     */ 
/* 676 */   private static final BitSet alphaBitSet = new BitSet(128);
/* 677 */   private static final BitSet numericBitSet = new BitSet(128);
/* 678 */   private static final BitSet alphaNumericBitSet = new BitSet(128);
/* 679 */   private static final BitSet protocolBitSet = new BitSet(128);
/* 680 */   private static final BitSet hostNameBitSet = new BitSet(128);
/*     */   private String protocol;
/*     */   private String host;
/*     */   private int port;
/*     */   private String urlPath;
/*     */   private transient String toString;
/* 731 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "JMXServiceURL");
/*     */ 
/*     */   public JMXServiceURL(String paramString)
/*     */     throws MalformedURLException
/*     */   {
/* 142 */     int i = paramString.length();
/*     */ 
/* 146 */     for (int j = 0; j < i; j++) {
/* 147 */       k = paramString.charAt(j);
/* 148 */       if ((k < 32) || (k >= 127)) {
/* 149 */         throw new MalformedURLException("Service URL contains non-ASCII character 0x" + Integer.toHexString(k));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 157 */     int k = "service:jmx:".length();
/* 158 */     if (!paramString.regionMatches(true, 0, "service:jmx:", 0, k))
/*     */     {
/* 163 */       throw new MalformedURLException("Service URL must start with service:jmx:");
/*     */     }
/*     */ 
/* 168 */     int m = k;
/* 169 */     int n = indexOf(paramString, ':', m);
/* 170 */     this.protocol = paramString.substring(m, n).toLowerCase();
/*     */ 
/* 173 */     if (!paramString.regionMatches(n, "://", 0, 3)) {
/* 174 */       throw new MalformedURLException("Missing \"://\" after protocol name");
/*     */     }
/*     */ 
/* 179 */     int i1 = n + 3;
/*     */     int i2;
/* 181 */     if ((i1 < i) && (paramString.charAt(i1) == '['))
/*     */     {
/* 183 */       i2 = paramString.indexOf(']', i1) + 1;
/* 184 */       if (i2 == 0)
/* 185 */         throw new MalformedURLException("Bad host name: [ without ]");
/* 186 */       this.host = paramString.substring(i1 + 1, i2 - 1);
/* 187 */       if (!isNumericIPv6Address(this.host))
/* 188 */         throw new MalformedURLException("Address inside [...] must be numeric IPv6 address");
/*     */     }
/*     */     else
/*     */     {
/* 192 */       i2 = indexOfFirstNotInSet(paramString, hostNameBitSet, i1);
/*     */ 
/* 194 */       this.host = paramString.substring(i1, i2);
/*     */     }
/*     */     int i3;
/* 199 */     if ((i2 < i) && (paramString.charAt(i2) == ':')) {
/* 200 */       if (this.host.length() == 0) {
/* 201 */         throw new MalformedURLException("Cannot give port number without host name");
/*     */       }
/*     */ 
/* 204 */       i4 = i2 + 1;
/* 205 */       i3 = indexOfFirstNotInSet(paramString, numericBitSet, i4);
/*     */ 
/* 207 */       String str = paramString.substring(i4, i3);
/*     */       try {
/* 209 */         this.port = Integer.parseInt(str);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/* 211 */         throw new MalformedURLException("Bad port number: \"" + str + "\": " + localNumberFormatException);
/*     */       }
/*     */     }
/*     */     else {
/* 215 */       i3 = i2;
/* 216 */       this.port = 0;
/*     */     }
/*     */ 
/* 220 */     int i4 = i3;
/* 221 */     if (i4 < i)
/* 222 */       this.urlPath = paramString.substring(i4);
/*     */     else {
/* 224 */       this.urlPath = "";
/*     */     }
/* 226 */     validate();
/*     */   }
/*     */ 
/*     */   public JMXServiceURL(String paramString1, String paramString2, int paramInt)
/*     */     throws MalformedURLException
/*     */   {
/* 253 */     this(paramString1, paramString2, paramInt, null);
/*     */   }
/*     */ 
/*     */   public JMXServiceURL(String paramString1, String paramString2, int paramInt, String paramString3)
/*     */     throws MalformedURLException
/*     */   {
/* 281 */     if (paramString1 == null) {
/* 282 */       paramString1 = "jmxmp";
/*     */     }
/* 284 */     if (paramString2 == null) {
/*     */       InetAddress localInetAddress;
/*     */       try {
/* 287 */         localInetAddress = InetAddress.getLocalHost();
/*     */       } catch (UnknownHostException localUnknownHostException) {
/* 289 */         throw new MalformedURLException("Local host name unknown: " + localUnknownHostException);
/*     */       }
/*     */ 
/* 293 */       paramString2 = localInetAddress.getHostName();
/*     */       try
/*     */       {
/* 303 */         validateHost(paramString2, paramInt);
/*     */       } catch (MalformedURLException localMalformedURLException) {
/* 305 */         if (logger.fineOn()) {
/* 306 */           logger.fine("JMXServiceURL", "Replacing illegal local host name " + paramString2 + " with numeric IP address " + "(see RFC 1034)", localMalformedURLException);
/*     */         }
/*     */ 
/* 311 */         paramString2 = localInetAddress.getHostAddress();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 317 */     if (paramString2.startsWith("[")) {
/* 318 */       if (!paramString2.endsWith("]")) {
/* 319 */         throw new MalformedURLException("Host starts with [ but does not end with ]");
/*     */       }
/*     */ 
/* 322 */       paramString2 = paramString2.substring(1, paramString2.length() - 1);
/* 323 */       if (!isNumericIPv6Address(paramString2)) {
/* 324 */         throw new MalformedURLException("Address inside [...] must be numeric IPv6 address");
/*     */       }
/*     */ 
/* 327 */       if (paramString2.startsWith("[")) {
/* 328 */         throw new MalformedURLException("More than one [[...]]");
/*     */       }
/*     */     }
/* 331 */     this.protocol = paramString1.toLowerCase();
/* 332 */     this.host = paramString2;
/* 333 */     this.port = paramInt;
/*     */ 
/* 335 */     if (paramString3 == null)
/* 336 */       paramString3 = "";
/* 337 */     this.urlPath = paramString3;
/*     */ 
/* 339 */     validate();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 345 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 346 */     String str1 = (String)localGetField.get("host", null);
/* 347 */     int i = localGetField.get("port", -1);
/* 348 */     String str2 = (String)localGetField.get("protocol", null);
/* 349 */     String str3 = (String)localGetField.get("urlPath", null);
/*     */ 
/* 351 */     if ((str2 == null) || (str3 == null) || (str1 == null)) {
/* 352 */       StringBuilder localStringBuilder = new StringBuilder("Trying to deserialize an invalid instance of JMXServiceURL").append('[');
/* 353 */       int j = 1;
/* 354 */       if (str2 == null) {
/* 355 */         localStringBuilder.append("protocol=null");
/* 356 */         j = 0;
/*     */       }
/* 358 */       if (str1 == null) {
/* 359 */         localStringBuilder.append(j != 0 ? "" : ",").append("host=null");
/* 360 */         j = 0;
/*     */       }
/* 362 */       if (str3 == null) {
/* 363 */         localStringBuilder.append(j != 0 ? "" : ",").append("urlPath=null");
/*     */       }
/* 365 */       localStringBuilder.append(']');
/* 366 */       throw new InvalidObjectException(localStringBuilder.toString());
/*     */     }
/*     */ 
/* 369 */     if ((str1.contains("[")) || (str1.contains("]"))) {
/* 370 */       throw new InvalidObjectException("Invalid host name: " + str1);
/*     */     }
/*     */     try
/*     */     {
/* 374 */       validate(str2, str1, i, str3);
/* 375 */       this.protocol = str2;
/* 376 */       this.host = str1;
/* 377 */       this.port = i;
/* 378 */       this.urlPath = str3;
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 380 */       throw new InvalidObjectException("Trying to deserialize an invalid instance of JMXServiceURL: " + localMalformedURLException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void validate(String paramString1, String paramString2, int paramInt, String paramString3)
/*     */     throws MalformedURLException
/*     */   {
/* 389 */     int i = indexOfFirstNotInSet(paramString1, protocolBitSet, 0);
/* 390 */     if ((i == 0) || (i < paramString1.length()) || (!alphaBitSet.get(paramString1.charAt(0))))
/*     */     {
/* 392 */       throw new MalformedURLException("Missing or invalid protocol name: \"" + paramString1 + "\"");
/*     */     }
/*     */ 
/* 397 */     validateHost(paramString2, paramInt);
/*     */ 
/* 400 */     if (paramInt < 0) {
/* 401 */       throw new MalformedURLException("Bad port: " + paramInt);
/*     */     }
/*     */ 
/* 404 */     if ((paramString3.length() > 0) && 
/* 405 */       (!paramString3.startsWith("/")) && (!paramString3.startsWith(";")))
/* 406 */       throw new MalformedURLException("Bad URL path: " + paramString3);
/*     */   }
/*     */ 
/*     */   private void validate() throws MalformedURLException
/*     */   {
/* 411 */     validate(this.protocol, this.host, this.port, this.urlPath);
/*     */   }
/*     */ 
/*     */   private static void validateHost(String paramString, int paramInt)
/*     */     throws MalformedURLException
/*     */   {
/* 417 */     if (paramString.length() == 0) {
/* 418 */       if (paramInt != 0) {
/* 419 */         throw new MalformedURLException("Cannot give port number without host name");
/*     */       }
/*     */ 
/* 422 */       return;
/*     */     }
/*     */ 
/* 425 */     if (isNumericIPv6Address(paramString))
/*     */     {
/*     */       try
/*     */       {
/* 433 */         InetAddress.getByName(paramString);
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/* 439 */         MalformedURLException localMalformedURLException = new MalformedURLException("Bad IPv6 address: " + paramString);
/*     */ 
/* 441 */         EnvHelp.initCause(localMalformedURLException, localException1);
/* 442 */         throw localMalformedURLException;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 465 */       int i = paramString.length();
/* 466 */       int j = 46;
/* 467 */       int k = 0;
/* 468 */       int m = 0;
/*     */       int i1;
/* 471 */       for (int n = 0; n < i; n++) {
/* 472 */         i1 = paramString.charAt(n);
/* 473 */         boolean bool = alphaNumericBitSet.get(i1);
/* 474 */         if (j == 46)
/* 475 */           m = i1;
/* 476 */         if (bool) {
/* 477 */           j = 97;
/* 478 */         } else if (i1 == 45) {
/* 479 */           if (j == 46)
/*     */             break;
/* 481 */           j = 45;
/* 482 */         } else if (i1 == 46) {
/* 483 */           k = 1;
/* 484 */           if (j != 97)
/*     */             break;
/* 486 */           j = 46;
/*     */         } else {
/* 488 */           j = 46;
/* 489 */           break;
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 494 */         if (j != 97)
/* 495 */           throw randomException;
/* 496 */         if ((k != 0) && (!alphaBitSet.get(m)))
/*     */         {
/* 504 */           StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ".", true);
/* 505 */           for (i1 = 0; i1 < 4; i1++) {
/* 506 */             String str = localStringTokenizer.nextToken();
/* 507 */             int i2 = Integer.parseInt(str);
/* 508 */             if ((i2 < 0) || (i2 > 255))
/* 509 */               throw randomException;
/* 510 */             if ((i1 < 3) && (!localStringTokenizer.nextToken().equals(".")))
/* 511 */               throw randomException;
/*     */           }
/* 513 */           if (localStringTokenizer.hasMoreTokens())
/* 514 */             throw randomException;
/*     */         }
/*     */       } catch (Exception localException2) {
/* 517 */         throw new MalformedURLException("Bad host: \"" + paramString + "\"");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getProtocol()
/*     */   {
/* 531 */     return this.protocol;
/*     */   }
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 551 */     return this.host;
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 561 */     return this.port;
/*     */   }
/*     */ 
/*     */   public String getURLPath()
/*     */   {
/* 573 */     return this.urlPath;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 596 */     if (this.toString != null)
/* 597 */       return this.toString;
/* 598 */     StringBuilder localStringBuilder = new StringBuilder("service:jmx:");
/* 599 */     localStringBuilder.append(getProtocol()).append("://");
/* 600 */     String str = getHost();
/* 601 */     if (isNumericIPv6Address(str))
/* 602 */       localStringBuilder.append('[').append(str).append(']');
/*     */     else
/* 604 */       localStringBuilder.append(str);
/* 605 */     int i = getPort();
/* 606 */     if (i != 0)
/* 607 */       localStringBuilder.append(':').append(i);
/* 608 */     localStringBuilder.append(getURLPath());
/* 609 */     this.toString = localStringBuilder.toString();
/* 610 */     return this.toString;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 628 */     if (!(paramObject instanceof JMXServiceURL))
/* 629 */       return false;
/* 630 */     JMXServiceURL localJMXServiceURL = (JMXServiceURL)paramObject;
/* 631 */     return (localJMXServiceURL.getProtocol().equalsIgnoreCase(getProtocol())) && (localJMXServiceURL.getHost().equalsIgnoreCase(getHost())) && (localJMXServiceURL.getPort() == getPort()) && (localJMXServiceURL.getURLPath().equals(getURLPath()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 639 */     return toString().hashCode();
/*     */   }
/*     */ 
/*     */   private static boolean isNumericIPv6Address(String paramString)
/*     */   {
/* 647 */     return paramString.indexOf(':') >= 0;
/*     */   }
/*     */ 
/*     */   private static int indexOf(String paramString, char paramChar, int paramInt)
/*     */   {
/* 652 */     int i = paramString.indexOf(paramChar, paramInt);
/* 653 */     if (i < 0) {
/* 654 */       return paramString.length();
/*     */     }
/* 656 */     return i;
/*     */   }
/*     */ 
/*     */   private static int indexOfFirstNotInSet(String paramString, BitSet paramBitSet, int paramInt)
/*     */   {
/* 661 */     int i = paramString.length();
/* 662 */     int j = paramInt;
/*     */ 
/* 664 */     while (j < i)
/*     */     {
/* 666 */       int k = paramString.charAt(j);
/* 667 */       if (k >= 128)
/*     */         break;
/* 669 */       if (!paramBitSet.get(k))
/*     */         break;
/* 671 */       j++;
/*     */     }
/* 673 */     return j;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 686 */     for (int i = 48; i <= 57; i = (char)(i + 1)) {
/* 687 */       numericBitSet.set(i);
/*     */     }
/* 689 */     for (i = 65; i <= 90; i = (char)(i + 1))
/* 690 */       alphaBitSet.set(i);
/* 691 */     for (i = 97; i <= 122; i = (char)(i + 1)) {
/* 692 */       alphaBitSet.set(i);
/*     */     }
/* 694 */     alphaNumericBitSet.or(alphaBitSet);
/* 695 */     alphaNumericBitSet.or(numericBitSet);
/*     */ 
/* 697 */     protocolBitSet.or(alphaNumericBitSet);
/* 698 */     protocolBitSet.set(43);
/* 699 */     protocolBitSet.set(45);
/*     */ 
/* 701 */     hostNameBitSet.or(alphaNumericBitSet);
/* 702 */     hostNameBitSet.set(45);
/* 703 */     hostNameBitSet.set(46);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.JMXServiceURL
 * JD-Core Version:    0.6.2
 */