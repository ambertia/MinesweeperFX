## MinesweeperFX

Personal project with the goal of creating a functional implementation of the game Minesweeper in a portable JAR using OpenJFX.

## Folder Structure

- `assets`: Auxiliary storage of assets e.g. vector icons
- `docs`: Documentation (needs to be updated) - To-do list, drawio charts / PDFs.
- `src`: Source files and asset files used at runtime 

## Requirements

This project was built using OpenJDK 17 and OpenJFX 17, and as such will require a JRE of comparable version to run.

## Installation

I plan to release portable JARs in the future. Beyond that, the software can be built by downloading the repository and running `mvn install` in the root of the repository, which will generated a shaded JAR in `target`.

Alternatively, the program can be run directly by running `mvn javafx:run`.
