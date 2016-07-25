/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class XMLDSigRI extends Provider
/*     */ {
/*     */   static final long serialVersionUID = -5049765099299494554L;
/*     */   private static final String INFO = "XMLDSig (DOM XMLSignatureFactory; DOM KeyInfoFactory)";
/*     */ 
/*     */   public XMLDSigRI()
/*     */   {
/*  60 */     super("XMLDSig", 1.0D, "XMLDSig (DOM XMLSignatureFactory; DOM KeyInfoFactory)");
/*     */ 
/*  62 */     final HashMap localHashMap = new HashMap();
/*  63 */     localHashMap.put("XMLSignatureFactory.DOM", "org.jcp.xml.dsig.internal.dom.DOMXMLSignatureFactory");
/*     */ 
/*  65 */     localHashMap.put("KeyInfoFactory.DOM", "org.jcp.xml.dsig.internal.dom.DOMKeyInfoFactory");
/*     */ 
/*  70 */     localHashMap.put("TransformService.http://www.w3.org/TR/2001/REC-xml-c14n-20010315", "org.jcp.xml.dsig.internal.dom.DOMCanonicalXMLC14NMethod");
/*     */ 
/*  72 */     localHashMap.put("Alg.Alias.TransformService.INCLUSIVE", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
/*     */ 
/*  74 */     localHashMap.put("TransformService.http://www.w3.org/TR/2001/REC-xml-c14n-20010315 MechanismType", "DOM");
/*     */ 
/*  78 */     localHashMap.put("TransformService.http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", "org.jcp.xml.dsig.internal.dom.DOMCanonicalXMLC14NMethod");
/*     */ 
/*  81 */     localHashMap.put("Alg.Alias.TransformService.INCLUSIVE_WITH_COMMENTS", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
/*     */ 
/*  83 */     localHashMap.put("TransformService.http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments MechanismType", "DOM");
/*     */ 
/*  88 */     localHashMap.put("TransformService.http://www.w3.org/2006/12/xml-c14n11", "org.jcp.xml.dsig.internal.dom.DOMCanonicalXMLC14N11Method");
/*     */ 
/*  91 */     localHashMap.put("TransformService.http://www.w3.org/2006/12/xml-c14n11 MechanismType", "DOM");
/*     */ 
/*  96 */     localHashMap.put("TransformService.http://www.w3.org/2006/12/xml-c14n11#WithComments", "org.jcp.xml.dsig.internal.dom.DOMCanonicalXMLC14N11Method");
/*     */ 
/*  99 */     localHashMap.put("TransformService.http://www.w3.org/2006/12/xml-c14n11#WithComments MechanismType", "DOM");
/*     */ 
/* 104 */     localHashMap.put("TransformService.http://www.w3.org/2001/10/xml-exc-c14n#", "org.jcp.xml.dsig.internal.dom.DOMExcC14NMethod");
/*     */ 
/* 106 */     localHashMap.put("Alg.Alias.TransformService.EXCLUSIVE", "http://www.w3.org/2001/10/xml-exc-c14n#");
/*     */ 
/* 108 */     localHashMap.put("TransformService.http://www.w3.org/2001/10/xml-exc-c14n# MechanismType", "DOM");
/*     */ 
/* 112 */     localHashMap.put("TransformService.http://www.w3.org/2001/10/xml-exc-c14n#WithComments", "org.jcp.xml.dsig.internal.dom.DOMExcC14NMethod");
/*     */ 
/* 115 */     localHashMap.put("Alg.Alias.TransformService.EXCLUSIVE_WITH_COMMENTS", "http://www.w3.org/2001/10/xml-exc-c14n#WithComments");
/*     */ 
/* 117 */     localHashMap.put("TransformService.http://www.w3.org/2001/10/xml-exc-c14n#WithComments MechanismType", "DOM");
/*     */ 
/* 122 */     localHashMap.put("TransformService.http://www.w3.org/2000/09/xmldsig#base64", "org.jcp.xml.dsig.internal.dom.DOMBase64Transform");
/*     */ 
/* 124 */     localHashMap.put("Alg.Alias.TransformService.BASE64", "http://www.w3.org/2000/09/xmldsig#base64");
/* 125 */     localHashMap.put("TransformService.http://www.w3.org/2000/09/xmldsig#base64 MechanismType", "DOM");
/*     */ 
/* 129 */     localHashMap.put("TransformService.http://www.w3.org/2000/09/xmldsig#enveloped-signature", "org.jcp.xml.dsig.internal.dom.DOMEnvelopedTransform");
/*     */ 
/* 131 */     localHashMap.put("Alg.Alias.TransformService.ENVELOPED", "http://www.w3.org/2000/09/xmldsig#enveloped-signature");
/* 132 */     localHashMap.put("TransformService.http://www.w3.org/2000/09/xmldsig#enveloped-signature MechanismType", "DOM");
/*     */ 
/* 136 */     localHashMap.put("TransformService.http://www.w3.org/2002/06/xmldsig-filter2", "org.jcp.xml.dsig.internal.dom.DOMXPathFilter2Transform");
/*     */ 
/* 138 */     localHashMap.put("Alg.Alias.TransformService.XPATH2", "http://www.w3.org/2002/06/xmldsig-filter2");
/* 139 */     localHashMap.put("TransformService.http://www.w3.org/2002/06/xmldsig-filter2 MechanismType", "DOM");
/*     */ 
/* 143 */     localHashMap.put("TransformService.http://www.w3.org/TR/1999/REC-xpath-19991116", "org.jcp.xml.dsig.internal.dom.DOMXPathTransform");
/*     */ 
/* 145 */     localHashMap.put("Alg.Alias.TransformService.XPATH", "http://www.w3.org/TR/1999/REC-xpath-19991116");
/* 146 */     localHashMap.put("TransformService.http://www.w3.org/TR/1999/REC-xpath-19991116 MechanismType", "DOM");
/*     */ 
/* 150 */     localHashMap.put("TransformService.http://www.w3.org/TR/1999/REC-xslt-19991116", "org.jcp.xml.dsig.internal.dom.DOMXSLTTransform");
/*     */ 
/* 152 */     localHashMap.put("Alg.Alias.TransformService.XSLT", "http://www.w3.org/TR/1999/REC-xslt-19991116");
/* 153 */     localHashMap.put("TransformService.http://www.w3.org/TR/1999/REC-xslt-19991116 MechanismType", "DOM");
/*     */ 
/* 156 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 158 */         XMLDSigRI.this.putAll(localHashMap);
/* 159 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.XMLDSigRI
 * JD-Core Version:    0.6.2
 */