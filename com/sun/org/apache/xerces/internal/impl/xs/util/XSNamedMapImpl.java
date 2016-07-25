/*     */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObject;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class XSNamedMapImpl extends AbstractMap
/*     */   implements XSNamedMap
/*     */ {
/*  51 */   public static final XSNamedMapImpl EMPTY_MAP = new XSNamedMapImpl(new XSObject[0], 0);
/*     */   final String[] fNamespaces;
/*     */   final int fNSNum;
/*     */   final SymbolHash[] fMaps;
/*  61 */   XSObject[] fArray = null;
/*     */ 
/*  64 */   int fLength = -1;
/*     */ 
/*  66 */   private Set fEntrySet = null;
/*     */ 
/*     */   public XSNamedMapImpl(String namespace, SymbolHash map)
/*     */   {
/*  75 */     this.fNamespaces = new String[] { namespace };
/*  76 */     this.fMaps = new SymbolHash[] { map };
/*  77 */     this.fNSNum = 1;
/*     */   }
/*     */ 
/*     */   public XSNamedMapImpl(String[] namespaces, SymbolHash[] maps, int num)
/*     */   {
/*  88 */     this.fNamespaces = namespaces;
/*  89 */     this.fMaps = maps;
/*  90 */     this.fNSNum = num;
/*     */   }
/*     */ 
/*     */   public XSNamedMapImpl(XSObject[] array, int length)
/*     */   {
/* 100 */     if (length == 0) {
/* 101 */       this.fNamespaces = null;
/* 102 */       this.fMaps = null;
/* 103 */       this.fNSNum = 0;
/* 104 */       this.fArray = array;
/* 105 */       this.fLength = 0;
/* 106 */       return;
/*     */     }
/*     */ 
/* 110 */     this.fNamespaces = new String[] { array[0].getNamespace() };
/* 111 */     this.fMaps = null;
/* 112 */     this.fNSNum = 1;
/*     */ 
/* 114 */     this.fArray = array;
/* 115 */     this.fLength = length;
/*     */   }
/*     */ 
/*     */   public synchronized int getLength()
/*     */   {
/* 124 */     if (this.fLength == -1) {
/* 125 */       this.fLength = 0;
/* 126 */       for (int i = 0; i < this.fNSNum; i++) {
/* 127 */         this.fLength += this.fMaps[i].getLength();
/*     */       }
/*     */     }
/* 130 */     return this.fLength;
/*     */   }
/*     */ 
/*     */   public XSObject itemByName(String namespace, String localName)
/*     */   {
/* 149 */     for (int i = 0; i < this.fNSNum; i++) {
/* 150 */       if (isEqual(namespace, this.fNamespaces[i]))
/*     */       {
/* 153 */         if (this.fMaps != null) {
/* 154 */           return (XSObject)this.fMaps[i].get(localName);
/*     */         }
/*     */ 
/* 159 */         for (int j = 0; j < this.fLength; j++) {
/* 160 */           XSObject ret = this.fArray[j];
/* 161 */           if (ret.getName().equals(localName)) {
/* 162 */             return ret;
/*     */           }
/*     */         }
/* 165 */         return null;
/*     */       }
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized XSObject item(int index)
/*     */   {
/* 181 */     if (this.fArray == null)
/*     */     {
/* 183 */       getLength();
/* 184 */       this.fArray = new XSObject[this.fLength];
/* 185 */       int pos = 0;
/*     */ 
/* 187 */       for (int i = 0; i < this.fNSNum; i++) {
/* 188 */         pos += this.fMaps[i].getValues(this.fArray, pos);
/*     */       }
/*     */     }
/* 191 */     if ((index < 0) || (index >= this.fLength)) {
/* 192 */       return null;
/*     */     }
/* 194 */     return this.fArray[index];
/*     */   }
/*     */ 
/*     */   static boolean isEqual(String one, String two) {
/* 198 */     return two == null ? true : one != null ? one.equals(two) : false;
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object key)
/*     */   {
/* 206 */     return get(key) != null;
/*     */   }
/*     */ 
/*     */   public Object get(Object key) {
/* 210 */     if ((key instanceof QName)) {
/* 211 */       QName name = (QName)key;
/* 212 */       String namespaceURI = name.getNamespaceURI();
/* 213 */       if ("".equals(namespaceURI)) {
/* 214 */         namespaceURI = null;
/*     */       }
/* 216 */       String localPart = name.getLocalPart();
/* 217 */       return itemByName(namespaceURI, localPart);
/*     */     }
/* 219 */     return null;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 223 */     return getLength();
/*     */   }
/*     */ 
/*     */   public synchronized Set entrySet()
/*     */   {
/* 228 */     if (this.fEntrySet == null) {
/* 229 */       final int length = getLength();
/* 230 */       final XSNamedMapEntry[] entries = new XSNamedMapEntry[length];
/* 231 */       for (int i = 0; i < length; i++) {
/* 232 */         XSObject xso = item(i);
/* 233 */         entries[i] = new XSNamedMapEntry(new QName(xso.getNamespace(), xso.getName()), xso);
/*     */       }
/*     */ 
/* 236 */       this.fEntrySet = new AbstractSet() {
/*     */         public Iterator iterator() {
/* 238 */           return new Iterator() {
/* 239 */             private int index = 0;
/*     */ 
/* 241 */             public boolean hasNext() { return this.index < XSNamedMapImpl.1.this.val$length; }
/*     */ 
/*     */             public Object next() {
/* 244 */               if (this.index < XSNamedMapImpl.1.this.val$length) {
/* 245 */                 return XSNamedMapImpl.1.this.val$entries[(this.index++)];
/*     */               }
/* 247 */               throw new NoSuchElementException();
/*     */             }
/*     */             public void remove() {
/* 250 */               throw new UnsupportedOperationException();
/*     */             } } ;
/*     */         }
/*     */ 
/*     */         public int size() {
/* 255 */           return length;
/*     */         }
/*     */       };
/*     */     }
/* 259 */     return this.fEntrySet;
/*     */   }
/*     */   private static final class XSNamedMapEntry implements Map.Entry {
/*     */     private final QName key;
/*     */     private final XSObject value;
/*     */ 
/*     */     public XSNamedMapEntry(QName key, XSObject value) {
/* 267 */       this.key = key;
/* 268 */       this.value = value;
/*     */     }
/*     */     public Object getKey() {
/* 271 */       return this.key;
/*     */     }
/*     */     public Object getValue() {
/* 274 */       return this.value;
/*     */     }
/*     */     public Object setValue(Object value) {
/* 277 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public boolean equals(Object o) {
/* 280 */       if ((o instanceof Map.Entry)) {
/* 281 */         Map.Entry e = (Map.Entry)o;
/* 282 */         Object otherKey = e.getKey();
/* 283 */         Object otherValue = e.getValue();
/* 284 */         return (this.key == null ? otherKey == null : this.key.equals(otherKey)) && (this.value == null ? otherValue == null : this.value.equals(otherValue));
/*     */       }
/*     */ 
/* 287 */       return false;
/*     */     }
/*     */     public int hashCode() {
/* 290 */       return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 294 */       StringBuffer buffer = new StringBuffer();
/* 295 */       buffer.append(String.valueOf(this.key));
/* 296 */       buffer.append('=');
/* 297 */       buffer.append(String.valueOf(this.value));
/* 298 */       return buffer.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl
 * JD-Core Version:    0.6.2
 */