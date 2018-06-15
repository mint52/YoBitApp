package com.example.kolyannow.myapplication.database;

import com.example.kolyannow.myapplication.Pair;

import java.util.List;

public interface IDatabaseHandler {
    public void addPair(Pair pair);
    public Pair getPair(int id);
    public List<Pair> getAllPairs();
    public int getPairCount();
    public int updatePair(Pair pair);
    public void deletePair(Pair pair);
    public void deleteAll();
}
