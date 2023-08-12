package rna.avaliacao;

import rna.Neuronio;
import rna.RedeNeural;

public class Avaliador{
   AuxiliaresAvaliacao aux;

   EntropiaCruzada entropiaCruzada = new EntropiaCruzada();
   ErroMedioQuadrado erroMedioQuadrado = new ErroMedioQuadrado();

   public Avaliador(){
      this.aux = new AuxiliaresAvaliacao();
   }


   /**
    * Calcula a precisão da rede neural em relação aos dados de entrada e saída fornecidos (regressão).
    * @param rede rede neural que será avaliada.
    * @param entrada dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return A precisão da rede neural em forma de probabilidade.
    */
   public double calcularPrecisao(RedeNeural rede, double[][] entrada, double[][] saida){
      double[] dadosEntrada = new double[entrada[0].length];
      double[] dadosSaida = new double[saida[0].length];
      double erroMedio = 0;
      int nSaida = rede.obterCamadaSaida().obterQuantidadeNeuronios();

      for(int i = 0; i < entrada.length; i++){//percorrer linhas dos dados
         //preencher dados de entrada e saída
         dadosEntrada = entrada[i];
         dadosSaida = saida[i];

         rede.calcularSaida(dadosEntrada);

         for(int k = 0; k < nSaida; k++){
            Neuronio neuronio = rede.obterCamadaSaida().neuronios[k];
            erroMedio += Math.abs(dadosSaida[k] - neuronio.saida);
         }
      }

      erroMedio /= entrada.length;
      return (1 - erroMedio); // Converter em um valor relativo a porcentagem
   }


   /**
    * Calcula a precisão da rede neural em relação aos dados de entrada e saída fornecidos (classificação).
    * @param rede rede neural que será avaliada.
    * @param entrada dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return A acurácia da rede neural em forma de probabilidade.
    */
   public double calcularAcuracia(RedeNeural rede, double[][] entrada, double[][] saida){
      int qAmostras = entrada.length;
      int acertos = 0;
      double acuracia;
      double[] dadosEntrada = new double[entrada[0].length];
      double[] dadosSaida = new double[saida[0].length];

      for(int i = 0; i < qAmostras; i++){
         //preencher dados de entrada e saída
         dadosEntrada = entrada[i];
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
   public double erroMedioQuadrado(RedeNeural rede, double[][] entrada, double[][] saida){
      return erroMedioQuadrado.calcular(rede, entrada, saida);
   }


   /**
    * Calcula a entropia cruzada da rede neural em relação aos dados de entrada e saída fornecidos
    * @param rede rede neural que será avaliada.
    * @param dados dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return entropia cruzada da rede em relação ao dados fornecidos (custo/perda).
    */
   public double entropiaCruzada(RedeNeural rede, double[][] entrada, double[][] saida){  
      return entropiaCruzada.calcular(rede, entrada, saida);
   }
}
