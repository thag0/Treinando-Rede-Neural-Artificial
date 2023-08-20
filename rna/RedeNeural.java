package rna;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Random;

import rna.avaliacao.Avaliador;
import rna.otimizadores.AdaGrad;
import rna.otimizadores.Adam;
import rna.otimizadores.GradientDescent;
import rna.otimizadores.Otimizador;
import rna.otimizadores.RMSProp;
import rna.otimizadores.SGD;
import rna.treinamento.Treino;

//TODO
//Implementar formas melhores de treinar com uma grande quantidade de dados.
//Formas mais elaboradas de otmizadores.
//Suporte ao treino da rede usando argmax ou softmax na saída

/**
 * Modelo de Rede Neural Multilayer Perceptron baseado em feedforward criado do zero. Possui um conjunto de camadas 
 * e cada camada possui um conjunto de neurônios artificiais.
 * <p>
 *    O modelo pode ser usado para problemas de regressão e classificação, contando com algoritmos de treino 
 *    atualmente baseados no backpropagation com adição da ideia de momentum na atualização dos pesos.
 * </p>
 * Possui opções de configuração tanto para hiperparâmetros como taxa de aprendizagem e momentun, quanto para
 * funções de ativações de camadas individuais, valor de alcance máximo e mínimo na aleatorização dos pesos iniciais e 
 * otimizadores que serão usados durante o treino. 
 * <p>
 *    Após configurar as propriedades da rede, o modelo precisará ser compilado para efetivamente poder ser utilizado.
 * </p>
 * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, Campus Tucuruí. Ano de 2023.
 */
public class RedeNeural implements Cloneable, Serializable{
   //estrutura 
   private Camada entrada;
   private Camada[] ocultas;
   private Camada saida;
   private int[] arquitetura;
   
   //parâmertos importantes
   private double TAXA_APRENDIZAGEM = 0.01;
   private double TAXA_MOMENTUM = 0;
   private int BIAS = 1;
   private double alcancePeso = 1.0;
   private int inicializadorPeso = 0;//aleatorio
   private boolean modeloCompilado = false;
   private Otimizador otimizadorAtual = new SGD();//otimizador padrão
   private boolean nesterov = false;// acelerador de nesterov

   private String nome = getClass().getSimpleName();
   
   private Treino treino = new Treino();

   /**
    * Objeto responsável pelo retorno de desempenho da Rede Neural.
    * Contém implementações de métodos tanto para cálculo de perdas
    * quanto para cálculo de métricas.
    * <p>
    *    Cada instância de rede neural possui seu próprio avaliador.
    * </p>
    */
   public Avaliador avaliador = new Avaliador(this);

   Random random = new Random();//treino embaralhado


   /**
    * <p>
    *    Cria uma instância de rede neural artificial. A arquitetura da rede será baseada de acordo com cada posição do array,
    *    cada valor contido nele representará a quantidade de neurônios da camada correspondente.
    * </p> 
    * <p>
    *    A camada de entrada deverá ser especificada pelo indice 0, a camada de saída 
    *    será representada pelo último valor do array e as camadas ocultas serão representadas pelos valores intermediários.
    * </p>
    * <p>
    *   Os valores de todos os parâmetros pedidos <strong>NÃO devem</strong> ser menores que 1.
    * </p>
    * <p>
    *    Após instanciar o modelo, é necessário compilar por meio da função <pre>compilar()</pre>
    * </p>
    * <p>
    *    Certifique-se de configurar as propriedades da rede por meio das funções de configuração fornecidas
    *    como, alcance dos pesos iniciais, taxa de aprendizagem e uso de bias. Caso não seja usada nenhuma 
    *    das funções de configuração, a rede será compilada com os valores padrão.
    * </p>
    * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, Campus Tucuruí. Ano de 2023.
    * @param arquitetura modelo de arquitetura específico da rede.
    * @throws IllegalArgumentException se o array de arquitetura não possuir, pelo menos, três elementos.
    * @throws IllegalArgumentException se os valores fornecidos forem menores que um.
    */
   public RedeNeural(int[] arquitetura){
      if(arquitetura.length < 3) throw new IllegalArgumentException("A arquitetura da rede não pode conter menos de três elementos");
      
      for(int i = 0; i < arquitetura.length; i++){
         if(arquitetura[i] < 1) throw new IllegalArgumentException("Os valores fornecidos devem ser maiores ou iguais a um.");
      }

      int quantidadeOcultas = arquitetura.length-2;//evitar problemas
      this.arquitetura = new int[1 + quantidadeOcultas + 1];
      this.arquitetura[0] = arquitetura[0];
      for(int i = 0; i < quantidadeOcultas; i++) this.arquitetura[i+1] = arquitetura[i+1];
      this.arquitetura[this.arquitetura.length-1] = arquitetura[arquitetura.length-1];
   }


