/*    */ package com.sun.org.glassfish.external.probe.provider;
/*    */ 
/*    */ public enum PluginPoint
/*    */ {
/* 36 */   SERVER("server", "server"), 
/* 37 */   APPLICATIONS("applications", "server/applications");
/*    */ 
/*    */   String name;
/*    */   String path;
/*    */ 
/* 43 */   private PluginPoint(String lname, String lpath) { this.name = lname;
/* 44 */     this.path = lpath; }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 48 */     return this.name;
/*    */   }
/*    */ 
/*    */   public String getPath() {
/* 52 */     return this.path;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.probe.provider.PluginPoint
 * JD-Core Version:    0.6.2
 */