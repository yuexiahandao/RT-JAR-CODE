/*      */ package com.sun.org.apache.xerces.internal.util;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.utils.Objects;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public class URI
/*      */   implements Serializable
/*      */ {
/*      */   static final long serialVersionUID = 1601921774685357214L;
/*   96 */   private static final byte[] fgLookupTable = new byte[''];
/*      */   private static final int RESERVED_CHARACTERS = 1;
/*      */   private static final int MARK_CHARACTERS = 2;
/*      */   private static final int SCHEME_CHARACTERS = 4;
/*      */   private static final int USERINFO_CHARACTERS = 8;
/*      */   private static final int ASCII_ALPHA_CHARACTERS = 16;
/*      */   private static final int ASCII_DIGIT_CHARACTERS = 32;
/*      */   private static final int ASCII_HEX_CHARACTERS = 64;
/*      */   private static final int PATH_CHARACTERS = 128;
/*      */   private static final int MASK_ALPHA_NUMERIC = 48;
/*      */   private static final int MASK_UNRESERVED_MASK = 50;
/*      */   private static final int MASK_URI_CHARACTER = 51;
/*      */   private static final int MASK_SCHEME_CHARACTER = 52;
/*      */   private static final int MASK_USERINFO_CHARACTER = 58;
/*      */   private static final int MASK_PATH_CHARACTER = 178;
/*  217 */   private String m_scheme = null;
/*      */ 
/*  220 */   private String m_userinfo = null;
/*      */ 
/*  223 */   private String m_host = null;
/*      */ 
/*  226 */   private int m_port = -1;
/*      */ 
/*  229 */   private String m_regAuthority = null;
/*      */ 
/*  232 */   private String m_path = null;
/*      */ 
/*  236 */   private String m_queryString = null;
/*      */ 
/*  239 */   private String m_fragment = null;
/*      */ 
/*  241 */   private static boolean DEBUG = false;
/*      */ 
/*      */   public URI()
/*      */   {
/*      */   }
/*      */ 
/*      */   public URI(URI p_other)
/*      */   {
/*  256 */     initialize(p_other);
/*      */   }
/*      */ 
/*      */   public URI(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  275 */     this((URI)null, p_uriSpec);
/*      */   }
/*      */ 
/*      */   public URI(String p_uriSpec, boolean allowNonAbsoluteURI)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  298 */     this((URI)null, p_uriSpec, allowNonAbsoluteURI);
/*      */   }
/*      */ 
/*      */   public URI(URI p_base, String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  314 */     initialize(p_base, p_uriSpec);
/*      */   }
/*      */ 
/*      */   public URI(URI p_base, String p_uriSpec, boolean allowNonAbsoluteURI)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  335 */     initialize(p_base, p_uriSpec, allowNonAbsoluteURI);
/*      */   }
/*      */ 
/*      */   public URI(String p_scheme, String p_schemeSpecificPart)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  352 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0)) {
/*  353 */       throw new MalformedURIException("Cannot construct URI with null/empty scheme!");
/*      */     }
/*      */ 
/*  356 */     if ((p_schemeSpecificPart == null) || (p_schemeSpecificPart.trim().length() == 0))
/*      */     {
/*  358 */       throw new MalformedURIException("Cannot construct URI with null/empty scheme-specific part!");
/*      */     }
/*      */ 
/*  361 */     setScheme(p_scheme);
/*  362 */     setPath(p_schemeSpecificPart);
/*      */   }
/*      */ 
/*      */   public URI(String p_scheme, String p_host, String p_path, String p_queryString, String p_fragment)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  389 */     this(p_scheme, null, p_host, -1, p_path, p_queryString, p_fragment);
/*      */   }
/*      */ 
/*      */   public URI(String p_scheme, String p_userinfo, String p_host, int p_port, String p_path, String p_queryString, String p_fragment)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  421 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0)) {
/*  422 */       throw new MalformedURIException("Scheme is required!");
/*      */     }
/*      */ 
/*  425 */     if (p_host == null) {
/*  426 */       if (p_userinfo != null) {
/*  427 */         throw new MalformedURIException("Userinfo may not be specified if host is not specified!");
/*      */       }
/*      */ 
/*  430 */       if (p_port != -1) {
/*  431 */         throw new MalformedURIException("Port may not be specified if host is not specified!");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  436 */     if (p_path != null) {
/*  437 */       if ((p_path.indexOf('?') != -1) && (p_queryString != null)) {
/*  438 */         throw new MalformedURIException("Query string cannot be specified in path and query string!");
/*      */       }
/*      */ 
/*  442 */       if ((p_path.indexOf('#') != -1) && (p_fragment != null)) {
/*  443 */         throw new MalformedURIException("Fragment cannot be specified in both the path and fragment!");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  448 */     setScheme(p_scheme);
/*  449 */     setHost(p_host);
/*  450 */     setPort(p_port);
/*  451 */     setUserinfo(p_userinfo);
/*  452 */     setPath(p_path);
/*  453 */     setQueryString(p_queryString);
/*  454 */     setFragment(p_fragment);
/*      */   }
/*      */ 
/*      */   private void initialize(URI p_other)
/*      */   {
/*  463 */     this.m_scheme = p_other.getScheme();
/*  464 */     this.m_userinfo = p_other.getUserinfo();
/*  465 */     this.m_host = p_other.getHost();
/*  466 */     this.m_port = p_other.getPort();
/*  467 */     this.m_regAuthority = p_other.getRegBasedAuthority();
/*  468 */     this.m_path = p_other.getPath();
/*  469 */     this.m_queryString = p_other.getQueryString();
/*  470 */     this.m_fragment = p_other.getFragment();
/*      */   }
/*      */ 
/*      */   private void initialize(URI p_base, String p_uriSpec, boolean allowNonAbsoluteURI)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  494 */     String uriSpec = p_uriSpec;
/*  495 */     int uriSpecLen = uriSpec != null ? uriSpec.length() : 0;
/*      */ 
/*  497 */     if ((p_base == null) && (uriSpecLen == 0)) {
/*  498 */       if (allowNonAbsoluteURI) {
/*  499 */         this.m_path = "";
/*  500 */         return;
/*      */       }
/*  502 */       throw new MalformedURIException("Cannot initialize URI with empty parameters.");
/*      */     }
/*      */ 
/*  506 */     if (uriSpecLen == 0) {
/*  507 */       initialize(p_base);
/*  508 */       return;
/*      */     }
/*      */ 
/*  511 */     int index = 0;
/*      */ 
/*  514 */     int colonIdx = uriSpec.indexOf(':');
/*  515 */     if (colonIdx != -1) {
/*  516 */       int searchFrom = colonIdx - 1;
/*      */ 
/*  518 */       int slashIdx = uriSpec.lastIndexOf('/', searchFrom);
/*  519 */       int queryIdx = uriSpec.lastIndexOf('?', searchFrom);
/*  520 */       int fragmentIdx = uriSpec.lastIndexOf('#', searchFrom);
/*      */ 
/*  522 */       if ((colonIdx == 0) || (slashIdx != -1) || (queryIdx != -1) || (fragmentIdx != -1))
/*      */       {
/*  525 */         if ((colonIdx == 0) || ((p_base == null) && (fragmentIdx != 0) && (!allowNonAbsoluteURI)))
/*  526 */           throw new MalformedURIException("No scheme found in URI.");
/*      */       }
/*      */       else
/*      */       {
/*  530 */         initializeScheme(uriSpec);
/*  531 */         index = this.m_scheme.length() + 1;
/*      */ 
/*  534 */         if ((colonIdx == uriSpecLen - 1) || (uriSpec.charAt(colonIdx + 1) == '#')) {
/*  535 */           throw new MalformedURIException("Scheme specific part cannot be empty.");
/*      */         }
/*      */       }
/*      */     }
/*  539 */     else if ((p_base == null) && (uriSpec.indexOf('#') != 0) && (!allowNonAbsoluteURI)) {
/*  540 */       throw new MalformedURIException("No scheme found in URI.");
/*      */     }
/*      */ 
/*  552 */     if ((index + 1 < uriSpecLen) && (uriSpec.charAt(index) == '/') && (uriSpec.charAt(index + 1) == '/'))
/*      */     {
/*  554 */       index += 2;
/*  555 */       int startPos = index;
/*      */ 
/*  558 */       char testChar = '\000';
/*  559 */       while (index < uriSpecLen) {
/*  560 */         testChar = uriSpec.charAt(index);
/*  561 */         if ((testChar == '/') || (testChar == '?') || (testChar == '#')) {
/*      */           break;
/*      */         }
/*  564 */         index++;
/*      */       }
/*      */ 
/*  570 */       if (index > startPos)
/*      */       {
/*  573 */         if (!initializeAuthority(uriSpec.substring(startPos, index))) {
/*  574 */           index = startPos - 2;
/*      */         }
/*      */       }
/*      */       else {
/*  578 */         this.m_host = "";
/*      */       }
/*      */     }
/*      */ 
/*  582 */     initializePath(uriSpec, index);
/*      */ 
/*  589 */     if (p_base != null)
/*  590 */       absolutize(p_base);
/*      */   }
/*      */ 
/*      */   private void initialize(URI p_base, String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  613 */     String uriSpec = p_uriSpec;
/*  614 */     int uriSpecLen = uriSpec != null ? uriSpec.length() : 0;
/*      */ 
/*  616 */     if ((p_base == null) && (uriSpecLen == 0)) {
/*  617 */       throw new MalformedURIException("Cannot initialize URI with empty parameters.");
/*      */     }
/*      */ 
/*  622 */     if (uriSpecLen == 0) {
/*  623 */       initialize(p_base);
/*  624 */       return;
/*      */     }
/*      */ 
/*  627 */     int index = 0;
/*      */ 
/*  630 */     int colonIdx = uriSpec.indexOf(':');
/*  631 */     if (colonIdx != -1) {
/*  632 */       int searchFrom = colonIdx - 1;
/*      */ 
/*  634 */       int slashIdx = uriSpec.lastIndexOf('/', searchFrom);
/*  635 */       int queryIdx = uriSpec.lastIndexOf('?', searchFrom);
/*  636 */       int fragmentIdx = uriSpec.lastIndexOf('#', searchFrom);
/*      */ 
/*  638 */       if ((colonIdx == 0) || (slashIdx != -1) || (queryIdx != -1) || (fragmentIdx != -1))
/*      */       {
/*  641 */         if ((colonIdx == 0) || ((p_base == null) && (fragmentIdx != 0)))
/*  642 */           throw new MalformedURIException("No scheme found in URI.");
/*      */       }
/*      */       else
/*      */       {
/*  646 */         initializeScheme(uriSpec);
/*  647 */         index = this.m_scheme.length() + 1;
/*      */ 
/*  650 */         if ((colonIdx == uriSpecLen - 1) || (uriSpec.charAt(colonIdx + 1) == '#')) {
/*  651 */           throw new MalformedURIException("Scheme specific part cannot be empty.");
/*      */         }
/*      */       }
/*      */     }
/*  655 */     else if ((p_base == null) && (uriSpec.indexOf('#') != 0)) {
/*  656 */       throw new MalformedURIException("No scheme found in URI.");
/*      */     }
/*      */ 
/*  668 */     if ((index + 1 < uriSpecLen) && (uriSpec.charAt(index) == '/') && (uriSpec.charAt(index + 1) == '/'))
/*      */     {
/*  670 */       index += 2;
/*  671 */       int startPos = index;
/*      */ 
/*  674 */       char testChar = '\000';
/*  675 */       while (index < uriSpecLen) {
/*  676 */         testChar = uriSpec.charAt(index);
/*  677 */         if ((testChar == '/') || (testChar == '?') || (testChar == '#')) {
/*      */           break;
/*      */         }
/*  680 */         index++;
/*      */       }
/*      */ 
/*  686 */       if (index > startPos)
/*      */       {
/*  689 */         if (!initializeAuthority(uriSpec.substring(startPos, index)))
/*  690 */           index = startPos - 2;
/*      */       }
/*  692 */       else if (index < uriSpecLen)
/*      */       {
/*  696 */         this.m_host = "";
/*      */       }
/*  698 */       else throw new MalformedURIException("Expected authority.");
/*      */ 
/*      */     }
/*      */ 
/*  702 */     initializePath(uriSpec, index);
/*      */ 
/*  709 */     if (p_base != null)
/*  710 */       absolutize(p_base);
/*      */   }
/*      */ 
/*      */   public void absolutize(URI p_base)
/*      */   {
/*  728 */     if ((this.m_path.length() == 0) && (this.m_scheme == null) && (this.m_host == null) && (this.m_regAuthority == null))
/*      */     {
/*  730 */       this.m_scheme = p_base.getScheme();
/*  731 */       this.m_userinfo = p_base.getUserinfo();
/*  732 */       this.m_host = p_base.getHost();
/*  733 */       this.m_port = p_base.getPort();
/*  734 */       this.m_regAuthority = p_base.getRegBasedAuthority();
/*  735 */       this.m_path = p_base.getPath();
/*      */ 
/*  737 */       if (this.m_queryString == null) {
/*  738 */         this.m_queryString = p_base.getQueryString();
/*      */ 
/*  740 */         if (this.m_fragment == null) {
/*  741 */           this.m_fragment = p_base.getFragment();
/*      */         }
/*      */       }
/*  744 */       return;
/*      */     }
/*      */ 
/*  749 */     if (this.m_scheme == null) {
/*  750 */       this.m_scheme = p_base.getScheme();
/*      */     }
/*      */     else {
/*  753 */       return;
/*      */     }
/*      */ 
/*  758 */     if ((this.m_host == null) && (this.m_regAuthority == null)) {
/*  759 */       this.m_userinfo = p_base.getUserinfo();
/*  760 */       this.m_host = p_base.getHost();
/*  761 */       this.m_port = p_base.getPort();
/*  762 */       this.m_regAuthority = p_base.getRegBasedAuthority();
/*      */     }
/*      */     else {
/*  765 */       return;
/*      */     }
/*      */ 
/*  769 */     if ((this.m_path.length() > 0) && (this.m_path.startsWith("/")))
/*      */     {
/*  771 */       return;
/*      */     }
/*      */ 
/*  776 */     String path = "";
/*  777 */     String basePath = p_base.getPath();
/*      */ 
/*  780 */     if ((basePath != null) && (basePath.length() > 0)) {
/*  781 */       int lastSlash = basePath.lastIndexOf('/');
/*  782 */       if (lastSlash != -1) {
/*  783 */         path = basePath.substring(0, lastSlash + 1);
/*      */       }
/*      */     }
/*  786 */     else if (this.m_path.length() > 0) {
/*  787 */       path = "/";
/*      */     }
/*      */ 
/*  791 */     path = path.concat(this.m_path);
/*      */ 
/*  794 */     int index = -1;
/*  795 */     while ((index = path.indexOf("/./")) != -1) {
/*  796 */       path = path.substring(0, index + 1).concat(path.substring(index + 3));
/*      */     }
/*      */ 
/*  800 */     if (path.endsWith("/.")) {
/*  801 */       path = path.substring(0, path.length() - 1);
/*      */     }
/*      */ 
/*  806 */     index = 1;
/*  807 */     int segIndex = -1;
/*  808 */     String tempString = null;
/*      */ 
/*  810 */     while ((index = path.indexOf("/../", index)) > 0) {
/*  811 */       tempString = path.substring(0, path.indexOf("/../"));
/*  812 */       segIndex = tempString.lastIndexOf('/');
/*  813 */       if (segIndex != -1) {
/*  814 */         if (!tempString.substring(segIndex).equals("..")) {
/*  815 */           path = path.substring(0, segIndex + 1).concat(path.substring(index + 4));
/*  816 */           index = segIndex;
/*      */         }
/*      */         else {
/*  819 */           index += 4;
/*      */         }
/*      */       }
/*      */       else {
/*  823 */         index += 4;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  829 */     if (path.endsWith("/..")) {
/*  830 */       tempString = path.substring(0, path.length() - 3);
/*  831 */       segIndex = tempString.lastIndexOf('/');
/*  832 */       if (segIndex != -1) {
/*  833 */         path = path.substring(0, segIndex + 1);
/*      */       }
/*      */     }
/*  836 */     this.m_path = path;
/*      */   }
/*      */ 
/*      */   private void initializeScheme(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  849 */     int uriSpecLen = p_uriSpec.length();
/*  850 */     int index = 0;
/*  851 */     String scheme = null;
/*  852 */     char testChar = '\000';
/*      */ 
/*  854 */     while (index < uriSpecLen) {
/*  855 */       testChar = p_uriSpec.charAt(index);
/*  856 */       if ((testChar == ':') || (testChar == '/') || (testChar == '?') || (testChar == '#'))
/*      */       {
/*      */         break;
/*      */       }
/*  860 */       index++;
/*      */     }
/*  862 */     scheme = p_uriSpec.substring(0, index);
/*      */ 
/*  864 */     if (scheme.length() == 0) {
/*  865 */       throw new MalformedURIException("No scheme found in URI.");
/*      */     }
/*      */ 
/*  868 */     setScheme(scheme);
/*      */   }
/*      */ 
/*      */   private boolean initializeAuthority(String p_uriSpec)
/*      */   {
/*  883 */     int index = 0;
/*  884 */     int start = 0;
/*  885 */     int end = p_uriSpec.length();
/*      */ 
/*  887 */     char testChar = '\000';
/*  888 */     String userinfo = null;
/*      */ 
/*  891 */     if (p_uriSpec.indexOf('@', start) != -1) {
/*  892 */       while (index < end) {
/*  893 */         testChar = p_uriSpec.charAt(index);
/*  894 */         if (testChar == '@') {
/*      */           break;
/*      */         }
/*  897 */         index++;
/*      */       }
/*  899 */       userinfo = p_uriSpec.substring(start, index);
/*  900 */       index++;
/*      */     }
/*      */ 
/*  905 */     String host = null;
/*  906 */     start = index;
/*  907 */     boolean hasPort = false;
/*  908 */     if (index < end) {
/*  909 */       if (p_uriSpec.charAt(start) == '[') {
/*  910 */         int bracketIndex = p_uriSpec.indexOf(']', start);
/*  911 */         index = bracketIndex != -1 ? bracketIndex : end;
/*  912 */         if ((index + 1 < end) && (p_uriSpec.charAt(index + 1) == ':')) {
/*  913 */           index++;
/*  914 */           hasPort = true;
/*      */         }
/*      */         else {
/*  917 */           index = end;
/*      */         }
/*      */       }
/*      */       else {
/*  921 */         int colonIndex = p_uriSpec.lastIndexOf(':', end);
/*  922 */         index = colonIndex > start ? colonIndex : end;
/*  923 */         hasPort = index != end;
/*      */       }
/*      */     }
/*  926 */     host = p_uriSpec.substring(start, index);
/*  927 */     int port = -1;
/*  928 */     if (host.length() > 0)
/*      */     {
/*  930 */       if (hasPort) {
/*  931 */         index++;
/*  932 */         start = index;
/*  933 */         while (index < end) {
/*  934 */           index++;
/*      */         }
/*  936 */         String portStr = p_uriSpec.substring(start, index);
/*  937 */         if (portStr.length() > 0)
/*      */         {
/*      */           try
/*      */           {
/*  949 */             port = Integer.parseInt(portStr);
/*  950 */             if (port == -1) port--; 
/*      */           }
/*      */           catch (NumberFormatException nfe)
/*      */           {
/*  953 */             port = -2;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  959 */     if (isValidServerBasedAuthority(host, port, userinfo)) {
/*  960 */       this.m_host = host;
/*  961 */       this.m_port = port;
/*  962 */       this.m_userinfo = userinfo;
/*  963 */       return true;
/*      */     }
/*      */ 
/*  969 */     if (isValidRegistryBasedAuthority(p_uriSpec)) {
/*  970 */       this.m_regAuthority = p_uriSpec;
/*  971 */       return true;
/*      */     }
/*  973 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isValidServerBasedAuthority(String host, int port, String userinfo)
/*      */   {
/*  990 */     if (!isWellFormedAddress(host)) {
/*  991 */       return false;
/*      */     }
/*      */ 
/*  998 */     if ((port < -1) || (port > 65535)) {
/*  999 */       return false;
/*      */     }
/*      */ 
/* 1003 */     if (userinfo != null)
/*      */     {
/* 1006 */       int index = 0;
/* 1007 */       int end = userinfo.length();
/* 1008 */       char testChar = '\000';
/* 1009 */       while (index < end) {
/* 1010 */         testChar = userinfo.charAt(index);
/* 1011 */         if (testChar == '%') {
/* 1012 */           if ((index + 2 >= end) || (!isHex(userinfo.charAt(index + 1))) || (!isHex(userinfo.charAt(index + 2))))
/*      */           {
/* 1015 */             return false;
/*      */           }
/* 1017 */           index += 2;
/*      */         }
/* 1019 */         else if (!isUserinfoCharacter(testChar)) {
/* 1020 */           return false;
/*      */         }
/* 1022 */         index++;
/*      */       }
/*      */     }
/* 1025 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean isValidRegistryBasedAuthority(String authority)
/*      */   {
/* 1036 */     int index = 0;
/* 1037 */     int end = authority.length();
/*      */ 
/* 1040 */     while (index < end) {
/* 1041 */       char testChar = authority.charAt(index);
/*      */ 
/* 1044 */       if (testChar == '%') {
/* 1045 */         if ((index + 2 >= end) || (!isHex(authority.charAt(index + 1))) || (!isHex(authority.charAt(index + 2))))
/*      */         {
/* 1048 */           return false;
/*      */         }
/* 1050 */         index += 2;
/*      */       }
/* 1054 */       else if (!isPathCharacter(testChar)) {
/* 1055 */         return false;
/*      */       }
/* 1057 */       index++;
/*      */     }
/* 1059 */     return true;
/*      */   }
/*      */ 
/*      */   private void initializePath(String p_uriSpec, int p_nStartIndex)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1072 */     if (p_uriSpec == null) {
/* 1073 */       throw new MalformedURIException("Cannot initialize path from null string!");
/*      */     }
/*      */ 
/* 1077 */     int index = p_nStartIndex;
/* 1078 */     int start = p_nStartIndex;
/* 1079 */     int end = p_uriSpec.length();
/* 1080 */     char testChar = '\000';
/*      */ 
/* 1083 */     if (start < end)
/*      */     {
/* 1085 */       if ((getScheme() == null) || (p_uriSpec.charAt(start) == '/'));
/* 1090 */       while (index < end) {
/* 1091 */         testChar = p_uriSpec.charAt(index);
/*      */ 
/* 1094 */         if (testChar == '%') {
/* 1095 */           if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */           {
/* 1098 */             throw new MalformedURIException("Path contains invalid escape sequence!");
/*      */           }
/*      */ 
/* 1101 */           index += 2;
/*      */         }
/* 1105 */         else if (!isPathCharacter(testChar)) {
/* 1106 */           if ((testChar == '?') || (testChar == '#')) {
/*      */             break;
/*      */           }
/* 1109 */           throw new MalformedURIException("Path contains invalid character: " + testChar);
/*      */         }
/*      */ 
/* 1112 */         index++; continue;
/*      */ 
/* 1119 */         while (index < end) {
/* 1120 */           testChar = p_uriSpec.charAt(index);
/*      */ 
/* 1122 */           if ((testChar == '?') || (testChar == '#'))
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/* 1127 */           if (testChar == '%') {
/* 1128 */             if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */             {
/* 1131 */               throw new MalformedURIException("Opaque part contains invalid escape sequence!");
/*      */             }
/*      */ 
/* 1134 */             index += 2;
/*      */           }
/* 1141 */           else if (!isURICharacter(testChar)) {
/* 1142 */             throw new MalformedURIException("Opaque part contains invalid character: " + testChar);
/*      */           }
/*      */ 
/* 1145 */           index++;
/*      */         }
/*      */       }
/*      */     }
/* 1149 */     this.m_path = p_uriSpec.substring(start, index);
/*      */ 
/* 1152 */     if (testChar == '?') {
/* 1153 */       index++;
/* 1154 */       start = index;
/* 1155 */       while (index < end) {
/* 1156 */         testChar = p_uriSpec.charAt(index);
/* 1157 */         if (testChar == '#') {
/*      */           break;
/*      */         }
/* 1160 */         if (testChar == '%') {
/* 1161 */           if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */           {
/* 1164 */             throw new MalformedURIException("Query string contains invalid escape sequence!");
/*      */           }
/*      */ 
/* 1167 */           index += 2;
/*      */         }
/* 1169 */         else if (!isURICharacter(testChar)) {
/* 1170 */           throw new MalformedURIException("Query string contains invalid character: " + testChar);
/*      */         }
/*      */ 
/* 1173 */         index++;
/*      */       }
/* 1175 */       this.m_queryString = p_uriSpec.substring(start, index);
/*      */     }
/*      */ 
/* 1179 */     if (testChar == '#') {
/* 1180 */       index++;
/* 1181 */       start = index;
/* 1182 */       while (index < end) {
/* 1183 */         testChar = p_uriSpec.charAt(index);
/*      */ 
/* 1185 */         if (testChar == '%') {
/* 1186 */           if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */           {
/* 1189 */             throw new MalformedURIException("Fragment contains invalid escape sequence!");
/*      */           }
/*      */ 
/* 1192 */           index += 2;
/*      */         }
/* 1194 */         else if (!isURICharacter(testChar)) {
/* 1195 */           throw new MalformedURIException("Fragment contains invalid character: " + testChar);
/*      */         }
/*      */ 
/* 1198 */         index++;
/*      */       }
/* 1200 */       this.m_fragment = p_uriSpec.substring(start, index);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getScheme()
/*      */   {
/* 1210 */     return this.m_scheme;
/*      */   }
/*      */ 
/*      */   public String getSchemeSpecificPart()
/*      */   {
/* 1220 */     StringBuilder schemespec = new StringBuilder();
/*      */ 
/* 1222 */     if ((this.m_host != null) || (this.m_regAuthority != null)) {
/* 1223 */       schemespec.append("//");
/*      */ 
/* 1226 */       if (this.m_host != null)
/*      */       {
/* 1228 */         if (this.m_userinfo != null) {
/* 1229 */           schemespec.append(this.m_userinfo);
/* 1230 */           schemespec.append('@');
/*      */         }
/*      */ 
/* 1233 */         schemespec.append(this.m_host);
/*      */ 
/* 1235 */         if (this.m_port != -1) {
/* 1236 */           schemespec.append(':');
/* 1237 */           schemespec.append(this.m_port);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1242 */         schemespec.append(this.m_regAuthority);
/*      */       }
/*      */     }
/*      */ 
/* 1246 */     if (this.m_path != null) {
/* 1247 */       schemespec.append(this.m_path);
/*      */     }
/*      */ 
/* 1250 */     if (this.m_queryString != null) {
/* 1251 */       schemespec.append('?');
/* 1252 */       schemespec.append(this.m_queryString);
/*      */     }
/*      */ 
/* 1255 */     if (this.m_fragment != null) {
/* 1256 */       schemespec.append('#');
/* 1257 */       schemespec.append(this.m_fragment);
/*      */     }
/*      */ 
/* 1260 */     return schemespec.toString();
/*      */   }
/*      */ 
/*      */   public String getUserinfo()
/*      */   {
/* 1269 */     return this.m_userinfo;
/*      */   }
/*      */ 
/*      */   public String getHost()
/*      */   {
/* 1278 */     return this.m_host;
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/* 1287 */     return this.m_port;
/*      */   }
/*      */ 
/*      */   public String getRegBasedAuthority()
/*      */   {
/* 1296 */     return this.m_regAuthority;
/*      */   }
/*      */ 
/*      */   public String getAuthority()
/*      */   {
/* 1305 */     StringBuilder authority = new StringBuilder();
/* 1306 */     if ((this.m_host != null) || (this.m_regAuthority != null)) {
/* 1307 */       authority.append("//");
/*      */ 
/* 1310 */       if (this.m_host != null)
/*      */       {
/* 1312 */         if (this.m_userinfo != null) {
/* 1313 */           authority.append(this.m_userinfo);
/* 1314 */           authority.append('@');
/*      */         }
/*      */ 
/* 1317 */         authority.append(this.m_host);
/*      */ 
/* 1319 */         if (this.m_port != -1) {
/* 1320 */           authority.append(':');
/* 1321 */           authority.append(this.m_port);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1326 */         authority.append(this.m_regAuthority);
/*      */       }
/*      */     }
/* 1329 */     return authority.toString();
/*      */   }
/*      */ 
/*      */   public String getPath(boolean p_includeQueryString, boolean p_includeFragment)
/*      */   {
/* 1348 */     StringBuilder pathString = new StringBuilder(this.m_path);
/*      */ 
/* 1350 */     if ((p_includeQueryString) && (this.m_queryString != null)) {
/* 1351 */       pathString.append('?');
/* 1352 */       pathString.append(this.m_queryString);
/*      */     }
/*      */ 
/* 1355 */     if ((p_includeFragment) && (this.m_fragment != null)) {
/* 1356 */       pathString.append('#');
/* 1357 */       pathString.append(this.m_fragment);
/*      */     }
/* 1359 */     return pathString.toString();
/*      */   }
/*      */ 
/*      */   public String getPath()
/*      */   {
/* 1369 */     return this.m_path;
/*      */   }
/*      */ 
/*      */   public String getQueryString()
/*      */   {
/* 1380 */     return this.m_queryString;
/*      */   }
/*      */ 
/*      */   public String getFragment()
/*      */   {
/* 1391 */     return this.m_fragment;
/*      */   }
/*      */ 
/*      */   public void setScheme(String p_scheme)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1404 */     if (p_scheme == null) {
/* 1405 */       throw new MalformedURIException("Cannot set scheme from null string!");
/*      */     }
/*      */ 
/* 1408 */     if (!isConformantSchemeName(p_scheme)) {
/* 1409 */       throw new MalformedURIException("The scheme is not conformant.");
/*      */     }
/*      */ 
/* 1412 */     this.m_scheme = p_scheme.toLowerCase();
/*      */   }
/*      */ 
/*      */   public void setUserinfo(String p_userinfo)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1425 */     if (p_userinfo == null) {
/* 1426 */       this.m_userinfo = null;
/* 1427 */       return;
/*      */     }
/*      */ 
/* 1430 */     if (this.m_host == null) {
/* 1431 */       throw new MalformedURIException("Userinfo cannot be set when host is null!");
/*      */     }
/*      */ 
/* 1437 */     int index = 0;
/* 1438 */     int end = p_userinfo.length();
/* 1439 */     char testChar = '\000';
/* 1440 */     while (index < end) {
/* 1441 */       testChar = p_userinfo.charAt(index);
/* 1442 */       if (testChar == '%') {
/* 1443 */         if ((index + 2 >= end) || (!isHex(p_userinfo.charAt(index + 1))) || (!isHex(p_userinfo.charAt(index + 2))))
/*      */         {
/* 1446 */           throw new MalformedURIException("Userinfo contains invalid escape sequence!");
/*      */         }
/*      */ 
/*      */       }
/* 1450 */       else if (!isUserinfoCharacter(testChar)) {
/* 1451 */         throw new MalformedURIException("Userinfo contains invalid character:" + testChar);
/*      */       }
/*      */ 
/* 1454 */       index++;
/*      */     }
/*      */ 
/* 1457 */     this.m_userinfo = p_userinfo;
/*      */   }
/*      */ 
/*      */   public void setHost(String p_host)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1473 */     if ((p_host == null) || (p_host.length() == 0)) {
/* 1474 */       if (p_host != null) {
/* 1475 */         this.m_regAuthority = null;
/*      */       }
/* 1477 */       this.m_host = p_host;
/* 1478 */       this.m_userinfo = null;
/* 1479 */       this.m_port = -1;
/* 1480 */       return;
/*      */     }
/* 1482 */     if (!isWellFormedAddress(p_host)) {
/* 1483 */       throw new MalformedURIException("Host is not a well formed address!");
/*      */     }
/* 1485 */     this.m_host = p_host;
/* 1486 */     this.m_regAuthority = null;
/*      */   }
/*      */ 
/*      */   public void setPort(int p_port)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1501 */     if ((p_port >= 0) && (p_port <= 65535)) {
/* 1502 */       if (this.m_host == null) {
/* 1503 */         throw new MalformedURIException("Port cannot be set when host is null!");
/*      */       }
/*      */ 
/*      */     }
/* 1507 */     else if (p_port != -1) {
/* 1508 */       throw new MalformedURIException("Invalid port number!");
/*      */     }
/* 1510 */     this.m_port = p_port;
/*      */   }
/*      */ 
/*      */   public void setRegBasedAuthority(String authority)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1527 */     if (authority == null) {
/* 1528 */       this.m_regAuthority = null;
/* 1529 */       return;
/*      */     }
/*      */ 
/* 1533 */     if ((authority.length() < 1) || (!isValidRegistryBasedAuthority(authority)) || (authority.indexOf('/') != -1))
/*      */     {
/* 1536 */       throw new MalformedURIException("Registry based authority is not well formed.");
/*      */     }
/* 1538 */     this.m_regAuthority = authority;
/* 1539 */     this.m_host = null;
/* 1540 */     this.m_userinfo = null;
/* 1541 */     this.m_port = -1;
/*      */   }
/*      */ 
/*      */   public void setPath(String p_path)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1559 */     if (p_path == null) {
/* 1560 */       this.m_path = null;
/* 1561 */       this.m_queryString = null;
/* 1562 */       this.m_fragment = null;
/*      */     }
/*      */     else {
/* 1565 */       initializePath(p_path, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void appendPath(String p_addToPath)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1584 */     if ((p_addToPath == null) || (p_addToPath.trim().length() == 0)) {
/* 1585 */       return;
/*      */     }
/*      */ 
/* 1588 */     if (!isURIString(p_addToPath)) {
/* 1589 */       throw new MalformedURIException("Path contains invalid character!");
/*      */     }
/*      */ 
/* 1593 */     if ((this.m_path == null) || (this.m_path.trim().length() == 0)) {
/* 1594 */       if (p_addToPath.startsWith("/")) {
/* 1595 */         this.m_path = p_addToPath;
/*      */       }
/*      */       else {
/* 1598 */         this.m_path = ("/" + p_addToPath);
/*      */       }
/*      */     }
/* 1601 */     else if (this.m_path.endsWith("/")) {
/* 1602 */       if (p_addToPath.startsWith("/")) {
/* 1603 */         this.m_path = this.m_path.concat(p_addToPath.substring(1));
/*      */       }
/*      */       else {
/* 1606 */         this.m_path = this.m_path.concat(p_addToPath);
/*      */       }
/*      */ 
/*      */     }
/* 1610 */     else if (p_addToPath.startsWith("/")) {
/* 1611 */       this.m_path = this.m_path.concat(p_addToPath);
/*      */     }
/*      */     else
/* 1614 */       this.m_path = this.m_path.concat("/" + p_addToPath);
/*      */   }
/*      */ 
/*      */   public void setQueryString(String p_queryString)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1631 */     if (p_queryString == null) {
/* 1632 */       this.m_queryString = null;
/*      */     } else {
/* 1634 */       if (!isGenericURI()) {
/* 1635 */         throw new MalformedURIException("Query string can only be set for a generic URI!");
/*      */       }
/*      */ 
/* 1638 */       if (getPath() == null) {
/* 1639 */         throw new MalformedURIException("Query string cannot be set when path is null!");
/*      */       }
/*      */ 
/* 1642 */       if (!isURIString(p_queryString)) {
/* 1643 */         throw new MalformedURIException("Query string contains invalid character!");
/*      */       }
/*      */ 
/* 1647 */       this.m_queryString = p_queryString;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFragment(String p_fragment)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1663 */     if (p_fragment == null) {
/* 1664 */       this.m_fragment = null;
/*      */     } else {
/* 1666 */       if (!isGenericURI()) {
/* 1667 */         throw new MalformedURIException("Fragment can only be set for a generic URI!");
/*      */       }
/*      */ 
/* 1670 */       if (getPath() == null) {
/* 1671 */         throw new MalformedURIException("Fragment cannot be set when path is null!");
/*      */       }
/*      */ 
/* 1674 */       if (!isURIString(p_fragment)) {
/* 1675 */         throw new MalformedURIException("Fragment contains invalid character!");
/*      */       }
/*      */ 
/* 1679 */       this.m_fragment = p_fragment;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equals(Object p_test)
/*      */   {
/* 1693 */     if ((p_test instanceof URI)) {
/* 1694 */       URI testURI = (URI)p_test;
/* 1695 */       if (((this.m_scheme == null) && (testURI.m_scheme == null)) || ((this.m_scheme != null) && (testURI.m_scheme != null) && (this.m_scheme.equals(testURI.m_scheme)) && (((this.m_userinfo == null) && (testURI.m_userinfo == null)) || ((this.m_userinfo != null) && (testURI.m_userinfo != null) && (this.m_userinfo.equals(testURI.m_userinfo)) && (((this.m_host == null) && (testURI.m_host == null)) || ((this.m_host != null) && (testURI.m_host != null) && (this.m_host.equals(testURI.m_host)) && (this.m_port == testURI.m_port) && (((this.m_path == null) && (testURI.m_path == null)) || ((this.m_path != null) && (testURI.m_path != null) && (this.m_path.equals(testURI.m_path)) && (((this.m_queryString == null) && (testURI.m_queryString == null)) || ((this.m_queryString != null) && (testURI.m_queryString != null) && (this.m_queryString.equals(testURI.m_queryString)) && (((this.m_fragment == null) && (testURI.m_fragment == null)) || ((this.m_fragment != null) && (testURI.m_fragment != null) && (this.m_fragment.equals(testURI.m_fragment))))))))))))))
/*      */       {
/* 1714 */         return true;
/*      */       }
/*      */     }
/* 1717 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1722 */     int hash = 5;
/* 1723 */     hash = 47 * hash + Objects.hashCode(this.m_scheme);
/* 1724 */     hash = 47 * hash + Objects.hashCode(this.m_userinfo);
/* 1725 */     hash = 47 * hash + Objects.hashCode(this.m_host);
/* 1726 */     hash = 47 * hash + this.m_port;
/* 1727 */     hash = 47 * hash + Objects.hashCode(this.m_path);
/* 1728 */     hash = 47 * hash + Objects.hashCode(this.m_queryString);
/* 1729 */     hash = 47 * hash + Objects.hashCode(this.m_fragment);
/* 1730 */     return hash;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1740 */     StringBuilder uriSpecString = new StringBuilder();
/*      */ 
/* 1742 */     if (this.m_scheme != null) {
/* 1743 */       uriSpecString.append(this.m_scheme);
/* 1744 */       uriSpecString.append(':');
/*      */     }
/* 1746 */     uriSpecString.append(getSchemeSpecificPart());
/* 1747 */     return uriSpecString.toString();
/*      */   }
/*      */ 
/*      */   public boolean isGenericURI()
/*      */   {
/* 1760 */     return this.m_host != null;
/*      */   }
/*      */ 
/*      */   public boolean isAbsoluteURI()
/*      */   {
/* 1771 */     return this.m_scheme != null;
/*      */   }
/*      */ 
/*      */   public static boolean isConformantSchemeName(String p_scheme)
/*      */   {
/* 1782 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0)) {
/* 1783 */       return false;
/*      */     }
/*      */ 
/* 1786 */     if (!isAlpha(p_scheme.charAt(0))) {
/* 1787 */       return false;
/*      */     }
/*      */ 
/* 1791 */     int schemeLength = p_scheme.length();
/* 1792 */     for (int i = 1; i < schemeLength; i++) {
/* 1793 */       char testChar = p_scheme.charAt(i);
/* 1794 */       if (!isSchemeCharacter(testChar)) {
/* 1795 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1799 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isWellFormedAddress(String address)
/*      */   {
/* 1815 */     if (address == null) {
/* 1816 */       return false;
/*      */     }
/*      */ 
/* 1819 */     int addrLength = address.length();
/* 1820 */     if (addrLength == 0) {
/* 1821 */       return false;
/*      */     }
/*      */ 
/* 1825 */     if (address.startsWith("[")) {
/* 1826 */       return isWellFormedIPv6Reference(address);
/*      */     }
/*      */ 
/* 1830 */     if ((address.startsWith(".")) || (address.startsWith("-")) || (address.endsWith("-")))
/*      */     {
/* 1833 */       return false;
/*      */     }
/*      */ 
/* 1839 */     int index = address.lastIndexOf('.');
/* 1840 */     if (address.endsWith(".")) {
/* 1841 */       index = address.substring(0, index).lastIndexOf('.');
/*      */     }
/*      */ 
/* 1844 */     if ((index + 1 < addrLength) && (isDigit(address.charAt(index + 1)))) {
/* 1845 */       return isWellFormedIPv4Address(address);
/*      */     }
/*      */ 
/* 1855 */     if (addrLength > 255) {
/* 1856 */       return false;
/*      */     }
/*      */ 
/* 1862 */     int labelCharCount = 0;
/*      */ 
/* 1864 */     for (int i = 0; i < addrLength; i++) {
/* 1865 */       char testChar = address.charAt(i);
/* 1866 */       if (testChar == '.') {
/* 1867 */         if (!isAlphanum(address.charAt(i - 1))) {
/* 1868 */           return false;
/*      */         }
/* 1870 */         if ((i + 1 < addrLength) && (!isAlphanum(address.charAt(i + 1)))) {
/* 1871 */           return false;
/*      */         }
/* 1873 */         labelCharCount = 0;
/*      */       } else {
/* 1875 */         if ((!isAlphanum(testChar)) && (testChar != '-')) {
/* 1876 */           return false;
/*      */         }
/*      */ 
/* 1879 */         labelCharCount++; if (labelCharCount > 63) {
/* 1880 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 1884 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isWellFormedIPv4Address(String address)
/*      */   {
/* 1900 */     int addrLength = address.length();
/*      */ 
/* 1902 */     int numDots = 0;
/* 1903 */     int numDigits = 0;
/*      */ 
/* 1915 */     for (int i = 0; i < addrLength; i++) {
/* 1916 */       char testChar = address.charAt(i);
/* 1917 */       if (testChar == '.') {
/* 1918 */         if (((i > 0) && (!isDigit(address.charAt(i - 1)))) || ((i + 1 < addrLength) && (!isDigit(address.charAt(i + 1)))))
/*      */         {
/* 1920 */           return false;
/*      */         }
/* 1922 */         numDigits = 0;
/* 1923 */         numDots++; if (numDots > 3)
/* 1924 */           return false;
/*      */       }
/*      */       else {
/* 1927 */         if (!isDigit(testChar)) {
/* 1928 */           return false;
/*      */         }
/*      */ 
/* 1932 */         numDigits++; if (numDigits > 3) {
/* 1933 */           return false;
/*      */         }
/*      */ 
/* 1936 */         if (numDigits == 3) {
/* 1937 */           char first = address.charAt(i - 2);
/* 1938 */           char second = address.charAt(i - 1);
/* 1939 */           if ((first >= '2') && ((first != '2') || ((second >= '5') && ((second != '5') || (testChar > '5')))))
/*      */           {
/* 1943 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1947 */     return numDots == 3;
/*      */   }
/*      */ 
/*      */   public static boolean isWellFormedIPv6Reference(String address)
/*      */   {
/* 1967 */     int addrLength = address.length();
/* 1968 */     int index = 1;
/* 1969 */     int end = addrLength - 1;
/*      */ 
/* 1972 */     if ((addrLength <= 2) || (address.charAt(0) != '[') || (address.charAt(end) != ']'))
/*      */     {
/* 1974 */       return false;
/*      */     }
/*      */ 
/* 1978 */     int[] counter = new int[1];
/*      */ 
/* 1981 */     index = scanHexSequence(address, index, end, counter);
/* 1982 */     if (index == -1) {
/* 1983 */       return false;
/*      */     }
/*      */ 
/* 1986 */     if (index == end) {
/* 1987 */       return counter[0] == 8;
/*      */     }
/*      */ 
/* 1990 */     if ((index + 1 < end) && (address.charAt(index) == ':')) {
/* 1991 */       if (address.charAt(index + 1) == ':')
/*      */       {
/* 1993 */         if (counter[0] += 1 > 8) {
/* 1994 */           return false;
/*      */         }
/* 1996 */         index += 2;
/*      */ 
/* 1998 */         if (index == end) {
/* 1999 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2006 */         return (counter[0] == 6) && (isWellFormedIPv4Address(address.substring(index + 1, end)));
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2011 */       return false;
/*      */     }
/*      */ 
/* 2015 */     int prevCount = counter[0];
/* 2016 */     index = scanHexSequence(address, index, end, counter);
/*      */ 
/* 2021 */     if (index != end) if (index == -1) break label221; label221: return isWellFormedIPv4Address(address.substring(counter[0] > prevCount ? index + 1 : index, end)); } 
/*      */   private static int scanHexSequence(String address, int index, int end, int[] counter) { // Byte code:
/*      */     //   0: iconst_0
/*      */     //   1: istore 5
/*      */     //   3: iload_1
/*      */     //   4: istore 6
/*      */     //   6: iload_1
/*      */     //   7: iload_2
/*      */     //   8: if_icmpge +147 -> 155
/*      */     //   11: aload_0
/*      */     //   12: iload_1
/*      */     //   13: invokevirtual 467	java/lang/String:charAt	(I)C
/*      */     //   16: istore 4
/*      */     //   18: iload 4
/*      */     //   20: bipush 58
/*      */     //   22: if_icmpne +55 -> 77
/*      */     //   25: iload 5
/*      */     //   27: ifle +18 -> 45
/*      */     //   30: aload_3
/*      */     //   31: iconst_0
/*      */     //   32: dup2
/*      */     //   33: iaload
/*      */     //   34: iconst_1
/*      */     //   35: iadd
/*      */     //   36: dup_x2
/*      */     //   37: iastore
/*      */     //   38: bipush 8
/*      */     //   40: if_icmple +5 -> 45
/*      */     //   43: iconst_m1
/*      */     //   44: ireturn
/*      */     //   45: iload 5
/*      */     //   47: ifeq +22 -> 69
/*      */     //   50: iload_1
/*      */     //   51: iconst_1
/*      */     //   52: iadd
/*      */     //   53: iload_2
/*      */     //   54: if_icmpge +17 -> 71
/*      */     //   57: aload_0
/*      */     //   58: iload_1
/*      */     //   59: iconst_1
/*      */     //   60: iadd
/*      */     //   61: invokevirtual 467	java/lang/String:charAt	(I)C
/*      */     //   64: bipush 58
/*      */     //   66: if_icmpne +5 -> 71
/*      */     //   69: iload_1
/*      */     //   70: ireturn
/*      */     //   71: iconst_0
/*      */     //   72: istore 5
/*      */     //   74: goto +75 -> 149
/*      */     //   77: iload 4
/*      */     //   79: invokestatic 424	com/sun/org/apache/xerces/internal/util/URI:isHex	(C)Z
/*      */     //   82: ifne +56 -> 138
/*      */     //   85: iload 4
/*      */     //   87: bipush 46
/*      */     //   89: if_icmpne +47 -> 136
/*      */     //   92: iload 5
/*      */     //   94: iconst_4
/*      */     //   95: if_icmpge +41 -> 136
/*      */     //   98: iload 5
/*      */     //   100: ifle +36 -> 136
/*      */     //   103: aload_3
/*      */     //   104: iconst_0
/*      */     //   105: iaload
/*      */     //   106: bipush 6
/*      */     //   108: if_icmpgt +28 -> 136
/*      */     //   111: iload_1
/*      */     //   112: iload 5
/*      */     //   114: isub
/*      */     //   115: iconst_1
/*      */     //   116: isub
/*      */     //   117: istore 7
/*      */     //   119: iload 7
/*      */     //   121: iload 6
/*      */     //   123: if_icmplt +8 -> 131
/*      */     //   126: iload 7
/*      */     //   128: goto +7 -> 135
/*      */     //   131: iload 7
/*      */     //   133: iconst_1
/*      */     //   134: iadd
/*      */     //   135: ireturn
/*      */     //   136: iconst_m1
/*      */     //   137: ireturn
/*      */     //   138: iinc 5 1
/*      */     //   141: iload 5
/*      */     //   143: iconst_4
/*      */     //   144: if_icmple +5 -> 149
/*      */     //   147: iconst_m1
/*      */     //   148: ireturn
/*      */     //   149: iinc 1 1
/*      */     //   152: goto -146 -> 6
/*      */     //   155: iload 5
/*      */     //   157: ifle +20 -> 177
/*      */     //   160: aload_3
/*      */     //   161: iconst_0
/*      */     //   162: dup2
/*      */     //   163: iaload
/*      */     //   164: iconst_1
/*      */     //   165: iadd
/*      */     //   166: dup_x2
/*      */     //   167: iastore
/*      */     //   168: bipush 8
/*      */     //   170: if_icmpgt +7 -> 177
/*      */     //   173: iload_2
/*      */     //   174: goto +4 -> 178
/*      */     //   177: iconst_m1
/*      */     //   178: ireturn } 
/* 2087 */   private static boolean isDigit(char p_char) { return (p_char >= '0') && (p_char <= '9'); }
/*      */ 
/*      */ 
/*      */   private static boolean isHex(char p_char)
/*      */   {
/* 2097 */     return (p_char <= 'f') && ((fgLookupTable[p_char] & 0x40) != 0);
/*      */   }
/*      */ 
/*      */   private static boolean isAlpha(char p_char)
/*      */   {
/* 2106 */     return ((p_char >= 'a') && (p_char <= 'z')) || ((p_char >= 'A') && (p_char <= 'Z'));
/*      */   }
/*      */ 
/*      */   private static boolean isAlphanum(char p_char)
/*      */   {
/* 2115 */     return (p_char <= 'z') && ((fgLookupTable[p_char] & 0x30) != 0);
/*      */   }
/*      */ 
/*      */   private static boolean isReservedCharacter(char p_char)
/*      */   {
/* 2125 */     return (p_char <= ']') && ((fgLookupTable[p_char] & 0x1) != 0);
/*      */   }
/*      */ 
/*      */   private static boolean isUnreservedCharacter(char p_char)
/*      */   {
/* 2134 */     return (p_char <= '~') && ((fgLookupTable[p_char] & 0x32) != 0);
/*      */   }
/*      */ 
/*      */   private static boolean isURICharacter(char p_char)
/*      */   {
/* 2144 */     return (p_char <= '~') && ((fgLookupTable[p_char] & 0x33) != 0);
/*      */   }
/*      */ 
/*      */   private static boolean isSchemeCharacter(char p_char)
/*      */   {
/* 2153 */     return (p_char <= 'z') && ((fgLookupTable[p_char] & 0x34) != 0);
/*      */   }
/*      */ 
/*      */   private static boolean isUserinfoCharacter(char p_char)
/*      */   {
/* 2162 */     return (p_char <= 'z') && ((fgLookupTable[p_char] & 0x3A) != 0);
/*      */   }
/*      */ 
/*      */   private static boolean isPathCharacter(char p_char)
/*      */   {
/* 2171 */     return (p_char <= '~') && ((fgLookupTable[p_char] & 0xB2) != 0);
/*      */   }
/*      */ 
/*      */   private static boolean isURIString(String p_uric)
/*      */   {
/* 2183 */     if (p_uric == null) {
/* 2184 */       return false;
/*      */     }
/* 2186 */     int end = p_uric.length();
/* 2187 */     char testChar = '\000';
/* 2188 */     for (int i = 0; i < end; i++) {
/* 2189 */       testChar = p_uric.charAt(i);
/* 2190 */       if (testChar == '%') {
/* 2191 */         if ((i + 2 >= end) || (!isHex(p_uric.charAt(i + 1))) || (!isHex(p_uric.charAt(i + 2))))
/*      */         {
/* 2194 */           return false;
/*      */         }
/*      */ 
/* 2197 */         i += 2;
/*      */       }
/* 2201 */       else if (!isURICharacter(testChar))
/*      */       {
/* 2205 */         return false;
/*      */       }
/*      */     }
/* 2208 */     return true;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  149 */     for (int i = 48; i <= 57; i++)
/*      */     {
/*      */       int tmp21_20 = i;
/*      */       byte[] tmp21_17 = fgLookupTable; tmp21_17[tmp21_20] = ((byte)(tmp21_17[tmp21_20] | 0x60));
/*      */     }
/*      */ 
/*  154 */     for (int i = 65; i <= 70; i++)
/*      */     {
/*      */       int tmp47_46 = i;
/*      */       byte[] tmp47_43 = fgLookupTable; tmp47_43[tmp47_46] = ((byte)(tmp47_43[tmp47_46] | 0x50));
/*      */       int tmp61_60 = (i + 32);
/*      */       byte[] tmp61_54 = fgLookupTable; tmp61_54[tmp61_60] = ((byte)(tmp61_54[tmp61_60] | 0x50));
/*      */     }
/*      */ 
/*  160 */     for (int i = 71; i <= 90; i++)
/*      */     {
/*      */       int tmp87_86 = i;
/*      */       byte[] tmp87_83 = fgLookupTable; tmp87_83[tmp87_86] = ((byte)(tmp87_83[tmp87_86] | 0x10));
/*      */       int tmp101_100 = (i + 32);
/*      */       byte[] tmp101_94 = fgLookupTable; tmp101_94[tmp101_100] = ((byte)(tmp101_94[tmp101_100] | 0x10));
/*      */     }
/*      */     byte[] tmp119_114 = fgLookupTable; tmp119_114[59] = ((byte)(tmp119_114[59] | 0x1));
/*      */     byte[] tmp130_125 = fgLookupTable; tmp130_125[47] = ((byte)(tmp130_125[47] | 0x1));
/*      */     byte[] tmp141_136 = fgLookupTable; tmp141_136[63] = ((byte)(tmp141_136[63] | 0x1));
/*      */     byte[] tmp152_147 = fgLookupTable; tmp152_147[58] = ((byte)(tmp152_147[58] | 0x1));
/*      */     byte[] tmp163_158 = fgLookupTable; tmp163_158[64] = ((byte)(tmp163_158[64] | 0x1));
/*      */     byte[] tmp174_169 = fgLookupTable; tmp174_169[38] = ((byte)(tmp174_169[38] | 0x1));
/*      */     byte[] tmp185_180 = fgLookupTable; tmp185_180[61] = ((byte)(tmp185_180[61] | 0x1));
/*      */     byte[] tmp196_191 = fgLookupTable; tmp196_191[43] = ((byte)(tmp196_191[43] | 0x1));
/*      */     byte[] tmp207_202 = fgLookupTable; tmp207_202[36] = ((byte)(tmp207_202[36] | 0x1));
/*      */     byte[] tmp218_213 = fgLookupTable; tmp218_213[44] = ((byte)(tmp218_213[44] | 0x1));
/*      */     byte[] tmp229_224 = fgLookupTable; tmp229_224[91] = ((byte)(tmp229_224[91] | 0x1));
/*      */     byte[] tmp240_235 = fgLookupTable; tmp240_235[93] = ((byte)(tmp240_235[93] | 0x1));
/*      */     byte[] tmp251_246 = fgLookupTable; tmp251_246[45] = ((byte)(tmp251_246[45] | 0x2));
/*      */     byte[] tmp262_257 = fgLookupTable; tmp262_257[95] = ((byte)(tmp262_257[95] | 0x2));
/*      */     byte[] tmp273_268 = fgLookupTable; tmp273_268[46] = ((byte)(tmp273_268[46] | 0x2));
/*      */     byte[] tmp284_279 = fgLookupTable; tmp284_279[33] = ((byte)(tmp284_279[33] | 0x2));
/*      */     byte[] tmp295_290 = fgLookupTable; tmp295_290[126] = ((byte)(tmp295_290[126] | 0x2));
/*      */     byte[] tmp306_301 = fgLookupTable; tmp306_301[42] = ((byte)(tmp306_301[42] | 0x2));
/*      */     byte[] tmp317_312 = fgLookupTable; tmp317_312[39] = ((byte)(tmp317_312[39] | 0x2));
/*      */     byte[] tmp328_323 = fgLookupTable; tmp328_323[40] = ((byte)(tmp328_323[40] | 0x2));
/*      */     byte[] tmp339_334 = fgLookupTable; tmp339_334[41] = ((byte)(tmp339_334[41] | 0x2));
/*      */     byte[] tmp350_345 = fgLookupTable; tmp350_345[43] = ((byte)(tmp350_345[43] | 0x4));
/*      */     byte[] tmp361_356 = fgLookupTable; tmp361_356[45] = ((byte)(tmp361_356[45] | 0x4));
/*      */     byte[] tmp372_367 = fgLookupTable; tmp372_367[46] = ((byte)(tmp372_367[46] | 0x4));
/*      */     byte[] tmp383_378 = fgLookupTable; tmp383_378[59] = ((byte)(tmp383_378[59] | 0x8));
/*      */     byte[] tmp395_390 = fgLookupTable; tmp395_390[58] = ((byte)(tmp395_390[58] | 0x8));
/*      */     byte[] tmp407_402 = fgLookupTable; tmp407_402[38] = ((byte)(tmp407_402[38] | 0x8));
/*      */     byte[] tmp419_414 = fgLookupTable; tmp419_414[61] = ((byte)(tmp419_414[61] | 0x8));
/*      */     byte[] tmp431_426 = fgLookupTable; tmp431_426[43] = ((byte)(tmp431_426[43] | 0x8));
/*      */     byte[] tmp443_438 = fgLookupTable; tmp443_438[36] = ((byte)(tmp443_438[36] | 0x8));
/*      */     byte[] tmp455_450 = fgLookupTable; tmp455_450[44] = ((byte)(tmp455_450[44] | 0x8));
/*      */     byte[] tmp467_462 = fgLookupTable; tmp467_462[59] = ((byte)(tmp467_462[59] | 0x80));
/*      */     byte[] tmp480_475 = fgLookupTable; tmp480_475[47] = ((byte)(tmp480_475[47] | 0x80));
/*      */     byte[] tmp493_488 = fgLookupTable; tmp493_488[58] = ((byte)(tmp493_488[58] | 0x80));
/*      */     byte[] tmp506_501 = fgLookupTable; tmp506_501[64] = ((byte)(tmp506_501[64] | 0x80));
/*      */     byte[] tmp519_514 = fgLookupTable; tmp519_514[38] = ((byte)(tmp519_514[38] | 0x80));
/*      */     byte[] tmp532_527 = fgLookupTable; tmp532_527[61] = ((byte)(tmp532_527[61] | 0x80));
/*      */     byte[] tmp545_540 = fgLookupTable; tmp545_540[43] = ((byte)(tmp545_540[43] | 0x80));
/*      */     byte[] tmp558_553 = fgLookupTable; tmp558_553[36] = ((byte)(tmp558_553[36] | 0x80));
/*      */     byte[] tmp571_566 = fgLookupTable; tmp571_566[44] = ((byte)(tmp571_566[44] | 0x80));
/*      */   }
/*      */ 
/*      */   public static class MalformedURIException extends IOException
/*      */   {
/*      */     static final long serialVersionUID = -6695054834342951930L;
/*      */ 
/*      */     public MalformedURIException()
/*      */     {
/*      */     }
/*      */ 
/*      */     public MalformedURIException(String p_msg)
/*      */     {
/*   89 */       super();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.URI
 * JD-Core Version:    0.6.2
 */