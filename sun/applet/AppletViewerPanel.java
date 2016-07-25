/*     */ package sun.applet;
/*     */ 
/*     */ import java.applet.AppletContext;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ class AppletViewerPanel extends AppletPanel
/*     */ {
/*  48 */   static boolean debug = false;
/*     */   URL documentURL;
/*     */   URL baseURL;
/*     */   Hashtable atts;
/*     */   private static final long serialVersionUID = 8890989370785545619L;
/*     */ 
/*     */   AppletViewerPanel(URL paramURL, Hashtable paramHashtable)
/*     */   {
/*  74 */     this.documentURL = paramURL;
/*  75 */     this.atts = paramHashtable;
/*     */ 
/*  77 */     String str1 = getParameter("codebase");
/*  78 */     if (str1 != null) {
/*  79 */       if (!str1.endsWith("/"))
/*  80 */         str1 = str1 + "/";
/*     */       try
/*     */       {
/*  83 */         this.baseURL = new URL(paramURL, str1);
/*     */       } catch (MalformedURLException localMalformedURLException1) {
/*     */       }
/*     */     }
/*  87 */     if (this.baseURL == null) {
/*  88 */       String str2 = paramURL.getFile();
/*  89 */       int i = str2.lastIndexOf('/');
/*  90 */       if ((i >= 0) && (i < str2.length() - 1)) {
/*     */         try {
/*  92 */           this.baseURL = new URL(paramURL, str2.substring(0, i + 1));
/*     */         }
/*     */         catch (MalformedURLException localMalformedURLException2)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*  99 */     if (this.baseURL == null)
/* 100 */       this.baseURL = paramURL;
/*     */   }
/*     */ 
/*     */   public String getParameter(String paramString)
/*     */   {
/* 109 */     return (String)this.atts.get(paramString.toLowerCase());
/*     */   }
/*     */ 
/*     */   public URL getDocumentBase()
/*     */   {
/* 116 */     return this.documentURL;
/*     */   }
/*     */ 
/*     */   public URL getCodeBase()
/*     */   {
/* 124 */     return this.baseURL;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 131 */     String str = getParameter("width");
/* 132 */     if (str != null) {
/* 133 */       return Integer.valueOf(str).intValue();
/*     */     }
/* 135 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 143 */     String str = getParameter("height");
/* 144 */     if (str != null) {
/* 145 */       return Integer.valueOf(str).intValue();
/*     */     }
/* 147 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean hasInitialFocus()
/*     */   {
/* 159 */     if ((isJDK11Applet()) || (isJDK12Applet())) {
/* 160 */       return false;
/*     */     }
/* 162 */     String str = getParameter("initial_focus");
/*     */ 
/* 164 */     if (str != null)
/*     */     {
/* 166 */       if (str.toLowerCase().equals("false")) {
/* 167 */         return false;
/*     */       }
/*     */     }
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */   public String getCode()
/*     */   {
/* 177 */     return getParameter("code");
/*     */   }
/*     */ 
/*     */   public String getJarFiles()
/*     */   {
/* 186 */     return getParameter("archive");
/*     */   }
/*     */ 
/*     */   public String getSerializedObject()
/*     */   {
/* 193 */     return getParameter("object");
/*     */   }
/*     */ 
/*     */   public AppletContext getAppletContext()
/*     */   {
/* 202 */     return (AppletContext)getParent();
/*     */   }
/*     */ 
/*     */   static void debug(String paramString) {
/* 206 */     if (debug)
/* 207 */       System.err.println("AppletViewerPanel:::" + paramString);
/*     */   }
/*     */ 
/*     */   static void debug(String paramString, Throwable paramThrowable) {
/* 211 */     if (debug) {
/* 212 */       paramThrowable.printStackTrace();
/* 213 */       debug(paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletViewerPanel
 * JD-Core Version:    0.6.2
 */