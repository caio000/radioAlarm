package br.edu.ifspcaraguatatuba.control;

import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Tocador {
	
	//-------------- Status do Player -----------------------
	private final int NOTSTARTED = 0;
	private final int PLAYING    = 1;
	private final int PAUSED     = 2;
	private final int FINISHED   = 3;
	
	private int playerStatus = NOTSTARTED;
	private final Object playerLock = new Object();
	
	private Player player;
	
	
	
	
	
	
	
	
	
	public Tocador(final InputStream inputStream) throws JavaLayerException {
		this.player = new Player(inputStream);
	}
	
	
	
	
	
	public void play() {
		synchronized (playerLock) {
			
			switch (playerStatus) {
			case NOTSTARTED:
				final Runnable r = new Runnable() {
					@Override
					public void run() {
						// TODO Iniciar a musica
						playMusic();
					}
				};
				
				final Thread t = new Thread(r);
				t.setPriority(Thread.MAX_PRIORITY);
				playerStatus = PLAYING;
				t.start();
				
				break;
				
			case PAUSED:
				resume();
				break;

			default:
				break;
			}
		}
	}
	
	public boolean resume() {
        synchronized (playerLock) {
            if (playerStatus == PAUSED) {
                playerStatus = PLAYING;
                playerLock.notifyAll();
            }
            return playerStatus == PLAYING;
        }
    }
	
	private void playMusic () {
		while (playerStatus != FINISHED) {
            try {
                if (!player.play(1)) {
                    break;
                }
            } catch (final JavaLayerException e) {
                break;
            }
            // check if paused or terminated
            synchronized (playerLock) {
                while (playerStatus == PAUSED) {
                    try {
                        playerLock.wait();
                    } catch (final InterruptedException e) {
                        // terminate player
                        break;
                    }
                }
            }
        }
        close();
	}
	
	public boolean pause() {
        synchronized (playerLock) {
            if (playerStatus == PLAYING) {
                playerStatus = PAUSED;
            }
            return playerStatus == PAUSED;
        }
    }
	
	public void close() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
        }
        try {
            player.close();
        } catch (final Exception e) {
            // ignore, we are terminating anyway
        }
    }

}
