/*     */ package com.sun.org.apache.regexp.internal;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class REDebugCompiler extends RECompiler
/*     */ {
/*  37 */   static Hashtable hashOpcode = new Hashtable();
/*     */ 
/*     */   String opcodeToString(char opcode)
/*     */   {
/*  72 */     String ret = (String)hashOpcode.get(new Integer(opcode));
/*     */ 
/*  75 */     if (ret == null)
/*     */     {
/*  77 */       ret = "OP_????";
/*     */     }
/*  79 */     return ret;
/*     */   }
/*     */ 
/*     */   String charToString(char c)
/*     */   {
/*  90 */     if ((c < ' ') || (c > ''))
/*     */     {
/*  92 */       return "\\" + c;
/*     */     }
/*     */ 
/*  96 */     return String.valueOf(c);
/*     */   }
/*     */ 
/*     */   String nodeToString(int node)
/*     */   {
/* 107 */     char opcode = this.instruction[(node + 0)];
/* 108 */     int opdata = this.instruction[(node + 1)];
/*     */ 
/* 111 */     return opcodeToString(opcode) + ", opdata = " + opdata;
/*     */   }
/*     */ 
/*     */   public void dumpProgram(PrintWriter p)
/*     */   {
/* 153 */     for (int i = 0; i < this.lenInstruction; )
/*     */     {
/* 156 */       char opcode = this.instruction[(i + 0)];
/* 157 */       char opdata = this.instruction[(i + 1)];
/* 158 */       short next = (short)this.instruction[(i + 2)];
/*     */ 
/* 161 */       p.print(i + ". " + nodeToString(i) + ", next = ");
/*     */ 
/* 164 */       if (next == 0)
/*     */       {
/* 166 */         p.print("none");
/*     */       }
/*     */       else
/*     */       {
/* 170 */         p.print(i + next);
/*     */       }
/*     */ 
/* 174 */       i += 3;
/*     */ 
/* 177 */       if (opcode == '[')
/*     */       {
/* 180 */         p.print(", [");
/*     */ 
/* 183 */         int rangeCount = opdata;
/* 184 */         for (int r = 0; r < rangeCount; r++)
/*     */         {
/* 187 */           char charFirst = this.instruction[(i++)];
/* 188 */           char charLast = this.instruction[(i++)];
/*     */ 
/* 191 */           if (charFirst == charLast)
/*     */           {
/* 193 */             p.print(charToString(charFirst));
/*     */           }
/*     */           else
/*     */           {
/* 197 */             p.print(charToString(charFirst) + "-" + charToString(charLast));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 202 */         p.print("]");
/*     */       }
/*     */ 
/* 206 */       if (opcode == 'A')
/*     */       {
/* 209 */         p.print(", \"");
/*     */ 
/* 212 */         for (int len = opdata; len-- != 0; )
/*     */         {
/* 214 */           p.print(charToString(this.instruction[(i++)]));
/*     */         }
/*     */ 
/* 218 */         p.print("\"");
/*     */       }
/*     */ 
/* 222 */       p.println("");
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  40 */     hashOpcode.put(new Integer(56), "OP_RELUCTANTSTAR");
/*  41 */     hashOpcode.put(new Integer(61), "OP_RELUCTANTPLUS");
/*  42 */     hashOpcode.put(new Integer(47), "OP_RELUCTANTMAYBE");
/*  43 */     hashOpcode.put(new Integer(69), "OP_END");
/*  44 */     hashOpcode.put(new Integer(94), "OP_BOL");
/*  45 */     hashOpcode.put(new Integer(36), "OP_EOL");
/*  46 */     hashOpcode.put(new Integer(46), "OP_ANY");
/*  47 */     hashOpcode.put(new Integer(91), "OP_ANYOF");
/*  48 */     hashOpcode.put(new Integer(124), "OP_BRANCH");
/*  49 */     hashOpcode.put(new Integer(65), "OP_ATOM");
/*  50 */     hashOpcode.put(new Integer(42), "OP_STAR");
/*  51 */     hashOpcode.put(new Integer(43), "OP_PLUS");
/*  52 */     hashOpcode.put(new Integer(63), "OP_MAYBE");
/*  53 */     hashOpcode.put(new Integer(78), "OP_NOTHING");
/*  54 */     hashOpcode.put(new Integer(71), "OP_GOTO");
/*  55 */     hashOpcode.put(new Integer(92), "OP_ESCAPE");
/*  56 */     hashOpcode.put(new Integer(40), "OP_OPEN");
/*  57 */     hashOpcode.put(new Integer(41), "OP_CLOSE");
/*  58 */     hashOpcode.put(new Integer(35), "OP_BACKREF");
/*  59 */     hashOpcode.put(new Integer(80), "OP_POSIXCLASS");
/*  60 */     hashOpcode.put(new Integer(60), "OP_OPEN_CLUSTER");
/*  61 */     hashOpcode.put(new Integer(62), "OP_CLOSE_CLUSTER");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.regexp.internal.REDebugCompiler
 * JD-Core Version:    0.6.2
 */