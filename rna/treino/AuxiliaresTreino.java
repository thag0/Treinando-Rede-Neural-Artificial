package rna.treino;

import java.util.Random;

/**
 * Operadores auxiliares para o treino da rede neural;
 */
class AuxiliaresTreino{
      Random random = new Random();

   public AuxiliaresTreino(){

   }

   /**
    * Embaralha os dados da matriz usando o algoritmo Fisher-Yates.
    * @param entradas matriz com os dados de entrada.
    * @param saidas matriz com os dados de saída.
    */
   public void embaralharDados(double[][] entradas, double[][] saidas){
      int linhas = entradas.length;
  
      //evitar muitas inicializações
      double tempDados[];
      double tempSaidas[];
      int i, indiceAleatorio;

      for(i = linhas - 1; i > 0; i--){
         indiceAleatorio = random.nextInt(i + 1);
  
         tempDados = entradas[i];
         entradas[i] = entradas[indiceAleatorio];
         entradas[indiceAleatorio] = tempDados;

         tempSaidas = saidas[i];
         saidas[i] = saidas[indiceAleatorio];
         saidas[indiceAleatorio] = tempSaidas;
      }
   }

   /**
    * Dedicado para treino em lote e multithread em implementações futuras.
    * @param dados conjunto de dados completo.
    * @param inicio índice de inicio do lote.
    * @param fim índice final do lote.
    * @return lote contendo os dados de acordo com os índices fornecidos.
    */
   public double[][] obterSubMatriz(double[][] dados, int inicio, int fim){
      if(inicio < 0 || fim > dados.length || inicio >= fim){
         throw new IllegalArgumentException("Índices de início ou fim inválidos.");
      }

      int linhas = fim - inicio;
      int colunas = dados[0].length;
      double[][] subMatriz = new double[linhas][colunas];

      for(int i = 0; i < linhas; i++){
         for(int j = 0; j < colunas; j++){
            subMatriz[i][j] = dados[inicio + i][j];
         }
      }

      return subMatriz;
   }
}
