package utilitarios.ged;

import java.util.ArrayList;


/**
 * Conversor de dados para o Ged
 */
class ConversorDados{
   
   /**
    * Objeto responsável por interpretar arquivos csv já lidos no formato 
    * {@code ArrayList<String[]>} e convertê-los em dados numéricos.
    */
   public ConversorDados(){

   }


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
