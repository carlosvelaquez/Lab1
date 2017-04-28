package carlosvelasquez_lab1;

import java.util.Scanner;

public class CarlosVelasquez_Lab1 {
    static int contFilas, turno;
    static Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Laboratorio 1 - Carlos Velasquez");
        System.out.println("-   -   -   -   -   -   -   -   -   -");
        //HNEFATAFL
        
        System.out.println("¡Bienvenido a HNEFATAFL!");
        char[][] tablero = generarTablero();
        
        turno = 1;
        String equipo;
        char[] tipoPieza = new char[2], piezaEnemigos = new char[2];
        
        do {
            if (turno == 2) {
                tipoPieza[0] = '*';
                tipoPieza[1] = '*';
                piezaEnemigos[0] = '-';
                piezaEnemigos[1] = '-';
                equipo = "Moscovitas";
            }else{
                tipoPieza[0] = '-';
                tipoPieza[1] = '+';
                piezaEnemigos[0] = '*';
                piezaEnemigos[1] = '*';
                equipo = "Suecos";
            }
            System.out.println("-   -   -   -   -   -   -   -   -   -   -");
            mostrarTablero(tablero);
            System.out.println("");
            System.out.println("¡Es turno de los " + equipo + "!");
            //int[] inmunidad = checkInmunidad(tablero, tipoPieza, piezaEnemigos);
            do{
                System.out.println("");
                System.out.print("Ingrese la coordenada X de la pieza: ");
                int posX = entrada.nextInt();
                System.out.print("Ingrese la coordenada Y de la pieza: ");
                int posY = entrada.nextInt();
                if (posX < 0 || posX > 10 || posY < 0 || posY > 10) {
                        System.out.println("[ERROR] Las coordenadas superan los limites del tablero.");
                }else{

                    if (tablero[posX][posY] == tipoPieza[0] || (turno == 1 && tablero[posX][posY] == '+')) {
                        System.out.print("\nIngrese la coordenada X nueva: ");
                        int newX = entrada.nextInt();
                        System.out.print("Ingrese la coordenada Y nueva: ");
                        int newY = entrada.nextInt();

                        if (newX < 0 || newX > 10 || newY < 0 || newY > 10) {
                            System.out.println("[ERROR] Las coordenadas superan los limites del tablero.");
                        }else if (posX != newX && posY != newY){
                            System.out.println("[ERROR] Solo se puede mover horizontal o verticalmente.");
                        }else if (posX == newX && posY == newY){
                            System.out.println("[ERROR] Las coordenadas antiguas y las nuevas son las mismas.");
                        }else{
                            if (checkMovimiento(posX, posY, newX, newY, tablero) == true) {
                                tablero = moverPieza(posX, posY, newX, newY, tablero);

                                if (turno == 1)
                                    turno = 2;
                                else
                                    turno = 1;

                                tablero = checkSucesos(newX, newY, tablero, tipoPieza, piezaEnemigos);
                                break;
                            }else{
                                System.out.println("[ERROR] Movimiento invalido, hay una pieza en el camino.");
                            }
                        }

                    }else if (tablero[posX][posY] == ' '){
                        System.out.println("[ERROR] Ese espacio esta vacio.");
                    }else{
                        System.out.println("[ERROR] Esa pieza no es de las suyas.");
                    }
                }
                
            }while(true);
            
        } while (true);
        
        
    }
    
    static char[][] generarTablero(){
        char[][] generado = new char[11][11];
        String[] piezasNegras = {"0,4", "0,5", "0,6", "1,4", "1,6", "2,5", "4,0", "4,1", "4,9", "4,10", "5,0", "5,2", "5,8", "5,10", "6,0", "6,1", "6,9", "6,10", "8,5", "9,4", "9,6", "10,4", "10,5", "10,6"};
        String[] piezasBlancas = {"3,5", "4,4", "4,5", "4,6", "5,3", "5,4", "5,5", "5,6", "5,7", "6,4", "6,5", "6,6", "7,5"};
        
        for (int col = 0; col < generado.length; col++) {
            for (int fil = 0; fil < generado[0].length; fil++) {
                generado[col][fil] = ' ';
            }
        }
        
        for (int llenarN = 0; llenarN < piezasNegras.length; llenarN++) {
            String[] pos = piezasNegras[llenarN].split(",");
            
            int x = Integer.parseInt(pos[0]);
            int y = Integer.parseInt(pos[1]);
            
            generado[x][y] = '*';
        }
        
        for (int llenarB = 0; llenarB < piezasBlancas.length; llenarB++) {
            String[] pos = piezasBlancas[llenarB].split(",");
            
            int x = Integer.parseInt(pos[0]);
            int y = Integer.parseInt(pos[1]);
            
            generado[x][y] = '-';
        }
        
        generado[5][5] = '+';
        
        return generado;
    }
    
    static void imprimir(char[][] matriz){
        
        if (matriz.length == 1) {
            System.out.print(contFilas);
            for (int i = 0; i < matriz[0].length; i++) {
                System.out.print(" | " + matriz[0][i]);
            }
            System.out.println(" |");
        } else {
            System.out.print(contFilas + " ");
            for (int i = 0; i < matriz[0].length; i++) {
                System.out.print(" | " + matriz[0][i]);
            }
            
            char[][] matrizNueva = new char[matriz.length-1][matriz[0].length];
            
            for (int i = 1; i < matriz.length; i++) {
                matrizNueva[i-1] = matriz[i].clone();
            }
            System.out.println(" | ");
            
            contFilas++;
            imprimir(matrizNueva);
        }
    }
    
    static void mostrarTablero(char[][] tablero){
        System.out.println("     0   1   2   3   4   5   6   7   8   9   10");
        contFilas = 0;
        imprimir(tablero);
    }
    
    static boolean checkMovimiento(int x, int y, int nuevoX, int nuevoY, char[][] tablero){
        int menorX, mayorX, menorY, mayorY, cont = 0;
        
        if (x > nuevoX) {
            menorX = nuevoX;
            mayorX = x;
        }else{
            menorX = x;
            mayorX = nuevoX;
        }
        
        if (y > nuevoY) {
            menorY = nuevoY;
            mayorY = y;
        }else{
            menorY = y;
            mayorY = nuevoY;
        }
        
        for (int i = menorY; i <= mayorY; i++) {
            for (int j = menorX; j <= mayorX; j++) {
                if (tablero[j][i] != ' ') {
                    cont ++;
                }
                if (cont > 1) {
                    return false;
                }
            }
        }
        return true;
    }
    
    static char[][] moverPieza(int x, int y, int nuevoX, int nuevoY, char[][] tablero){
        char pieza = tablero[x][y];
        tablero[nuevoX][nuevoY] = pieza;
        tablero[x][y] = ' ';
        return tablero;
    }
    
    static char[][] checkSucesos(int ultimoX, int ultimoY, char[][] tablero, char[] piezaProp, char[] piezaEnem){
        int contComidas = 0, contInm = 0;
        
        /*for (int filas = 0; filas < tablero.length; filas++) {
            for (int columnas = 0; columnas < tablero[0].length; columnas++) {
                if (tablero[filas][columnas] == piezaEnem) {
                    try{
                        if ((tablero[filas-1][columnas] == piezaProp && tablero[filas+1][columnas]== piezaProp)
                                || (tablero[filas][columnas-1]== piezaProp && tablero[filas][columnas+1]== piezaProp)) {
                            
                                tablero[filas][columnas] = ' ';
                                contComidas++;
                            
                        }
                    }catch(ArrayIndexOutOfBoundsException ex){}
                }
            }
        }*/
        try{
            for (int filas = ultimoX - 1; filas <= ultimoX + 1; filas++) {
                for (int columnas = ultimoY - 1; columnas <= ultimoY + 1; columnas++) {
                    if (tablero[filas][columnas] == '+' && piezaProp[0] == '*') {
                        if ((tablero[filas-1][columnas] == piezaProp[0] && tablero[filas+1][columnas]== piezaProp[1])
                                && (tablero[filas][columnas-1]== piezaProp[0] && tablero[filas][columnas+1]== piezaProp[1])) {

                                tablero[filas][columnas] = ' ';
                                contComidas++;

                        }
                    }
                    if (tablero[filas][columnas] == piezaEnem[0] || tablero[filas][columnas] == piezaEnem[1]) {
                        if ((tablero[filas-1][columnas] == piezaProp[0] && tablero[filas+1][columnas]== piezaProp[0])
                                || (tablero[filas][columnas-1]== piezaProp[0] && tablero[filas][columnas+1]== piezaProp[0])) {

                                tablero[filas][columnas] = ' ';
                                contComidas++;

                        }
                        
                        else if ((tablero[filas-1][columnas] == piezaProp[1] && tablero[filas+1][columnas]== piezaProp[1])
                                || (tablero[filas][columnas-1]== piezaProp[1] && tablero[filas][columnas+1]== piezaProp[1])) {

                                tablero[filas][columnas] = ' ';
                                contComidas++;

                        }
                        
                        else if ((tablero[filas-1][columnas] == piezaProp[1] && tablero[filas+1][columnas]== piezaProp[0])
                                || (tablero[filas][columnas-1]== piezaProp[1] && tablero[filas][columnas+1]== piezaProp[0])) {

                                tablero[filas][columnas] = ' ';
                                contComidas++;

                        }
                        
                        else if ((tablero[filas-1][columnas] == piezaProp[0] && tablero[filas+1][columnas]== piezaProp[1])
                                || (tablero[filas][columnas-1]== piezaProp[0] && tablero[filas][columnas+1]== piezaProp[1])) {

                                tablero[filas][columnas] = ' ';
                                contComidas++;

                        }
                        
                    }
                }
            }
        }catch(ArrayIndexOutOfBoundsException ex){}
            
        if (contComidas > 0){
            System.out.println("-   -   -   -   -   -   -   -   -   -");
            System.out.println("UNA PIEZA HA SIDO COMIDA.");
        }
        
        checkGane(tablero);
        
        return tablero;
    }
    
    static void checkGane(char[][] tablero){
        boolean hayRey = false, hayBlancas = false, hayNegras = false, reyGana = false;
        
        for (int filas = 0; filas < tablero.length; filas++) {
            for (int columnas = 0; columnas < tablero[0].length; columnas++) {
                if (tablero[filas][columnas] == '+') {
                    hayRey = true;
                }
                
                if (tablero[filas][columnas] == '+' && ((filas == 0 || filas == 10) || (columnas == 0 || columnas == 10))) {
                    reyGana = true;
                }
                
                if (tablero[filas][columnas] == '-') {
                    hayBlancas = true;
                }
                
                if (tablero[filas][columnas] == '*') {
                    hayNegras = true;
                }
            }
        }
        
        if (hayRey == false || reyGana == true || hayBlancas == false || hayNegras == false) {
            System.out.println("-   -   -   -   -   -   -   -   -   -");
            System.out.println("JUEGO TERMINADO");
            mostrarTablero(tablero);
            
            if (hayRey == false) {
                System.out.println("\n¡El Rey ha sido derrotado!\n¡Ganan los Moscovitas!");
            }
            
            if (reyGana == true) {
                System.out.println("\n"
                        + "¡El Rey ha escapado!\n¡Ganan los Suecos!");
            }
            
            if (hayBlancas == false) {
                System.out.println("\n¡Todos los Suecos han sido derrotados!\n¡Ganan los Moscovitas!");
            }
            
            if (hayNegras == false) {
                System.out.println("\n¡Todos los Moscovitas han sido derrotados!\n¡Ganan los Suecos!");
            }
            
            System.exit(0);
        }
        
    }
}
