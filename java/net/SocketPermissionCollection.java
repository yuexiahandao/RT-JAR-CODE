/*      */ package java.net;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.Serializable;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.List;
/*      */ import java.util.Vector;
/*      */ 
/*      */ final class SocketPermissionCollection extends PermissionCollection
/*      */   implements Serializable
/*      */ {
/*      */   private transient List perms;
/*      */   private static final long serialVersionUID = 2787186408602843674L;
/* 1439 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("permissions", Vector.class) };
/*      */ 
/*      */   public SocketPermissionCollection()
/*      */   {
/* 1340 */     this.perms = new ArrayList();
/*      */   }
/*      */ 
/*      */   public void add(Permission paramPermission)
/*      */   {
/* 1358 */     if (!(paramPermission instanceof SocketPermission)) {
/* 1359 */       throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*      */     }
/* 1361 */     if (isReadOnly()) {
/* 1362 */       throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
/*      */     }
/*      */ 
/* 1367 */     synchronized (this) {
/* 1368 */       this.perms.add(0, paramPermission);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean implies(Permission paramPermission)
/*      */   {
/* 1384 */     if (!(paramPermission instanceof SocketPermission)) {
/* 1385 */       return false;
/*      */     }
/* 1387 */     SocketPermission localSocketPermission1 = (SocketPermission)paramPermission;
/*      */ 
/* 1389 */     int i = localSocketPermission1.getMask();
/* 1390 */     int j = 0;
/* 1391 */     int k = i;
/*      */ 
/* 1393 */     synchronized (this) {
/* 1394 */       int m = this.perms.size();
/*      */ 
/* 1396 */       for (int n = 0; n < m; n++) {
/* 1397 */         SocketPermission localSocketPermission2 = (SocketPermission)this.perms.get(n);
/*      */ 
/* 1399 */         if (((k & localSocketPermission2.getMask()) != 0) && (localSocketPermission2.impliesIgnoreMask(localSocketPermission1))) {
/* 1400 */           j |= localSocketPermission2.getMask();
/* 1401 */           if ((j & i) == i)
/* 1402 */             return true;
/* 1403 */           k = i ^ j;
/*      */         }
/*      */       }
/*      */     }
/* 1407 */     return false;
/*      */   }
/*      */ 
/*      */   public Enumeration elements()
/*      */   {
/* 1419 */     synchronized (this) {
/* 1420 */       return Collections.enumeration(this.perms);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1454 */     Vector localVector = new Vector(this.perms.size());
/*      */ 
/* 1456 */     synchronized (this) {
/* 1457 */       localVector.addAll(this.perms);
/*      */     }
/*      */ 
/* 1460 */     ??? = paramObjectOutputStream.putFields();
/* 1461 */     ((ObjectOutputStream.PutField)???).put("permissions", localVector);
/* 1462 */     paramObjectOutputStream.writeFields();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1473 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*      */ 
/* 1476 */     Vector localVector = (Vector)localGetField.get("permissions", null);
/* 1477 */     this.perms = new ArrayList(localVector.size());
/* 1478 */     this.perms.addAll(localVector);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.SocketPermissionCollection
 * JD-Core Version:    0.6.2
 */