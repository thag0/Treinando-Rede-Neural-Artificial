package rna.avaliacao.perda;

import rna.estrutura.RedeNeural;

public class ErroMedioQuadrado extends Perda{

   @Override
   public double calcular(RedeNeural rede, double[][] entrada, double[][] saida){  
      double[] dadosEntrada = new double[entrada[0].length];//tamanho das colunas da entrada
      double[] dadosSaida = new double[saida[0].length];//tamanho de colunas da saída
      double[] saidaRede = new double[rede.obterTamanhoSaida()];
      
      int amostras = entrada.length;
      double perda = 0.0;

      for(int i = 0; i < amostras; i++){
         System.arraycopy(entrada[i], 0, dadosEntrada, 0, entrada[i].length);
         System.arraycopy(saida[i], 0, dadosSaida, 0, saida[i].length);

         rede.calcularSaida(dadosEntrada);
         System.arraycopy(rede.obterSaidas(), 0, saidaRede, 0, saidaRede.length);

         for(int j = 0; j < saidaRede.length; j++){
            double d = dadosSaida[j] - saidaRede[j];
            perda += (d * d);
         }
      }

      perda /= entrada.length;

      return perda;
   }

   @Override
   public double calcularErro(double previsto, double real){
      return 2*(real - previsto);
   }
}
