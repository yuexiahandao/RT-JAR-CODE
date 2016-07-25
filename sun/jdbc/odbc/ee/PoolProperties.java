/*    */ package sun.jdbc.odbc.ee;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class PoolProperties
/*    */ {
/*    */   public static final String MINPOOLSIZE = "minPoolSize";
/*    */   public static final String MAXPOOLSIZE = "maxPoolSize";
/*    */   public static final String INITIALPOOLSIZE = "initialPoolSize";
/*    */   public static final String MAXIDLETIME = "maxIdleTime";
/*    */   public static final String TIMEOUTFROMPOOL = "timeOutFromPool";
/*    */   public static final String MAINTENANCEINTERVAL = "mInterval";
/* 37 */   private Hashtable properties = new Hashtable();
/*    */ 
/*    */   public PoolProperties()
/*    */   {
/* 43 */     this.properties.put("minPoolSize", new Integer(0));
/* 44 */     this.properties.put("maxPoolSize", new Integer(0));
/* 45 */     this.properties.put("initialPoolSize", new Integer(0));
/* 46 */     this.properties.put("maxIdleTime", new Integer(0));
/* 47 */     this.properties.put("timeOutFromPool", new Integer(0));
/* 48 */     this.properties.put("mInterval", new Integer(0));
/*    */   }
/*    */ 
/*    */   public int get(String paramString)
/*    */   {
/* 57 */     return ((Integer)this.properties.get(paramString)).intValue();
/*    */   }
/*    */ 
/*    */   public void set(String paramString, int paramInt)
/*    */   {
/* 67 */     this.properties.put(paramString, new Integer(paramInt));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.PoolProperties
 * JD-Core Version:    0.6.2
 */