/*     */ package javax.xml.namespace;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public class QName
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID;
/*     */   private static final long defaultSerialVersionUID = -9120448754896609940L;
/*     */   private static final long compatibleSerialVersionUID = 4418622981026545151L;
/* 106 */   private static boolean useDefaultSerialVersionUID = true;
/*     */   private final String namespaceURI;
/*     */   private final String localPart;
/*     */   private final String prefix;
/*     */ 
/*     */   public QName(String namespaceURI, String localPart)
/*     */   {
/* 188 */     this(namespaceURI, localPart, "");
/*     */   }
/*     */ 
/*     */   public QName(String namespaceURI, String localPart, String prefix)
/*     */   {
/* 235 */     if (namespaceURI == null)
/* 236 */       this.namespaceURI = "";
/*     */     else {
/* 238 */       this.namespaceURI = namespaceURI;
/*     */     }
/*     */ 
/* 243 */     if (localPart == null) {
/* 244 */       throw new IllegalArgumentException("local part cannot be \"null\" when creating a QName");
/*     */     }
/*     */ 
/* 247 */     this.localPart = localPart;
/*     */ 
/* 250 */     if (prefix == null) {
/* 251 */       throw new IllegalArgumentException("prefix cannot be \"null\" when creating a QName");
/*     */     }
/*     */ 
/* 254 */     this.prefix = prefix;
/*     */   }
/*     */ 
/*     */   public QName(String localPart)
/*     */   {
/* 297 */     this("", localPart, "");
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI()
/*     */   {
/* 309 */     return this.namespaceURI;
/*     */   }
/*     */ 
/*     */   public String getLocalPart()
/*     */   {
/* 318 */     return this.localPart;
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 333 */     return this.prefix;
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object objectToTest)
/*     */   {
/* 359 */     if (objectToTest == this) {
/* 360 */       return true;
/*     */     }
/*     */ 
/* 363 */     if ((objectToTest == null) || (!(objectToTest instanceof QName))) {
/* 364 */       return false;
/*     */     }
/*     */ 
/* 367 */     QName qName = (QName)objectToTest;
/*     */ 
/* 369 */     return (this.localPart.equals(qName.localPart)) && (this.namespaceURI.equals(qName.namespaceURI));
/*     */   }
/*     */ 
/*     */   public final int hashCode()
/*     */   {
/* 387 */     return this.namespaceURI.hashCode() ^ this.localPart.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 415 */     if (this.namespaceURI.equals("")) {
/* 416 */       return this.localPart;
/*     */     }
/* 418 */     return "{" + this.namespaceURI + "}" + this.localPart;
/*     */   }
/*     */ 
/*     */   public static QName valueOf(String qNameAsString)
/*     */   {
/* 470 */     if (qNameAsString == null) {
/* 471 */       throw new IllegalArgumentException("cannot create QName from \"null\" or \"\" String");
/*     */     }
/*     */ 
/* 476 */     if (qNameAsString.length() == 0) {
/* 477 */       return new QName("", qNameAsString, "");
/*     */     }
/*     */ 
/* 484 */     if (qNameAsString.charAt(0) != '{') {
/* 485 */       return new QName("", qNameAsString, "");
/*     */     }
/*     */ 
/* 492 */     if (qNameAsString.startsWith("{}")) {
/* 493 */       throw new IllegalArgumentException("Namespace URI .equals(XMLConstants.NULL_NS_URI), .equals(\"\"), only the local part, \"" + qNameAsString.substring(2 + "".length()) + "\", " + "should be provided.");
/*     */     }
/*     */ 
/* 504 */     int endOfNamespaceURI = qNameAsString.indexOf('}');
/* 505 */     if (endOfNamespaceURI == -1) {
/* 506 */       throw new IllegalArgumentException("cannot create QName from \"" + qNameAsString + "\", missing closing \"}\"");
/*     */     }
/*     */ 
/* 511 */     return new QName(qNameAsString.substring(1, endOfNamespaceURI), qNameAsString.substring(endOfNamespaceURI + 1), "");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 110 */       String valueUseCompatibleSerialVersionUID = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run() {
/* 113 */           return System.getProperty("com.sun.xml.namespace.QName.useCompatibleSerialVersionUID");
/*     */         }
/*     */       });
/* 117 */       useDefaultSerialVersionUID = (valueUseCompatibleSerialVersionUID == null) || (!valueUseCompatibleSerialVersionUID.equals("1.0"));
/*     */     }
/*     */     catch (Exception exception) {
/* 120 */       useDefaultSerialVersionUID = true;
/*     */     }
/*     */ 
/* 124 */     if (useDefaultSerialVersionUID)
/* 125 */       serialVersionUID = -9120448754896609940L;
/*     */     else
/* 127 */       serialVersionUID = 4418622981026545151L;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.namespace.QName
 * JD-Core Version:    0.6.2
 */