package com.example.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.entity.Direction;
import com.example.entity.Item;
import com.example.entity.Room;
import com.example.entity.RoomConnection;

/**
 * Représente une partie jouée par le joueur.
 */
public class Game
{
    /**
     * Le gestionnaire qui surveille les saisies dans la console
     */
    private Scanner scanner;
    /**
     * La liste de tous les lieux existant dans l'univers du jeu
     */
    private Room[] rooms;
    /**
     * La liste de toutes les directions existant dans l'univers du jeu
     */
    private Direction[] directions;
    /**
     * Le jeu est-il en cours d'exécution?
     */
    private boolean isRunning;
    /**
     * Le lieu dans lequel le joueur se trouve actuellement
     */
    private Room currentRoom;

    /**
     * Crée une nouvelle partie
     */
    public Game()
    {
        scanner = new Scanner(System.in);
    }

    /**
     * Initialise la partie en créant les objets de l'univers
     */
    public void setup()
    {
        // Définit que la partie est en cours d'exécution
        isRunning = true;

        // Crée les directions
        Direction east = new Direction("east");
        Direction south = new Direction("south");
        Direction west = new Direction("west");
        Direction north = new Direction("north");
        directions = new Direction[] { east, south, west, north };
        // Crée les lieux
        Room bedroom = new Room("bedroom", "This is where you usually sleep. It's quite small, but at least the bed is comfy.");
        Room bathroom = new Room("bathroom", "This is the bathroom. There's no windows in there, so it tends to get easily dank.");
        Room kitchen = new Room("kitchen", "This is the kitchen. It still smells of yesterday's dinner.");
        // Crée les passages entre les lieux
        new RoomConnection(bedroom, bathroom, east);
        new RoomConnection(bathroom, bedroom, west);
        // Crée les éléments interactifs
        new Item(bedroom, "bed");
        new Item(bedroom, "curtains");
        new Item(bathroom, "shower");
        new Item(bathroom, "toothbrush");
        new Item(kitchen, "cookie");
        new Item(kitchen, "faucet");

        // Crée une liste contenant tous les lieux
        this.rooms = new Room[] { bedroom, bathroom, kitchen };

        // Définit le lieu de départ du joueur
        currentRoom = bedroom;
    }

    /**
     * Décrit un cycle d'exécution de la partie
     */
    public void update()
    {
        System.out.println("");

        // Affiche la description du lieu
        System.out.println(String.format("You are in the %s.", currentRoom.getName()));
        System.out.println(currentRoom.getDescription());
        // Affiche la liste des éléments interactifs présents dans le lieu
        if (currentRoom.getItems().isEmpty()) {
            System.out.println("No available items.");
        } else {
            System.out.print("Available items: ");
            List<String> itemNames = new ArrayList<>();
            for (Item item : currentRoom.getItems()) {
                itemNames.add(item.getName());
            }
            System.out.println(String.join(", ", itemNames) + ".");
        }
        // Affiche la liste des directions disponibles à partir du lieu
        System.out.print("Available directions: ");
        List<String> directionsNames = new ArrayList<>();
        for (RoomConnection connection : currentRoom.getConnectionsFrom()) {
            directionsNames.add(connection.getDirection().getName());
        }
        System.out.println(String.join(", ", directionsNames) + ".");

        // Attend une saisie utilisateur
        String userInput = scanner.nextLine();

        // Cherche une direction correspondant à la saisie de l'utilisateur
        for (Direction direction : directions) {
            // Si la saisie utilisateur correspond au nom de la direction
            if (userInput.equals(direction.getName())) {
                // Demande au lieu actuel de trouver le lieu d'arrivée lorsqu'on emprunte cette direction
                Room room = currentRoom.getRoomInDirection(direction);
                // Si ce lieu n'existe pas
                if (room == null) {
                    System.out.println("You cannot go in that direction!");
                    return;
                }
                // Si ce lieu existe, change le lieu actuel pour le nouveau lieu
                currentRoom = room;
                return;
            }
        }

        // Si aucune direction ne correspond à la saisie utilisateur
        System.out.println("This direction doesn't exist!");
    }

    /**
     * Permet de savoir si la partie est en cours
     * @return
     */
    public boolean isRunning()
    {
        return isRunning;
    }

    /**
     * @return Le lieu dans lequel le joueur se trouve actuellement
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }
}
