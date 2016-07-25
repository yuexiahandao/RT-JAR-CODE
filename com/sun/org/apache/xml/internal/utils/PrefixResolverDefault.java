/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class PrefixResolverDefault
/*     */   implements PrefixResolver
/*     */ {
/*     */   Node m_context;
/*     */ 
/*     */   public PrefixResolverDefault(Node xpathExpressionContext)
/*     */   {
/*  52 */     this.m_context = xpathExpressionContext;
/*     */   }
/*     */ 
/*     */   public String getNamespaceForPrefix(String prefix)
/*     */   {
/*  65 */     return getNamespaceForPrefix(prefix, this.m_context);
/*     */   }
/*     */ 
/*     */   public String getNamespaceForPrefix(String prefix, Node namespaceContext)
/*     */   {
/*  82 */     Node parent = namespaceContext;
/*  83 */     String namespace = null;
/*     */ 
/*  85 */     if (prefix.equals("xml"))
/*     */     {
/*  87 */       namespace = "http://www.w3.org/XML/1998/namespace";
/*     */     }
/*     */     else
/*     */     {
/*     */       int type;
/*  94 */       while ((null != parent) && (null == namespace) && (((type = parent.getNodeType()) == 1) || (type == 5)))
/*     */       {
/*  97 */         if (type == 1)
/*     */         {
/*  99 */           if (parent.getNodeName().indexOf(prefix + ":") == 0)
/* 100 */             return parent.getNamespaceURI();
/* 101 */           NamedNodeMap nnm = parent.getAttributes();
/*     */ 
/* 103 */           for (int i = 0; i < nnm.getLength(); i++)
/*     */           {
/* 105 */             Node attr = nnm.item(i);
/* 106 */             String aname = attr.getNodeName();
/* 107 */             boolean isPrefix = aname.startsWith("xmlns:");
/*     */ 
/* 109 */             if ((isPrefix) || (aname.equals("xmlns")))
/*     */             {
/* 111 */               int index = aname.indexOf(':');
/* 112 */               String p = isPrefix ? aname.substring(index + 1) : "";
/*     */ 
/* 114 */               if (p.equals(prefix))
/*     */               {
/* 116 */                 namespace = attr.getNodeValue();
/*     */ 
/* 118 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 124 */         parent = parent.getParentNode();
/*     */       }
/*     */     }
/*     */ 
/* 128 */     return namespace;
/*     */   }
/*     */ 
/*     */   public String getBaseIdentifier()
/*     */   {
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean handlesNullPrefixes()
/*     */   {
/* 144 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.PrefixResolverDefault
 * JD-Core Version:    0.6.2
 */