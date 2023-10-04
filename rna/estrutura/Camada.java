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
 * Representa uma camada densa de neurônios dentro da Rede Neural.
 * <p>
 *    Cada camada possui um conjunto de neurônios e uma função de ativação que 
 *    pode ser configurada.
 * </p>
 *    O bias configurado será aplicado em cada neurônio individualmente.
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
    * Auxiliar na verificação de bias aplicado aos
    * neurônios da camada.
    */
   private boolean bias;

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
    * Capacidade de entrada de dados da camada.
    */
   private int tamanhoEntrada;

   /**
    * Função de ativação da camada que atuará no resultado do somatório entre
    * os pesos e entradas com a adição do bias (se houver).
    */
   private FuncaoAtivacao ativacao;

   /**
    * Auxiliar no controle de inicialização da camada, para evitar problemas de 
    * uso indevido com recursos não alocados.
    */
   private boolean inicializada = false;

   /**
    * Inicializa uma camada densa individual para a Rede Neural.
    * <p>
    *    Após instanciar a camada é preciso inicializar os neurônios dela.
    * </p>
    * Depois de inicializada, a camada pode ser usada por completo.
    * @param neuronios quantidade de neurônios desejados para a camada.
    * @param usarBias define se os neurônios da camada terão um víes aplicado,
    * onde será criada uma entrada e um peso adicional ao neurônio.
    */
   public Camada(int neuronios, boolean usarBias){
      this.neuronios = new Neuronio[neuronios];
      this.bias = usarBias;
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
    * Instancia os todos neurônios da camada, inicializando seus atributos e pesos de 
    * acordo com o inicializador fornecido.
    * @param entrada capacidade de dados de entrada da camada, deve corresponder a quantidade
    * de neurônios da camada anterior, ou no caso da camada de entrada, deve corresponder a 
    * quantidade de dados de entrada.
    * @param alcancePeso valor de alcance da aleatorização dos pesos.
    * @param inicializador inicializador para a geração dos pesos dos neurônios da camada.
    * @throws IllegalArgumentException se o inicializador for nulo.
    */
   public void inicializar(int entrada, double alcancePeso, Inicializador inicializador){
      if(inicializador == null){
         throw new IllegalArgumentException("O inicializador não pode ser nulo.");
      }

      this.tamanhoEntrada = entrada;
      for(int i = 0; i < this.neuronios.length; i++){
         this.neuronios[i] = new Neuronio(entrada, this.bias);
         this.neuronios[i].inicializarPesos(inicializador, alcancePeso, this.neuronios.length);
      }

      this.inicializada = true;//camada pode ser usada
   }

   /**
    * Verificador de inicialização da camada para evitar problemas.
    * @throws IllegalArgumentException caso a camada não esteja inicializada.
    */
   private void verificarInicializacao(){
      if(!this.inicializada){
         throw new IllegalArgumentException(
            "A camada (" + this.id + ") não foi inicializada."
         );
      }
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
         case 10: this.ativacao = new Argmax(); break;
         case 11: this.ativacao = new Softmax(); break;
         case 12: this.ativacao = new SoftPlus(); break;

         default: throw new IllegalArgumentException(
            "Valor fornecido ("  + ativacao + ") para a função de ativação está fora de alcance."
         );
      }
   }

   /**
    * Configura a função de ativação da camada através de uma instância de 
    * {@code FuncaoAtivacao} que será usada para ativar seus neurônios.
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
         throw new IllegalArgumentException(
            "A nova função de ativação não pode ser nula."
         );
      }

      this.ativacao = ativacao;
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
    * @param entrada dados de entrada que serão processados pelos neurônios.
    */
   public void ativarNeuronios(double[] entrada){
      this.verificarInicializacao();

      for(int i = 0; i < this.neuronios.length; i++){
         this.neuronios[i].calcularSaida(entrada);
      }
      this.ativacao.ativar(this.neuronios);
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
      this.verificarInicializacao();
      this.ativacao.derivada(this.neuronios);
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
      this.verificarInicializacao();

      if(id < 0 || id >= this.neuronios.length){
         throw new IllegalArgumentException(
            "Índice fornecido para busca do neurônio (" + id + ") é inválido"
         );
      }

      return this.neuronios[id];
   }

   /**
    * Retorna todo o conjunto de neurônios da camada.
    * @return todos os neurônios presentes na camada.
    */
   public Neuronio[] neuronios(){
      this.verificarInicializacao();
      return this.neuronios;
   }

   /**
    * Retorna o valor da quantidade de neurônios da camada.
    * @return quantidade de neurônios presentes na camada.
    */
   public int quantidadeNeuronios(){
      this.verificarInicializacao();
      return this.neuronios.length;
   }

   /**
    * Retorna a capacidade de entrada da camada.
    * @return tamanho de entrada da camada.
    */
   public int tamanhoEntrada(){
      this.verificarInicializacao();
      return this.tamanhoEntrada;
   }

   /**
    * Verifica se a camada atual possui o bias configurado para seus neurônios.
    * @return true caso possua bias configurado, false caso contrário.
    */
   public boolean temBias(){
      this.verificarInicializacao();
      return this.bias;
   }

   /**
    * Retorda a quantidade de conexões totais da camada, em outras palavras, 
    * retorna o somatório do número de conexões de cada neurônio (incluindo os 
    * valores de entradas e pesos dos bias, caso configurados).
    * @return a quantidade de conexões totais.
    */
   public int numConexoes(){
      this.verificarInicializacao();
      int numConexoes = 0;
      for(int i = 0; i < this.neuronios.length; i++){
         numConexoes += this.neuronios[i].numPesos();
      }
      return numConexoes;
   }

   /**
    * Retorna um array contendo a saída de cada neurônio presente
    * na camada, em ordem crescente.
    * @return saídas dos neurônios da camada.
    */
   public double[] obterSaida(){
      this.verificarInicializacao();
      double[] saida = new double[this.neuronios.length];
      for(int i = 0; i < saida.length; i++){
         saida[i] = this.neuronios[i].saida;
      }

      return saida;
   }

   /**
    * Indica algumas informações sobre a camada, como:
    * <ul>
    *    <li>Id da camada dentro da Rede Neural em que foi criada.</li>
    *    <li>Status de inicialização.</li>
    *    <li>Função de ativação.</li>
    *    <li>Quantidade de neurônios.</li>
    *    <li>Quantidade de conexões.</li>
    *    <li>Bias configurado para seus neurônios.</li>
    * </ul>
    * Algumas informações não estarão disponíveis caso a camada não esteja
    * inicializada.
    * @return buffer formatado contendo as informações da camada.
    */
   public String info(){
      String buffer = "";
      String espacamento = "    ";
      
      buffer += "Informações " + this.getClass().getSimpleName() + " " + this.id + " = [\n";

      buffer += espacamento + "Inicializado: " + this.inicializada + "\n";
      buffer += espacamento + "Ativação: " + this.ativacao.getClass().getSimpleName() + "\n";
      buffer += espacamento + "Quantidade neurônios: " + this.neuronios.length + "\n";
      
      if(this.inicializada){
         buffer += espacamento + "Quantidade de conexões: " + this.numConexoes() + "\n";
         buffer += espacamento + "Bias: " + this.bias + "\n";
         buffer += espacamento + "Tamanho Entrada: " + this.tamanhoEntrada + "\n";
         
      }else{
         buffer += espacamento + "Quantidade de conexões: necessário inicializar\n";
         buffer += espacamento + "Bias: necessário inicializar\n";
         buffer += espacamento + "Tamanho Entrada: necessário inicializar\n";
      }

      buffer += "]\n";

      return buffer;
   }

   /**
    * Clona a instância da camada, criando um novo objeto com as 
    * mesmas características mas em outro espaço de memória.
    * @return clone da camada.
    */
   @Override
   public Camada clone(){
      verificarInicializacao();

      try{
         Camada clone = (Camada) super.clone();

         clone.ativacao = this.ativacao;
         clone.bias = this.bias;
         clone.inicializada = this.inicializada;
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
