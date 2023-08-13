package rna.treinamento;

import java.util.ArrayList;
import java.util.Random;

import rna.Camada;
import rna.Neuronio;

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
         System.arraycopy(dados[inicio + i], 0, subMatriz[i], 0, colunas);
      }

      return subMatriz;
   }


   /**
    * Zera todos os gradientes para o cálculo do gradiente em lote.
    * @param redec
    */
   public void zerarGradientesAcumulados(ArrayList<Camada> redec){
         for(int i = 1; i < redec.size(); i++){ 
            
            Camada camadaAtual = redec.get(i);
            int nNeuronios = camadaAtual.obterQuantidadeNeuronios();
            nNeuronios -= (camadaAtual.temBias) ? 1 : 0;
            for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
               
               Neuronio neuronio = camadaAtual.neuronios[j];
               for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
                  neuronio.gradienteAcumulado[k] = 0;
               }
            }
         }
   }
}