   /**
    * <p>
    *    Cria uma instância de rede neural artificial. A arquitetura da rede será baseada de acordo com os valores 
    *    de número de neurônios fornecidos.
    * </p>
    * <p>
    *   Os valores de todos os parâmetros pedidos <strong>NÃO devem</strong> ser menores que 1.
    * </p>
    * <p>
    *    Após instanciar o modelo, é necessário compilar por meio da função <pre>compilar()</pre>
    * </p>
    * <p>
    *    Certifique-se de configurar as propriedades da rede por meio das funções de configuração fornecidas
    *    como, alcance dos pesos iniciais, taxa de aprendizagem e uso de bias. Caso não seja usada nenhuma 
    *    das funções de configuração, a rede será compilada com os valores padrão.
    * </p>
    * @param nEntrada número de neurônios da camada de entrada.
    * @param nOcultas número de neurônios das camadas ocultas.
    * @param nSaida número de neurônios da camada de saída.
    * @param qOcultas quantidade de camadas ocultas.
    * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, Campus Tucuruí. Ano de 2023.
    * @throws IllegalArgumentException se algum dos valores fornecidos for menor que 1.
    */
   public RedeNeural(int nEntrada, int nOcultas, int nSaida, int qOcultas){
      if(nEntrada < 1 || nOcultas < 1 || nSaida < 1 || qOcultas < 1){
         throw new IllegalArgumentException("Os valores fornecidos devem ser maiores ou iguais a 1.");
      }

      this.arquitetura = new int[1 + qOcultas + 1];

      this.arquitetura[0] = nEntrada;
      for(int i = 1; i < this.arquitetura.length-1; i++){
         this.arquitetura[i] = nOcultas;
      }
      this.arquitetura[this.arquitetura.length-1] = nSaida;
   }


   /**
    * <p>
    *    Altera o nome da rede neural.
    * </p>
    * O nome é apenas estético e não influencia na performance ou na 
    * usabilidade da rede neural.
    * <p>
    *    O nome padrão é o mesmo nome da classe (RedeNeural).
    * </p>
    * @param nome novo nome da rede.
    * @throws IllegalArgumentException se o novo nome for inválido.
    */
   public void configurarNome(String nome){
      if(nome == null) throw new IllegalArgumentException("O novo nome da rede neural não pode ser nulo.");
      if(nome.isBlank() || nome.isEmpty()){
         throw new IllegalArgumentException("O novo nome da rede neural não pode estar vazio.");
      }

      this.nome = nome;
   }


   /**
    * Define o valor máximo e mínimo na hora de aleatorizar os pesos da rede 
    * para a compilação, os novos valores não podem ser menores ou iguais a zero.
    * <p>
    *    O valor padrão de alcance é 1.
    * </p>
    * @param alcancePesos novo valor máximo e mínimo.
    * @throws IllegalArgumentException se o novo valor for menor ou igual a zero.
    */
   public void configurarAlcancePesos(double alcancePesos){
      if(alcancePesos <= 0){
         throw new IllegalArgumentException("Os novos valores de alcance dos pesos não podem ser menores ou iguais a zero.");
      }
      this.alcancePeso = alcancePesos;
   }


   /**
    * Configura a inicialização dos pesos da rede neural. A forma de inicialização pode
    * afetar o tempo de convergência da rede durante o treinamento.
    * <p>
    *    Inicializadores disponíveis:
    * </p>
    * <ul>
    *    <li>1 - Aleatória com valor dos pesos definido por {@code alcancePeso}.</li>
    *    <li>2 - He.</li>
    *    <li>3 - LeCun.</li>
    * </ul>
    * <p>
    *    O valor padrão de inicializador é 0.
    * </p>
    * @param inicializador novo valor de inicializador de pesos da rede.
    * @throws IllegalArgumentException se o novo valor de otimizador for menor que um.
    * @throws IllegalArgumentException se o novo valor de otimização for inválido, no momento da compilação.
    */
   public void configurarInicializacaoPesos(int inicializador){
      if(inicializador < 1) throw new IllegalArgumentException("O novo valor de otimizador não pode ser menor que um.");

      this.inicializadorPeso = inicializador;
   }


   /**
    * Define se a rede neural usará um neurônio adicional como bias nas camadas da rede.
    * O bias não é adicionado na camada de saída.
    * <p>
    *    O valor padrão para uso do bias é true.
    * </p>
    * @param usarBias novo valor para o uso do bias.
    */
   public void configurarBias(boolean usarBias){
      this.BIAS = (usarBias) ? 1 : 0;
   }


   /**
    * Define o novo valor de taxa de aprendizagem da rede. O valor é usado durante o método de treino.
    * O valor da taxa de aprendizagem difine "o quanto a rede vai aprender com o erro durante o treinamento".
    * Certifique-se de não usar valores muito altos ou muito baixos para não gerar resultados inesperados 
    * durante o treino.
    * <p>
    *    O valor padrão é 0.1.
    * </p>
    * @param taxaAprendizagem novo valor de taxa de aprendizagem.
    * @throws IllegalArgumentException caso o novo valor de taxa de aprendizagem seja igual a zero.
    */
   public void configurarTaxaAprendizagem(double taxaAprendizagem){
      if(taxaAprendizagem <= 0){
         throw new IllegalArgumentException("O valor da nova taxa de aprendizagem não pode ser menor ou igual a zero.");
      }
      this.TAXA_APRENDIZAGEM = taxaAprendizagem;
   }


   /**
    * Define o novo valor do momentum que vai ser usado no treinamento da rede.
    * <p>
    *    O momentum é uma técnica utilizada no treinamento de redes neurais para acelerar a convergência e
    *    melhorar a estabilidade das atualizações dos pesos. Ele introduz um termo de momento nas atualizações
    *    dos pesos, permitindo que as atualizações tenham inércia, acumulando uma fração dos gradientes das
    *    iterações anteriores.
    * </p>
    * Normalmente esse valor fica entre 0 e 1, onde 0 significa que o momentum não terá efeito  e 1 
    * significa que o momentum terá o máximo de inércia, acumulando totalmente os gradientes anteriores. 
    * <p>
    *    O valor padrão é 0, onde o momentum não é aplicado no treino.
    * </p>
    * @param momentum novo valor de momentum.
    * @throws IllegalArgumentException se o valor de momentum for menor que zero.
    */
   public void configurarMomentum(double momentum){
      if(momentum < 0){
         throw new IllegalArgumentException("O valor de momentum não pode ser menor que zero.");
      }
      this.TAXA_MOMENTUM = momentum;
   }


