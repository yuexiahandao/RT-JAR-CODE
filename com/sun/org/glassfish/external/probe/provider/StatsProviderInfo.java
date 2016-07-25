/*    */ package com.sun.org.glassfish.external.probe.provider;
/*    */ 
/*    */ public class StatsProviderInfo
/*    */ {
/*    */   private String configElement;
/*    */   private PluginPoint pp;
/*    */   private String subTreeRoot;
/*    */   private Object statsProvider;
/* 55 */   private String configLevelStr = null;
/*    */   private final String invokerId;
/*    */ 
/*    */   public StatsProviderInfo(String configElement, PluginPoint pp, String subTreeRoot, Object statsProvider)
/*    */   {
/* 38 */     this(configElement, pp, subTreeRoot, statsProvider, null);
/*    */   }
/*    */ 
/*    */   public StatsProviderInfo(String configElement, PluginPoint pp, String subTreeRoot, Object statsProvider, String invokerId)
/*    */   {
/* 44 */     this.configElement = configElement;
/* 45 */     this.pp = pp;
/* 46 */     this.subTreeRoot = subTreeRoot;
/* 47 */     this.statsProvider = statsProvider;
/* 48 */     this.invokerId = invokerId;
/*    */   }
/*    */ 
/*    */   public String getConfigElement()
/*    */   {
/* 59 */     return this.configElement;
/*    */   }
/*    */ 
/*    */   public PluginPoint getPluginPoint() {
/* 63 */     return this.pp;
/*    */   }
/*    */ 
/*    */   public String getSubTreeRoot() {
/* 67 */     return this.subTreeRoot;
/*    */   }
/*    */ 
/*    */   public Object getStatsProvider() {
/* 71 */     return this.statsProvider;
/*    */   }
/*    */ 
/*    */   public String getConfigLevel() {
/* 75 */     return this.configLevelStr;
/*    */   }
/*    */ 
/*    */   public void setConfigLevel(String configLevelStr) {
/* 79 */     this.configLevelStr = configLevelStr;
/*    */   }
/*    */ 
/*    */   public String getInvokerId() {
/* 83 */     return this.invokerId;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.probe.provider.StatsProviderInfo
 * JD-Core Version:    0.6.2
 */