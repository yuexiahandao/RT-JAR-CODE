/*      */ package com.sun.org.apache.xml.internal.utils;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.utils.Objects;
/*      */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public class URI
/*      */   implements Serializable
/*      */ {
/*      */   static final long serialVersionUID = 7096266377907081897L;
/*      */   private static final String RESERVED_CHARACTERS = ";/?:@&=+$,";
/*      */   private static final String MARK_CHARACTERS = "-_.!~*'() ";
/*      */   private static final String SCHEME_CHARACTERS = "+-.";
/*      */   private static final String USERINFO_CHARACTERS = ";:&=+$,";
/*  117 */   private String m_scheme = null;
/*      */ 
/*  121 */   private String m_userinfo = null;
/*      */ 
/*  125 */   private String m_host = null;
/*      */ 
/*  129 */   private int m_port = -1;
/*      */ 
/*  133 */   private String m_path = null;
/*      */ 
/*  140 */   private String m_queryString = null;
/*      */ 
/*  144 */   private String m_fragment = null;
/*      */ 
/*  147 */   private static boolean DEBUG = false;
/*      */ 
/*      */   public URI()
/*      */   {
/*      */   }
/*      */ 
/*      */   public URI(URI p_other)
/*      */   {
/*  162 */     initialize(p_other);
/*      */   }
/*      */ 
/*      */   public URI(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  182 */     this((URI)null, p_uriSpec);
/*      */   }
/*      */ 
/*      */   public URI(URI p_base, String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  199 */     initialize(p_base, p_uriSpec);
/*      */   }
/*      */ 
/*      */   public URI(String p_scheme, String p_schemeSpecificPart)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  218 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0))
/*      */     {
/*  220 */       throw new MalformedURIException("Cannot construct URI with null/empty scheme!");
/*      */     }
/*      */ 
/*  224 */     if ((p_schemeSpecificPart == null) || (p_schemeSpecificPart.trim().length() == 0))
/*      */     {
/*  227 */       throw new MalformedURIException("Cannot construct URI with null/empty scheme-specific part!");
/*      */     }
/*      */ 
/*  231 */     setScheme(p_scheme);
/*  232 */     setPath(p_schemeSpecificPart);
/*      */   }
/*      */ 
/*      */   public URI(String p_scheme, String p_host, String p_path, String p_queryString, String p_fragment)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  259 */     this(p_scheme, null, p_host, -1, p_path, p_queryString, p_fragment);
/*      */   }
/*      */ 
/*      */   public URI(String p_scheme, String p_userinfo, String p_host, int p_port, String p_path, String p_queryString, String p_fragment)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  291 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0))
/*      */     {
/*  293 */       throw new MalformedURIException(XMLMessages.createXMLMessage("ER_SCHEME_REQUIRED", null));
/*      */     }
/*      */ 
/*  296 */     if (p_host == null)
/*      */     {
/*  298 */       if (p_userinfo != null)
/*      */       {
/*  300 */         throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_USERINFO_IF_NO_HOST", null));
/*      */       }
/*      */ 
/*  304 */       if (p_port != -1)
/*      */       {
/*  306 */         throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_PORT_IF_NO_HOST", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  311 */     if (p_path != null)
/*      */     {
/*  313 */       if ((p_path.indexOf('?') != -1) && (p_queryString != null))
/*      */       {
/*  315 */         throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_QUERY_STRING_IN_PATH", null));
/*      */       }
/*      */ 
/*  319 */       if ((p_path.indexOf('#') != -1) && (p_fragment != null))
/*      */       {
/*  321 */         throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_FRAGMENT_STRING_IN_PATH", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  326 */     setScheme(p_scheme);
/*  327 */     setHost(p_host);
/*  328 */     setPort(p_port);
/*  329 */     setUserinfo(p_userinfo);
/*  330 */     setPath(p_path);
/*  331 */     setQueryString(p_queryString);
/*  332 */     setFragment(p_fragment);
/*      */   }
/*      */ 
/*      */   private void initialize(URI p_other)
/*      */   {
/*  343 */     this.m_scheme = p_other.getScheme();
/*  344 */     this.m_userinfo = p_other.getUserinfo();
/*  345 */     this.m_host = p_other.getHost();
/*  346 */     this.m_port = p_other.getPort();
/*  347 */     this.m_path = p_other.getPath();
/*  348 */     this.m_queryString = p_other.getQueryString();
/*  349 */     this.m_fragment = p_other.getFragment();
/*      */   }
/*      */ 
/*      */   private void initialize(URI p_base, String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  372 */     if ((p_base == null) && ((p_uriSpec == null) || (p_uriSpec.trim().length() == 0)))
/*      */     {
/*  375 */       throw new MalformedURIException(XMLMessages.createXMLMessage("ER_CANNOT_INIT_URI_EMPTY_PARMS", null));
/*      */     }
/*      */ 
/*  380 */     if ((p_uriSpec == null) || (p_uriSpec.trim().length() == 0))
/*      */     {
/*  382 */       initialize(p_base);
/*      */ 
/*  384 */       return;
/*      */     }
/*      */ 
/*  387 */     String uriSpec = p_uriSpec.trim();
/*  388 */     int uriSpecLen = uriSpec.length();
/*  389 */     int index = 0;
/*      */ 
/*  392 */     int colonIndex = uriSpec.indexOf(':');
/*  393 */     if (colonIndex < 0)
/*      */     {
/*  395 */       if (p_base == null)
/*      */       {
/*  397 */         throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_SCHEME_IN_URI", new Object[] { uriSpec }));
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  402 */       initializeScheme(uriSpec);
/*  403 */       uriSpec = uriSpec.substring(colonIndex + 1);
/*      */ 
/*  405 */       if ((this.m_scheme != null) && (p_base != null))
/*      */       {
/*  421 */         if ((uriSpec.startsWith("/")) || (!this.m_scheme.equals(p_base.m_scheme)) || (!p_base.getSchemeSpecificPart().startsWith("/")))
/*      */         {
/*  423 */           p_base = null;
/*      */         }
/*      */       }
/*      */ 
/*  427 */       uriSpecLen = uriSpec.length();
/*      */     }
/*      */ 
/*  431 */     if ((index + 1 < uriSpecLen) && (uriSpec.substring(index).startsWith("//")))
/*      */     {
/*  434 */       index += 2;
/*      */ 
/*  436 */       int startPos = index;
/*      */ 
/*  439 */       char testChar = '\000';
/*      */ 
/*  441 */       while (index < uriSpecLen)
/*      */       {
/*  443 */         testChar = uriSpec.charAt(index);
/*      */ 
/*  445 */         if ((testChar == '/') || (testChar == '?') || (testChar == '#'))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  450 */         index++;
/*      */       }
/*      */ 
/*  455 */       if (index > startPos)
/*      */       {
/*  457 */         initializeAuthority(uriSpec.substring(startPos, index));
/*      */       }
/*      */       else
/*      */       {
/*  461 */         this.m_host = "";
/*      */       }
/*      */     }
/*      */ 
/*  465 */     initializePath(uriSpec.substring(index));
/*      */ 
/*  472 */     if (p_base != null)
/*      */     {
/*  482 */       if ((this.m_path.length() == 0) && (this.m_scheme == null) && (this.m_host == null))
/*      */       {
/*  484 */         this.m_scheme = p_base.getScheme();
/*  485 */         this.m_userinfo = p_base.getUserinfo();
/*  486 */         this.m_host = p_base.getHost();
/*  487 */         this.m_port = p_base.getPort();
/*  488 */         this.m_path = p_base.getPath();
/*      */ 
/*  490 */         if (this.m_queryString == null)
/*      */         {
/*  492 */           this.m_queryString = p_base.getQueryString();
/*      */         }
/*      */ 
/*  495 */         return;
/*      */       }
/*      */ 
/*  500 */       if (this.m_scheme == null)
/*      */       {
/*  502 */         this.m_scheme = p_base.getScheme();
/*      */       }
/*      */ 
/*  507 */       if (this.m_host == null)
/*      */       {
/*  509 */         this.m_userinfo = p_base.getUserinfo();
/*  510 */         this.m_host = p_base.getHost();
/*  511 */         this.m_port = p_base.getPort();
/*      */       }
/*      */       else
/*      */       {
/*  515 */         return;
/*      */       }
/*      */ 
/*  519 */       if ((this.m_path.length() > 0) && (this.m_path.startsWith("/")))
/*      */       {
/*  521 */         return;
/*      */       }
/*      */ 
/*  526 */       String path = "";
/*  527 */       String basePath = p_base.getPath();
/*      */ 
/*  530 */       if (basePath != null)
/*      */       {
/*  532 */         int lastSlash = basePath.lastIndexOf('/');
/*      */ 
/*  534 */         if (lastSlash != -1)
/*      */         {
/*  536 */           path = basePath.substring(0, lastSlash + 1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  541 */       path = path.concat(this.m_path);
/*      */ 
/*  544 */       index = -1;
/*      */ 
/*  546 */       while ((index = path.indexOf("/./")) != -1)
/*      */       {
/*  548 */         path = path.substring(0, index + 1).concat(path.substring(index + 3));
/*      */       }
/*      */ 
/*  552 */       if (path.endsWith("/."))
/*      */       {
/*  554 */         path = path.substring(0, path.length() - 1);
/*      */       }
/*      */ 
/*  559 */       index = -1;
/*      */ 
/*  561 */       int segIndex = -1;
/*  562 */       String tempString = null;
/*      */ 
/*  564 */       while ((index = path.indexOf("/../")) > 0)
/*      */       {
/*  566 */         tempString = path.substring(0, path.indexOf("/../"));
/*  567 */         segIndex = tempString.lastIndexOf('/');
/*      */ 
/*  569 */         if ((segIndex != -1) && 
/*  571 */           (!tempString.substring(segIndex++).equals("..")))
/*      */         {
/*  573 */           path = path.substring(0, segIndex).concat(path.substring(index + 4));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  581 */       if (path.endsWith("/.."))
/*      */       {
/*  583 */         tempString = path.substring(0, path.length() - 3);
/*  584 */         segIndex = tempString.lastIndexOf('/');
/*      */ 
/*  586 */         if (segIndex != -1)
/*      */         {
/*  588 */           path = path.substring(0, segIndex + 1);
/*      */         }
/*      */       }
/*      */ 
/*  592 */       this.m_path = path;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initializeScheme(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  607 */     int uriSpecLen = p_uriSpec.length();
/*  608 */     int index = 0;
/*  609 */     String scheme = null;
/*  610 */     char testChar = '\000';
/*      */ 
/*  612 */     while (index < uriSpecLen)
/*      */     {
/*  614 */       testChar = p_uriSpec.charAt(index);
/*      */ 
/*  616 */       if ((testChar == ':') || (testChar == '/') || (testChar == '?') || (testChar == '#'))
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  622 */       index++;
/*      */     }
/*      */ 
/*  625 */     scheme = p_uriSpec.substring(0, index);
/*      */ 
/*  627 */     if (scheme.length() == 0)
/*      */     {
/*  629 */       throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_SCHEME_INURI", null));
/*      */     }
/*      */ 
/*  633 */     setScheme(scheme);
/*      */   }
/*      */ 
/*      */   private void initializeAuthority(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  649 */     int index = 0;
/*  650 */     int start = 0;
/*  651 */     int end = p_uriSpec.length();
/*  652 */     char testChar = '\000';
/*  653 */     String userinfo = null;
/*      */ 
/*  656 */     if (p_uriSpec.indexOf('@', start) != -1)
/*      */     {
/*  658 */       while (index < end)
/*      */       {
/*  660 */         testChar = p_uriSpec.charAt(index);
/*      */ 
/*  662 */         if (testChar == '@')
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  667 */         index++;
/*      */       }
/*      */ 
/*  670 */       userinfo = p_uriSpec.substring(start, index);
/*      */ 
/*  672 */       index++;
/*      */     }
/*      */ 
/*  676 */     String host = null;
/*      */ 
/*  678 */     start = index;
/*      */ 
/*  680 */     while (index < end)
/*      */     {
/*  682 */       testChar = p_uriSpec.charAt(index);
/*      */ 
/*  684 */       if (testChar == ':')
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  689 */       index++;
/*      */     }
/*      */ 
/*  692 */     host = p_uriSpec.substring(start, index);
/*      */ 
/*  694 */     int port = -1;
/*      */ 
/*  696 */     if (host.length() > 0)
/*      */     {
/*  700 */       if (testChar == ':')
/*      */       {
/*  702 */         index++;
/*      */ 
/*  704 */         start = index;
/*      */ 
/*  706 */         while (index < end)
/*      */         {
/*  708 */           index++;
/*      */         }
/*      */ 
/*  711 */         String portStr = p_uriSpec.substring(start, index);
/*      */ 
/*  713 */         if (portStr.length() > 0)
/*      */         {
/*  715 */           for (int i = 0; i < portStr.length(); i++)
/*      */           {
/*  717 */             if (!isDigit(portStr.charAt(i)))
/*      */             {
/*  719 */               throw new MalformedURIException(portStr + " is invalid. Port should only contain digits!");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */           try
/*      */           {
/*  726 */             port = Integer.parseInt(portStr);
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
/*  737 */     setHost(host);
/*  738 */     setPort(port);
/*  739 */     setUserinfo(userinfo);
/*      */   }
/*      */ 
/*      */   private void initializePath(String p_uriSpec)
/*      */     throws URI.MalformedURIException
/*      */   {
/*  752 */     if (p_uriSpec == null)
/*      */     {
/*  754 */       throw new MalformedURIException("Cannot initialize path from null string!");
/*      */     }
/*      */ 
/*  758 */     int index = 0;
/*  759 */     int start = 0;
/*  760 */     int end = p_uriSpec.length();
/*  761 */     char testChar = '\000';
/*      */ 
/*  764 */     while (index < end)
/*      */     {
/*  766 */       testChar = p_uriSpec.charAt(index);
/*      */ 
/*  768 */       if ((testChar == '?') || (testChar == '#'))
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  774 */       if (testChar == '%')
/*      */       {
/*  776 */         if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */         {
/*  779 */           throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", null));
/*      */         }
/*      */ 
/*      */       }
/*  783 */       else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */       {
/*  786 */         if ('\\' != testChar) {
/*  787 */           throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PATH_INVALID_CHAR", new Object[] { String.valueOf(testChar) }));
/*      */         }
/*      */       }
/*      */ 
/*  791 */       index++;
/*      */     }
/*      */ 
/*  794 */     this.m_path = p_uriSpec.substring(start, index);
/*      */ 
/*  797 */     if (testChar == '?')
/*      */     {
/*  799 */       index++;
/*      */ 
/*  801 */       start = index;
/*      */ 
/*  803 */       while (index < end)
/*      */       {
/*  805 */         testChar = p_uriSpec.charAt(index);
/*      */ 
/*  807 */         if (testChar == '#')
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  812 */         if (testChar == '%')
/*      */         {
/*  814 */           if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */           {
/*  817 */             throw new MalformedURIException("Query string contains invalid escape sequence!");
/*      */           }
/*      */ 
/*      */         }
/*  821 */         else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */         {
/*  824 */           throw new MalformedURIException("Query string contains invalid character:" + testChar);
/*      */         }
/*      */ 
/*  828 */         index++;
/*      */       }
/*      */ 
/*  831 */       this.m_queryString = p_uriSpec.substring(start, index);
/*      */     }
/*      */ 
/*  835 */     if (testChar == '#')
/*      */     {
/*  837 */       index++;
/*      */ 
/*  839 */       start = index;
/*      */ 
/*  841 */       while (index < end)
/*      */       {
/*  843 */         testChar = p_uriSpec.charAt(index);
/*      */ 
/*  845 */         if (testChar == '%')
/*      */         {
/*  847 */           if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
/*      */           {
/*  850 */             throw new MalformedURIException("Fragment contains invalid escape sequence!");
/*      */           }
/*      */ 
/*      */         }
/*  854 */         else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */         {
/*  857 */           throw new MalformedURIException("Fragment contains invalid character:" + testChar);
/*      */         }
/*      */ 
/*  861 */         index++;
/*      */       }
/*      */ 
/*  864 */       this.m_fragment = p_uriSpec.substring(start, index);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getScheme()
/*      */   {
/*  875 */     return this.m_scheme;
/*      */   }
/*      */ 
/*      */   public String getSchemeSpecificPart()
/*      */   {
/*  887 */     StringBuilder schemespec = new StringBuilder();
/*      */ 
/*  889 */     if ((this.m_userinfo != null) || (this.m_host != null) || (this.m_port != -1))
/*      */     {
/*  891 */       schemespec.append("//");
/*      */     }
/*      */ 
/*  894 */     if (this.m_userinfo != null)
/*      */     {
/*  896 */       schemespec.append(this.m_userinfo);
/*  897 */       schemespec.append('@');
/*      */     }
/*      */ 
/*  900 */     if (this.m_host != null)
/*      */     {
/*  902 */       schemespec.append(this.m_host);
/*      */     }
/*      */ 
/*  905 */     if (this.m_port != -1)
/*      */     {
/*  907 */       schemespec.append(':');
/*  908 */       schemespec.append(this.m_port);
/*      */     }
/*      */ 
/*  911 */     if (this.m_path != null)
/*      */     {
/*  913 */       schemespec.append(this.m_path);
/*      */     }
/*      */ 
/*  916 */     if (this.m_queryString != null)
/*      */     {
/*  918 */       schemespec.append('?');
/*  919 */       schemespec.append(this.m_queryString);
/*      */     }
/*      */ 
/*  922 */     if (this.m_fragment != null)
/*      */     {
/*  924 */       schemespec.append('#');
/*  925 */       schemespec.append(this.m_fragment);
/*      */     }
/*      */ 
/*  928 */     return schemespec.toString();
/*      */   }
/*      */ 
/*      */   public String getUserinfo()
/*      */   {
/*  938 */     return this.m_userinfo;
/*      */   }
/*      */ 
/*      */   public String getHost()
/*      */   {
/*  948 */     return this.m_host;
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/*  958 */     return this.m_port;
/*      */   }
/*      */ 
/*      */   public String getPath(boolean p_includeQueryString, boolean p_includeFragment)
/*      */   {
/*  979 */     StringBuilder pathString = new StringBuilder(this.m_path);
/*      */ 
/*  981 */     if ((p_includeQueryString) && (this.m_queryString != null))
/*      */     {
/*  983 */       pathString.append('?');
/*  984 */       pathString.append(this.m_queryString);
/*      */     }
/*      */ 
/*  987 */     if ((p_includeFragment) && (this.m_fragment != null))
/*      */     {
/*  989 */       pathString.append('#');
/*  990 */       pathString.append(this.m_fragment);
/*      */     }
/*      */ 
/*  993 */     return pathString.toString();
/*      */   }
/*      */ 
/*      */   public String getPath()
/*      */   {
/* 1004 */     return this.m_path;
/*      */   }
/*      */ 
/*      */   public String getQueryString()
/*      */   {
/* 1016 */     return this.m_queryString;
/*      */   }
/*      */ 
/*      */   public String getFragment()
/*      */   {
/* 1028 */     return this.m_fragment;
/*      */   }
/*      */ 
/*      */   public void setScheme(String p_scheme)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1043 */     if (p_scheme == null)
/*      */     {
/* 1045 */       throw new MalformedURIException(XMLMessages.createXMLMessage("ER_SCHEME_FROM_NULL_STRING", null));
/*      */     }
/*      */ 
/* 1048 */     if (!isConformantSchemeName(p_scheme))
/*      */     {
/* 1050 */       throw new MalformedURIException(XMLMessages.createXMLMessage("ER_SCHEME_NOT_CONFORMANT", null));
/*      */     }
/*      */ 
/* 1053 */     this.m_scheme = p_scheme.toLowerCase();
/*      */   }
/*      */ 
/*      */   public void setUserinfo(String p_userinfo)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1068 */     if (p_userinfo == null)
/*      */     {
/* 1070 */       this.m_userinfo = null;
/*      */     }
/*      */     else
/*      */     {
/* 1074 */       if (this.m_host == null)
/*      */       {
/* 1076 */         throw new MalformedURIException("Userinfo cannot be set when host is null!");
/*      */       }
/*      */ 
/* 1082 */       int index = 0;
/* 1083 */       int end = p_userinfo.length();
/* 1084 */       char testChar = '\000';
/*      */ 
/* 1086 */       while (index < end)
/*      */       {
/* 1088 */         testChar = p_userinfo.charAt(index);
/*      */ 
/* 1090 */         if (testChar == '%')
/*      */         {
/* 1092 */           if ((index + 2 >= end) || (!isHex(p_userinfo.charAt(index + 1))) || (!isHex(p_userinfo.charAt(index + 2))))
/*      */           {
/* 1095 */             throw new MalformedURIException("Userinfo contains invalid escape sequence!");
/*      */           }
/*      */ 
/*      */         }
/* 1099 */         else if ((!isUnreservedCharacter(testChar)) && (";:&=+$,".indexOf(testChar) == -1))
/*      */         {
/* 1102 */           throw new MalformedURIException("Userinfo contains invalid character:" + testChar);
/*      */         }
/*      */ 
/* 1106 */         index++;
/*      */       }
/*      */     }
/*      */ 
/* 1110 */     this.m_userinfo = p_userinfo;
/*      */   }
/*      */ 
/*      */   public void setHost(String p_host)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1125 */     if ((p_host == null) || (p_host.trim().length() == 0))
/*      */     {
/* 1127 */       this.m_host = p_host;
/* 1128 */       this.m_userinfo = null;
/* 1129 */       this.m_port = -1;
/*      */     }
/* 1131 */     else if (!isWellFormedAddress(p_host))
/*      */     {
/* 1133 */       throw new MalformedURIException(XMLMessages.createXMLMessage("ER_HOST_ADDRESS_NOT_WELLFORMED", null));
/*      */     }
/*      */ 
/* 1136 */     this.m_host = p_host;
/*      */   }
/*      */ 
/*      */   public void setPort(int p_port)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1153 */     if ((p_port >= 0) && (p_port <= 65535))
/*      */     {
/* 1155 */       if (this.m_host == null)
/*      */       {
/* 1157 */         throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PORT_WHEN_HOST_NULL", null));
/*      */       }
/*      */ 
/*      */     }
/* 1161 */     else if (p_port != -1)
/*      */     {
/* 1163 */       throw new MalformedURIException(XMLMessages.createXMLMessage("ER_INVALID_PORT", null));
/*      */     }
/*      */ 
/* 1166 */     this.m_port = p_port;
/*      */   }
/*      */ 
/*      */   public void setPath(String p_path)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1186 */     if (p_path == null)
/*      */     {
/* 1188 */       this.m_path = null;
/* 1189 */       this.m_queryString = null;
/* 1190 */       this.m_fragment = null;
/*      */     }
/*      */     else
/*      */     {
/* 1194 */       initializePath(p_path);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void appendPath(String p_addToPath)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1214 */     if ((p_addToPath == null) || (p_addToPath.trim().length() == 0))
/*      */     {
/* 1216 */       return;
/*      */     }
/*      */ 
/* 1219 */     if (!isURIString(p_addToPath))
/*      */     {
/* 1221 */       throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PATH_INVALID_CHAR", new Object[] { p_addToPath }));
/*      */     }
/*      */ 
/* 1224 */     if ((this.m_path == null) || (this.m_path.trim().length() == 0))
/*      */     {
/* 1226 */       if (p_addToPath.startsWith("/"))
/*      */       {
/* 1228 */         this.m_path = p_addToPath;
/*      */       }
/*      */       else
/*      */       {
/* 1232 */         this.m_path = ("/" + p_addToPath);
/*      */       }
/*      */     }
/* 1235 */     else if (this.m_path.endsWith("/"))
/*      */     {
/* 1237 */       if (p_addToPath.startsWith("/"))
/*      */       {
/* 1239 */         this.m_path = this.m_path.concat(p_addToPath.substring(1));
/*      */       }
/*      */       else
/*      */       {
/* 1243 */         this.m_path = this.m_path.concat(p_addToPath);
/*      */       }
/*      */ 
/*      */     }
/* 1248 */     else if (p_addToPath.startsWith("/"))
/*      */     {
/* 1250 */       this.m_path = this.m_path.concat(p_addToPath);
/*      */     }
/*      */     else
/*      */     {
/* 1254 */       this.m_path = this.m_path.concat("/" + p_addToPath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setQueryString(String p_queryString)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1274 */     if (p_queryString == null)
/*      */     {
/* 1276 */       this.m_queryString = null;
/*      */     } else {
/* 1278 */       if (!isGenericURI())
/*      */       {
/* 1280 */         throw new MalformedURIException("Query string can only be set for a generic URI!");
/*      */       }
/*      */ 
/* 1283 */       if (getPath() == null)
/*      */       {
/* 1285 */         throw new MalformedURIException("Query string cannot be set when path is null!");
/*      */       }
/*      */ 
/* 1288 */       if (!isURIString(p_queryString))
/*      */       {
/* 1290 */         throw new MalformedURIException("Query string contains invalid character!");
/*      */       }
/*      */ 
/* 1295 */       this.m_queryString = p_queryString;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFragment(String p_fragment)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 1313 */     if (p_fragment == null)
/*      */     {
/* 1315 */       this.m_fragment = null;
/*      */     } else {
/* 1317 */       if (!isGenericURI())
/*      */       {
/* 1319 */         throw new MalformedURIException(XMLMessages.createXMLMessage("ER_FRAG_FOR_GENERIC_URI", null));
/*      */       }
/*      */ 
/* 1322 */       if (getPath() == null)
/*      */       {
/* 1324 */         throw new MalformedURIException(XMLMessages.createXMLMessage("ER_FRAG_WHEN_PATH_NULL", null));
/*      */       }
/*      */ 
/* 1327 */       if (!isURIString(p_fragment))
/*      */       {
/* 1329 */         throw new MalformedURIException(XMLMessages.createXMLMessage("ER_FRAG_INVALID_CHAR", null));
/*      */       }
/*      */ 
/* 1333 */       this.m_fragment = p_fragment;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equals(Object p_test)
/*      */   {
/* 1349 */     if ((p_test instanceof URI))
/*      */     {
/* 1351 */       URI testURI = (URI)p_test;
/*      */ 
/* 1353 */       if (((this.m_scheme == null) && (testURI.m_scheme == null)) || ((this.m_scheme != null) && (testURI.m_scheme != null) && (this.m_scheme.equals(testURI.m_scheme)) && (((this.m_userinfo == null) && (testURI.m_userinfo == null)) || ((this.m_userinfo != null) && (testURI.m_userinfo != null) && (this.m_userinfo.equals(testURI.m_userinfo)) && (((this.m_host == null) && (testURI.m_host == null)) || ((this.m_host != null) && (testURI.m_host != null) && (this.m_host.equals(testURI.m_host)) && (this.m_port == testURI.m_port) && (((this.m_path == null) && (testURI.m_path == null)) || ((this.m_path != null) && (testURI.m_path != null) && (this.m_path.equals(testURI.m_path)) && (((this.m_queryString == null) && (testURI.m_queryString == null)) || ((this.m_queryString != null) && (testURI.m_queryString != null) && (this.m_queryString.equals(testURI.m_queryString)) && (((this.m_fragment == null) && (testURI.m_fragment == null)) || ((this.m_fragment != null) && (testURI.m_fragment != null) && (this.m_fragment.equals(testURI.m_fragment))))))))))))))
/*      */       {
/* 1361 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1365 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1370 */     int hash = 7;
/* 1371 */     hash = 59 * hash + Objects.hashCode(this.m_scheme);
/* 1372 */     hash = 59 * hash + Objects.hashCode(this.m_userinfo);
/* 1373 */     hash = 59 * hash + Objects.hashCode(this.m_host);
/* 1374 */     hash = 59 * hash + this.m_port;
/* 1375 */     hash = 59 * hash + Objects.hashCode(this.m_path);
/* 1376 */     hash = 59 * hash + Objects.hashCode(this.m_queryString);
/* 1377 */     hash = 59 * hash + Objects.hashCode(this.m_fragment);
/* 1378 */     return hash;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1390 */     StringBuilder uriSpecString = new StringBuilder();
/*      */ 
/* 1392 */     if (this.m_scheme != null)
/*      */     {
/* 1394 */       uriSpecString.append(this.m_scheme);
/* 1395 */       uriSpecString.append(':');
/*      */     }
/*      */ 
/* 1398 */     uriSpecString.append(getSchemeSpecificPart());
/*      */ 
/* 1400 */     return uriSpecString.toString();
/*      */   }
/*      */ 
/*      */   public boolean isGenericURI()
/*      */   {
/* 1415 */     return this.m_host != null;
/*      */   }
/*      */ 
/*      */   public static boolean isConformantSchemeName(String p_scheme)
/*      */   {
/* 1430 */     if ((p_scheme == null) || (p_scheme.trim().length() == 0))
/*      */     {
/* 1432 */       return false;
/*      */     }
/*      */ 
/* 1435 */     if (!isAlpha(p_scheme.charAt(0)))
/*      */     {
/* 1437 */       return false;
/*      */     }
/*      */ 
/* 1442 */     for (int i = 1; i < p_scheme.length(); i++)
/*      */     {
/* 1444 */       char testChar = p_scheme.charAt(i);
/*      */ 
/* 1446 */       if ((!isAlphanum(testChar)) && ("+-.".indexOf(testChar) == -1))
/*      */       {
/* 1448 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1452 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isWellFormedAddress(String p_address)
/*      */   {
/* 1471 */     if (p_address == null)
/*      */     {
/* 1473 */       return false;
/*      */     }
/*      */ 
/* 1476 */     String address = p_address.trim();
/* 1477 */     int addrLength = address.length();
/*      */ 
/* 1479 */     if ((addrLength == 0) || (addrLength > 255))
/*      */     {
/* 1481 */       return false;
/*      */     }
/*      */ 
/* 1484 */     if ((address.startsWith(".")) || (address.startsWith("-")))
/*      */     {
/* 1486 */       return false;
/*      */     }
/*      */ 
/* 1492 */     int index = address.lastIndexOf('.');
/*      */ 
/* 1494 */     if (address.endsWith("."))
/*      */     {
/* 1496 */       index = address.substring(0, index).lastIndexOf('.');
/*      */     }
/*      */ 
/* 1499 */     if ((index + 1 < addrLength) && (isDigit(p_address.charAt(index + 1))))
/*      */     {
/* 1502 */       int numDots = 0;
/*      */ 
/* 1507 */       for (int i = 0; i < addrLength; i++)
/*      */       {
/* 1509 */         char testChar = address.charAt(i);
/*      */ 
/* 1511 */         if (testChar == '.')
/*      */         {
/* 1513 */           if ((!isDigit(address.charAt(i - 1))) || ((i + 1 < addrLength) && (!isDigit(address.charAt(i + 1)))))
/*      */           {
/* 1516 */             return false;
/*      */           }
/*      */ 
/* 1519 */           numDots++;
/*      */         }
/* 1521 */         else if (!isDigit(testChar))
/*      */         {
/* 1523 */           return false;
/*      */         }
/*      */       }
/*      */ 
/* 1527 */       if (numDots != 3)
/*      */       {
/* 1529 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1539 */       for (int i = 0; i < addrLength; i++)
/*      */       {
/* 1541 */         char testChar = address.charAt(i);
/*      */ 
/* 1543 */         if (testChar == '.')
/*      */         {
/* 1545 */           if (!isAlphanum(address.charAt(i - 1)))
/*      */           {
/* 1547 */             return false;
/*      */           }
/*      */ 
/* 1550 */           if ((i + 1 < addrLength) && (!isAlphanum(address.charAt(i + 1))))
/*      */           {
/* 1552 */             return false;
/*      */           }
/*      */         }
/* 1555 */         else if ((!isAlphanum(testChar)) && (testChar != '-'))
/*      */         {
/* 1557 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1562 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean isDigit(char p_char)
/*      */   {
/* 1574 */     return (p_char >= '0') && (p_char <= '9');
/*      */   }
/*      */ 
/*      */   private static boolean isHex(char p_char)
/*      */   {
/* 1587 */     return (isDigit(p_char)) || ((p_char >= 'a') && (p_char <= 'f')) || ((p_char >= 'A') && (p_char <= 'F'));
/*      */   }
/*      */ 
/*      */   private static boolean isAlpha(char p_char)
/*      */   {
/* 1600 */     return ((p_char >= 'a') && (p_char <= 'z')) || ((p_char >= 'A') && (p_char <= 'Z'));
/*      */   }
/*      */ 
/*      */   private static boolean isAlphanum(char p_char)
/*      */   {
/* 1613 */     return (isAlpha(p_char)) || (isDigit(p_char));
/*      */   }
/*      */ 
/*      */   private static boolean isReservedCharacter(char p_char)
/*      */   {
/* 1626 */     return ";/?:@&=+$,".indexOf(p_char) != -1;
/*      */   }
/*      */ 
/*      */   private static boolean isUnreservedCharacter(char p_char)
/*      */   {
/* 1638 */     return (isAlphanum(p_char)) || ("-_.!~*'() ".indexOf(p_char) != -1);
/*      */   }
/*      */ 
/*      */   private static boolean isURIString(String p_uric)
/*      */   {
/* 1653 */     if (p_uric == null)
/*      */     {
/* 1655 */       return false;
/*      */     }
/*      */ 
/* 1658 */     int end = p_uric.length();
/* 1659 */     char testChar = '\000';
/*      */ 
/* 1661 */     for (int i = 0; i < end; i++)
/*      */     {
/* 1663 */       testChar = p_uric.charAt(i);
/*      */ 
/* 1665 */       if (testChar == '%')
/*      */       {
/* 1667 */         if ((i + 2 >= end) || (!isHex(p_uric.charAt(i + 1))) || (!isHex(p_uric.charAt(i + 2))))
/*      */         {
/* 1670 */           return false;
/*      */         }
/*      */ 
/* 1674 */         i += 2;
/*      */       }
/* 1680 */       else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
/*      */       {
/* 1686 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1690 */     return true;
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
/*   93 */       super();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.URI
 * JD-Core Version:    0.6.2
 */