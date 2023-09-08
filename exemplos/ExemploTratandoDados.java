package exemplos;

import rna.estrutura.RedeNeural;
import rna.otimizadores.Adam;
import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class ExemploTratandoDados{
   public static void main(String[] args){
      limparConsole();
      Ged ged = new Ged();

      Dados breastCancer = ged.lerCsv("./dados/datasets-maiores/breast-cancer-wisconsin.csv");

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
      double[][][] treinoTeste = ged.separarTreinoTeste(dados, 0.25f);
      double[][] treino = treinoTeste[0];
      double[][] teste = treinoTeste[1];
      double[][] treinoEntrada = ged.separarDadosEntrada(treino, qEntradas);
      double[][] treinoSaida = ged.separarDadosSaida(treino, qSaidas);
      double[][] testeEntrada = ged.separarDadosEntrada(teste, qEntradas);
      double[][] testeSaida = ged.separarDadosSaida(teste, qSaidas);

      //construindo a rede neural
      int[] arq = {qEntradas, 8, 8, qSaidas};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar();
      rede.configurarTaxaAprendizagem(0.01);
      rede.configurarMomentum(0.99);
      rede.configurarOtimizador(new Adam());
      rede.configurarFuncaoAtivacao(2);
      rede.treinar(treinoEntrada, treinoSaida, 2_000);

      //avaliando resultados
      System.out.println(rede.info());
      System.out.println("Perda = " + rede.avaliador.erroMedioQuadrado(testeEntrada, testeSaida));
      var precisao = 1 - rede.avaliador.erroMedioAbsoluto(testeEntrada, testeSaida);
      System.out.println("Precisão = " + (precisao * 100) + "%");

   }


   static void limparConsole(){
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
