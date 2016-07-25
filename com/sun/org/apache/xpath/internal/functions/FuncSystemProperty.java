/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import com.sun.org.apache.xpath.internal.objects.XString;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Properties;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FuncSystemProperty extends FunctionOneArg
/*     */ {
/*     */   static final long serialVersionUID = 3694874980992204867L;
/*     */   static final String XSLT_PROPERTIES = "com/sun/org/apache/xalan/internal/res/XSLTInfo.properties";
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*  63 */     String fullName = this.m_arg0.execute(xctxt).str();
/*  64 */     int indexOfNSSep = fullName.indexOf(':');
/*     */ 
/*  66 */     String propName = "";
/*     */ 
/*  70 */     Properties xsltInfo = new Properties();
/*     */ 
/*  72 */     loadPropertyFile("com/sun/org/apache/xalan/internal/res/XSLTInfo.properties", xsltInfo);
/*     */     String result;
/*  74 */     if (indexOfNSSep > 0)
/*     */     {
/*  76 */       String prefix = indexOfNSSep >= 0 ? fullName.substring(0, indexOfNSSep) : "";
/*     */ 
/*  80 */       String namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
/*  81 */       propName = indexOfNSSep < 0 ? fullName : fullName.substring(indexOfNSSep + 1);
/*     */ 
/*  84 */       if ((namespace.startsWith("http://www.w3.org/XSL/Transform")) || (namespace.equals("http://www.w3.org/1999/XSL/Transform")))
/*     */       {
/*  87 */         String result = xsltInfo.getProperty(propName);
/*     */ 
/*  89 */         if (null == result)
/*     */         {
/*  91 */           warn(xctxt, "WG_PROPERTY_NOT_SUPPORTED", new Object[] { fullName });
/*     */ 
/*  94 */           return XString.EMPTYSTRING;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  99 */         warn(xctxt, "WG_DONT_DO_ANYTHING_WITH_NS", new Object[] { namespace, fullName });
/*     */         try
/*     */         {
/* 105 */           String result = SecuritySupport.getSystemProperty(propName);
/*     */ 
/* 107 */           if (null == result)
/*     */           {
/* 111 */             return XString.EMPTYSTRING;
/*     */           }
/*     */         }
/*     */         catch (SecurityException se)
/*     */         {
/* 116 */           warn(xctxt, "WG_SECURITY_EXCEPTION", new Object[] { fullName });
/*     */ 
/* 119 */           return XString.EMPTYSTRING;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 127 */         result = SecuritySupport.getSystemProperty(fullName);
/*     */ 
/* 129 */         if (null == result)
/*     */         {
/* 133 */           return XString.EMPTYSTRING;
/*     */         }
/*     */       }
/*     */       catch (SecurityException se)
/*     */       {
/* 138 */         warn(xctxt, "WG_SECURITY_EXCEPTION", new Object[] { fullName });
/*     */ 
/* 141 */         return XString.EMPTYSTRING;
/*     */       }
/*     */     }
/*     */ 
/* 145 */     if ((propName.equals("version")) && (result.length() > 0))
/*     */     {
/*     */       try
/*     */       {
/* 150 */         return new XString("1.0");
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 154 */         return new XString(result);
/*     */       }
/*     */     }
/*     */ 
/* 158 */     return new XString(result);
/*     */   }
/*     */ 
/*     */   public void loadPropertyFile(String file, Properties target)
/*     */   {
/*     */     try
/*     */     {
/* 173 */       InputStream is = SecuritySupport.getResourceAsStream(ObjectFactory.findClassLoader(), file);
/*     */ 
/* 177 */       BufferedInputStream bis = new BufferedInputStream(is);
/*     */ 
/* 179 */       target.load(bis);
/* 180 */       bis.close();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 185 */       throw new WrappedRuntimeException(ex);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncSystemProperty
 * JD-Core Version:    0.6.2
 */