/*     */ package javax.script;
/*     */ 
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SimpleScriptContext
/*     */   implements ScriptContext
/*     */ {
/*     */   protected Writer writer;
/*     */   protected Writer errorWriter;
/*     */   protected Reader reader;
/*     */   protected Bindings engineScope;
/*     */   protected Bindings globalScope;
/* 320 */   private static List<Integer> scopes = Collections.unmodifiableList(scopes);
/*     */ 
/*     */   public SimpleScriptContext()
/*     */   {
/*  87 */     this.engineScope = new SimpleBindings();
/*  88 */     this.globalScope = null;
/*  89 */     this.reader = new InputStreamReader(System.in);
/*  90 */     this.writer = new PrintWriter(System.out, true);
/*  91 */     this.errorWriter = new PrintWriter(System.err, true);
/*     */   }
/*     */ 
/*     */   public void setBindings(Bindings paramBindings, int paramInt)
/*     */   {
/* 110 */     switch (paramInt)
/*     */     {
/*     */     case 100:
/* 113 */       if (paramBindings == null) {
/* 114 */         throw new NullPointerException("Engine scope cannot be null.");
/*     */       }
/* 116 */       this.engineScope = paramBindings;
/* 117 */       break;
/*     */     case 200:
/* 119 */       this.globalScope = paramBindings;
/* 120 */       break;
/*     */     default:
/* 122 */       throw new IllegalArgumentException("Invalid scope value.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String paramString)
/*     */   {
/* 141 */     if (this.engineScope.containsKey(paramString))
/* 142 */       return getAttribute(paramString, 100);
/* 143 */     if ((this.globalScope != null) && (this.globalScope.containsKey(paramString))) {
/* 144 */       return getAttribute(paramString, 200);
/*     */     }
/*     */ 
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String paramString, int paramInt)
/*     */   {
/* 164 */     switch (paramInt)
/*     */     {
/*     */     case 100:
/* 167 */       return this.engineScope.get(paramString);
/*     */     case 200:
/* 170 */       if (this.globalScope != null) {
/* 171 */         return this.globalScope.get(paramString);
/*     */       }
/* 173 */       return null;
/*     */     }
/*     */ 
/* 176 */     throw new IllegalArgumentException("Illegal scope value.");
/*     */   }
/*     */ 
/*     */   public Object removeAttribute(String paramString, int paramInt)
/*     */   {
/* 193 */     switch (paramInt)
/*     */     {
/*     */     case 100:
/* 196 */       if (getBindings(100) != null) {
/* 197 */         return getBindings(100).remove(paramString);
/*     */       }
/* 199 */       return null;
/*     */     case 200:
/* 202 */       if (getBindings(200) != null) {
/* 203 */         return getBindings(200).remove(paramString);
/*     */       }
/* 205 */       return null;
/*     */     }
/*     */ 
/* 208 */     throw new IllegalArgumentException("Illegal scope value.");
/*     */   }
/*     */ 
/*     */   public void setAttribute(String paramString, Object paramObject, int paramInt)
/*     */   {
/* 225 */     switch (paramInt)
/*     */     {
/*     */     case 100:
/* 228 */       this.engineScope.put(paramString, paramObject);
/* 229 */       return;
/*     */     case 200:
/* 232 */       if (this.globalScope != null) {
/* 233 */         this.globalScope.put(paramString, paramObject);
/*     */       }
/* 235 */       return;
/*     */     }
/*     */ 
/* 238 */     throw new IllegalArgumentException("Illegal scope value.");
/*     */   }
/*     */ 
/*     */   public Writer getWriter()
/*     */   {
/* 244 */     return this.writer;
/*     */   }
/*     */ 
/*     */   public Reader getReader()
/*     */   {
/* 249 */     return this.reader;
/*     */   }
/*     */ 
/*     */   public void setReader(Reader paramReader)
/*     */   {
/* 254 */     this.reader = paramReader;
/*     */   }
/*     */ 
/*     */   public void setWriter(Writer paramWriter)
/*     */   {
/* 259 */     this.writer = paramWriter;
/*     */   }
/*     */ 
/*     */   public Writer getErrorWriter()
/*     */   {
/* 264 */     return this.errorWriter;
/*     */   }
/*     */ 
/*     */   public void setErrorWriter(Writer paramWriter)
/*     */   {
/* 269 */     this.errorWriter = paramWriter;
/*     */   }
/*     */ 
/*     */   public int getAttributesScope(String paramString)
/*     */   {
/* 282 */     if (this.engineScope.containsKey(paramString))
/* 283 */       return 100;
/* 284 */     if ((this.globalScope != null) && (this.globalScope.containsKey(paramString))) {
/* 285 */       return 200;
/*     */     }
/* 287 */     return -1;
/*     */   }
/*     */ 
/*     */   public Bindings getBindings(int paramInt)
/*     */   {
/* 301 */     if (paramInt == 100)
/* 302 */       return this.engineScope;
/* 303 */     if (paramInt == 200) {
/* 304 */       return this.globalScope;
/*     */     }
/* 306 */     throw new IllegalArgumentException("Illegal scope value.");
/*     */   }
/*     */ 
/*     */   public List<Integer> getScopes()
/*     */   {
/* 312 */     return scopes;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 318 */     scopes.add(Integer.valueOf(100));
/* 319 */     scopes.add(Integer.valueOf(200));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.script.SimpleScriptContext
 * JD-Core Version:    0.6.2
 */