package javax.xml.soap;

import java.util.Iterator;
import java.util.Locale;
import javax.xml.namespace.QName;

public abstract interface SOAPFault extends SOAPBodyElement
{
  public abstract void setFaultCode(Name paramName)
    throws SOAPException;

  public abstract void setFaultCode(QName paramQName)
    throws SOAPException;

  public abstract void setFaultCode(String paramString)
    throws SOAPException;

  public abstract Name getFaultCodeAsName();

  public abstract QName getFaultCodeAsQName();

  public abstract Iterator getFaultSubcodes();

  public abstract void removeAllFaultSubcodes();

  public abstract void appendFaultSubcode(QName paramQName)
    throws SOAPException;

  public abstract String getFaultCode();

  public abstract void setFaultActor(String paramString)
    throws SOAPException;

  public abstract String getFaultActor();

  public abstract void setFaultString(String paramString)
    throws SOAPException;

  public abstract void setFaultString(String paramString, Locale paramLocale)
    throws SOAPException;

  public abstract String getFaultString();

  public abstract Locale getFaultStringLocale();

  public abstract boolean hasDetail();

  public abstract Detail getDetail();

  public abstract Detail addDetail()
    throws SOAPException;

  public abstract Iterator getFaultReasonLocales()
    throws SOAPException;

  public abstract Iterator getFaultReasonTexts()
    throws SOAPException;

  public abstract String getFaultReasonText(Locale paramLocale)
    throws SOAPException;

  public abstract void addFaultReasonText(String paramString, Locale paramLocale)
    throws SOAPException;

  public abstract String getFaultNode();

  public abstract void setFaultNode(String paramString)
    throws SOAPException;

  public abstract String getFaultRole();

  public abstract void setFaultRole(String paramString)
    throws SOAPException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPFault
 * JD-Core Version:    0.6.2
 */