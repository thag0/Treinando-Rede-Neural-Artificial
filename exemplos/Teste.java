package exemplos;

import java.util.Random;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;
import rna.inicializadores.Aleatorio;
import rna.inicializadores.AleatorioPositivo;
import rna.inicializadores.Inicializador;
import rna.inicializadores.Xavier;
import utilitarios.ged.Ged;

@SuppressWarnings("unused")
class Teste{
   public static void main(String[] args){
      Ged ged = new Ged();
      ged.limparConsole();
      
      double[] teste = new double[5];

      Inicializador ini = new Xavier();
      ini.configurarSeed(123);
      ini.inicializar(teste, 13, 3);
      ged.imprimirArray(teste, "valores");
   }
}