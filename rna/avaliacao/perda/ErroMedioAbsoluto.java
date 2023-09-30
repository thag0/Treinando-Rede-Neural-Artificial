package rna.avaliacao.perda;

import rna.estrutura.RedeNeural;

public class ErroMedioAbsoluto extends Perda{

   @Override
   public double calcular(RedeNeural rede, double[][] entrada, double[][] saida){
      double[] dadosEntrada = new double[entrada[0].length];
      double[] dadosSaida = new double[saida[0].length];
      double[] saidaRede = new double[rede.obterTamanhoSaida()];

      int amostras = entrada.length;
      double perda = 0;

      for(int i = 0; i < amostras; i++){
         System.arraycopy(entrada[i], 0, dadosEntrada, 0, entrada[i].length);
         System.arraycopy(saida[i], 0, dadosSaida, 0, saida[i].length);

         rede.calcularSaida(dadosEntrada);
         System.arraycopy(rede.obterSaidas(), 0, saidaRede, 0, saidaRede.length);

         for(int j = 0; j < saidaRede.length; j++){
            perda += Math.abs(dadosSaida[j] - saidaRede[j]);
         }
      }

      perda /= entrada.length;
      
      return perda;
   }

   @Override
   public double[] calcularErro(double[] previsto, double[] real){
      double[] erros = new double[previsto.length];
      for(int i = 0; i < previsto.length; i++){
         erros[i] = real[i] - previsto[i];
      }
      return erros;
   }
}
