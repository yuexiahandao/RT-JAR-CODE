/*     */ package com.sun.corba.se.spi.orbutil.fsm;
/*     */ 
/*     */ public class FSMTest
/*     */ {
/* 149 */   public static final State STATE1 = new StateImpl("1");
/* 150 */   public static final State STATE2 = new StateImpl("2");
/* 151 */   public static final State STATE3 = new StateImpl("3");
/* 152 */   public static final State STATE4 = new StateImpl("4");
/*     */ 
/* 154 */   public static final Input INPUT1 = new InputImpl("1");
/* 155 */   public static final Input INPUT2 = new InputImpl("2");
/* 156 */   public static final Input INPUT3 = new InputImpl("3");
/* 157 */   public static final Input INPUT4 = new InputImpl("4");
/*     */ 
/* 159 */   private Guard counterGuard = new Guard()
/*     */   {
/*     */     public Guard.Result evaluate(FSM paramAnonymousFSM, Input paramAnonymousInput) {
/* 162 */       MyFSM localMyFSM = (MyFSM)paramAnonymousFSM;
/* 163 */       return Guard.Result.convert(localMyFSM.counter < 3);
/*     */     }
/* 159 */   };
/*     */ 
/*     */   private static void add1(StateEngine paramStateEngine, State paramState1, Input paramInput, State paramState2)
/*     */   {
/* 169 */     paramStateEngine.add(paramState1, paramInput, new TestAction1(paramState1, paramInput, paramState2), paramState2);
/*     */   }
/*     */ 
/*     */   private static void add2(StateEngine paramStateEngine, State paramState1, State paramState2)
/*     */   {
/* 174 */     paramStateEngine.setDefault(paramState1, new TestAction2(paramState1, paramState2), paramState2);
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 179 */     TestAction3 localTestAction3 = new TestAction3(STATE3, INPUT1);
/*     */ 
/* 181 */     StateEngine localStateEngine = StateEngineFactory.create();
/* 182 */     add1(localStateEngine, STATE1, INPUT1, STATE1);
/* 183 */     add2(localStateEngine, STATE1, STATE2);
/*     */ 
/* 185 */     add1(localStateEngine, STATE2, INPUT1, STATE2);
/* 186 */     add1(localStateEngine, STATE2, INPUT2, STATE2);
/* 187 */     add1(localStateEngine, STATE2, INPUT3, STATE1);
/* 188 */     add1(localStateEngine, STATE2, INPUT4, STATE3);
/*     */ 
/* 190 */     localStateEngine.add(STATE3, INPUT1, localTestAction3, STATE3);
/* 191 */     localStateEngine.add(STATE3, INPUT1, localTestAction3, STATE4);
/* 192 */     add1(localStateEngine, STATE3, INPUT2, STATE1);
/* 193 */     add1(localStateEngine, STATE3, INPUT3, STATE2);
/* 194 */     add1(localStateEngine, STATE3, INPUT4, STATE2);
/*     */ 
/* 196 */     MyFSM localMyFSM = new MyFSM(localStateEngine);
/* 197 */     TestInput localTestInput1 = new TestInput(INPUT1, "1.1");
/* 198 */     TestInput localTestInput2 = new TestInput(INPUT1, "1.2");
/* 199 */     TestInput localTestInput3 = new TestInput(INPUT2, "2.1");
/* 200 */     TestInput localTestInput4 = new TestInput(INPUT2, "2.2");
/* 201 */     TestInput localTestInput5 = new TestInput(INPUT3, "3.1");
/* 202 */     TestInput localTestInput6 = new TestInput(INPUT3, "3.2");
/* 203 */     TestInput localTestInput7 = new TestInput(INPUT3, "3.3");
/* 204 */     TestInput localTestInput8 = new TestInput(INPUT4, "4.1");
/*     */ 
/* 206 */     localMyFSM.doIt(localTestInput1.getInput());
/* 207 */     localMyFSM.doIt(localTestInput2.getInput());
/* 208 */     localMyFSM.doIt(localTestInput8.getInput());
/* 209 */     localMyFSM.doIt(localTestInput1.getInput());
/* 210 */     localMyFSM.doIt(localTestInput4.getInput());
/* 211 */     localMyFSM.doIt(localTestInput5.getInput());
/* 212 */     localMyFSM.doIt(localTestInput7.getInput());
/* 213 */     localMyFSM.doIt(localTestInput8.getInput());
/* 214 */     localMyFSM.doIt(localTestInput8.getInput());
/* 215 */     localMyFSM.doIt(localTestInput8.getInput());
/* 216 */     localMyFSM.doIt(localTestInput4.getInput());
/* 217 */     localMyFSM.doIt(localTestInput6.getInput());
/* 218 */     localMyFSM.doIt(localTestInput8.getInput());
/* 219 */     localMyFSM.doIt(localTestInput1.getInput());
/* 220 */     localMyFSM.doIt(localTestInput2.getInput());
/* 221 */     localMyFSM.doIt(localTestInput1.getInput());
/* 222 */     localMyFSM.doIt(localTestInput1.getInput());
/* 223 */     localMyFSM.doIt(localTestInput1.getInput());
/* 224 */     localMyFSM.doIt(localTestInput1.getInput());
/* 225 */     localMyFSM.doIt(localTestInput1.getInput());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.fsm.FSMTest
 * JD-Core Version:    0.6.2
 */