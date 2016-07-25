/*    */ package java.util.prefs;
/*    */ 
/*    */ import java.io.NotSerializableException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.EventObject;
/*    */ 
/*    */ public class NodeChangeEvent extends EventObject
/*    */ {
/*    */   private Preferences child;
/*    */   private static final long serialVersionUID = 8068949086596572957L;
/*    */ 
/*    */   public NodeChangeEvent(Preferences paramPreferences1, Preferences paramPreferences2)
/*    */   {
/* 61 */     super(paramPreferences1);
/* 62 */     this.child = paramPreferences2;
/*    */   }
/*    */ 
/*    */   public Preferences getParent()
/*    */   {
/* 71 */     return (Preferences)getSource();
/*    */   }
/*    */ 
/*    */   public Preferences getChild()
/*    */   {
/* 80 */     return this.child;
/*    */   }
/*    */ 
/*    */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*    */     throws NotSerializableException
/*    */   {
/* 89 */     throw new NotSerializableException("Not serializable.");
/*    */   }
/*    */ 
/*    */   private void readObject(ObjectInputStream paramObjectInputStream)
/*    */     throws NotSerializableException
/*    */   {
/* 98 */     throw new NotSerializableException("Not serializable.");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.prefs.NodeChangeEvent
 * JD-Core Version:    0.6.2
 */