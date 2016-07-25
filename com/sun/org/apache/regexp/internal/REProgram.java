/*     */ package com.sun.org.apache.regexp.internal;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class REProgram
/*     */   implements Serializable
/*     */ {
/*     */   static final int OPT_HASBACKREFS = 1;
/*     */   char[] instruction;
/*     */   int lenInstruction;
/*     */   char[] prefix;
/*     */   int flags;
/*  44 */   int maxParens = -1;
/*     */ 
/*     */   public REProgram(char[] instruction)
/*     */   {
/*  52 */     this(instruction, instruction.length);
/*     */   }
/*     */ 
/*     */   public REProgram(int parens, char[] instruction)
/*     */   {
/*  62 */     this(instruction, instruction.length);
/*  63 */     this.maxParens = parens;
/*     */   }
/*     */ 
/*     */   public REProgram(char[] instruction, int lenInstruction)
/*     */   {
/*  73 */     setInstructions(instruction, lenInstruction);
/*     */   }
/*     */ 
/*     */   public char[] getInstructions()
/*     */   {
/*  85 */     if (this.lenInstruction != 0)
/*     */     {
/*  88 */       char[] ret = new char[this.lenInstruction];
/*  89 */       System.arraycopy(this.instruction, 0, ret, 0, this.lenInstruction);
/*  90 */       return ret;
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   public void setInstructions(char[] instruction, int lenInstruction)
/*     */   {
/* 108 */     this.instruction = instruction;
/* 109 */     this.lenInstruction = lenInstruction;
/*     */ 
/* 112 */     this.flags = 0;
/* 113 */     this.prefix = null;
/*     */ 
/* 116 */     if ((instruction != null) && (lenInstruction != 0))
/*     */     {
/* 119 */       if ((lenInstruction >= 3) && (instruction[0] == '|'))
/*     */       {
/* 122 */         int next = instruction[2];
/* 123 */         if (instruction[(next + 0)] == 'E')
/*     */         {
/* 126 */           if ((lenInstruction >= 6) && (instruction[3] == 'A'))
/*     */           {
/* 129 */             int lenAtom = instruction[4];
/* 130 */             this.prefix = new char[lenAtom];
/* 131 */             System.arraycopy(instruction, 6, this.prefix, 0, lenAtom);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 139 */       for (int i = 0; i < lenInstruction; i += 3)
/*     */       {
/* 141 */         switch (instruction[(i + 0)])
/*     */         {
/*     */         case '[':
/* 144 */           i += instruction[(i + 1)] * '\002';
/* 145 */           break;
/*     */         case 'A':
/* 148 */           i += instruction[(i + 1)];
/* 149 */           break;
/*     */         case '#':
/* 152 */           this.flags |= 1;
/* 153 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.regexp.internal.REProgram
 * JD-Core Version:    0.6.2
 */