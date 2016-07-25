/*     */ package com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ public class GetOpt
/*     */ {
/* 204 */   private Option theCurrentOption = null;
/*     */   private ListIterator theOptionsIterator;
/* 206 */   private List theOptions = null;
/* 207 */   private List theCmdArgs = null;
/* 208 */   private OptionMatcher theOptionMatcher = null;
/*     */ 
/*     */   public GetOpt(String[] args, String optString)
/*     */   {
/*  49 */     this.theOptions = new ArrayList();
/*  50 */     int currOptIndex = 0;
/*  51 */     this.theCmdArgs = new ArrayList();
/*  52 */     this.theOptionMatcher = new OptionMatcher(optString);
/*     */ 
/*  54 */     for (int i = 0; i < args.length; i++) {
/*  55 */       String token = args[i];
/*  56 */       int tokenLength = token.length();
/*  57 */       if (token.equals("--")) {
/*  58 */         currOptIndex = i + 1;
/*  59 */         break;
/*     */       }
/*  61 */       if ((token.startsWith("-")) && (tokenLength == 2))
/*     */       {
/*  63 */         this.theOptions.add(new Option(token.charAt(1)));
/*     */       }
/*  65 */       else if ((token.startsWith("-")) && (tokenLength > 2))
/*     */       {
/*  69 */         for (int j = 1; j < tokenLength; j++) {
/*  70 */           this.theOptions.add(new Option(token.charAt(j)));
/*     */         }
/*     */       }
/*  73 */       else if (!token.startsWith("-"))
/*     */       {
/*  76 */         if (this.theOptions.size() == 0) {
/*  77 */           currOptIndex = i;
/*  78 */           break;
/*     */         }
/*     */ 
/*  84 */         int indexoflast = 0;
/*  85 */         indexoflast = this.theOptions.size() - 1;
/*  86 */         Option op = (Option)this.theOptions.get(indexoflast);
/*  87 */         char opLetter = op.getArgLetter();
/*  88 */         if ((!op.hasArg()) && (this.theOptionMatcher.hasArg(opLetter))) {
/*  89 */           op.setArg(token);
/*     */         }
/*     */         else
/*     */         {
/*  97 */           currOptIndex = i;
/*  98 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 105 */     this.theOptionsIterator = this.theOptions.listIterator();
/*     */ 
/* 108 */     for (int i = currOptIndex; i < args.length; i++) {
/* 109 */       String token = args[i];
/* 110 */       this.theCmdArgs.add(token);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printOptions()
/*     */   {
/* 119 */     for (ListIterator it = this.theOptions.listIterator(); it.hasNext(); ) {
/* 120 */       Option opt = (Option)it.next();
/* 121 */       System.out.print("OPT =" + opt.getArgLetter());
/* 122 */       String arg = opt.getArgument();
/* 123 */       if (arg != null) {
/* 124 */         System.out.print(" " + arg);
/*     */       }
/* 126 */       System.out.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNextOption()
/*     */     throws IllegalArgumentException, MissingOptArgException
/*     */   {
/* 147 */     int retval = -1;
/* 148 */     if (this.theOptionsIterator.hasNext()) {
/* 149 */       this.theCurrentOption = ((Option)this.theOptionsIterator.next());
/* 150 */       char c = this.theCurrentOption.getArgLetter();
/* 151 */       boolean shouldHaveArg = this.theOptionMatcher.hasArg(c);
/* 152 */       String arg = this.theCurrentOption.getArgument();
/* 153 */       if (!this.theOptionMatcher.match(c)) {
/* 154 */         ErrorMsg msg = new ErrorMsg("ILLEGAL_CMDLINE_OPTION_ERR", new Character(c));
/*     */ 
/* 156 */         throw new IllegalArgumentException(msg.toString());
/*     */       }
/* 158 */       if ((shouldHaveArg) && (arg == null)) {
/* 159 */         ErrorMsg msg = new ErrorMsg("CMDLINE_OPT_MISSING_ARG_ERR", new Character(c));
/*     */ 
/* 161 */         throw new MissingOptArgException(msg.toString());
/*     */       }
/* 163 */       retval = c;
/*     */     }
/* 165 */     return retval;
/*     */   }
/*     */ 
/*     */   public String getOptionArg()
/*     */   {
/* 176 */     String retval = null;
/* 177 */     String tmp = this.theCurrentOption.getArgument();
/* 178 */     char c = this.theCurrentOption.getArgLetter();
/* 179 */     if (this.theOptionMatcher.hasArg(c)) {
/* 180 */       retval = tmp;
/*     */     }
/* 182 */     return retval;
/*     */   }
/*     */ 
/*     */   public String[] getCmdArgs()
/*     */   {
/* 195 */     String[] retval = new String[this.theCmdArgs.size()];
/* 196 */     int i = 0;
/* 197 */     for (ListIterator it = this.theCmdArgs.listIterator(); it.hasNext(); ) {
/* 198 */       retval[(i++)] = ((String)it.next());
/*     */     }
/* 200 */     return retval;
/*     */   }
/*     */ 
/*     */   class Option
/*     */   {
/*     */     private char theArgLetter;
/* 219 */     private String theArgument = null;
/*     */ 
/* 220 */     public Option(char argLetter) { this.theArgLetter = argLetter; } 
/*     */     public void setArg(String arg) {
/* 222 */       this.theArgument = arg;
/*     */     }
/* 224 */     public boolean hasArg() { return this.theArgument != null; } 
/* 225 */     public char getArgLetter() { return this.theArgLetter; } 
/* 226 */     public String getArgument() { return this.theArgument; }
/*     */ 
/*     */   }
/*     */ 
/*     */   class OptionMatcher
/*     */   {
/* 256 */     private String theOptString = null;
/*     */ 
/*     */     public OptionMatcher(String optString)
/*     */     {
/* 235 */       this.theOptString = optString;
/*     */     }
/*     */     public boolean match(char c) {
/* 238 */       boolean retval = false;
/* 239 */       if (this.theOptString.indexOf(c) != -1) {
/* 240 */         retval = true;
/*     */       }
/* 242 */       return retval;
/*     */     }
/*     */     public boolean hasArg(char c) {
/* 245 */       boolean retval = false;
/* 246 */       int index = this.theOptString.indexOf(c) + 1;
/* 247 */       if (index == this.theOptString.length())
/*     */       {
/* 249 */         retval = false;
/*     */       }
/* 251 */       else if (this.theOptString.charAt(index) == ':') {
/* 252 */         retval = true;
/*     */       }
/* 254 */       return retval;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOpt
 * JD-Core Version:    0.6.2
 */