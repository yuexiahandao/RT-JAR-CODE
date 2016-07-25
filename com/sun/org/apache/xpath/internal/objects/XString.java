/*      */ package com.sun.org.apache.xpath.internal.objects;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*      */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLCharacterRecognizer;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*      */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*      */ import com.sun.org.apache.xpath.internal.XPathContext;
/*      */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*      */ import java.util.Locale;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ 
/*      */ public class XString extends XObject
/*      */   implements XMLString
/*      */ {
/*      */   static final long serialVersionUID = 2020470518395094525L;
/*   45 */   public static final XString EMPTYSTRING = new XString("");
/*      */ 
/*      */   protected XString(Object val)
/*      */   {
/*   54 */     super(val);
/*      */   }
/*      */ 
/*      */   public XString(String val)
/*      */   {
/*   64 */     super(val);
/*      */   }
/*      */ 
/*      */   public int getType()
/*      */   {
/*   74 */     return 3;
/*      */   }
/*      */ 
/*      */   public String getTypeString()
/*      */   {
/*   85 */     return "#STRING";
/*      */   }
/*      */ 
/*      */   public boolean hasString()
/*      */   {
/*   95 */     return true;
/*      */   }
/*      */ 
/*      */   public double num()
/*      */   {
/*  106 */     return toDouble();
/*      */   }
/*      */ 
/*      */   public double toDouble()
/*      */   {
/*  124 */     XMLString s = trim();
/*  125 */     double result = (0.0D / 0.0D);
/*  126 */     for (int i = 0; i < s.length(); i++)
/*      */     {
/*  128 */       char c = s.charAt(i);
/*  129 */       if ((c != '-') && (c != '.') && ((c < '0') || (c > '9')))
/*      */       {
/*  132 */         return result;
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  137 */       result = Double.parseDouble(s.toString());
/*      */     } catch (NumberFormatException e) {
/*      */     }
/*  140 */     return result;
/*      */   }
/*      */ 
/*      */   public boolean bool()
/*      */   {
/*  151 */     return str().length() > 0;
/*      */   }
/*      */ 
/*      */   public XMLString xstr()
/*      */   {
/*  161 */     return this;
/*      */   }
/*      */ 
/*      */   public String str()
/*      */   {
/*  171 */     return null != this.m_obj ? (String)this.m_obj : "";
/*      */   }
/*      */ 
/*      */   public int rtf(XPathContext support)
/*      */   {
/*  184 */     DTM frag = support.createDocumentFragment();
/*      */ 
/*  186 */     frag.appendTextChild(str());
/*      */ 
/*  188 */     return frag.getDocument();
/*      */   }
/*      */ 
/*      */   public void dispatchCharactersEvents(ContentHandler ch)
/*      */     throws SAXException
/*      */   {
/*  206 */     String str = str();
/*      */ 
/*  208 */     ch.characters(str.toCharArray(), 0, str.length());
/*      */   }
/*      */ 
/*      */   public void dispatchAsComment(LexicalHandler lh)
/*      */     throws SAXException
/*      */   {
/*  224 */     String str = str();
/*      */ 
/*  226 */     lh.comment(str.toCharArray(), 0, str.length());
/*      */   }
/*      */ 
/*      */   public int length()
/*      */   {
/*  237 */     return str().length();
/*      */   }
/*      */ 
/*      */   public char charAt(int index)
/*      */   {
/*  255 */     return str().charAt(index);
/*      */   }
/*      */ 
/*      */   public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
/*      */   {
/*  281 */     str().getChars(srcBegin, srcEnd, dst, dstBegin);
/*      */   }
/*      */ 
/*      */   public boolean equals(XObject obj2)
/*      */   {
/*  299 */     int t = obj2.getType();
/*      */     try
/*      */     {
/*  302 */       if (4 == t) {
/*  303 */         return obj2.equals(this);
/*      */       }
/*      */ 
/*  307 */       if (1 == t) {
/*  308 */         return obj2.bool() == bool();
/*      */       }
/*      */ 
/*  311 */       if (2 == t)
/*  312 */         return obj2.num() == num();
/*      */     }
/*      */     catch (TransformerException te)
/*      */     {
/*  316 */       throw new WrappedRuntimeException(te);
/*      */     }
/*      */ 
/*  321 */     return xstr().equals(obj2.xstr());
/*      */   }
/*      */ 
/*      */   public boolean equals(String obj2)
/*      */   {
/*  337 */     return str().equals(obj2);
/*      */   }
/*      */ 
/*      */   public boolean equals(XMLString obj2)
/*      */   {
/*  355 */     if (obj2 != null) {
/*  356 */       if (!obj2.hasString()) {
/*  357 */         return obj2.equals(str());
/*      */       }
/*  359 */       return str().equals(obj2.toString());
/*      */     }
/*      */ 
/*  362 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object obj2)
/*      */   {
/*  381 */     if (null == obj2) {
/*  382 */       return false;
/*      */     }
/*      */ 
/*  387 */     if ((obj2 instanceof XNodeSet))
/*  388 */       return obj2.equals(this);
/*  389 */     if ((obj2 instanceof XNumber)) {
/*  390 */       return obj2.equals(this);
/*      */     }
/*  392 */     return str().equals(obj2.toString());
/*      */   }
/*      */ 
/*      */   public boolean equalsIgnoreCase(String anotherString)
/*      */   {
/*  412 */     return str().equalsIgnoreCase(anotherString);
/*      */   }
/*      */ 
/*      */   public int compareTo(XMLString xstr)
/*      */   {
/*  431 */     int len1 = length();
/*  432 */     int len2 = xstr.length();
/*  433 */     int n = Math.min(len1, len2);
/*  434 */     int i = 0;
/*  435 */     int j = 0;
/*      */ 
/*  437 */     while (n-- != 0)
/*      */     {
/*  439 */       char c1 = charAt(i);
/*  440 */       char c2 = xstr.charAt(j);
/*      */ 
/*  442 */       if (c1 != c2)
/*      */       {
/*  444 */         return c1 - c2;
/*      */       }
/*      */ 
/*  447 */       i++;
/*  448 */       j++;
/*      */     }
/*      */ 
/*  451 */     return len1 - len2;
/*      */   }
/*      */ 
/*      */   public int compareToIgnoreCase(XMLString str)
/*      */   {
/*  482 */     throw new WrappedRuntimeException(new NoSuchMethodException("Java 1.2 method, not yet implemented"));
/*      */   }
/*      */ 
/*      */   public boolean startsWith(String prefix, int toffset)
/*      */   {
/*  508 */     return str().startsWith(prefix, toffset);
/*      */   }
/*      */ 
/*      */   public boolean startsWith(String prefix)
/*      */   {
/*  527 */     return startsWith(prefix, 0);
/*      */   }
/*      */ 
/*      */   public boolean startsWith(XMLString prefix, int toffset)
/*      */   {
/*  552 */     int to = toffset;
/*  553 */     int tlim = length();
/*  554 */     int po = 0;
/*  555 */     int pc = prefix.length();
/*      */ 
/*  558 */     if ((toffset < 0) || (toffset > tlim - pc))
/*      */     {
/*  560 */       return false;
/*      */     }
/*      */     while (true) {
/*  563 */       pc--; if (pc < 0)
/*      */         break;
/*  565 */       if (charAt(to) != prefix.charAt(po))
/*      */       {
/*  567 */         return false;
/*      */       }
/*      */ 
/*  570 */       to++;
/*  571 */       po++;
/*      */     }
/*      */ 
/*  574 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean startsWith(XMLString prefix)
/*      */   {
/*  593 */     return startsWith(prefix, 0);
/*      */   }
/*      */ 
/*      */   public boolean endsWith(String suffix)
/*      */   {
/*  611 */     return str().endsWith(suffix);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  629 */     return str().hashCode();
/*      */   }
/*      */ 
/*      */   public int indexOf(int ch)
/*      */   {
/*  651 */     return str().indexOf(ch);
/*      */   }
/*      */ 
/*      */   public int indexOf(int ch, int fromIndex)
/*      */   {
/*  684 */     return str().indexOf(ch, fromIndex);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(int ch)
/*      */   {
/*  704 */     return str().lastIndexOf(ch);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(int ch, int fromIndex)
/*      */   {
/*  732 */     return str().lastIndexOf(ch, fromIndex);
/*      */   }
/*      */ 
/*      */   public int indexOf(String str)
/*      */   {
/*  754 */     return str().indexOf(str);
/*      */   }
/*      */ 
/*      */   public int indexOf(XMLString str)
/*      */   {
/*  776 */     return str().indexOf(str.toString());
/*      */   }
/*      */ 
/*      */   public int indexOf(String str, int fromIndex)
/*      */   {
/*  807 */     return str().indexOf(str, fromIndex);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(String str)
/*      */   {
/*  830 */     return str().lastIndexOf(str);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(String str, int fromIndex)
/*      */   {
/*  855 */     return str().lastIndexOf(str, fromIndex);
/*      */   }
/*      */ 
/*      */   public XMLString substring(int beginIndex)
/*      */   {
/*  877 */     return new XString(str().substring(beginIndex));
/*      */   }
/*      */ 
/*      */   public XMLString substring(int beginIndex, int endIndex)
/*      */   {
/*  898 */     return new XString(str().substring(beginIndex, endIndex));
/*      */   }
/*      */ 
/*      */   public XMLString concat(String str)
/*      */   {
/*  915 */     return new XString(str().concat(str));
/*      */   }
/*      */ 
/*      */   public XMLString toLowerCase(Locale locale)
/*      */   {
/*  929 */     return new XString(str().toLowerCase(locale));
/*      */   }
/*      */ 
/*      */   public XMLString toLowerCase()
/*      */   {
/*  944 */     return new XString(str().toLowerCase());
/*      */   }
/*      */ 
/*      */   public XMLString toUpperCase(Locale locale)
/*      */   {
/*  957 */     return new XString(str().toUpperCase(locale));
/*      */   }
/*      */ 
/*      */   public XMLString toUpperCase()
/*      */   {
/*  988 */     return new XString(str().toUpperCase());
/*      */   }
/*      */ 
/*      */   public XMLString trim()
/*      */   {
/*  998 */     return new XString(str().trim());
/*      */   }
/*      */ 
/*      */   private static boolean isSpace(char ch)
/*      */   {
/* 1010 */     return XMLCharacterRecognizer.isWhiteSpace(ch);
/*      */   }
/*      */ 
/*      */   public XMLString fixWhiteSpace(boolean trimHead, boolean trimTail, boolean doublePunctuationSpaces)
/*      */   {
/* 1032 */     int len = length();
/* 1033 */     char[] buf = new char[len];
/*      */ 
/* 1035 */     getChars(0, len, buf, 0);
/*      */ 
/* 1037 */     boolean edit = false;
/*      */ 
/* 1040 */     for (int s = 0; s < len; s++)
/*      */     {
/* 1042 */       if (isSpace(buf[s]))
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1049 */     int d = s;
/* 1050 */     boolean pres = false;
/*      */ 
/* 1052 */     for (; s < len; s++)
/*      */     {
/* 1054 */       char c = buf[s];
/*      */ 
/* 1056 */       if (isSpace(c))
/*      */       {
/* 1058 */         if (!pres)
/*      */         {
/* 1060 */           if (' ' != c)
/*      */           {
/* 1062 */             edit = true;
/*      */           }
/*      */ 
/* 1065 */           buf[(d++)] = ' ';
/*      */ 
/* 1067 */           if ((doublePunctuationSpaces) && (s != 0))
/*      */           {
/* 1069 */             char prevChar = buf[(s - 1)];
/*      */ 
/* 1071 */             if ((prevChar != '.') && (prevChar != '!') && (prevChar != '?'))
/*      */             {
/* 1074 */               pres = true;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1079 */             pres = true;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1084 */           edit = true;
/* 1085 */           pres = true;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1090 */         buf[(d++)] = c;
/* 1091 */         pres = false;
/*      */       }
/*      */     }
/*      */ 
/* 1095 */     if ((trimTail) && (1 <= d) && (' ' == buf[(d - 1)]))
/*      */     {
/* 1097 */       edit = true;
/*      */ 
/* 1099 */       d--;
/*      */     }
/*      */ 
/* 1102 */     int start = 0;
/*      */ 
/* 1104 */     if ((trimHead) && (0 < d) && (' ' == buf[0]))
/*      */     {
/* 1106 */       edit = true;
/*      */ 
/* 1108 */       start++;
/*      */     }
/*      */ 
/* 1111 */     XMLStringFactory xsf = XMLStringFactoryImpl.getFactory();
/*      */ 
/* 1113 */     return edit ? xsf.newstr(new String(buf, start, d - start)) : this;
/*      */   }
/*      */ 
/*      */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*      */   {
/* 1121 */     visitor.visitStringLiteral(owner, this);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XString
 * JD-Core Version:    0.6.2
 */