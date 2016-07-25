/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public final class CGIHandler
/*     */ {
/*     */   static int ContentLength;
/*     */   static String QueryString;
/*     */   static String RequestMethod;
/*     */   static String ServerName;
/*     */   static int ServerPort;
/*     */   private static CGICommandHandler[] commands;
/*     */   private static Hashtable<String, CGICommandHandler> commandLookup;
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*     */     try
/*     */     {
/* 136 */       int i = QueryString.indexOf("=");
/*     */       String str1;
/*     */       String str2;
/* 137 */       if (i == -1) {
/* 138 */         str1 = QueryString;
/* 139 */         str2 = "";
/*     */       }
/*     */       else {
/* 142 */         str1 = QueryString.substring(0, i);
/* 143 */         str2 = QueryString.substring(i + 1);
/*     */       }
/* 145 */       CGICommandHandler localCGICommandHandler = (CGICommandHandler)commandLookup.get(str1);
/*     */ 
/* 147 */       if (localCGICommandHandler != null)
/*     */         try {
/* 149 */           localCGICommandHandler.execute(str2);
/*     */         } catch (CGIClientException localCGIClientException) {
/* 151 */           returnClientError(localCGIClientException.getMessage());
/*     */         } catch (CGIServerException localCGIServerException) {
/* 153 */           returnServerError(localCGIServerException.getMessage());
/*     */         }
/*     */       else
/* 156 */         returnClientError("invalid command.");
/*     */     } catch (Exception localException) {
/* 158 */       returnServerError("internal error: " + localException.getMessage());
/*     */     }
/* 160 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   private static void returnClientError(String paramString)
/*     */   {
/* 169 */     System.out.println("Status: 400 Bad Request: " + paramString);
/* 170 */     System.out.println("Content-type: text/html");
/* 171 */     System.out.println("");
/* 172 */     System.out.println("<HTML><HEAD><TITLE>Java RMI Client Error</TITLE></HEAD><BODY>");
/*     */ 
/* 176 */     System.out.println("<H1>Java RMI Client Error</H1>");
/* 177 */     System.out.println("");
/* 178 */     System.out.println(paramString);
/* 179 */     System.out.println("</BODY></HTML>");
/* 180 */     System.exit(1);
/*     */   }
/*     */ 
/*     */   private static void returnServerError(String paramString)
/*     */   {
/* 189 */     System.out.println("Status: 500 Server Error: " + paramString);
/* 190 */     System.out.println("Content-type: text/html");
/* 191 */     System.out.println("");
/* 192 */     System.out.println("<HTML><HEAD><TITLE>Java RMI Server Error</TITLE></HEAD><BODY>");
/*     */ 
/* 196 */     System.out.println("<H1>Java RMI Server Error</H1>");
/* 197 */     System.out.println("");
/* 198 */     System.out.println(paramString);
/* 199 */     System.out.println("</BODY></HTML>");
/* 200 */     System.exit(1);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  94 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/*  97 */         CGIHandler.ContentLength = Integer.getInteger("CONTENT_LENGTH", 0).intValue();
/*     */ 
/*  99 */         CGIHandler.QueryString = System.getProperty("QUERY_STRING", "");
/* 100 */         CGIHandler.RequestMethod = System.getProperty("REQUEST_METHOD", "");
/* 101 */         CGIHandler.ServerName = System.getProperty("SERVER_NAME", "");
/* 102 */         CGIHandler.ServerPort = Integer.getInteger("SERVER_PORT", 0).intValue();
/* 103 */         return null;
/*     */       }
/*     */     });
/* 109 */     commands = new CGICommandHandler[] { new CGIForwardCommand(), new CGIGethostnameCommand(), new CGIPingCommand(), new CGITryHostnameCommand() };
/*     */ 
/* 119 */     commandLookup = new Hashtable();
/* 120 */     for (int i = 0; i < commands.length; i++)
/* 121 */       commandLookup.put(commands[i].getName(), commands[i]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.CGIHandler
 * JD-Core Version:    0.6.2
 */