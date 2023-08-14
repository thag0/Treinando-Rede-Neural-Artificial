package rna.avaliacao.perda;

import rna.RedeNeural;

public class ErroMedioQuadrado extends FuncaoPerda{

   @Override
   public double calcular(RedeNeural rede, double[][] entrada, double[][] saida){  
      double[] dadosEntrada = new double[entrada[0].length];//tamanho das colunas da entrada
      double[] dadosSaida = new double[saida[0].length];//tamanho de colunas da saída
      double[] saidaRede = new double[rede.obterCamadaSaida().obterQuantidadeNeuronios()];
      
      double diferenca;
      double perda = 0.0;
      for(int i = 0; i < entrada.length; i++){//percorrer as linhas da entrada
         //preencher dados de entrada e saída
         //método nativo parece ser mais eficiente
         System.arraycopy(entrada[i], 0, dadosEntrada, 0, entrada[i].length);
         System.arraycopy(saida[i], 0, dadosSaida, 0, saida[i].length);

         rede.calcularSaida(dadosEntrada);
         saidaRede = rede.obterSaidas();

         //esperado - predito
         for(int j = 0; j < saidaRede.length; j++){
            diferenca = dadosSaida[j] - saidaRede[j];
            perda += (diferenca * diferenca);
         }
      }

      perda /= entrada.length;

      return perda;      
   }
}
