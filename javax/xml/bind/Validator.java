package javax.xml.bind;

/** @deprecated */
public abstract interface Validator
{
  /** @deprecated */
  public abstract void setEventHandler(ValidationEventHandler paramValidationEventHandler)
    throws JAXBException;

  /** @deprecated */
  public abstract ValidationEventHandler getEventHandler()
    throws JAXBException;

  /** @deprecated */
  public abstract boolean validate(Object paramObject)
    throws JAXBException;

  /** @deprecated */
  public abstract boolean validateRoot(Object paramObject)
    throws JAXBException;

  /** @deprecated */
  public abstract void setProperty(String paramString, Object paramObject)
    throws PropertyException;

  /** @deprecated */
  public abstract Object getProperty(String paramString)
    throws PropertyException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.Validator
 * JD-Core Version:    0.6.2
 */