/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionNode;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.ExtensionsProvider;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNull;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import com.sun.org.apache.xpath.internal.res.XPATHMessages;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FuncExtFunction extends Function
/*     */ {
/*     */   static final long serialVersionUID = 5196115554693708718L;
/*     */   String m_namespace;
/*     */   String m_extensionName;
/*     */   Object m_methodKey;
/*  74 */   Vector m_argVec = new Vector();
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*  90 */     if (null != this.m_argVec)
/*     */     {
/*  92 */       int nArgs = this.m_argVec.size();
/*     */ 
/*  94 */       for (int i = 0; i < nArgs; i++)
/*     */       {
/*  96 */         Expression arg = (Expression)this.m_argVec.elementAt(i);
/*     */ 
/*  98 */         arg.fixupVariables(vars, globalsSize);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 110 */     return this.m_namespace;
/*     */   }
/*     */ 
/*     */   public String getFunctionName()
/*     */   {
/* 120 */     return this.m_extensionName;
/*     */   }
/*     */ 
/*     */   public Object getMethodKey()
/*     */   {
/* 130 */     return this.m_methodKey;
/*     */   }
/*     */ 
/*     */   public Expression getArg(int n)
/*     */   {
/* 140 */     if ((n >= 0) && (n < this.m_argVec.size())) {
/* 141 */       return (Expression)this.m_argVec.elementAt(n);
/*     */     }
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   public int getArgCount()
/*     */   {
/* 153 */     return this.m_argVec.size();
/*     */   }
/*     */ 
/*     */   public FuncExtFunction(String namespace, String extensionName, Object methodKey)
/*     */   {
/* 171 */     this.m_namespace = namespace;
/* 172 */     this.m_extensionName = extensionName;
/* 173 */     this.m_methodKey = methodKey;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 187 */     if (xctxt.isSecureProcessing()) {
/* 188 */       throw new TransformerException(XPATHMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[] { toString() }));
/*     */     }
/*     */ 
/* 194 */     Vector argVec = new Vector();
/* 195 */     int nArgs = this.m_argVec.size();
/*     */ 
/* 197 */     for (int i = 0; i < nArgs; i++)
/*     */     {
/* 199 */       Expression arg = (Expression)this.m_argVec.elementAt(i);
/*     */ 
/* 201 */       XObject xobj = arg.execute(xctxt);
/*     */ 
/* 205 */       xobj.allowDetachToRelease(false);
/* 206 */       argVec.addElement(xobj);
/*     */     }
/*     */ 
/* 209 */     ExtensionsProvider extProvider = (ExtensionsProvider)xctxt.getOwnerObject();
/* 210 */     Object val = extProvider.extFunction(this, argVec);
/*     */     XObject result;
/*     */     XObject result;
/* 212 */     if (null != val)
/*     */     {
/* 214 */       result = XObject.create(val, xctxt);
/*     */     }
/*     */     else
/*     */     {
/* 218 */       result = new XNull();
/*     */     }
/*     */ 
/* 221 */     return result;
/*     */   }
/*     */ 
/*     */   public void setArg(Expression arg, int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/* 237 */     this.m_argVec.addElement(arg);
/* 238 */     arg.exprSetParent(this);
/*     */   }
/*     */ 
/*     */   public void checkNumberArgs(int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void callArgVisitors(XPathVisitor visitor)
/*     */   {
/* 287 */     for (int i = 0; i < this.m_argVec.size(); i++)
/*     */     {
/* 289 */       Expression exp = (Expression)this.m_argVec.elementAt(i);
/* 290 */       exp.callVisitors(new ArgExtOwner(exp), visitor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void exprSetParent(ExpressionNode n)
/*     */   {
/* 305 */     super.exprSetParent(n);
/*     */ 
/* 307 */     int nArgs = this.m_argVec.size();
/*     */ 
/* 309 */     for (int i = 0; i < nArgs; i++)
/*     */     {
/* 311 */       Expression arg = (Expression)this.m_argVec.elementAt(i);
/*     */ 
/* 313 */       arg.exprSetParent(n);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void reportWrongNumberArgs()
/*     */     throws WrongNumberArgsException
/*     */   {
/* 325 */     String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { "Programmer's assertion:  the method FunctionMultiArgs.reportWrongNumberArgs() should never be called." });
/*     */ 
/* 329 */     throw new RuntimeException(fMsg);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 337 */     if ((this.m_namespace != null) && (this.m_namespace.length() > 0)) {
/* 338 */       return "{" + this.m_namespace + "}" + this.m_extensionName;
/*     */     }
/* 340 */     return this.m_extensionName;
/*     */   }
/*     */ 
/*     */   class ArgExtOwner
/*     */     implements ExpressionOwner
/*     */   {
/*     */     Expression m_exp;
/*     */ 
/*     */     ArgExtOwner(Expression exp)
/*     */     {
/* 259 */       this.m_exp = exp;
/*     */     }
/*     */ 
/*     */     public Expression getExpression()
/*     */     {
/* 267 */       return this.m_exp;
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 276 */       exp.exprSetParent(FuncExtFunction.this);
/* 277 */       this.m_exp = exp;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncExtFunction
 * JD-Core Version:    0.6.2
 */