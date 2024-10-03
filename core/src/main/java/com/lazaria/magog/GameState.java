package com.lazaria.magog;


public class GameState {
    private int lives;
    private boolean isGameOver;

    public GameState(int initialLives) {
        this.lives = initialLives;
        this.isGameOver = false;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
        if (lives == 0) {
            isGameOver = true;
        }
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameWon() {
        return false;
    }

    public void reset() {
        lives = 5;
        isGameOver = false;
    }
}

