package rna;

import java.io.Serializable;

/**
 * Representa uma camada de neurônios em uma rede neural.
 * Cada camada possui um conjunto de neurônios e uma função de ativação que pode ser configurada.
 * A função de ativação é aplicada aos valores de saída dos neurônios após a soma ponderada com os pesos.
 */
public class Camada implements Serializable{
   public Neuronio[] neuronios;
   public boolean temBias = true;

   //melhor controle das funções de ativação
   private enum FuncaoAtivacao{
      RELU,
      SIGMOID,
      TANH,
      LEAKY_RELU,
      ELU,
      LINEAR,
      SENO,
      ARGMAX,
      SOFTMAX
   }

   public FuncaoAtivacao ativacao = FuncaoAtivacao.RELU;


   /**
    * Inicializa uma instância de camada de RedeNeural. Ainda é preciso configurar a quantidade de neurõnios dela.
    * @param temBias define se a camada possui um neurônio de bias. Se true, será adicionado um neurônio adicional
    * que a saída é sempre 1.
    */
   public Camada(boolean temBias){
      this.temBias = temBias;
   }


   /**
    * Calcula a soma dos valores de saída multiplicado pelo peso correspondente ao neurônio da próxima camada
    * e aplica a função de ativação.
    * @param camadaAnterior camada anterior que contém os valores de saída dos neurônios e seus pesos
    */
   public void ativarNeuronios(Camada camadaAnterior){
      int qNeuronios = this.neuronios.length - ((temBias) ? 1 : 0);

      for(int i = 0; i < qNeuronios; i++){

         this.neuronios[i].entrada = 0;
         //somatorio dos pesos com as saídas
         for(int j = 0; j < camadaAnterior.neuronios.length; j++){
            //peso relativo ao neuronio da camada atual
            this.neuronios[i].entrada += (camadaAnterior.neuronios[j].saida * camadaAnterior.neuronios[j].pesos[i]);
         }
      }

      if(this.ativacao == FuncaoAtivacao.ARGMAX) Ativacoes.argmax(this);
      else if(this.ativacao == FuncaoAtivacao.SOFTMAX) Ativacoes.softmax(this);
      else{
         for(int i = 0; i < qNeuronios; i++){
            this.neuronios[i].saida = funcaoAtivacao(this.neuronios[i].entrada);
         }
      }
   }

   /**
    * Configura a função de ativação da camada
    * @param ativacao valor da nova função de ativação.
    * @throws IllegalArgumentException se o valor fornecido não corresponder a nenhuma função de ativação suportada.
    */
   public void configurarAtivacao(int ativacao){
      switch(ativacao){
         case 1: this.ativacao = FuncaoAtivacao.RELU; break;
         case 2: this.ativacao = FuncaoAtivacao.SIGMOID; break;
         case 3: this.ativacao = FuncaoAtivacao.TANH; break;
         case 4: this.ativacao = FuncaoAtivacao.LEAKY_RELU; break;
         case 5: this.ativacao = FuncaoAtivacao.ELU; break;
         case 6: this.ativacao = FuncaoAtivacao.LINEAR; break;
         case 7: this.ativacao = FuncaoAtivacao.SENO; break;
         case 8: this.ativacao = FuncaoAtivacao.ARGMAX; break;
         case 9: this.ativacao = FuncaoAtivacao.SOFTMAX; break;
         default: throw new IllegalArgumentException("Valor fornecido para a função de ativação está fora de alcance.");
      }
   }


   /**
    * Executa a função de ativação específica da camada.
    * @param valor valor de entrada do neurônio que será ativado.
    * @return valor resultante do cálculo da função de ativação.
    * @throws IllegalArgumentException caso haja algum erro na seleção da função de ativação.
    */
   private double funcaoAtivacao(double valor){
      if(this.ativacao == FuncaoAtivacao.RELU) return Ativacoes.relu(valor);
      if(this.ativacao == FuncaoAtivacao.SIGMOID) return Ativacoes.sigmoid(valor);
      if(this.ativacao == FuncaoAtivacao.TANH) return Ativacoes.tanH(valor);
      if(this.ativacao == FuncaoAtivacao.LEAKY_RELU) return Ativacoes.leakyRelu(valor);
      if(this.ativacao == FuncaoAtivacao.ELU) return Ativacoes.elu(valor);
      if(this.ativacao == FuncaoAtivacao.LINEAR) return Ativacoes.linear(valor);
      if(this.ativacao == FuncaoAtivacao.SENO) return Ativacoes.seno(valor);

      //se por algum motivo não achar a função de ativação
      throw new IllegalArgumentException("Erro ao selecionar a ativação.");
   }


   /**
    * Executa a função de ativação derivada específica da camada.
    * @param valor valor anterior do cálculo da função de ativação
    * @return valor resultante do cálculo da função de ativação derivada.
    * @throws IllegalArgumentException se houver algum erro na seleção da função de ativação derivada.
    */
   public double funcaoAtivacaoDx(double valor){
      if(this.ativacao == FuncaoAtivacao.RELU) return Ativacoes.reluDx(valor);
      if(this.ativacao == FuncaoAtivacao.SIGMOID) return Ativacoes.sigmoidDx(valor);
      if(this.ativacao == FuncaoAtivacao.TANH) return Ativacoes.tanHDx(valor);
      if(this.ativacao == FuncaoAtivacao.LEAKY_RELU) return Ativacoes.leakyReluDx(valor);
      if(this.ativacao == FuncaoAtivacao.ELU) return Ativacoes.eluDx(valor);
      if(this.ativacao == FuncaoAtivacao.LINEAR) return Ativacoes.linearDx(valor);
      if(this.ativacao == FuncaoAtivacao.SENO) return Ativacoes.senoDx(valor);

      throw new IllegalArgumentException("Erro ao selecionar a ativação derivada.");
   }
}