/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.InterceptorsSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SlotTableStack
/*     */ {
/*     */   private List tableContainer;
/*     */   private int currentIndex;
/*     */   private SlotTablePool tablePool;
/*     */   private ORB orb;
/*     */   private InterceptorsSystemException wrapper;
/*     */ 
/*     */   SlotTableStack(ORB paramORB, SlotTable paramSlotTable)
/*     */   {
/* 112 */     this.orb = paramORB;
/* 113 */     this.wrapper = InterceptorsSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/* 115 */     this.currentIndex = 0;
/* 116 */     this.tableContainer = new ArrayList();
/* 117 */     this.tablePool = new SlotTablePool();
/*     */ 
/* 121 */     this.tableContainer.add(this.currentIndex, paramSlotTable);
/* 122 */     this.currentIndex += 1;
/*     */   }
/*     */ 
/*     */   void pushSlotTable()
/*     */   {
/* 136 */     SlotTable localSlotTable1 = this.tablePool.getSlotTable();
/* 137 */     if (localSlotTable1 == null)
/*     */     {
/* 139 */       SlotTable localSlotTable2 = peekSlotTable();
/* 140 */       localSlotTable1 = new SlotTable(this.orb, localSlotTable2.getSize());
/*     */     }
/*     */ 
/* 143 */     if (this.currentIndex == this.tableContainer.size())
/*     */     {
/* 145 */       this.tableContainer.add(this.currentIndex, localSlotTable1); } else {
/* 146 */       if (this.currentIndex > this.tableContainer.size()) {
/* 147 */         throw this.wrapper.slotTableInvariant(new Integer(this.currentIndex), new Integer(this.tableContainer.size()));
/*     */       }
/*     */ 
/* 151 */       this.tableContainer.set(this.currentIndex, localSlotTable1);
/*     */     }
/* 153 */     this.currentIndex += 1;
/*     */   }
/*     */ 
/*     */   void popSlotTable()
/*     */   {
/* 166 */     if (this.currentIndex <= 1)
/*     */     {
/* 169 */       throw this.wrapper.cantPopOnlyPicurrent();
/*     */     }
/* 171 */     this.currentIndex -= 1;
/* 172 */     SlotTable localSlotTable = (SlotTable)this.tableContainer.get(this.currentIndex);
/* 173 */     this.tableContainer.set(this.currentIndex, null);
/* 174 */     localSlotTable.resetSlots();
/* 175 */     this.tablePool.putSlotTable(localSlotTable);
/*     */   }
/*     */ 
/*     */   SlotTable peekSlotTable()
/*     */   {
/* 183 */     return (SlotTable)this.tableContainer.get(this.currentIndex - 1);
/*     */   }
/*     */ 
/*     */   private class SlotTablePool
/*     */   {
/*     */     private SlotTable[] pool;
/*  54 */     private final int HIGH_WATER_MARK = 5;
/*     */     private int currentIndex;
/*     */ 
/*     */     SlotTablePool()
/*     */     {
/*  60 */       this.pool = new SlotTable[5];
/*  61 */       this.currentIndex = 0;
/*     */     }
/*     */ 
/*     */     void putSlotTable(SlotTable paramSlotTable)
/*     */     {
/*  70 */       if (this.currentIndex >= 5)
/*     */       {
/*  72 */         return;
/*     */       }
/*  74 */       this.pool[this.currentIndex] = paramSlotTable;
/*  75 */       this.currentIndex += 1;
/*     */     }
/*     */ 
/*     */     SlotTable getSlotTable()
/*     */     {
/*  83 */       if (this.currentIndex == 0) {
/*  84 */         return null;
/*     */       }
/*     */ 
/*  87 */       this.currentIndex -= 1;
/*  88 */       return this.pool[this.currentIndex];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.SlotTableStack
 * JD-Core Version:    0.6.2
 */