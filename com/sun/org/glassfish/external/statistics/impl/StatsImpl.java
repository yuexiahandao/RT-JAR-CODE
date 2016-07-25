/*    */ package com.sun.org.glassfish.external.statistics.impl;
/*    */ 
/*    */ import com.sun.org.glassfish.external.statistics.Statistic;
/*    */ import com.sun.org.glassfish.external.statistics.Stats;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public final class StatsImpl
/*    */   implements Stats
/*    */ {
/*    */   private final StatisticImpl[] statArray;
/*    */ 
/*    */   protected StatsImpl(StatisticImpl[] statisticArray)
/*    */   {
/* 42 */     this.statArray = statisticArray;
/*    */   }
/*    */ 
/*    */   public synchronized Statistic getStatistic(String statisticName) {
/* 46 */     Statistic stat = null;
/* 47 */     for (Statistic s : this.statArray) {
/* 48 */       if (s.getName().equals(statisticName)) {
/* 49 */         stat = s;
/* 50 */         break;
/*    */       }
/*    */     }
/* 53 */     return stat;
/*    */   }
/*    */ 
/*    */   public synchronized String[] getStatisticNames() {
/* 57 */     ArrayList list = new ArrayList();
/* 58 */     for (Statistic s : this.statArray) {
/* 59 */       list.add(s.getName());
/*    */     }
/* 61 */     String[] strArray = new String[list.size()];
/* 62 */     return (String[])list.toArray(strArray);
/*    */   }
/*    */ 
/*    */   public synchronized Statistic[] getStatistics() {
/* 66 */     return this.statArray;
/*    */   }
/*    */ 
/*    */   public synchronized void reset()
/*    */   {
/* 73 */     for (StatisticImpl s : this.statArray)
/* 74 */       s.reset();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.statistics.impl.StatsImpl
 * JD-Core Version:    0.6.2
 */