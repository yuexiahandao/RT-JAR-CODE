/*    */ package sun.rmi.transport;
/*    */ 
/*    */ import java.rmi.server.ObjID;
/*    */ 
/*    */ class ObjectEndpoint
/*    */ {
/*    */   private final ObjID id;
/*    */   private final Transport transport;
/*    */ 
/*    */   ObjectEndpoint(ObjID paramObjID, Transport paramTransport)
/*    */   {
/* 51 */     if (paramObjID == null) {
/* 52 */       throw new NullPointerException();
/*    */     }
/* 54 */     assert ((paramTransport != null) || (paramObjID.equals(new ObjID(2))));
/*    */ 
/* 56 */     this.id = paramObjID;
/* 57 */     this.transport = paramTransport;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 69 */     if ((paramObject instanceof ObjectEndpoint)) {
/* 70 */       ObjectEndpoint localObjectEndpoint = (ObjectEndpoint)paramObject;
/* 71 */       return (this.id.equals(localObjectEndpoint.id)) && (this.transport == localObjectEndpoint.transport);
/*    */     }
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 81 */     return this.id.hashCode() ^ (this.transport != null ? this.transport.hashCode() : 0);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 88 */     return this.id.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.ObjectEndpoint
 * JD-Core Version:    0.6.2
 */