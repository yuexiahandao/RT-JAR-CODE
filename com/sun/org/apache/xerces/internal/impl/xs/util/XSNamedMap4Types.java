/*     */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObject;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ 
/*     */ public final class XSNamedMap4Types extends XSNamedMapImpl
/*     */ {
/*     */   private final short fType;
/*     */ 
/*     */   public XSNamedMap4Types(String namespace, SymbolHash map, short type)
/*     */   {
/*  50 */     super(namespace, map);
/*  51 */     this.fType = type;
/*     */   }
/*     */ 
/*     */   public XSNamedMap4Types(String[] namespaces, SymbolHash[] maps, int num, short type)
/*     */   {
/*  63 */     super(namespaces, maps, num);
/*  64 */     this.fType = type;
/*     */   }
/*     */ 
/*     */   public synchronized int getLength()
/*     */   {
/*  73 */     if (this.fLength == -1)
/*     */     {
/*  75 */       int length = 0;
/*  76 */       for (int i = 0; i < this.fNSNum; i++) {
/*  77 */         length += this.fMaps[i].getLength();
/*     */       }
/*     */ 
/*  80 */       int pos = 0;
/*  81 */       XSObject[] array = new XSObject[length];
/*  82 */       for (int i = 0; i < this.fNSNum; i++) {
/*  83 */         pos += this.fMaps[i].getValues(array, pos);
/*     */       }
/*     */ 
/*  87 */       this.fLength = 0;
/*  88 */       this.fArray = new XSObject[length];
/*     */ 
/*  90 */       for (int i = 0; i < length; i++) {
/*  91 */         XSTypeDefinition type = (XSTypeDefinition)array[i];
/*  92 */         if (type.getTypeCategory() == this.fType) {
/*  93 */           this.fArray[(this.fLength++)] = type;
/*     */         }
/*     */       }
/*     */     }
/*  97 */     return this.fLength;
/*     */   }
/*     */ 
/*     */   public XSObject itemByName(String namespace, String localName)
/*     */   {
/* 111 */     for (int i = 0; i < this.fNSNum; i++) {
/* 112 */       if (isEqual(namespace, this.fNamespaces[i])) {
/* 113 */         XSTypeDefinition type = (XSTypeDefinition)this.fMaps[i].get(localName);
/*     */ 
/* 115 */         if ((type != null) && (type.getTypeCategory() == this.fType)) {
/* 116 */           return type;
/*     */         }
/* 118 */         return null;
/*     */       }
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized XSObject item(int index)
/*     */   {
/* 135 */     if (this.fArray == null) {
/* 136 */       getLength();
/*     */     }
/* 138 */     if ((index < 0) || (index >= this.fLength)) {
/* 139 */       return null;
/*     */     }
/* 141 */     return this.fArray[index];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMap4Types
 * JD-Core Version:    0.6.2
 */