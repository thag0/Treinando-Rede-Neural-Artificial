package exemplos;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import render.JanelaRede;
import rna.ativacoes.LeakyReLU;
import rna.ativacoes.Sigmoid;
import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;
import rna.inicializadores.Aleatorio;
import rna.inicializadores.Inicializador;
import rna.inicializadores.Xavier;
import rna.otimizadores.SGD;
import rna.serializacao.Serializador;
import utilitarios.ged.Ged;
import utilitarios.geim.Geim;
import utilitarios.ged.Dados;

@SuppressWarnings("unused")
public class Teste{
   static Ged ged = new Ged();
   static Geim geim = new Geim();

   public static void main(String[] args){
      ged.limparConsole();

      BufferedImage imagem = geim.lerImagem("/dados/mnist/5.png");
      int[][] cinza = geim.obterVermelho(imagem);

      // ged.imprimirMatriz(v, "vermelho");
      imprimirAscii(cinza);
   }

   static void imprimirAscii(int[][] dados){
      for(int y = 0; y < dados.length; y++){
         for(int x = 0; x < dados[y].length; x++){
            if(dados[y][x] == 0) System.out.print("    ");
            else if(dados[y][x] < 10) System.out.print("00" + dados[y][x] + " ");
            else if(dados[y][x] < 100) System.out.print("0" + dados[y][x] + " ");
            else System.out.print(dados[y][x] + " ");
         }
         System.out.println();
      }
   }
}
