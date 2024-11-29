# "Poke Random Battle Game" documentation (By Álvaro Perera Rodríguez)

# Table of Contents
1. [About the App](#about)
2. [Installing and Executing](#installing)
3. [Project Requirements](#requirements)

## About the App<a name="about"></a>
Poke Random Battle is a Pokemon fan game created in Android Studio as a project for Android Development. In the game you can create a profile, then you select from a 9 possible pokemon randomly listed, which one you want to battle with. The combat take place and depending on the result your profile will get a victory or a defeat and will display that in the profile selection.

### Here lies the resources used in the project:
| Resource | Usage |
| ----------- | ----------- |
| Kotlin & Android Studio | Developing the app, data and functionalities |
| [SQLite](https://www.sqlite.org/index.html) | Storing data in a local DB  |
| [Retrofit](https://square.github.io/retrofit/) | Library to perform API Calls |
| [Picasso](https://square.github.io/picasso/) | Library to retrieve a picture from an url |
| [PokeAPI](https://pokeapi.co/) | API to get Pokemon info |
| [GitHub](https://github.com/AlvProdz) | To store this repository|

## Installing and Executing<a name="installing"></a>

1. Clone this repository using the console, a git client or Android Studio: 
> git clone https://github.com/alvaroperera/PokeRandomBattleGame.git
2. Use Android Studio to open the project
3. Execute the gradle sync (if needed)
4. Press play to try the app in some Android Emmulator in your machine.

## Project Requirements<a name="requirements"></a>
This is how i implemented the several requirements inside the project and how they work:

| Requirement                                                                                          | Implementation |
|------------------------------------------------------------------------------------------------------| ----------- |
| At least 3 activities                                                                                | Project has 3 different activities: <ul><li>MainMenu to select player profile</li> <li>SelectTeamMembersActivity to select the Pokemon you want to play with</li><li>CombatActivity where the combat take place and the results are given </li>After that you will be taken back to the Main Menu</ul>
| At least 1 extra parameter to be sent to another activity                                            | Multiple extra parameters are being sent to other activities such as <ul><li>PARAM_PLAYER_ID to keep the info about the player</li> <li>PARAM_POKEMON_ID to track the info of the pokemon you are playing with</li></ul>|
| At least 1 dialogue as a reaction of an user action                                                  | Multiple dialogues are being shown as a reaction such as: <ul><li>When user is typing his name creating a new game</li><li>After selecting a Pokemon as your combat option to confirm the choice</li></ul>|
| At least one value saved in SharedPreferences                                                        | I decided to keep this out of the project as i am already implementing a DB and API calls |
| At least one Data Base Table to store relevant data of the app                                       | I am using SQLite to store the Player information in the table "Player" so i can retrieve if the player has been initialized, the number of victories and defeats|
| App must be able to perform API REST calls                                                           | The app perform API Calls to the PokeAPI to get the Pokemon info needed for combat|
| Use of a RecyclerView to list elements and capture at least one onClick event with a lambda function | The SelectTeamMembersActivity has a RecyclerView to show the possible combat options and tracks the onClick event in all the items to get ready to combat after the confirmation of the dialogue|


| Extra PointsRequirement | Implementation |
| ----------- | ----------- |
| Internationalization (At least one language) | Using the Strings resources in English  |
| Use of viewBinding | I am using the viewBinding feature in every Activity |

