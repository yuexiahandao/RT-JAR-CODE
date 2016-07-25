/*     */ package com.sun.script.javascript;
/*     */ 
/*     */ import com.sun.script.util.ScriptEngineFactoryBase;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.script.ScriptEngine;
/*     */ 
/*     */ public class RhinoScriptEngineFactory extends ScriptEngineFactoryBase
/*     */ {
/* 143 */   private static List<String> names = new ArrayList(6);
/*     */   private static List<String> mimeTypes;
/* 161 */   private static List<String> extensions = Collections.unmodifiableList(extensions);
/*     */ 
/*     */   public List<String> getExtensions()
/*     */   {
/*  44 */     return extensions;
/*     */   }
/*     */ 
/*     */   public List<String> getMimeTypes() {
/*  48 */     return mimeTypes;
/*     */   }
/*     */ 
/*     */   public List<String> getNames() {
/*  52 */     return names;
/*     */   }
/*     */ 
/*     */   public Object getParameter(String paramString) {
/*  56 */     if (paramString.equals("javax.script.name"))
/*  57 */       return "javascript";
/*  58 */     if (paramString.equals("javax.script.engine"))
/*  59 */       return "Mozilla Rhino";
/*  60 */     if (paramString.equals("javax.script.engine_version"))
/*  61 */       return "1.7 release 3 PRERELEASE";
/*  62 */     if (paramString.equals("javax.script.language"))
/*  63 */       return "ECMAScript";
/*  64 */     if (paramString.equals("javax.script.language_version"))
/*  65 */       return "1.8";
/*  66 */     if (paramString.equals("THREADING")) {
/*  67 */       return "MULTITHREADED";
/*     */     }
/*  69 */     throw new IllegalArgumentException("Invalid key");
/*     */   }
/*     */ 
/*     */   public ScriptEngine getScriptEngine()
/*     */   {
/*  74 */     RhinoScriptEngine localRhinoScriptEngine = new RhinoScriptEngine();
/*  75 */     localRhinoScriptEngine.setEngineFactory(this);
/*  76 */     return localRhinoScriptEngine;
/*     */   }
/*     */ 
/*     */   public String getMethodCallSyntax(String paramString1, String paramString2, String[] paramArrayOfString)
/*     */   {
/*  81 */     String str = paramString1 + "." + paramString2 + "(";
/*  82 */     int i = paramArrayOfString.length;
/*  83 */     if (i == 0) {
/*  84 */       str = str + ")";
/*  85 */       return str;
/*     */     }
/*     */ 
/*  88 */     for (int j = 0; j < i; j++) {
/*  89 */       str = str + paramArrayOfString[j];
/*  90 */       if (j != i - 1)
/*  91 */         str = str + ",";
/*     */       else {
/*  93 */         str = str + ")";
/*     */       }
/*     */     }
/*  96 */     return str;
/*     */   }
/*     */ 
/*     */   public String getOutputStatement(String paramString) {
/* 100 */     StringBuffer localStringBuffer = new StringBuffer();
/* 101 */     int i = paramString.length();
/* 102 */     localStringBuffer.append("print(\"");
/* 103 */     for (int j = 0; j < i; j++) {
/* 104 */       char c = paramString.charAt(j);
/* 105 */       switch (c) {
/*     */       case '"':
/* 107 */         localStringBuffer.append("\\\"");
/* 108 */         break;
/*     */       case '\\':
/* 110 */         localStringBuffer.append("\\\\");
/* 111 */         break;
/*     */       default:
/* 113 */         localStringBuffer.append(c);
/*     */       }
/*     */     }
/*     */ 
/* 117 */     localStringBuffer.append("\")");
/* 118 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String getProgram(String[] paramArrayOfString) {
/* 122 */     int i = paramArrayOfString.length;
/* 123 */     String str = "";
/* 124 */     for (int j = 0; j < i; j++) {
/* 125 */       str = str + paramArrayOfString[j] + ";";
/*     */     }
/*     */ 
/* 128 */     return str;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 144 */     names.add("js");
/* 145 */     names.add("rhino");
/* 146 */     names.add("JavaScript");
/* 147 */     names.add("javascript");
/* 148 */     names.add("ECMAScript");
/* 149 */     names.add("ecmascript");
/* 150 */     names = Collections.unmodifiableList(names);
/*     */ 
/* 152 */     mimeTypes = new ArrayList(4);
/* 153 */     mimeTypes.add("application/javascript");
/* 154 */     mimeTypes.add("application/ecmascript");
/* 155 */     mimeTypes.add("text/javascript");
/* 156 */     mimeTypes.add("text/ecmascript");
/* 157 */     mimeTypes = Collections.unmodifiableList(mimeTypes);
/*     */ 
/* 159 */     extensions = new ArrayList(1);
/* 160 */     extensions.add("js");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.javascript.RhinoScriptEngineFactory
 * JD-Core Version:    0.6.2
 */