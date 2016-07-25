package javax.xml.soap;

public abstract interface SOAPHeaderElement extends SOAPElement
{
  public abstract void setActor(String paramString);

  public abstract void setRole(String paramString)
    throws SOAPException;

  public abstract String getActor();

  public abstract String getRole();

  public abstract void setMustUnderstand(boolean paramBoolean);

  public abstract boolean getMustUnderstand();

  public abstract void setRelay(boolean paramBoolean)
    throws SOAPException;

  public abstract boolean getRelay();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPHeaderElement
 * JD-Core Version:    0.6.2
 */