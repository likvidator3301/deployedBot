package DataStructure;

public class Pair<T1, T2> {
    public T1 first;
    public T2 second;

    public Pair(T1 f, T2 s) {
        this.first = f;
        this.second = s;
    }

    @Override
    public String toString() {
        return first.toString() + " " + second.toString();
    }
}
