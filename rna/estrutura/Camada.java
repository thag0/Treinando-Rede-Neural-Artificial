package rna.estrutura;

import java.io.Serializable;

import rna.ativacoes.Argmax;
import rna.ativacoes.ELU;
import rna.ativacoes.FuncaoAtivacao;
import rna.ativacoes.GELU;
import rna.ativacoes.LeakyReLU;
import rna.ativacoes.Linear;
import rna.ativacoes.ReLU;
import rna.ativacoes.Seno;
import rna.ativacoes.Sigmoid;
import rna.ativacoes.SoftPlus;
import rna.ativacoes.Softmax;
import rna.ativacoes.Swish;
import rna.ativacoes.TanH;


/**
 * Representa uma camada de neurônios individual dentro de uma Rede Neural.
 * Cada camada possui um conjunto de neurônios e uma função de ativação que 
 * pode ser configurada.
 * <p>
 *    Após instanciar a camada é necessário inicializar seus neurônios.
 * </p>
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
    * <p>
    *    Após instanciar a camada é preciso inicialiar os neurônios dela.
    * </p>
    * @param temBias define se a camada possui um neurônio de bias. Se true, será 
    * adicionado um neurônio adicional que a saída é sempre 1.
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
    * @param nNeuronios quantidade de neurônios que a camada deve possuir, incluindo bias.
    * @param conexoes quantidade de pesos de cada neurônio, deve corresponder a quantidade de neurônios da camada anterior.
    * @param alcancePeso valor de alcance da aleatorização dos pesos.
    * @param inicializador inicializador customizado para os pesos iniciais da rede.
    */
   public void inicializarNeuronios(int nNeuronios, int conexoes, double alcancePeso, int inicializador){
      this.neuronios = new Neuronio[nNeuronios];
      
      for(int i = 0; i < this.neuronios.length; i++){
         this.neuronios[i] = new Neuronio(conexoes, alcancePeso, inicializador);
      }
   }

   /**
    * Calcula a soma dos valores de saída multiplicado pelo peso correspondente ao neurônio da próxima 
    * anterior e aplica a função de ativação.
    * @param anterior camada anterior que contém os valores de saída dos neurônios
    */
   public void ativarNeuronios(Camada anterior){
      int nNeuronios = this.neuronios.length-b;//desconsiderar bias
      Neuronio neuronio;

      //preencher entradas dos neuronios
      for(int i = 0; i < nNeuronios; i++){
         neuronio = this.neuronios[i];
         for(int j = 0; j < neuronio.entradas.length; j++){
            neuronio.entradas[j] = anterior.neuronios[j].saida;
         }
      }

      //calculando o somatorio das entradas com os pesos
      for(int i = 0; i < nNeuronios; i++){
         neuronio = this.neuronios[i];
         neuronio.somatorio();
      }

      this.ativacao.ativar(this.neuronios, nNeuronios);

      //sobrescreve a saída
      if(this.argmax) argmax();
   }

   /**
    * Configura a função de ativação da camada.
    * @param ativacao valor da nova função de ativação.
    * @throws IllegalArgumentException se o valor fornecido não corresponder a nenhuma função de ativação suportada.
    */
   public void configurarAtivacao(int ativacao){
      switch(ativacao){
         case 1 : this.ativacao = new ReLU(); break;
         case 2 : this.ativacao = new Sigmoid(); break;
         case 3 : this.ativacao = new TanH(); break;
         case 4 : this.ativacao = new LeakyReLU(); break;
         case 5 : this.ativacao = new ELU(); break;
         case 6 : this.ativacao = new Swish(); break;
         case 7 : this.ativacao = new GELU(); break;
         case 8 : this.ativacao = new Linear(); break;
         case 9 : this.ativacao = new Seno(); break;
         case 10: 
            this.ativacao = new Argmax(); 
            this.argmax = true; 
            this.softmax = false; 
            break;
         case 11: 
            this.ativacao = new Softmax(); 
            this.softmax = true;
            this.argmax = false;
            break;
         case 12: this.ativacao = new SoftPlus(); break;
         default: throw new IllegalArgumentException("Valor fornecido para a função de ativação está fora de alcance.");
      }
   }

   /**
    * Configura a função de ativação da camada.
    * <p>
    *    Configurando a ativação da camada usando uma instância de função 
    *    de ativação aumenta a liberdade de personalização dos hiperparâmetros
    *    que algumas funções podem ter.
    * </p>
    * As exceções de funções de ativação que não podem ser configuradas por esse 
    * métrodo são {@code Softmax} e {@code Argmax} devido às suas estruturas.
    * @param ativacao nova função de ativação.
    * @throws IllegalArgumentException se a função de ativação fornecida for nula.
    */
   public void configurarAtivacao(FuncaoAtivacao ativacao){
      if(ativacao == null){
         throw new IllegalArgumentException("A nova função de ativação não pode ser nula.");
      }

      this.ativacao = ativacao;
   }

   /**
    * Executa a função de ativação derivada específica da camada.
    * @param valor valor anterior do cálculo da função de ativação
    * @return valor resultante do cálculo da função de ativação derivada.
    */
   public void funcaoAtivacaoDx(){
      ativacao.derivada(this.neuronios, this.neuronios.length-b);
   }

   /**
    * Retrona o nome da função de ativação configurada para a camada.
    * @return nome da função de ativação configurada para a camada.
    */
   public FuncaoAtivacao obterAtivacao(){
      return this.ativacao;
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