package at.ac.fhcampuswien.fhmdb.dataLayer.database;

import at.ac.fhcampuswien.fhmdb.ui.Observable;
import at.ac.fhcampuswien.fhmdb.ui.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractObservable implements Observable
{
    List<Observer> subscriber = new ArrayList<>();
    @Override
    public void addSubscriber(Observer observer)
    {
        subscriber.add(observer);
    }

    @Override
    public void removeSubscriber(Observer observer)
    {
        subscriber.remove(observer);
    }

    @Override
    public void notifyAddedToWatchlist()
    {

    }

    public List<Observer> getSubscriber()
    {
        return subscriber;
    }
}
