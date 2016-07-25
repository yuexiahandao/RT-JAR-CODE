/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import com.sun.org.apache.xpath.internal.objects.XString;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FuncSubstring extends Function3Args
/*     */ {
/*     */   static final long serialVersionUID = -5996676095024715502L;
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*  51 */     XMLString s1 = this.m_arg0.execute(xctxt).xstr();
/*  52 */     double start = this.m_arg1.execute(xctxt).num();
/*  53 */     int lenOfS1 = s1.length();
/*     */ 
/*  56 */     if (lenOfS1 <= 0)
/*  57 */       return XString.EMPTYSTRING;
/*     */     int startIndex;
/*     */     int startIndex;
/*  62 */     if (Double.isNaN(start))
/*     */     {
/*  67 */       start = -1000000.0D;
/*  68 */       startIndex = 0;
/*     */     }
/*     */     else
/*     */     {
/*  72 */       start = Math.round(start);
/*  73 */       startIndex = start > 0.0D ? (int)start - 1 : 0;
/*     */     }
/*     */     XMLString substr;
/*     */     XMLString substr;
/*  76 */     if (null != this.m_arg2)
/*     */     {
/*  78 */       double len = this.m_arg2.num(xctxt);
/*  79 */       int end = (int)(Math.round(len) + start) - 1;
/*     */ 
/*  82 */       if (end < 0)
/*  83 */         end = 0;
/*  84 */       else if (end > lenOfS1) {
/*  85 */         end = lenOfS1;
/*     */       }
/*  87 */       if (startIndex > lenOfS1) {
/*  88 */         startIndex = lenOfS1;
/*     */       }
/*  90 */       substr = s1.substring(startIndex, end);
/*     */     }
/*     */     else
/*     */     {
/*  94 */       if (startIndex > lenOfS1)
/*  95 */         startIndex = lenOfS1;
/*  96 */       substr = s1.substring(startIndex);
/*     */     }
/*     */ 
/* 100 */     return (XString)substr;
/*     */   }
/*     */ 
/*     */   public void checkNumberArgs(int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/* 113 */     if (argNum < 2)
/* 114 */       reportWrongNumberArgs();
/*     */   }
/*     */ 
/*     */   protected void reportWrongNumberArgs()
/*     */     throws WrongNumberArgsException
/*     */   {
/* 124 */     throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("ER_TWO_OR_THREE", null));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncSubstring
 * JD-Core Version:    0.6.2
 */