package utilitarios.ged;

class ImpressaoMatriz{
   

   public ImpressaoMatriz(){

   }


   public void imprimirMatriz(int[][] matriz){
      String espacamento = "  ";

      System.out.println("Matriz = [");
      for(int i = 0; i < matriz.length; i++){
         System.out.print(espacamento);
         for(int j = 0; j < matriz[i].length; j++){
            System.out.print(matriz[i][j] + "  ");
         }
         System.out.println();
      }
      System.out.println("]");
   }


   public void imprimirMatriz(float[][] matriz){
      String espacamento = "  ";

      System.out.println("Matriz = [");
      for(int i = 0; i < matriz.length; i++){
         System.out.print(espacamento);
         for(int j = 0; j < matriz[i].length; j++){
            System.out.print(matriz[i][j] + "  ");
         }
         System.out.println();
      }
      System.out.println("]");
   }


   public void imprimirMatriz(double[][] matriz){
      String espacamento = "  ";

      System.out.println("Matriz = [");
      for(int i = 0; i < matriz.length; i++){
         System.out.print(espacamento);
         for(int j = 0; j < matriz[i].length; j++){
            System.out.print(matriz[i][j] + "  ");
         }
         System.out.println();
      }
      System.out.println("]");
   }
}
