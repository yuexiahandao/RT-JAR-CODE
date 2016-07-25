/*      */ package com.sun.xml.internal.messaging.saaj.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public class JaxmURI
/*      */   implements Serializable
/*      */ {
/*      */   private static final String RESERVED_CHARACTERS = ";/?:@&=+$,";
/*      */   private static final String MARK_CHARACTERS = "-_.!~*'() ";
/*      */   private static final String SCHEME_CHARACTERS = "+-.";
/*      */   private static final String USERINFO_CHARACTERS = ";:&=+$,";
/*  111 */   private String m_scheme = null;
/*      */ 
/*  114 */   private String m_userinfo = null;
/*      */ 
/*  117 */   private String m_host = null;
/*      */ 
/*  120 */   private int m_port = -1;
/*      */ 
/*  123 */   private String m_path = null;
/*      */ 
/*  127 */   private String m_queryString = null;
/*      */ 
/*  130 */   private String m_fragment = null;
/*      */ 
/*  132 */   private static boolean DEBUG = false;
/*      */ 
/*      */   public JaxmURI()
/*      */   {
/*      */   }
/*      */ 
/*      */   public JaxmURI(JaxmURI p_other)
/*      */   {
/*  147 */     initialize(p_other);
/*      */   }
/*      */ 
/*      */   public JaxmURI(String p_uriSpec)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  166 */     this((JaxmURI)null, p_uriSpec);
/*      */   }
/*      */ 
/*      */   public JaxmURI(JaxmURI p_base, String p_uriSpec)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  182 */     initialize(p_base, p_uriSpec);
/*      */   }
/*      */ 
/*      */   public JaxmURI(String p_scheme, String p_schemeSpecificPart)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  199 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0)) {
/*  200 */       throw new MalformedURIException("Cannot construct URI with null/empty scheme!");
/*      */     }
/*      */ 
/*  203 */     if ((p_schemeSpecificPart == null) || (p_schemeSpecificPart.trim().length() == 0))
/*      */     {
/*  205 */       throw new MalformedURIException("Cannot construct URI with null/empty scheme-specific part!");
/*      */     }
/*      */ 
/*  208 */     setScheme(p_scheme);
/*  209 */     setPath(p_schemeSpecificPart);
/*      */   }
/*      */ 
/*      */   public JaxmURI(String p_scheme, String p_host, String p_path, String p_queryString, String p_fragment)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  236 */     this(p_scheme, null, p_host, -1, p_path, p_queryString, p_fragment);
/*      */   }
/*      */ 
/*      */   public JaxmURI(String p_scheme, String p_userinfo, String p_host, int p_port, String p_path, String p_queryString, String p_fragment)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  268 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0)) {
/*  269 */       throw new MalformedURIException("Scheme is required!");
/*      */     }
/*      */ 
/*  272 */     if (p_host == null) {
/*  273 */       if (p_userinfo != null) {
/*  274 */         throw new MalformedURIException("Userinfo may not be specified if host is not specified!");
/*      */       }
/*      */ 
/*  277 */       if (p_port != -1) {
/*  278 */         throw new MalformedURIException("Port may not be specified if host is not specified!");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  283 */     if (p_path != null) {
/*  284 */       if ((p_path.indexOf('?') != -1) && (p_queryString != null)) {
/*  285 */         throw new MalformedURIException("Query string cannot be specified in path and query string!");
/*      */       }
/*      */ 
/*  289 */       if ((p_path.indexOf('#') != -1) && (p_fragment != null)) {
/*  290 */         throw new MalformedURIException("Fragment cannot be specified in both the path and fragment!");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  295 */     setScheme(p_scheme);
/*  296 */     setHost(p_host);
/*  297 */     setPort(p_port);
/*  298 */     setUserinfo(p_userinfo);
/*  299 */     setPath(p_path);
/*  300 */     setQueryString(p_queryString);
/*  301 */     setFragment(p_fragment);
/*      */   }
/*      */ 
/*      */   private void initialize(JaxmURI p_other)
/*      */   {
/*  310 */     this.m_scheme = p_other.getScheme();
/*  311 */     this.m_userinfo = p_other.getUserinfo();
/*  312 */     this.m_host = p_other.getHost();
/*  313 */     this.m_port = p_other.getPort();
/*  314 */     this.m_path = p_other.getPath();
/*  315 */     this.m_queryString = p_other.getQueryString();
/*  316 */     this.m_fragment = p_other.getFragment();
/*      */   }
/*      */ 
/*      */   private void initialize(JaxmURI p_base, String p_uriSpec)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  337 */     if ((p_base == null) && ((p_uriSpec == null) || (p_uriSpec.trim().length() == 0)))
/*      */     {
/*  339 */       throw new MalformedURIException("Cannot initialize URI with empty parameters.");
/*      */     }
/*      */ 
/*  344 */     if ((p_uriSpec == null) || (p_uriSpec.trim().length() == 0)) {
/*  345 */       initialize(p_base);
/*  346 */       return;
/*      */     }
/*      */ 
/*  349 */     String uriSpec = p_uriSpec.trim();
/*  350 */     int uriSpecLen = uriSpec.length();
/*  351 */     int index = 0;
/*      */ 
/*  355 */     int colonIdx = uriSpec.indexOf(':');
/*  356 */     int slashIdx = uriSpec.indexOf('/');
/*  357 */     if ((colonIdx < 2) || ((colonIdx > slashIdx) && (slashIdx != -1))) {
/*  358 */       int fragmentIdx = uriSpec.indexOf('#');
/*      */ 
/*  360 */       if ((p_base == null) && (fragmentIdx != 0))
/*  361 */         throw new MalformedURIException("No scheme found in URI.");
/*      */     }
/*      */     else
/*      */     {
/*  365 */       initializeScheme(uriSpec);
/*  366 */       index = this.m_scheme.length() + 1;
/*      */     }
/*      */ 
/*  370 */     if ((index + 1 < uriSpecLen) && (uriSpec.substring(index).startsWith("//")))
/*      */     {
/*  372 */       index += 2;
/*  373 */       int startPos = index;
/*      */ 
/*  376 */       char testChar = '\000';
/*  377 */       while (index < uriSpecLen) {
/*  378 */         testChar = uriSpec.charAt(index);
/*  379 */         if ((testChar == '/') || (testChar == '?') || (testChar == '#')) {
/*      */           break;
/*      */         }
/*  382 */         index++;
/*      */       }
/*      */ 
/*  387 */       if (index > startPos) {
/*  388 */         initializeAuthority(uriSpec.substring(startPos, index));
/*      */       }
/*      */       else {
/*  391 */         this.m_host = "";
/*      */       }
/*      */     }
/*      */ 
/*  395 */     initializePath(uriSpec.substring(index));
/*      */ 
/*  402 */     if (p_base != null)
/*      */     {
/*  411 */       if ((this.m_path.length() == 0) && (this.m_scheme == null) && (this.m_host == null))
/*      */       {
/*  413 */         this.m_scheme = p_base.getScheme();
/*  414 */         this.m_userinfo = p_base.getUserinfo();
/*  415 */         this.m_host = p_base.getHost();
/*  416 */         this.m_port = p_base.getPort();
/*  417 */         this.m_path = p_base.getPath();
/*      */ 
/*  419 */         if (this.m_queryString == null) {
/*  420 */           this.m_queryString = p_base.getQueryString();
/*      */         }
/*  422 */         return;
/*      */       }
/*      */ 
/*  427 */       if (this.m_scheme == null) {
/*  428 */         this.m_scheme = p_base.getScheme();
/*      */       }
/*      */       else {
/*  431 */         return;
/*      */       }
/*      */ 
/*  436 */       if (this.m_host == null) {
/*  437 */         this.m_userinfo = p_base.getUserinfo();
/*  438 */         this.m_host = p_base.getHost();
/*  439 */         this.m_port = p_base.getPort();
/*      */       }
/*      */       else {
/*  442 */         return;
/*      */       }
/*      */ 
/*  446 */       if ((this.m_path.length() > 0) && (this.m_path.startsWith("/")))
/*      */       {
/*  448 */         return;
/*      */       }
/*      */ 
/*  453 */       String path = new String();
/*  454 */       String basePath = p_base.getPath();
/*      */ 
/*  457 */       if (basePath != null) {
/*  458 */         int lastSlash = basePath.lastIndexOf('/');
/*  459 */         if (lastSlash != -1) {
/*  460 */           path = basePath.substring(0, lastSlash + 1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  465 */       path = path.concat(this.m_path);
/*      */ 
/*  468 */       index = -1;
/*  469 */       while ((index = path.indexOf("/./")) != -1) {
/*  470 */         path = path.substring(0, index + 1).concat(path.substring(index + 3));
/*      */       }
/*      */ 
/*  474 */       if (path.endsWith("/.")) {
/*  475 */         path = path.substring(0, path.length() - 1);
/*      */       }
/*      */ 
/*  480 */       index = 1;
/*  481 */       int segIndex = -1;
/*  482 */       String tempString = null;
/*      */ 
/*  484 */       while ((index = path.indexOf("/../", index)) > 0) {
/*  485 */         tempString = path.substring(0, path.indexOf("/../"));
/*  486 */         segIndex = tempString.lastIndexOf('/');
/*  487 */         if (segIndex != -1) {
/*  488 */           if (!tempString.substring(segIndex++).equals("..")) {
/*  489 */             path = path.substring(0, segIndex).concat(path.substring(index + 4));
/*      */           }
/*      */           else
/*  492 */             index += 4;
/*      */         }
/*      */         else {
/*  495 */           index += 4;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  500 */       if (path.endsWith("/..")) {
/*  501 */         tempString = path.substring(0, path.length() - 3);
/*  502 */         segIndex = tempString.lastIndexOf('/');
/*  503 */         if (segIndex != -1) {
/*  504 */           path = path.substring(0, segIndex + 1);
/*      */         }
/*      */       }
/*  507 */       this.m_path = path;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initializeScheme(String p_uriSpec)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  521 */     int uriSpecLen = p_uriSpec.length();
/*  522 */     int index = 0;
/*  523 */     String scheme = null;
/*  524 */     char testChar = '\000';
/*      */ 
/*  526 */     while (index < uriSpecLen) {
/*  527 */       testChar = p_uriSpec.charAt(index);
/*  528 */       if ((testChar == ':') || (testChar == '/') || (testChar == '?') || (testChar == '#'))
/*      */       {
/*      */         break;
/*      */       }
/*  532 */       index++;
/*      */     }
/*  534 */     scheme = p_uriSpec.substring(0, index);
/*      */ 
/*  536 */     if (scheme.length() == 0) {
/*  537 */       throw new MalformedURIException("No scheme found in URI.");
/*      */     }
/*      */ 
/*  540 */     setScheme(scheme);
/*      */   }
/*      */ 
/*      */   private void initializeAuthority(String p_uriSpec)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  554 */     int index = 0;
/*  555 */     int start = 0;
/*  556 */     int end = p_uriSpec.length();
/*  557 */     char testChar = '\000';
/*  558 */     String userinfo = null;
/*      */ 
/*  561 */     if (p_uriSpec.indexOf('@', start) != -1) {
/*  562 */       while (index < end) {
/*  563 */         testChar = p_uriSpec.charAt(index);
/*  564 */         if (testChar == '@') {
/*      */           break;
/*      */         }
/*  567 */         index++;
/*      */       }
/*  569 */       userinfo = p_uriSpec.substring(start, index);
/*  570 */       index++;
/*      */     }
/*      */ 
/*  574 */     String host = null;
/*  575 */     start = index;
/*  576 */     while (index < end) {
/*  577 */       testChar = p_uriSpec.charAt(index);
/*  578 */       if (testChar == ':') {
/*      */         break;
/*      */       }
/*  581 */       index++;
/*      */     }
/*  583 */     host = p_uriSpec.substring(start, index);
/*  584 */     int port = -1;
/*  585 */     if (host.length() > 0)
/*      */     {
/*  587 */       if (testChar == ':') {
/*  588 */         index++;
/*  589 */         start = index;
/*  590 */         while (index < end) {
/*  591 */           index++;
/*      */         }
/*  593 */         String portStr = p_uriSpec.substring(start, index);
/*  594 */         if (portStr.length() > 0) {
/*  595 */           for (int i = 0; i < portStr.length(); i++) {
/*  596 */             if (!isDigit(portStr.charAt(i))) {
/*  597 */               throw new MalformedURIException(portStr + " is invalid. Port should only contain digits!");
/*      */             }
/*      */           }
/*      */ 
/*      */           try
/*      */           {
/*  603 */             port = Integer.parseInt(portStr);
/*      */           }
/*      */           catch (NumberFormatException nfe)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  611 */     setHost(host);
/*  612 */     setPort(port);
/*  613 */     setUserinfo(userinfo);
/*      */   }
/*      */ 
/*      */   private void initializePath(String p_uriSpec)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  625 */     if (p_uriSpec == null) {
/*  626 */       throw new MalformedURIException("Cannot initialize path from null string!");
/*      */     }
/*      */ 
/*  630 */     int index = 0;
/*  631 */     int start = 0;
/*  632 */     int end = p_uriSpec.length();
/*  633 */     char testChar = '\000';
/*      */ 
/*  636 */     while (index < end) {
/*  637 */       testChar = p_uriSpec.charAt(index);
/*  638 */       if ((testChar == '?') || (testChar == '#'))
/*      */       {
/*      */         break;
/*      */       }
/*  642 */       if (testChar == '%') {
/*  643 */         if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */         {
/*  646 */           throw new MalformedURIException("Path contains invalid escape sequence!");
/*      */         }
/*      */ 
/*      */       }
/*  650 */       else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */       {
/*  652 */         throw new MalformedURIException("Path contains invalid character: " + testChar);
/*      */       }
/*      */ 
/*  655 */       index++;
/*      */     }
/*  657 */     this.m_path = p_uriSpec.substring(start, index);
/*      */ 
/*  660 */     if (testChar == '?') {
/*  661 */       index++;
/*  662 */       start = index;
/*  663 */       while (index < end) {
/*  664 */         testChar = p_uriSpec.charAt(index);
/*  665 */         if (testChar == '#') {
/*      */           break;
/*      */         }
/*  668 */         if (testChar == '%') {
/*  669 */           if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */           {
/*  672 */             throw new MalformedURIException("Query string contains invalid escape sequence!");
/*      */           }
/*      */ 
/*      */         }
/*  676 */         else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */         {
/*  678 */           throw new MalformedURIException("Query string contains invalid character:" + testChar);
/*      */         }
/*      */ 
/*  681 */         index++;
/*      */       }
/*  683 */       this.m_queryString = p_uriSpec.substring(start, index);
/*      */     }
/*      */ 
/*  687 */     if (testChar == '#') {
/*  688 */       index++;
/*  689 */       start = index;
/*  690 */       while (index < end) {
/*  691 */         testChar = p_uriSpec.charAt(index);
/*      */ 
/*  693 */         if (testChar == '%') {
/*  694 */           if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */           {
/*  697 */             throw new MalformedURIException("Fragment contains invalid escape sequence!");
/*      */           }
/*      */ 
/*      */         }
/*  701 */         else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */         {
/*  703 */           throw new MalformedURIException("Fragment contains invalid character:" + testChar);
/*      */         }
/*      */ 
/*  706 */         index++;
/*      */       }
/*  708 */       this.m_fragment = p_uriSpec.substring(start, index);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getScheme()
/*      */   {
/*  718 */     return this.m_scheme;
/*      */   }
/*      */ 
/*      */   public String getSchemeSpecificPart()
/*      */   {
/*  728 */     StringBuffer schemespec = new StringBuffer();
/*      */ 
/*  730 */     if ((this.m_userinfo != null) || (this.m_host != null) || (this.m_port != -1)) {
/*  731 */       schemespec.append("//");
/*      */     }
/*      */ 
/*  734 */     if (this.m_userinfo != null) {
/*  735 */       schemespec.append(this.m_userinfo);
/*  736 */       schemespec.append('@');
/*      */     }
/*      */ 
/*  739 */     if (this.m_host != null) {
/*  740 */       schemespec.append(this.m_host);
/*      */     }
/*      */ 
/*  743 */     if (this.m_port != -1) {
/*  744 */       schemespec.append(':');
/*  745 */       schemespec.append(this.m_port);
/*      */     }
/*      */ 
/*  748 */     if (this.m_path != null) {
/*  749 */       schemespec.append(this.m_path);
/*      */     }
/*      */ 
/*  752 */     if (this.m_queryString != null) {
/*  753 */       schemespec.append('?');
/*  754 */       schemespec.append(this.m_queryString);
/*      */     }
/*      */ 
/*  757 */     if (this.m_fragment != null) {
/*  758 */       schemespec.append('#');
/*  759 */       schemespec.append(this.m_fragment);
/*      */     }
/*      */ 
/*  762 */     return schemespec.toString();
/*      */   }
/*      */ 
/*      */   public String getUserinfo()
/*      */   {
/*  771 */     return this.m_userinfo;
/*      */   }
/*      */ 
/*      */   public String getHost()
/*      */   {
/*  780 */     return this.m_host;
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/*  789 */     return this.m_port;
/*      */   }
/*      */ 
/*      */   public String getPath(boolean p_includeQueryString, boolean p_includeFragment)
/*      */   {
/*  808 */     StringBuffer pathString = new StringBuffer(this.m_path);
/*      */ 
/*  810 */     if ((p_includeQueryString) && (this.m_queryString != null)) {
/*  811 */       pathString.append('?');
/*  812 */       pathString.append(this.m_queryString);
/*      */     }
/*      */ 
/*  815 */     if ((p_includeFragment) && (this.m_fragment != null)) {
/*  816 */       pathString.append('#');
/*  817 */       pathString.append(this.m_fragment);
/*      */     }
/*  819 */     return pathString.toString();
/*      */   }
/*      */ 
/*      */   public String getPath()
/*      */   {
/*  829 */     return this.m_path;
/*      */   }
/*      */ 
/*      */   public String getQueryString()
/*      */   {
/*  840 */     return this.m_queryString;
/*      */   }
/*      */ 
/*      */   public String getFragment()
/*      */   {
/*  851 */     return this.m_fragment;
/*      */   }
/*      */ 
/*      */   public void setScheme(String p_scheme)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  864 */     if (p_scheme == null) {
/*  865 */       throw new MalformedURIException("Cannot set scheme from null string!");
/*      */     }
/*      */ 
/*  868 */     if (!isConformantSchemeName(p_scheme)) {
/*  869 */       throw new MalformedURIException("The scheme is not conformant.");
/*      */     }
/*      */ 
/*  872 */     this.m_scheme = p_scheme.toLowerCase();
/*      */   }
/*      */ 
/*      */   public void setUserinfo(String p_userinfo)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  885 */     if (p_userinfo == null) {
/*  886 */       this.m_userinfo = null;
/*      */     }
/*      */     else {
/*  889 */       if (this.m_host == null) {
/*  890 */         throw new MalformedURIException("Userinfo cannot be set when host is null!");
/*      */       }
/*      */ 
/*  896 */       int index = 0;
/*  897 */       int end = p_userinfo.length();
/*  898 */       char testChar = '\000';
/*  899 */       while (index < end) {
/*  900 */         testChar = p_userinfo.charAt(index);
/*  901 */         if (testChar == '%') {
/*  902 */           if ((index + 2 >= end) || (!isHex(p_userinfo.charAt(index + 1))) || (!isHex(p_userinfo.charAt(index + 2))))
/*      */           {
/*  905 */             throw new MalformedURIException("Userinfo contains invalid escape sequence!");
/*      */           }
/*      */ 
/*      */         }
/*  909 */         else if ((!isUnreservedCharacter(testChar)) && (";:&=+$,".indexOf(testChar) == -1))
/*      */         {
/*  911 */           throw new MalformedURIException("Userinfo contains invalid character:" + testChar);
/*      */         }
/*      */ 
/*  914 */         index++;
/*      */       }
/*      */     }
/*  917 */     this.m_userinfo = p_userinfo;
/*      */   }
/*      */ 
/*      */   public void setHost(String p_host)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  930 */     if ((p_host == null) || (p_host.trim().length() == 0)) {
/*  931 */       this.m_host = p_host;
/*  932 */       this.m_userinfo = null;
/*  933 */       this.m_port = -1;
/*      */     }
/*  935 */     else if (!isWellFormedAddress(p_host)) {
/*  936 */       throw new MalformedURIException("Host is not a well formed address!");
/*      */     }
/*  938 */     this.m_host = p_host;
/*      */   }
/*      */ 
/*      */   public void setPort(int p_port)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  953 */     if ((p_port >= 0) && (p_port <= 65535)) {
/*  954 */       if (this.m_host == null) {
/*  955 */         throw new MalformedURIException("Port cannot be set when host is null!");
/*      */       }
/*      */ 
/*      */     }
/*  959 */     else if (p_port != -1) {
/*  960 */       throw new MalformedURIException("Invalid port number!");
/*      */     }
/*  962 */     this.m_port = p_port;
/*      */   }
/*      */ 
/*      */   public void setPath(String p_path)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/*  980 */     if (p_path == null) {
/*  981 */       this.m_path = null;
/*  982 */       this.m_queryString = null;
/*  983 */       this.m_fragment = null;
/*      */     }
/*      */     else {
/*  986 */       initializePath(p_path);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void appendPath(String p_addToPath)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/* 1005 */     if ((p_addToPath == null) || (p_addToPath.trim().length() == 0)) {
/* 1006 */       return;
/*      */     }
/*      */ 
/* 1009 */     if (!isURIString(p_addToPath)) {
/* 1010 */       throw new MalformedURIException("Path contains invalid character!");
/*      */     }
/*      */ 
/* 1014 */     if ((this.m_path == null) || (this.m_path.trim().length() == 0)) {
/* 1015 */       if (p_addToPath.startsWith("/")) {
/* 1016 */         this.m_path = p_addToPath;
/*      */       }
/*      */       else {
/* 1019 */         this.m_path = ("/" + p_addToPath);
/*      */       }
/*      */     }
/* 1022 */     else if (this.m_path.endsWith("/")) {
/* 1023 */       if (p_addToPath.startsWith("/")) {
/* 1024 */         this.m_path = this.m_path.concat(p_addToPath.substring(1));
/*      */       }
/*      */       else {
/* 1027 */         this.m_path = this.m_path.concat(p_addToPath);
/*      */       }
/*      */ 
/*      */     }
/* 1031 */     else if (p_addToPath.startsWith("/")) {
/* 1032 */       this.m_path = this.m_path.concat(p_addToPath);
/*      */     }
/*      */     else
/* 1035 */       this.m_path = this.m_path.concat("/" + p_addToPath);
/*      */   }
/*      */ 
/*      */   public void setQueryString(String p_queryString)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/* 1052 */     if (p_queryString == null) {
/* 1053 */       this.m_queryString = null;
/*      */     } else {
/* 1055 */       if (!isGenericURI()) {
/* 1056 */         throw new MalformedURIException("Query string can only be set for a generic URI!");
/*      */       }
/*      */ 
/* 1059 */       if (getPath() == null) {
/* 1060 */         throw new MalformedURIException("Query string cannot be set when path is null!");
/*      */       }
/*      */ 
/* 1063 */       if (!isURIString(p_queryString)) {
/* 1064 */         throw new MalformedURIException("Query string contains invalid character!");
/*      */       }
/*      */ 
/* 1068 */       this.m_queryString = p_queryString;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFragment(String p_fragment)
/*      */     throws JaxmURI.MalformedURIException
/*      */   {
/* 1084 */     if (p_fragment == null) {
/* 1085 */       this.m_fragment = null;
/*      */     } else {
/* 1087 */       if (!isGenericURI()) {
/* 1088 */         throw new MalformedURIException("Fragment can only be set for a generic URI!");
/*      */       }
/*      */ 
/* 1091 */       if (getPath() == null) {
/* 1092 */         throw new MalformedURIException("Fragment cannot be set when path is null!");
/*      */       }
/*      */ 
/* 1095 */       if (!isURIString(p_fragment)) {
/* 1096 */         throw new MalformedURIException("Fragment contains invalid character!");
/*      */       }
/*      */ 
/* 1100 */       this.m_fragment = p_fragment;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equals(Object p_test)
/*      */   {
/* 1113 */     if ((p_test instanceof JaxmURI)) {
/* 1114 */       JaxmURI testURI = (JaxmURI)p_test;
/* 1115 */       if (((this.m_scheme == null) && (testURI.m_scheme == null)) || ((this.m_scheme != null) && (testURI.m_scheme != null) && (this.m_scheme.equals(testURI.m_scheme)) && (((this.m_userinfo == null) && (testURI.m_userinfo == null)) || ((this.m_userinfo != null) && (testURI.m_userinfo != null) && (this.m_userinfo.equals(testURI.m_userinfo)) && (((this.m_host == null) && (testURI.m_host == null)) || ((this.m_host != null) && (testURI.m_host != null) && (this.m_host.equals(testURI.m_host)) && (this.m_port == testURI.m_port) && (((this.m_path == null) && (testURI.m_path == null)) || ((this.m_path != null) && (testURI.m_path != null) && (this.m_path.equals(testURI.m_path)) && (((this.m_queryString == null) && (testURI.m_queryString == null)) || ((this.m_queryString != null) && (testURI.m_queryString != null) && (this.m_queryString.equals(testURI.m_queryString)) && (((this.m_fragment == null) && (testURI.m_fragment == null)) || ((this.m_fragment != null) && (testURI.m_fragment != null) && (this.m_fragment.equals(testURI.m_fragment))))))))))))))
/*      */       {
/* 1134 */         return true;
/*      */       }
/*      */     }
/* 1137 */     return false;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1146 */     StringBuffer uriSpecString = new StringBuffer();
/*      */ 
/* 1148 */     if (this.m_scheme != null) {
/* 1149 */       uriSpecString.append(this.m_scheme);
/* 1150 */       uriSpecString.append(':');
/*      */     }
/* 1152 */     uriSpecString.append(getSchemeSpecificPart());
/* 1153 */     return uriSpecString.toString();
/*      */   }
/*      */ 
/*      */   public boolean isGenericURI()
/*      */   {
/* 1166 */     return this.m_host != null;
/*      */   }
/*      */ 
/*      */   public static boolean isConformantSchemeName(String p_scheme)
/*      */   {
/* 1177 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0)) {
/* 1178 */       return false;
/*      */     }
/*      */ 
/* 1181 */     if (!isAlpha(p_scheme.charAt(0))) {
/* 1182 */       return false;
/*      */     }
/*      */ 
/* 1186 */     for (int i = 1; i < p_scheme.length(); i++) {
/* 1187 */       char testChar = p_scheme.charAt(i);
/* 1188 */       if ((!isAlphanum(testChar)) && ("+-.".indexOf(testChar) == -1))
/*      */       {
/* 1190 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1194 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isWellFormedAddress(String p_address)
/*      */   {
/* 1209 */     if (p_address == null) {
/* 1210 */       return false;
/*      */     }
/*      */ 
/* 1213 */     String address = p_address.trim();
/* 1214 */     int addrLength = address.length();
/* 1215 */     if ((addrLength == 0) || (addrLength > 255)) {
/* 1216 */       return false;
/*      */     }
/*      */ 
/* 1219 */     if ((address.startsWith(".")) || (address.startsWith("-"))) {
/* 1220 */       return false;
/*      */     }
/*      */ 
/* 1226 */     int index = address.lastIndexOf('.');
/* 1227 */     if (address.endsWith(".")) {
/* 1228 */       index = address.substring(0, index).lastIndexOf('.');
/*      */     }
/*      */ 
/* 1231 */     if ((index + 1 < addrLength) && (isDigit(p_address.charAt(index + 1))))
/*      */     {
/* 1233 */       int numDots = 0;
/*      */ 
/* 1238 */       for (int i = 0; i < addrLength; i++) {
/* 1239 */         char testChar = address.charAt(i);
/* 1240 */         if (testChar == '.') {
/* 1241 */           if ((!isDigit(address.charAt(i - 1))) || ((i + 1 < addrLength) && (!isDigit(address.charAt(i + 1)))))
/*      */           {
/* 1243 */             return false;
/*      */           }
/* 1245 */           numDots++;
/*      */         }
/* 1247 */         else if (!isDigit(testChar)) {
/* 1248 */           return false;
/*      */         }
/*      */       }
/* 1251 */       if (numDots != 3) {
/* 1252 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1260 */       for (int i = 0; i < addrLength; i++) {
/* 1261 */         char testChar = address.charAt(i);
/* 1262 */         if (testChar == '.') {
/* 1263 */           if (!isAlphanum(address.charAt(i - 1))) {
/* 1264 */             return false;
/*      */           }
/* 1266 */           if ((i + 1 < addrLength) && (!isAlphanum(address.charAt(i + 1)))) {
/* 1267 */             return false;
/*      */           }
/*      */         }
/* 1270 */         else if ((!isAlphanum(testChar)) && (testChar != '-')) {
/* 1271 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 1275 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean isDigit(char p_char)
/*      */   {
/* 1285 */     return (p_char >= '0') && (p_char <= '9');
/*      */   }
/*      */ 
/*      */   private static boolean isHex(char p_char)
/*      */   {
/* 1295 */     return (isDigit(p_char)) || ((p_char >= 'a') && (p_char <= 'f')) || ((p_char >= 'A') && (p_char <= 'F'));
/*      */   }
/*      */ 
/*      */   private static boolean isAlpha(char p_char)
/*      */   {
/* 1306 */     return ((p_char >= 'a') && (p_char <= 'z')) || ((p_char >= 'A') && (p_char <= 'Z'));
/*      */   }
/*      */ 
/*      */   private static boolean isAlphanum(char p_char)
/*      */   {
/* 1316 */     return (isAlpha(p_char)) || (isDigit(p_char));
/*      */   }
/*      */ 
/*      */   private static boolean isReservedCharacter(char p_char)
/*      */   {
/* 1326 */     return ";/?:@&=+$,".indexOf(p_char) != -1;
/*      */   }
/*      */ 
/*      */   private static boolean isUnreservedCharacter(char p_char)
/*      */   {
/* 1335 */     return (isAlphanum(p_char)) || ("-_.!~*'() ".indexOf(p_char) != -1);
/*      */   }
/*      */ 
/*      */   private static boolean isURIString(String p_uric)
/*      */   {
/* 1347 */     if (p_uric == null) {
/* 1348 */       return false;
/*      */     }
/* 1350 */     int end = p_uric.length();
/* 1351 */     char testChar = '\000';
/* 1352 */     for (int i = 0; i < end; i++) {
/* 1353 */       testChar = p_uric.charAt(i);
/* 1354 */       if (testChar == '%') {
/* 1355 */         if ((i + 2 >= end) || (!isHex(p_uric.charAt(i + 1))) || (!isHex(p_uric.charAt(i + 2))))
/*      */         {
/* 1358 */           return false;
/*      */         }
/*      */ 
/* 1361 */         i += 2;
/*      */       }
/* 1365 */       else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */       {
/* 1370 */         return false;
/*      */       }
/*      */     }
/* 1373 */     return true;
/*      */   }
/*      */ 
/*      */   public static class MalformedURIException extends IOException
/*      */   {
/*      */     public MalformedURIException()
/*      */     {
/*      */     }
/*      */ 
/*      */     public MalformedURIException(String p_msg)
/*      */     {
/*   92 */       super();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.JaxmURI
 * JD-Core Version:    0.6.2
 */