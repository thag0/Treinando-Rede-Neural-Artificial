package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação LeakyReLU para uso dentro 
 * da {@code Rede Neural}.
 * <p>
 *    É possível configurar o valor de {@code alfa} para obter
 *    melhores resultados.
 * </p>
 */
public class LeakyReLU extends FuncaoAtivacao{

   /**
    * Valor alfa da função LeakyReLU.
    */
   private double alfa;

   /**
    * Instancia a função de ativação LeakyReLU com 
    * seu valor de alfa configurável.
    * @param alfa novo valor alfa.
    */
   public LeakyReLU(double alfa){
      this.alfa = alfa;
   }

   /**
    * Instancia a função de ativação LeakyReLU com 
    * seu valor de alfa padrão.
    * <p>
    *    O valor padrão para o alfa é {@code 0.01}.
    * </p>
    */
   public LeakyReLU(){
      this(0.01);
   }
   
   @Override
   public void ativar(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         neuronios[i].saida = (neuronios[i].somatorio > 0) ? neuronios[i].somatorio : alfa * neuronios[i].somatorio;
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         neuronios[i].derivada = (neuronios[i].somatorio > 0) ? 1 : alfa;
      }
   }
}
