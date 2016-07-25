/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.util.Collection;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class SCDynamicStoreConfig
/*     */ {
/*     */   private static native void installNotificationCallback();
/*     */ 
/*     */   private static native Hashtable<String, Object> getKerberosConfig();
/*     */ 
/*     */   private static Vector<String> unwrapHost(Collection<Hashtable<String, String>> paramCollection)
/*     */   {
/*  44 */     Vector localVector = new Vector();
/*  45 */     for (Hashtable localHashtable : paramCollection) {
/*  46 */       localVector.add(localHashtable.get("host"));
/*     */     }
/*  48 */     return localVector;
/*     */   }
/*     */ 
/*     */   private static Hashtable<String, Object> convertRealmConfigs(Hashtable<String, ?> paramHashtable)
/*     */   {
/*  58 */     Hashtable localHashtable1 = new Hashtable();
/*     */ 
/*  60 */     for (String str : paramHashtable.keySet())
/*     */     {
/*  62 */       Hashtable localHashtable2 = (Hashtable)paramHashtable.get(str);
/*  63 */       Collection localCollection1 = (Collection)localHashtable2.get("kdc");
/*     */ 
/*  66 */       Hashtable localHashtable3 = new Hashtable();
/*  67 */       if (localCollection1 != null) localHashtable3.put("kdc", unwrapHost(localCollection1));
/*     */ 
/*  70 */       Collection localCollection2 = (Collection)localHashtable2.get("kadmin");
/*  71 */       if (localCollection2 != null) localHashtable3.put("admin_server", unwrapHost(localCollection2));
/*     */ 
/*  74 */       localHashtable1.put(str, localHashtable3);
/*     */     }
/*     */ 
/*  77 */     return localHashtable1;
/*     */   }
/*     */ 
/*     */   public static Hashtable<String, Object> getConfig()
/*     */     throws IOException
/*     */   {
/*  89 */     Hashtable localHashtable1 = getKerberosConfig();
/*  90 */     if (localHashtable1 == null) {
/*  91 */       throw new IOException("Could not load configuration from SCDynamicStore");
/*     */     }
/*     */ 
/*  96 */     Hashtable localHashtable2 = (Hashtable)localHashtable1.get("realms");
/*  97 */     if (localHashtable2 != null) {
/*  98 */       localHashtable1.remove("realms");
/*  99 */       Hashtable localHashtable3 = convertRealmConfigs(localHashtable2);
/* 100 */       localHashtable1.put("realms", localHashtable3);
/*     */     }
/*     */ 
/* 104 */     return localHashtable1;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  39 */     AccessController.doPrivileged(new LoadLibraryAction("osx"));
/*  40 */     installNotificationCallback();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.SCDynamicStoreConfig
 * JD-Core Version:    0.6.2
 */