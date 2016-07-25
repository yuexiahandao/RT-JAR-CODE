/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class Utils
/*     */ {
/*     */   public static byte[] readBytesFromStream(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  48 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*  49 */     byte[] arrayOfByte = new byte[1024];
/*     */     while (true) {
/*  51 */       int i = paramInputStream.read(arrayOfByte);
/*  52 */       if (i == -1) {
/*     */         break;
/*     */       }
/*  55 */       localByteArrayOutputStream.write(arrayOfByte, 0, i);
/*  56 */       if (i < 1024) {
/*     */         break;
/*     */       }
/*     */     }
/*  60 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   static Set toNodeSet(Iterator paramIterator)
/*     */   {
/*  71 */     HashSet localHashSet = new HashSet();
/*  72 */     while (paramIterator.hasNext()) {
/*  73 */       Node localNode = (Node)paramIterator.next();
/*  74 */       localHashSet.add(localNode);
/*     */ 
/*  76 */       if (localNode.getNodeType() == 1) {
/*  77 */         NamedNodeMap localNamedNodeMap = localNode.getAttributes();
/*  78 */         int i = 0; for (int j = localNamedNodeMap.getLength(); i < j; i++) {
/*  79 */           localHashSet.add(localNamedNodeMap.item(i));
/*     */         }
/*     */       }
/*     */     }
/*  83 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   public static String parseIdFromSameDocumentURI(String paramString)
/*     */   {
/*  90 */     if (paramString.length() == 0) {
/*  91 */       return null;
/*     */     }
/*  93 */     String str = paramString.substring(1);
/*  94 */     if ((str != null) && (str.startsWith("xpointer(id("))) {
/*  95 */       int i = str.indexOf('\'');
/*  96 */       int j = str.indexOf('\'', i + 1);
/*  97 */       str = str.substring(i + 1, j);
/*     */     }
/*  99 */     return str;
/*     */   }
/*     */ 
/*     */   public static boolean sameDocumentURI(String paramString)
/*     */   {
/* 106 */     return (paramString != null) && ((paramString.length() == 0) || (paramString.charAt(0) == '#'));
/*     */   }
/*     */ 
/*     */   static boolean secureValidation(XMLCryptoContext paramXMLCryptoContext) {
/* 110 */     if (paramXMLCryptoContext == null) {
/* 111 */       return false;
/*     */     }
/* 113 */     return getBoolean(paramXMLCryptoContext, "org.jcp.xml.dsig.secureValidation");
/*     */   }
/*     */ 
/*     */   private static boolean getBoolean(XMLCryptoContext paramXMLCryptoContext, String paramString) {
/* 117 */     Boolean localBoolean = (Boolean)paramXMLCryptoContext.getProperty(paramString);
/* 118 */     return (localBoolean != null) && (localBoolean.booleanValue());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.Utils
 * JD-Core Version:    0.6.2
 */