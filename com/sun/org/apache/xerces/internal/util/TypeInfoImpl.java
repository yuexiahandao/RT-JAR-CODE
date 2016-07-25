/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import java.util.Hashtable;
/*     */ import org.w3c.dom.TypeInfo;
/*     */ 
/*     */ public class TypeInfoImpl
/*     */   implements TypeInfo
/*     */ {
/*     */   private final String typeNamespace;
/*     */   private final String typeName;
/*     */   private static final String dtdNamespaceURI = "http://www.w3.org/TR/REC-xml";
/* 113 */   private static final Hashtable dtdCache = new Hashtable();
/*     */ 
/*     */   public TypeInfoImpl()
/*     */   {
/*  83 */     this.typeNamespace = null;
/*  84 */     this.typeName = null;
/*     */   }
/*     */   public TypeInfoImpl(String typeNamespace, String typeName) {
/*  87 */     this.typeNamespace = typeNamespace;
/*  88 */     this.typeName = typeName;
/*     */   }
/*     */ 
/*     */   public TypeInfoImpl(XSTypeDefinition t) {
/*  92 */     this(t.getNamespace(), t.getName());
/*     */   }
/*     */ 
/*     */   public String getTypeName() {
/*  96 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public String getTypeNamespace() {
/* 100 */     return this.typeNamespace;
/*     */   }
/*     */ 
/*     */   public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod)
/*     */   {
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   public static TypeInfo getDTDTypeInfo(String name)
/*     */   {
/* 122 */     TypeInfo t = (TypeInfo)dtdCache.get(name);
/* 123 */     if (t == null) throw new IllegalArgumentException("Unknown DTD datatype " + name);
/* 124 */     return t;
/*     */   }
/*     */ 
/*     */   static {
/* 128 */     String[] typeNames = { "CDATA", "ID", "IDREF", "IDREFS", "NMTOKEN", "NMTOKENS", "ENTITY", "ENTITIES", "NOTATION" };
/*     */ 
/* 131 */     for (int i = 0; i < typeNames.length; i++)
/* 132 */       dtdCache.put(typeNames[i], new TypeInfoImpl("http://www.w3.org/TR/REC-xml", typeNames[i]));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.TypeInfoImpl
 * JD-Core Version:    0.6.2
 */