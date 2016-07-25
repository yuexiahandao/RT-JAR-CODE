/*     */ package com.sun.org.apache.xerces.internal.impl.xs.identity;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSIDCDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ 
/*     */ public abstract class IdentityConstraint
/*     */   implements XSIDCDefinition
/*     */ {
/*     */   protected short type;
/*     */   protected String fNamespace;
/*     */   protected String fIdentityConstraintName;
/*     */   protected String fElementName;
/*     */   protected Selector fSelector;
/*     */   protected int fFieldCount;
/*     */   protected Field[] fFields;
/*  67 */   protected XSAnnotationImpl[] fAnnotations = null;
/*     */   protected int fNumAnnotations;
/*     */ 
/*     */   protected IdentityConstraint(String namespace, String identityConstraintName, String elemName)
/*     */   {
/*  78 */     this.fNamespace = namespace;
/*  79 */     this.fIdentityConstraintName = identityConstraintName;
/*  80 */     this.fElementName = elemName;
/*     */   }
/*     */ 
/*     */   public String getIdentityConstraintName()
/*     */   {
/*  89 */     return this.fIdentityConstraintName;
/*     */   }
/*     */ 
/*     */   public void setSelector(Selector selector)
/*     */   {
/*  94 */     this.fSelector = selector;
/*     */   }
/*     */ 
/*     */   public Selector getSelector()
/*     */   {
/*  99 */     return this.fSelector;
/*     */   }
/*     */ 
/*     */   public void addField(Field field)
/*     */   {
/* 104 */     if (this.fFields == null)
/* 105 */       this.fFields = new Field[4];
/* 106 */     else if (this.fFieldCount == this.fFields.length)
/* 107 */       this.fFields = resize(this.fFields, this.fFieldCount * 2);
/* 108 */     this.fFields[(this.fFieldCount++)] = field;
/*     */   }
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 113 */     return this.fFieldCount;
/*     */   }
/*     */ 
/*     */   public Field getFieldAt(int index)
/*     */   {
/* 118 */     return this.fFields[index];
/*     */   }
/*     */ 
/*     */   public String getElementName()
/*     */   {
/* 123 */     return this.fElementName;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 132 */     String s = super.toString();
/* 133 */     int index1 = s.lastIndexOf('$');
/* 134 */     if (index1 != -1) {
/* 135 */       return s.substring(index1 + 1);
/*     */     }
/* 137 */     int index2 = s.lastIndexOf('.');
/* 138 */     if (index2 != -1) {
/* 139 */       return s.substring(index2 + 1);
/*     */     }
/* 141 */     return s;
/*     */   }
/*     */ 
/*     */   public boolean equals(IdentityConstraint id)
/*     */   {
/* 148 */     boolean areEqual = this.fIdentityConstraintName.equals(id.fIdentityConstraintName);
/* 149 */     if (!areEqual) return false;
/* 150 */     areEqual = this.fSelector.toString().equals(id.fSelector.toString());
/* 151 */     if (!areEqual) return false;
/* 152 */     areEqual = this.fFieldCount == id.fFieldCount;
/* 153 */     if (!areEqual) return false;
/* 154 */     for (int i = 0; i < this.fFieldCount; i++)
/* 155 */       if (!this.fFields[i].toString().equals(id.fFields[i].toString())) return false;
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   static final Field[] resize(Field[] oldArray, int newSize) {
/* 160 */     Field[] newArray = new Field[newSize];
/* 161 */     System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
/* 162 */     return newArray;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/* 169 */     return 10;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 177 */     return this.fIdentityConstraintName;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 186 */     return this.fNamespace;
/*     */   }
/*     */ 
/*     */   public short getCategory()
/*     */   {
/* 193 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getSelectorStr()
/*     */   {
/* 200 */     return this.fSelector != null ? this.fSelector.toString() : null;
/*     */   }
/*     */ 
/*     */   public StringList getFieldStrs()
/*     */   {
/* 207 */     String[] strs = new String[this.fFieldCount];
/* 208 */     for (int i = 0; i < this.fFieldCount; i++)
/* 209 */       strs[i] = this.fFields[i].toString();
/* 210 */     return new StringListImpl(strs, this.fFieldCount);
/*     */   }
/*     */ 
/*     */   public XSIDCDefinition getRefKey()
/*     */   {
/* 219 */     return null;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 226 */     return new XSObjectListImpl(this.fAnnotations, this.fNumAnnotations);
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 234 */     return null;
/*     */   }
/*     */ 
/*     */   public void addAnnotation(XSAnnotationImpl annotation) {
/* 238 */     if (annotation == null)
/* 239 */       return;
/* 240 */     if (this.fAnnotations == null) {
/* 241 */       this.fAnnotations = new XSAnnotationImpl[2];
/* 242 */     } else if (this.fNumAnnotations == this.fAnnotations.length) {
/* 243 */       XSAnnotationImpl[] newArray = new XSAnnotationImpl[this.fNumAnnotations << 1];
/* 244 */       System.arraycopy(this.fAnnotations, 0, newArray, 0, this.fNumAnnotations);
/* 245 */       this.fAnnotations = newArray;
/*     */     }
/* 247 */     this.fAnnotations[(this.fNumAnnotations++)] = annotation;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint
 * JD-Core Version:    0.6.2
 */