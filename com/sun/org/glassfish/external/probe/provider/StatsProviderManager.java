/*     */ package com.sun.org.glassfish.external.probe.provider;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class StatsProviderManager
/*     */ {
/*     */   static StatsProviderManagerDelegate spmd;
/* 136 */   static Vector<StatsProviderInfo> toBeRegistered = new Vector();
/*     */ 
/*     */   public static boolean register(String configElement, PluginPoint pp, String subTreeRoot, Object statsProvider)
/*     */   {
/*  44 */     return register(pp, configElement, subTreeRoot, statsProvider, null);
/*     */   }
/*     */ 
/*     */   public static boolean register(PluginPoint pp, String configElement, String subTreeRoot, Object statsProvider, String invokerId)
/*     */   {
/*  50 */     StatsProviderInfo spInfo = new StatsProviderInfo(configElement, pp, subTreeRoot, statsProvider, invokerId);
/*     */ 
/*  53 */     return registerStatsProvider(spInfo);
/*     */   }
/*     */ 
/*     */   public static boolean register(String configElement, PluginPoint pp, String subTreeRoot, Object statsProvider, String configLevelStr)
/*     */   {
/*  59 */     return register(configElement, pp, subTreeRoot, statsProvider, configLevelStr, null);
/*     */   }
/*     */ 
/*     */   public static boolean register(String configElement, PluginPoint pp, String subTreeRoot, Object statsProvider, String configLevelStr, String invokerId)
/*     */   {
/*  65 */     StatsProviderInfo spInfo = new StatsProviderInfo(configElement, pp, subTreeRoot, statsProvider, invokerId);
/*     */ 
/*  67 */     spInfo.setConfigLevel(configLevelStr);
/*     */ 
/*  69 */     return registerStatsProvider(spInfo);
/*     */   }
/*     */ 
/*     */   private static boolean registerStatsProvider(StatsProviderInfo spInfo)
/*     */   {
/*  74 */     if (spmd == null)
/*     */     {
/*  76 */       toBeRegistered.add(spInfo);
/*     */     } else {
/*  78 */       spmd.register(spInfo);
/*  79 */       return true;
/*     */     }
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean unregister(Object statsProvider)
/*     */   {
/*  86 */     if (spmd == null) {
/*  87 */       for (StatsProviderInfo spInfo : toBeRegistered)
/*  88 */         if (spInfo.getStatsProvider() == statsProvider) {
/*  89 */           toBeRegistered.remove(spInfo);
/*  90 */           break;
/*     */         }
/*     */     }
/*     */     else
/*     */     {
/*  95 */       spmd.unregister(statsProvider);
/*  96 */       return true;
/*     */     }
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean hasListeners(String probeStr)
/*     */   {
/* 104 */     if (spmd == null) {
/* 105 */       return false;
/*     */     }
/* 107 */     return spmd.hasListeners(probeStr);
/*     */   }
/*     */ 
/*     */   public static void setStatsProviderManagerDelegate(StatsProviderManagerDelegate lspmd)
/*     */   {
/* 115 */     if (lspmd == null)
/*     */     {
/* 117 */       return;
/*     */     }
/*     */ 
/* 121 */     spmd = lspmd;
/*     */ 
/* 126 */     for (StatsProviderInfo spInfo : toBeRegistered) {
/* 127 */       spmd.register(spInfo);
/*     */     }
/*     */ 
/* 131 */     toBeRegistered.clear();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.probe.provider.StatsProviderManager
 * JD-Core Version:    0.6.2
 */