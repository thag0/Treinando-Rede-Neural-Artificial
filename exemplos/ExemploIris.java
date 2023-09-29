package exemplos;

import rna.ativacoes.Sigmoid;
import rna.ativacoes.TanH;
import rna.avaliacao.perda.ErroMedioQuadrado;
import rna.estrutura.RedeNeural;
import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class ExemploIris{

   public static void main(String[] args){
      limparConsole();
      Ged ged = new Ged();

      // manusear dados
      // tratamento específico para os dados do iris com adaptação
      // para usar a tangente hiperbolica como função de ativação
      Dados iris = ged.lerCsv("./dados/datasets-maiores/iris.csv");
      ged.removerLinha(iris, 0);
      int[] shape = ged.shapeDados(iris);
      int ultimoIndice = shape[1]-1;

      ged.editarValor(iris, ultimoIndice, "Iris-setosa", "-1");
      ged.editarValor(iris, ultimoIndice, "Iris-versicolor", "0");
      ged.editarValor(iris, ultimoIndice, "Iris-virginica", "1");


      // converter os dados da estrutura de texto em valores numéricos para o 
      // treino da rede neural.
      // separar em treino e teste para evitar overfitting
      double[][] dados = ged.dadosParaDouble(iris);
      ged.embaralharDados(dados);
      double[][][] treinoTeste = (double[][][]) ged.separarTreinoTeste(dados, 0.2f);
      double[][] treino = treinoTeste[0];
      double[][] teste = treinoTeste[1];
      ged.embaralharDados(treino);
      ged.embaralharDados(teste);

      double[][] treinoX, treinoY, testeX, testeY;
      int colunasDados = 4;// quantidade de características dos dados (feature)
      int colunasClasses = 1;// quantidade de classificações dos dados (class)

      treinoX = (double[][]) ged.separarDadosEntrada(treino, colunasDados);
      treinoY = (double[][]) ged.separarDadosSaida(treino, colunasClasses);
      testeX = (double[][]) ged.separarDadosEntrada(teste, colunasDados);
      testeY = (double[][]) ged.separarDadosSaida(teste, colunasClasses);


      // criando, configurando e treinando a rede neural.
      // os valores de configuração não devem ser tomados como regra e 
      // devem se adaptar ao problema e os dados apresentados.
      int[] arq = {colunasDados, 10, 10, colunasClasses};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar(new ErroMedioQuadrado());
      rede.configurarAtivacao(new Sigmoid());
      rede.configurarAtivacao(rede.obterCamadaSaida(), new TanH());
      rede.treinar(treinoX, treinoY, 10_000);


      // avaliando os resultados da rede neural
      double precisao = 1 - rede.avaliador.erroMedioAbsoluto(testeX, testeY);
      double custo = rede.avaliador.erroMedioQuadrado(testeX, testeY);
      System.out.println(rede.info());
      System.out.println("Custo: " + custo);
      System.out.println("Precisão: " + (precisao * 100) + "%");
   }


   public static void limparConsole(){
      try{
         String nomeSistema = System.getProperty("os.name");

         if(nomeSistema.contains("Windows")){
         new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            return;
         }else{
            for (int i = 0; i < 100; i++){
               System.out.println();
            }
         }
      }catch(Exception e){
         return;
      }
   }
}
