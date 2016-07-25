/*    */ package java.text;
/*    */ 
/*    */ class DontCareFieldPosition extends FieldPosition
/*    */ {
/* 35 */   static final FieldPosition INSTANCE = new DontCareFieldPosition();
/*    */ 
/* 37 */   private final Format.FieldDelegate noDelegate = new Format.FieldDelegate() { public void formatted(Format.Field paramAnonymousField, Object paramAnonymousObject, int paramAnonymousInt1, int paramAnonymousInt2, StringBuffer paramAnonymousStringBuffer) {  } 
/* 37 */     public void formatted(int paramAnonymousInt1, Format.Field paramAnonymousField, Object paramAnonymousObject, int paramAnonymousInt2, int paramAnonymousInt3, StringBuffer paramAnonymousStringBuffer) {  }  } ;
/*    */ 
/*    */   private DontCareFieldPosition()
/*    */   {
/* 47 */     super(0);
/*    */   }
/*    */ 
/*    */   Format.FieldDelegate getFieldDelegate() {
/* 51 */     return this.noDelegate;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.DontCareFieldPosition
 * JD-Core Version:    0.6.2
 */