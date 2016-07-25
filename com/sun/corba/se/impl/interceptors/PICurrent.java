/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.LocalObject;
/*     */ import org.omg.PortableInterceptor.Current;
/*     */ import org.omg.PortableInterceptor.InvalidSlot;
/*     */ 
/*     */ public class PICurrent extends LocalObject
/*     */   implements Current
/*     */ {
/*     */   private int slotCounter;
/*     */   private ORB myORB;
/*     */   private OMGSystemException wrapper;
/*     */   private boolean orbInitializing;
/*  62 */   private ThreadLocal threadLocalSlotTable = new ThreadLocal()
/*     */   {
/*     */     protected Object initialValue() {
/*  65 */       SlotTable localSlotTable = new SlotTable(PICurrent.this.myORB, PICurrent.this.slotCounter);
/*  66 */       return new SlotTableStack(PICurrent.this.myORB, localSlotTable);
/*     */     }
/*  62 */   };
/*     */ 
/*     */   PICurrent(ORB paramORB)
/*     */   {
/*  75 */     this.myORB = paramORB;
/*  76 */     this.wrapper = OMGSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/*  78 */     this.orbInitializing = true;
/*  79 */     this.slotCounter = 0;
/*     */   }
/*     */ 
/*     */   int allocateSlotId()
/*     */   {
/*  88 */     int i = this.slotCounter;
/*  89 */     this.slotCounter += 1;
/*  90 */     return i;
/*     */   }
/*     */ 
/*     */   SlotTable getSlotTable()
/*     */   {
/*  99 */     SlotTable localSlotTable = ((SlotTableStack)this.threadLocalSlotTable.get()).peekSlotTable();
/*     */ 
/* 101 */     return localSlotTable;
/*     */   }
/*     */ 
/*     */   void pushSlotTable()
/*     */   {
/* 110 */     SlotTableStack localSlotTableStack = (SlotTableStack)this.threadLocalSlotTable.get();
/* 111 */     localSlotTableStack.pushSlotTable();
/*     */   }
/*     */ 
/*     */   void popSlotTable()
/*     */   {
/* 119 */     SlotTableStack localSlotTableStack = (SlotTableStack)this.threadLocalSlotTable.get();
/* 120 */     localSlotTableStack.popSlotTable();
/*     */   }
/*     */ 
/*     */   public void set_slot(int paramInt, Any paramAny)
/*     */     throws InvalidSlot
/*     */   {
/* 129 */     if (this.orbInitializing)
/*     */     {
/* 133 */       throw this.wrapper.invalidPiCall3();
/*     */     }
/*     */ 
/* 136 */     getSlotTable().set_slot(paramInt, paramAny);
/*     */   }
/*     */ 
/*     */   public Any get_slot(int paramInt)
/*     */     throws InvalidSlot
/*     */   {
/* 145 */     if (this.orbInitializing)
/*     */     {
/* 149 */       throw this.wrapper.invalidPiCall4();
/*     */     }
/*     */ 
/* 152 */     return getSlotTable().get_slot(paramInt);
/*     */   }
/*     */ 
/*     */   void resetSlotTable()
/*     */   {
/* 160 */     getSlotTable().resetSlots();
/*     */   }
/*     */ 
/*     */   void setORBInitializing(boolean paramBoolean)
/*     */   {
/* 168 */     this.orbInitializing = paramBoolean;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.PICurrent
 * JD-Core Version:    0.6.2
 */