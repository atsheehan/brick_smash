package com.bazbatlabs.bricksmash.models;

public final class Menu {

    private boolean startGame;

    public Menu() {
        this.startGame = false;
    }

    public void update() {}
    public void nextEntry() {}
    public void previousEntry() {}

    public void select() {
        startGame = true;
    }

    public boolean isReadyToStartGame() { return startGame; }
}
