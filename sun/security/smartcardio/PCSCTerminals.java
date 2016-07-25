/*     */ package sun.security.smartcardio;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.smartcardio.CardException;
/*     */ import javax.smartcardio.CardTerminal;
/*     */ import javax.smartcardio.CardTerminals;
/*     */ import javax.smartcardio.CardTerminals.State;
/*     */ 
/*     */ final class PCSCTerminals extends CardTerminals
/*     */ {
/*     */   private static long contextId;
/*     */   private Map<String, ReaderState> stateMap;
/*  60 */   private static final Map<String, Reference<TerminalImpl>> terminals = new HashMap();
/*     */ 
/*     */   static synchronized void initContext()
/*     */     throws PCSCException
/*     */   {
/*  55 */     if (contextId == 0L)
/*  56 */       contextId = PCSC.SCardEstablishContext(0);
/*     */   }
/*     */ 
/*     */   private static synchronized TerminalImpl implGetTerminal(String paramString)
/*     */   {
/*  64 */     Reference localReference = (Reference)terminals.get(paramString);
/*  65 */     TerminalImpl localTerminalImpl = localReference != null ? (TerminalImpl)localReference.get() : null;
/*  66 */     if (localTerminalImpl != null) {
/*  67 */       return localTerminalImpl;
/*     */     }
/*  69 */     localTerminalImpl = new TerminalImpl(contextId, paramString);
/*  70 */     terminals.put(paramString, new WeakReference(localTerminalImpl));
/*  71 */     return localTerminalImpl;
/*     */   }
/*     */ 
/*     */   public synchronized List<CardTerminal> list(CardTerminals.State paramState) throws CardException
/*     */   {
/*  76 */     if (paramState == null)
/*  77 */       throw new NullPointerException();
/*     */     try
/*     */     {
/*  80 */       String[] arrayOfString1 = PCSC.SCardListReaders(contextId);
/*  81 */       ArrayList localArrayList = new ArrayList(arrayOfString1.length);
/*  82 */       if (this.stateMap == null)
/*     */       {
/*  85 */         if (paramState == CardTerminals.State.CARD_INSERTION)
/*  86 */           paramState = CardTerminals.State.CARD_PRESENT;
/*  87 */         else if (paramState == CardTerminals.State.CARD_REMOVAL) {
/*  88 */           paramState = CardTerminals.State.CARD_ABSENT;
/*     */         }
/*     */       }
/*  91 */       for (String str : arrayOfString1) {
/*  92 */         TerminalImpl localTerminalImpl = implGetTerminal(str);
/*     */         ReaderState localReaderState;
/*  94 */         switch (1.$SwitchMap$javax$smartcardio$CardTerminals$State[paramState.ordinal()]) {
/*     */         case 1:
/*  96 */           localArrayList.add(localTerminalImpl);
/*  97 */           break;
/*     */         case 2:
/*  99 */           if (localTerminalImpl.isCardPresent())
/* 100 */             localArrayList.add(localTerminalImpl); break;
/*     */         case 3:
/* 104 */           if (!localTerminalImpl.isCardPresent())
/* 105 */             localArrayList.add(localTerminalImpl); break;
/*     */         case 4:
/* 109 */           localReaderState = (ReaderState)this.stateMap.get(str);
/* 110 */           if ((localReaderState != null) && (localReaderState.isInsertion()))
/* 111 */             localArrayList.add(localTerminalImpl); break;
/*     */         case 5:
/* 115 */           localReaderState = (ReaderState)this.stateMap.get(str);
/* 116 */           if ((localReaderState != null) && (localReaderState.isRemoval()))
/* 117 */             localArrayList.add(localTerminalImpl); break;
/*     */         default:
/* 121 */           throw new CardException("Unknown state: " + paramState);
/*     */         }
/*     */       }
/* 124 */       return Collections.unmodifiableList(localArrayList);
/*     */     } catch (PCSCException localPCSCException) {
/* 126 */       throw new CardException("list() failed", localPCSCException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean waitForChange(long paramLong)
/*     */     throws CardException
/*     */   {
/* 155 */     if (paramLong < 0L) {
/* 156 */       throw new IllegalArgumentException("Timeout must not be negative: " + paramLong);
/*     */     }
/*     */ 
/* 159 */     if (this.stateMap == null)
/*     */     {
/* 164 */       this.stateMap = new HashMap();
/* 165 */       waitForChange(0L);
/*     */     }
/* 167 */     if (paramLong == 0L)
/* 168 */       paramLong = -1L;
/*     */     try
/*     */     {
/* 171 */       String[] arrayOfString = PCSC.SCardListReaders(contextId);
/* 172 */       int i = arrayOfString.length;
/* 173 */       if (i == 0) {
/* 174 */         throw new IllegalStateException("No terminals available");
/*     */       }
/* 176 */       int[] arrayOfInt = new int[i];
/* 177 */       ReaderState[] arrayOfReaderState = new ReaderState[i];
/*     */       Object localObject;
/* 178 */       for (int j = 0; j < arrayOfString.length; j++) {
/* 179 */         localObject = arrayOfString[j];
/* 180 */         ReaderState localReaderState = (ReaderState)this.stateMap.get(localObject);
/* 181 */         if (localReaderState == null) {
/* 182 */           localReaderState = new ReaderState();
/*     */         }
/* 184 */         arrayOfReaderState[j] = localReaderState;
/* 185 */         arrayOfInt[j] = localReaderState.get();
/*     */       }
/* 187 */       arrayOfInt = PCSC.SCardGetStatusChange(contextId, paramLong, arrayOfInt, arrayOfString);
/* 188 */       this.stateMap.clear();
/* 189 */       for (j = 0; j < i; j++) {
/* 190 */         localObject = arrayOfReaderState[j];
/* 191 */         ((ReaderState)localObject).update(arrayOfInt[j]);
/* 192 */         this.stateMap.put(arrayOfString[j], localObject);
/*     */       }
/* 194 */       return true;
/*     */     } catch (PCSCException localPCSCException) {
/* 196 */       if (localPCSCException.code == -2146435062) {
/* 197 */         return false;
/*     */       }
/* 199 */       throw new CardException("waitForChange() failed", localPCSCException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static List<CardTerminal> waitForCards(List<? extends CardTerminal> paramList, long paramLong, boolean paramBoolean)
/*     */     throws CardException
/*     */   {
/*     */     long l;
/* 210 */     if (paramLong == 0L) {
/* 211 */       paramLong = -1L;
/* 212 */       l = -1L;
/*     */     }
/*     */     else
/*     */     {
/* 217 */       l = 0L;
/*     */     }
/*     */ 
/* 220 */     String[] arrayOfString = new String[paramList.size()];
/* 221 */     int i = 0;
/* 222 */     for (Object localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (CardTerminal)((Iterator)localObject1).next();
/* 223 */       if (!(localObject2 instanceof TerminalImpl)) {
/* 224 */         throw new IllegalArgumentException("Invalid terminal type: " + localObject2.getClass().getName());
/*     */       }
/*     */ 
/* 227 */       TerminalImpl localTerminalImpl = (TerminalImpl)localObject2;
/* 228 */       arrayOfString[(i++)] = localTerminalImpl.name;
/*     */     }
/*     */     Object localObject2;
/* 231 */     localObject1 = new int[arrayOfString.length];
/* 232 */     Arrays.fill((int[])localObject1, 0);
/*     */     try
/*     */     {
/*     */       while (true)
/*     */       {
/* 240 */         localObject1 = PCSC.SCardGetStatusChange(contextId, l, (int[])localObject1, arrayOfString);
/* 241 */         l = paramLong;
/*     */ 
/* 243 */         localObject2 = null;
/* 244 */         for (i = 0; i < arrayOfString.length; i++) {
/* 245 */           boolean bool = (localObject1[i] & 0x20) != 0;
/* 246 */           if (bool == paramBoolean) {
/* 247 */             if (localObject2 == null) {
/* 248 */               localObject2 = new ArrayList();
/*     */             }
/* 250 */             ((List)localObject2).add(implGetTerminal(arrayOfString[i]));
/*     */           }
/*     */         }
/*     */ 
/* 254 */         if (localObject2 != null)
/* 255 */           return Collections.unmodifiableList((List)localObject2);
/*     */       }
/*     */     }
/*     */     catch (PCSCException localPCSCException) {
/* 259 */       if (localPCSCException.code == -2146435062) {
/* 260 */         return Collections.emptyList();
/*     */       }
/* 262 */       throw new CardException("waitForCard() failed", localPCSCException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ReaderState
/*     */   {
/*     */     private int current;
/*     */     private int previous;
/*     */ 
/*     */     ReaderState()
/*     */     {
/* 133 */       this.current = 0;
/* 134 */       this.previous = 0;
/*     */     }
/*     */     int get() {
/* 137 */       return this.current;
/*     */     }
/*     */     void update(int paramInt) {
/* 140 */       this.previous = this.current;
/* 141 */       this.current = paramInt;
/*     */     }
/*     */     boolean isInsertion() {
/* 144 */       return (!present(this.previous)) && (present(this.current));
/*     */     }
/*     */     boolean isRemoval() {
/* 147 */       return (present(this.previous)) && (!present(this.current));
/*     */     }
/*     */     static boolean present(int paramInt) {
/* 150 */       return (paramInt & 0x20) != 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.smartcardio.PCSCTerminals
 * JD-Core Version:    0.6.2
 */