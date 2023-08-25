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

   //preencher
   
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
   

   //identidade


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


   //transposição

   
   public int[][] transporMatriz(int[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      int[][] transposta = new int[colunas][linhas];
  
      for (int i = 0; i < linhas; i++) {
         for (int j = 0; j < colunas; j++) {
            transposta[j][i] = matriz[i][j];
         }
      }
  
      return transposta;
   }

   
   public float[][] transporMatriz(float[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      float[][] transposta = new float[colunas][linhas];
  
      for (int i = 0; i < linhas; i++) {
         for (int j = 0; j < colunas; j++) {
            transposta[j][i] = matriz[i][j];
         }
      }
  
      return transposta;
   }

   
   public double[][] transporMatriz(double[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      double[][] transposta = new double[colunas][linhas];
  
      for (int i = 0; i < linhas; i++) {
         for (int j = 0; j < colunas; j++) {
            transposta[j][i] = matriz[i][j];
         }
      }
  
      return transposta;
   }

   
   //verificação de dimensionalidade pra soma
   
   
   private void dimensoesIguais(int[][] a, int[][] b){
      if((a.length != b.length) || (a[0].length != b[0].length)){
         throw new IllegalArgumentException("Número de linhas de A e B são diferentes.");
      }
   }


   private void dimensoesIguais(float[][] a, float[][] b){
      if((a.length != b.length) || (a[0].length != b[0].length)){
         throw new IllegalArgumentException("Número de linhas de A e B são diferentes.");
      }
   }


   private void dimensoesIguais(double[][] a, double[][] b){
      if((a.length != b.length) || (a[0].length != b[0].length)){
         throw new IllegalArgumentException("Número de linhas de A e B são diferentes.");
      }
   }


   //verificação de dimensionalidade pra multiplicação

   
   private void dimensoesIguaisMult(int[][] a, int[][] b){
      if(a[0].length != b.length){
         throw new IllegalArgumentException("Dimensões de A e B incompatíveis para multiplicação");
      }
   }


   private void dimensoesIguaisMult(float[][] a, float[][] b){
      if(a[0].length != b.length){
         throw new IllegalArgumentException("Dimensões de A e B incompatíveis para multiplicação");
      }
   }


   private void dimensoesIguaisMult(double[][] a, double[][] b){
      if(a[0].length != b.length){
         throw new IllegalArgumentException("Dimensões de A e B incompatíveis para multiplicação");
      }
   }


   // OPERAÇÕES MATRICIAIS -------------------------------------
   //soma


   public int[][] somarMatrizes(int[][] a, int[][] b){
      dimensoesIguais(a, b);
      int[][] soma = new int[a.length][a[0].length];

      for(int i = 0; i < soma.length; i++){
         for(int j = 0; j < soma[i].length; j++){
            soma[i][j] = a[i][j] + b[i][j];
         }
      }

      return soma;
   }


   public float[][] somarMatrizes(float[][] a, float[][] b){
      dimensoesIguais(a, b);
      float[][] soma = new float[a.length][a[0].length];

      for(int i = 0; i < soma.length; i++){
         for(int j = 0; j < soma[i].length; j++){
            soma[i][j] = a[i][j] + b[i][j];
         }
      }

      return soma;
   }


   public double[][] somarMatrizes(double[][] a, double[][] b){
      dimensoesIguais(a, b);
      double[][] soma = new double[a.length][a[0].length];

      for(int i = 0; i < soma.length; i++){
         for(int j = 0; j < soma[i].length; j++){
            soma[i][j] = a[i][j] + b[i][j];
         }
      }

      return soma;
   }


   //subtração


   public int[][] subtrairMatrizes(int[][] a, int[][] b){
      dimensoesIguais(a, b);
      int[][] soma = new int[a.length][a[0].length];

      for(int i = 0; i < soma.length; i++){
         for(int j = 0; j < soma[i].length; j++){
            soma[i][j] = a[i][j] - b[i][j];
         }
      }

      return soma;
   }


   public float[][] subtrairMatrizes(float[][] a, float[][] b){
      dimensoesIguais(a, b);
      float[][] soma = new float[a.length][a[0].length];

      for(int i = 0; i < soma.length; i++){
         for(int j = 0; j < soma[i].length; j++){
            soma[i][j] = a[i][j] - b[i][j];
         }
      }

      return soma;
   }


   public double[][] subtrairMatrizes(double[][] a, double[][] b){
      dimensoesIguais(a, b);
      double[][] soma = new double[a.length][a[0].length];

      for(int i = 0; i < soma.length; i++){
         for(int j = 0; j < soma[i].length; j++){
            soma[i][j] = a[i][j] - b[i][j];
         }
      }

      return soma;
   }


   //multiplicação


   public int[][] multiplicarMatrizes(int[][] a, int[][] b){
      dimensoesIguaisMult(a, b);

      int[][] mult = new int[b.length][a[0].length];
      int tamInterno = a[0].length;

      for(int i = 0; i < mult.length; i++){
         for(int j = 0; j < mult[i].length; j++){
            mult[i][j] = 0;
            for(int k = 0; k < tamInterno; k++){
               mult[i][j] += a[i][k] * b[k][j];
            }
         }
      }

      return mult;
   }


   public float[][] multiplicarMatrizes(float[][] a, float[][] b){
      dimensoesIguaisMult(a, b);

      float[][] mult = new float[b.length][a[0].length];
      float tamInterno = a[0].length;

      for(int i = 0; i < mult.length; i++){
         for(int j = 0; j < mult[i].length; j++){
            mult[i][j] = 0;
            for(int k = 0; k < tamInterno; k++){
               mult[i][j] += a[i][k] * b[k][j];
            }
         }
      }

      return mult;
   }


   public double[][] multiplicarMatrizes(double[][] a, double[][] b){
      dimensoesIguaisMult(a, b);

      double[][] mult = new double[b.length][a[0].length];
      int tamInterno = a[0].length;

      for(int i = 0; i < mult.length; i++){
         for(int j = 0; j < mult[i].length; j++){
            mult[i][j] = 0;
            for(int k = 0; k < tamInterno; k++){
               mult[i][j] += a[i][k] * b[k][j];
            }
         }
      }

      return mult;
   }


   //escalar

   
   public void multilpicarEscalar(int[][] matriz, int escalar){
      for(int i = 0; i < matriz.length; i++){
         for(int j = 0; j < matriz[i].length; j++){
            matriz[i][j] *= escalar;
         }
      }
   }

   
   public void multilpicarEscalar(float[][] matriz, float escalar){
      for(int i = 0; i < matriz.length; i++){
         for(int j = 0; j < matriz[i].length; j++){
            matriz[i][j] *= escalar;
         }
      }
   }

   
   public void multilpicarEscalar(double[][] matriz, double escalar){
      for(int i = 0; i < matriz.length; i++){
         for(int j = 0; j < matriz[i].length; j++){
            matriz[i][j] *= escalar;
         }
      }
   }


   //hadamard


   public int[][] hadamard(int[][] a, int[][] b){
      dimensoesIguais(a, b);
      int[][] hadamard = new int[a.length][b[0].length];

      for(int i = 0; i < hadamard.length; i++){
         for(int j = 0; j < hadamard[i].length; j++){
            hadamard[i][j] = a[i][j] * b[i][j];
         }
      }

      return hadamard;
   }


   public float[][] hadamard(float[][] a, float[][] b){
      dimensoesIguais(a, b);
      float[][] hadamard = new float[a.length][b[0].length];

      for(int i = 0; i < hadamard.length; i++){
         for(int j = 0; j < hadamard[i].length; j++){
            hadamard[i][j] = a[i][j] * b[i][j];
         }
      }

      return hadamard;
   }


   public double[][] hadamard(double[][] a, double[][] b){
      dimensoesIguais(a, b);
      double[][] hadamard = new double[a.length][b[0].length];

      for(int i = 0; i < hadamard.length; i++){
         for(int j = 0; j < hadamard[i].length; j++){
            hadamard[i][j] = a[i][j] * b[i][j];
         }
      }

      return hadamard;
   }
}
