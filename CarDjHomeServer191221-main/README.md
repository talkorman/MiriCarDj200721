<h1>MiRi CarDj Server side</h1>
<br>
	<p>
    The app server is connected to its own Data Base which was filled with English and Hebrew songs with its automatic searching web scrapping
    algorithm. That process was running for a week and filled up the data base with most of the songs from the 70s, 80, and 90s, mainly with
    http results of vinyl playing videos from YouTube for high sound quality.
</p>
<p>Currently the algorithm on the server perform the search on its own Data Base and if the results are less than 10, it performs auto scrapping search and add the results to the Data Base as well as sending them to the device. on this case, the return data to the mobile app can be delayed by up to 30 second. If the results at the Data base are more than 10 songs, the return result to the app is in about 5 to 10 seconds.
</p>
<h4>Technical specs:</h4>
	<p>Data base - Mongooze. Framework: Node + Express.</p>
<p>The server running on a remote Windows computer and it communicates with the app through socket on Heroku app.</p>
    
    
