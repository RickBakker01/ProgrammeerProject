# Van de kaart

## Description
This is an app that gives you a map with breweries nearby, or the breweries in a city you search for. 

### Screenshot:


## Technical design
#### High level overview
The app consists of multiple activities (screens), 4 async tasks, a request helper, a singleton and a splash screen.
When the app is opened from the app drawer, a splash screen is shown for 2.5 seconds. After that, the main activity is opened.
From there, the user can see breweries nearby, if there are any. In the toolbar, you can go to the login / user info page or 
you can go to the search activity. There you can type in a city and the app looks for breweries in that city. 

When the user is in the main activity and the user clicks on a marker on the map, the user can click on the marker title to go the info page about the selected
brewery. Here, a user sees some extra information about the brewery, including an image when it is available. When it s not, a standard image is shown. 
On the info page, a user can set the brewery as visited and leave a small rating. This is saved when the user is signed in. When the user gets back to this brewery,
the app shows the previously saved rating. 

When a user clicks on the account button, the app takes the user to the login screen or to the user info page, depending on whether the user is signed in.
If the user is signed in, his or her email is shown, plus the visited breweries. When a user clicks on a visited brewery, the user is taken to the info page and the
saved rating is shown. The user can delete visited breweries by click and hold a brewery in the visited brewery list. On the user info page, a user can also sign out.
If a user is not signed in, the account button will redirect the user to the login page, there a user can log in. If the user hasn't got an account yet, the user
can click on "Need an account?" and the user is taken to the registration page. When the user registers, the user is automatically signed in. 


### Some more detail
#### Markers and breweries
When the app is opened, the splash screen occurs. After 2.5 second, the main activity is opened. Then the app on first start will asks whether the user allows the app
to use location services. When the user agrees, the app will check if location services are turned on. Then the app wil search the users location. The location is
in coordinates and is the converted to a city with a geocoder. Then the app uses the city async task. This async task will get the brewery id's from your city. 
These id's are sent to the next async task, the coordinates async task. This gets the name, longitude and latitude from every id. After this, a broadcast manager is 
used to sent the data back to the main activity. Here, the longitudes and latitudes are used to set markers on the map. 

If no breweries are found in your current city, an integer is set increased with 1. On default, it is 0. When the integer is 1, a toast is shown that nothing is found.
Because the broadcast managers are in a loop, almost certain will the app return "no location found". So to prevent that the toast is shown multiple times, the toast
is only shown when the integer is 1. When the app finds more "no location found", the integer is set to 2, and then to 3, etc.

When a user clicks on a marker and clicks on the title, the id from this marker is sent to the info activity. Here, the app uses the last two async tasks. With the
info async task, the app gets most of the info about the brewery. With the image async task, the app gets the image url and the caption from the app. With broadcast
managers, all the data is sent back to the info activity. All async tasks use the httprequesthelper. Also, the app uses a singleton to easily get info from the database. 
Info activity checks if the user is signed in. When true, the save button is shown. When false, the login text is shown. With the save button, the user can save 
the rating about the brewery to the Firebase database. 
If user is signed in and he of she saved a brewery rating and goes to the user info page, a list with visited breweries is shown. When the user clicks on one, the id
of that brewery is sent back to the info activity and it starts again. On start, the info activity will search for an existing rating of the selected brewery in the 
database. If is has found it, the saved rating is shown. 


#### Searching
If the users wants, he or she can search for a city. The user can do that by clicking on the magnifying glass in the toolbar. The search activity will open.
Here the user can type in a city. The text is sent to the main activity and main activity does everything as normal, only not searching the users city, but instead
it uses the inserted city. Also, an integer is set to 0 for once. When the integer is 0, the app changes the camera to the first marker in the list with markers. 
It needs to do it only once because of the same reason why the toast is only shown once. 

#### User
When a user is not signed in and he or she clicks on the account button, the user is sent to the login page. The user can sign in or choose to make an account. 
Depending where the user came from, the user is sent back to a page. For instance, when the users signs in or registers from the info activity, the user is sent
back there. Same with the main activity. 
When the user is signed in and he or she clicks on the account button, the user is redirected to the user info page. When the user deletes a visited brewery, the
database is updated. 

## Challenges
At the first couple of days, I had a clear view of what I wanted to make. But then the first presentation messed it up. I got the tip to start with the map and the
users location. This took me a while to get it working properly. 
Furthermore, I wrote in my design document that I would use a SQLite database. I decided not to do that, and use Firebase instead. This gives the user the ability 
to use the app on multiple devices.
I also used two more plugins: Picasso, to show the images and Geocoder to convert coordinates to a city. 
The biggest challenge I had was the annoying API. Beermapping is a free API but I have to do 4 requests per brewery to get all the information I want. This took
longer than I hoped, but making an async task is easy for me now. 
Also, git was annoying me. It made a repo in a repo. I don't know why. I still don't fully understand git, but Renske helped me a lot. I now know more than I did before.
One of the most tough challenges were the bugs. I used a lot of code from the Google Maps API tutorial, but that was not sufficient for my app. I had to add a lot of code
in order to make it work. Google Maps is not that hard to use, when you read the documentation well. That took a while. 
I also got a lot quicker with debugging. I made at least 250 logs during development. This helped me a lot. 

## Decisions
I had a great idea of making a search bar in the toolbar, but that took to much time. That's why it is a different activity now. I think this is uglier, but I 
dind't have enough time to figure out how to do it in the toolbar. That would make the app more attractive, I think.
The decision to start the app with the map with the users current location was a great decision. The app is now much cleaner and is easier to use. I think it is
easier to understand for a new user how to use the app for the first time with this decision.
I have used Firebase instead of SQLite, which again makes the app more usable. The user is now encouraged to use the app on more devices or to share an account.