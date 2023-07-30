import java.util.ArrayList;

import rna.RedeNeural;
import utilitarios.ConversorDados;
import utilitarios.GerenciadorDados;

public class ExemploDataset{
   public static void main(String[] args) {
      GerenciadorDados gd = new GerenciadorDados();
      ConversorDados cd = new ConversorDados();

      //manusear dados
      ArrayList<String[]> iris = gd.lerCsv("./dados/datasets-maiores/iris.csv");
      gd.removerLinhaDados(iris, 0);
      gd.editarValorDados(iris, (iris.get(0).length-1), "Iris-setosa", "-1");
      gd.editarValorDados(iris, (iris.get(0).length-1), "Iris-versicolor", "0");
      gd.editarValorDados(iris, (iris.get(0).length-1), "Iris-virginica", "1");
      

      //transformar em treino e teste
      double[][] dados = cd.listaParaDadosDouble(iris);

      double[][][] treinoTeste;
      double[][] treino, teste;
      treinoTeste = gd.separarTreinoTeste(dados, 0.25f);
      treino = treinoTeste[0];
      teste = treinoTeste[1];
      gd.embaralharDados(treino);
      gd.embaralharDados(teste);

      double[][] treinoX, treinoY, testeX, testeY;
      int colunasDados = 4;// quantidade de características dos dados (features)
      int colunasClasses = 1;// quantidade de classificações dos dados (class)

      treinoX = gd.separarDadosEntrada(treino, colunasDados);
      treinoY = gd.separarDadosSaida(treino, colunasClasses);
      testeX = gd.separarDadosEntrada(teste, colunasDados);
      testeY = gd.separarDadosSaida(teste, colunasClasses);

      //treinando rede neural
      int[] arq = {colunasDados, 6, 4, colunasClasses};
      RedeNeural rede = new RedeNeural(arq);
      rede.configurarTaxaAprendizagem(0.001);
      rede.configurarMomentum(0.9);
      rede.compilar();
      rede.configurarFuncaoAtivacao(3);

      rede.treinoGradienteEstocastico(treinoX, treinoY, 1_000);
      double precisao = rede.calcularPrecisao(testeX, testeY);
      double custo = rede.funcaoDeCusto(testeX, testeY);
      System.out.println("Custo: " + custo);
      System.out.println("Precisão: " + (precisao * 100) + "%");
   }
}
