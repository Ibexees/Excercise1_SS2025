package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.dataLayer.database.WatchlistRepositoryEvent;

import java.util.ArrayList;
import java.util.List;

public interface Observer
{

   /* void movieSavedToWatchlist();
    void movieRemovedFromWatchlist();*/
    void onRepositoryEvent(WatchlistRepositoryEvent event);
}
