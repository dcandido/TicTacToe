package dcandido.tictactoe;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;

import static java.lang.Math.min;


public class tictactoe extends Activity {

    final int GRID_WIDTH = 3;
    final int GRID_HEIGHT = 3;

    Button buttons[][] = new Button[GRID_WIDTH][GRID_HEIGHT];
    LinearLayout rows[] = new LinearLayout[GRID_HEIGHT];
    LinearLayout rowHolder;

    // have the green player go first and the red player go second
    boolean greenPlayerTurn = true;

    final int GREEN = Color.rgb(0,200,0);
    final int DKGREEN = Color.rgb(0,100,0);
    final int RED = Color.rgb(200,0,0);
    final int DKRED = Color.rgb(100,0,0);
    final int GRAY = Color.LTGRAY;

    // call this method to set up each button
    private void configureButton(Button button) {
        button.setOnClickListener(buttonListener);
        button.setBackgroundColor(GRAY);
    }

    // call this method to set up each row
    private void configureRow(LinearLayout row) {
        row.setOrientation(LinearLayout.HORIZONTAL);
    }

    final int ROW_HOLDER_ID = 1234;
    // call this method to set up the rowholder
    private void configureRowHolder(LinearLayout rowHolder) {
        rowHolder.setOrientation(LinearLayout.VERTICAL);
        rowHolder.setBackgroundColor(greenPlayerTurn ? DKGREEN : RED);
        rowHolder.setId(ROW_HOLDER_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate the array of buttons and set up each button
        for (int width = 0; width < GRID_WIDTH; width++) {
            for (int height = 0; height < GRID_HEIGHT; height++) {
                buttons[width][height] = new Button(this);
                configureButton(buttons[width][height]);
            }
        }

        // populate the array of rows and set up each row
        for (int height = 0; height < GRID_HEIGHT; height++) {
            rows[height] = new LinearLayout(this);
            configureRow(rows[height]);
        }

        // initialize and set up the rowholder
        LinearLayout rowHolder = new LinearLayout(this);
        configureRowHolder(rowHolder);

        // make the rowholder the screen
        LayoutParams gridLayoutParameters = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setContentView(rowHolder, gridLayoutParameters);

        // determine a margin size based on the screen resolution
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        int marginSize = (size.x + size.y) / 100; // this is one-fiftieth of the average of the height and width

        // put margins on the buttons
        LayoutParams buttonLayoutParameters = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
        buttonLayoutParameters.topMargin = marginSize;
        buttonLayoutParameters.bottomMargin = marginSize;
        buttonLayoutParameters.leftMargin = marginSize;
        buttonLayoutParameters.rightMargin = marginSize;

        // put the buttons in the rows
        for (int height = 0; height < GRID_HEIGHT; height++) {
            for (int width = 0; width < GRID_WIDTH; width++) {
                rows[height].addView(buttons[width][height], buttonLayoutParameters);
            }
        }

        // put the rows in the rowholder
        LayoutParams rowLayoutParameters = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
        for (int height = 0; height < GRID_HEIGHT; height++) {
            rowHolder.addView(rows[height], rowLayoutParameters);
        }
    }

    public int getButtonColor(Button button) {
        ColorDrawable buttonColor = (ColorDrawable) button.getBackground();
        return buttonColor.getColor();
    }

    // TODO put constants at the top
    final int GREEN_WIN = 2;
    final int RED_WIN = 1;
    final int DRAW = 0;
    final int NOT_OVER = -1;

    public int checkForWinner() {

        // there is a winner if a row belongs to one player
        int rowButtonsOwned = 0;
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int column = 0; column < GRID_WIDTH; column++) {
                if (getButtonColor(buttons[row][column]) == GREEN) {
                    rowButtonsOwned += 1;
                }
                else if (getButtonColor(buttons[row][column]) == RED) {
                    rowButtonsOwned -= 1;
                }
            }
            if (rowButtonsOwned == GRID_WIDTH) {
                return GREEN_WIN;
            }
            else if (rowButtonsOwned == -GRID_WIDTH) {
                return RED_WIN;
            }
            rowButtonsOwned = 0;
        }

