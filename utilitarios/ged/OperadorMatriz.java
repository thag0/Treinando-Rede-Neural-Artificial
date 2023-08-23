package utilitarios.ged;


/**
 * Operador de matrizes do Ged.
 */
class OperadorMatriz{


   /**
    * Contém implementações de operações matriciais para dados
    * int, float e double.
    */
   public OperadorMatriz(){

   }
   
   public void preencherMatriz(int[][] matriz, int valor){
      for(int i = 0; i < matriz.length; i++){
         for(int j = 0; j < matriz[i].length; j++){
            matriz[i][j] = valor;
         }
      }
   }
   
   
   public void preencherMatriz(float[][] matriz, float valor){
      for(int i = 0; i < matriz.length; i++){
         for(int j = 0; j < matriz[i].length; j++){
            matriz[i][j] = valor;
         }
      }   
   }
   

   public void preencherMatriz(double[][] matriz, double valor){
      for(int i = 0; i < matriz.length; i++){
         for(int j = 0; j < matriz[i].length; j++){
            matriz[i][j] = valor;
         }
      }   
   }


   public void matrizIdentidade(int[][] matriz){
      for(int i = 0; i < matriz.length; i++){
         for(int j = 0; j < matriz[i].length; j++){
            matriz[i][j] = (i == j) ? 1 : 0;
         }
      }       
   }


   public void matrizIdentidade(float[][] matriz){
      for(int i = 0; i < matriz.length; i++){
         for(int j = 0; j < matriz[i].length; j++){
            matriz[i][j] = (i == j) ? 1 : 0;
         }
      }       
   }


   public void matrizIdentidade(double[][] matriz){
      for(int i = 0; i < matriz.length; i++){
         for(int j = 0; j < matriz[i].length; j++){
            matriz[i][j] = (i == j) ? 1 : 0;
         }
      }       
   }

}
