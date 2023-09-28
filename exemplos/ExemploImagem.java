package exemplos;

import java.awt.image.BufferedImage;

import rna.estrutura.RedeNeural;
import rna.inicializadores.Xavier;
import rna.otimizadores.SGD;
import utilitarios.ged.Ged;
import utilitarios.geim.Geim;

public class ExemploImagem{
   
   public static void main(String[] args){
      Ged ged = new Ged();
      Geim geim = new Geim();

      ged.limparConsole();

      //importando imagem para treino da rede
      final String caminho = "/dados/mnist/8.png";
      BufferedImage imagem = geim.lerImagem(caminho);
      double[][] dados = geim.imagemParaDadosTreinoEscalaCinza(imagem);
      int nEntrada = 2;// posição x y do pixel
      int nSaida = 1;// valor de escala de cinza/brilho do pixel

      //preparando dados para treinar a rede
      double[][] dadosEntrada = ged.separarDadosEntrada(dados, nEntrada);
      double[][] dadosSaida = ged.separarDadosSaida(dados, nSaida);

      //criando rede neural para lidar com a imagem
      //nesse exemplo queremos que ela tenha overfitting
      int[] arq = {nEntrada, 12, 12, nSaida};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar(new SGD(), new Xavier());
      rede.configurarAtivacao(2);
      rede.treinar(dadosEntrada, dadosSaida, 5_000);

      //avaliando resultados
      double precisao = 1 - rede.avaliador.erroMedioAbsoluto(dadosEntrada, dadosSaida);
      double perda = rede.avaliador.erroMedioQuadrado(dadosEntrada, dadosSaida);
      System.out.println(rede.info());
      System.out.println("Precisão = " + (precisao * 100));
      System.out.println("Perda = " + perda);
   }
}
