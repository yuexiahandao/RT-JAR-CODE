/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class SimpleAttributeSet
/*     */   implements MutableAttributeSet, Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -6631553454711782652L;
/*  57 */   public static final AttributeSet EMPTY = new EmptyAttributeSet();
/*     */ 
/*  59 */   private transient Hashtable<Object, Object> table = new Hashtable(3);
/*     */ 
/*     */   public SimpleAttributeSet()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SimpleAttributeSet(AttributeSet paramAttributeSet)
/*     */   {
/*  73 */     addAttributes(paramAttributeSet);
/*     */   }
/*     */ 
/*     */   private SimpleAttributeSet(Hashtable<Object, Object> paramHashtable) {
/*  77 */     this.table = paramHashtable;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  87 */     return this.table.isEmpty();
/*     */   }
/*     */ 
/*     */   public int getAttributeCount()
/*     */   {
/*  96 */     return this.table.size();
/*     */   }
/*     */ 
/*     */   public boolean isDefined(Object paramObject)
/*     */   {
/* 106 */     return this.table.containsKey(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean isEqual(AttributeSet paramAttributeSet)
/*     */   {
/* 116 */     return (getAttributeCount() == paramAttributeSet.getAttributeCount()) && (containsAttributes(paramAttributeSet));
/*     */   }
/*     */ 
/*     */   public AttributeSet copyAttributes()
/*     */   {
/* 126 */     return (AttributeSet)clone();
/*     */   }
/*     */ 
/*     */   public Enumeration<?> getAttributeNames()
/*     */   {
/* 135 */     return this.table.keys();
/*     */   }
/*     */ 
/*     */   public Object getAttribute(Object paramObject)
/*     */   {
/* 145 */     Object localObject = this.table.get(paramObject);
/* 146 */     if (localObject == null) {
/* 147 */       AttributeSet localAttributeSet = getResolveParent();
/* 148 */       if (localAttributeSet != null) {
/* 149 */         localObject = localAttributeSet.getAttribute(paramObject);
/*     */       }
/*     */     }
/* 152 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean containsAttribute(Object paramObject1, Object paramObject2)
/*     */   {
/* 164 */     return paramObject2.equals(getAttribute(paramObject1));
/*     */   }
/*     */ 
/*     */   public boolean containsAttributes(AttributeSet paramAttributeSet)
/*     */   {
/* 175 */     boolean bool = true;
/*     */ 
/* 177 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 178 */     while ((bool) && (localEnumeration.hasMoreElements())) {
/* 179 */       Object localObject = localEnumeration.nextElement();
/* 180 */       bool = paramAttributeSet.getAttribute(localObject).equals(getAttribute(localObject));
/*     */     }
/*     */ 
/* 183 */     return bool;
/*     */   }
/*     */ 
/*     */   public void addAttribute(Object paramObject1, Object paramObject2)
/*     */   {
/* 193 */     this.table.put(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public void addAttributes(AttributeSet paramAttributeSet)
/*     */   {
/* 202 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 203 */     while (localEnumeration.hasMoreElements()) {
/* 204 */       Object localObject = localEnumeration.nextElement();
/* 205 */       addAttribute(localObject, paramAttributeSet.getAttribute(localObject));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeAttribute(Object paramObject)
/*     */   {
/* 215 */     this.table.remove(paramObject);
/*     */   }
/*     */ 
/*     */   public void removeAttributes(Enumeration<?> paramEnumeration)
/*     */   {
/* 224 */     while (paramEnumeration.hasMoreElements())
/* 225 */       removeAttribute(paramEnumeration.nextElement());
/*     */   }
/*     */ 
/*     */   public void removeAttributes(AttributeSet paramAttributeSet)
/*     */   {
/* 234 */     if (paramAttributeSet == this) {
/* 235 */       this.table.clear();
/*     */     }
/*     */     else {
/* 238 */       Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 239 */       while (localEnumeration.hasMoreElements()) {
/* 240 */         Object localObject1 = localEnumeration.nextElement();
/* 241 */         Object localObject2 = paramAttributeSet.getAttribute(localObject1);
/* 242 */         if (localObject2.equals(getAttribute(localObject1)))
/* 243 */           removeAttribute(localObject1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public AttributeSet getResolveParent()
/*     */   {
/* 258 */     return (AttributeSet)this.table.get(StyleConstants.ResolveAttribute);
/*     */   }
/*     */ 
/*     */   public void setResolveParent(AttributeSet paramAttributeSet)
/*     */   {
/* 267 */     addAttribute(StyleConstants.ResolveAttribute, paramAttributeSet);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     SimpleAttributeSet localSimpleAttributeSet;
/*     */     try
/*     */     {
/* 280 */       localSimpleAttributeSet = (SimpleAttributeSet)super.clone();
/* 281 */       localSimpleAttributeSet.table = ((Hashtable)this.table.clone());
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 283 */       localSimpleAttributeSet = null;
/*     */     }
/* 285 */     return localSimpleAttributeSet;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 293 */     return this.table.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 305 */     if (this == paramObject) {
/* 306 */       return true;
/*     */     }
/* 308 */     if ((paramObject instanceof AttributeSet)) {
/* 309 */       AttributeSet localAttributeSet = (AttributeSet)paramObject;
/* 310 */       return isEqual(localAttributeSet);
/*     */     }
/* 312 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 321 */     String str = "";
/* 322 */     Enumeration localEnumeration = getAttributeNames();
/* 323 */     while (localEnumeration.hasMoreElements()) {
/* 324 */       Object localObject1 = localEnumeration.nextElement();
/* 325 */       Object localObject2 = getAttribute(localObject1);
/* 326 */       if ((localObject2 instanceof AttributeSet))
/*     */       {
/* 328 */         str = str + localObject1 + "=**AttributeSet** ";
/*     */       }
/* 330 */       else str = str + localObject1 + "=" + localObject2 + " ";
/*     */     }
/*     */ 
/* 333 */     return str;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 337 */     paramObjectOutputStream.defaultWriteObject();
/* 338 */     StyleContext.writeAttributeSet(paramObjectOutputStream, this);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws ClassNotFoundException, IOException
/*     */   {
/* 343 */     paramObjectInputStream.defaultReadObject();
/* 344 */     this.table = new Hashtable(3);
/* 345 */     StyleContext.readAttributeSet(paramObjectInputStream, this);
/*     */   }
/*     */ 
/*     */   static class EmptyAttributeSet
/*     */     implements AttributeSet, Serializable
/*     */   {
/*     */     static final long serialVersionUID = -8714803568785904228L;
/*     */ 
/*     */     public int getAttributeCount()
/*     */     {
/* 355 */       return 0;
/*     */     }
/*     */     public boolean isDefined(Object paramObject) {
/* 358 */       return false;
/*     */     }
/*     */     public boolean isEqual(AttributeSet paramAttributeSet) {
/* 361 */       return paramAttributeSet.getAttributeCount() == 0;
/*     */     }
/*     */     public AttributeSet copyAttributes() {
/* 364 */       return this;
/*     */     }
/*     */     public Object getAttribute(Object paramObject) {
/* 367 */       return null;
/*     */     }
/*     */     public Enumeration getAttributeNames() {
/* 370 */       return Collections.emptyEnumeration();
/*     */     }
/*     */     public boolean containsAttribute(Object paramObject1, Object paramObject2) {
/* 373 */       return false;
/*     */     }
/*     */     public boolean containsAttributes(AttributeSet paramAttributeSet) {
/* 376 */       return paramAttributeSet.getAttributeCount() == 0;
/*     */     }
/*     */     public AttributeSet getResolveParent() {
/* 379 */       return null;
/*     */     }
/*     */     public boolean equals(Object paramObject) {
/* 382 */       if (this == paramObject) {
/* 383 */         return true;
/*     */       }
/* 385 */       return ((paramObject instanceof Serializable)) && (((Serializable)paramObject).getAttributeCount() == 0);
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 389 */       return 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.SimpleAttributeSet
 * JD-Core Version:    0.6.2
 */