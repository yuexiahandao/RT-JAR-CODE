/*     */ package com.sun.org.apache.xpath.internal;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class XPathException extends TransformerException
/*     */ {
/*     */   static final long serialVersionUID = 4263549717619045963L;
/*  44 */   Object m_styleNode = null;
/*     */   protected Exception m_exception;
/*     */ 
/*     */   public Object getStylesheetNode()
/*     */   {
/*  52 */     return this.m_styleNode;
/*     */   }
/*     */ 
/*     */   public void setStylesheetNode(Object styleNode)
/*     */   {
/*  61 */     this.m_styleNode = styleNode;
/*     */   }
/*     */ 
/*     */   public XPathException(String message, ExpressionNode ex)
/*     */   {
/*  76 */     super(message);
/*  77 */     setLocator(ex);
/*  78 */     setStylesheetNode(getStylesheetNode(ex));
/*     */   }
/*     */ 
/*     */   public XPathException(String message)
/*     */   {
/*  88 */     super(message);
/*     */   }
/*     */ 
/*     */   public Node getStylesheetNode(ExpressionNode ex)
/*     */   {
/* 100 */     ExpressionNode owner = getExpressionOwner(ex);
/*     */ 
/* 102 */     if ((null != owner) && ((owner instanceof Node)))
/*     */     {
/* 104 */       return (Node)owner;
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   protected ExpressionNode getExpressionOwner(ExpressionNode ex)
/*     */   {
/* 116 */     ExpressionNode parent = ex.exprGetParent();
/* 117 */     while ((null != parent) && ((parent instanceof Expression)))
/* 118 */       parent = parent.exprGetParent();
/* 119 */     return parent;
/*     */   }
/*     */ 
/*     */   public XPathException(String message, Object styleNode)
/*     */   {
/* 134 */     super(message);
/*     */ 
/* 136 */     this.m_styleNode = styleNode;
/*     */   }
/*     */ 
/*     */   public XPathException(String message, Node styleNode, Exception e)
/*     */   {
/* 151 */     super(message);
/*     */ 
/* 153 */     this.m_styleNode = styleNode;
/* 154 */     this.m_exception = e;
/*     */   }
/*     */ 
/*     */   public XPathException(String message, Exception e)
/*     */   {
/* 167 */     super(message);
/*     */ 
/* 169 */     this.m_exception = e;
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream s)
/*     */   {
/* 181 */     if (s == null) {
/* 182 */       s = System.err;
/*     */     }
/*     */     try
/*     */     {
/* 186 */       super.printStackTrace(s);
/*     */     }
/*     */     catch (Exception e) {
/*     */     }
/* 190 */     Throwable exception = this.m_exception;
/*     */ 
/* 192 */     for (int i = 0; (i < 10) && (null != exception); i++)
/*     */     {
/* 194 */       s.println("---------");
/* 195 */       exception.printStackTrace(s);
/*     */ 
/* 197 */       if ((exception instanceof TransformerException))
/*     */       {
/* 199 */         TransformerException se = (TransformerException)exception;
/* 200 */         Throwable prev = exception;
/*     */ 
/* 202 */         exception = se.getException();
/*     */ 
/* 204 */         if (prev == exception)
/*     */           break;
/*     */       }
/*     */       else
/*     */       {
/* 209 */         exception = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 222 */     String lastMessage = super.getMessage();
/* 223 */     Throwable exception = this.m_exception;
/*     */ 
/* 225 */     while (null != exception)
/*     */     {
/* 227 */       String nextMessage = exception.getMessage();
/*     */ 
/* 229 */       if (null != nextMessage) {
/* 230 */         lastMessage = nextMessage;
/*     */       }
/* 232 */       if ((exception instanceof TransformerException))
/*     */       {
/* 234 */         TransformerException se = (TransformerException)exception;
/* 235 */         Throwable prev = exception;
/*     */ 
/* 237 */         exception = se.getException();
/*     */ 
/* 239 */         if (prev == exception)
/*     */           break;
/*     */       }
/*     */       else
/*     */       {
/* 244 */         exception = null;
/*     */       }
/*     */     }
/*     */ 
/* 248 */     return null != lastMessage ? lastMessage : "";
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter s)
/*     */   {
/* 260 */     if (s == null) {
/* 261 */       s = new PrintWriter(System.err);
/*     */     }
/*     */     try
/*     */     {
/* 265 */       super.printStackTrace(s);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/* 270 */     boolean isJdk14OrHigher = false;
/*     */     try {
/* 272 */       Throwable.class.getMethod("getCause", (Class[])null);
/* 273 */       isJdk14OrHigher = true;
/*     */     }
/*     */     catch (NoSuchMethodException nsme)
/*     */     {
/*     */     }
/*     */ 
/* 281 */     if (!isJdk14OrHigher)
/*     */     {
/* 283 */       Throwable exception = this.m_exception;
/*     */ 
/* 285 */       for (int i = 0; (i < 10) && (null != exception); i++)
/*     */       {
/* 287 */         s.println("---------");
/*     */         try
/*     */         {
/* 291 */           exception.printStackTrace(s);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 295 */           s.println("Could not print stack trace...");
/*     */         }
/*     */ 
/* 298 */         if ((exception instanceof TransformerException))
/*     */         {
/* 300 */           TransformerException se = (TransformerException)exception;
/* 301 */           Throwable prev = exception;
/*     */ 
/* 303 */           exception = se.getException();
/*     */ 
/* 305 */           if (prev == exception)
/*     */           {
/* 307 */             exception = null;
/*     */ 
/* 309 */             break;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 314 */           exception = null;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Throwable getException()
/*     */   {
/* 328 */     return this.m_exception;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.XPathException
 * JD-Core Version:    0.6.2
 */