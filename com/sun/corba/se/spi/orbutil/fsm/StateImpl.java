/*    */ package com.sun.corba.se.spi.orbutil.fsm;
/*    */ 
/*    */ import com.sun.corba.se.impl.orbutil.fsm.GuardedAction;
/*    */ import com.sun.corba.se.impl.orbutil.fsm.NameBase;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class StateImpl extends NameBase
/*    */   implements State
/*    */ {
/*    */   private Action defaultAction;
/*    */   private State defaultNextState;
/*    */   private Map inputToGuardedActions;
/*    */ 
/*    */   public StateImpl(String paramString)
/*    */   {
/* 48 */     super(paramString);
/* 49 */     this.defaultAction = null;
/* 50 */     this.inputToGuardedActions = new HashMap();
/*    */   }
/*    */ 
/*    */   public void preAction(FSM paramFSM)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void postAction(FSM paramFSM)
/*    */   {
/*    */   }
/*    */ 
/*    */   public State getDefaultNextState()
/*    */   {
/* 65 */     return this.defaultNextState;
/*    */   }
/*    */ 
/*    */   public void setDefaultNextState(State paramState)
/*    */   {
/* 70 */     this.defaultNextState = paramState;
/*    */   }
/*    */ 
/*    */   public Action getDefaultAction()
/*    */   {
/* 75 */     return this.defaultAction;
/*    */   }
/*    */ 
/*    */   public void setDefaultAction(Action paramAction)
/*    */   {
/* 80 */     this.defaultAction = paramAction;
/*    */   }
/*    */ 
/*    */   public void addGuardedAction(Input paramInput, GuardedAction paramGuardedAction)
/*    */   {
/* 85 */     Object localObject = (Set)this.inputToGuardedActions.get(paramInput);
/* 86 */     if (localObject == null) {
/* 87 */       localObject = new HashSet();
/* 88 */       this.inputToGuardedActions.put(paramInput, localObject);
/*    */     }
/*    */ 
/* 91 */     ((Set)localObject).add(paramGuardedAction);
/*    */   }
/*    */ 
/*    */   public Set getGuardedActions(Input paramInput)
/*    */   {
/* 96 */     return (Set)this.inputToGuardedActions.get(paramInput);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.fsm.StateImpl
 * JD-Core Version:    0.6.2
 */