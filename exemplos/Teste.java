package exemplos;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;
import rna.inicializadores.Xavier;
import utilitarios.ged.Ged;

@SuppressWarnings("unused")
class Teste{
   public static void main(String[] args){
      Ged ged = new Ged();
      ged.limparConsole();

      RedeNeural rna = new RedeNeural(new int[]{0, 2, 2});
      
   }
}