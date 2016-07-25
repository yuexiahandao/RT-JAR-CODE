/*    */ package javax.tools;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class DiagnosticCollector<S>
/*    */   implements DiagnosticListener<S>
/*    */ {
/* 42 */   private List<Diagnostic<? extends S>> diagnostics = Collections.synchronizedList(new ArrayList());
/*    */ 
/*    */   public void report(Diagnostic<? extends S> paramDiagnostic)
/*    */   {
/* 46 */     paramDiagnostic.getClass();
/* 47 */     this.diagnostics.add(paramDiagnostic);
/*    */   }
/*    */ 
/*    */   public List<Diagnostic<? extends S>> getDiagnostics()
/*    */   {
/* 56 */     return Collections.unmodifiableList(this.diagnostics);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.DiagnosticCollector
 * JD-Core Version:    0.6.2
 */