package Jogador;
import java.util.Random;

public class JogadorAzarado extends Jogador{
    Random r = new Random();
    public JogadorAzarado(String nome, String cor){
        super(nome, cor, "azarado");
    }
    public JogadorAzarado(Jogador jogadorAntigo) {
        super(jogadorAntigo.getNome(), jogadorAntigo.getCor(), "azarado");
        this.posicao = jogadorAntigo.getPosicao();
        this.jogadas = jogadorAntigo.getJogadas();
    }

    @Override
    public int jogarDados() {
        int d1, d2, soma;
        do {
            d1 = r.nextInt(6) + 1;
            d2 = r.nextInt(6) + 1;
            soma = d1 + d2;
        }while (soma > 6);
        return soma;
    }
}
