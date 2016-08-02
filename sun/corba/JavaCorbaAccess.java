package sun.corba;

import com.sun.corba.se.impl.io.ValueHandlerImpl;

public abstract interface JavaCorbaAccess {
    public abstract ValueHandlerImpl newValueHandlerImpl();

    public abstract Class<?> loadClass(String paramString)
            throws ClassNotFoundException;
}
