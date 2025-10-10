package Jogador;

// Classe base do jogador (vou deixar abstrata porque cada tipo joga os dados de um jeito)
public abstract class Jogador {

    // Dados Jogador.Jogador
    protected String nome;
    protected String cor;
    protected String sorte; // "sortudo", "azarado" ou "normal"
    protected int ultimoDado1 = 0;
    protected int ultimoDado2 = 0;

    // Dados Jogo
    protected int posicao = 0;         // começa na casa 0
    protected int jogadas = 0;         // quantas vezes já jogou
    protected boolean pularRodada = false; // se true, perde a próxima rodada

    public Jogador(String nome, String cor, String sorte) {
        this.nome = nome;
        this.cor = cor;
        this.sorte = sorte;
    }
    // Como os dados dependem da sorte, o abstrato serve para implementar de acordo com a sorte
    public abstract int jogarDados();

    // Move o Player
    public void mover(int casas) {
        posicao = posicao + casas;
        if (posicao > 40) {
            posicao = 40;
        }
    }

    // Só pra ver onde o jogador está
    public void mostrarPosicao() {
        System.out.println(cor + " - " + nome + " está na casa " + posicao);
    }

    // Contar que fez uma jogada
    public void contarJogada() {
        jogadas = jogadas + 1;
    }

    // Getters básicos (os que devo usar mais)
    public String getNome() { return nome; }
    public String getCor() { return cor; }
    public String getSorte() { return sorte; }
    public int getPosicao() { return posicao; }
    public int getJogadas() { return jogadas; }

    // Coisas da regra de "pular rodada"
    public boolean devePularRodada() { return pularRodada; }
    public void setPularRodada(boolean valor) { pularRodada = valor; }
    public void setPosicao(int novaPosicao) { posicao = novaPosicao; }
    public void setUltimosDados(int dado1, int dado2){
        this.ultimoDado1 = dado1;
        this.ultimoDado2 = dado2;
    }
    public boolean tirouDuplo() {
        return ultimoDado1 == ultimoDado2 && ultimoDado1 != 0;
    }
}
