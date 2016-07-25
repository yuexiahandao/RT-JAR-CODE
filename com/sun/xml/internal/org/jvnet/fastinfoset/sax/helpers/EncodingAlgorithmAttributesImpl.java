/*     */ package com.sun.xml.internal.org.jvnet.fastinfoset.sax.helpers;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public class EncodingAlgorithmAttributesImpl
/*     */   implements EncodingAlgorithmAttributes
/*     */ {
/*     */   private static final int DEFAULT_CAPACITY = 8;
/*     */   private static final int URI_OFFSET = 0;
/*     */   private static final int LOCALNAME_OFFSET = 1;
/*     */   private static final int QNAME_OFFSET = 2;
/*     */   private static final int TYPE_OFFSET = 3;
/*     */   private static final int VALUE_OFFSET = 4;
/*     */   private static final int ALGORITHMURI_OFFSET = 5;
/*     */   private static final int SIZE = 6;
/*     */   private Map _registeredEncodingAlgorithms;
/*     */   private int _length;
/*     */   private String[] _data;
/*     */   private int[] _algorithmIds;
/*     */   private Object[] _algorithmData;
/*     */   private String[] _alphabets;
/*     */   private boolean[] _toIndex;
/*     */ 
/*     */   public EncodingAlgorithmAttributesImpl()
/*     */   {
/*  89 */     this(null, null);
/*     */   }
/*     */ 
/*     */   public EncodingAlgorithmAttributesImpl(Attributes attributes)
/*     */   {
/* 101 */     this(null, attributes);
/*     */   }
/*     */ 
/*     */   public EncodingAlgorithmAttributesImpl(Map registeredEncodingAlgorithms, Attributes attributes)
/*     */   {
/* 116 */     this._data = new String[48];
/* 117 */     this._algorithmIds = new int[8];
/* 118 */     this._algorithmData = new Object[8];
/* 119 */     this._alphabets = new String[8];
/* 120 */     this._toIndex = new boolean[8];
/*     */ 
/* 122 */     this._registeredEncodingAlgorithms = registeredEncodingAlgorithms;
/*     */ 
/* 124 */     if (attributes != null)
/* 125 */       if ((attributes instanceof EncodingAlgorithmAttributes))
/* 126 */         setAttributes((EncodingAlgorithmAttributes)attributes);
/*     */       else
/* 128 */         setAttributes(attributes);
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/* 138 */     for (int i = 0; i < this._length; i++) {
/* 139 */       this._data[(i * 6 + 4)] = null;
/* 140 */       this._algorithmData[i] = null;
/*     */     }
/* 142 */     this._length = 0;
/*     */   }
/*     */ 
/*     */   public void addAttribute(String URI, String localName, String qName, String type, String value)
/*     */   {
/* 164 */     if (this._length >= this._algorithmData.length) {
/* 165 */       resize();
/*     */     }
/*     */ 
/* 168 */     int i = this._length * 6;
/* 169 */     this._data[(i++)] = replaceNull(URI);
/* 170 */     this._data[(i++)] = replaceNull(localName);
/* 171 */     this._data[(i++)] = replaceNull(qName);
/* 172 */     this._data[(i++)] = replaceNull(type);
/* 173 */     this._data[(i++)] = replaceNull(value);
/* 174 */     this._toIndex[this._length] = false;
/* 175 */     this._alphabets[this._length] = null;
/*     */ 
/* 177 */     this._length += 1;
/*     */   }
/*     */ 
/*     */   public void addAttribute(String URI, String localName, String qName, String type, String value, boolean index, String alphabet)
/*     */   {
/* 202 */     if (this._length >= this._algorithmData.length) {
/* 203 */       resize();
/*     */     }
/*     */ 
/* 206 */     int i = this._length * 6;
/* 207 */     this._data[(i++)] = replaceNull(URI);
/* 208 */     this._data[(i++)] = replaceNull(localName);
/* 209 */     this._data[(i++)] = replaceNull(qName);
/* 210 */     this._data[(i++)] = replaceNull(type);
/* 211 */     this._data[(i++)] = replaceNull(value);
/* 212 */     this._toIndex[this._length] = index;
/* 213 */     this._alphabets[this._length] = alphabet;
/*     */ 
/* 215 */     this._length += 1;
/*     */   }
/*     */ 
/*     */   public void addAttributeWithBuiltInAlgorithmData(String URI, String localName, String qName, int builtInAlgorithmID, Object algorithmData)
/*     */   {
/* 237 */     if (this._length >= this._algorithmData.length) {
/* 238 */       resize();
/*     */     }
/*     */ 
/* 241 */     int i = this._length * 6;
/* 242 */     this._data[(i++)] = replaceNull(URI);
/* 243 */     this._data[(i++)] = replaceNull(localName);
/* 244 */     this._data[(i++)] = replaceNull(qName);
/* 245 */     this._data[(i++)] = "CDATA";
/* 246 */     this._data[(i++)] = "";
/* 247 */     this._data[(i++)] = null;
/* 248 */     this._algorithmIds[this._length] = builtInAlgorithmID;
/* 249 */     this._algorithmData[this._length] = algorithmData;
/* 250 */     this._toIndex[this._length] = false;
/* 251 */     this._alphabets[this._length] = null;
/*     */ 
/* 253 */     this._length += 1;
/*     */   }
/*     */ 
/*     */   public void addAttributeWithAlgorithmData(String URI, String localName, String qName, String algorithmURI, int algorithmID, Object algorithmData)
/*     */   {
/* 276 */     if (this._length >= this._algorithmData.length) {
/* 277 */       resize();
/*     */     }
/*     */ 
/* 280 */     int i = this._length * 6;
/* 281 */     this._data[(i++)] = replaceNull(URI);
/* 282 */     this._data[(i++)] = replaceNull(localName);
/* 283 */     this._data[(i++)] = replaceNull(qName);
/* 284 */     this._data[(i++)] = "CDATA";
/* 285 */     this._data[(i++)] = "";
/* 286 */     this._data[(i++)] = algorithmURI;
/* 287 */     this._algorithmIds[this._length] = algorithmID;
/* 288 */     this._algorithmData[this._length] = algorithmData;
/* 289 */     this._toIndex[this._length] = false;
/* 290 */     this._alphabets[this._length] = null;
/*     */ 
/* 292 */     this._length += 1;
/*     */   }
/*     */ 
/*     */   public void replaceWithAttributeAlgorithmData(int index, String algorithmURI, int algorithmID, Object algorithmData)
/*     */   {
/* 309 */     if ((index < 0) || (index >= this._length)) return;
/*     */ 
/* 311 */     int i = index * 6;
/* 312 */     this._data[(i + 4)] = null;
/* 313 */     this._data[(i + 5)] = algorithmURI;
/* 314 */     this._algorithmIds[index] = algorithmID;
/* 315 */     this._algorithmData[index] = algorithmData;
/* 316 */     this._toIndex[index] = false;
/* 317 */     this._alphabets[index] = null;
/*     */   }
/*     */ 
/*     */   public void setAttributes(Attributes atts)
/*     */   {
/* 326 */     this._length = atts.getLength();
/* 327 */     if (this._length > 0)
/*     */     {
/* 329 */       if (this._length >= this._algorithmData.length) {
/* 330 */         resizeNoCopy();
/*     */       }
/*     */ 
/* 333 */       int index = 0;
/* 334 */       for (int i = 0; i < this._length; i++) {
/* 335 */         this._data[(index++)] = atts.getURI(i);
/* 336 */         this._data[(index++)] = atts.getLocalName(i);
/* 337 */         this._data[(index++)] = atts.getQName(i);
/* 338 */         this._data[(index++)] = atts.getType(i);
/* 339 */         this._data[(index++)] = atts.getValue(i);
/* 340 */         index++;
/* 341 */         this._toIndex[i] = false;
/* 342 */         this._alphabets[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttributes(EncodingAlgorithmAttributes atts)
/*     */   {
/* 353 */     this._length = atts.getLength();
/* 354 */     if (this._length > 0)
/*     */     {
/* 356 */       if (this._length >= this._algorithmData.length) {
/* 357 */         resizeNoCopy();
/*     */       }
/*     */ 
/* 360 */       int index = 0;
/* 361 */       for (int i = 0; i < this._length; i++) {
/* 362 */         this._data[(index++)] = atts.getURI(i);
/* 363 */         this._data[(index++)] = atts.getLocalName(i);
/* 364 */         this._data[(index++)] = atts.getQName(i);
/* 365 */         this._data[(index++)] = atts.getType(i);
/* 366 */         this._data[(index++)] = atts.getValue(i);
/* 367 */         this._data[(index++)] = atts.getAlgorithmURI(i);
/* 368 */         this._algorithmIds[i] = atts.getAlgorithmIndex(i);
/* 369 */         this._algorithmData[i] = atts.getAlgorithmData(i);
/* 370 */         this._toIndex[i] = false;
/* 371 */         this._alphabets[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getLength()
/*     */   {
/* 379 */     return this._length;
/*     */   }
/*     */ 
/*     */   public final String getLocalName(int index) {
/* 383 */     if ((index >= 0) && (index < this._length)) {
/* 384 */       return this._data[(index * 6 + 1)];
/*     */     }
/* 386 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getQName(int index)
/*     */   {
/* 391 */     if ((index >= 0) && (index < this._length)) {
/* 392 */       return this._data[(index * 6 + 2)];
/*     */     }
/* 394 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getType(int index)
/*     */   {
/* 399 */     if ((index >= 0) && (index < this._length)) {
/* 400 */       return this._data[(index * 6 + 3)];
/*     */     }
/* 402 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getURI(int index)
/*     */   {
/* 407 */     if ((index >= 0) && (index < this._length)) {
/* 408 */       return this._data[(index * 6 + 0)];
/*     */     }
/* 410 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getValue(int index)
/*     */   {
/* 415 */     if ((index >= 0) && (index < this._length)) {
/* 416 */       String value = this._data[(index * 6 + 4)];
/* 417 */       if (value != null) return value; 
/*     */     }
/* 419 */     else { return null; }
/*     */ 
/*     */ 
/* 422 */     if ((this._algorithmData[index] == null) || (this._registeredEncodingAlgorithms == null)) {
/* 423 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 427 */       return this._data[(index * 6 + 4)] =  = convertEncodingAlgorithmDataToString(this._algorithmIds[index], this._data[(index * 6 + 5)], this._algorithmData[index]).toString();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 432 */       return null; } catch (FastInfosetException e) {
/*     */     }
/* 434 */     return null;
/*     */   }
/*     */ 
/*     */   public final int getIndex(String qName)
/*     */   {
/* 439 */     for (int index = 0; index < this._length; index++) {
/* 440 */       if (qName.equals(this._data[(index * 6 + 2)])) {
/* 441 */         return index;
/*     */       }
/*     */     }
/* 444 */     return -1;
/*     */   }
/*     */ 
/*     */   public final String getType(String qName) {
/* 448 */     int index = getIndex(qName);
/* 449 */     if (index >= 0) {
/* 450 */       return this._data[(index * 6 + 3)];
/*     */     }
/* 452 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getValue(String qName)
/*     */   {
/* 457 */     int index = getIndex(qName);
/* 458 */     if (index >= 0) {
/* 459 */       return getValue(index);
/*     */     }
/* 461 */     return null;
/*     */   }
/*     */ 
/*     */   public final int getIndex(String uri, String localName)
/*     */   {
/* 466 */     for (int index = 0; index < this._length; index++) {
/* 467 */       if ((localName.equals(this._data[(index * 6 + 1)])) && (uri.equals(this._data[(index * 6 + 0)])))
/*     */       {
/* 469 */         return index;
/*     */       }
/*     */     }
/* 472 */     return -1;
/*     */   }
/*     */ 
/*     */   public final String getType(String uri, String localName) {
/* 476 */     int index = getIndex(uri, localName);
/* 477 */     if (index >= 0) {
/* 478 */       return this._data[(index * 6 + 3)];
/*     */     }
/* 480 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getValue(String uri, String localName)
/*     */   {
/* 485 */     int index = getIndex(uri, localName);
/* 486 */     if (index >= 0) {
/* 487 */       return getValue(index);
/*     */     }
/* 489 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithmURI(int index)
/*     */   {
/* 496 */     if ((index >= 0) && (index < this._length)) {
/* 497 */       return this._data[(index * 6 + 5)];
/*     */     }
/* 499 */     return null;
/*     */   }
/*     */ 
/*     */   public final int getAlgorithmIndex(int index)
/*     */   {
/* 504 */     if ((index >= 0) && (index < this._length)) {
/* 505 */       return this._algorithmIds[index];
/*     */     }
/* 507 */     return -1;
/*     */   }
/*     */ 
/*     */   public final Object getAlgorithmData(int index)
/*     */   {
/* 512 */     if ((index >= 0) && (index < this._length)) {
/* 513 */       return this._algorithmData[index];
/*     */     }
/* 515 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getAlpababet(int index)
/*     */   {
/* 522 */     if ((index >= 0) && (index < this._length)) {
/* 523 */       return this._alphabets[index];
/*     */     }
/* 525 */     return null;
/*     */   }
/*     */ 
/*     */   public final boolean getToIndex(int index)
/*     */   {
/* 530 */     if ((index >= 0) && (index < this._length)) {
/* 531 */       return this._toIndex[index];
/*     */     }
/* 533 */     return false;
/*     */   }
/*     */ 
/*     */   private final String replaceNull(String s)
/*     */   {
/* 540 */     return s != null ? s : "";
/*     */   }
/*     */ 
/*     */   private final void resizeNoCopy() {
/* 544 */     int newLength = this._length * 3 / 2 + 1;
/*     */ 
/* 546 */     this._data = new String[newLength * 6];
/* 547 */     this._algorithmIds = new int[newLength];
/* 548 */     this._algorithmData = new Object[newLength];
/*     */   }
/*     */ 
/*     */   private final void resize() {
/* 552 */     int newLength = this._length * 3 / 2 + 1;
/*     */ 
/* 554 */     String[] data = new String[newLength * 6];
/* 555 */     int[] algorithmIds = new int[newLength];
/* 556 */     Object[] algorithmData = new Object[newLength];
/* 557 */     String[] alphabets = new String[newLength];
/* 558 */     boolean[] toIndex = new boolean[newLength];
/*     */ 
/* 560 */     System.arraycopy(this._data, 0, data, 0, this._length * 6);
/* 561 */     System.arraycopy(this._algorithmIds, 0, algorithmIds, 0, this._length);
/* 562 */     System.arraycopy(this._algorithmData, 0, algorithmData, 0, this._length);
/* 563 */     System.arraycopy(this._alphabets, 0, alphabets, 0, this._length);
/* 564 */     System.arraycopy(this._toIndex, 0, toIndex, 0, this._length);
/*     */ 
/* 566 */     this._data = data;
/* 567 */     this._algorithmIds = algorithmIds;
/* 568 */     this._algorithmData = algorithmData;
/* 569 */     this._alphabets = alphabets;
/* 570 */     this._toIndex = toIndex;
/*     */   }
/*     */ 
/*     */   private final StringBuffer convertEncodingAlgorithmDataToString(int identifier, String URI, Object data) throws FastInfosetException, IOException
/*     */   {
/* 575 */     EncodingAlgorithm ea = null;
/* 576 */     if (identifier < 9) {
/* 577 */       ea = BuiltInEncodingAlgorithmFactory.getAlgorithm(identifier); } else {
/* 578 */       if (identifier == 9) {
/* 579 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
/*     */       }
/* 581 */       if (identifier >= 32) {
/* 582 */         if (URI == null) {
/* 583 */           throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent") + identifier);
/*     */         }
/*     */ 
/* 587 */         ea = (EncodingAlgorithm)this._registeredEncodingAlgorithms.get(URI);
/* 588 */         if (ea == null) {
/* 589 */           throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmNotRegistered") + URI);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 596 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
/*     */       }
/*     */     }
/*     */ 
/* 600 */     StringBuffer sb = new StringBuffer();
/* 601 */     ea.convertToCharacters(data, sb);
/* 602 */     return sb;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.sax.helpers.EncodingAlgorithmAttributesImpl
 * JD-Core Version:    0.6.2
 */