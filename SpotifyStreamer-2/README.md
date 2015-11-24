# Spotify Streamer
Learning to use some of the new opensource libraries that can make Android application development fun and productive.
Libraries used in this example:
 * RxJava & RxAndroid
 * Dagger 2
 * Retrofit 
 
 
 
You will need api key from the spotify web site to access the api which is needed for querying.

You will need api key from the spotify web site to access the api. This api needs to be added in FragmentNetworkRequest for the URL. 

Your Api key in ReviewsApi.java

	@GET("movie/{id}/reviews?api_key=yourapikey")

Your Api key in SpotifyMoviesApi.java
	@GET("discover/movie?api_key=yourapikey")
	
Your Api key in SpotifyMoviesApi.java

	@GET("movie/{id}/videos?api_key=yourapikey")

