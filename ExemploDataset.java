import java.util.ArrayList;

import rna.RedeNeural;

import utilitarios.ConversorDados;
import utilitarios.GerenciadorDados;

public class ExemploDataset{

   public static void main(String[] args) {
      GerenciadorDados gd = new GerenciadorDados();
      ConversorDados cd = new ConversorDados();

      // manusear dados
      // tratamento específico para os dados do iris com adaptação
      // para usar a tangente hiperbolica como função de ativação
      ArrayList<String[]> iris = gd.lerCsv("./dados/datasets-maiores/iris.csv");
      gd.removerLinhaDados(iris, 0);
      int ultimoIndice = (iris.get(0).length-1);
      gd.editarValorDados(iris, ultimoIndice, "Iris-setosa", "-1");
      gd.editarValorDados(iris, ultimoIndice, "Iris-versicolor", "0");
      gd.editarValorDados(iris, ultimoIndice, "Iris-virginica", "1");


      // converter os dados da estrutura de texto em valores numéricos para o 
      // treino da rede neural.
      // separar em treino e teste para evitar overfitting
      double[][] dados = cd.listaParaDadosDouble(iris);
      double[][][] treinoTeste = gd.separarTreinoTeste(dados, 0.25f);
      double[][] treino = treinoTeste[0];
      double[][] teste = treinoTeste[1];
      gd.embaralharDados(treino);
      gd.embaralharDados(teste);

      double[][] treinoX, treinoY, testeX, testeY;
      int colunasDados = 4;// quantidade de características dos dados (feature)
      int colunasClasses = 1;// quantidade de classificações dos dados (class)

      treinoX = gd.separarDadosEntrada(treino, colunasDados);
      treinoY = gd.separarDadosSaida(treino, colunasClasses);
      testeX = gd.separarDadosEntrada(teste, colunasDados);
      testeY = gd.separarDadosSaida(teste, colunasClasses);


      // criando, configurando e treinando a rede neural.
      // os valores de configuração não devem ser tomados como regra e 
      // devem se adaptar ao problema e os dados apresentados.
      int[] arq = {colunasDados, 6, 4, colunasClasses};
      RedeNeural rede = new RedeNeural(arq);
      rede.configurarTaxaAprendizagem(0.01);
      rede.configurarMomentum(0.9);
      rede.compilar();
      rede.configurarFuncaoAtivacao(3);
      rede.treinoGradienteEstocastico(treinoX, treinoY, 3_000);


      // avaliando os resultados da rede neural
      double precisao = rede.calcularPrecisao(testeX, testeY);
      double custo = rede.funcaoDeCusto(testeX, testeY);
      System.out.println(rede.obterInformacoes());
      System.out.println("Custo: " + custo);
      System.out.println("Precisão: " + (precisao * 100) + "%");
   }
}
