/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.peer.DesktopPeer;
/*     */ import java.io.File;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.DesktopBrowse;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class Desktop
/*     */ {
/*     */   private DesktopPeer peer;
/*     */ 
/*     */   private Desktop()
/*     */   {
/* 125 */     this.peer = Toolkit.getDefaultToolkit().createDesktopPeer(this);
/*     */   }
/*     */ 
/*     */   public static synchronized Desktop getDesktop()
/*     */   {
/* 142 */     if (GraphicsEnvironment.isHeadless()) throw new HeadlessException();
/* 143 */     if (!isDesktopSupported()) {
/* 144 */       throw new UnsupportedOperationException("Desktop API is not supported on the current platform");
/*     */     }
/*     */ 
/* 148 */     AppContext localAppContext = AppContext.getAppContext();
/* 149 */     Desktop localDesktop = (Desktop)localAppContext.get(Desktop.class);
/*     */ 
/* 151 */     if (localDesktop == null) {
/* 152 */       localDesktop = new Desktop();
/* 153 */       localAppContext.put(Desktop.class, localDesktop);
/*     */     }
/*     */ 
/* 156 */     return localDesktop;
/*     */   }
/*     */ 
/*     */   public static boolean isDesktopSupported()
/*     */   {
/* 169 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 170 */     if ((localToolkit instanceof SunToolkit)) {
/* 171 */       return ((SunToolkit)localToolkit).isDesktopSupported();
/*     */     }
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isSupported(Action paramAction)
/*     */   {
/* 193 */     return this.peer.isSupported(paramAction);
/*     */   }
/*     */ 
/*     */   private static void checkFileValidation(File paramFile)
/*     */   {
/* 206 */     if (paramFile == null) throw new NullPointerException("File must not be null");
/*     */ 
/* 208 */     if (!paramFile.exists()) {
/* 209 */       throw new IllegalArgumentException("The file: " + paramFile.getPath() + " doesn't exist.");
/*     */     }
/*     */ 
/* 213 */     paramFile.canRead();
/*     */   }
/*     */ 
/*     */   private void checkActionSupport(Action paramAction)
/*     */   {
/* 224 */     if (!isSupported(paramAction))
/* 225 */       throw new UnsupportedOperationException("The " + paramAction.name() + " action is not supported on the current platform!");
/*     */   }
/*     */ 
/*     */   private void checkAWTPermission()
/*     */   {
/* 237 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 238 */     if (localSecurityManager != null)
/* 239 */       localSecurityManager.checkPermission(new AWTPermission("showWindowWithoutWarningBanner"));
/*     */   }
/*     */ 
/*     */   public void open(File paramFile)
/*     */     throws IOException
/*     */   {
/* 267 */     checkAWTPermission();
/* 268 */     checkExec();
/* 269 */     checkActionSupport(Action.OPEN);
/* 270 */     checkFileValidation(paramFile);
/*     */ 
/* 272 */     this.peer.open(paramFile);
/*     */   }
/*     */ 
/*     */   public void edit(File paramFile)
/*     */     throws IOException
/*     */   {
/* 298 */     checkAWTPermission();
/* 299 */     checkExec();
/* 300 */     checkActionSupport(Action.EDIT);
/* 301 */     paramFile.canWrite();
/* 302 */     checkFileValidation(paramFile);
/*     */ 
/* 304 */     this.peer.edit(paramFile);
/*     */   }
/*     */ 
/*     */   public void print(File paramFile)
/*     */     throws IOException
/*     */   {
/* 328 */     checkExec();
/* 329 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 330 */     if (localSecurityManager != null) {
/* 331 */       localSecurityManager.checkPrintJobAccess();
/*     */     }
/* 333 */     checkActionSupport(Action.PRINT);
/* 334 */     checkFileValidation(paramFile);
/*     */ 
/* 336 */     this.peer.print(paramFile);
/*     */   }
/*     */ 
/*     */   public void browse(URI paramURI)
/*     */     throws IOException
/*     */   {
/* 374 */     Object localObject = null;
/*     */     try {
/* 376 */       checkAWTPermission();
/* 377 */       checkExec();
/*     */     } catch (SecurityException localSecurityException) {
/* 379 */       localObject = localSecurityException;
/*     */     }
/* 381 */     checkActionSupport(Action.BROWSE);
/* 382 */     if (paramURI == null) {
/* 383 */       throw new NullPointerException();
/*     */     }
/* 385 */     if (localObject == null) {
/* 386 */       this.peer.browse(paramURI);
/* 387 */       return;
/*     */     }
/*     */ 
/* 393 */     URL localURL = null;
/*     */     try {
/* 395 */       localURL = paramURI.toURL();
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 397 */       throw new IllegalArgumentException("Unable to convert URI to URL", localMalformedURLException);
/*     */     }
/* 399 */     DesktopBrowse localDesktopBrowse = DesktopBrowse.getInstance();
/* 400 */     if (localDesktopBrowse == null)
/*     */     {
/* 402 */       throw localObject;
/*     */     }
/* 404 */     localDesktopBrowse.browse(localURL);
/*     */   }
/*     */ 
/*     */   public void mail()
/*     */     throws IOException
/*     */   {
/* 423 */     checkAWTPermission();
/* 424 */     checkExec();
/* 425 */     checkActionSupport(Action.MAIL);
/* 426 */     URI localURI = null;
/*     */     try {
/* 428 */       localURI = new URI("mailto:?");
/* 429 */       this.peer.mail(localURI);
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mail(URI paramURI)
/*     */     throws IOException
/*     */   {
/* 465 */     checkAWTPermission();
/* 466 */     checkExec();
/* 467 */     checkActionSupport(Action.MAIL);
/* 468 */     if (paramURI == null) throw new NullPointerException();
/*     */ 
/* 470 */     if (!"mailto".equalsIgnoreCase(paramURI.getScheme())) {
/* 471 */       throw new IllegalArgumentException("URI scheme is not \"mailto\"");
/*     */     }
/*     */ 
/* 474 */     this.peer.mail(paramURI);
/*     */   }
/*     */ 
/*     */   private void checkExec() throws SecurityException {
/* 478 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 479 */     if (localSecurityManager != null)
/* 480 */       localSecurityManager.checkPermission(new FilePermission("<<ALL FILES>>", "execute"));
/*     */   }
/*     */ 
/*     */   public static enum Action
/*     */   {
/*  95 */     OPEN, 
/*     */ 
/* 100 */     EDIT, 
/*     */ 
/* 105 */     PRINT, 
/*     */ 
/* 111 */     MAIL, 
/*     */ 
/* 116 */     BROWSE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Desktop
 * JD-Core Version:    0.6.2
 */