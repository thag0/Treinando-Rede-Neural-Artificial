package exemplos;

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
import utilitarios.ged.Dados;

@SuppressWarnings("unused")
public class Teste{
   static Ged ged = new Ged();

   public static void main(String[] args){
      ged.limparConsole();

      double[][] in = {
         {0, 0},
         {0, 1},
         {1, 0},
         {1, 1}
      };
      double[][] out = {
         {0},
         {1},
         {1},
         {0}
      };

      int[] arq = {in[0].length, 2, out[0].length};
      RedeNeural rede = new RedeNeural(arq);
      SGD sgd = new SGD();
      rede.compilar(sgd, new Xavier());
      rede.configurarFuncaoAtivacao(new Sigmoid());
      System.out.println(rede.info());

      rede.treinar(in, out, 5_000);

      var perda = rede.avaliador.erroMedioQuadrado(in, out);
      System.out.println("perda = " + perda);

      JanelaRede jr = new JanelaRede(600, 400);
      jr.desenhar(rede);
   }
}
