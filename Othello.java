import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;



public class Othello {
 
  int x = 0;                    // X And Y Positions.
  int y = 0;

  private static final char NO_CHIP = ' ';      // 'b' for Black 

  private static final char BLACK_UP = 'b';     // 'r' for reds

  private static final char RED_UP = 'r';

  private static final int BOARD_SIZE = 8;      // Defining board_size. We can also increase the board

  char move = BLACK_UP;                         // First move is user one which is 'black - b'

  private JFrame frame=new JFrame("\t\t\tOthello by 19L-2745   ");            // Java Graphics

  private char[][] grid = new char[BOARD_SIZE][BOARD_SIZE];     //Grid size

  public Othello() {

    frame.setSize(529, 552);                  // Frame Size set 512x512
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        grid[i][j] = NO_CHIP;
      }
    }

    grid[3][3] = RED_UP;                        // Start_up pieces
    grid[3][4] = BLACK_UP;
    grid[4][3] = BLACK_UP;
    grid[4][4] = RED_UP;

  }

  //  The below function simply put the grid on frame.

  public JPanel putGridOnPanel(){
    
    JPanel panel = new JPanel() {
            
      @Override

      public void paint(Graphics g) {
         
          for (int y = 0; y < BOARD_SIZE; y++) {
              for (int x = 0; x < BOARD_SIZE; x++) {
                  g.setColor(Color.black.darker());
                  g.drawRect(x*64,y*64, 64, 64);  
                  if(grid[y][x]=='b') {
                    g.setColor(Color.black.darker());
                     g.fillOval(x*65, y*65, 60, 60);
                  } 
                  else if(grid[y][x]=='r'){
                    g.setColor(Color.red.darker());
                     g.fillOval(x*65, y*65, 60, 60);
                  } 
                  else{}     
              }
          }
      }
  };
  frame.add(panel);
//        frame.remove(panel);
  frame.setDefaultCloseOperation(3);
  frame.setVisible(true);
  return panel;
  }

  //  take_turn calls the direction method .. which has simply task of just turing the pieces
  //  Also it will keeps in mind all games rules.

  private void takeTurn(char turn, int row, int col, char[][] grid_2) {

    grid_2[row][col] = turn;

    direction(row, col, turn, 0, -1, grid_2);
    direction(row, col, turn, 0, 1, grid_2);

    direction(row, col, turn, 1, 0, grid_2);
    direction(row, col, turn, -1, 0, grid_2);

    direction(row, col, turn, 1, 1, grid_2);
    direction(row, col, turn, 1, -1, grid_2);
    direction(row, col, turn, -1, 1, grid_2);
    direction(row, col, turn, -1, -1, grid_2);

  }

  private void direction(int row, int column, char colour, int colDir, int rowDir, char[][] grid_2) {
    int currentRow = row + rowDir;
    int currentCol = column + colDir;
    if (currentRow == BOARD_SIZE || currentRow < 0 || currentCol == BOARD_SIZE || currentCol < 0) {
      return;
    }
    while (grid_2[currentRow][currentCol] == BLACK_UP || grid_2[currentRow][currentCol] == RED_UP) {
      if (grid_2[currentRow][currentCol] == colour) {
        while (!(row == currentRow && column == currentCol)) {
          grid_2[currentRow][currentCol] = colour;
          currentRow = currentRow - rowDir;
          currentCol = currentCol - colDir;
        }
        break;
      } else {
        currentRow = currentRow + rowDir;
        currentCol = currentCol + colDir;
      }
      if (currentRow < 0 || currentCol < 0 || currentRow == BOARD_SIZE || currentCol == BOARD_SIZE) {
        break;
      }
    }
  }

  // The function is valid_moves. it takes the board and return all the valid moves that a current player
  // moves, store in moves array. (helfull while in MinMax Algorithm)

  void valid_moves(char board[][], int moves[][], char player) {

    int rowdelta = 0;
    int coldelta = 0;
    int row = 0;
    int col = 0;
    int x = 0;
    int y = 0;

    char opponent = (player == 'b') ? 'r' : 'b';

    for (row = 0; row < BOARD_SIZE; row++)
      for (col = 0; col < BOARD_SIZE; col++)
        moves[row][col] = 0;

    for (row = 0; row < BOARD_SIZE; row++)
      for (col = 0; col < BOARD_SIZE; col++) {
        if (board[row][col] != ' ')
          continue;

        for (rowdelta = -1; rowdelta <= 1; rowdelta++)
          for (coldelta = -1; coldelta <= 1; coldelta++) {

            if (row + rowdelta < 0 || row + rowdelta >= BOARD_SIZE ||
                col + coldelta < 0 || col + coldelta >= BOARD_SIZE ||
                (rowdelta == 0 && coldelta == 0))
              continue;

            if (board[row + rowdelta][col + coldelta] == opponent) {

              x = row + rowdelta;
              y = col + coldelta;

              for (;;) {
                x += rowdelta;
                y += coldelta;

                if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= 8)
                  break;

                if (board[x][y] == ' ')
                  break;

                if (board[x][y] == player) {
                  moves[row][col] = 1;
                  break;
                }
              }
            }
          }
      }
  }

  // Display Grid on Console

  public void displayGrid(char[][] grid_3) {

    {

      System.out.println("     0   1   2   3   4   5   6   7");
      System.out.println("---------------------------------------");
      for (int i = 0; i < 8; i++) {
        System.out.print(" " + i + " | ");

        for (int j = 0; j < 8; j++) {
          System.out.print(grid_3[i][j] + " | ");
        }
        System.out.print("\n");

        System.out.println("---------------------------------------");
      }
      System.out.println("     0   1   2   3   4   5   6   7\n");
    }
  }

  // to check where all the space full

  private boolean gameOver() {

    boolean full = false;
    int countTot = 0;
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        if (grid[i][j] == BLACK_UP || grid[i][j] == RED_UP) {
          countTot++;
        }
      }
    }
    if (countTot == BOARD_SIZE*BOARD_SIZE) {
      full = true;
    }
    return full;
  }

  // Check which player finally wins (by counting the pices)

  private void endGame() {
    int countw = 0;
    int countb = 0;
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        if (grid[i][j] == BLACK_UP) {
          countb++;
        } else if (grid[i][j] == RED_UP) {
          countw++;
        }
      }
    }
    if (countb > countw) {
      System.out.println("Game over.. The winner is Black.");
    } else if (countw > countb) {
      System.out.println("Game over.. The winner is Black.");
    } else {
      System.out.println("It is a tie game. Gameover..");
    }

  }

  // this function will check wheater the player have any moves available.
  // if not then give it to another player

  boolean noTurnHave(int moves[][]) {

    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        if (moves[i][j] == 1) {
          return false;
        }
      }
    }
    return true;
  }

  // Copy the two grids.  (Helpfull in MinMax (Recussion)) 

  public void copy_grid_othello(char[][] grid, char[][] grid_copy) {

    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        grid_copy[i][j] = grid[i][j];
      }
    }

  }

  // Evaluation Function

  public int score_find(char[][] board) {
    int count_B = 0;
    int count_w = 0;
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        if (board[i][j] == BLACK_UP) {
          count_B++;
        } else if (board[i][j] == RED_UP) {
          count_w++;
        } else {
        }
      }
    }
    return count_w - count_B;
  }

  // MINMAX_Algorithm call starts from best move

  void bestMove(int moves[][]) {
                                      // AI to make its turn
    int bestScore = Integer.MIN_VALUE;
    int[] move = { 0, 0 };
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        // Is the spot available?
        if (moves[i][j] == 1) {

          char[][] grid_copy = new char[BOARD_SIZE][BOARD_SIZE];
          copy_grid_othello(grid, grid_copy);
          takeTurn(RED_UP, i, j, grid_copy);
          int score = minimax(grid_copy, 0, false);
          if (score > bestScore) {
            bestScore = score;
            move[0] = i;
            move[1] = j;
          }
        }
      }
    }

    takeTurn(RED_UP, move[0], move[1], grid);
  }

  int minimax(char[][] board, int depth, boolean isMaximizing) {
  
    if (depth == 2) {                     // Search until depth 2
      int x = score_find(board);
      return x;
    }

    if (isMaximizing) {
      int bestScore = Integer.MIN_VALUE;
      int[][] moves = new int[BOARD_SIZE][BOARD_SIZE];
      valid_moves(board, moves, RED_UP);

      for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
          // Is the spot available?
          if (moves[i][j] == 1) {

            char[][] board_2 = new char[BOARD_SIZE][BOARD_SIZE];
            copy_grid_othello(board, board_2);
            takeTurn(RED_UP, i, j, board_2);
            int score = minimax(board, depth + 1, false);
            if (score > bestScore) {
              bestScore = score;
            }
          }
        }
      }
      return bestScore;
    } else                // IF it minimizing.
     {
      int bestScore = Integer.MAX_VALUE;
      int[][] moves = new int[BOARD_SIZE][BOARD_SIZE];
      valid_moves(board, moves, BLACK_UP);

      for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
          // Is the spot available?
          if (moves[i][j] == 1) {

            char[][] board_2 = new char[BOARD_SIZE][BOARD_SIZE];
            copy_grid_othello(board, board_2);
            takeTurn(BLACK_UP, i, j, board_2);
            int score = minimax(board, depth + 1, true);
            if (bestScore > score) {
              bestScore = score;
            }
          }
        }
      }
      return bestScore;
    }
  }

  public static void main(String[] args) {

      //!game.gameOver()
      //game.endGame();
      Othello game = new Othello();
  
      int[][] move_chech1 = new int[BOARD_SIZE][BOARD_SIZE];
      int[][] move_chech2 = new int[BOARD_SIZE][BOARD_SIZE];
      game.move = BLACK_UP;
      game.displayGrid(game.grid);
      JPanel panel =game.putGridOnPanel();

      panel.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
         if(game.gameOver()==true){
            game.endGame();
            return;            
         }
         game.x=e.getY() / 64;
         game.y=e.getX() / 64;
        

         if (game.move == BLACK_UP) {

          System.out.println("Black turn");
          System.out.print('\n');         
  
          game.valid_moves(game.grid, move_chech1, game.move);
          if (game.noTurnHave(move_chech1) == false) {
            
            
            if (move_chech1[game.x][game.y] == 1) {
              System.out.println("Valid Move");
              game.takeTurn(game.move, game.x, game.y, game.grid);
              game.displayGrid(game.grid);
              game.putGridOnPanel();
              game.move = RED_UP;
            } else {
              System.out.println("Not Valid Move");
              game.move = BLACK_UP;
            }
            
          } else {
            game.move = RED_UP;
          }
  
        } 
        else{}

        if (game.move == RED_UP) {

          System.out.println("Red turn");
          game.valid_moves(game.grid, move_chech2, game.move);
          if (game.noTurnHave(move_chech2) == false) {
            game.bestMove(move_chech2);
            game.displayGrid(game.grid);
            game.putGridOnPanel();
            game.move = BLACK_UP;
  
          } else {
            game.move = BLACK_UP;         
          }
        }
        else{}
    }
  
      private void exit(int i) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
       
      }
  
      @Override
      public void mouseEntered(MouseEvent e) {}
  
      @Override
      public void mouseExited(MouseEvent e) {}
  
      @Override
      public void mousePressed(MouseEvent e) {}
  });

  }
}