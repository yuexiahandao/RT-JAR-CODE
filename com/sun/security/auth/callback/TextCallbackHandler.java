/*     */ package com.sun.security.auth.callback;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.ConfirmationCallback;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.TextOutputCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import sun.security.util.Password;
/*     */ 
/*     */ public class TextCallbackHandler
/*     */   implements CallbackHandler
/*     */ {
/*     */   public void handle(Callback[] paramArrayOfCallback)
/*     */     throws IOException, UnsupportedCallbackException
/*     */   {
/*  78 */     ConfirmationCallback localConfirmationCallback = null;
/*     */ 
/*  80 */     for (int i = 0; i < paramArrayOfCallback.length; i++)
/*     */     {
/*     */       Object localObject;
/*     */       String str1;
/*  81 */       if ((paramArrayOfCallback[i] instanceof TextOutputCallback)) {
/*  82 */         localObject = (TextOutputCallback)paramArrayOfCallback[i];
/*     */ 
/*  85 */         switch (((TextOutputCallback)localObject).getMessageType()) {
/*     */         case 0:
/*  87 */           str1 = "";
/*  88 */           break;
/*     */         case 1:
/*  90 */           str1 = "Warning: ";
/*  91 */           break;
/*     */         case 2:
/*  93 */           str1 = "Error: ";
/*  94 */           break;
/*     */         default:
/*  96 */           throw new UnsupportedCallbackException(paramArrayOfCallback[i], "Unrecognized message type");
/*     */         }
/*     */ 
/* 100 */         String str2 = ((TextOutputCallback)localObject).getMessage();
/* 101 */         if (str2 != null) {
/* 102 */           str1 = str1 + str2;
/*     */         }
/* 104 */         if (str1 != null) {
/* 105 */           System.err.println(str1);
/*     */         }
/*     */       }
/* 108 */       else if ((paramArrayOfCallback[i] instanceof NameCallback)) {
/* 109 */         localObject = (NameCallback)paramArrayOfCallback[i];
/*     */ 
/* 111 */         if (((NameCallback)localObject).getDefaultName() == null)
/* 112 */           System.err.print(((NameCallback)localObject).getPrompt());
/*     */         else {
/* 114 */           System.err.print(((NameCallback)localObject).getPrompt() + " [" + ((NameCallback)localObject).getDefaultName() + "] ");
/*     */         }
/*     */ 
/* 117 */         System.err.flush();
/*     */ 
/* 119 */         str1 = readLine();
/* 120 */         if (str1.equals("")) {
/* 121 */           str1 = ((NameCallback)localObject).getDefaultName();
/*     */         }
/*     */ 
/* 124 */         ((NameCallback)localObject).setName(str1);
/*     */       }
/* 126 */       else if ((paramArrayOfCallback[i] instanceof PasswordCallback)) {
/* 127 */         localObject = (PasswordCallback)paramArrayOfCallback[i];
/*     */ 
/* 129 */         System.err.print(((PasswordCallback)localObject).getPrompt());
/* 130 */         System.err.flush();
/*     */ 
/* 132 */         ((PasswordCallback)localObject).setPassword(Password.readPassword(System.in, ((PasswordCallback)localObject).isEchoOn()));
/*     */       }
/* 134 */       else if ((paramArrayOfCallback[i] instanceof ConfirmationCallback)) {
/* 135 */         localConfirmationCallback = (ConfirmationCallback)paramArrayOfCallback[i];
/*     */       }
/*     */       else {
/* 138 */         throw new UnsupportedCallbackException(paramArrayOfCallback[i], "Unrecognized Callback");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 144 */     if (localConfirmationCallback != null)
/* 145 */       doConfirmation(localConfirmationCallback);
/*     */   }
/*     */ 
/*     */   private String readLine()
/*     */     throws IOException
/*     */   {
/* 151 */     String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
/*     */ 
/* 153 */     if (str == null) {
/* 154 */       throw new IOException("Cannot read from System.in");
/*     */     }
/* 156 */     return str;
/*     */   }
/*     */ 
/*     */   private void doConfirmation(ConfirmationCallback paramConfirmationCallback)
/*     */     throws IOException, UnsupportedCallbackException
/*     */   {
/* 163 */     int i = paramConfirmationCallback.getMessageType();
/*     */     String str1;
/* 164 */     switch (i) {
/*     */     case 1:
/* 166 */       str1 = "Warning: ";
/* 167 */       break;
/*     */     case 2:
/* 169 */       str1 = "Error: ";
/* 170 */       break;
/*     */     case 0:
/* 172 */       str1 = "";
/* 173 */       break;
/*     */     default:
/* 175 */       throw new UnsupportedCallbackException(paramConfirmationCallback, "Unrecognized message type: " + i);
/*     */     }
/*     */ 
/* 189 */     int j = paramConfirmationCallback.getOptionType();
/*     */     Object arrayOf1OptionInfo;
/* 190 */     switch (j) {
/*     */     case 0:
/* 192 */       arrayOf1OptionInfo = new 1OptionInfo[] { new Object()
/*     */       {
/*     */         String name;
/*     */         int value;
/*     */       }
/*     */       , new Object()
/*     */       {
/*     */         String name;
/*     */         int value;
/*     */       }
/*     */        };
/* 196 */       break;
/*     */     case 1:
/* 198 */       arrayOf1OptionInfo = new 1OptionInfo[] { new Object()
/*     */       {
/*     */         String name;
/*     */         int value;
/*     */       }
/*     */       , new Object()
/*     */       {
/*     */         String name;
/*     */         int value;
/*     */       }
/*     */       , new Object()
/*     */       {
/*     */         String name;
/*     */         int value;
/*     */       }
/*     */        };
/* 203 */       break;
/*     */     case 2:
/* 205 */       arrayOf1OptionInfo = new 1OptionInfo[] { new Object()
/*     */       {
/*     */         String name;
/*     */         int value;
/*     */       }
/*     */       , new Object()
/*     */       {
/*     */         String name;
/*     */         int value;
/*     */       }
/*     */        };
/* 209 */       break;
/*     */     case -1:
/* 211 */       String[] arrayOfString = paramConfirmationCallback.getOptions();
/* 212 */       arrayOf1OptionInfo = new 1OptionInfo[arrayOfString.length];
/* 213 */       for (int m = 0; m < arrayOf1OptionInfo.length; m++)
/* 214 */         arrayOf1OptionInfo[m] = new Object() { String name;
/*     */           int value; } ; break;
/*     */     default:
/* 218 */       throw new UnsupportedCallbackException(paramConfirmationCallback, "Unrecognized option type: " + j);
/*     */     }
/*     */ 
/* 222 */     int k = paramConfirmationCallback.getDefaultOption();
/*     */ 
/* 224 */     String str2 = paramConfirmationCallback.getPrompt();
/* 225 */     if (str2 == null) {
/* 226 */       str2 = "";
/*     */     }
/* 228 */     str2 = str1 + str2;
/* 229 */     if (!str2.equals("")) {
/* 230 */       System.err.println(str2);
/*     */     }
/*     */ 
/* 233 */     for (int n = 0; n < arrayOf1OptionInfo.length; n++) {
/* 234 */       if (j == -1)
/*     */       {
/* 236 */         System.err.println(n + ". " + arrayOf1OptionInfo[n].name + (n == k ? " [default]" : ""));
/*     */       }
/*     */       else
/*     */       {
/* 241 */         System.err.println(n + ". " + arrayOf1OptionInfo[n].name + (arrayOf1OptionInfo[n].value == k ? " [default]" : ""));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 246 */     System.err.print("Enter a number: ");
/* 247 */     System.err.flush();
/*     */     try
/*     */     {
/* 250 */       n = Integer.parseInt(readLine());
/* 251 */       if ((n < 0) || (n > arrayOf1OptionInfo.length - 1)) {
/* 252 */         n = k;
/*     */       }
/* 254 */       n = arrayOf1OptionInfo[n].value;
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 256 */       n = k;
/*     */     }
/*     */ 
/* 259 */     paramConfirmationCallback.setSelectedIndex(n);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.callback.TextCallbackHandler
 * JD-Core Version:    0.6.2
 */