/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public class XStringForChars extends XString
/*     */ {
/*     */   static final long serialVersionUID = -2235248887220850467L;
/*     */   int m_start;
/*     */   int m_length;
/*  42 */   protected String m_strCache = null;
/*     */ 
/*     */   public XStringForChars(char[] val, int start, int length)
/*     */   {
/*  53 */     super(val);
/*  54 */     this.m_start = start;
/*  55 */     this.m_length = length;
/*  56 */     if (null == val)
/*  57 */       throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_FASTSTRINGBUFFER_CANNOT_BE_NULL", null));
/*     */   }
/*     */ 
/*     */   private XStringForChars(String val)
/*     */   {
/*  69 */     super(val);
/*  70 */     throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING", null));
/*     */   }
/*     */ 
/*     */   public FastStringBuffer fsb()
/*     */   {
/*  81 */     throw new RuntimeException(XSLMessages.createXPATHMessage("ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS", null));
/*     */   }
/*     */ 
/*     */   public void appendToFsb(FastStringBuffer fsb)
/*     */   {
/*  91 */     fsb.append((char[])this.m_obj, this.m_start, this.m_length);
/*     */   }
/*     */ 
/*     */   public boolean hasString()
/*     */   {
/* 102 */     return null != this.m_strCache;
/*     */   }
/*     */ 
/*     */   public String str()
/*     */   {
/* 113 */     if (null == this.m_strCache) {
/* 114 */       this.m_strCache = new String((char[])this.m_obj, this.m_start, this.m_length);
/*     */     }
/* 116 */     return this.m_strCache;
/*     */   }
/*     */ 
/*     */   public Object object()
/*     */   {
/* 128 */     return str();
/*     */   }
/*     */ 
/*     */   public void dispatchCharactersEvents(ContentHandler ch)
/*     */     throws SAXException
/*     */   {
/* 145 */     ch.characters((char[])this.m_obj, this.m_start, this.m_length);
/*     */   }
/*     */ 
/*     */   public void dispatchAsComment(LexicalHandler lh)
/*     */     throws SAXException
/*     */   {
/* 160 */     lh.comment((char[])this.m_obj, this.m_start, this.m_length);
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 171 */     return this.m_length;
/*     */   }
/*     */ 
/*     */   public char charAt(int index)
/*     */   {
/* 189 */     return ((char[])(char[])this.m_obj)[(index + this.m_start)];
/*     */   }
/*     */ 
/*     */   public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
/*     */   {
/* 215 */     System.arraycopy((char[])this.m_obj, this.m_start + srcBegin, dst, dstBegin, srcEnd);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XStringForChars
 * JD-Core Version:    0.6.2
 */