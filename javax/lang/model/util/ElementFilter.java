/*     */ package javax.lang.model.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ 
/*     */ public class ElementFilter
/*     */ {
/*  73 */   private static Set<ElementKind> CONSTRUCTOR_KIND = Collections.unmodifiableSet(EnumSet.of(ElementKind.CONSTRUCTOR));
/*     */ 
/*  76 */   private static Set<ElementKind> FIELD_KINDS = Collections.unmodifiableSet(EnumSet.of(ElementKind.FIELD, ElementKind.ENUM_CONSTANT));
/*     */ 
/*  79 */   private static Set<ElementKind> METHOD_KIND = Collections.unmodifiableSet(EnumSet.of(ElementKind.METHOD));
/*     */ 
/*  82 */   private static Set<ElementKind> PACKAGE_KIND = Collections.unmodifiableSet(EnumSet.of(ElementKind.PACKAGE));
/*     */ 
/*  85 */   private static Set<ElementKind> TYPE_KINDS = Collections.unmodifiableSet(EnumSet.of(ElementKind.CLASS, ElementKind.ENUM, ElementKind.INTERFACE, ElementKind.ANNOTATION_TYPE));
/*     */ 
/*     */   public static List<VariableElement> fieldsIn(Iterable<? extends Element> paramIterable)
/*     */   {
/*  97 */     return listFilter(paramIterable, FIELD_KINDS, VariableElement.class);
/*     */   }
/*     */ 
/*     */   public static Set<VariableElement> fieldsIn(Set<? extends Element> paramSet)
/*     */   {
/* 107 */     return setFilter(paramSet, FIELD_KINDS, VariableElement.class);
/*     */   }
/*     */ 
/*     */   public static List<ExecutableElement> constructorsIn(Iterable<? extends Element> paramIterable)
/*     */   {
/* 117 */     return listFilter(paramIterable, CONSTRUCTOR_KIND, ExecutableElement.class);
/*     */   }
/*     */ 
/*     */   public static Set<ExecutableElement> constructorsIn(Set<? extends Element> paramSet)
/*     */   {
/* 127 */     return setFilter(paramSet, CONSTRUCTOR_KIND, ExecutableElement.class);
/*     */   }
/*     */ 
/*     */   public static List<ExecutableElement> methodsIn(Iterable<? extends Element> paramIterable)
/*     */   {
/* 137 */     return listFilter(paramIterable, METHOD_KIND, ExecutableElement.class);
/*     */   }
/*     */ 
/*     */   public static Set<ExecutableElement> methodsIn(Set<? extends Element> paramSet)
/*     */   {
/* 147 */     return setFilter(paramSet, METHOD_KIND, ExecutableElement.class);
/*     */   }
/*     */ 
/*     */   public static List<TypeElement> typesIn(Iterable<? extends Element> paramIterable)
/*     */   {
/* 157 */     return listFilter(paramIterable, TYPE_KINDS, TypeElement.class);
/*     */   }
/*     */ 
/*     */   public static Set<TypeElement> typesIn(Set<? extends Element> paramSet)
/*     */   {
/* 167 */     return setFilter(paramSet, TYPE_KINDS, TypeElement.class);
/*     */   }
/*     */ 
/*     */   public static List<PackageElement> packagesIn(Iterable<? extends Element> paramIterable)
/*     */   {
/* 177 */     return listFilter(paramIterable, PACKAGE_KIND, PackageElement.class);
/*     */   }
/*     */ 
/*     */   public static Set<PackageElement> packagesIn(Set<? extends Element> paramSet)
/*     */   {
/* 187 */     return setFilter(paramSet, PACKAGE_KIND, PackageElement.class);
/*     */   }
/*     */ 
/*     */   private static <E extends Element> List<E> listFilter(Iterable<? extends Element> paramIterable, Set<ElementKind> paramSet, Class<E> paramClass)
/*     */   {
/* 194 */     ArrayList localArrayList = new ArrayList();
/* 195 */     for (Element localElement : paramIterable) {
/* 196 */       if (paramSet.contains(localElement.getKind()))
/* 197 */         localArrayList.add(paramClass.cast(localElement));
/*     */     }
/* 199 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static <E extends Element> Set<E> setFilter(Set<? extends Element> paramSet, Set<ElementKind> paramSet1, Class<E> paramClass)
/*     */   {
/* 207 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 208 */     for (Element localElement : paramSet) {
/* 209 */       if (paramSet1.contains(localElement.getKind()))
/* 210 */         localLinkedHashSet.add(paramClass.cast(localElement));
/*     */     }
/* 212 */     return localLinkedHashSet;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.util.ElementFilter
 * JD-Core Version:    0.6.2
 */