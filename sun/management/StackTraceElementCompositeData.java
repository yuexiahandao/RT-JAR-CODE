/*     */ package sun.management;
/*     */ 
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeDataSupport;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ 
/*     */ public class StackTraceElementCompositeData extends LazyCompositeData
/*     */ {
/*     */   private final StackTraceElement ste;
/*     */   private static final CompositeType stackTraceElementCompositeType;
/*     */   private static final String CLASS_NAME = "className";
/*     */   private static final String METHOD_NAME = "methodName";
/*     */   private static final String FILE_NAME = "fileName";
/*     */   private static final String LINE_NUMBER = "lineNumber";
/*     */   private static final String NATIVE_METHOD = "nativeMethod";
/* 101 */   private static final String[] stackTraceElementItemNames = { "className", "methodName", "fileName", "lineNumber", "nativeMethod" };
/*     */   private static final long serialVersionUID = -2704607706598396827L;
/*     */ 
/*     */   private StackTraceElementCompositeData(StackTraceElement paramStackTraceElement)
/*     */   {
/*  42 */     this.ste = paramStackTraceElement;
/*     */   }
/*     */ 
/*     */   public StackTraceElement getStackTraceElement() {
/*  46 */     return this.ste;
/*     */   }
/*     */ 
/*     */   public static StackTraceElement from(CompositeData paramCompositeData) {
/*  50 */     validateCompositeData(paramCompositeData);
/*     */ 
/*  52 */     return new StackTraceElement(getString(paramCompositeData, "className"), getString(paramCompositeData, "methodName"), getString(paramCompositeData, "fileName"), getInt(paramCompositeData, "lineNumber"));
/*     */   }
/*     */ 
/*     */   public static CompositeData toCompositeData(StackTraceElement paramStackTraceElement)
/*     */   {
/*  59 */     StackTraceElementCompositeData localStackTraceElementCompositeData = new StackTraceElementCompositeData(paramStackTraceElement);
/*  60 */     return localStackTraceElementCompositeData.getCompositeData();
/*     */   }
/*     */ 
/*     */   protected CompositeData getCompositeData()
/*     */   {
/*  66 */     Object[] arrayOfObject = { this.ste.getClassName(), this.ste.getMethodName(), this.ste.getFileName(), new Integer(this.ste.getLineNumber()), new Boolean(this.ste.isNativeMethod()) };
/*     */     try
/*     */     {
/*  74 */       return new CompositeDataSupport(stackTraceElementCompositeType, stackTraceElementItemNames, arrayOfObject);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException)
/*     */     {
/*  79 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void validateCompositeData(CompositeData paramCompositeData)
/*     */   {
/* 114 */     if (paramCompositeData == null) {
/* 115 */       throw new NullPointerException("Null CompositeData");
/*     */     }
/*     */ 
/* 118 */     if (!isTypeMatched(stackTraceElementCompositeType, paramCompositeData.getCompositeType()))
/* 119 */       throw new IllegalArgumentException("Unexpected composite type for StackTraceElement");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  86 */       stackTraceElementCompositeType = (CompositeType)MappedMXBeanType.toOpenType(StackTraceElement.class);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException)
/*     */     {
/*  90 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.StackTraceElementCompositeData
 * JD-Core Version:    0.6.2
 */