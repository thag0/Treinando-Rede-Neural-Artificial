package rna.otimizadores;

import rna.estrutura.Camada;

/**
 * Classe genérica para implementações de otimizadores do treino da rede neural.
 * <p>
 *      O otimizador já deve levar em consideração que os gradientes foram calculados previamente.
 * </p>
 * <p>
 *      Novos otimizadores devem implementar o método {@code atualizar()}, que atualizará os pesos
 *      da rede neural de acordo com seu próprio algoritmo.
 * </p>
 */
public abstract class Otimizador{

    /**
     * Inicializa os parâmetros do otimizador para que possa ser usado.
     * <p>
     *      Essa função deve ser chamada antes de usar o método de atualização de pesos
     *      do otimizador.
     * </p>
     * @param parametros quantidade de pesos da rede neural.
     */
    public void inicializar(int parametros){
        throw new java.lang.UnsupportedOperationException("Inicialização do otimizador não implementada.");
    }

    /**
     * Atualiza os pesos da Rede Neural de acordo com o otimizador configurado.
     * <p>
     *      A atualização de pesos é feita uma única vez em todos os parâmetros da rede.
     * </p>
     * @param redec Rede Neural em formato de lista de camadas.
     */
    public void atualizar(Camada[] redec){
        throw new java.lang.UnsupportedOperationException("Método de atualização do otimizador não foi implementado.");
    }

   /**
    * Mostra as opções de configurações do otimizador.
    * @return buffer formatado.
    */
    public String info(){
        throw new java.lang.UnsupportedOperationException("Método de informações do otimizador não foi implementado.");
    }
}
