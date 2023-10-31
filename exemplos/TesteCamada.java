package exemplos;

import rna.estrutura.Camada;
import rna.inicializadores.Xavier;
import utilitarios.ged.Ged;

public class TesteCamada{
   public static void main(String[] args){
      Ged ged = new Ged();
      ged.limparConsole();
      
      double[][] entrada = {
         {0, 0},
         {0, 1},
         {1, 0},
         {1, 1}
      };
      
      //simples teste unitário de uma camada densa
      //criada para se comportar como uma porta lógica OR.
      Camada camada = new Camada(1);
      camada.inicializar(2, 0, new Xavier());
      camada.configurarAtivacao("sigmoid");

      //valores de uma camada pré treinada
      double[] pesos = {10.51693203318985, 10.516960411442009};
      double bias = -4.797469093074007;
      camada.configurarPesos(0, pesos);
      camada.configurarBias(0, bias);

      for(int i = 0; i < entrada.length; i++){
         double[] amostra = entrada[i];
         camada.calcularSaida(amostra);
         System.out.println(amostra[0] + " - " + amostra[1] + " = " + camada.obterSaida()[0]);
      }
   }
}
