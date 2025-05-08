public class Venda {
    private Produto produto;
    private int quantidade;
    private double total;

    public Venda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.total = produto.getPreco() * quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getTotal() {
        return total;
    }
}
