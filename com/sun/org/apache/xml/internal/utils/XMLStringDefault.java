/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public class XMLStringDefault
/*     */   implements XMLString
/*     */ {
/*     */   private String m_str;
/*     */ 
/*     */   public XMLStringDefault(String str)
/*     */   {
/*  41 */     this.m_str = str;
/*     */   }
/*     */ 
/*     */   public void dispatchCharactersEvents(ContentHandler ch)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dispatchAsComment(LexicalHandler lh)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public XMLString fixWhiteSpace(boolean trimHead, boolean trimTail, boolean doublePunctuationSpaces)
/*     */   {
/*  92 */     return new XMLStringDefault(this.m_str.trim());
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 103 */     return this.m_str.length();
/*     */   }
/*     */ 
/*     */   public char charAt(int index)
/*     */   {
/* 121 */     return this.m_str.charAt(index);
/*     */   }
/*     */ 
/*     */   public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
/*     */   {
/* 148 */     int destIndex = dstBegin;
/* 149 */     for (int i = srcBegin; i < srcEnd; i++)
/*     */     {
/* 151 */       dst[(destIndex++)] = this.m_str.charAt(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(String obj2)
/*     */   {
/* 168 */     return this.m_str.equals(obj2);
/*     */   }
/*     */ 
/*     */   public boolean equals(XMLString anObject)
/*     */   {
/* 186 */     return this.m_str.equals(anObject.toString());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object anObject)
/*     */   {
/* 205 */     return this.m_str.equals(anObject);
/*     */   }
/*     */ 
/*     */   public boolean equalsIgnoreCase(String anotherString)
/*     */   {
/* 225 */     return this.m_str.equalsIgnoreCase(anotherString);
/*     */   }
/*     */ 
/*     */   public int compareTo(XMLString anotherString)
/*     */   {
/* 242 */     return this.m_str.compareTo(anotherString.toString());
/*     */   }
/*     */ 
/*     */   public int compareToIgnoreCase(XMLString str)
/*     */   {
/* 265 */     return this.m_str.compareToIgnoreCase(str.toString());
/*     */   }
/*     */ 
/*     */   public boolean startsWith(String prefix, int toffset)
/*     */   {
/* 289 */     return this.m_str.startsWith(prefix, toffset);
/*     */   }
/*     */ 
/*     */   public boolean startsWith(XMLString prefix, int toffset)
/*     */   {
/* 313 */     return this.m_str.startsWith(prefix.toString(), toffset);
/*     */   }
/*     */ 
/*     */   public boolean startsWith(String prefix)
/*     */   {
/* 333 */     return this.m_str.startsWith(prefix);
/*     */   }
/*     */ 
/*     */   public boolean startsWith(XMLString prefix)
/*     */   {
/* 353 */     return this.m_str.startsWith(prefix.toString());
/*     */   }
/*     */ 
/*     */   public boolean endsWith(String suffix)
/*     */   {
/* 371 */     return this.m_str.endsWith(suffix);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 389 */     return this.m_str.hashCode();
/*     */   }
/*     */ 
/*     */   public int indexOf(int ch)
/*     */   {
/* 411 */     return this.m_str.indexOf(ch);
/*     */   }
/*     */ 
/*     */   public int indexOf(int ch, int fromIndex)
/*     */   {
/* 444 */     return this.m_str.indexOf(ch, fromIndex);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(int ch)
/*     */   {
/* 464 */     return this.m_str.lastIndexOf(ch);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(int ch, int fromIndex)
/*     */   {
/* 492 */     return this.m_str.lastIndexOf(ch, fromIndex);
/*     */   }
/*     */ 
/*     */   public int indexOf(String str)
/*     */   {
/* 514 */     return this.m_str.indexOf(str);
/*     */   }
/*     */ 
/*     */   public int indexOf(XMLString str)
/*     */   {
/* 536 */     return this.m_str.indexOf(str.toString());
/*     */   }
/*     */ 
/*     */   public int indexOf(String str, int fromIndex)
/*     */   {
/* 567 */     return this.m_str.indexOf(str, fromIndex);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(String str)
/*     */   {
/* 590 */     return this.m_str.lastIndexOf(str);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(String str, int fromIndex)
/*     */   {
/* 615 */     return this.m_str.lastIndexOf(str, fromIndex);
/*     */   }
/*     */ 
/*     */   public XMLString substring(int beginIndex)
/*     */   {
/* 637 */     return new XMLStringDefault(this.m_str.substring(beginIndex));
/*     */   }
/*     */ 
/*     */   public XMLString substring(int beginIndex, int endIndex)
/*     */   {
/* 658 */     return new XMLStringDefault(this.m_str.substring(beginIndex, endIndex));
/*     */   }
/*     */ 
/*     */   public XMLString concat(String str)
/*     */   {
/* 673 */     return new XMLStringDefault(this.m_str.concat(str));
/*     */   }
/*     */ 
/*     */   public XMLString toLowerCase(Locale locale)
/*     */   {
/* 687 */     return new XMLStringDefault(this.m_str.toLowerCase(locale));
/*     */   }
/*     */ 
/*     */   public XMLString toLowerCase()
/*     */   {
/* 702 */     return new XMLStringDefault(this.m_str.toLowerCase());
/*     */   }
/*     */ 
/*     */   public XMLString toUpperCase(Locale locale)
/*     */   {
/* 715 */     return new XMLStringDefault(this.m_str.toUpperCase(locale));
/*     */   }
/*     */ 
/*     */   public XMLString toUpperCase()
/*     */   {
/* 746 */     return new XMLStringDefault(this.m_str.toUpperCase());
/*     */   }
/*     */ 
/*     */   public XMLString trim()
/*     */   {
/* 780 */     return new XMLStringDefault(this.m_str.trim());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 790 */     return this.m_str;
/*     */   }
/*     */ 
/*     */   public boolean hasString()
/*     */   {
/* 800 */     return true;
/*     */   }
/*     */ 
/*     */   public double toDouble()
/*     */   {
/*     */     try
/*     */     {
/* 813 */       return Double.valueOf(this.m_str).doubleValue();
/*     */     }
/*     */     catch (NumberFormatException nfe) {
/*     */     }
/* 817 */     return (0.0D / 0.0D);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.XMLStringDefault
 * JD-Core Version:    0.6.2
 */