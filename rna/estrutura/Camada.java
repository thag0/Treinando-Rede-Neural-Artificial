package rna.estrutura;

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
import rna.inicializadores.Inicializador;

/**
 * Representa uma camada densa de neurônios dentro de uma Rede Neural.
 * <p>
 *    Cada camada possui um conjunto de neurônios e uma função de ativação que 
 *    pode ser configurada.
 * </p>
 * O bias da camada atua apenas nos neurônios da camada seguinte, ele funciona
 * como um neurônio adicional presente na camda {@code x} e atua na camada {@code x+1}.
 * <p>
 *    Após instanciar a camada é necessário inicializar seus neurônios.
 * </p>
 */
public class Camada implements Cloneable{

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
    * <p>
    *    Seu valor padrão é 1, indicando que há um bias atuando
    *    como neurônio adicional.
    * </p>
    */
   private int b = 1;

   /**
    * Identificador da camada dentro da Rede Neural.
    * <p>
    *    Esse identificador deve corresponder ao índice da camada
    *    dentro do conjunto de camadas presente na rede, sendo 0 o 
    *    índice inicial.
    * </p>
    */
   private int id;

   /**
    * Função de ativação da camada que atuará no resultado do somatório entre
    * os pesos e entradas com a adição do bias (se houver).
    */
   FuncaoAtivacao ativacao;

   /**
    * Auxiliar na verficação se a camada está com a função de ativação
    * Argmax configuarda.
    * <p>
    *    É importante ajustar corretamente esse indicador pois ele é 
    *    usado durante o treinamento da Rede Neural.
    * </p>
    */
   private boolean argmax = false;

   /**
    * Auxiliar na verficação se a camada está com a função de ativação
    * Softmax configuarda.
    * <p>
    *    É importante ajustar corretamente esse indicador pois ele é 
    *    usado durante o treinamento da Rede Neural.
    * </p>
    */ 
   private boolean softmax = false;

