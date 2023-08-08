package rna;

import java.io.Serializable;

import rna.ativacoes.ELU;
import rna.ativacoes.FuncaoAtivacao;
import rna.ativacoes.GELU;
import rna.ativacoes.LeakyRelu;
import rna.ativacoes.Linear;
import rna.ativacoes.Relu;
import rna.ativacoes.Seno;
import rna.ativacoes.Sigmoid;
import rna.ativacoes.Swish;
import rna.ativacoes.TanH;


/**
 * Representa uma camada de neurônios em uma rede neural.
 * Cada camada possui um conjunto de neurônios e uma função de ativação que pode ser configurada.
 * A função de ativação é aplicada aos valores de saída dos neurônios após a soma ponderada com os pesos.
 */
public class Camada implements Serializable{
   public Neuronio[] neuronios;
   public boolean temBias = true;
   private int b = 1;
   private boolean argmax = false;
   private boolean softmax = false;

   public FuncaoAtivacao ativacao = new Relu();

   /**
    * Inicializa uma instância de camada de RedeNeural.
    *
    * Após instanciar a camada é preciso inicialiar os neurônios dela.
    * @param temBias define se a camada possui um neurônio de bias. Se true, será adicionado um neurônio adicional
    * que a saída é sempre 1.
    */
   public Camada(boolean temBias){
      this.temBias = temBias;

      if(temBias) b = 1;
      else b = 0;
   }


   /**
    * Calcula a soma dos valores de saída multiplicado pelo peso correspondente ao neurônio da próxima anterior
    * e aplica a função de ativação.
    * @param camadaAnterior camada anterior que contém os valores de saída dos neurônios
    */
   public void ativarNeuronios(Camada camadaAnterior){

      //preencher entradas dos neuronios
      for(int i = 0; i < (this.neuronios.length-b); i++){
         
         Neuronio neuronio = this.neuronios[i];
         for(int j = 0; j < neuronio.entradas.length; j++){
            neuronio.entradas[j] = camadaAnterior.neuronios[j].saida;
         }
      }

      //calculando o somatorio com os pesos
      double somatorio;
      for(int i = 0; i < (this.neuronios.length-b); i++){

         somatorio = 0.0;
         for(int j = 0; j < this.neuronios[i].entradas.length; j++){
            somatorio += (this.neuronios[i].entradas[j] * this.neuronios[i].pesos[j]);
         }
         this.neuronios[i].somatorio = somatorio;
         this.neuronios[i].saida = funcaoAtivacao(somatorio);
      }

      if(argmax) argmax();
      else if(softmax) softmax();
   }


   /**
    * Configura a função de ativação da camada
    * @param ativacao valor da nova função de ativação.
    * @throws IllegalArgumentException se o valor fornecido não corresponder a nenhuma função de ativação suportada.
    */
   public void configurarAtivacao(int ativacao){
      switch(ativacao){
         case 1: this.ativacao = new Relu(); break;
         case 2: this.ativacao = new Sigmoid(); break;
         case 3: this.ativacao = new TanH(); break;
         case 4: this.ativacao = new LeakyRelu(); break;
         case 5: this.ativacao = new ELU(); break;
         case 6: this.ativacao = new Swish(); break;
         case 7: this.ativacao = new GELU(); break;
         case 8: this.ativacao = new Linear(); break;
         case 9: this.ativacao = new Seno(); break;
         case 10: argmax = true; break;
         case 11: softmax = true; break;
         default: throw new IllegalArgumentException("Valor fornecido para a função de ativação está fora de alcance.");
      }
   }


   /**
    * Executa a função de ativação específica da camada.
    * @param valor valor de entrada do neurônio que será ativado.
    * @return valor resultante do cálculo da função de ativação.
    * @throws IllegalArgumentException caso haja algum erro na seleção da função de ativação.
    */
   public double funcaoAtivacao(double valor){
      return ativacao.ativar(valor);
   }


   /**
    * Executa a função de ativação derivada específica da camada.
    * @param valor valor anterior do cálculo da função de ativação
    * @return valor resultante do cálculo da função de ativação derivada.
    * @throws IllegalArgumentException se houver algum erro na seleção da função de ativação derivada.
    */
   public double funcaoAtivacaoDx(double valor){
      return ativacao.derivada(valor);
   }


   public String obterAtivacao(){
      if(argmax) return "Argmax";
      else if(softmax) return "Softmax";
      return this.ativacao.getClass().getSimpleName();
   }


   /**
    * Aplica a função de ativação argmax na saída dos neurônios.
    * Ela define a saída do neurônio de maior valor como 1 e dos demais como 0.
    */
   private void argmax(){
      int indiceMaximo = 0;
      double maiorValor = this.neuronios[0].saida;

      //buscar indice com maior valor
      for(int i = 1; i < this.neuronios.length; i++){
         if(this.neuronios[i].saida > maiorValor){
            maiorValor = this.neuronios[i].saida;
            indiceMaximo = i;
         }
      }

      //aplicar argmax
      for(int i = 0; i < this.neuronios.length; i++){
         this.neuronios[i].saida = (i == indiceMaximo) ? 1.0 : 0.0;
      }
   }


   /**
    * Aplica a função de ativação softmax na saída dos neurônios.
    * Ela normaliza as saídas dos neurônios para formar uma distribuição de probabilidade.
    */
   private void softmax(){
      double somaExp = 0.0;
      
      for(Neuronio neuronio : this.neuronios){
         somaExp += Math.exp(neuronio.saida);
      }

      for(Neuronio neuronio : this.neuronios){
         neuronio.saida = Math.exp(neuronio.saida) / somaExp;
      }
   }
   
}