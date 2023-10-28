package exemplos;

import java.util.Random;

import rna.estrutura.*;
import rna.inicializadores.*;
import utilitarios.ged.Ged;

@SuppressWarnings("unused")
class Teste{
   static Ged ged = new Ged();

   public static void main(String[] args){
      ged.limparConsole();

      Neuronio neuronio = new Neuronio(2);
   }
}