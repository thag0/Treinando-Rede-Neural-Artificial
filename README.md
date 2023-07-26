# Treinando Rede Neural Artificial
Tentando implementar um algortimo de treino e ferramentas para masuear os dados.

Aos poucos criando um pequeno ambiente de trabalho em volta disso

# Resultados

Até o momento já consegui usar a rede em testes menores, como pegar alguns conjuntos de dados para testar o desempenho dela e tive bons resultados com precições
entre 80% e 95%

Consegui testar a rede em um cenário que ela deve ampliar imagens e também consegui bons resultados como é possível ver a seguir:

![imagemApliadaPelaRede](https://github.com/thag0/Treinando-Rede-Neural-Artificial/assets/91092364/8610fd77-b739-4a5d-a976-085eddaf8a15)

*imagem original comparada com imagem criada pela rede neural.*

A rede consegue lidar com dados gerados por uma imagem pequena, desde que eles estejam normalizados para isso. Normalizei eles fazendo a rede trabalhar em intervalos entre
0 e 1 para x e y, onde quanto mais próximo de 1 o valor for, mais próximo ele vai estar do limite de altura e largura da imagem, então a rede trabalha com "porcentagens de coordenadas".
