package it.polimi.ingsw.client.viewUtilities;

import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.internal.CLibrary;

import java.util.*;

import static org.fusesource.jansi.internal.CLibrary.ioctl;
import static org.fusesource.jansi.internal.Kernel32.*;

/**
 * Contains all the Terminal Graphical Elements
 */
public enum AnsiGraphics
{
    ANSI_RESET ("\u001B[m"),
    ANSI_BRIGHT_WHITE("\u001B[97m"),
    ANSI_BRIGHT_RED("\u001B[91m"),
    ANSI_BRIGHT_GREEN("\u001B[92m"),
    ANSI_BRIGHT_YELLOW("\u001B[93m"),
    ANSI_BRIGHT_BLUE("\u001B[94m"),
    ANSI_BRIGHT_PURPLE("\u001B[95m"),
    ANSI_BRIGHT_CYAN("\u001B[96m"),
    ANSI_BRIGHT_GREY("\u001b[90m"),
    ANSI_BRIGHT_ORANGE("\u001b[38;5;208m"),
    STUDENT("\u25CF"),
    PROFESSOR("\u265B"),
    MOTHER_NATURE("\u2659"),
    TOWER("\u2656"),
    COIN("\u20A1"),
    NO_ENTRY_TILE("\u00F8"),

    DOUBLE_HORIZONTAL_LINE("\u2550"),
    DOUBLE_VERTICAL_LINE("\u2551"),
    DOUBLE_DOWNLEFT_ANGLE("\u255A"),
    DOUBLE_DOWNRIGHT_ANGLE("\u255D"),
    DOUBLE_UPLEFT_ANGLE("\u2554"),
    DOUBLE_UPRIGHT_ANGLE("\u2557"),
    SINGLE_HORIZONTAL_LINE("\u2500"),
    SINGLE_VERTICAL_LINE("\u2502"),
    SINGLE_DOWNLEFT_ANGLE("\u2570"),
    SINGLE_DOWNRIGHT_ANGLE("\u256F"),
    SINGLE_UPLEFT_ANGLE("\u256D"),
    SINGLE_UPRIGHT_ANGLE("\u256E"),
    DOUBLE_H_SINGLE_V_UP("\u2564"),
    DOUBLE_H_SINGLE_V_DOWN("\u2567"),
    SINGLE_V_SINGLE_H_LEFT("\u251C"),
    SINGLE_V_SINGLE_H_RIGHT("\u2524"),
    SINGLE_DIAGONAL_POSITIVE("\u2571"),
    SINGLE_DIAGONAL_NEGATIVE("\u2572"),

    CLEAR("\u001b[1J\u001b[;H"),
    INVERT("\u001b[7m"),
    RESET("\u001B[m");

    private String escape;
    private static int width = 0;
    private static int height = 0;
    private static int firstFreeLine;

    AnsiGraphics(String escape)
    {
        this.escape = escape;
    }