        // there is a winner if a column belongs to one player
        int columnButtonsOwned = 0;
        for (int column = 0; column < GRID_WIDTH; column++) {
            for (int row = 0; row < GRID_HEIGHT; row++) {
                if (getButtonColor(buttons[row][column]) == GREEN) {
                    columnButtonsOwned += 1;
                }
                else if (getButtonColor(buttons[row][column]) == RED) {
                    columnButtonsOwned -= 1;
                }
            }
            if (columnButtonsOwned == GRID_WIDTH) {
                return GREEN_WIN;
            }
            else if (columnButtonsOwned == -GRID_WIDTH) {
                return RED_WIN;
            }
            columnButtonsOwned = 0;
        }

        // there is a winner if the main diagonal belongs to one player
        int diagonalButtonsOwned = 0;
        for (int position = 0; position < GRID_WIDTH; position++) {
            if (getButtonColor(buttons[position][position]) == GREEN) {
                diagonalButtonsOwned += 1;
            }
            else if (getButtonColor(buttons[position][position]) == RED) {
                diagonalButtonsOwned -= 1;
            }
        }
        if (diagonalButtonsOwned == GRID_WIDTH) {
            return GREEN_WIN;
        }
        else if (diagonalButtonsOwned == -GRID_WIDTH) {
            return RED_WIN;
        }

        // there is a winner if the opposite diagonal belongs to one player
        int crossDiagonalButtonsOwned = 0;
        for (int position = 0; position < GRID_WIDTH; position++) {
            if (getButtonColor(buttons[position][GRID_HEIGHT - position - 1]) == GREEN) {
                crossDiagonalButtonsOwned += 1;
            }
            else if (getButtonColor(buttons[position][GRID_HEIGHT - position - 1]) == RED) {
                crossDiagonalButtonsOwned -= 1;
            }
        }
        if (crossDiagonalButtonsOwned == GRID_WIDTH) {
            return GREEN_WIN;
        }
        else if (crossDiagonalButtonsOwned == -GRID_WIDTH) {
            return RED_WIN;
        }

        // at this point, there is no winner
        // so if all buttons are owned, it's a draw, otherwise, it's not over
        int totalButtonsOwned = 0;
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int column = 0; column < GRID_WIDTH; column++) {
                if (getButtonColor(buttons[row][column]) != GRAY) {
                    totalButtonsOwned += 1;
                }
            }
        }
        if (totalButtonsOwned == GRID_WIDTH * GRID_HEIGHT) {
            return DRAW;
        } else {
            return NOT_OVER;
        }
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View view) {
            Button button = (Button) view;
            if (getButtonColor(button) == GRAY) {

                button.setBackgroundColor(greenPlayerTurn ? GREEN : RED);

                // change the turn
                greenPlayerTurn = !greenPlayerTurn;

                //change the background color according to the turn
                findViewById(ROW_HOLDER_ID).setBackgroundColor(greenPlayerTurn ? DKGREEN : DKRED);

                // if the game is over, restart the game
                if (checkForWinner() != NOT_OVER) {
                    newGame();
                }
            }
        }
    };

    public void newGame() {
        // have the green player go first and the red player go second
        greenPlayerTurn = true;
        findViewById(ROW_HOLDER_ID).setBackgroundColor(greenPlayerTurn ? DKGREEN : DKRED);
        // reset the color of each button
        for (int width = 0; width < GRID_WIDTH; width++) {
            for (int height = 0; height < GRID_HEIGHT; height++) {
                buttons[width][height].setBackgroundColor(GRAY);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tictactoe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tictactoe, container, false);
            return rootView;
        }
    }
}
