package rna.avaliacao.metrica;

import rna.estrutura.RedeNeural;

public class MatrizConfusao extends Metrica{

   @Override
   public int[][] calcularMatriz(RedeNeural rede, double[][] entradas, double[][] saidas){
      int nClasses = saidas[0].length;
      int[][] matriz = new int[nClasses][nClasses];

      double[] entrada = new double[entradas[0].length];
      double[] saida = new double[saidas[0].length];
      double[] saidaRede = new double[rede.obterCamadaSaida().quantidadeNeuronios()];

      for(int i = 0; i < entradas.length; i++){
         System.arraycopy(entradas[i], 0, entrada, 0, entradas[i].length);
         System.arraycopy(saidas[i], 0, saida, 0, saidas[i].length);

         rede.calcularSaida(entrada);
         saidaRede = rede.obterSaidas();

         int real = super.indiceMaiorValor(saida);
         int previsto = super.indiceMaiorValor(saidaRede);

         matriz[real][previsto]++;
      }

      return matriz;
   } 
}