    /**
     * Get the Width and Height of the terminal,
     * store the information inside variables
     * @return if the screen is large enough to show the graphics
     */
    private static boolean setDimensions(){
        width = AnsiConsole.getTerminalWidth();

        if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win")){
            long console = GetStdHandle(STD_OUTPUT_HANDLE);

            CONSOLE_SCREEN_BUFFER_INFO info = new CONSOLE_SCREEN_BUFFER_INFO();
            GetConsoleScreenBufferInfo(console, info);
            height = info.windowHeight();
            if (height <= 0){
                console = GetStdHandle(STD_ERROR_HANDLE);
                info = new CONSOLE_SCREEN_BUFFER_INFO();
                GetConsoleScreenBufferInfo(console, info);
                height = info.windowHeight();
            }
        } else {
            CLibrary.WinSize sz = new CLibrary.WinSize();
            ioctl(1, CLibrary.TIOCGWINSZ, sz);
            height = sz.ws_row;
            if (height <= 0){
                sz = new CLibrary.WinSize();
                ioctl(2, CLibrary.TIOCGWINSZ, sz);
                height = sz.ws_row;
            }
        }
        return (width > 120);
    }

    /**
     * Create the title with AnsiCode
     * @return the String to print
     */
    public static String getTitle(){
        int rightShift = 1, downShift = 1;
        if (setDimensions())
            rightShift = width/2 - 32;
        if (height >= 18)
            downShift = height/3 - 5;

        System.out.print(setPosition(300, 800) + CLEAR +""+RESET);
        String center[] = new String[6];
        for (int i = 0; i < 5; i++)
            center[i] = "\u001b["+(downShift + i)+";"+rightShift+"H";
        StringBuilder title = new StringBuilder();
        title.append(center[0] + INVERT+"       "+RESET+"  "+INVERT+"      "+RESET+"   "+INVERT+"  "+RESET+"   "+INVERT+"     "+RESET+"   "+INVERT+"  "+RESET+"   "+INVERT+"  "+RESET+"  "+INVERT+"        "+RESET+"  "+INVERT+"  "+RESET+"   "+INVERT+"  "+RESET+"  "+INVERT+"       "+RESET+"\n");
        title.append(center[1] + INVERT+"  "+RESET+"       "+INVERT+"  "+RESET+"   "+INVERT+"  "+RESET+"  "+INVERT+"  "+RESET+"  "+INVERT+"  "+RESET+"   "+INVERT+"  "+RESET+"  "+INVERT+"   "+RESET+"  "+INVERT+"  "+RESET+"     "+INVERT+"  "+RESET+"     "+INVERT+"  "+RESET+"   "+INVERT+"  "+RESET+"  "+INVERT+"  "+RESET+"\n");
        title.append(center[2] + INVERT+"       "+RESET+"  "+INVERT+"       "+RESET+"  "+INVERT+"  "+RESET+"  "+INVERT+"       "+RESET+"  "+INVERT+"  "+RESET+" "+INVERT+" "+RESET+" "+INVERT+"  "+RESET+"     "+INVERT+"  "+RESET+"     "+INVERT+"       "+RESET+"  "+INVERT+"       "+RESET+"\n");
        title.append(center[3] + INVERT+"  "+RESET+"       "+INVERT+"  "+RESET+" "+INVERT+"  "+RESET+"    "+INVERT+"  "+RESET+"  "+INVERT+"  "+RESET+"   "+INVERT+"  "+RESET+"  "+INVERT+"  "+RESET+"  "+INVERT+"   "+RESET+"     "+INVERT+"  "+RESET+"          "+INVERT+"  "+RESET+"       "+INVERT+"  "+RESET+"\n");
        title.append(center[4] + INVERT+"       "+RESET+"  "+INVERT+"  "+RESET+"   "+INVERT+"  "+RESET+"  "+INVERT+"  "+RESET+"  "+INVERT+"  "+RESET+"   "+INVERT+"  "+RESET+"  "+INVERT+"  "+RESET+"   "+INVERT+"  "+RESET+"     "+INVERT+"  "+RESET+"     "+INVERT+"       "+RESET+"  "+INVERT+"       "+RESET+"\n");
        return title.toString();
    }

    /**
     * Create the menu graphic with AnsiCode.
     * @return the String to print
     */
    public static String createMenu(){
        StringBuilder menu = new StringBuilder();

        menu.append(horizontalBar(""));

        for (int i = 0; i < 14; i++)
            menu.append(innerRow("", false));

        //Separator Line
        menu.append(getLeftOffset()).append(INVERT).append(" ").append(RESET);
        for (int i = 0; i < 118; i++)
            menu.append("═");
        menu.append(INVERT).append(" ").append(RESET).append("\n");;

        menu.append(chooseLine());

        //Final bar
        menu.append(horizontalBar(""));
        menu.append(cursorReposition());

        return menu.toString();
    }

    /**
     * Set the title of the Menu {@link #createMenu()}
     * @param s the title to insert
     * @return the String to print
     */
    public static String setTitle(String s){
        StringBuilder title = new StringBuilder();
        title.append(RESET + "\u001b[").append(height / 3 + 1).append(";H");

        return  "" + title.append(horizontalBar(s));
    }

    /**
     * Give the text to insert in the Menu {@link #createMenu()}
     * The text could be red if it is an error and can replace all the text already present or continue below
     * @param s the text to insert in the menu
     * @param error if the text is an error message
     * @param cleanAll if the menu text has to be cleared
     * @return the String to print
     */
    public static String putText(String s, boolean error, boolean cleanAll){
        if (cleanAll){
            clean();
            firstFreeLine = height/3 + 2;
        }

        if (firstFreeLine == height/3 + 15)
            clean();

        return "" + innerRow(s, error) + cursorReposition();
    }

    /**
     * Method in charge of cleaning the Menu text box
     */
    public static void clean(){
        firstFreeLine = height/3 + 3;

        System.out.print("\u001b["+firstFreeLine+";H");
        for(int i = 0; i < 12; i++) {
            System.out.print(innerRow("", false));
        }
    }

    private static StringBuilder innerRow(String s, boolean error){
        StringBuilder row = new StringBuilder();

        if (!s.equals("")) {
            row.append("\u001b[").append(firstFreeLine).append(";H");
            firstFreeLine++;
        }

        row.append(getLeftOffset()).append(RESET).append(INVERT).append(" ").append(RESET);
        if (error)
            row.append(ANSI_BRIGHT_RED);
        row.append(s);
        for (int i = 0; i < 118 - s.length(); i++)
            row.append(" ");

        return row.append(RESET).append(INVERT).append(" ").append(RESET).append("\n");
    }

    private static StringBuilder cursorReposition(){
        StringBuilder cursor = new StringBuilder();
        StringBuilder position = new StringBuilder();
        position.append("\u001b[");
        if (height >=24)
            position.append(height / 3 + 17);
        else
            position.append(height - 1);

        cursor.append(position).append(";").append(width / 2 + 70).append("H\u001b[1K");

        return cursor.append(position).append(";H").append(chooseLine()).append(position).append(";").append(width / 2 - 50).append("H").append(ANSI_BRIGHT_GREEN);

    }

    private static StringBuilder horizontalBar(String s){
        StringBuilder horizontalBar = new StringBuilder();

        horizontalBar.append(INVERT).append(getLeftOffset()).append(" ").append(s);
        for (int i = 0; i < 119 - s.length(); i++)
            horizontalBar.append(" ");

        return horizontalBar.append(RESET).append("\n");
    }

    private static StringBuilder chooseLine(){
        StringBuilder line = new StringBuilder();
        line.append(getLeftOffset()).append(INVERT).append(" ").append(RESET);
        line.append(ANSI_BRIGHT_GREEN).append("INSERT:").append(RESET);
        line.append("\u001b["+111+"C").append(INVERT).append(" ").append(RESET).append("\n");
        return line;
    }

    private static String getLeftOffset(){
        if (width > 121)
            return "\u001b["+(width/2 - 60)+"C";

        return "" + RESET;
    }

    /**
     *
     * @param gameInfo
     * @return
     */

    public static String createGame(GameInfo gameInfo){
        int leftOffside = (width - 154)/2;
        StringBuilder game = new StringBuilder();
        game.append(RESET);
        for (Integer number : gameInfo.getAssistants())
            game.append(assistant(3+(number/2 + number%2 -1)*7, leftOffside+((number%2 + 1)%2)*7, number));

        if (gameInfo.isExpertMode())
            for (int i = 0; i < 3; i++)
                game.append(character(7+i*10, 18+leftOffside, gameInfo.getCharacterCost(i), gameInfo.getCharacterInfo(i), i+1));

        game.append(islands(gameInfo, 12, 37+leftOffside));

        for (int i = 0; i < gameInfo.getNumPlayer(); i++)
            game.append(cloud(gameInfo, 16, leftOffside+54+i*15+ (gameInfo.getNumPlayer()+1)%2*8,i));

        int index = 0;
        for (String player : gameInfo.getPlayersName()) {
            if (!player.equals(gameInfo.getFrontPlayer())) {
                if (gameInfo.isExpertMode())
                    game.append(setPosition(10 + index*15, 149+leftOffside)).append(COIN).append(": ").append(gameInfo.getNumCoin(player));
                game.append(setPosition(6+index*15, 124+leftOffside)).append(player.toUpperCase());
                game.append(assistant(12 + index*15, 148+leftOffside, gameInfo.getLastUsed(player)));
                game.append(board(gameInfo, 21, 9, 7 + index * 15, 124+leftOffside, false, player));
                index++;
            }
        }

        int hOffset = 0;
        if (height < 50){
            game.append(setPosition(38, 0));
            for (int i = 0; i < 12; i++)
                game.append("\n");

            hOffset = 50 - height;
        }


        for (String player : gameInfo.getPlayersName()) {
            if (player.equals(gameInfo.getFrontPlayer())) {
                if (gameInfo.isExpertMode())
                    game.append(setPosition(41-hOffset, 98 + leftOffside)).append(COIN).append(": ").append(gameInfo.getNumCoin(player));
                game.append(assistant(43-hOffset, 97 + leftOffside, gameInfo.getLastUsed(player)));
                game.append(board(gameInfo, 37, 9, 38-hOffset, 56 + leftOffside, true, gameInfo.getFrontPlayer()));
            }
        }



        String turn;
        turn = gameInfo.getCurrentPlayer();
        if (gameInfo.getFrontPlayer().equals(gameInfo.getCurrentPlayer()))
            turn = "YOUR";
        game.append(setPosition(42-hOffset, 12+leftOffside)).append(colored(0, "IT'S "+turn+" TURN!"));
        game.append(setPosition(44-hOffset, 11+leftOffside)).append(colored(0, "MOVABLE STUDENTS: "+ gameInfo.getRemainingMoves()));

        game.append(setPosition(39-hOffset, 124+leftOffside)).append(colored(4, "COMMAND INFO"));
        game.append(setPosition(41-hOffset, 124+leftOffside)).append(colored(4, "1: Assistant Selection"));
        game.append(setPosition(42-hOffset, 124+leftOffside)).append(colored(4, "2: Move student to dining"));
        game.append(setPosition(43-hOffset, 124+leftOffside)).append(colored(4, "3: Move student to island"));
        game.append(setPosition(44-hOffset, 124+leftOffside)).append(colored(4, "4: Move mother nature"));
        game.append(setPosition(45-hOffset, 124+leftOffside)).append(colored(4, "5: Character infos (Expert mode)"));
        game.append(setPosition(46-hOffset, 124+leftOffside)).append(colored(4, "6: Character selection (Expert mode)"));
        game.append(setPosition(47-hOffset, 124+leftOffside)).append(colored(4, "7: Cloud selection"));

        game.append(setPosition(49-hOffset,0));
        return game.toString();
    }

    private static StringBuilder islands(GameInfo gameInfo, int vPosition, int hPosition){
        int[][] islandPositions = {{0,0},{-6, 11},{-10, 24},{-10, 40},{-6, 53},{0, 64},{7, 64},{13, 53},{17, 40},{17, 24},{13,11},{7, 0}};
        int [][] diagPositions = {{+3, -10},{-3, -10},{-3, +10},{+3, +10}};
        int [][] vertPositions = {{-6,0},{+6, 0}};
        int [][] horPositions = {{0,-13},{0,+13}};
        List<int[]> before = new ArrayList<>();
        before.add(new int[]{0, 1, 3, 4, 6, 7, 9, 10});
        before.add(new int[]{5, 11});
        before.add(new int[]{2,8});
        List<int[]> after = new ArrayList<>();
        after.add(new int[]{1, 2, 4, 5, 7, 8, 10, 11});
        after.add(new int[]{0, 6});
        after.add(new int[]{3,9});
        int actualVert;
        int actualHoriz;

        StringBuilder islands = new StringBuilder();
        int index = 0;
        int color;
        for (int i = 0; i < 5; i++)
            if (gameInfo.getProfessors()[i] == 0)
                islands.append(setPosition(vPosition+2, hPosition+33+i*3)).append(colored(i, PROFESSOR.escape));
        for (int i = 0; i < gameInfo.getNumIsland(); i++){
            color = gameInfo.getTowersOnIsland(i);
            if (gameInfo.getIslandSize(i) == 1) {
                islands.append(islandStructure(vPosition + islandPositions[index][0], hPosition + islandPositions[index][1], i, color, true, gameInfo));
                actualVert = vPosition + islandPositions[index][0];
                actualHoriz = hPosition + islandPositions[index][1];
            }else {
                islands.append(islandStructure(vPosition + islandPositions[index + gameInfo.getIslandSize(i) / 2][0], hPosition + islandPositions[index + gameInfo.getIslandSize(i) / 2][1], i, color, true, gameInfo));
                actualVert = vPosition + islandPositions[index + gameInfo.getIslandSize(i) / 2][0];
                actualHoriz = hPosition + islandPositions[index + gameInfo.getIslandSize(i) / 2][1];
                for (int j = index + gameInfo.getIslandSize(i) / 2 - 1; j >= index; j--) {
                    switch (islandUnion(before, j)) {
                        case 0:
                            islands.append(islandStructure(actualVert + diagPositions[j / 3][0], actualHoriz + diagPositions[j / 3][1], i, color, false, gameInfo));
                            actualVert += diagPositions[j / 3][0];
                            actualHoriz += diagPositions[j / 3][1];
                            break;
                        case 1:
                            islands.append(islandStructure(actualVert +  vertPositions[(j - 1) / 6][0], actualHoriz + vertPositions[(j - 1) / 6][1], i, color, false, gameInfo));
                            actualVert += vertPositions[(j - 1) / 6][0];
                            actualHoriz += vertPositions[(j - 1) / 6][1];
                            break;
                        case 2:
                            islands.append(islandStructure(actualVert + horPositions[j / 6][0], actualHoriz + horPositions[j / 6][1], i, color, false, gameInfo));
                            actualVert += horPositions[j / 6][0];
                            actualHoriz += horPositions[j / 6][1];
                            break;
                    }
                }
                actualVert = vPosition + islandPositions[index + gameInfo.getIslandSize(i) / 2][0];
                actualHoriz = hPosition + islandPositions[index + gameInfo.getIslandSize(i) / 2][1];
                for (int j = index + gameInfo.getIslandSize(i) / 2 + 1; j < index + gameInfo.getIslandSize(i); j++) {
                    switch (islandUnion(after, j)) {
                        case 0:
                            islands.append(islandStructure(actualVert - diagPositions[j / 3][0], actualHoriz - diagPositions[j / 3][1], i, color, false, gameInfo));
                            actualVert -= diagPositions[j / 3][0];
                            actualHoriz -= diagPositions[j / 3][1];
                            break;
                        case 1:
                            islands.append(islandStructure(actualVert + vertPositions[j / 6][0], actualHoriz + vertPositions[j / 6][1], i, color, false, gameInfo));
                            actualVert += vertPositions[j/ 6][0];
                            actualHoriz += vertPositions[j / 6][1];
                            break;
                        case 2:
                            islands.append(islandStructure(actualVert - horPositions[j / 6][0], actualHoriz - horPositions[j / 6][1], i, color, false, gameInfo));
                            actualVert -= horPositions[j / 6][0];
                            actualHoriz -= horPositions[j / 6][1];
                            break;
                    }
                }
            }
            index += gameInfo.getIslandSize(i);
        }
        return islands;
    }

    private static int islandUnion(List<int[]> list, int island){
        for (int[] array : list)
            for (int i : array)
                if (i == island)
                    return list.indexOf(array);

        return -1;
    }

    private static StringBuilder character(int vPosition, int hPosition, int costo, Integer[] info, int position) {
        StringBuilder character = new StringBuilder();
        character.append(perimeter(vPosition, hPosition, 7, 4, "SINGLE"));
        character.append(setPosition(vPosition + 1, hPosition + 1)).append(COIN).append(costo);
        Integer[] student = distributed(info);
        Arrays.sort(student);
        if (info[5] != -1)
            character.append(setPosition(vPosition+2, hPosition+3)).append(info[5]).append(" ").append(NO_ENTRY_TILE);
        else if (student.length == 4)
            for (int i = 1; i <= 4; i++)
                character.append(setPosition(vPosition+2 + (i/2 + i%2 - 1), hPosition + 3 + (i%2 + 1)%2 * 2)).append(colored(student[i-1], STUDENT.escape));
        else if (student.length == 6)
            for (int i = 1; i <= 6; i++)
                character.append(setPosition(vPosition+2 + (i-1)/3, hPosition + 2 + (i%3 + 2)%3 * 2)).append(colored(student[i-1], STUDENT.escape));

        character.append(setPosition(vPosition+4, hPosition+4)).append(position);
        return character;
    }

    private static StringBuilder board(GameInfo gameInfo, int width, int height, int vPosition, int hPosition, boolean front, String player){
        StringBuilder board = new StringBuilder();
        board.append(boardStructure(vPosition, hPosition, width, height));
        Integer[] entranceStudents = distributed(gameInfo.getEntranceStudents(player));

        int vertical = vPosition + 1;
        int horizontal = hPosition + 2;
        for (Integer entranceStudent : entranceStudents) {
            board.append(setPosition(vertical, horizontal)).append(colored(entranceStudent, STUDENT.escape));
            horizontal += 2;
            vertical += 1;
            if (horizontal > hPosition + 4)
                horizontal = hPosition + 2;
        }

        vertical = vPosition + 1;
        horizontal = hPosition + 8;
        int index = 0;
        for (Integer diningStudent : gameInfo.getDiningStudents(player)){
            if (!front && diningStudent != 0)
                board.append(setPosition(vertical, horizontal)).append(diningStudent).append(" ").append(colored(index, STUDENT.escape));
            else {
                for (int i = 0; i < diningStudent; i++, horizontal += 2) {
                    board.append(setPosition(vertical, horizontal)).append(colored(index, STUDENT.escape));
                }
            }
            vertical += 2;
            index++;
            horizontal = hPosition + 8;
        }

        vertical = vPosition + 1;
        horizontal = hPosition + 14;
        if (front)
            horizontal += 16;
        index = 0;
        for (int professor : gameInfo.getProfessors(player)){
            if (professor != 0)
                board.append(setPosition(vertical, horizontal)).append(colored(index, PROFESSOR.escape));
            index++;
            vertical += 2;
        }

        vertical = vPosition + 1;
        horizontal = hPosition + 18;
        int reset = 0;
        if (front)
            horizontal += 16;
        int color = gameInfo.getPlayersTowerColor(player);
        for (int i = 0; i < gameInfo.getBoardTower(player); i++){
            board.append(setPosition(vertical, horizontal)).append(colored(color, TOWER.escape));

            horizontal += 2;
            reset++;
            if (reset > 1){
                reset = 0;
                horizontal = hPosition + 18;
                if (front)
                    horizontal += 16;

                vertical += 2;
            }
        }
        return board;
    }

    private static StringBuilder cloud(GameInfo gameInfo, int vPosition, int hPosition, int number){
        StringBuilder cloud = new StringBuilder();
        cloud.append(setPosition(vPosition, hPosition+4)).append(SINGLE_UPLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_UPRIGHT_ANGLE);
        cloud.append(setPosition(vPosition + 1, hPosition + 2)).append(SINGLE_UPLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_DOWNRIGHT_ANGLE).append("   ").append(SINGLE_DOWNLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_UPRIGHT_ANGLE);
        cloud.append(setPosition(vPosition + 2, hPosition)).append(SINGLE_UPLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_DOWNRIGHT_ANGLE).append("       ").append(SINGLE_DOWNLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_UPRIGHT_ANGLE);
        cloud.append(setPosition(vPosition+3, hPosition)).append(SINGLE_VERTICAL_LINE).append("           ").append(SINGLE_VERTICAL_LINE);
        cloud.append(setPosition(vPosition + 4, hPosition)).append(SINGLE_DOWNLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_UPRIGHT_ANGLE).append("       ").append(SINGLE_UPLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_DOWNRIGHT_ANGLE);
        cloud.append(setPosition(vPosition + 5, hPosition + 2)).append(SINGLE_DOWNLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_UPRIGHT_ANGLE).append("   ").append(SINGLE_UPLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_DOWNRIGHT_ANGLE);
        cloud.append(setPosition(vPosition+6, hPosition+4)).append(SINGLE_DOWNLEFT_ANGLE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_HORIZONTAL_LINE).append(SINGLE_DOWNRIGHT_ANGLE);

        Integer[] cloudStudents = distributed(gameInfo.getCloudStudents(number));
        cloud.append(setPosition(vPosition+5, hPosition+6)).append(number+1);
        if (cloudStudents.length == 3)
            for (int i = 0; i < 3; i++)
                cloud.append(setPosition(vPosition+3, hPosition+4+i*2)).append(colored(cloudStudents[i], STUDENT.escape));
        else if (cloudStudents.length == 4)
            for (int i = 0; i < 4; i++)
                cloud.append(setPosition(vPosition+2+i/2*2, hPosition+5+i%2*2)).append(colored(cloudStudents[i], STUDENT.escape));

        return cloud;
    }

    private static StringBuilder assistant(int vPosition, int hPosition, int number){
        StringBuilder assistant = new StringBuilder();
        if (number == -1){
            assistant.append(perimeter(vPosition, hPosition, 5, 4, "SINGLE"));
            return assistant;
        }

        assistant.append(perimeter(vPosition, hPosition, 5, 4, "SINGLE"));
        assistant.append(setPosition(vPosition+1, hPosition+1)).append("MOVES");
        assistant.append(setPosition(vPosition+2, hPosition+3)).append(number/2 + number%2);
        assistant.append(setPosition(vPosition+4, hPosition+3)).append(number);

        return assistant;
    }

    private static StringBuilder islandStructure(int vPosition, int hPosition, int number, int color, boolean stud, GameInfo gameInfo){
        StringBuilder structure = new StringBuilder();
        structure.append(setPosition(vPosition, hPosition+3)).append("_______");
        structure.append(setPosition(vPosition+1, hPosition+2)).append(SINGLE_DIAGONAL_POSITIVE).append(setPosition(vPosition+1, hPosition+10)).append(SINGLE_DIAGONAL_NEGATIVE);
        structure.append(setPosition(vPosition+2, hPosition+1)).append(SINGLE_DIAGONAL_POSITIVE).append(setPosition(vPosition+2, hPosition+11)).append(SINGLE_DIAGONAL_NEGATIVE);
        structure.append(setPosition(vPosition+3, hPosition)).append(SINGLE_DIAGONAL_POSITIVE).append(setPosition(vPosition+3, hPosition+12)).append(SINGLE_DIAGONAL_NEGATIVE);
        structure.append(setPosition(vPosition+4, hPosition)).append(SINGLE_DIAGONAL_NEGATIVE).append(setPosition(vPosition+4, hPosition+12)).append(SINGLE_DIAGONAL_POSITIVE);
        structure.append(setPosition(vPosition+5, hPosition+1)).append(SINGLE_DIAGONAL_NEGATIVE).append(setPosition(vPosition+5, hPosition+11)).append(SINGLE_DIAGONAL_POSITIVE);
        structure.append(setPosition(vPosition+6, hPosition+2)).append(SINGLE_DIAGONAL_NEGATIVE).append("_______").append(SINGLE_DIAGONAL_POSITIVE);
        structure.append(setPosition(vPosition+5, hPosition+3)).append(number+1);
        if (color!=-1)
            structure.append(setPosition(vPosition+5, hPosition+9)).append(colored(color ,TOWER.escape));
        int index = 0;
        if (stud) {
            if (number == gameInfo.getMotherNaturePosition())
                structure.append(setPosition(vPosition+5, hPosition+6)).append(colored(8, MOTHER_NATURE.escape));
            if (gameInfo.hasNoEntryTile(number))
                structure.append(setPosition(vPosition + 5, hPosition + 6)).append(NO_ENTRY_TILE);
            for (Integer student : gameInfo.getStudentsOnIsland(number)) {
                if (student != 0)
                    structure.append(setPosition(vPosition + 2 + index / 2 - index / 4, hPosition + 3 + index * 3 - index / 2 * 6 + index / 4 * 6)).append(student).append(colored(index, STUDENT.escape));
                index++;
            }
        }
        return structure;
    }
    private static StringBuilder perimeter(int vPosition, int hPosition, int length, int height, String graphicType){
        StringBuilder label = new StringBuilder();
        label.append(setPosition(vPosition, hPosition)).append(getAnsiCharacter(graphicType, "UPLEFT_ANGLE"));
        label.append(verticalLine(height, vPosition + 1, hPosition, getAnsiCharacter(graphicType, "vertical_line")));
        label.append(setPosition(vPosition + height + 1, hPosition)).append(getAnsiCharacter(graphicType, "DOWNLEFT_ANGLE"));
        label.append(horizontalLine(length, vPosition+ height + 1, hPosition+1, getAnsiCharacter(graphicType, "horizontal_line")));
        label.append(setPosition(vPosition+height+1, hPosition+length+1)).append(getAnsiCharacter(graphicType, "DOWNRIGHT_ANGLE"));
        label.append(verticalLine(height, vPosition+1, hPosition+length+1, getAnsiCharacter(graphicType, "vertical_line")));
        label.append(setPosition(vPosition, hPosition+length+1)).append(getAnsiCharacter(graphicType, "UPRIGHT_ANGLE"));
        label.append(horizontalLine(length, vPosition, hPosition+1, getAnsiCharacter(graphicType, "horizontal_line")));
        return label;
    }
    private static StringBuilder boardStructure(int vPosition, int hPosition, int length, int height){
        StringBuilder label = new StringBuilder();
        label.append(perimeter(vPosition, hPosition, length, height, "DOUBLE"));
        label.append(boardVertical(height, vPosition, hPosition+6));
        label.append(boardVertical(height, vPosition, hPosition+length-9));
        label.append(boardVertical(height, vPosition, hPosition+length-5));
        for (int i = 1; i < 5; i++)
            label.append(boardHorizontal(length-16, vPosition+i*2, hPosition+6));
        return label;
    }
    private static StringBuilder horizontalLine(int length, int vPosition, int hPosition, AnsiGraphics type){
        StringBuilder line = new StringBuilder();
        line.append(setPosition(vPosition, hPosition));
        for (int i = 0; i < length; i++){
            line.append(type);
        }
        return line;
    }
    private static StringBuilder verticalLine(int length, int vPosition, int hPosition, AnsiGraphics type){
        int vertical = vPosition;
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < length; i++, vertical++){
            line.append(setPosition(vertical, hPosition));
            line.append(type);
        }
        return line;
    }
    private static StringBuilder boardVertical(int length, int vPosition, int hPosition){
        StringBuilder line = new StringBuilder();
        int vertical = vPosition + 1;
        line.append(setPosition(vPosition, hPosition)).append(getAnsiCharacter("DOUBLE", "h_single_v_up"));
        for (int i = 0; i < length; i++, vertical++) {
            line.append(setPosition(vertical, hPosition));
            line.append(SINGLE_VERTICAL_LINE);
        }
        return line.append(setPosition(vertical, hPosition)).append(getAnsiCharacter("DOUBLE", "h_single_v_down"));
    }
    private static StringBuilder boardHorizontal(int length, int vPosition, int hPosition){
        StringBuilder line = new StringBuilder();
        int horizontal = hPosition + 1;
        line.append(setPosition(vPosition, hPosition)).append(getAnsiCharacter("SINGLE", "v_single_h_left"));
        for (int i = 0; i < length; i++, horizontal++) {
            line.append(setPosition(vPosition, horizontal));
            line.append(SINGLE_HORIZONTAL_LINE);
        }
        return line.append(setPosition(vPosition, horizontal)).append(getAnsiCharacter("SINGLE", "v_single_H_right"));
    }

    /**
     *
     * @param text
     * @return
     */
    public static String finalMessages(String text){
        return setPosition(23, width/2 - text.length()/2) + colored(1, text.toUpperCase()) + setPosition(24, width/2 - 23/2) + colored(1, "PRESS ENTER TO CONTINUE");
    }

    private static String setPosition(int v, int h){
        return "\u001b["+v+";"+h+"H";
    }

    private static AnsiGraphics getAnsiCharacter(String lineType, String type){
        for (AnsiGraphics a : AnsiGraphics.values()) {
            if (a.name().equalsIgnoreCase(lineType + "_" + type))
                return a;
        }
        return null;
    }

    private static StringBuilder colored(int color, String text){
        StringBuilder coloredText = new StringBuilder();
        AnsiGraphics selectedColor = null;
        switch (color) {
            case 0:
                selectedColor = ANSI_BRIGHT_GREEN;
                break;
            case 1:
                selectedColor = ANSI_BRIGHT_RED;
                break;
            case 2:
                selectedColor = ANSI_BRIGHT_YELLOW;
                break;
            case 3:
                selectedColor = ANSI_BRIGHT_PURPLE;
                break;
            case 4:
                selectedColor = ANSI_BRIGHT_CYAN;
                break;
            case 5:
                selectedColor = ANSI_BRIGHT_WHITE;
                break;
            case 6:
                selectedColor = ANSI_BRIGHT_BLUE;
                break;
            case 7:
                selectedColor = ANSI_BRIGHT_GREY;
                break;
            case 8:
                selectedColor = ANSI_BRIGHT_ORANGE;
                break;
        }
        return coloredText.append(selectedColor).append(text).append(ANSI_RESET);
    }

    private static Integer[] distributed(Integer[] original){
        List<Integer> distributed = new ArrayList<>();
        int index = 0;
        for (Integer num : original){
            for (int i = 0; i < num; i++)
                distributed.add(index);
            index++;
        }
        Integer[] modified = new Integer[distributed.size()];
        modified = distributed.toArray(modified);
        return modified;
    }

    @Override
    public String toString() {
        return escape;
    }

}