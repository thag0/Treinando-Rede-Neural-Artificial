package exemplos;

import rna.ativacoes.Sigmoid;
import rna.estrutura.RedeNeural;
import rna.inicializadores.Xavier;
import rna.otimizadores.SGD;
import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class ExemploTratandoDados{
   public static void main(String[] args){
      Ged ged = new Ged();
      ged.limparConsole();

      Dados breastCancer = ged.lerCsv("./dados/datasets-maiores/breast-cancer-wisconsin.csv");
      breastCancer.editarNome("Breast Cancer");

      //removendo coluna que guarda ids porque não contribuem pro aprendizado
      ged.removerColuna(breastCancer, 0);

      //substituindo valores ausente pela média da coluna
      ged.preencherAusentes(breastCancer, 5, breastCancer.media(5));
      
      //trocando valores 2 para 0
      ged.editarValor(breastCancer, 9, "2", "0");

      //trocando valores 4 para 1
      ged.editarValor(breastCancer, 9, "4", "1");

      System.out.println(breastCancer.info());

      //convertendo conjunto de dados para valores numéricos
      double[][] dados = ged.dadosParaDouble(breastCancer);
      
      //separando dados de treino e teste
      int qEntradas = 9;
      int qSaidas = 1;
      ged.embaralharDados(dados);
      double[][][] treinoTeste = (double[][][]) ged.separarTreinoTeste(dados, 0.25f);
      double[][] treino = treinoTeste[0];
      double[][] teste = treinoTeste[1];
      double[][] treinoX = (double[][]) ged.separarDadosEntrada(treino, qEntradas);
      double[][] treinoY = (double[][]) ged.separarDadosSaida(treino, qSaidas);
      double[][] testeX = (double[][]) ged.separarDadosEntrada(teste, qEntradas);
      double[][] testeY = (double[][]) ged.separarDadosSaida(teste, qSaidas);

      //construindo a rede neural
      int[] arq = {qEntradas, 10, 10, qSaidas};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar(new SGD(), new Xavier());
      rede.configurarAtivacao(new Sigmoid());
      rede.treinar(treinoX, treinoY, 5_000);

      //avaliando resultados
      System.out.println(rede.info());
      System.out.println("Perda = " + rede.avaliador.erroMedioQuadrado(testeX, testeY));
      var precisao = 1 - rede.avaliador.erroMedioAbsoluto(testeX, testeY);
      System.out.println("Precisão = " + (precisao * 100) + "%");

   }
}
