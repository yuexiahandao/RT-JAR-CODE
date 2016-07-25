/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ public abstract class State<T extends JComponent>
/*     */ {
/*  74 */   static final Map<String, StandardState> standardStates = new HashMap(7);
/*  75 */   static final State Enabled = new StandardState(1, null);
/*  76 */   static final State MouseOver = new StandardState(2, null);
/*  77 */   static final State Pressed = new StandardState(4, null);
/*  78 */   static final State Disabled = new StandardState(8, null);
/*  79 */   static final State Focused = new StandardState(256, null);
/*  80 */   static final State Selected = new StandardState(512, null);
/*  81 */   static final State Default = new StandardState(1024, null);
/*     */   private String name;
/*     */ 
/*     */   protected State(String paramString)
/*     */   {
/* 101 */     this.name = paramString;
/*     */   }
/*     */   public String toString() {
/* 104 */     return this.name;
/*     */   }
/*     */ 
/*     */   boolean isInState(T paramT, int paramInt)
/*     */   {
/* 127 */     return isInState(paramT);
/*     */   }
/*     */ 
/*     */   protected abstract boolean isInState(T paramT);
/*     */ 
/*     */   String getName()
/*     */   {
/* 148 */     return this.name;
/*     */   }
/*     */   static boolean isStandardStateName(String paramString) {
/* 151 */     return standardStates.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   static StandardState getStandardState(String paramString) {
/* 155 */     return (StandardState)standardStates.get(paramString);
/*     */   }
/*     */ 
/*     */   static final class StandardState extends State<JComponent> {
/*     */     private int state;
/*     */ 
/*     */     private StandardState(int paramInt) {
/* 162 */       super();
/* 163 */       this.state = paramInt;
/* 164 */       standardStates.put(getName(), this);
/*     */     }
/*     */ 
/*     */     public int getState() {
/* 168 */       return this.state;
/*     */     }
/*     */ 
/*     */     boolean isInState(JComponent paramJComponent, int paramInt)
/*     */     {
/* 173 */       return (paramInt & this.state) == this.state;
/*     */     }
/*     */ 
/*     */     protected boolean isInState(JComponent paramJComponent)
/*     */     {
/* 178 */       throw new AssertionError("This method should never be called");
/*     */     }
/*     */ 
/*     */     private static String toString(int paramInt) {
/* 182 */       StringBuffer localStringBuffer = new StringBuffer();
/* 183 */       if ((paramInt & 0x400) == 1024) {
/* 184 */         localStringBuffer.append("Default");
/*     */       }
/* 186 */       if ((paramInt & 0x8) == 8) {
/* 187 */         if (localStringBuffer.length() > 0) localStringBuffer.append("+");
/* 188 */         localStringBuffer.append("Disabled");
/*     */       }
/* 190 */       if ((paramInt & 0x1) == 1) {
/* 191 */         if (localStringBuffer.length() > 0) localStringBuffer.append("+");
/* 192 */         localStringBuffer.append("Enabled");
/*     */       }
/* 194 */       if ((paramInt & 0x100) == 256) {
/* 195 */         if (localStringBuffer.length() > 0) localStringBuffer.append("+");
/* 196 */         localStringBuffer.append("Focused");
/*     */       }
/* 198 */       if ((paramInt & 0x2) == 2) {
/* 199 */         if (localStringBuffer.length() > 0) localStringBuffer.append("+");
/* 200 */         localStringBuffer.append("MouseOver");
/*     */       }
/* 202 */       if ((paramInt & 0x4) == 4) {
/* 203 */         if (localStringBuffer.length() > 0) localStringBuffer.append("+");
/* 204 */         localStringBuffer.append("Pressed");
/*     */       }
/* 206 */       if ((paramInt & 0x200) == 512) {
/* 207 */         if (localStringBuffer.length() > 0) localStringBuffer.append("+");
/* 208 */         localStringBuffer.append("Selected");
/*     */       }
/* 210 */       return localStringBuffer.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.State
 * JD-Core Version:    0.6.2
 */