/*     */ package sun.net.dns;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class ResolverConfigurationImpl extends ResolverConfiguration
/*     */ {
/*     */   private static Object lock;
/*     */   private final ResolverConfiguration.Options opts;
/*     */   private static boolean changed;
/*     */   private static long lastRefresh;
/*     */   private static final int TIMEOUT = 120000;
/*     */   private static String os_searchlist;
/*     */   private static String os_nameservers;
/*     */   private static LinkedList searchlist;
/*     */   private static LinkedList nameservers;
/*     */ 
/*     */   private LinkedList<String> stringToList(String paramString)
/*     */   {
/*  66 */     LinkedList localLinkedList = new LinkedList();
/*     */ 
/*  69 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ", ");
/*  70 */     while (localStringTokenizer.hasMoreTokens()) {
/*  71 */       String str = localStringTokenizer.nextToken();
/*  72 */       if (!localLinkedList.contains(str)) {
/*  73 */         localLinkedList.add(str);
/*     */       }
/*     */     }
/*  76 */     return localLinkedList;
/*     */   }
/*     */ 
/*     */   private void loadConfig()
/*     */   {
/*  82 */     assert (Thread.holdsLock(lock));
/*     */ 
/*  87 */     if (changed) {
/*  88 */       changed = false;
/*     */     }
/*  90 */     else if (lastRefresh >= 0L) {
/*  91 */       long l = System.currentTimeMillis();
/*  92 */       if (l - lastRefresh < 120000L) {
/*  93 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 101 */     loadDNSconfig0();
/*     */ 
/* 103 */     lastRefresh = System.currentTimeMillis();
/* 104 */     searchlist = stringToList(os_searchlist);
/* 105 */     nameservers = stringToList(os_nameservers);
/* 106 */     os_searchlist = null;
/* 107 */     os_nameservers = null;
/*     */   }
/*     */ 
/*     */   ResolverConfigurationImpl() {
/* 111 */     this.opts = new OptionsImpl();
/*     */   }
/*     */ 
/*     */   public List<String> searchlist() {
/* 115 */     synchronized (lock) {
/* 116 */       loadConfig();
/*     */ 
/* 119 */       return (List)searchlist.clone();
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<String> nameservers() {
/* 124 */     synchronized (lock) {
/* 125 */       loadConfig();
/*     */ 
/* 128 */       return (List)nameservers.clone();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ResolverConfiguration.Options options() {
/* 133 */     return this.opts;
/*     */   }
/*     */ 
/*     */   static native void init0();
/*     */ 
/*     */   static native void loadDNSconfig0();
/*     */ 
/*     */   static native int notifyAddrChange0();
/*     */ 
/*     */   static
/*     */   {
/*  40 */     lock = new Object();
/*     */ 
/*  46 */     changed = false;
/*     */ 
/*  49 */     lastRefresh = -1L;
/*     */ 
/* 161 */     AccessController.doPrivileged(new LoadLibraryAction("net"));
/*     */ 
/* 163 */     init0();
/*     */ 
/* 166 */     AddressChangeListener localAddressChangeListener = new AddressChangeListener();
/* 167 */     localAddressChangeListener.setDaemon(true);
/* 168 */     localAddressChangeListener.start();
/*     */   }
/*     */ 
/*     */   static class AddressChangeListener extends Thread
/*     */   {
/*     */     public void run()
/*     */     {
/*     */       while (true)
/*     */       {
/* 142 */         if (ResolverConfigurationImpl.notifyAddrChange0() != 0)
/* 143 */           return;
/* 144 */         synchronized (ResolverConfigurationImpl.lock) {
/* 145 */           ResolverConfigurationImpl.access$102(true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.dns.ResolverConfigurationImpl
 * JD-Core Version:    0.6.2
 */