/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public final class AttributeSetUtilities
/*     */ {
/*     */   public static AttributeSet unmodifiableView(AttributeSet paramAttributeSet)
/*     */   {
/* 210 */     if (paramAttributeSet == null) {
/* 211 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 214 */     return new UnmodifiableAttributeSet(paramAttributeSet);
/*     */   }
/*     */ 
/*     */   public static DocAttributeSet unmodifiableView(DocAttributeSet paramDocAttributeSet)
/*     */   {
/* 229 */     if (paramDocAttributeSet == null) {
/* 230 */       throw new NullPointerException();
/*     */     }
/* 232 */     return new UnmodifiableDocAttributeSet(paramDocAttributeSet);
/*     */   }
/*     */ 
/*     */   public static PrintRequestAttributeSet unmodifiableView(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */   {
/* 247 */     if (paramPrintRequestAttributeSet == null) {
/* 248 */       throw new NullPointerException();
/*     */     }
/* 250 */     return new UnmodifiablePrintRequestAttributeSet(paramPrintRequestAttributeSet);
/*     */   }
/*     */ 
/*     */   public static PrintJobAttributeSet unmodifiableView(PrintJobAttributeSet paramPrintJobAttributeSet)
/*     */   {
/* 265 */     if (paramPrintJobAttributeSet == null) {
/* 266 */       throw new NullPointerException();
/*     */     }
/* 268 */     return new UnmodifiablePrintJobAttributeSet(paramPrintJobAttributeSet);
/*     */   }
/*     */ 
/*     */   public static PrintServiceAttributeSet unmodifiableView(PrintServiceAttributeSet paramPrintServiceAttributeSet)
/*     */   {
/* 283 */     if (paramPrintServiceAttributeSet == null) {
/* 284 */       throw new NullPointerException();
/*     */     }
/* 286 */     return new UnmodifiablePrintServiceAttributeSet(paramPrintServiceAttributeSet);
/*     */   }
/*     */ 
/*     */   public static AttributeSet synchronizedView(AttributeSet paramAttributeSet)
/*     */   {
/* 416 */     if (paramAttributeSet == null) {
/* 417 */       throw new NullPointerException();
/*     */     }
/* 419 */     return new SynchronizedAttributeSet(paramAttributeSet);
/*     */   }
/*     */ 
/*     */   public static DocAttributeSet synchronizedView(DocAttributeSet paramDocAttributeSet)
/*     */   {
/* 434 */     if (paramDocAttributeSet == null) {
/* 435 */       throw new NullPointerException();
/*     */     }
/* 437 */     return new SynchronizedDocAttributeSet(paramDocAttributeSet);
/*     */   }
/*     */ 
/*     */   public static PrintRequestAttributeSet synchronizedView(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */   {
/* 452 */     if (paramPrintRequestAttributeSet == null) {
/* 453 */       throw new NullPointerException();
/*     */     }
/* 455 */     return new SynchronizedPrintRequestAttributeSet(paramPrintRequestAttributeSet);
/*     */   }
/*     */ 
/*     */   public static PrintJobAttributeSet synchronizedView(PrintJobAttributeSet paramPrintJobAttributeSet)
/*     */   {
/* 470 */     if (paramPrintJobAttributeSet == null) {
/* 471 */       throw new NullPointerException();
/*     */     }
/* 473 */     return new SynchronizedPrintJobAttributeSet(paramPrintJobAttributeSet);
/*     */   }
/*     */ 
/*     */   public static PrintServiceAttributeSet synchronizedView(PrintServiceAttributeSet paramPrintServiceAttributeSet)
/*     */   {
/* 485 */     if (paramPrintServiceAttributeSet == null) {
/* 486 */       throw new NullPointerException();
/*     */     }
/* 488 */     return new SynchronizedPrintServiceAttributeSet(paramPrintServiceAttributeSet);
/*     */   }
/*     */ 
/*     */   public static Class<?> verifyAttributeCategory(Object paramObject, Class<?> paramClass)
/*     */   {
/* 515 */     Class localClass = (Class)paramObject;
/* 516 */     if (paramClass.isAssignableFrom(localClass)) {
/* 517 */       return localClass;
/*     */     }
/*     */ 
/* 520 */     throw new ClassCastException();
/*     */   }
/*     */ 
/*     */   public static Attribute verifyAttributeValue(Object paramObject, Class<?> paramClass)
/*     */   {
/* 546 */     if (paramObject == null) {
/* 547 */       throw new NullPointerException();
/*     */     }
/* 549 */     if (paramClass.isInstance(paramObject)) {
/* 550 */       return (Attribute)paramObject;
/*     */     }
/* 552 */     throw new ClassCastException();
/*     */   }
/*     */ 
/*     */   public static void verifyCategoryForValue(Class<?> paramClass, Attribute paramAttribute)
/*     */   {
/* 574 */     if (!paramClass.equals(paramAttribute.getCategory()))
/* 575 */       throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   private static class SynchronizedAttributeSet
/*     */     implements AttributeSet, Serializable
/*     */   {
/*     */     private AttributeSet attrset;
/*     */ 
/*     */     public SynchronizedAttributeSet(AttributeSet paramAttributeSet)
/*     */     {
/* 298 */       this.attrset = paramAttributeSet;
/*     */     }
/*     */ 
/*     */     public synchronized Attribute get(Class<?> paramClass) {
/* 302 */       return this.attrset.get(paramClass);
/*     */     }
/*     */ 
/*     */     public synchronized boolean add(Attribute paramAttribute) {
/* 306 */       return this.attrset.add(paramAttribute);
/*     */     }
/*     */ 
/*     */     public synchronized boolean remove(Class<?> paramClass) {
/* 310 */       return this.attrset.remove(paramClass);
/*     */     }
/*     */ 
/*     */     public synchronized boolean remove(Attribute paramAttribute) {
/* 314 */       return this.attrset.remove(paramAttribute);
/*     */     }
/*     */ 
/*     */     public synchronized boolean containsKey(Class<?> paramClass) {
/* 318 */       return this.attrset.containsKey(paramClass);
/*     */     }
/*     */ 
/*     */     public synchronized boolean containsValue(Attribute paramAttribute) {
/* 322 */       return this.attrset.containsValue(paramAttribute);
/*     */     }
/*     */ 
/*     */     public synchronized boolean addAll(AttributeSet paramAttributeSet) {
/* 326 */       return this.attrset.addAll(paramAttributeSet);
/*     */     }
/*     */ 
/*     */     public synchronized int size() {
/* 330 */       return this.attrset.size();
/*     */     }
/*     */ 
/*     */     public synchronized Attribute[] toArray() {
/* 334 */       return this.attrset.toArray();
/*     */     }
/*     */ 
/*     */     public synchronized void clear() {
/* 338 */       this.attrset.clear();
/*     */     }
/*     */ 
/*     */     public synchronized boolean isEmpty() {
/* 342 */       return this.attrset.isEmpty();
/*     */     }
/*     */ 
/*     */     public synchronized boolean equals(Object paramObject) {
/* 346 */       return this.attrset.equals(paramObject);
/*     */     }
/*     */ 
/*     */     public synchronized int hashCode() {
/* 350 */       return this.attrset.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SynchronizedDocAttributeSet extends AttributeSetUtilities.SynchronizedAttributeSet
/*     */     implements DocAttributeSet, Serializable
/*     */   {
/*     */     public SynchronizedDocAttributeSet(DocAttributeSet paramDocAttributeSet)
/*     */     {
/* 362 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SynchronizedPrintJobAttributeSet extends AttributeSetUtilities.SynchronizedAttributeSet
/*     */     implements PrintJobAttributeSet, Serializable
/*     */   {
/*     */     public SynchronizedPrintJobAttributeSet(PrintJobAttributeSet paramPrintJobAttributeSet)
/*     */     {
/* 388 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SynchronizedPrintRequestAttributeSet extends AttributeSetUtilities.SynchronizedAttributeSet
/*     */     implements PrintRequestAttributeSet, Serializable
/*     */   {
/*     */     public SynchronizedPrintRequestAttributeSet(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */     {
/* 375 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SynchronizedPrintServiceAttributeSet extends AttributeSetUtilities.SynchronizedAttributeSet
/*     */     implements PrintServiceAttributeSet, Serializable
/*     */   {
/*     */     public SynchronizedPrintServiceAttributeSet(PrintServiceAttributeSet paramPrintServiceAttributeSet)
/*     */     {
/* 400 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class UnmodifiableAttributeSet
/*     */     implements AttributeSet, Serializable
/*     */   {
/*     */     private AttributeSet attrset;
/*     */ 
/*     */     public UnmodifiableAttributeSet(AttributeSet paramAttributeSet)
/*     */     {
/*  87 */       this.attrset = paramAttributeSet;
/*     */     }
/*     */ 
/*     */     public Attribute get(Class<?> paramClass) {
/*  91 */       return this.attrset.get(paramClass);
/*     */     }
/*     */ 
/*     */     public boolean add(Attribute paramAttribute) {
/*  95 */       throw new UnmodifiableSetException();
/*     */     }
/*     */ 
/*     */     public synchronized boolean remove(Class<?> paramClass) {
/*  99 */       throw new UnmodifiableSetException();
/*     */     }
/*     */ 
/*     */     public boolean remove(Attribute paramAttribute) {
/* 103 */       throw new UnmodifiableSetException();
/*     */     }
/*     */ 
/*     */     public boolean containsKey(Class<?> paramClass) {
/* 107 */       return this.attrset.containsKey(paramClass);
/*     */     }
/*     */ 
/*     */     public boolean containsValue(Attribute paramAttribute) {
/* 111 */       return this.attrset.containsValue(paramAttribute);
/*     */     }
/*     */ 
/*     */     public boolean addAll(AttributeSet paramAttributeSet) {
/* 115 */       throw new UnmodifiableSetException();
/*     */     }
/*     */ 
/*     */     public int size() {
/* 119 */       return this.attrset.size();
/*     */     }
/*     */ 
/*     */     public Attribute[] toArray() {
/* 123 */       return this.attrset.toArray();
/*     */     }
/*     */ 
/*     */     public void clear() {
/* 127 */       throw new UnmodifiableSetException();
/*     */     }
/*     */ 
/*     */     public boolean isEmpty() {
/* 131 */       return this.attrset.isEmpty();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 135 */       return this.attrset.equals(paramObject);
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 139 */       return this.attrset.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class UnmodifiableDocAttributeSet extends AttributeSetUtilities.UnmodifiableAttributeSet
/*     */     implements DocAttributeSet, Serializable
/*     */   {
/*     */     public UnmodifiableDocAttributeSet(DocAttributeSet paramDocAttributeSet)
/*     */     {
/* 153 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class UnmodifiablePrintJobAttributeSet extends AttributeSetUtilities.UnmodifiableAttributeSet
/*     */     implements PrintJobAttributeSet, Serializable
/*     */   {
/*     */     public UnmodifiablePrintJobAttributeSet(PrintJobAttributeSet paramPrintJobAttributeSet)
/*     */     {
/* 181 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class UnmodifiablePrintRequestAttributeSet extends AttributeSetUtilities.UnmodifiableAttributeSet
/*     */     implements PrintRequestAttributeSet, Serializable
/*     */   {
/*     */     public UnmodifiablePrintRequestAttributeSet(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */     {
/* 167 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class UnmodifiablePrintServiceAttributeSet extends AttributeSetUtilities.UnmodifiableAttributeSet
/*     */     implements PrintServiceAttributeSet, Serializable
/*     */   {
/*     */     public UnmodifiablePrintServiceAttributeSet(PrintServiceAttributeSet paramPrintServiceAttributeSet)
/*     */     {
/* 195 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.AttributeSetUtilities
 * JD-Core Version:    0.6.2
 */