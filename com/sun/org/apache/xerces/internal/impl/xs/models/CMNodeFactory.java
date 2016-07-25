/*     */ package com.sun.org.apache.xerces.internal.impl.xs.models;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.Limit;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ 
/*     */ public class CMNodeFactory
/*     */ {
/*     */   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*     */   private static final boolean DEBUG = false;
/*     */   private static final int MULTIPLICITY = 1;
/*  57 */   private int nodeCount = 0;
/*     */   private int maxNodeLimit;
/*     */   private XMLErrorReporter fErrorReporter;
/*  71 */   private XMLSecurityManager fSecurityManager = null;
/*     */ 
/*     */   public void reset(XMLComponentManager componentManager)
/*     */   {
/*  78 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*     */     try {
/*  80 */       this.fSecurityManager = ((XMLSecurityManager)componentManager.getProperty("http://apache.org/xml/properties/security-manager"));
/*     */ 
/*  82 */       if (this.fSecurityManager != null)
/*  83 */         this.maxNodeLimit = (this.fSecurityManager.getLimit(XMLSecurityManager.Limit.MAX_OCCUR_NODE_LIMIT) * 1);
/*     */     }
/*     */     catch (XMLConfigurationException e)
/*     */     {
/*  87 */       this.fSecurityManager = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public CMNode getCMLeafNode(int type, Object leaf, int id, int position)
/*     */   {
/*  93 */     return new XSCMLeaf(type, leaf, id, position);
/*     */   }
/*     */ 
/*     */   public CMNode getCMRepeatingLeafNode(int type, Object leaf, int minOccurs, int maxOccurs, int id, int position)
/*     */   {
/*  98 */     nodeCountCheck();
/*  99 */     return new XSCMRepeatingLeaf(type, leaf, minOccurs, maxOccurs, id, position);
/*     */   }
/*     */ 
/*     */   public CMNode getCMUniOpNode(int type, CMNode childNode) {
/* 103 */     nodeCountCheck();
/* 104 */     return new XSCMUniOp(type, childNode);
/*     */   }
/*     */ 
/*     */   public CMNode getCMBinOpNode(int type, CMNode leftNode, CMNode rightNode) {
/* 108 */     return new XSCMBinOp(type, leftNode, rightNode);
/*     */   }
/*     */ 
/*     */   public void nodeCountCheck() {
/* 112 */     if ((this.fSecurityManager != null) && (!this.fSecurityManager.isNoLimit(this.maxNodeLimit)) && (this.nodeCount++ > this.maxNodeLimit))
/*     */     {
/* 118 */       this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "maxOccurLimit", new Object[] { new Integer(this.maxNodeLimit) }, (short)2);
/*     */ 
/* 121 */       this.nodeCount = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resetNodeCount()
/*     */   {
/* 128 */     this.nodeCount = 0;
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 149 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 150 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*     */ 
/* 152 */       if ((suffixLength == "security-manager".length()) && (propertyId.endsWith("security-manager")))
/*     */       {
/* 154 */         this.fSecurityManager = ((XMLSecurityManager)value);
/* 155 */         this.maxNodeLimit = (this.fSecurityManager != null ? this.fSecurityManager.getLimit(XMLSecurityManager.Limit.MAX_OCCUR_NODE_LIMIT) * 1 : 0);
/*     */ 
/* 157 */         return;
/*     */       }
/* 159 */       if ((suffixLength == "internal/error-reporter".length()) && (propertyId.endsWith("internal/error-reporter")))
/*     */       {
/* 161 */         this.fErrorReporter = ((XMLErrorReporter)value);
/* 162 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.models.CMNodeFactory
 * JD-Core Version:    0.6.2
 */