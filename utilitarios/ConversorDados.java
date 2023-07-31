package utilitarios;

import java.util.ArrayList;

public class ConversorDados{
   
   /**
    * Objeto responsável por interpretar arquivos csv já lidos no formato 
    * {@code ArrayList<String[]>} e convertê-los em dados numéricos.
    */
   public ConversorDados(){

   }


   /**
    * Converte a lista lida em csv para uma matriz bidimensional com os valores numéricos.
    * @param lista lista com os dados.
    * @return matriz convertida para valores tipo integer.
    */
   public int[][] listaParaDadosInt(ArrayList<String[]> lista){
      int[][] dados = new int[lista.size()][lista.get(0).length];
      String buffer = "";

      for(int i = 0; i < lista.size(); i++){
         for(int j = 0; j < lista.get(i).length; j++){
            try{
               //cuidado na hora de converter
               buffer = lista.get(i)[j].replaceAll(" ", "");
               dados[i][j] = Integer.parseInt(buffer);
            }catch(Exception e){
               e.printStackTrace();
            }
         }
      }
   
      return dados;
   }

   
   /**
    * Converte a lista lida em csv para uma matriz bidimensional com os valores numéricos
    * @param lista lista com os dados 
    * @return matriz convertida para valores tipo float.
    */
   public float[][] listaParaDadosFloat(ArrayList<String[]> lista){
      float[][] dados = new float[lista.size()][lista.get(0).length];

      for(int i = 0; i < lista.size(); i++){
         for(int j = 0; j < lista.get(i).length; j++){
            try{
               dados[i][j] = Float.parseFloat(lista.get(i)[j]);
            }catch(Exception e){
               e.printStackTrace();
            }
         }
      }
   
      return dados;
   }

   
   /**
    * Converte a lista lida em csv para uma matriz bidimensional com os valores numéricos
    * @param lista lista com os dados 
    * @return matriz convertida para valores tipo double.
    */
   public double[][] listaParaDadosDouble(ArrayList<String[]> lista){
      double[][] dados = new double[lista.size()][lista.get(0).length];

      for(int i = 0; i < lista.size(); i++){
         for(int j = 0; j < lista.get(i).length; j++){
            try{
               dados[i][j] = Double.parseDouble(lista.get(i)[j]);
            }catch(Exception e){
               e.printStackTrace();
            }
         }
      }
   
      return dados;
   }
}
