/*     */ package javax.script;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.misc.Service;
/*     */ import sun.misc.ServiceConfigurationError;
/*     */ 
/*     */ public class ScriptEngineManager
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private HashSet<ScriptEngineFactory> engineSpis;
/*     */   private HashMap<String, ScriptEngineFactory> nameAssociations;
/*     */   private HashMap<String, ScriptEngineFactory> extensionAssociations;
/*     */   private HashMap<String, ScriptEngineFactory> mimeTypeAssociations;
/*     */   private Bindings globalScope;
/*     */ 
/*     */   public ScriptEngineManager()
/*     */   {
/*  64 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*  65 */     init(localClassLoader);
/*     */   }
/*     */ 
/*     */   public ScriptEngineManager(ClassLoader paramClassLoader)
/*     */   {
/*  79 */     init(paramClassLoader);
/*     */   }
/*     */ 
/*     */   private void init(final ClassLoader paramClassLoader) {
/*  83 */     this.globalScope = new SimpleBindings();
/*  84 */     this.engineSpis = new HashSet();
/*  85 */     this.nameAssociations = new HashMap();
/*  86 */     this.extensionAssociations = new HashMap();
/*  87 */     this.mimeTypeAssociations = new HashMap();
/*  88 */     List localList = (List)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public List<ScriptEngineFactory> run() {
/*  91 */         return ScriptEngineManager.this.initEngines(paramClassLoader);
/*     */       }
/*     */     });
/*  94 */     for (ScriptEngineFactory localScriptEngineFactory : localList)
/*  95 */       this.engineSpis.add(localScriptEngineFactory);
/*     */   }
/*     */ 
/*     */   private List<ScriptEngineFactory> initEngines(ClassLoader paramClassLoader)
/*     */   {
/* 100 */     Iterator localIterator = null;
/*     */     try {
/* 102 */       if (paramClassLoader != null)
/* 103 */         localIterator = Service.providers(ScriptEngineFactory.class, paramClassLoader);
/*     */       else
/* 105 */         localIterator = Service.installedProviders(ScriptEngineFactory.class);
/*     */     }
/*     */     catch (ServiceConfigurationError localServiceConfigurationError1) {
/* 108 */       System.err.println("Can't find ScriptEngineFactory providers: " + localServiceConfigurationError1.getMessage());
/*     */ 
/* 116 */       return null;
/*     */     }
/*     */ 
/* 119 */     ArrayList localArrayList = new ArrayList();
/*     */     try {
/* 121 */       while (localIterator.hasNext()) {
/*     */         try {
/* 123 */           ScriptEngineFactory localScriptEngineFactory = (ScriptEngineFactory)localIterator.next();
/* 124 */           localArrayList.add(localScriptEngineFactory);
/*     */         } catch (ServiceConfigurationError localServiceConfigurationError2) {
/* 126 */           System.err.println("ScriptEngineManager providers.next(): " + localServiceConfigurationError2.getMessage());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (ServiceConfigurationError localServiceConfigurationError3)
/*     */     {
/* 136 */       System.err.println("ScriptEngineManager providers.hasNext(): " + localServiceConfigurationError3.getMessage());
/*     */     }
/*     */ 
/* 145 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public void setBindings(Bindings paramBindings)
/*     */   {
/* 158 */     if (paramBindings == null) {
/* 159 */       throw new IllegalArgumentException("Global scope cannot be null.");
/*     */     }
/*     */ 
/* 162 */     this.globalScope = paramBindings;
/*     */   }
/*     */ 
/*     */   public Bindings getBindings()
/*     */   {
/* 173 */     return this.globalScope;
/*     */   }
/*     */ 
/*     */   public void put(String paramString, Object paramObject)
/*     */   {
/* 184 */     this.globalScope.put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */   {
/* 193 */     return this.globalScope.get(paramString);
/*     */   }
/*     */ 
/*     */   public ScriptEngine getEngineByName(String paramString)
/*     */   {
/* 213 */     if (paramString == null) throw new NullPointerException();
/*     */     Object localObject1;
/* 216 */     if (null != (localObject1 = this.nameAssociations.get(paramString))) {
/* 217 */       localObject2 = (ScriptEngineFactory)localObject1;
/*     */       try {
/* 219 */         ScriptEngine localScriptEngine1 = ((ScriptEngineFactory)localObject2).getScriptEngine();
/* 220 */         localScriptEngine1.setBindings(getBindings(), 200);
/* 221 */         return localScriptEngine1;
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */     }
/* 227 */     for (Object localObject2 = this.engineSpis.iterator(); ((Iterator)localObject2).hasNext(); ) { localScriptEngineFactory = (ScriptEngineFactory)((Iterator)localObject2).next();
/* 228 */       List localList = null;
/*     */       try {
/* 230 */         localList = localScriptEngineFactory.getNames();
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/* 235 */       if (localList != null)
/* 236 */         for (String str : localList)
/* 237 */           if (paramString.equals(str))
/*     */             try {
/* 239 */               ScriptEngine localScriptEngine2 = localScriptEngineFactory.getScriptEngine();
/* 240 */               localScriptEngine2.setBindings(getBindings(), 200);
/* 241 */               return localScriptEngine2;
/*     */             }
/*     */             catch (Exception localException3)
/*     */             {
/*     */             }
/*     */     }
/*     */     ScriptEngineFactory localScriptEngineFactory;
/* 250 */     return null;
/*     */   }
/*     */ 
/*     */   public ScriptEngine getEngineByExtension(String paramString)
/*     */   {
/* 264 */     if (paramString == null) throw new NullPointerException();
/*     */     Object localObject1;
/* 267 */     if (null != (localObject1 = this.extensionAssociations.get(paramString))) {
/* 268 */       localObject2 = (ScriptEngineFactory)localObject1;
/*     */       try {
/* 270 */         ScriptEngine localScriptEngine1 = ((ScriptEngineFactory)localObject2).getScriptEngine();
/* 271 */         localScriptEngine1.setBindings(getBindings(), 200);
/* 272 */         return localScriptEngine1;
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */     }
/* 278 */     for (Object localObject2 = this.engineSpis.iterator(); ((Iterator)localObject2).hasNext(); ) { localScriptEngineFactory = (ScriptEngineFactory)((Iterator)localObject2).next();
/* 279 */       List localList = null;
/*     */       try {
/* 281 */         localList = localScriptEngineFactory.getExtensions();
/*     */       }
/*     */       catch (Exception localException2) {
/*     */       }
/* 285 */       if (localList != null)
/* 286 */         for (String str : localList)
/* 287 */           if (paramString.equals(str))
/*     */             try {
/* 289 */               ScriptEngine localScriptEngine2 = localScriptEngineFactory.getScriptEngine();
/* 290 */               localScriptEngine2.setBindings(getBindings(), 200);
/* 291 */               return localScriptEngine2;
/*     */             }
/*     */             catch (Exception localException3)
/*     */             {
/*     */             }
/*     */     }
/*     */     ScriptEngineFactory localScriptEngineFactory;
/* 298 */     return null;
/*     */   }
/*     */ 
/*     */   public ScriptEngine getEngineByMimeType(String paramString)
/*     */   {
/* 312 */     if (paramString == null) throw new NullPointerException();
/*     */     Object localObject1;
/* 315 */     if (null != (localObject1 = this.mimeTypeAssociations.get(paramString))) {
/* 316 */       localObject2 = (ScriptEngineFactory)localObject1;
/*     */       try {
/* 318 */         ScriptEngine localScriptEngine1 = ((ScriptEngineFactory)localObject2).getScriptEngine();
/* 319 */         localScriptEngine1.setBindings(getBindings(), 200);
/* 320 */         return localScriptEngine1;
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */     }
/* 326 */     for (Object localObject2 = this.engineSpis.iterator(); ((Iterator)localObject2).hasNext(); ) { localScriptEngineFactory = (ScriptEngineFactory)((Iterator)localObject2).next();
/* 327 */       List localList = null;
/*     */       try {
/* 329 */         localList = localScriptEngineFactory.getMimeTypes();
/*     */       }
/*     */       catch (Exception localException2) {
/*     */       }
/* 333 */       if (localList != null)
/* 334 */         for (String str : localList)
/* 335 */           if (paramString.equals(str))
/*     */             try {
/* 337 */               ScriptEngine localScriptEngine2 = localScriptEngineFactory.getScriptEngine();
/* 338 */               localScriptEngine2.setBindings(getBindings(), 200);
/* 339 */               return localScriptEngine2;
/*     */             }
/*     */             catch (Exception localException3)
/*     */             {
/*     */             }
/*     */     }
/*     */     ScriptEngineFactory localScriptEngineFactory;
/* 346 */     return null;
/*     */   }
/*     */ 
/*     */   public List<ScriptEngineFactory> getEngineFactories()
/*     */   {
/* 355 */     ArrayList localArrayList = new ArrayList(this.engineSpis.size());
/* 356 */     for (ScriptEngineFactory localScriptEngineFactory : this.engineSpis) {
/* 357 */       localArrayList.add(localScriptEngineFactory);
/*     */     }
/* 359 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public void registerEngineName(String paramString, ScriptEngineFactory paramScriptEngineFactory)
/*     */   {
/* 370 */     if ((paramString == null) || (paramScriptEngineFactory == null)) throw new NullPointerException();
/* 371 */     this.nameAssociations.put(paramString, paramScriptEngineFactory);
/*     */   }
/*     */ 
/*     */   public void registerEngineMimeType(String paramString, ScriptEngineFactory paramScriptEngineFactory)
/*     */   {
/* 385 */     if ((paramString == null) || (paramScriptEngineFactory == null)) throw new NullPointerException();
/* 386 */     this.mimeTypeAssociations.put(paramString, paramScriptEngineFactory);
/*     */   }
/*     */ 
/*     */   public void registerEngineExtension(String paramString, ScriptEngineFactory paramScriptEngineFactory)
/*     */   {
/* 399 */     if ((paramString == null) || (paramScriptEngineFactory == null)) throw new NullPointerException();
/* 400 */     this.extensionAssociations.put(paramString, paramScriptEngineFactory);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.script.ScriptEngineManager
 * JD-Core Version:    0.6.2
 */