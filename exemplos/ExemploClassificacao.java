package exemplos;

import java.util.ArrayList;

import rna.RedeNeural;
import utilitarios.ged.Ged;

public class ExemploClassificacao{
   public static void main(String[] args){
      limparConsole();
      Ged ged = new Ged();

      //carregando dados e tratando
      ArrayList<String[]> dataset = ged.lerCsv("./dados/datasets-maiores/iris.csv");
      ged.removerLinha(dataset, 0);
      int ultimoIndice = dataset.get(0).length-1;
      ged.categorizar(dataset, ultimoIndice);
      int[] shape = ged.obterShapeLista(dataset);
      System.out.println("Shape dados = [" + shape[0] + ", " + shape[1] + "]");

      //separando dados de treino e teste
      double[][] dados = ged.listaParaDadosDouble(dataset);
      double[][][] treinoTeste = ged.separarTreinoTeste(dados, 0.25f);
      double[][] treino = treinoTeste[0];
      double[][] teste = treinoTeste[1];
      int qEntradas = 4;// dados de entrada (features)
      int qSaidas = 3;// classificações (class)

      ged.embaralharDados(treino);
      double[][] treinoEntrada = ged.separarDadosEntrada(treino, qEntradas);
      double[][] treinoSaida = ged.separarDadosSaida(treino, qSaidas);

      ged.embaralharDados(teste);
      double[][] testeEntrada = ged.separarDadosEntrada(teste, qEntradas);
      double[][] testeSaida = ged.separarDadosSaida(teste, qSaidas);

      //criando e configurando a rede neural
      int[] arq = {qEntradas, 10, 10, qSaidas};
      RedeNeural rede = new RedeNeural(arq);
      rede.configurarMomentum(0.9);
      rede.configurarTaxaAprendizagem(0.001);
      rede.configurarOtimizador(2, true);
      rede.compilar();
      rede.configurarFuncaoAtivacao(4);
      rede.configurarFuncaoAtivacao(rede.obterCamadaSaida(), 11);//softmax
      
      //treinando e avaliando os resultados
      rede.treinar(treinoEntrada, treinoSaida, 10_000);
      System.out.println(rede.obterInformacoes());
      double acuraria = rede.calcularAcuracia(testeEntrada, testeSaida);
      double custo = rede.entropiaCruzada(testeEntrada, testeSaida);
      System.out.println("Acurácia = " + (acuraria * 100) + "%");
      System.out.println("Custo = " + custo);

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
