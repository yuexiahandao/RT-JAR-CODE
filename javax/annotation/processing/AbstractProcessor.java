/*     */ package javax.annotation.processing;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ 
/*     */ public abstract class AbstractProcessor
/*     */   implements Processor
/*     */ {
/*     */   protected ProcessingEnvironment processingEnv;
/*  64 */   private boolean initialized = false;
/*     */ 
/*     */   public Set<String> getSupportedOptions()
/*     */   {
/*  81 */     SupportedOptions localSupportedOptions = (SupportedOptions)getClass().getAnnotation(SupportedOptions.class);
/*  82 */     if (localSupportedOptions == null) {
/*  83 */       return Collections.emptySet();
/*     */     }
/*  85 */     return arrayToSet(localSupportedOptions.value());
/*     */   }
/*     */ 
/*     */   public Set<String> getSupportedAnnotationTypes()
/*     */   {
/*  98 */     SupportedAnnotationTypes localSupportedAnnotationTypes = (SupportedAnnotationTypes)getClass().getAnnotation(SupportedAnnotationTypes.class);
/*  99 */     if (localSupportedAnnotationTypes == null) {
/* 100 */       if (isInitialized()) {
/* 101 */         this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No SupportedAnnotationTypes annotation found on " + getClass().getName() + ", returning an empty set.");
/*     */       }
/*     */ 
/* 105 */       return Collections.emptySet();
/*     */     }
/*     */ 
/* 108 */     return arrayToSet(localSupportedAnnotationTypes.value());
/*     */   }
/*     */ 
/*     */   public SourceVersion getSupportedSourceVersion()
/*     */   {
/* 120 */     SupportedSourceVersion localSupportedSourceVersion = (SupportedSourceVersion)getClass().getAnnotation(SupportedSourceVersion.class);
/* 121 */     SourceVersion localSourceVersion = null;
/* 122 */     if (localSupportedSourceVersion == null) {
/* 123 */       localSourceVersion = SourceVersion.RELEASE_6;
/* 124 */       if (isInitialized()) {
/* 125 */         this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No SupportedSourceVersion annotation found on " + getClass().getName() + ", returning " + localSourceVersion + ".");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 130 */       localSourceVersion = localSupportedSourceVersion.value();
/* 131 */     }return localSourceVersion;
/*     */   }
/*     */ 
/*     */   public synchronized void init(ProcessingEnvironment paramProcessingEnvironment)
/*     */   {
/* 147 */     if (this.initialized)
/* 148 */       throw new IllegalStateException("Cannot call init more than once.");
/* 149 */     if (paramProcessingEnvironment == null) {
/* 150 */       throw new NullPointerException("Tool provided null ProcessingEnvironment");
/*     */     }
/* 152 */     this.processingEnv = paramProcessingEnvironment;
/* 153 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public abstract boolean process(Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment);
/*     */ 
/*     */   public Iterable<? extends Completion> getCompletions(Element paramElement, AnnotationMirror paramAnnotationMirror, ExecutableElement paramExecutableElement, String paramString)
/*     */   {
/* 174 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   protected synchronized boolean isInitialized()
/*     */   {
/* 185 */     return this.initialized;
/*     */   }
/*     */ 
/*     */   private static Set<String> arrayToSet(String[] paramArrayOfString) {
/* 189 */     assert (paramArrayOfString != null);
/* 190 */     HashSet localHashSet = new HashSet(paramArrayOfString.length);
/* 191 */     for (String str : paramArrayOfString)
/* 192 */       localHashSet.add(str);
/* 193 */     return Collections.unmodifiableSet(localHashSet);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.annotation.processing.AbstractProcessor
 * JD-Core Version:    0.6.2
 */