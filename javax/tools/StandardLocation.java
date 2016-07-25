/*     */ package javax.tools;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ public enum StandardLocation
/*     */   implements JavaFileManager.Location
/*     */ {
/*  43 */   CLASS_OUTPUT, 
/*     */ 
/*  48 */   SOURCE_OUTPUT, 
/*     */ 
/*  53 */   CLASS_PATH, 
/*     */ 
/*  58 */   SOURCE_PATH, 
/*     */ 
/*  63 */   ANNOTATION_PROCESSOR_PATH, 
/*     */ 
/*  69 */   PLATFORM_CLASS_PATH;
/*     */ 
/*  94 */   private static ConcurrentMap<String, JavaFileManager.Location> locations = new ConcurrentHashMap();
/*     */ 
/*     */   public static JavaFileManager.Location locationFor(String paramString)
/*     */   {
/*  82 */     if (locations.isEmpty())
/*     */     {
/*  84 */       for (StandardLocation localStandardLocation : values())
/*  85 */         locations.putIfAbsent(localStandardLocation.getName(), localStandardLocation);
/*     */     }
/*  87 */     locations.putIfAbsent(paramString.toString(), new JavaFileManager.Location() {
/*  88 */       public String getName() { return this.val$name; } 
/*  89 */       public boolean isOutputLocation() { return this.val$name.endsWith("_OUTPUT"); }
/*     */ 
/*     */     });
/*  91 */     return (JavaFileManager.Location)locations.get(paramString);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  97 */     return name();
/*     */   }
/*     */   public boolean isOutputLocation() {
/* 100 */     return (this == CLASS_OUTPUT) || (this == SOURCE_OUTPUT);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.StandardLocation
 * JD-Core Version:    0.6.2
 */