/*     */ package javax.activation;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ public abstract class CommandMap
/*     */ {
/*  43 */   private static CommandMap defaultCommandMap = null;
/*  44 */   private static Map<ClassLoader, CommandMap> map = new WeakHashMap();
/*     */ 
/*     */   public static synchronized CommandMap getDefaultCommandMap()
/*     */   {
/*  65 */     if (defaultCommandMap != null) {
/*  66 */       return defaultCommandMap;
/*     */     }
/*     */ 
/*  69 */     ClassLoader tccl = SecuritySupport.getContextClassLoader();
/*  70 */     CommandMap def = (CommandMap)map.get(tccl);
/*  71 */     if (def == null) {
/*  72 */       def = new MailcapCommandMap();
/*  73 */       map.put(tccl, def);
/*     */     }
/*  75 */     return def;
/*     */   }
/*     */ 
/*     */   public static synchronized void setDefaultCommandMap(CommandMap commandMap)
/*     */   {
/*  87 */     SecurityManager security = System.getSecurityManager();
/*  88 */     if (security != null) {
/*     */       try
/*     */       {
/*  91 */         security.checkSetFactory();
/*     */       }
/*     */       catch (SecurityException ex)
/*     */       {
/*  96 */         if ((CommandMap.class.getClassLoader() == null) || (CommandMap.class.getClassLoader() != commandMap.getClass().getClassLoader()))
/*     */         {
/*  99 */           throw ex;
/*     */         }
/*     */       }
/*     */     }
/* 103 */     map.remove(SecuritySupport.getContextClassLoader());
/* 104 */     defaultCommandMap = commandMap;
/*     */   }
/*     */ 
/*     */   public abstract CommandInfo[] getPreferredCommands(String paramString);
/*     */ 
/*     */   public CommandInfo[] getPreferredCommands(String mimeType, DataSource ds)
/*     */   {
/* 132 */     return getPreferredCommands(mimeType);
/*     */   }
/*     */ 
/*     */   public abstract CommandInfo[] getAllCommands(String paramString);
/*     */ 
/*     */   public CommandInfo[] getAllCommands(String mimeType, DataSource ds)
/*     */   {
/* 160 */     return getAllCommands(mimeType);
/*     */   }
/*     */ 
/*     */   public abstract CommandInfo getCommand(String paramString1, String paramString2);
/*     */ 
/*     */   public CommandInfo getCommand(String mimeType, String cmdName, DataSource ds)
/*     */   {
/* 189 */     return getCommand(mimeType, cmdName);
/*     */   }
/*     */ 
/*     */   public abstract DataContentHandler createDataContentHandler(String paramString);
/*     */ 
/*     */   public DataContentHandler createDataContentHandler(String mimeType, DataSource ds)
/*     */   {
/* 221 */     return createDataContentHandler(mimeType);
/*     */   }
/*     */ 
/*     */   public String[] getMimeTypes()
/*     */   {
/* 233 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.CommandMap
 * JD-Core Version:    0.6.2
 */