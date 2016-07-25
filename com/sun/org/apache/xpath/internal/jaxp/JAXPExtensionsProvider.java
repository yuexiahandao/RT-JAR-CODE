/*     */ package com.sun.org.apache.xpath.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xalan.internal.utils.FeatureManager;
/*     */ import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.ExtensionsProvider;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncExtFunction;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Vector;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.xpath.XPathFunction;
/*     */ import javax.xml.xpath.XPathFunctionException;
/*     */ import javax.xml.xpath.XPathFunctionResolver;
/*     */ 
/*     */ public class JAXPExtensionsProvider
/*     */   implements ExtensionsProvider
/*     */ {
/*     */   private final XPathFunctionResolver resolver;
/*  50 */   private boolean extensionInvocationDisabled = false;
/*     */ 
/*     */   public JAXPExtensionsProvider(XPathFunctionResolver resolver) {
/*  53 */     this.resolver = resolver;
/*  54 */     this.extensionInvocationDisabled = false;
/*     */   }
/*     */ 
/*     */   public JAXPExtensionsProvider(XPathFunctionResolver resolver, boolean featureSecureProcessing, FeatureManager featureManager)
/*     */   {
/*  59 */     this.resolver = resolver;
/*  60 */     if ((featureSecureProcessing) && (!featureManager.isFeatureEnabled(FeatureManager.Feature.ORACLE_ENABLE_EXTENSION_FUNCTION)))
/*     */     {
/*  62 */       this.extensionInvocationDisabled = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean functionAvailable(String ns, String funcName)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/*  73 */       if (funcName == null) {
/*  74 */         String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "Function Name" });
/*     */ 
/*  77 */         throw new NullPointerException(fmsg);
/*     */       }
/*     */ 
/*  80 */       QName myQName = new QName(ns, funcName);
/*  81 */       XPathFunction xpathFunction = this.resolver.resolveFunction(myQName, 0);
/*     */ 
/*  83 */       if (xpathFunction == null) {
/*  84 */         return false;
/*     */       }
/*  86 */       return true; } catch (Exception e) {
/*     */     }
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean elementAvailable(String ns, String elemName)
/*     */     throws TransformerException
/*     */   {
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   public Object extFunction(String ns, String funcName, Vector argVec, Object methodKey)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 110 */       if (funcName == null) {
/* 111 */         String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "Function Name" });
/*     */ 
/* 114 */         throw new NullPointerException(fmsg);
/*     */       }
/*     */ 
/* 117 */       QName myQName = new QName(ns, funcName);
/*     */ 
/* 122 */       if (this.extensionInvocationDisabled) {
/* 123 */         String fmsg = XSLMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[] { myQName.toString() });
/*     */ 
/* 126 */         throw new XPathFunctionException(fmsg);
/*     */       }
/*     */ 
/* 131 */       int arity = argVec.size();
/*     */ 
/* 133 */       XPathFunction xpathFunction = this.resolver.resolveFunction(myQName, arity);
/*     */ 
/* 137 */       ArrayList argList = new ArrayList(arity);
/* 138 */       for (int i = 0; i < arity; i++) {
/* 139 */         Object argument = argVec.elementAt(i);
/*     */ 
/* 142 */         if ((argument instanceof XNodeSet)) {
/* 143 */           argList.add(i, ((XNodeSet)argument).nodelist());
/* 144 */         } else if ((argument instanceof XObject)) {
/* 145 */           Object passedArgument = ((XObject)argument).object();
/* 146 */           argList.add(i, passedArgument);
/*     */         } else {
/* 148 */           argList.add(i, argument);
/*     */         }
/*     */       }
/*     */ 
/* 152 */       return xpathFunction.evaluate(argList);
/*     */     }
/*     */     catch (XPathFunctionException xfe)
/*     */     {
/* 156 */       throw new WrappedRuntimeException(xfe);
/*     */     } catch (Exception e) {
/* 158 */       throw new TransformerException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object extFunction(FuncExtFunction extFunction, Vector argVec)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 170 */       String namespace = extFunction.getNamespace();
/* 171 */       String functionName = extFunction.getFunctionName();
/* 172 */       int arity = extFunction.getArgCount();
/* 173 */       QName myQName = new QName(namespace, functionName);
/*     */ 
/* 179 */       if (this.extensionInvocationDisabled) {
/* 180 */         String fmsg = XSLMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[] { myQName.toString() });
/*     */ 
/* 182 */         throw new XPathFunctionException(fmsg);
/*     */       }
/*     */ 
/* 185 */       XPathFunction xpathFunction = this.resolver.resolveFunction(myQName, arity);
/*     */ 
/* 188 */       ArrayList argList = new ArrayList(arity);
/* 189 */       for (int i = 0; i < arity; i++) {
/* 190 */         Object argument = argVec.elementAt(i);
/*     */ 
/* 193 */         if ((argument instanceof XNodeSet)) {
/* 194 */           argList.add(i, ((XNodeSet)argument).nodelist());
/* 195 */         } else if ((argument instanceof XObject)) {
/* 196 */           Object passedArgument = ((XObject)argument).object();
/* 197 */           argList.add(i, passedArgument);
/*     */         } else {
/* 199 */           argList.add(i, argument);
/*     */         }
/*     */       }
/*     */ 
/* 203 */       return xpathFunction.evaluate(argList);
/*     */     }
/*     */     catch (XPathFunctionException xfe)
/*     */     {
/* 208 */       throw new WrappedRuntimeException(xfe);
/*     */     } catch (Exception e) {
/* 210 */       throw new TransformerException(e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.jaxp.JAXPExtensionsProvider
 * JD-Core Version:    0.6.2
 */