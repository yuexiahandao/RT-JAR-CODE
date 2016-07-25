/*     */ package com.sun.xml.internal.stream;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.PropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.util.URI;
/*     */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
/*     */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import java.io.File;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class XMLEntityStorage
/*     */ {
/*     */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
/*     */   protected boolean fWarnDuplicateEntityDef;
/*  65 */   protected Hashtable fEntities = new Hashtable();
/*     */   protected Entity.ScannedEntity fCurrentEntity;
/*     */   private XMLEntityManager fEntityManager;
/*     */   protected XMLErrorReporter fErrorReporter;
/*     */   protected PropertyManager fPropertyManager;
/*  78 */   protected boolean fInExternalSubset = false;
/*     */   private static String gUserDir;
/*     */   private static String gEscapedUserDir;
/* 376 */   private static boolean[] gNeedEscaping = new boolean[''];
/*     */ 
/* 378 */   private static char[] gAfterEscaping1 = new char[''];
/*     */ 
/* 380 */   private static char[] gAfterEscaping2 = new char[''];
/* 381 */   private static char[] gHexChs = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */   public XMLEntityStorage(PropertyManager propertyManager)
/*     */   {
/*  82 */     this.fPropertyManager = propertyManager;
/*     */   }
/*     */ 
/*     */   public XMLEntityStorage(XMLEntityManager entityManager)
/*     */   {
/*  89 */     this.fEntityManager = entityManager;
/*     */   }
/*     */ 
/*     */   public void reset(PropertyManager propertyManager)
/*     */   {
/*  94 */     this.fErrorReporter = ((XMLErrorReporter)propertyManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*  95 */     this.fEntities.clear();
/*  96 */     this.fCurrentEntity = null;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 101 */     this.fEntities.clear();
/* 102 */     this.fCurrentEntity = null;
/*     */   }
/*     */ 
/*     */   public void reset(XMLComponentManager componentManager)
/*     */     throws XMLConfigurationException
/*     */   {
/* 124 */     this.fWarnDuplicateEntityDef = componentManager.getFeature("http://apache.org/xml/features/warn-on-duplicate-entitydef", false);
/*     */ 
/* 126 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*     */ 
/* 128 */     this.fEntities.clear();
/* 129 */     this.fCurrentEntity = null;
/*     */   }
/*     */ 
/*     */   public Entity getEntity(String name)
/*     */   {
/* 141 */     return (Entity)this.fEntities.get(name);
/*     */   }
/*     */ 
/*     */   public boolean hasEntities() {
/* 145 */     return this.fEntities != null;
/*     */   }
/*     */ 
/*     */   public int getEntitySize() {
/* 149 */     return this.fEntities.size();
/*     */   }
/*     */ 
/*     */   public Enumeration getEntityKeys() {
/* 153 */     return this.fEntities.keys();
/*     */   }
/*     */ 
/*     */   public void addInternalEntity(String name, String text)
/*     */   {
/* 170 */     if (!this.fEntities.containsKey(name)) {
/* 171 */       Entity entity = new Entity.InternalEntity(name, text, this.fInExternalSubset);
/* 172 */       this.fEntities.put(name, entity);
/*     */     }
/* 175 */     else if (this.fWarnDuplicateEntityDef) {
/* 176 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { name }, (short)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addExternalEntity(String name, String publicId, String literalSystemId, String baseSystemId)
/*     */   {
/* 209 */     if (!this.fEntities.containsKey(name)) {
/* 210 */       if (baseSystemId == null)
/*     */       {
/* 221 */         if ((this.fCurrentEntity != null) && (this.fCurrentEntity.entityLocation != null)) {
/* 222 */           baseSystemId = this.fCurrentEntity.entityLocation.getExpandedSystemId();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 236 */       this.fCurrentEntity = this.fEntityManager.getCurrentEntity();
/* 237 */       Entity entity = new Entity.ExternalEntity(name, new XMLResourceIdentifierImpl(publicId, literalSystemId, baseSystemId, expandSystemId(literalSystemId, baseSystemId)), null, this.fInExternalSubset);
/*     */ 
/* 244 */       this.fEntities.put(name, entity);
/*     */     }
/* 247 */     else if (this.fWarnDuplicateEntityDef) {
/* 248 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { name }, (short)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isExternalEntity(String entityName)
/*     */   {
/* 266 */     Entity entity = (Entity)this.fEntities.get(entityName);
/* 267 */     if (entity == null) {
/* 268 */       return false;
/*     */     }
/* 270 */     return entity.isExternal();
/*     */   }
/*     */ 
/*     */   public boolean isEntityDeclInExternalSubset(String entityName)
/*     */   {
/* 283 */     Entity entity = (Entity)this.fEntities.get(entityName);
/* 284 */     if (entity == null) {
/* 285 */       return false;
/*     */     }
/* 287 */     return entity.isEntityDeclInExternalSubset();
/*     */   }
/*     */ 
/*     */   public void addUnparsedEntity(String name, String publicId, String systemId, String baseSystemId, String notation)
/*     */   {
/* 310 */     this.fCurrentEntity = this.fEntityManager.getCurrentEntity();
/* 311 */     if (!this.fEntities.containsKey(name)) {
/* 312 */       Entity entity = new Entity.ExternalEntity(name, new XMLResourceIdentifierImpl(publicId, systemId, baseSystemId, null), notation, this.fInExternalSubset);
/*     */ 
/* 315 */       this.fEntities.put(name, entity);
/*     */     }
/* 318 */     else if (this.fWarnDuplicateEntityDef) {
/* 319 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[] { name }, (short)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isUnparsedEntity(String entityName)
/*     */   {
/* 336 */     Entity entity = (Entity)this.fEntities.get(entityName);
/* 337 */     if (entity == null) {
/* 338 */       return false;
/*     */     }
/* 340 */     return entity.isUnparsed();
/*     */   }
/*     */ 
/*     */   public boolean isDeclaredEntity(String entityName)
/*     */   {
/* 351 */     Entity entity = (Entity)this.fEntities.get(entityName);
/* 352 */     return entity != null;
/*     */   }
/*     */ 
/*     */   public static String expandSystemId(String systemId)
/*     */   {
/* 368 */     return expandSystemId(systemId, null);
/*     */   }
/*     */ 
/*     */   private static synchronized String getUserDir()
/*     */   {
/* 416 */     String userDir = "";
/*     */     try {
/* 418 */       userDir = SecuritySupport.getSystemProperty("user.dir");
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/*     */     }
/*     */ 
/* 424 */     if (userDir.length() == 0) {
/* 425 */       return "";
/*     */     }
/*     */ 
/* 429 */     if (userDir.equals(gUserDir)) {
/* 430 */       return gEscapedUserDir;
/*     */     }
/*     */ 
/* 434 */     gUserDir = userDir;
/*     */ 
/* 436 */     char separator = File.separatorChar;
/* 437 */     userDir = userDir.replace(separator, '/');
/*     */ 
/* 439 */     int len = userDir.length();
/* 440 */     StringBuffer buffer = new StringBuffer(len * 3);
/*     */ 
/* 442 */     if ((len >= 2) && (userDir.charAt(1) == ':')) {
/* 443 */       int ch = Character.toUpperCase(userDir.charAt(0));
/* 444 */       if ((ch >= 65) && (ch <= 90)) {
/* 445 */         buffer.append('/');
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 450 */     for (int i = 0; 
/* 451 */       i < len; i++) {
/* 452 */       int ch = userDir.charAt(i);
/*     */ 
/* 454 */       if (ch >= 128)
/*     */         break;
/* 456 */       if (gNeedEscaping[ch] != 0) {
/* 457 */         buffer.append('%');
/* 458 */         buffer.append(gAfterEscaping1[ch]);
/* 459 */         buffer.append(gAfterEscaping2[ch]);
/*     */       }
/*     */       else
/*     */       {
/* 463 */         buffer.append((char)ch);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 468 */     if (i < len)
/*     */     {
/* 470 */       byte[] bytes = null;
/*     */       try
/*     */       {
/* 473 */         bytes = userDir.substring(i).getBytes("UTF-8");
/*     */       }
/*     */       catch (UnsupportedEncodingException e) {
/* 476 */         return userDir;
/*     */       }
/* 478 */       len = bytes.length;
/*     */ 
/* 481 */       for (i = 0; i < len; i++) {
/* 482 */         byte b = bytes[i];
/*     */ 
/* 484 */         if (b < 0) {
/* 485 */           int ch = b + 256;
/* 486 */           buffer.append('%');
/* 487 */           buffer.append(gHexChs[(ch >> 4)]);
/* 488 */           buffer.append(gHexChs[(ch & 0xF)]);
/*     */         }
/* 490 */         else if (gNeedEscaping[b] != 0) {
/* 491 */           buffer.append('%');
/* 492 */           buffer.append(gAfterEscaping1[b]);
/* 493 */           buffer.append(gAfterEscaping2[b]);
/*     */         }
/*     */         else {
/* 496 */           buffer.append((char)b);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 502 */     if (!userDir.endsWith("/")) {
/* 503 */       buffer.append('/');
/*     */     }
/* 505 */     gEscapedUserDir = buffer.toString();
/*     */ 
/* 507 */     return gEscapedUserDir;
/*     */   }
/*     */ 
/*     */   public static String expandSystemId(String systemId, String baseSystemId)
/*     */   {
/* 526 */     if ((systemId == null) || (systemId.length() == 0)) {
/* 527 */       return systemId;
/*     */     }
/*     */     try
/*     */     {
/* 531 */       new URI(systemId);
/* 532 */       return systemId;
/*     */     }
/*     */     catch (URI.MalformedURIException e)
/*     */     {
/* 537 */       String id = fixURI(systemId);
/*     */ 
/* 540 */       URI base = null;
/* 541 */       URI uri = null;
/*     */       try {
/* 543 */         if ((baseSystemId == null) || (baseSystemId.length() == 0) || (baseSystemId.equals(systemId)))
/*     */         {
/* 545 */           String dir = getUserDir();
/* 546 */           base = new URI("file", "", dir, null, null);
/*     */         }
/*     */         else {
/*     */           try {
/* 550 */             base = new URI(fixURI(baseSystemId));
/*     */           }
/*     */           catch (URI.MalformedURIException e) {
/* 553 */             if (baseSystemId.indexOf(':') != -1)
/*     */             {
/* 556 */               base = new URI("file", "", fixURI(baseSystemId), null, null);
/*     */             }
/*     */             else {
/* 559 */               String dir = getUserDir();
/* 560 */               dir = dir + fixURI(baseSystemId);
/* 561 */               base = new URI("file", "", dir, null, null);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 566 */         uri = new URI(base, id);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */       }
/*     */ 
/* 573 */       if (uri == null) {
/* 574 */         return systemId;
/*     */       }
/* 576 */       return uri.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static String fixURI(String str)
/*     */   {
/* 593 */     str = str.replace(File.separatorChar, '/');
/*     */ 
/* 596 */     if (str.length() >= 2) {
/* 597 */       char ch1 = str.charAt(1);
/*     */ 
/* 599 */       if (ch1 == ':') {
/* 600 */         char ch0 = Character.toUpperCase(str.charAt(0));
/* 601 */         if ((ch0 >= 'A') && (ch0 <= 'Z')) {
/* 602 */           str = "/" + str;
/*     */         }
/*     */ 
/*     */       }
/* 606 */       else if ((ch1 == '/') && (str.charAt(0) == '/')) {
/* 607 */         str = "file:" + str;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 612 */     return str;
/*     */   }
/*     */ 
/*     */   public void startExternalSubset()
/*     */   {
/* 618 */     this.fInExternalSubset = true;
/*     */   }
/*     */ 
/*     */   public void endExternalSubset() {
/* 622 */     this.fInExternalSubset = false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 385 */     for (int i = 0; i <= 31; i++) {
/* 386 */       gNeedEscaping[i] = true;
/* 387 */       gAfterEscaping1[i] = gHexChs[(i >> 4)];
/* 388 */       gAfterEscaping2[i] = gHexChs[(i & 0xF)];
/*     */     }
/* 390 */     gNeedEscaping[127] = true;
/* 391 */     gAfterEscaping1[127] = '7';
/* 392 */     gAfterEscaping2[127] = 'F';
/* 393 */     char[] escChs = { ' ', '<', '>', '#', '%', '"', '{', '}', '|', '\\', '^', '~', '[', ']', '`' };
/*     */ 
/* 395 */     int len = escChs.length;
/*     */ 
/* 397 */     for (int i = 0; i < len; i++) {
/* 398 */       char ch = escChs[i];
/* 399 */       gNeedEscaping[ch] = true;
/* 400 */       gAfterEscaping1[ch] = gHexChs[(ch >> '\004')];
/* 401 */       gAfterEscaping2[ch] = gHexChs[(ch & 0xF)];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.XMLEntityStorage
 * JD-Core Version:    0.6.2
 */