   /**
    * Configura a função de ativação da camada correspondente, a função de ativação padrão é a ReLU. É preciso
    * compilar o modelo previamente para poder configurar suas funções de ativação.
    * <p>
    *    segue a lista das funções disponíveis:
    * </p>
    * <ul>
    *    <li>1 - ReLU.</li>
    *    <li>2 - Sigmoid.</li>
    *    <li>3 - Tangente Hiperbólica.</li>
    *    <li>4 - Leaky ReLU.</li>
    *    <li>5 - ELU.</li>
    *    <li>6 - Swish.</li>
    *    <li>7 - GELU.</li>
    *    <li>8 - Linear.</li>
    *    <li>9 - Seno.</li>
    *    <li>10 - Argmax.</li>
    *    <li>11 - Softmax.</li>
    * </ul>
    * @param camada camada que será configurada.
    * @param ativacao valor relativo a lista de ativações disponíveis.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public void configurarFuncaoAtivacao(Camada camada, int ativacao){
      this.modeloCompilado();
      if(camada.equals(this.entrada)){
         throw new IllegalArgumentException("Não é possível configurar função de ativação para a camada de entrada.");
      } 
      
      camada.configurarAtivacao(ativacao);
   }


   /**
    * Configura a função de ativação de todas as camadas da rede, a função de ativação padrão é a ReLU. É preciso
    * compilar o modelo previamente para poder configurar suas funções de ativação
    * <p>
    *    segue a lista das funções disponíveis:
    * </p>
    * <ul>
    *    <li>1 - ReLU.</li>
    *    <li>2 - Sigmoid.</li>
    *    <li>3 - Tangente Hiperbólica.</li>
    *    <li>4 - Leaky ReLU.</li>
    *    <li>5 - ELU.</li>
    *    <li>6 - Swish.</li>
    *    <li>7 - GELU.</li>
    *    <li>8 - Linear.</li>
    *    <li>9 - Seno.</li>
    *    <li>10 - Argmax.</li>
    *    <li>11 - Softmax.</li>
    * </ul>
    * @param ativacao valor relativo a lista de ativações disponíveis.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public void configurarFuncaoAtivacao(int ativacao){
      this.modeloCompilado();
      
      for(Camada camada : this.ocultas) camada.configurarAtivacao(ativacao);
      this.saida.configurarAtivacao(ativacao);
   }


   /**
    * Configura o otimizador usado durante o treino da rede neural.
    * <p>
    *    O otimizador padrão é o SGD (Gradiente Descendente Estocástico).
    * </p>
    * <p>
    *    Os otimizadores disponíveis são:
    * </p>
    * <ul>
    *    <li>1 - Backpropagation: Método clássico de retropropagação de erro para treinamento de redes neurais.</li>
    *    <li>2 - SGD (Gradiente Descendente Estocástico): Atualiza os pesos usando o conjunto de treino embaralhado.</li>
    *    <li>3 - AdaGrad: Um otimizador que adapta a taxa de aprendizado para cada parâmetro da rede com base em iterações anteriores.</li>
    *    <li>4 - RMSProp: Um otimizador que utiliza a média móvel dos quadrados dos gradientes acumulados para ajustar a taxa de aprendizado.</li>
    *    <li>5 - Adam: Um otimizador que combina o AdaGrad e o Momentum para convergência rápida e estável.</li>
    * </ul>
    * @param otimizador valor do novo otimizador.
    * @throws IllegalArgumentException se o valor fornecido do otimizador estiver fora da lista dos disponíveis.
    */
   public void configurarOtimizador(int otimizador){
      switch(otimizador){
         case 1 -> this.otimizadorAtual = new GradientDescent();
         case 2 -> this.otimizadorAtual = new SGD();
         case 3 -> this.otimizadorAtual = new AdaGrad();
         case 4 -> this.otimizadorAtual = new RMSProp();
         case 5 -> this.otimizadorAtual = new Adam();
         default-> throw new IllegalArgumentException("Valor fornecido do otimizador é inválido.");
      }
   }


   /**
    * Configura o otimizador usado durante o treino da rede neural.
    * <p>
    *    O otimizador padrão é o SGD (Gradiente Descendente Estocástico).
    * </p>
    * <p>
    *    Os otimizadores disponíveis são:
    * </p>
    * <ul>
    *    <li>1 - Backpropagation: Método clássico de retropropagação de erro para treinamento de redes neurais.</li>
    *    <li>2 - SGD (Gradiente Descendente Estocástico): Atualiza os pesos usando o conjunto de treino embaralhado.</li>
    *    <li>3 - AdaGrad: Um otimizador que adapta a taxa de aprendizado para cada parâmetro da rede com base em iterações anteriores.</li>
    *    <li>4 - RMSProp: Um otimizador que utiliza a média móvel dos quadrados dos gradientes acumulados para ajustar a taxa de aprendizado.</li>
    *    <li>5 - Adam: Um otimizador que combina o AdaGrad e o Momentum para convergência rápida e estável.</li>
    * </ul>
    * @param otimizador valor do novo otimizador.
    * @param nesterov configura se o otimizador vai usar o acelerador de Nesterov (Por enquanto só pro SGD)
    * @throws IllegalArgumentException se o valor fornecido do otimizador estiver fora da lista dos disponíveis.
    */
   public void configurarOtimizador(int otimizador, boolean nesterov){
      switch(otimizador){
         case 1 -> this.otimizadorAtual = new GradientDescent();
         case 2 -> this.otimizadorAtual = new SGD(nesterov);
         case 3 -> this.otimizadorAtual = new AdaGrad();
         case 4 -> this.otimizadorAtual = new RMSProp();
         case 5 -> this.otimizadorAtual = new Adam();
         default-> throw new IllegalArgumentException("Valor fornecido do otimizador é inválido.");
      }

      this.nesterov = nesterov;
   }


