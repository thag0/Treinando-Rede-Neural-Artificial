package exemplos;

import rna.ativacoes.Sigmoid;
import rna.estrutura.RedeNeural;
import rna.otimizadores.SGD;
import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class Teste{
   public static void main(String[] args) {
      double[][] e = {
         {0, 0},
         {0, 1},
         {1, 0},
         {1, 1}
      };

      double[][] s = {
         {0},
         {1},
         {1},
         {0}
      };

      int[] arq = {2, 2, 1};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar();
      rede.configurarFuncaoAtivacao(new Sigmoid());
      rede.configurarOtimizador(new SGD());
      rede.configurarTaxaAprendizagem(0.1);
      rede.configurarMomentum(0.99);

      rede.diferencaFinita(e, s, 1e-2, 40_000, 0);

      double[][] previsoes = rede.calcularSaida(e);

      Ged ged = new Ged();
      ged.imprimirDados(new Dados(previsoes));
   }
}
