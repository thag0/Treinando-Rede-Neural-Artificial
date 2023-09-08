package rna.avaliacao.metrica;

import rna.estrutura.RedeNeural;

public class Acuracia extends Metrica{

   @Override
   public double calcular(RedeNeural rede, double[][] entrada, double[][] saida){
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

         int indiceCalculado = super.indiceMaiorValor(rede.obterSaidas());
         int indiceEsperado = super.indiceMaiorValor(dadosSaida);

         if(indiceCalculado == indiceEsperado){
            acertos++;
         }
      }

      acuracia = (double)acertos / qAmostras;

      return acuracia;
   }
}
