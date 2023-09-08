package rna;

import java.io.Serializable;

import rna.ativacoes.ELU;
import rna.ativacoes.FuncaoAtivacao;
import rna.ativacoes.GELU;
import rna.ativacoes.LeakyReLU;
import rna.ativacoes.Linear;
import rna.ativacoes.ReLU;
import rna.ativacoes.Seno;
import rna.ativacoes.Sigmoid;
import rna.ativacoes.SoftPlus;
import rna.ativacoes.Swish;
import rna.ativacoes.TanH;


/**
 * Representa uma camada de neurônios individual dentro de uma Rede Neural.
 * Cada camada possui um conjunto de neurônios e uma função de ativação que pode ser configurada.
 */
public class Camada implements Serializable{

   /**
    * <p>
    *    Região crítica.
    * </p>
    * Conjunto de neurônios artificiais da camada.
    */
   Neuronio[] neuronios;

   /**
    * Auxiliar na contagem do neurônio adicional como bias
    * para verificação da quantidade de neurônios reais.
    */
   private int b = 1;

   /**
    * Identificador da camada dentro da Rede Neural.
    */
   private int id;

   /**
    * Função de ativação da camada.
    */
   public FuncaoAtivacao ativacao = new ReLU();

   /**
    * Auxiliar na verficação se a camada está com a função de ativação
    * Argmax configuarda.
    */
   private boolean argmax = false;

   /**
    * Auxiliar na verficação se a camada está com a função de ativação
    * Softmax configuarda.
    */ 
   private boolean softmax = false;


   /**
    * Inicializa uma instância de camada de RedeNeural.
    *
    * Após instanciar a camada é preciso inicialiar os neurônios dela.
    * @param temBias define se a camada possui um neurônio de bias. Se true, será adicionado um neurônio adicional
    * que a saída é sempre 1.
    */
   public Camada(boolean temBias){
      this.b = (temBias) ? 1 : 0;
   }


   /**
    * Configura o id da camada. O id deve indicar dentro da rede neural, em 
    * qual posição a camada está localizada.
    * @param id id da camada.
    */
   public void configurarId(int id){
      this.id = id;
   }


   /**
    * Instancia os neurônios da camada correspondente.
    * @param qNeuronios quantidade de neurônios que a camada deve possuir, incluindo bias.
    * @param qConexoes quantidade de pesos de cada neurônio, deve corresponder a quantidade de neurônios da camada anterior.
    * @param alcancePeso valor de alcance da aleatorização dos pesos.
    * @param inicializador inicializador customizado para os pesos iniciais da rede.
    */
   public void inicializarNeuronios(int qNeuronios, int qConexoes, double alcancePeso, int inicializador){
      this.neuronios = new Neuronio[qNeuronios];
      
      for(int i = 0; i < this.neuronios.length; i++){
         this.neuronios[i] = new Neuronio(qConexoes, alcancePeso, inicializador);
      }
   }


   /**
    * Calcula a soma dos valores de saída multiplicado pelo peso correspondente ao neurônio da próxima 
    * anterior e aplica a função de ativação.
    * @param camadaAnterior camada anterior que contém os valores de saída dos neurônios
    */
   public void ativarNeuronios(Camada camadaAnterior){
      Neuronio neuronio;

      //preencher entradas dos neuronios
      for(int i = 0; i < (this.neuronios.length-b); i++){
         neuronio = this.neuronios[i];
         for(int j = 0; j < neuronio.entradas.length; j++){
            neuronio.entradas[j] = camadaAnterior.neuronios[j].saida;
         }
      }

      //calculando o somatorio das entradas com os pesos
      int qNeuronios = this.neuronios.length-b;
      for(int i = 0; i < qNeuronios; i++){
         neuronio = this.neuronios[i];
         neuronio.somatorio();
         neuronio.saida = this.ativacao.ativar(neuronio.somatorio);
      }

      //sobrescreve a saída
      if(this.argmax) argmax();
      else if(this.softmax) softmax();
   }


   /**
    * Configura a função de ativação da camada.
    * @param ativacao valor da nova função de ativação.
    * @throws IllegalArgumentException se o valor fornecido não corresponder a nenhuma função de ativação suportada.
    */
   public void configurarAtivacao(int ativacao){
      switch(ativacao){
         case 1 -> this.ativacao = new ReLU();
         case 2 -> this.ativacao = new Sigmoid();
         case 3 -> this.ativacao = new TanH();
         case 4 -> this.ativacao = new LeakyReLU();
         case 5 -> this.ativacao = new ELU();
         case 6 -> this.ativacao = new Swish();
         case 7 -> this.ativacao = new GELU();
         case 8 -> this.ativacao = new Linear();
         case 9 -> this.ativacao = new Seno();
         case 10 -> argmax = true;
         case 11 -> softmax = true;
         case 12 -> this.ativacao = new SoftPlus();
         default -> throw new IllegalArgumentException("Valor fornecido para a função de ativação está fora de alcance.");
      }
   }


