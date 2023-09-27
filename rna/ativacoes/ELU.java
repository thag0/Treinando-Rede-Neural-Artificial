package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação ELU para uso dentro 
 * da {@code Rede Neural}.
 * <p>
 *    É possível configurar o valor de {@code alfa} para obter
 *    melhores resultados.
 * </p>
 */
public class ELU extends FuncaoAtivacao{
   
   /**
    * Valor alfa da função ELU.
    */
   private double alfa;

   /**
    * Instancia a função de ativação ELU com 
    * seu valor de alfa configurável.
    * @param alfa novo valor alfa.
    */
   public ELU(double alfa){
      this.alfa = alfa;
   }

   /**
    * Instancia a função de ativação ELU com 
    * seu valor de alfa padrão.
    * <p>
    *    O valor padrão para o alfa é {@code 0.01}.
    * </p>
    */
   public ELU(){
      this(0.01);
   }

   @Override
   public void ativar(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = (neuronios[i].somatorio > 0) ? neuronios[i].somatorio : alfa * (Math.exp(neuronios[i].somatorio) - 1);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].derivada = (neuronios[i].somatorio > 0) ? 1 : alfa * Math.exp(neuronios[i].somatorio);
      }
   }
}
