package org.example;

public class PongLogic {

    public static final int SERVER = 0;
    public static final int CLIENT = 1;
    public static final int RIGHT = 0;
    public static final int LEFT = 1;

    private static PongLogic instance;

    private final Repository repository;

    private PongLogic() {
        repository = new Repository(400,300, 250, 250, RIGHT, SERVER);
    }

    public static PongLogic getInstance() {
        if (instance == null) {
            instance = new PongLogic();
        }
        return instance;
    }

    public int getBallX() {
        return repository.getBallX();
    }

    public int getBallY() {
        return repository.getBallY();
    }

    public int getClientPlayerY() {
        return repository.getClientPlayerY();
    }

    public int getServerPlayerY() {
        return repository.getServerPlayerY();
    }

    public int getWhoAmI() {
        return repository.getWhoAmI();
    }

    public void setClientPlayerY(int y) {
        repository.setClientPlayerY(y);
    }

    public void setServerPlayerY(int y) {
        repository.setServerPlayerY(y);
    }

    public void moveBall() {
        if (repository.getDirection() == RIGHT)
            repository.setBallX(repository.getBallX() + 10);
        else
            repository.setBallX(repository.getBallX() - 10);
        if (repository.getBallX() >= 800 || repository.getBallX() <= 0)
            repository.setBallX(400);
        if (collision()) {
            repository.setDirection((repository.getDirection() == RIGHT) ? LEFT : RIGHT);
        }
    }

    private boolean collision() {
        if (repository.getBallX() == 20 &&
                repository.getBallY() >= repository.getServerPlayerY() &&
                repository.getBallY() <= repository.getServerPlayerY() + 50)
            return true;
        return repository.getBallX() == 780 &&
                repository.getBallY() >= repository.getClientPlayerY() &&
                repository.getBallY() <= repository.getClientPlayerY() + 50;
    }

    public void setWhoAmI(int client) {
        repository.setWhoAmI(client);
    }

    public Repository getPongData() {
        return repository;
    }

}