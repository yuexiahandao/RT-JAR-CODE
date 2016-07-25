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
/*     */ public class ResolverXPointer extends ResourceResolverSpi
/*     */ {
/*  53 */   static Logger log = Logger.getLogger(ResolverXPointer.class.getName());
/*     */   private static final String XP = "#xpointer(id(";
/* 141 */   private static final int XP_LENGTH = "#xpointer(id(".length();
/*     */ 
/*     */   public boolean engineIsThreadSafe()
/*     */   {
/*  58 */     return true;
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
/*     */     throws ResourceResolverException
/*     */   {
/*  67 */     Object localObject1 = null;
/*  68 */     Document localDocument = paramAttr.getOwnerElement().getOwnerDocument();
/*     */ 
/*  70 */     String str = paramAttr.getNodeValue();
/*  71 */     if (isXPointerSlash(str)) {
/*  72 */       localObject1 = localDocument;
/*     */     }
/*  74 */     else if (isXPointerId(str)) {
/*  75 */       localObject2 = getXPointerId(str);
/*  76 */       localObject1 = localDocument.getElementById((String)localObject2);
/*     */       Object localObject3;
/*  78 */       if (this.secureValidation) {
/*  79 */         localObject3 = paramAttr.getOwnerDocument().getDocumentElement();
/*  80 */         if (!XMLUtils.protectAgainstWrappingAttack((Node)localObject3, (String)localObject2)) {
/*  81 */           Object[] arrayOfObject = { localObject2 };
/*  82 */           throw new ResourceResolverException("signature.Verification.MultipleIDs", arrayOfObject, paramAttr, paramString);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  88 */       if (localObject1 == null) {
/*  89 */         localObject3 = new Object[] { localObject2 };
/*     */ 
/*  91 */         throw new ResourceResolverException("signature.Verification.MissingID", (Object[])localObject3, paramAttr, paramString);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  96 */     Object localObject2 = new XMLSignatureInput((Node)localObject1);
/*     */ 
/*  98 */     ((XMLSignatureInput)localObject2).setMIMEType("text/xml");
/*  99 */     if ((paramString != null) && (paramString.length() > 0))
/* 100 */       ((XMLSignatureInput)localObject2).setSourceURI(paramString.concat(paramAttr.getNodeValue()));
/*     */     else {
/* 102 */       ((XMLSignatureInput)localObject2).setSourceURI(paramAttr.getNodeValue());
/*     */     }
/*     */ 
/* 105 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public boolean engineCanResolve(Attr paramAttr, String paramString)
/*     */   {
/* 113 */     if (paramAttr == null) {
/* 114 */       return false;
/*     */     }
/* 116 */     String str = paramAttr.getNodeValue();
/* 117 */     if ((isXPointerSlash(str)) || (isXPointerId(str))) {
/* 118 */       return true;
/*     */     }
/*     */ 
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isXPointerSlash(String paramString)
/*     */   {
/* 132 */     if (paramString.equals("#xpointer(/)")) {
/* 133 */       return true;
/*     */     }
/*     */ 
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isXPointerId(String paramString)
/*     */   {
/* 152 */     if ((paramString.startsWith("#xpointer(id(")) && (paramString.endsWith("))")))
/*     */     {
/* 154 */       String str = paramString.substring(XP_LENGTH, paramString.length() - 2);
/*     */ 
/* 159 */       int i = str.length() - 1;
/* 160 */       if (((str.charAt(0) == '"') && (str.charAt(i) == '"')) || ((str.charAt(0) == '\'') && (str.charAt(i) == '\'')))
/*     */       {
/* 164 */         if (log.isLoggable(Level.FINE)) {
/* 165 */           log.log(Level.FINE, "Id=" + str.substring(1, i));
/*     */         }
/*     */ 
/* 168 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 172 */     return false;
/*     */   }
/*     */ 
/*     */   private static String getXPointerId(String paramString)
/*     */   {
/* 184 */     if ((paramString.startsWith("#xpointer(id(")) && (paramString.endsWith("))")))
/*     */     {
/* 186 */       String str = paramString.substring(XP_LENGTH, paramString.length() - 2);
/*     */ 
/* 188 */       int i = str.length() - 1;
/* 189 */       if (((str.charAt(0) == '"') && (str.charAt(i) == '"')) || ((str.charAt(0) == '\'') && (str.charAt(i) == '\'')))
/*     */       {
/* 193 */         return str.substring(1, i);
/*     */       }
/*     */     }
/*     */ 
/* 197 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverXPointer
 * JD-Core Version:    0.6.2
 */