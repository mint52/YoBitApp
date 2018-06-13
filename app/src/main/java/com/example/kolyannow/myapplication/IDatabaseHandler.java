package com.example.kolyannow.myapplication;

import java.util.List;

public interface IDatabaseHandler {
    public void addAccount(Account account);
    public Account getInfoAccount(int id);
    public List<Account> getAllInfoAccounts();
    public int getInfoAccountCount();
    public int updateInfoAccount(Account contact);
    public void deleteInfoAccount(Account contact);
    public void deleteAll();
}
