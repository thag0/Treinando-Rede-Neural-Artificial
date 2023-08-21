package exemplos;

import java.util.ArrayList;

import rna.RedeNeural;
import utilitarios.ged.Ged;

public class ExemploVariosPredicts{
   public static void main(String[] args){
      Ged ged = new Ged();
      ArrayList<String[]> dados = ged.lerCsv("./dados/portas-logicas/xorCascata.csv");
      double[][] xor = ged.listaParaDadosDouble(dados);

      int nEntrada = 3;
      int nSaida = 2;
      double[][] entrada = ged.separarDadosEntrada(xor, nEntrada);
      double[][] saida = ged.separarDadosSaida(xor, nSaida);

      int[] arq = {nEntrada, 5, nSaida};
      RedeNeural rede = new RedeNeural(arq);
      rede.configurarTaxaAprendizagem(0.2);
      rede.configurarMomentum(0.999);
      rede.configurarOtimizador(1);
      rede.configurarInicializacaoPesos(2);
      rede.compilar();
      rede.configurarFuncaoAtivacao(2);

      rede.treinar(entrada, saida, 5000);
      double[][] previsoes = rede.calcularSaida(entrada);

      ged.imprimirMatriz(saida, "Saídas esperadas");
      ged.imprimirMatriz(previsoes, "Saídas previstas");

      System.out.println("Custo = " + rede.avaliador.erroMedioQuadrado(entrada, saida));
   }
}
