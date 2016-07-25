/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExtensionsProvider;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.compiler.FunctionTable;
/*     */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FuncExtFunctionAvailable extends FunctionOneArg
/*     */ {
/*     */   static final long serialVersionUID = 5118814314918592241L;
/*  40 */   private transient FunctionTable m_functionTable = null;
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*  57 */     String fullName = this.m_arg0.execute(xctxt).str();
/*  58 */     int indexOfNSSep = fullName.indexOf(':');
/*     */     String methName;
/*     */     String namespace;
/*     */     String methName;
/*  60 */     if (indexOfNSSep < 0)
/*     */     {
/*  62 */       String prefix = "";
/*  63 */       String namespace = "http://www.w3.org/1999/XSL/Transform";
/*  64 */       methName = fullName;
/*     */     }
/*     */     else
/*     */     {
/*  68 */       String prefix = fullName.substring(0, indexOfNSSep);
/*  69 */       namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
/*  70 */       if (null == namespace)
/*  71 */         return XBoolean.S_FALSE;
/*  72 */       methName = fullName.substring(indexOfNSSep + 1);
/*     */     }
/*     */ 
/*  75 */     if (namespace.equals("http://www.w3.org/1999/XSL/Transform"))
/*     */     {
/*     */       try
/*     */       {
/*  79 */         if (null == this.m_functionTable) this.m_functionTable = new FunctionTable();
/*  80 */         return this.m_functionTable.functionAvailable(methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*  84 */         return XBoolean.S_FALSE;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  90 */     ExtensionsProvider extProvider = (ExtensionsProvider)xctxt.getOwnerObject();
/*  91 */     return extProvider.functionAvailable(namespace, methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
/*     */   }
/*     */ 
/*     */   public void setFunctionTable(FunctionTable aTable)
/*     */   {
/* 104 */     this.m_functionTable = aTable;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncExtFunctionAvailable
 * JD-Core Version:    0.6.2
 */