package exemplos;

import utilitarios.ged.Ged;
import rna.estrutura.RedeNeural;
import rna.otimizadores.SGD;
import utilitarios.ged.Dados;

public class ExemploVariosPredicts{
   public static void main(String[] args){
      Ged ged = new Ged();
      Dados dados = ged.lerCsv("./dados/portas-logicas/xor-cascata.csv");
      double[][] xor = ged.dadosParaDouble(dados);

      int nEntrada = 3;
      int nSaida = 2;
      double[][] entrada = ged.separarDadosEntrada(xor, nEntrada);
      double[][] saida = ged.separarDadosSaida(xor, nSaida);

      int[] arq = {nEntrada, 6, nSaida};
      RedeNeural rede = new RedeNeural(arq);
      rede.configurarOtimizador(new SGD());
      rede.configurarInicializador(2);
      rede.compilar();
      rede.configurarFuncaoAtivacao(2);

      rede.treinar(entrada, saida, 5000);
      double[][] previsoes = rede.calcularSaida(entrada);

      ged.imprimirMatriz(saida, "Saídas esperadas");
      ged.imprimirMatriz(previsoes, "Saídas previstas");

      System.out.println("Custo = " + rede.avaliador.erroMedioQuadrado(entrada, saida));
   }
}
