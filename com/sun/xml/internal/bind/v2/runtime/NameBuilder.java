/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class NameBuilder
/*     */ {
/*  49 */   private Map<String, Integer> uriIndexMap = new HashMap();
/*  50 */   private Set<String> nonDefaultableNsUris = new HashSet();
/*  51 */   private Map<String, Integer> localNameIndexMap = new HashMap();
/*  52 */   private QNameMap<Integer> elementQNameIndexMap = new QNameMap();
/*  53 */   private QNameMap<Integer> attributeQNameIndexMap = new QNameMap();
/*     */ 
/*     */   public Name createElementName(QName name) {
/*  56 */     return createElementName(name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */ 
/*     */   public Name createElementName(String nsUri, String localName) {
/*  60 */     return createName(nsUri, localName, false, this.elementQNameIndexMap);
/*     */   }
/*     */ 
/*     */   public Name createAttributeName(QName name) {
/*  64 */     return createAttributeName(name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */ 
/*     */   public Name createAttributeName(String nsUri, String localName) {
/*  68 */     assert (nsUri.intern() == nsUri);
/*  69 */     assert (localName.intern() == localName);
/*     */ 
/*  71 */     if (nsUri.length() == 0) {
/*  72 */       return new Name(allocIndex(this.attributeQNameIndexMap, "", localName), -1, nsUri, allocIndex(this.localNameIndexMap, localName), localName, true);
/*     */     }
/*     */ 
/*  80 */     this.nonDefaultableNsUris.add(nsUri);
/*  81 */     return createName(nsUri, localName, true, this.attributeQNameIndexMap);
/*     */   }
/*     */ 
/*     */   private Name createName(String nsUri, String localName, boolean isAttribute, QNameMap<Integer> map)
/*     */   {
/*  86 */     assert (nsUri.intern() == nsUri);
/*  87 */     assert (localName.intern() == localName);
/*     */ 
/*  89 */     return new Name(allocIndex(map, nsUri, localName), allocIndex(this.uriIndexMap, nsUri), nsUri, allocIndex(this.localNameIndexMap, localName), localName, isAttribute);
/*     */   }
/*     */ 
/*     */   private int allocIndex(Map<String, Integer> map, String str)
/*     */   {
/*  99 */     Integer i = (Integer)map.get(str);
/* 100 */     if (i == null) {
/* 101 */       i = Integer.valueOf(map.size());
/* 102 */       map.put(str, i);
/*     */     }
/* 104 */     return i.intValue();
/*     */   }
/*     */ 
/*     */   private int allocIndex(QNameMap<Integer> map, String nsUri, String localName) {
/* 108 */     Integer i = (Integer)map.get(nsUri, localName);
/* 109 */     if (i == null) {
/* 110 */       i = Integer.valueOf(map.size());
/* 111 */       map.put(nsUri, localName, i);
/*     */     }
/* 113 */     return i.intValue();
/*     */   }
/*     */ 
/*     */   public NameList conclude()
/*     */   {
/* 120 */     boolean[] nsUriCannotBeDefaulted = new boolean[this.uriIndexMap.size()];
/* 121 */     for (Map.Entry e : this.uriIndexMap.entrySet()) {
/* 122 */       nsUriCannotBeDefaulted[((Integer)e.getValue()).intValue()] = this.nonDefaultableNsUris.contains(e.getKey());
/*     */     }
/*     */ 
/* 125 */     NameList r = new NameList(list(this.uriIndexMap), nsUriCannotBeDefaulted, list(this.localNameIndexMap), this.elementQNameIndexMap.size(), this.attributeQNameIndexMap.size());
/*     */ 
/* 132 */     this.uriIndexMap = null;
/* 133 */     this.localNameIndexMap = null;
/* 134 */     return r;
/*     */   }
/*     */ 
/*     */   private String[] list(Map<String, Integer> map) {
/* 138 */     String[] r = new String[map.size()];
/* 139 */     for (Map.Entry e : map.entrySet())
/* 140 */       r[((Integer)e.getValue()).intValue()] = ((String)e.getKey());
/* 141 */     return r;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.NameBuilder
 * JD-Core Version:    0.6.2
 */