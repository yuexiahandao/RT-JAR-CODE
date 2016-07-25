/*     */ package com.sun.xml.internal.stream.buffer;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public final class AttributesHolder
/*     */   implements Attributes
/*     */ {
/*     */   protected static final int DEFAULT_CAPACITY = 8;
/*     */   protected static final int ITEM_SIZE = 8;
/*     */   protected static final int PREFIX = 0;
/*     */   protected static final int URI = 1;
/*     */   protected static final int LOCAL_NAME = 2;
/*     */   protected static final int QNAME = 3;
/*     */   protected static final int TYPE = 4;
/*     */   protected static final int VALUE = 5;
/*     */   protected int _attributeCount;
/*     */   protected String[] _strings;
/*     */ 
/*     */   public AttributesHolder()
/*     */   {
/*  53 */     this._strings = new String[64];
/*     */   }
/*     */ 
/*     */   public final int getLength() {
/*  57 */     return this._attributeCount;
/*     */   }
/*     */ 
/*     */   public final String getPrefix(int index) {
/*  61 */     return (index >= 0) && (index < this._attributeCount) ? this._strings[((index << 3) + 0)] : null;
/*     */   }
/*     */ 
/*     */   public final String getLocalName(int index)
/*     */   {
/*  66 */     return (index >= 0) && (index < this._attributeCount) ? this._strings[((index << 3) + 2)] : null;
/*     */   }
/*     */ 
/*     */   public final String getQName(int index)
/*     */   {
/*  71 */     return (index >= 0) && (index < this._attributeCount) ? this._strings[((index << 3) + 3)] : null;
/*     */   }
/*     */ 
/*     */   public final String getType(int index)
/*     */   {
/*  76 */     return (index >= 0) && (index < this._attributeCount) ? this._strings[((index << 3) + 4)] : null;
/*     */   }
/*     */ 
/*     */   public final String getURI(int index)
/*     */   {
/*  81 */     return (index >= 0) && (index < this._attributeCount) ? this._strings[((index << 3) + 1)] : null;
/*     */   }
/*     */ 
/*     */   public final String getValue(int index)
/*     */   {
/*  86 */     return (index >= 0) && (index < this._attributeCount) ? this._strings[((index << 3) + 5)] : null;
/*     */   }
/*     */ 
/*     */   public final int getIndex(String qName)
/*     */   {
/*  91 */     for (int i = 0; i < this._attributeCount; i++) {
/*  92 */       if (qName.equals(this._strings[((i << 3) + 3)])) {
/*  93 */         return i;
/*     */       }
/*     */     }
/*  96 */     return -1;
/*     */   }
/*     */ 
/*     */   public final String getType(String qName) {
/* 100 */     int i = (getIndex(qName) << 3) + 4;
/* 101 */     return i >= 0 ? this._strings[i] : null;
/*     */   }
/*     */ 
/*     */   public final String getValue(String qName) {
/* 105 */     int i = (getIndex(qName) << 3) + 5;
/* 106 */     return i >= 0 ? this._strings[i] : null;
/*     */   }
/*     */ 
/*     */   public final int getIndex(String uri, String localName) {
/* 110 */     for (int i = 0; i < this._attributeCount; i++) {
/* 111 */       if ((localName.equals(this._strings[((i << 3) + 2)])) && (uri.equals(this._strings[((i << 3) + 1)])))
/*     */       {
/* 113 */         return i;
/*     */       }
/*     */     }
/* 116 */     return -1;
/*     */   }
/*     */ 
/*     */   public final String getType(String uri, String localName) {
/* 120 */     int i = (getIndex(uri, localName) << 3) + 4;
/* 121 */     return i >= 0 ? this._strings[i] : null;
/*     */   }
/*     */ 
/*     */   public final String getValue(String uri, String localName) {
/* 125 */     int i = (getIndex(uri, localName) << 3) + 5;
/* 126 */     return i >= 0 ? this._strings[i] : null;
/*     */   }
/*     */ 
/*     */   public final void clear() {
/* 130 */     if (this._attributeCount > 0) {
/* 131 */       for (int i = 0; i < this._attributeCount; i++) {
/* 132 */         this._strings[((i << 3) + 5)] = null;
/*     */       }
/* 134 */       this._attributeCount = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void addAttributeWithQName(String uri, String localName, String qName, String type, String value)
/*     */   {
/* 147 */     int i = this._attributeCount << 3;
/* 148 */     if (i == this._strings.length) {
/* 149 */       resize(i);
/*     */     }
/*     */ 
/* 152 */     this._strings[(i + 0)] = null;
/* 153 */     this._strings[(i + 1)] = uri;
/* 154 */     this._strings[(i + 2)] = localName;
/* 155 */     this._strings[(i + 3)] = qName;
/* 156 */     this._strings[(i + 4)] = type;
/* 157 */     this._strings[(i + 5)] = value;
/*     */ 
/* 159 */     this._attributeCount += 1;
/*     */   }
/*     */ 
/*     */   public final void addAttributeWithPrefix(String prefix, String uri, String localName, String type, String value)
/*     */   {
/* 171 */     int i = this._attributeCount << 3;
/* 172 */     if (i == this._strings.length) {
/* 173 */       resize(i);
/*     */     }
/*     */ 
/* 176 */     this._strings[(i + 0)] = prefix;
/* 177 */     this._strings[(i + 1)] = uri;
/* 178 */     this._strings[(i + 2)] = localName;
/* 179 */     this._strings[(i + 3)] = null;
/* 180 */     this._strings[(i + 4)] = type;
/* 181 */     this._strings[(i + 5)] = value;
/*     */ 
/* 183 */     this._attributeCount += 1;
/*     */   }
/*     */ 
/*     */   private void resize(int length) {
/* 187 */     int newLength = length * 2;
/* 188 */     String[] strings = new String[newLength];
/* 189 */     System.arraycopy(this._strings, 0, strings, 0, length);
/* 190 */     this._strings = strings;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.AttributesHolder
 * JD-Core Version:    0.6.2
 */