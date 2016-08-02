package java.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

final class PermissionsHash extends PermissionCollection
        implements Serializable {
    private transient Map<Permission, Permission> permsMap;
    private static final long serialVersionUID = -8491988220802933440L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("perms", Hashtable.class)};

    PermissionsHash() {
        this.permsMap = new HashMap(11);
    }

    public void add(Permission paramPermission) {
        synchronized (this) {
            this.permsMap.put(paramPermission, paramPermission);
        }
    }

    public boolean implies(Permission paramPermission) {
        synchronized (this) {
            Permission localPermission1 = (Permission) this.permsMap.get(paramPermission);

            if (localPermission1 == null) {
                for (Permission localPermission2 : this.permsMap.values()) {
                    if (localPermission2.implies(paramPermission))
                        return true;
                }
                return false;
            }
            return true;
        }
    }

    public Enumeration<Permission> elements() {
        synchronized (this) {
            return Collections.enumeration(this.permsMap.values());
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream)
            throws IOException {
        Hashtable localHashtable = new Hashtable(this.permsMap.size() * 2);

        synchronized (this) {
            localHashtable.putAll(this.permsMap);
        }

        ???=paramObjectOutputStream.putFields();
        ((ObjectOutputStream.PutField) ? ??).put("perms", localHashtable);
        paramObjectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream paramObjectInputStream)
            throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();

        Hashtable localHashtable = (Hashtable) localGetField.get("perms", null);

        this.permsMap = new HashMap(localHashtable.size() * 2);
        this.permsMap.putAll(localHashtable);
    }
}
