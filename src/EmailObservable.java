interface EmailObservable {
    public void addObserver(EmailStatObserver o);
    public void removeObserver(EmailStatObserver o);
    public void notifyAllObservers();
}