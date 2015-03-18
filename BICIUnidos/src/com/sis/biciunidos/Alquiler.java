package com.sis.biciunidos;

public class Alquiler
{
  private String estructuraEstado;
  private String estructuraObs;
  private int fechaEntrega;
  private String frenoDelanteroEstado;
  private String frenoDelanteroObs;
  private String frenoTraseroEstado;
  private String frenoTraseroObs;
  private String llantaDelanteraEstado;
  private String llantaDelanteraObs;
  private String llantaTraseraEstado;
  private String llantaTraseraObs;
  
  public Alquiler(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10)
  {
    this.fechaEntrega = paramInt;
    this.llantaDelanteraEstado = paramString1;
    this.llantaTraseraEstado = paramString2;
    this.frenoDelanteroEstado = paramString3;
    this.frenoTraseroEstado = paramString4;
    this.llantaDelanteraObs = paramString6;
    this.llantaTraseraObs = paramString7;
    this.frenoDelanteroObs = paramString8;
    this.frenoTraseroObs = paramString9;
    this.estructuraEstado = paramString5;
    this.estructuraObs = paramString10;
  }
  
  public String getEstructuraEstado()
  {
    return this.estructuraEstado;
  }
  
  public String getEstructuraObs()
  {
    return this.estructuraObs;
  }
  
  public int getFechaEntrega()
  {
    return this.fechaEntrega;
  }
  
  public String getFrenoDelanteroEstado()
  {
    return this.frenoDelanteroEstado;
  }
  
  public String getFrenoDelanteroObs()
  {
    return this.frenoDelanteroObs;
  }
  
  public String getFrenoTraseroEstado()
  {
    return this.frenoTraseroEstado;
  }
  
  public String getFrenoTraseroObs()
  {
    return this.frenoTraseroObs;
  }
  
  public String getLlantaDelanteraEstado()
  {
    return this.llantaDelanteraEstado;
  }
  
  public String getLlantaDelanteraObs()
  {
    return this.llantaDelanteraObs;
  }
  
  public String getLlantaTraseraEstado()
  {
    return this.llantaTraseraEstado;
  }
  
  public String getLlantaTraseraObs()
  {
    return this.llantaTraseraObs;
  }
  
  public void setEstructuraEstado(String paramString)
  {
    this.estructuraEstado = paramString;
  }
  
  public void setEstructuraObs(String paramString)
  {
    this.estructuraObs = paramString;
  }
  
  public void setFechaEntrega(int paramInt)
  {
    this.fechaEntrega = paramInt;
  }
  
  public void setFrenoDelanteroEstado(String paramString)
  {
    this.frenoDelanteroEstado = paramString;
  }
  
  public void setFrenoDelanteroObs(String paramString)
  {
    this.frenoDelanteroObs = paramString;
  }
  
  public void setFrenoTraseroEstado(String paramString)
  {
    this.frenoTraseroEstado = paramString;
  }
  
  public void setFrenoTraseroObs(String paramString)
  {
    this.frenoTraseroObs = paramString;
  }
  
  public void setLlantaDelanteraEstado(String paramString)
  {
    this.llantaDelanteraEstado = paramString;
  }
  
  public void setLlantaDelanteraObs(String paramString)
  {
    this.llantaDelanteraObs = paramString;
  }
  
  public void setLlantaTraseraEstado(String paramString)
  {
    this.llantaTraseraEstado = paramString;
  }
  
  public void setLlantaTraseraObs(String paramString)
  {
    this.llantaTraseraObs = paramString;
  }
}


/* Location:           C:\Users\carklo\Desktop\dex2jar-0.0.9.15\classes-dex2jar.jar
 * Qualified Name:     com.sis.biciunidos.Alquiler
 * JD-Core Version:    0.7.0.1
 */