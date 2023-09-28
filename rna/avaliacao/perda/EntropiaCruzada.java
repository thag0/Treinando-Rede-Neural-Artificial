package rna.avaliacao.perda;

import rna.estrutura.RedeNeural;

public class EntropiaCruzada extends Perda{

   @Override
   public double calcular(RedeNeural rede, double[][] entrada, double[][] saida){  
      double[] dadosEntrada = new double[entrada[0].length];
      double[] dadosSaida = new double[saida[0].length];
      double[] saidaRede = new double[rede.obterCamadaSaida().quantidadeNeuronios()];
  
      double perda = 0.0;
      double epsilon = 1e-10;//evitar log 0
      for(int i = 0; i < entrada.length; i++){//percorrer amostras
         //preencher dados de entrada e saída
         //método nativo mais eficiente
         System.arraycopy(entrada[i], 0, dadosEntrada, 0, entrada[i].length);
         System.arraycopy(saida[i], 0, dadosSaida, 0, saida[i].length);
  
         rede.calcularSaida(dadosEntrada);
         System.arraycopy(rede.obterSaidas(), 0, saidaRede, 0, saidaRede.length);
         
         double perdaExemplo = 0.0;
         for(int j = 0; j < saidaRede.length; j++){
            double previsto = saidaRede[j];
            double real = dadosSaida[j];
            
            //fórmula da entropia cruzada para cada neurônio de saída
            perdaExemplo += (-real * Math.log(previsto + epsilon));
         }
         
         perda += perdaExemplo;
      }
  
      perda /= entrada.length;

      return perda;
   }

   @Override
   public double calcularErro(double previsto, double real){
      //é uma adaptação pra arquitetura de rede que fiz.
      return (real - previsto);
   }
}
