import java.util.Objects;

public class Player {
    private String name;
    private int counter;

    public Player(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void incrementCounter() {
        counter++;
    }

    public String getName() {
        return name;
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public String toString() {
        return "Игрок: " + name + "\n" + "Счетчик угаданных городов: " + counter + "\n";
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return counter == player.counter && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, counter);
    }
}
