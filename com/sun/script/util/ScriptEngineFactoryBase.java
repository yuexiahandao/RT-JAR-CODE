/*    */ package com.sun.script.util;
/*    */ 
/*    */ import javax.script.ScriptEngineFactory;
/*    */ 
/*    */ public abstract class ScriptEngineFactoryBase
/*    */   implements ScriptEngineFactory
/*    */ {
/*    */   public String getName()
/*    */   {
/* 38 */     return (String)getParameter("javax.script.name");
/*    */   }
/*    */ 
/*    */   public String getEngineName() {
/* 42 */     return (String)getParameter("javax.script.engine");
/*    */   }
/*    */ 
/*    */   public String getEngineVersion() {
/* 46 */     return (String)getParameter("javax.script.engine_version");
/*    */   }
/*    */ 
/*    */   public String getLanguageName() {
/* 50 */     return (String)getParameter("javax.script.language");
/*    */   }
/*    */ 
/*    */   public String getLanguageVersion() {
/* 54 */     return (String)getParameter("javax.script.language_version");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.util.ScriptEngineFactoryBase
 * JD-Core Version:    0.6.2
 */