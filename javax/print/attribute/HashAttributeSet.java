/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class HashAttributeSet
/*     */   implements AttributeSet, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5311560590283707917L;
/*     */   private Class myInterface;
/*  57 */   private transient HashMap attrMap = new HashMap();
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/*  70 */     paramObjectOutputStream.defaultWriteObject();
/*  71 */     Attribute[] arrayOfAttribute = toArray();
/*  72 */     paramObjectOutputStream.writeInt(arrayOfAttribute.length);
/*  73 */     for (int i = 0; i < arrayOfAttribute.length; i++)
/*  74 */       paramObjectOutputStream.writeObject(arrayOfAttribute[i]);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/*  84 */     paramObjectInputStream.defaultReadObject();
/*  85 */     this.attrMap = new HashMap();
/*  86 */     int i = paramObjectInputStream.readInt();
/*     */ 
/*  88 */     for (int j = 0; j < i; j++) {
/*  89 */       Attribute localAttribute = (Attribute)paramObjectInputStream.readObject();
/*  90 */       add(localAttribute);
/*     */     }
/*     */   }
/*     */ 
/*     */   public HashAttributeSet()
/*     */   {
/*  98 */     this(Attribute.class);
/*     */   }
/*     */ 
/*     */   public HashAttributeSet(Attribute paramAttribute)
/*     */   {
/* 111 */     this(paramAttribute, Attribute.class);
/*     */   }
/*     */ 
/*     */   public HashAttributeSet(Attribute[] paramArrayOfAttribute)
/*     */   {
/* 131 */     this(paramArrayOfAttribute, Attribute.class);
/*     */   }
/*     */ 
/*     */   public HashAttributeSet(AttributeSet paramAttributeSet)
/*     */   {
/* 143 */     this(paramAttributeSet, Attribute.class);
/*     */   }
/*     */ 
/*     */   protected HashAttributeSet(Class<?> paramClass)
/*     */   {
/* 157 */     if (paramClass == null) {
/* 158 */       throw new NullPointerException("null interface");
/*     */     }
/* 160 */     this.myInterface = paramClass;
/*     */   }
/*     */ 
/*     */   protected HashAttributeSet(Attribute paramAttribute, Class<?> paramClass)
/*     */   {
/* 182 */     if (paramClass == null) {
/* 183 */       throw new NullPointerException("null interface");
/*     */     }
/* 185 */     this.myInterface = paramClass;
/* 186 */     add(paramAttribute);
/*     */   }
/*     */ 
/*     */   protected HashAttributeSet(Attribute[] paramArrayOfAttribute, Class<?> paramClass)
/*     */   {
/* 215 */     if (paramClass == null) {
/* 216 */       throw new NullPointerException("null interface");
/*     */     }
/* 218 */     this.myInterface = paramClass;
/* 219 */     int i = paramArrayOfAttribute == null ? 0 : paramArrayOfAttribute.length;
/* 220 */     for (int j = 0; j < i; j++)
/* 221 */       add(paramArrayOfAttribute[j]);
/*     */   }
/*     */ 
/*     */   protected HashAttributeSet(AttributeSet paramAttributeSet, Class<?> paramClass)
/*     */   {
/* 243 */     this.myInterface = paramClass;
/* 244 */     if (paramAttributeSet != null) {
/* 245 */       Attribute[] arrayOfAttribute = paramAttributeSet.toArray();
/* 246 */       int i = arrayOfAttribute == null ? 0 : arrayOfAttribute.length;
/* 247 */       for (int j = 0; j < i; j++)
/* 248 */         add(arrayOfAttribute[j]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Attribute get(Class<?> paramClass)
/*     */   {
/* 277 */     return (Attribute)this.attrMap.get(AttributeSetUtilities.verifyAttributeCategory(paramClass, Attribute.class));
/*     */   }
/*     */ 
/*     */   public boolean add(Attribute paramAttribute)
/*     */   {
/* 301 */     Object localObject = this.attrMap.put(paramAttribute.getCategory(), AttributeSetUtilities.verifyAttributeValue(paramAttribute, this.myInterface));
/*     */ 
/* 305 */     return !paramAttribute.equals(localObject);
/*     */   }
/*     */ 
/*     */   public boolean remove(Class<?> paramClass)
/*     */   {
/* 325 */     return (paramClass != null) && (AttributeSetUtilities.verifyAttributeCategory(paramClass, Attribute.class) != null) && (this.attrMap.remove(paramClass) != null);
/*     */   }
/*     */ 
/*     */   public boolean remove(Attribute paramAttribute)
/*     */   {
/* 348 */     return (paramAttribute != null) && (this.attrMap.remove(paramAttribute.getCategory()) != null);
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Class<?> paramClass)
/*     */   {
/* 364 */     return (paramClass != null) && (AttributeSetUtilities.verifyAttributeCategory(paramClass, Attribute.class) != null) && (this.attrMap.get(paramClass) != null);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Attribute paramAttribute)
/*     */   {
/* 382 */     return (paramAttribute != null) && ((paramAttribute instanceof Attribute)) && (paramAttribute.equals(this.attrMap.get(paramAttribute.getCategory())));
/*     */   }
/*     */ 
/*     */   public boolean addAll(AttributeSet paramAttributeSet)
/*     */   {
/* 420 */     Attribute[] arrayOfAttribute = paramAttributeSet.toArray();
/* 421 */     boolean bool = false;
/* 422 */     for (int i = 0; i < arrayOfAttribute.length; i++) {
/* 423 */       Attribute localAttribute = AttributeSetUtilities.verifyAttributeValue(arrayOfAttribute[i], this.myInterface);
/*     */ 
/* 426 */       Object localObject = this.attrMap.put(localAttribute.getCategory(), localAttribute);
/* 427 */       bool = (!localAttribute.equals(localObject)) || (bool);
/*     */     }
/* 429 */     return bool;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 440 */     return this.attrMap.size();
/*     */   }
/*     */ 
/*     */   public Attribute[] toArray()
/*     */   {
/* 449 */     Attribute[] arrayOfAttribute = new Attribute[size()];
/* 450 */     this.attrMap.values().toArray(arrayOfAttribute);
/* 451 */     return arrayOfAttribute;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 463 */     this.attrMap.clear();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 472 */     return this.attrMap.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 490 */     if ((paramObject == null) || (!(paramObject instanceof Serializable))) {
/* 491 */       return false;
/*     */     }
/*     */ 
/* 494 */     AttributeSet localAttributeSet = (Serializable)paramObject;
/* 495 */     if (localAttributeSet.size() != size()) {
/* 496 */       return false;
/*     */     }
/*     */ 
/* 499 */     Attribute[] arrayOfAttribute = toArray();
/* 500 */     for (int i = 0; i < arrayOfAttribute.length; i++) {
/* 501 */       if (!localAttributeSet.containsValue(arrayOfAttribute[i])) {
/* 502 */         return false;
/*     */       }
/*     */     }
/* 505 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 520 */     int i = 0;
/* 521 */     Attribute[] arrayOfAttribute = toArray();
/* 522 */     for (int j = 0; j < arrayOfAttribute.length; j++) {
/* 523 */       i += arrayOfAttribute[j].hashCode();
/*     */     }
/* 525 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.HashAttributeSet
 * JD-Core Version:    0.6.2
 */