   /**
    * Executa a função de ativação derivada específica da camada.
    * @param valor valor anterior do cálculo da função de ativação
    * @return valor resultante do cálculo da função de ativação derivada.
    */
   public double funcaoAtivacaoDx(double valor){
      return ativacao.derivada(valor);
   }


   /**
    * Retrona o nome da função de ativação configurada para a camada.
    * @return nome da função de ativação configurada para a camada.
    */
   public String obterAtivacao(){
      if(argmax) return "Argmax";
      else if(softmax) return "Softmax";
      return this.ativacao.getClass().getSimpleName();
   }


   /**
    * Devolve o neurônio correspondente dentro da camada baseado 
    * no identificador fornecido.
    * @param id índice do neurônio.
    * @return neurõnio da camada indicado pelo índice.
    * @throws IllegalArgumentException se o índice for inválido.
    */
   public Neuronio neuronio(int id){
      if(id < 0 || id >= this.neuronios.length){
         throw new IllegalArgumentException("Índice fornecido para busca do neurônio é inválido");
      }
      return this.neuronios[id];
   }


   /**
    * Devolve todo o conjunto de neurônios da camada.
    * @return todos os neurônios presentes na camada, incluindo bias.
    */
   public Neuronio[] neuronios(){
      return this.neuronios;
   }


   /**
    * Retorna o valor da quantidade de neurônios da camada, incluindo bias.
    * @return quantidade de neurônios totais presente na camada.
    */
   public int quantidadeNeuronios(){
      return this.neuronios.length;
   }


   /**
    * Checa se a camada atual possui bias configurado como neurônio adicional.
    * @return true caso possua um neurônio adicional como bias, false caso contrário.
    */
   public boolean temBias(){
      return (b == 1) ? true : false;
   }


   /**
    * Auxiliar na verificação da função de ativação argmax para a camada.
    * @return caso a camada possua a função de ativação argmax configurada.
    */
   public boolean temArgmax(){
      return this.argmax;
   }


   /**
    * Auxiliar na verificação da função de ativação softmax para a camada.
    * @return caso a camada possua a função de ativação softmax configurada.
    */
   public boolean temSoftmax(){
      return this.softmax;
   }


   /**
    * Aplica a função de ativação argmax na saída dos neurônios.
    * Ela define a saída do neurônio de maior valor como 1 e dos demais como 0.
    */
   private void argmax(){
      int indiceMaior = 0;
      double maiorValor = this.neuronios[0].saida;

      //buscar indice com maior valor
      for(int i = 1; i < this.neuronios.length; i++){
         if(this.neuronios[i].saida > maiorValor){
            maiorValor = this.neuronios[i].saida;
            indiceMaior = i;
         }
      }

      //aplicar argmax
      for(int i = 0; i < this.neuronios.length; i++){
         this.neuronios[i].saida = (i == indiceMaior) ? 1.0 : 0.0;
      }
   }


   /**
    * Aplica a função de ativação softmax na saída dos neurônios.
    * Ela normaliza as saídas dos neurônios para formar uma distribuição de probabilidade.
    */
   private void softmax(){
      double somaExp = 0.0;
      
      for(Neuronio neuronio : this.neuronios){
         somaExp += Math.exp(neuronio.somatorio);
      }

      for(Neuronio neuronio : this.neuronios){
         neuronio.saida = Math.exp(neuronio.somatorio) / somaExp;
      }
   }

   
   /**
    * Indica algumas informações sobre a camada, como:
    * <ul>
    *    <li>Id da camada dentro da Rede Neural em que foi criada.</li>
    *    <li>Função de ativação.</li>
    *    <li>Quantidade de neurônios.</li>
    *    <li>Bias como neurônio adicional.</li>
    * </ul>
    * @return buffer formatado contendo as informações da camada.
    */
   public String info(){
      String buffer = "";
      String espacamento = "    ";
      
      buffer += "Informações " + this.getClass().getSimpleName() + " " + id + " = [\n";

      buffer += espacamento + "Ativação: " + this.obterAtivacao() + "\n";
      buffer += espacamento + "Quantidade neurônios: " + this.neuronios.length + "\n";
      buffer += espacamento + "Bias: " + ((b == 1) ? "true" : "false") + "\n"; 

      buffer += "]\n";

      return buffer;
   }
}