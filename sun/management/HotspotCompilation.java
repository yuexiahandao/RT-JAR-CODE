/*     */ package sun.management;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import sun.management.counter.Counter;
/*     */ import sun.management.counter.LongCounter;
/*     */ import sun.management.counter.StringCounter;
/*     */ 
/*     */ class HotspotCompilation
/*     */   implements HotspotCompilationMBean
/*     */ {
/*     */   private VMManagement jvm;
/*     */   private static final String JAVA_CI = "java.ci.";
/*     */   private static final String COM_SUN_CI = "com.sun.ci.";
/*     */   private static final String SUN_CI = "sun.ci.";
/*     */   private static final String CI_COUNTER_NAME_PATTERN = "java.ci.|com.sun.ci.|sun.ci.";
/*     */   private LongCounter compilerThreads;
/*     */   private LongCounter totalCompiles;
/*     */   private LongCounter totalBailouts;
/*     */   private LongCounter totalInvalidates;
/*     */   private LongCounter nmethodCodeSize;
/*     */   private LongCounter nmethodSize;
/*     */   private StringCounter lastMethod;
/*     */   private LongCounter lastSize;
/*     */   private LongCounter lastType;
/*     */   private StringCounter lastFailedMethod;
/*     */   private LongCounter lastFailedType;
/*     */   private StringCounter lastInvalidatedMethod;
/*     */   private LongCounter lastInvalidatedType;
/*     */   private CompilerThreadInfo[] threads;
/*     */   private int numActiveThreads;
/*     */   private Map<String, Counter> counters;
/*     */ 
/*     */   HotspotCompilation(VMManagement paramVMManagement)
/*     */   {
/*  53 */     this.jvm = paramVMManagement;
/*  54 */     initCompilerCounters();
/*     */   }
/*     */ 
/*     */   private Counter lookup(String paramString)
/*     */   {
/* 117 */     Counter localCounter = null;
/*     */ 
/* 123 */     if ((localCounter = (Counter)this.counters.get("sun.ci." + paramString)) != null) {
/* 124 */       return localCounter;
/*     */     }
/* 126 */     if ((localCounter = (Counter)this.counters.get("com.sun.ci." + paramString)) != null) {
/* 127 */       return localCounter;
/*     */     }
/* 129 */     if ((localCounter = (Counter)this.counters.get("java.ci." + paramString)) != null) {
/* 130 */       return localCounter;
/*     */     }
/*     */ 
/* 134 */     throw new AssertionError("Counter " + paramString + " does not exist");
/*     */   }
/*     */ 
/*     */   private void initCompilerCounters()
/*     */   {
/* 139 */     ListIterator localListIterator = getInternalCompilerCounters().listIterator();
/* 140 */     this.counters = new TreeMap();
/* 141 */     while (localListIterator.hasNext()) {
/* 142 */       Counter localCounter = (Counter)localListIterator.next();
/* 143 */       this.counters.put(localCounter.getName(), localCounter);
/*     */     }
/*     */ 
/* 146 */     this.compilerThreads = ((LongCounter)lookup("threads"));
/* 147 */     this.totalCompiles = ((LongCounter)lookup("totalCompiles"));
/* 148 */     this.totalBailouts = ((LongCounter)lookup("totalBailouts"));
/* 149 */     this.totalInvalidates = ((LongCounter)lookup("totalInvalidates"));
/* 150 */     this.nmethodCodeSize = ((LongCounter)lookup("nmethodCodeSize"));
/* 151 */     this.nmethodSize = ((LongCounter)lookup("nmethodSize"));
/* 152 */     this.lastMethod = ((StringCounter)lookup("lastMethod"));
/* 153 */     this.lastSize = ((LongCounter)lookup("lastSize"));
/* 154 */     this.lastType = ((LongCounter)lookup("lastType"));
/* 155 */     this.lastFailedMethod = ((StringCounter)lookup("lastFailedMethod"));
/* 156 */     this.lastFailedType = ((LongCounter)lookup("lastFailedType"));
/* 157 */     this.lastInvalidatedMethod = ((StringCounter)lookup("lastInvalidatedMethod"));
/* 158 */     this.lastInvalidatedType = ((LongCounter)lookup("lastInvalidatedType"));
/*     */ 
/* 160 */     this.numActiveThreads = ((int)this.compilerThreads.longValue());
/*     */ 
/* 163 */     this.threads = new CompilerThreadInfo[this.numActiveThreads + 1];
/*     */ 
/* 166 */     if (this.counters.containsKey("sun.ci.adapterThread.compiles")) {
/* 167 */       this.threads[0] = new CompilerThreadInfo("adapterThread", 0);
/* 168 */       this.numActiveThreads += 1;
/*     */     } else {
/* 170 */       this.threads[0] = null;
/*     */     }
/*     */ 
/* 173 */     for (int i = 1; i < this.threads.length; i++)
/* 174 */       this.threads[i] = new CompilerThreadInfo("compilerThread", i - 1);
/*     */   }
/*     */ 
/*     */   public int getCompilerThreadCount()
/*     */   {
/* 179 */     return this.numActiveThreads;
/*     */   }
/*     */ 
/*     */   public long getTotalCompileCount() {
/* 183 */     return this.totalCompiles.longValue();
/*     */   }
/*     */ 
/*     */   public long getBailoutCompileCount() {
/* 187 */     return this.totalBailouts.longValue();
/*     */   }
/*     */ 
/*     */   public long getInvalidatedCompileCount() {
/* 191 */     return this.totalInvalidates.longValue();
/*     */   }
/*     */ 
/*     */   public long getCompiledMethodCodeSize() {
/* 195 */     return this.nmethodCodeSize.longValue();
/*     */   }
/*     */ 
/*     */   public long getCompiledMethodSize() {
/* 199 */     return this.nmethodSize.longValue();
/*     */   }
/*     */ 
/*     */   public List<CompilerThreadStat> getCompilerThreadStats() {
/* 203 */     ArrayList localArrayList = new ArrayList(this.threads.length);
/* 204 */     int i = 0;
/* 205 */     if (this.threads[0] == null);
/* 207 */     for (i = 1; 
/* 209 */       i < this.threads.length; i++) {
/* 210 */       localArrayList.add(this.threads[i].getCompilerThreadStat());
/*     */     }
/* 212 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public MethodInfo getLastCompile() {
/* 216 */     return new MethodInfo(this.lastMethod.stringValue(), (int)this.lastType.longValue(), (int)this.lastSize.longValue());
/*     */   }
/*     */ 
/*     */   public MethodInfo getFailedCompile()
/*     */   {
/* 222 */     return new MethodInfo(this.lastFailedMethod.stringValue(), (int)this.lastFailedType.longValue(), -1);
/*     */   }
/*     */ 
/*     */   public MethodInfo getInvalidatedCompile()
/*     */   {
/* 228 */     return new MethodInfo(this.lastInvalidatedMethod.stringValue(), (int)this.lastInvalidatedType.longValue(), -1);
/*     */   }
/*     */ 
/*     */   public List<Counter> getInternalCompilerCounters()
/*     */   {
/* 234 */     return this.jvm.getInternalCounters("java.ci.|com.sun.ci.|sun.ci.");
/*     */   }
/*     */ 
/*     */   private class CompilerThreadInfo
/*     */   {
/*     */     int index;
/*     */     String name;
/*     */     StringCounter method;
/*     */     LongCounter type;
/*     */     LongCounter compiles;
/*     */     LongCounter time;
/*     */ 
/*     */     CompilerThreadInfo(String paramInt, int arg3)
/*     */     {
/*     */       int i;
/*  86 */       String str = paramInt + "." + i + ".";
/*  87 */       this.name = (paramInt + "-" + i);
/*  88 */       this.method = ((StringCounter)HotspotCompilation.this.lookup(str + "method"));
/*  89 */       this.type = ((LongCounter)HotspotCompilation.this.lookup(str + "type"));
/*  90 */       this.compiles = ((LongCounter)HotspotCompilation.this.lookup(str + "compiles"));
/*  91 */       this.time = ((LongCounter)HotspotCompilation.this.lookup(str + "time"));
/*     */     }
/*     */ 
/*     */     CompilerThreadInfo(String arg2)
/*     */     {
/*     */       String str1;
/*  94 */       String str2 = str1 + ".";
/*  95 */       this.name = str1;
/*  96 */       this.method = ((StringCounter)HotspotCompilation.this.lookup(str2 + "method"));
/*  97 */       this.type = ((LongCounter)HotspotCompilation.this.lookup(str2 + "type"));
/*  98 */       this.compiles = ((LongCounter)HotspotCompilation.this.lookup(str2 + "compiles"));
/*  99 */       this.time = ((LongCounter)HotspotCompilation.this.lookup(str2 + "time"));
/*     */     }
/*     */ 
/*     */     CompilerThreadStat getCompilerThreadStat() {
/* 103 */       MethodInfo localMethodInfo = new MethodInfo(this.method.stringValue(), (int)this.type.longValue(), -1);
/*     */ 
/* 106 */       return new CompilerThreadStat(this.name, this.compiles.longValue(), this.time.longValue(), localMethodInfo);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.HotspotCompilation
 * JD-Core Version:    0.6.2
 */