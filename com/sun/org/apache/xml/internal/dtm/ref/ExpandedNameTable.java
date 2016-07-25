/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ public class ExpandedNameTable
/*     */ {
/*     */   private ExtendedType[] m_extendedTypes;
/*  46 */   private static int m_initialSize = 128;
/*     */   private int m_nextType;
/*     */   public static final int ELEMENT = 1;
/*     */   public static final int ATTRIBUTE = 2;
/*     */   public static final int TEXT = 3;
/*     */   public static final int CDATA_SECTION = 4;
/*     */   public static final int ENTITY_REFERENCE = 5;
/*     */   public static final int ENTITY = 6;
/*     */   public static final int PROCESSING_INSTRUCTION = 7;
/*     */   public static final int COMMENT = 8;
/*     */   public static final int DOCUMENT = 9;
/*     */   public static final int DOCUMENT_TYPE = 10;
/*     */   public static final int DOCUMENT_FRAGMENT = 11;
/*     */   public static final int NOTATION = 12;
/*     */   public static final int NAMESPACE = 13;
/*  70 */   ExtendedType hashET = new ExtendedType(-1, "", "");
/*     */   private static ExtendedType[] m_defaultExtendedTypes;
/*  79 */   private static float m_loadFactor = 0.75F;
/*     */ 
/*  85 */   private static int m_initialCapacity = 203;
/*     */   private int m_capacity;
/*     */   private int m_threshold;
/*     */   private HashEntry[] m_table;
/*     */ 
/*     */   public ExpandedNameTable()
/*     */   {
/* 123 */     this.m_capacity = m_initialCapacity;
/* 124 */     this.m_threshold = ((int)(this.m_capacity * m_loadFactor));
/* 125 */     this.m_table = new HashEntry[this.m_capacity];
/*     */ 
/* 127 */     initExtendedTypes();
/*     */   }
/*     */ 
/*     */   private void initExtendedTypes()
/*     */   {
/* 137 */     this.m_extendedTypes = new ExtendedType[m_initialSize];
/* 138 */     for (int i = 0; i < 14; i++) {
/* 139 */       this.m_extendedTypes[i] = m_defaultExtendedTypes[i];
/* 140 */       this.m_table[i] = new HashEntry(m_defaultExtendedTypes[i], i, i, null);
/*     */     }
/*     */ 
/* 143 */     this.m_nextType = 14;
/*     */   }
/*     */ 
/*     */   public int getExpandedTypeID(String namespace, String localName, int type)
/*     */   {
/* 160 */     return getExpandedTypeID(namespace, localName, type, false);
/*     */   }
/*     */ 
/*     */   public int getExpandedTypeID(String namespace, String localName, int type, boolean searchOnly)
/*     */   {
/* 183 */     if (null == namespace)
/* 184 */       namespace = "";
/* 185 */     if (null == localName) {
/* 186 */       localName = "";
/*     */     }
/*     */ 
/* 189 */     int hash = type + namespace.hashCode() + localName.hashCode();
/*     */ 
/* 192 */     this.hashET.redefine(type, namespace, localName, hash);
/*     */ 
/* 195 */     int index = hash % this.m_capacity;
/* 196 */     if (index < 0) {
/* 197 */       index = -index;
/*     */     }
/*     */ 
/* 201 */     for (HashEntry e = this.m_table[index]; e != null; e = e.next)
/*     */     {
/* 203 */       if ((e.hash == hash) && (e.key.equals(this.hashET))) {
/* 204 */         return e.value;
/*     */       }
/*     */     }
/* 207 */     if (searchOnly)
/*     */     {
/* 209 */       return -1;
/*     */     }
/*     */ 
/* 213 */     if (this.m_nextType > this.m_threshold) {
/* 214 */       rehash();
/* 215 */       index = hash % this.m_capacity;
/* 216 */       if (index < 0) {
/* 217 */         index = -index;
/*     */       }
/*     */     }
/*     */ 
/* 221 */     ExtendedType newET = new ExtendedType(type, namespace, localName, hash);
/*     */ 
/* 224 */     if (this.m_extendedTypes.length == this.m_nextType) {
/* 225 */       ExtendedType[] newArray = new ExtendedType[this.m_extendedTypes.length * 2];
/* 226 */       System.arraycopy(this.m_extendedTypes, 0, newArray, 0, this.m_extendedTypes.length);
/*     */ 
/* 228 */       this.m_extendedTypes = newArray;
/*     */     }
/*     */ 
/* 231 */     this.m_extendedTypes[this.m_nextType] = newET;
/*     */ 
/* 235 */     HashEntry entry = new HashEntry(newET, this.m_nextType, hash, this.m_table[index]);
/* 236 */     this.m_table[index] = entry;
/*     */ 
/* 238 */     return this.m_nextType++;
/*     */   }
/*     */ 
/*     */   private void rehash()
/*     */   {
/* 249 */     int oldCapacity = this.m_capacity;
/* 250 */     HashEntry[] oldTable = this.m_table;
/*     */ 
/* 252 */     int newCapacity = 2 * oldCapacity + 1;
/* 253 */     this.m_capacity = newCapacity;
/* 254 */     this.m_threshold = ((int)(newCapacity * m_loadFactor));
/*     */ 
/* 256 */     this.m_table = new HashEntry[newCapacity];
/*     */     HashEntry old;
/* 257 */     for (int i = oldCapacity - 1; i >= 0; i--)
/*     */     {
/* 259 */       for (old = oldTable[i]; old != null; )
/*     */       {
/* 261 */         HashEntry e = old;
/* 262 */         old = old.next;
/*     */ 
/* 264 */         int newIndex = e.hash % newCapacity;
/* 265 */         if (newIndex < 0) {
/* 266 */           newIndex = -newIndex;
/*     */         }
/* 268 */         e.next = this.m_table[newIndex];
/* 269 */         this.m_table[newIndex] = e;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getExpandedTypeID(int type)
/*     */   {
/* 282 */     return type;
/*     */   }
/*     */ 
/*     */   public String getLocalName(int ExpandedNameID)
/*     */   {
/* 293 */     return this.m_extendedTypes[ExpandedNameID].getLocalName();
/*     */   }
/*     */ 
/*     */   public final int getLocalNameID(int ExpandedNameID)
/*     */   {
/* 305 */     if (this.m_extendedTypes[ExpandedNameID].getLocalName().equals("")) {
/* 306 */       return 0;
/*     */     }
/* 308 */     return ExpandedNameID;
/*     */   }
/*     */ 
/*     */   public String getNamespace(int ExpandedNameID)
/*     */   {
/* 321 */     String namespace = this.m_extendedTypes[ExpandedNameID].getNamespace();
/* 322 */     return namespace.equals("") ? null : namespace;
/*     */   }
/*     */ 
/*     */   public final int getNamespaceID(int ExpandedNameID)
/*     */   {
/* 334 */     if (this.m_extendedTypes[ExpandedNameID].getNamespace().equals("")) {
/* 335 */       return 0;
/*     */     }
/* 337 */     return ExpandedNameID;
/*     */   }
/*     */ 
/*     */   public final short getType(int ExpandedNameID)
/*     */   {
/* 349 */     return (short)this.m_extendedTypes[ExpandedNameID].getNodeType();
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 359 */     return this.m_nextType;
/*     */   }
/*     */ 
/*     */   public ExtendedType[] getExtendedTypes()
/*     */   {
/* 369 */     return this.m_extendedTypes;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 110 */     m_defaultExtendedTypes = new ExtendedType[14];
/*     */ 
/* 112 */     for (int i = 0; i < 14; i++)
/*     */     {
/* 114 */       m_defaultExtendedTypes[i] = new ExtendedType(i, "", "");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class HashEntry
/*     */   {
/*     */     ExtendedType key;
/*     */     int value;
/*     */     int hash;
/*     */     HashEntry next;
/*     */ 
/*     */     protected HashEntry(ExtendedType key, int value, int hash, HashEntry next)
/*     */     {
/* 386 */       this.key = key;
/* 387 */       this.value = value;
/* 388 */       this.hash = hash;
/* 389 */       this.next = next;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.ExpandedNameTable
 * JD-Core Version:    0.6.2
 */