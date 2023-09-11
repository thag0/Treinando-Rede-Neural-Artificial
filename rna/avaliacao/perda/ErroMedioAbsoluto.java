package rna.avaliacao.perda;

import rna.estrutura.RedeNeural;

public class ErroMedioAbsoluto extends FuncaoPerda{

   @Override
   public double calcular(RedeNeural rede, double[][] entrada, double[][] saida){
      double[] dadosEntrada = new double[entrada[0].length];
      double[] dadosSaida = new double[saida[0].length];
      double erroMedio = 0;

      for(int i = 0; i < entrada.length; i++){//percorrer linhas dos dados
         System.arraycopy(entrada[i], 0, dadosEntrada, 0, entrada[i].length);
         System.arraycopy(saida[i], 0, dadosSaida, 0, saida[i].length);

         rede.calcularSaida(dadosEntrada);
         double[] saidaRede = rede.obterSaidas();

         for(int k = 0; k < saidaRede.length; k++){
            erroMedio += Math.abs(dadosSaida[k] - saidaRede[k]);
         }
      }

      erroMedio /= entrada.length;
      
      return erroMedio;
   }
}
