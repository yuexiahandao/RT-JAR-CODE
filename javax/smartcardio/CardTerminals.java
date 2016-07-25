/*     */ package javax.smartcardio;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class CardTerminals
/*     */ {
/*     */   public List<CardTerminal> list()
/*     */     throws CardException
/*     */   {
/*  72 */     return list(State.ALL);
/*     */   }
/*     */ 
/*     */   public abstract List<CardTerminal> list(State paramState)
/*     */     throws CardException;
/*     */ 
/*     */   public CardTerminal getTerminal(String paramString)
/*     */   {
/* 114 */     if (paramString == null)
/* 115 */       throw new NullPointerException();
/*     */     try
/*     */     {
/* 118 */       for (CardTerminal localCardTerminal : list()) {
/* 119 */         if (localCardTerminal.getName().equals(paramString)) {
/* 120 */           return localCardTerminal;
/*     */         }
/*     */       }
/* 123 */       return null; } catch (CardException localCardException) {
/*     */     }
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   public void waitForChange()
/*     */     throws CardException
/*     */   {
/* 141 */     waitForChange(0L);
/*     */   }
/*     */ 
/*     */   public abstract boolean waitForChange(long paramLong)
/*     */     throws CardException;
/*     */ 
/*     */   public static enum State
/*     */   {
/* 196 */     ALL, 
/*     */ 
/* 200 */     CARD_PRESENT, 
/*     */ 
/* 204 */     CARD_ABSENT, 
/*     */ 
/* 210 */     CARD_INSERTION, 
/*     */ 
/* 216 */     CARD_REMOVAL;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.smartcardio.CardTerminals
 * JD-Core Version:    0.6.2
 */