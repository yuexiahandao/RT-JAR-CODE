/*    */ package sun.org.mozilla.javascript.internal;
/*    */ 
/*    */ public class NativeJavaConstructor extends BaseFunction
/*    */ {
/*    */   MemberBox ctor;
/*    */ 
/*    */   public NativeJavaConstructor(MemberBox paramMemberBox)
/*    */   {
/* 63 */     this.ctor = paramMemberBox;
/*    */   }
/*    */ 
/*    */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*    */   {
/* 70 */     return NativeJavaClass.constructSpecific(paramContext, paramScriptable1, paramArrayOfObject, this.ctor);
/*    */   }
/*    */ 
/*    */   public String getFunctionName()
/*    */   {
/* 76 */     String str = JavaMembers.liveConnectSignature(this.ctor.argTypes);
/* 77 */     return "<init>".concat(str);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 83 */     return "[JavaConstructor " + this.ctor.getName() + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeJavaConstructor
 * JD-Core Version:    0.6.2
 */