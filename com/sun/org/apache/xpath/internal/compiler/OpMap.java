/*     */ package com.sun.org.apache.xpath.internal.compiler;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.ObjectVector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class OpMap
/*     */ {
/*     */   protected String m_currentPattern;
/*     */   static final int MAXTOKENQUEUESIZE = 500;
/*     */   static final int BLOCKTOKENQUEUESIZE = 500;
/*  77 */   ObjectVector m_tokenQueue = new ObjectVector(500, 500);
/*     */ 
/* 123 */   OpMapVector m_opMap = null;
/*     */   public static final int MAPINDEX_LENGTH = 1;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  49 */     return this.m_currentPattern;
/*     */   }
/*     */ 
/*     */   public String getPatternString()
/*     */   {
/*  59 */     return this.m_currentPattern;
/*     */   }
/*     */ 
/*     */   public ObjectVector getTokenQueue()
/*     */   {
/*  86 */     return this.m_tokenQueue;
/*     */   }
/*     */ 
/*     */   public Object getToken(int pos)
/*     */   {
/*  98 */     return this.m_tokenQueue.elementAt(pos);
/*     */   }
/*     */ 
/*     */   public int getTokenQueueSize()
/*     */   {
/* 113 */     return this.m_tokenQueue.size();
/*     */   }
/*     */ 
/*     */   public OpMapVector getOpMap()
/*     */   {
/* 135 */     return this.m_opMap;
/*     */   }
/*     */ 
/*     */   void shrink()
/*     */   {
/* 154 */     int n = this.m_opMap.elementAt(1);
/* 155 */     this.m_opMap.setToSize(n + 4);
/*     */ 
/* 157 */     this.m_opMap.setElementAt(0, n);
/* 158 */     this.m_opMap.setElementAt(0, n + 1);
/* 159 */     this.m_opMap.setElementAt(0, n + 2);
/*     */ 
/* 162 */     n = this.m_tokenQueue.size();
/* 163 */     this.m_tokenQueue.setToSize(n + 4);
/*     */ 
/* 165 */     this.m_tokenQueue.setElementAt(null, n);
/* 166 */     this.m_tokenQueue.setElementAt(null, n + 1);
/* 167 */     this.m_tokenQueue.setElementAt(null, n + 2);
/*     */   }
/*     */ 
/*     */   public int getOp(int opPos)
/*     */   {
/* 178 */     return this.m_opMap.elementAt(opPos);
/*     */   }
/*     */ 
/*     */   public void setOp(int opPos, int value)
/*     */   {
/* 189 */     this.m_opMap.setElementAt(value, opPos);
/*     */   }
/*     */ 
/*     */   public int getNextOpPos(int opPos)
/*     */   {
/* 202 */     return opPos + this.m_opMap.elementAt(opPos + 1);
/*     */   }
/*     */ 
/*     */   public int getNextStepPos(int opPos)
/*     */   {
/* 215 */     int stepType = getOp(opPos);
/*     */ 
/* 217 */     if ((stepType >= 37) && (stepType <= 53))
/*     */     {
/* 220 */       return getNextOpPos(opPos);
/*     */     }
/* 222 */     if ((stepType >= 22) && (stepType <= 25))
/*     */     {
/* 225 */       int newOpPos = getNextOpPos(opPos);
/*     */ 
/* 227 */       while (29 == getOp(newOpPos))
/*     */       {
/* 229 */         newOpPos = getNextOpPos(newOpPos);
/*     */       }
/*     */ 
/* 232 */       stepType = getOp(newOpPos);
/*     */ 
/* 234 */       if ((stepType < 37) || (stepType > 53))
/*     */       {
/* 237 */         return -1;
/*     */       }
/*     */ 
/* 240 */       return newOpPos;
/*     */     }
/*     */ 
/* 244 */     throw new RuntimeException(XSLMessages.createXPATHMessage("ER_UNKNOWN_STEP", new Object[] { String.valueOf(stepType) }));
/*     */   }
/*     */ 
/*     */   public static int getNextOpPos(int[] opMap, int opPos)
/*     */   {
/* 260 */     return opPos + opMap[(opPos + 1)];
/*     */   }
/*     */ 
/*     */   public int getFirstPredicateOpPos(int opPos)
/*     */     throws TransformerException
/*     */   {
/* 279 */     int stepType = this.m_opMap.elementAt(opPos);
/*     */ 
/* 281 */     if ((stepType >= 37) && (stepType <= 53))
/*     */     {
/* 284 */       return opPos + this.m_opMap.elementAt(opPos + 2);
/*     */     }
/* 286 */     if ((stepType >= 22) && (stepType <= 25))
/*     */     {
/* 289 */       return opPos + this.m_opMap.elementAt(opPos + 1);
/*     */     }
/* 291 */     if (-2 == stepType)
/*     */     {
/* 293 */       return -2;
/*     */     }
/*     */ 
/* 297 */     error("ER_UNKNOWN_OPCODE", new Object[] { String.valueOf(stepType) });
/*     */ 
/* 299 */     return -1;
/*     */   }
/*     */ 
/*     */   public void error(String msg, Object[] args)
/*     */     throws TransformerException
/*     */   {
/* 319 */     String fmsg = XSLMessages.createXPATHMessage(msg, args);
/*     */ 
/* 322 */     throw new TransformerException(fmsg);
/*     */   }
/*     */ 
/*     */   public static int getFirstChildPos(int opPos)
/*     */   {
/* 335 */     return opPos + 2;
/*     */   }
/*     */ 
/*     */   public int getArgLength(int opPos)
/*     */   {
/* 347 */     return this.m_opMap.elementAt(opPos + 1);
/*     */   }
/*     */ 
/*     */   public int getArgLengthOfStep(int opPos)
/*     */   {
/* 359 */     return this.m_opMap.elementAt(opPos + 1 + 1) - 3;
/*     */   }
/*     */ 
/*     */   public static int getFirstChildPosOfStep(int opPos)
/*     */   {
/* 371 */     return opPos + 3;
/*     */   }
/*     */ 
/*     */   public int getStepTestType(int opPosOfStep)
/*     */   {
/* 383 */     return this.m_opMap.elementAt(opPosOfStep + 3);
/*     */   }
/*     */ 
/*     */   public String getStepNS(int opPosOfStep)
/*     */   {
/* 396 */     int argLenOfStep = getArgLengthOfStep(opPosOfStep);
/*     */ 
/* 399 */     if (argLenOfStep == 3)
/*     */     {
/* 401 */       int index = this.m_opMap.elementAt(opPosOfStep + 4);
/*     */ 
/* 403 */       if (index >= 0)
/* 404 */         return (String)this.m_tokenQueue.elementAt(index);
/* 405 */       if (-3 == index) {
/* 406 */         return "*";
/*     */       }
/* 408 */       return null;
/*     */     }
/*     */ 
/* 411 */     return null;
/*     */   }
/*     */ 
/*     */   public String getStepLocalName(int opPosOfStep)
/*     */   {
/* 423 */     int argLenOfStep = getArgLengthOfStep(opPosOfStep);
/*     */     int index;
/* 428 */     switch (argLenOfStep)
/*     */     {
/*     */     case 0:
/* 431 */       index = -2;
/* 432 */       break;
/*     */     case 1:
/* 434 */       index = -3;
/* 435 */       break;
/*     */     case 2:
/* 437 */       index = this.m_opMap.elementAt(opPosOfStep + 4);
/* 438 */       break;
/*     */     case 3:
/* 440 */       index = this.m_opMap.elementAt(opPosOfStep + 5);
/* 441 */       break;
/*     */     default:
/* 443 */       index = -2;
/*     */     }
/*     */ 
/* 449 */     if (index >= 0)
/* 450 */       return this.m_tokenQueue.elementAt(index).toString();
/* 451 */     if (-3 == index) {
/* 452 */       return "*";
/*     */     }
/* 454 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.compiler.OpMap
 * JD-Core Version:    0.6.2
 */