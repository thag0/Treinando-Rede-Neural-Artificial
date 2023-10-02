package rna.avaliacao.metrica;

import rna.estrutura.RedeNeural;

public class MatrizConfusao extends Metrica{

   @Override
   public int[][] calcularMatriz(RedeNeural rede, double[][] entradas, double[][] saidas){
      return super.calcularMatriz(rede, entradas, saidas);
   } 
}
