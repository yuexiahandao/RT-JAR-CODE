/*    */ package com.sun.xml.internal.ws.api.pipe;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
/*    */ import java.util.Map;
/*    */ 
/*    */ /** @deprecated */
/*    */ public final class PipeCloner extends TubeCloner
/*    */ {
/*    */   public static Pipe clone(Pipe p)
/*    */   {
/* 47 */     return new PipeCloner().copy(p);
/*    */   }
/*    */ 
/*    */   public <T extends Pipe> T copy(T p)
/*    */   {
/* 57 */     Pipe r = (Pipe)this.master2copy.get(p);
/* 58 */     if (r == null) {
/* 59 */       r = p.copy(this);
/*    */ 
/* 61 */       assert (this.master2copy.get(p) == r) : ("the pipe must call the add(...) method to register itself before start copying other pipes, but " + p + " hasn't done so");
/*    */     }
/* 63 */     return r;
/*    */   }
/*    */ 
/*    */   public void add(Pipe original, Pipe copy)
/*    */   {
/* 71 */     assert (!this.master2copy.containsKey(original));
/* 72 */     assert ((original != null) && (copy != null));
/* 73 */     this.master2copy.put(original, copy);
/*    */   }
/*    */ 
/*    */   public void add(AbstractTubeImpl original, AbstractTubeImpl copy)
/*    */   {
/* 80 */     add(original, copy);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.PipeCloner
 * JD-Core Version:    0.6.2
 */