/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLCharacterRecognizer;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public class XStringForFSB extends XString
/*     */ {
/*     */   static final long serialVersionUID = -1533039186550674548L;
/*     */   int m_start;
/*     */   int m_length;
/*  46 */   protected String m_strCache = null;
/*     */ 
/*  49 */   protected int m_hash = 0;
/*     */ 
/*     */   public XStringForFSB(FastStringBuffer val, int start, int length)
/*     */   {
/*  61 */     super(val);
/*     */ 
/*  63 */     this.m_start = start;
/*  64 */     this.m_length = length;
/*     */ 
/*  66 */     if (null == val)
/*  67 */       throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_FASTSTRINGBUFFER_CANNOT_BE_NULL", null));
/*     */   }
/*     */ 
/*     */   private XStringForFSB(String val)
/*     */   {
/*  79 */     super(val);
/*     */ 
/*  81 */     throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_FSB_CANNOT_TAKE_STRING", null));
/*     */   }
/*     */ 
/*     */   public FastStringBuffer fsb()
/*     */   {
/*  92 */     return (FastStringBuffer)this.m_obj;
/*     */   }
/*     */ 
/*     */   public void appendToFsb(FastStringBuffer fsb)
/*     */   {
/* 103 */     fsb.append(str());
/*     */   }
/*     */ 
/*     */   public boolean hasString()
/*     */   {
/* 113 */     return null != this.m_strCache;
/*     */   }
/*     */ 
/*     */   public Object object()
/*     */   {
/* 130 */     return str();
/*     */   }
/*     */ 
/*     */   public String str()
/*     */   {
/* 141 */     if (null == this.m_strCache)
/*     */     {
/* 143 */       this.m_strCache = fsb().getString(this.m_start, this.m_length);
/*     */     }
/*     */ 
/* 169 */     return this.m_strCache;
/*     */   }
/*     */ 
/*     */   public void dispatchCharactersEvents(ContentHandler ch)
/*     */     throws SAXException
/*     */   {
/* 186 */     fsb().sendSAXcharacters(ch, this.m_start, this.m_length);
/*     */   }
/*     */ 
/*     */   public void dispatchAsComment(LexicalHandler lh)
/*     */     throws SAXException
/*     */   {
/* 201 */     fsb().sendSAXComment(lh, this.m_start, this.m_length);
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 212 */     return this.m_length;
/*     */   }
/*     */ 
/*     */   public char charAt(int index)
/*     */   {
/* 230 */     return fsb().charAt(this.m_start + index);
/*     */   }
/*     */ 
/*     */   public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
/*     */   {
/* 259 */     int n = srcEnd - srcBegin;
/*     */ 
/* 261 */     if (n > this.m_length) {
/* 262 */       n = this.m_length;
/*     */     }
/* 264 */     if (n > dst.length - dstBegin) {
/* 265 */       n = dst.length - dstBegin;
/*     */     }
/* 267 */     int end = srcBegin + this.m_start + n;
/* 268 */     int d = dstBegin;
/* 269 */     FastStringBuffer fsb = fsb();
/*     */ 
/* 271 */     for (int i = srcBegin + this.m_start; i < end; i++)
/*     */     {
/* 273 */       dst[(d++)] = fsb.charAt(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(XMLString obj2)
/*     */   {
/* 294 */     if (this == obj2)
/*     */     {
/* 296 */       return true;
/*     */     }
/*     */ 
/* 299 */     int n = this.m_length;
/*     */ 
/* 301 */     if (n == obj2.length())
/*     */     {
/* 303 */       FastStringBuffer fsb = fsb();
/* 304 */       int i = this.m_start;
/* 305 */       int j = 0;
/*     */ 
/* 307 */       while (n-- != 0)
/*     */       {
/* 309 */         if (fsb.charAt(i) != obj2.charAt(j))
/*     */         {
/* 311 */           return false;
/*     */         }
/*     */ 
/* 314 */         i++;
/* 315 */         j++;
/*     */       }
/*     */ 
/* 318 */       return true;
/*     */     }
/*     */ 
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(XObject obj2)
/*     */   {
/* 336 */     if (this == obj2)
/*     */     {
/* 338 */       return true;
/*     */     }
/* 340 */     if (obj2.getType() == 2) {
/* 341 */       return obj2.equals(this);
/*     */     }
/* 343 */     String str = obj2.str();
/* 344 */     int n = this.m_length;
/*     */ 
/* 346 */     if (n == str.length())
/*     */     {
/* 348 */       FastStringBuffer fsb = fsb();
/* 349 */       int i = this.m_start;
/* 350 */       int j = 0;
/*     */ 
/* 352 */       while (n-- != 0)
/*     */       {
/* 354 */         if (fsb.charAt(i) != str.charAt(j))
/*     */         {
/* 356 */           return false;
/*     */         }
/*     */ 
/* 359 */         i++;
/* 360 */         j++;
/*     */       }
/*     */ 
/* 363 */       return true;
/*     */     }
/*     */ 
/* 366 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(String anotherString)
/*     */   {
/* 381 */     int n = this.m_length;
/*     */ 
/* 383 */     if (n == anotherString.length())
/*     */     {
/* 385 */       FastStringBuffer fsb = fsb();
/* 386 */       int i = this.m_start;
/* 387 */       int j = 0;
/*     */ 
/* 389 */       while (n-- != 0)
/*     */       {
/* 391 */         if (fsb.charAt(i) != anotherString.charAt(j))
/*     */         {
/* 393 */           return false;
/*     */         }
/*     */ 
/* 396 */         i++;
/* 397 */         j++;
/*     */       }
/*     */ 
/* 400 */       return true;
/*     */     }
/*     */ 
/* 403 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj2)
/*     */   {
/* 423 */     if (null == obj2) {
/* 424 */       return false;
/*     */     }
/* 426 */     if ((obj2 instanceof XNumber)) {
/* 427 */       return obj2.equals(this);
/*     */     }
/*     */ 
/* 432 */     if ((obj2 instanceof XNodeSet))
/* 433 */       return obj2.equals(this);
/* 434 */     if ((obj2 instanceof XStringForFSB)) {
/* 435 */       return equals((XMLString)obj2);
/*     */     }
/* 437 */     return equals(obj2.toString());
/*     */   }
/*     */ 
/*     */   public boolean equalsIgnoreCase(String anotherString)
/*     */   {
/* 457 */     return this.m_length == anotherString.length() ? str().equalsIgnoreCase(anotherString) : false;
/*     */   }
/*     */ 
/*     */   public int compareTo(XMLString xstr)
/*     */   {
/* 477 */     int len1 = this.m_length;
/* 478 */     int len2 = xstr.length();
/* 479 */     int n = Math.min(len1, len2);
/* 480 */     FastStringBuffer fsb = fsb();
/* 481 */     int i = this.m_start;
/* 482 */     int j = 0;
/*     */ 
/* 484 */     while (n-- != 0)
/*     */     {
/* 486 */       char c1 = fsb.charAt(i);
/* 487 */       char c2 = xstr.charAt(j);
/*     */ 
/* 489 */       if (c1 != c2)
/*     */       {
/* 491 */         return c1 - c2;
/*     */       }
/*     */ 
/* 494 */       i++;
/* 495 */       j++;
/*     */     }
/*     */ 
/* 498 */     return len1 - len2;
/*     */   }
/*     */ 
/*     */   public int compareToIgnoreCase(XMLString xstr)
/*     */   {
/* 523 */     int len1 = this.m_length;
/* 524 */     int len2 = xstr.length();
/* 525 */     int n = Math.min(len1, len2);
/* 526 */     FastStringBuffer fsb = fsb();
/* 527 */     int i = this.m_start;
/* 528 */     int j = 0;
/*     */ 
/* 530 */     while (n-- != 0)
/*     */     {
/* 532 */       char c1 = Character.toLowerCase(fsb.charAt(i));
/* 533 */       char c2 = Character.toLowerCase(xstr.charAt(j));
/*     */ 
/* 535 */       if (c1 != c2)
/*     */       {
/* 537 */         return c1 - c2;
/*     */       }
/*     */ 
/* 540 */       i++;
/* 541 */       j++;
/*     */     }
/*     */ 
/* 544 */     return len1 - len2;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 588 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean startsWith(XMLString prefix, int toffset)
/*     */   {
/* 613 */     FastStringBuffer fsb = fsb();
/* 614 */     int to = this.m_start + toffset;
/* 615 */     int tlim = this.m_start + this.m_length;
/* 616 */     int po = 0;
/* 617 */     int pc = prefix.length();
/*     */ 
/* 620 */     if ((toffset < 0) || (toffset > this.m_length - pc))
/*     */     {
/* 622 */       return false;
/*     */     }
/*     */     while (true) {
/* 625 */       pc--; if (pc < 0)
/*     */         break;
/* 627 */       if (fsb.charAt(to) != prefix.charAt(po))
/*     */       {
/* 629 */         return false;
/*     */       }
/*     */ 
/* 632 */       to++;
/* 633 */       po++;
/*     */     }
/*     */ 
/* 636 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean startsWith(XMLString prefix)
/*     */   {
/* 656 */     return startsWith(prefix, 0);
/*     */   }
/*     */ 
/*     */   public int indexOf(int ch)
/*     */   {
/* 678 */     return indexOf(ch, 0);
/*     */   }
/*     */ 
/*     */   public int indexOf(int ch, int fromIndex)
/*     */   {
/* 712 */     int max = this.m_start + this.m_length;
/* 713 */     FastStringBuffer fsb = fsb();
/*     */ 
/* 715 */     if (fromIndex < 0)
/*     */     {
/* 717 */       fromIndex = 0;
/*     */     }
/* 719 */     else if (fromIndex >= this.m_length)
/*     */     {
/* 723 */       return -1;
/*     */     }
/*     */ 
/* 726 */     for (int i = this.m_start + fromIndex; i < max; i++)
/*     */     {
/* 728 */       if (fsb.charAt(i) == ch)
/*     */       {
/* 730 */         return i - this.m_start;
/*     */       }
/*     */     }
/*     */ 
/* 734 */     return -1;
/*     */   }
/*     */ 
/*     */   public XMLString substring(int beginIndex)
/*     */   {
/* 757 */     int len = this.m_length - beginIndex;
/*     */ 
/* 759 */     if (len <= 0) {
/* 760 */       return XString.EMPTYSTRING;
/*     */     }
/*     */ 
/* 763 */     int start = this.m_start + beginIndex;
/*     */ 
/* 765 */     return new XStringForFSB(fsb(), start, len);
/*     */   }
/*     */ 
/*     */   public XMLString substring(int beginIndex, int endIndex)
/*     */   {
/* 788 */     int len = endIndex - beginIndex;
/*     */ 
/* 790 */     if (len > this.m_length) {
/* 791 */       len = this.m_length;
/*     */     }
/* 793 */     if (len <= 0) {
/* 794 */       return XString.EMPTYSTRING;
/*     */     }
/*     */ 
/* 797 */     int start = this.m_start + beginIndex;
/*     */ 
/* 799 */     return new XStringForFSB(fsb(), start, len);
/*     */   }
/*     */ 
/*     */   public XMLString concat(String str)
/*     */   {
/* 817 */     return new XString(str().concat(str));
/*     */   }
/*     */ 
/*     */   public XMLString trim()
/*     */   {
/* 827 */     return fixWhiteSpace(true, true, false);
/*     */   }
/*     */ 
/*     */   private static boolean isSpace(char ch)
/*     */   {
/* 839 */     return XMLCharacterRecognizer.isWhiteSpace(ch);
/*     */   }
/*     */ 
/*     */   public XMLString fixWhiteSpace(boolean trimHead, boolean trimTail, boolean doublePunctuationSpaces)
/*     */   {
/* 860 */     int end = this.m_length + this.m_start;
/* 861 */     char[] buf = new char[this.m_length];
/* 862 */     FastStringBuffer fsb = fsb();
/* 863 */     boolean edit = false;
/*     */ 
/* 866 */     int d = 0;
/* 867 */     boolean pres = false;
/*     */ 
/* 869 */     for (int s = this.m_start; s < end; s++)
/*     */     {
/* 871 */       char c = fsb.charAt(s);
/*     */ 
/* 873 */       if (isSpace(c))
/*     */       {
/* 875 */         if (!pres)
/*     */         {
/* 877 */           if (' ' != c)
/*     */           {
/* 879 */             edit = true;
/*     */           }
/*     */ 
/* 882 */           buf[(d++)] = ' ';
/*     */ 
/* 884 */           if ((doublePunctuationSpaces) && (d != 0))
/*     */           {
/* 886 */             char prevChar = buf[(d - 1)];
/*     */ 
/* 888 */             if ((prevChar != '.') && (prevChar != '!') && (prevChar != '?'))
/*     */             {
/* 891 */               pres = true;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 896 */             pres = true;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 901 */           edit = true;
/* 902 */           pres = true;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 907 */         buf[(d++)] = c;
/* 908 */         pres = false;
/*     */       }
/*     */     }
/*     */ 
/* 912 */     if ((trimTail) && (1 <= d) && (' ' == buf[(d - 1)]))
/*     */     {
/* 914 */       edit = true;
/*     */ 
/* 916 */       d--;
/*     */     }
/*     */ 
/* 919 */     int start = 0;
/*     */ 
/* 921 */     if ((trimHead) && (0 < d) && (' ' == buf[0]))
/*     */     {
/* 923 */       edit = true;
/*     */ 
/* 925 */       start++;
/*     */     }
/*     */ 
/* 928 */     XMLStringFactory xsf = XMLStringFactoryImpl.getFactory();
/*     */ 
/* 930 */     return edit ? xsf.newstr(buf, start, d - start) : this;
/*     */   }
/*     */ 
/*     */   public double toDouble()
/*     */   {
/* 951 */     if (this.m_length == 0) {
/* 952 */       return (0.0D / 0.0D);
/*     */     }
/*     */ 
/* 955 */     String valueString = fsb().getString(this.m_start, this.m_length);
/*     */ 
/* 964 */     for (int i = 0; (i < this.m_length) && 
/* 965 */       (XMLCharacterRecognizer.isWhiteSpace(valueString.charAt(i))); i++);
/* 967 */     if (i == this.m_length) return (0.0D / 0.0D);
/* 968 */     if (valueString.charAt(i) == '-')
/* 969 */       i++;
/* 970 */     for (; i < this.m_length; i++) {
/* 971 */       char c = valueString.charAt(i);
/* 972 */       if ((c != '.') && ((c < '0') || (c > '9')))
/*     */         break;
/*     */     }
/* 975 */     while ((i < this.m_length) && 
/* 976 */       (XMLCharacterRecognizer.isWhiteSpace(valueString.charAt(i)))) {
/* 975 */       i++;
/*     */     }
/*     */ 
/* 978 */     if (i != this.m_length)
/* 979 */       return (0.0D / 0.0D);
/*     */     try
/*     */     {
/* 982 */       return Double.parseDouble(valueString);
/*     */     } catch (NumberFormatException nfe) {
/*     */     }
/* 985 */     return (0.0D / 0.0D);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XStringForFSB
 * JD-Core Version:    0.6.2
 */