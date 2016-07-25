/*    */ package com.sun.jmx.remote.internal;
/*    */ 
/*    */ import java.util.AbstractList;
/*    */ 
/*    */ public class ArrayQueue<T> extends AbstractList<T>
/*    */ {
/*    */   private int capacity;
/*    */   private T[] queue;
/*    */   private int head;
/*    */   private int tail;
/*    */ 
/*    */   public ArrayQueue(int paramInt)
/*    */   {
/* 33 */     this.capacity = (paramInt + 1);
/* 34 */     this.queue = newArray(paramInt + 1);
/* 35 */     this.head = 0;
/* 36 */     this.tail = 0;
/*    */   }
/*    */ 
/*    */   public void resize(int paramInt) {
/* 40 */     int i = size();
/* 41 */     if (paramInt < i)
/* 42 */       throw new IndexOutOfBoundsException("Resizing would lose data");
/* 43 */     paramInt++;
/* 44 */     if (paramInt == this.capacity)
/* 45 */       return;
/* 46 */     Object[] arrayOfObject = newArray(paramInt);
/* 47 */     for (int j = 0; j < i; j++)
/* 48 */       arrayOfObject[j] = get(j);
/* 49 */     this.capacity = paramInt;
/* 50 */     this.queue = arrayOfObject;
/* 51 */     this.head = 0;
/* 52 */     this.tail = i;
/*    */   }
/*    */ 
/*    */   private T[] newArray(int paramInt)
/*    */   {
/* 57 */     return (Object[])new Object[paramInt];
/*    */   }
/*    */ 
/*    */   public boolean add(T paramT) {
/* 61 */     this.queue[this.tail] = paramT;
/* 62 */     int i = (this.tail + 1) % this.capacity;
/* 63 */     if (i == this.head)
/* 64 */       throw new IndexOutOfBoundsException("Queue full");
/* 65 */     this.tail = i;
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   public T remove(int paramInt) {
/* 70 */     if (paramInt != 0)
/* 71 */       throw new IllegalArgumentException("Can only remove head of queue");
/* 72 */     if (this.head == this.tail)
/* 73 */       throw new IndexOutOfBoundsException("Queue empty");
/* 74 */     Object localObject = this.queue[this.head];
/* 75 */     this.queue[this.head] = null;
/* 76 */     this.head = ((this.head + 1) % this.capacity);
/* 77 */     return localObject;
/*    */   }
/*    */ 
/*    */   public T get(int paramInt) {
/* 81 */     int i = size();
/* 82 */     if ((paramInt < 0) || (paramInt >= i)) {
/* 83 */       String str = "Index " + paramInt + ", queue size " + i;
/* 84 */       throw new IndexOutOfBoundsException(str);
/*    */     }
/* 86 */     int j = (this.head + paramInt) % this.capacity;
/* 87 */     return this.queue[j];
/*    */   }
/*    */ 
/*    */   public int size()
/*    */   {
/* 92 */     int i = this.tail - this.head;
/* 93 */     if (i < 0)
/* 94 */       i += this.capacity;
/* 95 */     return i;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.ArrayQueue
 * JD-Core Version:    0.6.2
 */