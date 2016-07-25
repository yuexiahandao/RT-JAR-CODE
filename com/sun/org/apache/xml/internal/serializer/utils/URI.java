/*      */ package com.sun.org.apache.xml.internal.serializer.utils;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.utils.Objects;
/*      */ import java.io.IOException;
/*      */ 
/*      */ final class URI
/*      */ {
/*      */   private static final String RESERVED_CHARACTERS = ";/?:@&=+$,";
/*      */   private static final String MARK_CHARACTERS = "-_.!~*'() ";
/*      */   private static final String SCHEME_CHARACTERS = "+-.";
/*      */   private static final String USERINFO_CHARACTERS = ";:&=+$,";
/*  119 */   private String m_scheme = null;
/*      */ 
/*  123 */   private String m_userinfo = null;
/*      */ 
/*  127 */   private String m_host = null;
/*      */ 
/*  131 */   private int m_port = -1;
/*      */ 
/*  135 */   private String m_path = null;
/*      */ 
/*  142 */   private String m_queryString = null;
/*      */ 
/*  146 */   private String m_fragment = null;
/*      */ 
/*  149 */   private static boolean DEBUG = false;
/*      */ 
/*      */   public URI()
/*      */   {
/*      */   }
/*      */ 
/*      */   public URI(URI p_other)
/*      */   {
/*  164 */     initialize(p_other);
/*      */   }
/*      */ 
/*      */   public URI(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  184 */     this((URI)null, p_uriSpec);
/*      */   }
/*      */ 
/*      */   public URI(URI p_base, String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  201 */     initialize(p_base, p_uriSpec);
/*      */   }
/*      */ 
/*      */   public URI(String p_scheme, String p_schemeSpecificPart)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  220 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0))
/*      */     {
/*  222 */       throw new MalformedURIException("Cannot construct URI with null/empty scheme!");
/*      */     }
/*      */ 
/*  226 */     if ((p_schemeSpecificPart == null) || (p_schemeSpecificPart.trim().length() == 0))
/*      */     {
/*  229 */       throw new MalformedURIException("Cannot construct URI with null/empty scheme-specific part!");
/*      */     }
/*      */ 
/*  233 */     setScheme(p_scheme);
/*  234 */     setPath(p_schemeSpecificPart);
/*      */   }
/*      */ 
/*      */   public URI(String p_scheme, String p_host, String p_path, String p_queryString, String p_fragment)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  261 */     this(p_scheme, null, p_host, -1, p_path, p_queryString, p_fragment);
/*      */   }
/*      */ 
/*      */   public URI(String p_scheme, String p_userinfo, String p_host, int p_port, String p_path, String p_queryString, String p_fragment)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  293 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0))
/*      */     {
/*  295 */       throw new MalformedURIException(Utils.messages.createMessage("ER_SCHEME_REQUIRED", null));
/*      */     }
/*      */ 
/*  298 */     if (p_host == null)
/*      */     {
/*  300 */       if (p_userinfo != null)
/*      */       {
/*  302 */         throw new MalformedURIException(Utils.messages.createMessage("ER_NO_USERINFO_IF_NO_HOST", null));
/*      */       }
/*      */ 
/*  306 */       if (p_port != -1)
/*      */       {
/*  308 */         throw new MalformedURIException(Utils.messages.createMessage("ER_NO_PORT_IF_NO_HOST", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  313 */     if (p_path != null)
/*      */     {
/*  315 */       if ((p_path.indexOf('?') != -1) && (p_queryString != null))
/*      */       {
/*  317 */         throw new MalformedURIException(Utils.messages.createMessage("ER_NO_QUERY_STRING_IN_PATH", null));
/*      */       }
/*      */ 
/*  321 */       if ((p_path.indexOf('#') != -1) && (p_fragment != null))
/*      */       {
/*  323 */         throw new MalformedURIException(Utils.messages.createMessage("ER_NO_FRAGMENT_STRING_IN_PATH", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  328 */     setScheme(p_scheme);
/*  329 */     setHost(p_host);
/*  330 */     setPort(p_port);
/*  331 */     setUserinfo(p_userinfo);
/*  332 */     setPath(p_path);
/*  333 */     setQueryString(p_queryString);
/*  334 */     setFragment(p_fragment);
/*      */   }
/*      */ 
/*      */   private void initialize(URI p_other)
/*      */   {
/*  345 */     this.m_scheme = p_other.getScheme();
/*  346 */     this.m_userinfo = p_other.getUserinfo();
/*  347 */     this.m_host = p_other.getHost();
/*  348 */     this.m_port = p_other.getPort();
/*  349 */     this.m_path = p_other.getPath();
/*  350 */     this.m_queryString = p_other.getQueryString();
/*  351 */     this.m_fragment = p_other.getFragment();
/*      */   }
/*      */ 
/*      */   private void initialize(URI p_base, String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  374 */     if ((p_base == null) && ((p_uriSpec == null) || (p_uriSpec.trim().length() == 0)))
/*      */     {
/*  377 */       throw new MalformedURIException(Utils.messages.createMessage("ER_CANNOT_INIT_URI_EMPTY_PARMS", null));
/*      */     }
/*      */ 
/*  382 */     if ((p_uriSpec == null) || (p_uriSpec.trim().length() == 0))
/*      */     {
/*  384 */       initialize(p_base);
/*      */ 
/*  386 */       return;
/*      */     }
/*      */ 
/*  389 */     String uriSpec = p_uriSpec.trim();
/*  390 */     int uriSpecLen = uriSpec.length();
/*  391 */     int index = 0;
/*      */ 
/*  394 */     int colonIndex = uriSpec.indexOf(':');
/*  395 */     if (colonIndex < 0)
/*      */     {
/*  397 */       if (p_base == null)
/*      */       {
/*  399 */         throw new MalformedURIException(Utils.messages.createMessage("ER_NO_SCHEME_IN_URI", new Object[] { uriSpec }));
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  404 */       initializeScheme(uriSpec);
/*  405 */       uriSpec = uriSpec.substring(colonIndex + 1);
/*  406 */       uriSpecLen = uriSpec.length();
/*      */     }
/*      */ 
/*  410 */     if ((index + 1 < uriSpecLen) && (uriSpec.substring(index).startsWith("//")))
/*      */     {
/*  413 */       index += 2;
/*      */ 
/*  415 */       int startPos = index;
/*      */ 
/*  418 */       char testChar = '\000';
/*      */ 
/*  420 */       while (index < uriSpecLen)
/*      */       {
/*  422 */         testChar = uriSpec.charAt(index);
/*      */ 
/*  424 */         if ((testChar == '/') || (testChar == '?') || (testChar == '#'))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  429 */         index++;
/*      */       }
/*      */ 
/*  434 */       if (index > startPos)
/*      */       {
/*  436 */         initializeAuthority(uriSpec.substring(startPos, index));
/*      */       }
/*      */       else
/*      */       {
/*  440 */         this.m_host = "";
/*      */       }
/*      */     }
/*      */ 
/*  444 */     initializePath(uriSpec.substring(index));
/*      */ 
/*  451 */     if (p_base != null)
/*      */     {
/*  461 */       if ((this.m_path.length() == 0) && (this.m_scheme == null) && (this.m_host == null))
/*      */       {
/*  463 */         this.m_scheme = p_base.getScheme();
/*  464 */         this.m_userinfo = p_base.getUserinfo();
/*  465 */         this.m_host = p_base.getHost();
/*  466 */         this.m_port = p_base.getPort();
/*  467 */         this.m_path = p_base.getPath();
/*      */ 
/*  469 */         if (this.m_queryString == null)
/*      */         {
/*  471 */           this.m_queryString = p_base.getQueryString();
/*      */         }
/*      */ 
/*  474 */         return;
/*      */       }
/*      */ 
/*  479 */       if (this.m_scheme == null)
/*      */       {
/*  481 */         this.m_scheme = p_base.getScheme();
/*      */       }
/*      */ 
/*  486 */       if (this.m_host == null)
/*      */       {
/*  488 */         this.m_userinfo = p_base.getUserinfo();
/*  489 */         this.m_host = p_base.getHost();
/*  490 */         this.m_port = p_base.getPort();
/*      */       }
/*      */       else
/*      */       {
/*  494 */         return;
/*      */       }
/*      */ 
/*  498 */       if ((this.m_path.length() > 0) && (this.m_path.startsWith("/")))
/*      */       {
/*  500 */         return;
/*      */       }
/*      */ 
/*  505 */       String path = "";
/*  506 */       String basePath = p_base.getPath();
/*      */ 
/*  509 */       if (basePath != null)
/*      */       {
/*  511 */         int lastSlash = basePath.lastIndexOf('/');
/*      */ 
/*  513 */         if (lastSlash != -1)
/*      */         {
/*  515 */           path = basePath.substring(0, lastSlash + 1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  520 */       path = path.concat(this.m_path);
/*      */ 
/*  523 */       index = -1;
/*      */ 
/*  525 */       while ((index = path.indexOf("/./")) != -1)
/*      */       {
/*  527 */         path = path.substring(0, index + 1).concat(path.substring(index + 3));
/*      */       }
/*      */ 
/*  531 */       if (path.endsWith("/."))
/*      */       {
/*  533 */         path = path.substring(0, path.length() - 1);
/*      */       }
/*      */ 
/*  538 */       index = -1;
/*      */ 
/*  540 */       int segIndex = -1;
/*  541 */       String tempString = null;
/*      */ 
/*  543 */       while ((index = path.indexOf("/../")) > 0)
/*      */       {
/*  545 */         tempString = path.substring(0, path.indexOf("/../"));
/*  546 */         segIndex = tempString.lastIndexOf('/');
/*      */ 
/*  548 */         if ((segIndex != -1) && 
/*  550 */           (!tempString.substring(segIndex++).equals("..")))
/*      */         {
/*  552 */           path = path.substring(0, segIndex).concat(path.substring(index + 4));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  560 */       if (path.endsWith("/.."))
/*      */       {
/*  562 */         tempString = path.substring(0, path.length() - 3);
/*  563 */         segIndex = tempString.lastIndexOf('/');
/*      */ 
/*  565 */         if (segIndex != -1)
/*      */         {
/*  567 */           path = path.substring(0, segIndex + 1);
/*      */         }
/*      */       }
/*      */ 
/*  571 */       this.m_path = path;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initializeScheme(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  586 */     int uriSpecLen = p_uriSpec.length();
/*  587 */     int index = 0;
/*  588 */     String scheme = null;
/*  589 */     char testChar = '\000';
/*      */ 
/*  591 */     while (index < uriSpecLen)
/*      */     {
/*  593 */       testChar = p_uriSpec.charAt(index);
/*      */ 
/*  595 */       if ((testChar == ':') || (testChar == '/') || (testChar == '?') || (testChar == '#'))
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  601 */       index++;
/*      */     }
/*      */ 
/*  604 */     scheme = p_uriSpec.substring(0, index);
/*      */ 
/*  606 */     if (scheme.length() == 0)
/*      */     {
/*  608 */       throw new MalformedURIException(Utils.messages.createMessage("ER_NO_SCHEME_INURI", null));
/*      */     }
/*      */ 
/*  612 */     setScheme(scheme);
/*      */   }
/*      */ 
/*      */   private void initializeAuthority(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  628 */     int index = 0;
/*  629 */     int start = 0;
/*  630 */     int end = p_uriSpec.length();
/*  631 */     char testChar = '\000';
/*  632 */     String userinfo = null;
/*      */ 
/*  635 */     if (p_uriSpec.indexOf('@', start) != -1)
/*      */     {
/*  637 */       while (index < end)
/*      */       {
/*  639 */         testChar = p_uriSpec.charAt(index);
/*      */ 
/*  641 */         if (testChar == '@')
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  646 */         index++;
/*      */       }
/*      */ 
/*  649 */       userinfo = p_uriSpec.substring(start, index);
/*      */ 
/*  651 */       index++;
/*      */     }
/*      */ 
/*  655 */     String host = null;
/*      */ 
/*  657 */     start = index;
/*      */ 
/*  659 */     while (index < end)
/*      */     {
/*  661 */       testChar = p_uriSpec.charAt(index);
/*      */ 
/*  663 */       if (testChar == ':')
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  668 */       index++;
/*      */     }
/*      */ 
/*  671 */     host = p_uriSpec.substring(start, index);
/*      */ 
/*  673 */     int port = -1;
/*      */ 
/*  675 */     if (host.length() > 0)
/*      */     {
/*  679 */       if (testChar == ':')
/*      */       {
/*  681 */         index++;
/*      */ 
/*  683 */         start = index;
/*      */ 
/*  685 */         while (index < end)
/*      */         {
/*  687 */           index++;
/*      */         }
/*      */ 
/*  690 */         String portStr = p_uriSpec.substring(start, index);
/*      */ 
/*  692 */         if (portStr.length() > 0)
/*      */         {
/*  694 */           for (int i = 0; i < portStr.length(); i++)
/*      */           {
/*  696 */             if (!isDigit(portStr.charAt(i)))
/*      */             {
/*  698 */               throw new MalformedURIException(portStr + " is invalid. Port should only contain digits!");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */           try
/*      */           {
/*  705 */             port = Integer.parseInt(portStr);
/*      */           }
/*      */           catch (NumberFormatException nfe)
/*      */           {
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  716 */     setHost(host);
/*  717 */     setPort(port);
/*  718 */     setUserinfo(userinfo);
/*      */   }
/*      */ 
/*      */   private void initializePath(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  731 */     if (p_uriSpec == null)
/*      */     {
/*  733 */       throw new MalformedURIException("Cannot initialize path from null string!");
/*      */     }
/*      */ 
/*  737 */     int index = 0;
/*  738 */     int start = 0;
/*  739 */     int end = p_uriSpec.length();
/*  740 */     char testChar = '\000';
/*      */ 
/*  743 */     while (index < end)
/*      */     {
/*  745 */       testChar = p_uriSpec.charAt(index);
/*      */ 
/*  747 */       if ((testChar == '?') || (testChar == '#'))
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  753 */       if (testChar == '%')
/*      */       {
/*  755 */         if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */         {
/*  758 */           throw new MalformedURIException(Utils.messages.createMessage("ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", null));
/*      */         }
/*      */ 
/*      */       }
/*  762 */       else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */       {
/*  765 */         if ('\\' != testChar) {
/*  766 */           throw new MalformedURIException(Utils.messages.createMessage("ER_PATH_INVALID_CHAR", new Object[] { String.valueOf(testChar) }));
/*      */         }
/*      */       }
/*      */ 
/*  770 */       index++;
/*      */     }
/*      */ 
/*  773 */     this.m_path = p_uriSpec.substring(start, index);
/*      */ 
/*  776 */     if (testChar == '?')
/*      */     {
/*  778 */       index++;
/*      */ 
/*  780 */       start = index;
/*      */ 
/*  782 */       while (index < end)
/*      */       {
/*  784 */         testChar = p_uriSpec.charAt(index);
/*      */ 
/*  786 */         if (testChar == '#')
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  791 */         if (testChar == '%')
/*      */         {
/*  793 */           if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */           {
/*  796 */             throw new MalformedURIException("Query string contains invalid escape sequence!");
/*      */           }
/*      */ 
/*      */         }
/*  800 */         else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */         {
/*  803 */           throw new MalformedURIException("Query string contains invalid character:" + testChar);
/*      */         }
/*      */ 
/*  807 */         index++;
/*      */       }
/*      */ 
/*  810 */       this.m_queryString = p_uriSpec.substring(start, index);
/*      */     }
/*      */ 
/*  814 */     if (testChar == '#')
/*      */     {
/*  816 */       index++;
/*      */ 
/*  818 */       start = index;
/*      */ 
/*  820 */       while (index < end)
/*      */       {
/*  822 */         testChar = p_uriSpec.charAt(index);
/*      */ 
/*  824 */         if (testChar == '%')
/*      */         {
/*  826 */           if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */           {
/*  829 */             throw new MalformedURIException("Fragment contains invalid escape sequence!");
/*      */           }
/*      */ 
/*      */         }
/*  833 */         else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */         {
/*  836 */           throw new MalformedURIException("Fragment contains invalid character:" + testChar);
/*      */         }
/*      */ 
/*  840 */         index++;
/*      */       }
/*      */ 
/*  843 */       this.m_fragment = p_uriSpec.substring(start, index);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getScheme()
/*      */   {
/*  854 */     return this.m_scheme;
/*      */   }
/*      */ 
/*      */   public String getSchemeSpecificPart()
/*      */   {
/*  866 */     StringBuilder schemespec = new StringBuilder();
/*      */ 
/*  868 */     if ((this.m_userinfo != null) || (this.m_host != null) || (this.m_port != -1))
/*      */     {
/*  870 */       schemespec.append("//");
/*      */     }
/*      */ 
/*  873 */     if (this.m_userinfo != null)
/*      */     {
/*  875 */       schemespec.append(this.m_userinfo);
/*  876 */       schemespec.append('@');
/*      */     }
/*      */ 
/*  879 */     if (this.m_host != null)
/*      */     {
/*  881 */       schemespec.append(this.m_host);
/*      */     }
/*      */ 
/*  884 */     if (this.m_port != -1)
/*      */     {
/*  886 */       schemespec.append(':');
/*  887 */       schemespec.append(this.m_port);
/*      */     }
/*      */ 
/*  890 */     if (this.m_path != null)
/*      */     {
/*  892 */       schemespec.append(this.m_path);
/*      */     }
/*      */ 
/*  895 */     if (this.m_queryString != null)
/*      */     {
/*  897 */       schemespec.append('?');
/*  898 */       schemespec.append(this.m_queryString);
/*      */     }
/*      */ 
/*  901 */     if (this.m_fragment != null)
/*      */     {
/*  903 */       schemespec.append('#');
/*  904 */       schemespec.append(this.m_fragment);
/*      */     }
/*      */ 
/*  907 */     return schemespec.toString();
/*      */   }
/*      */ 
/*      */   public String getUserinfo()
/*      */   {
/*  917 */     return this.m_userinfo;
/*      */   }
/*      */ 
/*      */   public String getHost()
/*      */   {
/*  927 */     return this.m_host;
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/*  937 */     return this.m_port;
/*      */   }
/*      */ 
/*      */   public String getPath(boolean p_includeQueryString, boolean p_includeFragment)
/*      */   {
/*  958 */     StringBuilder pathString = new StringBuilder(this.m_path);
/*      */ 
/*  960 */     if ((p_includeQueryString) && (this.m_queryString != null))
/*      */     {
/*  962 */       pathString.append('?');
/*  963 */       pathString.append(this.m_queryString);
/*      */     }
/*      */ 
/*  966 */     if ((p_includeFragment) && (this.m_fragment != null))
/*      */     {
/*  968 */       pathString.append('#');
/*  969 */       pathString.append(this.m_fragment);
/*      */     }
/*      */ 
/*  972 */     return pathString.toString();
/*      */   }
/*      */ 
/*      */   public String getPath()
/*      */   {
/*  983 */     return this.m_path;
/*      */   }
/*      */ 
/*      */   public String getQueryString()
/*      */   {
/*  995 */     return this.m_queryString;
/*      */   }
/*      */ 
/*      */   public String getFragment()
/*      */   {
/* 1007 */     return this.m_fragment;
/*      */   }
/*      */ 
/*      */   public void setScheme(String p_scheme)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1022 */     if (p_scheme == null)
/*      */     {
/* 1024 */       throw new MalformedURIException(Utils.messages.createMessage("ER_SCHEME_FROM_NULL_STRING", null));
/*      */     }
/*      */ 
/* 1027 */     if (!isConformantSchemeName(p_scheme))
/*      */     {
/* 1029 */       throw new MalformedURIException(Utils.messages.createMessage("ER_SCHEME_NOT_CONFORMANT", null));
/*      */     }
/*      */ 
/* 1032 */     this.m_scheme = p_scheme.toLowerCase();
/*      */   }
/*      */ 
/*      */   public void setUserinfo(String p_userinfo)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1047 */     if (p_userinfo == null)
/*      */     {
/* 1049 */       this.m_userinfo = null;
/*      */     }
/*      */     else
/*      */     {
/* 1053 */       if (this.m_host == null)
/*      */       {
/* 1055 */         throw new MalformedURIException("Userinfo cannot be set when host is null!");
/*      */       }
/*      */ 
/* 1061 */       int index = 0;
/* 1062 */       int end = p_userinfo.length();
/* 1063 */       char testChar = '\000';
/*      */ 
/* 1065 */       while (index < end)
/*      */       {
/* 1067 */         testChar = p_userinfo.charAt(index);
/*      */ 
/* 1069 */         if (testChar == '%')
/*      */         {
/* 1071 */           if ((index + 2 >= end) || (!isHex(p_userinfo.charAt(index + 1))) || (!isHex(p_userinfo.charAt(index + 2))))
/*      */           {
/* 1074 */             throw new MalformedURIException("Userinfo contains invalid escape sequence!");
/*      */           }
/*      */ 
/*      */         }
/* 1078 */         else if ((!isUnreservedCharacter(testChar)) && (";:&=+$,".indexOf(testChar) == -1))
/*      */         {
/* 1081 */           throw new MalformedURIException("Userinfo contains invalid character:" + testChar);
/*      */         }
/*      */ 
/* 1085 */         index++;
/*      */       }
/*      */     }
/*      */ 
/* 1089 */     this.m_userinfo = p_userinfo;
/*      */   }
/*      */ 
/*      */   public void setHost(String p_host)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1104 */     if ((p_host == null) || (p_host.trim().length() == 0))
/*      */     {
/* 1106 */       this.m_host = p_host;
/* 1107 */       this.m_userinfo = null;
/* 1108 */       this.m_port = -1;
/*      */     }
/* 1110 */     else if (!isWellFormedAddress(p_host))
/*      */     {
/* 1112 */       throw new MalformedURIException(Utils.messages.createMessage("ER_HOST_ADDRESS_NOT_WELLFORMED", null));
/*      */     }
/*      */ 
/* 1115 */     this.m_host = p_host;
/*      */   }
/*      */ 
/*      */   public void setPort(int p_port)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1132 */     if ((p_port >= 0) && (p_port <= 65535))
/*      */     {
/* 1134 */       if (this.m_host == null)
/*      */       {
/* 1136 */         throw new MalformedURIException(Utils.messages.createMessage("ER_PORT_WHEN_HOST_NULL", null));
/*      */       }
/*      */ 
/*      */     }
/* 1140 */     else if (p_port != -1)
/*      */     {
/* 1142 */       throw new MalformedURIException(Utils.messages.createMessage("ER_INVALID_PORT", null));
/*      */     }
/*      */ 
/* 1145 */     this.m_port = p_port;
/*      */   }
/*      */ 
/*      */   public void setPath(String p_path)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1165 */     if (p_path == null)
/*      */     {
/* 1167 */       this.m_path = null;
/* 1168 */       this.m_queryString = null;
/* 1169 */       this.m_fragment = null;
/*      */     }
/*      */     else
/*      */     {
/* 1173 */       initializePath(p_path);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void appendPath(String p_addToPath)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1193 */     if ((p_addToPath == null) || (p_addToPath.trim().length() == 0))
/*      */     {
/* 1195 */       return;
/*      */     }
/*      */ 
/* 1198 */     if (!isURIString(p_addToPath))
/*      */     {
/* 1200 */       throw new MalformedURIException(Utils.messages.createMessage("ER_PATH_INVALID_CHAR", new Object[] { p_addToPath }));
/*      */     }
/*      */ 
/* 1203 */     if ((this.m_path == null) || (this.m_path.trim().length() == 0))
/*      */     {
/* 1205 */       if (p_addToPath.startsWith("/"))
/*      */       {
/* 1207 */         this.m_path = p_addToPath;
/*      */       }
/*      */       else
/*      */       {
/* 1211 */         this.m_path = ("/" + p_addToPath);
/*      */       }
/*      */     }
/* 1214 */     else if (this.m_path.endsWith("/"))
/*      */     {
/* 1216 */       if (p_addToPath.startsWith("/"))
/*      */       {
/* 1218 */         this.m_path = this.m_path.concat(p_addToPath.substring(1));
/*      */       }
/*      */       else
/*      */       {
/* 1222 */         this.m_path = this.m_path.concat(p_addToPath);
/*      */       }
/*      */ 
/*      */     }
/* 1227 */     else if (p_addToPath.startsWith("/"))
/*      */     {
/* 1229 */       this.m_path = this.m_path.concat(p_addToPath);
/*      */     }
/*      */     else
/*      */     {
/* 1233 */       this.m_path = this.m_path.concat("/" + p_addToPath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setQueryString(String p_queryString)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1253 */     if (p_queryString == null)
/*      */     {
/* 1255 */       this.m_queryString = null;
/*      */     } else {
/* 1257 */       if (!isGenericURI())
/*      */       {
/* 1259 */         throw new MalformedURIException("Query string can only be set for a generic URI!");
/*      */       }
/*      */ 
/* 1262 */       if (getPath() == null)
/*      */       {
/* 1264 */         throw new MalformedURIException("Query string cannot be set when path is null!");
/*      */       }
/*      */ 
/* 1267 */       if (!isURIString(p_queryString))
/*      */       {
/* 1269 */         throw new MalformedURIException("Query string contains invalid character!");
/*      */       }
/*      */ 
/* 1274 */       this.m_queryString = p_queryString;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFragment(String p_fragment)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1292 */     if (p_fragment == null)
/*      */     {
/* 1294 */       this.m_fragment = null;
/*      */     } else {
/* 1296 */       if (!isGenericURI())
/*      */       {
/* 1298 */         throw new MalformedURIException(Utils.messages.createMessage("ER_FRAG_FOR_GENERIC_URI", null));
/*      */       }
/*      */ 
/* 1301 */       if (getPath() == null)
/*      */       {
/* 1303 */         throw new MalformedURIException(Utils.messages.createMessage("ER_FRAG_WHEN_PATH_NULL", null));
/*      */       }
/*      */ 
/* 1306 */       if (!isURIString(p_fragment))
/*      */       {
/* 1308 */         throw new MalformedURIException(Utils.messages.createMessage("ER_FRAG_INVALID_CHAR", null));
/*      */       }
/*      */ 
/* 1312 */       this.m_fragment = p_fragment;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equals(Object p_test)
/*      */   {
/* 1328 */     if ((p_test instanceof URI))
/*      */     {
/* 1330 */       URI testURI = (URI)p_test;
/*      */ 
/* 1332 */       if (((this.m_scheme == null) && (testURI.m_scheme == null)) || ((this.m_scheme != null) && (testURI.m_scheme != null) && (this.m_scheme.equals(testURI.m_scheme)) && (((this.m_userinfo == null) && (testURI.m_userinfo == null)) || ((this.m_userinfo != null) && (testURI.m_userinfo != null) && (this.m_userinfo.equals(testURI.m_userinfo)) && (((this.m_host == null) && (testURI.m_host == null)) || ((this.m_host != null) && (testURI.m_host != null) && (this.m_host.equals(testURI.m_host)) && (this.m_port == testURI.m_port) && (((this.m_path == null) && (testURI.m_path == null)) || ((this.m_path != null) && (testURI.m_path != null) && (this.m_path.equals(testURI.m_path)) && (((this.m_queryString == null) && (testURI.m_queryString == null)) || ((this.m_queryString != null) && (testURI.m_queryString != null) && (this.m_queryString.equals(testURI.m_queryString)) && (((this.m_fragment == null) && (testURI.m_fragment == null)) || ((this.m_fragment != null) && (testURI.m_fragment != null) && (this.m_fragment.equals(testURI.m_fragment))))))))))))))
/*      */       {
/* 1340 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1344 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1349 */     int hash = 5;
/* 1350 */     hash = 41 * hash + Objects.hashCode(this.m_scheme);
/* 1351 */     hash = 41 * hash + Objects.hashCode(this.m_userinfo);
/* 1352 */     hash = 41 * hash + Objects.hashCode(this.m_host);
/* 1353 */     hash = 41 * hash + this.m_port;
/* 1354 */     hash = 41 * hash + Objects.hashCode(this.m_path);
/* 1355 */     hash = 41 * hash + Objects.hashCode(this.m_queryString);
/* 1356 */     hash = 41 * hash + Objects.hashCode(this.m_fragment);
/* 1357 */     return hash;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1369 */     StringBuilder uriSpecString = new StringBuilder();
/*      */ 
/* 1371 */     if (this.m_scheme != null)
/*      */     {
/* 1373 */       uriSpecString.append(this.m_scheme);
/* 1374 */       uriSpecString.append(':');
/*      */     }
/*      */ 
/* 1377 */     uriSpecString.append(getSchemeSpecificPart());
/*      */ 
/* 1379 */     return uriSpecString.toString();
/*      */   }
/*      */ 
/*      */   public boolean isGenericURI()
/*      */   {
/* 1394 */     return this.m_host != null;
/*      */   }
/*      */ 
/*      */   public static boolean isConformantSchemeName(String p_scheme)
/*      */   {
/* 1409 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0))
/*      */     {
/* 1411 */       return false;
/*      */     }
/*      */ 
/* 1414 */     if (!isAlpha(p_scheme.charAt(0)))
/*      */     {
/* 1416 */       return false;
/*      */     }
/*      */ 
/* 1421 */     for (int i = 1; i < p_scheme.length(); i++)
/*      */     {
/* 1423 */       char testChar = p_scheme.charAt(i);
/*      */ 
/* 1425 */       if ((!isAlphanum(testChar)) && ("+-.".indexOf(testChar) == -1))
/*      */       {
/* 1427 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1431 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isWellFormedAddress(String p_address)
/*      */   {
/* 1450 */     if (p_address == null)
/*      */     {
/* 1452 */       return false;
/*      */     }
/*      */ 
/* 1455 */     String address = p_address.trim();
/* 1456 */     int addrLength = address.length();
/*      */ 
/* 1458 */     if ((addrLength == 0) || (addrLength > 255))
/*      */     {
/* 1460 */       return false;
/*      */     }
/*      */ 
/* 1463 */     if ((address.startsWith(".")) || (address.startsWith("-")))
/*      */     {
/* 1465 */       return false;
/*      */     }
/*      */ 
/* 1471 */     int index = address.lastIndexOf('.');
/*      */ 
/* 1473 */     if (address.endsWith("."))
/*      */     {
/* 1475 */       index = address.substring(0, index).lastIndexOf('.');
/*      */     }
/*      */ 
/* 1478 */     if ((index + 1 < addrLength) && (isDigit(p_address.charAt(index + 1))))
/*      */     {
/* 1481 */       int numDots = 0;
/*      */ 
/* 1486 */       for (int i = 0; i < addrLength; i++)
/*      */       {
/* 1488 */         char testChar = address.charAt(i);
/*      */ 
/* 1490 */         if (testChar == '.')
/*      */         {
/* 1492 */           if ((!isDigit(address.charAt(i - 1))) || ((i + 1 < addrLength) && (!isDigit(address.charAt(i + 1)))))
/*      */           {
/* 1495 */             return false;
/*      */           }
/*      */ 
/* 1498 */           numDots++;
/*      */         }
/* 1500 */         else if (!isDigit(testChar))
/*      */         {
/* 1502 */           return false;
/*      */         }
/*      */       }
/*      */ 
/* 1506 */       if (numDots != 3)
/*      */       {
/* 1508 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1518 */       for (int i = 0; i < addrLength; i++)
/*      */       {
/* 1520 */         char testChar = address.charAt(i);
/*      */ 
/* 1522 */         if (testChar == '.')
/*      */         {
/* 1524 */           if (!isAlphanum(address.charAt(i - 1)))
/*      */           {
/* 1526 */             return false;
/*      */           }
/*      */ 
/* 1529 */           if ((i + 1 < addrLength) && (!isAlphanum(address.charAt(i + 1))))
/*      */           {
/* 1531 */             return false;
/*      */           }
/*      */         }
/* 1534 */         else if ((!isAlphanum(testChar)) && (testChar != '-'))
/*      */         {
/* 1536 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1541 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean isDigit(char p_char)
/*      */   {
/* 1553 */     return (p_char >= '0') && (p_char <= '9');
/*      */   }
/*      */ 
/*      */   private static boolean isHex(char p_char)
/*      */   {
/* 1566 */     return (isDigit(p_char)) || ((p_char >= 'a') && (p_char <= 'f')) || ((p_char >= 'A') && (p_char <= 'F'));
/*      */   }
/*      */ 
/*      */   private static boolean isAlpha(char p_char)
/*      */   {
/* 1579 */     return ((p_char >= 'a') && (p_char <= 'z')) || ((p_char >= 'A') && (p_char <= 'Z'));
/*      */   }
/*      */ 
/*      */   private static boolean isAlphanum(char p_char)
/*      */   {
/* 1592 */     return (isAlpha(p_char)) || (isDigit(p_char));
/*      */   }
/*      */ 
/*      */   private static boolean isReservedCharacter(char p_char)
/*      */   {
/* 1605 */     return ";/?:@&=+$,".indexOf(p_char) != -1;
/*      */   }
/*      */ 
/*      */   private static boolean isUnreservedCharacter(char p_char)
/*      */   {
/* 1617 */     return (isAlphanum(p_char)) || ("-_.!~*'() ".indexOf(p_char) != -1);
/*      */   }
/*      */ 
/*      */   private static boolean isURIString(String p_uric)
/*      */   {
/* 1632 */     if (p_uric == null)
/*      */     {
/* 1634 */       return false;
/*      */     }
/*      */ 
/* 1637 */     int end = p_uric.length();
/* 1638 */     char testChar = '\000';
/*      */ 
/* 1640 */     for (int i = 0; i < end; i++)
/*      */     {
/* 1642 */       testChar = p_uric.charAt(i);
/*      */ 
/* 1644 */       if (testChar == '%')
/*      */       {
/* 1646 */         if ((i + 2 >= end) || (!isHex(p_uric.charAt(i + 1))) || (!isHex(p_uric.charAt(i + 2))))
/*      */         {
/* 1649 */           return false;
/*      */         }
/*      */ 
/* 1653 */         i += 2;
/*      */       }
/* 1659 */       else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */       {
/* 1665 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1669 */     return true;
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
/*   95 */       super();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.utils.URI
 * JD-Core Version:    0.6.2
 */