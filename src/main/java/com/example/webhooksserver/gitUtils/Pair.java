package com.example.webhooksserver.gitUtils;

public class Pair {

    private final String first;
    private final Integer second;

    public Pair(String first, Integer second) {
        assert first != null;
        assert second != null;

        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public Integer getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair))
            return false;
        Pair pairo = (Pair) o;
        return this.first.equals(pairo.getFirst()) && this.second.equals(pairo.getSecond());
    }

    @Override
    public String toString() {
        return this.first.toString() + "," + this.second.toString();
    }

}