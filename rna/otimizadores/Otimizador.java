package rna.otimizadores;

import java.io.Serializable;

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
public abstract class Otimizador implements Serializable{

   /**
    * Treina a rede neural de acordo com o otimizador configurado, atualizando 
    * seus pesos.
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
