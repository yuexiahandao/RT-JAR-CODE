/*    */ package com.sun.script.javascript;
/*    */ 
/*    */ import javax.script.CompiledScript;
/*    */ import javax.script.ScriptContext;
/*    */ import javax.script.ScriptEngine;
/*    */ import javax.script.ScriptException;
/*    */ import sun.org.mozilla.javascript.internal.Context;
/*    */ import sun.org.mozilla.javascript.internal.JavaScriptException;
/*    */ import sun.org.mozilla.javascript.internal.RhinoException;
/*    */ import sun.org.mozilla.javascript.internal.Script;
/*    */ import sun.org.mozilla.javascript.internal.Scriptable;
/*    */ 
/*    */ final class RhinoCompiledScript extends CompiledScript
/*    */ {
/*    */   private RhinoScriptEngine engine;
/*    */   private Script script;
/*    */ 
/*    */   RhinoCompiledScript(RhinoScriptEngine paramRhinoScriptEngine, Script paramScript)
/*    */   {
/* 43 */     this.engine = paramRhinoScriptEngine;
/* 44 */     this.script = paramScript;
/*    */   }
/*    */ 
/*    */   public Object eval(ScriptContext paramScriptContext) throws ScriptException
/*    */   {
/* 49 */     Object localObject1 = null;
/* 50 */     Context localContext = RhinoScriptEngine.enterContext();
/*    */     try
/*    */     {
/* 53 */       Scriptable localScriptable = this.engine.getRuntimeScope(paramScriptContext);
/* 54 */       Object localObject2 = this.script.exec(localContext, localScriptable);
/* 55 */       localObject1 = this.engine.unwrapReturnValue(localObject2);
/*    */     } catch (RhinoException localRhinoException) {
/* 57 */       int i = (i = localRhinoException.lineNumber()) == 0 ? -1 : i;
/*    */       String str;
/* 59 */       if ((localRhinoException instanceof JavaScriptException))
/* 60 */         str = String.valueOf(((JavaScriptException)localRhinoException).getValue());
/*    */       else {
/* 62 */         str = localRhinoException.toString();
/*    */       }
/* 64 */       ScriptException localScriptException = new ScriptException(str, localRhinoException.sourceName(), i);
/* 65 */       localScriptException.initCause(localRhinoException);
/* 66 */       throw localScriptException;
/*    */     } finally {
/* 68 */       Context.exit();
/*    */     }
/*    */ 
/* 71 */     return localObject1;
/*    */   }
/*    */ 
/*    */   public ScriptEngine getEngine() {
/* 75 */     return this.engine;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.javascript.RhinoCompiledScript
 * JD-Core Version:    0.6.2
 */