   /**
    * Define se durante o processor de treinamento, a rede vai salvar dados relacionados a 
    * função de custo de cada época.
    * <p>
    *    Calcular o custo é uma operação que pode ser computacionalmente cara, então deve ser
    *    bem avaliado querer ativar ou não esse recurso.
    * </p>
    * <p>
    *    O valor padrão é false.
    * </p>
    * @param historicoCusto se verdadeiro, a rede armazenara o histórico de custo de cada época.
    */
   public void configurarHistoricoCusto(boolean historicoCusto){
      this.treino.configurarHistoricoCusto(historicoCusto);
   }


   /**
    * Compila o modelo de rede inicializando as camadas, neurônios e pesos respectivos, 
    * baseado nos valores fornecidos. Antes da compilação é possível
    * informar alguns valores ajustáveis na inicialização da rede, como:
    * <ul>
    *    <li>Valor máximo e mínimo para os pesos gerados aleatoriamente.</li>
    *    <li>Funções de ativação para as camadas ocultas e para a camada de saída.</li>
    *    <li>Neurônios adicionais nas camadas atuando como bias.</li>
    *    <li>Taxa de aprendizagem.</li>
    *    <li>Taxa de momentum.</li>
    *    <li>Otimizador.</li>
    *    <li>Habilitar histórico de erros e custos durante o treino.</li>
    * </ul>
    * <p>
    *    Caso nenhuma configuração seja feita, a rede será inicializada com os valores padrão. 
    * </p>
    * Após a compilação o modelo está pronto para ser usado, mas deverá ser treinado.
    * <p>
    *    Para treinar o modelo deve-se fazer uso da função função {@code treinar()} informando os 
    *    dados necessários para a rede.
    * </p>
    * <p>
    *    Para usar as predições da rede basta usar a função {@code calcularSaida()} informando os
    *    dados necessários. Após a predição pode-se obter o resultado da rede por meio da função 
    *    {@code obterSaidas()};
    * </p>
    */
   public void compilar(){

      //adicionando bias como neuronio adicional nas camadas
      //não adicionar na camada de saída
      for(int i = 0; i < arquitetura.length-1; i++){
         arquitetura[i] += BIAS;
      }

      //passar as informações do bias paras as camadas
      boolean temBias = (this.BIAS == 1) ? true : false;
      
      //inicializar camada de entrada
      this.entrada = new Camada(temBias);
      this.entrada.inicializarNeuronios(arquitetura[0], 1, alcancePeso, inicializadorPeso);

      //inicializar camadas ocultas
      int quantidadeOcultas = this.arquitetura.length-2;
      this.ocultas = new Camada[quantidadeOcultas];
      for(int i = 0; i < this.ocultas.length; i++){// percorrer ocultas
         Camada novaOculta = new Camada(temBias);

         if(i == 0) novaOculta.inicializarNeuronios(arquitetura[i+1], arquitetura[0], alcancePeso, inicializadorPeso);
         else novaOculta.inicializarNeuronios(arquitetura[i+1], arquitetura[i], alcancePeso, inicializadorPeso);

         this.ocultas[i] = novaOculta;
      }

      //inicializar camada de saída
      this.saida = new Camada(false);
      this.saida.inicializarNeuronios(arquitetura[arquitetura.length-1], arquitetura[arquitetura.length-2], alcancePeso, inicializadorPeso);

      modeloCompilado = true;//modelo pode ser usado
   }


   /**
    * Verifica se o modelo já foi compilado para evitar problemas de uso indevido, bem como componentes nulos.
    * @throws IllegalArgumentException se o modelo não foi compilado.
    */
   private void modeloCompilado(){
      if(!this.modeloCompilado){
         throw new IllegalArgumentException("O modelo ainda não foi compilado");
      }
   }


   /**
    * Verifica se os dados são apropriados para serem usados dentro da rede neural, incluindo:
    * <ul>
    *    <li>Mesma quantidade de amostras nos dados de entrada e saída.</li>
    *    <li>Dados de entrada possuem a mesma quantidade de exemplos que a quantidade de neurônios
    *    da camada de entrada da rede, exluindo bias.</li>
    *    <li>Dados de saída possuem a mesma quantidade de exemplos que a quantidade de neurônios
    *    da camada de saída da rede.</li>
    * </ul>
    * Caso os dados fornecidos atendam a essas condições, o fluxo de execução segue normalmente.
    * @param dadosEntrada conjunto de dados de entrada.
    * @param dadosSaida conjunto de dados de saída.
    */
   private void consistenciaDados(double[][] dadosEntrada, double[][] dadosSaida){
      int nEntrada = this.obterCamadaEntrada().obterQuantidadeNeuronios();
      nEntrada -= (this.obterCamadaEntrada().temBias) ? 1 : 0;
      int nSaida = this.obterCamadaSaida().obterQuantidadeNeuronios();

      if(dadosEntrada.length != dadosSaida.length){
         throw new IllegalArgumentException("A quantidade de linhas de dados e saídas são diferentes.");
      }
      if(nEntrada != dadosEntrada[0].length){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de entrada e os neurônios de entrada da rede.");
      }
      if(nSaida != dadosSaida[0].length){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de saída e os neurônios de saída da rede.");
      }      
   }


