/*     */ package com.sun.org.apache.xpath.internal;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.Objects;
/*     */ import com.sun.org.apache.xml.internal.utils.QName;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ 
/*     */ public class Arg
/*     */ {
/*     */   private QName m_qname;
/*     */   private XObject m_val;
/*     */   private String m_expression;
/*     */   private boolean m_isFromWithParam;
/*     */   private boolean m_isVisible;
/*     */ 
/*     */   public final QName getQName()
/*     */   {
/*  52 */     return this.m_qname;
/*     */   }
/*     */ 
/*     */   public final void setQName(QName name)
/*     */   {
/*  62 */     this.m_qname = name;
/*     */   }
/*     */ 
/*     */   public final XObject getVal()
/*     */   {
/*  79 */     return this.m_val;
/*     */   }
/*     */ 
/*     */   public final void setVal(XObject val)
/*     */   {
/*  90 */     this.m_val = val;
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/*  99 */     if (null != this.m_val)
/*     */     {
/* 101 */       this.m_val.allowDetachToRelease(true);
/* 102 */       this.m_val.detach();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getExpression()
/*     */   {
/* 122 */     return this.m_expression;
/*     */   }
/*     */ 
/*     */   public void setExpression(String expr)
/*     */   {
/* 134 */     this.m_expression = expr;
/*     */   }
/*     */ 
/*     */   public boolean isFromWithParam()
/*     */   {
/* 149 */     return this.m_isFromWithParam;
/*     */   }
/*     */ 
/*     */   public boolean isVisible()
/*     */   {
/* 166 */     return this.m_isVisible;
/*     */   }
/*     */ 
/*     */   public void setIsVisible(boolean b)
/*     */   {
/* 174 */     this.m_isVisible = b;
/*     */   }
/*     */ 
/*     */   public Arg()
/*     */   {
/* 185 */     this.m_qname = new QName("");
/*     */ 
/* 187 */     this.m_val = null;
/* 188 */     this.m_expression = null;
/* 189 */     this.m_isVisible = true;
/* 190 */     this.m_isFromWithParam = false;
/*     */   }
/*     */ 
/*     */   public Arg(QName qname, String expression, boolean isFromWithParam)
/*     */   {
/* 203 */     this.m_qname = qname;
/* 204 */     this.m_val = null;
/* 205 */     this.m_expression = expression;
/* 206 */     this.m_isFromWithParam = isFromWithParam;
/* 207 */     this.m_isVisible = (!isFromWithParam);
/*     */   }
/*     */ 
/*     */   public Arg(QName qname, XObject val)
/*     */   {
/* 220 */     this.m_qname = qname;
/* 221 */     this.m_val = val;
/* 222 */     this.m_isVisible = true;
/* 223 */     this.m_isFromWithParam = false;
/* 224 */     this.m_expression = null;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 229 */     return Objects.hashCode(this.m_qname);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 243 */     if ((obj instanceof QName))
/*     */     {
/* 245 */       return this.m_qname.equals(obj);
/*     */     }
/*     */ 
/* 248 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */   public Arg(QName qname, XObject val, boolean isFromWithParam)
/*     */   {
/* 261 */     this.m_qname = qname;
/* 262 */     this.m_val = val;
/* 263 */     this.m_isFromWithParam = isFromWithParam;
/* 264 */     this.m_isVisible = (!isFromWithParam);
/* 265 */     this.m_expression = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.Arg
 * JD-Core Version:    0.6.2
 */