/*     */ package java.util;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public abstract class EnumSet<E extends Enum<E>> extends AbstractSet<E>
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   final Class<E> elementType;
/*     */   final Enum[] universe;
/*  93 */   private static Enum[] ZERO_LENGTH_ENUM_ARRAY = new Enum[0];
/*     */ 
/*     */   EnumSet(Class<E> paramClass, Enum[] paramArrayOfEnum) {
/*  96 */     this.elementType = paramClass;
/*  97 */     this.universe = paramArrayOfEnum;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> paramClass)
/*     */   {
/* 108 */     Enum[] arrayOfEnum = getUniverse(paramClass);
/* 109 */     if (arrayOfEnum == null) {
/* 110 */       throw new ClassCastException(paramClass + " not an enum");
/*     */     }
/* 112 */     if (arrayOfEnum.length <= 64) {
/* 113 */       return new RegularEnumSet(paramClass, arrayOfEnum);
/*     */     }
/* 115 */     return new JumboEnumSet(paramClass, arrayOfEnum);
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> allOf(Class<E> paramClass)
/*     */   {
/* 127 */     EnumSet localEnumSet = noneOf(paramClass);
/* 128 */     localEnumSet.addAll();
/* 129 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   abstract void addAll();
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> copyOf(EnumSet<E> paramEnumSet)
/*     */   {
/* 146 */     return paramEnumSet.clone();
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> copyOf(Collection<E> paramCollection)
/*     */   {
/* 162 */     if ((paramCollection instanceof EnumSet)) {
/* 163 */       return ((EnumSet)paramCollection).clone();
/*     */     }
/* 165 */     if (paramCollection.isEmpty())
/* 166 */       throw new IllegalArgumentException("Collection is empty");
/* 167 */     Iterator localIterator = paramCollection.iterator();
/* 168 */     Enum localEnum = (Enum)localIterator.next();
/* 169 */     EnumSet localEnumSet = of(localEnum);
/* 170 */     while (localIterator.hasNext())
/* 171 */       localEnumSet.add(localIterator.next());
/* 172 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> complementOf(EnumSet<E> paramEnumSet)
/*     */   {
/* 185 */     EnumSet localEnumSet = copyOf(paramEnumSet);
/* 186 */     localEnumSet.complement();
/* 187 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> of(E paramE)
/*     */   {
/* 204 */     EnumSet localEnumSet = noneOf(paramE.getDeclaringClass());
/* 205 */     localEnumSet.add(paramE);
/* 206 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> of(E paramE1, E paramE2)
/*     */   {
/* 224 */     EnumSet localEnumSet = noneOf(paramE1.getDeclaringClass());
/* 225 */     localEnumSet.add(paramE1);
/* 226 */     localEnumSet.add(paramE2);
/* 227 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> of(E paramE1, E paramE2, E paramE3)
/*     */   {
/* 246 */     EnumSet localEnumSet = noneOf(paramE1.getDeclaringClass());
/* 247 */     localEnumSet.add(paramE1);
/* 248 */     localEnumSet.add(paramE2);
/* 249 */     localEnumSet.add(paramE3);
/* 250 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> of(E paramE1, E paramE2, E paramE3, E paramE4)
/*     */   {
/* 270 */     EnumSet localEnumSet = noneOf(paramE1.getDeclaringClass());
/* 271 */     localEnumSet.add(paramE1);
/* 272 */     localEnumSet.add(paramE2);
/* 273 */     localEnumSet.add(paramE3);
/* 274 */     localEnumSet.add(paramE4);
/* 275 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5)
/*     */   {
/* 298 */     EnumSet localEnumSet = noneOf(paramE1.getDeclaringClass());
/* 299 */     localEnumSet.add(paramE1);
/* 300 */     localEnumSet.add(paramE2);
/* 301 */     localEnumSet.add(paramE3);
/* 302 */     localEnumSet.add(paramE4);
/* 303 */     localEnumSet.add(paramE5);
/* 304 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   @SafeVarargs
/*     */   public static <E extends Enum<E>> EnumSet<E> of(E paramE, E[] paramArrayOfE)
/*     */   {
/* 322 */     EnumSet localEnumSet = noneOf(paramE.getDeclaringClass());
/* 323 */     localEnumSet.add(paramE);
/* 324 */     for (E ? : paramArrayOfE)
/* 325 */       localEnumSet.add(?);
/* 326 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   public static <E extends Enum<E>> EnumSet<E> range(E paramE1, E paramE2)
/*     */   {
/* 343 */     if (paramE1.compareTo(paramE2) > 0)
/* 344 */       throw new IllegalArgumentException(paramE1 + " > " + paramE2);
/* 345 */     EnumSet localEnumSet = noneOf(paramE1.getDeclaringClass());
/* 346 */     localEnumSet.addRange(paramE1, paramE2);
/* 347 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   abstract void addRange(E paramE1, E paramE2);
/*     */ 
/*     */   public EnumSet<E> clone()
/*     */   {
/*     */     try
/*     */     {
/* 363 */       return (EnumSet)super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 365 */       throw new AssertionError(localCloneNotSupportedException);
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract void complement();
/*     */ 
/*     */   final void typeCheck(E paramE)
/*     */   {
/* 378 */     Class localClass = paramE.getClass();
/* 379 */     if ((localClass != this.elementType) && (localClass.getSuperclass() != this.elementType))
/* 380 */       throw new ClassCastException(localClass + " != " + this.elementType);
/*     */   }
/*     */ 
/*     */   private static <E extends Enum<E>> E[] getUniverse(Class<E> paramClass)
/*     */   {
/* 388 */     return SharedSecrets.getJavaLangAccess().getEnumConstantsShared(paramClass);
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 434 */     return new SerializationProxy(this);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws InvalidObjectException
/*     */   {
/* 441 */     throw new InvalidObjectException("Proxy required");
/*     */   }
/*     */ 
/*     */   private static class SerializationProxy<E extends Enum<E>>
/*     */     implements Serializable
/*     */   {
/*     */     private final Class<E> elementType;
/*     */     private final Enum[] elements;
/*     */     private static final long serialVersionUID = 362491234563181265L;
/*     */ 
/*     */     SerializationProxy(EnumSet<E> paramEnumSet)
/*     */     {
/* 419 */       this.elementType = paramEnumSet.elementType;
/* 420 */       this.elements = ((Enum[])paramEnumSet.toArray(EnumSet.ZERO_LENGTH_ENUM_ARRAY));
/*     */     }
/*     */ 
/*     */     private Object readResolve() {
/* 424 */       EnumSet localEnumSet = EnumSet.noneOf(this.elementType);
/* 425 */       for (Enum localEnum : this.elements)
/* 426 */         localEnumSet.add(localEnum);
/* 427 */       return localEnumSet;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.EnumSet
 * JD-Core Version:    0.6.2
 */