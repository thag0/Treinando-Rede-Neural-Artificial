package rna.avaliacao.perda;

import rna.estrutura.RedeNeural;

public class EntropiaCruzadaBinaria extends Perda{

   @Override
   public double calcular(RedeNeural rede, double[][] entrada, double[][] saida){
      double[] dadosEntrada = new double[entrada[0].length];
      double[] dadosSaida = new double[saida[0].length];
      double[] saidaRede = new double[rede.obterCamadaSaida().quantidadeNeuronios()];

      int nAmostras = entrada.length;
      double perda = 0.0;

      for(int i = 0; i < nAmostras; i++){
         //preencher dados de entrada e saída
         //método nativo mais eficiente
         System.arraycopy(entrada[i], 0, dadosEntrada, 0, entrada[i].length);
         System.arraycopy(saida[i], 0, dadosSaida, 0, saida[i].length);
         
         rede.calcularSaida(dadosEntrada);
         saidaRede = rede.obterSaidas();

         double perdaExemplo = 0.0;
         for(int j = 0; j < dadosSaida.length; j++){
            double previsto = saidaRede[j];
            double real = dadosSaida[j];
            
            //formula desse diabo
            perdaExemplo += -((real * Math.log(previsto)) + ((1 - real) * Math.log(1 - previsto)));
         }

         perda += perdaExemplo;
      }

      perda /= nAmostras;

      return perda;
   }
}
