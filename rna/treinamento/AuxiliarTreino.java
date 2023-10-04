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
    * Método exclusivo para separar a forma de calcular os erros da camada de saída.
    * Dando suporte não apenas para problemas de regressão.
    * <p>
    *    O cálculo do erro é dado por:
    *    <pre>
    *       er = (y - p)
    *    </pre>
    *    Onde:
    *    <p>
    *       {@code er} - erro do neurônio.
    *    </p>
    *    <p>
    *       {@code y} - saída desejada.
    *    </p>
    *    <p>
    *       {@code p} - saída prevista pelo neurônio.
    *    </p>
    * </p>
    * @param saida camada de saída da Rede Reural.
    * @param perda função de perda da rede usada para calcular os erros.
    * @param real array com as saídas esperadas.
    */
   void calcularErroSaida(Camada saida, Perda perda, double[] real){
      double[] previsto = new double[saida.quantidadeNeuronios()];
      for(int i = 0; i < previsto.length; i++){
         previsto[i] = saida.neuronio(i).saida;
      }

      double[] erros = perda.calcularErro(previsto, real);
      for(int i = 0; i < saida.quantidadeNeuronios(); i++){
         saida.neuronio(i).erro = erros[i];
      }
   }

   /**
    * Método exclusivo para separar a forma de calcular os erros das camadas ocultas
    * da rede neural.
    * <p>
    *    O erro do neurônio da camada oculta se da por:
    * </p>
    * <pre>
    *    e = d * ∑(pn * epn)
    * </pre>
    * Onde:
    * <p>
    *    e - erro do neurônio oculto
    * </p>
    * <p>
    *    d - resultado da derivada da função de ativação
    * </p>
    * <p>
    *    pn - peso do neurônio da camada seguinte, relativo a conexão com 
    *    o neurônio que terá o erro calculado
    * </p>
    * <p>
    *    epn - erro do neurônio da camada seguinte.
    * </p>
    * @param redec Rede Neural em formato de array de camadas.
    */
   void calcularErroOcultas(Camada[] redec){
      Camada camadaAtual, camadaProxima;
      Neuronio neuronio, neuronioProxima;

      // começar da ultima oculta
      // percorrer camadas ocultas de trás pra frente
      for(int i = redec.length-2; i >= 0; i--){
         
         camadaAtual = redec[i];
         camadaAtual.ativacaoDerivada();
         for(int j = 0; j < camadaAtual.quantidadeNeuronios(); j++){

            camadaProxima = redec[i+1];
            // percorrer neurônios da camada seguinte
            double somaErros = 0.0;
            neuronio = camadaAtual.neuronio(j);
            for(int k = 0; k < camadaProxima.quantidadeNeuronios(); k++){
               neuronioProxima = camadaProxima.neuronio(k);
               somaErros += neuronioProxima.pesos[j] * neuronioProxima.erro;
            }
            neuronio.erro = somaErros * neuronio.derivada;
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
}
