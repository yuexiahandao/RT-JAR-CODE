/*      */ package java.net;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.StringTokenizer;
/*      */ import sun.net.PortConfig;
/*      */ import sun.net.RegisteredDomain;
/*      */ import sun.net.util.IPAddressUtil;
/*      */ import sun.net.www.URLConnection;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ import sun.security.util.Debug;
/*      */ 
/*      */ public final class SocketPermission extends Permission
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -7204263841984476862L;
/*      */   private static final int CONNECT = 1;
/*      */   private static final int LISTEN = 2;
/*      */   private static final int ACCEPT = 4;
/*      */   private static final int RESOLVE = 8;
/*      */   private static final int NONE = 0;
/*      */   private static final int ALL = 15;
/*      */   private static final int PORT_MIN = 0;
/*      */   private static final int PORT_MAX = 65535;
/*      */   private static final int PRIV_PORT_MAX = 1023;
/*      */   private static final int DEF_EPH_LOW = 49152;
/*      */   private transient int mask;
/*      */   private String actions;
/*      */   private transient String hostname;
/*      */   private transient String cname;
/*      */   private transient InetAddress[] addresses;
/*      */   private transient boolean wildcard;
/*      */   private transient boolean init_with_ip;
/*      */   private transient boolean invalid;
/*      */   private transient int[] portrange;
/*  221 */   private transient boolean defaultDeny = false;
/*      */   private transient boolean untrusted;
/*      */   private transient boolean trusted;
/*  245 */   private static boolean trustNameService = localBoolean.booleanValue();
/*      */ 
/*  231 */   private static Debug debug = null;
/*  232 */   private static boolean debugInit = false;
/*      */ 
/*  235 */   private static final int ephemeralLow = initEphemeralPorts("low", 49152);
/*      */ 
/*  238 */   private static final int ephemeralHigh = initEphemeralPorts("high", 65535);
/*      */   private transient String cdomain;
/*      */   private transient String hdomain;
/*      */ 
/*      */   private static synchronized Debug getDebug()
/*      */   {
/*  250 */     if (!debugInit) {
/*  251 */       debug = Debug.getInstance("access");
/*  252 */       debugInit = true;
/*      */     }
/*  254 */     return debug;
/*      */   }
/*      */ 
/*      */   public SocketPermission(String paramString1, String paramString2)
/*      */   {
/*  288 */     super(getHost(paramString1));
/*      */ 
/*  290 */     init(getName(), getMask(paramString2));
/*      */   }
/*      */ 
/*      */   SocketPermission(String paramString, int paramInt)
/*      */   {
/*  295 */     super(getHost(paramString));
/*      */ 
/*  297 */     init(getName(), paramInt);
/*      */   }
/*      */ 
/*      */   private void setDeny() {
/*  301 */     this.defaultDeny = true;
/*      */   }
/*      */ 
/*      */   private static String getHost(String paramString)
/*      */   {
/*  306 */     if (paramString.equals(""))
/*  307 */       return "localhost";
/*      */     int i;
/*  314 */     if ((paramString.charAt(0) != '[') && 
/*  315 */       ((i = paramString.indexOf(':')) != paramString.lastIndexOf(':')))
/*      */     {
/*  320 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ":");
/*  321 */       int j = localStringTokenizer.countTokens();
/*  322 */       if (j == 9)
/*      */       {
/*  324 */         i = paramString.lastIndexOf(':');
/*  325 */         paramString = "[" + paramString.substring(0, i) + "]" + paramString.substring(i);
/*      */       }
/*  327 */       else if ((j == 8) && (paramString.indexOf("::") == -1))
/*      */       {
/*  329 */         paramString = "[" + paramString + "]";
/*      */       }
/*      */       else {
/*  332 */         throw new IllegalArgumentException("Ambiguous hostport part");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  337 */     return paramString;
/*      */   }
/*      */ 
/*      */   private int[] parsePort(String paramString)
/*      */     throws Exception
/*      */   {
/*  345 */     if ((paramString == null) || (paramString.equals("")) || (paramString.equals("*"))) {
/*  346 */       return new int[] { 0, 65535 };
/*      */     }
/*      */ 
/*  349 */     int i = paramString.indexOf('-');
/*      */ 
/*  351 */     if (i == -1) {
/*  352 */       int j = Integer.parseInt(paramString);
/*  353 */       return new int[] { j, j };
/*      */     }
/*  355 */     String str1 = paramString.substring(0, i);
/*  356 */     String str2 = paramString.substring(i + 1);
/*      */     int k;
/*  359 */     if (str1.equals(""))
/*  360 */       k = 0;
/*      */     else
/*  362 */       k = Integer.parseInt(str1);
/*      */     int m;
/*  365 */     if (str2.equals(""))
/*  366 */       m = 65535;
/*      */     else {
/*  368 */       m = Integer.parseInt(str2);
/*      */     }
/*  370 */     if ((k < 0) || (m < 0) || (m < k)) {
/*  371 */       throw new IllegalArgumentException("invalid port range");
/*      */     }
/*  373 */     return new int[] { k, m };
/*      */   }
/*      */ 
/*      */   private boolean includesEphemerals()
/*      */   {
/*  382 */     return this.portrange[0] == 0;
/*      */   }
/*      */ 
/*      */   private void init(String paramString, int paramInt)
/*      */   {
/*  393 */     if ((paramInt & 0xF) != paramInt) {
/*  394 */       throw new IllegalArgumentException("invalid actions mask");
/*      */     }
/*      */ 
/*  397 */     this.mask = (paramInt | 0x8);
/*      */ 
/*  408 */     int i = 0;
/*  409 */     int j = 0; int k = 0;
/*  410 */     int m = -1;
/*  411 */     String str1 = paramString;
/*  412 */     if (paramString.charAt(0) == '[') {
/*  413 */       j = 1;
/*  414 */       i = paramString.indexOf(']');
/*  415 */       if (i != -1)
/*  416 */         paramString = paramString.substring(j, i);
/*      */       else {
/*  418 */         throw new IllegalArgumentException("invalid host/port: " + paramString);
/*      */       }
/*      */ 
/*  421 */       m = str1.indexOf(':', i + 1);
/*      */     } else {
/*  423 */       j = 0;
/*  424 */       m = paramString.indexOf(':', i);
/*  425 */       k = m;
/*  426 */       if (m != -1) {
/*  427 */         paramString = paramString.substring(j, k);
/*      */       }
/*      */     }
/*      */ 
/*  431 */     if (m != -1) {
/*  432 */       String str2 = str1.substring(m + 1);
/*      */       try {
/*  434 */         this.portrange = parsePort(str2);
/*      */       } catch (Exception localException) {
/*  436 */         throw new IllegalArgumentException("invalid port range: " + str2);
/*      */       }
/*      */     }
/*      */     else {
/*  440 */       this.portrange = new int[] { 0, 65535 };
/*      */     }
/*      */ 
/*  443 */     this.hostname = paramString;
/*      */ 
/*  446 */     if (paramString.lastIndexOf('*') > 0) {
/*  447 */       throw new IllegalArgumentException("invalid host wildcard specification");
/*      */     }
/*  449 */     if (paramString.startsWith("*")) {
/*  450 */       this.wildcard = true;
/*  451 */       if (paramString.equals("*"))
/*  452 */         this.cname = "";
/*  453 */       else if (paramString.startsWith("*."))
/*  454 */         this.cname = paramString.substring(1).toLowerCase();
/*      */       else {
/*  456 */         throw new IllegalArgumentException("invalid host wildcard specification");
/*      */       }
/*      */ 
/*  459 */       return;
/*      */     }
/*  461 */     if (paramString.length() > 0)
/*      */     {
/*  463 */       char c = paramString.charAt(0);
/*  464 */       if ((c == ':') || (Character.digit(c, 16) != -1)) {
/*  465 */         byte[] arrayOfByte = IPAddressUtil.textToNumericFormatV4(paramString);
/*  466 */         if (arrayOfByte == null) {
/*  467 */           arrayOfByte = IPAddressUtil.textToNumericFormatV6(paramString);
/*      */         }
/*  469 */         if (arrayOfByte != null)
/*      */           try {
/*  471 */             this.addresses = new InetAddress[] { InetAddress.getByAddress(arrayOfByte) };
/*      */ 
/*  474 */             this.init_with_ip = true;
/*      */           }
/*      */           catch (UnknownHostException localUnknownHostException) {
/*  477 */             this.invalid = true;
/*      */           }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int getMask(String paramString)
/*      */   {
/*  493 */     if (paramString == null) {
/*  494 */       throw new NullPointerException("action can't be null");
/*      */     }
/*      */ 
/*  497 */     if (paramString.equals("")) {
/*  498 */       throw new IllegalArgumentException("action can't be empty");
/*      */     }
/*      */ 
/*  501 */     int i = 0;
/*      */ 
/*  504 */     if (paramString == "resolve")
/*  505 */       return 8;
/*  506 */     if (paramString == "connect")
/*  507 */       return 1;
/*  508 */     if (paramString == "listen")
/*  509 */       return 2;
/*  510 */     if (paramString == "accept")
/*  511 */       return 4;
/*  512 */     if (paramString == "connect,accept") {
/*  513 */       return 5;
/*      */     }
/*      */ 
/*  516 */     char[] arrayOfChar = paramString.toCharArray();
/*      */ 
/*  518 */     int j = arrayOfChar.length - 1;
/*  519 */     if (j < 0) {
/*  520 */       return i;
/*      */     }
/*  522 */     while (j != -1)
/*      */     {
/*      */       int k;
/*  526 */       while ((j != -1) && (((k = arrayOfChar[j]) == ' ') || (k == 13) || (k == 10) || (k == 12) || (k == 9)))
/*      */       {
/*  531 */         j--;
/*      */       }
/*      */       int m;
/*  536 */       if ((j >= 6) && ((arrayOfChar[(j - 6)] == 'c') || (arrayOfChar[(j - 6)] == 'C')) && ((arrayOfChar[(j - 5)] == 'o') || (arrayOfChar[(j - 5)] == 'O')) && ((arrayOfChar[(j - 4)] == 'n') || (arrayOfChar[(j - 4)] == 'N')) && ((arrayOfChar[(j - 3)] == 'n') || (arrayOfChar[(j - 3)] == 'N')) && ((arrayOfChar[(j - 2)] == 'e') || (arrayOfChar[(j - 2)] == 'E')) && ((arrayOfChar[(j - 1)] == 'c') || (arrayOfChar[(j - 1)] == 'C')) && ((arrayOfChar[j] == 't') || (arrayOfChar[j] == 'T')))
/*      */       {
/*  544 */         m = 7;
/*  545 */         i |= 1;
/*      */       }
/*  547 */       else if ((j >= 6) && ((arrayOfChar[(j - 6)] == 'r') || (arrayOfChar[(j - 6)] == 'R')) && ((arrayOfChar[(j - 5)] == 'e') || (arrayOfChar[(j - 5)] == 'E')) && ((arrayOfChar[(j - 4)] == 's') || (arrayOfChar[(j - 4)] == 'S')) && ((arrayOfChar[(j - 3)] == 'o') || (arrayOfChar[(j - 3)] == 'O')) && ((arrayOfChar[(j - 2)] == 'l') || (arrayOfChar[(j - 2)] == 'L')) && ((arrayOfChar[(j - 1)] == 'v') || (arrayOfChar[(j - 1)] == 'V')) && ((arrayOfChar[j] == 'e') || (arrayOfChar[j] == 'E')))
/*      */       {
/*  555 */         m = 7;
/*  556 */         i |= 8;
/*      */       }
/*  558 */       else if ((j >= 5) && ((arrayOfChar[(j - 5)] == 'l') || (arrayOfChar[(j - 5)] == 'L')) && ((arrayOfChar[(j - 4)] == 'i') || (arrayOfChar[(j - 4)] == 'I')) && ((arrayOfChar[(j - 3)] == 's') || (arrayOfChar[(j - 3)] == 'S')) && ((arrayOfChar[(j - 2)] == 't') || (arrayOfChar[(j - 2)] == 'T')) && ((arrayOfChar[(j - 1)] == 'e') || (arrayOfChar[(j - 1)] == 'E')) && ((arrayOfChar[j] == 'n') || (arrayOfChar[j] == 'N')))
/*      */       {
/*  565 */         m = 6;
/*  566 */         i |= 2;
/*      */       }
/*  568 */       else if ((j >= 5) && ((arrayOfChar[(j - 5)] == 'a') || (arrayOfChar[(j - 5)] == 'A')) && ((arrayOfChar[(j - 4)] == 'c') || (arrayOfChar[(j - 4)] == 'C')) && ((arrayOfChar[(j - 3)] == 'c') || (arrayOfChar[(j - 3)] == 'C')) && ((arrayOfChar[(j - 2)] == 'e') || (arrayOfChar[(j - 2)] == 'E')) && ((arrayOfChar[(j - 1)] == 'p') || (arrayOfChar[(j - 1)] == 'P')) && ((arrayOfChar[j] == 't') || (arrayOfChar[j] == 'T')))
/*      */       {
/*  575 */         m = 6;
/*  576 */         i |= 4;
/*      */       }
/*      */       else
/*      */       {
/*  580 */         throw new IllegalArgumentException("invalid permission: " + paramString);
/*      */       }
/*      */ 
/*  586 */       int n = 0;
/*  587 */       while ((j >= m) && (n == 0)) {
/*  588 */         switch (arrayOfChar[(j - m)]) {
/*      */         case ',':
/*  590 */           n = 1;
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*  594 */           break;
/*      */         default:
/*  596 */           throw new IllegalArgumentException("invalid permission: " + paramString);
/*      */         }
/*      */ 
/*  599 */         j--;
/*      */       }
/*      */ 
/*  603 */       j -= m;
/*      */     }
/*      */ 
/*  606 */     return i;
/*      */   }
/*      */ 
/*      */   private boolean isUntrusted()
/*      */     throws UnknownHostException
/*      */   {
/*  612 */     if (this.trusted) return false;
/*  613 */     if ((this.invalid) || (this.untrusted)) return true; try
/*      */     {
/*  615 */       if ((!trustNameService) && ((this.defaultDeny) || (URLConnection.isProxiedHost(this.hostname))))
/*      */       {
/*  617 */         if (this.cname == null) {
/*  618 */           getCanonName();
/*      */         }
/*  620 */         if (!match(this.cname, this.hostname))
/*      */         {
/*  622 */           if (!authorized(this.hostname, this.addresses[0].getAddress())) {
/*  623 */             this.untrusted = true;
/*  624 */             Debug localDebug = getDebug();
/*  625 */             if ((localDebug != null) && (Debug.isOn("failure"))) {
/*  626 */               localDebug.println("socket access restriction: proxied host (" + this.addresses[0] + ")" + " does not match " + this.cname + " from reverse lookup");
/*      */             }
/*  628 */             return true;
/*      */           }
/*      */         }
/*  631 */         this.trusted = true;
/*      */       }
/*      */     } catch (UnknownHostException localUnknownHostException) {
/*  634 */       this.invalid = true;
/*  635 */       throw localUnknownHostException;
/*      */     }
/*  637 */     return false;
/*      */   }
/*      */ 
/*      */   void getCanonName()
/*      */     throws UnknownHostException
/*      */   {
/*  647 */     if ((this.cname != null) || (this.invalid) || (this.untrusted)) return;
/*      */ 
/*      */     try
/*      */     {
/*  655 */       if (this.addresses == null) {
/*  656 */         getIP();
/*      */       }
/*      */ 
/*  661 */       if (this.init_with_ip)
/*  662 */         this.cname = this.addresses[0].getHostName(false).toLowerCase();
/*      */       else
/*  664 */         this.cname = InetAddress.getByName(this.addresses[0].getHostAddress()).getHostName(false).toLowerCase();
/*      */     }
/*      */     catch (UnknownHostException localUnknownHostException)
/*      */     {
/*  668 */       this.invalid = true;
/*  669 */       throw localUnknownHostException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean match(String paramString1, String paramString2)
/*      */   {
/*  676 */     String str1 = paramString1.toLowerCase();
/*  677 */     String str2 = paramString2.toLowerCase();
/*  678 */     if ((str1.startsWith(str2)) && ((str1.length() == str2.length()) || (str1.charAt(str2.length()) == '.')))
/*      */     {
/*  680 */       return true;
/*  681 */     }if (this.cdomain == null) {
/*  682 */       this.cdomain = RegisteredDomain.getRegisteredDomain(str1);
/*      */     }
/*  684 */     if (this.hdomain == null) {
/*  685 */       this.hdomain = RegisteredDomain.getRegisteredDomain(str2);
/*      */     }
/*      */ 
/*  688 */     return (this.cdomain.length() != 0) && (this.hdomain.length() != 0) && (this.cdomain.equals(this.hdomain));
/*      */   }
/*      */ 
/*      */   private boolean authorized(String paramString, byte[] paramArrayOfByte)
/*      */   {
/*  693 */     if (paramArrayOfByte.length == 4)
/*  694 */       return authorizedIPv4(paramString, paramArrayOfByte);
/*  695 */     if (paramArrayOfByte.length == 16) {
/*  696 */       return authorizedIPv6(paramString, paramArrayOfByte);
/*      */     }
/*  698 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean authorizedIPv4(String paramString, byte[] paramArrayOfByte) {
/*  702 */     String str = "";
/*      */     try
/*      */     {
/*  706 */       str = "auth." + (paramArrayOfByte[3] & 0xFF) + "." + (paramArrayOfByte[2] & 0xFF) + "." + (paramArrayOfByte[1] & 0xFF) + "." + (paramArrayOfByte[0] & 0xFF) + ".in-addr.arpa";
/*      */ 
/*  712 */       str = this.hostname + '.' + str;
/*  713 */       InetAddress localInetAddress = InetAddress.getAllByName0(str, false)[0];
/*  714 */       if (localInetAddress.equals(InetAddress.getByAddress(paramArrayOfByte))) {
/*  715 */         return true;
/*      */       }
/*  717 */       Debug localDebug1 = getDebug();
/*  718 */       if ((localDebug1 != null) && (Debug.isOn("failure")))
/*  719 */         localDebug1.println("socket access restriction: IP address of " + localInetAddress + " != " + InetAddress.getByAddress(paramArrayOfByte));
/*      */     }
/*      */     catch (UnknownHostException localUnknownHostException) {
/*  722 */       Debug localDebug2 = getDebug();
/*  723 */       if ((localDebug2 != null) && (Debug.isOn("failure"))) {
/*  724 */         localDebug2.println("socket access restriction: forward lookup failed for " + str);
/*      */       }
/*      */     }
/*  727 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean authorizedIPv6(String paramString, byte[] paramArrayOfByte) {
/*  731 */     String str = "";
/*      */     try
/*      */     {
/*  735 */       StringBuffer localStringBuffer = new StringBuffer(39);
/*      */ 
/*  737 */       for (int i = 15; i >= 0; i--) {
/*  738 */         localStringBuffer.append(Integer.toHexString(paramArrayOfByte[i] & 0xF));
/*  739 */         localStringBuffer.append('.');
/*  740 */         localStringBuffer.append(Integer.toHexString(paramArrayOfByte[i] >> 4 & 0xF));
/*  741 */         localStringBuffer.append('.');
/*      */       }
/*  743 */       str = "auth." + localStringBuffer.toString() + "IP6.ARPA";
/*      */ 
/*  745 */       str = this.hostname + '.' + str;
/*  746 */       InetAddress localInetAddress = InetAddress.getAllByName0(str, false)[0];
/*  747 */       if (localInetAddress.equals(InetAddress.getByAddress(paramArrayOfByte)))
/*  748 */         return true;
/*  749 */       localDebug = getDebug();
/*  750 */       if ((localDebug != null) && (Debug.isOn("failure")))
/*  751 */         localDebug.println("socket access restriction: IP address of " + localInetAddress + " != " + InetAddress.getByAddress(paramArrayOfByte));
/*      */     }
/*      */     catch (UnknownHostException localUnknownHostException) {
/*  754 */       Debug localDebug = getDebug();
/*  755 */       if ((localDebug != null) && (Debug.isOn("failure"))) {
/*  756 */         localDebug.println("socket access restriction: forward lookup failed for " + str);
/*      */       }
/*      */     }
/*  759 */     return false;
/*      */   }
/*      */ 
/*      */   void getIP()
/*      */     throws UnknownHostException
/*      */   {
/*  770 */     if ((this.addresses != null) || (this.wildcard) || (this.invalid)) return;
/*      */     try
/*      */     {
/*      */       String str;
/*  775 */       if (getName().charAt(0) == '[')
/*      */       {
/*  777 */         str = getName().substring(1, getName().indexOf(']'));
/*      */       } else {
/*  779 */         int i = getName().indexOf(":");
/*  780 */         if (i == -1)
/*  781 */           str = getName();
/*      */         else {
/*  783 */           str = getName().substring(0, i);
/*      */         }
/*      */       }
/*      */ 
/*  787 */       this.addresses = new InetAddress[] { InetAddress.getAllByName0(str, false)[0] };
/*      */     }
/*      */     catch (UnknownHostException localUnknownHostException)
/*      */     {
/*  791 */       this.invalid = true;
/*  792 */       throw localUnknownHostException;
/*      */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*  794 */       this.invalid = true;
/*  795 */       throw new UnknownHostException(getName());
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean implies(Permission paramPermission)
/*      */   {
/*  837 */     if (!(paramPermission instanceof SocketPermission)) {
/*  838 */       return false;
/*      */     }
/*  840 */     if (paramPermission == this) {
/*  841 */       return true;
/*      */     }
/*  843 */     SocketPermission localSocketPermission = (SocketPermission)paramPermission;
/*      */ 
/*  845 */     return ((this.mask & localSocketPermission.mask) == localSocketPermission.mask) && (impliesIgnoreMask(localSocketPermission));
/*      */   }
/*      */ 
/*      */   boolean impliesIgnoreMask(SocketPermission paramSocketPermission)
/*      */   {
/*  876 */     if ((paramSocketPermission.mask & 0x8) != paramSocketPermission.mask)
/*      */     {
/*  879 */       if ((paramSocketPermission.portrange[0] < this.portrange[0]) || (paramSocketPermission.portrange[1] > this.portrange[1]))
/*      */       {
/*  883 */         if ((includesEphemerals()) || (paramSocketPermission.includesEphemerals())) {
/*  884 */           if (!inRange(this.portrange[0], this.portrange[1], paramSocketPermission.portrange[0], paramSocketPermission.portrange[1]))
/*      */           {
/*  887 */             return false;
/*      */           }
/*      */         }
/*  890 */         else return false;
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  896 */     if ((this.wildcard) && ("".equals(this.cname))) {
/*  897 */       return true;
/*      */     }
/*      */ 
/*  900 */     if ((this.invalid) || (paramSocketPermission.invalid))
/*  901 */       return compareHostnames(paramSocketPermission);
/*      */     try
/*      */     {
/*      */       int i;
/*  905 */       if (this.init_with_ip) {
/*  906 */         if (paramSocketPermission.wildcard) {
/*  907 */           return false;
/*      */         }
/*  909 */         if (paramSocketPermission.init_with_ip) {
/*  910 */           return this.addresses[0].equals(paramSocketPermission.addresses[0]);
/*      */         }
/*  912 */         if (paramSocketPermission.addresses == null) {
/*  913 */           paramSocketPermission.getIP();
/*      */         }
/*  915 */         for (i = 0; i < paramSocketPermission.addresses.length; i++) {
/*  916 */           if (this.addresses[0].equals(paramSocketPermission.addresses[i])) {
/*  917 */             return true;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  922 */         return false;
/*      */       }
/*      */ 
/*  926 */       if ((this.wildcard) || (paramSocketPermission.wildcard))
/*      */       {
/*  930 */         if ((this.wildcard) && (paramSocketPermission.wildcard)) {
/*  931 */           return paramSocketPermission.cname.endsWith(this.cname);
/*      */         }
/*      */ 
/*  934 */         if (paramSocketPermission.wildcard) {
/*  935 */           return false;
/*      */         }
/*      */ 
/*  939 */         if (paramSocketPermission.cname == null) {
/*  940 */           paramSocketPermission.getCanonName();
/*      */         }
/*  942 */         return paramSocketPermission.cname.endsWith(this.cname);
/*      */       }
/*      */ 
/*  946 */       if (this.addresses == null) {
/*  947 */         getIP();
/*      */       }
/*      */ 
/*  950 */       if (paramSocketPermission.addresses == null) {
/*  951 */         paramSocketPermission.getIP();
/*      */       }
/*      */ 
/*  954 */       if ((!paramSocketPermission.init_with_ip) || (!isUntrusted())) {
/*  955 */         for (int j = 0; j < this.addresses.length; j++) {
/*  956 */           for (i = 0; i < paramSocketPermission.addresses.length; i++) {
/*  957 */             if (this.addresses[j].equals(paramSocketPermission.addresses[i])) {
/*  958 */               return true;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  964 */         if (this.cname == null) {
/*  965 */           getCanonName();
/*      */         }
/*      */ 
/*  968 */         if (paramSocketPermission.cname == null) {
/*  969 */           paramSocketPermission.getCanonName();
/*      */         }
/*      */ 
/*  972 */         return this.cname.equalsIgnoreCase(paramSocketPermission.cname);
/*      */       }
/*      */     }
/*      */     catch (UnknownHostException localUnknownHostException) {
/*  976 */       return compareHostnames(paramSocketPermission);
/*      */     }
/*      */ 
/*  982 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean compareHostnames(SocketPermission paramSocketPermission)
/*      */   {
/*  988 */     String str1 = this.hostname;
/*  989 */     String str2 = paramSocketPermission.hostname;
/*      */ 
/*  991 */     if (str1 == null) {
/*  992 */       return false;
/*      */     }
/*  994 */     return str1.equalsIgnoreCase(str2);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1008 */     if (paramObject == this) {
/* 1009 */       return true;
/*      */     }
/* 1011 */     if (!(paramObject instanceof SocketPermission)) {
/* 1012 */       return false;
/*      */     }
/* 1014 */     SocketPermission localSocketPermission = (SocketPermission)paramObject;
/*      */ 
/* 1019 */     if (this.mask != localSocketPermission.mask) return false;
/*      */ 
/* 1021 */     if ((localSocketPermission.mask & 0x8) != localSocketPermission.mask)
/*      */     {
/* 1023 */       if ((this.portrange[0] != localSocketPermission.portrange[0]) || (this.portrange[1] != localSocketPermission.portrange[1]))
/*      */       {
/* 1025 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1036 */     if (getName().equalsIgnoreCase(localSocketPermission.getName())) {
/* 1037 */       return true;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1045 */       getCanonName();
/* 1046 */       localSocketPermission.getCanonName();
/*      */     } catch (UnknownHostException localUnknownHostException) {
/* 1048 */       return false;
/*      */     }
/*      */ 
/* 1051 */     if ((this.invalid) || (localSocketPermission.invalid)) {
/* 1052 */       return false;
/*      */     }
/* 1054 */     if (this.cname != null) {
/* 1055 */       return this.cname.equalsIgnoreCase(localSocketPermission.cname);
/*      */     }
/*      */ 
/* 1058 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1075 */     if ((this.init_with_ip) || (this.wildcard)) {
/* 1076 */       return getName().hashCode();
/*      */     }
/*      */     try
/*      */     {
/* 1080 */       getCanonName();
/*      */     }
/*      */     catch (UnknownHostException localUnknownHostException)
/*      */     {
/*      */     }
/* 1085 */     if ((this.invalid) || (this.cname == null)) {
/* 1086 */       return getName().hashCode();
/*      */     }
/* 1088 */     return this.cname.hashCode();
/*      */   }
/*      */ 
/*      */   int getMask()
/*      */   {
/* 1098 */     return this.mask;
/*      */   }
/*      */ 
/*      */   private static String getActions(int paramInt)
/*      */   {
/* 1112 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1113 */     int i = 0;
/*      */ 
/* 1115 */     if ((paramInt & 0x1) == 1) {
/* 1116 */       i = 1;
/* 1117 */       localStringBuilder.append("connect");
/*      */     }
/*      */ 
/* 1120 */     if ((paramInt & 0x2) == 2) {
/* 1121 */       if (i != 0) localStringBuilder.append(','); else
/* 1122 */         i = 1;
/* 1123 */       localStringBuilder.append("listen");
/*      */     }
/*      */ 
/* 1126 */     if ((paramInt & 0x4) == 4) {
/* 1127 */       if (i != 0) localStringBuilder.append(','); else
/* 1128 */         i = 1;
/* 1129 */       localStringBuilder.append("accept");
/*      */     }
/*      */ 
/* 1133 */     if ((paramInt & 0x8) == 8) {
/* 1134 */       if (i != 0) localStringBuilder.append(','); else
/* 1135 */         i = 1;
/* 1136 */       localStringBuilder.append("resolve");
/*      */     }
/*      */ 
/* 1139 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public String getActions()
/*      */   {
/* 1151 */     if (this.actions == null) {
/* 1152 */       this.actions = getActions(this.mask);
/*      */     }
/* 1154 */     return this.actions;
/*      */   }
/*      */ 
/*      */   public PermissionCollection newPermissionCollection()
/*      */   {
/* 1170 */     return new SocketPermissionCollection();
/*      */   }
/*      */ 
/*      */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1183 */     if (this.actions == null)
/* 1184 */       getActions();
/* 1185 */     paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private synchronized void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1196 */     paramObjectInputStream.defaultReadObject();
/* 1197 */     init(getName(), getMask(this.actions));
/*      */   }
/*      */ 
/*      */   private static int initEphemeralPorts(String paramString, int paramInt)
/*      */   {
/* 1208 */     return ((Integer)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Integer run() {
/* 1211 */         int i = Integer.getInteger("jdk.net.ephemeralPortRange." + this.val$suffix, -1).intValue();
/*      */ 
/* 1214 */         if (i != -1) {
/* 1215 */           return Integer.valueOf(i);
/*      */         }
/* 1217 */         return Integer.valueOf(this.val$suffix.equals("low") ? PortConfig.getLower() : PortConfig.getUpper());
/*      */       }
/*      */     })).intValue();
/*      */   }
/*      */ 
/*      */   private static boolean inRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1234 */     if (paramInt3 == 0)
/*      */     {
/* 1236 */       if (!inRange(paramInt1, paramInt2, ephemeralLow, ephemeralHigh)) {
/* 1237 */         return false;
/*      */       }
/* 1239 */       if (paramInt4 == 0)
/*      */       {
/* 1241 */         return true;
/*      */       }
/*      */ 
/* 1244 */       paramInt3 = 1;
/*      */     }
/*      */ 
/* 1247 */     if ((paramInt1 == 0) && (paramInt2 == 0))
/*      */     {
/* 1249 */       return (paramInt3 >= ephemeralLow) && (paramInt4 <= ephemeralHigh);
/*      */     }
/*      */ 
/* 1252 */     if (paramInt1 != 0)
/*      */     {
/* 1254 */       return (paramInt3 >= paramInt1) && (paramInt4 <= paramInt2);
/*      */     }
/*      */ 
/* 1261 */     if (paramInt2 >= ephemeralLow - 1) {
/* 1262 */       return paramInt4 <= ephemeralHigh;
/*      */     }
/*      */ 
/* 1269 */     return ((paramInt3 <= paramInt2) && (paramInt4 <= paramInt2)) || ((paramInt3 >= ephemeralLow) && (paramInt4 <= ephemeralHigh));
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  243 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.net.trustNameService"));
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.SocketPermission
 * JD-Core Version:    0.6.2
 */