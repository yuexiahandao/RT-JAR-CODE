/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class XRTreeFragSelectWrapper extends XRTreeFrag
/*     */   implements Cloneable
/*     */ {
/*     */   static final long serialVersionUID = -6526177905590461251L;
/*     */ 
/*     */   public XRTreeFragSelectWrapper(Expression expr)
/*     */   {
/*  40 */     super(expr);
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*  55 */     ((Expression)this.m_obj).fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*  71 */     XObject m_selected = ((Expression)this.m_obj).execute(xctxt);
/*  72 */     m_selected.allowDetachToRelease(this.m_allowRelease);
/*  73 */     if (m_selected.getType() == 3) {
/*  74 */       return m_selected;
/*     */     }
/*  76 */     return new XString(m_selected.str());
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/*  90 */     throw new RuntimeException(XSLMessages.createXPATHMessage("ER_DETACH_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
/*     */   }
/*     */ 
/*     */   public double num()
/*     */     throws TransformerException
/*     */   {
/* 102 */     throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NUM_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
/*     */   }
/*     */ 
/*     */   public XMLString xstr()
/*     */   {
/* 113 */     throw new RuntimeException(XSLMessages.createXPATHMessage("ER_XSTR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
/*     */   }
/*     */ 
/*     */   public String str()
/*     */   {
/* 123 */     throw new RuntimeException(XSLMessages.createXPATHMessage("ER_STR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 133 */     return 3;
/*     */   }
/*     */ 
/*     */   public int rtf()
/*     */   {
/* 143 */     throw new RuntimeException(XSLMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
/*     */   }
/*     */ 
/*     */   public DTMIterator asNodeIterator()
/*     */   {
/* 153 */     throw new RuntimeException(XSLMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XRTreeFragSelectWrapper
 * JD-Core Version:    0.6.2
 */