package rna.inicializadores;

import java.util.Random;

/**
 * Classe responsável pelas funções de inicialização dos pesos
 * da Rede Neural.
 */
public class Inicializador{

   /**
    * Gerador de números pseudo aleatórios compartilhado
    * para as classes filhas.
    */
   protected Random random = new Random();

   /**
    * Configura o início do gerador aleatório.
    * @param seed nova seed de início.
    */
   public void configurarSeed(long seed){
      this.random.setSeed(seed);
   }

   /**
    * Inicializa os valores do array de acordo com o inicializador configurado.
    * @param array array de pesos do neurônio.
    * @param alcance valor de alcance da aleatorização
    */
   public void inicializar(double[] array, double alcance){
      throw new UnsupportedOperationException(
         "Método de inicialização (array, alcance) não implementado."
      );
   }

   /**
    * Inicializa os valores do array de acordo com o inicializador configurado.
    * @param array array de pesos do neurônio.
    * @param alcance valor de alcance da aleatorização
    */
   public void inicializar(double[] array, int entradas){
      throw new UnsupportedOperationException(
         "Método de inicialização (array, entradas) não implementado."
      );
   }

   /**
    * Inicializa os valores do array de acordo com o inicializador configurado.
    * @param array array de pesos do neurônio.
    * @param entradas quantidade de entradas do neurônio (ou também quantidade de 
    * saídas da camda anterior).
    * @param saidas quantidade de saídas da camada em que o neurônio está.
    */
   public void inicializar(double[] array, int entradas, int saidas){
      throw new UnsupportedOperationException(
         "Método de inicialização (array, entradas, saidas) não implementado."
      );
   }
}