   /**
    * <p>
    *    Propaga os dados de entrada pela rede neural pelo método de feedforward.
    * </p>
    * Os dados são alimentados para as entradas dos neurônios e é calculado o produto junto com os pesos.
    * No final é aplicado a função de ativação da camada no neurônio e o resultado fica armazenado na saída dele.
    * @param entradas dados usados para alimentar a camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos neurônios de entrada, excluindo o bias.
    */
   public void calcularSaida(double[] entradas){
      this.modeloCompilado();
      
      if(entradas.length != (this.entrada.obterQuantidadeNeuronios()-BIAS)){
         throw new IllegalArgumentException("As dimensões dos dados de entrada com os neurônios de entrada da rede não são iguais.");
      }

      //carregar dados na camada de entrada
      for(int i = 0; i < (this.entrada.obterQuantidadeNeuronios()-BIAS); i++){
         this.entrada.neuronios[i].saida = entradas[i];
      }
      
      //ativar neurônios das ocultas
      for(int i = 0; i < this.ocultas.length; i++){
         if(i == 0) this.ocultas[i].ativarNeuronios(this.entrada);
         else this.ocultas[i].ativarNeuronios(this.ocultas[i-1]);
      }

      //ativar neurônios da saída
      this.saida.ativarNeuronios(this.ocultas[this.ocultas.length-1]);
   }


   /**
    * <p>
    *    Propaga os dados de entrada pela rede neural pelo método de feedforward através do conjunto 
    *    de dados fornecido.
    * </p>
    * Os dados são alimentados para as entradas dos neurônios e é calculado o produto junto com os pesos.
    * No final é aplicado a função de ativação da camada no neurônio e o resultado fica armazenado na saída dele.
    * @param entradas dados usados para alimentar a camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos neurônios de entrada, excluindo o bias.
    * @return matriz contendo os resultados das predições da rede.
    */
   public double[][] calcularSaida(double[][] entradas){
      this.modeloCompilado();

      if(entradas[0].length != (this.entrada.neuronios.length-BIAS)){
         throw new IllegalArgumentException("As dimensões dos dados de entrada com os neurônios de entrada da rede não são iguais.");
      }

      //dimensões dos dados
      int nAmostras = entradas.length;
      int nSaidas = this.saida.obterQuantidadeNeuronios();

      double[][] resultados = new double[nAmostras][nSaidas];
      double[] entradaRede = new double[entradas[0].length];
      double[] saidaRede = new double[nSaidas];

      for(int i = 0; i < nAmostras; i++){//iterar pelos dados de entrada
         System.arraycopy(entradas[i], 0, entradaRede, 0, entradas[i].length);
         
         this.calcularSaida(entradaRede);
         saidaRede = this.obterSaidas();

         System.arraycopy(saidaRede, 0, resultados[i], 0, saidaRede.length);
      }

      return resultados;
   }


   /**
    * <p>
    *    Treina a rede de acordo com as configurações predefinidas.
    * </p>
    * Certifique-se de configurar adequadamente o modelo para obter os 
    * melhores resultados.
    * @param entradas dados de entrada do treino (features).
    * @param saidas dados de saída correspondente a entrada (class).
    * @param epochs quantidade de épocas.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se houver alguma inconsistência dos dados de entrada e saída para a operação.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    */
   public void treinar(double[][] entradas, double[][] saidas, int epochs){
      this.modeloCompilado();
      consistenciaDados(entradas, saidas);

      if(epochs < 1) throw new IllegalArgumentException("O valor de epochs deve ser maior que zero.");

      //enviar clones pra não embaralhar os dados originais
      if(otimizadorAtual.getClass().equals(rna.otimizadores.GradientDescent.class)){
         treino.treino(this, this.otimizadorAtual, entradas.clone(), saidas.clone(), epochs, false);
      
      }else{
         treino.treino(this, this.otimizadorAtual, entradas.clone(), saidas.clone(), epochs, true);
      }
   }


   /**
    * <p>
    *    Treina a rede de acordo com as configurações predefinidas.
    * </p>
    * <p>
    *    O modo de treinamento em lote ainda ta em teste e costuma demorar pra convergir
    *    se forem usados os mesmo parâmetros do treino convencional.
    * </p>
    * Certifique-se de configurar adequadamente o modelo para obter os 
    * melhores resultados.
    * @param entradas dados de entrada do treino (features).
    * @param saidas dados de saída correspondente a entrada (class).
    * @param epochs quantidade de épocas.
    * @param tamanhoLote tamanho que o lote vai assumir durante o treino.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se houver alguma inconsistência dos dados de entrada e saída para a operação.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    */
   public void treinar(double[][] entradas, double[][] saidas, int epochs, int tamanhoLote){
      this.modeloCompilado();
      consistenciaDados(entradas, saidas);

      if(epochs < 1){
         throw new IllegalArgumentException("O valor de epochs não pode ser menor que um");
      }
      if(tamanhoLote <= 0 || tamanhoLote > entradas.length){
         throw new IllegalArgumentException("O valor de tamanho do lote é inválido.");
      }

      //enviar clones pra não embaralhar os dados originais
      if(otimizadorAtual.getClass().equals(rna.otimizadores.GradientDescent.class)){
         treino.treino(this, this.otimizadorAtual, entradas.clone(), saidas.clone(), epochs, false, tamanhoLote);
      
      }else{
         treino.treino(this, this.otimizadorAtual, entradas.clone(), saidas.clone(), epochs, true, tamanhoLote);
      }
   }
   

