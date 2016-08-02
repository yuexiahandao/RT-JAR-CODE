package sun.security.tools.policytool;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.LinkedList;
import java.util.ListIterator;

import sun.security.provider.PolicyParser.GrantEntry;
import sun.security.provider.PolicyParser.PermissionEntry;
import sun.security.provider.PolicyParser.PrincipalEntry;

/**
 * policy入口
 */
class PolicyEntry {
    /**
     * 代码源
     */
    private CodeSource codesource;
    /**
     * policy工具类
     */
    private PolicyTool tool;
    /**
     * grant实体
     */
    private PolicyParser.GrantEntry grantEntry;
    private boolean testing = false;

    PolicyEntry(PolicyTool paramPolicyTool, PolicyParser.GrantEntry paramGrantEntry)
            throws MalformedURLException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
        this.tool = paramPolicyTool;

        URL localURL = null;

        if (paramGrantEntry.codeBase != null)
            localURL = new URL(paramGrantEntry.codeBase);
        this.codesource = new CodeSource(localURL, (Certificate[]) null);

        if (this.testing) {
            System.out.println("Adding Policy Entry:");
            System.out.println("    CodeBase = " + localURL);
            System.out.println("    Signers = " + paramGrantEntry.signedBy);
            System.out.println("    with " + paramGrantEntry.principals.size() + " Principals");
        }

        this.grantEntry = paramGrantEntry;
    }

    CodeSource getCodeSource() {
        return this.codesource;
    }

    PolicyParser.GrantEntry getGrantEntry() {
        return this.grantEntry;
    }

    String headerToString() {
        String str = principalsToString();
        if (str.length() == 0) {
            return codebaseToString();
        }
        return codebaseToString() + ", " + str;
    }

    String codebaseToString() {
        String str = new String();

        if ((this.grantEntry.codeBase != null) && (!this.grantEntry.codeBase.equals(""))) {
            str = str.concat("CodeBase \"" + this.grantEntry.codeBase + "\"");
        }

        if ((this.grantEntry.signedBy != null) && (!this.grantEntry.signedBy.equals(""))) {
            str = str.length() > 0 ? str.concat(", SignedBy \"" + this.grantEntry.signedBy + "\"") : str.concat("SignedBy \"" + this.grantEntry.signedBy + "\"");
        }

        if (str.length() == 0)
            return new String("CodeBase <ALL>");
        return str;
    }

    String principalsToString() {
        String str = "";
        if ((this.grantEntry.principals != null) && (!this.grantEntry.principals.isEmpty())) {
            StringBuffer localStringBuffer = new StringBuffer(200);
            ListIterator localListIterator = this.grantEntry.principals.listIterator();

            while (localListIterator.hasNext()) {
                PolicyParser.PrincipalEntry localPrincipalEntry = (PolicyParser.PrincipalEntry) localListIterator.next();
                localStringBuffer.append(" Principal " + localPrincipalEntry.getDisplayClass() + " " + localPrincipalEntry.getDisplayName(true));

                if (localListIterator.hasNext()) localStringBuffer.append(", ");
            }
            str = localStringBuffer.toString();
        }
        return str;
    }

    PolicyParser.PermissionEntry toPermissionEntry(Permission paramPermission) {
        String str = null;

        if ((paramPermission.getActions() != null) && (paramPermission.getActions().trim() != "")) {
            str = paramPermission.getActions();
        }
        PolicyParser.PermissionEntry localPermissionEntry = new PolicyParser.PermissionEntry(paramPermission.getClass().getName(), paramPermission.getName(), str);

        return localPermissionEntry;
    }
}
