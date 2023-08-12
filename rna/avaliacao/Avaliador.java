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
      double[] dadosEntrada = new double[entrada[0].length];//tamanho das colunas da entrada
      double[] dadosSaida = new double[saida[0].length];//tamanho de colunas da saída
      
      double diferenca;
      double custo = 0.0;
      for(int i = 0; i < entrada.length; i++){//percorrer as linhas da entrada
         //preencher dados de entrada e saída
         //método nativo parece ser mais eficiente
         System.arraycopy(entrada[i], 0, dadosEntrada, 0, entrada[i].length);
         System.arraycopy(saida[i], 0, dadosSaida, 0, saida[i].length);

         rede.calcularSaida(dadosEntrada);
         double[] saidaRede = rede.obterSaidas();

         //calcular custo com base na saída
         for(int j = 0; j < saidaRede.length; j++){
            diferenca = dadosSaida[j] - saidaRede[j];
            custo += (diferenca * diferenca);
         }
      }

      custo /= entrada.length;

      return custo;
   }


   /**
    * Calcula a entropia cruzada da rede neural em relação aos dados de entrada e saída fornecidos
    * @param rede rede neural que será avaliada.
    * @param dados dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return entropia cruzada da rede em relação ao dados fornecidos (custo/perda).
    */
   public double entropiaCruzada(RedeNeural rede, double[][] entrada, double[][] saida){  
      double[] dadosEntrada = new double[entrada[0].length];
      double[] dadosSaida = new double[saida[0].length];
  
      double custo = 0.0;
      double epsilon = 1e-9;//evitar log 0
      for(int i = 0; i < entrada.length; i++){//percorrer amostras
         //preencher dados de entrada e saída
         //método nativo mais eficiente
         System.arraycopy(entrada[i], 0, dadosEntrada, 0, entrada[i].length);
         System.arraycopy(saida[i], 0, dadosSaida, 0, saida[i].length);
  
         rede.calcularSaida(dadosEntrada);
         double[] saidaRede = rede.obterSaidas();
         
         double custoExemplo = 0.0;
         for(int k = 0; k < saidaRede.length; k++){
            double previsto = saidaRede[k];
            double real = dadosSaida[k];
            
            //fórmula da entropia cruzada para cada neurônio de saída
            custoExemplo += (-real * Math.log(previsto + epsilon));
         }
         
         custo += custoExemplo;
      }
  
      custo /= entrada.length;

      return custo;
   }
}