   /**
    * Método alternativo no treino da rede neural usando diferenciação finita (finite difference), que calcula a "derivada" da função de custo levando
    * a rede ao mínimo local dela. É importante encontrar um bom balanço entre a taxa de aprendizagem da rede e o valor de perturbação usado.
    * <p>
    *    Vale ressaltar que esse método é mais lento e menos eficiente que o backpropagation, em arquiteturas de rede maiores e que tenha uma grande 
    *    volume de dados de treino ou para problemas mais complexos ele pode demorar muito para convergir ou simplemente não funcionar como esperado.
    * </p>
    * <p>
    *    Ainda sim pode ser uma abordagem válida.
    * </p>
    * @param entradas matriz com os dados de entrada 
    * @param saidas matriz com os dados de saída
    * @param eps valor de perturbação
    * @param epochs número de épocas do treinamento
    * @param custoMinimo valor de custo desejável, o treino será finalizado caso o valor de custo mínimo seja atingido. Caso o custo mínimo seja zero, o treino
    * irá continuar até o final das épocas fornecidas
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se houver alguma inconsistência dos dados de entrada e saída para a operação.
    * @throws IllegalArgumentException se o valor de perturbação for igual a zero.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    * @throws IllegalArgumentException se o valor de custo mínimo for menor que zero.
    */
   public void diferencaFinita(double[][] entradas, double[][] saidas, double eps, int epochs, double custoMinimo){
      this.modeloCompilado();
      consistenciaDados(entradas, saidas);

      if(eps == 0){
         throw new IllegalArgumentException("O valor de perturbação não pode ser igual a zero.");
      }
      if(epochs < 1){
         throw new IllegalArgumentException("O valor de epochs não pode ser menor que um.");
      }
      if(custoMinimo < 0){
         throw new IllegalArgumentException("O valor de custo mínimo não pode ser negativo.");
      }

      RedeNeural redeG = this.clone();//copia da rede para guardar os valores de gradiente
      
      ArrayList<Camada> camadasRede = new ArrayList<Camada>();//copia da rede para camadas
      ArrayList<Camada> camadasGradiente = new ArrayList<Camada>();//copia da rede gradiente para camadas
      
      //colocando a rede de forma sequencial
      camadasRede.add(this.entrada);
      for(Camada camada : this.ocultas) camadasRede.add(camada);
      camadasRede.add(this.saida);
      
      //colocando a rede gradiente de forma sequencial
      camadasGradiente.add(redeG.entrada);
      for(Camada camada : redeG.ocultas) camadasGradiente.add(camada);
      camadasGradiente.add(redeG.saida);

      for(int epocas = 0; epocas < epochs; epocas++){
         double custo = avaliador.erroMedioQuadrado(entradas, saidas);
         if(custo < custoMinimo) break;

         double valorAnterior = 0;

         for(int i = 0; i < camadasRede.size(); i++){//percorrer camadas da rede
            for(int j = 0; j < camadasRede.get(i).neuronios.length; j++){//percorrer neuronios da camada
               for(int k = 0; k < camadasRede.get(i).neuronios[j].pesos.length; k++){//percorrer pesos do neuronio
                  valorAnterior = camadasRede.get(i).neuronios[j].pesos[k];
                  camadasRede.get(i).neuronios[j].pesos[k] += eps;
                  camadasGradiente.get(i).neuronios[j].pesos[k] = ((avaliador.erroMedioQuadrado(entradas, saidas) - custo)/eps);//derivada da função de custo
                  camadasRede.get(i).neuronios[j].pesos[k] = valorAnterior;
               }
            }
         }

         //atualizar pesos
         for(int i = 0; i < camadasRede.size(); i++){
            for(int j = 0; j < camadasRede.get(i).neuronios.length; j++){
               for(int k = 0; k < camadasRede.get(i).neuronios[j].pesos.length; k++){
                  camadasRede.get(i).neuronios[j].pesos[k] -= TAXA_APRENDIZAGEM * camadasGradiente.get(i).neuronios[j].pesos[k];
               }
            }
         }
      }

   }


   /**
    * Informa o valor do hiperparâmetro de traxa de aprendizagem da Rede Neural.
    * @return valor de taxa de aprendizagem da rede.
    */
   public double obterTaxaAprendizagem(){
      return this.TAXA_APRENDIZAGEM;
   }


   /**
    * Informa o valor do hiperparâmetro de traxa de momentum da Rede Neural.
    * @return valor de taxa de momentum da rede.
    */
   public double obterTaxaMomentum(){
      return this.TAXA_MOMENTUM;
   }


   /**
    * Informa qual tipo de otmizador está sendo usado para o treio da Rede Neural.
    * @return otimizador atual da rede.
    */
   public Otimizador obterOtimizador(){
      return this.otimizadorAtual;
   }


