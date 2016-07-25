/*     */ package com.sun.xml.internal.fastinfoset;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class QualifiedName
/*     */ {
/*     */   public String prefix;
/*     */   public String namespaceName;
/*     */   public String localName;
/*     */   public String qName;
/*     */   public int index;
/*     */   public int prefixIndex;
/*     */   public int namespaceNameIndex;
/*     */   public int localNameIndex;
/*     */   public int attributeId;
/*     */   public int attributeHash;
/*     */   private QName qNameObject;
/*     */ 
/*     */   public QualifiedName()
/*     */   {
/*     */   }
/*     */ 
/*     */   public QualifiedName(String prefix, String namespaceName, String localName, String qName)
/*     */   {
/*  48 */     this.prefix = prefix;
/*  49 */     this.namespaceName = namespaceName;
/*  50 */     this.localName = localName;
/*  51 */     this.qName = qName;
/*  52 */     this.index = -1;
/*  53 */     this.prefixIndex = 0;
/*  54 */     this.namespaceNameIndex = 0;
/*  55 */     this.localNameIndex = -1;
/*     */   }
/*     */ 
/*     */   public void set(String prefix, String namespaceName, String localName, String qName) {
/*  59 */     this.prefix = prefix;
/*  60 */     this.namespaceName = namespaceName;
/*  61 */     this.localName = localName;
/*  62 */     this.qName = qName;
/*  63 */     this.index = -1;
/*  64 */     this.prefixIndex = 0;
/*  65 */     this.namespaceNameIndex = 0;
/*  66 */     this.localNameIndex = -1;
/*  67 */     this.qNameObject = null;
/*     */   }
/*     */ 
/*     */   public QualifiedName(String prefix, String namespaceName, String localName, String qName, int index) {
/*  71 */     this.prefix = prefix;
/*  72 */     this.namespaceName = namespaceName;
/*  73 */     this.localName = localName;
/*  74 */     this.qName = qName;
/*  75 */     this.index = index;
/*  76 */     this.prefixIndex = 0;
/*  77 */     this.namespaceNameIndex = 0;
/*  78 */     this.localNameIndex = -1;
/*     */   }
/*     */ 
/*     */   public final QualifiedName set(String prefix, String namespaceName, String localName, String qName, int index) {
/*  82 */     this.prefix = prefix;
/*  83 */     this.namespaceName = namespaceName;
/*  84 */     this.localName = localName;
/*  85 */     this.qName = qName;
/*  86 */     this.index = index;
/*  87 */     this.prefixIndex = 0;
/*  88 */     this.namespaceNameIndex = 0;
/*  89 */     this.localNameIndex = -1;
/*  90 */     this.qNameObject = null;
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   public QualifiedName(String prefix, String namespaceName, String localName, String qName, int index, int prefixIndex, int namespaceNameIndex, int localNameIndex)
/*     */   {
/*  96 */     this.prefix = prefix;
/*  97 */     this.namespaceName = namespaceName;
/*  98 */     this.localName = localName;
/*  99 */     this.qName = qName;
/* 100 */     this.index = index;
/* 101 */     this.prefixIndex = (prefixIndex + 1);
/* 102 */     this.namespaceNameIndex = (namespaceNameIndex + 1);
/* 103 */     this.localNameIndex = localNameIndex;
/*     */   }
/*     */ 
/*     */   public final QualifiedName set(String prefix, String namespaceName, String localName, String qName, int index, int prefixIndex, int namespaceNameIndex, int localNameIndex)
/*     */   {
/* 108 */     this.prefix = prefix;
/* 109 */     this.namespaceName = namespaceName;
/* 110 */     this.localName = localName;
/* 111 */     this.qName = qName;
/* 112 */     this.index = index;
/* 113 */     this.prefixIndex = (prefixIndex + 1);
/* 114 */     this.namespaceNameIndex = (namespaceNameIndex + 1);
/* 115 */     this.localNameIndex = localNameIndex;
/* 116 */     this.qNameObject = null;
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   public QualifiedName(String prefix, String namespaceName, String localName) {
/* 121 */     this.prefix = prefix;
/* 122 */     this.namespaceName = namespaceName;
/* 123 */     this.localName = localName;
/* 124 */     this.qName = createQNameString(prefix, localName);
/* 125 */     this.index = -1;
/* 126 */     this.prefixIndex = 0;
/* 127 */     this.namespaceNameIndex = 0;
/* 128 */     this.localNameIndex = -1;
/*     */   }
/*     */ 
/*     */   public final QualifiedName set(String prefix, String namespaceName, String localName) {
/* 132 */     this.prefix = prefix;
/* 133 */     this.namespaceName = namespaceName;
/* 134 */     this.localName = localName;
/* 135 */     this.qName = createQNameString(prefix, localName);
/* 136 */     this.index = -1;
/* 137 */     this.prefixIndex = 0;
/* 138 */     this.namespaceNameIndex = 0;
/* 139 */     this.localNameIndex = -1;
/* 140 */     this.qNameObject = null;
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */   public QualifiedName(String prefix, String namespaceName, String localName, int prefixIndex, int namespaceNameIndex, int localNameIndex, char[] charBuffer)
/*     */   {
/* 147 */     this.prefix = prefix;
/* 148 */     this.namespaceName = namespaceName;
/* 149 */     this.localName = localName;
/*     */ 
/* 151 */     if (charBuffer != null) {
/* 152 */       int l1 = prefix.length();
/* 153 */       int l2 = localName.length();
/* 154 */       int total = l1 + l2 + 1;
/* 155 */       if (total < charBuffer.length) {
/* 156 */         prefix.getChars(0, l1, charBuffer, 0);
/* 157 */         charBuffer[l1] = ':';
/* 158 */         localName.getChars(0, l2, charBuffer, l1 + 1);
/* 159 */         this.qName = new String(charBuffer, 0, total);
/*     */       } else {
/* 161 */         this.qName = createQNameString(prefix, localName);
/*     */       }
/*     */     } else {
/* 164 */       this.qName = this.localName;
/*     */     }
/*     */ 
/* 167 */     this.prefixIndex = (prefixIndex + 1);
/* 168 */     this.namespaceNameIndex = (namespaceNameIndex + 1);
/* 169 */     this.localNameIndex = localNameIndex;
/* 170 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   public final QualifiedName set(String prefix, String namespaceName, String localName, int prefixIndex, int namespaceNameIndex, int localNameIndex, char[] charBuffer)
/*     */   {
/* 176 */     this.prefix = prefix;
/* 177 */     this.namespaceName = namespaceName;
/* 178 */     this.localName = localName;
/*     */ 
/* 180 */     if (charBuffer != null) {
/* 181 */       int l1 = prefix.length();
/* 182 */       int l2 = localName.length();
/* 183 */       int total = l1 + l2 + 1;
/* 184 */       if (total < charBuffer.length) {
/* 185 */         prefix.getChars(0, l1, charBuffer, 0);
/* 186 */         charBuffer[l1] = ':';
/* 187 */         localName.getChars(0, l2, charBuffer, l1 + 1);
/* 188 */         this.qName = new String(charBuffer, 0, total);
/*     */       } else {
/* 190 */         this.qName = createQNameString(prefix, localName);
/*     */       }
/*     */     } else {
/* 193 */       this.qName = this.localName;
/*     */     }
/*     */ 
/* 196 */     this.prefixIndex = (prefixIndex + 1);
/* 197 */     this.namespaceNameIndex = (namespaceNameIndex + 1);
/* 198 */     this.localNameIndex = localNameIndex;
/* 199 */     this.index = -1;
/* 200 */     this.qNameObject = null;
/* 201 */     return this;
/*     */   }
/*     */ 
/*     */   public QualifiedName(String prefix, String namespaceName, String localName, int index) {
/* 205 */     this.prefix = prefix;
/* 206 */     this.namespaceName = namespaceName;
/* 207 */     this.localName = localName;
/* 208 */     this.qName = createQNameString(prefix, localName);
/* 209 */     this.index = index;
/* 210 */     this.prefixIndex = 0;
/* 211 */     this.namespaceNameIndex = 0;
/* 212 */     this.localNameIndex = -1;
/*     */   }
/*     */ 
/*     */   public final QualifiedName set(String prefix, String namespaceName, String localName, int index) {
/* 216 */     this.prefix = prefix;
/* 217 */     this.namespaceName = namespaceName;
/* 218 */     this.localName = localName;
/* 219 */     this.qName = createQNameString(prefix, localName);
/* 220 */     this.index = index;
/* 221 */     this.prefixIndex = 0;
/* 222 */     this.namespaceNameIndex = 0;
/* 223 */     this.localNameIndex = -1;
/* 224 */     this.qNameObject = null;
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */   public QualifiedName(String prefix, String namespaceName, String localName, int index, int prefixIndex, int namespaceNameIndex, int localNameIndex)
/*     */   {
/* 230 */     this.prefix = prefix;
/* 231 */     this.namespaceName = namespaceName;
/* 232 */     this.localName = localName;
/* 233 */     this.qName = createQNameString(prefix, localName);
/* 234 */     this.index = index;
/* 235 */     this.prefixIndex = (prefixIndex + 1);
/* 236 */     this.namespaceNameIndex = (namespaceNameIndex + 1);
/* 237 */     this.localNameIndex = localNameIndex;
/*     */   }
/*     */ 
/*     */   public final QualifiedName set(String prefix, String namespaceName, String localName, int index, int prefixIndex, int namespaceNameIndex, int localNameIndex)
/*     */   {
/* 242 */     this.prefix = prefix;
/* 243 */     this.namespaceName = namespaceName;
/* 244 */     this.localName = localName;
/* 245 */     this.qName = createQNameString(prefix, localName);
/* 246 */     this.index = index;
/* 247 */     this.prefixIndex = (prefixIndex + 1);
/* 248 */     this.namespaceNameIndex = (namespaceNameIndex + 1);
/* 249 */     this.localNameIndex = localNameIndex;
/* 250 */     this.qNameObject = null;
/* 251 */     return this;
/*     */   }
/*     */ 
/*     */   public QualifiedName(String prefix, String namespaceName)
/*     */   {
/* 256 */     this.prefix = prefix;
/* 257 */     this.namespaceName = namespaceName;
/* 258 */     this.localName = "";
/* 259 */     this.qName = "";
/* 260 */     this.index = -1;
/* 261 */     this.prefixIndex = 0;
/* 262 */     this.namespaceNameIndex = 0;
/* 263 */     this.localNameIndex = -1;
/*     */   }
/*     */ 
/*     */   public final QualifiedName set(String prefix, String namespaceName) {
/* 267 */     this.prefix = prefix;
/* 268 */     this.namespaceName = namespaceName;
/* 269 */     this.localName = "";
/* 270 */     this.qName = "";
/* 271 */     this.index = -1;
/* 272 */     this.prefixIndex = 0;
/* 273 */     this.namespaceNameIndex = 0;
/* 274 */     this.localNameIndex = -1;
/* 275 */     this.qNameObject = null;
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */   public final QName getQName() {
/* 280 */     if (this.qNameObject == null) {
/* 281 */       this.qNameObject = new QName(this.namespaceName, this.localName, this.prefix);
/*     */     }
/*     */ 
/* 284 */     return this.qNameObject;
/*     */   }
/*     */ 
/*     */   public final String getQNameString() {
/* 288 */     if (this.qName != "") {
/* 289 */       return this.qName;
/*     */     }
/*     */ 
/* 292 */     return this.qName = createQNameString(this.prefix, this.localName);
/*     */   }
/*     */ 
/*     */   public final void createAttributeValues(int size) {
/* 296 */     this.attributeId = (this.localNameIndex | this.namespaceNameIndex << 20);
/* 297 */     this.attributeHash = (this.localNameIndex % size);
/*     */   }
/*     */ 
/*     */   private final String createQNameString(String p, String l) {
/* 301 */     if ((p != null) && (p.length() > 0)) {
/* 302 */       StringBuffer b = new StringBuffer(p);
/* 303 */       b.append(':');
/* 304 */       b.append(l);
/* 305 */       return b.toString();
/*     */     }
/* 307 */     return l;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.QualifiedName
 * JD-Core Version:    0.6.2
 */