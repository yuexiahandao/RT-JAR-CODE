/*     */ package javax.print;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public abstract class StreamPrintServiceFactory
/*     */ {
/*     */   private static Services getServices()
/*     */   {
/*  66 */     Services localServices = (Services)AppContext.getAppContext().get(Services.class);
/*     */ 
/*  68 */     if (localServices == null) {
/*  69 */       localServices = new Services();
/*  70 */       AppContext.getAppContext().put(Services.class, localServices);
/*     */     }
/*  72 */     return localServices;
/*     */   }
/*     */ 
/*     */   private static ArrayList getListOfFactories() {
/*  76 */     return getServices().listOfFactories;
/*     */   }
/*     */ 
/*     */   private static ArrayList initListOfFactories() {
/*  80 */     ArrayList localArrayList = new ArrayList();
/*  81 */     getServices().listOfFactories = localArrayList;
/*  82 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static StreamPrintServiceFactory[] lookupStreamPrintServiceFactories(DocFlavor paramDocFlavor, String paramString)
/*     */   {
/* 111 */     ArrayList localArrayList = getFactories(paramDocFlavor, paramString);
/* 112 */     return (StreamPrintServiceFactory[])localArrayList.toArray(new StreamPrintServiceFactory[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public abstract String getOutputFormat();
/*     */ 
/*     */   public abstract DocFlavor[] getSupportedDocFlavors();
/*     */ 
/*     */   public abstract StreamPrintService getPrintService(OutputStream paramOutputStream);
/*     */ 
/*     */   private static ArrayList getAllFactories()
/*     */   {
/* 166 */     synchronized (StreamPrintServiceFactory.class)
/*     */     {
/* 168 */       ArrayList localArrayList = getListOfFactories();
/* 169 */       if (localArrayList != null) {
/* 170 */         return localArrayList;
/*     */       }
/* 172 */       localArrayList = initListOfFactories();
/*     */       try
/*     */       {
/* 176 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Object run() {
/* 179 */             Iterator localIterator = ServiceLoader.load(StreamPrintServiceFactory.class).iterator();
/*     */ 
/* 182 */             ArrayList localArrayList = StreamPrintServiceFactory.access$100();
/* 183 */             while (localIterator.hasNext()) {
/*     */               try {
/* 185 */                 localArrayList.add(localIterator.next());
/*     */               }
/*     */               catch (ServiceConfigurationError localServiceConfigurationError) {
/* 188 */                 if (System.getSecurityManager() != null)
/* 189 */                   localServiceConfigurationError.printStackTrace();
/*     */                 else {
/* 191 */                   throw localServiceConfigurationError;
/*     */                 }
/*     */               }
/*     */             }
/* 195 */             return null;
/*     */           } } );
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException) {
/*     */       }
/* 200 */       return localArrayList;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isMember(DocFlavor paramDocFlavor, DocFlavor[] paramArrayOfDocFlavor) {
/* 205 */     for (int i = 0; i < paramArrayOfDocFlavor.length; i++) {
/* 206 */       if (paramDocFlavor.equals(paramArrayOfDocFlavor[i])) {
/* 207 */         return true;
/*     */       }
/*     */     }
/* 210 */     return false;
/*     */   }
/*     */ 
/*     */   private static ArrayList getFactories(DocFlavor paramDocFlavor, String paramString)
/*     */   {
/* 215 */     if ((paramDocFlavor == null) && (paramString == null)) {
/* 216 */       return getAllFactories();
/*     */     }
/*     */ 
/* 219 */     ArrayList localArrayList = new ArrayList();
/* 220 */     Iterator localIterator = getAllFactories().iterator();
/* 221 */     while (localIterator.hasNext()) {
/* 222 */       StreamPrintServiceFactory localStreamPrintServiceFactory = (StreamPrintServiceFactory)localIterator.next();
/*     */ 
/* 224 */       if (((paramString == null) || (paramString.equalsIgnoreCase(localStreamPrintServiceFactory.getOutputFormat()))) && ((paramDocFlavor == null) || (isMember(paramDocFlavor, localStreamPrintServiceFactory.getSupportedDocFlavors()))))
/*     */       {
/* 228 */         localArrayList.add(localStreamPrintServiceFactory);
/*     */       }
/*     */     }
/*     */ 
/* 232 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   static class Services
/*     */   {
/*  62 */     private ArrayList listOfFactories = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.StreamPrintServiceFactory
 * JD-Core Version:    0.6.2
 */