   /**
    * Retorna a camada de entrada da rede.
    * @return camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public Camada obterCamadaEntrada(){
      this.modeloCompilado();
      return this.entrada;
   }


   /**
    * @return quantiade de camadas ocultas da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int obterQuantidadeOcultas(){
      this.modeloCompilado();
      return this.ocultas.length;
   }


   /**
    * Retorna a camada oculta correspondente ao índice fornecido.
    * @param indice índice da busca.
    * @return camada oculta baseada na busca.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o índice estiver fora do alcance do tamanho das camadas ocultas.
    */
   public Camada obterCamadaOculta(int indice){
      this.modeloCompilado();
      if((indice < 0) || (indice > this.ocultas.length-1)){
         throw new IllegalArgumentException("O índice fornecido está fora do alcance das camadas disponíveis");
      }
   
      return this.ocultas[indice];
   }


   /**
    * Retorna a camada de saída da rede.
    * @return camada de saída.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public Camada obterCamadaSaida(){
      this.modeloCompilado();
      return this.saida;
   }


   /**
    * Copia os dados da saída de cada neurônio da camada de saída da rede neural para um vetor.
    * A ordem de cópia é crescente, do primeiro neurônio da saída ao último.
    * @return vetor com os dados das saídas da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public double[] obterSaidas(){
      this.modeloCompilado();

      double saida[] = new double[this.saida.neuronios.length];
      for(int i = 0; i < this.saida.neuronios.length; i++){
         saida[i] = this.saida.neuronios[i].saida;
      }

      return saida;
   }


   /**
    * Cria um array que representa a estrutura da Rede Neural. Nele cada elemento indica uma camada 
    * da rede e cada valor contido nesse elementos indica a quantidade de neurônios daquela camada
    * correspondente.
    * <p>
    *    Os valores podem sofrer alteração caso a rede possua o bias adicionado na hora da compilação, 
    *    incluindo um neurônio a mais em cada elementos do array.
    * </p>
    * @return array com a arquitetura da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int[] obterArquitetura(){
      this.modeloCompilado();
      return this.arquitetura;
   }


   /**
    * @return retorna o nome específico da rede.
    */
   public String obterNome(){
      return this.nome;
   }


   /**
    * Disponibiliza o histórico da função de custo da rede neural durante cada época
    * de treinamento.
    * @return lista contendo o histórico de custos durante o treinamento da rede.
    * @throws IllegalArgumentException se não foi habilitado previamente o cálculo do histórico de custos.
    */
   public ArrayList<Double> obterHistoricoCusto(){
      if(!this.treino.calcularHistoricoCusto){
         throw new IllegalArgumentException("Deve ser habilitado o cálculo do histórico de custos para obter os resultados.");
      }
      return this.treino.obterHistoricoCusto();
   }


   /**
    * Exibe as informações importantes da rede neural como:
    * <ul>
    *    <li>Otimizador atual.</li>
    *    <li>Valor da taxa de aprendizagem.</li>
    *    <li>Valor da taxa de momentum.</li>
    *    <li>Contém bias como neurônio adicional.</li>
    *    <li>Função de ativação de todas as camadas ocultas.</li>
    *    <li>Função de ativação da camada de saída.</li>
    *    <li>Arquitetura da rede.</li>
    * </ul>
    * @return buffer formatado contendo as informações.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public String obterInformacoes(){
      this.modeloCompilado();

      String buffer = "";
      String espacamento = "    ";
      System.out.println("\nInformações " + nome + " = [");

      //otimizador
      buffer += espacamento + "Otimizador: " + this.otimizadorAtual.getClass().getSimpleName();
      if(nesterov) buffer += " (Acelerador de Nesterov)";
      buffer += "\n";

      //hiperparâmetros
      buffer += espacamento + "Taxa de aprendizgem: " + TAXA_APRENDIZAGEM + "\n";
      buffer += espacamento + "Taxa de momentum: " + TAXA_MOMENTUM + "\n";

      //bias
      if(this.BIAS == 1) buffer += espacamento + "Bias = " + "true\n\n";
      else buffer += espacamento + "Bias: " + "false\n\n";

      for(int i = 0; i < this.ocultas.length; i++){
         buffer += espacamento + "Ativação oculta " + i + " = " + this.ocultas[i].obterAtivacao() + "\n";
      }
      buffer += espacamento + "Ativação saída = " + this.saida.obterAtivacao() + "\n";

      //arquitetura
      buffer += "\n" + espacamento + "arquitetura = {" + this.arquitetura[0];
      for(int i = 0; i < this.ocultas.length; i++) buffer += ", " + this.arquitetura[i+1];
      buffer += ", " + this.arquitetura[this.arquitetura.length-1] + "}";

      buffer += "\n]\n";

      return buffer;
   }

   
   /**
    * Clona a instância da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @return Clone da rede
    */
   @Override
   public RedeNeural clone(){
      this.modeloCompilado();

      try{
         RedeNeural clone = (RedeNeural) super.clone();

         //dados importantes
         clone.TAXA_APRENDIZAGEM = this.TAXA_APRENDIZAGEM;
         clone.TAXA_MOMENTUM = this.TAXA_MOMENTUM;
         clone.BIAS = this.BIAS;
         clone.arquitetura = this.arquitetura;

         //entrada
         clone.entrada = cloneCamada(this.entrada);

         //ocultas
         int quantidadeOcultas = this.arquitetura.length-2;
         clone.ocultas = new Camada[quantidadeOcultas];
         for(int i = 0; i < quantidadeOcultas; i++){
            clone.ocultas[i] = cloneCamada(this.ocultas[i]);
         }

         //saída
         clone.saida = cloneCamada(this.saida);

         return clone;
      }catch(CloneNotSupportedException e){
         throw new RuntimeException(e);
      }
   }


