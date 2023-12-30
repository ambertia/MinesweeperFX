# MinesweeperFX
#### Video Demo: https://youtu.be/0EoiR5q24qo
#### Description:
Personal project to create a functional implementation of the game Minesweeper using JavaFX libraries. Started out in July 2023, refactored, redesigned, and finished in December 2023 as a project for CS50x 2023.

#### Repository
If you're reading this from my submission rather than the repository, I have a repository at https://github.com/ambertia/MinesweeperFX

#### Program Structure
There are several classes that abstract away various functions of the game for convenience and design simplicity.

1. GameCell represents a single tile on the Minesweeper board; an atom of the game structure. GameCells independently keep track of gamestate information about themselves such as whether or not they are a mine, whether or not they are flagged, etc. They also maintain the visual content of the cell, updating icons and text appropriately. Furthermore, they do accept mouse input, but the cell only uses the input to update itself; more complex game-wide functionality is handled at a higher level.
2. GameContainer represents, in essence, a single Minesweeper "game". New games are made by instantiating GameContainers. GameContainer extends the JavaFX GridPane class so that it can be used as a JavaFX Node, and so that the constructor and methods can use GridPane methods and modify the pane internally. This class contains an ArrayList of GameCells, and arranges them in a rectangle according to the provided GameSpecification. The GameContainer handles higher level game interactivity, including the "waterfall" reveal of open areas with empty cells and the right-click "area reveal" around a cell if there are enough flags present. There is also functionality to ensure that the cell a player clicks first will always be empty!
3. GameDefaults is a small class with some convenience functions and public static fields pertaining to game difficulty and styling.
4. GameSpecification represents the "difficulty" of the game as an object, and new GameSpecifications can be created and modified to enable difficulty to be changed between the three presets and the Custom difficulty mode. It also has some public final fields to enable some aspects of the game, such as board size and the number of mines, to be easily accessible without having to call a function.
5. Main is just a wrapper that references the MinesweeperGame main() method; this class is the entry point in the Maven configuration. This was my first project to use Maven, and through some digging on the internet I found that Maven can be fickle about calling JavaFX applications directly.
6. MinesweeperGame is the actual overarching application, which expanded greatly towards the end of the project. MinesweeperGame arranges all of the JavaFX Scenes of the game and all of the UI elements that aren't related to the game board itself: buttons, menus, popups, the toolbar, and so on. It is the last place where mouse input is checked, and verifies whether a game is won or lost depending on certain conditions.
7. ZoomableScrollPane is an extension of the JavaFX ScrollPane, created by a Daniel Hári in response to a StackOverflow question (linked at the top of ZoomableScrollPane.java). I found the thread while searching about how I might create a game board that can be zoomed and dragged around. I made a few modifications to allow the zoom levels to be clamped within a certain range.

## Folder Structure

- `assets`: Auxiliary storage of assets e.g. vector icons.
- `docs`: Documentation (needs to be updated) - To-do list, drawio charts / PDFs.
- `src`: Source files and asset files used at runtime.

## Requirements

This project was built using OpenJDK 17 and OpenJFX 17, and as such will require a JRE of comparable version to run.

## Installation

I may release portable JARs in the future. Beyond that, the software can be built by downloading the repository and running `mvn install` in the root of the repository, which will generated a shaded JAR in `target`.

Alternatively, the program can be run directly by running `mvn javafx:run`.
