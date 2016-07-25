/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.MutableAttributeSet;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ 
/*     */ class MuxingAttributeSet
/*     */   implements AttributeSet, Serializable
/*     */ {
/*     */   private AttributeSet[] attrs;
/*     */ 
/*     */   public MuxingAttributeSet(AttributeSet[] paramArrayOfAttributeSet)
/*     */   {
/*  42 */     this.attrs = paramArrayOfAttributeSet;
/*     */   }
/*     */ 
/*     */   protected MuxingAttributeSet()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected synchronized void setAttributes(AttributeSet[] paramArrayOfAttributeSet)
/*     */   {
/*  59 */     this.attrs = paramArrayOfAttributeSet;
/*     */   }
/*     */ 
/*     */   protected synchronized AttributeSet[] getAttributes()
/*     */   {
/*  67 */     return this.attrs;
/*     */   }
/*     */ 
/*     */   protected synchronized void insertAttributeSetAt(AttributeSet paramAttributeSet, int paramInt)
/*     */   {
/*  77 */     int i = this.attrs.length;
/*  78 */     AttributeSet[] arrayOfAttributeSet = new Serializable[i + 1];
/*  79 */     if (paramInt < i) {
/*  80 */       if (paramInt > 0) {
/*  81 */         System.arraycopy(this.attrs, 0, arrayOfAttributeSet, 0, paramInt);
/*  82 */         System.arraycopy(this.attrs, paramInt, arrayOfAttributeSet, paramInt + 1, i - paramInt);
/*     */       }
/*     */       else
/*     */       {
/*  86 */         System.arraycopy(this.attrs, 0, arrayOfAttributeSet, 1, i);
/*     */       }
/*     */     }
/*     */     else {
/*  90 */       System.arraycopy(this.attrs, 0, arrayOfAttributeSet, 0, i);
/*     */     }
/*  92 */     arrayOfAttributeSet[paramInt] = paramAttributeSet;
/*  93 */     this.attrs = arrayOfAttributeSet;
/*     */   }
/*     */ 
/*     */   protected synchronized void removeAttributeSetAt(int paramInt)
/*     */   {
/* 102 */     int i = this.attrs.length;
/* 103 */     AttributeSet[] arrayOfAttributeSet = new Serializable[i - 1];
/* 104 */     if (i > 0) {
/* 105 */       if (paramInt == 0)
/*     */       {
/* 107 */         System.arraycopy(this.attrs, 1, arrayOfAttributeSet, 0, i - 1);
/*     */       }
/* 109 */       else if (paramInt < i - 1)
/*     */       {
/* 111 */         System.arraycopy(this.attrs, 0, arrayOfAttributeSet, 0, paramInt);
/* 112 */         System.arraycopy(this.attrs, paramInt + 1, arrayOfAttributeSet, paramInt, i - paramInt - 1);
/*     */       }
/*     */       else
/*     */       {
/* 117 */         System.arraycopy(this.attrs, 0, arrayOfAttributeSet, 0, i - 1);
/*     */       }
/*     */     }
/* 120 */     this.attrs = arrayOfAttributeSet;
/*     */   }
/*     */ 
/*     */   public int getAttributeCount()
/*     */   {
/* 132 */     AttributeSet[] arrayOfAttributeSet = getAttributes();
/* 133 */     int i = 0;
/* 134 */     for (int j = 0; j < arrayOfAttributeSet.length; j++) {
/* 135 */       i += arrayOfAttributeSet[j].getAttributeCount();
/*     */     }
/* 137 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean isDefined(Object paramObject)
/*     */   {
/* 151 */     AttributeSet[] arrayOfAttributeSet = getAttributes();
/* 152 */     for (int i = 0; i < arrayOfAttributeSet.length; i++) {
/* 153 */       if (arrayOfAttributeSet[i].isDefined(paramObject)) {
/* 154 */         return true;
/*     */       }
/*     */     }
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isEqual(AttributeSet paramAttributeSet)
/*     */   {
/* 168 */     return (getAttributeCount() == paramAttributeSet.getAttributeCount()) && (containsAttributes(paramAttributeSet));
/*     */   }
/*     */ 
/*     */   public AttributeSet copyAttributes()
/*     */   {
/* 179 */     AttributeSet[] arrayOfAttributeSet = getAttributes();
/* 180 */     SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 181 */     int i = 0;
/* 182 */     for (int j = arrayOfAttributeSet.length - 1; j >= 0; j--) {
/* 183 */       localSimpleAttributeSet.addAttributes(arrayOfAttributeSet[j]);
/*     */     }
/* 185 */     return localSimpleAttributeSet;
/*     */   }
/*     */ 
/*     */   public Object getAttribute(Object paramObject)
/*     */   {
/* 198 */     AttributeSet[] arrayOfAttributeSet = getAttributes();
/* 199 */     int i = arrayOfAttributeSet.length;
/* 200 */     for (int j = 0; j < i; j++) {
/* 201 */       Object localObject = arrayOfAttributeSet[j].getAttribute(paramObject);
/* 202 */       if (localObject != null) {
/* 203 */         return localObject;
/*     */       }
/*     */     }
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */   public Enumeration getAttributeNames()
/*     */   {
/* 216 */     return new MuxingAttributeNameEnumeration();
/*     */   }
/*     */ 
/*     */   public boolean containsAttribute(Object paramObject1, Object paramObject2)
/*     */   {
/* 228 */     return paramObject2.equals(getAttribute(paramObject1));
/*     */   }
/*     */ 
/*     */   public boolean containsAttributes(AttributeSet paramAttributeSet)
/*     */   {
/* 240 */     boolean bool = true;
/*     */ 
/* 242 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 243 */     while ((bool) && (localEnumeration.hasMoreElements())) {
/* 244 */       Object localObject = localEnumeration.nextElement();
/* 245 */       bool = paramAttributeSet.getAttribute(localObject).equals(getAttribute(localObject));
/*     */     }
/*     */ 
/* 248 */     return bool;
/*     */   }
/*     */ 
/*     */   public AttributeSet getResolveParent()
/*     */   {
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   private class MuxingAttributeNameEnumeration
/*     */     implements Enumeration
/*     */   {
/*     */     private int attrIndex;
/*     */     private Enumeration currentEnum;
/*     */ 
/*     */     MuxingAttributeNameEnumeration()
/*     */     {
/* 273 */       updateEnum();
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements() {
/* 277 */       if (this.currentEnum == null) {
/* 278 */         return false;
/*     */       }
/* 280 */       return this.currentEnum.hasMoreElements();
/*     */     }
/*     */ 
/*     */     public Object nextElement() {
/* 284 */       if (this.currentEnum == null) {
/* 285 */         throw new NoSuchElementException("No more names");
/*     */       }
/* 287 */       Object localObject = this.currentEnum.nextElement();
/* 288 */       if (!this.currentEnum.hasMoreElements()) {
/* 289 */         updateEnum();
/*     */       }
/* 291 */       return localObject;
/*     */     }
/*     */ 
/*     */     void updateEnum() {
/* 295 */       AttributeSet[] arrayOfAttributeSet = MuxingAttributeSet.this.getAttributes();
/* 296 */       this.currentEnum = null;
/* 297 */       while ((this.currentEnum == null) && (this.attrIndex < arrayOfAttributeSet.length)) {
/* 298 */         this.currentEnum = arrayOfAttributeSet[(this.attrIndex++)].getAttributeNames();
/* 299 */         if (!this.currentEnum.hasMoreElements())
/* 300 */           this.currentEnum = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.MuxingAttributeSet
 * JD-Core Version:    0.6.2
 */