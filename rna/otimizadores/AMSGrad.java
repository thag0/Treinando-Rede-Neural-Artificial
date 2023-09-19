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
	 * Contador de iterações.
	 */
	private long interacoes = 0;

	/**
	 * Variável para armazenar o valor máximo da segunda ordem acumulada.
	 */
	private double maxSegundaOrdem = 0;

	/**
	 * Inicializa uma nova instância de otimizador <strong> AMSGrad </strong> usando os valores de
	 * hiperparâmetros fornecidos.
    * @param tA valor de taxa de aprendizagem.
	 * @param epsilon usado para evitar a divisão por zero.
	 * @param beta1 decaimento do momento.
	 * @param beta2 decaimento do momento de segunda ordem.
	 */
	public AMSGrad(double tA, double epsilon, double beta1, double beta2){
		this.taxaAprendizagem = tA;
		this.epsilon = epsilon;
		this.beta1 = beta1;
		this.beta2 = beta2;
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
    *    {@code epsilon = 1e-7}
    * </p>
    * <p>
    *    {@code beta1 = 0.9}
    * </p>
    * <p>
    *    {@code beta2 = 0.999}
    * </p>
	 */
	public AMSGrad(){
		this(0.01, 1e-7, 0.9, 0.999);
	}

   /**
    * Aplica o algoritmo do AMSGrad para cada peso da rede neural.
    * <p>
    *    O AMSGrad funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= (tA * mc) / ((√ m2c) + eps)
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
    *    {@code m2c} - valor de momentum de segunda ordem corrigido.
    * </p>
    * Os valores de momentum corrigido (mc) e momentum de segunda ordem
    * corrigido (m2c) se dão por:
    * <pre>
    *    mc = m[i] / (1 - beta1ⁱ)
    * </pre>
    * <pre>
    *    m2c = max(max2ordem, m2[i]) / (1 - beta2ⁱ)
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
    *    {@code m2} - valor de momentum de segunda ordem correspondete a conexão 
    *    do peso que está sendo atualizado.
    * </p>
    * <p>
    *    {@code i} - contador de interações (épocas passadas em que o otimizador 
	 *		foi usado).
    * </p>
    */
	@Override
	public void atualizar(Camada[] redec){
		double mc, m2c, divB1, divB2, g;
		Neuronio neuronio;
		
		//percorrer rede, com exceção da camada de entrada
		interacoes++;
		for(int i = 1; i < redec.length; i++){

			Camada camada = redec[i];
			int nNeuronios = camada.quantidadeNeuroniosSemBias();
			for(int j = 0; j < nNeuronios; j++){

				divB1 = (1 - Math.pow(beta1, interacoes));
				divB2 = (1 - Math.pow(beta2, interacoes));

				neuronio = camada.neuronio(j);
				for(int k = 0; k < neuronio.pesos.length; k++){
					g = neuronio.gradiente[k];
					
					neuronio.momentum[k] =  (beta1 * neuronio.momentum[k])  + ((1 - beta1) * g);
					neuronio.velocidade[k] = (beta2 * neuronio.velocidade[k]) + ((1 - beta2) * g * g);

					maxSegundaOrdem = Math.max(maxSegundaOrdem, neuronio.velocidade[k]);

					mc = neuronio.momentum[k] / divB1;
					m2c = maxSegundaOrdem / divB2;
					neuronio.pesos[k] -= (taxaAprendizagem * mc) / (Math.sqrt(m2c) + epsilon);
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
