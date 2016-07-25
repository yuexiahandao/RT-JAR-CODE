/*     */ package com.sun.org.apache.xpath.internal.operations;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.QName;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.VariableStack;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.axes.PathComponent;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class Variable extends Expression
/*     */   implements PathComponent
/*     */ {
/*     */   static final long serialVersionUID = -4334975375609297049L;
/*  49 */   private boolean m_fixUpWasCalled = false;
/*     */   protected QName m_qname;
/*     */   protected int m_index;
/* 107 */   protected boolean m_isGlobal = false;
/*     */   static final String PSUEDOVARNAMESPACE = "http://xml.apache.org/xalan/psuedovar";
/*     */ 
/*     */   public void setIndex(int index)
/*     */   {
/*  70 */     this.m_index = index;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/*  80 */     return this.m_index;
/*     */   }
/*     */ 
/*     */   public void setIsGlobal(boolean isGlobal)
/*     */   {
/*  90 */     this.m_isGlobal = isGlobal;
/*     */   }
/*     */ 
/*     */   public boolean getGlobal()
/*     */   {
/* 100 */     return this.m_isGlobal;
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 121 */     this.m_fixUpWasCalled = true;
/* 122 */     int sz = vars.size();
/*     */ 
/* 124 */     for (int i = vars.size() - 1; i >= 0; i--)
/*     */     {
/* 126 */       QName qn = (QName)vars.elementAt(i);
/*     */ 
/* 128 */       if (qn.equals(this.m_qname))
/*     */       {
/* 131 */         if (i < globalsSize)
/*     */         {
/* 133 */           this.m_isGlobal = true;
/* 134 */           this.m_index = i;
/*     */         }
/*     */         else
/*     */         {
/* 138 */           this.m_index = (i - globalsSize);
/*     */         }
/*     */ 
/* 141 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 145 */     String msg = XSLMessages.createXPATHMessage("ER_COULD_NOT_FIND_VAR", new Object[] { this.m_qname.toString() });
/*     */ 
/* 148 */     TransformerException te = new TransformerException(msg, this);
/*     */ 
/* 150 */     throw new WrappedRuntimeException(te);
/*     */   }
/*     */ 
/*     */   public void setQName(QName qname)
/*     */   {
/* 162 */     this.m_qname = qname;
/*     */   }
/*     */ 
/*     */   public QName getQName()
/*     */   {
/* 172 */     return this.m_qname;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 190 */     return execute(xctxt, false);
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, boolean destructiveOK)
/*     */     throws TransformerException
/*     */   {
/* 208 */     PrefixResolver xprefixResolver = xctxt.getNamespaceContext();
/*     */     XObject result;
/*     */     XObject result;
/* 213 */     if (this.m_fixUpWasCalled)
/*     */     {
/*     */       XObject result;
/* 215 */       if (this.m_isGlobal)
/* 216 */         result = xctxt.getVarStack().getGlobalVariable(xctxt, this.m_index, destructiveOK);
/*     */       else
/* 218 */         result = xctxt.getVarStack().getLocalVariable(xctxt, this.m_index, destructiveOK);
/*     */     }
/*     */     else {
/* 221 */       result = xctxt.getVarStack().getVariableOrParam(xctxt, this.m_qname);
/*     */     }
/*     */ 
/* 224 */     if (null == result)
/*     */     {
/* 227 */       warn(xctxt, "WG_ILLEGAL_VARIABLE_REFERENCE", new Object[] { this.m_qname.getLocalPart() });
/*     */ 
/* 233 */       result = new XNodeSet(xctxt.getDTMManager());
/*     */     }
/*     */ 
/* 236 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean isStableNumber()
/*     */   {
/* 321 */     return true;
/*     */   }
/*     */ 
/*     */   public int getAnalysisBits()
/*     */   {
/* 348 */     return 67108864;
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 357 */     visitor.visitVariableRef(owner, this);
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 364 */     if (!isSameClass(expr)) {
/* 365 */       return false;
/*     */     }
/* 367 */     if (!this.m_qname.equals(((Variable)expr).m_qname)) {
/* 368 */       return false;
/*     */     }
/*     */ 
/* 378 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isPsuedoVarRef()
/*     */   {
/* 389 */     String ns = this.m_qname.getNamespaceURI();
/* 390 */     if ((null != ns) && (ns.equals("http://xml.apache.org/xalan/psuedovar")))
/*     */     {
/* 392 */       if (this.m_qname.getLocalName().startsWith("#"))
/* 393 */         return true;
/*     */     }
/* 395 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.Variable
 * JD-Core Version:    0.6.2
 */