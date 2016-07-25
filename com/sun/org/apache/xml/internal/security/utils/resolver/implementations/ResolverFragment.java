/*     */ package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class ResolverFragment extends ResourceResolverSpi
/*     */ {
/*  46 */   static Logger log = Logger.getLogger(ResolverFragment.class.getName());
/*     */ 
/*     */   public boolean engineIsThreadSafe()
/*     */   {
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
/*     */     throws ResourceResolverException
/*     */   {
/*  62 */     String str = paramAttr.getNodeValue();
/*  63 */     Document localDocument = paramAttr.getOwnerElement().getOwnerDocument();
/*     */ 
/*  65 */     Object localObject1 = null;
/*  66 */     if (str.equals(""))
/*     */     {
/*  73 */       log.log(Level.FINE, "ResolverFragment with empty URI (means complete document)");
/*  74 */       localObject1 = localDocument;
/*     */     }
/*     */     else
/*     */     {
/*  85 */       localObject2 = str.substring(1);
/*     */ 
/*  87 */       localObject1 = localDocument.getElementById((String)localObject2);
/*     */       Object localObject3;
/*  88 */       if (localObject1 == null) {
/*  89 */         localObject3 = new Object[] { localObject2 };
/*  90 */         throw new ResourceResolverException("signature.Verification.MissingID", (Object[])localObject3, paramAttr, paramString);
/*     */       }
/*     */ 
/*  93 */       if (this.secureValidation) {
/*  94 */         localObject3 = paramAttr.getOwnerDocument().getDocumentElement();
/*  95 */         if (!XMLUtils.protectAgainstWrappingAttack((Node)localObject3, (String)localObject2)) {
/*  96 */           Object[] arrayOfObject = { localObject2 };
/*  97 */           throw new ResourceResolverException("signature.Verification.MultipleIDs", arrayOfObject, paramAttr, paramString);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 102 */       if (log.isLoggable(Level.FINE)) {
/* 103 */         log.log(Level.FINE, "Try to catch an Element with ID " + (String)localObject2 + " and Element was " + localObject1);
/*     */       }
/*     */     }
/* 106 */     Object localObject2 = new XMLSignatureInput((Node)localObject1);
/* 107 */     ((XMLSignatureInput)localObject2).setExcludeComments(true);
/*     */ 
/* 109 */     ((XMLSignatureInput)localObject2).setMIMEType("text/xml");
/* 110 */     if ((paramString != null) && (paramString.length() > 0))
/* 111 */       ((XMLSignatureInput)localObject2).setSourceURI(paramString.concat(paramAttr.getNodeValue()));
/*     */     else {
/* 113 */       ((XMLSignatureInput)localObject2).setSourceURI(paramAttr.getNodeValue());
/*     */     }
/* 115 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public boolean engineCanResolve(Attr paramAttr, String paramString)
/*     */   {
/* 127 */     if (paramAttr == null) {
/* 128 */       log.log(Level.FINE, "Quick fail for null uri");
/* 129 */       return false;
/*     */     }
/*     */ 
/* 132 */     String str = paramAttr.getNodeValue();
/*     */ 
/* 134 */     if ((str.equals("")) || ((str.charAt(0) == '#') && ((str.charAt(1) != 'x') || (!str.startsWith("#xpointer(")))))
/*     */     {
/* 140 */       if (log.isLoggable(Level.FINE))
/* 141 */         log.log(Level.FINE, "State I can resolve reference: \"" + str + "\"");
/* 142 */       return true;
/*     */     }
/* 144 */     if (log.isLoggable(Level.FINE))
/* 145 */       log.log(Level.FINE, "Do not seem to be able to resolve reference: \"" + str + "\"");
/* 146 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverFragment
 * JD-Core Version:    0.6.2
 */