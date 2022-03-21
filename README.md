<h1>MiRi CarDj</h1> 
<br>
<a href="https://github.com/talkorman/CarDjHomeServer191221/tree/main">For server side repository please click here</a><br><br>
<a href="https://www.youtube.com/watch?v=AuF2vx0ksyg">
	<img width="240"src="https://user-images.githubusercontent.com/3162057/159336234-23655ae4-9e78-44ee-81c3-0be317b6a24c.png"></a>
<br>
<p>
MiRi CarDj app is a robotic human voice Disc Jockey which speak with the driver and serves him 
according to his requests on searching, choosing, playing and building play lists of YouTube songs.
The driver tells which song he wants to find, the app finding him results which are presented visually and spoken by the app,
The driver chose if to test a result, try the next one, try another one from the list, just to play it or to add it to his saved play list.
The driver can choose to add or remove songs and of course to play his saved favorite songs one after another.
The UI is a single screen:
At the top is the Video player,
Below is the play list which the app built for the driver - shifting list of images, songs titles and number for the driver to choose to play.
Below that is the result list which the app shows after it fetching them from its data base - shifting list of images, songs titles and number 
for the driver to choose to try to see if it’s the result he wants or just to play it.
Below that at the bottom of the screen located the only one button which is a Microphone activator.
The device microphone is activated implicitly by the app when the device is not talking or not playing.
If the driver wants to tell something to the app while a song is playing, the driver need to press on the microphone button to let the app
explicitly switch to a listening mode. On listening mode, the app is listening continuously and response immediately after a recognized request 
was spoken by the driver.
</p>

<img width="400" alt="profile" src="https://user-images.githubusercontent.com/3162057/159328249-6ec3c590-7184-45b0-9702-ba9d1b7f6e17.png">

<h2>Working process:</h2>
  
  <h3>Search for song results: </h3>
	<p>
    The driver says "I want " + a song/artist name. Once the results received and presented on the screen, 
		the app is reading for the driver the names of the
    songs inside the list so the driver don't need to look on the screen.
  </p><br>
	
  <h3>Choosing a song from the results:</h3>
	<p>
    After the app tell to the driver the song details, it's waiting 10 seconds for the driver response.</p>
    <p>If the driver says "yes" the app will play the song.</p>
    <p>If the driver says "no" the app will tell the details of the next song.</p>
    <p>If the driver says "Result number" + song number, the app will play the song related to that number that is flickering on the song image.</p>
    <p>If the driver says "Chose again" the device will switch to listening mode and stop reading song details.</p>
    <p>If the driver doesn’t say anything for 10 seconds, the app will say "I repeat" and will read the current song result again.</p>
  </p><br>
  
  <h3>Adding a chosen song to the play list</h3>
  <p>While the app telling a certain result song details, just by saying "OK!" it will say "adding to play list" and the song will appear at
	the play list</p>
  <p>If the driver decide to add to song to the play list after testing the song by playing it, he just need to press the microphone button
	and say "OK!" and the app will say "adding to play list" and the song will join to the play list items</p><br>
  <h3>Playing songs from the play list:</h3>
	<p>
    <p>If the driver says "Go!", the app will play the songs of the playlist from the beginning one after another.</p>
    <p>If the driver says "Play number" + song number, the app will play the song with the flickering number on its image.</p>
    <p>If the driver says "Delete song" + song number, the app will delete the song associated with that number from the play list.</p>
    </p><br>
		<div>
		<img width="240" src="https://user-images.githubusercontent.com/3162057/159329347-ee2a6e8d-d090-44be-b6d8-f9029408dcb4.jpg">
	  <img width="240" src="https://user-images.githubusercontent.com/3162057/159330751-115cd493-8466-4c43-8f64-a6acd79842cf.jpg">
	  <img width="240" src="https://user-images.githubusercontent.com/3162057/159331225-bde5d057-c374-460b-a5e0-e3b04a5b0d2b.jpg">
	</div>
 <h3>Searching songs from other languages:</h3>
 <p>
         <p> If the driver says "Write!", the app will open the Gboard interface with the current chosen language.</p>
     <p>The driver need to press the microphone icon on the Gboard interface and say the song/artist name in its own language.</p>
     <p>Then the driver need to press the microphone button of the app and the app will perform the search.</p>
     <p>In this option, the app will only read for the driver the index numbers of the songs results.</p>
    </p><br>
  <h3>Shut down listening mode:</h3>
	<p>
    In some occasions when the app is on listening mode and the driver wants just to continue listening to a playing song, the only
    word that need to tell the app is "Release!" and the app will immediately stop listening and continue play the song.
    </p><br>
	<p>	
  <h3>Server side</h3> <a href="https://github.com/talkorman/CarDjHomeServer191221/tree/main">press here for server repository</a>     
	<p>
    The app server is connected to its own Data Base which was filled with English and Hebrew song with its automatic searching web scrapping
    algorithm. That process was running for a week and filled up the data base with most of the songs from the 70s, 80, and 90s, mainly with
    http results of vinyl playing videos from YouTube for high sound quality.
</p>
<p>Currently the algorithm on the server perform the search on its own Data Base and if the results are less than 10, it performs auto scrapping search and add the results to the Data Base as well as sending them to the device. on this case, the return data to the mobile app can be delayed by up to 30 second. If the results at the Data base are more than 10 songs, the return result to the app is in about 5 to 10 seconds.
</p>
<h4>Technical specs:</h4>
	<p>Data base - Mongooze. Framework: Node + Express.</p>
<p>The server running on a remote Windows computer and it communicates with the app through socket on Heroku app.</p>
