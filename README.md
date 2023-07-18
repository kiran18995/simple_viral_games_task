# Dog Image Generator App

This repository contains the code for a simple mobile app that generates random images of dogs and allows users to view and save them for later.

## Screens
The app consists of three screens:

### Home Screen
The home screen is the initial screen of the app and provides buttons to navigate to other screens.

![Screenshot_1689697794](https://github.com/kiran18995/simple_viral_games_task/assets/48232762/4fc9ffc3-24ad-401e-8370-decfb699bc1d)

### Generate Dogs Screen
This screen allows users to generate random images of dogs. It has a "Generate!" button that sends a request to a public dog images API (https://dog.ceo/api/breeds/image/random). Upon hitting the "Generate!" button and receiving valid image data, the image is displayed on the screen and stored in an LRU cache to hold the 20 most recent image data generated from API requests. The cache persists across app sessions, ensuring that recently generated dog images are available for viewing later.

![Screenshot_1689697805](https://github.com/kiran18995/simple_viral_games_task/assets/48232762/e5bf3e87-b102-47c9-9c41-d318c0bc0122)


### My Recently Generated Dogs Screen
This screen displays a scrollable gallery of dog images that are retrieved from the LRU cache. Users can view the images they have generated recently. Additionally, there is a "Clear Dogs" button that clears out the cache and the gallery, removing all stored images.

![Screenshot_1689697812](https://github.com/kiran18995/simple_viral_games_task/assets/48232762/2e203f43-1aa6-44a9-8260-f60861536848)


#### Installation and Usage
To run the app locally, follow these steps:

Clone the repository: git clone <repository-url>
Install the necessary dependencies: <command-to-install-dependencies>
Run the app on your preferred mobile device or simulator: <command-to-run-app>
Technologies Used
The app is built using the following technologies:

Programming Language: Kotlin
Libraries: Retrofit, Glide
