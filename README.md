# TMDB Movie List App

TMDB Movies is a native android app.


```bash
1) Get movies list from TMDB Api
2) Store infomation offline in SQLite DB for offline threading.
3) Use Retrofit2 as a REST Client.
4) Use Glide for image rendering

```
## Compatibility

```bash
1) Compatible from Android 6 to Android 11.
2) Compatible with phone and tablet.
```

## Design pattern

```bash
1) Singleton pattern used for memory efficiency.
```

## Architecture

```bash
1) Uses Model-View-Controller architecture.
```



## MainActivities.

```bash
1) MainActivity - Fetching, storing displaying the data.
2) MovieDetailsActivity - display movie details. 
3) SplashActivity - Splash Activity.
```


## Other.

```bash
1) MyRecyclerViewAdapter - RecyclerView Adapter.
2) Movies - Custom movie object. 
3) APIClient - Retrofit builder.
4) MovieApi - Interface
5) DatabaseHelper - Database.
```


## Permissions.

```bash
1) ACCESS_NETWORK_STATE - Check Internet connection.
2) INTERNET - Uses Internet. 
3) WRITE_EXTERNAL_STORAGE - To store data offline (Images).


```


