package rna.avaliacao;

import rna.Neuronio;
import rna.RedeNeural;

public class Avaliador{
   AuxiliaresAvaliacao aux;

   public Avaliador(){
      this.aux = new AuxiliaresAvaliacao();
   }


   /**
    * Calcula a precisão da rede neural em relação aos dados de entrada e saída fornecidos (regressão).
    * @param rede rede neural que será avaliada.
    * @param dados dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return A precisão da rede neural em forma de probabilidade.
    */
   public double calcularPrecisao(RedeNeural rede, double[][] dados, double[][] saida){
      double[] dadosEntrada = new double[dados[0].length];
      double[] dadosSaida = new double[saida[0].length];
      double erroMedio = 0;
      int nSaida = rede.obterCamadaSaida().obterQuantidadeNeuronios();

      for(int i = 0; i < dados.length; i++){//percorrer linhas dos dados
         //preencher dados de entrada e saída
         dadosEntrada = dados[i];
         dadosSaida = saida[i];

         rede.calcularSaida(dadosEntrada);

         for(int k = 0; k < nSaida; k++){
            Neuronio neuronio = rede.obterCamadaSaida().neuronios[k];
            erroMedio += Math.abs(dadosSaida[k] - neuronio.saida);
         }
      }

      erroMedio /= dados.length;
      return (1 - erroMedio); // Converter em um valor relativo a porcentagem
   }


   /**
    * Calcula a precisão da rede neural em relação aos dados de entrada e saída fornecidos (classificação).
    * @param rede rede neural que será avaliada.
    * @param dados dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return A acurácia da rede neural em forma de probabilidade.
    */
   public double calcularAcuracia(RedeNeural rede, double[][] dados, double[][] saida){
      int qAmostras = dados.length;
      int acertos = 0;
      double acuracia;
      double[] dadosEntrada = new double[dados[0].length];
      double[] dadosSaida = new double[saida[0].length];

      for(int i = 0; i < qAmostras; i++){
         //preencher dados de entrada e saída
         dadosEntrada = dados[i];
         dadosSaida = saida[i];

         rede.calcularSaida(dadosEntrada);

         int indiceCalculado = aux.indiceMaiorValor(rede.obterSaidas());
         int indiceEsperado = aux.indiceMaiorValor(dadosSaida);

         if(indiceCalculado == indiceEsperado){
            acertos++;
         }
      }

      acuracia = (double)acertos / qAmostras;

      return acuracia;
   }


   /**
    * Calcula o erro médio quadrado da rede neural em relação aos dados de entrada e saída fornecidos.
    * @param rede rede neural que será avaliada.
    * @param dados dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return erro médio quadrado da rede em relação ao dados fornecidos (custo/perda).
    */
   public double erroMedioQuadrado(RedeNeural rede, double[][] dados, double[][] saida){
      double[] dadosEntrada = new double[dados[0].length];//tamanho das colunas da entrada
      double[] dadosSaida = new double[saida[0].length];//tamanho de colunas da saída
      
      int i, j;
      int nSaida = rede.obterCamadaSaida().obterQuantidadeNeuronios();
      double diferenca;
      double custo = 0.0;

      for(i = 0; i < dados.length; i++){//percorrer as linhas da entrada
         //preencher dados de entrada e saída
         dadosEntrada = dados[i];
         dadosSaida = saida[i];

         rede.calcularSaida(dadosEntrada);

         //calcular custo com base na saída
         for(j = 0; j < nSaida; j++){
            Neuronio neuronio = rede.obterCamadaSaida().neuronios[j];
            diferenca = dadosSaida[j] - neuronio.saida;
            custo += (diferenca * diferenca);
         }
      }

      custo /= dados.length;

      return custo;
   }


   /**
    * Calcula a entropia cruzada da rede neural em relação aos dados de entrada e saída fornecidos
    * @param rede rede neural que será avaliada.
    * @param dados dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return entropia cruzada da rede em relação ao dados fornecidos (custo/perda).
    */
   public double entropiaCruzada(RedeNeural rede, double[][] dados, double[][] saida){  
      double[] dadosEntrada = new double[dados[0].length];
      double[] dadosSaida = new double[saida[0].length];
  
      int nSaida = rede.obterCamadaSaida().obterQuantidadeNeuronios();
      double custo = 0.0;
      double epsilon = 1e-9;//evitar log 0

      for(int i = 0; i < dados.length; i++){//percorrer amostras
         //preencher dados de entrada e saída
         dadosEntrada = dados[i];
         dadosSaida = saida[i];
  
         rede.calcularSaida(dadosEntrada);

         double custoExemplo = 0.0;
         for(int k = 0; k < nSaida; k++){
            Neuronio neuronio = rede.obterCamadaSaida().neuronios[k];
            double dadoPrevisto = neuronio.saida;
            double dadoReal = dadosSaida[k];
            
            //fórmula da entropia cruzada para cada neurônio de saída
            custoExemplo += (-dadoReal * Math.log(dadoPrevisto + epsilon));
         }
         
         custo += custoExemplo;
      }
  
      custo /= dados.length;

      return custo;
   }
}
