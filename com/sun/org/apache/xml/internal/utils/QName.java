/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import java.io.Serializable;
/*     */ import java.util.Stack;
/*     */ import java.util.StringTokenizer;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class QName
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 467434581652829920L;
/*     */   protected String _localName;
/*     */   protected String _namespaceURI;
/*     */   protected String _prefix;
/*     */   public static final String S_XMLNAMESPACEURI = "http://www.w3.org/XML/1998/namespace";
/*     */   private int m_hashCode;
/*     */ 
/*     */   public QName()
/*     */   {
/*     */   }
/*     */ 
/*     */   public QName(String namespaceURI, String localName)
/*     */   {
/*  95 */     this(namespaceURI, localName, false);
/*     */   }
/*     */ 
/*     */   public QName(String namespaceURI, String localName, boolean validate)
/*     */   {
/* 112 */     if (localName == null) {
/* 113 */       throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_NULL", null));
/*     */     }
/*     */ 
/* 116 */     if (validate)
/*     */     {
/* 118 */       if (!XML11Char.isXML11ValidNCName(localName))
/*     */       {
/* 120 */         throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 125 */     this._namespaceURI = namespaceURI;
/* 126 */     this._localName = localName;
/* 127 */     this.m_hashCode = toString().hashCode();
/*     */   }
/*     */ 
/*     */   public QName(String namespaceURI, String prefix, String localName)
/*     */   {
/* 141 */     this(namespaceURI, prefix, localName, false);
/*     */   }
/*     */ 
/*     */   public QName(String namespaceURI, String prefix, String localName, boolean validate)
/*     */   {
/* 159 */     if (localName == null) {
/* 160 */       throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_NULL", null));
/*     */     }
/*     */ 
/* 163 */     if (validate)
/*     */     {
/* 165 */       if (!XML11Char.isXML11ValidNCName(localName))
/*     */       {
/* 167 */         throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
/*     */       }
/*     */ 
/* 171 */       if ((null != prefix) && (!XML11Char.isXML11ValidNCName(prefix)))
/*     */       {
/* 173 */         throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_PREFIX_INVALID", null));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 178 */     this._namespaceURI = namespaceURI;
/* 179 */     this._prefix = prefix;
/* 180 */     this._localName = localName;
/* 181 */     this.m_hashCode = toString().hashCode();
/*     */   }
/*     */ 
/*     */   public QName(String localName)
/*     */   {
/* 193 */     this(localName, false);
/*     */   }
/*     */ 
/*     */   public QName(String localName, boolean validate)
/*     */   {
/* 209 */     if (localName == null) {
/* 210 */       throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_NULL", null));
/*     */     }
/*     */ 
/* 213 */     if (validate)
/*     */     {
/* 215 */       if (!XML11Char.isXML11ValidNCName(localName))
/*     */       {
/* 217 */         throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
/*     */       }
/*     */     }
/*     */ 
/* 221 */     this._namespaceURI = null;
/* 222 */     this._localName = localName;
/* 223 */     this.m_hashCode = toString().hashCode();
/*     */   }
/*     */ 
/*     */   public QName(String qname, Stack namespaces)
/*     */   {
/* 236 */     this(qname, namespaces, false);
/*     */   }
/*     */ 
/*     */   public QName(String qname, Stack namespaces, boolean validate)
/*     */   {
/* 252 */     String namespace = null;
/* 253 */     String prefix = null;
/* 254 */     int indexOfNSSep = qname.indexOf(':');
/*     */ 
/* 256 */     if (indexOfNSSep > 0)
/*     */     {
/* 258 */       prefix = qname.substring(0, indexOfNSSep);
/*     */ 
/* 260 */       if (prefix.equals("xml"))
/*     */       {
/* 262 */         namespace = "http://www.w3.org/XML/1998/namespace";
/*     */       }
/*     */       else {
/* 265 */         if (prefix.equals("xmlns"))
/*     */         {
/* 267 */           return;
/*     */         }
/*     */ 
/* 271 */         int depth = namespaces.size();
/*     */ 
/* 273 */         for (int i = depth - 1; i >= 0; i--)
/*     */         {
/* 275 */           NameSpace ns = (NameSpace)namespaces.elementAt(i);
/*     */ 
/* 277 */           while (null != ns)
/*     */           {
/* 279 */             if ((null != ns.m_prefix) && (prefix.equals(ns.m_prefix)))
/*     */             {
/* 281 */               namespace = ns.m_uri;
/* 282 */               i = -1;
/*     */ 
/* 284 */               break;
/*     */             }
/*     */ 
/* 287 */             ns = ns.m_next;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 292 */       if (null == namespace)
/*     */       {
/* 294 */         throw new RuntimeException(XMLMessages.createXMLMessage("ER_PREFIX_MUST_RESOLVE", new Object[] { prefix }));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 301 */     this._localName = (indexOfNSSep < 0 ? qname : qname.substring(indexOfNSSep + 1));
/*     */ 
/* 304 */     if (validate)
/*     */     {
/* 306 */       if ((this._localName == null) || (!XML11Char.isXML11ValidNCName(this._localName)))
/*     */       {
/* 308 */         throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
/*     */       }
/*     */     }
/*     */ 
/* 312 */     this._namespaceURI = namespace;
/* 313 */     this._prefix = prefix;
/* 314 */     this.m_hashCode = toString().hashCode();
/*     */   }
/*     */ 
/*     */   public QName(String qname, Element namespaceContext, PrefixResolver resolver)
/*     */   {
/* 329 */     this(qname, namespaceContext, resolver, false);
/*     */   }
/*     */ 
/*     */   public QName(String qname, Element namespaceContext, PrefixResolver resolver, boolean validate)
/*     */   {
/* 347 */     this._namespaceURI = null;
/*     */ 
/* 349 */     int indexOfNSSep = qname.indexOf(':');
/*     */ 
/* 351 */     if (indexOfNSSep > 0)
/*     */     {
/* 353 */       if (null != namespaceContext)
/*     */       {
/* 355 */         String prefix = qname.substring(0, indexOfNSSep);
/*     */ 
/* 357 */         this._prefix = prefix;
/*     */ 
/* 359 */         if (prefix.equals("xml"))
/*     */         {
/* 361 */           this._namespaceURI = "http://www.w3.org/XML/1998/namespace";
/*     */         }
/*     */         else
/*     */         {
/* 365 */           if (prefix.equals("xmlns"))
/*     */           {
/* 367 */             return;
/*     */           }
/*     */ 
/* 371 */           this._namespaceURI = resolver.getNamespaceForPrefix(prefix, namespaceContext);
/*     */         }
/*     */ 
/* 375 */         if (null == this._namespaceURI)
/*     */         {
/* 377 */           throw new RuntimeException(XMLMessages.createXMLMessage("ER_PREFIX_MUST_RESOLVE", new Object[] { prefix }));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 390 */     this._localName = (indexOfNSSep < 0 ? qname : qname.substring(indexOfNSSep + 1));
/*     */ 
/* 393 */     if (validate)
/*     */     {
/* 395 */       if ((this._localName == null) || (!XML11Char.isXML11ValidNCName(this._localName)))
/*     */       {
/* 397 */         throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 402 */     this.m_hashCode = toString().hashCode();
/*     */   }
/*     */ 
/*     */   public QName(String qname, PrefixResolver resolver)
/*     */   {
/* 416 */     this(qname, resolver, false);
/*     */   }
/*     */ 
/*     */   public QName(String qname, PrefixResolver resolver, boolean validate)
/*     */   {
/* 432 */     String prefix = null;
/* 433 */     this._namespaceURI = null;
/*     */ 
/* 435 */     int indexOfNSSep = qname.indexOf(':');
/*     */ 
/* 437 */     if (indexOfNSSep > 0)
/*     */     {
/* 439 */       prefix = qname.substring(0, indexOfNSSep);
/*     */ 
/* 441 */       if (prefix.equals("xml"))
/*     */       {
/* 443 */         this._namespaceURI = "http://www.w3.org/XML/1998/namespace";
/*     */       }
/*     */       else
/*     */       {
/* 447 */         this._namespaceURI = resolver.getNamespaceForPrefix(prefix);
/*     */       }
/*     */ 
/* 450 */       if (null == this._namespaceURI)
/*     */       {
/* 452 */         throw new RuntimeException(XMLMessages.createXMLMessage("ER_PREFIX_MUST_RESOLVE", new Object[] { prefix }));
/*     */       }
/*     */ 
/* 457 */       this._localName = qname.substring(indexOfNSSep + 1);
/*     */     } else {
/* 459 */       if (indexOfNSSep == 0)
/*     */       {
/* 461 */         throw new RuntimeException(XMLMessages.createXMLMessage("ER_NAME_CANT_START_WITH_COLON", null));
/*     */       }
/*     */ 
/* 468 */       this._localName = qname;
/*     */     }
/*     */ 
/* 471 */     if (validate)
/*     */     {
/* 473 */       if ((this._localName == null) || (!XML11Char.isXML11ValidNCName(this._localName)))
/*     */       {
/* 475 */         throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 481 */     this.m_hashCode = toString().hashCode();
/* 482 */     this._prefix = prefix;
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI()
/*     */   {
/* 493 */     return this._namespaceURI;
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 504 */     return this._prefix;
/*     */   }
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 514 */     return this._localName;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 527 */     return this._namespaceURI != null ? "{" + this._namespaceURI + "}" + this._localName : this._prefix != null ? this._prefix + ":" + this._localName : this._localName;
/*     */   }
/*     */ 
/*     */   public String toNamespacedString()
/*     */   {
/* 543 */     return this._namespaceURI != null ? "{" + this._namespaceURI + "}" + this._localName : this._localName;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 555 */     return getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public String getLocalPart()
/*     */   {
/* 565 */     return getLocalName();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 575 */     return this.m_hashCode;
/*     */   }
/*     */ 
/*     */   public boolean equals(String ns, String localPart)
/*     */   {
/* 591 */     String thisnamespace = getNamespaceURI();
/*     */ 
/* 593 */     return (getLocalName().equals(localPart)) && ((null != thisnamespace) && (null != ns) ? thisnamespace.equals(ns) : (null == thisnamespace) && (null == ns));
/*     */   }
/*     */ 
/*     */   public boolean equals(Object object)
/*     */   {
/* 609 */     if (object == this) {
/* 610 */       return true;
/*     */     }
/* 612 */     if ((object instanceof QName)) {
/* 613 */       QName qname = (QName)object;
/* 614 */       String thisnamespace = getNamespaceURI();
/* 615 */       String thatnamespace = qname.getNamespaceURI();
/*     */ 
/* 617 */       return (getLocalName().equals(qname.getLocalName())) && ((null != thisnamespace) && (null != thatnamespace) ? thisnamespace.equals(thatnamespace) : (null == thisnamespace) && (null == thatnamespace));
/*     */     }
/*     */ 
/* 623 */     return false;
/*     */   }
/*     */ 
/*     */   public static QName getQNameFromString(String name)
/*     */   {
/* 637 */     StringTokenizer tokenizer = new StringTokenizer(name, "{}", false);
/*     */ 
/* 639 */     String s1 = tokenizer.nextToken();
/* 640 */     String s2 = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
/*     */     QName qname;
/*     */     QName qname;
/* 642 */     if (null == s2)
/* 643 */       qname = new QName(null, s1);
/*     */     else {
/* 645 */       qname = new QName(s1, s2);
/*     */     }
/* 647 */     return qname;
/*     */   }
/*     */ 
/*     */   public static boolean isXMLNSDecl(String attRawName)
/*     */   {
/* 661 */     return (attRawName.startsWith("xmlns")) && ((attRawName.equals("xmlns")) || (attRawName.startsWith("xmlns:")));
/*     */   }
/*     */ 
/*     */   public static String getPrefixFromXMLNSDecl(String attRawName)
/*     */   {
/* 677 */     int index = attRawName.indexOf(':');
/*     */ 
/* 679 */     return index >= 0 ? attRawName.substring(index + 1) : "";
/*     */   }
/*     */ 
/*     */   public static String getLocalPart(String qname)
/*     */   {
/* 692 */     int index = qname.indexOf(':');
/*     */ 
/* 694 */     return index < 0 ? qname : qname.substring(index + 1);
/*     */   }
/*     */ 
/*     */   public static String getPrefixPart(String qname)
/*     */   {
/* 707 */     int index = qname.indexOf(':');
/*     */ 
/* 709 */     return index >= 0 ? qname.substring(0, index) : "";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.QName
 * JD-Core Version:    0.6.2
 */