   /**
    * Inicializa uma camada individual para a Rede Neural.
    * <p>
    *    Após instanciar a camada é preciso inicializar os neurônios dela.
    * </p>
    * @param temBias define se a camada possui um neurônio de bias. Se true, será 
    * adicionado um neurônio adicional que a saída é sempre 1.
    */
   public Camada(boolean temBias){
      this.b = (temBias) ? 1 : 0;
      this.ativacao = new ReLU();
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
    * Instancia os todos neurônios da camada correspondente, configurando suas entradas e 
    * pesos.
    * @param neuronios quantidade de neurônios que a camada deve possuir, incluindo bias.
    * @param conexoes quantidade de pesos de cada neurônio, deve corresponder a quantidade 
    * de neurônios da camada anterior.
    * @param alcancePeso valor de alcance da aleatorização dos pesos.
    * @param inicializador inicializador customizado para os pesos iniciais da rede.
    */
   public void inicializar(int neuronios, int conexoes, double alcancePeso, Inicializador inicializador){
      this.neuronios = new Neuronio[neuronios];
      
      for(int i = 0; i < this.neuronios.length; i++){
         this.neuronios[i] = new Neuronio(conexoes, this.temBias());
         this.neuronios[i].inicializarPesos(inicializador, alcancePeso, this.neuronios.length);
      }
   }

   /**
    * Realiza a operação do somatório de cada peso do neurônio com sua entrada.
    * <p>
    *    As entradas do neurônio correspondem ás saídas dos neurônios da camada anterior.
    *    Com isso cada neurônio multiplica o {@code peso} da conexão pelo valor da {@code entrada}
    *    correspondente.
    * </p>
    * <p>
    *    Após o somatório é aplicada a função de ativação em cada neurônio e o resultado
    *    é salvo na sua {@code saída}.
    * </p>
    * @param anterior camada anterior que contém os valores de saída dos neurônios.
    */
   public void ativarNeuronios(double[] entrada){
      // esse método de cópia é mais eficiente do que
      // criar um array intermediário e usar o system.arraycopy
      // nas entradas dos neurônios
      for(int i = 0; i < this.neuronios.length; i++){
         for(int j = 0; j < this.neuronios[i].tamanhoEntrada(); j++){
            this.neuronios[i].entradas[j] = entrada[j];
         }
         this.neuronios[i].somatorio();
      }

      this.ativacao.ativar(this.neuronios, this.neuronios.length);
   }

   /**
    * Realiza a operação do somatório de cada peso do neurônio com sua entrada.
    * <p>
    *    As entradas do neurônio correspondem ás saídas dos neurônios da camada anterior.
    *    Com isso cada neurônio multiplica o {@code peso} da conexão pelo valor da {@code entrada}
    *    correspondente.
    * </p>
    * <p>
    *    Após o somatório é aplicada a função de ativação em cada neurônio e o resultado
    *    é salvo na sua {@code saída}.
    * </p>
    * @param anterior camada anterior que contém os valores de saída dos neurônios.
    */
   public void ativarNeuronios(Camada anterior){
      // esse método de cópia é mais eficiente do que
      // criar um array intermediário e usar o system.arraycopy
      // nas entradas dos neurônios
      for(int i = 0; i < this.neuronios.length; i++){
         for(int j = 0; j < this.neuronios[i].tamanhoEntrada(); j++){
            this.neuronios[i].entradas[j] = anterior.neuronios[j].saida;
         }
         this.neuronios[i].somatorio();
      }

      this.ativacao.ativar(this.neuronios, this.neuronios.length);
   }

   /**
    * Configura a função de ativação da camada.
    * <p>
    *    Ativações disponíveis:
    * </p>
    * <ul>
    *    <li> 1 - ReLU. </li>
    *    <li> 2 - Sigmoid. </li>
    *    <li> 3 - Tangente Hiperbólica. </li>
    *    <li> 4 - Leaky ReLU. </li>
    *    <li> 5 - ELU .</li>
    *    <li> 6 - Swish. </li>
    *    <li> 7 - GELU. </li>
    *    <li> 8 - Linear. </li>
    *    <li> 9 - Seno. </li>
    *    <li> 10 - Argmax. </li>
    *    <li> 11 - Softmax. </li>
    *    <li> 12 - Softplus. </li>
    * </ul>
    * @param ativacao valor da nova função de ativação.
    * @throws IllegalArgumentException se o valor fornecido não corresponder a nenhuma 
    * função de ativação suportada.
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
    * Configura a função de ativação da camada através de uma instância de {@code FuncaoAtivacao}.
    * <p>
    *    Configurando a ativação da camada usando uma instância de função 
    *    de ativação aumenta a liberdade de personalização dos hiperparâmetros
    *    que algumas funções podem ter.
    * </p>
    * @param ativacao nova função de ativação.
    * @throws IllegalArgumentException se a função de ativação fornecida for nula.
    */
   public void configurarAtivacao(FuncaoAtivacao ativacao){
      if(ativacao == null){
         throw new IllegalArgumentException("A nova função de ativação não pode ser nula.");
      }

      if(ativacao instanceof Softmax){
         this.softmax = true;
         this.argmax = false;
      
      }else if(ativacao instanceof Argmax){
         this.argmax = true;
         this.softmax = false;
      }

      this.ativacao = ativacao;
   }

   /**
    * Executa a derivada da função de ativação específica da camada
    * em todos os neurônios dela.
    * <p>
    *    O resultado da derivada de cada neurônio estará salvo na 
    *    propriedade {@code neuronio.derivada}.
    * </p>
    */
   public void ativacaoDerivada(){
      this.ativacao.derivada(this.neuronios, this.neuronios.length);
   }

   /**
    * Retorna a instância da função de ativação configurada para a camada.
    * @return função de ativação da camada.
    */
   public FuncaoAtivacao obterAtivacao(){
      return this.ativacao;
   }

   /**
    * Retorna o neurônio da camada baseado no identificador fornecido.
    * @param id índice do neurônio.
    * @return neurônio da camada indicado pelo índice.
    * @throws IllegalArgumentException se o índice for inválido.
    */
   public Neuronio neuronio(int id){
      if(id < 0 || id >= this.neuronios.length){
         throw new IllegalArgumentException("Índice fornecido para busca do neurônio é inválido");
      }
      return this.neuronios[id];
   }

   /**
    * Retorna todo o conjunto de neurônios da camada.
    * @return todos os neurônios presentes na camada, incluindo bias.
    */
   public Neuronio[] neuronios(){
      return this.neuronios;
   }

   /**
    * Retorna o valor da quantidade de neurônios da camada, {@code excluindo bias}.
    * @return quantidade de neurônios presentes na camada.
    */
   public int quantidadeNeuronios(){
      return this.neuronios.length;
   }

   /**
    * Verifica se a camada atual possui o bias configurado como neurônio adicional.
    * @return true caso possua um neurônio adicional como bias, false caso contrário.
    */
   public boolean temBias(){
      return (this.b == 1);
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
    * Retorda a quantidade de conexões totais da camada, em outras palavras, retorna
    * o somatório do número de conexões de cada neurônio.
    * @return a quantidade de conexões totais.
    */
   public int numConexoes(){
      int numConexoes = 0;
      for(int i = 0; i < this.neuronios.length; i++){
         numConexoes += this.neuronios[i].numConexoes();
      }
      return numConexoes;
   }

   /**
    * Indica algumas informações sobre a camada, como:
    * <ul>
    *    <li>Id da camada dentro da Rede Neural em que foi criada.</li>
    *    <li>Função de ativação.</li>
    *    <li>Quantidade de neurônios.</li>
    *    <li>Quantidade de conexões.</li>
    *    <li>Bias como neurônio adicional.</li>
    * </ul>
    * @return buffer formatado contendo as informações da camada.
    */
   public String info(){
      String buffer = "";
      String espacamento = "    ";
      
      buffer += "Informações " + this.getClass().getSimpleName() + " " + this.id + " = [\n";

      buffer += espacamento + "Ativação: " + this.ativacao.getClass().getSimpleName() + "\n";
      buffer += espacamento + "Quantidade neurônios: " + this.neuronios.length + "\n";
      buffer += espacamento + "Quantidade de conexões: " + this.numConexoes() + "\n";
      buffer += espacamento + "Bias: " + ((this.b == 1) ? "true" : "false") + "\n"; 

      buffer += "]\n";

      return buffer;
   }

   /**
    * Clona a instância da camada, criando um novo objeto com as mesmas características
    * mas em outro espaço de memória.
    * @return clone da camada.
    */
   @Override
   public Camada clone(){
      try{
         Camada clone = (Camada) super.clone();

         clone.argmax = this.argmax;
         clone.softmax = this.softmax;
         clone.ativacao = this.ativacao;
         clone.b = this.b;
         clone.id = this.id;

         clone.neuronios = new Neuronio[this.neuronios.length];
         for(int i = 0; i < clone.neuronios.length; i++){
            clone.neuronios[i] = this.neuronio(i).clone();
         }

         return clone;
      }catch(Exception e){
         throw new RuntimeException(e);
      }
   }
}