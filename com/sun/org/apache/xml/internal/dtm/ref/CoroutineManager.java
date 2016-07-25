/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ public class CoroutineManager
/*     */ {
/* 115 */   BitSet m_activeIDs = new BitSet();
/*     */   static final int m_unreasonableId = 1024;
/* 151 */   Object m_yield = null;
/*     */   static final int NOBODY = -1;
/*     */   static final int ANYBODY = -1;
/* 162 */   int m_nextCoroutine = -1;
/*     */ 
/*     */   public synchronized int co_joinCoroutineSet(int coroutineID)
/*     */   {
/* 188 */     if (coroutineID >= 0)
/*     */     {
/* 190 */       if ((coroutineID >= 1024) || (this.m_activeIDs.get(coroutineID))) {
/* 191 */         return -1;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 197 */       coroutineID = 0;
/* 198 */       while (coroutineID < 1024)
/*     */       {
/* 200 */         if (!this.m_activeIDs.get(coroutineID)) break;
/* 201 */         coroutineID++;
/*     */       }
/*     */ 
/* 205 */       if (coroutineID >= 1024) {
/* 206 */         return -1;
/*     */       }
/*     */     }
/* 209 */     this.m_activeIDs.set(coroutineID);
/* 210 */     return coroutineID;
/*     */   }
/*     */ 
/*     */   public synchronized Object co_entry_pause(int thisCoroutine)
/*     */     throws NoSuchMethodException
/*     */   {
/* 230 */     if (!this.m_activeIDs.get(thisCoroutine)) {
/* 231 */       throw new NoSuchMethodException();
/*     */     }
/* 233 */     while (this.m_nextCoroutine != thisCoroutine)
/*     */     {
/*     */       try
/*     */       {
/* 237 */         wait();
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 246 */     return this.m_yield;
/*     */   }
/*     */ 
/*     */   public synchronized Object co_resume(Object arg_object, int thisCoroutine, int toCoroutine)
/*     */     throws NoSuchMethodException
/*     */   {
/* 265 */     if (!this.m_activeIDs.get(toCoroutine)) {
/* 266 */       throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[] { Integer.toString(toCoroutine) }));
/*     */     }
/*     */ 
/* 270 */     this.m_yield = arg_object;
/* 271 */     this.m_nextCoroutine = toCoroutine;
/*     */ 
/* 273 */     notify();
/* 274 */     while ((this.m_nextCoroutine != thisCoroutine) || (this.m_nextCoroutine == -1) || (this.m_nextCoroutine == -1))
/*     */     {
/*     */       try
/*     */       {
/* 279 */         wait();
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 288 */     if (this.m_nextCoroutine == -1)
/*     */     {
/* 291 */       co_exit(thisCoroutine);
/*     */ 
/* 294 */       throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_CO_EXIT", null));
/*     */     }
/*     */ 
/* 297 */     return this.m_yield;
/*     */   }
/*     */ 
/*     */   public synchronized void co_exit(int thisCoroutine)
/*     */   {
/* 315 */     this.m_activeIDs.clear(thisCoroutine);
/* 316 */     this.m_nextCoroutine = -1;
/* 317 */     notify();
/*     */   }
/*     */ 
/*     */   public synchronized void co_exit_to(Object arg_object, int thisCoroutine, int toCoroutine)
/*     */     throws NoSuchMethodException
/*     */   {
/* 334 */     if (!this.m_activeIDs.get(toCoroutine)) {
/* 335 */       throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[] { Integer.toString(toCoroutine) }));
/*     */     }
/*     */ 
/* 339 */     this.m_yield = arg_object;
/* 340 */     this.m_nextCoroutine = toCoroutine;
/*     */ 
/* 342 */     this.m_activeIDs.clear(thisCoroutine);
/*     */ 
/* 344 */     notify();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.CoroutineManager
 * JD-Core Version:    0.6.2
 */