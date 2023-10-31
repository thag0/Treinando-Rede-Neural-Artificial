package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação ELU para uso dentro 
 * da {@code Rede Neural}.
 * <p>
 *    É possível configurar o valor de {@code alfa} para obter
 *    melhores resultados.
 * </p>
 */
public class ELU extends Ativacao{
   
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
   public void calcular(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = (neuronio.somatorio > 0) ? neuronio.somatorio : alfa * (Math.exp(neuronio.somatorio) - 1);
      }
   }

   @Override
   public void derivada(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.derivada = (neuronio.somatorio > 0) ? 1 : alfa * Math.exp(neuronio.somatorio);
      }
   }
}
