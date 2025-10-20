package Jogador;
import java.util.Random;

public class JogadorNormal extends Jogador {
    private Random r = new Random();
    public JogadorNormal(String nome, String cor) {
        super(nome, cor, "normal");
    }
    public JogadorNormal(Jogador jogadorAntigo) {
        super(jogadorAntigo.getNome(), jogadorAntigo.getCor(), "normal");
        this.posicao = jogadorAntigo.getPosicao();
        this.jogadas = jogadorAntigo.getJogadas();
    }

    @Override
    public int [] jogarDados() {
        int d1 = r.nextInt(6) + 1; // 1 a 6
        int d2 = r.nextInt(6) + 1; // 1 a 6
        int[] dados = new int [2];
        dados[0] = d1;
        dados[1] = d2;
        return dados;
    }
}