/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Messages;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Utils;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.PropertyResourceBundle;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ final class CharInfo
/*     */ {
/*  55 */   private HashMap m_charToString = new HashMap();
/*     */   public static final String HTML_ENTITIES_RESOURCE = "com.sun.org.apache.xml.internal.serializer.HTMLEntities";
/*     */   public static final String XML_ENTITIES_RESOURCE = "com.sun.org.apache.xml.internal.serializer.XMLEntities";
/*     */   public static final char S_HORIZONAL_TAB = '\t';
/*     */   public static final char S_LINEFEED = '\n';
/*     */   public static final char S_CARRIAGERETURN = '\r';
/*     */   final boolean onlyQuotAmpLtGt;
/*     */   private static final int ASCII_MAX = 128;
/*  93 */   private boolean[] isSpecialAttrASCII = new boolean[''];
/*     */ 
/*  98 */   private boolean[] isSpecialTextASCII = new boolean[''];
/*     */ 
/* 100 */   private boolean[] isCleanTextASCII = new boolean[''];
/*     */ 
/* 107 */   private int[] array_of_bits = createEmptySetOfIntegers(65535);
/*     */   private static final int SHIFT_PER_WORD = 5;
/*     */   private static final int LOW_ORDER_BITMASK = 31;
/*     */   private int firstWordNotUsed;
/* 537 */   private static HashMap m_getCharInfoCache = new HashMap();
/*     */ 
/*     */   private CharInfo(String entitiesResource, String method)
/*     */   {
/* 160 */     this(entitiesResource, method, false);
/*     */   }
/*     */ 
/*     */   private CharInfo(String entitiesResource, String method, boolean internal)
/*     */   {
/* 165 */     ResourceBundle entities = null;
/* 166 */     boolean noExtraEntities = true;
/*     */     try
/*     */     {
/* 177 */       if (internal)
/*     */       {
/* 180 */         entities = PropertyResourceBundle.getBundle(entitiesResource);
/*     */       } else {
/* 182 */         ClassLoader cl = SecuritySupport.getContextClassLoader();
/* 183 */         if (cl != null)
/* 184 */           entities = PropertyResourceBundle.getBundle(entitiesResource, Locale.getDefault(), cl);
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/* 190 */     if (entities != null) {
/* 191 */       Enumeration keys = entities.getKeys();
/* 192 */       while (keys.hasMoreElements()) {
/* 193 */         String name = (String)keys.nextElement();
/* 194 */         String value = entities.getString(name);
/* 195 */         int code = Integer.parseInt(value);
/* 196 */         defineEntity(name, (char)code);
/* 197 */         if (extraEntity(code))
/* 198 */           noExtraEntities = false;
/*     */       }
/* 200 */       set(10);
/* 201 */       set(13);
/*     */     } else {
/* 203 */       InputStream is = null;
/* 204 */       String err = null;
/*     */       try
/*     */       {
/* 209 */         if (internal) {
/* 210 */           is = CharInfo.class.getResourceAsStream(entitiesResource);
/*     */         } else {
/* 212 */           ClassLoader cl = SecuritySupport.getContextClassLoader();
/* 213 */           if (cl != null) {
/*     */             try {
/* 215 */               is = cl.getResourceAsStream(entitiesResource);
/*     */             } catch (Exception e) {
/* 217 */               err = e.getMessage();
/*     */             }
/*     */           }
/*     */ 
/* 221 */           if (is == null) {
/*     */             try {
/* 223 */               URL url = new URL(entitiesResource);
/* 224 */               is = url.openStream();
/*     */             } catch (Exception e) {
/* 226 */               err = e.getMessage();
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 231 */         if (is == null) {
/* 232 */           throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_FIND", new Object[] { entitiesResource, err }));
/*     */         }
/*     */ 
/*     */         BufferedReader reader;
/*     */         try
/*     */         {
/* 259 */           reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */         } catch (UnsupportedEncodingException e) {
/* 261 */           reader = new BufferedReader(new InputStreamReader(is));
/*     */         }
/*     */ 
/* 264 */         String line = reader.readLine();
/*     */ 
/* 266 */         while (line != null) {
/* 267 */           if ((line.length() == 0) || (line.charAt(0) == '#')) {
/* 268 */             line = reader.readLine();
/*     */           }
/*     */           else
/*     */           {
/* 273 */             int index = line.indexOf(' ');
/*     */ 
/* 275 */             if (index > 1) {
/* 276 */               String name = line.substring(0, index);
/*     */ 
/* 278 */               index++;
/*     */ 
/* 280 */               if (index < line.length()) {
/* 281 */                 String value = line.substring(index);
/* 282 */                 index = value.indexOf(' ');
/*     */ 
/* 284 */                 if (index > 0) {
/* 285 */                   value = value.substring(0, index);
/*     */                 }
/*     */ 
/* 288 */                 int code = Integer.parseInt(value);
/*     */ 
/* 290 */                 defineEntity(name, (char)code);
/* 291 */                 if (extraEntity(code)) {
/* 292 */                   noExtraEntities = false;
/*     */                 }
/*     */               }
/*     */             }
/* 296 */             line = reader.readLine();
/*     */           }
/*     */         }
/* 299 */         is.close();
/* 300 */         set(10);
/* 301 */         set(13);
/*     */       } catch (Exception e) {
/* 303 */         throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_LOAD", new Object[] { entitiesResource, e.toString(), entitiesResource, e.toString() }));
/*     */       }
/*     */       finally
/*     */       {
/* 311 */         if (is != null) {
/*     */           try {
/* 313 */             is.close();
/*     */           }
/*     */           catch (Exception except)
/*     */           {
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 324 */     for (int ch = 0; ch < 128; ch++) {
/* 325 */       if (((32 > ch) && (10 != ch) && (13 != ch) && (9 != ch)) || ((!get(ch)) || (34 == ch)))
/*     */       {
/* 328 */         this.isCleanTextASCII[ch] = true;
/* 329 */         this.isSpecialTextASCII[ch] = false;
/*     */       }
/*     */       else {
/* 332 */         this.isCleanTextASCII[ch] = false;
/* 333 */         this.isSpecialTextASCII[ch] = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 338 */     this.onlyQuotAmpLtGt = noExtraEntities;
/*     */ 
/* 341 */     for (int i = 0; i < 128; i++) {
/* 342 */       this.isSpecialAttrASCII[i] = get(i);
/*     */     }
/*     */ 
/* 353 */     if ("xml".equals(method))
/*     */     {
/* 355 */       this.isSpecialAttrASCII[9] = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void defineEntity(String name, char value)
/*     */   {
/* 372 */     StringBuilder sb = new StringBuilder("&");
/* 373 */     sb.append(name);
/* 374 */     sb.append(';');
/* 375 */     String entityString = sb.toString();
/*     */ 
/* 377 */     defineChar2StringMapping(entityString, value);
/*     */   }
/*     */ 
/*     */   String getOutputStringForChar(char value)
/*     */   {
/* 404 */     CharKey charKey = new CharKey();
/* 405 */     charKey.setChar(value);
/* 406 */     return (String)this.m_charToString.get(charKey);
/*     */   }
/*     */ 
/*     */   final boolean isSpecialAttrChar(int value)
/*     */   {
/* 424 */     if (value < 128) {
/* 425 */       return this.isSpecialAttrASCII[value];
/*     */     }
/*     */ 
/* 429 */     return get(value);
/*     */   }
/*     */ 
/*     */   final boolean isSpecialTextChar(int value)
/*     */   {
/* 447 */     if (value < 128) {
/* 448 */       return this.isSpecialTextASCII[value];
/*     */     }
/*     */ 
/* 452 */     return get(value);
/*     */   }
/*     */ 
/*     */   final boolean isTextASCIIClean(int value)
/*     */   {
/* 464 */     return this.isCleanTextASCII[value];
/*     */   }
/*     */ 
/*     */   static CharInfo getCharInfoInternal(String entitiesFileName, String method)
/*     */   {
/* 481 */     CharInfo charInfo = (CharInfo)m_getCharInfoCache.get(entitiesFileName);
/* 482 */     if (charInfo != null) {
/* 483 */       return charInfo;
/*     */     }
/*     */ 
/* 486 */     charInfo = new CharInfo(entitiesFileName, method, true);
/* 487 */     m_getCharInfoCache.put(entitiesFileName, charInfo);
/* 488 */     return charInfo;
/*     */   }
/*     */ 
/*     */   static CharInfo getCharInfo(String entitiesFileName, String method)
/*     */   {
/*     */     try
/*     */     {
/* 516 */       return new CharInfo(entitiesFileName, method, false);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */       String absoluteEntitiesFileName;
/*     */       String absoluteEntitiesFileName;
/* 521 */       if (entitiesFileName.indexOf(':') < 0)
/* 522 */         absoluteEntitiesFileName = SystemIDResolver.getAbsoluteURIFromRelative(entitiesFileName);
/*     */       else {
/*     */         try
/*     */         {
/* 526 */           absoluteEntitiesFileName = SystemIDResolver.getAbsoluteURI(entitiesFileName, null);
/*     */         }
/*     */         catch (TransformerException te) {
/* 529 */           throw new WrappedRuntimeException(te);
/*     */         }
/*     */       }
/*     */ 
/* 533 */       return new CharInfo(absoluteEntitiesFileName, method, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int arrayIndex(int i)
/*     */   {
/* 546 */     return i >> 5;
/*     */   }
/*     */ 
/*     */   private static int bit(int i)
/*     */   {
/* 555 */     int ret = 1 << (i & 0x1F);
/* 556 */     return ret;
/*     */   }
/*     */ 
/*     */   private int[] createEmptySetOfIntegers(int max)
/*     */   {
/* 564 */     this.firstWordNotUsed = 0;
/*     */ 
/* 566 */     int[] arr = new int[arrayIndex(max - 1) + 1];
/* 567 */     return arr;
/*     */   }
/*     */ 
/*     */   private final void set(int i)
/*     */   {
/* 578 */     setASCIIdirty(i);
/*     */ 
/* 580 */     int j = i >> 5;
/* 581 */     int k = j + 1;
/*     */ 
/* 583 */     if (this.firstWordNotUsed < k) {
/* 584 */       this.firstWordNotUsed = k;
/*     */     }
/* 586 */     this.array_of_bits[j] |= 1 << (i & 0x1F);
/*     */   }
/*     */ 
/*     */   private final boolean get(int i)
/*     */   {
/* 602 */     boolean in_the_set = false;
/* 603 */     int j = i >> 5;
/*     */ 
/* 606 */     if (j < this.firstWordNotUsed) {
/* 607 */       in_the_set = (this.array_of_bits[j] & 1 << (i & 0x1F)) != 0;
/*     */     }
/*     */ 
/* 610 */     return in_the_set;
/*     */   }
/*     */ 
/*     */   private boolean extraEntity(int entityValue)
/*     */   {
/* 622 */     boolean extra = false;
/* 623 */     if (entityValue < 128)
/*     */     {
/* 625 */       switch (entityValue)
/*     */       {
/*     */       case 34:
/*     */       case 38:
/*     */       case 60:
/*     */       case 62:
/* 631 */         break;
/*     */       default:
/* 633 */         extra = true;
/*     */       }
/*     */     }
/* 636 */     return extra;
/*     */   }
/*     */ 
/*     */   private void setASCIIdirty(int j)
/*     */   {
/* 647 */     if ((0 <= j) && (j < 128))
/*     */     {
/* 649 */       this.isCleanTextASCII[j] = false;
/* 650 */       this.isSpecialTextASCII[j] = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setASCIIclean(int j)
/*     */   {
/* 662 */     if ((0 <= j) && (j < 128))
/*     */     {
/* 664 */       this.isCleanTextASCII[j] = true;
/* 665 */       this.isSpecialTextASCII[j] = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void defineChar2StringMapping(String outputString, char inputChar)
/*     */   {
/* 671 */     CharKey character = new CharKey(inputChar);
/* 672 */     this.m_charToString.put(character, outputString);
/* 673 */     set(inputChar);
/*     */   }
/*     */ 
/*     */   private static class CharKey
/*     */   {
/*     */     private char m_char;
/*     */ 
/*     */     public CharKey(char key)
/*     */     {
/* 698 */       this.m_char = key;
/*     */     }
/*     */ 
/*     */     public CharKey()
/*     */     {
/*     */     }
/*     */ 
/*     */     public final void setChar(char c)
/*     */     {
/* 717 */       this.m_char = c;
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 729 */       return this.m_char;
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object obj)
/*     */     {
/* 741 */       return ((CharKey)obj).m_char == this.m_char;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.CharInfo
 * JD-Core Version:    0.6.2
 */