package utilitarios;

import java.util.ArrayList;

public class ConversorDados{
   
   public ConversorDados(){

   }

   
   /**
    * Converte a lista lida em csv para uma matriz bidimensional com os valores numéricos
    * @param lista lista com os dados 
    * @return matriz convertida para valores tipo double
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
