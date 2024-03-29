package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação LeakyReLU para uso dentro 
 * da {@code Rede Neural}.
 * <p>
 *    É possível configurar o valor de {@code alfa} para obter
 *    melhores resultados.
 * </p>
 */
public class LeakyReLU extends Ativacao{

   /**
    * Valor alfa da função LeakyReLU.
    */
   private double alfa;

   /**
    * Instancia a função de ativação LeakyReLU com seu valor de alfa configurável.
    * <p>
    *    A ativação LeakyReLU funciona semelhante a função ReLU, retornando o próprio 
    *    valor recebido caso ele seja maior que um, mas caso contrário ela retorna um 
    *    pequeno valor alfa que será multiplicado pela saída.
    * </p>
    * @param alfa novo valor alfa.
    */
   public LeakyReLU(double alfa){
      this.alfa = alfa;
   }

   /**
    * Instancia a função de ativação LeakyReLU com o valor de alfa padrão.
    * <p>
    *    A ativação LeakyReLU funciona semelhante a função ReLU, retornando o próprio 
    *    valor recebido caso ele seja maior que um, mas caso contrário ela retorna um 
    *    pequeno valor alfa que será multiplicado pela saída.
    * </p>
    * <p>
    *    O valor padrão para o alfa é {@code 0.01}.
    * </p>
    */
   public LeakyReLU(){
      this(0.01);
   }
  
   @Override
   public void calcular(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = (neuronio.somatorio > 0) ? neuronio.somatorio : alfa * neuronio.somatorio;
      }
   }

   @Override
   public void derivada(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.derivada = (neuronio.somatorio > 0) ? 1 : alfa;
      }
   }
}
