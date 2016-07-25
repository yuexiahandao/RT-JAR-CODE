/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Repository;
/*     */ import com.sun.corba.se.spi.activation.RepositoryHelper;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ public class ServerTool
/*     */ {
/*     */   static final String helpCommand = "help";
/*     */   static final String toolName = "servertool";
/*     */   static final String commandArg = "-cmd";
/*     */   private static final boolean debug = false;
/* 230 */   ORB orb = null;
/*     */ 
/* 236 */   static Vector handlers = new Vector();
/*     */   static int maxNameLen;
/*     */ 
/*     */   static int getServerIdForAlias(ORB paramORB, String paramString)
/*     */     throws ServerNotRegistered
/*     */   {
/*     */     try
/*     */     {
/*  60 */       Repository localRepository = RepositoryHelper.narrow(paramORB.resolve_initial_references("ServerRepository"));
/*     */ 
/*  62 */       int i = localRepository.getServerID(paramString);
/*     */ 
/*  64 */       return localRepository.getServerID(paramString); } catch (Exception localException) {
/*     */     }
/*  66 */     throw new ServerNotRegistered();
/*     */   }
/*     */ 
/*     */   void run(String[] paramArrayOfString)
/*     */   {
/*  72 */     String[] arrayOfString = null;
/*     */ 
/*  75 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*     */     {
/*  77 */       if (paramArrayOfString[i].equals("-cmd"))
/*     */       {
/*  79 */         int j = paramArrayOfString.length - i - 1;
/*  80 */         arrayOfString = new String[j];
/*  81 */         for (int k = 0; k < j; k++) arrayOfString[k] = paramArrayOfString[(++i)];
/*     */ 
/*  83 */         break;
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  89 */       Properties localProperties = System.getProperties();
/*  90 */       localProperties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
/*     */ 
/*  92 */       this.orb = ORB.init(paramArrayOfString, localProperties);
/*     */ 
/*  95 */       if (arrayOfString != null) { executeCommand(arrayOfString); }
/*     */       else
/*     */       {
/*  99 */         BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(System.in));
/*     */ 
/* 103 */         System.out.println(CorbaResourceUtil.getText("servertool.banner"));
/*     */         while (true)
/*     */         {
/* 107 */           arrayOfString = readCommand(localBufferedReader);
/* 108 */           if (arrayOfString != null) executeCommand(arrayOfString); else
/* 109 */             printAvailableCommands();
/*     */         }
/*     */       }
/*     */     } catch (Exception localException) {
/* 113 */       System.out.println(CorbaResourceUtil.getText("servertool.usage", "servertool"));
/* 114 */       System.out.println();
/* 115 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 121 */     ServerTool localServerTool = new ServerTool();
/* 122 */     localServerTool.run(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   String[] readCommand(BufferedReader paramBufferedReader)
/*     */   {
/* 127 */     System.out.print("servertool > ");
/*     */     try
/*     */     {
/* 130 */       int i = 0;
/* 131 */       String[] arrayOfString = null;
/*     */ 
/* 133 */       String str = paramBufferedReader.readLine();
/*     */ 
/* 135 */       if (str != null) {
/* 136 */         StringTokenizer localStringTokenizer = new StringTokenizer(str);
/* 137 */         if (localStringTokenizer.countTokens() != 0) {
/* 138 */           arrayOfString = new String[localStringTokenizer.countTokens()];
/* 139 */           while (localStringTokenizer.hasMoreTokens()) arrayOfString[(i++)] = localStringTokenizer.nextToken();
/*     */         }
/*     */       }
/*     */ 
/* 143 */       return arrayOfString;
/*     */     } catch (Exception localException) {
/* 145 */       System.out.println(CorbaResourceUtil.getText("servertool.usage", "servertool"));
/* 146 */       System.out.println();
/* 147 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */   void printAvailableCommands()
/*     */   {
/* 158 */     System.out.println(CorbaResourceUtil.getText("servertool.shorthelp"));
/*     */ 
/* 160 */     for (int i = 0; i < handlers.size(); i++) {
/* 161 */       CommandHandler localCommandHandler = (CommandHandler)handlers.elementAt(i);
/* 162 */       System.out.print("\t" + localCommandHandler.getCommandName());
/* 163 */       for (int j = localCommandHandler.getCommandName().length(); 
/* 164 */         j < maxNameLen; j++) System.out.print(" ");
/* 165 */       System.out.print(" - ");
/* 166 */       localCommandHandler.printCommandHelp(System.out, true);
/*     */     }
/*     */ 
/* 170 */     System.out.println();
/*     */   }
/*     */ 
/*     */   void executeCommand(String[] paramArrayOfString)
/*     */   {
/*     */     CommandHandler localCommandHandler;
/* 179 */     if (paramArrayOfString[0].equals("help")) {
/* 180 */       if (paramArrayOfString.length == 1) printAvailableCommands();
/*     */       else
/*     */       {
/* 183 */         for (i = 0; i < handlers.size(); i++) {
/* 184 */           localCommandHandler = (CommandHandler)handlers.elementAt(i);
/* 185 */           if (localCommandHandler.getCommandName().equals(paramArrayOfString[1])) {
/* 186 */             localCommandHandler.printCommandHelp(System.out, false);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 192 */       return;
/*     */     }
/*     */ 
/* 196 */     for (int i = 0; i < handlers.size(); i++) {
/* 197 */       localCommandHandler = (CommandHandler)handlers.elementAt(i);
/* 198 */       if (localCommandHandler.getCommandName().equals(paramArrayOfString[0])) {
/* 199 */         String[] arrayOfString = new String[paramArrayOfString.length - 1];
/*     */ 
/* 202 */         for (int j = 0; j < arrayOfString.length; j++) {
/* 203 */           arrayOfString[j] = paramArrayOfString[(j + 1)];
/*     */         }
/*     */         try
/*     */         {
/* 207 */           System.out.println();
/*     */ 
/* 209 */           boolean bool = localCommandHandler.processCommand(arrayOfString, this.orb, System.out);
/*     */ 
/* 211 */           if (bool == true) {
/* 212 */             localCommandHandler.printCommandHelp(System.out, false);
/*     */           }
/*     */ 
/* 216 */           System.out.println();
/*     */         }
/*     */         catch (Exception localException) {
/*     */         }
/* 220 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 225 */     printAvailableCommands();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 237 */     handlers.addElement(new RegisterServer());
/* 238 */     handlers.addElement(new UnRegisterServer());
/* 239 */     handlers.addElement(new GetServerID());
/* 240 */     handlers.addElement(new ListServers());
/* 241 */     handlers.addElement(new ListAliases());
/* 242 */     handlers.addElement(new ListActiveServers());
/* 243 */     handlers.addElement(new LocateServer());
/* 244 */     handlers.addElement(new LocateServerForORB());
/* 245 */     handlers.addElement(new ListORBs());
/* 246 */     handlers.addElement(new ShutdownServer());
/* 247 */     handlers.addElement(new StartServer());
/* 248 */     handlers.addElement(new Help());
/* 249 */     handlers.addElement(new Quit());
/*     */ 
/* 252 */     maxNameLen = 0;
/*     */ 
/* 254 */     for (int j = 0; j < handlers.size(); j++) {
/* 255 */       CommandHandler localCommandHandler = (CommandHandler)handlers.elementAt(j);
/* 256 */       int i = localCommandHandler.getCommandName().length();
/* 257 */       if (i > maxNameLen) maxNameLen = i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ServerTool
 * JD-Core Version:    0.6.2
 */