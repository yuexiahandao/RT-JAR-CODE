package sun.security.action;

import java.security.PrivilegedAction;

/**
 * 用于加载系统库
 */
public class LoadLibraryAction
        implements PrivilegedAction<Void> {
    private String theLib;

    public LoadLibraryAction(String paramString) {
        this.theLib = paramString;
    }

    public Void run() {
        System.loadLibrary(this.theLib);
        return null;
    }
}
