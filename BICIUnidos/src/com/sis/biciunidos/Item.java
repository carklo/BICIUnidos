package com.sis.biciunidos;

import java.util.ArrayList;

public class Item
{
  private ArrayList<SubItem> childs;
  private int imagen;
  private String nombre;
  
  public Item(String paramString, ArrayList<SubItem> paramArrayList, int paramInt)
  {
    this.nombre = paramString;
    this.childs = paramArrayList;
    this.imagen = paramInt;
  }
  
  public ArrayList<SubItem> getChilds()
  {
    return this.childs;
  }
  
  public int getImagen()
  {
    return this.imagen;
  }
  
  public String getNombre()
  {
    return this.nombre;
  }
  
  public void setChilds(ArrayList<SubItem> paramArrayList)
  {
    this.childs = paramArrayList;
  }
  
  public void setImagen(int paramInt)
  {
    this.imagen = paramInt;
  }
  
  public void setNombre(String paramString)
  {
    this.nombre = paramString;
  }
}