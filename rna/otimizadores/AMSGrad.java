package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação do algoritmo de otimização AMSGrad, que é uma variação do 
 * algoritmo Adam que resolve um problema de convergência em potencial do Adam.
 * <p>
 * 	Os hiperparâmetros do AMSGrad podem ser ajustados para controlar o 
 * 	comportamento do otimizador durante o treinamento.
 * </p>
 */
public class AMSGrad extends Otimizador{

   /**
    * Valor de taxa de aprendizagem do otimizador.
    */
	private double taxaAprendizagem;

	/**
	 * Usado para evitar divisão por zero.
	 */
	private double epsilon;

	/**
	 * Decaimento do momentum de primeira ordem.
	 */
	private double beta1;

	/**
	 * Decaimento do momentum de segunda ordem.
	 */
	private double beta2;

   /**
    * Coeficientes de momentum.
    */
	private double[] momentum;

	/**
	 * Coeficientes de momentum de segunda orgem.
	 */
	private double[] velocidade;

	/**
	 * Coeficientes de momentum de segunda orgem corrigidos.
	 */
	private double[] vCorrigido;

	/**
	 * Contador de iterações.
	 */
	private long interacoes;

	/**
	 * Inicializa uma nova instância de otimizador <strong> AMSGrad </strong> usando os valores de
	 * hiperparâmetros fornecidos.
    * @param tA valor de taxa de aprendizagem.
	 * @param epsilon usado para evitar a divisão por zero.
	 * @param beta1 decaimento do momento.
	 * @param beta2 decaimento do momento de segunda ordem.
	 */
	public AMSGrad(double tA, double beta1, double beta2, double epsilon){
		this.taxaAprendizagem = tA;
		this.beta1 = beta1;
		this.beta2 = beta2;
		this.epsilon = epsilon;
	}

	/**
	 * Inicializa uma nova instância de otimizador <strong> AMSGrad </strong>.
	 * <p>
	 * Os hiperparâmetros do AMSGrad serão inicializados com os valores padrão, que
	 * são:
    * <p>
    *    {@code taxaAprendizagem = 0.01}
    * </p>
    * <p>
    *    {@code beta1 = 0.9}
    * </p>
    * <p>
    *    {@code beta2 = 0.999}
    * </p>
    * <p>
    *    {@code epsilon = 1e-7}
    * </p>
	 */
	public AMSGrad(){
		this(0.01, 0.9, 0.999, 1e-7);
	}

	@Override
	public void inicializar(int parametros){
		this.momentum = new double[parametros];
		this.velocidade = new double[parametros];
		this.vCorrigido = new double[parametros];
		this.interacoes = 0;
	}

   /**
    * Aplica o algoritmo do AMSGrad para cada peso da rede neural.
    * <p>
    *    O AMSGrad funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= (tA * mc) / ((√ vc) + eps)
    * </pre>
    * Onde:
    * <p>
    *    {@code p} - peso que será atualizado.
    * </p>
    * <p>
    *    {@code tA} - valor de taxa de aprendizagem (learning rate).
    * </p>
    * <p>
    *    {@code mc} - valor de momentum corrigido.
    * </p>
    * <p>
    *    {@code vc} - valor de momentum de segunda ordem corrigido.
    * </p>
    * Os valores de momentum corrigido (mc) e momentum de segunda ordem
    * corrigido (vc) se dão por:
    * <pre>
    *    mc = m[i] / (1 - beta1ⁱ)
    * </pre>
    * <pre>
    *    vc = vC[i] / (1 - beta2ⁱ)
    * </pre>
    * Onde:
    * <p>
    *    {@code m} - valor de momentum correspondete a conexão do peso que está
    *     sendo atualizado.
    * </p>
	 *	<p>
	 *		{@code max2ordem} - valor máximo de segunda ordem calculado.
	 *	</p>
    * <p>
    *    {@code vC} - valor de momentum de segunda ordem corrigido correspondente a 
	 *		conexão do peso que está sendo atualizado.
    * </p>
    * <p>
    *    {@code i} - contador de interações do otimizador.
    * </p>
	 * O valor de momentum de segunda ordem corrigido (vC) é dado por:
	 * <pre>
	 * vC[i] = max(vC[i], v[i])
	 * </pre>
	 * Onde:
	 * <p>
    *    {@code v} - coeficiente de momentum de segunda ordem correspondente a
	 *		conexão do peso que está sendo atualizado.
    * </p>
    */
	@Override
	public void atualizar(Camada[] redec){
		interacoes++;
		
		double mChapeu, vChapeu, g;
		double forcaB1 = (1 - Math.pow(beta1, interacoes));
		double forcaB2 = (1 - Math.pow(beta2, interacoes));

		int id = 0;//indice de busca na lista de coeficientes
		for(Camada camada : redec){
			for(Neuronio neuronio : camada.neuronios()){
				for(int i = 0; i < neuronio.pesos.length; i++){
					g = neuronio.gradientes[i];
					
					momentum[id] =   (beta1 * momentum[id])   + ((1 - beta1) * g);
					velocidade[id] = (beta2 * velocidade[id]) + ((1 - beta2) * g * g);

					vCorrigido[id] = Math.max(vCorrigido[id], velocidade[id]);

					mChapeu = momentum[id] / forcaB1;
					vChapeu = vCorrigido[id] / forcaB2;

					neuronio.pesos[i] -= (taxaAprendizagem * mChapeu) / (Math.sqrt(vChapeu) + epsilon);

					id++;
				}			
			}
		}
  	}

	@Override
	public String info(){
		String buffer = "";

		String espacamento = "    ";
		buffer += espacamento + "TaxaAprendizagem: " + this.taxaAprendizagem + "\n";
		buffer += espacamento + "Beta1: " + this.beta1 + "\n";
		buffer += espacamento + "Beta2: " + this.beta2 + "\n";
		buffer += espacamento + "Epsilon: " + this.epsilon + "\n";

		return buffer;
	}

}
