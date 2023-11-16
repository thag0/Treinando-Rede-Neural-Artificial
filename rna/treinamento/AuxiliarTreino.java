package rna.treinamento;

import java.util.Random;

import rna.avaliacao.perda.Perda;
import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Operadores auxiliares para o treino da rede neural;
 */
class AuxiliarTreino{
   Random random = new Random();

   public AuxiliarTreino(){

   }

   /**
    * Configura a seed inicial do gerador de números aleatórios.
    * @param seed nova seed.
    */
    public void configurarSeed(long seed){
      this.random.setSeed(seed);
   }

   /**
    * Calcula os gradientes de cada neurônio das camadas da Rede Neural
    * <p>
    *    Para a camada de saída, os gradientes são calculados usando a derivada
    *    da função de perda configurada para a Rede Neural.
    * </p>
    * <p>
    *    Nas demais camadas intermediárias, o gradiente do neurônio se dá por:
    * </p>
    * <pre>
    *    g = d * ∑(pn * gpn)
    * </pre>
    * Onde:
    * <p>
    *    g - gradiente do neurônio.
    * </p>
    * <p>
    *    d - resultado da derivada da função de ativação do neurônio.
    * </p>
    * <p>
    *    pn - peso do neurônio da camada seguinte, relativo a conexão com 
    *    o neurônio que terá o gradiente calculado.
    * </p>
    * <p>
    *    gpn - gradiente do neurônio da camada seguinte.
    * </p>
    * @param redec conjunto de camadas da Rede Neural.
    * @param perda função de perda da Rede Neural.
    * @param real valores reais baseados na entrada alimentada, que serão usados
    * para calcular os gradientes da camada de saída.
    */
   void calcularGradientes(Camada[] redec, Perda perda, double[] real){
      //saída
      Camada saida = redec[redec.length-1];
      double[] previsto = saida.obterSaida();
      double[] gradientes = perda.derivada(previsto, real);

      for(int i = 0; i < saida.quantidadeNeuronios(); i++){
         saida.neuronio(i).gradiente = gradientes[i];
      }

      //ocultas
      //começar da ultima oculta
      //percorrer camadas ocultas de trás pra frente
      for(int i = redec.length-2; i >= 0; i--){
   
         Camada camadaAtual = redec[i];
         Camada camadaProxima = redec[i+1];
         camadaAtual.ativacaoDerivada();
         for(int j = 0; j < camadaAtual.quantidadeNeuronios(); j++){

            //calcular a soma dos gradientes da camada seguinte
            Neuronio neuronio = camadaAtual.neuronio(j);
            double somaGradientes = 0.0;
            for(int k = 0; k < camadaProxima.quantidadeNeuronios(); k++){
               Neuronio neuronioProxima = camadaProxima.neuronio(k);
               somaGradientes += neuronioProxima.pesos[j] * neuronioProxima.gradiente;
            }
            neuronio.gradiente = somaGradientes * neuronio.derivada;
         }
      }
   }

   /**
    * Embaralha os dados da matriz usando o algoritmo Fisher-Yates.
    * @param entradas matriz com os dados de entrada.
    * @param saidas matriz com os dados de saída.
    */
   void embaralharDados(double[][] entradas, double[][] saidas){
      int linhas = entradas.length;
      int colEntrada = entradas[0].length;
      int colSaida = saidas[0].length;
  
      //evitar muitas inicializações
      double tempEntradas[] = new double[colEntrada];
      double tempSaidas[] = new double[colSaida];
      int i, idAleatorio;

      for(i = linhas - 1; i > 0; i--){
         idAleatorio = random.nextInt(i+1);

         //trocar entradas
         System.arraycopy(entradas[i], 0, tempEntradas, 0, colEntrada);
         System.arraycopy(entradas[idAleatorio], 0, entradas[i], 0, colEntrada);
         System.arraycopy(tempEntradas, 0, entradas[idAleatorio], 0, colEntrada);

         //trocar saídas
         System.arraycopy(saidas[i], 0, tempSaidas, 0, colSaida);
         System.arraycopy(saidas[idAleatorio], 0, saidas[i], 0, colSaida);
         System.arraycopy(tempSaidas, 0, saidas[idAleatorio], 0, colSaida); 
      }
   }

   /**
    * Dedicado para treino em lote e multithread em implementações futuras.
    * @param dados conjunto de dados completo.
    * @param inicio índice de inicio do lote.
    * @param fim índice final do lote.
    * @return lote contendo os dados de acordo com os índices fornecidos.
    */
   double[][] obterSubMatriz(double[][] dados, int inicio, int fim){
      if(inicio < 0 || fim > dados.length || inicio >= fim){
         throw new IllegalArgumentException("Índices de início ou fim inválidos.");
      }

      int linhas = fim - inicio;
      int colunas = dados[0].length;
      double[][] subMatriz = new double[linhas][colunas];

      for(int i = 0; i < linhas; i++){
         System.arraycopy(dados[inicio+i], 0, subMatriz[i], 0, colunas);
      }

      return subMatriz;
   }

   /**
    * Encontra o índice com o maior valor contido no array fornecido
    * @param dados array contendo os dados
    * @return índice com o maior valor contido nos dados.
    */
   int indiceMaiorValor(double[] dados){
      int indiceMaiorValor = 0;
      double maiorValor = dados[0];
  
      for(int i = 1; i < dados.length; i++){
         if(dados[i] > maiorValor){
            maiorValor = dados[i];
            indiceMaiorValor = i;
         }
      }

      return indiceMaiorValor;
   }

   /**
    * Adiciona o novo valor de perda no final do histórico.
    * @param historico histórico com os valores de perda da rede.
    * @param valor novo valor que será adicionado.
    */
   double[] adicionarPerda(double[] historico, double valor){
      double[] aux = historico;
      historico = new double[historico.length + 1];
      
      for(int i = 0; i < aux.length; i++){
         historico[i] = aux[i];
      }
      historico[historico.length-1] = valor;

      return historico;
   }
}