   /**
    * Clona uma instância de camada da rede neural.
    * @param camada camada original
    * @return clone da camada fornecida.
    */
   private Camada cloneCamada(Camada camada){
      Camada clone = new Camada(camada.temBias);
      clone.neuronios = new Neuronio[camada.neuronios.length];
      clone.ativacao = camada.ativacao;
      clone.temBias = camada.temBias;

      for (int i = 0; i < camada.neuronios.length; i++) {
         clone.neuronios[i] = cloneNeuronio(camada.neuronios[i]);
      }

      return clone;
   }


   /**
    * Clona uma instância de neurônio da rede neural.
    * @param neuronio neurônio original.
    * @return clone do neurônio fornecido.
    */
   private Neuronio cloneNeuronio(Neuronio neuronio){
      Neuronio clone = new Neuronio(neuronio.pesos.length, this.alcancePeso, this.inicializadorPeso);

      //método nativo mais eficiente na cópia de vetores
      System.arraycopy(neuronio.entradas, 0, clone.entradas, 0, clone.entradas.length);
      System.arraycopy(neuronio.pesos, 0, clone.pesos, 0, clone.pesos.length);
      System.arraycopy(neuronio.momentum, 0, clone.momentum, 0, clone.momentum.length);
      System.arraycopy(neuronio.acumuladorGradiente, 0, clone.acumuladorGradiente, 0, clone.acumuladorGradiente.length);
      System.arraycopy(neuronio.acumuladorSegundaOrdem, 0, clone.acumuladorSegundaOrdem, 0, clone.acumuladorSegundaOrdem.length);
      System.arraycopy(neuronio.gradiente, 0, clone.gradiente, 0, clone.gradiente.length);
      System.arraycopy(neuronio.gradienteAcumulado, 0, clone.gradienteAcumulado, 0, clone.gradienteAcumulado.length); 

      return clone;
   }


   /**
    * Salva a classe da rede em um arquivo especificado, o caminho não leva em consideração
    * o formato, de preferência deve ser .dat, caso seja especificado apenas o nome, o arquivo
    * será salvo no mesmo diretório que o arquivo principal.
    * @param caminho caminho de destino do arquivo que será salvo.
    */
   public void salvarArquivoRede(String caminho){
      try{
         FileOutputStream arquivo = new FileOutputStream(caminho);
         ObjectOutputStream objeto = new ObjectOutputStream(arquivo);

         objeto.writeObject(this);
         objeto.close();
         arquivo.close();

      }catch(Exception e){
         e.printStackTrace();
      }
   }


   /**
    * Lê um arquivo de rede neural no caminho especificado, o caminho não leva em consideração
    * o formato, logo precisa ser especificado.
    * @param caminho caminho do arquivo de rede salvo
    * @return Rede lida pelo arquivo.
    */
   public static RedeNeural lerArquivoRede(String caminho){
      RedeNeural rede = null;

      try{
         FileInputStream arquivo = new FileInputStream(caminho);
         ObjectInputStream objeto = new ObjectInputStream(arquivo);

         rede = (RedeNeural) objeto.readObject();
         objeto.close();
         arquivo.close();

      }catch(Exception e){
         e.printStackTrace();
      }

      return rede;
   }


   @Override
   public String toString(){
      this.modeloCompilado();

      String buffer = "";
      String espacamento = "   ";
      String espacamentoDuplo = espacamento + espacamento;
      String espacamentoTriplo = espacamento + espacamento + espacamento;
      
      buffer += "\nArquitetura " + nome + " = [\n";

      //ocultas
      for(int i = 0; i < this.ocultas.length; i++){

         buffer += espacamento + "Camada oculta " + i + " = [\n";
         for(int j = 0; j < this.ocultas[i].neuronios.length-BIAS; j++){
            
            buffer += espacamentoDuplo + "n" + j + " = [\n";
            for(int k = 0; k < this.ocultas[i].neuronios[j].pesos.length; k++){
               if(k == this.ocultas[i].neuronios[j].pesos.length-1 && (this.BIAS == 1)){
                  buffer += espacamentoTriplo + "pb" + " = " + this.ocultas[i].neuronios[j].pesos[k] + "\n";
               
               }else{
                  buffer += espacamentoTriplo + "p" + k + " = " + this.ocultas[i].neuronios[j].pesos[k] + "\n";
               }
            }
            buffer += espacamentoDuplo + "]\n";

         }
         buffer += espacamento + "]\n\n";

      }

      //saida
      buffer += espacamento + "Camada saída = [\n";
      for(int i = 0; i < this.saida.neuronios.length; i++){

         buffer += espacamentoDuplo + "n" + i + " = [\n";
         for(int j = 0; j < this.saida.neuronios[i].pesos.length; j++){
            if(j == this.saida.neuronios[i].pesos.length-1 && (this.BIAS == 1)){
               buffer += espacamentoTriplo + "pb" + " = " + this.saida.neuronios[i].pesos[j] + "\n";
            
            }else buffer += espacamentoTriplo + "p" + j + " = " + this.saida.neuronios[i].pesos[j] + "\n";
         }
         buffer += espacamentoDuplo + "]\n";

      }
      buffer += espacamento + "]\n";

      buffer += "]\n";

      return buffer;
   }
}
