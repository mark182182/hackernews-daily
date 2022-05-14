package hackernews.mailsender.service;


public abstract class Service implements Runnable {

  public abstract void init();

  @Override
  public abstract void run();

  public abstract void stop();
}
