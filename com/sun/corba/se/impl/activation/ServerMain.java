/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.spi.activation.Activator;
/*     */ import com.sun.corba.se.spi.activation.ActivatorHelper;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ public class ServerMain
/*     */ {
/*     */   public static final int OK = 0;
/*     */   public static final int MAIN_CLASS_NOT_FOUND = 1;
/*     */   public static final int NO_MAIN_METHOD = 2;
/*     */   public static final int APPLICATION_ERROR = 3;
/*     */   public static final int UNKNOWN_ERROR = 4;
/*     */   public static final int NO_SERVER_ID = 5;
/*     */   public static final int REGISTRATION_FAILED = 6;
/*     */   private static final boolean debug = false;
/*     */ 
/*     */   public static String printResult(int paramInt)
/*     */   {
/*  65 */     switch (paramInt) { case 0:
/*  66 */       return "Server terminated normally";
/*     */     case 1:
/*  67 */       return "main class not found";
/*     */     case 2:
/*  68 */       return "no main method";
/*     */     case 3:
/*  69 */       return "application error";
/*     */     case 5:
/*  70 */       return "server ID not defined";
/*     */     case 6:
/*  71 */       return "server registration failed";
/*  72 */     case 4: } return "unknown error";
/*     */   }
/*     */ 
/*     */   private void redirectIOStreams()
/*     */   {
/*     */     try
/*     */     {
/*  80 */       String str1 = System.getProperty("com.sun.CORBA.activation.DbDir") + System.getProperty("file.separator") + "logs" + System.getProperty("file.separator");
/*     */ 
/*  86 */       File localFile = new File(str1);
/*  87 */       String str2 = System.getProperty("com.sun.CORBA.POA.ORBServerId");
/*     */ 
/*  90 */       FileOutputStream localFileOutputStream1 = new FileOutputStream(str1 + str2 + ".out", true);
/*     */ 
/*  92 */       FileOutputStream localFileOutputStream2 = new FileOutputStream(str1 + str2 + ".err", true);
/*     */ 
/*  95 */       PrintStream localPrintStream1 = new PrintStream(localFileOutputStream1, true);
/*  96 */       PrintStream localPrintStream2 = new PrintStream(localFileOutputStream2, true);
/*     */ 
/*  98 */       System.setOut(localPrintStream1);
/*  99 */       System.setErr(localPrintStream2);
/*     */ 
/* 101 */       logInformation("Server started");
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void writeLogMessage(PrintStream paramPrintStream, String paramString)
/*     */   {
/* 110 */     Date localDate = new Date();
/* 111 */     paramPrintStream.print("[" + localDate.toString() + "] " + paramString + "\n");
/*     */   }
/*     */ 
/*     */   public static void logInformation(String paramString)
/*     */   {
/* 118 */     writeLogMessage(System.out, "        " + paramString);
/*     */   }
/*     */ 
/*     */   public static void logError(String paramString)
/*     */   {
/* 125 */     writeLogMessage(System.out, "ERROR:  " + paramString);
/* 126 */     writeLogMessage(System.err, "ERROR:  " + paramString);
/*     */   }
/*     */ 
/*     */   public static void logTerminal(String paramString, int paramInt)
/*     */   {
/* 136 */     if (paramInt == 0) {
/* 137 */       writeLogMessage(System.out, "        " + paramString);
/*     */     } else {
/* 139 */       writeLogMessage(System.out, "FATAL:  " + printResult(paramInt) + ": " + paramString);
/*     */ 
/* 142 */       writeLogMessage(System.err, "FATAL:  " + printResult(paramInt) + ": " + paramString);
/*     */     }
/*     */ 
/* 146 */     System.exit(paramInt);
/*     */   }
/*     */ 
/*     */   private Method getMainMethod(Class paramClass)
/*     */   {
/* 151 */     Class[] arrayOfClass = { [Ljava.lang.String.class };
/* 152 */     Method localMethod = null;
/*     */     try
/*     */     {
/* 155 */       localMethod = paramClass.getDeclaredMethod("main", arrayOfClass);
/*     */     } catch (Exception localException) {
/* 157 */       logTerminal(localException.getMessage(), 2);
/*     */     }
/*     */ 
/* 160 */     if (!isPublicStaticVoid(localMethod)) {
/* 161 */       logTerminal("", 2);
/*     */     }
/* 163 */     return localMethod;
/*     */   }
/*     */ 
/*     */   private boolean isPublicStaticVoid(Method paramMethod)
/*     */   {
/* 169 */     int i = paramMethod.getModifiers();
/* 170 */     if ((!Modifier.isPublic(i)) || (!Modifier.isStatic(i))) {
/* 171 */       logError(paramMethod.getName() + " is not public static");
/* 172 */       return false;
/*     */     }
/*     */ 
/* 176 */     if (paramMethod.getExceptionTypes().length != 0) {
/* 177 */       logError(paramMethod.getName() + " declares exceptions");
/* 178 */       return false;
/*     */     }
/*     */ 
/* 181 */     if (!paramMethod.getReturnType().equals(Void.TYPE)) {
/* 182 */       logError(paramMethod.getName() + " does not have a void return type");
/* 183 */       return false;
/*     */     }
/*     */ 
/* 186 */     return true;
/*     */   }
/*     */ 
/*     */   private Method getNamedMethod(Class paramClass, String paramString)
/*     */   {
/* 191 */     Class[] arrayOfClass = { ORB.class };
/* 192 */     Method localMethod = null;
/*     */     try
/*     */     {
/* 195 */       localMethod = paramClass.getDeclaredMethod(paramString, arrayOfClass);
/*     */     } catch (Exception localException) {
/* 197 */       return null;
/*     */     }
/*     */ 
/* 200 */     if (!isPublicStaticVoid(localMethod)) {
/* 201 */       return null;
/*     */     }
/* 203 */     return localMethod;
/*     */   }
/*     */ 
/*     */   private void run(String[] paramArrayOfString)
/*     */   {
/*     */     try {
/* 209 */       redirectIOStreams();
/*     */ 
/* 211 */       String str = System.getProperty("com.sun.CORBA.POA.ORBServerName");
/*     */ 
/* 217 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 219 */       if (localClassLoader == null) {
/* 220 */         localClassLoader = ClassLoader.getSystemClassLoader();
/*     */       }
/*     */ 
/* 223 */       Class localClass = null;
/*     */       try
/*     */       {
/* 227 */         localClass = Class.forName(str);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException2) {
/* 230 */         localClass = Class.forName(str, true, localClassLoader);
/*     */       }
/*     */ 
/* 237 */       Method localMethod = getMainMethod(localClass);
/*     */ 
/* 244 */       boolean bool = Boolean.getBoolean("com.sun.CORBA.activation.ORBServerVerify");
/*     */ 
/* 246 */       if (bool) {
/* 247 */         if (localMethod == null) {
/* 248 */           logTerminal("", 2);
/*     */         }
/*     */         else
/*     */         {
/* 252 */           logTerminal("", 0);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 257 */       registerCallback(localClass);
/*     */ 
/* 260 */       Object[] arrayOfObject = new Object[1];
/* 261 */       arrayOfObject[0] = paramArrayOfString;
/* 262 */       localMethod.invoke(null, arrayOfObject);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1) {
/* 265 */       logTerminal("ClassNotFound exception: " + localClassNotFoundException1.getMessage(), 1);
/*     */     }
/*     */     catch (Exception localException) {
/* 268 */       logTerminal("Exception: " + localException.getMessage(), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 274 */     ServerMain localServerMain = new ServerMain();
/* 275 */     localServerMain.run(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   private int getServerId()
/*     */   {
/* 282 */     Integer localInteger = Integer.getInteger("com.sun.CORBA.POA.ORBServerId");
/*     */ 
/* 284 */     if (localInteger == null) {
/* 285 */       logTerminal("", 5);
/*     */     }
/* 287 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   private void registerCallback(Class paramClass)
/*     */   {
/* 292 */     Method localMethod1 = getNamedMethod(paramClass, "install");
/* 293 */     Method localMethod2 = getNamedMethod(paramClass, "uninstall");
/* 294 */     Method localMethod3 = getNamedMethod(paramClass, "shutdown");
/*     */ 
/* 296 */     Properties localProperties = new Properties();
/* 297 */     localProperties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
/*     */ 
/* 301 */     localProperties.put("com.sun.CORBA.POA.ORBActivated", "false");
/* 302 */     String[] arrayOfString = null;
/* 303 */     ORB localORB = ORB.init(arrayOfString, localProperties);
/*     */ 
/* 305 */     ServerCallback localServerCallback = new ServerCallback(localORB, localMethod1, localMethod2, localMethod3);
/*     */ 
/* 308 */     int i = getServerId();
/*     */     try
/*     */     {
/* 311 */       Activator localActivator = ActivatorHelper.narrow(localORB.resolve_initial_references("ServerActivator"));
/*     */ 
/* 313 */       localActivator.active(i, localServerCallback);
/*     */     } catch (Exception localException) {
/* 315 */       logTerminal("exception " + localException.getMessage(), 6);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ServerMain
 * JD-Core Version:    0.6.2
 */